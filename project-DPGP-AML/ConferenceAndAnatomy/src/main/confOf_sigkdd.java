package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class confOf_sigkdd {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\资料\\Active-Interactive-conference\\conference-dataset\\confOf.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\sigkdd.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\confOf-sigkdd.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        /*alignment.add("paper---paper");
        alignment.add("conference---conference");
        alignment.add("person---person");
        alignment.add("author---author");*/

        alignment.add("paper---paper");
        alignment.add("student---registration_student");
        alignment.add("conference---conference");
        alignment.add("person---person");
        alignment.add("member_pc---registration_non-member");
        alignment.add("member_pc---program_committee_member");
        alignment.add("registration_of_participants_event---registration_student");
        alignment.add("chair_pc---program_chair");
        alignment.add("chair_pc---general_chair");
        alignment.add("short_paper---paper");
        alignment.add("administrative_event---registration_student");
        alignment.add("author---author");
        alignment.add("organization---organizator");
        alignment.add("reviewing_event---review");
        alignment.add("poster---webmaster");
        alignment.add("member---registration_non-member");
        alignment.add("member---program_committee_member");
        alignment.add("submission_event---deadline_paper_submission");
        alignment.add("reviewing_results_event---review");

        //词库：注意排列顺序
        List<String> words = new ArrayList<>();
        words.add("");
        words.add("author");
        words.add("chair");
        words.add("committee");
        words.add("conference");
        words.add("general");
        words.add("member");
        words.add("pc");
        words.add("paper");
        words.add("person");
        words.add("program");

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

        //三轮排除：通过分词策略，排除语义矛盾的实体
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list1 = exclude.divide(words, strs[0]);
            List<String> list2 = exclude.divide(words, strs[1]);
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            boolean flag4 = false;
            for (int p = 0; p < list1.size(); p++) {
                for (int q = 0; q < list2.size(); q++) {
                    if (list1.get(p).equals(list2.get(q))) {
                        flag1 = true;
                        break;
                    }
                }
            }
            if (flag1) {
                for (int p = 0; p < list1.size(); p++) {
                    if (list1.get(p).equals("pc")||list1.get(p).equals("program")) {
                        flag2 = true;
                        break;
                    }
                }
                for (int q = 0; q < list2.size(); q++) {
                    if (list2.get(q).equals("pc")||list2.get(q).equals("program")) {
                        flag3 = true;
                        break;
                    }
                }
                if (flag2 != flag3)
                    flag4 = true;
            }
            if (flag4)
                tempAlignment.add(alignment.get(i));
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

        /*for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.7);
            for (int j = 0; j < list.size(); j++) {
                propertyMappings.add(list.get(j));
            }
        }*/

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
