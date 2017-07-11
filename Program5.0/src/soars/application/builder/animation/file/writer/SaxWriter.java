/*
 * 2005/05/16
 */
package soars.application.builder.animation.file.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.builder.animation.main.MainFrame;
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
			OutputStreamWriter outputStreamWriter
				= new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "animator_builder_data", new AttributesImpl());

			if ( !MainFrame.get_instance().write( writer))
				return false;

			writer.endElement( null, null, "animator_builder_data");

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
