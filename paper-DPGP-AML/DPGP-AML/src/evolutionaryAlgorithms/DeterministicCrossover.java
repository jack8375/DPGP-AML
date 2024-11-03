package evolutionaryAlgorithms;

import Tree.LinkedTreeNode;
import Tree.decode;
import newFitness.Fitness;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DeterministicCrossover implements Serializable{

    public static List<ArrayList<LinkedTreeNode>> deterministicCrossover(List<ArrayList<LinkedTreeNode>> populationBetter, int selectionSize, List<String> PSA, List<List<String>> source, List<List<String>> target) throws IOException {

        double[] fitnessOfPopulation = Fitness.fitnessOfPopulation(populationBetter, PSA, source, target);
        for (int i = 0; i < populationBetter.size() / 2; i++) {

            ArrayList<LinkedTreeNode> parentInd1 = parentInd(populationBetter, selectionSize, PSA, fitnessOfPopulation, source, target);
            int index1 = indToIndex(parentInd1, populationBetter);
            populationBetter.remove(index1);
            double[] fitnessOfPopulation1 = new double[fitnessOfPopulation.length - 1];
            for (int j = 0; j < fitnessOfPopulation1.length; j++) {
                fitnessOfPopulation1[j] = populationBetter.get(j).get(0).getApproximateFitness();
            }
            ArrayList<LinkedTreeNode> parentInd2 = parentInd(populationBetter, selectionSize, PSA, fitnessOfPopulation1, source, target);
            int index2 = indToIndex(parentInd2, populationBetter);
            populationBetter.remove(index2);

            ArrayList<LinkedTreeNode> parentCopyInd1 = copy(parentInd1);
            ArrayList<LinkedTreeNode> parentCopyInd2 = copy(parentInd2);
            double parentInd1Fitness = Fitness.getFitness(parentCopyInd1, PSA, source, target);
            double parentInd2Fitness = Fitness.getFitness(parentCopyInd2, PSA, source, target);
            List<ArrayList<LinkedTreeNode>> offspring = crossover(parentInd1, parentInd2);
            ArrayList<LinkedTreeNode> offspring1 = offspring.get(0);
            ArrayList<LinkedTreeNode> offspring2 = offspring.get(1);
            double offspring1Fitness = Fitness.getFitness(offspring1, PSA, source, target);
            double offspring2Fitness = Fitness.getFitness(offspring2, PSA, source, target);

            double semSim1 = semSim(offspring1, parentCopyInd1, source, target);
            double semSim2 = semSim(offspring1, parentCopyInd2, source, target);
            double semSim3 = semSim(offspring2, parentCopyInd1, source, target);
            double semSim4 = semSim(offspring2, parentCopyInd2, source, target);

            if (semSim1 < semSim2 && semSim3 < semSim4) {
                if (offspring1Fitness > parentInd1Fitness) {
                    if (offspring1Fitness > offspring2Fitness) {
                        populationBetter.add(offspring1);
                    } else {
                        populationBetter.add(offspring2);
                    }
                } else {
                    if (parentInd1Fitness >= offspring2Fitness) {
                        populationBetter.add(parentCopyInd1);
                    } else {
                        populationBetter.add(offspring2);
                    }
                }
                populationBetter.add(parentCopyInd2);
            }

            if (semSim1 >= semSim2 && semSim3 >= semSim4) {
                if (offspring1Fitness > parentInd2Fitness) {
                    if (offspring1Fitness > offspring2Fitness) {
                        populationBetter.add(offspring1);
                    } else {
                        populationBetter.add(offspring2);
                    }
                } else {
                    if (parentInd2Fitness >= offspring2Fitness) {
                        populationBetter.add(parentCopyInd2);
                    } else {
                        populationBetter.add(offspring2);
                    }
                }
                populationBetter.add(parentCopyInd1);
            }

            if (semSim1 < semSim2 && semSim3 >= semSim4) {
                if (offspring1Fitness > parentInd1Fitness) {
                    populationBetter.add(offspring1);
                } else {
                    populationBetter.add(parentCopyInd1);
                }
                if (offspring2Fitness > parentInd2Fitness) {
                    populationBetter.add(offspring2);
                } else {
                    populationBetter.add(parentCopyInd2);
                }
            }

            if (semSim1 >= semSim2 && semSim3 < semSim4) {
                if (offspring1Fitness > parentInd2Fitness) {
                    populationBetter.add(offspring1);
                } else {
                    populationBetter.add(parentCopyInd2);
                }
                if (offspring2Fitness > parentInd1Fitness) {
                    populationBetter.add(offspring2);
                } else {
                    populationBetter.add(parentCopyInd1);
                }
            }
        }
        return populationBetter;
    }

    public static List<ArrayList<LinkedTreeNode>> crossover(ArrayList<LinkedTreeNode> parentInd1, ArrayList<LinkedTreeNode> parentInd2) {

        List<ArrayList<LinkedTreeNode>> offspring = new ArrayList<>();
        int n1, n2;
        while (true) {
            n1 = Double.valueOf(Math.random() * parentInd1.size()).intValue();
            if (parentInd1.get(n1).getStr().equals("add") || parentInd1.get(n1).getStr().equals("sub") || parentInd1.get(n1).getStr().equals("mul") || parentInd1.get(n1).getStr().equals("div")
                    || parentInd1.get(n1).getStr().equals("max") || parentInd1.get(n1).getStr().equals("min") || parentInd1.get(n1).getStr().equals("avg"))
                break;
        }
        while (true) {
            n2 = Double.valueOf(Math.random() * parentInd2.size()).intValue();
            if (parentInd2.get(n2).getStr().equals("add") || parentInd2.get(n2).getStr().equals("sub") || parentInd2.get(n2).getStr().equals("mul") || parentInd2.get(n2).getStr().equals("div")
                    || parentInd2.get(n2).getStr().equals("max") || parentInd2.get(n2).getStr().equals("min") || parentInd2.get(n2).getStr().equals("avg"))
                break;
        }
        LinkedTreeNode node1 = parentInd1.get(n1);
        LinkedTreeNode node2 = parentInd2.get(n2);
        LinkedTreeNode nodeCross = new LinkedTreeNode();

        if (node1.getDeep() == 1)
            node1.getParent().setChild(nodeCross);
        else {
            if (node1.getParent().getRight() == node1)
                node1.getParent().setRight(nodeCross);
            if (node1.getParent().getLeft() == node1)
                node1.getParent().setLeft(nodeCross);
        }
        nodeCross.setParent(node1.getParent());
        nodeCross.setDeep(node1.getParent().getDeep() + 1);

        if (node2.getDeep() == 1)
            node2.getParent().setChild(node1);
        else {
            if (node2.getParent().getRight() == node2)
                node2.getParent().setRight(node1);
            if (node2.getParent().getLeft() == node2)
                node2.getParent().setLeft(node1);
        }
        node1.setParent(node2.getParent());
        node1.setDeep(node2.getParent().getDeep() + 1);

        if (nodeCross.getDeep() == 1)
            nodeCross.getParent().setChild(node2);
        else {
            if (nodeCross.getParent().getRight() == nodeCross)
                nodeCross.getParent().setRight(node2);
            if (nodeCross.getParent().getLeft() == nodeCross)
                nodeCross.getParent().setLeft(node2);
        }
        node2.setParent(nodeCross.getParent());
        node2.setDeep(nodeCross.getParent().getDeep() + 1);
        nodeCross.setParent(null);

        offspring.add(nodeToTree(parentInd1.get(0)));
        offspring.add(nodeToTree(parentInd2.get(0)));
        return offspring;
    }

    public static double semSim(ArrayList<LinkedTreeNode> ind1, ArrayList<LinkedTreeNode> ind2, List<List<String>> source, List<List<String>> target) throws IOException {
        List<double[][]> binaryAndSimilarityMatrix1 = decode.decodeBT(ind1, source, target);
        double[][] binaryMatrix1 = binaryAndSimilarityMatrix1.get(0);
        List<double[][]> binaryAndSimilarityMatrix2 = decode.decodeBT(ind2, source, target);
        double[][] binaryMatrix2 = binaryAndSimilarityMatrix2.get(0);
        //int size = binaryMatrix1.length * binaryMatrix1[0].length;
        double sum = 0;
        for (int i = 0; i < binaryMatrix1.length; i++) {
            for (int j = 0; j < binaryMatrix1[i].length; j++) {
                sum += Math.pow(binaryMatrix1[i][j] - binaryMatrix2[i][j], 2);
            }
        }
        //double semSim = 1 - (Math.sqrt(sum) / size);
        return sum;
    }

    public static ArrayList<LinkedTreeNode> parentInd(List<ArrayList<LinkedTreeNode>> population, int selectionSize, List<String> PSA, double[] fitnessOfPopulation, List<List<String>> source, List<List<String>> target) throws IOException {
        List<ArrayList<LinkedTreeNode>> subPopulation1 = roulette(population, selectionSize, fitnessOfPopulation);
        List<ArrayList<LinkedTreeNode>> subPopulation2 = roulette(population, selectionSize, fitnessOfPopulation);
        double[] fitnessOfSubPopulation1 = new double[selectionSize];
        for (int i = 0; i < fitnessOfSubPopulation1.length; i++) {
            fitnessOfSubPopulation1[i] = subPopulation1.get(i).get(0).getApproximateFitness();
        }
        double[] fitnessOfSubPopulation2 = new double[selectionSize];
        for (int i = 0; i < fitnessOfSubPopulation2.length; i++) {
            fitnessOfSubPopulation2[i] = subPopulation2.get(i).get(0).getApproximateFitness();
        }
        int maxIndex1 = 0;
        for (int i = 1; i < fitnessOfSubPopulation1.length; i++) {
            if (fitnessOfSubPopulation1[i] > fitnessOfSubPopulation1[maxIndex1])
                maxIndex1 = i;
            if (fitnessOfSubPopulation1[i] == fitnessOfSubPopulation1[maxIndex1] && subPopulation1.get(i).size() < subPopulation1.get(maxIndex1).size())
                maxIndex1 = i;
        }

        int maxIndex2 = 0;
        for (int j = 1; j < fitnessOfSubPopulation2.length; j++) {
            if (fitnessOfSubPopulation2[j] > fitnessOfSubPopulation2[maxIndex2])
                maxIndex2 = j;
            if (fitnessOfSubPopulation2[j] == fitnessOfSubPopulation2[maxIndex2] && subPopulation2.get(j).size() < subPopulation2.get(maxIndex2).size())
                maxIndex2 = j;
        }

        if (subPopulation1.get(maxIndex1).size() <= subPopulation2.get(maxIndex2).size())
            return subPopulation1.get(maxIndex1);
        else
            return subPopulation2.get(maxIndex2);
    }

    public static int indToIndex(ArrayList<LinkedTreeNode> ind, List<ArrayList<LinkedTreeNode>> population) {
        int i = 0;
        for (; i < population.size(); i++) {
            if (population.get(i).get(0) == ind.get(0))
                break;
        }
        return i;
    }

    public static List<ArrayList<LinkedTreeNode>> roulette(List<ArrayList<LinkedTreeNode>> population, int selectionSize, double[] fitnessOfPopulation) {
        List<ArrayList<LinkedTreeNode>> subPopulation = new ArrayList<>();
        double[] copyFitness = new double[fitnessOfPopulation.length];
        for (int i = 0; i < copyFitness.length; i++) {
            copyFitness[i] = fitnessOfPopulation[i];
        }
        for (int i = 0; i < selectionSize; i++) {
            double r = Math.random();
            double[] probabilityOfFitness = new double[copyFitness.length];
            double sum = 0;
            for (int j = 0; j < copyFitness.length; j++) {
                sum += copyFitness[j];
            }
            double acc = 0;
            for (int j = 0; j < copyFitness.length; j++) {
                acc += copyFitness[j];
                probabilityOfFitness[j] = acc / sum;
            }
            for (int index = 0; index < probabilityOfFitness.length; index++) {
                if (r <= probabilityOfFitness[index]) {
                    subPopulation.add(population.get(index));
                    copyFitness[index] = 0;
                    break;
                }
            }
        }
        return subPopulation;
    }

    public static ArrayList<LinkedTreeNode> nodeToTree(LinkedTreeNode node) {
        ArrayList<LinkedTreeNode> list = new ArrayList<>();
        list.add(node);
        for (int i = 0; i < list.size(); i++) {
            LinkedTreeNode root = list.get(i);
            if (root.getChild() != null) {
                list.add(root.getChild());
            }
            if (root.getLeft() != null) {
                list.add(root.getLeft());
            }
            if (root.getRight() != null) {
                list.add(root.getRight());
            }
        }
        return list;
    }

    public static ArrayList<LinkedTreeNode> copy(ArrayList<LinkedTreeNode> list) {
        ArrayList<LinkedTreeNode> copy = new ArrayList<>();
        LinkedTreeNode root = new LinkedTreeNode();
        root.setStr(list.get(0).getStr());
        root.setDeep(0);
        root.setParent(null);
        copy.add(root);
        LinkedTreeNode rootChild = new LinkedTreeNode();
        rootChild.setStr(list.get(1).getStr());
        rootChild.setDeep(1);
        rootChild.setParent(root);
        root.setChild(rootChild);
        copy.add(rootChild);
        int index = 2;
        for (int i = 1; i < copy.size(); i++) {
            root = copy.get(i);
            if (!root.getStr().equals("NGram") && !root.getStr().equals("SMOA") && !root.getStr().equals("wuAndPalmer") && !root.getStr().equals("sonGreedy")) {
                LinkedTreeNode left = new LinkedTreeNode();
                left.setStr(list.get(index).getStr());
                left.setDeep(list.get(index).getDeep());
                left.setParent(root);
                root.setLeft(left);
                copy.add(left);
                index++;
                LinkedTreeNode right = new LinkedTreeNode();
                right.setStr(list.get(index).getStr());
                right.setDeep(list.get(index).getDeep());
                right.setParent(root);
                root.setRight(right);
                copy.add(right);
                index++;
            }
        }
        return copy;
    }

    public static LinkedTreeNode copyTree(LinkedTreeNode root) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(root);
        oos.close();
        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        LinkedTreeNode newTree = (LinkedTreeNode) ois.readObject();
        ois.close();
        return newTree;
    }
}
