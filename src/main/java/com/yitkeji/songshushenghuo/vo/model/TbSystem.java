package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_system")
public class TbSystem {

    @PrimaryKey("system_id")
    private Long systemId;

    private String key;

    private String value;

    private String type;

    private String comment;

    private Integer readOnly;

    private Integer status;

    private Date lastupdatetime;

}