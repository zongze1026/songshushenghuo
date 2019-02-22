package com.yitkeji.songshushenghuo.vo.req.app;

import com.yitkeji.songshushenghuo.vo.enums.Channel;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ApiModel
public class PlanPreviewReq extends BaseAppReq {

    @ApiModelProperty("通道")
    private Channel channel;

    @ApiModelProperty("地区代码")
    private Integer areaCode;

    @ApiModelProperty("银行卡ID")
    private Long cardId;

    @ApiModelProperty("短验请求编码")
    private String requestNo;

    @ApiModelProperty("短信验证码")
    private String code;

    @ApiModelProperty("还款金额")
    private double planMoney;

    @ApiModelProperty("每天还款次数")
    private Integer dayPlanNum;

    @ApiModelProperty("选中的日期")
    private List<String> planDays;

    @ApiModelProperty("选中的日期;示例：2018-11-21,2018-11-22")
    private String planDaysStr;


    /**
     * IOS专用的方法（因无法传送JSON数组格式的planDays数据。）
     * @param days
     */
    public void setPlanDaysStr(String days){
        this.planDays = new ArrayList<>();
        String[] temArr = days.split(",");
        for(int i=0; i<temArr.length; i++){
            this.planDays.add(temArr[i]);
        }
    }

}
