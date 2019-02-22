package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_yunshanfu_registed")
public class YunshanfuRegisted {

    @PrimaryKey("regist_id")
    private Long registId;
    private String channel;
    private Long userId;
    private Long debitCardId;
    private String name;
    private String idcard;
    private String cusid;
    private String outcusid;
    private double creditRate;
    private double debitRate;
    private double withdrawDepositRate;
    private double withdrawDepositSingleFee;
    private Date createTime;

}