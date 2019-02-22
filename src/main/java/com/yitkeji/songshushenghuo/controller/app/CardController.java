package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.channel.BankAuthChannel;
import com.yitkeji.songshushenghuo.service.CardService;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.AuthStatus;
import com.yitkeji.songshushenghuo.vo.enums.CardStatus;
import com.yitkeji.songshushenghuo.vo.enums.CardType;
import com.yitkeji.songshushenghuo.vo.model.BankAuth;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.app.CardAddReq;
import com.yitkeji.songshushenghuo.vo.req.app.CardDelReq;
import com.yitkeji.songshushenghuo.vo.req.app.CardPageReq;
import com.yitkeji.songshushenghuo.vo.res.app.CardRes;
import com.yitkeji.songshushenghuo.vo.res.app.UserRes;
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
import javax.validation.Valid;
import java.util.*;

@Api(tags = {"银行卡接口"})
@RestController
@RequestMapping("/app")
public class CardController extends BaseAppController {

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;

    /**
     * 添加卡
     * @param req
     * @return
     */
    @ApiOperation(value = "添加卡")
    @RequestMapping(value = "/card/add", method = RequestMethod.POST)
    public Result<UserRes> add(@RequestBody @Valid CardAddReq req, HttpServletRequest request){
        User user = getCacheUser(request);

        // 卡号已存在
        Card card = cardService.findByCardNo(req.getCardNo());
        if(card != null){
            if(CardStatus.DELETED.getCode() != card.getStatus()){
                return Result.fail("该卡已存在");
            }
            BeanUtils.copyProperties(req, card);
        }else{
            card = ObjectUtil.convert(req, Card.class);
            card.setName(user.getRealName());
            card.setIdcard(user.getIdcard());
        }
        card.setStatus(CardStatus.NORMAL.getCode());
        card.setUserId(user.getUserId());
        card.setCreateTime(new Date());

        // 四要素认证
        Object result = BankAuthChannel.getInstance().cardVerify(card, user);
        if (result instanceof String){
            return Result.fail((String)result);
        }
        BankAuth bankAuth = (BankAuth)result;
        if(bankAuth.getType().getCode() != req.getType()){
            return Result.fail("银行卡类型不符");
        }
        if (req.getType() == CardType.CREDIT.getCode()){
            if (req.getCvv() == null){
                return Result.fail("cvv码不能为空");

            }
            if (req.getExpiryDate() == null){
                return Result.fail("有效期不能为空");
            }
        }

        if (CardType.DEBIT.getCode() == req.getType()){
           if (StringUtils.isBlank(bankAuth.matchBank().getCode()) || StringUtils.isBlank(bankAuth.getBankCode())){
               return Result.fail("不支持该银行");
           }
        }

        card.setCardBank(bankAuth.getBankName());
        card.setBankCode(bankAuth.matchBank().getCode());
        // 更新卡信息
        cardService.saveOrUpdate(card);

        // 首次添加卡片
        if(user.getAuthStatus() != AuthStatus.SUCCESS.getCode()){
            user.setAuthStatus(AuthStatus.SUCCESS.getCode());
            user.setDebitCardId(card.getCardId());
            userService.updateByPrimaryKey(user, "debitCardId","authStatus");
        }

        return Result.success(ObjectUtil.convert(user, UserRes.class));
    }

    /**
     * 获取银行卡列表
     * @param req
     * @return
     */
    @ApiOperation(value = "获取银行卡列表")
    @RequestMapping(value = "/card/list", method = RequestMethod.GET)
    public Result<Page<CardRes>> list(CardPageReq req, HttpServletRequest request){
        Page page = initPage(req);
        User user = getCacheUser(request);
        QueryBuilder queryBuilder = new QueryBuilder(Card.class);
        queryBuilder.setEQQueryCond("userId", user.getUserId());
        if(req.getType() != null){
            queryBuilder.setEQQueryCond("type", req.getType());
        }
        queryBuilder.setEQQueryCond("status", CardStatus.NORMAL.getCode());
        page = cardService.findListByPage(page, queryBuilder, CardRes.class);
        return Result.success(page);
    }

    /**
     * 删除信用卡
     * @param req
     * @return
     */
    @ApiOperation(value = "删除银行卡")
    @RequestMapping(value = "/card/remove", method = RequestMethod.POST)
    public Result delCard(@RequestBody CardDelReq req, HttpServletRequest request){
        User user = getCacheUser(request);

        if(req.getCardId() == user.getDebitCardId()){
            return Result.fail("不能删除默认储蓄卡");
        }

        Card card = cardService.findByPrimaryKey(req.getCardId());

        if(card == null || card.getStatus() == CardStatus.DELETED.getCode()){
            return Result.fail("该卡不存在");
        }
        card.setStatus(CardStatus.DELETED.getCode());
        card.setLastupdatetime(new Date());
        int modifyRows = cardService.updateByPrimaryKey(card, "status");
        return Result.auto(modifyRows > 0);
    }
}
