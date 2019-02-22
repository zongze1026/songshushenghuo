package com.yitkeji.songshushenghuo.controller.agent;

import com.yitkeji.channel.util.SnowflakeIdWorker;
import com.yitkeji.songshushenghuo.service.RoleService;
import com.yitkeji.songshushenghuo.service.UserService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.RoleStatus;
import com.yitkeji.songshushenghuo.vo.enums.UserStatus;
import com.yitkeji.songshushenghuo.vo.enums.Vip;
import com.yitkeji.songshushenghuo.vo.model.Admin;
import com.yitkeji.songshushenghuo.vo.model.Role;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.agent.AgentLoginReq;
import com.yitkeji.songshushenghuo.vo.req.agent.AgentModifyReq;
import com.yitkeji.songshushenghuo.vo.res.agent.UserRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Api(tags = {"代理商管理"})
@RestController
public class AgentAgentController extends BaseAgentController{

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    /**
     * 登录
     * @param req
     * @param request
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/agent/login", method = RequestMethod.POST)
    public Result login(@RequestBody AgentLoginReq req, HttpServletRequest request){
        QueryBuilder queryBuilder = new QueryBuilder(User.class);
        queryBuilder.setEQQueryCond("userName", req.getUserName());
        queryBuilder.setEQQueryCond("password", User.encryptionPwd(req.getUserName(), req.getPassword()));
        User agent = userService.findOne(queryBuilder);

        if(null == agent){
            return Result.fail("用户名或密码错误");
        }

        if (agent != null && agent.getVip() <= Vip.VIP2.getLevel()){
            return Result.fail("无访问权限，请升级至vip以上");
        }

        if(!UserStatus.NORMAL.equals(agent.getStatus())){
            switch (agent.getStatus()){
                case DISABLED: return Result.fail("当前账户被禁用，请联系管理员");
                case DELETED: return Result.fail("当前账户已被删除，请联系管理员");
                default:
            }
        }
        Role role = roleService.findByPrimaryKey(agent.getRoleId());
        if(!RoleStatus.NORMAL.equals(role.getStatus())){
            return Result.fail("当前角色被禁用，请联系管理员");
        }

        if(SystemCfg.getInstance().getAdmin().getSyncLogin()){
            RedisUtil.expired(CacheKey.AGENT_TOKEN.getKey(agent.getAgentToken()));
        }
        agent.setAgentToken(SnowflakeIdWorker.getId(0, 0));
        userService.updateByPrimaryKey(agent, "agentToken");
        return Result.success(ObjectUtil.convert(agent, UserRes.class));
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/agent/exit", method = RequestMethod.POST)
    public Result exit(HttpServletRequest request){
        User agent = getCacheAgent(request);
        RedisUtil.expired(CacheKey.AGENT_TOKEN.getKey(agent.getAgentToken()));
        return Result.success();
    }


    /**
     * 详情
     * @return
     */
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/agent/bsinfo", method = RequestMethod.GET)
    public Result bsinfo(HttpServletRequest request){
        return Result.success(ObjectUtil.convert(getCacheAgent(request), UserRes.class));
    }

    /**
     * 修改密码
     * @return
     */
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/agent/bsupdate", method = RequestMethod.POST)
    public Result bsupdate(@RequestBody AgentModifyReq agentModifyReq, HttpServletRequest request){
        User agent = getCacheAgent(request);

        if (agentModifyReq == null || agentModifyReq.getPassword() == null || agentModifyReq.getNewPassword() == null){
            return Result.fail("参数不能为空");
        }
        if(!User.encryptionPwd(agent.getUserName(), agentModifyReq.getPassword()).equals(agent.getPassword())){
            return Result.fail("原密码不正确");
        }
        if(agent.getPassword().equals(Admin.encryptionPwd(agent.getUserName(), agentModifyReq.getNewPassword()))){
            return Result.fail("新密码不能和旧密码相同");
        }
        agent.setPassword(User.encryptionPwd(agent.getUserName(), agentModifyReq.getNewPassword()));
        int rows = userService.updateByPrimaryKey(agent, "password");
        return Result.auto(rows > 0, "更新失败");
    }
}
