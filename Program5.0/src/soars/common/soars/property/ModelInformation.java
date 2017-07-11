/**
 * 
 */
package soars.common.soars.property;

import java.io.File;
import java.io.StringReader;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import soars.common.soars.constant.CommonConstant;
import soars.common.soars.tool.DocumentWriter;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class ModelInformation {

	/**
	 * 
	 */
	private File _file = null;

	/**
	 * 
	 */
	public String _title = "";

	/**
	 * 
	 */
	public String _date = "";

	/**
	 * 
	 */
	public String _author = "";

	/**
	 * 
	 */
	public String _email = "";

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * @param file
	 * @return
	 */
	public static ModelInformation get(File file) {
		ModelInformation modelInformation = new ModelInformation( file);
		if ( !modelInformation.load())
			return null;

		return modelInformation;
	}

	/**
	 * @param file
	 */
	private ModelInformation(File file) {
		super();
		_file = file;
	}

	/**
	 * @return
	 */
	private boolean load() {
		byte[] data = ZipUtility.get_binary( _file, CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._visualShellZipFileName);
		if ( null == data)
			return false;

		String text = ZipUtility.get_text( data, CommonConstant._visualShellRootDirectoryName + "/" + CommonConstant._visualShellDataFilename, "UTF8");
		if ( null == text)
			return false;

		if ( !read( text))
			return false;

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean read(String text) {
		Document document = DomUtility.read( new InputSource( new StringReader( text)));
		if ( null == document)
			return false;

		Element root = document.getDocumentElement();
		if ( null == root)
			return false;

		Node node = XmlTool.get_node( root, "comment_data");
		if ( null == node)
			return false;

		String value = XmlTool.get_attribute( node, "title");
		if ( null != value)
			_title = value;

		value = XmlTool.get_attribute( node, "date");
		if ( null != value)
			_date = value;

		value = XmlTool.get_attribute( node, "author");
		if ( null != value)
			_author = value;

		value = XmlTool.get_attribute( node, "email");
		if ( null != value)
			_email = value;

		value = XmlTool.get_node_value( node);
		if ( null != value)
			_comment = value;

		return true;
	}

	/**
	 * 
	 */
	public void cleanup() {
	}

	/**
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @return
	 */
	public boolean update(String title, String date, String author, String email, String comment) {
		if ( null == _file || !_file.exists() || !_file.isFile())
			return false;

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !SoarsCommonTool.decompress( _file, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File rootDirectory = new File( parentDirectory, CommonConstant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !update_visualshell_data_file( title, date, author, email, comment, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		if ( !SoarsCommonTool.update( _file, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		_title = title;
		_date = date;
		_author = author;
		_email = email;
		_comment = comment;

		return true;
	}

	/**
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @param rootDirectory
	 * @param parentDirectory
	 * @return
	 */
	private boolean update_visualshell_data_file(String title, String date, String author, String email, String comment, File rootDirectory, File parentDirectory) {
		File file = new File( rootDirectory, CommonConstant._visualShellDataFilename);
		if ( !file.exists() || !file.isFile())
			return false;

		Document document = DomUtility.read( file);
		if ( null == document)
			return false;

		Element root = document.getDocumentElement();
		if ( null == root)
			return false;

		Element element = ( Element)XmlTool.get_node( root, "comment_data");
		if ( null == element)
			return false;

		XmlTool.set_attribute( document, element, "title", title);
		XmlTool.set_attribute( document, element, "date", date);
		XmlTool.set_attribute( document, element, "author", author);
		XmlTool.set_attribute( document, element, "email", email);
		XmlTool.set_text( document, element, comment);

		if ( !DomUtility.write( document, file, "UTF-8", null))
			return false;

		if ( !DocumentWriter.execute( rootDirectory, file))
			return false;

		return true;
	}
}
