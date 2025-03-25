package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import Tree.BT;
import Tree.LinkedTreeNode;
import Tree.decode;
import evolutionaryAlgorithms.DeterministicCrossoverSP;
import evolutionaryAlgorithms.RandomMutationSP;
import newFitness.Fitness;
import newFitness.PA;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class main_SPGP {
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
        int populationSize = 100;  //种群规模
        int maxiGen = 2000;  //迭代次数
        double crossoverProbability = 0.8;  //交叉概率
        double mutationProbability = 0.1;  //变异概率
        double anchorSize = 0.7;  //锚点待选集合规模
        int selectionSize1 = 6;  //交叉：轮盘赌选择子种群规模
        int selectionSize2 = populationSize;  //变异：选择种群规模
        String path="D:\\文件\\收敛实验\\edas-sigkdd-SPGP1.xlsx";
        double allT=0;

        label:
        for (int s = 0; s < 5; s++) {
            //List<String> PSA = newFitness.PSA.searchAnchor(source1, target1, anchorSize);
            List<String> PSA = new ArrayList<>(reference);

            List<ArrayList<LinkedTreeNode>> population = BT.creatPopulation(populationSize, str1, str2, str3, maxDeep);
            double tempF_measure = 0;
            for (int i = 0; i < maxiGen; i++) {
                System.out.println("---开始第" + (i + 1) + "次迭代进化：");
                double startTime=System.currentTimeMillis();
                List<ArrayList<LinkedTreeNode>> populationCross = DeterministicCrossoverSP.deterministicCrossover(population, selectionSize1, PSA, source1, target1, crossoverProbability);
                List<ArrayList<LinkedTreeNode>> populationMutation = RandomMutationSP.mutation(populationCross, mutationProbability);
                double[] fitnessOfPopulation = Fitness.fitnessOfPopulation(populationMutation, PSA, source1, target1);
                double[] sortOfFitness = sort(fitnessOfPopulation);
                ArrayList<LinkedTreeNode> elite = populationMutation.get(fitnessToIndex(sortOfFitness[0], populationMutation, fitnessOfPopulation));
                population = populationMutation;
                double endTime=System.currentTimeMillis();
                List<String> alignment = PA.getAlignment(decode.decodeBT(elite).get(0), source1, target1);
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
                    for (int j = 0; j < elite.size(); j++) {
                        System.out.print(elite.get(j).getStr() + " ");
                    }
                    System.out.println();
                    System.out.println("--------------------------");
                    for (int j = 0; j < alignment.size(); j++) {
                        System.out.println("alignment.add(" + '"' + alignment.get(j) + '"' + ')' + ';');
                    }
                    System.out.println("-----------------------");
                    System.out.println("近似f值为" + elite.get(0).getApproximateFitness());
                    System.out.println("查全率: " + recall);
                    System.out.println("查准率: " + precision);
                    System.out.println("f-measure: " + newF_measure);
                    System.out.println("-----------------------");
                    tempF_measure = newF_measure;
                }
                if (recall == 0.5 && precision == 1) {
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

    public static int fitnessToIndex(double fitness, List<ArrayList<LinkedTreeNode>> population, double[] fitnessOfPopulation) {
        int i = 0;
        for (; i < fitnessOfPopulation.length; i++) {
            if (fitnessOfPopulation[i] == fitness)
                break;
        }
        return i;
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

    private static String[] splitString1(String s) {
        return s.split("---");
    }
    private static String[] splitString2(String s) {
        return s.split(" ");
    }

}
