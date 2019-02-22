package com.yitkeji.songshushenghuo.validation.enums;

import lombok.Getter;

@Getter
public enum CardInfo {
    /**
     * 身份证号
     */
    IDCARD("身份证号"),
    /**
     * 银行卡号
     */
    CARDNO("银行卡号"),
    /**
     * 银行名称
     */
    BANK("银行名称"),
    /**
     * CVV码
     */
    CVV("CVV码"),
    /**
     * 有效期
     */
    EXPIRED("有效期"),
    /**
     * 账单日
     */
    BILLDAY("账单日"),
    /**
     * 还款日
     */
    REPAYMENTDAY("还款日");

    String desc;

    CardInfo(String desc) {
        this.desc = desc;
    }
}
