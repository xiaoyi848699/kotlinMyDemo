package demo.xy.com.xytdcq.uitls;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class HexUtil {

	public static final int TYPE_YEAR = 10;
	public static final int TYPE_MONTH = 20;
	public static final int TYPE_DAY = 30;
	public static final int TYPE_FULL = 0;

	public static String formatHexString(byte[] bytearray) {
		StringBuffer hexString = new StringBuffer();// new
		// StringBuffer("hex string = ");
		for (int i = 0; i < bytearray.length; ++i) {

			String hexStr = Integer.toHexString(decodeUnsigned(bytearray[i]));
			if (hexStr.length() == 1) {
				hexStr = "0" + hexStr;
			}
			hexString.append(hexStr);
			// hexString.append(" ");
		}
		return hexString.toString();
	}

	public static short decodeUnsigned(byte signed) {
		if (signed >= 0)
			return (short) signed;
		else
			return (short) (256 + (short) signed);
	}

	public static short encodeUnsigned(int positive) {
		if (positive < 32768)
			return (short) positive;
		else
			return (short) (-(0x10000 - positive));
	}

	public static int decodeUnsigned(short signed) {
		if (signed >= 0)
			return signed;
		else
			return 0x10000 + signed;
	}
	/**
	 * byte 数组转 int
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToInt16(byte[] b) {
		BigInteger bigInt = new BigInteger(formatHexString(b), 16);
		return String.valueOf(bigInt);
	}


	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStr(byte[] b) {

		String stmp = "";
		StringBuffer sb = new StringBuffer();

		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				sb.append("0").append(stmp).append(" ");
			else
				sb.append(stmp).append(" ");
		}
		return sb.toString().toUpperCase();
	}

	/**
	 * bytes转换成十六进制字符串
	 */
	public static String byte2HexStrWrite(byte[] b) {
		StringBuffer hs = new StringBuffer();
		String stmp = "";
		for (int n = 0; n < b.length; n++) {
			stmp = (Integer.toHexString(b[n] & 0XFF));
			if (stmp.length() == 1)
				hs.append("0").append(stmp);
			else
				hs.append(stmp);
		}
		return hs.toString().toUpperCase();
	}

	/**
	 * 字节数组转为普通字符串（ASCII对应的字符）
	 * 
	 * @param bytearray
	 *            byte[]
	 * @return String
	 */
	public static String bytetoString(byte[] bytearray) {
		// String result = "";
		char temp;
		StringBuffer result = new StringBuffer();
		int length = bytearray.length;
		for (int i = 0; i < length; i++) {
			temp = (char) bytearray[i];
			if (bytearray[i] != (byte) 0xFF && bytearray[i] != (byte) 0x00) {
				// result += temp;
				result.append(temp);
			}
		}
		return result.toString();
	}

	/**
	 * 十六进制转二进制 获取start 到 end之间的字符
	 * 
	 * @param hexString
	 * @return
	 */
	public static String hexString2binaryString(String hexString, int start,
			int end) {
		if (null == hexString || "".equals(hexString.trim())
				|| hexString.length() % 2 != 0)
			return null;
		// String bString = "";
		StringBuffer tmp = new StringBuffer();
		for (int i = 0; i < hexString.length(); i++) {
			tmp.append("0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16)));
			// bString += tmp.substring(tmp.length() - 4);
		}
		String str = "";
		if (tmp.length() >= end) {
			str = tmp.substring(start, end);
		}

		return str;
	}
	/**
	 * 十六进制转二进制 获取start 到 end之间的字符
	 * 
	 * @param hexString
	 * @return
	 */
	public static String hexString2binaryString(String hexString) {
		if (hexString == null || hexString.length() % 2 != 0)
			return null;
		String bString = "", tmp;
		for (int i = 0; i < hexString.length(); i++) {
			tmp = "0000"
					+ Integer.toBinaryString(Integer.parseInt(
							hexString.substring(i, i + 1), 16));
			bString += tmp.substring(tmp.length() - 4);
		}
		return bString;
	}

	/**
	 * 十进制数转二进制数
	 * 
	 * @param d
	 *            十进制数
	 * @return 十进制数转换成二进制的字符串
	 */
	public static String decimal2BinaryStr(double d) {
		String result = decimal2BinaryStr_Inte(d);
		result += decimal2BinaryStr_Deci(d);
		return result;
	}

	/**
	 * 十进制整数部分转二进制数
	 * 
	 * @param d
	 *            十进制数
	 * @return 十进制整数部分转换成二进制的字符串
	 */
	public static String decimal2BinaryStr_Inte(double d) {
		// return Integer.toBinaryString((int)d);
		/*
		 * 本来利用上面的Integer.toBinaryString(int)就可以得到整数部分的二进制结果，
		 * 但为了展示十进制转二进制的算法，现选择以下程序来进行转换
		 */
		// String result = "";
		StringBuffer result = new StringBuffer();
		long inte = (long) d;
		int index = 0;
		// while(true){
		while (inte != 0) {
			result.append(inte % 2);
			inte = inte / 2;
			index++;
			if (index % 4 == 0) {
				// result+=" ";
			}
			if (inte == 0) {
				while (index % 4 != 0) {
					result.append("0");
					index++;
				}
				break;
			}
		}
		char[] c = result.toString().toCharArray();
		char[] cc = new char[c.length];
		for (int i = c.length; i > 0; i--) {
			cc[cc.length - i] = c[i - 1];
		}
		return new String(cc);
	}

	/**
	 * 十进制小数部分转二进制
	 * 
	 * @param d
	 *            十进制数
	 * @return 十进制小数部分转换成二进制小数的字符串
	 */
	public static String decimal2BinaryStr_Deci(double d) {
		return decimal2BinaryStr_Deci(d, 0);
	}

	/**
	 * 十进制小数部分转二进制
	 * 
	 * @param d
	 *            十进制数
	 * @param scale
	 *            小数部分精确的位数
	 * @return 十进制小数部分转换成二进制小数的字符串
	 */
	public static String decimal2BinaryStr_Deci(double d, int scale) {
		double deci = sub(d, (long) d);
		if (deci == 0) {
			return "";
		}
		// 为了防止程序因所转换的数据转换后的结果是一个无限循环的二进制小数，因此给其一个默认的精确度
		if (scale == 0) {
			scale = (String.valueOf(deci).length() - 2) * 4;
		}
		int index = 0;
		StringBuilder inteStr = new StringBuilder();
		double tempD = 0.d;
		// while(true){
		while (deci != 0 && index != scale) {
			if (deci == 0 || index == scale) {
				while (index % 4 != 0) {
					inteStr.append("0");
					index++;
				}
				break;
			}
			if (index == 0) {
				inteStr.append(".");
			}
			tempD = deci * 2;
			inteStr.append((int) tempD);
			deci = sub(tempD, (int) tempD);
			index++;
			if (index % 4 == 0) {
				// inteStr.append(" ");
			}
		}
		return inteStr.toString();
	}

	/**
	 * 提供精确的减法运算。
	 * 
	 * @param v1
	 *            被减数
	 * @param v2
	 *            减数
	 * @return 两个参数的差
	 */
	public static double sub(double v1, double v2) {
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		return b1.subtract(b2).doubleValue();
	}
	public static String getIPAddress(Context context) {
		NetworkInfo info = ((ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
				try {
					//Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
					for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
						NetworkInterface intf = en.nextElement();
						for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}

			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
				WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
				return ipAddress;
			}
		} else {
			//当前无网络连接,请在设置中打开网络
		}
		return null;
	}

	/**
	 * 将得到的int类型的IP转换为String类型
	 *
	 * @param ip
	 * @return
	 */
	public static String intIP2StringIP(int ip) {
		return (ip & 0xFF) + "." +
				((ip >> 8) & 0xFF) + "." +
				((ip >> 16) & 0xFF) + "." +
				(ip >> 24 & 0xFF);
	}
}
