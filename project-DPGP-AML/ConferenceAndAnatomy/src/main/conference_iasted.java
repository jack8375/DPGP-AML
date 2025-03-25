package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class conference_iasted {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\Conference.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\iasted.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\conference-iasted.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> newAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        /*alignment.add("conference_document---document");
        alignment.add("review---review");
        alignment.add("conference---conference_days");
        alignment.add("conference---conference_hall");
        alignment.add("conference---conference_city");
        alignment.add("presentation---presentation");
        alignment.add("tutorial---tutorial");
        alignment.add("person---person");
        alignment.add("reviewer---reviewer");*/

        alignment.add("conference_part---conference_days");
        alignment.add("conference_part---conference_airport");
        alignment.add("conference_part---conference_hotel");
        alignment.add("conference_part---conference_hall");
        alignment.add("conference_part---conference_city");
        alignment.add("conference_part---conference_building");
        alignment.add("conference_part---conference_activity");
        alignment.add("conference_part---conference_state");
        alignment.add("conference_part---conference_hiker");
        alignment.add("conference_contribution---conference_city");
        alignment.add("conference_applicant---conference_days");
        alignment.add("conference_applicant---conference_hall");
        alignment.add("conference_applicant---conference_city");
        alignment.add("conference_applicant---conference_restaurant");
        alignment.add("active_conference_participant---activity_before_conference");
        alignment.add("conference_document---conference_days");
        alignment.add("conference_document---conference_hall");
        alignment.add("conference_document---conference_city");
        alignment.add("conference_document---document");
        alignment.add("conference_participant---conference_restaurant");
        alignment.add("conference_volume---conference_days");
        alignment.add("conference_volume---conference_hotel");
        alignment.add("conference_volume---conference_hall");
        alignment.add("conference_volume---conference_city");
        alignment.add("conference_volume---conference_state");
        alignment.add("conference_volume---conference_hiker");
        alignment.add("review---review");
        alignment.add("review---reviewer");
        alignment.add("conference_www---conference_days");
        alignment.add("conference_www---conference_airport");
        alignment.add("conference_www---conference_hotel");
        alignment.add("conference_www---conference_hall");
        alignment.add("conference_www---conference_city");
        alignment.add("conference_www---conference_building");
        alignment.add("conference_www---conference_activity");
        alignment.add("conference_www---conference_restaurant");
        alignment.add("conference_www---conference_state");
        alignment.add("conference_www---conference_hiker");
        alignment.add("conference_proceedings---conference_building");
        alignment.add("conference_fees---conference_days");
        alignment.add("conference_fees---conference_airport");
        alignment.add("conference_fees---conference_hotel");
        alignment.add("conference_fees---conference_hall");
        alignment.add("conference_fees---conference_city");
        alignment.add("conference_fees---conference_building");
        alignment.add("conference_fees---conference_activity");
        alignment.add("conference_fees---conference_state");
        alignment.add("conference_fees---conference_hiker");
        alignment.add("conference---conference_days");
        alignment.add("conference---conference_airport");
        alignment.add("conference---conference_hotel");
        alignment.add("conference---conference_hall");
        alignment.add("conference---conference_city");
        alignment.add("conference---activity_after_conference");
        alignment.add("conference---conference_building");
        alignment.add("conference---conference_activity");
        alignment.add("conference---conference_restaurant");
        alignment.add("conference---conference_state");
        alignment.add("conference---conference_hiker");
        alignment.add("conference---one_conference_day");
        alignment.add("regular_author---author");
        alignment.add("invited_speaker---speaker");
        alignment.add("review_preference---review");
        alignment.add("presentation---video_presentation");
        alignment.add("presentation---powerpoint_presentation");
        alignment.add("presentation---presentation");
        alignment.add("tutorial---tutorial_speaker");
        alignment.add("tutorial---tutorial");
        alignment.add("reviewed_contribution---review");
        alignment.add("reviewed_contribution---reviewer");
        alignment.add("review_expertise---review");
        alignment.add("review_expertise---reviewer");
        alignment.add("person---person");
        alignment.add("conference_contributor---conference_city");
        alignment.add("reviewer---review");
        alignment.add("reviewer---reviewer");

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
                if (max < 0.8 && max != 0)
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

        //二轮排除：通过局部一致性原则(LCP)，排除子父类关系矛盾的匹配对
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
                if (max < 1)
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
