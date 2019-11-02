package work.share.jpa.adds.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;



/**
 * @version V1.0, 2019-10-29
 * @author <a href="http://www.luohao.work">Alexander Lo</a>
 * @code 加载Mapper
 */
public class XmlScanner {
    private static final Logger logger = LoggerFactory.getLogger(XmlScanner.class);
    private ResourceLoader resourceLoader;
    private String basePackage = "classpath*:META-INF/**/*Mapper.xml";

    public XmlScanner(ResourceLoader resourceLoader){
        this.resourceLoader = resourceLoader;
    }


    public void scan() {
        try {
            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);
            Resource[] resources = resourcePatternResolver.getResources(this.basePackage);
            handler(resources);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("XmlScanner scan error");
        }

    }


    protected void handler(Resource[] resources) throws IOException, ClassNotFoundException {
        ParsingTemplate parsingTemplate = new DefaultParsingTemplate(this.resourceLoader);
        for (Resource resource: resources)
            parsingTemplate.parse(resource);
    }



}
