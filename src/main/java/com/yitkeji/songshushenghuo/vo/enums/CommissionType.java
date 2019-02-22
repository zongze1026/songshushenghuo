package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

@Getter
public enum CommissionType {
    /**
     * 分润类型
     */
    REGIST("注册", 0),
    UPVIP("推荐升级-VIP", 1),
    CASH("收款分润", 2),
    NOVICE_AUTH("新手任务-实名", 3),
    NOVICE_CASH("新手任务-收款", 4),
    REFERRAL_AUTH("推荐奖励-实名",5),
    REFERRAL_CASH("推荐奖励-收款",6);

    private int code;
    private String desc;

    CommissionType(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final CommissionType getType(int code){
        for(CommissionType commissionType: CommissionType.values()){
            if(commissionType.getCode() == code){
                return commissionType;
            }
        }
        return null;
    }
}
