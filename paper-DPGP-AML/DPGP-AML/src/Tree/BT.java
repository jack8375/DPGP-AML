package Tree;

import java.util.ArrayList;
import java.util.List;

public class BT {
    public static ArrayList<LinkedTreeNode> createTree(String[] str1, String[] str2, String[] str3, int maxDeep) {

        ArrayList<LinkedTreeNode> linkedtree = new ArrayList<>();
        int s1 = Double.valueOf(Math.random() * str1.length).intValue();
        int s2_1 = Double.valueOf(Math.random() * str2.length).intValue();

        LinkedTreeNode root = new LinkedTreeNode();
        root.setStr(str1[s1]);
        root.setValue(null);
        root.setDeep(0);
        root.setParent(null);
        linkedtree.add(root);
        LinkedTreeNode rootChild = new LinkedTreeNode();
        rootChild.setStr(str2[s2_1]);
        rootChild.setDeep(1);
        rootChild.setParent(root);
        root.setChild(rootChild);
        linkedtree.add(rootChild);

        for (int i = 1; i < linkedtree.size(); i++) {
            root = linkedtree.get(i);
            while (!root.getStr().equals("NGram") && !root.getStr().equals("SMOA") && !root.getStr().equals("wuAndPalmer") && !root.getStr().equals("sonGreedy")) {
                double r1 = Math.random();
                double r2 = Math.random();
                if (linkedtree.get(linkedtree.size() - 1).getDeep() >= maxDeep) {
                    r1 = r2 = 0.1;
                }

                if (r1 > 0.5) {
                    int s2_2 = Double.valueOf(Math.random() * str2.length).intValue();
                    LinkedTreeNode left = new LinkedTreeNode();
                    left.setStr(str2[s2_2]);
                    left.setDeep(root.getDeep() + 1);
                    left.setParent(root);
                    root.setLeft(left);
                    linkedtree.add(left);
                } else {
                    int s3 = Double.valueOf(Math.random() * str3.length).intValue();
                    LinkedTreeNode left = new LinkedTreeNode();
                    left.setStr(str3[s3]);
                    left.setDeep(root.getDeep() + 1);
                    left.setParent(root);
                    root.setLeft(left);
                    linkedtree.add(left);
                }

                if (r2 > 0.5) {
                    int s2_2 = Double.valueOf(Math.random() * str2.length).intValue();
                    LinkedTreeNode right = new LinkedTreeNode();
                    right.setStr(str2[s2_2]);
                    right.setDeep(root.getDeep() + 1);
                    right.setParent(root);
                    root.setRight(right);
                    linkedtree.add(right);
                } else {
                    int s3 = Double.valueOf(Math.random() * str3.length).intValue();
                    LinkedTreeNode right = new LinkedTreeNode();
                    right.setStr(str3[s3]);
                    right.setDeep(root.getDeep() + 1);
                    right.setParent(root);
                    root.setRight(right);
                    linkedtree.add(right);
                }

                i++;
                root = linkedtree.get(i);
            }
        }
        return linkedtree;
    }


    public static List<ArrayList<LinkedTreeNode>> creatPopulation(int populationSize, String[] str1, String[] str2, String[] str3, int maxDeep) {
        List<ArrayList<LinkedTreeNode>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++)
            population.add(createTree(str1, str2, str3, maxDeep));
        return population;
    }

}