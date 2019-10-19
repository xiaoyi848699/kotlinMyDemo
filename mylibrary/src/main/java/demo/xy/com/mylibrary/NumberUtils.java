package demo.xy.com.mylibrary;

import java.text.DecimalFormat;

public class NumberUtils {
    /**
     * //小数点后保留位数
     * @param mDouble  数据
     * @param valMM  位数
     * @return
     */
    public static String getDecimals(double mDouble,int valMM) {
        //小数点后保留位数
        String format = "###0.00";
        switch (valMM) {
            case 0:
                format = "###0";
                break;
            case 1:
                format = "###0.0";
                break;
            case 2:
                format = "###0.00";
                break;
            case 3:
                format = "###0.000";
                break;
            case 4:
                format = "###0.0000";
                break;
            default:
                break;
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);//格式化设置
        String data = decimalFormat.format(mDouble);
        return  data.replace(",", ".");//多语言解析会返回“,”
    }
}
