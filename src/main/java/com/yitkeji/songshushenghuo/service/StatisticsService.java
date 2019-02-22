package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@Service
public class StatisticsService extends BaseService<Statistics> {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommissionService commissionService;

    public boolean hasStatistics(Long userId, Calendar calendar){
        QueryBuilder queryBuilder = new QueryBuilder(Statistics.class);
        queryBuilder.setEQQueryCond("createTime", DateUtil.getDayStartTime(calendar.getTime()));
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        }
        return count(queryBuilder) > 0;
    }

    /**
     * 统计某日的数据
     * @return
     */
    public Statistics statisticsByUserIdAndDate(Long userId, Date date){

        Date dayStartTime = DateUtil.getDayStartTime(date);
        Date dayEndTime = DateUtil.getDayEndTime(date);
        Statistics statistics = findByCreateTime(userId, date);
        if(statistics == null){
            statistics = new Statistics();
            statistics.setUserId(userId);
            statistics.setCreateTime(dayStartTime);
        }

        // 用户增量统计
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        if(userId != null){
            queryBuilder.setEQQueryCond("agentId", userId);
        }
        int ucount = userService.count(queryBuilder);
        statistics.setUserCount(ucount);

        queryBuilder = new QueryBuilder(User.class);
        if(userId != null){
            queryBuilder.setEQQueryCond("agentId", userId);
        }
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        queryBuilder.setEQQueryCond("vip", Vip.VIP1.getLevel());
        statistics.setUserUpvip1Count(userService.count(queryBuilder));

        queryBuilder = new QueryBuilder(User.class);
        if(userId != null){
            queryBuilder.setEQQueryCond("agentId", userId);
        }
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        queryBuilder.setEQQueryCond("vip", Vip.VIP2.getLevel());
        statistics.setUserUpvip2Count(userService.count(queryBuilder));

        queryBuilder = new QueryBuilder(User.class);
        if(userId != null){
            queryBuilder.setEQQueryCond("agentId", userId);
        }
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        queryBuilder.setEQQueryCond("vip", Vip.VIP3.getLevel());
        statistics.setUserUpvip3Count(userService.count(queryBuilder));

        queryBuilder = new QueryBuilder(User.class);
        if(userId != null){
            queryBuilder.setEQQueryCond("agentId", userId);
        }
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        queryBuilder.setEQQueryCond("vip", Vip.VIP4.getLevel());
        statistics.setUserUpvip4Count(userService.count(queryBuilder));



        // 订单增量统计
        queryBuilder = new QueryBuilder(Order.class);
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        queryBuilder.setEQQueryCond("status", OrderStatus.SUCCESS.getCode());
        if(userId != null){
            queryBuilder.setEQQueryCond("agentId", userId);
        }
        List<Map<String, Object>> list = orderService.countAndSumOrder(queryBuilder);
        for(Map<String, Object> map: list){
            int type = (int)map.get("type");
            long count = (long)map.get("count");
            long sum = ((BigDecimal)map.get("sum")).longValue();

            if(type == OrderType.INCOME_CASH.getCode()){
                statistics.setOrderCashCount(count);
                statistics.setOrderCashSum(sum);
            }else if(type == OrderType.SPENDING_UPVIP.getCode()){
                statistics.setOrderUpvipCount(count);
                statistics.setOrderUpvipSum(sum);
            }
            statistics.setOrderCount(statistics.getOrderCount() + count);
            statistics.setOrderSum(statistics.getOrderSum() + sum);
        }


        // 分润增量统计
        queryBuilder = new QueryBuilder(Commission.class);
        queryBuilder.setBetweenQueryCond("createTime", dayStartTime, dayEndTime);
        if(userId != null){
            queryBuilder.setEQQueryCond("targetUserId", userId);
        }
        list = commissionService.countAndSumCommission(queryBuilder);
        for(Map<String, Object> map: list){
            int type = (int)map.get("type");
            long count = (long)map.get("count");
            long sum = ((BigDecimal)map.get("sum")).longValue();

            switch (CommissionType.getType(type)){
                case CASH:
                    statistics.setCommissionCashCount(count);
                    statistics.setCommissionCashSum(sum);
                    break;
                case UPVIP:
                    statistics.setCommissionUpvipCount(count);
                    statistics.setCommissionUpvipSum(sum);
                    break;
                case NOVICE_AUTH:
                    statistics.setCommissionNoviceAuthCount(count);
                    statistics.setCommissionNoviceAuthSum(sum);
                    break;
                case NOVICE_CASH:
                    statistics.setCommissionNoviceCashCount(count);
                    statistics.setCommissionNoviceCashSum(sum);
                    break;
                case REFERRAL_AUTH:
                    statistics.setCommissionReferralAuthCount(count);
                    statistics.setCommissionReferralAuthSum(sum);
                    break;
                case REFERRAL_CASH:
                    statistics.setCommissionReferralCashCount(count);
                    statistics.setCommissionReferralCashSum(sum);
                    break;
                    default:
            }
            statistics.setCommissionCount(statistics.getCommissionCount() + count);
            statistics.setCommissionSum(statistics.getCommissionSum() + sum);
        }
        return statistics;
    }


    public Statistics sumStatistics(Long userId){
        return sumStatistics(userId,null);
    }


    public Statistics sumStatistics(Long userId, QueryBuilder queryBuilder){
        if(queryBuilder == null){
            queryBuilder = new QueryBuilder(StatisticsSum.class);
        }
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        } else {
            queryBuilder.setIsNullCond("userId");
        }
        queryBuilder.addColumns("userId");
        queryBuilder.addColumns("sum(commissionSum) as commissionSum");
        queryBuilder.addColumns("sum(commissionCount) as commissionCount");
        queryBuilder.addColumns("sum(commissionCashSum) as commissionCashSum");
        queryBuilder.addColumns("sum(commissionCashCount) as commissionCashCount");
        queryBuilder.addColumns("sum(commissionUpvipSum) as commissionUpvipSum");
        queryBuilder.addColumns("sum(commissionUpvipCount) as commissionUpvipCount");
        queryBuilder.addColumns("sum(commissionRegistSum) as commissionRegistSum");
        queryBuilder.addColumns("sum(commissionRegistCount) as commissionRegistCount");
        queryBuilder.addColumns("sum(commissionNoviceAuthSum) as commissionNoviceAuthSum");
        queryBuilder.addColumns("sum(commissionNoviceAuthCount) as commissionNoviceAuthCount");
        queryBuilder.addColumns("sum(commissionNoviceCashSum) as commissionNoviceCashSum");
        queryBuilder.addColumns("sum(commissionNoviceCashCount) as commissionNoviceCashCount");
        queryBuilder.addColumns("sum(commissionReferralAuthSum) as commissionReferralAuthSum");
        queryBuilder.addColumns("sum(commissionReferralAuthCount) as commissionReferralAuthCount");
        queryBuilder.addColumns("sum(commissionReferralCashSum) as commissionReferralCashSum");
        queryBuilder.addColumns("sum(commissionReferralCashCount) as commissionReferralCashCount");
        queryBuilder.addColumns("sum(orderSum) as orderSum");
        queryBuilder.addColumns("sum(orderCount) as orderCount");
        queryBuilder.addColumns("sum(orderCashSum) as orderCashSum");
        queryBuilder.addColumns("sum(orderCashCount) as orderCashCount");
        queryBuilder.addColumns("sum(orderUpvipSum) as orderUpvipSum");
        queryBuilder.addColumns("sum(orderUpvipCount) as orderUpvipCount");
        queryBuilder.addColumns("sum(orderWithdrawSum) as orderWithdrawSum");
        queryBuilder.addColumns("sum(orderWithdrawCount) as orderWithdrawCount");
        queryBuilder.addColumns("sum(userCount) as userCount");
        queryBuilder.addColumns("sum(userUpvip1Count) as userUpvip1Count");
        queryBuilder.addColumns("sum(userUpvip2Count) as userUpvip2Count");
        queryBuilder.addColumns("sum(userUpvip3Count) as userUpvip3Count");
        queryBuilder.addColumns("sum(userUpvip4Count) as userUpvip4Count");
        StatisticsSum statisticsSum = findOne(queryBuilder, StatisticsSum.class);
        return new Statistics(statisticsSum);
    }


    public Statistics findByCreateTime(Long userId, Date date){
        QueryBuilder queryBuilder = new QueryBuilder(Statistics.class);
        queryBuilder.setEQQueryCond("createTime", DateUtil.format(date, DateUtil.DATEFORMAT_TEN));
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        }
        return findOne(queryBuilder);
    }


    public static final Statistics getTodayCache(){
        String today = DateUtil.format(new Date(), DateUtil.DATEFORMAT_TEN);
        Statistics statistics = RedisUtil.get(CacheKey.STATISTICS.getKey(today));
        if(statistics == null){
            statistics = SpringUtil.getBean(StatisticsService.class).statisticsByUserIdAndDate(null, DateUtil.getTodayStartTime());
            statistics.setCreateTime(DateUtil.getTodayStartTime());
            RedisUtil.set(CacheKey.STATISTICS.getKey(today), statistics, 1, TimeUnit.DAYS);
        }
        return statistics;
    }


    public static final Statistics getTodayCache(Long userId){
        String today = DateUtil.format(new Date(), DateUtil.DATEFORMAT_TEN);
        Statistics statistics = RedisUtil.get(CacheKey.STATISTICS_AGENT.getKey(today, userId));
        if(statistics == null){
            statistics = SpringUtil.getBean(StatisticsService.class).statisticsByUserIdAndDate(userId, DateUtil.getTodayStartTime());
            statistics.setCreateTime(DateUtil.getTodayStartTime());
            RedisUtil.set(CacheKey.STATISTICS_AGENT.getKey(today, userId), statistics, 1, TimeUnit.DAYS);
        }
        return statistics;
    }


    public static final Statistics getSumStatisticsCache(Long userId){
        String today = DateUtil.format(new Date(), DateUtil.DATEFORMAT_TEN);
        String cacheKey = userId == null ? CacheKey.STATISTICS_AGENT_SUM.getKey(today) : CacheKey.STATISTICS_SUM.getKey(today, userId);

        Statistics statistics = RedisUtil.get(cacheKey);
        if(statistics == null){
            statistics = SpringUtil.getBean(StatisticsService.class).sumStatistics(null);
            RedisUtil.set(cacheKey, statistics, 1, TimeUnit.DAYS);
        }
        return statistics;
    }


    public static final synchronized void freshTodayCache(Order order){
        String today = DateUtil.format(new Date(), DateUtil.DATEFORMAT_TEN);
        Statistics statistics = getTodayCache();
        Statistics statisticsAgent = getTodayCache(order.getAgentId());
        switch (order.matchOrderType()){

            case SPENDING_UPVIP: {
                statistics.setOrderUpvipCount(statistics.getOrderUpvipCount() + 1);
                statistics.setOrderUpvipSum(statistics.getOrderUpvipSum() + order.getMoney());
                statisticsAgent.setOrderUpvipCount(statisticsAgent.getOrderUpvipCount() + 1);
                statisticsAgent.setOrderUpvipSum(statisticsAgent.getOrderUpvipSum() + order.getMoney());
            } break;

            case INCOME_CASH: {
                statistics.setOrderCashCount(statistics.getOrderCashCount() + 1);
                statistics.setOrderCashSum(statistics.getOrderCashSum() + order.getMoney());
                statisticsAgent.setOrderCashCount(statisticsAgent.getOrderCashCount() + 1);
                statisticsAgent.setOrderCashSum(statisticsAgent.getOrderCashSum() + order.getMoney());
            } break;

            case INCOME_WITHDRAW: {
                statistics.setOrderWithdrawCount(statistics.getOrderWithdrawCount() + 1);
                statistics.setOrderWithdrawSum(statistics.getOrderWithdrawSum() + order.getMoney());
                statisticsAgent.setOrderWithdrawCount(statisticsAgent.getOrderWithdrawCount() + 1);
                statisticsAgent.setOrderWithdrawSum(statisticsAgent.getOrderWithdrawSum() + order.getMoney());
            } break;
            default:
        }
        statistics.setOrderCount(statistics.getOrderCount() + 1);
        statistics.setOrderSum(statistics.getOrderSum() + order.getMoney());
        statisticsAgent.setOrderCount(statisticsAgent.getOrderCount() + 1);
        statisticsAgent.setOrderSum(statisticsAgent.getOrderSum() + order.getMoney());
        RedisUtil.set(CacheKey.STATISTICS.getKey(today), statistics, 1, TimeUnit.DAYS);
        RedisUtil.set(CacheKey.STATISTICS_AGENT.getKey(today, order.getAgentId()), statisticsAgent, 1, TimeUnit.DAYS);
    }


    public static final synchronized void freshTodayCache(User user){
        String today = DateUtil.format(new Date(), DateUtil.DATEFORMAT_TEN);
        Statistics statistics = getTodayCache();
        Statistics statisticsAgent = getTodayCache(user.getAgentId());
        switch (user.matchVip()){
            case VIP1: {
                statistics.setUserUpvip1Count(statistics.getUserUpvip1Count() + 1);
                statisticsAgent.setUserUpvip1Count(statisticsAgent.getUserUpvip1Count() + 1);
            } break;
            case VIP2: {
                statistics.setUserUpvip2Count(statistics.getUserUpvip2Count() + 1);
                statisticsAgent.setUserUpvip2Count(statisticsAgent.getUserUpvip2Count() + 1);
            } break;
            case VIP3: {
                statistics.setUserUpvip3Count(statistics.getUserUpvip3Count() + 1);
                statisticsAgent.setUserUpvip3Count(statisticsAgent.getUserUpvip3Count() + 1);
            } break;
            case VIP4: {
                statistics.setUserUpvip4Count(statistics.getUserUpvip4Count() + 1);
                statisticsAgent.setUserUpvip4Count(statisticsAgent.getUserUpvip4Count() + 1);
            } break;
            default:
        }
        if(today.equals(DateUtil.format(user.getCreateTime(), DateUtil.DATEFORMAT_TEN))){
            statistics.setUserCount(statistics.getUserCount() + 1);
            statisticsAgent.setUserCount(statisticsAgent.getUserCount() + 1);
        }
        RedisUtil.set(CacheKey.STATISTICS.getKey(today), statistics, 1, TimeUnit.DAYS);
        RedisUtil.set(CacheKey.STATISTICS_AGENT.getKey(today, user.getAgentId()), statisticsAgent, 1, TimeUnit.DAYS);
    }


    public static final synchronized void freshTodayCache(Commission commission){
        String today = DateUtil.format(new Date(), DateUtil.DATEFORMAT_TEN);
        Statistics statistics = getTodayCache();
        Statistics statisticsAgent = getTodayCache(commission.getTargetUserId());
        switch (commission.matchType()){
            case CASH: {
                statistics.setCommissionCashCount(statistics.getCommissionCashCount() + 1);
                statistics.setCommissionCashSum(statistics.getCommissionCashSum() + commission.getMoney());
                statisticsAgent.setCommissionCashCount(statisticsAgent.getCommissionCashCount() + 1);
                statisticsAgent.setCommissionCashSum(statisticsAgent.getCommissionCashSum() + commission.getMoney());
            } break;
            case UPVIP:{
                statistics.setCommissionUpvipCount(statistics.getCommissionUpvipCount() + 1);
                statistics.setCommissionUpvipSum(statistics.getCommissionUpvipSum() + commission.getMoney());
                statisticsAgent.setCommissionUpvipCount(statisticsAgent.getCommissionUpvipCount() + 1);
                statisticsAgent.setCommissionUpvipSum(statisticsAgent.getCommissionUpvipSum() + commission.getMoney());
            } break;
            case REGIST:{
                statistics.setCommissionRegistCount(statistics.getCommissionRegistCount() + 1);
                statistics.setCommissionRegistSum(statistics.getCommissionRegistSum() + commission.getMoney());
                statisticsAgent.setCommissionRegistCount(statisticsAgent.getCommissionRegistCount() + 1);
                statisticsAgent.setCommissionRegistSum(statisticsAgent.getCommissionRegistSum() + commission.getMoney());
            } break;
            case NOVICE_AUTH:{
                statistics.setCommissionNoviceAuthCount(statistics.getCommissionNoviceAuthCount() + 1);
                statistics.setCommissionNoviceAuthSum(statistics.getCommissionNoviceAuthSum() + commission.getMoney());
                statisticsAgent.setCommissionNoviceAuthCount(statisticsAgent.getCommissionNoviceAuthCount() + 1);
                statisticsAgent.setCommissionNoviceAuthSum(statisticsAgent.getCommissionNoviceAuthSum() + commission.getMoney());
            } break;
            case NOVICE_CASH:{
                statistics.setCommissionNoviceCashCount(statistics.getCommissionNoviceCashCount() + 1);
                statistics.setCommissionNoviceCashSum(statistics.getCommissionNoviceCashSum() + commission.getMoney());
                statisticsAgent.setCommissionNoviceCashCount(statisticsAgent.getCommissionNoviceCashCount() + 1);
                statisticsAgent.setCommissionNoviceCashSum(statisticsAgent.getCommissionNoviceCashSum() + commission.getMoney());
            } break;
            default:
        }
        statistics.setCommissionCount(statistics.getCommissionCount() + 1);
        statistics.setCommissionSum(statistics.getCommissionSum() + commission.getMoney());
        statisticsAgent.setCommissionCount(statisticsAgent.getCommissionCount() + 1);
        statisticsAgent.setCommissionSum(statisticsAgent.getCommissionSum() + commission.getMoney());
        RedisUtil.set(CacheKey.STATISTICS.getKey(today), statistics, 1, TimeUnit.DAYS);
        RedisUtil.set(CacheKey.STATISTICS_AGENT.getKey(today, commission.getTargetUserId()), statisticsAgent, 1, TimeUnit.DAYS);
    }

}
