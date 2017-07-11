/**
 * 
 */
package soars.application.visualshell.object.entity.base.object.class_variable;

import java.nio.IntBuffer;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.base.ObjectBase;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The class variable class
 * @author kurata / SOARS project
 */
public class ClassVariableObject extends ObjectBase {

	/**
	 * Jar filename.
	 */
	public String _jarFilename = "";

	/**
	 * Class name.
	 */
	public String _classname = "";

	/**
	 * Creates this object.
	 */
	public ClassVariableObject() {
		super("class variable");
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the specified name
	 * @param jarFilename the specified jar filename
	 * @param classname the specified class name
	 */
	public ClassVariableObject(String name, String jarFilename, String classname) {
		super("class variable", name);
		_jarFilename = jarFilename;
		_classname = classname;
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the specified name
	 * @param jarFilename the specified jar filename
	 * @param classname the specified class name
	 * @param comment the specified comment
	 */
	public ClassVariableObject(String name, String jarFilename, String classname, String comment) {
		super("class variable", name, comment);
		_jarFilename = jarFilename;
		_classname = classname;
	}

	/**
	 * Creates this object with the spcified data.
	 * @param classVariableObject the spcified data
	 */
	public ClassVariableObject(ClassVariableObject classVariableObject) {
		super(classVariableObject);
		_jarFilename = classVariableObject._jarFilename;
		_classname = classVariableObject._classname;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#copy(soars.application.visualshell.object.entiy.base.object.base.ObjectBase)
	 */
	public void copy(ObjectBase objectBase) {
		super.copy(objectBase);
		ClassVariableObject classVariableObject = ( ClassVariableObject)objectBase;
		_jarFilename = classVariableObject._jarFilename;
		_classname = classVariableObject._classname;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object object) {
		if ( !( object instanceof ClassVariableObject))
			return false;

		if ( !super.equals( object))
			return false;

		ClassVariableObject classVariableObject = ( ClassVariableObject)object;

		return ( classVariableObject._jarFilename.equals( _jarFilename)
			&& classVariableObject._classname.equals( _classname));
	}

	/**
	 * @return
	 */
	public boolean is_exchange_algebra() {
		// TODO Auto-generated method stub
		return _classname.equals( Constant._exchangeAlgebraClassname);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#can_paste(soars.application.visualshell.object.entiy.base.EntityBase, soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(EntityBase entityBase, Layer drawObjects) {
		if ( !JarFileProperties.get_instance().contains( _jarFilename, _classname)) {
			String[] message = new String[] {
				( ( entityBase instanceof AgentObject) ? "Agent" : "Spot"),
				"name = " + entityBase._name,
				"Class variable : Not exist : " + "[" + _classname + ", " + _jarFilename + "]"
			};

			WarningManager.get_instance().add( message);

			return false;
		}

		if ( drawObjects.other_uses_this_class_variable_as_different_class( _name, _jarFilename, _classname, entityBase))
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_initial_data()
	 */
	public String get_initial_data() {
		String jarFilename = get_initial_data_jar_filename( _jarFilename);
		if ( null == jarFilename || jarFilename.equals( ""))
			return "";

		return ( ResourceManager.get_instance().get( "initial.data.class.variable")
			+ "\t" + _name + "\t" + jarFilename + "\t" + _classname + Constant._lineSeparator);
	}

	/**
	 * @param jarFilename
	 * @return
	 */
	private String get_initial_data_jar_filename(String jarFilename) {
		if ( jarFilename.equals( Constant._javaClasses))
			return jarFilename;

		for ( int i = 0; i < Constant._functionalObjectDirectories.length; ++i) {
			if ( jarFilename.startsWith( Constant._functionalObjectDirectories[ i] + "/"))
				return jarFilename.substring( ( Constant._functionalObjectDirectories[ i] + "/").length());
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#get_script(java.lang.String, java.nio.IntBuffer, soars.application.visualshell.object.experiment.InitialValueMap)
	 */
	public String get_script(String prefix, IntBuffer counter, InitialValueMap initialValueMap) {
		String script = ( "\t" + prefix + "setClass " + _name + "=" + _classname + " ; " + prefix + "logEquip " + _name);
		counter.put( 0, counter.get( 0) + 1);
		return script;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer)
	 */
	public void write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "jar_filename", "", get_jar_filename());
		attributesImpl.addAttribute( null, null, "classname", "", Writer.escapeAttributeCharData( _classname));
		write( writer, attributesImpl);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#write(soars.common.utility.xml.sax.Writer, java.lang.String)
	 */
	public void write(Writer writer, String number) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "number", "", Writer.escapeAttributeCharData( number));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "jar_filename", "", get_jar_filename());
		attributesImpl.addAttribute( null, null, "classname", "", Writer.escapeAttributeCharData( _classname));
		write( writer, attributesImpl);
	}

	/**
	 * @return
	 */
	private String get_jar_filename() {
		// TODO Auto-generated method stub
		if ( !_jarFilename.startsWith( Constant._functionalObjectDirectories[ 1]))
			return Writer.escapeAttributeCharData( _jarFilename);
		else
			return Writer.escapeAttributeCharData( Constant._userModuleDirectoryNameSymbol
				+ ( Constant._functionalObjectDirectories[ 1].endsWith( "/") ? "/" : "")
				+ _jarFilename.substring( Constant._functionalObjectDirectories[ 1].length()));
	
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print()
	 */
	public void print() {
		System.out.println( "\t" + _name + ", " + _jarFilename + ", " + _classname);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entiy.base.object.base.ObjectBase#print(java.util.Vector)
	 */
	public void print(Vector<int[]> indices) {
		if ( indices.isEmpty()) {
			System.out.println( "\t" + _name + ", " + _jarFilename + ", " + _classname + ", " + _comment);
			return;
		}

		for ( int i = 0; i < indices.size(); ++i) {
			int[] range = ( int[])indices.get( i);
			System.out.println( "\t" + _name + ", " + _jarFilename + ", " + _classname + ", " + range[ 0] + "-" + range[ 1] + ", " + _comment);
		}
	}
}
