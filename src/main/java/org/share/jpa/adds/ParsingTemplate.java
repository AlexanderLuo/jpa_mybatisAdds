package org.share.jpa.adds;

import org.share.jpa.adds.parser.XNode;
import org.share.jpa.adds.scripting.MixedSqlNode;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public interface ParsingTemplate {


    default void parse(String path) throws Throwable { }

    InputStream load(String path) throws IOException;
    XNode resolveMapperNode(InputStream inputStream) throws IOException;
    List<XNode> resolveSQLNode(XNode root);
    MixedSqlNode doParse(XNode sqlNode);





}
