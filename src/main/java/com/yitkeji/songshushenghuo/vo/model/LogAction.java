package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_log_action")
public class LogAction {

    @PrimaryKey("log_action_id")
    private Long logActionId;

    private Long adminId;

    private Long userId;

    private String module;

    private String name;

    private String path;

    private String data;

    private String result;

    private String clientIp;

    private Date createTime;
}