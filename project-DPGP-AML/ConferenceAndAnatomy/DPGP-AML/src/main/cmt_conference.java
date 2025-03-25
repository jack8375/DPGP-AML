package main;

import Parse.Parse_Algnment;
import Parse.Parse_Class;
import Parse.Parse_DataProperity;
import Parse.Parse_ObjectProperity;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class cmt_conference {
    public static void main(String[] args) throws Exception {

        String s1 = "D:\\conference\\DPGP-AML\\conference-dataset\\cmt.owl";
        List<List<String>> source1 = Parse_Class.Find_Class(s1);
        List<List<String>> source2 = Parse_ObjectProperity.Find_ObjectProperity(s1);
        List<List<String>> source3 = Parse_DataProperity.Find_DataProperity(s1);
        String s2 = "D:\\conference\\DPGP-AML\\conference-dataset\\Conference.owl";
        List<List<String>> target1 = Parse_Class.Find_Class(s2);
        List<List<String>> target2 = Parse_ObjectProperity.Find_ObjectProperity(s2);
        List<List<String>> target3 = Parse_DataProperity.Find_DataProperity(s2);
        String s3 = "D:\\conference\\DPGP-AML\\reference-alignment\\cmt-conference.rdf";
        List<String> reference = Parse_Algnment.parseRefalignFile(s3);

        //词库：注意排列顺序
        List<String> words = new ArrayList<>();
        words.add("author");
        words.add("abstract");
        words.add("chair");
        words.add("committee");
        words.add("co-author");
        words.add("conference");
        words.add("contribution");
        words.add("document");
        words.add("man");
        words.add("not");
        words.add("paper");
        words.add("person");
        words.add("program");
        words.add("preference");
        words.add("review");
        words.add("regular");
        words.add("reviewer");

        List<String> alignment = new ArrayList<>();
        List<String> newAlignment = new ArrayList<>();
        List<String> tempAlignment = new ArrayList<>();
        List<String> propertyMappings = new ArrayList<>();
        alignment.add("person---person");
        alignment.add("review---review");
        alignment.add("conferencemember---person");
        alignment.add("programcommitteemember---committee_member");
        alignment.add("meta-reviewer---reviewer");
        alignment.add("reviewer---reviewer");
        alignment.add("paperabstract---abstract");
        alignment.add("conference---conference_www");
        alignment.add("chairman---chair");
        alignment.add("preference---review_preference");
        alignment.add("author---regular_author");
        alignment.add("rejection---rejected_contribution");
        alignment.add("paper---regular_contribution");
        alignment.add("authornotreviewer---reviewer");
        alignment.add("meta-review---review");
        alignment.add("co-author---contribution_co-author");
        alignment.add("programcommitteechair---program_committee");
        alignment.add("programcommittee---program_committee");
        alignment.add("document---conference_document");
        alignment.add("user---reviewer");
        alignment.add("externalreviewer---reviewer");
        alignment.add("conferencechair---conference_www");

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
                if (max > 0.8)
                    newAlignment.add(alignment.get(i));
                else tempAlignment.add(alignment.get(i));
            }
        }
        //由上述代码得paper---paper,reviewer---reviewer为错误的匹配对，显然，子类的匹配同样错误
        //tempAlignment.add("externalreviewer---reviewer");
        //tempAlignment.add("author---contribution_co-author");
        tempAlignment.add("conferencemember---person");
        //tempAlignment.add("meta-reviewer---reviewer");
        tempAlignment.add("paperabstract---extended_abstract");
        tempAlignment.add("author---contribution_co-author");



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

        //三轮排除：通过父概念存在明显差异，排除高相似度但不具备匹配关系的实体
        for (int i = 0; i < alignment.size(); i++) {
            String[] strs = splitString1(alignment.get(i));
            List<String> father1;
            List<String> father2;
            int index1 = 0;
            for (; index1 < source1.get(1).size(); index1++) {
                if (source1.get(1).get(index1).equals(strs[0]))
                    break;
            }
            father1 = exclude.getAllFather(source1.get(1).get(index1), source1);
            for (int j = 0; j < father1.size(); j++) {
                if (father1.get(j).equals(strs[0]))
                    father1.remove(j);
            }

            int index2 = 0;
            for (; index2 < target1.get(1).size(); index2++) {
                if (target1.get(1).get(index2).equals(strs[1]))
                    break;
            }
            father2 = exclude.getAllFather(target1.get(1).get(index2), target1);
            for (int j = 0; j < father2.size(); j++) {
                if (father2.get(j).equals(strs[1]))
                    father2.remove(j);
            }

            if (father1.size() != 0 && father2.size() != 0) {
                double max = 0;
                for (int p = 0; p < father1.size(); p++) {
                    for (int q = 0; q < father2.size(); q++) {
                        double sim = WaysOfSimilarity.SMOASimilarity(father1.get(p), father2.get(q));
                        if (sim > max)
                            max = sim;
                    }
                }
                if (max < 0.4)
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

        //四轮排除：通过分词策略，排除语义矛盾的实体
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
                    if (list1.get(p).equals("not")) {
                        flag2 = true;
                        break;
                    }
                }
                for (int q = 0; q < list2.size(); q++) {
                    if (list2.get(q).equals("not")) {
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

        //匹配property(只匹配dataproperty，需要修改方法)
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


        for (int i = 0; i < alignment.size(); i++) {
            System.out.println(alignment.get(i));
        }
        System.out.println("------------------");
        for (int i = 0; i < newAlignment.size(); i++) {
            System.out.println(newAlignment.get(i));
        }
        System.out.println("------------------");
        for (int i = 0; i < tempAlignment.size(); i++) {
            System.out.println(tempAlignment.get(i));
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

//用standard alignment来运行automatic算法，测试DPGP部分的性能
        /*List<String> PSA = new ArrayList<>();
        //PSA.add("assignexternalreviewer---invites_co-reviewers");
        PSA.add("subjectarea---topic");
        PSA.add("review---review");
        //PSA.add("email---has_an_email");
        PSA.add("paperabstract---abstract");
        //PSA.add("assignedbyreviewer---invited_by");
        PSA.add("conference---conference_volume");
        PSA.add("conference---conference");
        PSA.add("person---person");
        PSA.add("co-author---contribution_co-author");
        PSA.add("document---conference_document");
        PSA.add("author---regular_author");
        PSA.add("preference---review_preference");
        PSA.add("chairman---chair");
        PSA.add("programcommittee---program_committee");*/
