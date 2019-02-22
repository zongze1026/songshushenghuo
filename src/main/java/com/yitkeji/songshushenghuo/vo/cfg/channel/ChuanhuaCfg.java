package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChuanhuaCfg extends BaseChannelCfg{
    private String baseurl;
    private String merchantNo;
    private String key;
    private String notifyUrl;

}
