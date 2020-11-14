package work.share.jpa.adds.hook;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.support.RepositoryProxyPostProcessor;


/**
 * @author <a href="http://www.luohao.work">Alexander Lo</a>
 * @version V1.0, 2019-10-29
 * @code 后处理器 用于方法追踪
 */
public class JpaAddsPostProcessor implements RepositoryProxyPostProcessor {


    static class MapperMethodLogger implements MethodInterceptor {
        private static final Logger logger = LoggerFactory.getLogger(MapperMethodLogger.class);

        @Override
        public Object invoke(MethodInvocation methodInvocation) throws Throwable {
            logger.info("[jpa-mybatis-adds ---]  ---" + methodInvocation.getMethod().getName());
            return methodInvocation.proceed();
        }
    }


    @Override
    public void postProcess(ProxyFactory factory, RepositoryInformation repositoryInformation) {
        factory.addAdvice(new MapperMethodLogger());

    }
}
