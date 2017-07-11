/**
 * 
 */
package soars.application.manager.model.main.panel.tree.property;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import soars.application.manager.model.main.Constant;
import soars.common.soars.property.ModelInformation;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.file.ZipUtility;
import soars.common.utility.tool.resource.Resource;

/**
 * @author kurata
 *
 */
public class NodeProperty {

	/**
	 * 
	 */
	public File _directory = null;

	/**
	 * 
	 */
	public String _title = null;

	/**
	 * 
	 */
	public BufferedImage _bufferedImage = null;

	/**
	 * 
	 */
	public TableSelection _tableSelection = new TableSelection();

	/**
	 * 
	 */
	public NodeProperty() {
		super();
	}

	/**
	 * 
	 */
	public void cleanup() {
		if ( null == _directory || !_directory.exists() || !_directory.isDirectory())
			return;

		FileUtility.delete( _directory, true);
		_directory = null;
	}

	/**
	 * @param file
	 * @return
	 */
	public ModelInformation get_model_information(File file) {
		return ModelInformation.get( file);
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean setup(File file) {
		_directory = SoarsCommonTool.make_parent_directory();
		if ( null == _directory)
			return false;

		if ( !decompress( file)) {
			FileUtility.delete( _directory, true);
			return false;
		}

		_bufferedImage = get_image( file);

		_title = get_title();

		return true;
	}

	/**
	 * @param file
	 * @return
	 */
	private boolean decompress(File file) {
		boolean exist = false;
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName)) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !ZipUtility.decompress( zipInputStream, new String[] { Constant._visualShellRootDirectoryName + "/doc/"}, _directory)) {
						return false;
					}
					exist = true;
					break;
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return exist;
	}

	/**
	 * @param file
	 * @return
	 */
	private BufferedImage get_image(File file) {
		BufferedImage bufferedImage = null;
		File imageFile = new File( _directory, Constant._visualShellRootDirectoryName + "/theme/image.png");
		try {
			ZipInputStream zipInputStream = new ZipInputStream( new FileInputStream( file));
			ZipEntry zipEntry = null;
			try {
				while ( null != ( zipEntry = zipInputStream.getNextEntry())) {
					if ( zipEntry.isDirectory()) {
						zipInputStream.closeEntry();
						continue;
					}
					if ( !zipEntry.getName().equals( Constant._soarsRootDirectoryName + "/" + Constant._visualShellZipFileName)) {
						zipInputStream.closeEntry();
						continue;
					}
					bufferedImage = ZipUtility.get_image( zipInputStream, Constant._visualShellRootDirectoryName + "/theme/image.png");
					break;
				}
			} finally {
				zipInputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return bufferedImage;
	}

	/**
	 * @return
	 */
	private String get_title() {
		File file = new File( _directory, Constant._visualShellRootDirectoryName + "/" + Constant._documentHtmlFilename);
		if ( !file.exists() || !file.isFile())
			return null;
//			return ResourceManager.get( "model.tree.no.title");

		String text = FileUtility.read_text_from_file( file, "UTF8");
		if ( null == text)
			return null;
//			return ResourceManager.get( "model.tree.no.title");

		String[] line = text.split( "\n");
		if ( null == line || 2 > line.length)
			return null;
//			return ResourceManager.get( "model.tree.no.title");

		if ( !line[ 1].startsWith( "<head><title>") || !line[ 1].endsWith( "</title>"))
			return null;
//			return ResourceManager.get( "model.tree.no.title");

		String title = line[ 1].substring( "<head><title>".length(), line[ 1].length() - "</title>".length());
		if ( null == title || title.equals( ""))
			return null;
//			return ResourceManager.get( "model.tree.no.title");

		return title;
	}

	/**
	 * @param modelInformation
	 * @param title
	 * @param date
	 * @param author
	 * @param email
	 * @param comment
	 * @return
	 */
	public boolean update_model_information(ModelInformation modelInformation, String title, String date, String author, String email, String comment) {
		if ( !modelInformation.update( title, date, author, email, comment))
			return false;

		_title = title;
		return true;
	}

	/**
	 * @param file
	 * @param newImagefile
	 * @return
	 */
	public boolean update_image(File file, File newImagefile) {
		if ( null == file)
			return false;

		BufferedImage bufferedImage = null;

		if ( null != newImagefile) {
			bufferedImage = Resource.load_image( newImagefile);
			if ( null == bufferedImage)
				return false;
		}

		File parentDirectory = SoarsCommonTool.make_parent_directory();
		if ( null == parentDirectory)
			return false;

		if ( !SoarsCommonTool.decompress( file, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File rootDirectory = new File( parentDirectory, Constant._visualShellRootDirectoryName);
		if ( !rootDirectory.exists() || !rootDirectory.isDirectory()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File themeDirectory = new File( rootDirectory, "theme");
		if ( !themeDirectory.exists() && !themeDirectory.mkdirs()) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		File imageFile = new File( themeDirectory, "image.png");
		if ( null != bufferedImage) {
			if ( !Resource.write_image( bufferedImage, "png", imageFile)) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}
		} else {
			if ( !imageFile.delete()) {
				FileUtility.delete( parentDirectory, true);
				return false;
			}
		}

		if ( !SoarsCommonTool.update( file, rootDirectory, parentDirectory)) {
			FileUtility.delete( parentDirectory, true);
			return false;
		}

		FileUtility.delete( parentDirectory, true);

		_bufferedImage = bufferedImage;

		return true;
	}

	/**
	 * @param file
	 * @param nodePropertyMap
	 */
	static public void update_nodePropertyMap(File file, Map<File, NodeProperty> nodePropertyMap) {
		if ( !file.isFile())
			return;

		NodeProperty nodeProperty = nodePropertyMap.get( file);
		if ( null != nodeProperty)
			return;

		nodeProperty = new NodeProperty();
		nodeProperty.setup( file);
		nodePropertyMap.put( file, nodeProperty);
	}

	/**
	 * @param file
	 * @param nodePropertyMap
	 * @param tableSelectionMap
	 */
	public static void update_nodePropertyMap(File file, Map<File, NodeProperty> nodePropertyMap, Map<File, TableSelection> tableSelectionMap) {
		if ( !file.isFile())
			return;

		TableSelection tableSelection = tableSelectionMap.get( file);

		NodeProperty nodeProperty = nodePropertyMap.get( file);
		if ( null != nodeProperty) {
			if ( null != tableSelection)
				nodeProperty._tableSelection = tableSelection;
			return;
		}

		nodeProperty = new NodeProperty();
		nodeProperty.setup( file);
		if ( null != tableSelection)
			nodeProperty._tableSelection = tableSelection;
		nodePropertyMap.put( file, nodeProperty);
	}

	/**
	 * @param file
	 * @param nodePropertyMap
	 */
	public static void cleanup_nodePropertyMap(File file, Map<File, NodeProperty> nodePropertyMap) {
		if ( !file.isFile())
			return;

		NodeProperty nodeProperty = nodePropertyMap.get( file);
		if ( null == nodeProperty)
			return;

		nodeProperty.cleanup();
		nodePropertyMap.remove( file);
	}

	/**
	 * @param nodePropertyMap
	 */
	public static void cleanup_nodePropertyMap(Map<File, NodeProperty> nodePropertyMap) {
		List<File> files = new ArrayList<File>();

		Iterator iterator = nodePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			File file = ( File)entry.getKey();
			NodeProperty nodeProperty = ( NodeProperty)entry.getValue();
			if ( file.exists() && file.isFile())
				continue;

			nodeProperty.cleanup();
			files.add( file);
		}

		for ( int i = 0; i < files.size(); ++i)
			nodePropertyMap.remove( files.get( i));
	}

	/**
	 * @param nodePropertyMap
	 */
	public static void cleanup_nodePropertyMap_all(Map<File, NodeProperty> nodePropertyMap) {
		Iterator iterator = nodePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			File file = ( File)entry.getKey();
			NodeProperty nodeProperty = ( NodeProperty)entry.getValue();
			nodeProperty.cleanup();
		}
		nodePropertyMap.clear();
	}

	/**
	 * @param nodePropertyMap
	 * @param tableSelectionMap
	 */
	public static void copy(Map<File, NodeProperty> nodePropertyMap, Map<File, TableSelection> tableSelectionMap) {
		Iterator iterator = nodePropertyMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			File file = ( File)entry.getKey();
			NodeProperty nodeProperty = ( NodeProperty)entry.getValue();
			tableSelectionMap.put( file, new TableSelection( nodeProperty._tableSelection));
		}
	}
}
