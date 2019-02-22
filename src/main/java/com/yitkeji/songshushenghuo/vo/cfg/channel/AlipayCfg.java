package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AlipayCfg extends BaseChannelCfg{
    private String url;
    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
}
