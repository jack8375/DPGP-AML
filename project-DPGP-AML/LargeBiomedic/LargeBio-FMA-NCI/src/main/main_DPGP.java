package main;

import AML.QuestionableMapping;
import AML.voteAndUpdate;
import Tree.BT;
import Tree.LinkedTreeNode;
import Tree.decode;
import evolutionaryAlgorithms.DeterministicCrossover;
import evolutionaryAlgorithms.PopulationDiversityEnhancement;
import evolutionaryAlgorithms.RandomMutation;
import newFitness.Fitness;
import wRF.DTNode;
import wRF.train;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class main_DPGP {
    public static void main(String[] args) throws Exception {
        String[] str1 = {"top1", "top30", "top50"};
        String[] str2 = {"add", "sub", "mul", "div", "max", "min", "avg"};
        String[] str3 = {"NGram", "SMOA", "dictionary"};

        List<String> reference = definedMatrix.readMappedList(definedMatrix.Reference_FMA_NCI);
        List<String> anchors = definedMatrix.readMappedList(definedMatrix.Anchors_FMA_NCI);

        int maxDeep = 3;  //GP树最大深度
        int populationSize = 50;  //种群规模
        int maxiGen = 500;  //迭代次数
        double crossoverProbability = 0.8;  //交叉概率
        double mutationProbability = 0.1;  //变异概率
        double anchorSize = 0.7;  //锚点待选集合规模
        int selectionSize1 = 6;  //交叉：轮盘赌选择子种群规模
        int selectionSize2 = populationSize;  //变异：选择种群规模
        int neighborhoodSize = 6;  //种群多样性增强：拥挤度计算的相邻个体
        int SPsize = 10;  //种群多样性增强：替换拥挤个体数
        double errorRate = 0.3;  //专家错误率
        int expertNumber = 9;  //专家数量
        char[] labels = {'零', '一', '二', '三', '四', '五', '六', '七', '八'};  //RF标签集
        int treeNumber = 7;  //RF中训练DT的数量
        double st = 0.01;  //CGA的优化步长
        double threshold = 0.9;  //QMS：用于获取具有高SF值的可能正确的映射的阈值
        int thresholdForPopulationDivergenceEnhancement = 50;  //激活种群多样性增强的阈值
        int thresholdForExpertInvolvement = 70;  //激活专家介入的阈值
        String path="D:\\文件\\收敛实验\\LargeBio-FMA-NCI-DPGP1.xlsx";

        for (int z = 0; z < 8; z++) {
            System.out.println("第" + (z + 1) + "次测试...");
            List<List<List<String>>> clusters = definedMatrix.readMappedThreeList(definedMatrix.clusters_FMA_NCI);
            int tasksNum = clusters.get(0).size();  //任务数量
            Set<String> totalAlignment = new HashSet<>();
            double F_measure;
            double QMSNum = 0;
            double interactNum = 0;

            for (int i = 0; i < tasksNum; i++) {
                System.out.println("执行子本体任务:" + (i + 1));
                List<String> PSA = newFitness.PSA.searchAnchors(i);
                List<ArrayList<LinkedTreeNode>> populationBetter = new ArrayList<>();
                List<ArrayList<LinkedTreeNode>> populationWorse = new ArrayList<>();
                List<ArrayList<LinkedTreeNode>> population1 = BT.creatPopulation(populationSize, str1, str2, str3, maxDeep);
                List<ArrayList<LinkedTreeNode>> population2 = BT.creatPopulation(populationSize, str1, str2, str3, maxDeep);
                double[] sortOfFitness1 = sort(Fitness.fitnessOfPopulation(population1, PSA, clusters.get(0), clusters.get(1), i));
                double[] sortOfFitness2 = sort(Fitness.fitnessOfPopulation(population2, PSA, clusters.get(0), clusters.get(1), i));
                double betterFitness = 0;
                for (int j = 0; j < sortOfFitness1.length; j++) {
                    if (sortOfFitness1[j] > sortOfFitness2[j]) {
                        populationBetter = population1;
                        populationWorse = population2;
                        betterFitness = sortOfFitness1[0];
                        break;
                    } else if (sortOfFitness1[j] < sortOfFitness2[j]) {
                        populationBetter = population2;
                        populationWorse = population1;
                        betterFitness = sortOfFitness2[0];
                        break;
                    } else continue;
                }
                int numOfBetterUnchanged = 0;
                int numOfGlobalUnchanged = 0;
                int totalRequests = 0;
                ArrayList<LinkedTreeNode> updateGlobalElite = new ArrayList<>();

                for (int j = 0; j < maxiGen; j++) {
                    //System.out.println("---开始第" + (j + 1) + "次迭代进化：");
                    List<ArrayList<LinkedTreeNode>> populationCross = DeterministicCrossover.deterministicCrossover(populationBetter, selectionSize1, PSA, clusters.get(0), clusters.get(1), i, crossoverProbability);
                    List<ArrayList<LinkedTreeNode>> populationMutation = RandomMutation.mutation(populationWorse, selectionSize2, PSA, clusters.get(0), clusters.get(1), i, mutationProbability);
                    double[] fitnessOfPopulationCross = Fitness.fitnessOfPopulation(populationCross, PSA, clusters.get(0), clusters.get(1), i);
                    double[] fitnessOfPopulationMutation = Fitness.fitnessOfPopulation(populationMutation, PSA, clusters.get(0), clusters.get(1), i);
                    double[] sortOfCross = sort(fitnessOfPopulationCross);
                    double[] sortOfMutation = sort(fitnessOfPopulationMutation);
                    ArrayList<LinkedTreeNode> newBetterElite = populationCross.get(fitnessToIndex(sortOfCross[0], fitnessOfPopulationCross));
                    ArrayList<LinkedTreeNode> newWorseElite = populationMutation.get(fitnessToIndex(sortOfMutation[0], fitnessOfPopulationMutation));
                    ArrayList<LinkedTreeNode> interactiveInd;
                    if (sortOfMutation[0] > sortOfCross[0]) {
                        populationBetter = populationMutation;
                        populationWorse = populationCross;
                        updateGlobalElite = newWorseElite;
                        interactiveInd = populationMutation.get(fitnessToIndex(sortOfMutation[30], fitnessOfPopulationMutation));
                        if (Math.abs(sortOfMutation[0] - betterFitness) < 0.0001) {
                            numOfBetterUnchanged++;
                            numOfGlobalUnchanged++;
                        } else {
                            numOfBetterUnchanged = 0;
                            numOfGlobalUnchanged = 0;
                        }
                        betterFitness = sortOfMutation[0];

                    } else {
                        populationBetter = populationCross;
                        populationWorse = populationMutation;
                        updateGlobalElite = newBetterElite;
                        interactiveInd = populationCross.get(fitnessToIndex(sortOfCross[30], fitnessOfPopulationCross));
                        if (Math.abs(sortOfCross[0] - betterFitness) < 0.0001) {
                            numOfBetterUnchanged++;
                            numOfGlobalUnchanged++;
                        } else {
                            numOfBetterUnchanged = 0;
                            numOfGlobalUnchanged = 0;
                        }
                        betterFitness = sortOfCross[0];
                    }

                    if (numOfBetterUnchanged >= thresholdForPopulationDivergenceEnhancement) {
                        PopulationDiversityEnhancement.diversityEnhancement(populationBetter, populationWorse, neighborhoodSize, SPsize, i);
                        //System.out.println("激活种群多样性增强!");
                        numOfBetterUnchanged = 0;
                    }

                    if (numOfGlobalUnchanged >= thresholdForExpertInvolvement || j == 4) {
                        List<String> alignment = newFitness.PSA.getAlignment(decode.decodeBT(interactiveInd, i).get(0), clusters.get(0), clusters.get(1), i);
                        List<String> partialAlignment = newFitness.PSA.getPartialAlignment(PSA, alignment);
                        totalRequests++;
                        double[][] EVA = AML.EVA.getEVA(errorRate, expertNumber, PSA, reference);
                        double[] results = AML.EVA.getResults(PSA);
                        List<List<DTNode>> RF = train.creatRF(EVA, results, labels, treeNumber);
                        double[] optimalWeight = train.optimalWeight(RF, EVA, results, st);
                        List<String> QMS = QuestionableMapping.QMS(alignment, partialAlignment, PSA, threshold);
                        QMSNum = QMSNum + QMS.size();
                        double[][] QMS_EV = voteAndUpdate.getQMS_EV(errorRate, expertNumber, QMS, reference);
                        voteAndUpdate.updatePSA(QMS, PSA, RF, optimalWeight, QMS_EV);
                        interactNum++;
                        /*for (int k = 0; k < PSA.size(); k++) {
                            System.out.println(PSA.get(k));
                        }*/
                        if (PSA.size() == 0)
                            break;
                        System.out.println("激活专家介入,更新PSA!");
                        System.out.println("QMS平均验证数量：" + QMSNum / interactNum);
                        //System.out.println("专家介入次数：" + totalRequests);
                        numOfGlobalUnchanged = 0;
                    }
                }
                List<String> alignment = newFitness.PSA.getAlignment(decode.decodeBT(updateGlobalElite, i).get(0), clusters.get(0), clusters.get(1), i);
                totalAlignment.addAll(alignment);
                /*System.out.println("------------------");
                for (int k = 0; k < alignment.size(); k++) {
                    System.out.println(alignment.get(k));
                }*/
            }
            totalAlignment.addAll(anchors);

            double matchNum = 0;
            for (int i = 0; i < clusters.get(0).size(); i++) {
                matchNum = matchNum + clusters.get(0).get(i).size() * clusters.get(1).get(i).size();
            }

            double num = 0;
            for (int p = 0; p < reference.size(); p++) {
                for (String element : totalAlignment) {
                    if (reference.get(p).equals(element)) {
                        num++;
                        break;
                    }
                }
            }
            double recall = num / reference.size();
            double precision = num / totalAlignment.size();
            double TP = num;
            double FP = totalAlignment.size() - num;
            double FN = reference.size() - num;
            double TN = matchNum - totalAlignment.size() - reference.size() + num;
            if (recall == 0 || precision == 0)
                F_measure = 0;
            else F_measure = (2 * recall * precision) / (recall + precision);
            double balancedAccuracy = (TP / (TP + FN) + TN / (TN + FP)) / 2;
            System.out.println("-----------------------");
            System.out.println("查全率: " + recall);
            System.out.println("查准率: " + precision);
            System.out.println("f-measure: " + F_measure);
            System.out.println("balancedAccuracy:" + balancedAccuracy);
            System.out.println("-----------------------");
        }

    }

    public static double[] sort(double[] arr) {
        double[] copyArr = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copyArr[i] = arr[i];
        }
        for (int i = 0; i < copyArr.length; i++) {
            for (int j = 0; j < copyArr.length - i - 1; j++) {
                if (copyArr[j] < copyArr[j + 1]) {
                    double temp = copyArr[j];
                    copyArr[j] = copyArr[j + 1];
                    copyArr[j + 1] = temp;
                }
            }
        }
        return copyArr;
    }

    public static int fitnessToIndex(double fitness, double[] fitnessOfPopulation) {
        int i = 0;
        for (; i < fitnessOfPopulation.length; i++) {
            if (fitnessOfPopulation[i] == fitness)
                break;
        }
        return i;
    }
}
