package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum CommissionFlag {

    FLASE("未分润", 0),

    TRUE("分润", 1);



    private int code;
    private String desc;

    CommissionFlag(String desc, int code){
        this.desc = desc;
        this.code = code;
    }


}
