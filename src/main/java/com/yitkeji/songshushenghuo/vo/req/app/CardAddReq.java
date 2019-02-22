package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.validation.BankCardNumber;
import com.yitkeji.songshushenghuo.validation.CreditCardCvv;
import com.yitkeji.songshushenghuo.validation.CreditCardExpiredDay;
import com.yitkeji.songshushenghuo.validation.Phone;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@ApiModel
public class CardAddReq extends BaseAppReq {

    @ApiModelProperty("银行卡号")
    private String cardNo;

    @ApiModelProperty("预留手机号")
    private String phone;

    @ApiModelProperty("有效期")
    private String expiryDate;

    @ApiModelProperty("cvv码")
    private String cvv;

    @ApiModelProperty("银行卡类型（1信用卡，0储蓄卡）")
    private Integer type;

    @BankCardNumber
    public String getCardNo() {
        return cardNo;
    }

    @Phone
    public String getPhone() {
        return phone;
    }

    @CreditCardExpiredDay(allowEmpty = true)
    public String getExpiryDate() {
        return expiryDate;
    }

    @CreditCardCvv(allowEmpty = true)
    public String getCvv() {
        return cvv;
    }


}
