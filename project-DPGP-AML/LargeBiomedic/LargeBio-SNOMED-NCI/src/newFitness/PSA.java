package newFitness;

import main.definedMatrix;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PSA {
    public static List<String> searchAnchors(int num) throws IOException, ClassNotFoundException {
        List<String> PSA = new ArrayList<>();
        List<String> anchors = definedMatrix.readMappedList(definedMatrix.Anchors_SNOMED_NCI);
        for (int i = 3 * num; i < 3 * (num + 1); i++) {
            PSA.add(anchors.get(i));
        }
        return PSA;
    }

    //获取partial alignment
    public static List<String> getPartialAlignment(List<String> PSA, List<String> alignment) {
        List<String> partialAlignment = new ArrayList<>();
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < PSA.size(); j++) {
                String[] strA = splitString(alignment.get(i));
                String[] strPSA = splitString(PSA.get(j));
                if (strA[0].equals(strPSA[0]) || strA[1].equals(strPSA[1])) {  //equals
                    partialAlignment.add(alignment.get(i));
                    break;
                }
            }
        }
        return partialAlignment;
    }

    //由生成的二值矩阵获取Alignment
    public static List<String> getAlignment(double[][] binaryMatrix, List<List<String>> sourceCluster, List<List<String>> targetCluster, int num) {
        List<String> alignment = new ArrayList<>();
        for (int i = 0; i < binaryMatrix.length; i++) {
            for (int j = 0; j < binaryMatrix[i].length; j++) {
                if (binaryMatrix[i][j] == 1)
                    alignment.add(sourceCluster.get(num).get(i) + "~~~" + targetCluster.get(num).get(j));
            }
        }
        return alignment;
    }

    private static String[] splitString(String s) {
        return s.split("~~~");
    }

}
