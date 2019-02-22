package com.yitkeji.songshushenghuo.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 签名工具类
 * 
 * @author tinn
 *
 */
public class SignUtil {

	/** 签名属性名 sign **/
	private static final String SIGN_KEY = "sign";

	/** 密钥属性名key**/
	private static final String SECRET_KEY = "key";

	/**
	 * 生成签名
	 *
	 * @param signObjs
	 *            签名所需元素
	 * @return
	 */
	public static String signByObject(String channelKey, Object... signObjs) {
		// 计算签名所需要的内容
		StringBuilder signContent = new StringBuilder();
		signContent.append(channelKey);
		// 有参数的情况下，签名内容需要拼接参数
		if (signObjs != null && signObjs.length > 0) {
			for (Object signElement : signObjs) {
				if (signElement != null) {
					signContent.append(signElement);
				}
			}
		}
		// 生成签名
		String sign = EncryptUtil.md5Encrypt(signContent.toString());
		return sign;
	}

	/**
	 * 签名
	 *
	 * @param map
	 * @return
	 */
	public static String signByMap(String channelKey, TreeMap<String, Object> map) {
		try {
			StringBuilder sb = new StringBuilder();
			Iterator<String> iterator = map.keySet().iterator();
			sb.append(channelKey);
			while (iterator.hasNext()) {
				Object key = iterator.next();
				Object valueObj = map.get(key);
				if (valueObj != null) {
					// 并将获取的值进行拼接
					String value = valueObj.toString();
					sb.append(value);
				}
			}
			String signData = EncryptUtil.md5Encrypt(sb.toString());
			return signData;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成MD5签名
	 */
	public static String getMD5Sign(String pub_key, String partner_order_id, String sign_time, String security_key) {
		try {
			String signStr = String.format("pub_key=%s|partner_order_id=%s|sign_time=%s|security_key=%s", pub_key, partner_order_id, sign_time, security_key);
			return ZGLTool.MD5Encrpytion(signStr.getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 福建米联，计算签名
	 *
	 * @param map
	 *            要参与签名的map数据
	 * @param md5Key
	 *            密钥
	 * @return 签名
	 */
	public static String getSign(Map<String, ?> map, String md5Key) {
		if (map == null || map.isEmpty()) {
			return null;
		}
		JSONObject jsonObj = new JSONObject();
		jsonObj.putAll(map);
		return getSign(jsonObj, md5Key);
	}

	/**
	 * 福建米联，计算签名
	 *
	 * @param jsonObj
	 *            要参与签名的json数据
	 * @param md5Key
	 *            密钥
	 * @return 签名
	 */
	public static String getSign(JSONObject jsonObj, String md5Key) {
		if (jsonObj == null || jsonObj.isEmpty()) {
			return null;
		}
		String str2Sign = buildParam4Sign(jsonObj, SIGN_KEY, md5Key);
		String result = DigestUtils.md5Hex(str2Sign).toUpperCase();
		return result;
	}

	/**
	 * 福建米联，拼接用于签名的参数
	 * @param jsonObj
	 * @return
	 */
	private static String buildParam4Sign(JSONObject jsonObj, String signKey, String md5Key) {
		Set<String> keySet = jsonObj.keySet();
		StringBuilder param = new StringBuilder(20 * keySet.size());
		String[] keys = keySet.toArray(new String[keySet.size()]);
		Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
		for (String key : keys) {
			// 排除sign
			if (signKey.equals(key)) {
				continue;
			}
			Object value = jsonObj.get(key);
			// 排除值为null的情况
			if (value != null&& !"null".equals(value) &&!"".equals(value)) {
				param.append(key).append("=").append(value).append("&");
			}
		}
		param.append(SECRET_KEY).append("=").append(md5Key);
		return param.toString();
	}
}
