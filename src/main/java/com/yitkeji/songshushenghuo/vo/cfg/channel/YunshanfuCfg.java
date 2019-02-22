package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class YunshanfuCfg extends BaseChannelCfg{

    private String merchantNo;
    private String key;
    private String baseurl;
    private String notifyUrl;
    private Map<String, String> mccid;

}
