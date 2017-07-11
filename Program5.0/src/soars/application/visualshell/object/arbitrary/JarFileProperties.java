/**
 * 
 */
package soars.application.visualshell.object.arbitrary;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.swing.JOptionPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.environment.SoarsCommonEnvironment;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.stream.StreamPumper;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * Manages the jar files for the functional object.
 * @author kurata / SOARS project
 */
public class JarFileProperties {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private JarFileProperties _jarFileProperties = null;

	/**
	 * 
	 */
	private String _directory = System.getProperty( Constant._soarsProperty) + File.separator
		+ "program" + File.separator
		+ "visualshell" + File.separator
		+ "environment" + File.separator;

	/**
	 * 
	 */
	private String _qualifiedName = "jarfile_properties";

	/**
	 * 
	 */
	private Document _document = null;

	/**
	 * 
	 */
	private Element _rootElement = null;

	/**
	 * 
	 */
	private Element _moduleElement = null;

	/**
	 * 
	 */
	private Element _jarFileElement = null;

	/**
	 * 
	 */
	private Element _classElement = null;

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( !JavaClasses.initialize())
				System.exit( 1);

			if ( null == _jarFileProperties) {
				_jarFileProperties = new JarFileProperties();
				if ( !_jarFileProperties.initialize())
					System.exit( 1);
			}
		}
	}

	/**
	 * Returns the instance of this class.
	 * @return the instance of this class
	 */
	public static JarFileProperties get_instance() {
		if ( null == _jarFileProperties)
			System.exit( 1);

		return _jarFileProperties;
	}

	/**
	 * @return
	 */
	private boolean initialize() {
		_document = XmlTool.create_document( null, _qualifiedName, null);
		if ( null == _document)
			return false;

		return parse();
	}

	/**
	 * @return
	 */
	private boolean parse() {
		String[] cmdarray = get_cmdarray();

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray);
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		Element root = _document.getDocumentElement();
		if ( null == root)
			return false;

		boolean result = true;
		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream()));
		Map<File, Element> map = new HashMap<File, Element>();
		try {
			String line = null;
			while ( true) {
				line = bufferedReader.readLine();
				if ( null == line)
					break;

				if ( !analyze( line, root, map)) {
					result = false;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result;
	}

	/**
	 * @return
	 */
	private String[] get_cmdarray() {
		String memory_size = CommonEnvironment.get_instance().get_memory_size();
		String osname = System.getProperty( "os.name");
		List< String>list = new ArrayList< String>();
		if ( 0 <= osname.indexOf( "Windows")) {
			list.add( Constant._windowsJava);
		} else if ( 0 <= osname.indexOf( "Mac")) {
			list.add( Constant.get_mac_java_command());
			list.add( "-Dfile.encoding=UTF-8");
		} else {
			list.add( Tool.get_java_command());
		}

//		list.add( "-Djava.endorsed.dirs=" + Constant._xerces_directory);
		if ( !memory_size.equals( "0"))
			list.add( "-Xmx" + memory_size + "m");
		list.add( "-jar");
		list.add( Constant._jarfilesAnalyzerJarFilename);

		for ( int i = 0; i < Constant._functionalObjectDirectories.length; ++i)
			list.add( Constant._functionalObjectDirectories[ i]);

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param line
	 * @param root
	 * @param map
	 * @return
	 */
	private boolean analyze(String line, Element root, Map<File, Element> map) {
		//System.out.println( line);
		if ( line.startsWith( "root:") && !line.matches( "^root:+[ ]*")) {
			String name = line.substring( "root:".length());
			_rootElement = XmlTool.create_and_append_node( "root", _document, root);
			if ( null == _rootElement)
				return false;

			XmlTool.set_attribute( _document, _rootElement, "name", name);
		} else if ( line.startsWith( "module:") && !line.matches( "^module:+[ ]*")) {
			if ( null == _rootElement)
				return false;

			String name = line.substring( "module:".length());
			_moduleElement = XmlTool.create_and_append_node( "module", _document, _rootElement);
			if ( null == _moduleElement)
				return false;

			XmlTool.set_attribute( _document, _moduleElement, "name", name);

			map.clear();

			if ( !name.equals( Constant._moduleSpringFilename))
				map.put( new File( name), _moduleElement);

		} else if ( line.startsWith( "folder:") && !line.matches( "^folder:+[ ]*")) {
			if ( null == _moduleElement)
				return false;

			String name = line.substring( "folder:".length());
			File folder = new File( name);
			Element parent = map.get( folder.getParentFile());
			Element element = XmlTool.create_and_append_node( "folder", _document, ( ( null != parent) ? parent : _moduleElement));
			if ( null == element)
				return false;

			XmlTool.set_attribute( _document, element, "name", name);

			map.put( folder, element);

		} else if ( line.startsWith( "jarfile:") && !line.matches( "^jarfile:+[ ]*")) {
			if ( null == _moduleElement)
				return false;

			String name = line.substring( "jarfile:".length());
			File file = new File( name);
			Element parent = map.get( file.getParentFile());
			_jarFileElement = XmlTool.create_and_append_node( "jarfile", _document, ( ( null != parent) ? parent : _moduleElement));
			if ( null == _jarFileElement)
				return false;

			XmlTool.set_attribute( _document, _jarFileElement, "name", name);
		} else if ( line.startsWith( "class:") && !line.matches( "^class:+[ ]*")) {
			if ( null == _jarFileElement)
				return false;

			String name = line.substring( "class:".length());
			_classElement = XmlTool.create_and_append_node( "class", _document, _jarFileElement);
			if ( null == _classElement)
				return false;

			XmlTool.set_attribute( _document, _classElement, "name", name);
		} else if ( line.startsWith( "method:") && !line.matches( "^method:+[ ]*")) {
			if ( null == _classElement)
				return false;

			String name = line.substring( "method:".length(), line.indexOf( "return_type:"));
			Element element = XmlTool.create_and_append_node( "method", _document, _classElement);
			if ( null == element)
				return false;

			XmlTool.set_attribute( _document, element, "name", name);

			String return_type = line.substring( line.indexOf( "return_type:") + "return_type:".length(), line.indexOf( "parameter_type:"));
			XmlTool.set_attribute( _document, element, "return_type", return_type);

			String parameter_type = line.substring( line.indexOf( "parameter_type:") + "parameter_type:".length());
			if ( !parameter_type.equals( "")) {
				String[] parameter_types = parameter_type.split( "\\\t");
				for ( int i = 0; i < parameter_types.length; ++i) {
					if ( parameter_types[ i].equals( ""))
						return false;

					XmlTool.set_attribute( _document, element, "parameter_type" + i, parameter_types[ i]);
				}
			}
		} else
			return false;

		return true;
	}

	/**
	 * Returns true for merging the document tree of the java classes into one of this class successfully.
	 * @return true for merging the document tree of the java classes into one of this class successfully
	 */
	public boolean merge() {
		Node root = get_root_node();
		if ( null == root)
			return false;

		NodeList nodeList = JavaClasses.get_instance().get_nodes();
		if ( null == nodeList)
			return false;

		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			root.appendChild( _document.importNode( node, true));
		}

		//serialize();

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
	 * Returns true if the document tree contains the specified data.
	 * @param jar_filename the specified jar file name
	 * @param classname the specified class name
	 * @param method the specified method name
	 * @param parameters the specified array of the argument type names
	 * @param returnValue the specified array of the return value type names
	 * @return true if the document tree contains the specified data
	 */
	public boolean contains(String jarFilename, String classname, String method, String[][] parameters, String[] returnValue) {
		Node node = get_jarfile_node( jarFilename);
		if ( null == node)
			return false;

		node = get_class_node( node, classname);
		if ( null == node)
			return false;

		node = get_method_node( node,
			( ( !method.equals( classname) && classname.endsWith( "." + method)) ? classname : method),
			parameters, returnValue);
		if ( null == node)
			return false;

		return true;
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param jar_filename the specified jar file name
	 * @param classname the specified class name
	 * @return true if the document tree contains the specified data
	 */
	public boolean contains(String jarFilename, String classname) {
		Node node = get_jarfile_node( jarFilename);
		if ( null == node)
			return false;

		node = get_class_node( node, classname);
		if ( null == node)
			return false;

		return true;
	}

	/**
	 * Returns the node which contains the specified jar file name in the document tree(Document Object Model).
	 * @param jar_filename the specified jar file name
	 * @return the node which contains the specified jar file name in the document tree(Document Object Model)
	 */
	public Node get_jarfile_node(String jarFilename) {
		Element root = ( Element)get_root_node();
		if ( null == root)
			return null;

		if ( jarFilename.equals( Constant._javaClasses))
			return XmlTool.get_node( root, "root[@name=\"" + Constant._javaClasses + "\"]");
		else {
			File jarfile = new File( jarFilename);
			if ( null == jarfile || !jarfile.exists() || !jarfile.isFile())
				return null;

			NodeList nodeList = XmlTool.get_node_list( root, "root");
			if ( null == nodeList)
				return null;

			for ( int i = 0; i < nodeList.getLength(); ++i) {
				Node node = nodeList.item( i);
				if ( null == node)
					continue;

				String name = XmlTool.get_attribute( node, "name");
				if ( null == name || name.equals( Constant._javaClasses))
					continue;

				File directory = new File( name);
				if ( null == directory || !directory.exists() || !directory.isDirectory())
					continue;

				if ( !FileUtility.is_parent( jarfile, directory))
					continue;

				return get_jarfile_node( node, jarFilename, jarfile);
			}
			return null;
		}
	}

	/**
	 * @param root
	 * @param jarFilename
	 * @param jarFile
	 * @return
	 */
	private Node get_jarfile_node(Node root, String jarFilename, File jarFile) {
		NodeList nodeList = XmlTool.get_node_list( root, "module");
		if ( null == nodeList)
			return null;

		Node default_node = null;
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			if ( name.equals( Constant._noDefinedModule)) {
				default_node = node;
				continue;
			}

			File modulefile = new File( name);
			if ( null == modulefile || !modulefile.exists() || !modulefile.isFile())
				continue;

			if ( !FileUtility.is_parent( jarFile, modulefile.getParentFile()))
				continue;

			return get_jarfile_node_recursively( node, jarFilename, jarFile);
		}

		if ( null == default_node)
			return null;

		return get_jarfile_node( default_node, jarFilename);
	}

	/**
	 * @param parent
	 * @param jarFilename
	 * @param jarFile
	 * @return
	 */
	private Node get_jarfile_node_recursively(Node parent, String jarFilename, File jarFile) {
		Node node = get_jarfile_node( parent, jarFilename);
		if ( null != node)
			return node;

		NodeList nodeList = parent.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			node = nodeList.item( i);
			if ( null == node || !node.getNodeName().equals( "folder"))
				continue;

			String name = XmlTool.get_attribute( node, "name");
			if ( null == name)
				continue;

			File folder = new File( name);
			if ( null == folder || !folder.exists() || !folder.isDirectory())
				continue;

			if ( !FileUtility.is_parent( jarFile, folder))
				continue;

			return get_jarfile_node_recursively( node, jarFilename, jarFile);
		}
		return null;
	}

	/**
	 * @param node
	 * @param jarfilename
	 * @return
	 */
	private Node get_jarfile_node(Node node, String jarfilename) {
		return XmlTool.get_node( node, "jarfile[@name=\"" + jarfilename + "\"]");
	}

	/**
	 * @param jarfileNode
	 * @param classname
	 * @return
	 */
	public Node get_class_node(Node jarfileNode, String classname) {
		return XmlTool.get_node( jarfileNode, "class[@name=\"" + classname + "\"]");
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param classNode the specified class name node
	 * @param methodname the specified method name
	 * @param parameters the specified array of the argument type names
	 * @param returnValue the specified array of the return value type names
	 * @return true if the document tree contains the specified data
	 */
	public Node get_method_node(Node classNode, String methodname, String[][] parameters, String[] returnValue) {
		NodeList nodeList = XmlTool.get_node_list( classNode, "method[@name=\"" + methodname + "\"]");
		if ( null == nodeList)
			return null;

		for ( int i = 0; i < nodeList.getLength(); ++i) {

			Node node = nodeList.item( i);
			if ( null == node)
				continue;

			String[] parameterTypes = get_parameter_types( node);
			if ( !same_parameter_types( parameterTypes, parameters))
				continue;

			String returnType = XmlTool.get_attribute( node, "return_type");
			if ( !same_return_type( returnType, returnValue))
				continue;

			return node;
		}

		return null;
	}

	/**
	 * @param node
	 * @return
	 */
	private String[] get_parameter_types(Node node) {
		List<String> parameterTypes = new ArrayList<String>();
		int index = 0;
		while ( true) {
			String parameterType = XmlTool.get_attribute( node, "parameter_type" + index);
			if ( null == parameterType)
				break;

			parameterTypes.add( parameterType);
			++index;
		}

		return parameterTypes.isEmpty() ? null : ( String[])parameterTypes.toArray( new String[ 0]);
	}

	/**
	 * @param parameterTypes
	 * @param parameters
	 * @return
	 */
	private boolean same_parameter_types(String[] parameterTypes, String[][] parameters) {
		if ( null == parameterTypes)
			return ( null == parameters || 0 == parameters.length);

		if ( null == parameters)
			return ( null == parameterTypes || 0 == parameterTypes.length);

		if ( parameterTypes.length != parameters.length)
			return false;

		for ( int i = 0; i < parameterTypes.length; ++i) {
			if ( !parameterTypes[ i].equals( parameters[ i][ 1]))
				return false;
		}

		return true;
	}

	/**
	 * @param returnType
	 * @param returnValue
	 * @return
	 */
	private boolean same_return_type(String returnType, String[] returnValue) {
		if ( null == returnType)
			return ( null == returnValue || returnValue[ 1].equals( "") || returnValue[ 1].equals( "void"));

		if ( null == returnValue)
			return ( null == returnType || returnType.equals( "") || returnType.equals( "void"));

		return returnType.equals( returnValue[ 1]);
	}

	/**
	 * Returns true for updating the java classes for the functional object.
	 * @return true for updating the java classes for the functional object
	 */
	public boolean update_java_classes() {
		if ( !remove_java_classes_node())
			return false;

		if ( !JavaClasses.update())
			return false;

		if ( !merge())
			return false;

		return true;
	}

	/**
	 * @return
	 */
	private boolean remove_java_classes_node() {
		Node root = get_root_node();
		if ( null == root)
			return false;

		Node node = XmlTool.get_node( root, "root[@name=\"" + Constant._javaClasses + "\"]");
		if ( null == node)
			return false;

		root.removeChild( node);

		return true;
	}

	/**
	 * Returns true if the document tree contains the specified data.
	 * @param jarFilename the specified jar file name
	 * @param classname the specified class name
	 * @param appendedJavaClassList the array of the java classes
	 * @return true if the document tree contains the specified data
	 */
	public boolean exist(String jarFilename, String classname, List<String> appendedJavaClassList) {
		if ( contains( jarFilename, classname))
			return true;

		if ( !jarFilename.equals( Constant._javaClasses))
			return false;
		else {
			// Javaのクラスが存在しているなら追加する
			// そうでなければ読み込みを続けるかどうか？確認する
			// 読み込み中止ならfalseを返す
			Node node = get_jarfile_node( Constant._javaClasses);
			if ( null == node)
				return ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					MainFrame.get_instance(),
					"Java class : " + classname + "\n"
						+ ResourceManager.get_instance().get( "file.open.java.class.not.exist.message") + "\n"
						+ ResourceManager.get_instance().get( "file.open.confirm.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.YES_NO_OPTION));

			if ( !JavaClasses.get_instance().append( classname, node, _document))
				return ( JOptionPane.NO_OPTION == JOptionPane.showConfirmDialog(
					MainFrame.get_instance(),
					"Java class : " + classname + "\n"
						+ ResourceManager.get_instance().get( "file.open.java.class.not.exist.message") + "\n"
						+ ResourceManager.get_instance().get( "file.open.confirm.message"),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.YES_NO_OPTION));

			appendedJavaClassList.add( classname);

			return true;
		}
	}

	/**
	 * @return
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

		return DomUtility.write( _document, new File( temporaryDirectory, "jarfile_properties.xml"), "UTF-8", null);
	}

	// TODO 以下要修正

	/**
	 * Returns true for appending the array of the jar file informations to the document tree(Document Object Model).
	 * @param jarFileList the array of the jar files
	 * @return true for appending the array of the jar file informations to the document tree(Document Object Model)
	 */
	public boolean append(List<File> jarFileList) {
		Element root = _document.getDocumentElement();
		if ( null == root)
			return false;

		List<File> newJarFileList = new ArrayList<File>();
		for ( int i = 0; i < jarFileList.size(); ++i) {
			File path = jarFileList.get( i);
			Node node = XmlTool.get_node( root, "jarfile[@name=\"" + path.getPath().replaceAll( "\\\\", "/") + "\"]");
			if ( null == node) {
				newJarFileList.add( path);
				continue;
			}

			root.removeChild( node);
			newJarFileList.add( path);
		}


		URLClassLoader urlClassLoader = load( newJarFileList);
		if ( null == urlClassLoader)
			return false;


		for ( int i = 0; i < newJarFileList.size(); ++i) {
			File path = ( File)newJarFileList.get( i);

			Node node = XmlTool.create_and_append_node( "jarfile", _document, root);
			if ( null == node)
				return false;

			XmlTool.set_attribute( _document, ( Element)node, "name", path.getPath().replaceAll( "\\\\", "/"));

			if ( !parse( ( Element)node, path, urlClassLoader))
				continue;
		}

		return true;
	}

	/**
	 * @param jarFileList
	 * @return
	 */
	private URLClassLoader load(List<File> jarFileList) {
		List<URL> urlList = new ArrayList<URL>();

		for ( int i = 0; i < jarFileList.size(); ++i) {
			File path = ( File)jarFileList.get( i);
			try {
				URL url = new URL( "jar:file:" + path.getPath() + "!/");
				urlList.add( url);
			} catch (MalformedURLException e) {
				//e.printStackTrace();
				return null;
			}
		}

		URL[] urls = ( URL[])urlList.toArray( new URL[ 0]);

		return new URLClassLoader( urls, ClassLoader.getSystemClassLoader().getParent());
	}

	/**
	 * @param parent
	 * @param path
	 * @param urlClassLoader
	 * @return
	 */
	private boolean parse(Element parent, File path, URLClassLoader urlClassLoader) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile( path.getPath());
		} catch (IOException e) {
			//e.printStackTrace();
			return false;
		}

		Enumeration enumeration = jarFile.entries();
		if ( null == enumeration)
			return false;

		while ( enumeration.hasMoreElements()) {
			ZipEntry zipEntry = ( ZipEntry)enumeration.nextElement();
			String name = zipEntry.getName();
			if ( !name.endsWith( ".class") || 0 <= name.indexOf( '$'))
				continue;

			name = name.substring( 0, name.length() - ".class".length());
			name = name.replaceAll( "/", ".");
			if ( !parse( name, parent, urlClassLoader))
				continue;
		}
		return true;
	}

	/**
	 * @param name
	 * @param parent
	 * @param urlClassLoader
	 * @return
	 */
	private boolean parse(String name, Element parent, URLClassLoader urlClassLoader) {
		Class cls = null;
		try {
			cls = urlClassLoader.loadClass( name);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			//System.out.println( "\t- ClassNotFoundException!");
			//System.out.println( "");
			return false;
		} catch (Throwable e) {
			//e.printStackTrace();
			//System.out.println( "\t- Class error!");
			//System.out.println( "");
			return false;
		}

		if ( null == cls) {
			//System.out.println( "\t- Class error!");
			//System.out.println( "");
			return false;
		}

		if ( cls.isInterface())
			return false;

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
			return false;
		}

		Element element = XmlTool.create_and_append_node( "class", _document, parent);
		if ( null == element)
			return false;

		XmlTool.set_attribute( _document, element, "name", name);

		for ( int i = 0; i < members.size(); ++i) {
			if ( members.get( i) instanceof Constructor) {
				Constructor constructor = ( Constructor)members.get( i);
				parse( constructor.getName(), null, constructor.getParameterTypes(), element);
			} else if ( members.get( i) instanceof Method) {
				Method method = ( Method)members.get( i);
				parse( method.getName(), method.getReturnType(), method.getParameterTypes(), element);
			}
		}

		return true;
	}

	/**
	 * @param name
	 * @param returnType
	 * @param parameterTypes
	 * @param parent
	 * @return
	 */
	private boolean parse(String name, Class returnType, Class[] parameterTypes, Element parent) {
		Element element = XmlTool.create_and_append_node( "method", _document, parent);
		if ( null == element)
			return false;

		XmlTool.set_attribute( _document, element, "name", name);

		XmlTool.set_attribute( _document, element, "return_type", ( ( null == returnType) ? "" : returnType.getName()));

		for ( int i = 0; i < parameterTypes.length; ++i)
			XmlTool.set_attribute( _document, element, "parameter_type" + i, parameterTypes[ i].getName());

		return true;
	}
}
