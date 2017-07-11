/**
 * 
 */
package soars.application.manager.model.file;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.manager.model.main.Constant;
import soars.common.soars.tool.DocumentWriter;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class VisualShell {

	/**
	 * 
	 */
	private String _title = "";

	/**
	 * 
	 */
	private String _date = "";

	/**
	 * 
	 */
	private String _author = "";

	/**
	 * 
	 */
	private String _email = "";

	/**
	 * 
	 */
	private String _comment = "";

	/**
	 * 
	 */
	private File _parentDirectory = null;

	/**
	 * 
	 */
	private File _rootDirectory = null;

	/**
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 */
	public VisualShell(String title, String date, String author, String email, String comment) {
		super();
		_title = title;
		_date = date;
		_author = author;
		_email = email;
		_comment = comment;
		
	}

	/**
	 * @param file 
	 * @param mainPanel
	 * @return
	 */
	public boolean make(File file, Component mainPanel) {
		if ( !setup_work_directory())
			return false;

		if ( !write( new File( _rootDirectory, Constant._visualShellDataFilename), mainPanel)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !DocumentWriter.execute( _rootDirectory, new File( _rootDirectory, Constant._visualShellDataFilename))) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		if ( !ZipUtility.compress( file, _rootDirectory, _parentDirectory)) {
			FileUtility.delete( _parentDirectory, true);
			return false;
		}

		FileUtility.delete( _parentDirectory, true);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_work_directory() {
		if ( null != _parentDirectory)
			return true;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		_parentDirectory = parentDirectory;
		_rootDirectory = rootDirectory;

		return true;
	}

	/**
	 * @param file
	 * @param mainPanel
	 * @return
	 */
	private boolean write(File file, Component mainPanel) {
		try {
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			if ( null == outputStreamWriter)
				return false;

			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			writer.startElement( null, null, "visual_shell_data", new AttributesImpl());

			write_layer_data( writer, mainPanel);
			write_simulation_data( writer);
			write_log_data( writer);
			write_comment_data( writer);

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
	 * @param mainPanel
	 * @throws SAXException
	 */
	private void write_layer_data(Writer writer, Component mainPanel) throws SAXException {
		writer.startElement( null, null, "layer_data", new AttributesImpl());

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", "1");
		writer.startElement( null, null, "layer", attributesImpl);

		Point spotPosition = new Point();
		Dimension spotDimension = new Dimension();
		get_spot_position_and_dimension( spotPosition, spotDimension, mainPanel);

		Point rolePosition = new Point();
		get_role_position( rolePosition, spotDimension, mainPanel);

		write_global_spot( spotPosition, writer);
		write_global_role( rolePosition, writer);

		writer.endElement( null, null, "layer");

		writer.endElement( null, null, "layer_data");
	}

	/**
	 * @param spotPosition
	 * @param spotDimension
	 * @param mainPanel
	 */
	private void get_spot_position_and_dimension(Point spotPosition, Dimension spotDimension, Component mainPanel) {
		// TODO Auto-generated method stub
		Graphics2D graphics2D = ( Graphics2D)mainPanel.getGraphics();
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		String name = "Name : " + Constant._globalSpotName;
		String role = "Role : " + Constant._globalRoleName;
		int width = fontMetrics.stringWidth( name) + name.length();
		width = Math.max( width, fontMetrics.stringWidth( role) + role.length());
		int height = ( ( fontMetrics.getHeight() + 1) * 2);

		spotPosition.x = ( width > Constant._visualShellSpotIconWidth) ? ( ( width - Constant._visualShellSpotIconWidth) / 2) : 0;
		spotPosition.y = 0;

		spotDimension.width = width;
		spotDimension.height = ( Constant._visualShellSpotIconHeight + height);
	}

	/**
	 * @param rolePosition
	 * @param spotDimension
	 * @param mainPanel
	 */
	private void get_role_position(Point rolePosition, Dimension spotDimension, Component mainPanel) {
		// TODO Auto-generated method stub
		Graphics2D graphics2D = ( Graphics2D)mainPanel.getGraphics();
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		int width = fontMetrics.stringWidth( Constant._globalRoleName) + Constant._globalRoleName.length();

		rolePosition.x = ( width > Constant._visualShellSpotRoleIconWidth) ? ( ( width - Constant._visualShellSpotRoleIconWidth) / 2) : 0;
		rolePosition.y = ( spotDimension.height + Constant._visualShellRoleConnectionLength + 2 * Constant._visualShellRoleConnectionRadius);
	}

	/**
	 * @param spotPosition
	 * @param writer
	 * @throws SAXException
	 */
	private void write_global_spot(Point spotPosition, Writer writer) throws SAXException {
		// TODO Auto-generated method stub
		AttributesImpl attributesImpl = new AttributesImpl();
		//<spot id="0" name=Constant._globalSpotName global="true" x="40" y="0" image="" gis="" gis_coordinates_x="" gis_coordinates_y="" number="" initial_role=Constant._globalRoleName/>
		attributesImpl.addAttribute( null, null, "id", "", "0");
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( Constant._globalSpotName));
		attributesImpl.addAttribute( null, null, "global", "", "true");
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( spotPosition.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( spotPosition.y));
		attributesImpl.addAttribute( null, null, "image", "", "");
		attributesImpl.addAttribute( null, null, "gis", "", "");
		attributesImpl.addAttribute( null, null, "gis_coordinates_x", "", "");
		attributesImpl.addAttribute( null, null, "gis_coordinates_y", "", "");
		attributesImpl.addAttribute( null, null, "number", "", "");
		attributesImpl.addAttribute( null, null, "initial_role", "", Writer.escapeAttributeCharData( Constant._globalRoleName));
		writer.writeElement( null, null, "spot", attributesImpl);
	}

	/**
	 * @param rolePosition
	 * @param writer
	 * @throws SAXException
	 */
	private void write_global_role(Point rolePosition, Writer writer) throws SAXException {
		// TODO Auto-generated method stub
		AttributesImpl attributesImpl = new AttributesImpl();
		//<spot_role id="1" name=Constant._globalRoleName global="true" x="22" y="81">
		attributesImpl.addAttribute( null, null, "id", "", "1");
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( Constant._globalRoleName));
		attributesImpl.addAttribute( null, null, "global", "", "true");
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( rolePosition.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( rolePosition.y));
		writer.startElement( null, null, "spot_role", attributesImpl);

		write_global_rule_data( writer);

		writer.endElement( null, null, "spot_role");
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_global_rule_data(Writer writer) throws SAXException {
		// TODO Auto-generated method stub
		AttributesImpl attributesImpl = new AttributesImpl();
		//<rule_data row="100" column="51"/>
		attributesImpl.addAttribute( null, null, "row", "", "100");
		attributesImpl.addAttribute( null, null, "column", "", "51");
		writer.writeElement( null, null, "rule_data", attributesImpl);
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_simulation_data(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "export_end_time", "", "true");
		attributesImpl.addAttribute( null, null, "random_seed", "", "");
		writer.startElement( null, null, "simulation_data", attributesImpl);

		write_simulation_data( "start", writer);
		write_simulation_data( "step", writer);
		write_simulation_data( "end", writer);
		write_simulation_data( "log_step", writer);

		writer.endElement( null, null, "simulation_data");
	}

	/**
	 * @param name
	 * @param writer
	 * @throws SAXException
	 */
	private void write_simulation_data(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "day", "", "0");
		attributesImpl.addAttribute( null, null, "hour", "", "00");
		attributesImpl.addAttribute( null, null, "minute", "", "00");
		writer.writeElement( null, null, name, attributesImpl);
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_log_data(Writer writer) throws SAXException {
		writer.startElement( null, null, "log_data", new AttributesImpl());

		writer.startElement( null, null, "agent_keyword_data", new AttributesImpl());

		write_log_data( "$Name", "false", writer);
		write_log_data( "$Role", "false", writer);
		write_log_data( "$Spot", "true", writer);

		writer.endElement( null, null, "agent_keyword_data");

		writer.endElement( null, null, "log_data");
	}

	/**
	 * @param name
	 * @param flag
	 * @param writer
	 * @throws SAXException
	 */
	private void write_log_data(String name, String flag, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));
		attributesImpl.addAttribute( null, null, "flag", "", flag);
		writer.writeElement( null, null, "agent_keyword", attributesImpl);
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_comment_data(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));
		attributesImpl.addAttribute( null, null, "date", "", Writer.escapeAttributeCharData( _date));
		attributesImpl.addAttribute( null, null, "author", "", Writer.escapeAttributeCharData( _author));
		attributesImpl.addAttribute( null, null, "email", "", Writer.escapeAttributeCharData( _email));
		writer.startElement( null, null, "comment_data", attributesImpl);

		writer.characters( _comment.toCharArray(), 0, _comment.length());

		writer.endElement( null, null, "comment_data");
	}
}
