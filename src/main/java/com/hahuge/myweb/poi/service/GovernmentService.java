package com.hahuge.myweb.poi.service;

import java.util.List;

import com.hahuge.myweb.commom.service.IService;
import com.hahuge.myweb.poi.model.Summary;

public interface GovernmentService extends IService<Summary,String> {
	public List<Summary> listByVillage(String village);
	
	public List<Summary> getVillages();
	
	public List<Summary> checkData();
}
