/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple.panel.keyword;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.field.selector.Field;
import soars.application.visualshell.object.gis.edit.field.selector.SelectGisDataFieldsDlg;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.simple.panel.base.GisSimpleVariablePanel;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.application.visualshell.object.gis.object.keyword.GisKeywordObject;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class GisKeywordPanel extends GisSimpleVariablePanel {

	/**
	 * 
	 */
	private List<Field> _fields = new ArrayList<Field>();

	/**
	 * 
	 */
	private TextField _initialValueTextField = null;

	/**
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisKeywordPanel(GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super("keyword", gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_initial_value_textField( parent);
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
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters1));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_initial_value_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( ResourceManager.get_instance().get( "edit.keyword.dialog.initial.value"));
		button.setForeground( _color);
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				get_fields();
			}
		});
		_labels.add( button);
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));

		_initialValueTextField = new TextField();
		_initialValueTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters3));
		_initialValueTextField.setSelectionColor( _color);
		_initialValueTextField.setForeground( _color);
		_initialValueTextField.setEditable( false);
		_components.add( _initialValueTextField);
		panel.add( _initialValueTextField);

		parent.add( panel);
	}

	/**
	 * 
	 */
	protected void get_fields() {
		SelectGisDataFieldsDlg selectGisDataFieldsDlg = new SelectGisDataFieldsDlg( _owner, ResourceManager.get_instance().get( "select.keyword.initial.value.field.dialog.title"), true, _gisDataManager.get_fields( false)/*_gisDataManager._availableKeywordFields*/, _fields, _gisDataManager, Constant._prohibitedCharacters3);
		if ( !selectGisDataFieldsDlg.do_modal())
			return;

		_fields = selectGisDataFieldsDlg._selectedFields;
		_initialValueTextField.setText( Field.get( _fields));
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.keyword.dialog.comment"));
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
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#update(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	public void update(GisObjectBase gisObjectBase) {
		GisSimpleVariableObject gisSimpleVariableObject = ( GisSimpleVariableObject)gisObjectBase;
		if ( null == gisSimpleVariableObject || !(gisSimpleVariableObject instanceof GisKeywordObject)) {
			_nameTextField.setText( "");
			//_initialValueComboBox.setSelectedItem(arg0).setText( "");
			_commentTextField.setText( "");
			_textUndoRedoManagers.clear();
			setup_textUndoRedoManagers();
			return;
		}

		GisKeywordObject gisKeywordObject = ( GisKeywordObject)gisSimpleVariableObject;
		_nameTextField.setText( gisKeywordObject._name);
		copy( gisKeywordObject._fields);
		_initialValueTextField.setText( Field.get( gisKeywordObject._fields));
		_commentTextField.setText( gisKeywordObject._comment);
		_textUndoRedoManagers.clear();
		setup_textUndoRedoManagers();
	}

	/**
	 * @param fields
	 */
	private void copy(List<Field> fields) {
		_fields.clear();
		for ( Field field:fields)
			_fields.add( field);
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
		_fields.clear();
		_initialValueTextField.setText( "");
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#is_empty()
	 */
	@Override
	protected boolean is_empty() {
		return ( super.is_empty() && _initialValueTextField.getText().equals( ""));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#create_and_get()
	 */
	@Override
	protected GisObjectBase create_and_get() {
		GisKeywordObject gisKeywordObject = new GisKeywordObject();
		gisKeywordObject._name = _nameTextField.getText();
		gisKeywordObject.copy( _fields);
		gisKeywordObject._comment = _commentTextField.getText();
		return gisKeywordObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisKeywordObject))
			return false;

		GisKeywordObject gisKeywordObject = ( GisKeywordObject)gisObjectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

//		if ( _entityBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
//			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}

		if ( null == _initialValueTextField.getText()
			|| 0 < _initialValueTextField.getText().indexOf( '$')
			|| _initialValueTextField.getText().equals( "$")
			|| _initialValueTextField.getText().startsWith( " ")
			|| _initialValueTextField.getText().endsWith( " ")
			|| _initialValueTextField.getText().equals( "$Name")
			|| _initialValueTextField.getText().equals( "$Role")
			|| _initialValueTextField.getText().equals( "$Spot")
			|| 0 <= _initialValueTextField.getText().indexOf( Constant._experimentName)) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( _initialValueTextField.getText().startsWith( "$")
			&& ( 0 <= _initialValueTextField.getText().indexOf( " ")
			|| 0 < _initialValueTextField.getText().indexOf( "$", 1)
			|| 0 < _initialValueTextField.getText().indexOf( ")", 1))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if ( !_nameTextField.getText().equals( gisKeywordObject._name)) {
			if ( _gisVariableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] propertyPages = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPages.length; ++i) {
			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPages[ i]);
			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "keyword");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_entityBase*/)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
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
		if ( !( gisObjectBase instanceof GisKeywordObject))
			return;

		GisKeywordObject gisKeywordObject = ( GisKeywordObject)gisObjectBase;

		if ( !gisKeywordObject._name.equals( "") && !_nameTextField.getText().equals( gisKeywordObject._name))
			_gisPropertyPanelBaseMap.get( "variable").update_object_name( "keyword", gisKeywordObject._name, _nameTextField.getText());

		gisKeywordObject._name = _nameTextField.getText();
		gisKeywordObject.copy( _fields);
		gisKeywordObject._comment = _commentTextField.getText();
	}
}
