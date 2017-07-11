/*
 * 2005/05/16
 */
package soars.application.animator.file.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.main.internal.WindowProperty;
import soars.common.utility.xml.sax.Writer;

/**
 * The XML SAX writer for Animator data.
 * @author kurata / SOARS project
 */
public class SaxWriter {

	/**
	 * Returns true if the specified Animator data is saved to the specified file successfully.
	 * @param windowProperty
	 * @param file the specified file
	 * @param objectManager 
	 * @return true if the specified Animator data is saved to the specified file successfully
	 */
	public static boolean execute(WindowProperty windowProperty, File file, ObjectManager objectManager) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animation_data", new AttributesImpl());

			if ( !objectManager.write( windowProperty, writer))
				return false;

			writer.endElement( null, null, "animation_data");

			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
