package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_user_withdraw")
public class UserWithdraw {

    @PrimaryKey("user_withdraw_id")
    private Long userWithdrawId;

    private Long userId;

    private Long cardId;

    private Long orderId;

    private int money;

    private int status;

    private String comment;

    private Date createTime;

    private Date lastupdatetime;
}
