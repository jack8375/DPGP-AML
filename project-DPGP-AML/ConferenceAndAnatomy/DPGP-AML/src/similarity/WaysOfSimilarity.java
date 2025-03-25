package similarity;

import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.WuAndPalmer;
import fr.inrialpes.exmo.ontosim.string.StringDistances;
import org.apache.lucene.search.spell.NGramDistance;

import java.util.List;

public class WaysOfSimilarity {

    // WordNet安装路径选择（不要进入到最内层）
    private static String dir = "WordNet-2.1";
    // WordNet版本为2.1版
    private static JWS ws = new JWS(dir, "2.1");
    // 变量声明
    private static WuAndPalmer wu = ws.getWuAndPalmer();


    //n:n个字符为一组进行切分,创建NGram集合
    public static double NGramSimilarity(String s1, String s2, int n) {
        if ((s1 == null || s1.length() <= 0 || s1.equals("null")) || (s2 == null || s2.length() <= 0 || s2.equals("null")))
            return 0.0;
        if (s1.equals(s2))
            return 1.0;
        //Java的底层方法
        NGramDistance ng = new NGramDistance(n);
        return ng.getDistance(s1.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " ").replace(" ", ""),
                s2.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " ").replace(" ", ""));
    }


    public static double SMOASimilarity(String s1, String s2) {
        if ((s1 == null || s1.length() <= 0 || s1.equals("null")) || (s2 == null || s2.length() <= 0 || s2.equals("null")))
            return 0.0;
        //文本的相似度计算：
        //文本1中的第一个单词和文本2中的每一个单词都进行相似度计算，取max
        //文本1中的第二个单词......直到最后一个单词，max求和
        //return (2 * sum) / (strs1.length + strs2.length)
        String[] strs1 = splitString(s1.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " "));
        String[] strs2 = splitString(s2.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " "));
        double sum = 0.0;
        for (int i = 0; i < strs1.length; i++) {
            double max = 0.0;
            for (int j = 0; j < strs2.length; j++) {
                double temp = SMOA(strs1[i], strs2[j]);
                if (temp > max) {
                    max = temp;
                }
            }
            sum = sum + max;
        }
        return (2 * sum) / (strs1.length + strs2.length);
    }

    //SMOA计算词的相似度
    private static double SMOA(String a, String b) {
        if ((a == null || a.length() <= 0) || (b == null || b.length() <= 0)) {
            return 0.0;
        }
        if (a.equals(b)) {
            return 1.0;
        }
        return 1d - StringDistances.smoaDistance(a, b);
    }


    public static double wuAndPalmerSimilarity(String s1, String s2) {
        if ((s1 == null || s1.length() <= 0 || s1.equals("null")) || (s2 == null || s2.length() <= 0 || s2.equals("null")))
            return 0.0;
        String[] strs1 = splitString(s1.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " "));
        String[] strs2 = splitString(s2.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " "));
        double sum = 0.0;
        for (int i = 0; i < strs1.length; i++) {
            double max = 0.0;
            for (int j = 0; j < strs2.length; j++) {
                double temp = maxScoreOfWu(strs1[i], strs2[j]);
                if (temp > max) {
                    max = temp;
                }
            }
            sum = sum + max;
        }
        return (2 * sum) / (strs1.length + strs2.length);
    }

    private static double maxScoreOfWu(String a, String b) {
        if ((a == null || a.length() <= 0) || (b == null || b.length() <= 0)) {
            return 0.0;
        }
        if (a.equals(b)) {
            return 1.0;
        }
        double sc1 = wu.max(a, b, "n");
        double sc2 = wu.max(a, b, "v");
        return sc1 > sc2 ? sc1 : sc2;
    }

    //基于结构的相似度方法：
    //取概念自身和它的直接子类，找出最相似的作为源概念和目标概念的相似度
    public static double sonGreedySimilarity(String s1, String s2) {
        String[] strs1 = splitString(s1);
        String[] strs2 = splitString(s2);
        double max = 0.0;
        for (int i = 0; i < strs1.length; i++) {
            for (int j = 0; j < strs2.length; j++) {
                double temp = SMOA(strs1[i], strs2[j]);
                if (temp > max)
                    max = temp;
            }
        }
        return max;
    }

    private static String[] splitString(String s) {
        return s.split(" ");
    }
}
