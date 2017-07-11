/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel;

import java.awt.Component;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.class_variable.ClassVariablePropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.extransfer.ExTransferPropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.file.FilePropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.image.ImagePropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.initial_data_file.InitialDataFilePropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.others.OthersPropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.property.PropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.simple.SimpleVariablePropertyPanel;
import soars.application.visualshell.object.entity.base.edit.panel.variable.VariablePropertyPanel;
import soars.application.visualshell.object.entity.base.edit.tree.ObjectTree;

/**
 * @author kurata
 *
 */
public class ObjectPanel extends JPanel {

	/**
	 * 
	 */
	public ObjectTree _objectTree = null;

	/**
	 * 
	 */
	private EntityBase _entityBase = null;

	/**
	 * 
	 */
	private List<PropertyPanelBase> _propertyPanelBases = new ArrayList<PropertyPanelBase>();

	/**
	 * 
	 */
	static private int _agentSelectedIndex = 0;

	/**
	 * 
	 */
	static private int _spotSelectedIndex = 0;

	/**
	 * @param entityBase
	 */
	public ObjectPanel(EntityBase entityBase) {
		super();
		_entityBase = entityBase;
	}

	/**
	 * 
	 */
	private void get_selected_index() {
		if ( _entityBase instanceof AgentObject)
			_agentSelectedIndex = getSelectedIndex();
		else
			_spotSelectedIndex = getSelectedIndex();
	}

	/**
	 * @return
	 */
	private int getSelectedIndex() {
		for ( int i = 0; i < _propertyPanelBases.size(); ++i) {
			if ( _propertyPanelBases.get( i).isVisible())
				return i;
		}
		return 0;
	}

	/**
	 * 
	 */
	private void set_selected_index() {
		if ( _entityBase instanceof AgentObject)
			setSelectedIndex( _agentSelectedIndex);
		else
			setSelectedIndex( _spotSelectedIndex);
	}

	/**
	 * @param index
	 */
	private void setSelectedIndex(int index) {
		for ( PropertyPanelBase propertyPanelBase:_propertyPanelBases)
			propertyPanelBase.setVisible( propertyPanelBase.equals( _propertyPanelBases.get( index)));
	}

	/**
	 * 
	 */
	private void select() {
		for ( PropertyPanelBase propertyPanelBase:_propertyPanelBases) {
			if ( propertyPanelBase.isVisible())
				_objectTree.select( propertyPanelBase);
		}
	}

