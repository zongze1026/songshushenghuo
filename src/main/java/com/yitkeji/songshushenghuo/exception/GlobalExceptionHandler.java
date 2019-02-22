package com.yitkeji.songshushenghuo.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.yitkeji.songshushenghuo.component.EmailUtil;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Result;
import io.swagger.annotations.ApiModelProperty;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	private static Logger logger = Logger.getLogger(GlobalExceptionHandler.class);

	@Autowired
	private RedisTemplate redisTemplate;


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Result handleIllegalParamException(MethodArgumentNotValidException e) {
		List<ObjectError> errors = e.getBindingResult().getAllErrors();
		String tips = "参数不合法";
		if (errors.size() > 0) {
			tips = errors.get(0).getDefaultMessage();
		}
		return Result.fail(tips);
	}


	@ExceptionHandler(HttpMessageNotReadableException.class)
	public Result handleHttpMessageNotReadableException(HttpMessageNotReadableException e) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
		String tips = "参数不合法";
		if(e.getCause() instanceof InvalidFormatException){
			InvalidFormatException ie = (InvalidFormatException)e.getCause();
			Class fromClass = ie.getPath().get(0).getFrom().getClass();
			String fieldName = ie.getPath().get(0).getFieldName();
			String apiModelProperty = ObjectUtil.getField(fromClass, fieldName).getAnnotation(ApiModelProperty.class).value();
			tips =  apiModelProperty + "不合法";
		}
		return Result.fail(tips);
	}


	@ExceptionHandler(Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
		e.printStackTrace();
		return EmailUtil.getInstance().reportException(e, request);
	}
}
