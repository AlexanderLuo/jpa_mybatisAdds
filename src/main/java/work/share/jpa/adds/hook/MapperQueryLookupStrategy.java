package work.share.jpa.adds.hook;

import work.share.jpa.adds.AddsContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.provider.QueryExtractor;
import org.springframework.data.jpa.repository.query.JpaQueryLookupStrategy;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.QueryMethodEvaluationContextProvider;
import org.springframework.data.repository.query.RepositoryQuery;

import javax.persistence.EntityManager;
import java.lang.reflect.Method;

/**
 * @author <a href="http://www.luohao.work">Alexander Lo</a>
 * @version V1.0, 2019-10-29
 * @code hook
 */
public class MapperQueryLookupStrategy implements QueryLookupStrategy {

    private final EntityManager entityManager;

    private QueryLookupStrategy jpaQueryLookupStrategy;

    private QueryExtractor extractor;

    private static final Logger logger = LoggerFactory.getLogger(MapperQueryLookupStrategy.class);

    public MapperQueryLookupStrategy(EntityManager entityManager, Key key, QueryExtractor extractor,
                                     QueryMethodEvaluationContextProvider queryMethodEvaluationContextProvider) {
        this.jpaQueryLookupStrategy = JpaQueryLookupStrategy.create(entityManager, key, extractor, queryMethodEvaluationContextProvider);
        this.extractor = extractor;
        this.entityManager = entityManager;
    }


    public static QueryLookupStrategy create(EntityManager entityManager, Key key, QueryExtractor extractor,
                                             QueryMethodEvaluationContextProvider queryMethodEvaluationContextProvider) {
        return new MapperQueryLookupStrategy(entityManager, key, extractor, queryMethodEvaluationContextProvider);
    }

    @Override
    public RepositoryQuery resolveQuery(Method method, RepositoryMetadata metadata, ProjectionFactory factory,
                                        NamedQueries namedQueries) {

        Class<?> mapperInterface = metadata.getRepositoryInterface();
        if (AddsContext.containsSQLNode(mapperInterface, method.getName())) {
            return new MapperQuery(new JpaQueryMethod(method, metadata, factory, extractor),
                    entityManager,
                    AddsContext.getMapper(mapperInterface).get(method.getName()));
        } else {
            return jpaQueryLookupStrategy.resolveQuery(method, metadata, factory, namedQueries);
        }

    }


}
