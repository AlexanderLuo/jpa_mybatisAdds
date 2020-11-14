package work.share.jpa.adds.resource;

import work.share.jpa.adds.AddsContext;
import work.share.jpa.adds.parser.XNode;
import work.share.jpa.adds.parser.XPathParser;
import work.share.jpa.adds.scripting.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class DefaultParsingTemplate implements ParsingTemplate {
    private final Map<String, NodeHandler> nodeHandlerMap = new HashMap<>();

    private ResourceLoader resourceLoader;

    public DefaultParsingTemplate(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void parse(Resource resource) throws IOException, ClassNotFoundException {

        InputStream inputStream = resource.getInputStream();
        XPathParser parser = new XPathParser(inputStream, false, null, null);
        XNode root = parser.evalNode("/mapper");
        inputStream.close();
        if (StringUtils.isEmpty(root.getStringAttribute("namespace")))
            throw new RuntimeException(resource.getFilename() + " no  namespace");


        Class<?> mapperInterface = Objects.requireNonNull(this.resourceLoader.getClassLoader()).loadClass(root.getStringAttribute("namespace"));


        List<XNode> sqls = root.evalNodes("select|insert|update|delete");


        for (XNode sql : sqls) {
            String methodName = sql.getStringAttribute("id");
            MixedSqlNode mixedSqlNode = doParse(sql);
            AddsContext.addMapperSQLNode(mapperInterface, methodName, mixedSqlNode);

        }

    }


    public MixedSqlNode doParse(XNode sqlNode) {
        List<SqlNode> contents = new ArrayList<>();
        NodeList children = sqlNode.getNode().getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            XNode child = sqlNode.newXNode(children.item(i));
            if (child.getNode().getNodeType() == Node.CDATA_SECTION_NODE || child.getNode().getNodeType() == Node.TEXT_NODE) {
                String data = child.getStringBody("");
                TextSqlNode textSqlNode = new TextSqlNode(data);
                contents.add(textSqlNode);
            } else if (child.getNode().getNodeType() == Node.ELEMENT_NODE) { // issue #628
                String nodeName = child.getNode().getNodeName();
                NodeHandler handler = nodeHandlerMap.get(nodeName);
                if (handler == null) {
                    throw new RuntimeException("Unknown element <" + nodeName + "> in SQL statement.");
                }
                handler.handleNode(child, contents);
            }
        }

        return new MixedSqlNode(contents);
    }


    public DefaultParsingTemplate() {
        nodeHandlerMap.put("if", new IfHandler());
        nodeHandlerMap.put("trim", new TrimHandler());
        nodeHandlerMap.put("where", new WhereHandler());
        nodeHandlerMap.put("set", new SetHandler());
//        nodeHandlerMap.put("foreach", new ForEachHandler());
//        nodeHandlerMap.put("choose", new ChooseHandler());
        nodeHandlerMap.put("when", new IfHandler());
        nodeHandlerMap.put("otherwise", new OtherwiseHandler());
//        nodeHandlerMap.put("bind", new BindHandler());
    }


    private interface NodeHandler {
        void handleNode(XNode nodeToHandle, List<SqlNode> targetContents);
    }

//    private class BindHandler implements NodeHandler {
//        public BindHandler() {
//            // Prevent Synthetic Access
//        }
//
//        @Override
//        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
//            final String name = nodeToHandle.getStringAttribute("name");
//            final String expression = nodeToHandle.getStringAttribute("value");
//            final VarDeclSqlNode node = new VarDeclSqlNode(name, expression);
//            targetContents.add(node);
//        }
//    }

    private class TrimHandler implements NodeHandler {
        public TrimHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = doParse(nodeToHandle);
            String prefix = nodeToHandle.getStringAttribute("prefix");
            String prefixOverrides = nodeToHandle.getStringAttribute("prefixOverrides");
            String suffix = nodeToHandle.getStringAttribute("suffix");
            String suffixOverrides = nodeToHandle.getStringAttribute("suffixOverrides");
            TrimSqlNode trim = new TrimSqlNode(mixedSqlNode, prefix, prefixOverrides, suffix, suffixOverrides);
            targetContents.add(trim);
        }
    }

    private class WhereHandler implements NodeHandler {
        public WhereHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = doParse(nodeToHandle);
            WhereSqlNode where = new WhereSqlNode(mixedSqlNode);
            targetContents.add(where);
        }
    }

    private class SetHandler implements NodeHandler {
        public SetHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = doParse(nodeToHandle);
            SetSqlNode set = new SetSqlNode(mixedSqlNode);
            targetContents.add(set);
        }
    }

