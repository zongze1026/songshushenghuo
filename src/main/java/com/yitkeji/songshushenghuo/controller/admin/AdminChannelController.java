package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.channel.*;
import com.yitkeji.songshushenghuo.service.ChangjieNewRegistedService;
import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.service.YunketongRegistedService;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.model.ChangjieNewRegisted;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.YunketongRegisted;
import com.yitkeji.songshushenghuo.vo.req.admin.ChannelReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"通道管理"})
@RestController
public class AdminChannelController  extends BaseAdminController{

    @Autowired
    private OrderService orderService;

    @Autowired
    private YunketongRegistedService yunketongRegistedService;

    @Autowired
    private ChangjieNewRegistedService changjieNewRegistedService;

    @ApiOperation(value = "手动提现")
    @RequestMapping(value = "/channel/settlement", method = RequestMethod.POST)
    public Result settlement(@RequestBody ChannelReq channelReq, HttpServletRequest request){
        Order order = orderService.findByPrimaryKey(channelReq.getOrderId());

        if (order.getChannel().contains("CHANGJIE")){
            order = ChangjieLdChannel.getInstance().settlement(order);
        }

        if (order.getChannel().contains("YUNKETONG")){
            order = YunketongLdChannel.getInstance().settlement(order);
        }
        return Result.auto(order.matchOrderStatus().equals(OrderStatus.SUCCESS), order.getComment());
    }

    @ApiOperation(value = "查询结算结果")
    @RequestMapping(value = "/channel/queryPay", method = RequestMethod.POST)
        public Result queryPay(@RequestBody ChannelReq channelReq){
        Order order = orderService.findByPrimaryKey(channelReq.getOrderId());

        if (order.getChannel().contains("CHANGJIE")){
            QueryBuilder queryBuilder = new QueryBuilder(ChangjieNewRegisted.class);
            queryBuilder.setEQQueryCond("userId", channelReq.getUserId());
            ChangjieNewRegisted changjieNewRegisted = changjieNewRegistedService.findOne(queryBuilder);
            ChangjieLdChannel.getInstance().queryOrder(changjieNewRegisted, order, 1);
        }

        if (order.getChannel().contains("YUNKETONG")){
            QueryBuilder queryBuilder = new QueryBuilder(YunketongRegisted.class);
            queryBuilder.setEQQueryCond("userId", channelReq.getUserId());
            YunketongRegisted yunketongRegisted = yunketongRegistedService.findOne(queryBuilder);
            YunketongLdChannel.getInstance().queryOrder(yunketongRegisted, order, 1);
        }

        return Result.success();
    }
}
