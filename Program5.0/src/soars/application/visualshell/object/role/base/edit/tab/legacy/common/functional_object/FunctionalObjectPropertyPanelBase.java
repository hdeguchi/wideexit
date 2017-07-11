/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.arbitrary.JarFileProperties;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.object.base.Rule;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.base.object.legacy.common.functional_object.FunctionalObjectRule;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.xml.dom.XmlTool;

/**
 * @author kurata
 *
 */
public class FunctionalObjectPropertyPanelBase extends RulePropertyPanelBase {

	/**
	 * 
	 */
	protected Map<String, TreeMap<String, MethodObject>> _methodObjectMapMap = new HashMap<String, TreeMap<String, MethodObject>>();

	/**
	 * 
	 */
	protected Map<String, Map<String, String[]>> _agentValueMapMap = new HashMap<String, Map<String, String[]>>();

	/**
	 * 
	 */
	protected Map<String, Map<String, String[]>> _spotValueMapMap = new HashMap<String, Map<String, String[]>>();

	/**
	 * 
	 */
	protected String[] _probabilityNames = null;

	/**
	 * 
	 */
	protected String[] _keywordNames = null;

	/**
	 * 
	 */
	protected String[] _numberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _integerNumberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _realNumberObjectNames = null;

	/**
	 * 
	 */
	protected String[] _collectionNames = null;

	/**
	 * 
	 */
	protected String[] _listNames = null;

	/**
	 * 
	 */
	protected String[] _mapNames = null;

	/**
	 * 
	 */
	protected String[] _classVariableNames = null;

	/**
	 * 
	 */
	protected String[] _fileVariableNames = null;

	/**
	 * 
	 */
	protected CheckBox _spotCheckBox = null;

	/**
	 * 
	 */
	protected ObjectSelector _spotSelector = null;

	/**
	 * 
	 */
	protected CheckBox _spotVariableCheckBox = null;

	/**
	 * 
	 */
	protected ComboBox _spotVariableComboBox = null;

	/**
	 * 
	 */
	protected ComboBox _classVriableComboBox = null;

	/**
	 * 
	 */
	protected TextField _classnameTextField = null;

	/**
	 * 
	 */
	protected TextField _jarFilenameTextField = null;

	/**
	 * 
	 */
	protected JLabel _dummy = null;

	/**
	 * 
	 */
	protected ComboBox _methodComboBox = null;

	/**
	 * 
	 */
	protected TextField _returnTypeTextField = null;

	/**
	 * 
	 */
	protected String _returnType = "";

	/**
	 * 
	 */
	protected TextField _returnValueTextField = null;

	/**
	 * 
	 */
	protected JButton _returnValueButton = null;

	/**
	 * 
	 */
	protected ParameterTable _parameterTable = null;

	/**
	 * 
	 */
	protected JScrollPane _parameterTableScrollPane = null;

	/**
	 * 
	 */
	protected List<List<JLabel>> _labels = new ArrayList<List<JLabel>>();

	/**
	 * @param title
	 * @param kind
	 * @param type
	 * @param color
	 * @param role
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public FunctionalObjectPropertyPanelBase(String title, String kind, String type, Color color, Role role, int index, Frame owner, EditRoleFrame parent) {
		super(title, kind, type, color, role, index, owner, parent);
		for ( int i = 0; i < 2; ++i)
			_labels.add( new ArrayList<JLabel>());
	}

	/**
	 * @return
	 */
	protected String get_return_value() {
		return "";
	}

	/**
	 * @param text
	 */
	protected void set_return_value(String text) {
	}

	/**
	 * @param enable
	 */
	protected void set_enable_return_value_button(boolean enable) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;


		setLayout( new BorderLayout());


		JPanel basicPanel = new JPanel();
		basicPanel.setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( northPanel);

		setup_spotCheckBox_and_spotSelector( northPanel);

		insert_vertical_strut( northPanel);

		setup_spotVariableCheckBox_and_spotVariableComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_classVriableComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_methodComboBox( northPanel);

		insert_vertical_strut( northPanel);

		setup_returnTypeTextField( northPanel);

		insert_vertical_strut( northPanel);

		setup_returnValueTextField( northPanel);

		insert_vertical_strut( northPanel);

		if ( !setup_parameterTable( northPanel))
			return false;

		insert_vertical_strut( northPanel);

		basicPanel.add( northPanel, "North");


		add( basicPanel);


		setup_apply_button();


		adjust();


