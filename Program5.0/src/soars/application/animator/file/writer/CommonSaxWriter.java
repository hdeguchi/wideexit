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

import javax.swing.JDesktopPane;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.internal.InternalFrameRectangleMap;
import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.chart.ChartObjectMap;
import soars.application.animator.setting.common.CommonProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The XML SAX writer for Animator common data.
 * @author kurata / SOARS project
 */
public class CommonSaxWriter {

	/**
	 * Returns true if the specified Animator data is saved to the specified file successfully.
	 * @param internalFrameRectangleMap
	 * @param desktopPane
	 * @param file the specified file
	 * @param objectManager
	 * @return true if the specified Animator data is saved to the specified file successfully
	 */
	public static boolean execute(InternalFrameRectangleMap internalFrameRectangleMap, JDesktopPane desktopPane, File file, ObjectManager objectManager) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animation_common_data", new AttributesImpl());

			if ( !objectManager._scenarioManager._headerObject.write_common_header( writer))
				return false;

			if ( !write_common_properties( writer))
				return false;

			if ( !write_image_properties( writer))
				return false;

			if ( !ChartObjectMap.get_instance().write( internalFrameRectangleMap, desktopPane, writer))
				return false;

			writer.endElement( null, null, "animation_common_data");

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

		if ( !CommonProperty.get_instance().write( writer))
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
