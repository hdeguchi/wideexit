/**
 * 
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

import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class CommonGraphicDataSaxWriter {

	/**
	 * Returns true if the specified Animator data is saved to the specified file successfully.
	 * @param file the specified file
	 * @return true if the specified Animator data is saved to the specified file successfully
	 */
	public static boolean execute(File file) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animator_common_graphic_data", new AttributesImpl());

			if ( !write_common_properties( writer))
				return false;

			if ( !write_image_properties( writer))
				return false;

			writer.endElement( null, null, "animator_common_graphic_data");

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

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private static boolean write_common_properties(Writer writer) throws SAXException {
		writer.startElement( null, null, "property", new AttributesImpl());

		if ( !CommonProperty.get_instance().write_graphic_data( writer))
			return false;

		writer.endElement( null, null, "property");

		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private static boolean write_image_properties(Writer writer) throws SAXException {
		if ( ImagePropertyManager.get_instance().isEmpty())
			return true;

		writer.startElement( null, null, "image", new AttributesImpl());

		if ( !ImagePropertyManager.get_instance().write( writer))
			return false;

		writer.endElement( null, null, "image");

		return true;
	}
}
