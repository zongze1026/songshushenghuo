package com.yitkeji.songshushenghuo.vo.model;

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
@Table("tb_card")
public class Card {

    @PrimaryKey("card_id")
    private Long cardId;

    private Long userId;

    private String cardNo;

    private String cardBank;

    private String bankCode;

    private String expiryDate;

    private String cvv;

    private String name;

    private String idcard;

    private String phone;

    private String email;

    private int type;

    private int status;

    private Date createTime;

    private Date lastupdatetime;

    public Bank matchBank(){
        return Banks.getByCode(bankCode, matchType());
    }

    public CardType matchType(){
        for(CardType type: CardType.values()){
            if(type.getCode() == this.type){
                return type;
            }
        }
        return null;
    }
}