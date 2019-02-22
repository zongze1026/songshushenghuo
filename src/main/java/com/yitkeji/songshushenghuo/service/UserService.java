package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.mapper.UserMapper;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.vip.RateElement;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.Commission;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.model.UserRate;
import com.yitkeji.songshushenghuo.vo.res.app.CountUserRes;
import com.yitkeji.songshushenghuo.vo.res.app.UserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.querydsl.QuerydslUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserService extends BaseService<User> {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CommissionService commissionService;

    @Override
    public int add(User object) {
        int rows = super.add(object);
        StatisticsService.freshTodayCache(object);
        return rows;
    }

    /**
     * 根据手机号查询
     * @param phone
     * @return
     */
    public User findByPhone(String phone){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("status", UserStatus.NORMAL);
        queryBuilder.setEQQueryCond("phone", phone);
        return findOne(queryBuilder);
    }

    /**
     * 根据用户名查询
     * @param userName
     * @return
     */
    public User findByUserName(String userName){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("userName", userName);
        return findOne(queryBuilder);
    }

    /**
     * 根据推荐码查询
     * @param referralCode
     * @return
     */
    public User findByReferralCode(String referralCode){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("referralCode", referralCode);
        return findOne(queryBuilder);
    }

    public List<User> findAgents(){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setGreatThanOrEqQueryCond("vip", Vip.VIP3.getLevel());
        return findList(queryBuilder);
    }

    /**
     * 升级vip
     * @param user
     * @param vip
     * @param type
     * @param orderId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean upVip(User user, Vip vip, UpVipType type, Long orderId){
        user.setUpvipTime(Calendar.getInstance().getTime());
        User referral = user.getReferenceId() == null ? null: findByPrimaryKey(user.getReferenceId());
        User agent = user.getAgentId() == null ? null: findByPrimaryKey(user.getAgentId());

        if(type.equals(UpVipType.BUY)){
            if(user.getVip() == Vip.VIP1.getLevel()){
                Double money = Vip.getVip(vip.getLevel()).getShare().getUpvip()[0];

                if(vip.equals(Vip.VIP2)){
                    Commission commission = new Commission();
                    commission.setOrderId(orderId);

                    // 分发推荐人奖励
                    if(referral != null){
                        commission.setTargetUserId(referral.getUserId());
                        commission.setMoney(money.intValue());
                        setCommission(user, commission);
                        upMoney(referral.getUserId(), money.intValue());

                        // 统计推荐人推荐VIP的数量，判定是否自动升级
                        if(referral.getVip() == Vip.VIP1.getLevel()){
                            int count = countVipFriends(referral, Vip.VIP2);
                            // VIP推荐了10个VIP
                            if(count + 1 > 9){
                                upVip(referral, Vip.VIP2, UpVipType.AUTO, orderId);
                            }
                        }
                    }
                    // 分发代理奖励
                    if (agent != null){
                        Vip agentVip = Vip.getVip(agent.getVip());
                        if (agentVip.getLevel() > Vip.VIP2.getLevel()){
                            commission = new Commission();
                            commission.setOrderId(orderId);
                            commission.setTargetUserId(agent.getUserId());
                            commission.setMoney(agentVip.getShare().getUpvip()[1].intValue());
                            commission.setTargetUserId(agent.getUserId());
                            setCommission(user, commission);
                            upMoney(agent.getUserId(), agentVip.getShare().getUpvip()[1].intValue());
                        }
                    }
                }
            }
        }
        user.setVip(vip.getLevel());
        switch (vip){
            case VIP2: user.setUpvipTime(new Date()); break;
            case VIP3: user.setUpvip3Time(new Date()); break;
            case VIP4: user.setUpvip4Time(new Date()); break;
            default:break;
        }
        this.updateByPrimaryKey(user, "vip", "upvipTime", "upvip3Time", "upvip4Time");
        StatisticsService.freshTodayCache(user);
        return true;
    }

    /**
     * 普通用户交易满30万升级成vip
     * @param order 订单详情
     * @param user 用户信息
     */
    public void orderFullUpVipMoney(Order order, User user) {
        if (user.getVip() == Vip.VIP1.getLevel()){
            Map<String, Object> map = orderService.sumOrder(order.getUserId(), null);
            if(map != null && map.get("sum") != null){
                int orderMoney = ((BigDecimal) map.get("sum")).intValue();
                int orderFullUpVipMoney = SystemCfg.getInstance().getSwitcher().getOrderFullUpVipMoney();
                if ((orderMoney + order.getMoney())  >= orderFullUpVipMoney){
                    user.setVip(Vip.VIP2.getLevel());
                    super.updateByPrimaryKey(user, "vip");
                }
            }
        }
    }

    private void setCommission(User user, Commission commission) {
        commission.setLevel(CommissionLevel.LEVEL2);
        commission.setType(CommissionType.UPVIP.getCode());
        commission.setSourceUserId(user.getUserId());
        commissionService.add(commission);
    }

    public int countVipFriends(User user, Vip vip){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("vip", vip.getLevel());
        queryBuilder.setEQQueryCond("referenceId", user.getUserId());
        return count(queryBuilder);
    }

    private void freshCache(Long userId) {
        User user = findByPrimaryKey(userId);
        RedisUtil.set(CacheKey.TOKEN.getKey(user.getToken()), user, 7, TimeUnit.DAYS);
        RedisUtil.set(CacheKey.AGENT_TOKEN.getKey(user.getAgentToken()), user, 7, TimeUnit.DAYS);

    }

    public User findByPrimaryKey(Long userId, UserStatus userStatus) {
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        if (userStatus != null) {
            queryBuilder.setEQQueryCond("status", userStatus);
        }
        queryBuilder.setEQQueryCond("userId", userId);
        return super.findOne(queryBuilder);
    }

    public int countByIdcard(String idcard){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("idcard", idcard);
        return count(queryBuilder);
    }

    @Override
    public int updateByPrimaryKey(User user, String... columns) {
        int rows = super.updateByPrimaryKey(user, columns);
        // 刷新缓存
        freshCache(user.getUserId());
        return rows;
    }

    public List<Map<String, Object>> countVip(QueryBuilder queryBuilder) {
        if (null == queryBuilder) {
            queryBuilder = new QueryBuilder(User.class);
        }
        queryBuilder.addColumns("count(1) as count", "vip");
        queryBuilder.setGroup("vip");
        return super.findList(queryBuilder, Map.class);
    }

    public List<Map<String, Object>> countFriends(Long userId) {
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.addColumns("count(1) as count", "authStatus");
        queryBuilder.setEQQueryCond("referenceId", userId);
        queryBuilder.setGroup("authStatus");
        return super.findList(queryBuilder, Map.class);
    }

    public UserRate computeUserRate(User user) {
        UserRate userRate = new UserRate();
        Vip vip = user.matchVip();
        if (null != vip) {
            userRate.setRate(vip.getRate());
            userRate.setFixedAmount(vip.getFixedAmount());
            userRate.setCashRate(vip.getCashRate());
            userRate.setCashFixedAmount(vip.getCashFixedAmount());
            userRate.setYunshanfuRate(vip.getYunshanfuRate().getRepayment().getRate());
            userRate.setYunshanfuFixedAmount(vip.getYunshanfuRate().getRepayment().getFixedAmount());
            RateElement withdrawRate = SystemCfg.getInstance().getRate().getWithdraw();
            userRate.setWithdrawRate(withdrawRate.getRate());
            userRate.setWithdrawFixedAmount(withdrawRate.getFixedAmount());
        }
        return userRate;
    }

    public int upMoney(Long userId, int money) {
        int rows = userMapper.upMoney(userId, money);
        freshCache(userId);
        return rows;
    }

    public boolean checkUserName(String userName) {
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("userName", userName);
        return super.count(queryBuilder) > 0;
    }

    public UserRes findUserInfo(Long userId) {
        return userMapper.findUserInfo(userId);
    }

    public int downMoney(Long userId, int money) {
        int rows = userMapper.downMoney(userId, money);
        freshCache(userId);
        return rows;
    }

    /**
     * 根据用户上级找到挂靠的上级
     *
     * @param user
     * @param agent
     * @return null || User
     */
    public User findAgent(User user, User agent) {
        if (user.getVip() < agent.getVip()) {
            return agent;
        }

        // 用户上级也没有关联上级
        if (agent.getAgentId() == null) {
            return null;
        }
        return findAgent(user, findByPrimaryKey(agent.getAgentId(), UserStatus.NORMAL));

    }

    /**
     * 统计用户
     * @param queryBuilder
     * @return
     */
    public List<Map<String, Object>> countAndSumUser(QueryBuilder queryBuilder) {
        if (null == queryBuilder) {
            queryBuilder = new QueryBuilder(User.class);
        }
        queryBuilder.addColumns("count(1) as count", "sum(money) as sum", "vip");
        queryBuilder.setGroup("vip");
        return this.findList(queryBuilder, Map.class);
    }
}
