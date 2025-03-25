package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class iasted_sigkdd {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\iasted.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\sigkdd.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\iasted-sigkdd.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> newAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        alignment.add("conference_days---conference");
        alignment.add("hotel_presenter---hotel");
        alignment.add("speaker_lecture---speaker");
        alignment.add("speaker---speaker");
        alignment.add("registration_form---registration_fee");
        alignment.add("nonauthor_registration_fee---registration_fee");
        alignment.add("conference_airport---conference");
        alignment.add("conference_hotel---conference");
        alignment.add("registration---registration_fee");
        alignment.add("author---author");
        alignment.add("deadline_hotel_reservation---deadline");
        alignment.add("submissions_deadline---deadline_paper_submission");
        alignment.add("conference_hall---conference_hall");
        alignment.add("student_registration_fee---registration_student");
        alignment.add("conference_city---conference");
        alignment.add("review---review");
        alignment.add("memeber_registration_fee---registration_fee");
        alignment.add("sponzorship---sponzor");
        alignment.add("conference_building---conference");
        alignment.add("sponsor---sponzor");
        alignment.add("conference_activity---conference");
        alignment.add("person---person");
        alignment.add("deadline_for_notification_of_acceptance---deadline_author_notification");
        alignment.add("registration_fee---registration_fee");
        alignment.add("place---place");
        alignment.add("hotel_room---hotel");
        alignment.add("listener---listener");
        alignment.add("reviewer---review");
        alignment.add("document---document");
        alignment.add("deadline---deadline");
        alignment.add("non_speaker---speaker");
        alignment.add("nonmember_registration_fee---registration_non-member");
        alignment.add("conference_restaurant---conference");
        alignment.add("conference_state---conference");
        alignment.add("payment_document---document");
        alignment.add("fee---fee");
        alignment.add("conference_hiker---conference");
        alignment.add("one_conference_day---conference");
        alignment.add("main_office---main_office");
        alignment.add("hotel_fee---hotel");

        //一轮排除：通过局部一致性原则(LCP)，排除子父类关系矛盾的匹配对
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) == 1) {
                List<String> allFather1;
                List<String> allFather2;
                List<String> allSon1;
                List<String> allSon2;
                int index1 = 0;
                for (; index1 < source1.get(1).size(); index1++) {
                    if (source1.get(1).get(index1).equals(strs[0]))
                        break;
                }
                allFather1 = exclude.getAllFather(source1.get(1).get(index1), source1);
                allSon1 = exclude.getAllSon(source1.get(1).get(index1), source1);

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[1]))
                        break;
                }
                allFather2 = exclude.getAllFather(target1.get(1).get(index2), target1);
                allSon2 = exclude.getAllSon(target1.get(1).get(index2), target1);

                for (int j = 0; j < alignment.size(); j++) {
                    String[] align = splitString1(alignment.get(j));
                    if (WaysOfSimilarity.SMOASimilarity(align[0], align[1]) != 1) {
                        boolean flag = false;
                        boolean flag1 = false;
                        boolean flag2 = false;
                        for (int p = 0; p < allFather1.size(); p++) {
                            if (allFather1.get(p).equals(align[0]))
                                flag1 = true;
                        }
                        for (int q = 0; q < allFather2.size(); q++) {
                            if (allFather2.get(q).equals(align[1]))
                                flag2 = true;
                        }
                        if (flag1 != flag2)
                            flag = true;
                        if (flag)
                            tempAlignment.add(alignment.get(j));
                    }
                }

                for (int j = 0; j < alignment.size(); j++) {
                    String[] align = splitString1(alignment.get(j));
                    if (WaysOfSimilarity.SMOASimilarity(align[0], align[1]) != 1) {
                        boolean flag = false;
                        boolean flag1 = false;
                        boolean flag2 = false;
                        for (int p = 0; p < allSon1.size(); p++) {
                            if (allSon1.get(p).equals(align[0]))
                                flag1 = true;
                        }
                        for (int q = 0; q < allSon2.size(); q++) {
                            if (allSon2.get(q).equals(align[1]))
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
        alignment.add("sponsor---sponzor");

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
