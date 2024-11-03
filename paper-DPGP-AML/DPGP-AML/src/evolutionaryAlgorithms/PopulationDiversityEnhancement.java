package evolutionaryAlgorithms;

import Tree.LinkedTreeNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PopulationDiversityEnhancement {

    public static void diversityEnhancement(List<ArrayList<LinkedTreeNode>> optimaPopulation, List<ArrayList<LinkedTreeNode>> otherPopulation,
                                            int neighborhoodSize, int SPsize, List<List<String>> source, List<List<String>> target) throws IOException {
        List<ArrayList<LinkedTreeNode>> unionPopulation = new ArrayList<>();
        int[] indexArr1 = crowdnessSort1(optimaPopulation, neighborhoodSize, source, target);
        int m = indexArr1.length - 1;
        for (int i = 0; i < SPsize; i++) {
            unionPopulation.add(optimaPopulation.get(indexArr1[m]));
            m--;
        }
        int[] indexArr2 = crowdnessSort2(otherPopulation, neighborhoodSize, source, target);
        int n = 0;
        for (int i = 0; i < SPsize; i++) {
            unionPopulation.add(otherPopulation.get(indexArr2[n]));
            n++;
        }

        List<ArrayList<LinkedTreeNode>> tempPopulation1 = new ArrayList<>();
        List<ArrayList<LinkedTreeNode>> tempPopulation2 = new ArrayList<>();
        int[] indexArr3 = crowdnessSort2(unionPopulation, neighborhoodSize, source, target);
        int p = 0;
        int q = unionPopulation.size() - 1;
        for (int i = 0; i < SPsize; i++) {
            tempPopulation1.add(unionPopulation.get(indexArr3[p]));
            tempPopulation2.add(unionPopulation.get(indexArr3[q]));
            p++;
            q--;
        }
        int j = indexArr1.length - 1;
        int k = 0;
        for (int i = 0; i < SPsize; i++) {
            int index1 = indexArr1[j];
            int index2 = indexArr2[k];
            optimaPopulation.remove(index1);
            optimaPopulation.add(index1, tempPopulation1.get(i));
            j--;
            otherPopulation.remove(index2);
            otherPopulation.add(index2, tempPopulation2.get(i));
            k++;
        }
    }


    public static int[] crowdnessSort1(List<ArrayList<LinkedTreeNode>> population, int neighborhoodSize, List<List<String>> source, List<List<String>> target) throws IOException {
        double[] crowdnessArr = new double[population.size()];
        for (int i = 0; i < population.size(); i++) {
            crowdnessArr[i] = crowdness1(i, population, neighborhoodSize, source, target);
        }
        return indexSort(crowdnessArr);
    }

    public static int[] crowdnessSort2(List<ArrayList<LinkedTreeNode>> population, int neighborhoodSize, List<List<String>> source, List<List<String>> target) throws IOException {
        double[] crowdnessArr = new double[population.size()];
        for (int i = 0; i < population.size(); i++) {
            crowdnessArr[i] = crowdness2(i, population, neighborhoodSize, source, target);
        }
        return indexSort(crowdnessArr);
    }

    public static double crowdness1(int indIndex, List<ArrayList<LinkedTreeNode>> population, int neighborhoodSize, List<List<String>> source, List<List<String>> target) throws IOException {
        double[] semSimArr = new double[population.size() - 1];
        for (int i = 0; i < population.size(); i++) {
            if (i < indIndex)
                semSimArr[i] = DeterministicCrossover.semSim(population.get(indIndex), population.get(i), source, target);
            if (i == indIndex)
                continue;
            if (i > indIndex)
                semSimArr[i - 1] = DeterministicCrossover.semSim(population.get(indIndex), population.get(i), source, target);
        }
        double[] newArr = sort(semSimArr);
        int j = newArr.length - 1;
        double sum = 0;
        for (int i = 0; i < neighborhoodSize; i++) {
            sum += newArr[j];
            j--;
        }
        return sum / neighborhoodSize;
    }

    public static double crowdness2(int indIndex, List<ArrayList<LinkedTreeNode>> population, int neighborhoodSize, List<List<String>> source, List<List<String>> target) throws IOException {
        double[] semSimArr = new double[population.size() - 1];
        for (int i = 0; i < population.size(); i++) {
            if (i < indIndex)
                semSimArr[i] = DeterministicCrossover.semSim(population.get(indIndex), population.get(i), source, target);
            if (i == indIndex)
                continue;
            if (i > indIndex)
                semSimArr[i - 1] = DeterministicCrossover.semSim(population.get(indIndex), population.get(i), source, target);
        }
        double[] newArr = sort(semSimArr);
        int j = 0;
        double sum = 0;
        for (int i = 0; i < neighborhoodSize; i++) {
            sum += newArr[j];
            j++;
        }
        return sum / neighborhoodSize;
    }


    public static int[] indexSort(double[] arr) {
        double[] copyArr = new double[arr.length];
        int[] indexArr = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copyArr[i] = arr[i];
        }
        double[] newArr = sort(arr);
        for (int i = 0; i < newArr.length; i++) {
            for (int j = 0; j < copyArr.length; j++) {
                if (newArr[i] == copyArr[j]) {
                    indexArr[i] = j;
                    copyArr[j] = 99999;
                    break;
                }
            }
        }
        return indexArr;
    }

    private static double[] sort(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        return arr;
    }

}
