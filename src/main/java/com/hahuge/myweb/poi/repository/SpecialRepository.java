package com.hahuge.myweb.poi.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import com.hahuge.myweb.commom.dao.mysql.SpringJdbcRepositoryImpl;
import com.hahuge.myweb.poi.model.Summary;


public class SpecialRepository extends SpringJdbcRepositoryImpl<Summary, String> {

    public SpecialRepository(Class<Summary> clazz, DataSource dataSource, String tableName) {
        super(clazz, dataSource, tableName);
    }
    
    public List<Summary> listByVillage(String village){
    	String sql1 = "select pg.name,SUM(pg.area) as area,pg.accNum,pg.village from poi_government";
    	String sql2 = " pg group by pg.name,pg.accNum,pg.village having pg.village= :village";
    	Map<String,String> map = new HashMap<String,String>();
    	map.put("village", village);
    	List<Summary> list = this.query(sql1+sql2, map, Summary.class);
    	return list;
    }
    
    public List<Summary> getVillages(){
    	String sql = "select distinct village from poi_government";
    	List<Summary> list = this.query(sql, null, Summary.class);
		return list;
    }
    
    public List<Summary> checkData(){
    	String sql ="select * from poi_government where name is null or area='0.00' or accNum is null or village is null or year is null";
    	List<Summary> list = this.query(sql, null, Summary.class);
    	return list;
    }
}