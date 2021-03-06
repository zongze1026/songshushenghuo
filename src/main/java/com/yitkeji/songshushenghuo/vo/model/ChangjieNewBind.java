package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_changjie_new_bind")
public class ChangjieNewBind {

    @PrimaryKey("bind_id")
    private Long bindId;
    private String channel;
    private Long cardId;
    private String merchantNo;
    private String requestNo;
    private Date createTime;

}