/*
 * 2003/11/13
 */
package soars.common.utility.tool.encryption;

import java.security.*;

/**
 * @author kurata
 */
public class MD5 {

	/**
	 *	
	 */
	static protected char[] _hex ={
		'0', '1', '2', '3',
		'4', '5', '6', '7',
		'8', '9', 'a', 'b',
		'c', 'd', 'e', 'f'
	};

	/**
	 *	
	 */
	public static String toDigest( byte[] data) {

		MessageDigest md5 = null;
		byte[] digest = null;
		//String md5_str = "";
		StringBuffer buf = new StringBuffer();

		//
		try{
			md5 = MessageDigest.getInstance( "MD5");
		}
		catch( java.security.NoSuchAlgorithmException e ){
			System.err.println("MD5 Algorithm not supported...");
			return null;
		}

		//
		digest = md5.digest( data);
		for( int i=0; i<digest.length; i++ ){
			int n =  (int)(digest[i]);
			if( n<0 )	n = 256 + n;
			
			//System.out.println("original:"+ n );
			//System.out.println("upper:"+ (n/16) );
			//System.out.println("lower:"+ (n%16) );
			
			buf.append( _hex[ (n/16) ] );
			buf.append( _hex[ (n%16) ] );
		}

		return new String( buf );
	}
}
