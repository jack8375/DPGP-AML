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
import evolutionaryAlgorithms.DeterministicCrossoverDP;
import evolutionaryAlgorithms.PopulationDiversityEnhancement;
import evolutionaryAlgorithms.RandomMutationDP;
import newFitness.Fitness;
import newFitness.PA;
import similarity.WaysOfSimilarity;
import wRF.DTNode;
import wRF.train;

import java.util.ArrayList;
import java.util.List;

public class main_DPGP {
    public static void main(String[] args) throws Exception {

        String[] str1 = {"top1", "top30", "top50"};
        String[] str2 = {"add", "sub", "mul", "div", "max", "min", "avg"};
        String[] str3 = {"NGram", "SMOA", "wuAndPalmer", "sonGreedy"};

        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\edas.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\sigkdd.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\edas-sigkdd.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        int maxDeep = 3;  //GP树最大深度
        int populationSize = 50;  //种群规模
        int maxiGen = 2000;  //迭代次数
        double crossoverProbability = 1;  //交叉概率
        double mutationProbability = 1;  //变异概率
        double anchorSize = 0.9;  //锚点待选集合规模
        int selectionSize1 = 6;  //交叉：轮盘赌选择子种群规模
        int selectionSize2 = populationSize;  //变异：选择种群规模
        int neighborhoodSize = 6;  //种群多样性增强：拥挤度计算的相邻个体
        int SPsize = 10;  //种群多样性增强：替换拥挤个体数
        double errorRate = 0;  //专家错误率
        int expertNumber = 9;  //专家数量
        char[] labels = {'零', '一', '二', '三', '四', '五', '六', '七', '八'};  //RF标签集
//        int expertNumber = 1;  //专家数量
//        char[] labels = {'零'};
        int treeNumber = 7;  //RF中训练DT的数量
        double st = 0.01;  //CGA的优化步长
        double threshold = 0.7;  //QMS：用于获取具有高SF值的可能正确的映射的阈值
        int thresholdForPopulationDivergenceEnhancement = 50;  //激活种群多样性增强的阈值
        int thresholdForExpertInvolvement = 2000;  //激活专家介入的阈值
        //decode：阈值过滤
        String path="D:\\文件\\收敛实验\\edas-sigkdd-DPGP.xlsx";
        double allT=0;

        label:
        for (int s = 0; s < 1; s++) {
            //List<String> PSA = newFitness.PSA.searchAnchor(source1, target1, anchorSize);
            List<String> PSA = new ArrayList<>(reference);

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
                double startTime=System.currentTimeMillis();
                List<ArrayList<LinkedTreeNode>> populationCross = DeterministicCrossoverDP.deterministicCrossover(populationBetter, selectionSize1, PSA, source1, target1, crossoverProbability);
                List<ArrayList<LinkedTreeNode>> populationMutation = RandomMutationDP.mutation(populationWorse, selectionSize2, PSA, source1, target1, mutationProbability);
                double[] fitnessOfPopulationCross = Fitness.fitnessOfPopulation(populationCross, PSA, source1, target1);
                double[] fitnessOfPopulationMutation = Fitness.fitnessOfPopulation(populationMutation, PSA, source1, target1);

                /*for (int j = 0; j < fitnessOfPopulationCross.length; j++) {
                    System.out.println(fitnessOfPopulationCross[j]);
                }
                System.out.println("--------------------------");
                for (int j = 0; j < fitnessOfPopulationMutation.length; j++) {
                    System.out.println(fitnessOfPopulationMutation[j]);
                }
                System.out.println("--------------------------");*/
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
                    List<String> partialAlignment = PA.getPartialAlignment(PSA, PA.getAlignment(decode.decodeBT(updateGlobalElite).get(0), source1, target1));
                    totalRequests++;

                    double[][] EVA = AML.EVA.getEVA(errorRate, expertNumber, PSA, reference);
                    double[] results = AML.EVA.getResults(PSA);
                    List<List<DTNode>> RF = train.creatRF(EVA, results, labels, treeNumber);
                    double[] optimalWeight = train.optimalWeight(RF, EVA, results, st);
                    //double[] optimalWeight = LogisticRegression.optimalWeight(EVA, results, 500, 2e-03);

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
                    //List<String> updatePSA = LogisticRegression.updatePSA(QMS, PSA, optimalWeight, QMS_EV);
                    //List<String> updatePSA = NaiveBayes.updatePSA(EVA, results, QMS, PSA, QMS_EV);
                    /*SupportVectorMachines svm = new SupportVectorMachines(0.0001);
                    List<String> updatePSA = svm.updatePSA(EVA, results, QMS, PSA, QMS_EV);*/

                    for (int j = 0; j < updatePSA.size(); j++) {
                        System.out.println(updatePSA.get(j));
                    }
                    System.out.println("激活专家介入,更新PSA!");
                    System.out.println("专家介入次数：" + totalRequests);
                    numOfGlobalUnchanged = 0;
                    if (PSA.size() == 0) {
                        System.out.println("分类器异常");
                        continue label;
                    }
                }

                double endTime=System.currentTimeMillis();
                List<String> alignment = PA.getAlignment(decode.decodeBT(updateGlobalElite).get(0), source1, target1);

                List<String> tempAlignment = new ArrayList<>();
                List<String> newAlignment = new ArrayList<>();
                List<String> propertyMappings = new ArrayList<>();


                //一轮排除：通过局部一致性原则(LCP)，排除子父类关系矛盾的匹配对
                for (int m = 0; m < alignment.size(); m++) {
                    String[] strs = splitString1(alignment.get(m));
                    if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) == 1) {
                        List<String> allFather1;
                        List<String> allFather2;
                        List<String> allSon1;
                        List<String> allSon2;
                        int index1 = 0;
                        for (; index1 < source1.get(1).size(); index1++) {
                            if (source1.get(1).get(index1).equals(strs[0]))
                                break;
                        }
                        allFather1 = exclude.getAllFather(source1.get(1).get(index1), source1);
                        allSon1 = exclude.getAllSon(source1.get(1).get(index1), source1);

                        int index2 = 0;
                        for (; index2 < target1.get(1).size(); index2++) {
                            if (target1.get(1).get(index2).equals(strs[1]))
                                break;
                        }
                        allFather2 = exclude.getAllFather(target1.get(1).get(index2), target1);
                        allSon2 = exclude.getAllSon(target1.get(1).get(index2), target1);

                        for (int j = 0; j < alignment.size(); j++) {
                            String[] align = splitString1(alignment.get(j));
                            if (WaysOfSimilarity.SMOASimilarity(align[0], align[1]) != 1) {
                                boolean flag = false;
                                boolean flag1 = false;
                                boolean flag2 = false;
                                for (int p = 0; p < allFather1.size(); p++) {
                                    if (allFather1.get(p).equals(align[0]))
                                        flag1 = true;
                                }
                                for (int q = 0; q < allFather2.size(); q++) {
                                    if (allFather2.get(q).equals(align[1]))
                                        flag2 = true;
                                }
                                if (flag1 != flag2)
                                    flag = true;
                                if (flag)
                                    tempAlignment.add(alignment.get(j));
                            }
                        }

                        for (int j = 0; j < alignment.size(); j++) {
                            String[] align = splitString1(alignment.get(j));
                            if (WaysOfSimilarity.SMOASimilarity(align[0], align[1]) != 1) {
                                boolean flag = false;
                                boolean flag1 = false;
                                boolean flag2 = false;
                                for (int p = 0; p < allSon1.size(); p++) {
                                    if (allSon1.get(p).equals(align[0]))
                                        flag1 = true;
                                }
                                for (int q = 0; q < allSon2.size(); q++) {
                                    if (allSon2.get(q).equals(align[1]))
                                        flag2 = true;
                                }
                                if (flag1 != flag2)
                                    flag = true;
                                if (flag)
                                    tempAlignment.add(alignment.get(j));
                            }
                        }
                    }
                }
                delete(tempAlignment);
                for (int m = 0; m < alignment.size(); m++) {
                    for (int j = 0; j < tempAlignment.size(); j++) {
                        if (alignment.get(m).equals(tempAlignment.get(j))) {
                            alignment.remove(m);
                            m--;
                            break;
                        }
                    }
                }

                //二轮排除：通过子父类概念是否相似，排除高相似度但不具备匹配关系的实体
                for (int m = 0; m < alignment.size(); m++) {
                    String[] strs = splitString1(alignment.get(m));
                    if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) != 1) {
                        List<String> fatherAndSon1 = new ArrayList<>();
                        List<String> fatherAndSon2 = new ArrayList<>();
                        int index1 = 0;
                        for (; index1 < source1.get(1).size(); index1++) {
                            if (source1.get(1).get(index1).equals(strs[0]))
                                break;
                        }
                        fatherAndSon1 = exclude.getAllFatherAndSon(source1.get(1).get(index1), source1);

                        int index2 = 0;
                        for (; index2 < target1.get(1).size(); index2++) {
                            if (target1.get(1).get(index2).equals(strs[1]))
                                break;
                        }
                        fatherAndSon2 = exclude.getAllFatherAndSon(target1.get(1).get(index2), target1);

                        double max = 0;
                        if (fatherAndSon1.size() != 0 && fatherAndSon2.size() != 0) {
                            for (int p = 0; p < fatherAndSon1.size(); p++) {
                                for (int q = 0; q < fatherAndSon2.size(); q++) {
                                    double sim = WaysOfSimilarity.SMOASimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                                    if (sim > max)
                                        max = sim;
                                }
                            }
                        }
                        if (max < 1)
                            tempAlignment.add(alignment.get(m));
                    }
                }
                for (int m = 0; m < alignment.size(); m++) {
                    for (int j = 0; j < tempAlignment.size(); j++) {
                        if (alignment.get(m).equals(tempAlignment.get(j))) {
                            alignment.remove(m);
                            m--;
                            break;
                        }
                    }
                }

                for (int m = 0; m < alignment.size(); m++) {
                    String[] strs = splitString1(alignment.get(m));
                    List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.5);
                    for (int j = 0; j < list.size(); j++) {
                        propertyMappings.add(list.get(j));
                    }
                }

                for (int m = 0; m < propertyMappings.size(); m++) {
                    alignment.add(propertyMappings.get(m));
                }


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
                double[] arr = new double[2];
                double t = endTime - startTime;
                allT = allT + t;
                arr[0] = allT;
                arr[1] = newF_measure;
                excel.writeToExcel1(arr, path, 1, i);
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
                if (recall >= 1 && precision == 1) {
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

    private static String[] splitString1(String s) {
        return s.split("---");
    }
    private static String[] splitString2(String s) {
        return s.split(" ");
    }
}
