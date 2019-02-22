package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum CardType {

    /**
     * 信用卡
     */
    CREDIT("信用卡", 1),
    /**
     * 储蓄卡
     */
    DEBIT("储蓄卡", 0);

    private String desc;
    private int code;

    CardType(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final CardType getType(int code){
        for(CardType type: CardType.values()){
            if(type.getCode() == code){
                return type;
            }
        }
        return null;
    }
}
