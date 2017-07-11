/**
 * 
 */
package soars.application.visualshell.object.arbitrary;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.JOptionPane;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import bsh.ClassPathException;
import bsh.classpath.BshClassPath;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * Manages the java classes for the functional object.
 * @author kurata / SOARS project
 */
public class JavaClasses {

	/**
	 * 
	 */
	static private JavaClasses _javaClasses = null;

	/**
	 * 
	 */
	static private TreeMap<String, List<String>> _javaClassMap = new TreeMap<String, List<String>>();

	/**
	 * 
	 */
	private List<String> _classList;

	/**
	 * 
	 */
	private String _qualifiedName = "java_classes";

	/**
	 * 
	 */
	private Document _document = null;

	/**
	 * 
	 */
	private static final String _directory =
		System.getProperty( Constant._soarsProperty) + File.separator
			+ "program" + File.separator
			+ "visualshell" + File.separator
			+ "environment" + File.separator;

	/**
	 * 
	 */
	private static final String _filename = "java_classes.xml";

	/**
	 * 
	 */
	private static final String _java_classes_spring_id = "javaClasses";

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static JavaClasses get_instance() {
		if ( null == _javaClasses)
			System.exit( 1);

		return _javaClasses;
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @return true if this object is initialized successfully
	 */
	public static boolean initialize() {
		if ( !setup_javaClassMap())
			return false;

		return update();
	}

	/**
	 * Returns true if this object is updated successfully.
	 * @return true if this object is updated successfully
	 */
	public static boolean update() {
		if ( !read())
			return false;

		if ( null == _javaClasses) {
			_javaClasses = new JavaClasses();
			_javaClasses.setclassList( new ArrayList());
			if ( !_javaClasses.setup())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	private static boolean read() {
		File file = new File( _directory + _filename);
		if ( !file.exists() && !create_java_classes_xml_file( null))
			return true;

		if ( !file.isFile())
			return true;

		if ( !file.canRead())
			return true;

		Resource resource = new FileSystemResource( file);
		XmlBeanFactory xmlBeanFactory = new XmlBeanFactory( resource);
		_javaClasses = ( JavaClasses)xmlBeanFactory.getBean( _java_classes_spring_id, JavaClasses.class);
		if ( null == _javaClasses)
			return true;

		if ( !_javaClasses.setup())
			return false;

		return true;
	}

	/**
	 * Returns the array of the java classes.
	 * @return the array of the java classes
	 */
	public List<String> getclassList() {
		if ( null == _classList)
			_classList = new ArrayList<String>();

		return _classList;
	}

	/**
	 * Sets the new array of the java classes.
	 * @param classList the new array of the java classes
	 */
	public void setclassList(List<String> classList) {
		_classList = classList;
	}

	/**
	 * Returns true for appending the specified java class successfully.
	 * @param classname the name of the specified java class
	 * @param parent the parent node of the new java class node in the document tree(Document Object Model)
	 * @param document the document tree(Document Object Model)
	 * @return true for appending the specified java class successfully
	 */
	public boolean append(String classname, Node parent, Document document) {
		if ( !append( classname, ( Element)parent, document))
			return false;

		getclassList().add( classname);
		return create_java_classes_xml_file();
	}

	/**
	 * Returns true if this object is initialized successfully.
	 * @return true if this object is initialized successfully
	 */
	public boolean setup() {
		_document = XmlTool.create_document( null, _qualifiedName, null);
		if ( null == _document)
			return false;

		Element root = _document.getDocumentElement();
		if ( null == root)
			return false;

		Node node = XmlTool.create_and_append_node( "root", _document, root);
		if ( null == node)
			return false;

		XmlTool.set_attribute( _document, ( Element)node, "name", Constant._javaClasses);

		for ( int i = 0; i < getclassList().size(); ++i) {
			if ( !append( getclassList().get( i), ( Element)node, _document))
				return false;
		}

		//serialize();

		return true;
	}

	/**
	 * @param name
	 * @param parent
	 * @param document
	 * @return
	 */
	private boolean append(String name, Element parent, Document document) {
		Class cls = null;
		try {
			cls = ClassLoader.getSystemClassLoader().loadClass( name);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			return true;
		}

		Member[] constructors = null;
		try {
			constructors = cls.getConstructors();
		} catch (Throwable ex) {
			//System.out.println( "\t- Method error!");
			//System.out.println( "");
			constructors = null;
		}

		Member[] methods = null;
		try {
			methods = cls.getMethods();
		} catch (Throwable ex) {
			//System.out.println( "\t- Method error!");
			//System.out.println( "");
			methods = null;
		}

		List<Member> members = new ArrayList<Member>();

		if ( null != constructors && 0 < constructors.length) {
			for ( int i = 0; i < constructors.length; ++i) {
				if ( 0 <= constructors[ i].getName().indexOf( '$'))
					continue;

				members.add( constructors[ i]);
			}
		}

		if ( null != methods && 0 < methods.length) {
			for ( int i = 0; i < methods.length; ++i) {
				if ( 0 <= methods[ i].getName().indexOf( '$'))
					continue;

				members.add( methods[ i]);
			}
		}

		if ( members.isEmpty()) {
			//System.out.println( "\t- No method!");
			//System.out.println( "");
			return true;
		}

		Element element = XmlTool.create_and_append_node( "class", document, parent);
		if ( null == element)
			return false;

		XmlTool.set_attribute( document, element, "name", name);

		for ( int i = 0; i < members.size(); ++i) {
			if ( members.get( i) instanceof Constructor) {
				Constructor constructor = ( Constructor)members.get( i);
				append( constructor.getName(), null, constructor.getParameterTypes(), element, document);
			} else if ( members.get( i) instanceof Method) {
				Method method = ( Method)members.get( i);
				append( method.getName(), method.getReturnType(), method.getParameterTypes(), element, document);
			}
		}

		return true;
	}

	/**
	 * @param name
	 * @param return_type
	 * @param parameter_types
	 * @param parent
	 * @param document
	 * @return
	 */
	private boolean append(String name, Class return_type, Class[] parameter_types, Element parent, Document document) {
		Element element = XmlTool.create_and_append_node( "method", document, parent);
		if ( null == element)
			return false;

		XmlTool.set_attribute( document, element, "name", name);

		XmlTool.set_attribute( document, element, "return_type", ( ( null == return_type) ? "" : return_type.getName()));

		for ( int i = 0; i < parameter_types.length; ++i)
			XmlTool.set_attribute( document, element, "parameter_type" + i, parameter_types[ i].getName());

		return true;
	}

	/**
	 * Returns the root node of the document tree(Document Object Model).
	 * @return the root node of the document tree(Document Object Model)
	 */
	public Node get_root_node() {
		if ( null == _document)
			return null;

		return _document.getDocumentElement();
	}

	/**
	 * Returns the array of the jar file nodes in the document tree(Document Object Model).
	 * @return the array of the jar file nodes in the document tree(Document Object Model)
	 */
	public NodeList get_nodes() {
		if ( null == _document)
			return null;

		Element root = _document.getDocumentElement();
		if ( null == root)
			return null;

		return XmlTool.get_node_list( root, "root");
	}

	/**
	 * @return
	 */
	private static boolean setup_javaClassMap() {
		BshClassPath bshClassPath;
		try {
			bshClassPath = BshClassPath.getBootClassPath();
		} catch (ClassPathException e) {
			e.printStackTrace();
			return false;
		}

		Set set = bshClassPath.getPackagesSet();
		Iterator iterator = set.iterator();
		while ( iterator.hasNext())
			append( ( String)iterator.next(), bshClassPath);

		//print();

		return true;
	}

	/**
	 * @param package_name
	 * @param bshClassPath
	 */
	private static void append(String package_name, BshClassPath bshClassPath) {
		Set set = bshClassPath.getClassesForPackage( package_name);
		Iterator iterator = set.iterator();
		while ( iterator.hasNext()) {
			String classname = ( String)iterator.next();
			if ( 0 <= classname.indexOf( "$" ))
				continue;

			String[] words = classname.split( "\\.");
			if ( 0 == words.length)
				continue;

			List<String> list = _javaClassMap.get( words[ words.length - 1]);
			if ( null == list) {
				list = new ArrayList<String>();
				_javaClassMap.put( words[ words.length - 1], list);
			}

			list.add( classname);
		}
	}

	/**
	 * Returns the array of the java class names which contain the specified word.
	 * @param word the specified word
	 * @return the array of the java class names which contain the specified word
	 */
	public static String[] get_java_classes(String word) {
		if ( null == word || word.equals( ""))
			return null;

		List<String> result = new ArrayList<String>();
		Iterator iterator = _javaClassMap.keySet().iterator();
		while ( iterator.hasNext()) {
			String name = ( String)iterator.next();
			if ( !name.toLowerCase().startsWith( word.toLowerCase())) {
//				if ( 0 > word.compareTo( name))
//					break;
//				else
					continue;
			}

			if ( !name.matches( "^[A-Z].*"))
				continue;

			List<String> list = _javaClassMap.get( name);
			if ( null == list || list.isEmpty())
				continue;

			result.addAll( list);
		}

		String[] javaClasses = result.toArray( new String[ 0]);
		Arrays.sort( javaClasses);
		return javaClasses;
	}

	/**
	 * Returns true for storing the array of the java class names to the file.
	 * @param java_classes the array of the java class names
	 * @return true for storing the array of the java class names to the file
	 */
	public static boolean create_java_classes_xml_file(String[] java_classes) {
		if ( null == _javaClasses)
			return false;

		JavaClasses.get_instance().getclassList().clear();
		if ( null != java_classes) {
			for ( int i = 0; i < java_classes.length; ++i)
				JavaClasses.get_instance().getclassList().add( java_classes[ i]);
		}

		return JavaClasses.get_instance().create_java_classes_xml_file();
	}

	/**
	 * @return
	 */
	private boolean create_java_classes_xml_file() {
		File directory = new File( _directory);
		if ( !directory.exists())
			directory.mkdirs();

		try {
			File file = new File( _directory + _filename);
			OutputStreamWriter outputStreamWriter = new OutputStreamWriter( new FileOutputStream( file), "UTF-8");
			String text = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
			text += "<!DOCTYPE beans PUBLIC \"-//SPRING//DTD BEAN//EN\" \"http://www.springframework.org/dtd/spring-beans.dtd\">\n";
			text += "\n";
			text += "<beans>\n";
			text += "  <bean id=\"javaClasses\" class=\"" + getClass().getName() + "\">\n";
			text += "    <property name=\"classList\">\n";
			text += "      <list>\n";

			for ( int i = 0; i < getclassList().size(); ++i)
				text += "        <value>" + getclassList().get( i) + "</value>\n";

			text += "      </list>\n";
			text += "    </property>\n";
			text += "  </bean>\n";
			text += "</beans>\n";

			outputStreamWriter.write( text);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * Show the message for the java class appended newly.
	 * @param appendedJavaClassList the array of the java class names appended newly
	 */
	public static void show_message_dialog(List appendedJavaClassList) {
		if ( appendedJavaClassList.isEmpty())
			return;

		String message = "";
		for ( int i = 0; i < appendedJavaClassList.size(); ++i)
			message += ( "Java class : " + ( String)appendedJavaClassList.get( i) + "\n");

		JOptionPane.showMessageDialog(
			MainFrame.get_instance(),
			message + ResourceManager.get_instance().get( "file.open.java.class.appended.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Returns true for storing the document tree to the temporary file.
	 * @return true for storing the document tree to the temporary file
	 */
	public boolean serialize() {
		if ( null == _document)
			return false;

		String currentDirectoryName = System.getProperty( Constant._soarsHome);
		if ( null == currentDirectoryName)
			return false;

		String temporaryDirectoryName = SoarsCommonEnvironment.get_instance().get( SoarsCommonEnvironment._tmpKey, "");
		if ( temporaryDirectoryName.equals( ""))
			return false;

		File temporaryDirectory = new File( currentDirectoryName + "/" + temporaryDirectoryName /*+ "/.soars/program/visualshell/test"*/);
		if ( temporaryDirectory.exists() && !temporaryDirectory.isDirectory())
			return false;

		if ( !temporaryDirectory.exists() && !temporaryDirectory.mkdirs())
			return false;

		return DomUtility.write( _document, new File( temporaryDirectory, "java_class_names.xml"), "UTF-8", null);
	}

	/**
	 * 
	 */
	private static void print() {
		OutputStreamWriter outputStreamWriter;
		try {
			outputStreamWriter = new OutputStreamWriter( new FileOutputStream( new File( "java_class_names.txt")));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}

		Iterator iterator = _javaClassMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String name = ( String)entry.getKey();
			name += " : ";
			List<String> list = ( List)entry.getValue();
			for ( int i = 0; i < list.size(); ++i)
				name += ( ( ( 0 == i) ? "" : ", ") + list.get( i));
			//System.out.println( name);
			try {
				outputStreamWriter.write( name + "\n");
			} catch (IOException e1) {
				e1.printStackTrace();
				return;
			}
		}

		try {
			outputStreamWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			outputStreamWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
