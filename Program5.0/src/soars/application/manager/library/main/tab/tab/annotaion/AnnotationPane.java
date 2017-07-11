/**
 * 
 */
package soars.application.manager.library.main.tab.tab.annotaion;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.manager.library.main.Constant;
import soars.application.manager.library.main.Environment;
import soars.application.manager.library.main.ResourceManager;
import soars.application.manager.library.main.tab.tab.annotaion.component.Components;
import soars.application.manager.library.main.tab.tab.annotaion.tree.AnnotationData;
import soars.application.manager.library.main.tab.tab.annotaion.tree.ClassTree;
import soars.application.manager.library.main.tab.tab.annotaion.warning.WarningDlg1;
import soars.common.soars.environment.BasicEnvironment;
import soars.common.soars.environment.CommonEnvironment;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.stream.StreamPumper;
import soars.common.utility.xml.dom.DomUtility;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class AnnotationPane extends JSplitPane implements IMessageCallback {

	/**
	 * 
	 */
	private String _dividerLocationKey = "";

	/**
	 * 
	 */
	private ClassTree _classTree = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * 
	 */
	private Map<String, Components> _componentsMap = new HashMap<String, Components>();

	/**
	 * 
	 */
	private Element _classElement = null;

	/**
	 * 
	 */
	private File _annotationFile = null;

	/**
	 * @param dividerLocationKey
	 * @param parent
	 * @param owner
	 * 
	 */
	public AnnotationPane(String dividerLocationKey, Frame owner, Component parent) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		_dividerLocationKey = dividerLocationKey;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		if ( !setup_classTree())
			return false;

		if ( !setup_components())
			return false;

		adjust();

		setDividerLocation( Integer.parseInt( Environment.get_instance().get( _dividerLocationKey, "100")));

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_classTree() {
		_classTree = new ClassTree( this, _owner, _parent);
		if ( !_classTree.setup( ))
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _classTree);

		setLeftComponent( scrollPane);
		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_components() {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new GridLayout( 2, 1));

		String defaultLanguage = get_default_language();
		if ( !setup_components( defaultLanguage, centerPanel))
			return false;

		if ( !setup_components( get_another_language( defaultLanguage), centerPanel))
			return false;

		panel.add( centerPanel);


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));
		SwingTool.insert_vertical_strut( southPanel, 5);
		panel.add( southPanel, "South");


		setRightComponent( panel);

		return true;
	}

	/**
	 * @return
	 */
	private String get_default_language() {
		String defaultLanguage = Locale.getDefault().getLanguage();
		return ( defaultLanguage.equals( "ja") ? "ja" : "en");
	}

	/**
	 * @param defaultLanguage
	 * @return
	 */
	private String get_another_language(String defaultLanguage) {
		return ( defaultLanguage.equals( "ja") ? "en" : "ja");
	}

	/**
	 * @param language
	 * @param parent
	 * @return
	 */
	private boolean setup_components(String language, JPanel parent) {
		Components components = new Components( language);
		if ( !components.create())
			return false;

		_componentsMap.put( language, components);
		parent.add( components);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( Components components:_componentsMap.values())
			width = components.get_max_width( width);
		for ( Components components:_componentsMap.values())
			components.adjust( width);
	}

	/**
	 * 
	 */
	public void optimize_divider_location() {
		setDividerLocation( 100);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}

	/**
	 * 
	 */
	public void set_property_to_environment_file() {
		Environment.get_instance().set( _dividerLocationKey, String.valueOf( getDividerLocation()));
	}

	/**
	 * @param annotationData
	 */
	public void readFrom(AnnotationData annotationData) {
		for ( Components components:_componentsMap.values())
			components.readFrom( annotationData);
	}

	/**
	 * @param annotationData
	 */
	public void writeTo(AnnotationData annotationData) {
		for ( Components components:_componentsMap.values())
			components.writeTo( annotationData);
	}

	/**
	 * 
	 */
	public void store() {
		// TODO Auto-generated method stub
		if ( null != _annotationFile && _annotationFile.exists())
			_classTree.writeTo( _annotationFile);
	}

	/**
	 * 
	 */
	private void clear_components() {
		for ( Components components:_componentsMap.values())
			components.clear();
	}

	/**
	 * @param file
	 * @param save
	 * @return
	 */
	public boolean update(File file, boolean save) {
		//System.out.println( save);
		return update_class_tree( file, save);
		// これをスレッドにしてはいけない→デッドロックが発生する
//		return MessageDlg.execute( _owner, ResourceManager.get_instance().get( "application.title"), true,
//			"on_update_class_tree", ResourceManager.get_instance().get( "file.open.show.message"),
//			new Object[] { file, Boolean.valueOf( save)}, this, _owner);
	}

	/**
	 * @param files
	 * @return
	 */
	public boolean update(File[] files) {
		WarningManager.get_instance().cleanup();

		boolean result = MessageDlg.execute( _owner, ResourceManager.get_instance().get( "application.title"), true,
			"on_update_annotaion_files",
			ResourceManager.get_instance().get( "dialog.cancel"),
			ResourceManager.get_instance().get( "updating.annotation.files.message"),
			files, this, _owner);

		if ( !WarningManager.get_instance().isEmpty()) {
			WarningDlg1 warningDlg1 = new WarningDlg1(
				_owner,
				ResourceManager.get_instance().get( "warning.dialog1.title"),
				ResourceManager.get_instance().get( "warning.dialog1.message1"),
				_parent);
			warningDlg1.do_modal();
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "on_update_class_tree"))
			return update_class_tree( ( File)objects[ 0], ( ( Boolean)objects[ 1]).booleanValue());
		else if ( id.equals( "on_update_annotaion_files"))
			return update_annotaion_files( ( File[])objects, messageDlg);
		return true;
	}

	/**
	 * @param file
	 * @param save
	 * @return
	 */
	public boolean update_class_tree(File file, boolean save) {
		if ( save && null != _annotationFile && _annotationFile.exists())
			// TODO 現在の状態をClassTreeへ保存、ClassTreeをファイルへ保存
			_classTree.writeTo( _annotationFile);

		_annotationFile = null;

		clear_components();

		if ( null == file || file.isDirectory() || !file.isFile() || !file.getName().toLowerCase().endsWith( ".jar"))
			_classTree.clear();
		else {
			File annotationFile = new File( file.getParent(), file.getName().substring( 0, file.getName().length() - ".jar".length()) + Constant._moduleAnnotationFileExtension);
			if ( annotationFile.exists() && annotationFile.isFile()) {
				if ( !load( file, annotationFile)) {
					_classTree.clear();
					return false;
				}
			} else {
				if ( !create( file, annotationFile)) {
					_classTree.clear();
					return false;
				}
			}
			_annotationFile = annotationFile;
		}
		return true;
	}

	/**
	 * @param file
	 * @param annotationFile
	 * @return
	 */
	private boolean load(File file, File annotationFile) {
		Document document = DomUtility.read( annotationFile);
		if ( null == document)
			return false;

		return _classTree.update( document, file);
	}

	/**
	 * @param file
	 * @param annotationFile
	 * @return
	 */
	private boolean create(File file, File annotationFile) {
		Document document = create( file, ( MessageDlg)null);
		if ( null == document)
			return false;

		if ( !DomUtility.write( document, annotationFile, "UTF-8", null))
			return false;

		return _classTree.update( document, file);
	}

	/**
	 * @param file
	 * @param messageDlg
	 * @return
	 */
	private Document create(File file, MessageDlg messageDlg) {
		String[] cmdarray = get_cmdarray( file);

		Process process;
		try {
			process = ( Process)Runtime.getRuntime().exec( cmdarray);
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		Document document = XmlTool.create_document( null, "jarfile_properties", null);
		if ( null == document)
			return null;

		Element root = document.getDocumentElement();
		if ( null == root)
			return null;

		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream()));
		try {
			String line = null;
			while ( true) {
				line = bufferedReader.readLine();
				if ( null == line)
					break;

				if ( !analyze( line, document, root)) {
					document = null;
					break;
				}

				if ( null != messageDlg && messageDlg._canceled) {
					process.destroy();
					try {
						process.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//document = null;
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			document = null;
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return document;
	}

	/**
	 * @param file
	 * @return
	 */
	private String[] get_cmdarray(File file) {
		File userModuleDirectory = BasicEnvironment.get_instance().get_projectSubFoler( Constant._userModuleDirectoryName);
		if ( null == userModuleDirectory)
			return null;

		String memorySize = CommonEnvironment.get_instance().get_memory_size();
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

		if ( !memorySize.equals( "0"))
			list.add( "-Xmx" + memorySize + "m");
		list.add( "-jar");
		list.add( Constant._jarfileAnalyzerJarFilename);
		list.add( file.getPath().replaceAll( "\\\\", "/"));
		list.add( Constant._systemModuleDirectory);
		list.add( userModuleDirectory.getAbsolutePath());

		return list.toArray( new String[ 0]);
	}

	/**
	 * @param line
	 * @param document
	 * @param root
	 * @return
	 */
	private boolean analyze(String line, Document document, Element root) {
		//System.out.println( line);

		if ( line.startsWith( "jarfile:") && !line.matches( "^jarfile:+[ ]*")) {
			_classElement = null;
			XmlTool.set_attribute( document, root, "en", "");
			XmlTool.set_attribute( document, root, "ja", "");
			XmlTool.create_and_append_text_node( "en", document, "", root);
			XmlTool.create_and_append_text_node( "ja", document, "", root);
			return true;
		} else if ( line.startsWith( "class:") && !line.matches( "^class:+[ ]*")) {
			if ( null == root)
				return false;

			String name = line.substring( "class:".length());
			_classElement = XmlTool.create_and_append_node( "class", document, root);
			if ( null == _classElement)
				return false;

			XmlTool.set_attribute( document, _classElement, "name", name);
			XmlTool.set_attribute( document, _classElement, "en", "");
			XmlTool.set_attribute( document, _classElement, "ja", "");
			XmlTool.create_and_append_text_node( "en", document, "", _classElement);
			XmlTool.create_and_append_text_node( "ja", document, "", _classElement);
		} else if ( line.startsWith( "method:") && !line.matches( "^method:+[ ]*")) {
			if ( null == _classElement)
				return false;

			String method = line.substring( "method:".length(), line.indexOf( "return_type:"));
			String returnType = line.substring( line.indexOf( "return_type:") + "return_type:".length(), line.indexOf( "parameter_type:"));
			String parameterType = line.substring( line.indexOf( "parameter_type:") + "parameter_type:".length());
			String[] parameterTypes = parameterType.equals( "") ? new String[] {} : parameterType.split( "\\\t");
			if ( ignore( method, returnType, parameterTypes))
				return true;

			Element methodElement = XmlTool.create_and_append_node( "method", document, _classElement);
			if ( null == methodElement)
				return false;

			XmlTool.set_attribute( document, methodElement, "name", method);
			XmlTool.set_attribute( document, methodElement, "en", "");
			XmlTool.set_attribute( document, methodElement, "ja", "");
			XmlTool.create_and_append_text_node( "en", document, "", methodElement);
			XmlTool.create_and_append_text_node( "ja", document, "", methodElement);


			if ( !returnType.equals( "void") && !returnType.equals( "")) {
				Element returnElement = XmlTool.create_and_append_node( "return", document, methodElement);
				if ( null == methodElement)
					return false;

				XmlTool.set_attribute( document, returnElement, "type", returnType);
				XmlTool.set_attribute( document, returnElement, "en", "");
				XmlTool.set_attribute( document, returnElement, "ja", "");
				XmlTool.create_and_append_text_node( "en", document, "", returnElement);
				XmlTool.create_and_append_text_node( "ja", document, "", returnElement);
			}


			for ( int i = 0; i < parameterTypes.length; ++i) {
				if ( parameterTypes[ i].equals( ""))
					return false;

				Element parameterElement = XmlTool.create_and_append_node( "parameter" + i, document, methodElement);
				if ( null == parameterElement)
					return false;

				XmlTool.set_attribute( document, parameterElement, "type", parameterTypes[ i]);
				XmlTool.set_attribute( document, parameterElement, "en", "");
				XmlTool.set_attribute( document, parameterElement, "ja", "");
				XmlTool.create_and_append_text_node( "en", document, "", parameterElement);
				XmlTool.create_and_append_text_node( "ja", document, "", parameterElement);
			}
		} else
			return false;

		return true;
	}

	/**
	 * @param method
	 * @param returnType
	 * @param parameterTypes
	 * @return
	 */
	private boolean ignore(String method, String returnType, String[] parameterTypes) {
		return ( ( method.equals( "equals") && returnType.equals( "boolean") && 1 == parameterTypes.length && parameterTypes[ 0].equals( "java.lang.Object"))
			|| ( method.equals( "getClass") && returnType.equals( "java.lang.Class") && 0 == parameterTypes.length)
			|| ( method.equals( "hashCode") && returnType.equals( "int") && 0 == parameterTypes.length)
			|| ( method.equals( "notify") && returnType.equals( "void") && 0 == parameterTypes.length)
			|| ( method.equals( "notifyAll") && returnType.equals( "void") && 0 == parameterTypes.length)
			|| ( method.equals( "toString") && returnType.equals( "java.lang.String") && 0 == parameterTypes.length)
			|| ( method.equals( "wait") && returnType.equals( "void") && 0 == parameterTypes.length)
			|| ( method.equals( "wait") && returnType.equals( "void") && 1 == parameterTypes.length && parameterTypes[ 0].equals( "long"))
			|| ( method.equals( "wait") && returnType.equals( "void") && 2 == parameterTypes.length && parameterTypes[ 0].equals( "long") && parameterTypes[ 1].equals( "int")));
	}

	/**
	 * @param files
	 * @param messageDlg
	 * @return
	 */
	public boolean update_annotaion_files(File[] files, MessageDlg messageDlg) {
		if ( null != _annotationFile && _annotationFile.exists())
			// TODO 現在の状態をClassTreeへ保存、ClassTreeをファイルへ保存
			_classTree.writeTo( _annotationFile);

		List<File> list = new ArrayList<File>();
		for ( File file:files)
			get_files( file, list);

		for ( File file:list) {
			if ( messageDlg._canceled)
				return true;

			if ( !update_annotation_file( file, 1 == files.length && null != files[ 0] && files[ 0].isFile() && files[ 0].getName().toLowerCase().endsWith( ".jar"), messageDlg))
				WarningManager.get_instance().add( new String[] {file.getAbsolutePath().replaceAll( "\\\\", "/")});
		}

		return true;
	}

	/**
	 * @param file
	 * @param list
	 */
	private void get_files(File file, List<File> list) {
		if ( null == file || !file.exists())
			return;

		if ( file.isFile() && file.getName().toLowerCase().endsWith( ".jar"))
			list.add( file);

		if ( file.isDirectory()) {
			File[] files = file.listFiles();
			if ( null == files)
				return;

			for ( File f:files)
				get_files( f, list);
		}
	}

	/**
	 * @param file
	 * @param selectOne
	 * @param messageDlg
	 * @return
	 */
	private boolean update_annotation_file(File file, boolean selectOne, MessageDlg messageDlg) {
		if ( null == file || file.isDirectory() || !file.isFile() || !file.getName().toLowerCase().endsWith( ".jar"))
			return true;
		else {
			File annotationFile = new File( file.getParent(), file.getName().substring( 0, file.getName().length() - ".jar".length()) + Constant._moduleAnnotationFileExtension);
			//System.out.println( annotationFile.getName());
			//messageDlg.update_message( annotationFile.getName());
			if ( annotationFile.exists() && annotationFile.isFile()) {
				if ( !update_annotation_file( file, annotationFile, selectOne, messageDlg))
					return false;

				if ( messageDlg._canceled)
					return true;

			} else {
				Document document = create( file, messageDlg);

				if ( messageDlg._canceled)
					return true;

				if ( null == document)
					return false;

				if ( !DomUtility.write( document, annotationFile, "UTF-8", null))
					return false;

				return ( selectOne ? _classTree.update( document, file) : true);
			}
			if ( selectOne)
				_annotationFile = annotationFile;
		}
		return true;
	}

	/**
	 * @param file
	 * @param annotationFile
	 * @param selectOne
	 * @param messageDlg
	 * @return
	 */
	private boolean update_annotation_file(File file, File annotationFile, boolean selectOne, MessageDlg messageDlg) {
		Document document1 = create( file, messageDlg);
		if ( null == document1)
			return false;

		Node root1 = document1.getDocumentElement();
		if ( null == root1)
			return false;

		Document document2 = DomUtility.read( annotationFile);
		if ( null == document2)
			return false;

		Node root2 = document2.getDocumentElement();
		if ( null == root2)
			return false;

		update_attribute( document1, root1, root2, "en");
		update_attribute( document1, root1, root2, "ja");
		update_text_node( document1, root1, root2, "en");
		update_text_node( document1, root1, root2, "ja");

		// document1を更新
		NodeList classNodeList = root1.getChildNodes();
		for ( int i = 0; i < classNodeList.getLength(); ++i) {
			Node classNode = classNodeList.item( i);
			if ( null == classNode)
				continue;

			if ( !classNode.getNodeName().equals( "class"))
				continue;

			String name = XmlTool.get_attribute( classNode, "name");
			if ( null == name)
				return false;

			Node node = get_class_node( root2, name);
			if ( null == node)
				continue;

			update_attribute( document1, classNode, node, "en");
			update_attribute( document1, classNode, node, "ja");
			update_text_node( document1, classNode, node, "en");
			update_text_node( document1, classNode, node, "ja");

			if ( !update_class_node( document1, classNode, node, messageDlg))
				return false;

			if ( messageDlg._canceled)
				return true;
		}

		if ( !DomUtility.write( document1, annotationFile, "UTF-8", null))
			return false;

		return ( selectOne ? _classTree.update( document1, file) : true);
	}

	/**
	 * @param parent
	 * @param name
	 * @return
	 */
	private Node get_class_node(Node parent, String name) {
		NodeList nodeList = parent.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node childNode = nodeList.item( i);
			if ( null == childNode)
				continue;

			String value = XmlTool.get_attribute( childNode, "name");
			if ( null == value)
				continue;

			if ( !name.equals( value))
				continue;

			return childNode;
		}
		return null;
	}

	/**
	 * @param document
	 * @param classNode1
	 * @param classNode2
	 * @param messageDlg
	 * @return
	 */
	private boolean update_class_node(Document document, Node classNode1, Node classNode2, MessageDlg messageDlg) {
		NodeList methodNodeList = classNode1.getChildNodes();
		for ( int i = 0; i < methodNodeList.getLength(); ++i) {
			Node methodNode = methodNodeList.item( i);
			if ( null == methodNode)
				continue;

			if ( !methodNode.getNodeName().equals( "method"))
				continue;

			String name = XmlTool.get_attribute( methodNode, "name");
			if ( null == name)
				return false;

			Node node = get_method_node( methodNode, classNode2, name);
			if ( null == node)
				continue;

			update_attribute( document, methodNode, node, "en");
			update_attribute( document, methodNode, node, "ja");
			update_text_node( document, methodNode, node, "en");
			update_text_node( document, methodNode, node, "ja");

			if ( !update_method_node( document, methodNode, node))
				return false;

			if ( messageDlg._canceled)
				return true;
		}
		return true;
	}

	/**
	 * @param methodNode
	 * @param parent
	 * @param name
	 * @return
	 */
	private Node get_method_node(Node methodNode, Node parent, String name) {
		NodeList nodeList = parent.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node childNode = nodeList.item( i);
			if ( null == childNode)
				continue;

			String value = XmlTool.get_attribute( childNode, "name");
			if ( null == value)
				continue;

			if ( !name.equals( value))
				continue;

			if ( !is_same( methodNode, childNode))
				continue;

			// childNodeがmethodNodeと全く同じ戻り値と引数を持っていたらchildNodeを返す
			return childNode;
		}
		return null;
	}

	/**
	 * @param methodNode1
	 * @param methodNode2
	 * @return
	 */
	private boolean is_same(Node methodNode1, Node methodNode2) {
		// methodNode1がmethodNode2と全く同じ戻り値と引数を持っていたらtrueを返す
		Node node1 = XmlTool.get_node( methodNode1, "return");
		Node node2 = XmlTool.get_node( methodNode2, "return");
		if ( ( null == node1 && null != node2) || ( null != node1 && null == node2))
			return false;

		if ( null != node1 && null != node2) {
			String type1 = XmlTool.get_attribute( node1, "type");
			if ( null == type1)
				return false;

			String type2 = XmlTool.get_attribute( node2, "type");
			if ( null == type2)
				return false;

			if ( !type1.equals( type2))
				return false;
		}

		int index = 0;
		while ( true) {
			node1 = XmlTool.get_node( methodNode1, "parameter" + String.valueOf( index));
			node2 = XmlTool.get_node( methodNode2, "parameter" + String.valueOf( index));
			if ( null == node1 && null == node2)
				return true;
			else if ( ( null == node1 && null != node2) || ( null != node1 && null == node2))
				return false;
			else {
				String type1 = XmlTool.get_attribute( node1, "type");
				if ( null == type1)
					return false;

				String type2 = XmlTool.get_attribute( node2, "type");
				if ( null == type2)
					return false;

				if ( !type1.equals( type2))
					return false;
			}
			++index;
		}
	}

	/**
	 * @param document
	 * @param methodNode1
	 * @param methodNode2
	 * @return
	 */
	private boolean update_method_node(Document document, Node methodNode1, Node methodNode2) {
		// methodNode2のannotation情報をmethodNode1へコピー
		NodeList nodeList = methodNode1.getChildNodes();
		for ( int i = 0; i < nodeList.getLength(); ++i) {
			Node node1 = nodeList.item( i);
			if ( null == node1)
				continue;

			if ( !node1.getNodeName().equals( "return") && !node1.getNodeName().startsWith( "parameter"))
				continue;

			Node node2 = XmlTool.get_node( methodNode2, node1.getNodeName());
			if ( null == node2)
				return false;

			update_attribute( document, node1, node2, "en");
			update_attribute( document, node1, node2, "ja");
			update_text_node( document, node1, node2, "en");
			update_text_node( document, node1, node2, "ja");
		}
		return true;
	}

	/**
	 * @param document
	 * @param node1
	 * @param node2
	 * @param name
	 */
	private void update_attribute(Document document, Node node1, Node node2, String name) {
		XmlTool.set_attribute( document, ( Element)node1, name, XmlTool.get_attribute( node2, name));
//		String attribute = XmlTool.get_attribute( node2, name);
//		if ( XmlTool.get_attribute( node1, name).equals( attribute))
//			return;
//
//		XmlTool.set_attribute( document, ( Element)node1, name, attribute);
	}

	/**
	 * @param document
	 * @param node1
	 * @param node2
	 * @param name
	 */
	private void update_text_node(Document document, Node node1, Node node2, String name) {
		XmlTool.create_and_append_text_node( name, document, XmlTool.get_node_value( XmlTool.get_node( node2, name)), ( Element)node1);
//		String value = XmlTool.get_node_value( XmlTool.get_node( node2, name));
//		if ( XmlTool.get_node_value( XmlTool.get_node( node1, name)).equals( value))
//			return;
//
//		XmlTool.create_and_append_text_node( name, document, value, ( Element)node1);
	}
}
