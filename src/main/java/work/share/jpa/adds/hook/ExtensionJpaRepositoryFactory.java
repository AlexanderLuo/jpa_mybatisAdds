package work.share.jpa.adds.hook;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.provider.PersistenceProvider;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.lang.Nullable;

import javax.persistence.EntityManager;
import java.util.Optional;

/**
 * @version V1.0, 2019-10-29
 * @author <a href="http://www.luohao.work">Alexander Lo</a>
 * @code 重写生成 query 的方法 注入自定义的生成器
 */
public class ExtensionJpaRepositoryFactory extends JpaRepositoryFactory {
    private final EntityManager entityManager;

    private final QueryExtractor extractor;

    private static final Logger logger = LoggerFactory.getLogger(ExtensionJpaRepositoryFactory.class);

    /**
     * Creates a new {@link JpaRepositoryFactory}.
     *
     * @param entityManager must not be {@literal null}
     */
    public ExtensionJpaRepositoryFactory(EntityManager entityManager) {
        super(entityManager);
        this.entityManager = entityManager;
        this.extractor = PersistenceProvider.fromEntityManager(entityManager);
        logger.info("[---Jpa---Adds---]			ExtensionJpaRepositoryFactory()");

        //听说这个可以挂在更多的东西
    }



    /********************************************************************************************************************
     *  Hook 点
    ********************************************************************************************************************/
    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(@Nullable QueryLookupStrategy.Key key,
                                                                   QueryMethodEvaluationContextProvider evaluationContextProvider) {

        logger.info("[---Jpa---Adds---##]			ExtensionJpaRepositoryFactory#getQueryLookupStrategy");
        return Optional.of(MapperQueryLookupStrategy.create(entityManager, key, extractor, evaluationContextProvider));
    }



}
