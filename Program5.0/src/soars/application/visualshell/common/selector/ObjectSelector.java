/*
 * Created on 2006/01/28
 */
package soars.application.visualshell.common.selector;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.combo.CommonComboBoxRenderer;
import soars.common.utility.swing.panel.StandardPanel;
import soars.common.utility.swing.spinner.INumberSpinnerHandler;
import soars.common.utility.swing.spinner.NumberSpinner;
import soars.common.utility.swing.window.Dialog;

/**
 * The agent or spot selector class.
 * @author kurata / SOARS project
 */
public class ObjectSelector extends JPanel implements INumberSpinnerHandler {

	/**
	 * Type of data which this object deals with("agent" or "spot")
	 */
	public String _type = "";

	/**
	 * Interface for the callback of this object.
	 */
	public IObjectSelectorHandler _objectSelectorHandler = null;

	/**
	 * Combo box for the object names.
	 */
	public ComboBox _objectNameComboBox = null;

	/**
	 * Spin control for the object numbers.
	 */
	public NumberSpinner _numberSpinner = null;

	/**
	 * 
	 */
	private String _previousName = "";

	/**
	 * 
	 */
	private String _previousNumber = "";

	/**
	 * 
	 */
	private boolean _ignore = false;

	/**
	 * Creates the instance of this class with the specified data.
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(LayoutManager layoutManager) {
		super();
		setup_layout( layoutManager);
		setup();
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, boolean contains_empty, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String[] names, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, names, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String[] names, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, names, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String[] names, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, names, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, String[] names, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, names, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, names, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, names, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( null, names, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, names, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _objectNameComboBox.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, boolean contains_empty, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String[] names, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, names, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String[] names, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, names, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String[] names, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, names, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, String[] names, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, names, color, right);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, tabbedPage);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, dialog);
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, names, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, names, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, contains_empty, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( null, names, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 */
	public ObjectSelector(String type, String full_name, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, names, color, right);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param tabbedPage the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, StandardPanel tabbedPage) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, tabbedPage);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, boolean contains_empty, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, contains_empty, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * Creates the instance of this class with the specified data.
	 * @param type the type of data which this object deals with("agent" or "spot")
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param comboBox_width the width of Combo box for the object names
	 * @param numberSpinner_width the width of Spin control for the object numbers
	 * @param color the display color
	 * @param right whether to right-align
	 * @param objectSelectorHandler
	 * @param layoutManager the interface for classes that know how to lay out Containers
	 * @param dialog the component which has this object
	 */
	public ObjectSelector(String type, String full_name, String[] names, int comboBox_width, int numberSpinner_width, Color color, boolean right, IObjectSelectorHandler objectSelectorHandler, LayoutManager layoutManager, Dialog dialog) {
		super();
		_type = type;
		_objectSelectorHandler = objectSelectorHandler;
		setup_layout( layoutManager);
		setup( full_name, names, color, right, dialog);
		_objectNameComboBox.setPreferredSize( new Dimension( comboBox_width, _objectNameComboBox.getPreferredSize().height));
		_numberSpinner.setPreferredSize( new Dimension( numberSpinner_width, _numberSpinner.getPreferredSize().height));
	}

	/**
	 * @param layoutManager
	 */
	private void setup_layout(LayoutManager layoutManager) {
		if ( null != layoutManager)
			setLayout( layoutManager);
		else
			setLayout( new BoxLayout( this, BoxLayout.X_AXIS));
	}

	/**
	 * Sets the specified interface for the callback of this object.
	 * @param objectSelectorHandler the specified interface
	 */
	public void set_handler(IObjectSelectorHandler objectSelectorHandler) {
		_objectSelectorHandler = objectSelectorHandler;
	}

	/**
	 * 
	 */
	private void setup() {
		_objectNameComboBox = new ComboBox();
		_objectNameComboBox.setEnabled( false);
		add( _objectNameComboBox);

		_numberSpinner = new NumberSpinner();
		_numberSpinner.setEnabled( false);
		add( _numberSpinner);
	}

	/**
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 */
	private void setup(String full_name, boolean contains_empty, Color color, boolean right) {
		String[] names = null;
		if ( _type.equals( "agent"))
			names = LayerManager.get_instance().get_agent_names( contains_empty);
		else
			names = LayerManager.get_instance().get_spot_names( contains_empty);

		setup( full_name, names, color, right);
	}

	/**
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 */
	private void setup(String full_name, String[] names, Color color, boolean right) {
		_objectNameComboBox = ComboBox.create( names, color, right, new CommonComboBoxRenderer( color, right));
		if ( 0 < _objectNameComboBox.getItemCount())
			_objectNameComboBox.setSelectedIndex( 0);

		_objectNameComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_action_performed( arg0);
			}
		});

		add( _objectNameComboBox);


		_numberSpinner = new NumberSpinner( color, this);
		set( full_name);

		add( _numberSpinner);
	}

	/**
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param tabbedPage the component which has this object
	 */
	private void setup(String full_name, boolean contains_empty, Color color, boolean right, StandardPanel tabbedPage) {
		String[] names = null;
		if ( _type.equals( "agent"))
			names = LayerManager.get_instance().get_agent_names( contains_empty);
		else
			names = LayerManager.get_instance().get_spot_names( contains_empty);

		setup( full_name, names, color, right, tabbedPage);
	}

	/**
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param tabbedPage the component which has this object
	 */
	private void setup(String full_name, String[] names, Color color, boolean right, StandardPanel tabbedPage) {
		_objectNameComboBox = ComboBox.create( names, color, right, new CommonComboBoxRenderer( color, right), tabbedPage);
		if ( 0 < _objectNameComboBox.getItemCount())
			_objectNameComboBox.setSelectedIndex( 0);

		_objectNameComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_action_performed( arg0);
			}
		});

		add( _objectNameComboBox);


		_numberSpinner = new NumberSpinner( color, this, tabbedPage);
		set( full_name);

		add( _numberSpinner);
	}

	/**
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name 
	 * @param color the display color
	 * @param right whether to right-align
	 * @param dialog the component which has this object
	 */
	private void setup(String full_name, boolean contains_empty, Color color, boolean right, Dialog dialog) {
		String[] names = null;
		if ( _type.equals( "agent"))
			names = LayerManager.get_instance().get_agent_names( contains_empty);
		else
			names = LayerManager.get_instance().get_spot_names( contains_empty);

		setup( full_name, names, color, right, dialog);
	}

	/**
	 * @param full_name the name to be selected
	 * @param names the names of the objects
	 * @param color the display color
	 * @param right whether to right-align
	 * @param dialog the component which has this object
	 */
	private void setup(String full_name, String[] names, Color color, boolean right, Dialog dialog) {
		_objectNameComboBox = ComboBox.create( names, color, right, new CommonComboBoxRenderer( color, right), dialog);
		if ( 0 < _objectNameComboBox.getItemCount())
			_objectNameComboBox.setSelectedIndex( 0);

		_objectNameComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_action_performed( arg0);
			}
		});

		add( _objectNameComboBox);


		_numberSpinner = new NumberSpinner( color, this, dialog);
		set( full_name);

		add( _numberSpinner);
	}

	/**
	 * @param full_name the name to be selected
	 * @param contains_empty whether this component contains the empty name
	 */
	public void update(String full_name, boolean contains_empty) {
		update( contains_empty);
		set( full_name);
	}

	/**
	 * @param contains_empty whether this component contains the empty name
	 */
	public void update(boolean contains_empty) {
		String[] names = null;
		if ( _type.equals( "agent"))
			names = LayerManager.get_instance().get_agent_names( contains_empty);
		else
			names = LayerManager.get_instance().get_spot_names( contains_empty);

		CommonTool.update( _objectNameComboBox, names);
	}

	/**
	 *  
	 */
	public void cleanup() {
		_objectNameComboBox.removeAllItems();
		_numberSpinner.set_value( 1);
		_numberSpinner.setEnabled( false);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_action_performed(ActionEvent actionEvent) {
		_ignore = true;

		String name = ( String)_objectNameComboBox.getSelectedItem();
		if ( null == name || name.equals( ""))
			_numberSpinner.setEnabled( false);
		else {
			EntityBase entityBase = null;
			if ( _type.equals( "agent"))
				entityBase = LayerManager.get_instance().get_agent( name);
			else
				entityBase = LayerManager.get_instance().get_spot( name);

			if ( entityBase._number.equals( "")) {
				_numberSpinner.setEnabled( false);
			} else {
				int max_number = Integer.parseInt( entityBase._number);
				_numberSpinner.set_maximum( max_number);

				int number = _numberSpinner.get_value();
				if ( number > max_number)
					_numberSpinner.setValue( new Integer( max_number));

				_numberSpinner.setEnabled( true);
			}
		}

		if ( null != _objectSelectorHandler && !_previousName.equals( name)) {
			String number = ( _numberSpinner.isEnabled() ? String.valueOf( _numberSpinner.get_value()) : "");
			String full_name = ( ( null == name || name.equals( "")) ? "" : ( name + number));
			_objectSelectorHandler.changed( ( ( null == name) ? "" : name), number, full_name, this);
			_previousName = ( ( null == name) ? "" : name);
		}

		_ignore = false;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.spinner.INumberSpinnerHandler#changed(java.lang.String, soars.common.utility.swing.spinner.NumberSpinner)
	 */
	public void changed(String number, NumberSpinner numberSpinner) {
		if ( _ignore)
			return;

		if ( null != _objectSelectorHandler && !_previousNumber.equals( number)) {
			String name = ( String)_objectNameComboBox.getSelectedItem();
			String full_name = ( ( null == name || name.equals( "")) ? "" : ( name + number));
			_objectSelectorHandler.changed( ( ( null == name) ? "" : name), number, full_name, this);
			_previousNumber = number;
		}
	}

	/**
	 * Selects the specified name of the object.
	 * @param full_name the name to be selected
	 */
	public void set(String full_name) {
		EntityBase entityBase = null;
		if ( null == full_name || full_name.equals( "")) {
			_objectNameComboBox.setSelectedItem( "");
			_numberSpinner.setEnabled( false);
		} else {
			if ( _type.equals( "agent"))
				entityBase = LayerManager.get_instance().get_agent( full_name);
			else
				entityBase = LayerManager.get_instance().get_spot( full_name);

			if ( null != entityBase) {
				_objectNameComboBox.setSelectedItem( full_name);
				_numberSpinner.setEnabled( false);
				_previousName = full_name;
			} else {
				if ( _type.equals( "agent"))
					entityBase = LayerManager.get_instance().get_agent_has_this_name( full_name);
				else
					entityBase = LayerManager.get_instance().get_spot_has_this_name( full_name);

				if ( null == entityBase) {
					_objectNameComboBox.setSelectedItem( full_name);
					_numberSpinner.setEnabled( false);
					_previousName = full_name;
				} else {
					String number_text = full_name.substring( entityBase._name.length());
					int number = Integer.parseInt( number_text);
					int max_number = Integer.parseInt( entityBase._number);
					if ( 1 > number || number > max_number) {
						_objectNameComboBox.setSelectedItem( full_name);
						_numberSpinner.setEnabled( false);
						_previousName = full_name;
					} else {
						_objectNameComboBox.setSelectedItem( entityBase._name);
						_numberSpinner.set_value( number);
						_numberSpinner.set_maximum( max_number);
						_previousName = full_name;
						_previousNumber = number_text;
					}
				}
			}
		}
	}

	/**
	 * Selects the specified name and number.
	 * @param name the specified name
	 * @param number the specified number
	 */
	public void set(String name, String number) {
		_objectNameComboBox.setSelectedItem( name);
		_numberSpinner.set_value( Integer.parseInt( number));
	}

	/**
	 * Selects the specified name and number.
	 * @param name the specified name
	 * @param number the specified number
	 */
	public void set(String name, int number) {
		_objectNameComboBox.setSelectedItem( name);
		_numberSpinner.set_value( number);
	}

	/**
	 * Returns the selected name of the object.
	 * @return the selected name of the object
	 */
	public String get() {
		String name = ( String)_objectNameComboBox.getSelectedItem();
		if ( null == name || name.equals( ""))
			return "";

		if ( _numberSpinner.isEnabled())
			name += String.valueOf( _numberSpinner.get_value());

		return name;
	}

	/**
	 * Returns the selected name.
	 * @return the selected name
	 */
	public String get_name() {
		String name = ( String)_objectNameComboBox.getSelectedItem();
		if ( null == name || name.equals( ""))
			return "";

		return name;
	}

	/**
	 * Returns the selected number.
	 * @return the selected number
	 */
	public String get_number() {
		if ( !_numberSpinner.isEnabled())
			return "";

		return String.valueOf( _numberSpinner.get_value());
	}

	/**
	 * Sets whether or not this component is enabled.
	 * @param enable true if this component should be enabled, false otherwise
	 */
	public void setEnabled(boolean enable) {
		_objectNameComboBox.setEnabled( enable);
		if ( !enable)
			_numberSpinner.setEnabled( enable);
		else {
			String name = ( String)_objectNameComboBox.getSelectedItem();
			if ( null == name || name.equals( ""))
				return;

			EntityBase entityBase;
			if ( _type.equals( "agent"))
				entityBase = LayerManager.get_instance().get_agent( name);
			else
				entityBase = LayerManager.get_instance().get_spot( name);

			if ( entityBase._number.equals( ""))
				return;

			_numberSpinner.setEnabled( enable);
		}
	}

	/**
	 * Selects the first item of the Combo box for the object names.
	 */
	public void selectFirstItem() {
		String name = ( String)_objectNameComboBox.getItemAt( 0);
		if ( null == name)
			return;

		if ( name.equals( "")) {
			_objectNameComboBox.setSelectedItem( "");
			_numberSpinner.setEnabled( false);
		}

		EntityBase entityBase;
		if ( _type.equals( "agent"))
			entityBase = LayerManager.get_instance().get_agent( name);
		else
			entityBase = LayerManager.get_instance().get_spot( name);

		name += ( entityBase._number.equals( "") ? "" : "1");

		set( name);
	}

	/**
	 * Returns true if this object has no name.
	 * @return true if this object has no name
	 */
	public boolean is_empty() {
		return ( 0 == _objectNameComboBox.getItemCount());
	}

	/**
	 * Resize this component with the specified width.
	 * @param the specified width
	 */
	public void setWidth(int width) {
		_objectNameComboBox.setPreferredSize(
			new Dimension( width - _numberSpinner.getPreferredSize().width, _objectNameComboBox.getPreferredSize().height));
	}
}
