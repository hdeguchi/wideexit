/*
 * 2005/05/16
 */
package soars.application.visualshell.object.gis.file.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.writer.SaxWriter;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.GisDataFrame;
import soars.common.utility.xml.sax.Writer;

/**
 * The XML SAX writer for Visual Shell data.
 * @author kurata / SOARS project
 */
public class GisSaxWriter {

	/**
	 * Returns true if the specified GIS data is saved to the specified file successfully.
	 * @param file the specified file
	 * @param gisDataManager
	 * @param gisDataFrame 
	 * @return true if the specified GIS data is saved to the specified file successfully
	 */
	public static boolean execute(File file, GisDataManager gisDataManager, GisDataFrame gisDataFrame) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "version", "", SaxWriter._version);
			writer.startElement( null, null, "gis_data", attributesImpl);

			if ( !gisDataFrame.write( gisDataManager, writer))
				return false;

			writer.endElement( null, null, "gis_data");

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
