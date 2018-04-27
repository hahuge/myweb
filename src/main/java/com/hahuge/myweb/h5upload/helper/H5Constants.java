package com.hahuge.myweb.h5upload.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class H5Constants {

	public static String H5_UPLOAD_PATH;

	@Value("${com.hahuge.myweb.img.upload.path}")
	public void setImportPath(String courseStudyPath) {
		H5_UPLOAD_PATH = courseStudyPath;
	}
}
