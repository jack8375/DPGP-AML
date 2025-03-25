package main;

import java.io.IOException;
import java.util.List;

public class test1 {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        List<String> reference = definedMatrix.readMappedList(definedMatrix.Reference_FMA_SNOMED);
        List<String> anchors = definedMatrix.readMappedList(definedMatrix.Anchors_FMA_SNOMED);
        double F_measure;
        double num = 0;
        for (int p = 0; p < reference.size(); p++) {
            for (String element : anchors) {
                if (reference.get(p).equals(element)) {
                    num++;
                    break;
                }
            }
        }
        double recall = num / reference.size();
        double precision = num / anchors.size();
        if (recall == 0 || precision == 0)
            F_measure = 0;
        else F_measure = (2 * recall * precision) / (recall + precision);
        System.out.println("-----------------------");
        System.out.println("查全率: " + recall);
        System.out.println("查准率: " + precision);
        System.out.println("f-measure: " + F_measure);
        System.out.println("-----------------------");
    }
}