		return true;
	}

	/**
	 * @param parent
	 */
	private void setup_spotCheckBox_and_spotSelector(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spotCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.spot.check.box.name"),
			false, true);
		_spotCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_method_deselected( /*!_spot_checkBox.isSelected(),*/
					ItemEvent.SELECTED == arg0.getStateChange() || _spotVariableCheckBox.isSelected(),
					( String)_classVriableComboBox.getSelectedItem(),
					( String)_methodComboBox.getSelectedItem());
				_spotSelector.setEnabled(
					ItemEvent.SELECTED == arg0.getStateChange());
				on_update( ItemEvent.SELECTED == arg0.getStateChange(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
			}
		});
		panel.add( _spotCheckBox);

		_spotSelector = create_spot_selector( true, true, panel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_spotVariableCheckBox_and_spotVariableComboBox( JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_spotVariableCheckBox = create_checkBox(
			ResourceManager.get_instance().get( "edit.rule.dialog.spot.variable.check.box.name"),
			true, true);
		_spotVariableCheckBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				on_update( _spotCheckBox.isSelected(),
					_spotSelector,
					_spotVariableCheckBox,
					_spotVariableComboBox);
			}
		});
		panel.add( _spotVariableCheckBox);

		_spotVariableComboBox = create_comboBox( null, _standardControlWidth, false);
		panel.add( _spotVariableComboBox);

		parent.add( panel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#changed(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	public void changed(String name, String number, String fullName, ObjectSelector objectSelector) {
		on_method_deselected(
			_spotCheckBox.isSelected() || _spotVariableCheckBox.isSelected(),
			( String)_classVriableComboBox.getSelectedItem(),
			( String)_methodComboBox.getSelectedItem());

		super.changed(name, number, fullName, objectSelector);
	}

	/**
	 * @param parent
	 */
	private void setup_classVriableComboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.class.variable"), false);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.get( 0).add( label);

		_classVriableComboBox = create_comboBox( null, _standardControlWidth, false);
		_classVriableComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_class_variable_selected( arg0);
			}
		});
		_classVriableComboBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( ItemEvent.DESELECTED != arg0.getStateChange())
					return;

				on_method_deselected(
					_spotCheckBox.isSelected() || _spotVariableCheckBox.isSelected(),
					( String)arg0.getItem(),
					( String)_methodComboBox.getSelectedItem());
			}
		});
		panel.add( _classVriableComboBox);


		label = create_label( " "
			+ ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.class.name"),
			false);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.get( 1).add( label);

		_classnameTextField = create_textField( _standardControlWidth, false);
		_classnameTextField.setEditable( false);
		panel.add( _classnameTextField);

		parent.add( panel);


		insert_vertical_strut( parent);


		panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_dummy = new JLabel();
		panel.add( _dummy);

		label = create_label( " "
			+ ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.jar.filename"),
			false);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.get( 1).add( label);

		_jarFilenameTextField = create_textField( _standardControlWidth, false);
		_jarFilenameTextField.setEditable( false);
		panel.add( _jarFilenameTextField);

		parent.add( panel);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_class_variable_selected(ActionEvent actionEvent) {
		String classVariableName = ( String)_classVriableComboBox.getSelectedItem();
		if ( null == classVariableName) {
			_methodComboBox.removeAllItems();
			return;
		}

		ClassVariableObject classVariableObject;
		if ( !_spotCheckBox.isSelected() && !_spotVariableCheckBox.isSelected())
			classVariableObject = get_agent_class_variable( classVariableName);
		else
			classVariableObject = get_spot_class_variable( classVariableName);
		if ( null == classVariableObject) {
			if ( !Environment.get_instance().is_exchange_algebra_enable()) {
				_methodComboBox.removeAllItems();
				return;
			} else {
				String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
				if ( CommonRuleManipulator.is_object( "exchange algebra", spot + classVariableName))
					// TODO もし選択されているのが交換代数なら、、、
					classVariableObject = new ClassVariableObject( classVariableName, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname);
				else {
					_methodComboBox.removeAllItems();
					return;
				}
			}
		}

		//_classname_textField.setText( classVariableObject._classname);
		if ( null == classVariableObject._classname || classVariableObject._classname.equals( ""))
			_classnameTextField.setText( "");
		else {
			String[] words = classVariableObject._classname.split( "\\.");
			_classnameTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_classnameTextField.setToolTipText( classVariableObject._classname);

		//_jar_filename_textField.setText( classVariableObject._jar_filename);
		if ( null == classVariableObject._jarFilename || classVariableObject._jarFilename.equals( ""))
			_jarFilenameTextField.setText( "");
		else {
			String[] words = classVariableObject._jarFilename.split( "/");
			_jarFilenameTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
		}

		_jarFilenameTextField.setToolTipText( classVariableObject._jarFilename);


		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap) {
			Node node = JarFileProperties.get_instance().get_jarfile_node( classVariableObject._jarFilename);
			if ( null == node)
				return;

			node = JarFileProperties.get_instance().get_class_node( node, classVariableObject._classname);
			if ( null == node)
				return;

			NodeList nodeList = XmlTool.get_node_list( node, "method");
			if ( null == nodeList)
				return;

			List<MethodObject> methodList = new ArrayList<MethodObject>();
			for ( int i = 0; i < nodeList.getLength(); ++i) {
				node = nodeList.item( i);
				if ( null == node)
					continue;

				String name = XmlTool.get_attribute( node, "name");
				if ( null == name)
					continue;

				if ( name.equals( classVariableObject._classname)) {
					String[] words = name.split( "\\.");
					name = ( ( null != words && 0 < words.length) ? words[ words.length - 1] : name);
				}

				String returnType = XmlTool.get_attribute( node, "return_type");
				// TODO Auto-generated method stub
				if ( null == returnType
					|| ( _kind.equals( "condition") && ( returnType.equals( "") || returnType.equals( "void"))))
					continue;

				List<String> parameterTypes = new ArrayList<String>();
				int index = 0;
				while ( true) {
					String parameterType = XmlTool.get_attribute( node, "parameter_type" + index);
					if ( null == parameterType)
						break;

					parameterTypes.add( parameterType);

					++index;
				}

				methodList.add( new MethodObject( name, returnType, parameterTypes));
			}

			MethodObject[] methodObjects = methodList.toArray( new MethodObject[ 0]);
			Arrays.sort( methodObjects, new MethodObjectComparator());

			methodObjectMap = new TreeMap<String, MethodObject>();

			for ( int i = 0; i < methodObjects.length; ++i) {
				if ( i == methodObjects.length - 1)
					// TODO 2012.8.1
					methodObjectMap.put( methodObjects[ i].get_full_method_name(), methodObjects[ i]);
					//methodObjectMap.put( methodObjects[ i]._name, methodObjects[ i]);
				else {
					if ( methodObjects[ i]._name.equals( methodObjects[ i + 1]._name))
						i = append( methodObjects, methodObjects[ i]._name, i, methodObjectMap);
					else
						// TODO 2012.8.1
						methodObjectMap.put( methodObjects[ i].get_full_method_name(), methodObjects[ i]);
						//methodObjectMap.put( methodObjects[ i]._name, methodObjects[ i]);
				}
			}

			_methodObjectMapMap.put( classVariableObject._classname, methodObjectMap);
		}

		if ( methodObjectMap.isEmpty()) {
			_methodComboBox.removeAllItems();
			return;
		}

		List<String> methodNames = new ArrayList<String>( methodObjectMap.keySet());
		CommonTool.update( _methodComboBox, methodNames.toArray( new String[ 0]));

//		// TODO 2012.8.1
//		String max = "";
//		for ( String methodName:methodNames) {
//			if ( max.length() < methodName.length())
//				max = methodName;
//		}
//		//FontMetrics fontMetrics = _methodComboBox.getGraphics().getFontMetrics();
//		FontMetrics fontMetrics = MainFrame.get_instance().getGraphics().getFontMetrics();
//		int width = fontMetrics.stringWidth( max);
//		if ( _standardControlWidth < width)
//			_methodComboBox.setPreferredSize( new Dimension( _standardControlWidth < width ? width : _standardControlWidth, _methodComboBox.getPreferredSize().height));
	}

	/**
	 * @param methodObjects
	 * @param name
	 * @param start
	 * @param methodObjectMap
	 * @return
	 */
	private int append(MethodObject[] methodObjects, String name, int start, TreeMap<String, MethodObject> methodObjectMap) {
		int index = 1;
		for ( int i = start; i < methodObjects.length; ++i) {
			if ( !methodObjects[ i]._name.equals( name))
				return ( i - 1);

			// TODO 2012.8.1
			methodObjectMap.put( methodObjects[ i].get_full_method_name(), methodObjects[ i]);
			//methodObjectMap.put( name + " - (" + index + ")", methodObjects[ i]);
			++index;
		}
		return ( methodObjects.length - 1);
	}

	/**
	 * @param comboBox
	 * @param object_names
	 */
	private void update(JComboBox comboBox, MethodObject[] methodObjects) {
		if ( null == methodObjects || 0 == methodObjects.length) {
			comboBox.removeAllItems();
			return;
		}

		MethodObject methodObject = ( MethodObject)comboBox.getSelectedItem();

		comboBox.removeAllItems();

		int index = 0;
		for ( int i = 0; i < methodObjects.length; ++i) {
			comboBox.addItem( methodObjects[ i]);
			if ( methodObjects[ i].equals( methodObject))
				index = i;
		}

		comboBox.setSelectedIndex( index);
	}

	/**
	 * @param parent
	 */
	private void setup_methodComboBox(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label( ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.method"), false);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.get( 0).add( label);

		_methodComboBox = create_comboBox( null, _standardControlWidth, false);
		_methodComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_method_selected( arg0);
			}
		});
		_methodComboBox.addItemListener( new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if ( ItemEvent.DESELECTED != arg0.getStateChange())
					return;

				on_method_deselected(
					_spotCheckBox.isSelected() || _spotVariableCheckBox.isSelected(),
					( String)_classVriableComboBox.getSelectedItem(),
					( String)arg0.getItem());
			}
		});
		panel.add( _methodComboBox);

		parent.add( panel);
	}

	/**
	 * @param spot
	 * @param classVariableName
	 * @param methodName
	 */
	protected void on_method_deselected(boolean spot, String classVariableName, String methodName) {
		Map<String, String[]> valueMap = get_value_map( classVariableName, !spot ? _agentValueMapMap : _spotValueMapMap);
		if ( null == valueMap)
			return;

		_parameterTable.update_value_map( valueMap, methodName, get_return_value());
	}

	/**
	 * @param classVariableName
	 * @param objectValueMap 
	 * @return
	 */
	private Map<String, String[]> get_value_map(String classVariableName, Map<String, Map<String, String[]>> objectValueMap) {
		if ( null == classVariableName)
			return null;

		Map<String, String[]> valueMap = objectValueMap.get( classVariableName);
		if ( null == valueMap) {
			valueMap = new HashMap<String, String[]>();
			objectValueMap.put( classVariableName, valueMap);
		}

		return valueMap;
	}

	/**
	 * @param actionEvent
	 */
	protected void on_method_selected(ActionEvent actionEvent) {
		DefaultTableModel defaultTableModel = ( DefaultTableModel)_parameterTable.getModel();

		String methodName = ( String)_methodComboBox.getSelectedItem();
		if ( null == methodName) {
			_returnTypeTextField.setText( "");
			set_enable_return_value_button( false);
			_parameterTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}

		String classVariableName = ( String)_classVriableComboBox.getSelectedItem();
		if ( null == classVariableName) {
			_returnTypeTextField.setText( "");
			set_enable_return_value_button( false);
			_parameterTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}

		ClassVariableObject classVariableObject;
		if ( !_spotCheckBox.isSelected() && !_spotVariableCheckBox.isSelected())
			classVariableObject = get_agent_class_variable( classVariableName);
		else
			classVariableObject = get_spot_class_variable( classVariableName);
		if ( null == classVariableObject) {
			if ( !Environment.get_instance().is_exchange_algebra_enable()) {
				_returnTypeTextField.setText( "");
				set_enable_return_value_button( false);
				_parameterTable.removeAll();
				defaultTableModel.setRowCount( 0);
				return;
			} else {
				String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
				if ( CommonRuleManipulator.is_object( "exchange algebra", spot + classVariableName))
					// TODO もし選択されているのが交換代数なら、、、
					classVariableObject = new ClassVariableObject( classVariableName, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname);
				else {
					_returnTypeTextField.setText( "");
					set_enable_return_value_button( false);
					_parameterTable.removeAll();
					defaultTableModel.setRowCount( 0);
					return;
				}
			}
		}

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap) {
			_returnTypeTextField.setText( "");
			set_enable_return_value_button( false);
			_parameterTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}

		MethodObject methodObject = methodObjectMap.get( methodName);
		if ( null == methodObject) {
			_returnTypeTextField.setText( "");
			set_enable_return_value_button( false);
			_parameterTable.removeAll();
			defaultTableModel.setRowCount( 0);
			return;
		}



		set_enable_return_value_button( ( null == methodObject._returnType || methodObject._returnType.equals( "") || methodObject._returnType.equals( "void")) ? false : true);

		

		//_return_type_textField.setText( ( null != methodObject._return_type) ? methodObject._return_type : "");
		if ( null == methodObject._returnType || methodObject._returnType.equals( "")) {
			_returnTypeTextField.setText( "");
			_returnType = "";
		} else {
			String[] words = methodObject._returnType.split( "\\.");
			_returnTypeTextField.setText( ( null != words && 0 < words.length) ? words[ words.length - 1] : "");
			_returnType = methodObject._returnType;
		}

		_returnTypeTextField.setToolTipText( ( null != methodObject._returnType && !methodObject._returnType.equals( "")) ? methodObject._returnType : null);



		_parameterTable.removeAll();
		defaultTableModel.setRowCount( 0);

		Map<String, String[]> valueMap;
		if ( !_spotCheckBox.isSelected() && !_spotVariableCheckBox.isSelected())
			valueMap = _agentValueMapMap.get( classVariableName);
		else
			valueMap = _spotValueMapMap.get( classVariableName);

		String[] values = null;
		if ( null != valueMap) {
			values = valueMap.get( methodName);
			if ( null != values) {
				if ( values.length != methodObject._parameterTypes.length + 1)
					values = null;
			}
		}

		set_return_value( ( null == values) ? "" : values[ values.length - 1]);

		Object[] objects = new Object[ 2];
		for ( int i = 0; i < methodObject._parameterTypes.length; ++i) {
			objects[ 0] = methodObject._parameterTypes[ i];
			if ( null == objects[ 0])
				continue;
				
			objects[ 1] = ( ( null == values) ? "" : values[ i]);
			
			defaultTableModel.addRow( objects);
		}
	}

	/**
	 * @param parent
	 */
	private void setup_returnTypeTextField(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		JLabel label = create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.common.functional.object.label.return.type"),
			false);
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.get( 0).add( label);

		_returnTypeTextField = create_textField( _standardControlWidth, false);
		_returnTypeTextField.setEditable( false);
		panel.add( _returnTypeTextField);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_returnValueTextField(JPanel parent) {
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_parameterTable(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_parameterTable = new ParameterTable( _color, _role, _owner, _parent);
		if ( !_parameterTable.setup())
			return false;

		_parameterTableScrollPane = new JScrollPane();
		_parameterTableScrollPane.getViewport().setView( _parameterTable);
		panel.add( _parameterTableScrollPane);

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width1 = _spotCheckBox.getPreferredSize().width;

		width1 = Math.max( width1, _spotVariableCheckBox.getPreferredSize().width);

		for ( JLabel label:_labels.get( 0)) {
			if ( null == label)
				continue;

			width1 = Math.max( width1, label.getPreferredSize().width);
		}

		_spotCheckBox.setPreferredSize( new Dimension(
			width1, _spotCheckBox.getPreferredSize().height));

		_spotVariableCheckBox.setPreferredSize( new Dimension(
			width1, _spotVariableCheckBox.getPreferredSize().height));

		for ( JLabel label:_labels.get( 0)) {
			if ( null == label)
				continue;

			label.setPreferredSize( new Dimension( width1, label.getPreferredSize().height));
		}


		_dummy.setPreferredSize( new Dimension(
			width1 + _classVriableComboBox.getPreferredSize().width + 5,
			_dummy.getPreferredSize().height));


		int width2 = 0;
		for ( JLabel label:_labels.get( 1))
			width2 = Math.max( width2, label.getPreferredSize().width);

		for ( JLabel label:_labels.get( 1))
			label.setPreferredSize( new Dimension( width2, label.getPreferredSize().height));

		// TODO 2012.8.1
		_methodComboBox.setPreferredSize( new Dimension( 2 * _standardControlWidth + width2 + 10, _methodComboBox.getPreferredSize().height));

		_parameterTableScrollPane.setPreferredSize( new Dimension(
			width1 + _standardControlWidth + 5, 120));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#reset(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void reset(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		CommonTool.update( spotVariableComboBox, !_spotCheckBox.isSelected() ? get_agent_spot_variable_names( false) : get_spot_spot_variable_names( false));

		super.reset(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		objectSelector.setEnabled( _spotCheckBox.isSelected());

		// TODO Auto-generated method stub
		if ( Environment.get_instance().is_exchange_algebra_enable())
			CommonTool.update( _classVriableComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? LayerManager.get_instance().get_agent_object_names( new String[] { "class variable", "exchange algebra"}, false) : LayerManager.get_instance().get_spot_object_names( new String[] { "class variable", "exchange algebra"}, false));
		else
			CommonTool.update( _classVriableComboBox, ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_class_variable_names( false) : get_spot_class_variable_names( false));

		_probabilityNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_probability_names( true) : get_spot_probability_names( true);
		_keywordNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_keyword_names( true) : get_spot_keyword_names( true);
		_numberObjectNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_number_object_names( true) : get_spot_number_object_names( true);
		_integerNumberObjectNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_integer_number_object_names( true) : get_spot_integer_number_object_names( true);
		_realNumberObjectNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_real_number_object_names( true) : get_spot_real_number_object_names( true);
		_collectionNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_collection_names( true) : get_spot_collection_names( true);
		_listNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_list_names( true) : get_spot_list_names( true);
		_mapNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_map_names( true) : get_spot_map_names( true);
		if ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) 
			_classVariableNames = ( Environment.get_instance().is_exchange_algebra_enable()
				? LayerManager.get_instance().get_agent_object_names( new String[] { "class variable", "exchange algebra"}, true)
				: LayerManager.get_instance().get_agent_object_names( "class variable", true));
		else
			_classVariableNames = ( Environment.get_instance().is_exchange_algebra_enable()
				? LayerManager.get_instance().get_spot_object_names( new String[] { "class variable", "exchange algebra"}, true)
				: LayerManager.get_instance().get_spot_object_names( "class variable", true));
		_fileVariableNames = ( !_spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? get_agent_file_names( true) : get_spot_file_names( true);

		_parameterTable.update(
			get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox),
			_probabilityNames,
			_keywordNames,
			_numberObjectNames,
			_integerNumberObjectNames,
			_realNumberObjectNames,
			_collectionNames,
			_listNames,
			_mapNames,
			_classVariableNames,
			_fileVariableNames);
//			get_agent_probability_names( true),
//			get_agent_keyword_names( true),
//			get_agent_number_object_names( true),
//			get_agent_integer_number_object_names( true),
//			get_agent_real_number_object_names( true),
//			get_agent_collection_names( true),
//			get_agent_list_names( true),
//			get_agent_map_names( true));
//		_parameterTable.update(
//			get_agent_probability_names( false),
//			get_agent_keyword_names( true),
//			get_agent_number_object_names( false),
//			get_agent_integer_number_object_names( false),
//			get_agent_real_number_object_names( false),
//			get_agent_collection_names( false),
//			get_agent_list_names( false),
//			get_agent_map_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(objectSelector, spotVariableCheckBox, spotVariableComboBox);

		// TODO Auto-generated method stub
		if ( Environment.get_instance().is_exchange_algebra_enable())
			CommonTool.update( _classVriableComboBox, LayerManager.get_instance().get_spot_object_names( new String[] { "class variable", "exchange algebra"}, false));
		else
			CommonTool.update( _classVriableComboBox, get_spot_class_variable_names( false));

		_probabilityNames = get_spot_probability_names( true);
		_keywordNames = get_spot_keyword_names( true);
		_numberObjectNames = get_spot_number_object_names( true);
		_integerNumberObjectNames = get_spot_integer_number_object_names( true);
		_realNumberObjectNames = get_spot_real_number_object_names( true);
		_collectionNames = get_spot_collection_names( true);
		_listNames = get_spot_list_names( true);
		_mapNames = get_spot_map_names( true);
		_classVariableNames = ( Environment.get_instance().is_exchange_algebra_enable()
			? LayerManager.get_instance().get_spot_object_names( new String[] { "class variable", "exchange algebra"}, true)
			: LayerManager.get_instance().get_spot_object_names( "class variable", true));
		_fileVariableNames = get_spot_file_names( true);

		_parameterTable.update(
			get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox),
			_probabilityNames,
			_keywordNames,
			_numberObjectNames,
			_integerNumberObjectNames,
			_realNumberObjectNames,
			_collectionNames,
			_listNames,
			_mapNames,
			_classVariableNames,
			_fileVariableNames);
//			get_spot_probability_names( true),
//			get_spot_keyword_names( true),
//			get_spot_number_object_names( true),
//			get_spot_integer_number_object_names( true),
//			get_spot_real_number_object_names( true),
//			get_spot_collection_names( true),
//			get_spot_list_names( true),
//			get_spot_map_names( true));
//		_parameterTable.update(
//			get_spot_probability_names( false),
//			get_spot_keyword_names( true),
//			get_spot_number_object_names( false),
//			get_spot_integer_number_object_names( false),
//			get_spot_real_number_object_names( false),
//			get_spot_collection_names( false),
//			get_spot_list_names( false),
//			get_spot_map_names( false));
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector, soars.common.utility.swing.button.CheckBox, soars.common.utility.swing.combo.ComboBox)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector, CheckBox spotVariableCheckBox, ComboBox spotVariableComboBox) {
		if ( !objectSelector.equals( _spotSelector))
			return;

		super.update(spotObject, number, objectSelector, spotVariableCheckBox, spotVariableComboBox);

		// TODO Auto-generated method stub
		if ( Environment.get_instance().is_exchange_algebra_enable())
			CommonTool.update( _classVriableComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( new String[] { "class variable", "exchange algebra"}, number, false) : LayerManager.get_instance().get_spot_object_names( new String[] { "class variable", "exchange algebra"}, false));
		else
			CommonTool.update( _classVriableComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "class variable", number, false) : get_spot_class_variable_names( false));

		_probabilityNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "probability", number, true) : get_spot_probability_names( true);
		_keywordNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, true) : get_spot_keyword_names( true);
		_numberObjectNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "number object", number, true) : get_spot_number_object_names( true);
		_integerNumberObjectNames = !spotVariableCheckBox.isSelected() ? spotObject.get_number_object_names( "integer", number, true) : get_spot_integer_number_object_names( true);
		_realNumberObjectNames = !spotVariableCheckBox.isSelected() ? spotObject.get_number_object_names( "real number", number, true) : get_spot_real_number_object_names( true);
		_collectionNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "collection", number, true) : get_spot_collection_names( true);
		_listNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "list", number, true) : get_spot_list_names( true);
		_mapNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "map", number, true) : get_spot_map_names( true);
		if ( !spotVariableCheckBox.isSelected()) 
			_classVariableNames = ( Environment.get_instance().is_exchange_algebra_enable()
				? spotObject.get_object_names( new String[] { "class variable", "exchange algebra"}, number, true)
				: spotObject.get_object_names( "class variable", number, true));
		else
			_classVariableNames = ( Environment.get_instance().is_exchange_algebra_enable()
				? LayerManager.get_instance().get_spot_object_names( new String[] { "class variable", "exchange algebra"}, true)
				: LayerManager.get_instance().get_spot_object_names( "class variable", true));
		_fileVariableNames = !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "file", number, true) : get_spot_file_names( true);

		_parameterTable.update(
			get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox),
			_probabilityNames,
			_keywordNames,
			_numberObjectNames,
			_integerNumberObjectNames,
			_realNumberObjectNames,
			_collectionNames,
			_listNames,
			_mapNames,
			_classVariableNames,
			_fileVariableNames);
