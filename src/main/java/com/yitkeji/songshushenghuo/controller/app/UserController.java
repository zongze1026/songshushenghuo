package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.channel.YoudunChannel;
import com.yitkeji.channel.inter.SmsChannel;
import com.yitkeji.songshushenghuo.service.CardService;
import com.yitkeji.songshushenghuo.service.CommissionService;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.util.*;
import com.yitkeji.songshushenghuo.vo.enums.*;
import com.yitkeji.songshushenghuo.vo.model.Card;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.BaseReq;
import com.yitkeji.songshushenghuo.vo.req.app.*;
import com.yitkeji.songshushenghuo.vo.res.app.UserInviteFriendsRes;
import com.yitkeji.songshushenghuo.vo.res.app.UserRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;


@Api(tags = {"用户相关接口"})
@RestController
public class UserController  extends BaseAppController {

    @Autowired
    private UserService userService;

    @Autowired
    private CardService cardService;

    @Autowired
    private CommissionService commissionService;


    /**
     * 注册
     * @param req
     * @return
     */
    @ApiOperation(value = "注册")
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public Result<Boolean> regist(@RequestBody @Valid UserRegisterReq req) {

        if(!SmsChannel.checkCode(req.getPhone(), SmsType.REGIST, req.getCode())) {
            return Result.fail("校验码错误");
        }

        // 验证是否已注册过
        User user = userService.findByUserName(req.getPhone());
        if(user != null){
            return Result.fail("该手机号已注册，请更换");
        }
        user = ObjectUtil.convert(req, User.class);
        user.setUserName(req.getPhone());
        user.setPhone(req.getPhone());
        user.setPassword(User.encryptionPwd(user.getUserName(), req.getPassword()));
        user.setAuthStatus(AuthStatus.NORMAL.getCode());
        user.setVip(Vip.VIP1.getLevel());
        user.setStatus(UserStatus.NORMAL);
        user.setMoney(0);
        user.setCreateTime(new Date());

        // 关联上级
        if(!StringUtils.isEmpty(req.getReferralCode())){
            User referral = userService.findByReferralCode(req.getReferralCode());
            if(referral == null){
                return Result.fail("请输入正确的邀请码");
            }
            user.setReferenceId(referral.getUserId());
            if (referral.getVip() > Vip.VIP2.getLevel()){ user.setAgentId(referral.getUserId()); }
            else{ user.setAgentId(referral.getAgentId()); }
        }
        userService.add(user);
        user.setReferralCode(User.buildReferralCode(user.getUserId()));
        userService.updateByPrimaryKey(user);
        return Result.success();

    }

    /**
     * 登陆
     * @param req
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    public Result<UserRes> login(@RequestBody @Valid UserLoginReq req, HttpServletRequest request) {
        User user = userService.findByUserName(req.getUserName());
        if(user == null){
            return Result.fail("该手机号未注册，请注册");
        }
        if(!UserStatus.NORMAL.equals(user.getStatus())){
            return Result.fail("当前账户已被禁用，请联系客服");
        }
        String encryptionedPassword = User.encryptionPwd(req.getUserName(), req.getPassword());
        if(!user.getPassword().equals(encryptionedPassword)){
            return Result.fail("密码错误，请重新输入");
        }
        removeCacheUser(user.getToken());
        user.setLastLoginIp(BaseReq.getRealIp(request));
        user.setLastLoginTime(new Date());
        user.resetToken();
        userService.updateByPrimaryKey(user,"lastLoginIp", "lastLoginTime", "token");
        request.setAttribute("user", user);
        return info(request);
    }

    /**
     * 获取用户信息
     * @return
     */
    @ApiOperation(value = "获取用户信息")
    @RequestMapping(value = "/user/get", method = RequestMethod.GET)
    public Result<UserRes> info(HttpServletRequest request) {
        User user = getCacheUser(request);

        // TODO 优化查询语句
        UserRes userRes = userService.findUserInfo(user.getUserId());

        // 用户是否已绑过信用卡
        if(user.getIdcard() != null){
            QueryBuilder countCard = new QueryBuilder(Card.class);
            countCard.setEQQueryCond("idcard", user.getIdcard());
            countCard.setEQQueryCond("type", CardType.CREDIT.getCode());
            int cardCount = cardService.count(countCard);
            if (cardCount > 0){ userRes.setBindCard(true); }
        }

        Map<String, Object> mapAll = commissionService.sumUserCommission(user.getUserId());
        Map<String, Object> mapToday = commissionService.sumUserCommissionToday(user.getUserId());
        if (mapAll != null){
            BigDecimal sumCommissionAll = (BigDecimal)mapAll.get("sum");
            userRes.setSumCommissionAll(sumCommissionAll.intValue());
        }
        if (mapToday != null){
            BigDecimal sumCommissionToday = (BigDecimal)mapToday.get("sum");
            userRes.setSumCommissionToday(sumCommissionToday.intValue());
        }

        List<Map<String, Object>> countFriends = userService.countFriends(user.getUserId());
        for(Map<String, Object> map: countFriends){
            int authStatus = (int)map.get("authStatus");
            long count = (long)map.get("count");
            userRes.setCdirectFriends(userRes.getCdirectFriends() + count);
            if(authStatus == AuthStatus.SUCCESS.getCode()){
                userRes.setCdirectFriendsAuthed(userRes.getCdirectFriendsAuthed() + count);
            }
        }
        return Result.success(userRes);
    }

    /**
     * 账号退出
     * @return
     */
    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/user/exit", method = RequestMethod.GET)
    public Result exit(HttpServletRequest request){
        User user = getCacheUser(request);
        removeCacheUser(user.getToken());
        return Result.success();
    }

