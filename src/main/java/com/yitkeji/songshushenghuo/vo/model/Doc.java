package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import com.yitkeji.songshushenghuo.vo.enums.DocType;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_doc")
public class Doc {

    @PrimaryKey("doc_id")
    private Long docId;

    private String title;

    private String module;

    private String androidPageLabel;

    private String iosPageLabel;

    private String image;

    private String color;

    private String desc;

    private String url;

    private DocType type;

    private int status;

    private Boolean readOnly;

    private Date createTime;

    private Date lastupdatetime;

    private String content;
}