//    private class ForEachHandler implements NodeHandler {
//        public ForEachHandler() {
//            // Prevent Synthetic Access
//        }
//
//        @Override
//        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
//            MixedSqlNode mixedSqlNode = parseSqlRoot(nodeToHandle);
//            String collection = nodeToHandle.getStringAttribute("collection");
//            String item = nodeToHandle.getStringAttribute("item");
//            String index = nodeToHandle.getStringAttribute("index");
//            String open = nodeToHandle.getStringAttribute("open");
//            String close = nodeToHandle.getStringAttribute("close");
//            String separator = nodeToHandle.getStringAttribute("separator");
//            ForEachSqlNode forEachSqlNode = new ForEachSqlNode(configuration, mixedSqlNode, collection, index, item, open, close, separator);
//            targetContents.add(forEachSqlNode);
//        }
//    }

    /********************************************************************************************************************
     *  <if></if>
     ********************************************************************************************************************/
    private class IfHandler implements NodeHandler {
        public IfHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = doParse(nodeToHandle);
            String test = nodeToHandle.getStringAttribute("test");
            IfSqlNode ifSqlNode = new IfSqlNode(mixedSqlNode, test);
            targetContents.add(ifSqlNode);
        }
    }

    private class OtherwiseHandler implements NodeHandler {
        public OtherwiseHandler() {
            // Prevent Synthetic Access
        }

        @Override
        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
            MixedSqlNode mixedSqlNode = doParse(nodeToHandle);
            targetContents.add(mixedSqlNode);
        }
    }

//    private class ChooseHandler implements NodeHandler {
//        public ChooseHandler() {
//            // Prevent Synthetic Access
//        }
//
//        @Override
//        public void handleNode(XNode nodeToHandle, List<SqlNode> targetContents) {
//            List<SqlNode> whenSqlNodes = new ArrayList<>();
//            List<SqlNode> otherwiseSqlNodes = new ArrayList<>();
//            handleWhenOtherwiseNodes(nodeToHandle, whenSqlNodes, otherwiseSqlNodes);
//            SqlNode defaultSqlNode = getDefaultSqlNode(otherwiseSqlNodes);
//            ChooseSqlNode chooseSqlNode = new ChooseSqlNode(whenSqlNodes, defaultSqlNode);
//            targetContents.add(chooseSqlNode);
//        }
//
//        private void handleWhenOtherwiseNodes(XNode chooseSqlNode, List<SqlNode> ifSqlNodes, List<SqlNode> defaultSqlNodes) {
//            List<XNode> children = chooseSqlNode.getChildren();
//            for (XNode child : children) {
//                String nodeName = child.getNode().getNodeName();
//                NodeHandler handler = nodeHandlerMap.get(nodeName);
//                if (handler instanceof IfHandler) {
//                    handler.handleNode(child, ifSqlNodes);
//                } else if (handler instanceof OtherwiseHandler) {
//                    handler.handleNode(child, defaultSqlNodes);
//                }
//            }
//        }
//
//        private SqlNode getDefaultSqlNode(List<SqlNode> defaultSqlNodes) {
//            SqlNode defaultSqlNode = null;
//            if (defaultSqlNodes.size() == 1) {
//                defaultSqlNode = defaultSqlNodes.get(0);
//            } else if (defaultSqlNodes.size() > 1) {
//                throw new RuntimeException("Too many default (otherwise) elements in choose statement.");
//            }
//            return defaultSqlNode;
//        }
//    }


}
