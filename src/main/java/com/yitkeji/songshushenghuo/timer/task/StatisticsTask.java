package com.yitkeji.songshushenghuo.timer.task;


import com.yitkeji.songshushenghuo.component.EmailUtil;
import com.yitkeji.songshushenghuo.service.StatisticsMonthService;
import com.yitkeji.songshushenghuo.service.StatisticsService;
import com.yitkeji.songshushenghuo.service.StatisticsWeekService;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.vo.model.Statistics;
import com.yitkeji.songshushenghuo.vo.model.StatisticsMonth;
import com.yitkeji.songshushenghuo.vo.model.StatisticsWeek;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 设定统计任务的定时脚本
 */
@Component
public class StatisticsTask extends BaseTask implements Runnable{

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private StatisticsWeekService statisticsWeekService;

    @Autowired
    private StatisticsMonthService statisticsMonthService;

    @Autowired
    private UserService userService;

    @Override
    public void run() {
        // 设定定时器
        try{

            // 更新总统计
            setDayStatistics(null);
            setWeekStatistics(null);
            setMonthStatistics(null);

            // 更新代理商统计
            List<User> agentList = userService.findAgents();
            for(User agent: agentList){
                setDayStatistics(agent.getUserId());
                setWeekStatistics(agent.getUserId());
                setMonthStatistics(agent.getUserId());
            }

        }catch (Exception e){
            e.printStackTrace();
            EmailUtil.getInstance().reportException(e);
        }
        saveStatus();
    }

    /**
     * 增加昨日统计
     */
    private void setDayStatistics(Long userId){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1);
        if(!statisticsService.hasStatistics(userId, calendar)){
            Statistics statistics = statisticsService.statisticsByUserIdAndDate(userId, calendar.getTime());
            statisticsService.add(statistics);
        }
    }

    /**
     * 增加每周统计
     */
    private void setWeekStatistics(Long userId){

        // 更新上周
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR, calendar.get(Calendar.WEEK_OF_YEAR - 1));
        int year = calendar.get(Calendar.YEAR);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        int lastYearWeek = year * 100 + week;
        if(!statisticsWeekService.hasStatistics(userId, lastYearWeek)) {
            StatisticsWeek statisticsWeek = statisticsWeekService.statistics(userId, year, week);
            statisticsWeekService.add(statisticsWeek);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        week = calendar.get(Calendar.WEEK_OF_YEAR);

        // 更新本周
        StatisticsWeek statisticsWeek = statisticsWeekService.statistics(userId, year, week);
        statisticsWeekService.saveOrUpdate(statisticsWeek);
    }

    /**
     * 增加每月统计
     */
    private void setMonthStatistics(Long userId){

        // 更新上月
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) - 1);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int yearMonth = year * 100 + month;
        if(!statisticsMonthService.hasStatistics(userId, yearMonth)){
            StatisticsMonth statisticsMonth = statisticsMonthService.statistics(userId, year, month);
            statisticsMonthService.add(statisticsMonth);
        }

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);

        // 更新本月
        StatisticsMonth statisticsMonth = statisticsMonthService.statistics(userId, year, month);
        statisticsMonthService.saveOrUpdate(statisticsMonth);
    }
}
