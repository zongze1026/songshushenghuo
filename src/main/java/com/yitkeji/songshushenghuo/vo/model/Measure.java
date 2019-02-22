package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@Table("tb_measure")
public class Measure {

    @PrimaryKey("measure_id")
    private Long measureId;

    private Long userId;

    private Long cardId;

    private String measureUrl;

    private Date createTime;
}
