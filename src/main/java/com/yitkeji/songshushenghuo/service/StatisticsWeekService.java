package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.Statistics;
import com.yitkeji.songshushenghuo.vo.model.StatisticsWeek;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class StatisticsWeekService extends BaseService<StatisticsWeek> {

    @Autowired
    private StatisticsService statisticsService;



    public boolean hasStatistics(Long userId, int yearWeek){
        QueryBuilder queryBuilder = new QueryBuilder(StatisticsWeek.class);
        queryBuilder.setEQQueryCond("yearWeek", yearWeek);
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        }
        return count(queryBuilder) > 0;
    }


    public StatisticsWeek findByYearAndWeek(Long userId, int year, int week){

        if(userId != null){
            QueryBuilder queryBuilder = new QueryBuilder(StatisticsWeek.class);
            queryBuilder.setEQQueryCond("yearWeek", year * 100 + week);
            queryBuilder.setEQQueryCond("userId", userId);
            return findOne(queryBuilder);
        }
        return null;
    }


    public StatisticsWeek statistics(Long userId, int year, int week){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week);
        calendar.set(Calendar.DAY_OF_WEEK, 2);

        Date start = calendar.getTime();
        calendar.set(Calendar.WEEK_OF_YEAR, week + 1);
        Date end = calendar.getTime();

        QueryBuilder queryBuilder = new QueryBuilder(Statistics.class);
        queryBuilder.setBetweenQueryCond("createTime", start, end);
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        }
        Statistics statistics = statisticsService.sumStatistics(userId, queryBuilder);

        StatisticsWeek statisticsWeek = findByYearAndWeek(userId, year, week);
        if(statisticsWeek == null){
            statisticsWeek = new StatisticsWeek();
            statisticsWeek.setUserId(userId);
            statisticsWeek.setYearWeek(year * 100 + week);
        }
        return ObjectUtil.convert(statistics, statisticsWeek);
    }

}
