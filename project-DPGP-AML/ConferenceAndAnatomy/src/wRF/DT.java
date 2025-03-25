package wRF;

import java.util.ArrayList;
import java.util.List;

public class DT {
    public static List<DTNode> creatTree(double[][] dataSet, double[] results, char[] labels) {
        List<DTNode> DTree = new ArrayList<>();
        //初始化根节点
        DTNode root = new DTNode();
        root.setDataSet(dataSet);
        root.setResults(results);
        root.setLabels(labels);
        DTree.add(root);

        for (int i = 0; i < DTree.size(); i++) {
            //System.out.println("第" + i + "次划定标签");
            root = DTree.get(i);
            if (pureLeaf(root)) {  //对于纯叶节点，放置分类标签'y'或'n'
                if (root.getResults()[0] == 0)
                    root.setClassifyLabel('n');
                else root.setClassifyLabel('y');
            } else {
                if (root.getLabels().length == 0)  //如果所有特征都遍历完仍未得到纯叶节点，则返回当前节点中最多的类别
                    root.setClassifyLabel(moreClass(root));
                else {  //生成左右子节点
                    double[][] nodeDataset = root.getDataSet();
                    double[] nodeResults = root.getResults();
                    char[] nodeLabels = root.getLabels();
                    char label = maxEDlabel(nodeDataset, nodeResults, nodeLabels);
                    if (label == 'n' || label == 'y')
                        root.setClassifyLabel(label);
                    else {
                        root.setClassifyLabel(label);
                        int feat = switch (label) {
                            case '零' -> 0;
                            case '一' -> 1;
                            case '二' -> 2;
                            case '三' -> 3;
                            case '四' -> 4;
                            case '五' -> 5;
                            case '六' -> 6;
                            case '七' -> 7;
                            case '八' -> 8;
                            case '九' -> 9;
                            default -> throw new IllegalStateException("Unexpected value: " + label);
                        };
                        int num1 = 0, num2 = 0;
                        for (int j = 0; j < nodeDataset.length; j++) {
                            if (nodeDataset[j][feat] == 0)
                                num1++;
                            else num2++;
                        }

                        DTNode left = new DTNode();
                        DTNode right = new DTNode();
                        double[][] leftDataSet = new double[num1][nodeDataset[0].length];
                        double[] leftResults = new double[num1];
                        char[] leftLabels = delete(nodeLabels, label);
                        double[][] rightDataSet = new double[num2][nodeDataset[0].length];
                        double[] rightResults = new double[num2];
                        char[] rightLabels = delete(nodeLabels, label);
                        int leftIndex = 0, rightIndex = 0;
                        for (int j = 0; j < nodeDataset.length; j++) {
                            if (nodeDataset[j][feat] == 0) {
                                leftDataSet[leftIndex] = nodeDataset[j];
                                leftResults[leftIndex] = nodeResults[j];
                                leftIndex++;
                            } else {
                                rightDataSet[rightIndex] = nodeDataset[j];
                                rightResults[rightIndex] = nodeResults[j];
                                rightIndex++;
                            }
                        }
                        left.setDataSet(leftDataSet);
                        left.setResults(leftResults);
                        left.setLabels(leftLabels);
                        left.setParent(root);
                        root.setLeft(left);
                        DTree.add(left);

                        right.setDataSet(rightDataSet);
                        right.setResults(rightResults);
                        right.setLabels(rightLabels);
                        right.setParent(root);
                        root.setRight(right);
                        DTree.add(right);
                    }
                }
            }
            //System.out.println(DTree.get(i).getClassifyLabel());
        }
        return DTree;
    }

    //决策树对测试数据的分类
    public static int decodeDT(List<DTNode> trainTree, double[] test) {
        DTNode root = trainTree.get(0);
        while (true) {
            char label = root.getClassifyLabel();
            if (label == 'n')
                return 0;
            else if (label == 'y')
                return 1;
            else {
                int feat = switch (label) {
                    case '零' -> 0;
                    case '一' -> 1;
                    case '二' -> 2;
                    case '三' -> 3;
                    case '四' -> 4;
                    case '五' -> 5;
                    case '六' -> 6;
                    case '七' -> 7;
                    case '八' -> 8;
                    case '九' -> 9;
                    default -> throw new IllegalStateException("Unexpected value: " + label);
                };
                if (test[feat] == 0)
                    root = root.getLeft();
                else
                    root = root.getRight();
            }
        }
    }


    //判断当前节点是否为纯叶节点，若是，则停止生成树
    public static boolean pureLeaf(DTNode node) {
        int num1 = 0, num2 = 0;
        double[] nodeResults = node.getResults();
        for (int i = 0; i < nodeResults.length; i++) {
            if (nodeResults[i] == 0)
                num1++;
            else num2++;
        }
        if (num1 == nodeResults.length || num2 == nodeResults.length)
            return true;
        else return false;
    }

