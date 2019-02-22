package com.yitkeji.songshushenghuo.vo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Bank;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Banks;
import com.yitkeji.songshushenghuo.vo.enums.CardType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_bank_auth")
public class BankAuth {

    @PrimaryKey("bank_auth_id")
    private Long bankAuthId;

    private String name;

    private String cardNo;

    private String idcard;

    private String phone;

    private String bankName;

    private String bankKind;

    private String bankType;

    private String bankCode;

    private Date createTime;

    @JsonIgnore
    public CardType getType(){
        if("信用卡,贷记卡".contains(bankType)){
            return CardType.CREDIT;
        }
        return CardType.DEBIT;
    }

    @JsonIgnore
    public Bank matchBank(){
        return Banks.getByCode(bankCode, getType());
    }

}