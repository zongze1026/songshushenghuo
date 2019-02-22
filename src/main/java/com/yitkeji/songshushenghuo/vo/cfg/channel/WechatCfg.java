package com.yitkeji.songshushenghuo.vo.cfg.channel;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WechatCfg extends BaseChannelCfg{

    private String appid_app;
    private String appsecret;
    private String mch_id_app;
    private String key;
    private String notifyUrl;

}
