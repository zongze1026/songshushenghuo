package com.yitkeji.songshushenghuo.controller.admin;

import com.yitkeji.songshushenghuo.controller.BaseController;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class BaseAdminController extends BaseController {
    private Logger logger = Logger.getLogger(BaseAdminController.class);


    protected Class getResClass(Class modelClass){
        return super.getResClass(modelClass, "admin");
    }
}
