package com.yitkeji.songshushenghuo.vo.enums;


import lombok.Getter;

/**
 * 管理员账户状态
 */

@Getter
public enum AdminStatus{
    /**
     * 正常
     */
    NORMAL("正常"),
    /**
     * 禁用
     */
    DISABLED("禁用"),
    /**
     * 删除
     */
    DELETED("删除");

    private String desc;
    AdminStatus(String desc){
        this.desc = desc;
    }
}
