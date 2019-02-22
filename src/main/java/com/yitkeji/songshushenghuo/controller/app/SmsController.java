package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.channel.inter.SmsChannel;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.util.ChannelRouter;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.RegexUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.BusinessType;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.SmsType;
import com.yitkeji.songshushenghuo.vo.req.app.SmsReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@Api(tags = {"短信接口"})
@RestController
public class SmsController extends BaseAppController {

    @Autowired
    private UserService userService;

    @ApiOperation(value = "发送短信验证码")
    @RequestMapping(value = "/sms/code", method = RequestMethod.POST)
    public Result sendCodeSms(@RequestBody SmsReq smsReq) {

        SmsType smsType = SmsType.getType(smsReq.getType());
        if (smsType == null) {
            return Result.fail("无当前短信类型");
        }
        if(!verify(smsReq.getCodeId(),smsReq.getImgCode())){
            return Result.fail("验证码错误");
        }
        switch (smsType){
            case REGIST:
                if(userService.checkUserName(smsReq.getPhone())){
                    return Result.fail("该手机号已注册，请更换");
                }
                if (StringUtils.isEmpty(smsReq.getCodeId()) ||StringUtils.isEmpty(smsReq.getImgCode())) {
                    return Result.fail("参数不能为空");
                }
                break;
            case FOUNDPWD:
                if(!userService.checkUserName(smsReq.getPhone())){
                    return Result.fail("该手机号未注册，请注册");
                }
                break;
            default:
        }

        SmsChannel smsChannel = ChannelRouter.getChannel(BusinessType.SMS);
        if(smsChannel == null){
            return Result.fail("发送短信失败");
        }
        smsChannel.sendCodeMsg(smsReq.getPhone(), smsType);
        return Result.success();
    }

    public  Boolean verify(String codeId, String imgCode) {
        String code = RedisUtil.get(CacheKey.IMGCODE.getKey(codeId));
        if (imgCode.equalsIgnoreCase(code)) {
            return true;
        } else {
            return false;
        }
    }
}
