package org.share.jpa.adds;

import org.share.jpa.adds.parser.XNode;
import org.share.jpa.adds.parser.XPathParser;
import org.share.jpa.adds.scripting.MixedSqlNode;
import org.share.jpa.adds.scripting.SqlNode;
import org.share.jpa.adds.scripting.StaticTextSqlNode;
import org.share.jpa.adds.scripting.TextSqlNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsingTemplate {
    private final Map<String, NodeHandler> nodeHandlerMap = new HashMap<>();
    private interface NodeHandler {
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents);
    }

    public ParsingTemplate(){
        nodeHandlerMap.put("trim", new TrimHandler());
        nodeHandlerMap.put("where", new WhereHandler());
        nodeHandlerMap.put("set", new SetHandler());
        nodeHandlerMap.put("foreach", new ForEachHandler());
        nodeHandlerMap.put("if", new IfHandler());
        nodeHandlerMap.put("choose", new ChooseHandler());
        nodeHandlerMap.put("when", new IfHandler());
        nodeHandlerMap.put("otherwise", new OtherwiseHandler());
        nodeHandlerMap.put("bind", new BindHandler());
    }



    /**
     * [select|insert|update|delete]
     * @param node
     * @return
     */

    MixedSqlNode parseSqlRoot(XNode node){
        List<SqlNode> contents = new ArrayList<>();
        NodeList children = node.getNode().getChildNodes();
        boolean isDynamic = false;

        for (int i = 0; i < children.getLength(); i++) {
            XNode child = node.newXNode(children.item(i));
            if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE || child.getNode().getNodeType() == Node.TEXT_NODE) {
                String data = child.getStringBody("");
                TextSqlNode textSqlNode = new TextSqlNode(data);
                if (textSqlNode.isDynamic()) {
                    contents.add(textSqlNode);
                    isDynamic = true;
                } else {
                    contents.add(new StaticTextSqlNode(data));
                }
            } else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) { // issue #628
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = nodeHandlerMap.get(nodeName);
                if (handler == null) {
                    throw new RuntimeException("Unknown element <" + nodeName + "> in SQL statement.");
                }
                handler.handleNode(child, contents);
                isDynamic = true;
            }
        }


        MixedSqlNode mixedSqlNode = new MixedSqlNode(contents);
        mixedSqlNode.isDynamic = isDynamic;
        return  mixedSqlNode;
    }
}
