package com.yitkeji.songshushenghuo.util;


import com.alibaba.fastjson.JSON;
import com.yitkeji.channel.*;
import com.yitkeji.channel.inter.ChannelInterface;
import com.yitkeji.songshushenghuo.component.EmailUtil;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.Channel;
import lombok.Getter;

import java.util.*;

/**
 * 示例：new ChannelRouter().filter().getList();
 * @author Administrator
 */
@Getter
public class ChannelRouter {

    private List list;
    private BusinessType businessType = null;
    private String[] unions = null;
    private Integer money = null;
    private boolean filterSm = false;
    public ChannelRouter(List list){
        this.list = list;
    }

    public ChannelRouter(){
        list = new ArrayList<>();
        // 云闪付
        list.add(YunshanfuChannel.getInstance());

        // 落地
        list.add(ChangjieLdChannel.getInstance());
        list.add(ChangjieJsLdChannel.getInstance());
        list.add(ChangjieFjLdChannel.getInstance());
        list.add(YunketongLdChannel.getInstance());

        // 落地2
        list.add(ChangjieLd2Channel.getInstance());
        list.add(ChangjieJsLd2Channel.getInstance());
        list.add(ChangjieFjLd2Channel.getInstance());
        list.add(YunketongLd2Channel.getInstance());

        // 商旅
        list.add(ChangjieSlChannel.getInstance());
        list.add(ChangjieJsSlChannel.getInstance());
        list.add(ChangjieFjSlChannel.getInstance());
        list.add(YunketongSlChannel.getInstance());

        // 小额
        list.add(ChangjieJsxdhsmChannel.getInstance());
        list.add(ChangjieFjSmChannel.getInstance());

        // 区域性
        list.add(ChangjieFjSbankChannel.getInstance());

        // 通联
        list.add(TonglianChannel.getInstance());

        // 代付
        list.add(ChangjieDfChannel.getInstance());
        list.add(ChangjieKftDfChannel.getInstance());

        // 支付宝、微信
        list.add(AlipayChannel.getInstance());
        list.add(WechatChannel.getInstance());

        // 地图
        list.add(AMapChannel.getInstance());

        // 短信
        list.add(Wodong1Channel.getInstance());
        list.add(Wodong2Channel.getInstance());
    }


    public ChannelRouter filterBusinessType(BusinessType businessType){
        this.businessType = businessType;
        return this;
    }

    public ChannelRouter filterUnion(String... unions){
        this.unions = unions;
        return this;
    }

    public ChannelRouter filterMoney(Integer money){
        this.money = money;
        return this;
    }

    public ChannelRouter filterSm(){
        this.filterSm = true;
        return this;
    }

    public <T extends ChannelInterface> List<T> getList(){
        Iterator<ChannelInterface> iterator = list.iterator();
        while (iterator.hasNext()){
            ChannelInterface channel = iterator.next();

            try{
                // 业务类型筛选
                if(this.businessType != null){
                    if(channel.getCfg() == null){
                        System.out.println(JSON.toJSONString(channel.getCfg()));
                        continue;
                    }
                    if(!this.businessType.equals(channel.getCfg().getBusinessType())){
                        iterator.remove();
                        continue;
                    }
                }

                // 银行筛选
                if(this.unions != null && this.unions.length > 0){
                    String banks = channel.getCfg().getBanks();
                    boolean removed = false;
                    for(String union: this.unions){
                        if(banks != null && banks.indexOf(union) == -1){
                            removed = true;
                            iterator.remove();
                            break;
                        }
                    }
                    if(removed){
                        continue;
                    }
                }

                // 金额筛选
                if(this.money != null){
                    if(this.money > channel.getCfg().getMaxMoney() || this.money < channel.getCfg().getMinMoney()){
                        iterator.remove();
                        continue;
                    }
                }

                // 小额通道筛选
                if(this.filterSm){
                    if(channel.getChannel().isSmall()){
                        iterator.remove();
                        continue;
                    }
                }

                // 禁用
                if(channel.getCfg().getDisabled()){
                    iterator.remove();
                    continue;
                }
            }catch (Exception e){
                EmailUtil.getInstance().reportException(e);
                iterator.remove();
            }
        }

        // 排序
        Collections.sort(list, (Comparator<T>) (o1, o2) -> {
            if(o1.getCfg() == null || o1.getCfg().getOrder() == null){
                return 1;
            }
            if(o2.getCfg() == null || o2.getCfg().getOrder() == null){
                return -1;
            }
            return o1.getCfg().getOrder() - o2.getCfg().getOrder();
        });

        return list;
    }

    public <T extends ChannelInterface> T getChannel(Channel channel){
        Iterator<ChannelInterface> iterator = list.iterator();
        while (iterator.hasNext()){
            T c = (T) iterator.next();
            if(c.getChannel().equals(channel)){
                return c;
            }
        }
        return null;
    }

    /**
     * 根据业务类型快速选择一条通道
     * @param businessType
     * @param <T>
     * @return
     */
    public static final <T extends ChannelInterface> T getChannel(BusinessType businessType){
        List<T> list = new ChannelRouter().filterBusinessType(businessType).getList();
        if(list.size() < 1){
            return null;
        }
        return list.get(0);
    }
}
