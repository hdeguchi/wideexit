/**
 * 
 */
package soars.application.visualshell.object.gis.edit.panel.simple.panel.spot;

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.gis.GisDataManager;
import soars.application.visualshell.object.gis.edit.panel.base.GisPropertyPanelBase;
import soars.application.visualshell.object.gis.edit.panel.base.GisVariableTableBase;
import soars.application.visualshell.object.gis.edit.panel.simple.panel.base.GisSimpleVariablePanel;
import soars.application.visualshell.object.gis.object.base.GisObjectBase;
import soars.application.visualshell.object.gis.object.base.GisSimpleVariableObject;
import soars.application.visualshell.object.gis.object.spot.GisSpotVariableObject;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class GisSpotVariablePanel extends GisSimpleVariablePanel {

	/**
	 * 
	 */
	private ObjectSelector _spotSelector = null;

	/**
	 * @param gisDataManager
	 * @param gisPropertyPanelBaseMap
	 * @param gisVariableTableBase
	 * @param color
	 * @param owner
	 * @param parent
	 */
	public GisSpotVariablePanel(GisDataManager gisDataManager, Map<String, GisPropertyPanelBase> gisPropertyPanelBaseMap, GisVariableTableBase gisVariableTableBase, Color color, Frame owner, Component parent) {
		super("spot variable", gisDataManager, gisPropertyPanelBaseMap, gisVariableTableBase, color, owner, parent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#setup_center_panel(javax.swing.JPanel)
	 */
	@Override
	protected boolean setup_center_panel(JPanel parent) {
		insert_vertical_strut( parent);
		setup_name_textField( parent);
		insert_vertical_strut( parent);
		setup_spot_selector( parent);
		insert_vertical_strut( parent);
		setup_comment_textField( parent);
		insert_vertical_strut( parent);
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
		_nameTextField.setDocument( new TextExcluder( Constant._prohibitedCharacters13));
		_nameTextField.setSelectionColor( _color);
		_nameTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _nameTextField, this));
		_components.add( _nameTextField);
		panel.add( _nameTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spot_selector(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.spot.variable.dialog.initial.value"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_spotSelector = new ObjectSelector( "spot", "", true, 500, 100, _color, true, null);
		_components.add( _spotSelector);
		panel.add( _spotSelector);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_comment_textField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "edit.time.variable.dialog.comment"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		label.setForeground( _color);
		_labels.add( label);
		panel.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_commentTextField = new TextField();
		_commentTextField.setSelectionColor( _color);
		_commentTextField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _commentTextField, this));
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
		if ( null == gisSimpleVariableObject || !(gisSimpleVariableObject instanceof GisSpotVariableObject)) {
			_nameTextField.setText( "");
			//_spotSelector.selectFirstItem();
			//_initialValueComboBox.setSelectedItem(arg0).setText( "");
			_commentTextField.setText( "");
			_textUndoRedoManagers.clear();
			setup_textUndoRedoManagers();
			return;
		}

		GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)gisSimpleVariableObject;
		_nameTextField.setText( gisSpotVariableObject._name);
		_spotSelector.set( gisSpotVariableObject._initialValue);
		_commentTextField.setText( gisSpotVariableObject._comment);
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
		//_spotSelector.selectFirstItem();
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
		GisSpotVariableObject gisSpotVariableObject = new GisSpotVariableObject();
		gisSpotVariableObject._name = _nameTextField.getText();
		gisSpotVariableObject._initialValue = _spotSelector.get();
		gisSpotVariableObject._comment = _commentTextField.getText();
		return gisSpotVariableObject;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.gis.edit.panel.base.GisPanelBase#can_get_data(soars.application.visualshell.object.gis.object.base.GisObjectBase)
	 */
	@Override
	protected boolean can_get_data(GisObjectBase gisObjectBase) {
		if ( !( gisObjectBase instanceof GisSpotVariableObject))
			return false;

		GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)gisObjectBase;

		if ( !Constant.is_correct_name( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

//		if ( _entityBase instanceof AgentObject && ( _nameTextField.getText().equals( "$Name")
//			|| _nameTextField.getText().equals( "$Role") || _nameTextField.getText().equals( "$Spot"))) {
//			JOptionPane.showMessageDialog( _parent,
//				ResourceManager.get_instance().get( "edit.object.dialog.invalid.name.error.message"),
//				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
//				JOptionPane.ERROR_MESSAGE);
//			return false;
//		}

		if ( !_nameTextField.getText().equals( gisSpotVariableObject._name)) {
			if ( _gisVariableTableBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( ( null != LayerManager.get_instance().get_agent_has_this_name( _nameTextField.getText()))
			|| ( null != LayerManager.get_instance().get_spot_has_this_name( _nameTextField.getText()))) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
				JOptionPane.ERROR_MESSAGE);
			return false;
		}

		String[] propertyPages = Constant.get_propertyPanelBases( "simple variable");

		for ( int i = 0; i < propertyPages.length; ++i) {
			GisPropertyPanelBase gisPropertyPanelBase = _gisPropertyPanelBaseMap.get( propertyPages[ i]);
			if ( null != gisPropertyPanelBase && gisPropertyPanelBase.contains( _nameTextField.getText())) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		String[] kinds = Constant.get_kinds( "spot variable");

		for ( int i = 0; i < kinds.length; ++i) {
			if ( LayerManager.get_instance().is_object_name( kinds[ i], _nameTextField.getText(), new SpotObject()/*_entityBase*/)) {
				JOptionPane.showMessageDialog( _parent,
					ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
					ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
					JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		if ( null != LayerManager.get_instance().get_chart( _nameTextField.getText())) {
			JOptionPane.showMessageDialog( _parent,
				ResourceManager.get_instance().get( "edit.object.dialog.duplicated.name.error.message"),
				ResourceManager.get_instance().get( "edit.object.dialog.tree.spot.variable"),
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
		if ( !( gisObjectBase instanceof GisSpotVariableObject))
			return;

		GisSpotVariableObject gisSpotVariableObject = ( GisSpotVariableObject)gisObjectBase;

		if ( !gisSpotVariableObject._name.equals( "") && !_nameTextField.getText().equals( gisSpotVariableObject._name))
			_gisPropertyPanelBaseMap.get( "variable").update_object_name( "spot variable", gisSpotVariableObject._name, _nameTextField.getText());

		gisSpotVariableObject._name = _nameTextField.getText();
		gisSpotVariableObject._initialValue = _spotSelector.get();
		gisSpotVariableObject._comment = _commentTextField.getText();
	}
}
