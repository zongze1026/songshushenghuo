package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 订单类型枚举类
 */
@Getter
public enum OrderType{

    /**
     * 订单类型
     */
    ALL("全部", 0),
    SPENDING_UPVIP("升级VIP", -1001),
    INCOME_CASH("收款", 1001),
    INCOME_WITHDRAW("提现", 1003);

    private String desc;
    private int code;

    OrderType(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final OrderType getType(int code){
        for(OrderType orderType: OrderType.values()){
            if(orderType.getCode() == code){
                return orderType;
            }
        }
        return null;
    }

    public static final Map<String, Integer> getOrderFiltrateTypeMap(){
        Map<String, Integer> orderFiltrateTypeMap = new LinkedHashMap<>();
        for(OrderType orderType : OrderType.values()){
            orderFiltrateTypeMap.put(orderType.desc, orderType.code);
        }
        return orderFiltrateTypeMap;
    }
}
