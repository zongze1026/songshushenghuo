package com.yitkeji.songshushenghuo.mapper;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.res.app.OrderRes;
import com.yitkeji.songshushenghuo.vo.res.app.SumCashRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface OrderMapper {

    /**
     * 订单详情
     */
    @Select("select tor.order_id as orderId,tor.order_no as orderNo,tor.tracking_no as trackingNo,tor.desc as orderDesc,tor.comment as comment," +
            "tcr.card_no as sourceCardNo,tcr.card_bank as sourceCardBank,DATE_FORMAT(tor.lastupdatetime,'%Y-%m-%d %T') as strLastupdatetime," +
            "tcd.card_no as targetCardNo,tcd.card_bank as targetCardBank,tor.money as money,tor.arrive_money as arriveMoney," +
            "tor.status as status,tor.type as type,DATE_FORMAT(tor.create_time,'%Y-%m-%d %T') as strCreateTime from tb_order tor " +
            "left join tb_card tcr on tor.source_card_id = tcr.card_id left join tb_card tcd on tor.target_card_id = tcd.card_id " +
            "WHERE tor.order_id = #{orderId}")
    OrderRes findOrderInfo(@Param("orderId") Long orderId);

    @Select("<script>"+
            " SELECT SUM(money) AS money,tb_order.type FROM tb_order WHERE status = \"1\" and" +
            " agent_id IN " +
            " <foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item.userId} " +
            " </foreach>"+
            " GROUP BY tb_order.type,agent_id"+
            " </script>")
     List<Order>  queryOrderTotalByAgentId(List<User> list);


    @Select("<script>" +
            "select user_id from tb_order " +
            "where status = #{status} and" +
            "type = #{type} and" +
            "user_id in" +
            "<if test='list != null and list.size() > 0'>" +
            " <foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item.userId} " +
            " </foreach>"+
            "</if>"+
            "group by user_id"+
            "having sum(money) > #{limitMoney}"+
            " </script>"
    )
    List<Order> queryOrderTotal(@Param("limitMoney") int limitMoney,@Param("type") String type,@Param("status") String status,@Param("list") List<User> list);


    @Select("select agent_id,user_id,money from tb_order where order_id = #{orderId} and commission_flag = #{commissionFlag} and type = #{type} and status = #{status}  for update")
    Order getOrderForUpdate(@Param("orderId") String orderId,@Param("commissionFlag") int commissionFlag,@Param("type") String type,@Param("status") String status);

    @Update("update tb_order set orderId = #{orderId} where commission_flag = #{commissionFlag}")
    void updateCommissionFlag(@Param("orderId") String orderId,int commissionFlag);
}
