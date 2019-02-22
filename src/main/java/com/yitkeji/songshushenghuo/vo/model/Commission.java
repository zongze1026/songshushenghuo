package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.enums.CommissionLevel;
import com.yitkeji.songshushenghuo.vo.enums.CommissionType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_commission")
public class Commission {

    @PrimaryKey("commission_id")
    private Long commissionId;

    private Long orderId;

    private Long sourceUserId;

    private Long targetUserId;

    private CommissionLevel level;

    private Integer type;

    private int money;

    private Date createTime;

    public CommissionType matchType(){
        return CommissionType.getType(type);
    }
}