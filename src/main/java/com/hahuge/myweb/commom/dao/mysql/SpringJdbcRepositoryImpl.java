package com.hahuge.myweb.commom.dao.mysql;

import java.beans.IndexedPropertyDescriptor;
import java.beans.PropertyDescriptor;
import java.beans.Transient;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.hahuge.myweb.commom.dao.Repository;
import com.hahuge.myweb.commom.model.Entity;
import com.hahuge.myweb.commom.model.search.Pageable;
import com.hahuge.myweb.commom.model.search.ResultSet;
import com.hahuge.myweb.commom.model.search.Search;
import com.hahuge.myweb.commom.utils.BeanUtils;

public class SpringJdbcRepositoryImpl<T extends Entity<ID>, ID extends Serializable> implements Repository<T, ID> {

    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    protected SimpleJdbcInsert             jdbcInsert;

    protected DataSource                 dataSource;

    private String                       tableName;

    public SpringJdbcRepositoryImpl(Class<T> clazz){
        this.clazz = clazz;
        // this.clazz = GenericUtils.getSuperClassGenricType(this.getClass());
    }

    public SpringJdbcRepositoryImpl(Class<T> clazz, DataSource dataSource){
        this(clazz, dataSource, null);
    }

    public SpringJdbcRepositoryImpl(Class<T> clazz, DataSource dataSource, String tableName){
        this.clazz = clazz;
        this.tableName = StringUtils.isBlank(tableName) ? clazz.getSimpleName().toLowerCase() : tableName;
        this.setDataSource(dataSource);
    }

    @Override
	public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(this.dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName(this.getTableName());
        this.jdbcInsert.compile();
    }

