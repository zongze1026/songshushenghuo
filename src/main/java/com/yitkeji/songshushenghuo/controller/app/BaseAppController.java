package com.yitkeji.songshushenghuo.controller.app;

import com.yitkeji.songshushenghuo.controller.BaseController;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import com.yitkeji.songshushenghuo.vo.model.User;
import com.yitkeji.songshushenghuo.vo.req.PageReq;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/app")
public class BaseAppController extends BaseController {
    private Logger logger = Logger.getLogger(BaseAppController.class);


    public User getCacheUser(HttpServletRequest request){
        Object object = request.getAttribute("user");
        if(object == null){
            return null;
        }
        return (User)object;
    }


    public void removeCacheUser(String token){
         RedisUtil.expired(CacheKey.TOKEN.getKey(token));
    }


    protected Page initPage(Map<String, Object> params){
        Page page = new Page();
        try{
            if(params.containsKey("npage")){
                page.setNpage((int)params.get("npage"));
                params.remove("npage");
            }
            if(params.containsKey("pageSize")){
                page.setPageSize((int)params.get("pageSize"));
                params.remove("pageSize");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return page;
    }

    protected Page initPage(PageReq pageReq){
        return initPage(ObjectUtil.objectToMap(pageReq));
    }

}
