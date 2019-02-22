package com.yitkeji.songshushenghuo.controller.pc;

import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Bank;
import com.yitkeji.songshushenghuo.vo.cfg.bank.Banks;
import com.yitkeji.songshushenghuo.vo.res.app.BankListRes;
import com.yitkeji.songshushenghuo.vo.res.pc.AppInfoRes;
import com.yitkeji.songshushenghuo.vo.res.pc.SwitcherCfgRes;
import com.yitkeji.songshushenghuo.vo.res.pc.SystemInfoRes;
import com.yitkeji.songshushenghuo.vo.res.pc.VipCfgRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Api(tags = {"系统配置"})
@CrossOrigin
@RestController
@RequestMapping("/system")
public class SystemController {

    @ApiOperation(value = "获取所有系统配置")
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public Result<SystemInfoRes> info() {
        return Result.success(new SystemInfoRes());
    }


    @ApiOperation(value = "获取APP配置")
    @RequestMapping(value = "/appinfo", method = RequestMethod.GET)
    public Result<AppInfoRes> appinfo(){
        return Result.success(new SystemInfoRes().getAppinfo());
    }


    @ApiOperation(value = "获取系统开关配置")
    @RequestMapping(value = "/switcher", method = RequestMethod.GET)
    public Result<SwitcherCfgRes> switcher(){
        return Result.success(new SystemInfoRes().getSwitcher());
    }


    @ApiOperation(value = "获取支持的信用卡列表")
    @RequestMapping(value = "/creditBanks", method = RequestMethod.GET)
    public Result<Collection<Bank>> banks(){
        return Result.success(SystemCfg.getInstance().getBanks().values());
    }

    @ApiOperation(value = "获取支持的储蓄卡列表")
    @RequestMapping(value = "/debitBanks", method = RequestMethod.GET)
    public Result<Collection<BankListRes>> debitBanks(){
        Banks debitBank = SystemCfg.getInstance().getDebitbanks();
        Map<String, BankListRes> temMap = new HashMap<>();

        for(String str: debitBank.keySet()){
            Bank bank = debitBank.get(str);
            temMap.put(bank.getBankName(), ObjectUtil.convert(bank, BankListRes.class));
        }
        return Result.success(temMap.values());
    }


    @ApiOperation(value = "获取vip配置")
    @RequestMapping(value = "/vip", method = RequestMethod.GET)
    public Result<VipCfgRes> vip(){
        return Result.success(new SystemInfoRes().getVip());
    }

}
