package com.gsbina.android.nfc.sample;

public class Util {

	/**
	 * byte 配列を 16 進表記の文字列に変換する。<br>
	 * byte から 16 進表記の変換については {@link Util#byteToString(byte)} を利用する。<br>
	 * 各文字列の連結には '-' を使用する。
	 * 
	 * @param bytes
	 *            byte 配列
	 * @return
	 */
	public static String bytesToString(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		boolean isFirst = true;
		for (byte b : bytes) {
			if (isFirst) {
				isFirst = false;
			} else {
				sb.append("-");
			}
			sb.append(byteToString(b));
		}

		return sb.toString();
	}

	/**
	 * byte を 16 進表記の文字列に変換する。<br>
	 * 2桁に満たない場合は、先頭に 0 を補完する
	 * 
	 * @param b
	 *            byte
	 * @return
	 */
	public static Object byteToString(byte b) {
		String s = Integer.toHexString(b & 0xff).toUpperCase();
		if (s.length() > 1) {
			return s;
		} else {
			return "0" + s;
		}
	}

}
