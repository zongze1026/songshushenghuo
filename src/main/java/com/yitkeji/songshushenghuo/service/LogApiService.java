package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.vo.model.LogApi;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class LogApiService extends BaseService<LogApi> {

    private Logger logger = Logger.getLogger(LogApiService.class);

}
