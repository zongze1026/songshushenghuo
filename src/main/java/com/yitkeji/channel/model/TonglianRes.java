package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TonglianRes {

    private String appid;
    private String cusid;
    private String outcusid;
    private String retcode;
    private String retmsg;
    private String sign;

    private Long partnerId;
    private String partnerKey;
    private String partnerName;
    private String partnerContact;
    private String partnerBank;
    private String partnerBankNo;
    private String partnerPhone;
    private Integer partnerIsRun;
    private String partnerSettlementType;
    private String partnerCreateDate;
    private String partnerEditDate;

    private String errmsg;
    private String orderid;
    private String trxid;
    private String trxstatus;
    private Thpinfo thpinfo;

    private String fintime;

    private String agreeid;

    private String acctno;
    private String actualamount;
    private String amount;
    private String fee;


    private String balance;

    @Setter
    @Getter
    public class Thpinfo{
        private String sign;
        private String tphtrxcrtime;
        private String tphtrxid;
        private String trxflag;
        private String trxsn;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getTphtrxcrtime() {
            return tphtrxcrtime;
        }

        public void setTphtrxcrtime(String tphtrxcrtime) {
            this.tphtrxcrtime = tphtrxcrtime;
        }

        public String getTphtrxid() {
            return tphtrxid;
        }

        public void setTphtrxid(String tphtrxid) {
            this.tphtrxid = tphtrxid;
        }

        public String getTrxflag() {
            return trxflag;
        }

        public void setTrxflag(String trxflag) {
            this.trxflag = trxflag;
        }

        public String getTrxsn() {
            return trxsn;
        }

        public void setTrxsn(String trxsn) {
            this.trxsn = trxsn;
        }
    }

}
