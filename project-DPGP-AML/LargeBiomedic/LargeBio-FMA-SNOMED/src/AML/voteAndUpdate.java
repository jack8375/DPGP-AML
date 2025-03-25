package AML;

import wRF.DT;
import wRF.DTNode;

import java.util.List;

public class voteAndUpdate {
    //更新PSA
    public static List<String> updatePSA(List<String> QMS, List<String> PSA, List<List<DTNode>> RF, double[] optimalWeight, double[][] QMS_EV) {
        for (int i = 0; i < QMS_EV.length; i++) {
            double[] vote = new double[RF.size()];
            for (int j = 0; j < RF.size(); j++) {
                vote[j] = DT.decodeDT(RF.get(j), QMS_EV[i]);
            }
            double weightPositive = 0;
            double weightNegative = 0;
            for (int j = 0; j < vote.length; j++) {
                if (vote[j] == 1)
                    weightPositive += optimalWeight[j];
                else weightNegative += optimalWeight[j];
            }
            if (weightPositive > weightNegative) {
                boolean flag = true;  //防止将QMS和PSA重复的映射添加进来
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

    //获取QMS的专家验证
    public static double[][] getQMS_EV(double errorRate, int expertNumber, List<String> QMS, List<String> algnment) {
        double[][] QMS_EV = new double[QMS.size()][expertNumber];
        for (int i = 0; i < QMS.size(); i++) {
            for (int p = 0; p < expertNumber; p++) {
                boolean flag = false;
                for (int q = 0; q < algnment.size(); q++) {
                    if (algnment.get(q).equals(QMS.get(i))) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    if (Math.random() >= errorRate)
                        QMS_EV[i][p] = 1;
                    else QMS_EV[i][p] = 0;
                } else {
                    if (Math.random() >= errorRate)
                        QMS_EV[i][p] = 0;
                    else QMS_EV[i][p] = 1;
                }
            }
        }
        return QMS_EV;
    }
}
