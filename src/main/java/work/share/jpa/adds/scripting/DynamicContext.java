package work.share.jpa.adds.scripting;

import ognl.OgnlContext;
import ognl.OgnlRuntime;
import ognl.PropertyAccessor;
import work.share.reflection.api.ObjectReflector;
import work.share.reflection.api.ReflectApi;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class DynamicContext {
    static {
        OgnlRuntime.setPropertyAccessor(ContextMap.class, new ContextAccessor());
    }

    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    private final ContextMap bindings;
    private int uniqueNumber = 0;   // 用于解析 循环时 生成序列id


    public DynamicContext(Object parameterMetaObject) {
        ObjectReflector objectOperator = ReflectApi.forObject(parameterMetaObject);
        bindings = new ContextMap(objectOperator);
        bindings.put(PARAMETER_OBJECT_KEY, parameterMetaObject);
    }


    public Map<String, Object> getBindings() {
        return this.bindings;
    }

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }


    public void appendSql(String sql) {
        sqlBuilder.add(sql);
    }

    public String getSql() {
        return sqlBuilder.toString().trim();
    }


    /**
     * 对象的静态代理 用于  一致性的 set get 方案实现
     */
    static class ContextMap extends HashMap<String, Object> {
        private final ObjectReflector objectOperator;

        public ContextMap(ObjectReflector objectOperator) {
            this.objectOperator = objectOperator;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }

            if (objectOperator == null) {
                return null;
            }

            return objectOperator.getValue(strKey);

        }
    }

    static class ContextAccessor implements PropertyAccessor {

        @Override
        public Object getProperty(Map context, Object target, Object name) {
            Map map = (Map) target;

            Object result = map.get(name);
            if (map.containsKey(name) || result != null) {
                return result;
            }

            Object parameterObject = map.get(PARAMETER_OBJECT_KEY);
            if (parameterObject instanceof Map) {
                return ((Map) parameterObject).get(name);
            }

            return null;
        }

        @Override
        public void setProperty(Map context, Object target, Object name, Object value) {
            Map<Object, Object> map = (Map<Object, Object>) target;
            map.put(name, value);
        }

        @Override
        public String getSourceAccessor(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }

        @Override
        public String getSourceSetter(OgnlContext arg0, Object arg1, Object arg2) {
            return null;
        }
    }

}
