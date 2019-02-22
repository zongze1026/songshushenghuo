package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.mapper.OrderMapper;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.Commission;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.res.app.OrderRes;
import com.yitkeji.songshushenghuo.vo.res.app.SumCashRes;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Service
public class OrderService extends BaseService<Order> {

    private Logger logger = Logger.getLogger(OrderService.class);

    @Autowired
    private UserService userService;

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private OrderMapper orderMapper;


    public Order createBaseOrder(Channel channel, User user, Card sourceCard, Card targetCard, Integer money, OrderType orderType) {
        Order order = new Order();
        order.setUserId(user.getUserId());
        order.setAgentId(user.getAgentId());
        if (null != sourceCard) {
            order.setSourceCardId(sourceCard.getCardId());
        }
        if (null != targetCard) {
            order.setTargetCardId(targetCard.getCardId());
        }
        if (null != channel) {
            order.setChannel(channel.name());
        }
        order.setType(orderType.getCode());
        order.setDesc(orderType.getDesc());
        order.setCreateTime(new Date());
        order.setMoney(money);
        order.setOrderNo(SystemCfg.getInstance().getAppinfo().getPack().substring(0, 1).toUpperCase() + System.currentTimeMillis() + new Double(Math.random() * 1000 + 1000).intValue());
        order.setStatus(OrderStatus.CREATED.getCode());
        return order;
    }

    public Order createCashOrder(Channel channel, User user, Card card, Card debitCard, Integer money) {
        Order order = this.createBaseOrder(channel, user, card, debitCard, money, OrderType.INCOME_CASH);
        super.add(order);
        return order;
    }

    public Order createWithdrawOrder(Channel channel, User user, Card debitCard, Integer money) {
        Order order = createBaseOrder(channel, user, null, debitCard, money, OrderType.INCOME_WITHDRAW);
        super.add(order);
        return order;
    }

    public Order createUpvipOrder(Channel channel, User user, Vip vip) {
        Vip userVip = Vip.valueOf("VIP" + user.getVip());
        int price = vip.getPrice().intValue() - userVip.getPrice().intValue();
        return createBaseOrder(channel, user, null, null, price, OrderType.SPENDING_UPVIP);
    }

    /**
     * 将一笔订单置为失败状态
     *
     * @param order
     * @param comment
     * @return
     */
    public Order failOrder(Order order, String comment) {
        if ("操作成功".equals(comment)) {
            comment = "交易失败";
        }
        order.setComment(comment);
        order.setStatus(OrderStatus.FAILED.getCode());
        if (order.matchOrderType().equals(OrderType.INCOME_WITHDRAW)) {
            userService.upMoney(order.getUserId(), order.getMoney());
        }
        super.updateByPrimaryKey(order, "status", "comment", "trackingNo", "withdrawTrackingNo");
        return order;
    }

    /**
     * 将一笔订单置为成功状态
     */
    public Order successOrder(Order order) {
        User user = userService.findByPrimaryKey(order.getUserId());
        order.setStatus(OrderStatus.SUCCESS.getCode());
        order.setComment(null);

        switch (order.matchOrderType()){
            // 升级VIP
            case SPENDING_UPVIP:
                Double realVipPrice = order.getMoney() + Vip.valueOf("VIP" + user.getVip()).getPrice();
                for(Vip vip: Vip.values()){
                    if(realVipPrice.equals(vip.getPrice())){
                        userService.upVip(user, vip, UpVipType.BUY, order.getOrderId());
                        break;
                    }
                }break;

            // 新手收款奖励
            case INCOME_CASH:
                Map<String, Object> map1 = sumOrder(order.getUserId(), OrderType.INCOME_CASH);
                System.out.println("map1 :" + map1);
                if(map1 != null && map1.get("sum") != null){
                    int orderMoney = ((BigDecimal) map1.get("sum")).intValue();
                    int fullRewardMoney = SystemCfg.getInstance().getSwitcher().getOrderFullRewardMoney();
                    int noviceCashMoney = SystemCfg.getInstance().getSwitcher().getNoviceAuthAndCashMoney();
                    if ((orderMoney + order.getMoney()) >= fullRewardMoney && orderMoney < fullRewardMoney){
                        commissionService.noviceTaskCommission(user, order, CommissionType.NOVICE_CASH, CommissionType.REFERRAL_CASH, noviceCashMoney);
                    }
                }break;default:
        }

        // 普通用户累计交易满30万免费升级成VIP
        userService.orderFullUpVipMoney(order, user);

        // TODO 考虑分布式
        try{
            ShareService.getInstance().commission(order);
        }catch (Exception e){
            e.printStackTrace();
            order.setComment("分润异常");
        }

        super.updateByPrimaryKey(order, "status", "comment", "trackingNo", "withdrawTrackingNo", "rateMoney", "arriveMoney");
        StatisticsService.freshTodayCache(order);
        return order;
    }

    /**
     * 将一笔订单置为过期状态
     * @param order
     * @return
     */
    public Order expireOrder(Order order) {
        order.setStatus(OrderStatus.CANCEL.getCode());
        order.setComment("订单超时取消");
        super.updateByPrimaryKey(order, "status", "comment");
        return order;
    }

    public Map<String, Object> sumOrder(Long userId, OrderType type) {
        QueryBuilder queryBuilder = new QueryBuilder(Order.class);
        queryBuilder.addColumns("sum(money) as sum");
        queryBuilder.setEQQueryCond("userId", userId);
        if (type != null){
            queryBuilder.setEQQueryCond("type", type.getCode());
        }
        queryBuilder.setEQQueryCond("status", OrderStatus.SUCCESS.getCode());
        return this.sum(queryBuilder);
    }

    public List<Map<String, Object>> countAndSumOrder(QueryBuilder queryBuilder) {
        if (null == queryBuilder) {
            queryBuilder = new QueryBuilder(Order.class);
        }
        queryBuilder.addColumns("count(1) as count", "sum(money) as sum", "type");
        queryBuilder.setEQQueryCond("status", OrderStatus.SUCCESS.getCode());
        queryBuilder.setGroup("type");
        return this.findList(queryBuilder, Map.class);
    }

    public OrderRes findOrderInfo(Long orderId) {
        return orderMapper.findOrderInfo(orderId);
    }

}
