package org.tree.example.poi;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author er_dong_chen
 * @date 2019年3月24日
 * <p>
 * Workbook > Sheet > Row > Cell
 * HSSF > xls
 * XSSF > xlsx
 * ps: 区别只在类名
 */
public class Excel {
    private String in = "S:/Project/generate/in.";
    private String out = "S:/Project/generate/out.";

    @Test
    public void test() throws Exception {
        dealExcel("xls");
        dealExcel("xlsx");
    }

    private void dealExcel(String format) throws Exception {
        Workbook workbook = _getWorkbook(format);
        if (workbook == null)
            throw new Exception("格式只能为 xls 和 xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        System.out.println(sheet.getSheetName());
        for (int i = 3; i < 10; i++) {
            Row row = sheet.getRow(i);
            row.getCell(0).setCellValue("hello world");
        }
        workbook.write(new FileOutputStream(out + format));
        workbook.close();
        System.out.println("success to deal !!!");
    }

    private Workbook _getWorkbook(String format) throws IOException {
        if ("xls".equals(format))
            return new HSSFWorkbook(new FileInputStream(in + format));
        if ("xlsx".equals(format))
            return new XSSFWorkbook(new FileInputStream(in + format));
        return null;
    }
}
