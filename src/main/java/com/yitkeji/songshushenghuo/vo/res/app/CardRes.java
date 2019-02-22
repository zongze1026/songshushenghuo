package com.yitkeji.songshushenghuo.vo.res.app;

import com.yitkeji.songshushenghuo.vo.cfg.bank.Banks;
import com.yitkeji.songshushenghuo.vo.enums.CardType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@ApiModel
public class CardRes{


    @ApiModelProperty("银行卡ID")
    private Long cardId;

    @ApiModelProperty("用户ID")
    private Long userId;

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("身份证号")
    private String idcard;

    @ApiModelProperty("银行名称")
    private String cardBank;

    @ApiModelProperty("银行简码")
    private String bankCode;

    @ApiModelProperty("持卡人姓名")
    private String name;

    @ApiModelProperty("预留手机号")
    private String phone;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("银行卡类型")
    private int type;

    @ApiModelProperty("银行卡状态")
    private int status;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("银行图标地址")
    private String icon;

    @ApiModelProperty("银行卡默认图片")
    private String image;

    @ApiModelProperty("银行图标基色")
    private String color;

    public CardType matchType(){
        for(CardType type: CardType.values()){
            if(type.getCode() == this.type){
                return type;
            }
        }
        return null;
    }

    public String getIcon() {
        return Banks.getByCode(bankCode, matchType()).getIcon();
    }

    public String getImage() {
        return Banks.getByCode(bankCode, matchType()).getImage();
    }

    public String getColor() {
        return Banks.getByCode(bankCode, matchType()).getColor();
    }
}
