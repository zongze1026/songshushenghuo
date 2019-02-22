package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewsCfg extends BaseChannelCfg{

    private Boolean disabled;
    private String baseurl;
    private String key;

}