    /**
     * 找回密码
     * @param req
     * @return
     */
    @ApiOperation(value = "找回密码")
    @RequestMapping(value = "/user/foundpwd", method = RequestMethod.POST)
    public Result<UserRes> foundPwd(@RequestBody  @Valid UserFoundPwdReq req, HttpServletRequest request) {
        if(!SmsChannel.checkCode(req.getPhone(), SmsType.FOUNDPWD, req.getCode())){
            return Result.fail("校验码错误");
        }

        User user = userService.findByPhone(req.getPhone());
        if(user == null){
            return Result.fail("该手机号未注册，请注册");
        }

        user.setPassword(User.encryptionPwd(user.getUserName(), req.getPassword()));
        user.resetToken().resetAgentToken();
        userService.updateByPrimaryKey(user, "password", "token", "agentToken");
        return Result.success();
    }

    /**
     * 修改登录密码
     * @return
     */
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/user/editpwd", method = RequestMethod.POST)
    public Result editpwd(@RequestBody  @Valid UserModifyPwdReq req, HttpServletRequest request) {
        User user = getCacheUser(request);
        if(!user.getPassword().equals(User.encryptionPwd(user.getUserName(), req.getOldPwd()))){
            return Result.fail("原密码错误");
        }
        if(user.getPassword().equals(User.encryptionPwd(user.getUserName(), req.getPassword()))){
            return Result.fail("新密码不能和旧密码相同");
        }
        user.setPassword(User.encryptionPwd(user.getUserName(), req.getPassword()));
        int rows = userService.updateByPrimaryKey(user, "password");
        return Result.auto(rows > 0, "修改失败");
    }

    /**
     * 实名认证
     * @param req
     * @return
     */
    @ApiOperation(value = "实名认证")
    @RequestMapping(value = "/user/auth", method = RequestMethod.POST)
    public Result auth(@RequestBody UserAuthReq req, HttpServletRequest request) {
        User user = getCacheUser(request);

        if(user.getAuthStatus() == AuthStatus.PASS.getCode()){
            return Result.fail("该用户已认证");
        }

        Object result = YoudunChannel.getInstance().youdunVerify(user, req.getPartnerOrderId());
        if (result instanceof String){
            return Result.fail((String)result);
        }
        user = (User)result;
        user.setAuthStatus(AuthStatus.PASS.getCode());
        userService.updateByPrimaryKey(user, "idcard", "realName", "authStatus");
        return Result.success(ObjectUtil.convert(user, UserRes.class));
    }

    /**
     * 获取下级列表
     */
    @ApiOperation(value = "获取下级列表")
    @RequestMapping(value = "/user/friends", method = RequestMethod.GET)
    public Result<Page<UserRes>> friends( BaseAppPageReq req, HttpServletRequest request) {
        Page page = initPage(ObjectUtil.objectToMap(req));
        User user = getCacheUser(request);
        QueryBuilder queryUser = new QueryBuilder(User.class);
        queryUser.setEQQueryCond("agentId", user.getUserId());
        queryUser.addOrder(queryUser.getPrimaryKey(), false);
        page = userService.findListByPage(page, queryUser, UserRes.class);
        return Result.success(page);
    }

    /**
     * 邀請好友列表
     */
    @ApiOperation(value = "获取邀請好友列表")
    @RequestMapping(value = "/user/invite/friends", method = RequestMethod.GET)
    public Result<Page<UserInviteFriendsRes>> userInviteFriends(UserInviteFriendsReq req, HttpServletRequest request) {
        Page page = initPage(ObjectUtil.objectToMap(req));
        User user = getCacheUser(request);

        QueryBuilder queryUser = new QueryBuilder(User.class);
        if (req.getAuthStatus() != null && req.getAuthStatus().equals(AuthStatus.SUCCESS)){
            queryUser.setEQQueryCond("authStatus", AuthStatus.SUCCESS.getCode());
        }
        queryUser.setEQQueryCond("referenceId", user.getUserId());
        queryUser.addOrder(queryUser.getPrimaryKey(), false);
        page = userService.findListByPage(page, queryUser, UserInviteFriendsRes.class);
        return Result.success(page);
    }

    @ApiOperation(value = "获取图形验证码")
    @RequestMapping(value = "/user/imgcode", method = RequestMethod.GET)
    public byte[] getByte(String codeId) {
        Object[] objs = VerifyUtil.createImage();
        String randomStr = (String) objs[0];
        RedisUtil.expired(CacheKey.IMGCODE.getKey(codeId));
        RedisUtil.set(CacheKey.IMGCODE.getKey(codeId), randomStr);
        return (byte[]) objs[1];
    }

    // TODO 图形验证码无效。前端可轻易绕过
    @ApiOperation(value = "验证图形验证码")
    @RequestMapping(value = "/user/verify", method = RequestMethod.GET)
    public Result<String> sendCaptcha(String codeId, String imgCode) {

        if (StringUtils.isEmpty(codeId) || StringUtils.isEmpty(imgCode)) {
            return Result.fail("参数不能为空");
        }
        String code = RedisUtil.get(CacheKey.IMGCODE.getKey(codeId));
        if (imgCode.equalsIgnoreCase(code)) {
            return Result.success();
        } else {
            return Result.fail("输入图片验证码有误");
        }
    }

    @ApiOperation(value = "校验身份证信息")
    @RequestMapping(value = "/user/checkIdcard", method = RequestMethod.POST)
    public Result<String> checkIdcard(@RequestBody @Valid UserCheckIdcardReq req, HttpServletRequest request){
        int count = userService.countByIdcard(req.getIdcard());
        return Result.auto(count < 1,  "该用户已经实名注册");
    }

}
