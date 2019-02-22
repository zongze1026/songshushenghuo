package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.songshushenghuo.service.CommissionService;
import com.yitkeji.songshushenghuo.util.*;
import com.yitkeji.songshushenghuo.vo.enums.CommissionFiltrateType;
import com.yitkeji.songshushenghuo.vo.enums.CommissionType;
import com.yitkeji.songshushenghuo.vo.model.Commission;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.app.CommissionInfoReq;
import com.yitkeji.songshushenghuo.vo.req.app.CommissionPageReq;
import com.yitkeji.songshushenghuo.vo.res.app.CommissionRes;
import com.yitkeji.songshushenghuo.vo.res.app.TypeRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Api(tags = {"分润接口"})
@RestController
public class CommissionController extends BaseAppController {

    @Autowired
    private CommissionService commissionService;

    /**
     * 分润列表
     */
    @ApiOperation(value = "获取分润列表")
    @RequestMapping(value = "/commission/list", method = RequestMethod.GET)
    public Result<Page<CommissionRes>> commissionList(CommissionPageReq req, HttpServletRequest request){
        Page page = initPage(req);
        User user = getCacheUser(request);

        QueryBuilder queryCommission = new QueryBuilder(Commission.class);
        queryCommission.setEQQueryCond("targetUserId", user.getUserId());

        if(req.getStartTime() != null && req.getEndTime() != null){
            Date startTime = DateUtil.parse(req.getStartTime(), DateUtil.DATEFORMAT_NINTEEN);
            Date endTime = DateUtil.parse(req.getEndTime(), DateUtil.DATEFORMAT_NINTEEN);
            queryCommission.setBetweenQueryCond("createTime", startTime, endTime);
        }

        if (req.getType() == CommissionFiltrateType.CASH.getCode()){
            queryCommission.setInQueryCond("Commission.type", CommissionType.CASH.getCode());
        }
        else if (req.getType() == CommissionFiltrateType.NOVICE.getCode()){
            queryCommission.setInQueryCond("Commission.type", CommissionType.NOVICE_AUTH.getCode(),CommissionType.NOVICE_CASH.getCode());
        }
        else if (req.getType() == CommissionFiltrateType.REFERRAL.getCode()){
            queryCommission.setInQueryCond("Commission.type", CommissionType.UPVIP.getCode(),CommissionType.REFERRAL_AUTH.getCode(),CommissionType.REFERRAL_CASH.getCode());
        }

        queryCommission.addOrder(queryCommission.getPrimaryKey(), false);
        page = commissionService.findListByPage(page, queryCommission, CommissionRes.class);
        return Result.success(page);
    }

    /**
     * 分润详情
     */
    @ApiOperation(value = "获取分润详情")
    @RequestMapping(value = "/commission/info", method = RequestMethod.GET)
    public Result<CommissionRes> commissionInfo(CommissionInfoReq commissionInfoReq){
        QueryBuilder queryCommission = new QueryBuilder(Commission.class);
        queryCommission.setEQQueryCond("commissionId", commissionInfoReq.getCommissionId());
        CommissionRes commissionRes = commissionService.findOne(queryCommission, CommissionRes.class);
        return Result.success(commissionRes);
    }

    /**
     * 分润类型
     */
    @ApiOperation(value = "获取分润类型")
    @RequestMapping(value = "/commission/type", method = RequestMethod.GET)
    public Result<List<TypeRes>> commissionType(){

        List<TypeRes> typeResList = new ArrayList<>();
        Map<String, Integer> commissionTypeMap = CommissionFiltrateType.getCommissionFiltrateTypeMap();
        Iterator<Map.Entry<String, Integer>> iterator = commissionTypeMap.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Integer> entry = iterator.next();
            TypeRes typeRes = new TypeRes();
            typeRes.setDesc(entry.getKey());
            typeRes.setCode(entry.getValue());
            typeResList.add(typeRes);
        }
        return Result.success(typeResList);
    }
}
