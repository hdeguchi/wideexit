/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple.panel.number;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.simple.panel.base.GisSimpleVariablePanel;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.application.visualshell.object.gis.object.number.GisNumberObject;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class GisNumberVariablePanel extends GisSimpleVariablePanel {

	/**
	 * 
	 */
	private ComboBox _typeComboBox = null;

	/**
	 * 
	 */
	private ComboBox _initialValueComboBox = null;

	/**
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisNumberVariablePanel(GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super("number object", gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_type_comboBox( parent);
		insert_vertical_strut( parent);
		setup_initial_value_comboBox( parent);
		insert_vertical_strut( parent);
		setup_comment_textField( parent);
		//insert_vertical_strut( parent);
		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_name_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.object.dialog.name"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_nameTextField = new TextField();
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters4));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_type_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.type"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_typeComboBox = new ComboBox( new String[] {
				ResourceManager.get_instance().get( "number.object.integer"),
				ResourceManager.get_instance().get( "number.object.real.number")},
			_color, false, new CommonComboBoxRenderer( _color, false));
		_typeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> fields = _gisDataManager.get_numeric_fields( ( String)_typeComboBox.getSelectedItem(), false);
				CommonTool.update( _initialValueComboBox, fields.toArray( new String[ 0]));
			}
		});
		_components.add( _typeComboBox);
		panel.add( _typeComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_comboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueComboBox = new ComboBox( _color, false, new CommonComboBoxRenderer( _color, false));
		_components.add( _initialValueComboBox);
		panel.add( _initialValueComboBox);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.number.object.dialog.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new TextField();
		_commentTextField.setSelectionColor( _color);
		_commentTextField.setForeground( _color);
		_components.add( _commentTextField);
		panel.add( _commentTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_typeComboBox.setSelectedItem( ResourceManager.get_instance().get( "number.object.integer"));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void update(GisObjectBase gisObjectBase) {
		GisSimpleVariableObject gisSimpleVariableObject = ( GisSimpleVariableObject)gisObjectBase;
		if ( null == gisSimpleVariableObject || !(gisSimpleVariableObject instanceof GisNumberObject)) {
			_nameTextField.setText( "");
			//_initialValueComboBox.setSelectedItem(arg0).setText( "");
			_commentTextField.setText( "");
			_textUndoRedoManagers.clear();
			setup_textUndoRedoManagers();
			return;
		}

		GisNumberObject gisNumberObject = ( GisNumberObject)gisSimpleVariableObject;
		_nameTextField.setText( gisNumberObject._name);
		_typeComboBox.setSelectedItem( GisNumberObject.get_type_name( gisNumberObject._type));
		_initialValueComboBox.setSelectedItem( Field.get( gisNumberObject._fields));
		_commentTextField.setText( gisNumberObject._comment);
		_textUndoRedoManagers.clear();
		setup_textUndoRedoManagers();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_textUndoRedoManagers()
	 */
	@Override
	protected void setup_textUndoRedoManagers() {
		if ( !_textUndoRedoManagers.isEmpty())
			return;

		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#clear()
	 */
	@Override
	public void clear() {
		super.clear();
		_typeComboBox.setSelectedIndex( 0);
//		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return super.is_empty();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#create_and_get()
	 */
	@Override
	protected GisObjectBase create_and_get() {
		GisNumberObject gisNumberObject = new GisNumberObject();
		gisNumberObject._name = _nameTextField.getText();
		gisNumberObject._type = GisNumberObject.get_type( ( String)_typeComboBox.getSelectedItem());
		gisNumberObject._fields = Field.get( ( String)_initialValueComboBox.getSelectedItem());
		gisNumberObject._comment = _commentTextField.getText();
		return gisNumberObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisNumberObject))
			return false;

		GisNumberObject gisNumberObject = ( GisNumberObject)gisObjectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String type = ( String)_typeComboBox.getSelectedItem();
		if ( null == type || type.equals( ""))
			return false;

		if ( !_nameTextField.getText().equals( gisNumberObject._name)) {
			if ( _gisVariableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// 初期値については別途チェックが必要
//		String initial_value = _initialValueTextField.getText();
//
//		if ( !initial_value.equals( "")) {
//			if ( initial_value.equals( "$") || 0 < initial_value.indexOf( '$')) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			if ( initial_value.startsWith( "$") && 0 < initial_value.indexOf( "$", 1)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			if ( initial_value.startsWith( "$") && 0 < initial_value.indexOf( ")", 1)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			if ( initial_value.equals( "$Name")
//				|| initial_value.equals( "$Role")
//				|| initial_value.equals( "$Spot")
//				|| 0 <= initial_value.indexOf( Constant._experimentName)) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//
//			initial_value = NumberObject.is_correct( initial_value, NumberObject.get_type( type));
//			if ( null == initial_value) {
//				JOptionPane.showMessageDialog( _parent,
//					ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
//					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
//					JOptionPane.ERROR_MESSAGE);
//				return false;
//			}
//		}

		String[] propertyPages = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPages.length; ++i) {
			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPages[ i]);
			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "number object");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_entityBase*/)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		// 型チェック
		if ( !gisNumberObject._type.equals( ( String)_typeComboBox.getSelectedItem())) {
			if ( !LayerManager.get_instance().is_number_object_type_correct( "spot", gisNumberObject._name, ( String)_typeComboBox.getSelectedItem())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.invalid.number.object.type.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.number.object"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected void get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisNumberObject))
			return;

		GisNumberObject gisNumberObject = ( GisNumberObject)gisObjectBase;

		String type = ( String)_typeComboBox.getSelectedItem();

		if ( !gisNumberObject._name.equals( "") && !_nameTextField.getText().equals( gisNumberObject._name))
			_gisPropertyPanelBaseMap.get( "variable").update_object_name( "number object", gisNumberObject._name, _nameTextField.getText());

		gisNumberObject._name = _nameTextField.getText();
		gisNumberObject._type = GisNumberObject.get_type( type);
		gisNumberObject._fields = Field.get( ( String)_initialValueComboBox.getSelectedItem());
		gisNumberObject._comment = _commentTextField.getText();
	}
}
