package com.yitkeji.songshushenghuo.timer.task;

import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import org.springframework.stereotype.Component;


/**
 * 刷新配置的任务脚本
 */
@Component
public class FreshSystemCfgTask extends BaseTask implements Runnable{


    @Override
    public void run() {
        SystemCfg.getInstance().freshConfig();
        saveStatus();
    }
}
