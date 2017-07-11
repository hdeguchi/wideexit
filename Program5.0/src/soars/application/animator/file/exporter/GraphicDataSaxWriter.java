/*
 * 2005/07/01
 */
package soars.application.animator.file.exporter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.internal.ObjectManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The XML SAX writer for Animator graphics data.
 * @author kurata / SOARS project
 */
public class GraphicDataSaxWriter {

	/**
	 * Returns true if the specified Animator graphics data is saved to the specified file successfully.
	 * @param file the specified file
	 * @param rootDirectory
	 * @param objectManager
	 * @return if the specified Animator graphics data is saved to the specified file successfully
	 */
	public static boolean execute(File file, File rootDirectory, ObjectManager objectManager) {
		try {
			OutputStreamWriter outputStreamWriter
				= new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animator_graphic_data", new AttributesImpl());

			if ( !objectManager.write_graphic_data( rootDirectory, writer))
				return false;

			writer.endElement( null, null, "animator_graphic_data");

			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}
