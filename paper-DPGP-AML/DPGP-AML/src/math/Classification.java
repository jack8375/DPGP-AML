package math;

public class Classification {

    public static double[][] thresholdFiltering(double[][] sm, double threshold) {
        double[][] threSM = new double[sm.length][sm[0].length];
        for (int i = 0; i < sm.length; i++) {
            for (int j = 0; j < sm[i].length; j++) {
                threSM[i][j] = sm[i][j] >= threshold ? sm[i][j] : 0;
            }
        }
        return threSM;
    }

    public static double[][] top1BinaryMatrix(double[][] sm) {
        for (int i = 0; i < sm.length; i++) {
            double max_1 = getTop1(sm[i]);
            if (max_1 == 0)
                continue;
            else {
                for (int j = 0; j < sm[i].length; j++) {
                    sm[i][j] = sm[i][j] == max_1 ? 1 : 0;
                }
            }
        }
        return sm;
    }

    public static double[][] topPercentBinaryMatrix(double[][] sm, double percent) {
        for (int i = 0; i < sm.length; i++) {
            double max_percent = getTopPercent(sm[i], percent);
            if (max_percent == 0)
                continue;
            else {
                for (int j = 0; j < sm[i].length; j++) {
                    sm[i][j] = sm[i][j] >= max_percent ? 1 : 0;
                }
            }
        }
        return sm;
    }


    private static double getTopPercent(double[] arr, double percent) {
        double[] temparr = copy(arr);
        int index = (int) (temparr.length * percent);
        sort(temparr);
        if (temparr[index - 1] != 0) {
            return temparr[index - 1];
        } else {
            int i = 1;
            for (; i < temparr.length; i++) {
                if (temparr[i] == 0)
                    break;
            }
            return temparr[i - 1];
        }
    }

    private static double getTop1(double[] arr) {
        double[] temparr = copy(arr);
        sort(temparr);
        return temparr[0];
    }

    private static double[] copy(double[] arr) {
        double[] copyarr = new double[arr.length];
        for (int i = 0; i < arr.length; i++) {
            copyarr[i] = arr[i];
        }
        return copyarr;
    }

    public static void sort(double[] arr) {
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length - i - 1; j++) {
                if (arr[j] < arr[j + 1]) {
                    double temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
