package org.share.jpa.adds.parser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.xml.DefaultDocumentLoader;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.beans.factory.xml.ResourceEntityResolver;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.xml.DomUtils;
import org.springframework.util.xml.SimpleSaxErrorHandler;
import org.springframework.util.xml.XmlValidationModeDetector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;

import java.util.List;

/**
 * .
 *
 * @author stormning on 2016/12/17.
 */
public class XmlNamedTemplateResolver   {
    protected final Log logger = LogFactory.getLog(XmlNamedTemplateResolver.class);


    private DocumentLoader documentLoader = new DefaultDocumentLoader();

    private EntityResolver entityResolver;

    private ErrorHandler errorHandler = new SimpleSaxErrorHandler(logger);

    public XmlNamedTemplateResolver(ResourceLoader resourceLoader) {
        this.entityResolver = new ResourceEntityResolver(resourceLoader);
    }


    public List<Element> resolve(Resource resource) throws Exception {

        InputSource inputSource = new InputSource(resource.getInputStream());
        inputSource.setEncoding("UTF-8");
        Document doc = documentLoader.loadDocument(inputSource, entityResolver, errorHandler,
                XmlValidationModeDetector.VALIDATION_XSD, false);

        return DomUtils.getChildElementsByTagName(doc.getDocumentElement(), "sql");
    }
}
