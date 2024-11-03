package main;

import java.util.ArrayList;
import java.util.List;

public class ExcludeMethods {

    public static List<String> getAllFatherAndSon(String s, List<List<String>> class1) {
        List<String> allSon = getAllSon(s, class1);
        List<String> allFather = getAllFather(s, class1);
        List<String> allFatherAndSon = new ArrayList<>(allSon);
        allFatherAndSon.addAll(allFather);
        for (int i = 0; i < allFatherAndSon.size() - 1; i++) {
            for (int j = i + 1; j < allFatherAndSon.size(); j++) {
                if (allFatherAndSon.get(i).equals(allFatherAndSon.get(j))) {
                    allFatherAndSon.remove(j);
                    j--;
                }
            }
        }
        return allFatherAndSon;
    }

    public static List<String> getAllSon(String s, List<List<String>> class1) {
        List<String> allSon = new ArrayList<>();
        allSon.add(s);
        for (int i = 0; i < allSon.size(); i++) {
            int index = 0;
            for (; index < class1.get(1).size(); index++) {
                if (class1.get(1).get(index).equals(allSon.get(i)))
                    break;
            }
            if (index == 73 || index == 49)
                break;
            if (class1.get(5).get(index).length() != 0) {
                String newString = class1.get(5).get(index).substring(1);
                String[] strsSon = splitString1(newString);
                for (int j = 0; j < strsSon.length; j++)
                    allSon.add(strsSon[j]);
            }
        }
        return allSon;
    }

    public static List<String> getAllFather(String s, List<List<String>> class1) {
        List<String> allFather = new ArrayList<>();
        allFather.add(s);
        for (int i = 0; i < allFather.size(); i++) {
            int index = 0;
            for (; index < class1.get(1).size(); index++) {
                if (class1.get(1).get(index).equals(allFather.get(i)))
                    break;
            }
            if (index == 73 || index == 49)
                break;
            if (class1.get(6).get(index).length() != 0) {
                String newString = class1.get(6).get(index).substring(1);
                String[] strsFather = splitString1(newString);
                for (int j = 0; j < strsFather.length; j++)
                    allFather.add(strsFather[j]);
            }
        }
        return allFather;
    }

    public static List<String> divide(List<String> words, String entity) {
        String[] strs = splitString2(entity);
        List<String> divideWords = new ArrayList<>();
        List<String> tempList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            boolean flag = false;
            for (int j = 0; j < words.size(); j++) {
                if (strs[i].equals(words.get(j))) {
                    flag = true;
                    break;
                }
            }
            if (flag)
                divideWords.add(strs[i]);
            else tempList.add(strs[i]);
        }
        for (int i = 0; i < tempList.size(); i++) {
            String word = tempList.get(i);
            for (int j = 0; j < words.size(); j++) {
                if (word.charAt(0) == words.get(j).charAt(0)) {
                    String subWord = word.substring(0, words.get(j).length());
                    if (subWord.equals(words.get(j))) {
                        divideWords.add(subWord);
                        String newWord = word.substring(subWord.length());
                        boolean flag = false;
                        for (int p = 0; p < words.size(); p++) {
                            if (newWord.equals(words.get(p))) {
                                flag = true;
                                break;
                            }
                        }
                        if (flag) {
                            divideWords.add(newWord);
                            break;
                        } else word = newWord;
                    }
                }
            }
        }
        return divideWords;
    }

    private static String[] splitString1(String s) {
        return s.split(" ");
    }

    private static String[] splitString2(String s) {
        return s.split("_");
    }
}
