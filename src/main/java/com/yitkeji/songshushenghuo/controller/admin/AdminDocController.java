package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.service.DocService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.model.Doc;
import com.yitkeji.songshushenghuo.vo.req.admin.DocReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Api(tags = {"文章管理"})
@RestController
public class AdminDocController extends BaseAdminController{

    @Autowired
    private DocService docService;

    /**
     * 修改文章
     * @return
     */
    @ApiOperation(value = "更新文档")
    @RequestMapping(value = "/doc/update", method = RequestMethod.POST)
    public Result update(@RequestBody DocReq docReq) {
        Doc doc = ObjectUtil.convert(docReq, Doc.class);
        doc.setLastupdatetime(new Date());
        int row = docService.saveOrUpdate(doc);
        return Result.auto(row > 0, "更新失败");
    }
}
