package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum AuthStatus {
    /**
     * 未提交
     */
    NORMAL("未提交", 0),
    /**
     * 通过实名，未绑卡
     */
    PASS("通过实名，未绑卡", 1),
    /**
     * 审核成功
     */
    SUCCESS("审核成功", 2),
    /**
     * 审核失败
     */
    FAIL("审核失败", -1);

    private String desc;
    private int code;

    AuthStatus(String desc, int code){
        this.desc = desc;
        this.code = code;
    }
}
