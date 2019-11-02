package work.share.jpa.adds.hook;

import org.hibernate.query.NativeQuery;
import org.springframework.data.util.ClassTypeInformation;
import org.springframework.data.util.TypeInformation;
import work.share.jpa.adds.builder.AopTargetUtils;
import work.share.jpa.adds.builder.QuerySQLBuilder;
import work.share.jpa.adds.scripting.MixedSqlNode;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.AbstractJpaQuery;
import org.springframework.data.jpa.repository.query.JpaParameters;
import org.springframework.data.jpa.repository.query.JpaQueryMethod;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.repository.query.Parameter;
import org.springframework.data.repository.query.ParameterAccessor;
import org.springframework.data.repository.query.ParametersParameterAccessor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.HashMap;
import java.util.Map;

/**
 * @version V1.0, 2019-10-29
 * @author <a href="http://www.luohao.work">Alexander Lo</a>
 * @code  最终的sql生成器  接入mapper的动态解析方案
 */
public class MapperQuery extends AbstractJpaQuery {

    private MixedSqlNode mixedSqlNode;

    /**
     * Creates a new {@link AbstractJpaQuery} from the given {@link JpaQueryMethod}.
     *
     * @param method jpa query method
     * @param em     entity manager
     */
    MapperQuery(JpaQueryMethod method, EntityManager em) {
        super(method, em);
    }

    MapperQuery(JpaQueryMethod method, EntityManager em, MixedSqlNode mixedSqlNode) {
        this(method,em);
        this.mixedSqlNode = mixedSqlNode;
    }

    @Override
    protected Query doCreateQuery(Object[] values) {
        String nativeQuery = getQuery(values);

        JpaParameters parameters = getQueryMethod().getParameters();

        ParameterAccessor accessor = new ParametersParameterAccessor(parameters, values);

        String sortedQueryString = QueryUtils
                .applySorting(nativeQuery, accessor.getSort(), QueryUtils.detectAlias(nativeQuery));

//        Query query = bind(createJpaQuery0(sortedQueryString), values);
        Query query =  createJpaQuery0(sortedQueryString);

        if (parameters.hasPageableParameter()) {
            Pageable pageable = (Pageable) (values[parameters.getPageableIndex()]);
            if (pageable != null) {
                query.setFirstResult((int) pageable.getOffset());
                query.setMaxResults(pageable.getPageSize());
            }
        }
        return query;
    }


    private String getQuery(Object[] values) {
        return this.mixedSqlNode.fire(mergeParams(values)).getSql();
    }



    private Map<String, Object> mergeParams(Object[] values) {
        JpaParameters parameters = getQueryMethod().getParameters();
        Map<String, Object> params = new HashMap<>();

        for (int i = 0; i < parameters.getNumberOfParameters(); i++) {
            Object value = values[i];
            Parameter parameter = parameters.getParameter(i);
            if (value != null){
                params.put(parameter.getName().orElse(null),value);
            }

//            if (value != null && canBindParameter(parameter)) {
//                if (!QuerySQLBuilder.isValidValue(value)) {
//                    continue;
//                }
//                Class<?> clz = value.getClass();
//                if (clz.isPrimitive() || String.class.isAssignableFrom(clz) || Number.class.isAssignableFrom(clz)
//                        || clz.isArray() || Collection.class.isAssignableFrom(clz) || clz.isEnum()) {
//                    params.put(parameter.getName().orElse(null), value);
//                } else {
//                    params = QuerySQLBuilder.toParams(value);
//                }
//            }
        }
        return params;
    }


    private Query createJpaQuery0(String queryString) {
        Class<?> objectType = getQueryMethod().getReturnedObjectType();

        //get original proxy query.
        Query oriProxyQuery;
        //must be hibernate QueryImpl
        NativeQuery query;


        if (getQueryMethod().isQueryForEntity()) {
            oriProxyQuery = getEntityManager().createNativeQuery(queryString, objectType);
        }
        else {
            //  极度麻烦的 返回类型转换
            oriProxyQuery = getEntityManager().createNativeQuery(queryString);
            query = AopTargetUtils.getTarget(oriProxyQuery);
            Class<?> genericType;

            if (objectType.isAssignableFrom(Map.class)) {
                genericType = objectType;
            } else {
                // 获取返回对象的实际类型
                ClassTypeInformation<?> ctif = ClassTypeInformation.from(objectType);
                TypeInformation<?> actualType = ctif.getActualType();
                genericType = actualType.getType();
            }

            if (genericType != Void.class) {
                QuerySQLBuilder.transform(query, genericType);
            }

        }
        //return the original proxy query, for a series of JPA actions, e.g.:close em.
        return oriProxyQuery;
    }

    private String getEntityName() {
        return getQueryMethod().getEntityInformation().getJavaType().getSimpleName();
    }

    private String getMethodName() {
        return getQueryMethod().getName();
    }


    /**
     * 基于原始sql 的计数器
     * @param values
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    protected TypedQuery<Long> doCreateCountQuery(Object[] values) {
        TypedQuery query = (TypedQuery) getEntityManager().createNativeQuery(QuerySQLBuilder.toCountQuery(getQuery(values)));
//        bind(query, values);
        return query;
    }

//    private Query bind(Query query, Object[] values) {
//        //get proxy target if exist.
//        //must be hibernate QueryImpl
//        NativeQuery targetQuery = AopTargetUtils.getTarget(query);
//        Map<String, Object> params = getParams(values);
//        if (!CollectionUtils.isEmpty(params)) {
//            QuerySQLBuilder.setParams(targetQuery, params);
//        }
//        return query;
//    }

    private boolean canBindParameter(Parameter parameter) {
        return parameter.isBindable();
    }
}