    //获取当前节点中的较多类
    public static char moreClass(DTNode node) {
        int num1 = 0, num2 = 0;
        for (int i = 0; i < node.getResults().length; i++) {
            if (node.getResults()[i] == 0)
                num1++;
            else num2++;
        }
        if (num1 >= num2)
            return 'n';
        else return 'y';
    }

    //获取最大熵差对应的特征值标签
    public static char maxEDlabel(double[][] dataSet, double[] results, char[] labels) {
        double[] EDarr = new double[labels.length];
        for (int i = 0; i < labels.length; i++) {
            EDarr[i] = entropyDifference(dataSet, results, labels[i]);
        }
        sort(EDarr);
        if (EDarr[0] == 0) {  //最大熵差为0，则每个熵差均为0，代表每个特征都无法完成划分，直接返回较多类作为特征值标签
            int num1 = 0, num2 = 0;
            for (int i = 0; i < results.length; i++) {
                if (results[i] == 0)
                    num1++;
                else num2++;
            }
            if (num1 >= num2)
                return 'n';
            else return 'y';
        } else {
            int index = 0;
            for (int i = 0; i < labels.length; i++) {
                if (entropyDifference(dataSet, results, labels[i]) == EDarr[0]) {
                    index = i;
                    break;
                }
            }
            return labels[index];
        }
    }

    //计算特征值对应的熵差
    public static double entropyDifference(double[][] dataSet, double[] results, char label) {
        int feat = switch (label) {
            case '零' -> 0;
            case '一' -> 1;
            case '二' -> 2;
            case '三' -> 3;
            case '四' -> 4;
            case '五' -> 5;
            case '六' -> 6;
            case '七' -> 7;
            case '八' -> 8;
            case '九' -> 9;
            default -> throw new IllegalStateException("Unexpected value: " + label);
        };
        double Eparent;  //父类的熵
        double parentClass1 = 0, parentClass2 = 0;
        for (int i = 0; i < results.length; i++) {
            if (results[i] == 0)
                parentClass1++;
            else parentClass2++;
        }
        double p = parentClass1 / (parentClass1 + parentClass2);
        double q = parentClass2 / (parentClass1 + parentClass2);
        if ((p == 0 && q == 1) || (p == 1 && q == 0))
            Eparent = 0;
        else Eparent = -p * logarithm(p, 2) - q * logarithm(q, 2);

        double Echild1;  //子类1的熵
        double Echild2;  //子类2的熵
        double child1Class1 = 0, child1Class2 = 0, child2Class1 = 0, child2Class2 = 0;
        double weight1 = 0, weight2 = 0;
        for (int i = 0; i < dataSet.length; i++) {
            if (dataSet[i][feat] == 0) {
                weight1++;
                if (results[i] == 0)
                    child1Class1++;
                else child1Class2++;

            }
            if (dataSet[i][feat] == 1) {
                weight2++;
                if (results[i] == 0)
                    child2Class1++;
                else child2Class2++;
            }
        }
        double entropyDifference;  //熵差
        if ((child1Class1 + child1Class2) == 0 || (child2Class1 + child2Class2) == 0)
            entropyDifference = 0;  //若某个特征划分出空叶子节点，则置为一次无用的划分，熵差为0
        else {
            double m1 = child1Class1 / (child1Class1 + child1Class2);
            double n1 = child1Class2 / (child1Class1 + child1Class2);
            if ((m1 == 0 && n1 == 1) || (m1 == 1 && n1 == 0))
                Echild1 = 0;
            else Echild1 = -m1 * logarithm(m1, 2) - n1 * logarithm(n1, 2);
            double m2 = child2Class1 / (child2Class1 + child2Class2);
            double n2 = child2Class2 / (child2Class1 + child2Class2);
            if ((m2 == 0 && n2 == 1) || (m2 == 1 && n2 == 0))
                Echild2 = 0;
            else Echild2 = -m2 * logarithm(m2, 2) - n2 * logarithm(n2, 2);
            entropyDifference = Eparent - ((weight1 / dataSet.length) * Echild1) - ((weight2 / dataSet.length) * Echild2);
        }
        return entropyDifference;
    }

    //删除标签
    private static char[] delete(char[] labels, char deleteLabel) {
        char[] newArr = new char[labels.length - 1];
        int index = 0;
        for (int i = 0; i < labels.length; i++) {
            if (labels[i] != deleteLabel) {
                newArr[index] = labels[i];
                index++;
            }
        }
        return newArr;
    }

    private static double logarithm(double value, double base) {
        return Math.log(value) / Math.log(base);
    }

    //降序排列
    private static void sort(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
