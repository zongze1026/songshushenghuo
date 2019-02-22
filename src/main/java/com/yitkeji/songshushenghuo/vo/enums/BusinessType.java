package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

/**
 * 业务类型
 */
@Getter
public enum BusinessType {
    /**
     * 升级VIP
     */
    UPVIP("升级VIP"),

    /**
     * 卡测评
     */
    CARDMEASURE("卡测评"),

    /**
     * 收款
     */
    CASH("收款"),

    /**
     * 还款
     */
    REPAYMENT("还款"),

    /**
     * 余额提现
     */
    WITHDRAW("余额提现"),

    /**
     * 地图定位
     */
    MAP("地图定位"),

    /**
     * 短信
     */
    SMS("短信")

    ;

    private String desc;
    BusinessType(String desc){
        this.desc = desc;
    }
}
