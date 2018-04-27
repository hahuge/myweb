package com.hahuge.myweb.h5upload.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hahuge.myweb.h5upload.helper.H5Constants;

@Controller
public class UploadController {
	@RequestMapping(value = "h5upload")
	public String index(){
		return "h5upload/upload";
	}
	
	@RequestMapping(value = "upload")
	@ResponseBody
	public Map<String, Object> upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request, ModelMap model){
		String path = request.getSession().getServletContext().getRealPath("/")+H5Constants.H5_UPLOAD_PATH;
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
//      model.addAttribute("fileUrl", request.getContextPath()+"/upload/"+fileName);  
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("path", H5Constants.H5_UPLOAD_PATH+fileName);
        return map;
	}
}
