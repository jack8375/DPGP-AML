package Tree;

public class LinkedTreeNode {

    private LinkedTreeNode parent;  //父节点
    private String str;  //函数集—————classification方法，construction方法，相似度度量方法
    private LinkedTreeNode left;  //左子节点
    private LinkedTreeNode right;  //右子节点
    private LinkedTreeNode child;  //根子节点
    private int deep;  //深度
    private double[][] value;  //节点对应的相似度矩阵
    private double approximateRecall;  //近似查全
    private double approximatePrecision;  //近似查准
    private double approximateFitness;  //近似f度量


    public LinkedTreeNode getParent() {
        return parent;
    }

    public void setParent(LinkedTreeNode parent) {
        this.parent = parent;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public LinkedTreeNode getLeft() {
        return left;
    }

    public void setLeft(LinkedTreeNode left) {
        this.left = left;
    }

    public LinkedTreeNode getRight() {
        return right;
    }

    public void setRight(LinkedTreeNode right) {
        this.right = right;
    }

    public LinkedTreeNode getChild() {
        return child;
    }

    public void setChild(LinkedTreeNode child) {
        this.child = child;
    }

    public int getDeep() {
        return deep;
    }

    public void setDeep(int deep) {
        this.deep = deep;
    }

    public double[][] getValue() {
        return value;
    }

    public void setValue(double[][] value) {
        this.value = value;
    }

    public double getApproximateRecall() {
        return approximateRecall;
    }

    public void setApproximateRecall(double approximateRecall) {
        this.approximateRecall = approximateRecall;
    }

    public double getApproximatePrecision() {
        return approximatePrecision;
    }

    public void setApproximatePrecision(double approximatePrecision) {
        this.approximatePrecision = approximatePrecision;
    }

    public double getApproximateFitness() {
        return approximateFitness;
    }

    public void setApproximateFitness(double approximateFitness) {
        this.approximateFitness = approximateFitness;
    }
}
