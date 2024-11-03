package math;

public class Construction {

    public static double[][] add(double[][] sm1, double[][] sm2) {
        double[][] addArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                double temp = sm1[i][j] + sm2[i][j];
                addArr[i][j] = temp > 1 ? 1 : temp;
            }
        }
        return addArr;
    }

    public static double[][] sub(double[][] sm1, double[][] sm2) {
        double[][] subArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                if (sm1[i][j] >= sm2[i][j]) {
                    subArr[i][j] = sm1[i][j] - sm2[i][j];
                } else subArr[i][j] = sm2[i][j] - sm1[i][j];
            }
        }
        return subArr;
    }

    public static double[][] mul(double[][] sm1, double[][] sm2) {
        double[][] mulArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                mulArr[i][j] = sm1[i][j] * sm2[i][j];
            }
        }
        return mulArr;
    }

    public static double[][] div(double[][] sm1, double[][] sm2) {
        double[][] divArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                divArr[i][j] = protectedDiv(sm1[i][j], sm2[i][j]);
            }
        }
        return divArr;
    }

    public static double[][] max(double[][] sm1, double[][] sm2) {
        double[][] maxArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                maxArr[i][j] = Math.max(sm1[i][j], sm2[i][j]);
            }
        }
        return maxArr;
    }

    public static double[][] min(double[][] sm1, double[][] sm2) {
        double[][] minArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                minArr[i][j] = Math.min(sm1[i][j], sm2[i][j]);
            }
        }
        return minArr;
    }

    public static double[][] avg(double[][] sm1, double[][] sm2) {
        double[][] avgArr = new double[sm1.length][sm1[0].length];
        for (int i = 0; i < sm1.length; i++) {
            for (int j = 0; j < sm1[i].length; j++) {
                avgArr[i][j] = (sm1[i][j] + sm2[i][j]) / 2;
            }
        }
        return avgArr;
    }


    private static double protectedDiv(double i1, double i2) {
        double temp = 0;
        if (i1 <= i2) {
            if (i2 == 0) {
                temp = 1;
            } else temp = i1 / i2;
        }
        if (i1 > i2) {
            if (i1 == 0) {
                temp = 1;
            } else temp = i2 / i1;
        }
        return temp;
    }

}
