package com.yitkeji.test.songshushenghuo.pc;

import com.yitkeji.songshushenghuo.vo.req.pc.SystemInfoReq;
import com.yitkeji.songshushenghuo.vo.res.pc.SystemInfoRes;
import com.yitkeji.test.songshushenghuo.BaseTest;
import org.apache.log4j.Logger;
import org.junit.Test;


public class SystemInfoTest extends BaseTest {
    private Logger logger = Logger.getLogger(SystemInfoTest.class);

    @Test
    public void info(){
        SystemInfoReq req = new SystemInfoReq();
        req.setName("vip");
        getJson(baseurl + "/system/info", req);
    }
}
