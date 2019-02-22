package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分润筛选类型枚举类
 */
@Getter
public enum  CommissionFiltrateType {

    /**
     * 分润筛选类型
     */
    REGIST("全部", 0),
    CASH("收款分润", 1),
    REFERRAL("推荐奖励",2),
    NOVICE("新手任务", 3);

    private int code;
    private String desc;

    CommissionFiltrateType(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final Map<String, Integer> getCommissionFiltrateTypeMap(){
        Map<String, Integer> commissionFiltrateTypeMap = new LinkedHashMap<>();
        for(CommissionFiltrateType planStatus : CommissionFiltrateType.values()){
            commissionFiltrateTypeMap.put(planStatus.desc, planStatus.code);
        }
        return commissionFiltrateTypeMap;
    }
}
