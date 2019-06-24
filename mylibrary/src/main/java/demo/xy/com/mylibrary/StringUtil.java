package demo.xy.com.mylibrary;

import java.math.BigDecimal;

/**
 * @Description:
 * @author: dong
 * @date: 2017/10/23 18:21.
 */

public class StringUtil {

    /**
     * 转换保留两位小数显示
     *
     * @param number
     * @return
     */
    public static double parseNumberToPrice(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    public static String parseDobleToString(double number) {
        BigDecimal bigDecimal = new BigDecimal(number);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字长度为2,英文字符长度为1
     *
     * @param str 需要得到长度的字符串
     * @return 得到的字符串长度
     */
    public static int getStrLength(String str) {
        if (str == null) return 0;
        char[] c = str.toCharArray();
        int len = 0;
        int k = 0x80;
        for (int i = 0; i < c.length; i++) {
            len++;
            if (c[i] / k != 0) {
                len++;
            }
        }
        return len;
    }
    // 判断一个字符是否是中文
    public static boolean isChinese(char c) {
        return c >= 0x4E00 && c <= 0x9FA5;// 根据字节码判断
    }

    // 判断一个字符串是否含有中文
    public static boolean hisChinese(String str) {
        if (str == null)
            return false;
        for (char c : str.toCharArray()) {
            if (isChinese(c))
                return true;// 有一个中文字符就返回
        }
        return false;
    }
    public static String ToDBC(String input) {
        char c[] = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {//空格
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {//半角与全角相差 65248
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }
}
