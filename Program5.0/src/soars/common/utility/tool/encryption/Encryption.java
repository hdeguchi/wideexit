/*
 * Created on 2006/05/12
 */
package soars.common.utility.tool.encryption;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author kurata
 */
public class Encryption {

	/**
	 * @param key
	 * @param value
	 * @param algorithm
	 * @return
	 */
	public static String encrypt(String key, String value, String algorithm) {
		byte[] data = encrypt( key, value.getBytes(), algorithm);
		if ( null == data)
			return null;

		String result = "";
		for ( int i = 0; i < data.length; ++i) {
			String text = Integer.toHexString( data[ i] & 0xff);
			if ( 2 == text.length())
				result += text;
			else if ( 1 == text.length())
				result += ( "0" + text);
			else
				return null;
		}

		return result;
	}

	/**
	 * @param key
	 * @param data
	 * @param algorithm
	 * @return
	 */
	public static byte[] encrypt(String key, byte[] data, String algorithm) {
		SecretKeySpec secretKeySpec = new SecretKeySpec( key.getBytes(), algorithm);
		Cipher cipher;
		try {
			cipher = Cipher.getInstance( algorithm);
			cipher.init( Cipher.ENCRYPT_MODE, secretKeySpec);
			return cipher.doFinal( data);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param key
	 * @param value
	 * @param algorithm
	 * @return
	 */
	public static String decrypt(String key, String value, String algorithm) {
		if ( value.equals( "") || 0 != ( value.length() % 2))
			return null;

		byte[] data = new byte[ value.length() / 2];
		for ( int i = 0, j = 0; i < value.length(); i += 2, ++j) {
			String text = value.substring( i, i + 2);
			data[ j] = Integer.valueOf( text, 16).byteValue();
		}

		data = decrypt( key, data, algorithm);
		return ( ( null == data) ? null : new String( data));
	}

	/**
	 * @param key
	 * @param data
	 * @param algorithm
	 * @return
	 */
	public static byte[] decrypt(String key, byte[] data, String algorithm) {
		SecretKeySpec secretKeySpec = new SecretKeySpec( key.getBytes(), algorithm);
		Cipher cipher;
		try {
			cipher = Cipher.getInstance( algorithm);
			cipher.init( Cipher.DECRYPT_MODE, secretKeySpec);
			byte[] decrypted = cipher.doFinal( data);
			return decrypted;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalStateException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
			return null;
		} catch (BadPaddingException e) {
			e.printStackTrace();
			return null;
		}
	}
}
