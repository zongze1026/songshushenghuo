package com.yitkeji.songshushenghuo.controller.agent;

import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.service.inter.GlobalService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.CondReq;
import com.yitkeji.songshushenghuo.vo.req.PageReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用列表详情接口
 */
@Api(tags = {"通用接口"})
@RestController
public class AgentMainController extends BaseAgentController {

    /**
     * 列表
     * @param module
     * @param pageReq
     * @return
     */
    @ApiOperation(value = "列表")
    @RequestMapping(value = "/{module}/list", method = RequestMethod.GET)
    public Result<Page> findList(@PathVariable("module") String module, PageReq pageReq, HttpServletRequest request) throws BaseException {
        Page page = new Page(pageReq);
        User agent = getCacheAgent(request);
        QueryBuilder queryBuilder = initQueryBuilder(module, pageReq);
        String simpleName = queryBuilder.getBoClass().getSimpleName();

        if(ObjectUtil.getField(queryBuilder.getBoClass(), "agentId") != null){
            queryBuilder.setEQQueryCond("agentId", agent.getUserId());
        }else if(ObjectUtil.getField(queryBuilder.getBoClass(), "targetUserId") != null){
            queryBuilder.setEQQueryCond("targetUserId", agent.getUserId());
        }else if(ObjectUtil.getField(queryBuilder.getBoClass(), "userId") != null){
            QueryBuilder queryBuilder1 = new QueryBuilder(User.class);
            queryBuilder1.setAliasName("a1");
            queryBuilder1.setEQQueryCond("a1.userId", simpleName + ".userId");
            queryBuilder1.setEQQueryCond("a1.agentId", agent.getUserId());
            queryBuilder.innerJoin(queryBuilder1);
        }
        queryBuilder.addOrder(queryBuilder.getPrimaryKey(), false);
        try{
            addJoinColumns(queryBuilder);
            GlobalService service = getGlobalService(module);
            Class resClass = getResClass(queryBuilder.getBoClass());
            if(resClass != null){
                page = service.findListByPage(page, queryBuilder, resClass);
            }else{
                page = service.findListByPage(page, queryBuilder);
            }
        }catch (Exception e){
            Result.fail("查询失败");
        }
        return Result.success(filterRes(page, request, false));
    }

    /**
     * 详情
     * @param module
     * @param condReq
     * @return
     */
    @ApiOperation(value = "详情")
    @RequestMapping(value = "/{module}/info", method = RequestMethod.GET)
    public Result findOne(@PathVariable("module") String module, CondReq condReq, HttpServletRequest request) throws BaseException {
        if(condReq.getLocalCond().size() < 1){
            return Result.success("参数不能为空");
        }
        User agent = getCacheAgent(request);
        QueryBuilder queryBuilder = initQueryBuilder(module, condReq);
        if(ObjectUtil.getField(queryBuilder.getBoClass(), "agentId") != null){
            queryBuilder.setEQQueryCond("agentId", agent.getUserId());
        }
        addJoinColumns(queryBuilder);
        Class resClass = getResClass(queryBuilder.getBoClass());

        Object object;
        if(resClass == null){
            object = getGlobalService(module).findOne(queryBuilder);
        }else{
            object = getGlobalService(module).findOne(queryBuilder, resClass);
        }
        return Result.success(filterRes(object, request, false));
    }
}
