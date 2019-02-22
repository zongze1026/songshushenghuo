package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.enums.OrderStatus;
import com.yitkeji.songshushenghuo.vo.enums.OrderType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_order")
public class Order {

    @PrimaryKey("order_id")
    private Long orderId;

    private String channel;

    private Integer areaCode;

    private String orderNo;

    private String trackingNo;

    private String withdrawTrackingNo;

    private Long agentId;

    private Long userId;

    private Long sourceCardId;

    private Long targetCardId;

    private int type;

    private int money;

    private int rateMoney;

    private int arriveMoney;

    private String desc;

    private String status;

    private String comment;

    private Date createTime;

    private Date lastupdatetime;


    /**
     * 返回订单类型
     *
     * @return OrderType
     */
    public OrderType matchOrderType() {
        for (OrderType orderType : OrderType.values()) {
            if (orderType.getCode() == this.getType()) {
                return orderType;
            }
        }
        return null;
    }


    /**
     * 返回订单状态
     *
     * @return OrderStatus
     */
    public OrderStatus matchOrderStatus() {
        for (OrderStatus orderStatus : OrderStatus.values()) {
            if (orderStatus.getCode() == this.getStatus()) {
                return orderStatus;
            }
        }
        return null;
    }
}