/**
 * 
 */
package soars.common.soars.module;

import java.io.File;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;

/**
 * @author kurata
 *
 */
public class Module {

	/**
	 * 
	 */
	public String _enable = "";

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _comment = "";

	/**
	 * 
	 */
	public String _filePath = "";

	/**
	 * @return
	 */
	public String getEnable() {
		if ( null == _enable)
			_enable = "";

		return _enable;
	}

	/**
	 * @param enable
	 */
	public void setEnable(String enable) {
		_enable = enable;
	}

	/**
	 * @return
	 */
	public String getName() {
		if ( null == _name)
			_name = "";

		return _name;
	}

	/**
	 * @param name
	 */
	public void setName(String name) {
		_name = name;
	}

	/**
	 * @return
	 */
	public String getComment() {
		if ( null == _comment)
			_comment = "";

		return _comment;
	}

	/**
	 * @param comment
	 */
	public void setComment(String comment) {
		_comment = comment;
	}
//
//	/**
//	 * @param filePath
//	 */
//	public Module(File filePath) {
//		super();
//		_filePath = filePath.getAbsolutePath();
//	}

	/**
	 * @return
	 */
	public File getFilePath() {
		return new File( _filePath);
	}

	/**
	 * @param filePath
	 */
	public void setFilePath(File filePath) {
		_filePath = filePath.getAbsolutePath();
	}

	/**
	 * @return
	 */
	public boolean is_correct() {
		return !_name.equals( "");
	}

	/**
	 * @return
	 */
	public String get_html() {
//		String text = "<html lang=\"ja\">\n";
//
//		text += "<head>\n";
//		text += "<META http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n";
//		text += "<title>Lydian7th Home Page</title>\n";
//		text += "</head>\n";

		String text = "<body>\n";
//		String text = "<body text=\"#000000\" link=\"#000080\" vlink=\"#800000\" alink=\"#800080\">\n";
//		text += "<body text=\"#000000\" link=\"#000080\" vlink=\"#800000\" alink=\"#800080\">\n";
//		text += "<body text=\"#000000\" link=\"#000080\" vlink=\"#800000\" alink=\"#800080\">\n";
		text += "<p><b><font color=\"#0000ff\" size=+1>[" + Tool.encodeHtmlFromString( _name) + "]</font></b></p>\n";
//		text += "<p><b><font size=+1>[" + Tool.encodeHtmlFromString( _name) + "]</font></b></p>\n";
		text += "<p><b><>" + Tool.encodeHtmlFromString( _comment) + "</font></b></p>\n";
		text += "</body>\n";

//		text += "</html>\n";

		return text;
	}

	/**
	 * @return
	 */
	public boolean write() {
		File file = new File( _filePath);

		setName( _name);
		setComment( _comment);

		String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		text += "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\">\n\n";

		text += "<beans>\n";

		text += "\t<bean id=\"" + CommonConstant._moduleSpringID + "\" class=\""+ CommonConstant._moduleClass + "\">\n";

		text += "\t\t<property name=\"enable\">\n";
		text += "\t\t\t<value>" + _enable + "</value>\n";
		text += "\t\t</property>\n";
		
		text += "\t\t<property name=\"name\">\n";
		text += "\t\t\t<value>" + Tool.encodeXmlFromString( _name) + "</value>\n";
		text += "\t\t</property>\n";

		text += "\t\t<property name=\"comment\">\n";
		text += "\t\t\t<value>" + Tool.encodeXmlFromString( _comment) + "</value>\n";
		text += "\t\t</property>\n";
		
		text += "\t</bean>\n";

		text += "</beans>\n";

		return FileUtility.write_text_to_file( file, text, "UTF-8");
	}

	/**
	 * @param file
	 * @return
	 */
	static public Module get_module(File file) {
		try {
//			Log log = LogFactory.getLog( XmlBeanFactory.class);
			Resource resource = new FileSystemResource( file);
			XmlBeanFactory xmlBeanFactory = new XmlBeanFactory( resource);
			Module module = ( Module)xmlBeanFactory.getBean( CommonConstant._moduleSpringID, Module.class);
			if ( null == module)
				return null;

			if ( !module.is_correct())
				return null;

			module.setFilePath( file);

			return module;
		} catch (BeansException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
		return getEnable().equals( "true");
	}
}
