package com.duggan.workflow.shared.model.reports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.duggan.workflow.shared.model.DocumentLine;
import com.duggan.workflow.shared.model.catalog.Catalog;
import com.duggan.workflow.shared.model.catalog.CatalogColumn;

public class GenerateCatalogueExcel {

	ArrayList<CatalogColumn> columns = new ArrayList<CatalogColumn>();
	
	ArrayList<DocumentLine> docLines = new ArrayList<>();

	public GenerateCatalogueExcel() {

	}

	private Workbook wb = new HSSFWorkbook();
	
	static HashMap<String, CellStyle> styles = null;
	static HashMap<String, Font> fonts = null;

	public GenerateCatalogueExcel(Catalog catalog, ArrayList<DocumentLine> data) {
		columns = catalog.getColumns();
		docLines = data;
	}

	private HashMap<String, CellStyle> createStyles(Workbook wb) {
		HashMap<String, CellStyle> styles = new HashMap<String, CellStyle>();
		DataFormat df = wb.createDataFormat();

		CellStyle style;

		Font sheetHeadingFont = wb.createFont();
		sheetHeadingFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		sheetHeadingFont.setFontHeightInPoints((short) 16);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(sheetHeadingFont);
		styles.put("sheet_heading", style);

		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		headerFont.setFontHeightInPoints((short) 14);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		styles.put("header", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setFont(headerFont);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("header_date", style);

		Font font1 = wb.createFont();
		font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font1);
		style.setWrapText(true);
		styles.put("cell_b", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font1);
		styles.put("cell_b_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_b_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setDataFormat(df.getFormat("#,##0.0")); //
		styles.put("cell_currency", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_g", style);

		Font font2 = wb.createFont();
		font2.setColor(IndexedColors.BLUE.getIndex());
		font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font2);
		styles.put("cell_bb", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setFont(font1);
		style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_bg", style);

		Font font3 = wb.createFont();
		font3.setFontHeightInPoints((short) 14);
		font3.setColor(IndexedColors.DARK_BLUE.getIndex());
		font3.setBoldweight(Font.BOLDWEIGHT_BOLD);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(font3);
		style.setWrapText(true);
		styles.put("cell_h", style);

		Font subHeaderFont = wb.createFont();
		subHeaderFont.setItalic(true);
		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setFont(subHeaderFont);
		style.setWrapText(true);
		styles.put("cell_h_sub", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		style.setWrapText(true);
		styles.put("cell_normal", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setWrapText(true);
		styles.put("cell_normal_centered", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_RIGHT);
		style.setWrapText(true);
		style.setDataFormat(df.getFormat("d-mmm"));
		styles.put("cell_normal_date", style);

		style = createBorderedStyle(wb);
		style.setAlignment(CellStyle.ALIGN_LEFT);
		style.setIndention((short) 1);
		style.setWrapText(true);
		styles.put("cell_indented", style);

		style = createBorderedStyle(wb);
		style.setFillForegroundColor(IndexedColors.BLUE.getIndex());
		style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		styles.put("cell_blue", style);

		return styles;
	}

	private CellStyle createBorderedStyle(Workbook wb2) {
		CellStyle style = wb.createCellStyle();
		style.setVerticalAlignment(CellStyle.VERTICAL_TOP);
		return style;
	}

	private HashMap<String, Font> createFonts(Workbook wb2) {
		fonts = new HashMap<>();
		Font headerFont = wb.createFont();
		headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
		fonts.put("header", headerFont);

		Font font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(IndexedColors.GREEN.index);
		fonts.put("font-success", font);

		font = wb.createFont();
		font.setBoldweight(Font.BOLDWEIGHT_BOLD);
		font.setColor(Font.COLOR_RED);
		fonts.put("font-failure", font);

		font = wb.createFont();
		font.setColor(IndexedColors.VIOLET.index);
		fonts.put("_nodata", font);

		font = wb.createFont();
		font.setColor(IndexedColors.BLACK.index);
		fonts.put("default", font);

		return fonts;
	}

	private void generate(Workbook workbook, String sheetName, ArrayList<DocumentLine> data) {
		Sheet sheet = workbook.createSheet(sheetName);

		// sheet.setDisplayGridlines(false);
		// sheet.setPrintGridlines(false);
		sheet.setFitToPage(true);
		sheet.setHorizontallyCenter(true);
		PrintSetup printSetup = sheet.getPrintSetup();
		printSetup.setLandscape(true);

		// the following three statements are required only for HSSF
		sheet.setAutobreaks(true);
		printSetup.setFitHeight((short) 1);
		printSetup.setFitWidth((short) 1);

		// the header row: centered text in 48pt font

		// Row headingRow = sheet.createRow(0);
		// headingRow.setHeightInPoints(30f);
		// Cell headingCell = headingRow.createCell(0);
		// headingCell.setCellStyle(styles.get("sheet_heading"));
		// headingCell.setCellValue(sheetName);
		// sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 7));

		Row headerRow = sheet.createRow(0);
		headerRow.setHeightInPoints(30f);
		for (int i = 0; i < columns.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(columns.get(i).getName());
			cell.setCellStyle(styles.get("header"));
		}

		CreationHelper helper = wb.getCreationHelper();
		int rownum = 1;
		paintRows(data, rownum, helper, sheet);

		sheet.setColumnWidth(0, 256 * 10);// ICPAK Member
		sheet.setColumnWidth(1, 256 * 10);// MemberNo
		sheet.setColumnWidth(2, 256 * 10);// ERN NO
		sheet.setColumnWidth(3, 256 * 40);// DelegateNames
		sheet.setColumnWidth(4, 256 * 20);// Delegate Email
		sheet.setColumnWidth(5, 256 * 20);// Funding/ Estimate
		sheet.setColumnWidth(6, 256 * 40);// Actual
		sheet.setColumnWidth(7, 256 * 20);// Status
		sheet.setColumnWidth(8, 256 * 20);// Remarks
		sheet.setColumnWidth(9, 256 * 10);// Monitoring test
		sheet.setZoom(3, 4);
	}

	private int paintRows(ArrayList<DocumentLine> data, int rownum, CreationHelper helper, Sheet sheet) {
		if (data == null || data.isEmpty()) {
			return rownum - 1;
		}

		for (Integer i = 0; i < data.size(); i++, rownum++) {
			Row row = sheet.createRow(rownum);
			DocumentLine detail = data.get(i);
			bindData(detail, helper, sheet, row, i);

			// rownum = paintRows(data, rownum + 1, helper, sheet);
		}

		return --rownum;
	}

	private void bindData(DocumentLine detail, CreationHelper helper, Sheet sheet, Row row, Integer i) {
		SimpleDateFormat formater = new SimpleDateFormat("dd/MM/yyyy");

		for (int j = 0; j <  columns.size(); j++) {
			Cell cell = row.createCell(j);
			String styleName = null;

			if (j == 0) {
//				cell.setCellValue((detail.getMemberNo() != null ? "1" : "0"));
			}
			
			styleName = "cell_normal_centered";
			cell.setCellStyle(styles.get(styleName));
		}

	}

	public byte[] getBytes() throws IOException {
		ByteArrayOutputStream byteo = new ByteArrayOutputStream();
		wb.write(byteo);
		return byteo.toByteArray();
	}

}
