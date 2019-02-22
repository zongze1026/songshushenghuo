package com.yitkeji.test.songshushenghuo;


import org.junit.Test;

public class CallbackTest extends BaseTest{

    @Test
    public void upvipNotify(){
//        postXml(baseurl + "/callback/wechat", "<xml><appid><![CDATA[wxed5426551dc787a9]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1501836841]]></mch_id><nonce_str><![CDATA[yitkeji.com]]></nonce_str><openid><![CDATA[oYXMI0jtWxOXqdisQ14-s46vYg0M]]></openid><out_trade_no><![CDATA[W1531481180035]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[98D3EFE334F25CFE242EBCF6C83C5F39]]></sign><time_end><![CDATA[20180713192655]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4200000134201807130118487001]]></transaction_id></xml>");
        postForm(baseurl + "/callback/alipay", "gmt_create=2018-10-18+15%3A13%3A44&charset=UTF-8&seller_email=songshushenghuo%40sina.com&subject=%E5%85%85%E5%80%BCvip&sign=C9MczM9ZlRkLXii9Lm0r0varoJaCYuhxcvmAHWtZ1ogTy5J%2BTq%2BibXiG%2ByKrJ5Kjv%2BiVAcSUMGxdnizdZdD5K1GL6O1IlsSakGpV8ADgyiYGpH1X97F%2FIZ%2BNvjltkXSJueuw5MzSjD9nO80BxW9wLIl%2FpqdP1NPubuBAr6r6ZdHsFmZrH0JPj2aArB%2Bo3%2BBaxby5MPAErW0UsyDPslE8DWrXrSNmEqOW9PUJ8zHoxVNxzqRGwy7yNbHcAiWGNHov5ebAVsxKyJaH3vedkGwSPxCGFULmrdLjQS6ZeMiRGJvgEqrbv1XV8sQcutjMVnM2KrSx%2F6qRsWxKIktigPZzNw%3D%3D&body=%E5%85%85%E5%80%BCvip&buyer_id=2088702589294233&invoice_amount=0.01&notify_id=2018101800222151344094231017072007&fund_bill_list=%5B%7B%22amount%22%3A%220.01%22%2C%22fundChannel%22%3A%22ALIPAYACCOUNT%22%7D%5D&notify_type=trade_status_sync&trade_status=TRADE_SUCCESS&receipt_amount=0.01&app_id=2018101761708472&buyer_pay_amount=0.01&sign_type=RSA2&seller_id=2088331139619710&gmt_payment=2018-10-18+15%3A13%3A44&notify_time=2018-10-18+15%3A27%3A17&version=1.0&out_trade_no=W15398468182341586&total_amount=0.01&trade_no=2018101822001494231005355577&auth_app_id=2018101761708472&buyer_logon_id=158****9564&point_amount=0.00");
    }

    @Test
    public void normal(){
        postJson(baseurl + "/callback/normal", "");
    }
}
