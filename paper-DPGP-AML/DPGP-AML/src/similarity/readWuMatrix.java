package similarity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class readWuMatrix {
    public static double[][] read(String s, int num1, int num2) throws IOException {
        String ss = "D:\\conference\\SF\\" + s + ".txt";
        BufferedReader bf = new BufferedReader(new FileReader(ss));
        String textLine = new String();

        StringBuffer sb = new StringBuffer();
        while((textLine = bf.readLine()) != null) {
            sb.append(textLine).append(",");
        }
        String[] numbers = sb.toString().split(",");
        double[][] number = new double[num1][num2];
        String[] stmp = null;
        for(int i = 0; i < numbers.length; i++) {
            stmp = numbers[i].split(" ");
            for(int j = 0; j < stmp.length; j++) {
                number[i][j] = Double.parseDouble(stmp[j]);
            }
        }
        bf.close();
        return number;
    }

    public static void writeFile(double[][] matrix, String fileRoad) throws IOException {
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
