package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.RoleEnum;
import com.yitkeji.songshushenghuo.vo.enums.Vip;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.admin.UserUpdateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = {"用户管理"})
@RestController
public class AdminUserController extends BaseAdminController{

    @Autowired
    private UserService userService;

    @ApiOperation(value = "升级VIP")
    @RequestMapping(value = "/user/upvip", method = RequestMethod.POST)
    @Transactional
    public Result upvip(@RequestBody UserUpdateReq userUpdateReq) {
        User user = userService.findByPrimaryKey(userUpdateReq.getUserId());
        BeanUtils.copyProperties(userUpdateReq, user);

        if (user.getVip() > Vip.VIP2.getLevel()){
            user.setAgentId(null);
            user.setRoleId((long) RoleEnum.AGENT.getCode());
        }
        int num = userService.updateByPrimaryKey(user, "vip","agentId","roleId");
        return Result.auto(num > 0, "更新失败");
    }

    /**
     * 修改用户信息
     * @return
     */
    @ApiOperation(value = "更新")
    @RequestMapping(value = "/user/update", method = RequestMethod.POST)
    @Transactional
    public Result update(@RequestBody UserUpdateReq userUpdateReq, HttpServletRequest request) {
        User user = userService.findByPrimaryKey(userUpdateReq.getUserId());
        BeanUtils.copyProperties(userUpdateReq, user);
        user.setLastupdatetime(new Date());
        int num = userService.updateByPrimaryKey(user, "referenceId","agentId","status","lastupdatetime");
        return Result.auto(num > 0, "更新失败");
    }
}
