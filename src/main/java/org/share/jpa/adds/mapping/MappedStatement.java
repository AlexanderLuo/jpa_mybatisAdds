///**
// *    Copyright 2009-2019 the original author or authors.
// *
// *    Licensed under the Apache License, Version 2.0 (the "License");
// *    you may not use this file except in compliance with the License.
// *    You may obtain a copy of the License at
// *
// *       http://www.apache.org/licenses/LICENSE-2.0
// *
// *    Unless required by applicable law or agreed to in writing, software
// *    distributed under the License is distributed on an "AS IS" BASIS,
// *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// *    See the License for the specific language governing permissions and
// *    limitations under the License.
// */
//package org.share.jpa.adds.mapping;
//
//
//
///**
// * @author Clinton Begin
// */
//public final class MappedStatement {
//
//  private String resource;
//  private String id;
//  private Integer fetchSize;
//  private Integer timeout;
//  private StatementType statementType;
////  private ResultSetType resultSetType;
//  private SqlSource sqlSource;
////  private ParameterMap parameterMap;
////  private List<ResultMap> resultMaps;
//  private boolean flushCacheRequired;
//  private boolean useCache;
//  private boolean resultOrdered;
//  private SqlCommandType sqlCommandType;
////  private KeyGenerator keyGenerator;
//  private String[] keyProperties;
//  private String[] keyColumns;
//  private boolean hasNestedResultMaps;
//  private String databaseId;
//  private String[] resultSets;
//
//  MappedStatement() {
//    // constructor disabled
//  }
//
//  public static class Builder {
//    private MappedStatement mappedStatement = new MappedStatement();
//
//    public Builder(String id, SqlSource sqlSource, SqlCommandType sqlCommandType) {
//      mappedStatement.id = id;
//      mappedStatement.sqlSource = sqlSource;
//      mappedStatement.statementType = StatementType.PREPARED;
////      mappedStatement.resultSetType = ResultSetType.DEFAULT;
////      mappedStatement.parameterMap = new ParameterMap.Builder(configuration, "defaultParameterMap", null, new ArrayList<>()).build();
////      mappedStatement.resultMaps = new ArrayList<>();
//      mappedStatement.sqlCommandType = sqlCommandType;
////      mappedStatement.keyGenerator = configuration.isUseGeneratedKeys() && SqlCommandType.INSERT.equals(sqlCommandType) ? Jdbc3KeyGenerator.INSTANCE : NoKeyGenerator.INSTANCE;
//
//
//    }
//
//    public Builder resource(String resource) {
//      mappedStatement.resource = resource;
//      return this;
//    }
//
//    public String id() {
//      return mappedStatement.id;
//    }
//
////    public Builder parameterMap(ParameterMap parameterMap) {
////      mappedStatement.parameterMap = parameterMap;
////      return this;
////    }
////
////    public Builder resultMaps(List<ResultMap> resultMaps) {
////      mappedStatement.resultMaps = resultMaps;
////      for (ResultMap resultMap : resultMaps) {
////        mappedStatement.hasNestedResultMaps = mappedStatement.hasNestedResultMaps || resultMap.hasNestedResultMaps();
////      }
////      return this;
////    }
//
//    public Builder fetchSize(Integer fetchSize) {
//      mappedStatement.fetchSize = fetchSize;
//      return this;
//    }
//
//    public Builder timeout(Integer timeout) {
//      mappedStatement.timeout = timeout;
//      return this;
//    }
//
//    public Builder statementType(StatementType statementType) {
//      mappedStatement.statementType = statementType;
//      return this;
//    }
//
////    public Builder resultSetType(ResultSetType resultSetType) {
////      mappedStatement.resultSetType = resultSetType == null ? ResultSetType.DEFAULT : resultSetType;
////      return this;
////    }
////
////    public Builder cache(Cache cache) {
////      mappedStatement.cache = cache;
////      return this;
////    }
//
//    public Builder flushCacheRequired(boolean flushCacheRequired) {
//      mappedStatement.flushCacheRequired = flushCacheRequired;
//      return this;
//    }
//
//    public Builder useCache(boolean useCache) {
//      mappedStatement.useCache = useCache;
//      return this;
//    }
//
//    public Builder resultOrdered(boolean resultOrdered) {
//      mappedStatement.resultOrdered = resultOrdered;
//      return this;
//    }
//
////    public Builder keyGenerator(KeyGenerator keyGenerator) {
////      mappedStatement.keyGenerator = keyGenerator;
////      return this;
////    }
//
//    public Builder keyProperty(String keyProperty) {
//      mappedStatement.keyProperties = delimitedStringToArray(keyProperty);
//      return this;
//    }
//
//    public Builder keyColumn(String keyColumn) {
//      mappedStatement.keyColumns = delimitedStringToArray(keyColumn);
//      return this;
//    }
//
//    public Builder databaseId(String databaseId) {
//      mappedStatement.databaseId = databaseId;
//      return this;
//    }
//
////    public Builder lang(LanguageDriver driver) {
////      mappedStatement.lang = driver;
////      return this;
////    }
//
//    public Builder resultSets(String resultSet) {
//      mappedStatement.resultSets = delimitedStringToArray(resultSet);
//      return this;
//    }
//
//
//
//    public MappedStatement build() {
//      return mappedStatement;
//    }
//  }
//
//
//  public SqlCommandType getSqlCommandType() {
//    return sqlCommandType;
//  }
//
//  public String getResource() {
//    return resource;
//  }
//
//
//  public String getId() {
//    return id;
//  }
//
//  public boolean hasNestedResultMaps() {
//    return hasNestedResultMaps;
//  }
//
//  public Integer getFetchSize() {
//    return fetchSize;
//  }
//
//  public Integer getTimeout() {
//    return timeout;
//  }
//
//  public StatementType getStatementType() {
//    return statementType;
//  }
//
//
//  public SqlSource getSqlSource() {
//    return sqlSource;
//  }
//
//
//
//
//  public boolean isFlushCacheRequired() {
//    return flushCacheRequired;
//  }
//
//  public boolean isUseCache() {
//    return useCache;
//  }
//
//  public boolean isResultOrdered() {
//    return resultOrdered;
//  }
//
//  public String getDatabaseId() {
//    return databaseId;
//  }
//
//  public String[] getKeyProperties() {
//    return keyProperties;
//  }
//
//  public String[] getKeyColumns() {
//    return keyColumns;
//  }
//
//
//
//  public String[] getResultSets() {
//    return resultSets;
//  }
//
//
//
//  public BoundSql getBoundSql(Object parameterObject) {
//    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
//    List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
//    if (parameterMappings == null || parameterMappings.isEmpty()) {
//      boundSql = new BoundSql(configuration, boundSql.getSql(), parameterMap.getParameterMappings(), parameterObject);
//    }
//
//    // check for nested result maps in parameter mappings (issue #30)
//    for (ParameterMapping pm : boundSql.getParameterMappings()) {
//      String rmId = pm.getResultMapId();
//      if (rmId != null) {
//        ResultMap rm = configuration.getResultMap(rmId);
//        if (rm != null) {
//          hasNestedResultMaps |= rm.hasNestedResultMaps();
//        }
//      }
//    }
//
//    return boundSql;
//  }
//
//  private static String[] delimitedStringToArray(String in) {
//    if (in == null || in.trim().length() == 0) {
//      return null;
//    } else {
//      return in.split(",");
//    }
//  }
//
//}
