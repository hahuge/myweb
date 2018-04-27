package com.hahuge.myweb.poi.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@Lazy(false)
public class Constants {

	public static String POI_EXPORT_PATH;
	public static String POI_IMPORT_PATH;

	@Value("${com.hahuge.myweb.poi.export.path}")
	public void setExportPath(String exportVoteResultPath) {
		POI_EXPORT_PATH = exportVoteResultPath;
	}

	@Value("${com.hahuge.myweb.poi.import.path}")
	public void setImportPath(String courseStudyPath) {
		POI_IMPORT_PATH = courseStudyPath;
	}
}