    @Override
    public T findOne(ID id) {
        String table = this.getTableName();
        String sql = "select * from " + table + " where id=:id";
        Map<String, ID> paramMap = Collections.singletonMap("id", id);
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, paramMap,BeanPropertyRowMapper.newInstance(clazz));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

    }

    @Override
    public int delete(ID id) {
        String table = this.getTableName();
        String sql = "delete from " + table + " where id=:id";
        return namedParameterJdbcTemplate.update(sql, Collections.singletonMap("id", id));
    }

    @Override
    public ID insert(T entity) {
        Validate.notNull(entity.getId(), "insert data id must set value.data=[{}]", entity);
        jdbcInsert.execute(new BeanPropertySqlParameterSource(entity));
        return entity.getId();
    }

    @Override
    @SuppressWarnings("unchecked")
    public ID insert(Map<String, Object> rowMap) {
        Validate.isTrue(null != rowMap && null != rowMap.get(KEY_ID), "insert data and id must not null. data=[{}]",
                rowMap);
        jdbcInsert.execute(rowMap);
        return (ID) rowMap.get(KEY_ID);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public <R> List<R> query(String sql, Map<String, ?> paramMap, Class<R> returnType) {
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(paramMap),
                                                new BeanPropertyRowMapper(returnType));
    }

    @Override
    public List<Map<String, Object>> query(String sql, Map<String, ?> paramMap) {
        return namedParameterJdbcTemplate.queryForList(sql, paramMap);
    }

    @Override
    public List<T> list(Search search) {
        return this.list(search, null);
    }
    
    @Override
    public T listOne(Search search) {
    	Pageable page = new Pageable();
    	page.setPageNumber(1);
    	page.setPageSize(1);
    	List<T>  list =  this.list(search, page);
    	return (null==list || list.isEmpty()) ? null: list.get(0);
    }


    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> listAll() {
        String sql = "select * from " + this.getTableName();
        return namedParameterJdbcTemplate.query(sql, Collections.EMPTY_MAP, new BeanPropertyRowMapper(this.clazz));
    }

    @Override
    public ResultSet<T> listAndCount(Search search, Pageable pageable) {
        List<T> list = list(search, pageable);
        long total = count(search);
        ResultSet<T> rs = new ResultSet<T>(list, pageable, total);
        return rs;
    }

    @Override
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<T> list(Search search, Pageable pageable) {
        if (null == search) {
            return listAll();
        }
        String sql = SearchUtil.generateSQL(null, this.getTableName(), search, pageable);
        Map<String, Object> paramMap = SearchUtil.generateParamMap(search);
        return namedParameterJdbcTemplate.query(sql, paramMap, new BeanPropertyRowMapper(this.clazz));
    }

    @Override
    public long count(Search search) {
        if (null == search) {
            return this.count();
        }
        String sql = SearchUtil.generateCountSQL(null, this.getTableName(), search, null);
        Map<String, Object> paramMap = SearchUtil.generateParamMap(search);
        return namedParameterJdbcTemplate.queryForObject(sql, paramMap, Long.class);
    }

    @Override
    public int update(String sql, Map<String, ?> paramMap) {
        return namedParameterJdbcTemplate.update(sql, paramMap);
    }

    @Override
    public int updateById(ID id, Map<String, ?> paramMap) {
        if (null == id || MapUtils.isEmpty(paramMap)) {
            return 0;
        }
        if (paramMap.containsKey(KEY_ID)) {
            paramMap.remove(KEY_ID);
        }
        List<String> sets = new ArrayList<String>(paramMap.size());
        for (String key : paramMap.keySet()) {
            sets.add(String.format(" %s=:%s ", key, key));
        }

        //String idStr = NumberUtils.isNumber(String.valueOf(id)) ? String.valueOf(id) : String.format("'%s'", id);
        String idStr = (id instanceof Number) ? String.valueOf(id):String.format("'%s'", id);
        
        String sql_set = Joiner.on(",").join(sets);
        String sql = String.format("update %s set %s where id=%s", this.getTableName(), sql_set, idStr);
        return this.update(sql, paramMap);
    }

    @Override
    public int update(T t) {

        if (null == t || null == t.getId()) {
            return 0;
        }
        Map<String, Object> map = BeanUtils.toMap(t);

        //过滤没有读的属性 和 忽略的属性
        List<String> delkeys = Lists.newArrayList();
        for (String key :  map.keySet()) {
            if( !isReadable(t, key)){
                delkeys.add(key);
            }
        }
        for (String key : delkeys) {
			map.remove(key);
		}
        
        return this.updateById(t.getId(), map);
    }

    //copy PropertyUtils.isReadable
    private static boolean isReadable(Object bean, String name) {
        if(bean == null) {
            throw new IllegalArgumentException("No bean specified");
        } else if(name == null) {
            throw new IllegalArgumentException("No name specified");
        } else if(bean instanceof DynaBean) {
            return ((DynaBean)bean).getDynaClass().getDynaProperty(name) != null;
        } else {
            try {
                PropertyDescriptor e = PropertyUtils.getPropertyDescriptor(bean, name);
                if(e != null) {
                    Method e1 = e.getReadMethod();
                    if(e1 == null && e instanceof IndexedPropertyDescriptor) {
                        e1 = ((IndexedPropertyDescriptor)e).getIndexedReadMethod();
                    }

                    if(null==e1){
                        return false;
                    }

                    // 添加逻辑，是否有排查注解
                    Transient aTransient = e1.getAnnotation(Transient.class);
                    if(null!=aTransient){
                        return false;
                    }

                    return e1 != null;
                } else {
                    return false;
                }
            } catch (IllegalAccessException var5) {
                return false;
            } catch (InvocationTargetException var6) {
                return false;
            } catch (NoSuchMethodException var7) {
                return false;
            }
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public long count() {
        String sql = "select count(*) from " + this.getTableName();
        return namedParameterJdbcTemplate.queryForObject(sql, Collections.EMPTY_MAP, Long.class);
    }

    public NamedParameterJdbcOperations getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    protected String getTableName() {
        return this.tableName;
    }

    protected Class<T> clazz;
    
}
