package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    /**
     *
     */
    SUPER_ADMIN("超级管理员", 1),
    ADMIN("管理员", 2),
    AGENT("代理商", 3);

    private String desc;
    private int code;

    RoleEnum(String desc, int code){
        this.desc = desc;
        this.code = code;
    }
}
