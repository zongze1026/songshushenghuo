package com.yitkeji.songshushenghuo.controller.agent;

import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.res.pc.SwitcherCfgRes;
import com.yitkeji.songshushenghuo.vo.res.pc.SystemInfoRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = {"系统配置"})
@RestController
public class AgentSystemController extends BaseAgentController{

    @ApiOperation(value = "获取系统配置")
    @RequestMapping(value = "/system/info", method = RequestMethod.GET)
    public Result<SystemInfoRes> info() {
        SystemInfoRes res = new SystemInfoRes();
        BeanUtils.copyProperties(SystemCfg.getInstance(), res);
        res.setSwitcher(ObjectUtil.convert(SystemCfg.getInstance().getSwitcher(), SwitcherCfgRes.class));
        return Result.success(res);
    }

}
