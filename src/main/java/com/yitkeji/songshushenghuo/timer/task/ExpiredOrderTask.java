package com.yitkeji.songshushenghuo.timer.task;

import com.yitkeji.songshushenghuo.service.OrderService;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.List;



/**
 * 订单过期策略脚本
 */

@Component
public class ExpiredOrderTask extends BaseTask implements Runnable{

    @Autowired
    private OrderService orderService;

    @Override
    public void run() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - SystemCfg.getInstance().getSwitcher().getExpiredOrder());
        QueryBuilder queryBuilder = new QueryBuilder(Order.class);
        queryBuilder.setEQQueryCond("status", OrderStatus.CREATED.getCode());
        queryBuilder.setLessThanOrEqQueryCond("createTime", calendar.getTime());
        List<Order> list = orderService.findList(queryBuilder);
        for(Order order: list){
            orderService.expireOrder(order);
        }
        saveStatus();
    }
}
