package work.share.jpa.adds;

import work.share.jpa.adds.hook.ExtensionJpaRepositoryFactory;
import work.share.jpa.adds.hook.JpaAddsPostProcessor;
import work.share.jpa.adds.resource.XmlScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;


/**
 * @version V1.0, 2019-10-29
 * @author <a href="http://www.luohao.work">Alexander Lo</a>
 * @code FactoryBean
 */
public class ExtensionRepositoryFactoryBean<R extends JpaRepository<T, I>, T, I extends Serializable>
		extends JpaRepositoryFactoryBean<R, T, I>
			implements ResourceLoaderAware, InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(JpaRepositoryFactoryBean.class);


	/**
	 * Creates a new {@link JpaRepositoryFactoryBean} for the given repository interface.
	 *
	 * @param repositoryInterface must not be {@literal null}.
	 */
	public ExtensionRepositoryFactoryBean(Class<? extends R> repositoryInterface) {
		super(repositoryInterface);
		AddsContext.LOEDED = true;
		logger.debug("---Jpa---Adds---]		ExtensionRepositoryFactoryBean   create");
	}



	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		logger.debug("[---Jpa---Adds---]		ExtensionRepositoryFactoryBean#createRepositoryFactory");
		RepositoryFactorySupport factorySupport = new ExtensionJpaRepositoryFactory(entityManager);

		// 扩展点  添加一些 拦截器 之类的
		factorySupport.addRepositoryProxyPostProcessor(new JpaAddsPostProcessor());
		return factorySupport;
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		new XmlScanner(resourceLoader).scan();
	}
}
