package com.yitkeji.songshushenghuo.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @desc    用户信息映射
 * @author  穆抓刚
 * @create  2018/4/9 16:52
 **/
@Mapper
public interface SystemMapper {

    @Select("show table status")
    List<Map<String, Object>> showTableStatus();
}