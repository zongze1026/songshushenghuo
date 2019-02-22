package com.yitkeji.songshushenghuo.mapper;

import com.yitkeji.songshushenghuo.vo.res.app.CountUserRes;
import com.yitkeji.songshushenghuo.vo.res.app.UserRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMapper {

    @Update("update tb_user set money = money + #{money} where user_id = #{userId}")
    int upMoney(@Param("userId") Long userId, @Param("money") int money);

    @Update("update tb_user set money = money - #{money} where user_id = #{userId}")
    int downMoney(@Param("userId") Long userId, @Param("money") int money);

    @Select("select u.*,   u1.vip as referenceVip,u1.phone AS referencePhone,u1.real_name as referenceRealName,u2.vip as agentVip,u2.phone as agentPhone,u2.real_name as agentRealName from tb_user u left join tb_user u1 on u.reference_id = u1.user_id left join tb_user u2 on u.agent_id = u2.user_id where u.user_id = #{userId}")
    UserRes findUserInfo(@Param("userId") Long userId);
}