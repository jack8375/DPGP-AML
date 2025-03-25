package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class cmt_ekaw {
    public static void main(String[] args) throws Exception {

        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\cmt.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\ekaw.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\cmt-ekaw.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        List<String> alignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> newAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();
        /*alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("conferencemember---conference");
        alignment.add("paperfullversion---paper");
        alignment.add("meta-reviewer---review");
        alignment.add("reviewer---review");
        alignment.add("paperabstract---abstract");
        alignment.add("conference---conference");
        alignment.add("author---paper_author");
        alignment.add("paper---paper");
        alignment.add("meta-review---review");
        alignment.add("document---document");
        alignment.add("conferencechair---conference");*/

        /*alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("review---neutral_review");
        alignment.add("conferencemember---conference_banquet");
        alignment.add("conferencemember---conference_paper");
        alignment.add("conferencemember---conference");
        alignment.add("conferencemember---conference_participant");
        alignment.add("conferencemember---conference_session");
        alignment.add("conferencemember---conference_proceedings");
        alignment.add("conferencemember---conference_trip");
        alignment.add("paperfullversion---paper");
        alignment.add("meta-reviewer---review");
        alignment.add("reviewer---review");
        alignment.add("reviewer---possible_reviewer");
        alignment.add("paperabstract---paper");
        alignment.add("paperabstract---abstract");
        alignment.add("conference---conference_banquet");
        alignment.add("conference---conference_paper");
        alignment.add("conference---conference");
        alignment.add("conference---conference_participant");
        alignment.add("conference---conference_session");
        alignment.add("conference---conference_proceedings");
        alignment.add("conference---conference_trip");
        alignment.add("preference---conference");
        alignment.add("author---paper_author");
        alignment.add("rejection---rejected_paper");
        alignment.add("paper---demo_paper");
        alignment.add("paper---paper_author");
        alignment.add("paper---paper");
        alignment.add("paper---poster_paper");
        alignment.add("meta-review---review");
        alignment.add("document---document");
        alignment.add("conferencechair---conference_banquet");
        alignment.add("conferencechair---conference_paper");
        alignment.add("conferencechair---conference");
        alignment.add("conferencechair---conference_participant");
        alignment.add("conferencechair---conference_session");
        alignment.add("conferencechair---conference_proceedings");
        alignment.add("conferencechair---conference_trip");*/

        alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("conferencemember---conference_participant");
        alignment.add("conferencemember---agency_staff_member");
        alignment.add("paperfullversion---demo_paper");
        alignment.add("paperfullversion---regular_paper");
        alignment.add("paperfullversion---poster_paper");
        alignment.add("paperabstract---workshop_paper");
        alignment.add("paperabstract---regular_paper");
        alignment.add("paperabstract---industrial_paper");
        alignment.add("conference---conference");
        alignment.add("author---paper_author");
        alignment.add("paper---paper");
        alignment.add("co-author---paper_author");
        alignment.add("document---document");


        //一轮排除：通过局部一致性原则(LCP)，排除子父类关系矛盾的匹配对
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strs[0], strs[1]) == 1)
                newAlignment.add(alignment.get(i));
        }
        for (int i = 0; i < newAlignment.size(); i++) {
            String[] strs = splitString1(newAlignment.get(i));
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
            for (int j = 0; j < fatherAndSon2.size(); j++) {
                if (fatherAndSon2.get(j).equals(strs[1]))
                    fatherAndSon2.remove(j);
            }

            for (int j = 0; j < alignment.size(); j++) {
                String[] align = splitString1(alignment.get(j));
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
        for (int i = 0; i < alignment.size(); i++) {
            for (int j = 0; j < tempAlignment.size(); j++) {
                if (alignment.get(i).equals(tempAlignment.get(j))) {
                    alignment.remove(i);
                    i--;
                    break;
                }
            }
        }

        //
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
                fatherAndSon1.add(strs[0]);

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
                //fatherAndSon2.add(strs[1]);

                double max = 0;
                if (fatherAndSon1.size() != 0 && fatherAndSon2.size() != 0) {
                    for (int p = 0; p < fatherAndSon1.size(); p++) {
                        for (int q = 0; q < fatherAndSon2.size(); q++) {
                            double sim1 = WaysOfSimilarity.SMOASimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            double sim2 = WaysOfSimilarity.wuAndPalmerSimilarity(fatherAndSon1.get(p), fatherAndSon2.get(q));
                            double sim=sim1>sim2?sim1:sim2;
                            if (sim > max)
                                max = sim;
                        }
                    }
                }
                if (max < 1)
                    tempAlignment.add(alignment.get(i));
            }
        }
        tempAlignment.add("conferencemember---agency_staff_member");
        tempAlignment.add("paperfullversion---demo_paper");
        tempAlignment.add("paperfullversion---poster_paper");
        tempAlignment.add("co-author---paper_author");
        tempAlignment.add("paperabstract---workshop_paper");
        tempAlignment.add("paperabstract---regular_paper");

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

    private static String[] splitString1(String s) {
        return s.split("---");
    }

    private static String[] splitString2(String s) {
        return s.split(" ");
    }
}