	/**
	 * @param component
	 */
	public void select(Component component) {
		for ( PropertyPanelBase propertyPanelBase:_propertyPanelBases)
			propertyPanelBase.setVisible( component.equals( propertyPanelBase));
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean confirm(Component component) {
		PropertyPanelBase propertyPanelBase = ( PropertyPanelBase)component;
		return propertyPanelBase.confirm( true);
	}

	/**
	 * @param objectTree
	 * @param owner
	 * @param parent
	 * @return
	 */
	public boolean setup(ObjectTree objectTree, Frame owner, Component parent) {
		_objectTree = objectTree;

		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		Map<String, PropertyPanelBase> propertyPanelBaseMap = new HashMap<String, PropertyPanelBase>();

		PropertyPanel propertyPanel = new PropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.basic.property"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( propertyPanel, propertyPanelBaseMap, "property"))
			return false;

		SimpleVariablePropertyPanel simpleVariablePropertyPanel = new SimpleVariablePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.simple.variable"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( simpleVariablePropertyPanel, propertyPanelBaseMap, "simple variable"))
			return false;

		VariablePropertyPanel variablePropertyPanel = new VariablePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.variable"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( variablePropertyPanel, propertyPanelBaseMap, "variable"))
			return false;

		if ( Environment.get_instance().is_functional_object_enable()) {
			ClassVariablePropertyPanel classVariablePropertyPanel = new ClassVariablePropertyPanel(
				ResourceManager.get_instance().get( "edit.object.dialog.tree.class.variable"),
				_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
			if ( !create( classVariablePropertyPanel, propertyPanelBaseMap, "class variable"))
				return false;
		}

		FilePropertyPanel filePropertyPanel = new FilePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.file"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( filePropertyPanel, propertyPanelBaseMap, "file"))
			return false;

		InitialDataFilePropertyPanel initialDataFilePropertyPanel = new InitialDataFilePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.initial.data.file"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( initialDataFilePropertyPanel, propertyPanelBaseMap, "initial data file"))
			return false;

		if ( Environment.get_instance().is_exchange_algebra_enable() && Environment.get_instance().is_extransfer_enable()) {
			ExTransferPropertyPanel exTransferPropertyPanel = new ExTransferPropertyPanel(
				ResourceManager.get_instance().get( "edit.object.dialog.tree.extransfer"),
				_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
			if ( !create( exTransferPropertyPanel, propertyPanelBaseMap, "extransfer"))
				return false;
		}

		ImagePropertyPanel imagePropertyPanel = new ImagePropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.image"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( imagePropertyPanel, propertyPanelBaseMap, "image"))
			return false;

		OthersPropertyPanel othersPropertyPanel = new OthersPropertyPanel(
			ResourceManager.get_instance().get( "edit.object.dialog.tree.others"),
			_entityBase, propertyPanelBaseMap, _propertyPanelBases.size(), owner, parent);
		if ( !create( othersPropertyPanel, propertyPanelBaseMap, "others"))
			return false;

		_objectTree.expand();

		return true;
	}

	/**
	 * @param propertyPanelBase
	 * @param propertyPanelBaseMap
	 * @param key
	 * @return
	 */
	private boolean create(PropertyPanelBase propertyPanelBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, String key) {
		if ( !propertyPanelBase.create())
			return false;

		add( propertyPanelBase);
		_objectTree.append( propertyPanelBase);
		propertyPanelBaseMap.put( key, propertyPanelBase);
		_propertyPanelBases.add( propertyPanelBase);
		return true;
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		for ( PropertyPanelBase propertyPanelBase:_propertyPanelBases) {
			propertyPanelBase.on_setup_completed();
			propertyPanelBase.setVisible( propertyPanelBase.equals( _propertyPanelBases.get( 0)));
		}

		set_selected_index();
		select();
	}

	/**
	 * @param entityBase
	 * @return
	 */
	public boolean on_ok(EntityBase entityBase) {
		//for ( PropertyPanelBase propertyPanelBase:_propertyPanelBases) {
		// 参照されているものを後から保存しないとダメ！
		for ( int i = _propertyPanelBases.size() - 1; 0 <= i; --i) {
			PropertyPanelBase propertyPanelBase = _propertyPanelBases.get( i);
			if ( ( propertyPanelBase instanceof PropertyPanel)
				|| ( propertyPanelBase instanceof ClassVariablePropertyPanel)
				|| ( propertyPanelBase instanceof FilePropertyPanel)
				|| ( propertyPanelBase instanceof ExTransferPropertyPanel)
				|| ( propertyPanelBase instanceof ImagePropertyPanel)) {
				if ( !propertyPanelBase.on_ok())
					return false;
			} else {
				if ( !entityBase.is_multi()) {
					if ( !propertyPanelBase.on_ok())
						return false;
				}
			}
		}

		get_selected_index();

		return true;
	}

	/**
	 * 
	 */
	public void on_cancel() {
		for ( PropertyPanelBase propertyPanelBase:_propertyPanelBases) {
			if ( ( propertyPanelBase instanceof ClassVariablePropertyPanel)
				|| ( propertyPanelBase instanceof FilePropertyPanel)
				|| ( propertyPanelBase instanceof InitialDataFilePropertyPanel)
				|| ( propertyPanelBase instanceof ExTransferPropertyPanel))
				propertyPanelBase.on_cancel();
		}

		get_selected_index();
	}

	/**
	 * 
	 */
	public void windowClosing() {
		get_selected_index();
	}
}
