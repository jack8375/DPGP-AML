package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class cmt_sigkdd {
    public static void main(String[] args) throws Exception {
        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\cmt.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\sigkdd.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\cmt-sigkdd.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> newAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();
        /*alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("programcommitteemember---program_committee_member");
        alignment.add("conference---conference");
        alignment.add("author---author");
        alignment.add("paper---paper");
        alignment.add("programcommittee---program_committee");
        alignment.add("document---document");
        alignment.add("conferencechair---conference_hall");*/

        /*alignment.add("administrator---abstract");
        alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("conferencemember---conference");
        alignment.add("conferencemember---conference_hall");
        alignment.add("programcommitteemember---organizing_committee_member");
        alignment.add("programcommitteemember---program_committee");
        alignment.add("programcommitteemember---program_committee_member");
        alignment.add("reviewer---review");
        alignment.add("paperabstract---abstract");
        alignment.add("conference---conference");
        alignment.add("conference---conference_hall");
        alignment.add("preference---conference");
        alignment.add("author---author");
        alignment.add("paper---paper");
        alignment.add("rejection---review");
        alignment.add("authornotreviewer---author_of_paper");
        alignment.add("authornotreviewer---author_of_paper_student");
        alignment.add("meta-review---review");
        alignment.add("co-author---committee");
        alignment.add("co-author---author");
        alignment.add("associatedchair---program_chair");
        alignment.add("programcommitteechair---program_chair");
        alignment.add("programcommitteechair---program_committee");
        alignment.add("programcommitteechair---program_committee_member");
        alignment.add("programcommittee---committee");
        alignment.add("programcommittee---organizing_committee");
        alignment.add("programcommittee---program_chair");
        alignment.add("programcommittee---program_committee");
        alignment.add("programcommittee---program_committee_member");
        alignment.add("document---document");
        alignment.add("conferencechair---conference");
        alignment.add("conferencechair---conference_hall");
        alignment.add("conferencechair---general_chair");
        alignment.add("decision---deadline");*/

        alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("programcommitteemember---program_committee_member");
        alignment.add("paperabstract---abstract");
        alignment.add("conference---conference");
        alignment.add("author---author");
        alignment.add("paper---paper");
        alignment.add("authornotreviewer---author_of_paper");
        alignment.add("authornotreviewer---author_of_paper_student");
        alignment.add("meta-review---review");
        alignment.add("associatedchair---program_chair");
        alignment.add("programcommitteechair---program_chair");
        alignment.add("programcommittee---program_committee");
        alignment.add("document---document");
        alignment.add("conferencechair---general_chair");


        //词库：注意排列顺序
        List<String> words = new ArrayList<>();
        words.add("author");
        words.add("co");
        words.add("chair");
        words.add("conference");
        words.add("committee");
        words.add("document");
        words.add("member");
        words.add("person");
        words.add("program");
        words.add("review");

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
                        if(flag1 && flag2)
                            newAlignment.add(alignment.get(j));
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
                        if(flag1 && flag2)
                            newAlignment.add(alignment.get(j));
                    }
                }
            }
        }
        delete(tempAlignment);
        for (int i = 0; i < tempAlignment.size(); i++) {
            for (int j = 0; j < newAlignment.size(); j++) {
                if(tempAlignment.get(i).equals(newAlignment.get(j))){
                    tempAlignment.remove(i);
                    i--;
                    break;
                }
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
                fatherAndSon2.add(strs[1]);

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
                if (max < 0.7 && max != 0)
                    tempAlignment.add(alignment.get(i));
            }
        }
        tempAlignment.add("programcommitteechair---program_committee_member");
        tempAlignment.add("programcommittee---committee");
        tempAlignment.add("authornotreviewer---author_of_paper_student");
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (alignment.get(i).equals(tempAlignment.get(j))) {
                    alignment.remove(i);
                    i--;
                    break;
                }
            }
        }


        //匹配property
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.83);
            for (int j = 0; j < list.size(); j++) {
                propertyMappings.add(list.get(j));
            }
        }
        delete(propertyMappings);

        for (int i = 0; i < propertyMappings.size(); i++) {
            alignment.add(propertyMappings.get(i));
        }

        for (int i = 0; i < tempAlignment.size(); i++) {
            System.out.println(tempAlignment.get(i));
        }
        System.out.println("--------------------");

        for (int i = 0; i < alignment.size(); i++) {
            System.out.println(alignment.get(i));
        }
        System.out.println("--------------------");

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

    //删除重复元素
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
}
