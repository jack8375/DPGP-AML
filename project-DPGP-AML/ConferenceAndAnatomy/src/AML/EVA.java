package AML;

import java.util.ArrayList;
import java.util.List;

public class EVA {

    //获取EVA（训练wRF的数据集）
    /*public static double[][] getEVA(double errorRate, int expertNumber, List<String> PSA, List<String> algnment) {
        int length = 0;
        for (int i = 0; i < PSA.size(); i++) {
            length++;
        }
        double[][] EVA = new double[length * length][expertNumber];
        List<List<String>> expertList = new ArrayList<>();

        for (int i = 0; i < expertNumber; i++) {
            List<String> expert = expert(errorRate, algnment);
            expertList.add(expert);
        }
        for (int i = 0; i < PSA.size(); i++) {
            String positive = PSA.get(i);
            for (int p = 0; p < expertList.size(); p++) {
                List<String> expert = expertList.get(p);
                boolean flag = false;
                for (int q = 0; q < expert.size(); q++) {
                    if (expert.get(q).equals(positive)) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    EVA[i][p] = 1;
                else EVA[i][p] = 0;
            }
        }

        List<String> negativeMapping = negativeMapping(PSA);
        for (int i = 0; i < negativeMapping.size(); i++) {
            String negative = negativeMapping.get(i);
            for (int p = 0; p < expertList.size(); p++) {
                List<String> expert = expertList.get(p);
                boolean flag = false;
                for (int q = 0; q < expert.size(); q++) {
                    if (expert.get(q).equals(negative)) {
                        flag = true;
                        break;
                    }
                }
                if (flag)
                    EVA[i + PSA.size()][p] = 1;
                else EVA[i + PSA.size()][p] = 0;
            }
        }
        return EVA;
    }*/

    //获取EVA（训练wRF的数据集）
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

    //获取EVA对应的结果集合
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

    //构建专家
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

    //获取PSA对应的负映射样本
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
