package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class confOf_edas {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\资料\\Active-Interactive-conference\\conference-dataset\\confOf.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\edas.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\confOf-edas.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        /*alignment.add("paper---paper");
        alignment.add("conference---conference");
        alignment.add("reception---reception");
        alignment.add("person---person");
        alignment.add("member_pc---tpcmember");
        alignment.add("short_paper---paper");
        alignment.add("country---country");
        alignment.add("author---author");
        alignment.add("organization---organization");
        alignment.add("reviewing_event---review");
        alignment.add("workshop---workshop");
        alignment.add("event---mealevent");
        alignment.add("event---talkevent");
        alignment.add("topic---topic");
        alignment.add("member---tpcmember");
        alignment.add("social_event---socialevent");
        alignment.add("reviewing_results_event---review");*/

        /*alignment.add("paper---paper");
        alignment.add("company---organization");
        alignment.add("scholar---person");
        alignment.add("conference---conference");
        alignment.add("reception---reception");
        alignment.add("person---person");
        alignment.add("member_pc---tpcmember");
        alignment.add("tutorial---conference");
        alignment.add("short_paper---paper");
        alignment.add("country---country");
        alignment.add("banquet---reception");
        alignment.add("trip---excursion");
        alignment.add("author---author");
        alignment.add("organization---organization");
        alignment.add("reviewing_event---review");
        alignment.add("workshop---workshop");
        alignment.add("participant---attendee");
        alignment.add("regular---person");
        alignment.add("volunteer---person");
        alignment.add("event---mealevent");
        alignment.add("event---talkevent");
        alignment.add("contribution---place");
        alignment.add("topic---topic");
        alignment.add("member---tpcmember");
        alignment.add("social_event---socialevent");
        alignment.add("reviewing_results_event---review");*/

        /*alignment.add("paper---paper");
        alignment.add("company---organization");
        alignment.add("scholar---person");
        alignment.add("student---person");
        alignment.add("conference---conference");
        alignment.add("reception---reception");
        alignment.add("reception---socialevent");
        alignment.add("person---person");
        alignment.add("administrator---person");
        alignment.add("member_pc---person");
        alignment.add("member_pc---tpcmember");
        alignment.add("tutorial---conference");
        alignment.add("short_paper---paper");
        alignment.add("short_paper---document");
        alignment.add("country---country");
        alignment.add("banquet---reception");
        alignment.add("trip---excursion");
        alignment.add("administrative_event---review");
        alignment.add("administrative_event---document");
        alignment.add("author---author");
        alignment.add("organization---organization");
        alignment.add("reviewing_event---review");
        alignment.add("reviewing_event---document");
        alignment.add("workshop---academicevent");
        alignment.add("workshop---workshop");
        alignment.add("participant---attendee");
        alignment.add("regular---person");
        alignment.add("poster---person");
        alignment.add("volunteer---person");
        alignment.add("event---nonacademicevent");
        alignment.add("event---socialevent");
        alignment.add("contribution---paperpresentation");
        alignment.add("contribution---academicevent");
        alignment.add("topic---topic");
        alignment.add("member---tpcmember");
        alignment.add("social_event---nonacademicevent");
        alignment.add("social_event---socialevent");
        alignment.add("working_event---academicevent");
        alignment.add("working_event---workshop");
        alignment.add("working_event---conference");
        alignment.add("city---country");
        alignment.add("reviewing_results_event---review");
        alignment.add("reviewing_results_event---document");*/

        alignment.add("paper---paper");
        alignment.add("conference---conference");
        alignment.add("reception---reception");
        alignment.add("person---person");
        alignment.add("country---country");
        alignment.add("author---author");
        alignment.add("organization---organization");
        alignment.add("workshop---workshop");
        alignment.add("topic---topic");
        alignment.add("social_event---socialevent");

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
                fatherAndSon2.add(strs[1]);

                double max = 0;
                if (fatherAndSon1.size() != 0) {
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
                if (max < 0.53 && max != 0)
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

        //三轮排除：通过子父类概念是否相似，排除高相似度但不具备匹配关系的实体
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

        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.8);
            for (int j = 0; j < list.size(); j++) {
                propertyMappings.add(list.get(j));
            }
        }

        for (int i = 0; i < propertyMappings.size(); i++) {
            String[] strs = splitString1(propertyMappings.get(i));
            int index1 = 0;
            boolean flag1 = true;
            for (; index1 < source2.get(1).size(); index1++) {
                if (source2.get(1).get(index1).equals(strs[0]))
                    break;
            }
            if (index1 >= source2.get(1).size()) {
                index1 = 0;
                flag1 = false;
                for (; index1 < source3.get(1).size(); index1++) {
                    if (source3.get(1).get(index1).equals(strs[0]))
                        break;
                }
            }
            String[] strs1;
            String domain1 = "";
            String range1 = "";
            if (flag1) {
                strs1 = splitString1(source2.get(3).get(index1));
                domain1 = strs1[0];
                range1 = strs1[1];
            } else {
                strs1 = splitString1(source3.get(3).get(index1));
                domain1 = strs1[0];
                range1 = strs1[1];
            }

            int index2 = 0;
            boolean flag2 = true;
            for (; index2 < target2.get(1).size(); index2++) {
                if (target2.get(1).get(index2).equals(strs[1]))
                    break;
            }
            if (index2 >= target2.get(1).size()) {
                index2 = 0;
                flag2 = false;
                for (; index2 < target3.get(1).size(); index2++) {
                    if (target3.get(1).get(index2).equals(strs[1]))
                        break;
                }
            }
            String[] strs2;
            String domain2 = "";
            String range2 = "";
            if (flag2) {
                strs2 = splitString1(target2.get(3).get(index2));
                domain2 = strs2[0];
                range2 = strs2[1];
            } else {
                strs2 = splitString1(target3.get(3).get(index2));
                domain2 = strs2[0];
                range2 = strs2[1];
            }

            if (domain1.equals("null") || range1.equals("null")) {
                if (domain2.equals("null") || range2.equals("null")) {
                } else tempAlignment.add(propertyMappings.get(i));
            }
            if (domain2.equals("null") || range2.equals("null")) {
                if (domain1.equals("null") || range1.equals("null")) {
                } else tempAlignment.add(propertyMappings.get(i));
            }
        }
        for (int i = 0; i < propertyMappings.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (propertyMappings.get(i).equals(tempAlignment.get(j))) {
                    propertyMappings.remove(i);
                    i--;
                    break;
                }
            }
        }

        for (int i = 0; i < tempAlignment.size(); i++) {
            System.out.println(tempAlignment.get(i));
        }
        System.out.println("------------------");

        for (int i = 0; i < propertyMappings.size(); i++) {
            alignment.add(propertyMappings.get(i));
        }

        for (int i = 0; i < alignment.size(); i++) {
            System.out.println(alignment.get(i));
        }

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
