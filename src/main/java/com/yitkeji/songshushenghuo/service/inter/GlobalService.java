package com.yitkeji.songshushenghuo.service.inter;

import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;

import java.util.List;
import java.util.Map;

public interface GlobalService<T> {

    T findByPrimaryKey(Long primaryKey);

    T findOne(QueryBuilder queryBuilder);

    <A> A findOne(QueryBuilder queryBuilder, Class<A> targetClass);

    List<T> findList(QueryBuilder queryBuilder);

    List findList(QueryBuilder queryBuilder, Class targetClass);

    Page findListByPage(Page page, QueryBuilder queryBuilder);

    Page findListByPage(Page page, QueryBuilder queryBuilder, Class targetClass);

    int count(QueryBuilder queryBuilder);

    int add(T object);

    int add(List<T> list);

    Map<String, Object> sum(QueryBuilder queryBuilder);

    int updateByPrimaryKey(T object, String... columns);

    int saveOrUpdate(T object);
}
