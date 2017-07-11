/**
 * 
 */
package soars.common.utility.xml.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * @author kurata
 *
 */
public class DomUtility {

	/**
	 * @param filename
	 * @return
	 */
	public static Document read(String filename) {
		return read( new File( filename));
	}

	/**
	 * @param file
	 * @return
	 */
	public static Document read(File file) {
		if ( !file.exists() || !file.canRead())
			return null;

		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		FileInputStream fileInputStream;
		try {
			fileInputStream = new FileInputStream( file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		Document document;
		try {
			document = documentBuilder.parse( fileInputStream);
		} catch (SAXException e) {
			e.printStackTrace();
			try {
				fileInputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			try {
				fileInputStream.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return null;
		}

		try {
			fileInputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * @param inputSource
	 * @return
	 */
	public static Document read(InputSource inputSource) {
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		Document document;
		try {
			document = documentBuilder.parse( inputSource);
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return document;
	}

	/**
	 * @param document
	 * @param filename
	 * @param encoding
	 * @param eol
	 * @return
	 */
	public static boolean write(Document document, String filename, String encoding, String eol) {
		return write( document, new File( filename), encoding, eol);
	}

	/**
	 * @param document
	 * @param file
	 * @param encoding
	 * @param eol
	 * @return
	 */
	public static boolean write(Document document, File file, String encoding, String eol) {
		OutputStreamWriter outputStreamWriter;
		try {
			outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), encoding);
			if ( null == outputStreamWriter)
				return false;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		PrintWriter printWriter = new PrintWriter( outputStreamWriter);

		DOMImplementationLS domImplementationLS = ( DOMImplementationLS)document.getImplementation();
		//DOMImplementationLS domImplementationLS = ( DOMImplementationLS)new DOMImplementationImpl();

		LSSerializer lsSerializer = domImplementationLS.createLSSerializer();

		LSOutput lsOutput = domImplementationLS.createLSOutput();

		if ( null != encoding)
			lsOutput.setEncoding( encoding);

		lsOutput.setCharacterStream( printWriter);

		if ( null != eol)
			lsSerializer.setNewLine( eol);

		boolean result = lsSerializer.write( document.getDocumentElement(), lsOutput);
		try {
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
