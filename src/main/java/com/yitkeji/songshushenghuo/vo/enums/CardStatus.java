package com.yitkeji.songshushenghuo.vo.enums;


import lombok.Getter;

/**
 * 订单状态
 */
@Getter
public enum CardStatus {
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

    CardStatus(String desc, int code) {
        this.desc = desc;
        this.code = code;
    }

    public static final CardStatus getStatus(int code) {
        for (CardStatus status : CardStatus.values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return null;
    }
}
