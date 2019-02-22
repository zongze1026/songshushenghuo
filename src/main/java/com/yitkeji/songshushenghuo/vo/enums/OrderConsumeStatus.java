package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

/**
 * 订单结算状态
 */
@Getter
public enum OrderConsumeStatus {

    CREATED("未结算", 0),
    SUCCESS("已结算", 1),
    FAILED("结算失败", 2);


    private String desc;
    private int code;

    OrderConsumeStatus(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

}
