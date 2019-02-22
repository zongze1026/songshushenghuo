package com.yitkeji.songshushenghuo.vo.model;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Table("tb_ip")
public class Ip {

    @PrimaryKey("start_ip")
    private String startIp;
    private String endIp;
    private String country;
    private String local;
}