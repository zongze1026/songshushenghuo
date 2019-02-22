package com.yitkeji.songshushenghuo.vo.cfg.switc;

import com.yitkeji.songshushenghuo.util.DateUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;

@Setter
@Getter
@ApiModel
public class TimeRange {


    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    public boolean checkDate(Date date){
        String[] startStr = startTime.split(":");
        String[] endStr = endTime.split(":");

        Calendar calendarStart = Calendar.getInstance();
        calendarStart.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startStr[0]));
        calendarStart.set(Calendar.MINUTE, Integer.parseInt(startStr[1]));
        calendarStart.set(Calendar.SECOND, 0);

        Calendar calendarEnd = Calendar.getInstance();
        calendarEnd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endStr[0]));
        calendarEnd.set(Calendar.MINUTE, Integer.parseInt(endStr[1]));
        calendarEnd.set(Calendar.SECOND, 0);

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date);
        return calendarStart.before(calendarDate) && calendarEnd.after(calendarDate);
    }

    public Calendar getStartTime(Date date){
        String[] startStr = startTime.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDayStartTime(date));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(startStr[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(startStr[1]));
        return calendar;
    }

    public Calendar getEndTime(Date date){
        String[] endStr = endTime.split(":");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(DateUtil.getDayStartTime(date));

        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(endStr[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(endStr[1]));
        return calendar;
    }
}
