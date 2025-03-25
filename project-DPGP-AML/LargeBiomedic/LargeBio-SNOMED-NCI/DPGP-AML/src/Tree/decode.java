package Tree;

import main.definedMatrix;
import math.Classification;
import math.Construction;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class decode {
    public static List<double[][]> decodeBT(ArrayList<LinkedTreeNode> linkedTree, int num) throws IOException {

        int[][] size = definedMatrix.readArrayInt(definedMatrix.cluster_Size_SNOMED_NCI, 291, 2);
        List<double[][]> binaryAndSimilarityMatrix = new ArrayList<>();
        //从底层的相似度度量方法开始计算
        for (int i = linkedTree.size() - 1; i >= 0; i--) {
            if (linkedTree.get(i).getStr().equals("NGram")) {
                linkedTree.get(i).setValue(definedMatrix.readArrayFromFile("D:\\Biomed\\LargeBio-SNOMED-NCI\\block_matrix\\NGram\\NGram_" + (num + 1), size[num][0], size[num][1]));
            }
            if (linkedTree.get(i).getStr().equals("SMOA")) {
                linkedTree.get(i).setValue(definedMatrix.readArrayFromFile("D:\\Biomed\\LargeBio-SNOMED-NCI\\block_matrix\\SMOA\\SMOA_" + (num + 1), size[num][0], size[num][1]));
            }
            if (linkedTree.get(i).getStr().equals("dictionary")) {
                linkedTree.get(i).setValue(definedMatrix.readArrayFromFile("D:\\Biomed\\LargeBio-SNOMED-NCI\\block_matrix\\Dictionary\\dictionary_" + (num + 1), size[num][0], size[num][1]));
            }

            //计算construction层
            if (linkedTree.get(i).getStr().equals("add")) {
                linkedTree.get(i).setValue(Construction.add(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }
            if (linkedTree.get(i).getStr().equals("sub")) {
                linkedTree.get(i).setValue(Construction.sub(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }
            if (linkedTree.get(i).getStr().equals("mul")) {
                linkedTree.get(i).setValue(Construction.mul(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }
            if (linkedTree.get(i).getStr().equals("div")) {
                linkedTree.get(i).setValue(Construction.div(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }
            if (linkedTree.get(i).getStr().equals("max")) {
                linkedTree.get(i).setValue(Construction.max(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }
            if (linkedTree.get(i).getStr().equals("min")) {
                linkedTree.get(i).setValue(Construction.min(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }
            if (linkedTree.get(i).getStr().equals("avg")) {
                linkedTree.get(i).setValue(Construction.avg(linkedTree.get(i).getLeft().getValue(), linkedTree.get(i).getRight().getValue()));
                linkedTree.get(i).getRight().setValue(null);
                linkedTree.get(i).getLeft().setValue(null);
            }

            //计算classification层
            if (linkedTree.get(i).getStr().equals("top1")) {
                linkedTree.get(i).setValue(Classification.top1BinaryMatrix(Classification.thresholdFiltering(linkedTree.get(i).getChild().getValue(), 0.6)));
            }
            if (linkedTree.get(i).getStr().equals("top30")) {
                linkedTree.get(i).setValue(Classification.topPercentBinaryMatrix(Classification.thresholdFiltering(linkedTree.get(i).getChild().getValue(), 0.6), 0.3));
            }
            if (linkedTree.get(i).getStr().equals("top50")) {
                linkedTree.get(i).setValue(Classification.topPercentBinaryMatrix(Classification.thresholdFiltering(linkedTree.get(i).getChild().getValue(), 0.6), 0.5));
            }
        }

        double[][] binaryMatrix = linkedTree.get(0).getValue();
        double[][] similarityMatrix = linkedTree.get(0).getChild().getValue();
        for (int i = 0; i < similarityMatrix.length; i++) {
            double min = 1;
            double max = 0;
            for (int j = 0; j < similarityMatrix[i].length; j++) {
                if (similarityMatrix[i][j] < min)
                    min = similarityMatrix[i][j];
                if (similarityMatrix[i][j] > max)
                    max = similarityMatrix[i][j];
                if (min == 0 && max == 1)
                    break;
            }
            for (int j = 0; j < similarityMatrix[i].length; j++) {
                similarityMatrix[i][j] = (similarityMatrix[i][j] - min) / (max - min);  //归一化
            }
        }

        double[][] copyBinaryMatrix = new double[binaryMatrix.length][binaryMatrix[0].length];
        for (int i = 0; i < binaryMatrix.length; i++) {
            for (int j = 0; j < binaryMatrix[0].length; j++) {
                copyBinaryMatrix[i][j] = binaryMatrix[i][j];
            }
        }
        double[][] copySimilarityMatrix = new double[similarityMatrix.length][similarityMatrix[0].length];
        for (int i = 0; i < similarityMatrix.length; i++) {
            for (int j = 0; j < similarityMatrix[0].length; j++) {
                copySimilarityMatrix[i][j] = similarityMatrix[i][j];
            }
        }
        binaryAndSimilarityMatrix.add(copyBinaryMatrix);
        binaryAndSimilarityMatrix.add(copySimilarityMatrix);
        linkedTree.get(0).setValue(null);
        linkedTree.get(0).getChild().setValue(null);
        return binaryAndSimilarityMatrix;
    }
}
