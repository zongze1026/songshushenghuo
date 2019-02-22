package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.service.RoleService;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.RoleStatus;
import com.yitkeji.songshushenghuo.vo.model.Role;
import com.yitkeji.songshushenghuo.vo.req.admin.RoleAddReq;
import com.yitkeji.songshushenghuo.vo.req.admin.RoleUpdateReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Api(tags = {"角色管理"})
@RestController
public class AdminRoleController extends BaseAdminController {

    @Autowired
    private RoleService roleService;


    /**
     * 添加角色
     */
    @ApiOperation(value = "新增")
    @RequestMapping(value = "/role/add", method = RequestMethod.POST)
    public Result add(@RequestBody RoleAddReq roleAddReq, HttpServletRequest request) {
            Role role = new Role();
            BeanUtils.copyProperties(roleAddReq, role);
            role.setStatus(RoleStatus.NORMAL);
            role.setCreateTime(new Date());
            int rows = roleService.add(role);
            return Result.auto(rows > 0, "添加失败");
    }

    /**
     * 更新角色信息
     */
    @ApiOperation(value = "更新")
    @RequestMapping(value = "/role/update", method = RequestMethod.POST)
    public Result update(@RequestBody RoleUpdateReq roleUpdateReq, HttpServletRequest request) {
        Role role = new Role();
        if(roleUpdateReq.getRoleId() != null){
            role = roleService.findByPrimaryKey(roleUpdateReq.getRoleId());
        }
        BeanUtils.copyProperties(roleUpdateReq, role);
        role.setLastupdatetime(new Date());
        int rows = roleService.updateByPrimaryKey(role);
        return Result.auto(rows > 0, "更新失败");
    }
}
