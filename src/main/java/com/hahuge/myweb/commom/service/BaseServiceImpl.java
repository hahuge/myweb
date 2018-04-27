package com.hahuge.myweb.commom.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.hahuge.myweb.commom.dao.Repository;
import com.hahuge.myweb.commom.model.Entity;
import com.hahuge.myweb.commom.model.search.Pageable;
import com.hahuge.myweb.commom.model.search.ResultSet;
import com.hahuge.myweb.commom.model.search.Search;



public abstract class BaseServiceImpl<T extends Entity<ID>, ID extends Serializable> implements IService<T, ID> {

   // protected Repository<T, ID> repository;


    @Override
	public abstract Repository<T, ID> getRepository();

    @Override
	public T findOne(ID id) {
        return getRepository().findOne(id);
    }

    @Override
    public int delete(ID id) {
        return getRepository().delete(id);
    }

    @Override
    public ID insert(T entity) {
        return getRepository().insert(entity);
    }

    @Override
    public ID insert(Map<String, Object> rowMap) {
        return getRepository().insert(rowMap);
    }

    @Override
    public T findOne(Search search){
        List<T> list = this.list(search,new Pageable(1,1));
        if(null==list || list.size()<1){
            return null;
        }
        return list.get(0);
    }

    @Override
    public List<T> list(Search search) {
        return getRepository().list(search);
    }

    @Override
    public List<T> listAll() {
        return getRepository().listAll();
    }

    @Override
    public ResultSet<T> listAndCount(Search search, Pageable pageable) {
        return getRepository().listAndCount(search, pageable);
    }

    @Override
    public List<T> list(Search search, Pageable pageable) {
        return getRepository().list(search, pageable);
    }

    @Override
    public long count(Search search) {
        return getRepository().count(search);
    }

    @Override
    public long count() {
        return getRepository().count();
    }
    
    @Override
    public  int update(T t){
    	return getRepository().update(t);
    }

}
