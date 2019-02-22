package com.yitkeji.songshushenghuo.vo.cfg.channel;

import com.yitkeji.songshushenghuo.vo.cfg.SmsTemplate;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SmsCfg extends BaseChannelCfg{
    private String url;
    private String userid;
    private String username;
    private String password;
    private int timelimit;
    private SmsTemplate template;
}
