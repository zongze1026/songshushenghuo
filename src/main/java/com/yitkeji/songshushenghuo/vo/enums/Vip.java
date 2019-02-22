package com.yitkeji.songshushenghuo.vo.enums;

import com.yitkeji.songshushenghuo.vo.cfg.rate.RateCfg;
import com.yitkeji.songshushenghuo.vo.cfg.vip.VipShare;
import lombok.Getter;
import lombok.Setter;


/**
 * VIP
 * 配置信息与VipCfg同步加载
 */
@Getter
public enum Vip {

    /**
     *
     */
    VIP1(1, "普通用户"),
    VIP2(2, "vip会员"),
    VIP3(3,"运营中心"),
    VIP4(4,"分公司");

    private int level;

    private String desc;

    @Setter
    private String name;

    @Setter
    private Double price;

    @Setter
    private int periodOfValidity;

    @Setter
    private Double referralPrice;

    @Setter
    private Double rate;

    @Setter
    private Long fixedAmount;

    @Setter
    private Double cashRate;

    @Setter
    private Long cashFixedAmount;

    @Setter
    private String icon;

    @Setter
    private VipShare share;

    @Setter
    private RateCfg defaultRate;

    @Setter
    private RateCfg yunshanfuRate;

    Vip(int level, String desc){
        this.desc = desc;
        this.level = level;
    }


    public static final Vip getVip(int level){
        for(Vip vip: Vip.values()){
            if(vip.getLevel() == level){
                return vip;
            }
        }
        return null;
    }
}
