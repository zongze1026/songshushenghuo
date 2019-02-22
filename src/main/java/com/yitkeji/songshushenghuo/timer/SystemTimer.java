package com.yitkeji.songshushenghuo.timer;

import com.yitkeji.songshushenghuo.timer.task.ExpiredOrderTask;
import com.yitkeji.songshushenghuo.timer.task.FreshSystemCfgTask;
import com.yitkeji.songshushenghuo.timer.task.StatisticsTask;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.TimerCfg;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SystemTimer {

    private Map<String, Future> futureMap = new HashMap<>();

    private static final class SystemTimerHolder{
        private static final SystemTimer SYSTEM_TIMER = new SystemTimer();
    }

    private SystemTimer(){
        this.loadDefaultTask();
    }

    public static final SystemTimer getInstance(){
        return SystemTimerHolder.SYSTEM_TIMER;
    }

    /**
     * 加载基本定时器任务
     * TODO 脚本并发的处理
     */
    private void loadDefaultTask(){
        TimerCfg timerCfg = SystemCfg.getInstance().getTimer();
        addTask("refreshSystemCfgTask", SpringUtil.getBean(FreshSystemCfgTask.class), timerCfg.getRefreshConfig() * 1000, timerCfg.getRefreshConfig() * 1000);
        addTask("expiredOrderTask", SpringUtil.getBean(ExpiredOrderTask.class), 0, timerCfg.getExpiredOrder() * 1000);
        addTask("statisticsTask", SpringUtil.getBean(StatisticsTask.class), 0, timerCfg.getStatistics() * 1000);
    }


    /**
     * 添加任务
     * @param key 唯一标志，后期可根据key终止任务
     * @param runnable 任务内容
     * @param initialDelay 首次执行延时时间(毫秒)
     * @param period 定时执行时间间隔(毫秒)
     * @return
     */
    public Future addTask(String key, Runnable runnable, long initialDelay, long period){
        if(null != futureMap.get(key)){
            return null;
        }
        //TODO 线程优化
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        Future future = service.scheduleAtFixedRate(runnable, initialDelay, period, TimeUnit.MILLISECONDS);

        futureMap.put(key, future);
        return future;
    }

    /**
     * 终止任务
     * @param key
     * @return
     */
    public Boolean removeTask(String key){
        if(null != futureMap.get(key)){
            Future future = futureMap.get(key);
            future.cancel(true);
            futureMap.remove(key);
            return true;
        }
        return false;
    }

    public Future getFuture(String key){
        return futureMap.get(key);
    }
}
