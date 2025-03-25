package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class edas_iasted {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\edas.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\iasted.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\edas-iasted.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();

        alignment.add("coffeebreak---coffee_break");
        alignment.add("paperpresentation---presentation");
        alignment.add("author---author");
        alignment.add("reviewer---reviewer");
        alignment.add("reception---coctail_reception");
        alignment.add("sessionchair---session_chair");
        alignment.add("callformanuscripts---final_manuscript");
        alignment.add("programme---submission");
        alignment.add("review---review");
        alignment.add("place---place");
        alignment.add("welcometalk---welcome_address");
        alignment.add("conference---conference_days");
        alignment.add("conference---conference_city");
        alignment.add("conferencevenueplace---conference_hall");
        alignment.add("socialevent---social_program");
        alignment.add("paper---submission");
        alignment.add("document---document");
        alignment.add("freetimebreak---time");
        alignment.add("sponsorship---sponzorship");

        //一轮排除：通过子父类概念是否相似，排除一模一样但不具备匹配关系的实体
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
                if (source1.get(3).get(index1).length() != 0) {
                    String newString = source1.get(3).get(index1).substring(1);
                    String[] strsSonAndFather = splitString2(newString);
                    for (int j = 0; j < strsSonAndFather.length; j++)
                        fatherAndSon1.add(strsSonAndFather[j]);
                }

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[1]))
                        break;
                }
                if (target1.get(3).get(index2).length() != 0) {
                    String newString = target1.get(3).get(index2).substring(1);
                    String[] strsSonAndFather = splitString2(newString);
                    for (int j = 0; j < strsSonAndFather.length; j++)
                        fatherAndSon2.add(strsSonAndFather[j]);
                }

                double max = 0;
                if (fatherAndSon1.size() != 0 && fatherAndSon2.size() != 0) {
                    for (int p = 0; p < fatherAndSon1.size(); p++) {
                        for (int q = 0; q < fatherAndSon2.size(); q++) {
                            double sim = WaysOfSimilarity.SMOASimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            if (sim > max)
                                max = sim;
                        }
                    }
                }
                if (max < 0.3 && max != 0)
                    tempAlignment.add(alignment.get(i));
            }
        }
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (alignment.get(i).equals(tempAlignment.get(j))) {
                    alignment.remove(i);
                    i--;
                    break;
                }
            }
        }

        //一轮排除：通过局部一致性原则(LCP)，排除子父类关系矛盾的匹配对
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) == 1) {
                List<String> fatherAndSon1;
                List<String> fatherAndSon2;
                int index1 = 0;
                for (; index1 < source1.get(1).size(); index1++) {
                    if (source1.get(1).get(index1).equals(strs[0]))
                        break;
                }
                fatherAndSon1 = exclude.getAllFatherAndSon(source1.get(1).get(index1), source1);
                for (int j = 0; j < fatherAndSon1.size(); j++) {
                    if (fatherAndSon1.get(j).equals(strs[0]))
                        fatherAndSon1.remove(j);
                }

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[1]))
                        break;
                }
                fatherAndSon2 = exclude.getAllFatherAndSon(target1.get(1).get(index2), target1);

                for (int j = 0; j < alignment.size(); j++) {
                    String[] align = splitString1(alignment.get(j));
                    if (WaysOfSimilarity.SMOASimilarity(align[0], align[1]) != 1) {
                        boolean flag = false;
                        boolean flag1 = false;
                        boolean flag2 = false;
                        for (int p = 0; p < fatherAndSon1.size(); p++) {
                            if (fatherAndSon1.get(p).equals(align[0]))
                                flag1 = true;
                        }
                        for (int q = 0; q < fatherAndSon2.size(); q++) {
                            if (fatherAndSon2.get(q).equals(align[1]))
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
        }

        //二轮排除：通过子父类概念是否相似，排除高相似度但不具备匹配关系的实体
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) != 1) {
                List<String> fatherAndSon1 = new ArrayList<>();
                List<String> fatherAndSon2 = new ArrayList<>();
                int index1 = 0;
                for (; index1 < source1.get(1).size(); index1++) {
                    if (source1.get(1).get(index1).equals(strs[0]))
                        break;
                }
                fatherAndSon1 = exclude.getAllFatherAndSon(source1.get(1).get(index1), source1);
                for (int j = 0; j < fatherAndSon1.size(); j++) {
                    if (fatherAndSon1.get(j).equals(strs[0]))
                        fatherAndSon1.remove(j);
                }

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[1]))
                        break;
                }
                fatherAndSon2 = exclude.getAllFatherAndSon(target1.get(1).get(index2), target1);
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
                if (max < 0.79)
                    tempAlignment.add(alignment.get(i));
            }
        }

        tempAlignment.add("conference---conference_days");
        tempAlignment.add("programme---submission");
        tempAlignment.add("");


        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (alignment.get(i).equals(tempAlignment.get(j))) {
                    alignment.remove(i);
                    i--;
                    break;
                }
            }
        }
        alignment.add("sponsorship---sponzorship");

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

    private static String[] splitString2(String s) {
        return s.split(" ");
    }
}
