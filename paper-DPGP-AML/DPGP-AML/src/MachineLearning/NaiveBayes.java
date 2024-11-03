package MachineLearning;

import java.util.List;

public class NaiveBayes {

    public static List<String> updatePSA(double[][] EVA, double[] results, List<String> QMS, List<String> PSA, double[][] QMS_EV) {
        double num1 = 0;
        double num0 = 0;
        double num1_1 = 0;
        double num1_0 = 0;
        double num0_1 = 0;
        double num0_0 = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] == 1) {
                num1++;
                for (int j = 0; j < EVA[i].length; j++) {
                    if (EVA[i][j] == 1)
                        num1_1++;
                    else num1_0++;
                }
            }
            if (results[i] == 0) {
                num0++;
                for (int j = 0; j < EVA[i].length; j++) {
                    if (EVA[i][j] == 1)
                        num0_1++;
                    else num0_0++;
                }
            }
        }
        if (num1_1 == 0 || num1_0 == 0 || num0_1 == 0 || num0_0 == 0) {
            num1_1++;
            num1_0++;
            num0_1++;
            num0_0++;
        }
        double pro1 = num1 / (num1 + num0);
        double pro0 = num0 / (num1 + num0);
        double pro1_1 = num1_1 / (num1_1 + num1_0);
        double pro1_0 = num1_0 / (num1_1 + num1_0);
        double pro0_1 = num0_1 / (num0_1 + num0_0);
        double pro0_0 = num0_0 / (num0_1 + num0_0);
        for (int i = 0; i < QMS_EV.length; i++) {
            double testNum1 = 0;
            double testNum0 = 0;
            for (int j = 0; j < QMS_EV[i].length; j++) {
                if (QMS_EV[i][j] == 1)
                    testNum1++;
                else testNum0++;
            }
            double classify1 = pro1 * Math.pow(pro1_1, testNum1) * Math.pow(pro1_0, testNum0);
            double classify0 = pro0 * Math.pow(pro0_1, testNum1) * Math.pow(pro0_0, testNum0);
            if (classify1 > classify0) {
                boolean flag = true;
                for (int j = 0; j < PSA.size(); j++) {
                    if (PSA.get(j).equals(QMS.get(i))) {
                        flag = false;
                        break;
                    }
                }
                if (flag)
                    PSA.add(QMS.get(i));
            }
        }
        return PSA;
    }
}
