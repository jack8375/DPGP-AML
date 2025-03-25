package newFitness;

import Tree.LinkedTreeNode;
import Tree.decode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fitness {

    //由种群得到对应的适应度集合
    public static double[] fitnessOfPopulation(List<ArrayList<LinkedTreeNode>> population, List<String> PSA, List<List<String>> sourceCluster, List<List<String>> targetCluster, int taskNum) throws IOException {
        double[] fitnessOfPopulation = new double[population.size()];
        for (int i = 0; i < fitnessOfPopulation.length; i++) {
            fitnessOfPopulation[i] = getFitness(population.get(i), PSA, sourceCluster, targetCluster, taskNum);
        }
        return fitnessOfPopulation;
    }

    //计算个体的适应度
    public static double getFitness(ArrayList<LinkedTreeNode> linkedTree, List<String> PSA, List<List<String>> sourceCluster, List<List<String>> targetCluster, int taskNum) throws IOException {
        /*for (int i = 0; i < linkedTree.size(); i++) {
            System.out.print(linkedTree.get(i).getStr() + " ");
        }
        System.out.println();*/
        List<double[][]> binaryAndSimilarityMatrix = decode.decodeBT(linkedTree, taskNum);
        List<String> alignment = newFitness.PSA.getAlignment(binaryAndSimilarityMatrix.get(0), sourceCluster, targetCluster, taskNum);
        if (PSA.size() == 3)
            alignment.addAll(PSA);
        double num = 0;
        for (int i = 0; i < PSA.size(); i++) {
            for (int j = 0; j < alignment.size(); j++) {
                if (PSA.get(i).equals(alignment.get(j))) {
                    num++;
                    break;
                }
            }
        }
        double precision = num / alignment.size();  //查准率
        double recall = num / PSA.size();  //查全率
        double fitness = 2 * precision * recall / (precision + recall);
        linkedTree.get(0).setApproximatePrecision(precision);
        linkedTree.get(0).setApproximateRecall(recall);
        if (recall == 0 || precision == 0) {
            linkedTree.get(0).setApproximateFitness(0.00001);
            return 0.00001;
        } else {
            linkedTree.get(0).setApproximateFitness(fitness);
            return fitness;
        }
    }

    //通过匹配关系，返回最终相似度矩阵对应的相似度值
    public static double simValue(String matching, double[][] similarityMatrix, List<List<String>> sourceCluster, List<List<String>> targetCluster, int num) {
        int i = 0;
        int j = 0;
        for (; i < sourceCluster.get(num).size(); i++) {
            if (splitString(matching)[0].equals(sourceCluster.get(num).get(i)))
                break;
        }
        for (; j < targetCluster.get(num).size(); j++) {
            if (splitString(matching)[1].equals(targetCluster.get(num).get(j)))
                break;
        }
        return similarityMatrix[i][j];
    }

    private static String[] splitString(String s) {
        return s.split("~~~");
    }
}
