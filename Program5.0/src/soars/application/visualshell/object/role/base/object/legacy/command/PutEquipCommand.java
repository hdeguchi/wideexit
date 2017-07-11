/**
 * 
 */
package soars.application.visualshell.object.role.base.object.legacy.command;

import java.util.Vector;

import soars.application.visualshell.layer.ILayerManipulator;
import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.command.base.EquipCommand;

/**
 * @author kurata
 *
 */
public class PutEquipCommand extends EquipCommand {

	/**
	 * 
	 */
	public static String _reservedWord = "putEquip ";

	/**
	 * @param value
	 * @return
	 */
	public static int get_kind(String value) {
		return get_kind( value, LayerManager.get_instance());
	}

	/**
	 * @param value
	 * @param layerManipulator
	 * @return
	 */
	private static int get_kind(String value, ILayerManipulator layerManipulator) {
		String reserved_word = CommonRuleManipulator.get_reserved_word( value);
		if ( null == reserved_word)
			return -1;

		if ( !reserved_word.equals( _reservedWord))
			return -1;

		String[] elements = CommonRuleManipulator.get_elements( value);
		if ( null == elements || 2 != elements.length)
			return -1;

		for ( int i = 0; i < _kinds.length; ++i) {
			if ( CommonRuleManipulator.is_object( _kinds[ i], elements[ 0], layerManipulator)
				&& CommonRuleManipulator.is_object( _kinds[ i], elements[ 1], layerManipulator))
				return i;
		}

		return -1;
	}

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public PutEquipCommand(String kind, String type, String value) {
		super(kind, type, value);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_names()
	 */
	@Override
	protected String[] get_used_spot_names() {
		return new String[] { get_used_spot_name()};
	}

	/**
	 * @return
	 */
	private String get_used_spot_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		return CommonRuleManipulator.extract_spot_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_spot_variable_names(soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String[] get_used_spot_variable_names(Role role) {
		String[] spotVariableNames = new String[] { get_used_spot_variable_name(), null, null};

		String[] names = get_used_object_names( 7);
		if ( null != names)
			System.arraycopy( names, 0, spotVariableNames, 1, 2);

		return spotVariableNames;
	}

	/**
	 * @return
	 */
	private String get_used_spot_variable_name() {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		return CommonRuleManipulator.get_spot_variable_name2( elements[ 0]);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_probability_names()
	 */
	@Override
	protected String[] get_used_probability_names() {
		return get_used_object_names( 0);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_collection_names()
	 */
	@Override
	protected String[] get_used_collection_names() {
		return get_used_object_names( 1);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_list_names()
	 */
	@Override
	protected String[] get_used_list_names() {
		return get_used_object_names( 2);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_map_names()
	 */
	@Override
	protected String[] get_used_map_names() {
		return get_used_object_names( 3);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_keyword_names()
	 */
	@Override
	protected String[] get_used_keyword_names() {
		return get_used_object_names( 4);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_number_object_names()
	 */
	@Override
	protected String[] get_used_number_object_names() {
		return get_used_object_names( 5);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_used_time_variable_names()
	 */
	@Override
	protected String[] get_used_time_variable_names() {
		return get_used_object_names( 6);
	}

	/**
	 * @param kind
	 * @return
	 */
	private String[] get_used_object_names(int kind) {
		if ( get_kind( _value) != kind)
			return null;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return null;

		return new String[] { elements[ 0], elements[ 1]};
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges,String newHeadName, Vector<String[]> newRanges) {
		int kind = get_kind( _value);
		if ( 0 > kind)
			return false;

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		elements[ 0] = CommonRuleManipulator.update_spot_name2( elements[ 0], newName, originalName, headName, ranges, newHeadName, newRanges);
		if ( null == elements[ 0])
			return false;

		_value = _reservedWord;
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_spot_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_spot_variable_name(String name, String newName, String type, Role role) {
		boolean result1 = update_object_name( name, newName, type);

		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return result1;

		elements[ 0] = CommonRuleManipulator.update_spot_variable_name2( elements[ 0], name, newName, type);
		if ( null == elements[ 0])
			return result1;

		_value = _reservedWord;
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_probability_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_probability_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_collection_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_collection_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_list_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_list_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_map_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_map_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_keyword_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_keyword_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_number_object_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_number_object_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#update_time_variable_name(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected boolean update_time_variable_name(String name, String newName, String type, Role role) {
		return update_object_name( name, newName, type);
	}

	/**
	 * @param name
	 * @param newName
	 * @param type
	 * @return
	 */
	private boolean update_object_name(String name, String newName, String type) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		String element0 = CommonRuleManipulator.update_object_name( elements[ 0], name, newName, type);
		String element1 = CommonRuleManipulator.update_object_name( elements[ 1], name, newName, type);

		if ( null == element0 && null == element1)
			return false;

		elements[ 0] = ( ( null == element0) ? elements[ 0] : element0);
		elements[ 1] = ( ( null == element1) ? elements[ 1] : element1);

		_value = _reservedWord;
		for ( int i = 0; i < elements.length; ++i)
			_value += ( ( ( 0 == i) ? "" : "=") + elements[ i]);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#can_paste(soars.application.visualshell.object.role.base.Role, soars.application.visualshell.layer.Layer)
	 */
	@Override
	protected boolean can_paste(Role role, Layer drawObjects) {
		int kind = get_kind( _value, drawObjects);
		if ( 0 > kind)
			return false;

		if ( !can_paste_spot_name( kind, drawObjects))
			return false;

		return can_paste_object_names( kind, drawObjects);
	}

	/**
	 * @param kind
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_spot_name(int kind, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		return CommonRuleManipulator.can_paste_spot_and_spot_variable_name2( elements[ 0], drawObjects);
	}

	/**
	 * @param type
	 * @param drawObjects
	 * @return
	 */
	private boolean can_paste_object_names(int kind, Layer drawObjects) {
		String[] elements = CommonRuleManipulator.get_elements( _value);
		if ( null == elements || 2 != elements.length)
			return false;

		return ( CommonRuleManipulator.can_paste_object( _kinds[ kind], elements[ 0], drawObjects)
			&& CommonRuleManipulator.can_paste_object( _kinds[ kind], elements[ 1], drawObjects));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.base.Rule#get_script(java.lang.String, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	protected String get_script(String value, Role role) {
		String[] elements1 = CommonRuleManipulator.split( value);
		if ( null == elements1)
			return "";

		String[] elements2 = elements1[ 1].split( "=");
		if ( null == elements2 || 2 != elements2.length)
			return "";

		String[] prefixAndObject1 = CommonRuleManipulator.get_prefix_and_object( elements2[ 1]);
		if ( !prefixAndObject1[ 0].equals( "") && !prefixAndObject1[ 0].equals( "<>"))
			return "";

		String[] prefixAndObject2 = CommonRuleManipulator.get_prefix_and_object( elements2[ 0]);
		if ( prefixAndObject2.equals( ""))
			return "";

		boolean keyword = ( CommonRuleManipulator.is_object( "keyword", elements2[ 0], LayerManager.get_instance())
			&& CommonRuleManipulator.is_object( "keyword", elements2[ 1], LayerManager.get_instance()));

		boolean timeVariable = ( CommonRuleManipulator.is_object( "time variable", elements2[ 0], LayerManager.get_instance())
			&& CommonRuleManipulator.is_object( "time variable", elements2[ 1], LayerManager.get_instance()));

		boolean spotVariable = ( CommonRuleManipulator.is_object( "spot variable", elements2[ 0], LayerManager.get_instance())
			&& CommonRuleManipulator.is_object( "spot variable", elements2[ 1], LayerManager.get_instance()));

		return get_script( prefixAndObject2[ 0], elements1[ 0], prefixAndObject2[ 1],
			( prefixAndObject1[ 0].equals( "") ? elements2[ 1] : prefixAndObject1[ 1]), prefixAndObject2[ 0], keyword, timeVariable, spotVariable);
	}
}
