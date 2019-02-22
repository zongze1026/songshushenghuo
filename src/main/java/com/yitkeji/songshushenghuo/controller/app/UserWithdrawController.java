package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.songshushenghuo.service.UserWithdrawService;
import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.UserWithdrawStatus;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.UserWithdraw;
import com.yitkeji.songshushenghuo.vo.req.app.UpdateWithdrawReq;
import com.yitkeji.songshushenghuo.vo.req.app.UserWithdrawPagReq;
import com.yitkeji.songshushenghuo.vo.req.app.WithdrawInfReq;
import com.yitkeji.songshushenghuo.vo.res.app.UserWithdrawRes;
import com.yitkeji.songshushenghuo.vo.res.app.UserWithdrawStatusRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Api(tags = {"用户申请提现接口"})
@RestController
public class UserWithdrawController extends BaseAppController {

    @Autowired
    private UserWithdrawService userWithdrawService;

    /**
     * 申请提现记录
     */
    @ApiOperation(value = "获取申请提现记录")
    @RequestMapping(value = "/withdraw/list", method = RequestMethod.GET)
    public Result<Page<UserWithdrawRes>> withdrawList(UserWithdrawPagReq req, HttpServletRequest request){
        Page page = initPage(req);
        User user = getCacheUser(request);
        QueryBuilder queryUserWithdraw = new QueryBuilder(UserWithdraw.class);

        if (req.getStatus() != null && req.getStatus() != UserWithdrawStatus.ALL.getCode()){
            queryUserWithdraw.setInQueryCond("UserWithdraw.status", req.getStatus());
        }
        queryUserWithdraw.addColumns("*","Card.*","UserWithdraw.createTime as withdrawCreateTime","UserWithdraw.money as withdrawMoney");
        queryUserWithdraw.setEQQueryCond("User.userId", "UserWithdraw.userId");
        queryUserWithdraw.setEQQueryCond("Card.cardId", "UserWithdraw.cardId");
        queryUserWithdraw.setEQQueryCond("UserWithdraw.userId", user.getUserId());
        queryUserWithdraw.setEQQueryCond("UserWithdraw.cardId", user.getDebitCardId());
        queryUserWithdraw.addOrder(queryUserWithdraw.getPrimaryKey(), false);
        page = userWithdrawService.findListByPage(page, queryUserWithdraw, UserWithdrawRes.class);

        if (page.getData() != null && page.getData().size() > 0){
            List<UserWithdrawRes> UserWithdrawList = page.getData();
            for (UserWithdrawRes userWithdrawPagRes : UserWithdrawList){
                userWithdrawPagRes.setStatusDesc(UserWithdrawStatus.getStatus(userWithdrawPagRes.getStatus()).getDesc());
                userWithdrawPagRes.setStrCreateTime(DateUtil.format(userWithdrawPagRes.getWithdrawCreateTime(),DateUtil.DATEFORMAT_NINTEEN));
            }
            page.setData(UserWithdrawList);
        }
        return Result.success(page);
    }

    /**
     * 申请提现详情
     */
    @ApiOperation(value = "获取申请提现详情")
    @RequestMapping(value = "/withdraw/info", method = RequestMethod.GET)
    public Result<UserWithdrawRes> withdrawInfo(WithdrawInfReq withdrawInfReq){
        UserWithdrawRes userWithdrawRes = userWithdrawService.findUserWithdrawInfo(withdrawInfReq.getUserWithdrawId());
        userWithdrawRes.setStatusDesc(UserWithdrawStatus.getStatus(userWithdrawRes.getStatus()).getDesc());
        userWithdrawRes.setStrCreateTime(DateUtil.format(userWithdrawRes.getWithdrawCreateTime(),DateUtil.DATEFORMAT_NINTEEN));
        return Result.success(userWithdrawRes);
    }

    /**
     * 修改提现
     */
    @ApiOperation(value = "修改提现")
    @RequestMapping(value = "/withdraw/update", method = RequestMethod.POST)
    public Result updateWithdraw(@RequestBody UpdateWithdrawReq updateWithdrawReq){
        UserWithdraw userWithdraw = new UserWithdraw();
        int count = 0;
        if (updateWithdrawReq.getMoney() > SystemCfg.getInstance().getSwitcher().getMaxWithdrawMoney()){
            BeanUtils.copyProperties(updateWithdrawReq, userWithdraw);
            userWithdraw.setLastupdatetime(new Date());
            count = userWithdrawService.updateByPrimaryKey(userWithdraw,"money","lastupdatetime");
        }else {
            Result.fail("申请提现金额必须大于" + (SystemCfg.getInstance().getSwitcher().getMaxWithdrawMoney() / 100) + "元");
        }
        return count > 0 ? Result.success() : Result.fail("修改失败");
    }

    /**
     * 申请提现状态
     */
    @ApiOperation(value = "获取申请提现状态")
    @RequestMapping(value = "/withdraw/status", method = RequestMethod.GET)
    public Result<List<UserWithdrawStatusRes>> withdrawStatus(){
        List<UserWithdrawStatusRes> userWithdrawStatusResList = UserWithdrawStatus.getUserWithdrawStatus();
        return Result.success(userWithdrawStatusResList);
    }
}
