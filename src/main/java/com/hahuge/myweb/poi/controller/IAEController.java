package com.hahuge.myweb.poi.controller;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hahuge.myweb.commom.PathServlet;
import com.hahuge.myweb.commom.dao.IdGenerator;
import com.hahuge.myweb.commom.model.search.Search;
import com.hahuge.myweb.poi.helper.Constants;
import com.hahuge.myweb.poi.model.Summary;
import com.hahuge.myweb.poi.service.GovernmentService;
import com.hahuge.myweb.poi.utils.ExcelUtils;

@Controller
@RequestMapping(value = "/poi/")
public class IAEController {
	
	@Resource(name = "governmentServiceImpl")
	private GovernmentService governmentService;
	
	@RequestMapping(value = "readExcel")
	public String importExcel(HttpServletRequest request) throws Exception{
		String filePath = request.getParameter("path");
		List<List<Object>> lists = ExcelUtils.readExcel(filePath);
		for(List<Object> list : lists) {
			Search search = Search.newInstance();
			search.addEqualFilter("name", list.get(0));
			search.addEqualFilter("area", new DecimalFormat("#,##0.00").format(new Double(list.get(1)+""))+"");
			search.addEqualFilter("village", list.get(3));
			search.addEqualFilter("year", list.get(4));
			List<Summary> li = governmentService.list(search);
			if(li == null || li.size() == 0){
				Summary summary = new Summary();
				summary.setId(IdGenerator.uuid32());
				summary.setName((String) list.get(0));
				summary.setArea(new DecimalFormat("#,##0.00").format(new Double(list.get(1)+""))+"");
				summary.setAccNum((String) list.get(2));
				summary.setVillage((String) list.get(3));
				summary.setYear((String) list.get(4));
				governmentService.insert(summary);
			}
		}
		return "redirect:/index.xhtml";
	}
	
	@RequestMapping(value = "exportExcel")
	@ResponseBody
	public Map<String,Object> exportExcel(HttpServletRequest request) throws IOException{
		String village = request.getParameter("village");
		String excelPath = String.format("%s%s", PathServlet.PATH, Constants.POI_EXPORT_PATH);
		List<Summary> list = governmentService.listByVillage(village);
		String path = ExcelUtils.writeExcel(list, excelPath, village+".xlsx");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("path", path);
		return map;
	}
	
	@RequestMapping(value = "listvillages")
	@ResponseBody
	public List<String> listVillages(){
		List<Summary> list = governmentService.getVillages();
		List<String> li = new ArrayList<String>();
		for(Summary summary : list){
			li.add(summary.getVillage());
		}
		return li;
	}
	
	@RequestMapping(value = "uploadExcel")
	public String uploadExcel(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model){
		String path = request.getSession().getServletContext().getRealPath("/")+Constants.POI_IMPORT_PATH;
		String fileName = request.getParameter("fileName");
        File targetFile = new File(path, fileName);    
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }
        try {
            file.transferTo(targetFile);
        } catch (Exception e) {    
            e.printStackTrace();    
        }    
        model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);  
        model.addAttribute("path", path+"\\"+fileName);
        return "redirect:readExcel.xhtml";
	}
	
	@RequestMapping(value = "del")
	@ResponseBody
	public Integer del(HttpServletRequest request){
		String id = request.getParameter("id");
		if(StringUtils.isNotBlank(id)){
			return governmentService.delete(id);
		}
		return null;
	}
	
	@RequestMapping(value = "checkData")
	@ResponseBody
	public List<Summary> checkData(){
		List<Summary> list = governmentService.checkData();
		if(list != null && list.size()>0){
			return list;
		}
		return null;
	}
}
