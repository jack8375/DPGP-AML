package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;

import java.util.ArrayList;
import java.util.List;

public class cmt_edas {
    public static void main(String[] args) throws Exception {

        String s1 = "D:\\资料\\Active-Interactive-conference\\conference-dataset\\cmt.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\ontology-matching\\DPGP-AML\\conference-dataset\\edas.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\ontology-matching\\DPGP-AML\\reference-alignment\\cmt-edas.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        //词库：注意排列顺序
        List<String> words = new ArrayList<>();
        words.add("by");
        words.add("conference");
        words.add("date");
        words.add("end");
        words.add("has");
        words.add("is");
        words.add("member");
        words.add("of");
        words.add("read");
        words.add("reviewer");
        words.add("reviewed");

        List<String> alignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();
        alignment.add("paper---paper");
        alignment.add("conferencechair---conferencechair");
        alignment.add("review---review");
        alignment.add("conference---conference");
        alignment.add("person---person");
        alignment.add("document---document");
        alignment.add("author---author");
        alignment.add("reviewer---reviewer");

        List<String> tempAlignment = new ArrayList<>();
        //匹配property(所有property)
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.72);
            for (int j = 0; j < list.size(); j++) {
                propertyMappings.add(list.get(j));
            }
        }

        //一轮排除：通过分词策略，排除语义矛盾的property
        for (int i = 0; i < propertyMappings.size(); i++) {
            String[] strs = splitString1(propertyMappings.get(i));
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
                    if (list1.get(p).equals("end")) {
                        flag2 = true;
                        break;
                    }
                }
                for (int q = 0; q < list2.size(); q++) {
                    if (list2.get(q).equals("end")) {
                        flag3 = true;
                        break;
                    }
                }
                if (flag2 != flag3)
                    flag4 = true;
            }
            if (flag4)
                tempAlignment.add(propertyMappings.get(i));
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

        //二轮排除：利用domain和range排除高相似度但不具备匹配关系的property
        for (int i = 0; i < propertyMappings.size(); i++) {
            String[] strs = splitString1(propertyMappings.get(i));
            int index1 = 0;
            for (; index1 < source2.get(1).size(); index1++) {
                if (source2.get(1).get(index1).equals(strs[0]))
                    break;
            }
            String[] strs1 = splitString1(source2.get(3).get(index1));
            String domain1 = strs1[0];
            String range1 = strs1[1];

            int index2 = 0;
            for (; index2 < target2.get(1).size(); index2++) {
                if (target2.get(1).get(index2).equals(strs[1]))
                    break;
            }
            String[] strs2 = splitString1(target2.get(3).get(index2));
            String domain2 = strs2[0];
            String range2 = strs2[1];

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
