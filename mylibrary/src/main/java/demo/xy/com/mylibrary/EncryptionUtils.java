package demo.xy.com.mylibrary;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import demo.xy.com.mylibrary.log.Write;

/**
 * Created by xy on 2018/12/6.
 */
public class EncryptionUtils {
    private final static String[] hexDigits = { "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    /**
     * MD5 加密
     * @return 加密后的密码
     */
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            Write.debug("NoSuchAlgorithmException:"+e.getMessage());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        return md5StrBuff.toString();
    }
    /**
     * 将摘要信息转换成SHA-256编码
     * @param message 摘要信息
     * @return SHA-256编码之后的字符串
     */
    public static String sha256Encode(String message){
        return encode("SHA-256",message);
    }
    /**
     * 将字节数组转换为16进制的字符串
     * @param byteArray 字节数组
     * @return 16进制的字符串
     */
    private static String byteArrayToHexString(byte[] byteArray){
        StringBuffer sb = new StringBuffer();
        for(byte byt:byteArray){
            sb.append(byteToHexString(byt));
        }
        return sb.toString();
    }
    /**
     * 将字节转换为16进制字符串
     * @param byt 字节
     * @return 16进制字符串
     */
    private static String byteToHexString(byte byt) {
        int n = byt;
        if (n < 0)
            n = 256 + n;
        return hexDigits[n/16] + hexDigits[n%16];
    }
    /**
     * 将摘要信息转换为相应的编码
     * @param code 编码类型
     * @param message 摘要信息
     * @return 相应的编码字符串
     */
    private static String encode(String code,String message){
        MessageDigest md;
        String encode = null;
        try {
            md = MessageDigest.getInstance(code);
            encode = byteArrayToHexString(md.digest(message
                    .getBytes("UTF-8")));
        } catch (Exception e) {
            Write.debug("encode fail: "+e.getMessage());
        }
        return encode;
    }

}
