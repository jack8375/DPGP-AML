package similarity;

import edu.sussex.nlp.jws.JWS;
import edu.sussex.nlp.jws.WuAndPalmer;
import fr.inrialpes.exmo.ontosim.string.StringDistances;
import org.apache.lucene.search.spell.NGramDistance;

public class WaysOfSimilarity {

    private static String dir = "WordNet-2.1";
    private static JWS ws = new JWS(dir, "2.1");
    private static WuAndPalmer wu = ws.getWuAndPalmer();


    public static double NGramSimilarity(String s1, String s2, int n) {
        if ((s1 == null || s1.length() <= 0 || s1.equals("null")) || (s2 == null || s2.length() <= 0 || s2.equals("null")))
            return 0.0;
        if (s1.equals(s2))
            return 1.0;
        NGramDistance ng = new NGramDistance(n);
        return ng.getDistance(s1.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " ").replace(" ", ""),
                s2.replace("(", " ").replace(")", " ").replace("/", " ").replace(",", " ").replace(" ", ""));
    }


    public static double SMOASimilarity(String s1, String s2) {
        if ((s1 == null || s1.length() <= 0 || s1.equals("null")) || (s2 == null || s2.length() <= 0 || s2.equals("null")))
            return 0.0;
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
