package Tree;

public class LinkedTreeNode {

    private LinkedTreeNode parent;
    private String str;
    private LinkedTreeNode left;
    private LinkedTreeNode right;
    private LinkedTreeNode child;
    private int deep;
    private double[][] value;
    private double approximateRecall;
    private double approximatePrecision;
    private double approximateFitness;


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
