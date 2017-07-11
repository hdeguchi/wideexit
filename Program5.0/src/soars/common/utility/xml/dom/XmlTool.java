/*
 * 2004/05/26
 */
package soars.common.utility.xml.dom;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.xpath.XPathAPI;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

/**
 * @author kurata
 */
public class XmlTool {

	/**
	 * @param node
	 * @param name
	 * @return
	 */
	public static Node get_node(Node node, String name) {
		try {
			Node childNode = XPathAPI.selectSingleNode( node, name);
			return childNode;
		} catch (TransformerException e) {
			//e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param node
	 * @param name
	 * @return
	 */
	public static NodeList get_node_list(Node node, String name) {
		try {
			NodeList nodeList = XPathAPI.selectNodeList( node, name);
			return nodeList;
		} catch (TransformerException e) {
			//e.printStackTrace();
			return null;
		}
	}

	/**
	 * @param node
	 * @param name
	 * @return
	 */
	public static String get_attribute(Node node, String name) {
		NamedNodeMap namedNodeMap = node.getAttributes();
		if ( null == namedNodeMap)
			return null;

		Node childNode = namedNodeMap.getNamedItem( name);
		if ( null == childNode)
			return null;

		return childNode.getNodeValue();
	}

	/**
	 * @param node
	 * @return
	 */
	public static String get_node_value(Node node) {
		NodeList nodeList = node.getChildNodes();
		if ( null == nodeList)
			return null;

		int n = nodeList.getLength();
		if ( 1 != nodeList.getLength())
			return null;

		return nodeList.item( 0).getNodeValue();
	}

	/**
	 * @param document
	 * @param element
	 * @param name
	 * @param value
	 */
	public static void set_attribute(Document document, Element element, String name, String value) {
		element.setAttributeNode( create_attribute( document, name, value));
	}

	/**
	 * @param document
	 * @param name
	 * @param value
	 * @return
	 */
	public static Attr create_attribute(Document document, String name, String value) {
		Attr attribute = document.createAttribute( name);
		attribute.setValue( value);
		return attribute;
	}

	/**
	 * @param node_name
	 * @param document
	 * @param parent
	 * @return
	 */
	public static Element create_and_append_node(String node_name, Document document, Element parent) {
		Element element = document.createElement( node_name);
		parent.appendChild( element);
		return element;
	}

	/**
	 * @param node_name
	 * @param document
	 * @param variableName
	 * @param parent
	 * @return
	 */
	public static Element create_and_append_text_node(String node_name, Document document, String variableName, Element parent) {
		Element element = create_text_node( node_name, document, variableName);
		parent.appendChild( element);
		return element;
	}

	/**
	 * @param node_name
	 * @param document
	 * @param variableName
	 * @return
	 */
	public static Element create_text_node(String node_name, Document document, String variableName) {
		Element element = document.createElement( node_name);
		set_text( document, element, variableName);
		return element;
	}

	/**
	 * @param document
	 * @param element
	 * @param variableName
	 */
	public static void set_text(Document document, Element element, String variableName) {
		remove_text( element);
		Text text = document.createTextNode( variableName);
		element.appendChild( text);
	}

	/**
	 * @param element
	 * @return
	 */
	public static boolean remove_text(Element element) {
		NodeList nodeList = element.getChildNodes();
		if ( null == nodeList)
			return false;

		for ( int i = 0; i < nodeList.getLength(); ++i) {

			Node childNode = nodeList.item( i);
			if ( null == childNode)
				continue;

			if ( 0 == childNode.getNodeName().compareTo( "#text")) {
				element.removeChild( childNode);
				return true;
			}
		}

		return false;
	}

	/**
	 * @return
	 */
	public static Document create_document() {
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setValidating( false);
		
		DocumentBuilder documentBuilder;
		try {
			documentBuilder = documentBuilderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
		
		return documentBuilder.newDocument();
	}

	/**
	 * @param namespaceURI
	 * @param qualifiedName
	 * @param documentType
	 * @return
	 */
	public static Document create_document(String namespaceURI, String qualifiedName, DocumentType documentType) {
		Document document = create_document();
		if ( null == document)
			return null;

		DOMImplementation domImplementation = document.getImplementation();
		return domImplementation.createDocument( namespaceURI, qualifiedName, documentType);
//		DOMImplementationImpl domImplementationImpl = new DOMImplementationImpl();
//		return domImplementationImpl.createDocument( namespaceURI, qualifiedName, documentType);
	}

//	/**
//	 * @param document
//	 * @param outputFormat
//	 * @param file
//	 * @return
//	 */
//	public static boolean serialize(Document document, OutputFormat outputFormat, File file) {
//
//		FileOutputStream fileOutputStream;
//		try {
//			fileOutputStream = new FileOutputStream( file);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		return serialize( document, outputFormat, fileOutputStream);
//	}
//
//	/**
//	 * @param document
//	 * @param outputFormat
//	 * @param filename
//	 * @return
//	 */
//	public static boolean serialize(Document document, OutputFormat outputFormat, String filename) {
//
//		FileOutputStream fileOutputStream;
//		try {
//			fileOutputStream = new FileOutputStream( filename);
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		return serialize( document, outputFormat, fileOutputStream);
//	}
//
//	/**
//	 * @param document
//	 * @param outputFormat
//	 * @param fileOutputStream
//	 * @return
//	 */
//	private static boolean serialize(Document document, OutputFormat outputFormat, FileOutputStream fileOutputStream) {
//		try {
//			DOMSerializer domSerializer = new XMLSerializer( fileOutputStream, outputFormat).asDOMSerializer();
//			domSerializer.serialize( document);
//			fileOutputStream.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//			return false;
//		}
//		return true;
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @param name
//	 * @return
//	 */
//	public static String get_attribute(Document document, String[] tags, String name) {
//		if ( null == document)
//			return null;
//
//		if ( null == tags || 0 == tags.length)
//			return null;
//
//		Element element = get_element( document, tags);
//		if ( null == element)
//			return null;
//
//		return get_attribute( element, name);
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @return
//	 */
//	public static String get_node_value(Document document, String[] tags) {
//		if ( null == document)
//			return null;
//
//		if ( null == tags || 0 == tags.length)
//			return null;
//
//		Element element = get_element( document, tags);
//		if ( null == element)
//			return null;
//
//		return get_node_value( element);
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @return
//	 */
//	public static boolean remove_element(Document document, String[] tags) {
//		if ( null == document)
//			return false;
//
//		if ( null == tags || 0 == tags.length)
//			return false;
//
//		Element element = get_element( document, tags);
//		if ( null == element)
//			return false;
//
//		element = ( Element)element.getParentNode();
//		if ( null == element)
//			return false;
//
//		NodeList nodeList = element.getChildNodes();
//		if ( null == nodeList)
//			return false;
//
//		for ( int i = 0; i < nodeList.getLength(); ++i) {
//
//			Node child_node = nodeList.item( i);
//			if ( null == child_node)
//				continue;
//
//			if ( 0 == child_node.getNodeName().compareTo( tags[ tags.length - 1])) {
//				element.removeChild( child_node);
//				return true;
//			}
//		}
//
//		return true;
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @param name
//	 * @return
//	 */
//	public static boolean remove_attribute(Document document, String[] tags, String name) {
//		if ( null == document)
//			return false;
//
//		if ( null == tags || 0 == tags.length)
//			return false;
//
//		Element element = get_element( document, tags);
//		if ( null == element)
//			return false;
//
//		if ( !element.hasAttribute( name))
//			return false;
//
//		element.removeAttribute( name);
//		return true;
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @param value
//	 * @return
//	 */
//	public static boolean remove_node_value(Document document, String[] tags) {
//		if ( null == document)
//			return false;
//
//		if ( null == tags || 0 == tags.length)
//			return false;
//
//		Element element = get_element( document, tags);
//		if ( null == element)
//			return false;
//
//		return remove_text( element);
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @return
//	 */
//	public static Element get_element(Document document, String[] tags) {
//		if ( null == document)
//			return null;
//
//		if ( null == tags || 0 == tags.length)
//			return null;
//
//		Element root = document.getDocumentElement();
//		//if ( null == root || 0 != tags[ 0].compareTo( root.getNodeName()))
//		if ( null == root)
//			return null;
//
//		//if ( 1 == tags.length)
//		//	return root;
//
//		NodeList nodeList = document.getElementsByTagName( tags[ tags.length - 1]);
//		if ( null == nodeList || 0 == nodeList.getLength())
//			return null;
//
//		for ( int i = 0; i < nodeList.getLength(); ++i) {
//
//			boolean agreement = true;
//
//			Element element = ( Element)nodeList.item( i);
//			if ( null == element)
//				continue;
//
//			for ( int j = tags.length - 2; j >= 0; --j) {
//				element = ( Element)element.getParentNode();
//				if ( null == element) {
//					agreement = false;
//					break;
//				}
//
//				if ( 0 == tags[ j].compareTo( element.getNodeName()))
//					continue;
//
//				agreement = false;
//			}
//
//			if ( !agreement)
//				continue;
//			else {
//				//if ( 0 != element.getNodeName().compareTo( root.getNodeName()))
//				//if ( element != root)
//				//if ( !element.equals( root))
//				//	return null;
//
//				return ( Element)nodeList.item( i);
//			}
//		}
//
//		return null;
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @param name
//	 * @param value
//	 * @return
//	 */
//	public static boolean set_attribute(Document document, String[] tags, String name, String value) {
//		if ( null == document)
//			return false;
//
//		if ( null == tags || 0 == tags.length)
//			return false;
//
//		Element element = create_element( document, tags);
//		if ( null == element)
//			return false;
//
//		set_attribute( document, element, name, value);
//		return true;
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @param value
//	 * @return
//	 */
//	public static boolean set_node_value(Document document, String[] tags, String value) {
//		if ( null == document)
//			return false;
//
//		if ( null == tags || 0 == tags.length)
//			return false;
//
//		Element element = create_element( document, tags);
//		if ( null == element)
//			return false;
//
//		set_text( document, element, value);
//		return true;
//	}
//
//	/**
//	 * @param document
//	 * @param tags
//	 * @return
//	 */
//	public static Element create_element(Document document, String[] tags) {
//		if ( null == document)
//			return null;
//
//		if ( null == tags || 0 == tags.length)
//			return null;
//
//		Element element = get_element( document, tags);
//		if ( null == element) {
//			element = document.getDocumentElement();
//			for ( int i = 0; i < tags.length; ++i) {
//				NodeList node_list = element.getChildNodes();
//				boolean found = false;
//				if ( null != node_list) {
//					for ( int j = 0; j < node_list.getLength(); ++j) {
//						if ( 0 == tags[ i].compareTo( node_list.item( j).getNodeName())) {
//							element = ( Element)node_list.item( j);
//							found = true;
//							break;
//						}
//					}
//				}
//
//				if ( found)
//					continue;
//
//				element = create_and_append_node( tags[ i], document, element);
//				if ( null == element)
//					return null;
//			}
//		}
//
//		return element;
//	}
}
