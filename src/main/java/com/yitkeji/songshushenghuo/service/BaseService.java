package com.yitkeji.songshushenghuo.service;

import com.yitkeji.songshushenghuo.mapper.GlobalMapper;
import com.yitkeji.songshushenghuo.service.inter.GlobalService;
import com.yitkeji.songshushenghuo.util.ObjectUtil;
import com.yitkeji.songshushenghuo.util.Page;
import com.yitkeji.songshushenghuo.util.QueryBuilder;
import com.yitkeji.songshushenghuo.util.SqlUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.ParameterizedType;
import java.util.*;


@Service
public class BaseService<T> implements GlobalService<T> {

    @Autowired
    private GlobalMapper globalMapper;

    @Override
    public T findByPrimaryKey(Long primaryKey) {
        Class<T> oClass = getEntityClass();
        QueryBuilder queryBuilder = new QueryBuilder(oClass);
        queryBuilder.setEQQueryCond(SqlUtil.getPrimaryKey(oClass), primaryKey);
        return findOne(queryBuilder);
    }

    @Override
    public T findOne(QueryBuilder queryBuilder) {
        return this.findOne(queryBuilder, (Class<T>) queryBuilder.getBoClass());
    }

    @Override
    public <A> A findOne(QueryBuilder queryBuilder, Class<A> targetClass) {
        return ObjectUtil.underlineToCamel(globalMapper.findOne(queryBuilder), targetClass);
    }

    @Override
    public List<T> findList(QueryBuilder queryBuilder) {
        return this.findList(queryBuilder, queryBuilder.getBoClass());
    }

    /**
     * 返回指定类型的对象
     *
     * @param queryBuilder
     * @param targetClass
     * @return
     */
    @Override
    public List findList(QueryBuilder queryBuilder, Class targetClass) {
        List list = globalMapper.findList(queryBuilder);
        return ObjectUtil.underlineToCamel(list, targetClass);
    }

    @Override
    public Page findListByPage(Page page, QueryBuilder queryBuilder) {
        return this.findListByPage(page, queryBuilder, queryBuilder.getBoClass());
    }

    @Override
    public Page findListByPage(Page page, QueryBuilder queryBuilder, Class targetClass) {
        if (page.getTotal() == 0) {
            page.setTotal(globalMapper.count(queryBuilder));
        }
        queryBuilder.putAll(ObjectUtil.objectToMap(page));
        List list = globalMapper.findListByPage(queryBuilder);
        page.setData(ObjectUtil.underlineToCamel(list, targetClass));
        return page;
    }

    @Override
    public int count(QueryBuilder queryBuilder) {
        return globalMapper.count(queryBuilder);
    }

    @Override
    public Map<String, Object> sum(QueryBuilder queryBuilder) {
        return globalMapper.sum(queryBuilder);
    }

    @Override
    public int add(T object) {
        Map<String, Object> cacheMap = buildCacheMap(object);
        int rows = globalMapper.insert(cacheMap);
        cacheMap.put(ObjectUtil.underlineToCamel(SqlUtil.getPrimaryKey(object)), cacheMap.get("__id"));
        removeCacheKey(cacheMap);
        ObjectUtil.underlineToCamel(cacheMap, object);
        return rows;
    }


    /**
     * 批量插入，不会反射回主键。。
     *
     * @param list
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int add(List<T> list) {
        if (list == null || list.size() < 1) {
            return 0;
        }
        List<Map<String, Object>> temList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();

        for (int i = 0; i < list.size(); i++) {
            T object = list.get(i);
            Map<String, Object> cacheMap = buildCacheMap(object);
            temList.add(cacheMap);
            for (String key : cacheMap.keySet()) {
                String mKey = key;
                mKey = i > 0 ? mKey + i : mKey;
                map.put(mKey, cacheMap.get(key));
            }
        }
        map.put("__list", temList);
        return globalMapper.insertList(map);
    }

    /**
     * 根据主键更新所有字段，在不指定columns时要慎重使用
     *
     * @param object
     * @param columns 强制并且只更新用户指定的字段
     * @return
     */
    @Override
    public int updateByPrimaryKey(T object, String... columns) {
        Map<String, Object> cacheMap = buildCacheMap(object);
        Map<String, Object> newCacheMap = cacheMap;
        if (columns.length > 0) {
            newCacheMap = new HashMap<>();
            for (String key : cacheMap.keySet()) {
                if (key.startsWith("__") || key.equals(ObjectUtil.underlineToCamel((String) cacheMap.get("__id")))) {
                    newCacheMap.put(key, cacheMap.get(key));
                }
            }
            for (String column : columns) {
                newCacheMap.put(column, cacheMap.get(column));
            }
        }
        return globalMapper.updateByPrimaryKey(newCacheMap);
    }

    @Override
    public int saveOrUpdate(T object) {
        Map<String, Object> cacheMap = buildCacheMap(object);
        String primaryKey = (String) cacheMap.get("__id");
        Long primaryKeyValue = (Long) cacheMap.get(ObjectUtil.underlineToCamel(primaryKey));
        if (primaryKeyValue != null && primaryKeyValue > 0) {
            return updateByPrimaryKey(object);
        } else {
            return add(object);
        }
    }

    /**
     * 获取泛型的类
     *
     * @return
     */
    private Class<T> getEntityClass() {
        String className = ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0].getTypeName();
        Class oClass = null;
        try {
            oClass = Class.forName(className);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return oClass;
    }

    private void removeCacheKey(Map<String, Object> map) {
        Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            if (entry.getKey().startsWith("__")) {
                iterator.remove();
            }
        }
    }

    private Map<String, Object> buildCacheMap(Object object) {
        Map<String, Object> cacheMap = ObjectUtil.objectToMap(object);
        String table = SqlUtil.getTable(object);
        String primaryKey = SqlUtil.getPrimaryKey(object);
        cacheMap.put("__table", table);
        cacheMap.put("__id", primaryKey);
        return cacheMap;
    }

}
