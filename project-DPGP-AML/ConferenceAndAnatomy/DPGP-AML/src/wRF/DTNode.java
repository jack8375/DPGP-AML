package wRF;

public class DTNode {

    private DTNode parent;
    private DTNode left;
    private DTNode right;
    private double[][] dataSet;
    private double[] results;
    private char[] labels;
    private char classifyLabel;  //节点的分类标签

    public DTNode getParent() {
        return parent;
    }

    public void setParent(DTNode parent) {
        this.parent = parent;
    }

    public DTNode getLeft() {
        return left;
    }

    public void setLeft(DTNode left) {
        this.left = left;
    }

    public DTNode getRight() {
        return right;
    }

    public void setRight(DTNode right) {
        this.right = right;
    }

    public double[][] getDataSet() {
        return dataSet;
    }

    public void setDataSet(double[][] dataSet) {
        this.dataSet = dataSet;
    }

    public double[] getResults() {
        return results;
    }

    public void setResults(double[] results) {
        this.results = results;
    }

    public char[] getLabels() {
        return labels;
    }

    public void setLabels(char[] labels) {
        this.labels = labels;
    }

    public char getClassifyLabel() {
        return classifyLabel;
    }

    public void setClassifyLabel(char classifyLabel) {
        this.classifyLabel = classifyLabel;
    }
}
