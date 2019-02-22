package com.yitkeji.songshushenghuo.controller.app;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.ChangjieNewBaseChannel;
import com.yitkeji.channel.NewsChannel;
import com.yitkeji.channel.YunketongBaseChannel;
import com.yitkeji.channel.inter.*;
import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.CardService;
import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.service.YunshanfuAreaService;
import com.yitkeji.songshushenghuo.util.*;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.switc.SwitcherCfg;
import com.yitkeji.songshushenghuo.vo.cfg.switc.TimeRange;
import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.YunshanfuArea;
import com.yitkeji.songshushenghuo.vo.req.BaseReq;
import com.yitkeji.songshushenghuo.vo.req.app.*;
import com.yitkeji.songshushenghuo.vo.res.app.NativePayRes;
import com.yitkeji.songshushenghuo.vo.res.app.OrderRes;
import com.yitkeji.songshushenghuo.vo.res.app.YunshanfuAreaRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = {"通道相关接口"})
@RestController
@RequestMapping("/app/channel")
public class ChannelController extends BaseAppController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CardService cardService;

    /**
     * 收款
     * @param req
     * @return
     */
    @ApiOperation(value = "收款")
    @RequestMapping(value = "/cash", method = RequestMethod.POST)
    public Result<OrderRes> cash(@RequestBody CashReq req, HttpServletRequest request) throws BaseException {
        User user = getCacheUser(request);
        TimeRange timeRange = SystemCfg.getInstance().getSwitcher().getCashTimeRange();
        if(!timeRange.checkDate(new Date())){
            return Result.fail("收款时间：" + timeRange.getStartTime() + "-" + timeRange.getEndTime());
        }
        ChannelParams channelParams = ObjectUtil.convert(req, ChannelParams.class);
        Card card = cardService.findByPrimaryKey(req.getScardId());
        Card debitCard = cardService.findByPrimaryKey(user.getDebitCardId());
        channelParams.setUser(user);
        channelParams.setCard(card);
        channelParams.setDebitCard(debitCard);
        channelParams.setIp(BaseReq.getRealIp(request));
        // 筛选通道

        ChannelRouter channelRouter = new ChannelRouter();
        channelRouter.filterUnion(card.matchBank().getUnion());
        channelRouter.filterBusinessType(BusinessType.CASH);
        List<CashChannel> cashChannels = channelRouter.getList();

        if(cashChannels.size() < 1){
            return Result.fail("暂不支持该银行");
        }

        CashChannel channel = cashChannels.get(0);

        if(req.getMoney() < 100000){
            List<CashChannel> smChannels = new ChannelRouter(cashChannels).filterSm().getList();
            if(smChannels.size() > 0){
                channel = smChannels.get(0);
            }
        }
        Object result;

        // 短验绑卡
        result = channel.cashFirst(channelParams);
        if(result instanceof String){
            return Result.fail((String)result);
        }
        if(SqlUtil.getPrimaryValue(result) == null){
            return Result.smsAuth(card.getPhone(), ObjectUtil.objectToMap(result));
        }

        // 开始支付
        // 畅捷通道支付不要短验，所以要手动补充一次验证码验证。
        if(channel instanceof ChangjieNewBaseChannel || channel instanceof YunketongBaseChannel){
            if(!StringUtils.isBlank(req.getRequestNo())){
                req.setRequestNo(null);
                req.setCode(null);
            }
            SmsChannel smsChannel = ChannelRouter.getChannel(BusinessType.SMS);
            if(StringUtils.isBlank(req.getCode())){
                if(smsChannel == null){
                    return Result.fail("发送短信失败");
                }
                smsChannel.sendCodeMsg(card.getPhone(), SmsType.CASH);
                return Result.smsAuth(card.getPhone(), null);
            }
            if(!SmsChannel.checkCode(card.getPhone(), SmsType.CASH, req.getCode())){
                return Result.fail("验证码不正确");
            }
        }

        result = channel.cash(channelParams);
        // 支付需要二次认证，返回的订单ID
        if(result instanceof Long){
            Map<String, Object> resMap = new HashMap<>();
            resMap.put("orderId", result);
            return Result.smsAuth(card.getPhone(), resMap);
        }
        OrderRes orderRes = ObjectUtil.convert(result, OrderRes.class);
        orderRes.setTargetCardBank(debitCard.matchBank().getBankName());
        orderRes.setTargetCardNo(debitCard.getCardNo());

        int withdrawRateMoney = Math.toIntExact(Vip.getVip(user.getVip()).getDefaultRate().getCash().getFixedAmount());
        orderRes.setArriveMoney(orderRes.getMoney() -  (orderRes.getRateMoney() + withdrawRateMoney));
        return Result.success(orderRes);
    }


    /**
     * 代付 TODO 优化该方法
     * @param withdrawReq
     * @return
     */
    @ApiOperation(value = "余额提现")
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public Result<OrderRes> withdraw(@RequestBody WithdrawReq withdrawReq, HttpServletRequest request){
        User user = getCacheUser(request);
        SwitcherCfg switcherCfg = SystemCfg.getInstance().getSwitcher();
        RateElement rate = SystemCfg.getInstance().getRate().getWithdraw();
        Double rateMoney = withdrawReq.getMoney() * rate.getRate() + rate.getFixedAmount();
        if(withdrawReq.getMoney() < rateMoney || user.getMoney() < withdrawReq.getMoney()){
            return Result.fail("余额不足");
        }

        Card debitCard = cardService.findByPrimaryKey(withdrawReq.getTcardId());
        if (withdrawReq.getMoney() > switcherCfg.getMaxWithdrawMoney()){
            return Result.fail("提现金额不能大于" + switcherCfg.getMaxWithdrawMoney() / 100 + "元");
        }

        if(withdrawReq.getMoney() < switcherCfg.getMinWithdrawMoney()){
            return Result.fail("提现金额不能少于" + switcherCfg.getMinWithdrawMoney() / 100 + "元");
        }

        SmsChannel smsChannel = ChannelRouter.getChannel(BusinessType.SMS);
        if(StringUtils.isBlank(withdrawReq.getCode())){
            if(smsChannel == null){
                return Result.fail("发送短信失败");
            }
            smsChannel.sendCodeMsg(debitCard.getPhone(), SmsType.WITHDRAW);
            return Result.smsAuth(debitCard.getPhone(), ObjectUtil.objectToMap(withdrawReq));
        }

        if(!SmsChannel.checkCode(debitCard.getPhone(), SmsType.WITHDRAW, withdrawReq.getCode())){
            return Result.fail("验证码不正确");
        }
        userService.downMoney(user.getUserId(), withdrawReq.getMoney());
        List<WithdrawChannel> channels = new ChannelRouter().filterBusinessType(BusinessType.WITHDRAW).getList();
        if(channels.size() < 1){
            return Result.fail("暂无可用的代付通道");
        }

        Order order = channels.get(0).withPayForCustomer(user, debitCard, withdrawReq.getMoney());
        OrderRes orderRes = new OrderRes();
        if(order != null){
            BeanUtils.copyProperties(order, orderRes);
        }

        Card card = cardService.findByPrimaryKey(order.getTargetCardId());
        orderRes.setTargetCardBank(card.matchBank().getBankName());
        orderRes.setTargetCardNo(card.getCardNo());
        return Result.success(orderRes);
    }


    @ApiOperation(value = "升级VIP")
    @RequestMapping(value = "/upvip", method = RequestMethod.POST)
    public Result upvip(@RequestBody UpvipReq req, HttpServletRequest request){
        User user = getCacheUser(request);
        Vip vip = Vip.valueOf("VIP" + req.getVip());
        if(user.getVip() >= req.getVip()){
            return Result.fail("你已经是" + vip.getName() + "啦");
        }
        if(req.getChannel() == null){
            req.setChannel(Channel.WECHAT);
        }
        PayChannel channel = new ChannelRouter().getChannel(req.getChannel());
        Order order = orderService.createUpvipOrder(channel.getChannel(), user, vip);
        Object result = channel.upvip(order, request.getRemoteAddr());
        if(result instanceof String){
            return Result.fail((String)result);
        }
        return Result.success(result);
    }

    @ApiOperation(value = "获取新闻资讯")
    @RequestMapping(value = "/news", method = RequestMethod.POST)
    public Result news(){
        Object result = NewsChannel.getHeadlines();
        if(result instanceof String){
            return Result.fail((String) result);
        }
        return Result.success((JSONObject) result);
    }

    @ApiOperation("支付")
    @RequestMapping(value = "/pay", method = RequestMethod.POST)
    public Result<NativePayRes> pay(@RequestBody PayReq req, HttpServletRequest request){
        User user = getCacheUser(request);
        PayChannel channel = new ChannelRouter().getChannel(req.getChannel());
        Order order = null;
        Object result = null;
        NativePayRes nativePayRes = new NativePayRes();

        // 升级VIP
        if(req.getBusinessType().equals(BusinessType.UPVIP)){
            Vip vip = Vip.valueOf("VIP" + req.getVip());
            order = orderService.createUpvipOrder(channel.getChannel(), user, vip);
            result = channel.upvip(order, request.getRemoteAddr());
        }
        if(result instanceof String){
            return Result.fail((String)result);
        }
        nativePayRes.setOrder(ObjectUtil.convert(order, OrderRes.class));
        nativePayRes.setSignData(result);
        return Result.success(nativePayRes);
    }



    @ApiOperation(value = "通道列表")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public Result<List<Channel>> list(@RequestBody ChannelListReq channelListReq){
        List<Channel> clist = new ArrayList<>();

        List<? extends ChannelInterface> list = new ChannelRouter().filterBusinessType(channelListReq.getBusinessType()).getList();
        for(ChannelInterface channel: list){
            clist.add(channel.getChannel());
        }
        return Result.success(clist);
    }

    @ApiOperation(value = "获取云闪付通道地区列表")
    @RequestMapping(value = "/yunshanfu/areas", method = RequestMethod.POST)
    public Result<Collection<YunshanfuAreaRes>> yunshanfuAreaList(){
        YunshanfuAreaService yunshanfuAreaService = SpringUtil.getBean(YunshanfuAreaService.class);
        List<YunshanfuAreaRes> list = yunshanfuAreaService.findList(new QueryBuilder(YunshanfuArea.class), YunshanfuAreaRes.class);
        Map<Integer, YunshanfuAreaRes> map = new HashMap<>();
        for(YunshanfuAreaRes province: list){
            if(province.getLevel() == YunshanfuAreaLevel.LEVEL2.ordinal()){
                map.put(province.getAreaCode(), province);
            }
        }

        for(YunshanfuAreaRes res: list){
            if(res.getLevel() == YunshanfuAreaLevel.LEVEL3.ordinal()){
                YunshanfuAreaRes province = map.get(res.getProvinceCode());
                province.getCities().add(res);
            }
        }
        return Result.success(map.values());
    }



}
