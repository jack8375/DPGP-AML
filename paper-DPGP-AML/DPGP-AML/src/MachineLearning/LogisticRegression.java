package MachineLearning;

import java.util.List;

public class LogisticRegression {

    public static double[] optimalWeight(double[][] EVA, double[] results, int epoch, double learningRate) {
        double[] weights = new double[EVA[0].length];
        double bestAcc = 0d;
        double[] bestWeights = new double[weights.length];
        for (int i = 0; i < epoch; i++) {
            double[] predicts = new double[results.length];
            for (int j = 0; j < predicts.length; j++) {
                predicts[j] = classify(EVA[j], weights);
            }
            double acc = accuracy(predicts, results);
            if (acc > bestAcc) {
                bestAcc = acc;
                bestWeights = weights.clone();
            }
            double[] diffs = new double[results.length];
            for (int p = 0; p < results.length; p++) {
                diffs[p] = results[p] - predicts[p];
            }
            for (int p = 0; p < weights.length; p++) {
                double step = 0d;
                for (int q = 0; q < results.length; q++) {
                    step += EVA[q][p] * diffs[q];
                }
                step = step / results.length;
                weights[p] += (learningRate * step);
            }
        }
        return bestWeights;
    }

    public static List<String> updatePSA(List<String> QMS, List<String> PSA, double[] optimalWeight, double[][] QMS_EV) {
        for (int i = 0; i < QMS_EV.length; i++) {
            double result = classify(QMS_EV[i], optimalWeight);
            if (result == 1) {
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

    public static double accuracy(double[] predicts, double[] results) {
        double num = 0;
        for (int i = 0; i < predicts.length; i++) {
            if (predicts[i] == results[i])
                num++;
        }
        return num / predicts.length;
    }

    public static double classify(double[] vector1, double[] vector2) {
        double x = sigmoid(dotProduct(vector1, vector2));
        if (x > 0.5)
            return 1;
        else return 0;
    }

    public static double dotProduct(double[] vector1, double[] vector2) {
        double res = 0d;
        for (int i = 0; i < vector1.length; i++) {
            res += (vector1[i] * vector2[i]);
        }
        return res;
    }

    public static double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

}
