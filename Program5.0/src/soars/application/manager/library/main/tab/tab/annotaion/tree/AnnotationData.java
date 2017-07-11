/**
 * 
 */
package soars.application.manager.library.main.tab.tab.annotaion.tree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class AnnotationData {

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * 
	 */
	public String _type = "";

	/**
	 * 
	 */
	public String _en = "";

	/**
	 * 
	 */
	public String _ja = "";

	/**
	 * 
	 */
	public String _enComment = "";

	/**
	 * 
	 */
	public String _jaComment = "";

	/**
	 * @param node
	 */
	public AnnotationData(Node node) {
		super();

		_kind = node.getNodeName();

		String value = XmlTool.get_attribute( node, "name");
		if ( null != value)
			_name = value;

		value = XmlTool.get_attribute( node, "type");
		if ( null != value)
			_type = value;

		value = XmlTool.get_attribute( node, "en");
		if ( null != value)
			_en = value;

		value = XmlTool.get_attribute( node, "ja");
		if ( null != value)
			_ja = value;
	}

	/**
	 * @param document 
	 * @param node
	 * @return
	 */
	public boolean writeTo(Document document, Element node) {
		if ( _kind.equals( "class"))
			XmlTool.set_attribute( document, node, "name", _name);
		else if ( _kind.equals( "method"))
			XmlTool.set_attribute( document, node, "name", _name);
		else if ( _kind.equals( "return"))
			XmlTool.set_attribute( document, node, "type", _type);
		else if ( _kind.startsWith( "parameter"))
			XmlTool.set_attribute( document, node, "type", _type);
		else {
			if ( !_kind.equals( "jarfile_properties"))
				return false;
		}
			
		XmlTool.set_attribute( document, node, "en", _en);
		XmlTool.set_attribute( document, node, "ja", _ja);
		XmlTool.create_and_append_text_node( "en", document, _enComment, node);
		XmlTool.create_and_append_text_node( "ja", document, _jaComment, node);

		return true;
	}
}
