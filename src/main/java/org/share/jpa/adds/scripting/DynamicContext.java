package org.share.jpa.adds.scripting;

import org.share.reflection.meta.MetaObject;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

public class DynamicContext {
    public static final String PARAMETER_OBJECT_KEY = "_parameter";

    private final StringJoiner sqlBuilder = new StringJoiner(" ");
    private final ContextMap bindings;


    public DynamicContext(MetaObject parameterMetaObject){
        bindings  = new ContextMap(parameterMetaObject);
        bindings.put(PARAMETER_OBJECT_KEY, parameterMetaObject);
    }


    public Map<String,Object> getBindings(){return  this.bindings;}

    public void bind(String name, Object value) {
        bindings.put(name, value);
    }

    public void appendSql(String sql) {
        sqlBuilder.add(sql);
    }
    public String getSql() {
        return sqlBuilder.toString().trim();
    }




    static class ContextMap extends HashMap<String, Object> {
        private final MetaObject parameterMetaObject;

        public ContextMap(MetaObject parameterMetaObject) {
            this.parameterMetaObject = parameterMetaObject;
        }

        @Override
        public Object get(Object key) {
            String strKey = (String) key;
            if (super.containsKey(strKey)) {
                return super.get(strKey);
            }

            if (parameterMetaObject == null) {
                return null;
            }

            return parameterMetaObject.getValue(strKey);

        }
    }

}
