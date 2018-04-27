package com.hahuge.myweb.commom.dao.mysql;


import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Maps;
import com.hahuge.myweb.commom.model.search.Filter;
import com.hahuge.myweb.commom.model.search.FilterParser;
import com.hahuge.myweb.commom.model.search.InternalUtil;


public class MysqlFilterParser implements FilterParser{
    
    private String sql;
    
    private Map<String,Object> paramMap = Maps.newHashMap();
    
    private volatile AtomicInteger paramIndex = new AtomicInteger();
    
    private Filter filter;
    
    private MysqlFilterParser(Filter filter){
        this.filter = filter;
    }
    public static MysqlFilterParser parse(Filter filter){
        MysqlFilterParser parser  =  new MysqlFilterParser(filter);
        parser.build();
        return parser;
    }
    
    public static MysqlFilterParser parse(List<Filter> filters){
        Filter allFilter = Filter.Joiner.and(filters);
        return parse(allFilter);
    }
    
    @Override
    public String getConditionParamXql() {
        return sql;
    }
    @Override
    public Map<String, Object> getConditionParamValueMap() {
        return paramMap;
    }
    
    @Override
    public String toString() {
        return String.format("value=[ sql=%s , paramMap=%s ]", sql, paramMap);
    }
    
    private void build(){
        sql = build(this.filter,paramMap,paramIndex);
    }
    
    @SuppressWarnings("unchecked")
	private String build(Filter subFilter,Map<String,Object> parentParamMap ,AtomicInteger paramIndex ){
        String property = subFilter.getProperty();
        String paramArg = subFilter.getProperty();
        Object value = subFilter.getValue();
        int operator = subFilter.getOperator();
        if(subFilter.isTakesValue()){
            paramArg = String.format("%s_%s", paramArg,paramIndex.getAndIncrement());
            parentParamMap.put(paramArg, subFilter.getValue());
        }
        switch (operator) {
            case Filter.OP_IN:
                return "`" + property + "` in (:"+paramArg+")";
            case Filter.OP_NOT_IN:
                return "`" + property + "` not in (:"+paramArg+")";
            case Filter.OP_EQUAL:
                return "`" + property + "` = " + ":"+paramArg;
            case Filter.OP_NOT_EQUAL:
                return "`" + property + "` != " + ":"+paramArg;
            case Filter.OP_GREATER_THAN:
                return "`" + property + "` > " + ":"+paramArg ;
            case Filter.OP_LESS_THAN:
                return "`" + property + "` < " + ":"+paramArg ;
            case Filter.OP_GREATER_OR_EQUAL:
                return "`" + property + "` >= " + ":"+paramArg ;
            case Filter.OP_LESS_OR_EQUAL:
                return "`" + property + "` <= " + ":"+paramArg;
            case Filter.OP_LIKE:
                return "`" + property + "` LIKE "  + ":"+paramArg;
            case Filter.OP_ILIKE:
                return "`" + property + "` ILIKE " + ":"+paramArg;
            case Filter.OP_NULL:
                return "`" + property + "` IS NULL";
            case Filter.OP_NOT_NULL:
                return "`" + property + "` IS NOT NULL";
            case Filter.OP_EMPTY:
                return "`" + property + "` IS EMPTY";
            case Filter.OP_NOT_EMPTY:
                return "`" + property + "` IS NOT EMPTY";
            case OP_AND:
            case OP_OR:
                if (!(value instanceof List)) {
                    return (operator == OP_AND ? "AND: " : "OR: ") + "**INVALID VALUE - NOT A LIST: (" + value
                           + ") **";
                }

                String op = operator == OP_AND ? " and " : " or ";

                StringBuilder sb = new StringBuilder("(");
                boolean first = true;
                for (Object o : ((List<Object>) value)) {
                    if (first) {
                        first = false;
                    } else {
                        sb.append(op);
                    }
                    if (o instanceof Filter) {
                        sb.append(  this.build((Filter)o, parentParamMap, paramIndex) );
                    } else {
                        sb.append("**INVALID VALUE - NOT A FILTER: (" + o + ") **");
                    }
                }
                if (first) return (operator == OP_AND ? "AND: " : "OR: ") + "**EMPTY LIST**";

                sb.append(")");
                return sb.toString();
            case Filter.OP_CUSTOM:
                return property;
            default:
                return "**INVALID OPERATOR: (" + operator + ") - VALUE: " + InternalUtil.paramDisplayString(value)
                       + " **";
        }
    }
}
