package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class anatomy {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\anatomy\\DPGP-AML\\anatomy-dataset\\human.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\anatomy\\DPGP-AML\\anatomy-dataset\\mouse.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\anatomy\\DPGP-AML\\anatomy-dataset\\reference.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        /*for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            int m = 0;
            for (; m < source1.get(1).size(); m++) {
                if (source1.get(1).get(m).equals(strs[1]))
                    break;
            }
            int n = 0;
            for (; n < target1.get(1).size(); n++) {
                if (target1.get(1).get(n).equals(strs[0]))
                    break;
            }

            if (WaysOfSimilarity.SMOASimilarity(source1.get(4).get(m), target1.get(4).get(n)) == 1) {
                List<String> fatherAndSon1;
                List<String> fatherAndSon2;
                int index1 = 0;
                for (; index1 < source1.get(1).size(); index1++) {
                    if (source1.get(1).get(index1).equals(strs[1]))
                        break;
                }
                fatherAndSon1 = exclude.getAllFatherAndSon(source1.get(1).get(index1), source1);

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[0]))
                        break;
                }
                fatherAndSon2 = exclude.getAllFatherAndSon(target1.get(1).get(index2), target1);

                for (int j = 0; j < alignment.size(); j++) {
                    String[] align = splitString1(alignment.get(j));
                    int u = 0;
                    for (; u < source1.get(1).size(); u++) {
                        if (source1.get(1).get(u).equals(strs[1]))
                            break;
                    }
                    int v = 0;
                    for (; v < target1.get(1).size(); v++) {
                        if (target1.get(1).get(v).equals(strs[0]))
                            break;
                    }
                    if (WaysOfSimilarity.SMOASimilarity(source1.get(4).get(u), target1.get(4).get(v)) != 1) {
                        boolean flag = false;
                        boolean flag1 = false;
                        boolean flag2 = false;
                        for (int p = 0; p < fatherAndSon1.size(); p++) {
                            if (fatherAndSon1.get(p).equals(align[1]))
                                flag1 = true;
                        }
                        for (int q = 0; q < fatherAndSon2.size(); q++) {
                            if (fatherAndSon2.get(q).equals(align[0]))
                                flag2 = true;
                        }
                        if (flag1 != flag2)
                            flag = true;
                        if (flag)
                            tempAlignment.add(alignment.get(j));
                    }
                }
            }
        }
        delete(tempAlignment);
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (alignment.get(i).equals(tempAlignment.get(j))) {
                    alignment.remove(i);
                    i--;
                    break;
                }
            }
        }*/

        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            int m = 0;
            for (; m < source1.get(1).size(); m++) {
                if (source1.get(1).get(m).equals(strs[1]))
                    break;
            }
            int n = 0;
            for (; n < target1.get(1).size(); n++) {
                if (target1.get(1).get(n).equals(strs[0]))
                    break;
            }

            if (WaysOfSimilarity.SMOASimilarity(source1.get(4).get(m), target1.get(4).get(n)) > 0.5) {
                tempAlignment.add(alignment.get(i));
                }
            }


            double num = 0;
            for (int p = 0; p < reference.size(); p++) {
                for (int q = 0; q < tempAlignment.size(); q++) {
                    if (reference.get(p).equals(tempAlignment.get(q))){
                        num++;
                        break;
                    }
                }
            }
            double recall = num / reference.size();
            double precision = num / tempAlignment.size();
            double f_measure;
            if (recall == 0 || precision == 0)
                f_measure = 0;
            else f_measure = (2 * recall * precision) / (recall + precision);
            System.out.println("查全率: " + recall);
            System.out.println("查准率: " + precision);
            System.out.println("f-measure: " + f_measure);
            System.out.println("-----------------------");
        }


    private static void delete(List<String> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).equals(list.get(j))) {
                    list.remove(j);
                    j--;
                }
            }
        }
    }

    private static String[] splitString1(String s) {
        return s.split("---");
    }
}
