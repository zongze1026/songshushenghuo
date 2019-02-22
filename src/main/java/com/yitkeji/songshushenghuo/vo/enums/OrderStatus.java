package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 订单状态
 */
@Getter
public enum OrderStatus {

    /**
     *
     */
    All("全部","0"),
    CREATED("处理中", "1"),
    SUCCESS("交易成功", "2"),
    FAILED("交易失败", "3"),
    CANCEL("处理超时", "4");


    private String desc;
    private String code;

    OrderStatus(String desc, String code){
        this.desc = desc;
        this.code = code;
    }

    public static final OrderStatus getStatus(String code){
        for(OrderStatus orderStatus: OrderStatus.values()){
            if(orderStatus.getCode().equals(code)){
                return orderStatus;
            }
        }
        return null;
    }

    public static final Map<String, String> getOrderFiltrateStatusMap(){
        Map<String, String> orderFiltrateTypeMap = new LinkedHashMap<>();
        for(OrderStatus orderFiltrateTypeMap1 : OrderStatus.values()){
            orderFiltrateTypeMap.put(orderFiltrateTypeMap1.getDesc(), orderFiltrateTypeMap1.getCode());
        }
        return orderFiltrateTypeMap;
    }

}
