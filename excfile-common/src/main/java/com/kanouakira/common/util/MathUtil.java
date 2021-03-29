package com.kanouakira.common.util;

import java.text.DecimalFormat;

/**
 * @author KanouAkira
 * @date 2021/3/26 16:38
 */
public class MathUtil {
    public static String calPercent(long a, long b){
        double num_a = a * 1D;
        double num_b = b * 1D;
        double num_percent = num_a / num_b;
        DecimalFormat df = new DecimalFormat("##.00%");
        return df.format(num_percent);
    }
}
