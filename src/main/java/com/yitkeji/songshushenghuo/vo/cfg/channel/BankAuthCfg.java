package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BankAuthCfg extends BaseChannelCfg{

    private String url;
    private String path;
    private String AppKey;
    private String AppSecret;
    private String AppCode;

}
