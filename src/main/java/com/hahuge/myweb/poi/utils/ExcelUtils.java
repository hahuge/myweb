package com.hahuge.myweb.poi.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.hahuge.myweb.poi.helper.Constants;
import com.hahuge.myweb.poi.model.Summary;

/**
 * @author Hahuge 操作Excel
 */
public class ExcelUtils {
	/**
	 * 读取Excel姓名，面积，一卡通账号
	 * 
	 * @param filePath
	 *            读取文件路径
	 * @return
	 * @throws Exception
	 */
	public static List<List<Object>> readExcel(String filePath) throws Exception {
		File file = new File(filePath);
		if (!file.exists()) {
			throw new Exception("找不到文件");
		}
		InputStream stream = new FileInputStream(filePath);
		String fileName = file.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		Workbook xwb = null;
		if (prefix.equals("xls")) {
			xwb = new HSSFWorkbook(stream);
			xwb.getSheetAt(0);
		} else if (prefix.equals("xlsx")) {
			xwb = new XSSFWorkbook(stream);
		} else {
			System.out.println("您输入的excel格式不正确");
		}
		List<List<Object>> list = new LinkedList<List<Object>>();
		Row row = null;
		Cell cell = null;
		int sheets = xwb.getNumberOfSheets();
		for (int k = 0; k < sheets; k++) {
			Sheet sheet = xwb.getSheetAt(k);
			for (int i = (sheet.getFirstRowNum() + 5); i <= (sheet.getPhysicalNumberOfRows() - 1); i++) {
				row = sheet.getRow(i);
				if (row == null) {
					continue;
				}
				List<Object> linked = new LinkedList<Object>();
				for (int j = row.getFirstCellNum(); j <= row.getLastCellNum(); j++) {
					Object value = null;
					cell = row.getCell(j);
					if (cell == null) {
						continue;
					}
					switch (cell.getCellType()) {
					case XSSFCell.CELL_TYPE_STRING:
						value = cell.getStringCellValue();
						break;
					case XSSFCell.CELL_TYPE_NUMERIC:
						// 日期数据返回LONG类型的时间戳
						if ("yyyy\"年\"m\"月\"d\"日\";@".equals(cell.getCellStyle().getDataFormatString())) {
							// System.out.println(cell.getNumericCellValue()+":日期格式："+cell.getCellStyle().getDataFormatString());
							// value =
							// DateUtils.getMillis(HSSFDateUtil.getJavaDate(cell.getNumericCellValue()))/1000;
						} else {
							value = cell.getNumericCellValue();
						}
						break;
					case XSSFCell.CELL_TYPE_BOOLEAN:
						value = cell.getBooleanCellValue();
						break;
					case XSSFCell.CELL_TYPE_FORMULA:
						value = cell.getNumericCellValue();
						break;
					case XSSFCell.CELL_TYPE_BLANK:
						// 空单元格
						break;
					default:
						value = cell.toString();
					}
					if (j == 1 || j == 6 || j == 13) {
						linked.add(value);
					}
				}
				linked.add(xwb.getSheetName(k));
				linked.add(fileName.substring(0, fileName.lastIndexOf(".")));
				if (linked.get(0) != null) {
					list.add(linked);
				}
			}
		}
		return list;
	}

	/**
	 * 导出Excel
	 * 
	 * @param list
	 * @param excelPath
	 * @throws IOException
	 */
	public static String writeExcel(List<Summary> list, String path, String excelName) throws IOException {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);// 水平居中
		XSSFSheet sheet = workbook.createSheet();
		sheet.setColumnWidth(0, 6500);
		sheet.setColumnWidth(1, 3000);
		sheet.setColumnWidth(2, 3000);
		sheet.setColumnWidth(3, 7000);
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell0 = row.createCell(0);
		cell0.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell0.setCellValue("农户编号");
		cell0.setCellStyle(cellStyle);
		XSSFCell cell1 = row.createCell(1);
		cell1.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell1.setCellValue("姓名");
		cell1.setCellStyle(cellStyle);
		XSSFCell cell2 = row.createCell(2);
		cell2.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell2.setCellValue("面积");
		cell2.setCellStyle(cellStyle);
		XSSFCell cell3 = row.createCell(3);
		cell3.setCellType(XSSFCell.CELL_TYPE_STRING);
		cell3.setCellValue("银行账号");
		cell3.setCellStyle(cellStyle);
		for (int n = 0; n < list.size(); n++) {
			XSSFRow row_value = sheet.createRow(n + 1);
			Summary summary = list.get(n);
			for (int i = 0; i < row.getLastCellNum(); i++) {
				XSSFCell cell = row_value.createCell(i);
				cell.setCellStyle(cellStyle);
				cell.setCellType(XSSFCell.CELL_TYPE_STRING);
				switch (i) {
				case 0:
					break;
				case 1:
					cell.setCellValue(summary.getName());
					break;
				case 2:
					cell.setCellValue(summary.getArea());
					break;
				case 3:
					cell.setCellValue(summary.getAccNum());
					break;
				default:
					break;
				}
			}
		}
		FileOutputStream fos = new FileOutputStream(path + excelName);
		workbook.write(fos);
		fos.flush();
		fos.close();
		return Constants.POI_EXPORT_PATH + excelName;
	}
}
