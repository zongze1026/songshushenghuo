package com.yitkeji.songshushenghuo.component;

import com.yitkeji.songshushenghuo.exception.BaseException;
import com.yitkeji.songshushenghuo.util.EncryptUtil;
import com.yitkeji.songshushenghuo.util.RedisUtil;
import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.util.SpringUtil;
import com.yitkeji.songshushenghuo.vo.cfg.SystemCfg;
import com.yitkeji.songshushenghuo.vo.enums.CacheKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@Component
@ActiveProfiles("yitkeji")
public class EmailUtil {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String Sender;

    public static final EmailUtil getInstance(){
        return SpringUtil.getBean(EmailUtil.class);
    }

    public void sendSimpleMail(String toUser, String title, String text){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(Sender);
            message.setSubject(title);
            message.setText(text);
            message.setTo(toUser);
            mailSender.send(message);
            System.out.println("邮件已发送给" + toUser);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void reportException(Exception e){
        try{
            String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            String message = String.format("异常时间：%s\n异常内容：%s", time, sw.toString());

            String cacheKey = CacheKey.EMAIL_NOTICE_COUNT.getKey(EncryptUtil.md5Encrypt(sw.toString()));
            if(null == RedisUtil.get(cacheKey)){
                String title = "【"+ SystemCfg.getInstance().getAppinfo().getName()+"】服务器端异常";
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        for(String email: SystemCfg.getInstance().getAdmin().getNoticeEmails()){
                            sendSimpleMail(email.trim(), title, message);
                        }
                    }
                }, 1000);
                RedisUtil.set(cacheKey, 1, 5, TimeUnit.MINUTES);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    public Result reportException(Exception e, HttpServletRequest request){
        String uri = request.getRequestURI();
        String params = (String)request.getAttribute("params");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String message = String.format("请求地址：%s\n请求参数：%s\n请求时间：%s\n异常内容：%s", uri, params, time, sw.toString());
        if(e instanceof BaseException) {
            return ((BaseException) e).getResult();
        }else{
            String cacheKey = CacheKey.EMAIL_NOTICE_COUNT.getKey(EncryptUtil.md5Encrypt(sw.toString()));
            if(null == RedisUtil.get(cacheKey)){
                String title = "【"+ SystemCfg.getInstance().getAppinfo().getName()+"】客户端请求异常";
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        for(String email: SystemCfg.getInstance().getAdmin().getNoticeEmails()){
                            sendSimpleMail(email.trim(), title, message);
                        }
                    }
                }, 1000);
                RedisUtil.set(cacheKey, 1, 5, TimeUnit.MINUTES);
            }
            return Result.fail();
        }
    }

}