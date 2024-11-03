package newFitness;

import Tree.LinkedTreeNode;
import Tree.decode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fitness {

    public static double[] fitnessOfPopulation(List<ArrayList<LinkedTreeNode>> population, List<String> PSA, List<List<String>> source, List<List<String>> target) throws IOException {
        double[] fitnessOfPopulation = new double[population.size()];
        for (int i = 0; i < fitnessOfPopulation.length; i++) {
            fitnessOfPopulation[i] = getFitness(population.get(i), PSA, source, target);
        }
        return fitnessOfPopulation;
    }


    public static double getFitness(ArrayList<LinkedTreeNode> linkedTree, List<String> PSA, List<List<String>> source, List<List<String>> target) throws IOException {
        List<double[][]> binaryAndSimilarityMatrix = decode.decodeBT(linkedTree, source, target);
        List<String> alignment = PA.getAlignment(binaryAndSimilarityMatrix.get(0), source, target);
        double simNum = 0;
        double num = 0;
        for (int i = 0; i < PSA.size(); i++) {
            for (int j = 0; j < alignment.size(); j++) {
                if (PSA.get(i).equals(alignment.get(j))) {
                    double sim = simValue(PSA.get(i), binaryAndSimilarityMatrix.get(1), source, target);
                    simNum += sim;
                    num++;
                    break;
                }
            }
        }

        double simLoss = simNum / num;
        double correctness = num / alignment.size();
        double precision = Math.sqrt(simLoss * correctness);
        double recall = num / PSA.size();
        double fitness = 2 * precision * recall / (precision + recall);
        linkedTree.get(0).setApproximatePrecision(precision);
        linkedTree.get(0).setApproximateRecall(recall);
        if (recall == 0 || precision == 0) {
            linkedTree.get(0).setApproximateFitness(0);
            return 0;
        } else {
            linkedTree.get(0).setApproximateFitness(fitness);
            return fitness;
        }
    }


    public static double simValue(String matching, double[][] similarityMatrix, List<List<String>> source, List<List<String>> target) {
        int i = 0;
        int j = 0;
        for (; i < source.get(1).size(); i++) {
            if (splitString(matching)[0].equals(source.get(1).get(i)))
                break;
        }
        for (; j < target.get(1).size(); j++) {
            if (splitString(matching)[1].equals(target.get(1).get(j)))
                break;
        }
        return similarityMatrix[i][j];
    }

    private static String[] splitString(String s) {
        return s.split("---");
    }
}
