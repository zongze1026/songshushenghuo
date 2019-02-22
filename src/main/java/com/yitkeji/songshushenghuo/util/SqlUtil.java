package com.yitkeji.songshushenghuo.util;

import com.yitkeji.songshushenghuo.vo.annotation.PrimaryKey;
import com.yitkeji.songshushenghuo.vo.annotation.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

public class SqlUtil extends HashMap<String, Object> {

    /**
     * 格式化字段，只允许\w或*的字符
     * @param column
     * @return
     */
    public static final String formatColumn(String column){
        if(!"*".equals(column)){
            column = column.replaceAll("\\W", "");
            column = ObjectUtil.camelToUnderline(column);
        }
        return String.format("`%s`", column);
    }

    /**
     * 获取表名
     * @param object
     * @return
     */
    public static final String getTable(Object object){
        if(!object.getClass().isAnnotationPresent(Table.class)){
            return null;
        }
        return object.getClass().getAnnotation(Table.class).value();
    }

    public static final String getTable(Class oClass){
        try {
            return getTable(oClass.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取主键
     * @param oClass
     * @return
     */
    public static final String getPrimaryKey(Class oClass){
        String primaryKey = null;
        List<Field> fieldList = ObjectUtil.getFields(oClass);
        for(Field field: fieldList){
            if(field.isAnnotationPresent(PrimaryKey.class)){
                primaryKey = field.getAnnotation(PrimaryKey.class).value();
                break;
            }
        }
        return primaryKey;
    }

    /**
     * 获取主键值
     * @param object
     * @return
     */
    public static final Long getPrimaryValue(Object object){
        String primaryKey = getPrimaryKey(object);
        return (Long)ObjectUtil.objectToMap(object).get(ObjectUtil.underlineToCamel(primaryKey));
    }

    public static final String getPrimaryKey(Object object){
        return getPrimaryKey(object.getClass());
    }

    public static void main(String[] args) {
        System.out.println(String.valueOf(null));
    }
}
