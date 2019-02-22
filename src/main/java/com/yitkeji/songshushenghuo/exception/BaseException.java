package com.yitkeji.songshushenghuo.exception;

import com.yitkeji.songshushenghuo.util.Result;
import com.yitkeji.songshushenghuo.vo.enums.ResCode;
import org.apache.commons.lang.StringUtils;

public class BaseException extends Exception{
    private Result result;

    public BaseException(Result result){
        super(result.getMsg());
        this.result = result;
    }

    public BaseException(ResCode resCode, String... msgs){
        super(StringUtils.join(msgs, ", "));
        this.result = new Result(resCode, msgs);
    }

    public BaseException(String msg){
        super(msg);
        this.result = Result.fail(msg);
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }
}
