package com.hahuge.myweb.commom.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hahuge.myweb.commom.dao.Repository;
import com.hahuge.myweb.commom.model.Entity;
import com.hahuge.myweb.commom.model.search.Pageable;
import com.hahuge.myweb.commom.model.search.ResultSet;
import com.hahuge.myweb.commom.model.search.Search;

public interface IService<T extends Entity<ID>, ID extends Serializable> {

    Repository<T, ID> getRepository();

    T findOne(ID id);

    int delete(ID id);

    ID insert(T entity);

    ID insert(Map<String, Object> rowMap);

    T findOne(Search search);

    List<T> list(Search search);

    List<T> listAll();

    ResultSet<T> listAndCount(Search search, Pageable pageable);

    List<T> list(Search search, Pageable pageable);

    long count(Search search);

    long count();
    int update(T t);

}
