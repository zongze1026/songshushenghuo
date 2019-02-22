package com.yitkeji.channel.model;

import com.yitkeji.channel.inter.Logger;
import com.yitkeji.songshushenghuo.service.LogApiService;
import com.yitkeji.songshushenghuo.vo.model.LogApi;

import java.util.Date;

public class ChannelLogger implements Logger {

    private LogApiService logApiService;
    private String apiName;

    public ChannelLogger(String apiName, LogApiService logApiService){
        this.apiName = apiName;
        this.logApiService = logApiService;
    }

    @Override
    public void httpLog(String url, String method, String data, String result, Date startTime, Date endTime) {
        LogApi logApi = new LogApi();
        logApi.setUrl(url);
        logApi.setData(data);
        logApi.setApiName(this.apiName);
        logApi.setResult(result);
        logApi.setMethod(method);
        logApi.setRunTime(startTime);
        logApi.setResTime(endTime);
        logApiService.add(logApi);
    }
}
