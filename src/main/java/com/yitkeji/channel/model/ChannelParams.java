package com.yitkeji.channel.model;

import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.User;
import lombok.Getter;
import lombok.Setter;

/**
 * 用来调用通道时传递参数
 */
@Setter
@Getter
public class ChannelParams {
    private String requestNo;
    private String code;
    private Card card;
    private User user;
    private Card debitCard;
    private Integer money;
    private Long orderId;
    private Integer areaCode;
    private String city;
    private String ip;
    private boolean isDefault = false;


    public void resetPosition(){
        this.ip = "115.236.54.154";
        this.city = "杭州市";
        isDefault = true;
    }
}
