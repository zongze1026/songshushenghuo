package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum AgentStatus {

    /**
     * 正常
     */
    NORMAL("正常", 1),
    /**
     * 禁用
     */
    DISABLED("禁用", 0),
    /**
     * 删除
     */
    DELETED("删除", -1);

    private String desc;
    private int code;

    AgentStatus(String desc, int code){
        this.desc = desc;
        this.code = code;
    }
}
