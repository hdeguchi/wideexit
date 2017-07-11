/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.initial_data_file;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.xml.sax.Writer;

/**
 * The file object class
 * @author kurata / SOARS project
 */
public class InitialDataFileObject extends ObjectBase {

	/**
	 * Creates this object.
	 */
	public InitialDataFileObject() {
		super("initial data file");
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 */
	public InitialDataFileObject(String name) {
		super("initial data file", name);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param comment the specified comment
	 */
	public InitialDataFileObject(String name, String comment) {
		super("initial data file", name, comment);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param fileObject the spcified data
	 */
	public InitialDataFileObject(InitialDataFileObject initialDataFileObject) {
		super(initialDataFileObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		// TODO Auto-generated method stub
		if ( !( object instanceof InitialDataFileObject))
			return false;

		return ( super.equals( object));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		// TODO Auto-generated method stub
//		String script = ( "\t" + prefix + "keyword " + _name);
//
//		String initial_value = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
//		if ( null != initial_value && !initial_value.equals( ""))
//			script += "=" + Constant._reserved_user_data_directory + initial_value;
//
//		counter.put( 0, counter.get( 0) + 1);
//
//		return script;
		return "";
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean uses_this_file(File file) {
		if ( _name.equals( "") /*|| _name.startsWith( "$")*/)
			return false;

		return _name.startsWith( ( file.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"));
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public boolean move_file(File srcPath, File destPath) {
		if ( _name.equals( "") /*|| _name.startsWith( "$")*/)
			return false;

		String srcValue = ( srcPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		String destValue = ( destPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");

		if ( !_name.startsWith( srcValue))
			return false;

		_name = ( destValue + _name.substring( srcValue.length()));
		return true;
	}

	/**
	 * @param files
	 * @return
	 */
	public boolean get(List<File> files) {
		// TODO Auto-generated method stub
		if ( _name.equals( ""))
			return true;

		File file = new File( LayerManager.get_instance().get_user_data_directory(), _name);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return false;

		if ( !files.contains( file))
			files.add( file);

		return true;
	}

	/**
	 * @return
	 */
	public boolean make() {
		if ( _name.equals( "") /*|| _name.startsWith( "$")*/)
			return true;

		File root = LayerManager.get_instance().get_user_data_directory();
		if ( null == root)
			return false;

		File file = new File( root, _name);
		if ( file.exists())
			return true;

		String[] values = FileUtility.get_directory_and_filename( _name);
		if ( null == values || 2 != values.length)
			return false;

		if ( !values[ 0].equals( "")) {
			File directory = new File( root.getAbsolutePath() + "/" + values[ 0]);
			if ( !directory.exists() && !directory.mkdirs())
				return false;
		}

		try {
			if ( !file.createNewFile())
				return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	public boolean is_correct() {
		// TODO Auto-generated method stub
		if ( _name.equals( ""))
			return true;

		File file = new File( LayerManager.get_instance().get_user_data_directory(), _name);
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return false;

		return InitialDataFileChecker.execute( _name, file);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer)
	 */
	public void write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		write( writer, attributesImpl);
	}
}
