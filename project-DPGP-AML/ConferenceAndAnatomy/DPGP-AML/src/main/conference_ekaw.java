package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class conference_ekaw {
    public static void main(String[] args) throws Exception {

        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\Conference.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\ekaw.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\conference-ekaw.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();

        /*alignment.add("paper---paper");
        alignment.add("poster---poster_paper");
        alignment.add("conference_part---conference");
        alignment.add("conference_contribution---conference_trip");
        alignment.add("conference_applicant---conference");
        alignment.add("active_conference_participant---conference_participant");
        alignment.add("conference_document---conference");
        alignment.add("conference_participant---conference_participant");
        alignment.add("conference_volume---conference");
        alignment.add("review---review");
        alignment.add("track---track");
        alignment.add("conference_www---conference");
        alignment.add("conference_proceedings---conference_proceedings");
        alignment.add("chair---oc_chair");
        alignment.add("chair---pc_chair");
        alignment.add("conference_fees---conference");
        alignment.add("organization---organisation");
        alignment.add("conference---conference");
        alignment.add("invited_speaker---invited_speaker");
        alignment.add("track-workshop_chair---workshop_chair");
        alignment.add("invited_talk---invited_talk");
        alignment.add("tutorial---tutorial");
        alignment.add("workshop---workshop");
        alignment.add("person---person");
        alignment.add("conference_contributor---conference_trip");
        alignment.add("passive_conference_participant---conference_participant");
        alignment.add("reviewer---review");
        alignment.add("abstract---abstract");*/

        //词库：注意排列顺序
        List<String> words = new ArrayList<>();
        words.add("active");
        words.add("abstract");
        words.add("chair");
        words.add("conference");
        words.add("document");
        words.add("extended");
        words.add("invited");
        words.add("individual");
        words.add("oc");
        words.add("organization");
        words.add("organisation");
        words.add("pc");
        words.add("paper");
        words.add("person");
        words.add("poster");
        words.add("passive");
        words.add("participant");
        words.add("proceedings");
        words.add("presentation");
        words.add("review");
        words.add("speaker");
        words.add("talk");
        words.add("track");
        words.add("tutorial");
        words.add("volume");
        words.add("workshop");

        alignment.add("paper---demo_paper");
        alignment.add("poster---poster_paper");
        alignment.add("camera_ready_contribution---camera_ready_paper");
        alignment.add("accepted_contribution---accepted_paper");
        alignment.add("publisher---proceedings_publisher");
        alignment.add("conference_participant---conference_participant");
        alignment.add("conference_volume---conference");
        alignment.add("rejected_contribution---rejected_paper");
        alignment.add("call_for_paper---paper");
        alignment.add("review---review");
        alignment.add("track---track");
        alignment.add("conference_proceedings---conference_proceedings");
        alignment.add("organization---organisation");
        alignment.add("conference---conference_paper");
        alignment.add("regular_author---paper_author");
        alignment.add("invited_speaker---invited_speaker");
        alignment.add("submitted_contribution---submitted_paper");
        alignment.add("extended_abstract---abstract");
        alignment.add("topic---research_topic");
        alignment.add("invited_talk---invited_talk");
        alignment.add("tutorial---tutorial");
        alignment.add("workshop---workshop");
        alignment.add("person---person");
        alignment.add("abstract---abstract");
        alignment.add("track-workshop_chair---workshop_chair");
        alignment.add("conference_document---document");

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
        tempAlignment.add("conference---conference");
        tempAlignment.add("conference_www---conference");
        tempAlignment.add("conference_fees---conference");
        tempAlignment.add("conference_announcement---conference");
        tempAlignment.add("conference_www---conference_trip");
        tempAlignment.add("organization---conference");
        tempAlignment.add("call_for_paper---poster_paper");
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
                    if (max < 0.8)
                        tempAlignment.add(alignment.get(i));
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
        alignment.add("track-workshop_chair---workshop_chair");
        alignment.add("conference_document---document");

        //匹配property
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> list = property.propertyMappings(strs[0], strs[1], source2, target2, source3, target3, 0.8);
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
