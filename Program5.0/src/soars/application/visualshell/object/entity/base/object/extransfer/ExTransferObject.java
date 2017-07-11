/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.extransfer;

import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.List;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.entity.base.object.base.SimpleVariableObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.utility.tool.file.FileUtility;

/**
 * The file object class
 * @author kurata / SOARS project
 */
public class ExTransferObject extends SimpleVariableObject {

	/**
	 * Creates this object.
	 */
	public ExTransferObject() {
		super("extransfer");
	}

	/**
	 * Creates this object with the spcified name and initial value.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 */
	public ExTransferObject(String name, String initialValue) {
		super("extransfer", name, initialValue);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param name the specified name
	 * @param initialValue the specified initial value
	 * @param comment the specified comment
	 */
	public ExTransferObject(String name, String initialValue, String comment) {
		super("extransfer", name, initialValue, comment);
	}

	/**
	 * Creates this object with the spcified data.
	 * @param exTransferObject the spcified data
	 */
	public ExTransferObject(ExTransferObject exTransferObject) {
		super(exTransferObject);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof ExTransferObject))
			return false;

		return ( super.equals( object));
	}

//	/* (non-Javadoc)
//	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
//	 */
//	public String get_initial_data() {
//		return get_initial_data( ResourceManager.get( "initial.data.file"));
//	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String initialValue = ( ( null == initialValueMap) ? _initialValue : initialValueMap.get_initial_value( _initialValue));
		if ( null == initialValue || initialValue.equals( ""))
			return "";

		String script = ( "\t" + prefix + "setClass " + Constant._aadlFunctionsClassVariableName + "=" + Constant._aadlFunctionsClassname);
		script += ( " ; " + prefix + "logEquip " + Constant._aadlFunctionsClassVariableName);

		script += ( " ; " + prefix + "addParamString " + Constant._aadlFunctionsClassVariableName + "=" + Constant._aadlFunctionsDefaultCsvEncoding);
		script += ( " ; " + prefix + "invokeClass " + Constant._aadlFunctionsClassVariableName + "=" + Constant._aadlFunctionsSetDefaultCsvEncodingMethodName);

		script += ( " ; " + prefix + "setClass " + _name + "=" + Constant._exTransferClassname);
		script += ( " ; " + prefix + "logEquip " + _name);

		script += ( " ; " + prefix + "addParamString " + Constant._aadlFunctionsClassVariableName + "=" + Constant._reservedUserDataDirectory + initialValue);
		script += ( " ; " + prefix + "invokeClass " + _name + "=" + Constant._aadlFunctionsClassVariableName + "=" + Constant._aadlFunctionsNewExTransferFromCsvFileMethodName);

		counter.put( 0, counter.get( 0) + 1);

		return script;
	}

	/**
	 * @param file
	 * @return
	 */
	public boolean uses_this_file(File file) {
		if ( _initialValue.equals( "") || _initialValue.startsWith( "$"))
			return false;

		return _initialValue.startsWith( ( file.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/"));
	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public boolean move_file(File srcPath, File destPath) {
		if ( _initialValue.equals( "") || _initialValue.startsWith( "$"))
			return false;

		String srcValue = ( srcPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");
		String destValue = ( destPath.getAbsolutePath().substring( LayerManager.get_instance().get_user_data_directory().getAbsolutePath().length() + 1)).replaceAll( "\\\\", "/");

		if ( !_initialValue.startsWith( srcValue))
			return false;

		_initialValue = ( destValue + _initialValue.substring( srcValue.length()));
		return true;
	}

	/**
	 * @param files
	 * @return
	 */
	public boolean get(List<File> files) {
		// TODO Auto-generated method stub
		if ( _initialValue.equals( ""))
			return true;

		File file = new File( LayerManager.get_instance().get_user_data_directory(), _initialValue);
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
		if ( _initialValue.equals( "") || _initialValue.startsWith( "$"))
			return true;

		File root = LayerManager.get_instance().get_user_data_directory();
		if ( null == root)
			return false;

		File file = new File( root, _initialValue);
		if ( file.exists())
			return true;

		String[] values = FileUtility.get_directory_and_filename( _initialValue);
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
}
