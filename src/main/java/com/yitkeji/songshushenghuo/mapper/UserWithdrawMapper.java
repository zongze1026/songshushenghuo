package com.yitkeji.songshushenghuo.mapper;

import com.yitkeji.songshushenghuo.vo.res.app.UserWithdrawRes;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserWithdrawMapper {

    /**
     * 用户申请提现详情
     */
    @Select("select *, `tb_user_withdraw`.`create_time` as withdrawCreateTime, `tb_user_withdraw`.`money` as withdrawMoney " +
            "from `tb_user_withdraw`, `tb_user`, `tb_card` where 1=1  and `tb_user_withdraw`.`user_withdraw_id` = #{userWithdrawId} " +
            "and `tb_user`.`user_id` = `tb_user_withdraw`.`user_id` and `tb_card`.`card_id` = `tb_user_withdraw`.`card_id`")
    UserWithdrawRes findUserWithdrawInfo(@Param("userWithdrawId") long userWithdrawId);
}
