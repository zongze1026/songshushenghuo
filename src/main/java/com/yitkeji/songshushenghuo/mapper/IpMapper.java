package com.yitkeji.songshushenghuo.mapper;

import com.yitkeji.songshushenghuo.vo.model.Ip;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IpMapper {


    @Select("select start_ip from tb_ip where country like #{province} and country like #{city}")
    List<Ip> findByProvinceAndCity(@Param("province") String province, @Param("city") String city);

    @Select("select start_ip from tb_ip where country like #{city}")
    List<Ip> findByCity(@Param("city") String city);
}
