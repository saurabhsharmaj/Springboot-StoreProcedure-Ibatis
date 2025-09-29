package com.bit.sp.utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ExcelExportUtil {

	/**
	 * Generic method to export any list of objects to Excel
	 * 
	 * @param response   HttpServletResponse
	 * @param dataList   List of objects to export
	 * @param headers    Column headers for Excel
	 * @param fieldNames Field names in the object (must match object properties)
	 * @param fileName   Name of the Excel file (without extension)
	 */
	public <T> void exportToExcel(HttpServletResponse response, List<T> dataList, List<String> headers,
			List<String> fieldNames, String fileName) throws IOException {

		if (dataList == null || dataList.isEmpty()) {
			throw new IllegalArgumentException("Data list cannot be null or empty");
		}

		try (Workbook workbook = new XSSFWorkbook()) {
			Sheet sheet = workbook.createSheet("Report");

			// Create header style
			CellStyle headerStyle = createHeaderStyle(workbook);
			// Create data style
			CellStyle dataStyle = createDataStyle(workbook);

			// Create header row
			createHeaderRow(sheet, headers, headerStyle);

			// Create data rows
			createDataRows(sheet, dataList, fieldNames, dataStyle);

			// Auto-size columns
			autoSizeColumns(sheet, headers.size());

			// Set response headers
			setupResponse(response, fileName);

			// Write workbook to response
			workbook.write(response.getOutputStream());
		}
	}

	/**
	 * Simplified method for exporting with default field mapping
	 */
	public <T> void exportToExcel(HttpServletResponse response, List<T> dataList, String fileName) throws IOException {

		if (dataList == null || dataList.isEmpty()) {
			throw new IllegalArgumentException("Data list cannot be null or empty");
		}

		// Use reflection to get field names and headers
		Class<?> clazz = dataList.get(0).getClass();
		Field[] fields = clazz.getDeclaredFields();

		List<String> headers = Arrays.stream(fields).map(Field::getName).map(this::capitalize)
				.collect(Collectors.toList());

		List<String> fieldNames = Arrays.stream(fields).map(Field::getName).collect(Collectors.toList());

		exportToExcel(response, dataList, headers, fieldNames, fileName);
	}

	private CellStyle createHeaderStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		Font font = workbook.createFont();
		font.setBold(true);
		font.setColor(IndexedColors.WHITE.getIndex());
		style.setFont(font);
		style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		return style;
	}

	private CellStyle createDataStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(HorizontalAlignment.LEFT);
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setWrapText(true);
		return style;
	}

	private void createHeaderRow(Sheet sheet, List<String> headers, CellStyle headerStyle) {
		Row headerRow = sheet.createRow(0);
		for (int i = 0; i < headers.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(headerStyle);
		}
	}

	private <T> void createDataRows(Sheet sheet, List<T> dataList, List<String> fieldNames, CellStyle dataStyle) {
		int rowNum = 1;
		for (T item : dataList) {
			Row row = sheet.createRow(rowNum++);
			createDataRow(row, item, fieldNames, dataStyle);
		}
	}

	private <T> void createDataRow(Row row, T item, List<String> fieldNames, CellStyle dataStyle) {
		for (int i = 0; i < fieldNames.size(); i++) {
			Cell cell = row.createCell(i);
			try {
				String fieldName = fieldNames.get(i);
				Field field = item.getClass().getDeclaredField(fieldName);
				field.setAccessible(true);
				Object value = field.get(item);
				setCellValue(cell, value);
			} catch (Exception e) {
				cell.setCellValue("Error");
			}
			cell.setCellStyle(dataStyle);
		}
	}

	private void setCellValue(Cell cell, Object value) {
		if (value == null) {
			cell.setCellValue("");
		} else if (value instanceof String) {
			cell.setCellValue((String) value);
		} else if (value instanceof Number) {
			cell.setCellValue(((Number) value).doubleValue());
		} else if (value instanceof Boolean) {
			cell.setCellValue((Boolean) value);
		} else if (value instanceof Date) {
			cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) value));
		} else {
			cell.setCellValue(value.toString());
		}
	}

	private void autoSizeColumns(Sheet sheet, int columnCount) {
		for (int i = 0; i < columnCount; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private void setupResponse(HttpServletResponse response, String fileName) {
		String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String fullFileName = fileName + "_" + timestamp + ".xlsx";

		response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fullFileName + "\"");
		response.setHeader(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
		response.setHeader(HttpHeaders.PRAGMA, "no-cache");
		response.setHeader(HttpHeaders.EXPIRES, "0");
	}

	private String capitalize(String str) {
		if (str == null || str.isEmpty())
			return str;
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
}