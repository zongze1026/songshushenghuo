package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class MiLianCfg extends BaseChannelCfg {

    private Boolean disabled;
    private String url;
    private String userId;
    private String key;
    private String notifyUrl;
}
