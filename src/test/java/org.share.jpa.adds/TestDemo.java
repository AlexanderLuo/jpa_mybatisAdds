package org.share.jpa.adds;

import org.junit.Before;
import org.junit.Test;
import org.share.jpa.adds.scripting.DynamicContext;
import org.share.jpa.adds.scripting.MixedSqlNode;

import java.util.Map;

public class TestDemo {
//    public  static String resource = "org/share/jpa/adds/AuthorMapper.xml";


    public static Class mapperInterface = MapperInterface.class;

    public static Param  po = new Param();
    static {
        po.setId("123");
        po.setName("luohao");
    }


    @Before
    public void run() throws Throwable {
//        String resource = "org/share/jpa/adds/AuthorMapper.xml";
        String resource = "META-INF/TestMapper.xml";
        DefaultParsingTemplate parsingTemplate = new DefaultParsingTemplate();
        parsingTemplate.parse(resource);
    }


    @Test
    public void parseResult(){
        Map map = AddsContext.getMapper(mapperInterface);

        MixedSqlNode sqlNode = (MixedSqlNode) map.get("selectUserByIf");

        DynamicContext dynamicContext = sqlNode.fire(po);

        System.err.println(dynamicContext.getSql());


//        DynamicContext dynamicContext = mixedSqlNode.fire(po);
//
//        System.err.println(dynamicContext.getSql());




    }

}
