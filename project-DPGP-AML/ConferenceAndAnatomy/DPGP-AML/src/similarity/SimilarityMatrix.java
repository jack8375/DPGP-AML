package similarity;

import java.util.List;

public class SimilarityMatrix {

    public static double[][] NGramSimilarityMatrix(List<String> list1, List<String> list2) {

        double[][] matrix = new double[list1.size()][list2.size()];

        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {

                matrix[i][j] = WaysOfSimilarity.NGramSimilarity(list1.get(i), list2.get(j), 3);
            }

        }
        return matrix;
    }

    public static double[][] SMOASimilarityMatrix(List<String> list1, List<String> list2) {

        double[][] matrix = new double[list1.size()][list2.size()];

        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {

                matrix[i][j] = WaysOfSimilarity.SMOASimilarity(list1.get(i), list2.get(j));
            }//内for

        }//外for
        return matrix;
    }//main

    public static double[][] wuAndPalmerSimilarityMatrix(List<String> list1, List<String> list2) {

        double[][] matrix = new double[list1.size()][list2.size()];

        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {

                matrix[i][j] = WaysOfSimilarity.wuAndPalmerSimilarity(list1.get(i), list2.get(j));
            }//内for

        }//外for
        return matrix;
    }//main

    public static double[][] sonGreedySimilarityMatrix(List<String> list1, List<String> list2) {

        double[][] matrix = new double[list1.size()][list2.size()];

        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list2.size(); j++) {

                matrix[i][j] = WaysOfSimilarity.sonGreedySimilarity(list1.get(i), list2.get(j));
            }

        }
        return matrix;
    }

}
