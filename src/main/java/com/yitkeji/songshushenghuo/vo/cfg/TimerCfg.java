package com.yitkeji.songshushenghuo.vo.cfg;

import lombok.Getter;
import lombok.Setter;

/**
 * 定时器配置项，单位统一为毫秒
 * 配置定时任务脚本的执行周期及策略。
 */
@Setter
@Getter
public class TimerCfg {
    /**
     * 是否依据策略延迟脚本的首次执行
     */
    private boolean intelligentDelay;

    /**
     * 刷新配置周期
     */
    private long refreshConfig;

    /**
     * 自动过期订单的周期
     */
    private long expiredOrder;

    /**
     * 表统计周期
     */
    private long statistics;

}
