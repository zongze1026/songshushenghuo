package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.ArithUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.enums.CommissionLevel;
import com.yitkeji.songshushenghuo.vo.enums.CommissionType;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.enums.Vip;
import com.yitkeji.songshushenghuo.vo.model.Commission;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

public class ShareService {
    private static Logger logger = Logger.getLogger(ShareService.class);

    private static CommissionService commissionService;
    private static UserService userService;


    private ShareService(){
        commissionService = SpringUtil.getBean(CommissionService.class);
        userService = SpringUtil.getBean(UserService.class);
    }
    private static final class ShareServiceHolder{
        private static final ShareService INSTANCE = new ShareService();
    }

    public static final ShareService getInstance(){
        return ShareServiceHolder.INSTANCE;
    }

    /**
     * 计算用户分润金额
     * @param target 受益用户
     * @param type 分润类型
     * @param level 分润层级
     * @param money 触发分润的金额
     * @return
     */
    public static Double computeUserCommissionMoney(User source, User target, CommissionType type, CommissionLevel level, Double money){
        if(null == money){
            return 0.0;
        }
        Vip targetVip = target.matchVip();
        Double rate = Vip.getVip(targetVip.getLevel()).getShare().getCash()[0];
        return rate > 1? rate: ArithUtil.mul(rate, money);
    }


    /**
     * 用户分发分润
     * @param source
     * @param target
     * @param type
     * @param level
     * @param money
     *
     */
    @Transactional(rollbackFor = Exception.class)
    private static void commission(User source, User target, User lastAgent, Order order, CommissionType type, CommissionLevel level, Double money){
        if(target == null){
            return ;
        }

        // 舍弃分以下金额
        int commissionMoney = computeUserCommissionMoney(lastAgent, target, type, level, money).intValue();

        if(commissionMoney > 0){
            // 新增分润记录
            Commission commission = new Commission();
            commission.setType(type.getCode());
            commission.setMoney(commissionMoney);
            commission.setLevel(level);
            commission.setOrderId(order.getOrderId());
            commission.setTargetUserId(target.getUserId());
            commission.setSourceUserId(source.getUserId());
            commissionService.add(commission);

            // 更新用户余额
            userService.upMoney(target.getUserId(), commissionMoney);
        }

        // 分润分发到上级代理
        if(null != target.getAgentId()){
            lastAgent = target;
            target = userService.findByPrimaryKey(target.getAgentId());
            if(target != null && (target.getVip() > lastAgent.getVip())){
                commission(source, target, lastAgent, order, type, level, money);
            }
        }
    }


    /**
     * 根据订单计算分润
     * 自动延伸到所有上级
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized void commission(Order order){
        // 只分润成功订单
        if(!order.matchOrderStatus().equals(OrderStatus.SUCCESS)){
            return ;
        }
        User user;
        if (null != order.getUserId() && null != order.getAgentId()){
            user = userService.findByPrimaryKey(order.getUserId());
            CommissionType commissionType;
            switch (order.matchOrderType()){
                case INCOME_CASH: commissionType = CommissionType.CASH; break;
                default:return;
            }
            User agent = userService.findByPrimaryKey(order.getAgentId());
            commission(user, agent, user, order,commissionType, CommissionLevel.LEVEL2, Double.valueOf(order.getMoney()));
        }
    }
}
