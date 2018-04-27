package com.hahuge.myweb.poi.repository;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hahuge.myweb.commom.dao.Repository;
import com.hahuge.myweb.commom.dao.mysql.SpringJdbcRepositoryImpl;
import com.hahuge.myweb.poi.model.Summary;

@Configuration("governmentRepositoryConfig")
public class RepositoryConfig {

    @Resource(name = "app.myweb.dataSource")
    private DataSource dataSource;

    @Bean(name = "summaryRepository")
    public Repository<Summary, String> getSummaryRepository() {
        SpringJdbcRepositoryImpl<Summary, String> summaryRepository = new SpringJdbcRepositoryImpl<Summary, String>(
        		Summary.class, dataSource, "poi_government");
        return summaryRepository;
    }
    //自定义
    @Bean(name = "specialRepository")
    public SpecialRepository getSpecialRepository() {
        SpecialRepository specialRepository = new SpecialRepository(Summary.class, dataSource, "poi_government");
        return specialRepository;
    }
}
