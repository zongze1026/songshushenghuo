package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class ChangjieResultMap {
    private String merchantCode;
    private String respCode;
    private String respMsg;
    private Integer signStatus;

    // queryPay
    private String orderNo;
    private String channelCode;
    private Integer orderStatus;
    private Integer revokeStatus;
    private Integer returnStatus;
    private Integer orderAmount;
    private Date payTime;
    private Integer merchantFee;
    private Integer receiveAmount;
    private String settleDate;

    // queryMerchantWallet
    private Integer quickpayBalance;
    private Integer quickpayD0Balance;
    private Integer quickpayT1Balance;

    // queryWithdraw
    private String spCode;
    private String reqFlowNo;
    private Integer walletType;
    private Double amount;
    private Integer remitStatus;
    private Date reqTime;
    private Date remitTime;


    // 畅捷代付
    private String bizCode;
    private String bizMsg;
    private String code;
    private String status;
}
