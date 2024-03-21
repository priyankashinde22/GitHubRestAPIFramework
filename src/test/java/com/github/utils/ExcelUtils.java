package com.github.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelUtils {
    XSSFWorkbook workbook;
    XSSFSheet sheet;

    public void ExcelUtils(String filename, String sheetName) throws Exception {
        workbook = new XSSFWorkbook(new FileInputStream(filename));
        sheet = workbook.getSheet(sheetName);
    }

    public ArrayList<Object> returnDateOfTheGivenRow(int rowNum) {
        int lastRowNum = sheet.getLastRowNum();
        if (rowNum > lastRowNum) {
            Assert.fail("There is no data for the row :: " + rowNum);
        }
        ArrayList<Object> rowData = new ArrayList<>();
        Row row = sheet.getRow(rowNum);
        int lastCellNum = row.getLastCellNum();
        for (int j = 0; j < lastCellNum; j++) {
            Cell cell = row.getCell(j);
            CellType cellType = cell.getCellType();
            switch (cellType) {
                case STRING:
                    log.info(cell.getStringCellValue() + " ");
                    rowData.add(cell.getStringCellValue());
                    break;
                case BOOLEAN:
                    log.info(cell.getBooleanCellValue() + " ");
                    rowData.add(cell.getBooleanCellValue());
                    break;
                case NUMERIC:
                    log.info(cell.getNumericCellValue() + " ");
                    rowData.add(cell.getNumericCellValue());
                    break;
            }
        }
        return rowData;
    }

    public ArrayList<ArrayList<Object>> returnTheCompleteData() {
        int lastRowNum = sheet.getLastRowNum();
        ArrayList<ArrayList<Object>> matrix = new ArrayList<>();
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            ArrayList<Object> rowData = new ArrayList<>();
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        log.info(cell.getStringCellValue() + " ");
                        rowData.add(cell.getStringCellValue());
                        break;
                    case BOOLEAN:
                        log.info(cell.getBooleanCellValue() + " ");
                        rowData.add(cell.getBooleanCellValue());
                        break;
                    case NUMERIC:
                        log.info(cell.getNumericCellValue() + " ");
                        rowData.add(cell.getNumericCellValue());
                        break;
                }
            }
            matrix.add(rowData);
        }
        return matrix;
    }

    public int getNumberOfRows() {
        return sheet.getLastRowNum();
    }

    public static void main(String[] args) throws IOException, InvalidFormatException {
        File file = new File("Files/Data.xlsx");
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
        XSSFSheet sheet = workbook.getSheet("Sheet1");//To get the specific sheet
        int lastRowNum = sheet.getLastRowNum();//To get the number of rows present
        for (int i = 0; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            int lastCellNum = row.getLastCellNum();
            log.info("");
            for (int j = 0; j < lastCellNum; j++) {
                Cell cell = row.getCell(j);
                CellType cellType = cell.getCellType();
                switch (cellType) {
                    case STRING:
                        log.info(cell.getStringCellValue() + " ");
                        break;
                    case BOOLEAN:
                        log.info(cell.getBooleanCellValue() + " ");
                        break;
                    case NUMERIC:
                        log.info(cell.getNumericCellValue() + " ");
                        break;
                }
            }
        }

        if (workbook.getSheet("Writing") != null) {
            int writingSheetIndex = workbook.getSheetIndex("Writing");
            workbook.removeSheetAt(writingSheetIndex);
        }
        XSSFSheet writingSheet = workbook.createSheet("Writing");
        for (int i = 0; i < 10; i++) {
            Row row = writingSheet.createRow(i);
            for (int j = 0; j < 4; j++) {
                Cell cell = row.createCell(j);
                cell.setCellValue(i + "" + j);
            }
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            workbook.write(fout);
        } catch (FileNotFoundException e) {

        } finally {
            fout.close();
            workbook.close();
        }
    }
}
