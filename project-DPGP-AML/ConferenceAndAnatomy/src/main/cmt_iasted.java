package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class cmt_iasted {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\资料\\Active-Interactive-conference\\conference-dataset\\cmt.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> sourceObject = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> sourceData = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\ontology-matching\\DPGP-AML\\conference-dataset\\iasted.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> targetObject = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> targetData = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\ontology-matching\\DPGP-AML\\reference-alignment\\cmt-iasted.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> newAlignment = new ArrayList<>();

        alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("reviewer---reviewer");
        alignment.add("author---author");
        alignment.add("document---document");

        //一轮排除：通过父概念是否相似，排除一模一样但不具备匹配关系的实体
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) == 1.0) {
                List<String> fatherAndSon1 = new ArrayList<>();
                List<String> fatherAndSon2 = new ArrayList<>();
                int index1 = 0;
                for (; index1 < source1.get(1).size(); index1++) {
                    if (source1.get(1).get(index1).equals(strs[0]))
                        break;
                }
                fatherAndSon1 = exclude.getAllFather(source1.get(1).get(index1), source1);
                for (int j = 0; j < fatherAndSon1.size(); j++) {
                    if (fatherAndSon1.get(j).equals(strs[0]))
                        fatherAndSon1.remove(j);
                }

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[1]))
                        break;
                }
                fatherAndSon2 = exclude.getAllFather(target1.get(1).get(index2), target1);
                for (int j = 0; j < fatherAndSon2.size(); j++) {
                    if (fatherAndSon2.get(j).equals(strs[1]))
                        fatherAndSon2.remove(j);
                }

                double max = 0;
                if (fatherAndSon1.size() != 0 && fatherAndSon2.size() != 0) {
                    for (int p = 0; p < fatherAndSon1.size(); p++) {
                        for (int q = 0; q < fatherAndSon2.size(); q++) {
                            double sim1 = WaysOfSimilarity.SMOASimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            double sim2 = WaysOfSimilarity.wuAndPalmerSimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            double sim = sim1 > sim2 ? sim1 : sim2;
                            if (sim > max)
                                max = sim;
                        }
                    }
                }
                if (max < 0.7)
                    tempAlignment.add(alignment.get(i));
            }
        }

        for (int i = 0; i < tempAlignment.size(); i++) {
            System.out.println(tempAlignment.get(i));
        }
        System.out.println("------------------");

        for (int i = 0; i < alignment.size(); i++) {
            System.out.println(alignment.get(i));
        }
        System.out.println("------------------");

        double num = 0;
        for (int p = 0; p < reference.size(); p++) {
            for (int q = 0; q < alignment.size(); q++) {
                if (reference.get(p).equals(alignment.get(q)))
                    num++;
            }
        }
        double recall = num / reference.size();
        double precision = num / alignment.size();
        double f_measure;
        if (recall == 0 || precision == 0)
            f_measure = 0;
        else f_measure = (2 * recall * precision) / (recall + precision);
        System.out.println("查全率: " + recall);
        System.out.println("查准率: " + precision);
        System.out.println("f-measure: " + f_measure);
        System.out.println("-----------------------");

    }

    private static String[] splitString1(String s) {
        return s.split("---");
    }

    private static String[] splitString2(String s) {
        return s.split(" ");
    }
}
