package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.service.CardService;
import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.service.UserWithdrawService;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import com.yitkeji.songshushenghuo.vo.enums.UserWithdrawStatus;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.UserWithdraw;
import com.yitkeji.songshushenghuo.vo.req.admin.UpdateWithdrawReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = {"用户提现管理"})
@RestController
public class AdminUserWithdrawController extends BaseAdminController {

    @Autowired
    private UserWithdrawService userWithdrawService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CardService cardService;

    @Autowired
    private UserService userService;


    /**
     * 修改提现
     */
    @Transactional(rollbackFor = Exception.class)
    @ApiOperation(value = "修改提现")
    @RequestMapping(value = "/userWithdraw/update", method = RequestMethod.POST)
    public Result updateWithdraw(@RequestBody UpdateWithdrawReq updateWithdrawReq, HttpServletRequest request){
        UserWithdraw userWithdraw = new UserWithdraw();
        BeanUtils.copyProperties(updateWithdrawReq, userWithdraw);
        userWithdraw.setLastupdatetime(new Date());
        int count = userWithdrawService.updateByPrimaryKey(userWithdraw,"status","comment");

        QueryBuilder queryUser = new QueryBuilder(User.class);
        queryUser.setEQQueryCond("status", UserStatus.NORMAL);
        queryUser.setEQQueryCond("userId", updateWithdrawReq.getUserId());
        User user = userService.findOne(queryUser);

        if (count > 0){
            if (updateWithdrawReq.getStatus() == UserWithdrawStatus.WITHDRAW_SUCCESS.getCode()){
                Card debitCard = cardService.findByPrimaryKey(user.getDebitCardId());
                RateElement rate = SystemCfg.getInstance().getRate().getWithdraw();
                Order order = orderService.createWithdrawOrder(null, user, debitCard, updateWithdrawReq.getMoney());
                Double rateMoney = Math.ceil(updateWithdrawReq.getMoney() * rate.getRate()) + rate.getFixedAmount();
                order.setRateMoney(rateMoney.intValue());
                order.setArriveMoney(order.getMoney() - order.getRateMoney());
                order.setTrackingNo(System.currentTimeMillis() + "");
                order.setStatus(OrderStatus.SUCCESS.getCode());
                orderService.updateByPrimaryKey(order);
                userWithdraw.setOrderId(order.getOrderId());
                userWithdrawService.updateByPrimaryKey(userWithdraw,"orderId");
            }
            else if (updateWithdrawReq.getStatus() == UserWithdrawStatus.CHECK_NO_PASS.getCode()){
                userService.upMoney(user.getUserId(), userWithdraw.getMoney());
            }

        }
        return count > 0 ? Result.success() : Result.fail("修改失败");
    }
}
