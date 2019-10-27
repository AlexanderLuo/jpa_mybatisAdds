package org.share.jpa.adds;



import org.share.jpa.adds.scripting.MixedSqlNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddsContext {

    private static  boolean useAble = false;


//    public static Map<String, XMLMapperBuilder> xmlCache = new ConcurrentHashMap<>(16);

//    public static Map<String, Map<String, MappedStatement>> sqlCache = new ConcurrentHashMap<>(16);




    private static Map<Class<?>/* mapperInterface */,
            Map<String/* Sql Id*/, MixedSqlNode /* caller*/>>  generator = new ConcurrentHashMap<>(16);




    public static void setMapper(Class<?> mapperInterface)  {
        generator.putIfAbsent(mapperInterface,new ConcurrentHashMap<>());
    }



    public static void addMapperSQLNode(Class<?> mapperInterface,String sqlId,MixedSqlNode sqlNode)  {
        Map<String, MixedSqlNode>  cache = generator.get(mapperInterface);
        if (cache == null)
            cache = new HashMap<>();

        if (cache.containsKey(sqlId))
            throw new RuntimeException(mapperInterface.getName() + " must have unique sel id" + sqlId);

        cache.put(sqlId,sqlNode);
        generator.put(mapperInterface,cache);

    }


    public static Map<String,MixedSqlNode> getMapper(Class<?> mapperInterface){
        return generator.get(mapperInterface);
    }

    public static Map<String,MixedSqlNode> getMapper(String mapperInterface) throws ClassNotFoundException {
        return generator.get(Class.forName(mapperInterface));
    }


}
