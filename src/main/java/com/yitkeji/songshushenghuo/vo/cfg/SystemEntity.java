package com.yitkeji.songshushenghuo.vo.cfg;

import com.yitkeji.songshushenghuo.vo.cfg.appinfo.AppInfo;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Banks;
import com.yitkeji.songshushenghuo.vo.cfg.channel.ChannelCfg;
import com.yitkeji.songshushenghuo.vo.cfg.disconf.Disconf;
import com.yitkeji.songshushenghuo.vo.cfg.rate.RateCfg;
import com.yitkeji.songshushenghuo.vo.cfg.switc.SwitcherCfg;
import com.yitkeji.songshushenghuo.vo.cfg.vip.VipCfg;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SystemEntity {
    private AppInfo appinfo;
    private RateCfg rate;
    private TimerCfg timer;
    private VipCfg vip;
    private SwitcherCfg switcher;
    private AdminCfg admin;
    private Banks banks;
    private ChannelCfg channel;
    private Banks debitbanks;
    private Disconf disconf;
}
