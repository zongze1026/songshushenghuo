package com.yitkeji.songshushenghuo.controller.agent;

import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.PageReq;
import com.yitkeji.songshushenghuo.vo.res.admin.OrderRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"订单管理"})
@RestController
public class AgentOrderController extends BaseAgentController {

    @Autowired
    private OrderService orderService;

    /**
     * 订单列表
     * @param pageReq
     * @return
     */
    @ApiOperation(value = "获取订单列表")
    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
    public Result<Page> findList(PageReq pageReq, HttpServletRequest request) throws BaseException {
        Page page = new Page(pageReq);
        User agent = getCacheAgent(request);
        QueryBuilder queryBuilder = initQueryBuilder("order", pageReq);
        queryBuilder.setEQQueryCond("agentId", agent.getUserId());
        addJoinColumns(queryBuilder);
        queryBuilder.addOrder(queryBuilder.getPrimaryKey(), false);
        page = orderService.findListByPage(page, queryBuilder, OrderRes.class);
        return Result.success(filterRes(page, request, false));
    }
}
