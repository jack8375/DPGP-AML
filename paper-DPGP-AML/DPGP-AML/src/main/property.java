package main;

import similarity.SimilarityMatrix;

import java.util.ArrayList;
import java.util.List;

public class property {
    public static List<String> propertyMappings(String sourceClass, String targetClass, List<List<String>> sourceObject, List<List<String>> targetObject,
                                                List<List<String>> sourceData, List<List<String>> targetData, double thre) {
        List<String> propertyMappings = new ArrayList<>();
        List<String> sourcePropertySet = new ArrayList<>();
        List<String> targetPropertySet = new ArrayList<>();

        for (int i = 0; i < sourceObject.get(3).size(); i++) {
            String[] domainAndRangeOfSourceObject = splitString(sourceObject.get(3).get(i));
            if (sourceClass.equals(domainAndRangeOfSourceObject[0]) || sourceClass.equals(domainAndRangeOfSourceObject[1]))
                sourcePropertySet.add(sourceObject.get(1).get(i));
        }
        for (int i = 0; i < targetObject.get(3).size(); i++) {
            String[] domainAndRangeOfTargetObject = splitString(targetObject.get(3).get(i));
            if (targetClass.equals(domainAndRangeOfTargetObject[0]) || targetClass.equals(domainAndRangeOfTargetObject[1]))
                targetPropertySet.add(targetObject.get(1).get(i));
        }
        for (int i = 0; i < sourceData.get(3).size(); i++) {
            String[] domainAndRangeOfSourceData = splitString(sourceData.get(3).get(i));
            if (sourceClass.equals(domainAndRangeOfSourceData[0]) || sourceClass.equals(domainAndRangeOfSourceData[1]))
                sourcePropertySet.add(sourceData.get(1).get(i));
        }
        for (int i = 0; i < targetData.get(3).size(); i++) {
            String[] domainAndRangeOfTargetData = splitString(targetData.get(3).get(i));
            if (targetClass.equals(domainAndRangeOfTargetData[0]) || targetClass.equals(domainAndRangeOfTargetData[1]))
                targetPropertySet.add(targetData.get(1).get(i));
        }
        double[][] SFmatrix = SimilarityMatrix.SMOASimilarityMatrix(sourcePropertySet, targetPropertySet);
        for (int i = 0; i < SFmatrix.length; i++) {
            for (int j = 0; j < SFmatrix[i].length; j++) {
                if (SFmatrix[i][j] > thre) {
                    boolean flag = true;
                    for (int p = 0; p < SFmatrix[i].length; p++) {
                        if (SFmatrix[i][p] > SFmatrix[i][j]) {
                            flag = false;
                            break;
                        }
                    }
                    for (int q = 0; q < SFmatrix.length; q++) {
                        if (SFmatrix[q][j] > SFmatrix[i][j]) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        propertyMappings.add(sourcePropertySet.get(i) + "---" + targetPropertySet.get(j));
                        for (int p = 0; p < SFmatrix[i].length; p++)
                            SFmatrix[i][p] = 0;
                        for (int q = 0; q < SFmatrix.length; q++)
                            SFmatrix[q][j] = 0;
                    }
                } else continue;
            }
        }
        return propertyMappings;
    }

    public static void remove(String[] strs) {
        for (int i = 0; i < strs.length; i++) {
            for (int j = 0; j < strs[i].length(); j++) {
                if (strs[i].charAt(j) == '-') {
                    String newStr1 = strs[i].substring(0, j);
                    String newStr2 = strs[i].substring(j + 1);
                    strs[i] = newStr1 + newStr2;
                }
            }
        }
    }

    private static String[] splitString(String s) {
        return s.split("---");
    }
}
