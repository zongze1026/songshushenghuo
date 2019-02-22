package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class YoudunCfg extends BaseChannelCfg{

    private String pub_key;
    private String security_key;
    private String url;
}
