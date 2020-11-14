package work.share.jpa.adds;


import work.share.jpa.adds.scripting.MixedSqlNode;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AddsContext {

    public static boolean LOEDED = false;


    private static Map<Class<?>/* mapperInterface */,
            Map<String/* Sql Id*/, MixedSqlNode /* caller*/>> generator = new ConcurrentHashMap<>(16);


    public static void addMapperSQLNode(Class<?> mapperInterface, String sqlId, MixedSqlNode sqlNode) {
        Map<String, MixedSqlNode> cache = generator.get(mapperInterface);
        if (cache == null)
            cache = new HashMap<>();

        if (cache.containsKey(sqlId))
            throw new RuntimeException(mapperInterface.getName() + " must have unique sql id" + sqlId);

        cache.put(sqlId, sqlNode);
        generator.put(mapperInterface, cache);

    }


    public static Map<String, MixedSqlNode> getMapper(Class<?> mapperInterface) {
        return generator.get(mapperInterface);
    }


    public static boolean containsMapper(Class<?> mapperInterface) {
        return generator.containsKey(mapperInterface);
    }

    public static boolean containsSQLNode(Class<?> mapperInterface, String sqlId) {
        return generator.containsKey(mapperInterface) && generator.get(mapperInterface).containsKey(sqlId);
    }


}
