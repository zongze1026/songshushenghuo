package com.yitkeji.songshushenghuo.controller.agent;

import com.yitkeji.songshushenghuo.controller.BaseController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/agent")
public class BaseAgentController extends BaseController {

    protected Class getResClass(Class modelClass){
        return super.getResClass(modelClass, "agent");
    }
}
