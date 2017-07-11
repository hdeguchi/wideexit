/*
 * 2005/05/16
 */
package soars.application.visualshell.file.writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.comment.CommentManager;
import soars.application.visualshell.object.experiment.ExperimentManager;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.log.LogManager;
import soars.application.visualshell.object.scripts.OtherScriptsManager;
import soars.application.visualshell.object.simulation.SimulationManager;
import soars.application.visualshell.object.stage.StageManager;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The XML SAX writer for Visual Shell data.
 * @author kurata / SOARS project
 */
public class SaxWriter {

	/**
	 * 
	 */
	static public String _version = "2.0";

	/**
	 * Returns true if the specified Visual Shell data is saved to the specified file successfully.
	 * @param file the specified file
	 * @return true if the specified Visual Shell data is saved to the specified file successfully
	 */
	public static boolean execute(File file) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "version", "", _version);
			writer.startElement( null, null, "visual_shell_data", attributesImpl);

			if ( !StageManager.get_instance().write( writer))
				return false;

			if ( !LayerManager.get_instance().write( writer))
				return false;

			if ( !SimulationManager.get_instance().write( writer))
				return false;

			if ( !LogManager.get_instance().write( writer))
				return false;

			if ( !VisualShellExpressionManager.get_instance().write( writer))
				return false;

			if ( !OtherScriptsManager.get_instance().write( writer))
				return false;

			if ( !CommentManager.get_instance().write( writer))
				return false;

			if ( !ExperimentManager.get_instance().write( writer))
				return false;

			if ( !write_image_properties( writer))
				return false;

			writer.endElement( null, null, "visual_shell_data");

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
	private static boolean write_image_properties(Writer writer) throws SAXException {
		if ( ImagePropertyManager.get_instance().isEmpty())
			return true;

		writer.startElement( null, null, "image_data", new AttributesImpl());

		if ( !ImagePropertyManager.get_instance().write( writer))
			return false;

		writer.endElement( null, null, "image_data");

		return true;
	}

	/**
	 * Returns true if the specified Visual Shell data is saved to the specified file successfully.
	 * @param file the specified file
	 * @param imagefiles the specified image files
	 * @return true if the specified Visual Shell data is saved to the specified file successfully
	 */
	public static boolean execute(File file, File[] imagefiles) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "version", "", _version);
			writer.startElement( null, null, "visual_shell_data", attributesImpl);

			if ( !LayerManager.get_instance().write_selected_objects( writer))
				return false;

			if ( !VisualShellExpressionManager.get_instance().write( writer))
				return false;

			if ( !write_image_properties( imagefiles, writer))
				return false;

			writer.endElement( null, null, "visual_shell_data");

			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
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
	 * @param imagefiles the specified image files
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private static boolean write_image_properties(File[] imagefiles, Writer writer) throws SAXException {
		if ( ImagePropertyManager.get_instance().isEmpty())
			return true;

		writer.startElement( null, null, "image_data", new AttributesImpl());

		if ( !ImagePropertyManager.get_instance().write( imagefiles, writer))
			return false;

		writer.endElement( null, null, "image_data");

		return true;
	}
}