//			spotObject.get_object_names( "probability", number, true),
//			spotObject.get_object_names( "keyword", number, true),
//			spotObject.get_object_names( "number object", number, true),
//			spotObject.get_number_object_names( "integer", number, true),
//			spotObject.get_number_object_names( "real number", number, true),
//			spotObject.get_object_names( "collection", number, true),
//			spotObject.get_object_names( "list", number, true),
//			spotObject.get_object_names( "map", number, true));
//		_parameterTable.update(
//			spotObject.get_object_names( "probability", number, false),
//			spotObject.get_object_names( "keyword", number, true),
//			spotObject.get_object_names( "number object", number, false),
//			spotObject.get_number_object_names( "integer", number, false),
//			spotObject.get_number_object_names( "real number", number, false),
//			spotObject.get_object_names( "collection", number, true),
//			spotObject.get_object_names( "list", number, true),
//			spotObject.get_object_names( "map", number, true));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(ObjectSelector objectSelector) {
		update( objectSelector, _spotVariableCheckBox, _spotVariableComboBox);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#update(soars.application.visualshell.object.entiy.spot.SpotObject, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	protected void update(SpotObject spotObject, String number, ObjectSelector objectSelector) {
		update( spotObject, number, objectSelector, _spotVariableCheckBox, _spotVariableComboBox);
	}

	/**
	 * 
	 */
	private void initialize() {
		if ( _role instanceof AgentRole) {
			_spotCheckBox.setSelected( false);
			_spotSelector.setEnabled( false);
		} else {
			_spotCheckBox.setSelected( true);
			_spotCheckBox.setEnabled( false);
			_spotSelector.setEnabled( true);
		}
	}

	/**
	 * 
	 */
	protected void set_handler() {
		_spotSelector.set_handler( this);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		reset( _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);
		super.on_setup_completed();
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.panel.StandardPanel#on_ok(java.awt.event.ActionEvent)
	 */
	@Override
	protected void on_ok(ActionEvent actionEvent) {
		_parent.on_apply( this, actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#set(soars.application.visualshell.object.role.base.rule.base.Rule)
	 */
	@Override
	public boolean set(Rule rule) {
		initialize();

		if ( null == rule || !_type.equals( rule._type) || rule._value.equals( "")) {
			set_handler();
			return false;
		}

		FunctionalObject functionalObject = FunctionalObjectRule.get_functionalObject( rule._value);
		if ( null == functionalObject) {
			set_handler();
			return false;
		}



		if ( !set( functionalObject._spots[ 0], functionalObject._spots[ 1], _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox)) {
			set_handler();
			return false;
		}

		_classVriableComboBox.setSelectedItem( functionalObject._classVariable);



		ClassVariableObject classVariableObject;
		if ( CommonRuleManipulator.get_semantic_prefix( functionalObject._spots).equals( ""))
			classVariableObject = get_agent_class_variable( functionalObject._classVariable);
		else
			classVariableObject = get_spot_class_variable( functionalObject._classVariable);
		if ( null == classVariableObject) {
			if ( !Environment.get_instance().is_exchange_algebra_enable()) {
				set_handler();
				return false;
			} else {
				if ( CommonRuleManipulator.is_object( "exchange algebra", CommonRuleManipulator.get_full_prefix( functionalObject._spots) + functionalObject._classVariable))
					// TODO もし交換代数なら、、、
					classVariableObject = new ClassVariableObject( functionalObject._classVariable, Constant._exchangeAlgebraJarFilename, Constant._exchangeAlgebraClassname);
				else {
					set_handler();
					return false;
				}
			}
		}

		TreeMap<String, MethodObject> methodObjectMap = _methodObjectMapMap.get( classVariableObject._classname);
		if ( null == methodObjectMap) {
			set_handler();
			return false;
		}

		// TODO 2012.8.1
		MethodObject methodObject = null;
//		List<String> methodNames = new ArrayList<String>( methodObjectMap.keySet());
//		for ( String methodName:methodNames) {
//			
//		}
		Iterator iterator = methodObjectMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			String methodName = ( String)entry.getKey();
			MethodObject mo = ( MethodObject)entry.getValue();
			if ( mo.equals( methodName, functionalObject)) {
				_methodComboBox.setSelectedItem( methodName);
				methodObject = mo;
				break;
			}
		}

		if ( null == methodObject) {
			set_handler();
			return false;
		}
//		MethodObject methodObject = methodObjectMap.get( functionalObject._method);
//		if ( null != methodObject)
//			_methodComboBox.setSelectedItem( functionalObject._method);
//		else {
//			int index = 1;
//			while ( true) {
//				String methodName = functionalObject._method + " - (" + index + ")";
//				methodObject = ( MethodObject)methodObjectMap.get( methodName);
//				if ( null == methodObject) {
//					set_handler();
//					return false;
//				}
//
//				if ( functionalObject.equals( methodObject)) {
//					_methodComboBox.setSelectedItem( methodName);
//					break;
//				}
//
//				++index;
//			}
//		}



		if ( !_parameterTable.set( functionalObject._parameters)) {
			set_handler();
			return false;
		}



		if ( null != functionalObject._returnValue) {
			if ( !functionalObject._returnValue[ 1].equals( methodObject._returnType)) {
				set_handler();
				return false;
			}

			set_return_value( functionalObject._returnValue[ 0]);
		}



		set_handler();

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.rule.edit.tab.base.RulePropertyPanelBase#get()
	 */
	@Override
	public Rule get() {
		String classVariable = ( String)_classVriableComboBox.getSelectedItem();
		if ( null == classVariable || classVariable.equals( ""))
			return null;

		String method = ( String)_methodComboBox.getSelectedItem();
		if ( null == method || method.equals( ""))
			return null;

		// TODO 2012.8.1
		String[] words = method.split( "\\(");
		//String[] words = method.split( " ");
		if ( null == words || 0 == words.length)
			return null;

		method = ( " " + words[ 0]);

		// TODO Auto-generated method stub
		String parameters = _parameterTable.get();
		if ( null == parameters)
			return null;

		String returnValue = "";
		if ( !_returnType.equals( "") && !_returnType.equals( "void"))
			returnValue = ( " return: " + get_return_value() + "=" + _returnType);

		String spot = get( _spotCheckBox, _spotSelector, _spotVariableCheckBox, _spotVariableComboBox);

		return Rule.create( _kind, _type, spot + classVariable + method + parameters + returnValue);
	}
}
