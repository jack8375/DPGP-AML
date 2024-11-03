package similarity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class Excel {

    public static void writeArrayToExcel(String name, double[][] data, List<String> rowNames, List<String> columnNames) throws IOException {

        String path = "D:\\anatomy\\实验结果";
        String Path = path + "/" + name + ".xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("DataSheet");

        Row firstRow = sheet.createRow(0);
        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = firstRow.createCell(i + 1);
            cell.setCellValue(columnNames.get(i));
        }

        for (int i = 0; i < rowNames.size(); i++) {
            Row row = sheet.createRow(i + 1);
            Cell cell = row.createCell(0);
            cell.setCellValue(rowNames.get(i));

            for (int j = 0; j < data[i].length; j++) {
                cell = row.createCell(j + 1);
                cell.setCellValue(data[i][j]);
            }
        }

        FileOutputStream out = new FileOutputStream(Path);
        workbook.write(out);
        out.close();
        System.out.println("Excel 文件生成成功！");
    }
}
