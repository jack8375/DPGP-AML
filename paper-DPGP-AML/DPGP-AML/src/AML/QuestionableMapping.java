package AML;

import main.ExcludeMethods;
import similarity.WaysOfSimilarity;

import java.util.ArrayList;
import java.util.List;

public class QuestionableMapping {

    public static List<String> QMS1(List<String> PA, List<String> PSA) {
        List<String> QMS1 = new ArrayList<>();
        for (int i = 0; i < PA.size() - 1; i++) {
            String[] pair = splitString1(PA.get(i));
            boolean flag = false;
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
        List<String> QMS2 = new ArrayList<>();
        for (int i = 0; i < PSA.size(); i++) {
            String[] strsPSA = splitString1(PSA.get(i));
            List<String> sourceFatherAndSon = ExcludeMethods.getAllFatherAndSon(strsPSA[0], sourceClass);
            List<String> targetFatherAndSon = ExcludeMethods.getAllFatherAndSon(strsPSA[1], targetClass);
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

    public static List<String> getFatherAndSon(List<List<String>> sourceOrTarget, String anchor) {
        List<String> fatherAndSon = new ArrayList<>();
        int i = 0;
        for (; i < sourceOrTarget.get(1).size(); i++) {
            if (sourceOrTarget.get(1).get(i).equals(anchor))
                break;
        }
        String s = sourceOrTarget.get(3).get(i);
        if (s.charAt(0) == ' ') {
            s = s.substring(1);
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
