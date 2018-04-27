package com.hahuge.myweb.poi.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.hahuge.myweb.commom.dao.Repository;
import com.hahuge.myweb.commom.service.BaseServiceImpl;
import com.hahuge.myweb.poi.model.Summary;
import com.hahuge.myweb.poi.repository.SpecialRepository;
import com.hahuge.myweb.poi.service.GovernmentService;
@Service("governmentServiceImpl")
public class GovernmentServiceImpl extends BaseServiceImpl<Summary, String> implements GovernmentService {
	
	@Resource(name = "summaryRepository")
	private Repository<Summary, String> summaryRepository;
	
	@Resource(name = "specialRepository")
	SpecialRepository specialRepository;
	
	@Override
	public Repository<Summary, String> getRepository() {
		return summaryRepository;
	}

	@Override
	public List<Summary> listByVillage(String village) {
		return specialRepository.listByVillage(village);
	}

	@Override
	public List<Summary> getVillages() {
		return specialRepository.getVillages();
	}

	@Override
	public List<Summary> checkData() {
		return specialRepository.checkData();
	}

}
