package main;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class excel {
    //写入随机变异
    public static void writeToExcel1(double[] data, String filePath, int rankNow, int rowIndex) {
        try (FileInputStream file = new FileInputStream(filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet("无交互"); // 获取名为“无交互”的工作表

            if (sheet == null) {
                // 如果找不到名为“无交互”的工作表，则创建一个新的工作表
                sheet = workbook.createSheet("无交互");
            }

            int col = rankNow * (data.length + 2); // 获取列号
            Row row = sheet.getRow(rowIndex);
            if (row == null) {
                row = sheet.createRow(rowIndex);
            }
            Cell cell = row.createCell(col);
            cell.setCellValue("迭代次数" + (rowIndex * 1)); // 设置单元格值为评估次数

            for (int i = 0; i < data.length; i++) {
                Cell dataCell = row.createCell(i + col + 1); // 从第 x 列开始创建单元格
                dataCell.setCellValue(data[i]); // 设置单元格的值为数组中的元素
            }
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
                System.out.println("数组已成功写入“无交互”工作表：" + filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("写入 Excel 表格时出现错误：" + e.getMessage());
        }
    }
}
