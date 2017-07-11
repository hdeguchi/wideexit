/*
 * 2005/05/16
 */
package soars.application.simulator.file.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.simulator.main.MainFrame;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class SaxWriter {

	/**
	 * @param file
	 * @return
	 */
	public static boolean execute(File file) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			AttributesImpl attributesImpl = new AttributesImpl();
			//attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( MainFrame.get_instance().get_simulator_window_title()));
			attributesImpl.addAttribute( null, null, "time", "", Writer.escapeAttributeCharData( MainFrame.get_instance().get_time()));
			writer.startElement( null, null, "simulator_data", attributesImpl);

			if ( !MainFrame.get_instance().write( writer))
				return false;

			writer.endElement( null, null, "simulator_data");

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
	 * 
	 */
	public SaxWriter() {
		super();
	}
}
