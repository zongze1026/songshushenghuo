package com.yitkeji.songshushenghuo.vo.enums;

import lombok.Getter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 计划状态
 */
@Getter
public enum PlanStatus {

    ALL("全部", 10),
    PREVIEW("预览", -1),
    CREATED("创建", 0),
    RUNNING("执行中", 1),
    SUCCESS("成功", 2),
    FAILED("失败", 3),
    CANCELED("撤销", 4);


    private String desc;
    private int code;

    PlanStatus(String desc, int code){
        this.desc = desc;
        this.code = code;
    }

    public static final PlanStatus getStatus(int code){
        for(PlanStatus planStatus: PlanStatus.values()){
            if(planStatus.getCode() == code){
                return planStatus;
            }
        }
        return null;
    }

    public static final Map<String, Integer> getPlanStatusMap(){
        Map<String, Integer> planStatusMap = new LinkedHashMap<>();
        for(PlanStatus planStatus : PlanStatus.values()){
            planStatusMap.put(planStatus.getDesc(), planStatus.code);
        }
        return planStatusMap;
    }
}
