package wRF;

import java.util.ArrayList;
import java.util.List;

public class train {

    //由EVA训练RF的最优权重
    public static double[] optimalWeight(List<List<DTNode>> RF, double[][] EVA, double[] results, double st) {
        double[] PV = new double[RF.size() * 7];
        for (int i = 0; i < PV.length; i++) {
            PV[i] = 0.5;
        }
        double[] eliteInd = new double[PV.length];
        for (int i = 0; i < eliteInd.length; i++) {
            double r = Math.random();
            if (r < PV[i])
                eliteInd[i] = 1;
            else eliteInd[i] = 0;
        }
        while (true) {
            boolean flag = true;
            for (int i = 0; i < PV.length; i++) {
                if (PV[i] != 0 && PV[i] != 1) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                return indToWeight(PV);
            else {
                double[] newInd = new double[PV.length];
                for (int i = 0; i < newInd.length; i++) {
                    double r = Math.random();
                    if (r < PV[i])
                        newInd[i] = 1;
                    else newInd[i] = 0;
                }
                //System.out.println(accuracy(indToWeight(newInd), RF, EVA, results));
                if (accuracy(indToWeight(newInd), RF, EVA, results) > accuracy(indToWeight(eliteInd), RF, EVA, results)) {
                    eliteInd = newInd;
                    //System.out.println("更新精英个体");
                }
                for (int i = 0; i < eliteInd.length; i++) {
                    if (eliteInd[i] == 1) {
                        PV[i] = PV[i] + st;
                        if (PV[i] > 1)
                            PV[i] = 1;
                    }
                    if (eliteInd[i] == 0) {
                        PV[i] = PV[i] - st;
                        if (PV[i] < 0)
                            PV[i] = 0;
                    }
                }
            }
        }
    }

    //计算准确率
    public static double accuracy(double[] weight, List<List<DTNode>> RF, double[][] EVA, double[] results) {
        double[] weightVote = new double[results.length];
        for (int i = 0; i < EVA.length; i++) {
            double[] vote = new double[RF.size()];
            for (int j = 0; j < RF.size(); j++) {
                vote[j] = DT.decodeDT(RF.get(j), EVA[i]);
            }
            double weightPositive = 0;
            double weightNegative = 0;
            for (int j = 0; j < vote.length; j++) {
                if (vote[j] == 1)
                    weightPositive += weight[j];
                else weightNegative += weight[j];
            }
            if (weightPositive > weightNegative)
                weightVote[i] = 1;
            else weightVote[i] = 0;
        }
        /*for (int i = 0; i < weightVote.length; i++) {
            System.out.print(weightVote[i] + " ");
        }*/
        double num = 0;
        for (int i = 0; i < weightVote.length; i++) {
            if (weightVote[i] == results[i])
                num++;
        }
        return num / results.length;
    }

    //将个体序列转化为权重
    public static double[] indToWeight(double[] ind) {
        double[] weight = new double[ind.length / 7];
        double[] tempArr = new double[ind.length / 7];
        for (int i = 0; i < tempArr.length; i++) {
            double[] subArr = new double[7];
            for (int j = 0; j < ind.length; j++) {
                subArr[j] = ind[j];
                if ((j + 1) % 7 == 0) {
                    ind = delete(ind);
                    break;
                }
            }
            double num = 0;
            for (int j = 0; j < subArr.length; j++) {
                num = num * 2 + subArr[j];
            }
            tempArr[i] = num;
        }
        double sum = 0;
        for (int i = 0; i < tempArr.length; i++) {
            sum += tempArr[i];
        }
        for (int i = 0; i < tempArr.length; i++) {
            weight[i] = tempArr[i] / sum;
        }
        return weight;
    }

    //构建RF
    public static List<List<DTNode>> creatRF(double[][] EVA, double[] results, char[] labels, int treeNumber) {
        List<List<DTNode>> RF = new ArrayList<>();
        for (int i = 0; i < treeNumber; i++) {
            List<DTNode> DTree = new ArrayList<>();
            //bagging:有放回的重复多次取样
            double[][] bagging = new double[EVA.length][];
            double[] baggingResults = new double[results.length];
            int index1 = 0;
            for (int j = 0; j < EVA.length; j++) {
                int s = Double.valueOf(Math.random() * EVA.length).intValue();
                bagging[index1] = EVA[s];
                baggingResults[index1] = results[s];
                index1++;
            }
            //随机选择特征子集
            int lablelNumber = (int) (Math.sqrt(EVA[0].length));  //通常选择特征数的开平方个
            //int lablelNumber = EVA[0].length;
            char[] subLabels = new char[lablelNumber];
            List<Character> labelList = new ArrayList<>();
            for (int j = 0; j < labels.length; j++) {
                labelList.add(labels[j]);
            }
            int index2 = 0;
            for (int j = 0; j < lablelNumber; j++) {
                int s = Double.valueOf(Math.random() * labelList.size()).intValue();
                subLabels[index2] = labelList.get(s);
                labelList.remove(s);
                index2++;
            }
            DTree = DT.creatTree(bagging, baggingResults, subLabels);
            RF.add(DTree);
        }
        return RF;
    }

    //删除数组的前7位
    public static double[] delete(double[] arr) {
        double[] newArr = new double[arr.length - 7];
        int index = 0;
        for (int i = 7; i < arr.length; i++) {
            newArr[index] = arr[i];
            index++;
        }
        return newArr;
    }

}

