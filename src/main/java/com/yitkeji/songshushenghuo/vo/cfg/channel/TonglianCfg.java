package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TonglianCfg extends BaseChannelCfg{

    private String merchantNo;
    private String key;
    private String baseurl;
    private String notifyUrl;

}
