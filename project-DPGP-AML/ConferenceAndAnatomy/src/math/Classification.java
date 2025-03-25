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

    //各行中最高的SF值置1，其余置0(**)
    public static double[][] top1BinaryMatrix(double[][] sm) {
        for (int i = 0; i < sm.length; i++) {
            double max_1 = getTop1(sm[i]);
            if (max_1 == 0)
                continue;  //若整行为0，则不执行操作
            else {
                for (int j = 0; j < sm[i].length; j++) {
                    sm[i][j] = sm[i][j] == max_1 ? 1 : 0;
                }
            }
        }
        return sm;
    }

    //各行中最高SF值的前percent之内置1，其余置0(median对应于50%)
    public static double[][] topPercentBinaryMatrix(double[][] sm, double percent) {
        for (int i = 0; i < sm.length; i++) {
            double max_percent = getTopPercent(sm[i], percent);
            if (max_percent == 0)
                continue;  //返回数组中最后一个不为0的元素时，若该元素也为0，则代表整行为0
            else {
                for (int j = 0; j < sm[i].length; j++) {
                    sm[i][j] = sm[i][j] >= max_percent ? 1 : 0;
                }
            }
        }
        return sm;
    }


    //获取前percent的SF临界值(median对应于50%)
    private static double getTopPercent(double[] arr, double percent) {
        double[] temparr = copy(arr);
        int index = (int) (temparr.length * percent);
        sort(temparr);
        if (temparr[index - 1] != 0) {
            return temparr[index - 1];  //如果临界值不为0，则直接返回
        } else {  //如果临界值为0，则返回数组中最后一个不为0的元素
            int i = 1;  //防止出现-1索引
            for (; i < temparr.length; i++) {
                if (temparr[i] == 0)
                    break;
            }
            return temparr[i - 1];
        }
    }

    //获取最大的SF值
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

    //降序排列
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
