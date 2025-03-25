package AML;

import main.exclude;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class QuestionableMapping {

    public static List<String> QMS(List<String> alignmentClass, List<String> PA, List<String> PSA, double threshold,
                                   List<List<String>> sourceClass, List<List<String>> targetClass) {
        List<String> QMS = new ArrayList<>();
        //1.获取partial alignment中一对多或多对一的映射
        List<String> QMS1 = new ArrayList<>();
        for (int i = 0; i < PA.size() - 1; i++) {
            String[] pair = splitString1(PA.get(i));
            boolean flag = false;  //当前映射的标志位，用于添加当前映射
            for (int j = i + 1; j < PA.size(); j++) {
                String[] otherPair = splitString1(PA.get(j));
                if (pair[0].equals(otherPair[0]) || pair[1].equals(otherPair[1])) {
                    QMS1.add(PA.get(j));
                    flag = true;
                }
            }
            if (flag)
                QMS1.add(PA.get(i));
        }
        delete(QMS1);
        for (int i = 0; i < QMS1.size(); i++) {
            for (int j = 0; j < PSA.size(); j++) {
                if (QMS1.get(i).equals(PSA.get(j))) {
                    PSA.remove(j);
                    break;
                }
            }
        }
        //2.获取不属于PA但在alignment中具有高SF值的映射
        List<String> QMS2 = new ArrayList<>();
        List<String> alignmentSubPA = alignmentSubPA(alignmentClass, PA);
        for (int i = 0; i < alignmentSubPA.size(); i++) {
            boolean flag = true;
            String[] strsAlignmentSubPA = splitString1(alignmentSubPA.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strsAlignmentSubPA[0], strsAlignmentSubPA[1]) >= threshold) {
                for (int j = 0; j < PSA.size(); j++) {
                    String[] strsPSA = splitString1(PSA.get(j));
                    List<String> sourceFatherAndSon = exclude.getAllFatherAndSon(strsPSA[0], sourceClass);
                    List<String> targetFatherAndSon = exclude.getAllFatherAndSon(strsPSA[1], targetClass);
                    for (int p = 0; p < sourceFatherAndSon.size(); p++) {
                        for (int q = 0; q < targetFatherAndSon.size(); q++) {
                            if (sourceFatherAndSon.get(p).equals(strsAlignmentSubPA[0]) && targetFatherAndSon.get(q).equals(strsAlignmentSubPA[1]))
                                QMS2.add(alignmentSubPA.get(i));  //满足局部一致性原则，添加到QMS
                            if (sourceFatherAndSon.get(p).equals(strsAlignmentSubPA[0]) || targetFatherAndSon.get(q).equals(strsAlignmentSubPA[1]))
                                flag = false;  //孤立概念的标志位
                        }
                    }
                }
            } else
                flag = false;
            if (flag)
                QMS2.add(alignmentSubPA.get(i));
        }
        for (int i = 0; i < QMS1.size(); i++) {
            QMS.add(QMS1.get(i));
        }
        for (int j = 0; j < QMS2.size(); j++) {
            QMS.add(QMS2.get(j));
        }
        return QMS;
    }

    public static List<String> QMS1(List<String> PA, List<String> PSA) {
        //1.获取partial alignment中一对多或多对一的映射
        List<String> QMS1 = new ArrayList<>();
        for (int i = 0; i < PA.size() - 1; i++) {
            String[] pair = splitString1(PA.get(i));
            boolean flag = false;  //当前映射的标志位，用于添加当前映射
            for (int j = i + 1; j < PA.size(); j++) {
                String[] otherPair = splitString1(PA.get(j));
                if (pair[0].equals(otherPair[0]) || pair[1].equals(otherPair[1])) {
                    QMS1.add(PA.get(j));
                    flag = true;
                }
            }
            if (flag)
                QMS1.add(PA.get(i));
        }
        delete(QMS1);
        for (int i = 0; i < QMS1.size(); i++) {
            for (int j = 0; j < PSA.size(); j++) {
                if (QMS1.get(i).equals(PSA.get(j))) {
                    PSA.remove(j);
                    break;
                }
            }
        }
        return QMS1;
    }

    public static List<String> QMS2(List<String> PSA, double threshold, List<List<String>> sourceClass, List<List<String>> targetClass) {
        //2.获取PSA子父类中具有高SF值的映射
        List<String> QMS2 = new ArrayList<>();
        for (int i = 0; i < PSA.size(); i++) {
            String[] strsPSA = splitString1(PSA.get(i));
            List<String> sourceFatherAndSon = exclude.getAllFatherAndSon(strsPSA[0], sourceClass);
            List<String> targetFatherAndSon = exclude.getAllFatherAndSon(strsPSA[1], targetClass);
            for (int p = 0; p < sourceFatherAndSon.size(); p++) {
                for (int q = 0; q < targetFatherAndSon.size(); q++) {
                    if ((WaysOfSimilarity.SMOASimilarity(sourceFatherAndSon.get(p), targetFatherAndSon.get(q)) >= threshold &&
                            WaysOfSimilarity.SMOASimilarity(sourceFatherAndSon.get(p), targetFatherAndSon.get(q)) < 1))
                        QMS2.add(sourceFatherAndSon.get(p) + "---" + targetFatherAndSon.get(q));
                }
            }
        }
        return QMS2;
    }

    //alignment - PA
    public static List<String> alignmentSubPA(List<String> alignment, List<String> PA) {
        List<String> alignmentSubPA = new ArrayList<>();
        for (int i = 0; i < alignment.size(); i++) {
            boolean flag = true;
            for (int j = 0; j < PA.size(); j++) {
                if (alignment.get(i).equals(PA.get(j))) {
                    flag = false;
                    break;
                }
            }
            if (flag)
                alignmentSubPA.add(alignment.get(i));
        }
        return alignmentSubPA;
    }

    //获取概念对应的子父类
    public static List<String> getFatherAndSon(List<List<String>> sourceOrTarget, String anchor) {
        List<String> fatherAndSon = new ArrayList<>();
        int i = 0;
        for (; i < sourceOrTarget.get(1).size(); i++) {
            if (sourceOrTarget.get(1).get(i).equals(anchor))
                break;
        }
        String s = sourceOrTarget.get(3).get(i);
        if (s.charAt(0) == ' ') {
            s = s.substring(1);  //没有父类时，去除首位的无关字符' '
            String[] strs = splitString2(s);
            for (int j = 0; j < strs.length; j++)
                fatherAndSon.add(strs[j]);
        } else if (s.equals("")) {
        } else {
            String[] strs = splitString2(s);
            for (int j = 0; j < strs.length; j++)
                fatherAndSon.add(strs[j]);
        }
        return fatherAndSon;
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

    private static String[] splitString1(String s) {
        return s.split("---");
    }

    private static String[] splitString2(String s) {
        return s.split(" ");
    }
}
