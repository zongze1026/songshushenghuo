package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.service.AdminService;
import com.yitkeji.songshushenghuo.service.RoleService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.util.ZGLTool;
import com.yitkeji.songshushenghuo.vo.enums.AdminStatus;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.enums.RoleStatus;
import com.yitkeji.songshushenghuo.vo.model.Admin;
import com.yitkeji.songshushenghuo.vo.model.Role;
import com.yitkeji.songshushenghuo.vo.req.BaseReq;
import com.yitkeji.songshushenghuo.vo.req.admin.AdminLoginReq;
import com.yitkeji.songshushenghuo.vo.req.admin.AdminModifyReq;
import com.yitkeji.songshushenghuo.vo.res.admin.AdminRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Api(tags = {"管理员管理"})
@RestController
public class AdminAdminController extends BaseAdminController{
    private Logger logger = Logger.getLogger(AdminAdminController.class);


    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    /**
     * 登录
     * @param adminLoginReq
     * @return
     */
    @ApiOperation(value = "登录")
    @RequestMapping(value = "/admin/login", method = RequestMethod.POST)
    public Result<AdminRes> login(@RequestBody AdminLoginReq adminLoginReq, HttpServletRequest request){
        QueryBuilder queryBuilder = new QueryBuilder(Admin.class);
        queryBuilder.setEQQueryCond("userName", adminLoginReq.getUserName());
        queryBuilder.setEQQueryCond("password", Admin.encryptionPwd(adminLoginReq.getUserName(), adminLoginReq.getPassword()));
        Admin admin = adminService.findOne(queryBuilder);
        if(null == admin){
            return Result.fail("用户名或密码错误");
        }

        if(!AdminStatus.NORMAL.equals(admin.getStatus())){
            switch (admin.getStatus()){
                case DISABLED: return Result.fail("当前账户被禁用，请联系管理员");
                case DELETED: return Result.fail("当前账户已删除，请联系管理员");
                default:
            }
        }
        Role role = roleService.findByPrimaryKey(admin.getRoleId());
        if(!RoleStatus.NORMAL.equals(role.getStatus())){
            return Result.fail("当前角色被禁用，请联系管理员");
        }

        if(!RoleStatus.NORMAL.equals(role.getStatus())){
            removeCacheAdmin(admin.getToken());
        }
        admin.setToken(ZGLTool.getUUID());
        admin.setLoginCount(admin.getLoginCount() + 1);
        admin.setLoginTime(new Date());
        admin.setLoginIp(BaseReq.getRealIp(request));
        adminService.updateByPrimaryKey(admin, "token", "loginCount", "loginTime", "loginIp");
        return Result.success(ObjectUtil.convert(admin, AdminRes.class));
    }

    public void removeCacheAdmin(String token){
        redisTemplate.opsForValue().set(String.format(CacheKey.ADMIN_TOKEN.getKey(token)), null, 1, TimeUnit.MILLISECONDS);
    }

    /**
     * 添加管理员
     * @param adminModifyReq
     * @return
     */
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    public Result add(@RequestBody AdminModifyReq adminModifyReq, HttpServletRequest request) {
        if (null != adminModifyReq.getUserName() && null != adminModifyReq.getPassword()) {
            QueryBuilder queryBuilder = new QueryBuilder(Admin.class);
            queryBuilder.setEQQueryCond("userName", adminModifyReq.getUserName());
            Admin admin = adminService.findOne(queryBuilder);
            if (admin != null) {
                return Result.fail("账号已存在");
            }
            admin = new Admin();
            BeanUtils.copyProperties(adminModifyReq, admin);
            admin.setPassword(Admin.encryptionPwd(admin.getUserName(), admin.getPassword()));
            admin.setStatus(AdminStatus.NORMAL);
            admin.setCreateTime(new Date());
            int num = adminService.add(admin);
            return num > 0 ? Result.success(admin) : Result.fail("添加失败");
        } else {
            return Result.fail("用户名或密码不能为空");
        }
    }

    /**
     * 更新管理员信息
     * @param adminModifyReq
     * @return
     */
    @ApiOperation(value = "更新")
    @RequestMapping(value = "/admin/update", method = RequestMethod.POST)
    public Result update(@RequestBody AdminModifyReq adminModifyReq, HttpServletRequest request) {
        Admin admin = new Admin();
        if(adminModifyReq.getAdminId() != null){
            admin = adminService.findByPrimaryKey(adminModifyReq.getAdminId());
        }
        BeanUtils.copyProperties(adminModifyReq, admin);
        if(!StringUtils.isEmpty(adminModifyReq.getNewPassword())){
            admin.setPassword(Admin.encryptionPwd(admin.getUserName(), adminModifyReq.getNewPassword()));
        }
        int rows = adminService.saveOrUpdate(admin);
        return Result.auto(rows > 0, "更新失败");
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @ApiOperation(value = "退出登录")
    @RequestMapping(value = "/admin/exit", method = RequestMethod.POST)
    public Result exit(HttpServletRequest request){
        Admin admin = getCacheAdmin(request);
        redisTemplate.opsForValue().set(CacheKey.ADMIN_TOKEN.getKey(admin.getAdminId()), null, 1, TimeUnit.MILLISECONDS);
        return Result.success();
    }

    /**
     * 详情
     * @return
     */
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/admin/bsinfo", method = RequestMethod.GET)
    public Result bsinfo(HttpServletRequest request){
        return Result.success(ObjectUtil.convert(getCacheAdmin(request), AdminRes.class));
    }

    /**
     * 修改密码
     * @return
     */
    @ApiOperation(value = "修改密码")
    @RequestMapping(value = "/admin/bsupdate", method = RequestMethod.POST)
    public Result bsupdate(@RequestBody AdminModifyReq adminModifyReq, HttpServletRequest request){
        Admin admin = getCacheAdmin(request);
        if (adminModifyReq == null || adminModifyReq.getPassword() == null || adminModifyReq.getNewPassword() == null){
            return Result.fail("参数不能为空");
        }
        if(!Admin.encryptionPwd(admin.getUserName(), adminModifyReq.getPassword()).equals(admin.getPassword())){
            return Result.fail("原密码不正确");
        }
        if(admin.getPassword().equals(Admin.encryptionPwd(admin.getUserName(), adminModifyReq.getNewPassword()))){
            return Result.fail("新密码不能和旧密码相同");
        }
        admin.setPassword(Admin.encryptionPwd(admin.getUserName(), adminModifyReq.getNewPassword()));
        int rows = adminService.updateByPrimaryKey(admin, "password");
        return Result.auto(rows > 0, "更新失败");
    }
}
