package com.yitkeji.songshushenghuo;


import com.yitkeji.songshushenghuo.timer.SystemTimer;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@ServletComponentScan
@EnableTransactionManagement
@SpringBootApplication
@EnableScheduling
public class SongshushenghuoApp {

    public static void main(String[] args) {

        ApplicationContext applicationContext = SpringApplication.run(SongshushenghuoApp.class, args);
        SpringUtil.setApplicationContext(applicationContext);
        RedisUtil.setRedisTemplate((RedisTemplate)SpringUtil.getBean("redisTemplate"));
        SystemTimer.getInstance(); // 启动定时器
    }
}
