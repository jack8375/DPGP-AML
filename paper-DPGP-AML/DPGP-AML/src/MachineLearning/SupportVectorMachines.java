package MachineLearning;

import java.util.List;

public class SupportVectorMachines {

    private int exampleNum;
    private int exampleDim;
    private double[] w;
    private double lambda;
    private double lr = 0.001;
    private double threshold = 0.001;
    private double cost;
    private double[] grad;
    private double[] yp;

    public SupportVectorMachines(double paramLambda) {
        lambda = paramLambda;
    }

    private void CostAndGrad(double[][] X, double[] y) {
        cost = 0;
        for (int m = 0; m < exampleNum; m++)
        {
            yp[m] = 0;
            for (int d = 0; d < exampleDim; d++) {
                yp[m] += X[m][d] * w[d];
            }

            if (y[m] * yp[m] - 1 < 0) {
                cost += (1 - y[m] * yp[m]);
            }
        }

        for (int d = 0; d < exampleDim; d++) {
            cost += 0.5 * lambda * w[d] * w[d];
        }

        for (int d = 0; d < exampleDim; d++) {
            grad[d] = Math.abs(lambda * w[d]);
            for (int m = 0; m < exampleNum; m++) {
                if (y[m] * yp[m] - 1 < 0) {
                    grad[d] -= y[m] * X[m][d];
                }
            }
        }
    }

    private void update() {
        for (int d = 0; d < exampleDim; d++) {
            w[d] -= lr * grad[d];
        }
    }

    public void Train(double[][] X, double[] y, int maxIters) {
        exampleNum = X.length;
        if (exampleNum <= 0) {
            System.out.println("num of example <=0!");
            return;
        }
        exampleDim = X[0].length;
        w = new double[exampleDim];
        grad = new double[exampleDim];
        yp = new double[exampleNum];

        for (int iter = 0; iter < maxIters; iter++) {
            CostAndGrad(X, y);
            if (cost < threshold) {
                break;
            }
            update();
        }
    }

    private int predict(double[] x) {
        double pre = 0;
        for (int j = 0; j < x.length; j++) {
            pre += x[j] * w[j];
        }
        if (pre >= 0)
            return 1;
        else return -1;
    }

    public List<String> updatePSA(double[][] EVA, double[] results, List<String> QMS, List<String> PSA, double[][] QMS_EV) {
        for (int i = 0; i < results.length; i++) {
            results[i] = (results[i] == 0) ? -1 : 1;
        }
        for (int i = 0; i < EVA.length; i++) {
            for (int j = 0; j < EVA[i].length; j++) {
                EVA[i][j] = (EVA[i][j] == 0) ? -1 : 1;
            }
        }
        SupportVectorMachines svm = new SupportVectorMachines(0.0001);
        svm.Train(EVA, results, 7000);

        for (int i = 0; i < QMS_EV.length; i++) {
            for (int j = 0; j < QMS_EV[i].length; j++) {
                QMS_EV[i][j] = (QMS_EV[i][j] == 0) ? -1 : 1;
            }
            int result = svm.predict(QMS_EV[i]);
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
}
