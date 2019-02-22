package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.TbSystemService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.model.TbSystem;
import com.yitkeji.songshushenghuo.vo.req.CondReq;
import com.yitkeji.songshushenghuo.vo.req.PageReq;
import com.yitkeji.songshushenghuo.vo.req.admin.SystemUpdateReq;
import com.yitkeji.songshushenghuo.vo.res.pc.SwitcherCfgRes;
import com.yitkeji.songshushenghuo.vo.res.pc.SystemInfoRes;
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

@Api(tags = {"系统配置"})
@RestController
public class AdminSystemController extends BaseAdminController{

    @Autowired
    private TbSystemService systemService;

    @ApiOperation(value = "获取系统配置")
    @RequestMapping(value = "/system/info", method = RequestMethod.GET)
    public Result<SystemInfoRes> info() {
        SystemInfoRes res = new SystemInfoRes();
        BeanUtils.copyProperties(SystemCfg.getInstance(), res);
        res.setSwitcher(ObjectUtil.convert(SystemCfg.getInstance().getSwitcher(), SwitcherCfgRes.class));
        return Result.success(res);
    }

    @ApiOperation(value = "获取系统配置详情")
    @RequestMapping(value = "/system/detail", method = RequestMethod.GET)
    public Result detail(CondReq condReq) throws BaseException {
        QueryBuilder queryBuilder = initQueryBuilder("tbSystem", condReq);
        queryBuilder.addOrder(queryBuilder.getPrimaryKey(), false);
        TbSystem system = systemService.findOne(queryBuilder);
        return Result.success(system);
    }

    @ApiOperation(value = "获取系统配置列表")
    @RequestMapping(value = "/system/list", method = RequestMethod.GET)
    public Result list(PageReq pageReq, HttpServletRequest request) throws BaseException {
        Page page = new Page(pageReq);
        QueryBuilder queryBuilder = initQueryBuilder("tbSystem", pageReq);
        queryBuilder.setNotInQueryCond("readOnly",0);
        queryBuilder.addOrder(queryBuilder.getPrimaryKey(), false);
        page = systemService.findListByPage(page, queryBuilder);
        return Result.success(filterRes(page,request, false));
    }

    @ApiOperation(value = "修改系统配置")
    @RequestMapping(value = "/system/update", method = RequestMethod.POST)
    public Result update(@RequestBody SystemUpdateReq systemUpdateReq){
        TbSystem system = systemService.findByPrimaryKey(systemUpdateReq.getSystemId());
        BeanUtils.copyProperties(systemUpdateReq, system);
        system.setLastupdatetime(new Date());
        int num = systemService.updateByPrimaryKey(system, "value","type");
        // 刷新配置
        SystemCfg.getInstance().freshConfig();
        return Result.auto(num > 0, "更新失败");
    }

}
