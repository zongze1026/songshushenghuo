package com.yitkeji.songshushenghuo.vo.enums;


import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;

/**
 * 缓存key常量
 */
public enum CacheKey {
    /**
     * 短信验证码
     */
    SMSCODE("smsCode:%s:%d"),
    /**
     * 图片验证码
     */
    IMGCODE("imgCode:%s"),

    /**
     * 限制发送验证码次数
     */
    MOBILESMSTIMES("mobileSmsTimes:%s:%d"),

    /**
     * 邮件通知统计信息
     */
    EMAIL_NOTICE_COUNT("notice-count"),

    /**
     * 今日统计
     */
    STATISTICS("statistics:%s"),

    /**
     * 今日代理商统计
     */
    STATISTICS_AGENT("user-statistics:%s:%d"),

    /**
     * 历史增量统计总量
     */
    STATISTICS_SUM("statistics-sum:%s"),

    /**
     * 代理商历史增量统计总量
     */
    STATISTICS_AGENT_SUM("statistics-agent-sum:%s:%d"),
    /**
     * 用户令牌
     */
    TOKEN("token:%s"),
    /**
     * 后台TOKEN
     */
    ADMIN_TOKEN("adminToken:%s"),
    /**
     * 代理商令牌
     */
    AGENT_TOKEN("agentToken:%s"),
    /**
     * 角色
     */
    ROLE("role:%d"),
    /**
     * 畅捷
     */
    CHANGJIE("changjie:%s"),
    /**
     * 微信
     */
    WECHAT("wechat:%s"),
    /**
     * 支付宝
     */
    ALIPAY("alipay:%s"),
    /**
     * 高德地图
     */
    AMAP("amap:%s"),
    /**
     * 通联
     */
    TONGLIAN("tonglian:%s"),
    /**
     * 云闪付
     */
    YUNSHANFU("yunshanfu:%s"),
    /**
     * 用户订单
     */
    USERORDER("user:order:%s"),
    /**
     * 接口请求频率控制
     */
    FREQUENCY("frequency:%s:%s"),

    /**
     * 接口请求缓存
     */
    API_CACHE("api-cache:%s");

    private String key;

    CacheKey(String key) {
        this.key = key;
    }

    public String getKey(Object... args) {
        return SystemCfg.getInstance().getAppinfo().getPack() + ":" + String.format(key, args);
    }
}
