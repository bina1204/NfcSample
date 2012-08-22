package com.gsbina.android.nfc.sample;

public class Util {

	/**
	 * byte �z��� 16 �i�\�L�̕�����ɕϊ�����B<br>
	 * byte ���� 16 �i�\�L�̕ϊ��ɂ��Ă� {@link Util#byteToString(byte)} �𗘗p����B<br>
	 * �e������̘A���ɂ� '-' ���g�p����B
	 * 
	 * @param bytes
	 *            byte �z��
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
	 * byte �� 16 �i�\�L�̕�����ɕϊ�����B<br>
	 * 2���ɖ����Ȃ��ꍇ�́A�擪�� 0 ��⊮����
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
