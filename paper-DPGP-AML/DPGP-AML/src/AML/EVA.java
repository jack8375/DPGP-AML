package AML;

import java.util.ArrayList;
import java.util.List;

public class EVA {

    public static double[][] getEVA(double errorRate, int expertNumber, List<String> PSA, List<String> algnment) {

        double[][] EVA = new double[PSA.size() * PSA.size()][expertNumber];
        for (int i = 0; i < PSA.size(); i++) {
            for (int p = 0; p < expertNumber; p++) {
                boolean flag = false;
                for (int q = 0; q < algnment.size(); q++) {
                    if (algnment.get(q).equals(PSA.get(i))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    double r = Math.random();
                    if (r >= errorRate)
                        EVA[i][p] = 1;
                    else EVA[i][p] = 0;
                } else {
                    double r = Math.random();
                    if (r >= errorRate)
                        EVA[i][p] = 0;
                    else EVA[i][p] = 1;
                }
            }
        }

        List<String> negativeMapping = negativeMapping(PSA);
        for (int i = 0; i < negativeMapping.size(); i++) {
            for (int p = 0; p < expertNumber; p++) {
                boolean flag = false;
                for (int q = 0; q < algnment.size(); q++) {
                    if (algnment.get(q).equals(negativeMapping.get(i))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    double r = Math.random();
                    if (r >= errorRate)
                        EVA[i + PSA.size()][p] = 1;
                    else EVA[i + PSA.size()][p] = 0;
                } else {
                    double r = Math.random();
                    if (r >= errorRate)
                        EVA[i + PSA.size()][p] = 0;
                    else EVA[i + PSA.size()][p] = 1;
                }
            }
        }
        return EVA;
    }

    public static double[] getResults(List<String> PSA) {
        int length = PSA.size();
        double[] results = new double[length * length];
        for (int i = 0; i < length; i++) {
            results[i] = 1;
        }
        for (int i = length; i < results.length; i++) {
            results[i] = 0;
        }
        return results;
    }

    public static List<String> expert(double errorRate, List<String> alignment) {
        List<String> copyAlgnment = new ArrayList<>();
        int temp = 0;
        for (int i = 0; i < alignment.size(); i++) {
            copyAlgnment.add(alignment.get(i));
            temp++;
        }
        int num = (int) (temp * errorRate);
        for (int i = 0; i < num; i++) {
            int s = Double.valueOf(Math.random() * copyAlgnment.size()).intValue();
            copyAlgnment.remove(s);
        }
        return copyAlgnment;
    }

    public static List<String> negativeMapping(List<String> PSA) {
        List<String[]> strsPSA = new ArrayList<>();
        List<String> negativeMapping = new ArrayList<>();
        for (int i = 0; i < PSA.size(); i++) {
            strsPSA.add(splitString(PSA.get(i)));
        }
        for (int i = 0; i < strsPSA.size(); i++) {
            String[] anchor1 = strsPSA.get(i);
            for (int j = 0; j < strsPSA.size(); j++) {
                String[] anchor2 = strsPSA.get(j);
                if (i != j)
                    negativeMapping.add(anchor1[0] + "---" + anchor2[1]);
            }
        }
        return negativeMapping;
    }

    private static String[] splitString(String s) {
        return s.split("---");
    }
}
