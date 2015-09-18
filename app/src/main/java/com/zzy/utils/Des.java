package com.zzy.utils;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Des {

	private static final String key = "AQWSZDE#$%CFT@HJ";

	/**
	 * 加密（使用DES算法）
	 * 
	 * @param txt
	 *            需要加密的文本
	 * @param key
	 *            密钥
	 * @return 成功加密的文本
	 * @throws InvalidKeySpecException
	 * @throws InvalidKeyException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String enCrypto(String txt) {
		StringBuffer sb = new StringBuffer();
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("DES");
			Cipher cipher = Cipher.getInstance("DES");
			SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.ENCRYPT_MODE, deskey);
			byte[] cipherText = cipher.doFinal(txt.getBytes());
			for (int n = 0; n < cipherText.length; n++) {
				String stmp = (java.lang.Integer
						.toHexString(cipherText[n] & 0XFF));

				if (stmp.length() == 1) {
					sb.append("0" + stmp);
				} else {
					sb.append(stmp);
				}
			}
			return sb.toString().toUpperCase();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return txt;
	}

	/**
	 * 解密（使用DES算法）
	 * 
	 * @param txt
	 *            需要解密的文本
	 * @param key
	 *            密钥
	 * @return 成功解密的文本
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IllegalBlockSizeException
	 * @throws BadPaddingException
	 */
	public static String deCrypto(String txt) {
		try {
			DESKeySpec desKeySpec = new DESKeySpec(key.getBytes());
			SecretKeyFactory skeyFactory = SecretKeyFactory.getInstance("DES");
			Cipher cipher = Cipher.getInstance("DES");
			SecretKey deskey = skeyFactory.generateSecret(desKeySpec);
			cipher.init(Cipher.DECRYPT_MODE, deskey);
			byte[] btxts = new byte[txt.length() / 2];
			for (int i = 0, count = txt.length(); i < count; i += 2) {
				btxts[i / 2] = (byte) Integer.parseInt(txt.substring(i, i + 2),
						16);
			}
			return (new String(cipher.doFinal(btxts)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return txt;
	}
}
