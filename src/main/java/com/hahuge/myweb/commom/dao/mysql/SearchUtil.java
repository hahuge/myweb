package com.hahuge.myweb.commom.dao.mysql;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

import com.google.common.base.Joiner;
import com.hahuge.myweb.commom.model.search.Filter;
import com.hahuge.myweb.commom.model.search.Pageable;
import com.hahuge.myweb.commom.model.search.Search;
import com.hahuge.myweb.commom.model.search.Sort;

public class SearchUtil {
    
    
    public static Map<String,Object> generateParamMap(Search search){
        List<Filter> filters = search.getFilters();
        if (null != filters && !filters.isEmpty()) {
            Filter allFilter = Filter.Joiner.and(filters);
           return MysqlFilterParser.parse(allFilter).getConditionParamValueMap();
        }
        return Collections.emptyMap();
    }

    public static String generateSQL(String selectFields, String tableName, Search search, Pageable pageable) {

        if (null == search) {
            search = Search.EMPTY;
        }
        String sql_select = selectFields;
        if (StringUtils.isBlank(sql_select)) {
            List<String> fields = search.getFields();
            if (null != fields && !fields.isEmpty()) {
                sql_select = Joiner.on(",").skipNulls().join(fields);
            } else {
                sql_select = "*";
            }
        }
        if (search.isDistinct()) {
            sql_select = String.format("distinct(%s)", sql_select);
        }

        String sql_from = tableName;
        
       Validate.notBlank(sql_from, "table name must not empty");

        String sql_where = StringUtils.EMPTY;
        List<Filter> filters = search.getFilters();
        if (null != filters && !filters.isEmpty()) {
            Filter allFilter = Filter.Joiner.and(filters);
            sql_where = String.format("where %s",MysqlFilterParser.parse(allFilter).getConditionParamXql());
        }

        String sql_order = StringUtils.EMPTY;

        List<Sort> sorts = search.getSorts();
        if (null != sorts && !sorts.isEmpty()) {

            StringBuilder sb = new StringBuilder("order by ");
            boolean isFirst = true;
            for (Sort sort : sorts) {
                sb.append(String.format("%s %s %s", isFirst ? "" : ",", sort.getProperty(), sort.isDesc() ? "desc" : "asc"));
                isFirst = false;
            }
            sql_order = sb.toString();
        }
        
        String sql_limit = StringUtils.EMPTY;
        
        if(null!=pageable && pageable.getPageSize()!=-1){
            
            sql_limit = String.format("limit %s,%s", pageable.getOffset(),pageable.getPageSize());
        }

        
        
        return String.format("select %s from %s %s %s %s", sql_select,sql_from,sql_where,sql_order,sql_limit);
    }
    
    public static String generateCountSQL(String selectFields, String tableName, Search search, Pageable pageable) {
        if (null == search) {
            search = Search.EMPTY;
        }

        String sql_from = tableName;
        
       Validate.notBlank(sql_from, "table name must empty");

        String sql_where = StringUtils.EMPTY;
        List<Filter> filters = search.getFilters();
        if (null != filters && !filters.isEmpty()) {
            Filter allFilter = Filter.Joiner.and(filters);
            sql_where = String.format("where %s",MysqlFilterParser.parse(allFilter).getConditionParamXql());
        }
        
        return String.format("select count(*) from %s %s ",sql_from,sql_where);
    }
    
    
    
    public static void main(String[] args) {
        
        String selectFields="id,name,age";
        String tableName="user u , org o ";
        Search search=new Search();
        search.setDistinct(true);
        search.addSort(Sort.asc("name"));
        search.addSort(Sort.desc("id"));
        Pageable pageable=new Pageable(1,10);
        
        search.addFilter(Filter.equal("name","songsp"));
        search.addFilter(Filter.greaterOrEqual("age",10));
        search.addFilter(Filter.Joiner.or(Filter.notEqual("name", "zhang"),Filter.like("nick", "song")));
        search.addFilter(Filter.custom("a.id = b.id"));
        
        String sql = SearchUtil.generateSQL(selectFields, tableName, search, pageable);
        
        Map<String,Object> paramMap = SearchUtil.generateParamMap(search);
        System.out.println(sql);
        System.out.println(paramMap);
    }

}
