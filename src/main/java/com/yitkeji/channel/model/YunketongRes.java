package com.yitkeji.channel.model;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class YunketongRes {
    private String state;
    private String code;
    private String message;
    private String data;
    private YunketongResultMap resultMap;
}
