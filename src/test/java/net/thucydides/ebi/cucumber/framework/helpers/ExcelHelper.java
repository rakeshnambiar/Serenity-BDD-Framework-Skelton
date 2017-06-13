package net.thucydides.ebi.cucumber.framework.helpers;

import com.monitorjbl.xlsx.StreamingReader;
import net.thucydides.ebi.cucumber.framework.hooks.ScenarioHook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.ParseException;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;


public class ExcelHelper {

    public static String ReadExcelData(String SheetName, String ParmName) throws Exception {

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/TestData.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheet(SheetName);
            Iterator<Row> rowIterator = worksheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0).getStringCellValue().equalsIgnoreCase(ParmName)) {
                    if (row.getCell(1)!=null)
                        if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            return String.valueOf(row.getCell(1).getNumericCellValue());
                        } else {
                            return row.getCell(1).getStringCellValue();
                        }
                    }
            }
        } catch (Exception e) {
            throw new Exception("Failed to retrieve value from test data xlsx \n" + e.getMessage());
        } finally {
            fileInputStream.close();
        }
        return null;
    }

    /**
     * Author - Rakesh
     * To fetch the multiple values from excel
     * @SheetName  Name of the excel sheet
     * @ParmName   parameter name in the excel sheet in single quote seperated by comma
     * @return String array
     * @throws ParseException
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static String[] ReadExcelValues(String SheetName, String ParmName) throws Exception {
        int arrLength = 1;
        String[] parmFields = ParmName.split(",");
        if (parmFields.length > 1) {
            arrLength = parmFields.length;
        }
        String[] value = new String[arrLength];
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/TestData.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheet(SheetName);
            for (int i = 0; i < arrLength; i++) {
                Iterator<Row> rowIterator = worksheet.iterator();
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getCell(0).getStringCellValue().equalsIgnoreCase(parmFields[i].replaceAll("'", "").trim())) {
                        if (row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                            value[i] = String.valueOf(row.getCell(1).getNumericCellValue());
                        } else {
                            value[i] = row.getCell(1).getStringCellValue();
                        }
                        break;
                    }
                }
            }
        } catch (Exception e) {
            throw new Exception("Failed to retrieve value from test data xlsx \n" + e.getMessage());
        } finally {
            fileInputStream.close();
        }
        return value;
    }

    /**
     * Author - Rakesh
     * To Kill a specific task.exe
     *
     */
    public static void taskkill(String strProcessName) {
        String strCmdLine = null;
        strCmdLine = String.format("taskkill -f -im " + strProcessName + ".exe");

        Runtime rt = Runtime.getRuntime();
        try {
            rt.exec(strCmdLine);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static String ReadExcelColumnData(String SheetName, String SearchData, String ColumnNameValue) throws Exception {
        String lookupValue=null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(System.getProperty("user.dir")+"/src/test/resources/TestData.xlsx");
            XSSFWorkbook workbook = new XSSFWorkbook(fileInputStream);
            XSSFSheet worksheet = workbook.getSheet(SheetName);
            int noOfColumns = worksheet.getRow(0).getLastCellNum();
            int colNumValue=-1;
            for (int i=0;i<noOfColumns;i++){
                if(worksheet.getRow(0).getCell(i).getStringCellValue().equalsIgnoreCase(ColumnNameValue)){
                    colNumValue=i;
                }
            }
            if(colNumValue==-1){
                throw new Exception("No Column Name matched to Search Value");
            }

            Iterator<Row> rowIterator = worksheet.iterator();
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0).getStringCellValue().equalsIgnoreCase(SearchData)) {
                    if (row.getCell(colNumValue).getCellType() == Cell.CELL_TYPE_NUMERIC) {
                        lookupValue = String.valueOf(row.getCell(colNumValue).getNumericCellValue());
                    } else {
                        lookupValue = row.getCell(colNumValue).getStringCellValue();
                    }
                        break;
                }
            }

        } catch (Exception e) {
            throw new Exception("Failed to retrieve value from test data xlsx \n" + e.getMessage());
        } finally {
            fileInputStream.close();
        }
        return lookupValue;
    }

}

