package AML;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuestionableMapping {
    public static List<String> QMS(List<String> alignment, List<String> PA, List<String> PSA, double threshold) {
        Set<String> QMS_H = new HashSet<>();
        //1.获取partial alignment中一对多或多对一的映射
        for (int i = 0; i < PA.size() - 1; i++) {
            String[] pair = splitString(PA.get(i));
            boolean flag = false;  //当前映射的标志位，用于添加当前映射
            for (int j = i + 1; j < PA.size(); j++) {
                String[] otherPair = splitString(PA.get(j));
                if (pair[0].equals(otherPair[0]) || pair[1].equals(otherPair[1])) {
                    QMS_H.add(PA.get(j));
                    flag = true;
                }
            }
            if (flag)
                QMS_H.add(PA.get(i));
        }
        for (String element : QMS_H) {
            for (int j = 0; j < PSA.size(); j++) {
                if (element.equals(PSA.get(j))) {
                    PSA.remove(j);
                    break;
                }
            }
        }

        //2.获取不属于PA但在alignment中具有高SF值的映射
        List<String> alignmentSubPA = alignmentSubPA(alignment, PA);
        for (int i = 0; i < alignmentSubPA.size(); i++) {
            String[] strsAlignmentSubPA = splitString(alignmentSubPA.get(i));
            if (WaysOfSimilarity.SMOASimilarity(strsAlignmentSubPA[0], strsAlignmentSubPA[1]) >= threshold)
                QMS_H.add(alignmentSubPA.get(i));
        }
        return new ArrayList<>(QMS_H);
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

    private static String[] splitString(String s) {
        return s.split("~~~");
    }
}
