/**
 * 
 */
package soars.common.utility.tool.mime.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeUtility;

/**
 * @author kurata
 *
 */
public class Base64 {

	/**
	 * @param data
	 * @return
	 */
	public static String encode(byte[] data) {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		OutputStream outputStream;
		try {
			outputStream = MimeUtility.encode( byteArrayOutputStream, "base64");
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
		try {
			outputStream.write(data);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		try {
			return byteArrayOutputStream.toString( "iso-8859-1");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param text
	 * @return
	 */
	public static byte[] decode(String text) {
		InputStream inputStream;
		try {
			inputStream = MimeUtility.decode( new ByteArrayInputStream( text.getBytes()), "base64");
		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
		byte[] buf = new byte[ 1024];
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		int len;
		try {
			while ( -1 != ( len = inputStream.read(buf)))
				byteArrayOutputStream.write(buf, 0, len);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return byteArrayOutputStream.toByteArray();
	}
}
