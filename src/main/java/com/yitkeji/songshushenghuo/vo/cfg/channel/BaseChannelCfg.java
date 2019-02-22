package com.yitkeji.songshushenghuo.vo.cfg.channel;

import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BaseChannelCfg {
    private Boolean disabled;

    private BusinessType businessType;

    private Integer minMoney;

    private Integer maxMoney;

    private Integer order;

    private String banks;
}
