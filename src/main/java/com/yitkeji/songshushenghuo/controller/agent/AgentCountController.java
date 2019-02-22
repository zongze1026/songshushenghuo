package com.yitkeji.songshushenghuo.controller.agent;

import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.StatisticsMonthService;
import com.yitkeji.songshushenghuo.service.StatisticsService;
import com.yitkeji.songshushenghuo.service.StatisticsWeekService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.StatisticsUnit;
import com.yitkeji.songshushenghuo.vo.model.Statistics;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.CondReq;
import com.yitkeji.songshushenghuo.vo.req.admin.CountChartReq;
import com.yitkeji.songshushenghuo.vo.res.admin.StatisticsRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api(tags = {"统计管理"})
@RestController
public class AgentCountController extends BaseAgentController {

    @Autowired
    private StatisticsService statisticsService;

    @Autowired
    private StatisticsWeekService statisticsWeekService;

    @Autowired
    private StatisticsMonthService statisticsMonthService;

    @ApiOperation(value = "统计详情")
    @RequestMapping(value = "/count/info", method = RequestMethod.GET)
    public Result<StatisticsRes> info(CondReq condReq, HttpServletRequest request) throws BaseException {

        User agent = getCacheAgent(request);

        // 总数据
        if(condReq.getLocalCond().size() < 1){
            Statistics statistics = StatisticsService.getSumStatisticsCache(agent.getUserId());
            return Result.success(ObjectUtil.convert(statistics, StatisticsRes.class));
        }

        // 按时间区段的增量合计
        QueryBuilder queryBuilder = initQueryBuilder("statistics", condReq);
        Statistics statistics = statisticsService.sumStatistics(agent.getUserId(), queryBuilder);
        return Result.success(ObjectUtil.convert(statistics, StatisticsRes.class));
    }

    @ApiOperation(value = "统计折线图")
    @RequestMapping(value = "/count/chart", method = RequestMethod.GET)
    public Result<List<StatisticsRes>> chart(CountChartReq req, HttpServletRequest request) throws BaseException {

        User agent = getCacheAgent(request);
        // 按天统计
        if(req.getStatisticsUnit().equals(StatisticsUnit.DAY)){
            QueryBuilder queryBuilder = initQueryBuilder("statistics", req);
            queryBuilder.setEQQueryCond("userId", agent.getUserId());
            List<StatisticsRes> list = statisticsService.findList(queryBuilder, StatisticsRes.class);
            return Result.success(list);
        }

        // 按周统计
        if(req.getStatisticsUnit().equals(StatisticsUnit.WEEK)){
            QueryBuilder queryBuilder = initQueryBuilder("statisticsWeek", req);
            queryBuilder.setEQQueryCond("userId", agent.getUserId());
            List<StatisticsRes> list = statisticsWeekService.findList(queryBuilder, StatisticsRes.class);
            return Result.success(list);
        }

        // 按月统计
        if(req.getStatisticsUnit().equals(StatisticsUnit.MONTH)){
            QueryBuilder queryBuilder = initQueryBuilder("statisticsMonth", req);
            queryBuilder.setEQQueryCond("userId", agent.getUserId());
            List<StatisticsRes> list = statisticsMonthService.findList(queryBuilder, StatisticsRes.class);
            return Result.success(list);
        }
        return Result.fail("不支持的统计单位");
    }
}
