package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.util.DateUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.enums.OrderType;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.app.OrderPageReq;
import com.yitkeji.songshushenghuo.vo.res.app.OrderFiltrateTypeRes;
import com.yitkeji.songshushenghuo.vo.res.app.OrderRes;
import com.yitkeji.songshushenghuo.vo.res.app.StatusRes;
import com.yitkeji.songshushenghuo.vo.res.app.TypeRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = {"订单接口"})
@RestController
public class OrderController extends BaseAppController {

    @Autowired
    private OrderService orderService;

    /**
     * 账单列表
     */
    @ApiOperation(value = "获取账单列表")
    @RequestMapping(value = "/order/list", method = RequestMethod.GET)
    public Result<Page<OrderRes>> list(OrderPageReq req, HttpServletRequest request){
        Page page = initPage(req);
        User user = getCacheUser(request);

        QueryBuilder queryOrder = new QueryBuilder(Order.class);
        queryOrder.setEQQueryCond("userId", user.getUserId());

        if (req.getType() != OrderType.ALL.getCode()){
            queryOrder.setInQueryCond("type", req.getType());
        }

        if(req.getStartTime() != null && req.getEndTime() != null){
            Date startTime = DateUtil.parse(req.getStartTime(), DateUtil.DATEFORMAT_NINTEEN);
            Date endTime = DateUtil.parse(req.getEndTime(), DateUtil.DATEFORMAT_NINTEEN);
            queryOrder.setBetweenQueryCond("createTime", startTime, endTime);
        }

        queryOrder.addOrder("orderId", false);
        page = orderService.findListByPage(page, queryOrder, OrderRes.class);
        return Result.success(page);
    }

    /**
     * 账单详情
     */
    @ApiOperation(value = "获取订单详情")
    @RequestMapping(value = "/order/info", method = RequestMethod.GET)
    public Result<OrderRes> orderInfo(Long orderId){
        OrderRes orderRes = orderService.findOrderInfo(orderId);
        return Result.success(orderRes);
    }

    /**
     * 订单筛选类型
     */
    @ApiOperation(value = "获取订单筛选类型")
    @RequestMapping(value = "/order/type", method = RequestMethod.GET)
    public Result<OrderFiltrateTypeRes> getTypeList() {

        List<TypeRes> typeResList = new ArrayList<>();
        Map<String, Integer> orderTypeMap = OrderType.getOrderFiltrateTypeMap();
        for (Map.Entry<String, Integer> entry : orderTypeMap.entrySet()) {
            TypeRes typeRes = new TypeRes();
            typeRes.setDesc(entry.getKey());
            typeRes.setCode(entry.getValue());
            typeResList.add(typeRes);
        }

        List<StatusRes> statusResList = new ArrayList<>();
        Map<String, String> orderStatusMap = OrderStatus.getOrderFiltrateStatusMap();
        for (Map.Entry<String, String> entry : orderStatusMap.entrySet()) {
            StatusRes statusRes = new StatusRes();
            statusRes.setDesc(entry.getKey());
            statusRes.setCode(entry.getValue());
            statusResList.add(statusRes);
        }

        OrderFiltrateTypeRes orderFiltrateTypeRes = new OrderFiltrateTypeRes();
        orderFiltrateTypeRes.setTypeList(typeResList);
        orderFiltrateTypeRes.setStatusList(statusResList);
        return Result.success(orderFiltrateTypeRes);
    }

}
