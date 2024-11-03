package newFitness;

import java.util.ArrayList;
import java.util.List;

public class PA {

    public static List<String> getPartialAlignment(List<String> PSA, List<String> alignment) {
        List<String> partialAlignment = new ArrayList<>();
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < PSA.size(); j++) {
                String[] strA = splitString(alignment.get(i));
                String[] strPSA = splitString(PSA.get(j));
                if (strA[0].equals(strPSA[0]) || strA[1].equals(strPSA[1])) {
                    partialAlignment.add(alignment.get(i));
                    break;
                }
            }
        }
        return partialAlignment;
    }


    public static List<String> getAlignment(double[][] binaryMatrix, List<List<String>> source, List<List<String>> target) {
        List<String> alignment = new ArrayList<>();
        for (int i = 0; i < binaryMatrix.length; i++) {
            for (int j = 0; j < binaryMatrix[i].length; j++) {
                if (binaryMatrix[i][j] == 1)
                    alignment.add(source.get(1).get(i) + "---" + target.get(1).get(j));
            }
        }
        return alignment;
    }

    private static String[] splitString(String s) {
        return s.split("---");
    }
}
