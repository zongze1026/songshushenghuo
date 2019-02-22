package com.yitkeji.songshushenghuo.controller.app;

import com.alibaba.fastjson.JSONObject;
import com.yitkeji.channel.NewsChannel;
import com.yitkeji.songshushenghuo.service.DocService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.DocStatus;
import com.yitkeji.songshushenghuo.vo.enums.DocType;
import com.yitkeji.songshushenghuo.vo.model.Doc;
import com.yitkeji.songshushenghuo.vo.req.app.DocInfoReq;
import com.yitkeji.songshushenghuo.vo.req.app.DocPageReq;
import com.yitkeji.songshushenghuo.vo.res.app.DocRes;
import com.yitkeji.songshushenghuo.vo.res.app.NewsRes;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(tags = {"文章接口"})
@RestController
public class DocController extends BaseAppController {

    @Autowired
    private DocService docService;

    /**
     * 某类型文章列表
     * @return
     */
    @ApiOperation(value = "获取文章列表")
    @RequestMapping(value = "/doc/list", method = RequestMethod.GET)
    public Result<Page<DocRes>> list(DocPageReq docPageReq) {
        Page page = initPage(docPageReq);
        QueryBuilder queryBuilder = new QueryBuilder(Doc.class);
        queryBuilder.setEQQueryCond("status",DocStatus.NORMAL.getCode());
        queryBuilder.addOrder(queryBuilder.getPrimaryKey(),false);

        List<DocRes> docResList;
        if(docPageReq.getType() != null) {
            queryBuilder.setEQQueryCond("type",docPageReq.getType());
            page = docService.findListByPage(page, queryBuilder, DocRes.class);
            docResList = page.getData();

            if (docResList == null || docResList.size() <= 0){
                return Result.success(page);
            }
            switch (docPageReq.getType()){
                case HOT_CARD:
                    List<DocRes> hotCardDocList = docService.findByType(DocType.CREDIT);
                    docResList.get(0).setHotCard(hotCardDocList);
                    break;
                case DYNAMIC_NEWS:
                    Object result = NewsChannel.getHeadlines();
                    List<NewsRes> list = JSONObject.parseArray(((JSONObject)result).getJSONArray("data").toJSONString(),NewsRes.class);
                    List<NewsRes> newsRes = list.subList(0, 3);
                    docResList.get(0).setNewsData(newsRes);
                    break;
                case LIFE_CHOICE:
                    List<DocRes> lifeSpeedyDocList = docService.findByType(DocType.LIFE_SPEEDY);
                    docResList.get(0).setLifeChoice(lifeSpeedyDocList);
                    break;default:
            }

            page.setData(docResList);
            return Result.success(page);
        }
        page = docService.findListByPage(page, queryBuilder, DocRes.class);
        return Result.success(page);
    }

    /**
     * 文章详情
     * @return
     */
    @ApiOperation(value = "获取文章详情", notes = "获取新手引导：固定参数docId=6 ")
    @RequestMapping(value = "/doc/info", method = RequestMethod.GET)
    public Result<DocRes> docInfo(DocInfoReq docInfoReq) {
        QueryBuilder queryBuilder = new QueryBuilder(Doc.class);
        queryBuilder.setEQQueryCond("docId", docInfoReq.getDocId());
        Doc doc = docService.findOne(queryBuilder);
        DocRes docRes = ObjectUtil.convert(doc, DocRes.class);
        return Result.success(docRes);
    }
}
