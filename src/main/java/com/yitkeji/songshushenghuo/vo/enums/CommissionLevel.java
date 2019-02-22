package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

/**
 * 分润层级
 */
@Getter
public enum CommissionLevel {
    /**
     * 直接分享
     */
    LEVEL2("直接分享"),
    /**
     * 间接分享
     */
    LEVEL3("间接分享");
    private String desc;
    CommissionLevel(String desc){
        this.desc = desc;
    }
}
