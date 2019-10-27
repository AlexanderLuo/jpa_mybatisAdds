package org.share.jpa.adds.scripting;

import org.junit.Test;
import org.share.jpa.adds.AddsContext;
import org.share.jpa.adds.TestDemo;

public class IfSqlNodeTest extends TestDemo {


    @Test
    public void ifTest(){

        MixedSqlNode mixedSqlNode = AddsContext.getMapper(mapperInterface).get("testIf");
        System.err.println(mixedSqlNode.fire(po).getSql());

    }


}
