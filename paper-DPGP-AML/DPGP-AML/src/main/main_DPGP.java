package main;

import AML.QuestionableMapping;
import AML.voteAndUpdate;
import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import Tree.BT;
import Tree.LinkedTreeNode;
import Tree.decode;
import evolutionaryAlgorithms.DeterministicCrossover;
import evolutionaryAlgorithms.PopulationDiversityEnhancement;
import evolutionaryAlgorithms.RandomMutation;
import newFitness.Fitness;
import newFitness.PA;
import wRF.DTNode;
import wRF.train;

import java.util.ArrayList;
import java.util.List;

public class main_DPGP {
    public static void main(String[] args) throws Exception {

        String[] str1 = {"top1", "top30", "top50"};
        String[] str2 = {"add", "sub", "mul", "div", "max", "min", "avg"};
        String[] str3 = {"NGram", "SMOA", "wuAndPalmer", "sonGreedy"};

        String s1 = "D:\\paper1\\DPGP-AML\\conference-dataset\\edas.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\paper1\\DPGP-AML\\conference-dataset\\iasted.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\paper1\\DPGP-AML\\reference-alignment\\edas-iasted.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        int maxDeep = 3;
        int populationSize = 50;
        int maxiGen = 2000;
        double anchorSize = 0.7;
        int selectionSize1 = 6;
        int selectionSize2 = populationSize;
        int neighborhoodSize = 6;
        int SPsize = 10;
        double errorRate = 0.1;
        int expertNumber = 9;
        char[] labels = {'零', '一', '二', '三', '四', '五', '六', '七', '八'};
        int treeNumber = 7;
        double st = 0.01;
        double threshold = 0.6;
        int thresholdForPopulationDivergenceEnhancement = 50;
        int thresholdForExpertInvolvement = 70;

        label:
        for (int s = 0; s < 30; s++) {
            List<String> PSA = newFitness.PSA.searchAnchor(source1, target1, anchorSize);
            List<ArrayList<LinkedTreeNode>> populationBetter = new ArrayList<>();
            List<ArrayList<LinkedTreeNode>> populationWorse = new ArrayList<>();
            List<ArrayList<LinkedTreeNode>> population1 = BT.creatPopulation(populationSize, str1, str2, str3, maxDeep);
            List<ArrayList<LinkedTreeNode>> population2 = BT.creatPopulation(populationSize, str1, str2, str3, maxDeep);
            double[] sortOfFitness1 = sort(Fitness.fitnessOfPopulation(population1, PSA, source1, target1));
            double[] sortOfFitness2 = sort(Fitness.fitnessOfPopulation(population2, PSA, source1, target1));
            double betterFitness = 0;
            double worseFitness = 0;
            for (int i = 0; i < sortOfFitness1.length; i++) {
                if (sortOfFitness1[i] > sortOfFitness2[i]) {
                    populationBetter = population1;
                    populationWorse = population2;
                    betterFitness = sortOfFitness1[0];
                    worseFitness = sortOfFitness2[0];
                    break;
                } else if (sortOfFitness1[i] < sortOfFitness2[i]) {
                    populationBetter = population2;
                    populationWorse = population1;
                    betterFitness = sortOfFitness2[0];
                    worseFitness = sortOfFitness1[0];
                    break;
                } else continue;
            }

            int numOfBetterUnchanged = 0;
            int numOfWorseUnchanged = 0;
            int numOfGlobalUnchanged = 0;
            int totalRequests = 0;
            int enhancement = 0;
            double QMSverify = 0;
            double tempF_measure = 0;

            for (int i = 0; i < maxiGen; i++) {
                System.out.println("---开始第" + (i + 1) + "次迭代进化：");
                List<ArrayList<LinkedTreeNode>> populationCross = DeterministicCrossover.deterministicCrossover(populationBetter, selectionSize1, PSA, source1, target1);
                List<ArrayList<LinkedTreeNode>> populationMutation = RandomMutation.mutation(populationWorse, selectionSize2, PSA, source1, target1);
                double[] fitnessOfPopulationCross = Fitness.fitnessOfPopulation(populationCross, PSA, source1, target1);
                double[] fitnessOfPopulationMutation = Fitness.fitnessOfPopulation(populationMutation, PSA, source1, target1);

                double[] sortOfCross = sort(fitnessOfPopulationCross);
                double[] sortOfMutation = sort(fitnessOfPopulationMutation);
                ArrayList<LinkedTreeNode> newBetterElite = populationCross.get(fitnessToIndex(sortOfCross[0], populationCross, fitnessOfPopulationCross));
                ArrayList<LinkedTreeNode> newWorseElite = populationMutation.get(fitnessToIndex(sortOfMutation[0], populationMutation, fitnessOfPopulationMutation));
                ArrayList<LinkedTreeNode> updateGlobalElite;
                if (sortOfMutation[0] > sortOfCross[0]) {
                    populationBetter = populationMutation;
                    populationWorse = populationCross;
                    updateGlobalElite = newWorseElite;
                    if (Math.abs(sortOfMutation[0] - betterFitness) < 0.0001) {
                        numOfBetterUnchanged++;
                        numOfGlobalUnchanged++;
                    } else {
                        numOfBetterUnchanged = 0;
                        numOfGlobalUnchanged = 0;
                    }
                    if (Math.abs(sortOfCross[0] - worseFitness) < 0.0001)
                        numOfWorseUnchanged++;
                    else numOfWorseUnchanged = 0;
                    betterFitness = sortOfMutation[0];
                    worseFitness = sortOfCross[0];

                } else {
                    populationBetter = populationCross;
                    populationWorse = populationMutation;
                    updateGlobalElite = newBetterElite;
                    if (Math.abs(sortOfCross[0] - betterFitness) < 0.0001) {
                        numOfBetterUnchanged++;
                        numOfGlobalUnchanged++;
                    } else {
                        numOfBetterUnchanged = 0;
                        numOfGlobalUnchanged = 0;
                    }
                    if (Math.abs(sortOfMutation[0] - worseFitness) < 0.0001)
                        numOfWorseUnchanged++;
                    else numOfWorseUnchanged = 0;
                    betterFitness = sortOfCross[0];
                    worseFitness = sortOfMutation[0];
                }


                if (numOfBetterUnchanged >= thresholdForPopulationDivergenceEnhancement) {
                    PopulationDiversityEnhancement.diversityEnhancement(populationBetter, populationWorse, neighborhoodSize, SPsize, source1, target1);
                    enhancement++;
                    System.out.println("激活种群多样性增强!");
                    System.out.println("激活种群多样性增强次数：" + enhancement);
                    numOfBetterUnchanged = 0;
                }
                if (numOfWorseUnchanged >= thresholdForPopulationDivergenceEnhancement) {
                    PopulationDiversityEnhancement.diversityEnhancement(populationWorse, populationBetter, neighborhoodSize, SPsize, source1, target1);
                    enhancement++;
                    System.out.println("激活种群多样性增强!");
                    System.out.println("激活种群多样性增强次数：" + enhancement);
                    numOfWorseUnchanged = 0;
                }
                if (numOfGlobalUnchanged >= thresholdForExpertInvolvement) {
                    List<String> partialAlignment = PA.getPartialAlignment(PSA, PA.getAlignment(decode.decodeBT(updateGlobalElite, source1, target1).get(0), source1, target1));
                    totalRequests++;

                    double[][] EVA = AML.EVA.getEVA(errorRate, expertNumber, PSA, reference);
                    double[] results = AML.EVA.getResults(PSA);
                    List<List<DTNode>> RF = train.creatRF(EVA, results, labels, treeNumber);
                    double[] optimalWeight = train.optimalWeight(RF, EVA, results, st);

                    List<String> QMS = new ArrayList<>();
                    List<String> QMS2 = QuestionableMapping.QMS2(PSA, threshold, source1, target1);
                    List<String> QMS1 = QuestionableMapping.QMS1(partialAlignment, PSA);
                    QMS.addAll(QMS1);
                    QMS.addAll(QMS2);
                    delete(QMS);
                    for (int j = 0; j < QMS.size(); j++) {
                        System.out.println(QMS.get(j));
                    }
                    QMSverify += QMS.size();
                    System.out.println(QMS.size());
                    System.out.println("平均每次验证的QMS规模：" + (QMSverify / totalRequests));
                    System.out.println("--------------------------");
                    double[][] QMS_EV = voteAndUpdate.getQMS_EV(errorRate, expertNumber, QMS, reference);
                    List<String> updatePSA = voteAndUpdate.updatePSA(QMS, PSA, RF, optimalWeight, QMS_EV);

                    for (int j = 0; j < updatePSA.size(); j++) {
                        System.out.println(updatePSA.get(j));
                    }
                    System.out.println("激活专家介入,更新PSA!");
                    System.out.println("专家介入次数：" + totalRequests);
                    numOfGlobalUnchanged = 0;
                    if (PSA.size() == 0) {
                        System.out.println("分类结束");
                        continue label;
                    }
                }

                List<String> alignment = PA.getAlignment(decode.decodeBT(updateGlobalElite, source1, target1).get(0), source1, target1);
                //List<String> tempAlignment = new ArrayList<>();
                //List<String> newAlignment = new ArrayList<>();
                //List<String> propertyMappings = new ArrayList<>();

                double num = 0;
                for (int p = 0; p < reference.size(); p++) {
                    for (int q = 0; q < alignment.size(); q++) {
                        if (reference.get(p).equals(alignment.get(q))) {
                            num++;
                            break;
                        }
                    }
                }
                double recall = num / reference.size();
                double precision = num / alignment.size();
                double newF_measure;
                if (recall == 0 || precision == 0)
                    newF_measure = 0;
                else newF_measure = (2 * recall * precision) / (recall + precision);
                if (newF_measure > tempF_measure) {
                    for (int j = 0; j < updateGlobalElite.size(); j++) {
                        System.out.print(updateGlobalElite.get(j).getStr() + " ");
                    }
                    System.out.println();
                    System.out.println("--------------------------");
                    for (int j = 0; j < alignment.size(); j++) {
                        System.out.println("alignment.add(" + '"' + alignment.get(j) + '"' + ')' + ';');
                    }
                    System.out.println("-----------------------");
                    System.out.println("近似f值为" + updateGlobalElite.get(0).getApproximateFitness());
                    System.out.println("查全率: " + recall);
                    System.out.println("查准率: " + precision);
                    System.out.println("f-measure: " + newF_measure);
                    System.out.println("-----------------------");
                    tempF_measure = newF_measure;
                }
                if (recall >= 0.9 && precision == 1) {
                    System.out.println("-----------------------");
                    continue label;
                }
            }
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

    private static void delete(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    j--;
                }
            }
        }
    }

    public static int fitnessToIndex(double fitness, List<ArrayList<LinkedTreeNode>> population, double[] fitnessOfPopulation) {
        int i = 0;
        for (; i < fitnessOfPopulation.length; i++) {
            if (fitnessOfPopulation[i] == fitness)
                break;
        }
        return i;
    }

}
