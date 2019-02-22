package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table("tb_yunshanfu_area")
public class YunshanfuArea {

    @PrimaryKey("area_id")
    private Long areaId;
    private Integer areaCode;
    private String areaName;
    private Integer provinceCode;
    private Integer level;
}