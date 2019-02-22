package com.yitkeji.songshushenghuo.vo.res.admin;

import com.alibaba.fastjson.JSON;
import com.yitkeji.songshushenghuo.util.Result;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class LogActionRes {

    @ApiModelProperty("主键ID")
    private Long logActionId;

    @ApiModelProperty("管理员ID")
    private Long adminId;

    @ApiModelProperty("管理员ID")
    private String adminName;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("模块")
    private String module;

    @ApiModelProperty("操作名称")
    private String name;

    @ApiModelProperty("操作结果")
    private String result;

    @ApiModelProperty("操作员IP")
    private String clientIp;

    @ApiModelProperty("创建时间")
    private Date createTime;


    public String getResult() {
        Result result = JSON.parseObject(this.result, Result.class);
        return result.getMsg();
    }

    public void setResult(String result) {
        this.result = result;
    }
}