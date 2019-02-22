package com.yitkeji.songshushenghuo.vo.cfg.vip;

import com.yitkeji.songshushenghuo.vo.enums.Vip;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

@Setter
@Getter
@ApiModel
public class VipCfg {


    @ApiModelProperty("VIP1")
    private VipElement vip1;

    @ApiModelProperty("VIP2")
    private VipElement vip2;

    @ApiModelProperty("VIP3")
    private VipElement vip3;

    @ApiModelProperty("VIP4")
    private VipElement vip4;


    public void setVip1(VipElement vip1) {
        this.vip1 = vip1;
        BeanUtils.copyProperties(vip1, Vip.VIP1);
    }

    public void setVip2(VipElement vip2) {
        this.vip2 = vip2;
        BeanUtils.copyProperties(vip2, Vip.VIP2);
    }

    public void setVip3(VipElement vip3) {
        this.vip3 = vip3;
        BeanUtils.copyProperties(vip3, Vip.VIP3);
    }

    public void setVip4(VipElement vip4) {
        this.vip4 = vip4;
        BeanUtils.copyProperties(vip4, Vip.VIP4);
    }


}
