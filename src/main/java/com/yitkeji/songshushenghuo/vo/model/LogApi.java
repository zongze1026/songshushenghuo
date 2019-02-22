package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_log_api")
public class LogApi {

    @PrimaryKey("log_api_id")
    private Long logApiId;

    private String apiName;

    private String method;

    private Date runTime;

    private Date resTime;

    private Date createTime;

    private String url;

    private String data;

    private String result;

}