package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.enums.CommissionLevel;
import com.yitkeji.songshushenghuo.vo.enums.CommissionType;
import com.yitkeji.songshushenghuo.vo.model.Commission;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


@Service
public class CommissionService extends BaseService<Commission> {

    @Autowired
    private UserService userService;


    @Override
    public int add(List<Commission> list) {
        int rows = super.add(list);
        for(Commission commission: list){
            StatisticsService.freshTodayCache(commission);
        }
        return rows;
    }

    @Override
    public int add(Commission object) {
        int rows = super.add(object);
        StatisticsService.freshTodayCache(object);
        return rows;
    }

    public List<Map<String, Object>> countAndSumCommission(QueryBuilder queryBuilder) {
        if (queryBuilder == null) {
            queryBuilder = new QueryBuilder(Commission.class);
        }
        queryBuilder.addColumns("count(1) as count", "sum(money) as sum", "type");
        queryBuilder.setGroup("type");
        return super.findList(queryBuilder, Map.class);
    }

    /**
     * 分润数量金额总计（用户）
     *
     * @param userId
     * @return
     */
    public Map<String, Object> sumUserCommission(Long userId) {
        QueryBuilder queryBuilder = new QueryBuilder(Commission.class);
        queryBuilder.addColumns("sum(money) as sum");
        queryBuilder.setEQQueryCond("targetUserId", userId);
        return super.sum(queryBuilder);
    }

    /**
     * 今日分润数量金额总计（用户）
     *
     * @param userId
     * @return
     */
    public Map<String, Object> sumUserCommissionToday(Long userId) {
        QueryBuilder queryBuilder = new QueryBuilder(Commission.class);
        queryBuilder.addColumns("sum(money) as sum");
        queryBuilder.setEQQueryCond("targetUserId", userId);
        queryBuilder.setBetweenQueryCond("createTime", DateUtil.getTodayStartTime(), DateUtil.getTodayEndTime());
        return super.sum(queryBuilder);
    }

    /**
     * 新手任务分润奖励
     * @param user 用户信息
     * @param commissionMoney 分润金额
     */
    public void noviceTaskCommission(User user, Order order, CommissionType userCommissionType, CommissionType referCommissionType, int commissionMoney) {

        // 用户的分润奖励
        Commission userCommission = new Commission();
        userCommission.setType(userCommissionType.getCode());
        userCommission.setMoney(commissionMoney);
        userCommission.setLevel(CommissionLevel.LEVEL2);
        userCommission.setOrderId(order == null ? null : order.getOrderId());
        userCommission.setTargetUserId(user.getUserId());
        userCommission.setSourceUserId(user.getUserId());
        super.add(userCommission);
        userService.upMoney(user.getUserId(), commissionMoney);

        // 推荐人的分润奖励
        if(user.getReferenceId() != null){
            Commission referenceCommission = new Commission();
            referenceCommission.setType(referCommissionType.getCode());
            referenceCommission.setMoney(commissionMoney);
            referenceCommission.setLevel(CommissionLevel.LEVEL2);
            referenceCommission.setOrderId(order == null ? null : order.getOrderId());
            referenceCommission.setSourceUserId(user.getUserId());
            referenceCommission.setTargetUserId(user.getReferenceId());
            super.add(referenceCommission);
            userService.upMoney(user.getReferenceId(), commissionMoney);
        }
    }

}
