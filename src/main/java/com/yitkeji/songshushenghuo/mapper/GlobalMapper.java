package com.yitkeji.songshushenghuo.mapper;

import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.SqlBuilder;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;


@Mapper
public interface GlobalMapper {


    @SelectProvider(type = SqlBuilder.class, method = "findListByPage")
    List<Map<String, Object>> findListByPage(QueryBuilder queryBuilder);

    @SelectProvider(type = SqlBuilder.class, method = "findList")
    List<Map<String, Object>> findList(QueryBuilder queryBuilder);

    @SelectProvider(type = SqlBuilder.class, method = "findOne")
    Map<String, Object> findOne(QueryBuilder queryBuilder);

    @SelectProvider(type = SqlBuilder.class, method = "count")
    int count(QueryBuilder queryBuilder);

    @SelectProvider(type = SqlBuilder.class, method = "sum")
    Map<String, Object> sum(QueryBuilder queryBuilder);

    @InsertProvider(type = SqlBuilder.class, method = "insert")
    @SelectKey(statement = "select LAST_INSERT_ID()",keyProperty = "__id", before = false,resultType = long.class)
    int insert(Map<String, Object> map);

    @InsertProvider(type = SqlBuilder.class, method = "insertList")
    @SelectKey(statement = "select LAST_INSERT_ID()",keyProperty = "__id", before = false,resultType = long.class)
    int insertList(Map<String, Object> map);

    @UpdateProvider(type = SqlBuilder.class, method = "updateByPrimaryKey")
    int updateByPrimaryKey(Map<String, Object> map);
}
