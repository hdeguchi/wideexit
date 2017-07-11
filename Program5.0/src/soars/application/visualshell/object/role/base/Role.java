/*
 * 2005/05/13
 */
package soars.application.visualshell.object.role.base;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.ImageObserver;
import java.nio.IntBuffer;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.importer.initial.role.RoleData;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.experiment.InitialValueMap;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectInObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectOutObject;
import soars.application.visualshell.object.role.base.object.RuleManager;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class Role extends DrawObject {

	/**
	 * 
	 */
	public RuleManager _ruleManager = null;

	/**
	 * 
	 */
	public ConnectInObject _connectInObject = null;

	/**
	 * 
	 */
	public ConnectOutObject _connectOutObject = null;

	/**
	 * 
	 */
	static public EditRoleFrame _editRoleFrame = null;

	/**
	 * @param role
	 * @return
	 */
	public static Role create_instance(Role role) {
		if ( role instanceof AgentRole)
			return new AgentRole( ( AgentRole)role);
		else if ( role instanceof SpotRole)
			return new SpotRole( ( SpotRole)role);
		else
			return null;
	}

	/**
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public Role(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		_ruleManager = new RuleManager();
		_connectInObject = new ConnectInObject( this);
		_connectOutObject = new ConnectOutObject( this);
	}

	/**
	 * @param global
	 * @param id
	 * @param name
	 * @param position
	 * @param graphics2D
	 */
	public Role(boolean global, int id, String name, Point position, Graphics2D graphics2D) {
		super(global, id, name, position, graphics2D);
		_ruleManager = new RuleManager();
		// TODO GlobalRoleの場合継承をどうするか？
		_connectInObject = new ConnectInObject( this);
		_connectOutObject = new ConnectOutObject( this);
	}

	/**
	 * @param role
	 */
	public Role(Role role) {
		super(role);
		_ruleManager = new RuleManager( role._ruleManager);
		_connectInObject = new ConnectInObject( role._connectInObject, this);
		_connectOutObject = new ConnectOutObject( role._connectOutObject, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#cleanup()
	 */
	@Override
	public void cleanup() {
		super.cleanup();
		_ruleManager.cleanup();
		disconnect( _connectInObject);
		disconnect( _connectOutObject);
		_connectInObject.cleanup();
		_connectOutObject.cleanup();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_name()
	 */
	@Override
	public String get_name() {
		if ( null == _connectInObject)
			return super.get_name();

		return _connectInObject.get_name();
	}

	/**
	 * @param outObject
	 */
	private void disconnect(ConnectObject connectObject) {
		while ( !connectObject._connectObjects.isEmpty())
			connectObject.disconnect( ( ConnectObject)connectObject._connectObjects.get( 0));
	}

	/**
	 * @param roleData
	 * @return
	 */
	public boolean update(RoleData roleData) {
		return _ruleManager.update( roleData._ruleManager);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#contains(java.awt.Point)
	 */
	@Override
	public boolean contains(Point point) {
		if ( super.contains(point))
			return true;

		if ( _connectInObject.contains( point, _position, _dimension))
			return true;

		if ( _connectOutObject.contains( point, _position, _dimension))
			return true;

		return false;
	}

	/**
	 * @param point
	 * @return
	 */
	public boolean isRole(Point point) {
		return super.contains(point);
	}

	/**
	 * @param point
	 * @return
	 */
	public boolean isConnectInObject(Point point) {
		return _connectInObject.contains( point, _position, _dimension);
	}

	/**
	 * @param point
	 * @return
	 */
	public boolean isConnectOutObject(Point point) {
		return _connectOutObject.contains( point, _position, _dimension);
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	public boolean is_closure(Vector<DrawObject> drawObjects) {
		return _connectInObject.is_closure( drawObjects);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#draw(java.awt.Graphics2D, java.awt.image.ImageObserver)
	 */
	@Override
	public void draw(Graphics2D graphics2D, ImageObserver imageObserver) {
		super.draw(graphics2D, imageObserver);
		graphics2D.setColor( _imageColor);
		graphics2D.draw3DRect( _position.x, _position.y, _dimension.width, _dimension.height, true);
		graphics2D.draw3DRect( _position.x + 4, _position.y, _dimension.width - 8, _dimension.height, true);

		_connectInObject.draw( _position, _dimension, graphics2D, imageObserver);
		_connectOutObject.draw( _position, _dimension, graphics2D, imageObserver);
	}

	/**
	 * @param graphics2D
	 */
	public void draw_connection(Graphics2D graphics2D) {
		//_connectOutObject.draw_connection( graphics2D);
		_connectInObject.draw_connection( graphics2D);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove()
	 */
	@Override
	public boolean can_remove() {
		return LayerManager.get_instance().can_remove_role( _name, true, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_paste()
	 */
	@Override
	public void on_paste() {
		_ruleManager.on_paste( this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges) {
		return _ruleManager.can_adjust_agent_name( _name, headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return _ruleManager.can_adjust_agent_name( _name, headName, ranges, newHeadName, newRanges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges) {
		return _ruleManager.can_adjust_spot_name( _name, headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return _ruleManager.can_adjust_spot_name( _name, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove(java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean can_remove(String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges) {
		// TODO 従来のもの
		return _ruleManager.can_remove( kind, name, otherSpotsHaveThisObjectName, headName, ranges, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove(java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean can_remove(String entityType, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges) {
		// TODO これからはこちらに移行してゆく
		return _ruleManager.can_remove( entityType, kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges, this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#is_number_object_type_correct(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean is_number_object_type_correct(String entityType, String numberObjectName, String newType) {
		return _ruleManager.is_number_object_type_correct( entityType, numberObjectName, newType, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_remove_role_name(java.lang.String)
	 */
	@Override
	public boolean can_remove_role_name(String roleName) {
		return _ruleManager.can_remove_role_name( _name, roleName);
	}

	/**
	 * @param expression
	 * @return
	 */
	public boolean can_remove_expression(Expression expression) {
		return _ruleManager.can_remove_expression( _name, expression);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return _ruleManager.update_agent_name_and_number(newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	@Override
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return _ruleManager.update_spot_name_and_number(newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_role_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_role_name(String originalName, String name) {
		return _ruleManager.update_role_name( originalName, name);
	}

	/**
	 * @param kind
	 * @param originalName
	 * @param newName
	 * @param entityType
	 * @return
	 */
	public boolean update_object_name(String kind, String originalName, String newName, String entityType) {
		return _ruleManager.update_object_name( kind, originalName, newName, entityType, this);
	}

	/**
	 * @param visualShellExpressionManager
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		return _ruleManager.update_expression( visualShellExpressionManager);
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @return
	 */
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		return _ruleManager.update_expression( newExpression, newVariableCount, originalExpression, this);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_agent_name_and_number(java.lang.String, java.util.Vector)
	 */
	@Override
	public void on_remove_agent_name_and_number(String headName, Vector<String[]> ranges) {
		_ruleManager.on_remove_agent_name_and_number(headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_spot_name_and_number(java.lang.String, java.util.Vector)
	 */
	@Override
	public void on_remove_spot_name_and_number(String headName, Vector<String[]> ranges) {
		_ruleManager.on_remove_spot_name_and_number(headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_role_name(java.util.Vector)
	 */
	@Override
	public void on_remove_role_name(Vector<String> roleNames) {
		_ruleManager.on_remove_role_name( roleNames);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#on_remove_stage_name(java.util.Vector)
	 */
	@Override
	public void on_remove_stage_name(Vector<String> stageNames) {
		_ruleManager.on_remove_stage_name( stageNames);
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		return _ruleManager.update_stage_name( _name, newName, originalName);
	}

	/**
	 * @param stageName
	 * @return
	 */
	public boolean can_remove_stage_name(String stageName) {
		return _ruleManager.can_remove_stage_name( _name, stageName);
	}

	/**
	 * @param stageNames
	 * @return
	 */
	public boolean can_adjust_stage_name(Vector<String> stageNames) {
		return _ruleManager.can_adjust_stage_name( _name, stageNames);
	}

	/**
	 * @return
	 */
	public boolean update_stage_manager() {
		return _ruleManager.update_stage_manager();
	}

	/**
	 * @param expressionMap
	 * @param usedExpressionMap
	 */
	public void get_used_expressions(TreeMap<String, Expression> expressionMap, TreeMap<String, Expression> usedExpressionMap) {
		_ruleManager.get_used_expressions( expressionMap, usedExpressionMap);
	}

	/**
	 * @param originalFunctionName
	 * @param newFunctionName
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		return _ruleManager.update_function( originalFunctionName, newFunctionName);
	}

	/**
	 * @param initialValues
	 * @param suffixes
	 */
	public void get_initial_values(Vector<String> initialValues, String[] suffixes) {
		_ruleManager.get_initial_values( initialValues, suffixes);
	}

	/**
	 * @return
	 */
	public boolean transform_time_conditions_and_commands() {
		return _ruleManager.transform_time_conditions_and_commands( this);
	}

	/**
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands() {
		return _ruleManager.transform_keyword_conditions_and_commands( this);
	}

	/**
	 * @return
	 */
	public boolean transform_numeric_conditions_and_commands() {
		return _ruleManager.transform_numeric_conditions_and_commands( this);
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean has_same_agent_name(String name, String number) {
		return _ruleManager.has_same_agent_name( name, number);
	}

	/**
	 * @param alias
	 * @return
	 */
	public boolean contains_this_alias(String alias) {
		return _ruleManager.contains_this_alias( alias);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#edit(javax.swing.JComponent, java.awt.Frame)
	 */
	@Override
	public void edit(JComponent component, Frame frame) {
		if ( null != _editRoleFrame)
			return;

		String key;
		if ( this instanceof AgentRole)
			key = "edit.agent.role.dialog.title";
		else if ( this instanceof SpotRole)
			key = "edit.spot.role.dialog.title";
		else
			return;

		_editRoleFrame = new EditRoleFrame( ResourceManager.get_instance().get( key) + " - " + _name, this);
		if ( !_editRoleFrame.create()) {
			_editRoleFrame = null;
			return;
		}
	}

	/**
	 * @param ruleCount
	 */
	public void how_many_rules(IntBuffer ruleCount) {
		_ruleManager.how_many_rules( ruleCount);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_initial_data()
	 */
	@Override
	public String get_initial_data() {
		String script = _ruleManager.get_initial_data( get_name(), this);

		if ( !script.equals( ""))
			script += Constant._lineSeparator;

		return script;
	}

	/**
	 * @param ruleCount
	 * @param initialValueMap
	 * @param demo
	 * @return
	 */
	public String get_script(int ruleCount, InitialValueMap initialValueMap, boolean demo) {
		return _ruleManager.get_script( get_name(), ruleCount, initialValueMap, demo, this);
	}

	/**
	 * @param name
	 * @param writer
	 * @return
	 */
	public boolean write(String name, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		write( get_name(), attributesImpl);

		writer.startElement( null, null, name, attributesImpl);

		write_comment( writer);

		if ( !_ruleManager.write( writer))
			return false;

		writer.endElement( null, null, name);
		return true;
	}
}
