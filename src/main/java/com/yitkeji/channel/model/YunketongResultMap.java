package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class YunketongResultMap {
    private String code;
    private String bizMsg = "请求失败";
    private String bizCode;
    private String status;

    // regist
    private String encryptKey;
    private String merchantNo;

    // bindCard
    private String bindStatus;

    // pay
    private String requestNo;
    private String payRequestNo;
    private String amount;

    private String isSms;
    private String isCalFee;
    private String fee;

    // queryWallet
    private String amountD0;
    private String amountT1;
    private String state;
}
