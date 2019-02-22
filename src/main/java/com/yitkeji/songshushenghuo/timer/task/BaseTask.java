package com.yitkeji.songshushenghuo.timer.task;
import java.util.Calendar;
import java.util.Date;

public class BaseTask {
    private Date lastRunTime;

    protected void saveStatus(){
        lastRunTime = Calendar.getInstance().getTime();
    }
}
