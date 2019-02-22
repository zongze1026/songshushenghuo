package com.yitkeji.songshushenghuo.mapper;

import com.yitkeji.songshushenghuo.vo.model.Commission;
import com.yitkeji.songshushenghuo.vo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Mapper
public interface CommissionMapper {

    @Select("select count(1) as count, sum(money) as sum, type from tb_commission where create_time between #{startTime} and #{endTime} group by type")
    List<Map<String, Object>> countAndSumByCreateTimeRangeGroupByType(Date startTime, Date endTime);

    @Select("<script>" +
            " select sum(money) as money,tb_commission.type,count(1) as count,target_user_id from tb_commission" +
            " where target_user_id in" +
            " <foreach collection=\"list\" item=\"item\" open=\"(\" separator=\",\" close=\")\">" +
            " #{item.userId} " +
            " </foreach>"+
            " group by target_user_id,tb_commission.type"+
            " </script>")
    List<Commission> queryCommissionTotalByTargetUserId(List<User> list);


}
