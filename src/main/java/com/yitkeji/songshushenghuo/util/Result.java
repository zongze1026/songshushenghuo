package com.yitkeji.songshushenghuo.util;

import com.yitkeji.songshushenghuo.vo.enums.ResCode;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;


/**
 * 接口响应对象，
 * 通常情况下直接调用success或error方法，附带具体的失败原因。
 * @param <T>
 */
@ApiModel
@Getter
@Setter
public class Result<T> {

	@ApiModelProperty("状态码")
	private int code;

	@ApiModelProperty("状态描述")
	private String msg;

	@ApiModelProperty("数据")
	private T data;

	public Result(){}

	public Result(ResCode resCode){
		this.code = resCode.getCode();
		this.msg = resCode.getMsg();
	}

	public Result(Integer resCode){
		this.code = resCode;
		this.msg = null;
	}

	public Result(ResCode resCode, T data, String... msgs){
		this.code = resCode.getCode();
		this.data = data;
		this.msg = resCode.getMsg(msgs);
	}

	// 请求失败
	public static Result fail(ResCode resCode){
		return new Result(resCode, null);
	}
	public static Result fail(String... msgs){
		return new Result(ResCode.FAIL, null, msgs);
	}

	// 系统异常
	public static Result error(ResCode resCode){
		return new Result(resCode, null);
	}
	public static Result error(String... msgs){
		return new Result(ResCode.FAIL, null, msgs);
	}

	// 请求成功
	public static Result success(){
		return new Result(ResCode.SUCCESS);
	}
	public static <T>Result<T> success(T data, String... msgs){
		return new Result(ResCode.SUCCESS, data, msgs);
	}
	public static Result auto(boolean isSuccess, String... msgs){
		return isSuccess ? success(): fail(msgs);
	}


	// 需要短信验证
	public static Result smsAuth(String phone, Object data){
		Map<String, Object> params;
		if(data instanceof Map){
			params = (Map<String, Object>)data;
		}else{
			params = ObjectUtil.objectToMap(data);
		}
		params.put("__phone", phone);
		return new Result(ResCode.NEEDCODE, params);
	}

	// 需要跳转页面
	public static Result accountExpired(){
		return new Result(ResCode.ACCOUNT_EXPIRED);
	}

	// 需要支付
	public static Result payAuth(){
		return new Result(ResCode.NEEDPAY);
	}

	// 需要跳转网页
	public static Result webAuth(String url, Object data){
		Map<String, Object> map = ObjectUtil.objectToMap(data);
		url += "?";
		String[] ks = (String[])map.keySet().toArray();
		for(int i=0; i<ks.length; i++){
			try {
				ks[i] = ks[i] + "=" + URLEncoder.encode((String)map.get(ks[i]), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				continue;
			}
		}
		return new Result(ResCode.NEEDWEB, url + StringUtils.join(ks, "&"));
	}

    // 需要确认操作
	public static Result tipCheck(String msg, Object data){
	    return new Result(ResCode.NEEDCHECK, data, msg);
    }
}
