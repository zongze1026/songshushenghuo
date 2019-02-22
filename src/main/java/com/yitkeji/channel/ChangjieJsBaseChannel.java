package com.yitkeji.channel;

import com.yitkeji.channel.inter.MapChannel;
import com.yitkeji.channel.model.ChannelParams;
import com.yitkeji.channel.model.Position;
import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.IpService;
import com.yitkeji.songshushenghuo.service.YunshanfuAreaService;
import com.yitkeji.songshushenghuo.util.ChannelRouter;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.util.SqlUtil;
import com.yitkeji.songshushenghuo.vo.cfg.channel.ChangjieNewCfg;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import com.yitkeji.songshushenghuo.vo.model.Ip;
import com.yitkeji.songshushenghuo.vo.model.Order;
import com.yitkeji.songshushenghuo.vo.model.YunshanfuArea;

import java.util.List;

public class ChangjieJsBaseChannel extends ChangjieNewBaseChannel {

    private YunshanfuAreaService yunshanfuAreaService;

    private IpService ipService;

    protected ChangjieJsBaseChannel(Channel channel, ChangjieNewCfg changjieNewCfg) {
        super(channel, changjieNewCfg);
        yunshanfuAreaService = SpringUtil.getBean(YunshanfuAreaService.class);
        ipService = SpringUtil.getBean(IpService.class);
    }

    @Override
    public Object cash(ChannelParams params) throws BaseException {
        params = setPosition(params);
        return super.cash(params);
    }

    @Override
    public Order repayment(ChannelParams params) throws BaseException {
        params = setPosition(params);
        return super.repayment(params);
    }


    private ChannelParams setPosition(ChannelParams params) throws BaseException {

        // 用户自选地区
        if(params.getAreaCode() != null){
            YunshanfuArea city = yunshanfuAreaService.findByAreaCode(params.getAreaCode());
            if(city.getProvinceCode() != null){
                YunshanfuArea province = yunshanfuAreaService.findByAreaCode(city.getProvinceCode());
                List<Ip> ips = ipService.findNearlyIps(province.getAreaName(), city.getAreaName());
                if(ips.size() > 0){
                    int random = Double.valueOf(Math.floor(Math.random() * ips.size())).intValue();
                    params.setIp(ips.get(random).getStartIp());
                    params.setCity(city.getAreaName());
                    return params;
                }
            }
        }

        // 根据IP匹配
        if(params.getIp() == null){
            params.setIp(params.getUser().getLastLoginIp());
        }

        if(params.getIp() != null){
            List<MapChannel> mapChannel = new ChannelRouter().filterBusinessType(BusinessType.MAP).getList();
            if(mapChannel.size() < 1){
                throw new BaseException("定位通道不可用");
            }
            Position position = mapChannel.get(0).position(params.getIp());
            params.setCity(position.getCity());
        }

        if(params.getIp() == null || params.getCity() == null || params.getIp().equals("127.0.0.1") || params.getIp().equals("localhost")){
            params.resetPosition();
        }
        return params;
    }
}
