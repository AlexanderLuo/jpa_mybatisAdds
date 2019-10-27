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
//package org.share.jpa.adds.builder;
//
//
//import org.share.jpa.adds.scripting.DynamicContext;
//import org.share.jpa.adds.scripting.SqlNode;
//
///**
// * @author Clinton Begin
// */
//public class DynamicSqlSource implements SqlSource {
//
//  private final SqlNode rootSqlNode;
//
//  public DynamicSqlSource(SqlNode rootSqlNode) {
//    this.rootSqlNode = rootSqlNode;
//  }
//
//  @Override
//  public BoundSql getBoundSql(Object parameterObject) {
//    DynamicContext context = new DynamicContext(parameterObject);
//    rootSqlNode.apply(context);
//
////    SqlSourceBuilder sqlSourceParser = new SqlSourceBuilder(configuration);
////    Class<?> parameterType = parameterObject == null ? Object.class : parameterObject.getClass();
////    SqlSource sqlSource = sqlSourceParser.parse(context.getSql(), parameterType, context.getBindings());
////    BoundSql boundSql = sqlSource.getBoundSql(parameterObject);
////    context.getBindings().forEach(boundSql::setAdditionalParameter);
//    return boundSql;
//  }
//
//}
