package similarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class readWuMatrix {
    public static double[][] read(String s, int num1, int num2) throws IOException {
        String ss = "D:\\conference\\SF\\" + s + ".txt";
        BufferedReader bf = new BufferedReader(new FileReader(ss));//filename为参数，文件路径，这里可能涉及到一些文件路径解析问题请百度。
        String textLine = new String();
        //代码优化
        StringBuffer sb = new StringBuffer();
        while((textLine = bf.readLine()) != null) {    //readline每次读取一行，也是最推荐的读取文件的方法
            //str += textLine + ",";                     //由于读取的是矩阵，每读取一行就用逗号隔开
            sb.append(textLine).append(",");
        }                                              //以上都是固定操作，为把filename里面的文件一行一行读入str中，每行用逗号隔开
        String[] numbers = sb.toString().split(",");//这里是根据逗号把每行分别存入numbers中，这一步其实可以直接在上面的while里实现，但是这样更详细操作空间更大
        double[][] number = new double[num1][num2];//矩阵数组
        String[] stmp = null;
        for(int i = 0; i < numbers.length; i++) {
            stmp = numbers[i].split(" ");//这里其实是把每一行的数字进一步分离
            for(int j = 0; j < stmp.length; j++) {
                number[i][j] = Double.parseDouble(stmp[j]);//这里Integer.parseInt这个函数是把字符串解析成整数
            }
        }
        bf.close();//**一定要记得关闭文件**
        return number;
    }

    public static void writeFile(double[][] matrix, String fileRoad) throws IOException {       //该函数是要将数组写入到文件中，每个元素就是一行
        File file = new File(fileRoad);
        FileWriter fw = new FileWriter(file);
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                fw.write(String.valueOf(matrix[i][j]) + " ");
            }
            fw.write("\n");
        }
        fw.close();
    }

    public static double[] valueToPar(List<String> standardAnswer, List<String> foundClassMapping) {

        int R = standardAnswer.size();
        int A = foundClassMapping.size();
        int RnA = aMappingIsTureForOtherMapping(foundClassMapping, standardAnswer);
        if (A == 0 || R == 0 || RnA == 0) {
            double[] d = {0.0, 0.0, 0.0};
            return d;
        }
        double recall = ((double) RnA) / ((double) R);
        double precision = ((double) RnA) / ((double) A);
        double f_measure = (2*recall * precision) / ((recall) + (precision));
        double[] d = {recall, precision, f_measure};

        return d;
    }
    public static int aMappingIsTureForOtherMapping(List<String> foundClassMapping, List<String> standardAnswer) {

        ArrayList<String> myCopy = new ArrayList<>(standardAnswer);
        myCopy.retainAll(foundClassMapping);
        return myCopy.size();
    }
}
