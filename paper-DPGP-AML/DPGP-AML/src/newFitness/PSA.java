package newFitness;

import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class PSA {

    public static List<String> searchAnchor(List<List<String>> sourceClass, List<List<String>> targetClass, double anchorSize) {
        List<String> partOfSource = select(sourceClass, anchorSize);
        List<String> partOfTarget = select(targetClass, anchorSize);
        List<String> PSA = new ArrayList<>();
        for (int i = 0; i < partOfSource.size(); i++) {
            for (int j = 0; j < partOfTarget.size(); j++) {
                if (WaysOfSimilarity.SMOASimilarity(partOfSource.get(i), partOfTarget.get(j)) == 1) {
                    PSA.add(partOfSource.get(i) + "---" + partOfTarget.get(j));
                }
            }
        }
        return PSA;
    }

    public static int getCountOfFatherAndSon(List<String> fatherAndSon, int index) {
        String s = fatherAndSon.get(index);
        if (s.equals(""))
            return 0;
        else {
            if (s.charAt(0) == ' ')
                s = s.substring(1);
            String[] strs = splitString(s);
            return strs.length;
        }
    }

    public static List<String> select(List<List<String>> ont, double anchorSize) {
        List<String> candidate = new ArrayList<>();

        int[] NumberOfFatherAndSon = new int[ont.get(3).size()];
        for (int i = 0; i < ont.get(3).size(); i++)
            NumberOfFatherAndSon[i] = getCountOfFatherAndSon(ont.get(3), i);

        int[] copyarr = new int[NumberOfFatherAndSon.length];
        for (int i = 0; i < NumberOfFatherAndSon.length; i++)
            copyarr[i] = NumberOfFatherAndSon[i];
        sort(copyarr);
        int index = (int) (NumberOfFatherAndSon.length * anchorSize);
        int temp = copyarr[index - 1];
        for (int i = 0, j = 0; i < NumberOfFatherAndSon.length && j < index; i++) {
            if (NumberOfFatherAndSon[i] >= temp) {
                candidate.add(ont.get(1).get(i));
                j++;
            }
        }
        return candidate;
    }


    private static void sort(int[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    private static String[] splitString(String s) {
        return s.split(" ");
    }
}
