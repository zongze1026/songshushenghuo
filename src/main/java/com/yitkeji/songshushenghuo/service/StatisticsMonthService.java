package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.model.Statistics;
import com.yitkeji.songshushenghuo.vo.model.StatisticsMonth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;


@Service
public class StatisticsMonthService extends BaseService<StatisticsMonth> {

    @Autowired
    private StatisticsService statisticsService;



    public boolean hasStatistics(Long userId, int yearMonth){
        QueryBuilder queryBuilder = new QueryBuilder(StatisticsMonth.class);
        queryBuilder.setEQQueryCond("yearMonth", yearMonth);
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        }
        return count(queryBuilder) > 0;
    }


    public StatisticsMonth findByYearAndMonth(Long userId, int year, int month){

        if(userId != null){
            QueryBuilder queryBuilder = new QueryBuilder(StatisticsMonth.class);
            queryBuilder.setEQQueryCond("yearMonth", year * 100 + month);
            queryBuilder.setEQQueryCond("userId", userId);
            return findOne(queryBuilder);
        }
        return null;
    }

    public StatisticsMonth statistics(Long userId, int year, int month){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        Date start = calendar.getTime();
        calendar.set(Calendar.MONTH, month + 1);
        Date end = calendar.getTime();

        QueryBuilder queryBuilder = new QueryBuilder(Statistics.class);
        queryBuilder.setBetweenQueryCond("createTime", start, end);
        if(userId != null){
            queryBuilder.setEQQueryCond("userId", userId);
        }
        Statistics statistics = statisticsService.sumStatistics(userId, queryBuilder);
        StatisticsMonth statisticsMonth = findByYearAndMonth(userId, year, month);
        if(statisticsMonth == null){
            statisticsMonth = new StatisticsMonth();
            statisticsMonth.setUserId(userId);
            statisticsMonth.setYearMonth(year * 100 + month);
        }
        BeanUtils.copyProperties(statistics, statisticsMonth);
        return statisticsMonth;
    }
}
