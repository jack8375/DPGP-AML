package main;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

public class definedMatrix {
    public final int row_FMA_NCI = 3696;
    public final int col_FMA_NCI = 6488;
    public final int row_FMA_SNOMED = 10157;
    public final int col_FMA_SNOMED = 13412;
    public final int row_SNOMED_NCI = 19765;
    public final int col_SNOMED_NCI = 17012;

    public static final String Class_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\class-SNOMED-NCI.bin";
    public static final String SMOA_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\SMOA-SNOMED-NCI.bin";
    public static final String NGram_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\NGram-SNOMED-NCI.bin";
    public static final String dictionary_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\dictionary-SNOMED-NCI.bin";
    public static final String source_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\source-SNOMED-NCI.bin";
    public static final String target_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\target-SNOMED-NCI.bin";
    public static final String Reference_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\reference-SNOMED-NCI.bin";
    public static final String Anchors_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\anchors-SNOMED-NCI.bin";
    public static final String clusters_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\clusters-SNOMED-NCI.bin";
    public static final String cluster_Size_SNOMED_NCI = "D:\\Biomed\\LargeBio-SNOMED-NCI\\TotalMatrix\\Cluster_Size-SNOMED-NCI.bin";

    /**
     * 将一维字符串集合从内存映射文件中读出
     */
    public static List<String> readMappedList(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        byte[] bytes = new byte[(int) file.length()];
        buffer.get(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<String> list = (List<String>) ois.readObject();
        channel.close();
        raf.close();
        return list;
    }

    /**
     * 将三维字符串集合从内存映射文件中读出
     */
    public static List<List<List<String>>> readMappedThreeList(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
        byte[] bytes = new byte[(int) file.length()];
        buffer.get(bytes);
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        List<List<List<String>>> collection = (List<List<List<String>>>) ois.readObject();
        channel.close();
        raf.close();
        return collection;
    }

    //二维数组从内存映射文件读出
    public static double[][] readArrayFromFile(String filePath, int numRows, int numCols) {
        double[][] matrix = new double[numRows][numCols];
        try (FileChannel channel = new RandomAccessFile(filePath, "r").getChannel()) {
            long bufferSize = 8L * numRows * numCols; // 计算缓冲区大小
            MappedByteBuffer buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, bufferSize);

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    matrix[i][j] = buffer.getDouble();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return matrix;
    }

    public static int[][] readArrayInt(String filePath, int row, int col) throws IOException {
        int[][] array = new int[row][col];
        try (RandomAccessFile raf = new RandomAccessFile(filePath, "r")) {
            FileChannel fc = raf.getChannel();
            int size = Double.BYTES * row * col;
            MappedByteBuffer mbb = fc.map(FileChannel.MapMode.READ_ONLY, 0, size);
            int index = 0;
            for (int i = 0; i < row; i++) {
                for (int j = 0; j < col; j++) {
                    array[i][j] = (int) mbb.getDouble(index); // 使用getDouble方法读取double类型数据
                    index += Double.BYTES;
                }
            }
            // 关闭文件通道
            fc.close();
        }
        return array;
    }

}
