package org.share.jpa.adds;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.mapping.MappedStatement;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParsingContext {


    public static Map<String, XMLMapperBuilder> xmlCache = new ConcurrentHashMap<>(16);

    public static Map<String, Map<String, MappedStatement>> sqlCache = new ConcurrentHashMap<>(16);




}
