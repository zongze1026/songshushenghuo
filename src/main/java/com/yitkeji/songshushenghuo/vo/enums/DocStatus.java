package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum DocStatus {

    NORMAL("正常", 1),
    DISABLED("禁用", 0),
    DELETED("删除", -1);

    private String desc;
    private int code;

    DocStatus(String desc, int code){
        this.desc = desc;
        this.code = code;
    }
}
