package com.yitkeji.songshushenghuo.vo.enums;


import lombok.Getter;

/**
 * API模块集合
 */

@Getter
public enum Channel {
    /**
     * 沃动短信1
     */
    WODONG1("沃动1"),
    /**
     * 沃动短信2
     */
    WODONG2("沃动2"),
    /**
     * 短信
     */
    SMS("短信"),
    /**
     * 畅捷商旅
     */
    CHANGJIESL("畅捷商旅"),
    /**
     * 畅捷落地
     */
    CHANGJIELD("畅捷落地"),
    /**
     * 畅捷落地2
     */
    CHANGJIELD2("畅捷落地2"),
    /**
     * 江苏畅捷商旅
     */
    CHANGJIEJSSL("江苏畅捷商旅"),
    /**
     * 江苏畅捷落地
     */
    CHANGJIEJSLD("江苏畅捷落地"),
    /**
     * 江苏畅捷落地2
     */
    CHANGJIEJSLD2("江苏畅捷落地2"),
    /**
     * 江苏畅捷小额1500
     */
    CHANGJIEJSXDHSM("江苏畅捷小额1500"),
    /**
     * 福建畅捷商旅
     */
    CHANGJIEFJSL("福建畅捷商旅"),
    /**
     * 福建畅捷落地
     */
    CHANGJIEFJLD("福建畅捷落地"),
    /**
     * 福建畅捷落地2
     */
    CHANGJIEFJLD2("福建畅捷落地2"),
    /**
     * 福建畅捷小额1500
     */
    CHANGJIEFJSM("福建畅捷小额1500"),
    /**
     * 福建畅捷区域性
     */
    CHANGJIEFJSBANK("福建畅捷区域性"),
    /**
     * 畅捷代付
     */
    CHANGJIEDF("畅捷代付"),
    /**
     * 畅捷快付通代付
     */
    CHANGJIEKFTDF("畅捷快付通代付"),
    /**
     * 云客通商旅
     */
    YUNKETONGSL("云客通商旅"),
    /**
     * 云客通落地
     */
    YUNKETONGLD("云客通落地"),
    /**
     * 云客通落地2
     */
    YUNKETONGLD2("云客通落地2"),
    /**
     * 通联
     */
    TONGLIAN("通联"),
    /**
     * 云闪付
     */
    YUNSHANFU("云闪付"),
    /**
     * 支付宝
     */
    ALIPAY("支付宝"),
    /**
     * 高德地图
     */
    AMAP("高德地图"),
    /**
     * 微信
     */
    WECHAT("微信"),
    /**
     * 有盾
     */
    YOUDUN("有盾"),
    /**
     * 新闻
     */
    NEWS("新闻"),
    /**
     * 卡测评
     */
    JIZHI("卡测评"),
    /**
     * 米联
     */
    MILIAN("米联"),
    /**
     * 四要素
     */
    BANKAUTH("四要素");
    private String desc;

    Channel(String desc) {
        this.desc = desc;
    }

    public static final Channel getChannel(String desc) {
        try {
            return Channel.valueOf(desc);
        } catch (Exception e) {
            return null;
        }
    }


    public boolean isSmall(){
        return this.equals(YUNSHANFU) || this.equals(CHANGJIEJSXDHSM) || this.equals(CHANGJIEFJSM);
    }
}
