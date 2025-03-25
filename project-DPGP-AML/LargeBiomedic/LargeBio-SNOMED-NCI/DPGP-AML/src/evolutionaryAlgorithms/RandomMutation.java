package evolutionaryAlgorithms;

import Tree.BT;
import Tree.LinkedTreeNode;
import newFitness.Fitness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RandomMutation {

    //变异操作
    public static List<ArrayList<LinkedTreeNode>> mutation(List<ArrayList<LinkedTreeNode>> populationWorse, int selectionSize, List<String> PSA,
                                                           List<List<String>> sourceCluster, List<List<String>> targetCluster, int taskNum, double mutationProbability) throws IOException {
        List<ArrayList<LinkedTreeNode>> populationUnion = new ArrayList<>();
        for (int i = 0; i < populationWorse.size(); i++) {
            populationUnion.add(DeterministicCrossover.copy(populationWorse.get(i)));
        }
        for (ArrayList<LinkedTreeNode> linkedTree : populationWorse) {
            double r = Math.random();
            if (r < 0.5) {
                int s;
                while (true) {
                    s = Double.valueOf(Math.random() * linkedTree.size()).intValue();  //随机取一个节点
                    double rand = Math.random();
                    if (s != 0 && rand < mutationProbability)
                        break;
                }
                linkedTree.get(s).setStr(mutationWay1(linkedTree.get(s)));
                populationUnion.add(linkedTree);
            } else {
                String[] str1 = {"top1", "top30", "top50"};
                String[] str2 = {"add", "sub", "mul", "div", "max", "min", "avg"};
                String[] str3 = {"NGram", "SMOA", "wuAndPalmer", "sonGreedy"};
                ArrayList<LinkedTreeNode> mutationTree = mutationWay2(linkedTree, str1, str2, str3, mutationProbability);
                populationUnion.add(mutationTree);
            }
        }
        List<ArrayList<LinkedTreeNode>> populationUpdate = DeterministicCrossover.roulette(populationUnion, selectionSize, Fitness.fitnessOfPopulation(populationUnion, PSA, sourceCluster, targetCluster, taskNum));
        //System.out.println("已执行种群变异");
        return populationUpdate;
    }

    //再随机生成一棵二叉树，两棵树执行一次交叉操作，相当于目标树function节点的变异
    public static ArrayList<LinkedTreeNode> mutationWay2(ArrayList<LinkedTreeNode> linkedTree, String[] str1, String[] str2, String[] str3, double mutationProbability) {
        List<ArrayList<LinkedTreeNode>> list = DeterministicCrossover.crossover(linkedTree, BT.createTree(str1, str2, str3, 3), mutationProbability);
        return list.get(0);
    }

    public static String mutationWay1(LinkedTreeNode node) {
        String mutationStr = "";
        if (node.getStr().equals("top1") || node.getStr().equals("top30") || node.getStr().equals("top50"))
            mutationStr = classificationWay(node.getStr());
        if (node.getStr().equals("add") || node.getStr().equals("sub") || node.getStr().equals("mul") || node.getStr().equals("div") ||
                node.getStr().equals("max") || node.getStr().equals("min") || node.getStr().equals("avg"))
            mutationStr = constructionWay(node.getStr());
        if (node.getStr().equals("NGram") || node.getStr().equals("SMOA") || node.getStr().equals("dictionary"))
            mutationStr = simWay(node.getStr());
        return mutationStr;
    }

    public static String classificationWay(String s) {
        String[] str1 = {"top1", "top30", "top50"};
        String mutationStr;
        while (true) {
            int s1 = Double.valueOf(Math.random() * str1.length).intValue();
            mutationStr = str1[s1];
            if (mutationStr.equals(s))
                continue;
            else break;
        }
        return mutationStr;
    }

    public static String constructionWay(String s) {
        String[] str2 = {"add", "sub", "mul", "div", "max", "min", "avg"};
        String mutationStr;
        while (true) {
            int s2 = Double.valueOf(Math.random() * str2.length).intValue();
            mutationStr = str2[s2];
            if (mutationStr.equals(s))
                continue;
            else break;
        }
        return mutationStr;
    }

    public static String simWay(String s) {
        String[] str3 = {"NGram", "SMOA", "dictionary"};
        String mutationStr;
        while (true) {
            int s3 = Double.valueOf(Math.random() * str3.length).intValue();
            mutationStr = str3[s3];
            if (mutationStr.equals(s))
                continue;
            else break;
        }
        return mutationStr;
    }

}
