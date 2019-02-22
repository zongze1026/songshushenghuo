package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class YunshanfuRes {
    private String appid; //返回码,Code码
    private String cusid; //商户号,商户号（唯一标识）
    private String outcusid; //商户识别号,商户识别号（唯一标识）
    private String retcode; //返回码,SUCCESS表示成功
    private String retmsg; //返回码描述,返回码描述信息
    private String sign; //签名,加密sign值
    private String state; //返回描述,1表示正常
    private String trxstatus; //交易状态,d
    private String agreeid; //返回码,Success为成功
    private String orderid; //商户订单号,交易返回
    private String errmsg; //返回描述,返回描述
    private String trxid; //交易单号,平台的交易流水号
    private String Sign; //签名字符串,d
    private String trxcode; //交易类型,d
    private String trxamt; //交易金额,d
    private String fintime; //交易完成时间,d
    private String acct; //交易信用卡卡号,d
    private String balance; //余额,单位：分
    private String acctno; //到账帐号,d
    private String amount; //提现模式,1代表全部提现
    private String actualamount; //到账金额,d
    private String fee; //手续费,d
}
