/**
 * 
 */
package soars.application.animator.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import soars.common.soars.property.Property;
import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class SelectAnimationDlg extends Dialog {

	/**
	 * 
	 */
	private int _minimumWidth;

	/**
	 * 
	 */
	private int _height;

	/**
	 * 
	 */
	private TreeMap<String, Property> _propertyMap = null;

	/**
	 * 
	 */
	private File _file = null;

	/**
	 * 
	 */
	private Map<String, String> _simulationMap = new HashMap<String, String>();

	/**
	 * 
	 */
	private Map<String, String> _animationMap = new HashMap<String, String>();

	/**
	 * 
	 */
	private JTextField _filenameTextField = null;

	/**
	 * 
	 */
	private JComboBox _simulationComboBox = null;

	/**
	 * 
	 */
	private JComboBox _animationComboBox = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	public String _id = "";

	/**
	 * 
	 */
	public String _index = "";

	/**
	 * 
	 */
	private boolean _reverse = true;

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param propertyMap 
	 * @param file 
	 */
	public SelectAnimationDlg(Frame arg0, String arg1, boolean arg2, TreeMap<String, Property> propertyMap, File file) {
		super(arg0, arg1, arg2);
		_propertyMap = propertyMap;
		_file = file;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if (!super.on_init_dialog())
			return false;


		//setResizable( false);


		getContentPane().setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		if ( !setup_north_panel( northPanel))
			return false;

		getContentPane().add( northPanel, "North");


		JPanel southPanel = new JPanel();
		southPanel.setLayout( new BoxLayout( southPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		insert_horizontal_glue( southPanel);

		setup_ok_and_cancel_button(
			panel,
			ResourceManager.get_instance().get( "dialog.ok"),
			ResourceManager.get_instance().get( "dialog.cancel"),
			false, false);
		southPanel.add( panel);

		insert_horizontal_glue( southPanel);

		getContentPane().add( southPanel, "South");


		adjust();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_panel(JPanel parent) {
		insert_horizontal_glue( parent);

		if ( !setup_filenameTextField( parent))
			return false;

		insert_horizontal_glue( parent);

		if ( !setup_simulationComboBox( parent))
			return false;

		insert_horizontal_glue( parent);

		if ( !setup_animationComboBox( parent))
			return false;

		if ( 0 < _simulationComboBox.getItemCount())
			_simulationComboBox.setSelectedIndex( 0);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_filenameTextField(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "select.animation.dialog.file.name.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_filenameTextField = new JTextField();
		if ( null != _file)
			_filenameTextField.setText( _file.getName());
		_filenameTextField.setEditable( false);
		panel.add( _filenameTextField);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_simulationComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "select.animation.dialog.simulation.combo.box.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_simulationComboBox = new JComboBox();
		setup_simulationComboBox();
		_simulationComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				update_animationComboBox();
			}
		});
		panel.add( _simulationComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void setup_simulationComboBox() {
		Set<String> set = _propertyMap.keySet();
		String[] ids = set.toArray( new String[ 0]);
		if ( _reverse) {
			for ( int i = ids.length - 1; i >= 0; --i)
				append_item_to_simulationComboBox( _propertyMap.get( ids[ i]));
		} else {
			for ( String id:ids)
				append_item_to_simulationComboBox( _propertyMap.get( id));
		}
	}

	/**
	 * @param property
	 */
	private void append_item_to_simulationComboBox(Property property) {
		if ( null == property)
			return;

		String item = ( ( !property._title.equals( "") ? property._title : ResourceManager.get_instance().get( "select.animation.dialog.no.title")) + " - [" + new Timestamp( Long.parseLong( property._id)).toString() + "]");
		_simulationComboBox.addItem( item);
		_simulationMap.put( item, property._id);
	}

	/**
	 * 
	 */
	protected void update_animationComboBox() {
		_animationComboBox.removeAllItems();

		String item = ( String)_simulationComboBox.getSelectedItem();
		if ( null == item || item.equals( ""))
			return;

		String id = _simulationMap.get( item);
		if ( null == id)
			return;

		Property property = _propertyMap.get( id);
		if ( null == property)
			return;

		Set<String> set = property._propertyMap.keySet();
		String[] indices = set.toArray( new String[ 0]);
		if ( _reverse) {
			for ( int i = indices.length - 1; i >= 0; --i)
				append_item_to_animationComboBox( property._propertyMap.get( indices[ i]));
		} else {
			for ( String index:indices)
				append_item_to_animationComboBox( property._propertyMap.get( index));
		}
	}

	/**
	 * @param property
	 */
	private void append_item_to_animationComboBox(Property property) {
		if ( null == property)
			return;

		String item = ( ( !property._title.equals( "") ? property._title : ResourceManager.get_instance().get( "select.animation.dialog.no.title")) + " - [" + new Timestamp( Long.parseLong( property._id)).toString() + "]");
		_animationComboBox.addItem( item);
		_animationMap.put( item, property._id);
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_animationComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JLabel label = new JLabel( ResourceManager.get_instance().get( "select.animation.dialog.animation.combo.box.label"));
		label.setHorizontalAlignment( SwingConstants.RIGHT);
		panel.add( label);
		_labels.add( label);

		panel.add( Box.createHorizontalStrut( 5));

		_animationComboBox = new JComboBox();
		panel.add( _animationComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);

		return true;
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = 0;
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);

		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));

		width = Math.max( _simulationComboBox.getPreferredSize().width, _animationComboBox.getPreferredSize().width);
		_simulationComboBox.setPreferredSize( new Dimension( width, _simulationComboBox.getPreferredSize().height));
		_animationComboBox.setPreferredSize( new Dimension( width, _animationComboBox.getPreferredSize().height));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_setup_completed()
	 */
	protected void on_setup_completed() {
		_minimumWidth = getSize().width;
		_height = getSize().height;

		addComponentListener( new ComponentAdapter() {
			public void componentResized(ComponentEvent e){
				int width = getSize().width;
				setSize( ( _minimumWidth > width) ? _minimumWidth : width, _height);
			}
		});
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		String item = ( String)_simulationComboBox.getSelectedItem();
		if ( null == item || item.equals( ""))
			return;

		String id = _simulationMap.get( item);
		if ( null == id || id.equals( ""))
			return;

		item = ( String)_animationComboBox.getSelectedItem();
		if ( null == item || item.equals( ""))
			return;

		String index = _animationMap.get( item);
		if ( null == index || index.equals( ""))
			return;

		_id = id;
		_index = index;

		super.on_ok(actionEvent);
	}
}
