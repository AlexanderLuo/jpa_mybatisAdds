package org.share.jpa.adds.parser;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;

import java.io.IOException;



public class FreemarkerSqlTemplates implements ResourceLoaderAware{

    private final Log logger = LogFactory.getLog(FreemarkerSqlTemplates.class);


    private static Configuration cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
    private static StringTemplateLoader sqlTemplateLoader = new StringTemplateLoader();

    static { cfg.setTemplateLoader(sqlTemplateLoader); }


    private ResourceLoader resourceLoader;


    private String templateBasePackage = "**";

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
        loadPatternResource();

    }



    private void loadPatternResource(){
        String suffixPattern = "/**/*.xml";
        String pattern = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX +
                ClassUtils.convertClassNameToResourcePath(templateBasePackage) + suffixPattern;

        PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver(resourceLoader);

        try {
            Resource[] resources = resourcePatternResolver.getResources(pattern);

        } catch (Throwable e) {
            e.printStackTrace();

        }


    }





}
