package Tree;

import java.util.ArrayList;
import java.util.List;

public class BT {
    public static ArrayList<LinkedTreeNode> createTree(String[] str1, String[] str2, String[] str3, int maxDeep) {

        ArrayList<LinkedTreeNode> linkedtree = new ArrayList<>();
        int s1 = Double.valueOf(Math.random() * str1.length).intValue();  //随机取一种分类方法
        int s2_1 = Double.valueOf(Math.random() * str2.length).intValue();  //随机取一种结构方法
        //初始化根节点
        LinkedTreeNode root = new LinkedTreeNode();
        root.setStr(str1[s1]);
        root.setValue(null);
        root.setDeep(0);
        root.setParent(null);
        linkedtree.add(root);
        //初始化根节点下的单一根子节点
        LinkedTreeNode rootChild = new LinkedTreeNode();
        rootChild.setStr(str2[s2_1]);
        rootChild.setDeep(1);
        rootChild.setParent(root);
        root.setChild(rootChild);
        linkedtree.add(rootChild);
        //从根子节点开始构建二叉树
        for (int i = 1; i < linkedtree.size(); i++) {
            root = linkedtree.get(i);
            while (!root.getStr().equals("NGram") && !root.getStr().equals("SMOA") && !root.getStr().equals("dictionary")) {  //终端节点则停止构建
                double r1 = Math.random();  //用于随机生成左子节点
                double r2 = Math.random();  //用于随机生成右子节点
                if (linkedtree.get(linkedtree.size() - 1).getDeep() >= maxDeep) {
                    r1 = r2 = 0.1;
                }  //强制生成终端节点，限制树的规模

                //生成左子节点，0.5概率生成结构层节点，0.5概率生成终端节点
                if (r1 > 0.5) {
                    int s2_2 = Double.valueOf(Math.random() * str2.length).intValue();  //随机取一种结构方法
                    LinkedTreeNode left = new LinkedTreeNode();
                    left.setStr(str2[s2_2]);
                    left.setDeep(root.getDeep() + 1);
                    left.setParent(root);
                    root.setLeft(left);
                    linkedtree.add(left);
                } else {
                    int s3 = Double.valueOf(Math.random() * str3.length).intValue();  //随机取一种相似度度量方法
                    LinkedTreeNode left = new LinkedTreeNode();
                    left.setStr(str3[s3]);
                    left.setDeep(root.getDeep() + 1);
                    left.setParent(root);
                    root.setLeft(left);
                    linkedtree.add(left);
                }

                //右子节点同理
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


    //创建种群
    public static List<ArrayList<LinkedTreeNode>> creatPopulation(int populationSize, String[] str1, String[] str2, String[] str3, int maxDeep) {
        List<ArrayList<LinkedTreeNode>> population = new ArrayList<>();
        for (int i = 0; i < populationSize; i++)
            population.add(createTree(str1, str2, str3, maxDeep));
        return population;
    }

}