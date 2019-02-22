package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_tonglian_bind")
public class TonglianBind {

    @PrimaryKey("bind_id")
    private Long bindId;
    private Long cardId;
    private String cusid;
    private String requestNo;
    private String agreeid;
    private Date createTime;

}