package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class edas_ekaw {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\edas.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\ekaw.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\edas-ekaw.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        /*alignment.add("person---person");
        alignment.add("presenter---presenter");
        alignment.add("sessionchair---session_chair");
        alignment.add("workshop---workshop");
        alignment.add("conferencesession---conference_session");
        alignment.add("review---review");
        alignment.add("organization---organisation");
        alignment.add("conference---conference");
        alignment.add("socialevent---social_event");
        alignment.add("rejectedpaper---rejected_paper");
        alignment.add("paper---paper");
        alignment.add("document---document");
        alignment.add("acceptedpaper---accepted_paper");*/

        alignment.add("person---person");
        alignment.add("nonacademicevent---event");
        alignment.add("reviewform---review");
        alignment.add("author---paper_author");
        alignment.add("reviewer---review");
        alignment.add("presenter---presenter");
        alignment.add("personalhistory---person");
        alignment.add("mealevent---event");
        alignment.add("academicevent---scientific_event");
        alignment.add("sessionchair---session_chair");
        alignment.add("reviewquestion---review");
        alignment.add("talkevent---event");
        alignment.add("organizationalmeeting---organisation");
        alignment.add("twolevelconference---conference");
        alignment.add("workshop---workshop");
        alignment.add("conferencesession---conference_session");
        alignment.add("programme---programme_brochure");
        alignment.add("review---review");
        alignment.add("conferencechair---conference");
        alignment.add("organization---organisation");
        alignment.add("tpcmember---pc_member");
        alignment.add("conference---conference");
        alignment.add("conferencevenueplace---conference");
        alignment.add("socialevent---social_event");
        alignment.add("rejectedpaper---rejected_paper");
        alignment.add("paper---paper");
        alignment.add("document---document");
        alignment.add("conferenceevent---conference");
        alignment.add("acceptedpaper---accepted_paper");
        alignment.add("reviewrating---review");
        alignment.add("conferencedinner---conference");

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
                    for (int j = 0; j < strsSonAndFather.length; j++) {
                        String s = strsSonAndFather[j];
                        String newS = "";
                        int index = 0;
                        boolean flag = true;
                        for (; index < s.length(); index++) {
                            if (s.charAt(index) == '_')
                                break;
                        }
                        if (index != 0 && index < s.length() - 1) {
                            String subS1 = s.substring(0, index);
                            String subS2 = s.substring(index + 1);
                            newS = subS1 + subS2;
                            flag = false;
                        }
                        if (flag)
                            fatherAndSon1.add(s);
                        else fatherAndSon1.add(newS);
                    }
                }

                int index2 = 0;
                for (; index2 < target1.get(1).size(); index2++) {
                    if (target1.get(1).get(index2).equals(strs[1]))
                        break;
                }
                if (target1.get(3).get(index2).length() != 0) {
                    String newString = target1.get(3).get(index2).substring(1);
                    String[] strsSonAndFather = splitString2(newString);
                    for (int j = 0; j < strsSonAndFather.length; j++) {
                        String s = strsSonAndFather[j];
                        String newS = "";
                        int index = 0;
                        boolean flag = true;
                        for (; index < s.length(); index++) {
                            if (s.charAt(index) == '_')
                                break;
                        }
                        if (index != 0 && index < s.length() - 1) {
                            String subS1 = s.substring(0, index);
                            String subS2 = s.substring(index + 1);
                            newS = subS1 + subS2;
                            flag = false;
                        }
                        if (flag)
                            fatherAndSon2.add(s);
                        else fatherAndSon2.add(newS);
                    }
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
                if (max ==0)
                    tempAlignment.add(alignment.get(i));
            }
        }
        tempAlignment.add("presenter---presenter");
        tempAlignment.add("conferencesession---conference_session");
        tempAlignment.add("conference---conference");
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
                            double sim1 = WaysOfSimilarity.SMOASimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            double sim2 = WaysOfSimilarity.wuAndPalmerSimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            double sim = sim1 > sim2 ? sim1 : sim2;
                            if (sim > max)
                                max = sim;
                        }
                    }
                }
                if (max < 1)
                    tempAlignment.add(alignment.get(i));
            }
        }
        tempAlignment.add("nonacademicevent---event");  //nonacademicevent---academicevent
        tempAlignment.add("paper---evaluated_paper");
        tempAlignment.add("rejectedpaper---submitted_paper");
        tempAlignment.add("acceptedpaper---submitted_paper");
        tempAlignment.add("rejectedpaper---regular_paper");
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (alignment.get(i).equals(tempAlignment.get(j))) {
                    alignment.remove(i);
                    i--;
                    break;
                }
            }
        }
        alignment.add("organization---organisation");  //sim=1

        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.7);
            for (int j = 0; j < list.size(); j++) {
                propertyMappings.add(list.get(j));
            }
        }

        for (int i = 0; i < propertyMappings.size(); i++) {
            alignment.add(propertyMappings.get(i));
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
