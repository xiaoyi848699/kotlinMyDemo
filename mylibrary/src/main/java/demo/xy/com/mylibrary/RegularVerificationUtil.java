package demo.xy.com.mylibrary;

import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import demo.xy.com.mylibrary.log.Write;

/**
 * Created by xy on 2018/12/6.
 */
public class RegularVerificationUtil {
    /**
     * 验证邮箱格式
     *
     * @param email
     * @return
     */
    public static boolean emailFormat(String email) {
        boolean tag = true;
        String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern pattern = Pattern.compile(pattern1);
        Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 验证输入的邮箱格式是否符合
     *
     * @param email
     * @return 是否合法
     */
    public static boolean emailFormat2(String email) {
        boolean tag = true;
        final String pattern1 = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        final Pattern pattern = Pattern.compile(pattern1);
        final Matcher mat = pattern.matcher(email);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 判断是否是手机号码
     *
     * @param mobiles
     *            手机号码
     * @return
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(14[0-9])|(17[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否含有中文下的标点符号
     *
     * @param tempStr
     * @return
     */
    public static boolean getCnCheckResult(String tempStr) {
        if (tempStr == null)
            return false;
        return Pattern.compile("[？！·￥（）……—【】‘；：“”’。，、？]").matcher(tempStr)
                .find();
    }

    /**
     * 判断字符串,不能包含以下字符，如果包含返回true 否则返回false
     * @param str
     * @return
     */
    public static boolean isLegalCharacter(String str){
//		String regex = "[\\<\\>:\\,'‘“”\\?()#&\\$|%+\\;~^]+$";
//		String regex = "[0-9a-zA-Z`!@$^*()-_=\\{\\}\\[\\]\\.\\<\\>\\/]+$";
        String regex = "[\\<\\>:\\,'\"\\?()#&\\$|%+\\;~^]+$";//'导致无法连续输入中文
        for (int i = 0; i < str.length(); i++) {
            String tempStr =  str.substring(i, i+1);
            if(Pattern.compile(regex).matcher(tempStr).find()){
                return true;
            }
        }
        return false;
    }
    /**
     * 判断字符串,只能包含以下字符，如果是返回true 否则返回false
     * @param str
     * @return
     */
    public static boolean isLegalInput(String str){
        String regex = "[0-9a-zA-Z`!@*()-_=\\{\\}\\[\\]\\.\\<\\>\\/]+$";
        return Pattern.compile(regex).matcher(str).matches();
    }

    /**
     * 判断字符串中是否是合法邮箱格式
     * @param email
     * @return
     */
    public static boolean isLegalEmail(String email){
        String check = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }
    /**
     * 是否包含中文，有中文返回true 否则返回false
     * @param s
     * @return
     */
    public static boolean checkfilename(String s){
        try {
            s=new String(s.getBytes("UTF-8"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            Write.debug("method name --> checkfilename :"+e.getMessage());
        }//用GBK编码
        String pattern="[\u4e00-\u9fa5]+";
        Pattern p=Pattern.compile(pattern);
        Matcher result=p.matcher(s);
        return result.find(); //是否含有中文字符
    }




    /**
     * a)口令长度至少6个字符； b)口令必须包含如下至少两种字符的组合： －至少一个小写字母； －至少一个大写字母； －至少一个数字；
     * －至少一个特殊字符：`~!@#$%^&*()-_=+\|[{}];:'",<.>/?和空格
     *
     * @param str
     * @return
     */
    public static boolean getCheckResult(String str) {

        if (getCnCheckResult(str)) {
            return false;
        }
        String result = check(str);

        if (!TextUtils.isEmpty(result)) {
            if (is_alpha(result)) {
                return true;
            }
        }
        return false;
    }

    public static boolean is_alpha(String alpha) {
        if (alpha == null)
            return false;
        return Pattern.compile("[a-zA-Z|0-9]").matcher(alpha).find();
    }

    public static String check(String result) {
        String str = "";
        String regaz = "[a-z]";
        String regAZ = "[A-Z]";
        // String regOther = "[`~!@#$%^&*()\\-_=+\\|\\[{}\\];:'\",<.>/?\\s]";
        String regDig = "[0-9]";

        if (Pattern.compile(regaz).matcher(result).find()) {
            return stringFilter(result, regaz);
        } else if (Pattern.compile(regAZ).matcher(result).find()) {
            return stringFilter(result, regAZ);
        } else if (Pattern.compile(regDig).matcher(result).find()) {
            return stringFilter(result, regDig);
        }

        return str;
    }

    // 过滤特殊字符
    public static String stringFilter(String str, String regEx) {
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String result = m.replaceAll("").trim();
        return result;
    }

}
