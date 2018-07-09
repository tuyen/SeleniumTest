package com.tuyennta.automation.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelHelper {
	/**
	 * Function open and return a Workbook with createNew option, if true will
	 * create a backup file and create new file
	 * 
	 * @param filePath
	 * @param createNew
	 * @return
	 * @throws EncryptedDocumentException
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public static Workbook openWorkbook(String filePath)
			throws EncryptedDocumentException, InvalidFormatException, IOException {
		File file = new File(filePath);
		FileInputStream fis = new FileInputStream(file);
		Workbook wb = WorkbookFactory.create(fis);
		fis.close();
		return wb;
	}
	
	/**
	 * Get cell value as string
	 * @param workbook
	 * @param cell
	 * @return
	 */
	public static String getCellValueAsString(Workbook workbook, Cell cell) {
		FormulaEvaluator formula = workbook.getCreationHelper().createFormulaEvaluator();
		String value = "";
		if (cell != null) {
			if (cell.getCellTypeEnum().equals(CellType.FORMULA)) {
				value = getCellValue(formula.evaluate(cell));
			} else {
				cell.setCellType(CellType.STRING);
				value = cell.getRichStringCellValue().getString();
			}
		}
		return value;
	}
	
	/**
	 * Function get value of CellValue as a string
	 * 
	 * @param cellValue
	 * @return
	 */
	private static String getCellValue(CellValue cellValue) {
		String value = null;
		if (cellValue.getCellTypeEnum().equals(CellType.NUMERIC)) {
			value = String.valueOf(cellValue.getNumberValue()).replaceFirst("\\.0+$", "");
		} else if (cellValue.getCellTypeEnum().equals(CellType.STRING)) {
			value = cellValue.getStringValue();
		} else if (cellValue.getCellTypeEnum().equals(CellType.BOOLEAN)) {
			value = String.valueOf(cellValue.getBooleanValue());
		} else {
			value = cellValue.formatAsString();
		}
		return value;
	}
}
