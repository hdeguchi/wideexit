/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.common.tool.CommonTool;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanelBase;
import soars.application.visualshell.object.role.base.object.legacy.command.ExchangeAlgebraCommand;
import soars.common.utility.swing.button.CheckBox;
import soars.common.utility.swing.button.RadioButton;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.tool.SwingTool;

/**
 * @author kurata
 *
 */
public class BasePanel extends JPanel {

	/**
	 * 
	 */
	private List<JComponent> _components = new ArrayList<JComponent>();

	/**
	 * 
	 */
	private ComboBox _baseTypeComboBox = null;

	/**
	 * 
	 */
	private List<JLabel> _labels = new ArrayList<JLabel>();

	/**
	 * 
	 */
	public TextField _baseTextField = null;

	/**
	 * 
	 */
	public ComboBox _keywordComboBox = null;

	/**
	 * 
	 */
	public HatPanel _hatPanel = null;

	/**
	 * 
	 */
	public List<ItemPanel> _itemPanels = new ArrayList<ItemPanel>();

	/**
	 * 
	 */
	public Map<String, JPanel> _panelMap = new HashMap<String, JPanel>();

	/**
	 * 
	 */
	private boolean _margin = false;

	/**
	 * 
	 */
	private RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * @param margin
	 * @param rulePropertyPanelBase
	 */
	public BasePanel(boolean margin, RulePropertyPanelBase rulePropertyPanelBase) {
		super();
		_margin = margin;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/**
	 * @param radioButton
	 * @return
	 */
	public boolean create(RadioButton radioButton) {
		setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		if ( !setup_baseTypeComboBox( radioButton, northPanel))
			return false;

		SwingTool.insert_vertical_strut( northPanel, 5);

		if ( !setup1( northPanel))
			return false;

		if ( !setup2( northPanel))
			return false;

		if ( !setup3( northPanel))
			return false;

		add( northPanel, "North");

		return true;
	}

	/**
	 * @param radioButton
	 * @param parent
	 * @return
	 */
	private boolean setup_baseTypeComboBox(RadioButton radioButton, JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		if ( null != radioButton)
			panel.add( radioButton);
		else {
			if ( _margin) {
				JLabel dummy = new JLabel();
				panel.add( dummy);
				_components.add( dummy);
			}
		}

		JLabel label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.base.type"),
			true);
		panel.add( label);
		_labels.add( label);

		_baseTypeComboBox = _rulePropertyPanelBase.create_comboBox(
			new String[] {
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"),
				ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keys")
			}, false);
		_baseTypeComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				select( ( String)_baseTypeComboBox.getSelectedItem());
				revalidate();
				repaint();
			}
		});
		panel.add( _baseTypeComboBox);

		parent.add( panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup1(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		if ( _margin) {
			JLabel dummy = new JLabel();
			panel.add( dummy);
			_components.add( dummy);
		}

		JLabel label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"),
			true);
		panel.add( label);
		_labels.add( label);

		_baseTextField = _rulePropertyPanelBase.create_textField( new TextExcluder( Constant._prohibitedCharacters12), false);
		panel.add( _baseTextField);

		parent.add( panel);

		_panelMap.put( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate"), panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup2(JPanel parent) {
		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		if ( _margin) {
			JLabel dummy = new JLabel();
			panel.add( dummy);
			_components.add( dummy);
		}

		JLabel label = _rulePropertyPanelBase.create_label(
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"),
			true);
		panel.add( label);
		_labels.add( label);

		_keywordComboBox = _rulePropertyPanelBase.create_comboBox( null, false);
		panel.add( _keywordComboBox);

		parent.add( panel);

		_panelMap.put( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"), panel);

		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup3(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		String[] labels = new String[] {
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.name"),
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.hat"),
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.unit"),
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.time"),
			ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.subject")
		};

		for ( int i = 0; i < labels.length; ++i) {
			if ( 1 == i) {
				_hatPanel = new HatPanel( _margin, labels[ i], _rulePropertyPanelBase);
				if ( !_hatPanel.create())
					return false;

				northPanel.add( _hatPanel);
			} else {
				ItemPanel itemPanel = new ItemPanel( _margin, labels[ i],
					( ( 3 == i)  ?
						new String[] { 
							ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"),
							ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.time.variable"),
							ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate.time"),
							ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate")} :
						new String[] { 
							ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keyword"),
							ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.immediate")}),
					_rulePropertyPanelBase);
				if ( !itemPanel.create())
					return false;

				northPanel.add( itemPanel);
				_itemPanels.add( itemPanel);
			}

			if ( i < labels.length - 1)
				SwingTool.insert_vertical_strut( northPanel, 5);
		}

		panel.add( northPanel, "North");

		parent.add( panel);

		_panelMap.put( ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.keys"), panel);

		return true;
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_width(int width) {
		for ( JLabel label:_labels)
			width = Math.max( width, label.getPreferredSize().width);
		for ( ItemPanel itemPanel:_itemPanels)
			width = Math.max( width, itemPanel.get_width( width));
		width = Math.max( width, _hatPanel.get_width( width));
		return width;
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		for ( JLabel label:_labels)
			label.setPreferredSize( new Dimension( width, label.getPreferredSize().height));
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.adjust( width);
		_hatPanel.adjust( width);
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_margin_width(int width) {
		for ( JComponent component:_components)
			width = Math.max( width, component.getPreferredSize().width);
		for ( ItemPanel itemPanel:_itemPanels)
			width = Math.max( width, itemPanel.get_margin_width( width));
		width = Math.max( width, _hatPanel.get_margin_width( width));
		return width;
	}

	/**
	 * @param width
	 */
	public void adjust_margin_width(int width) {
		for ( JComponent component:_components)
			component.setPreferredSize( new Dimension( width, component.getPreferredSize().height));
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.adjust_margin_width( width);
		_hatPanel.adjust_margin_width( width);
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
		_baseTypeComboBox.setSelectedIndex( 0);
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.on_setup_completed();
		_hatPanel.on_setup_completed();
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		_baseTypeComboBox.setEnabled( enabled);
		_baseTextField.setEnabled( enabled);
		_keywordComboBox.setEnabled( enabled);
		for ( JLabel label:_labels)
			label.setEnabled( enabled);
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.setEnabled( enabled);
		_hatPanel.setEnabled( enabled);
		super.setEnabled(enabled);
	}

	/**
	 * @param selectedItem
	 */
	public void select(String selectedItem) {
		Set<String> baseTypes = _panelMap.keySet();
		for ( String baseType:baseTypes)
			_panelMap.get( baseType).setVisible( baseType.equals( selectedItem));
	}

	/**
	 * @param spotCheckBox
	 * @param spotVariableCheckBox
	 */
	public void reset(CheckBox spotCheckBox, CheckBox spotVariableCheckBox) {
		CommonTool.update( _keywordComboBox, ( !spotCheckBox.isSelected() && !spotVariableCheckBox.isSelected()) ? RulePropertyPanelBase.get_agent_keyword_names( false) : RulePropertyPanelBase.get_spot_keyword_names( false));
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.reset( spotCheckBox, spotVariableCheckBox);
	}

	/**
	 * 
	 */
	public void update() {
		CommonTool.update( _keywordComboBox, RulePropertyPanelBase.get_spot_keyword_names( false));
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.update();
	}

	/**
	 * @param spotObject
	 * @param number
	 * @param spotVariableCheckBox
	 */
	public void update(SpotObject spotObject, String number, CheckBox spotVariableCheckBox) {
		CommonTool.update( _keywordComboBox, !spotVariableCheckBox.isSelected() ? spotObject.get_object_names( "keyword", number, false) : RulePropertyPanelBase.get_spot_keyword_names( false));
		for ( ItemPanel itemPanel:_itemPanels)
			itemPanel.update( spotObject, number, spotVariableCheckBox);
	}

	/**
	 * @param kind
	 * @param prefix
	 * @param elements
	 * @return
	 */
	public boolean set(int kind, String prefix, String[] elements) {
		_baseTypeComboBox.setSelectedIndex( kind);
		switch ( kind) {
			case 0:
				if ( elements[ 2].startsWith( "$"))
					_baseTextField.setText( elements[ 2]);
				else {
					if ( !ExchangeAlgebraCommandPropertyPanelBase.set_base( _baseTextField, elements[ 2]))
						return false;
				}
				break;
			case 1:
				_keywordComboBox.setSelectedItem( elements[ 2]);
				break;
			case 2:
				if ( !set_base_to_panels( prefix, elements))
					return false;

				break;
		default:
			return false;
		}
		return true;
	}

	/**
	 * @param prefix
	 * @param elements
	 * @return
	 */
	private boolean set_base_to_panels(String prefix, String[] elements) {
		if ( !_itemPanels.get( 0).set( prefix, elements[ 2]))
			return false;

		if ( !_hatPanel.set( elements[ 3]))
			return false;

		if ( !_itemPanels.get( 1).set( prefix, elements[ 4]))
			return false;

		if ( !_itemPanels.get( 2).set( prefix, elements[ 5]))
			return false;

		if ( !_itemPanels.get( 3).set( prefix, elements[ 6]))
			return false;

		return true;
	}

	/**
	 * @return
	 */
	public String get() {
		switch ( _baseTypeComboBox.getSelectedIndex()) {
			case 0:
				if ( !ExchangeAlgebraCommand.is_value_correct( _baseTextField.getText())) {
					//JOptionPane.showMessageDialog( _parent,
					//	ResourceManager.get_instance().get( "edit.object.dialog.invalid.initial.value.error.message"),
					//	ResourceManager.get_instance().get( "edit.object.dialog.tree.keyword"),
					//	JOptionPane.ERROR_MESSAGE);
					return null;
				}

				if ( _baseTextField.getText().startsWith( "$"))
					return _baseTextField.getText();
				else {
					String base = ExchangeAlgebraCommandPropertyPanelBase.get_base( _baseTextField);
					if ( null == base)
						return null;

					ExchangeAlgebraCommandPropertyPanelBase.set_base( _baseTextField, base);
					return base;
				}
			case 1:
				String base = ( String)_keywordComboBox.getSelectedItem();
				if ( null == base || base.equals( ""))
					return null;

				return base;
			case 2:
				return get_base_from_panels();
		}
		return null;
	}

	/**
	 * @return
	 */
	private String get_base_from_panels() {
		String[] keys = new String[] {
			_itemPanels.get( 0).get(),
			_hatPanel.get(),
			_itemPanels.get( 1).get(),
			_itemPanels.get( 2).get(),
			_itemPanels.get( 3).get()
		};

		String base = "";
		for ( int i = 0; i < keys.length; ++i) {
			if ( null == keys[ i])
				return null;

			base += keys[ i];
			base += ( i < keys.length - 1) ? ExchangeAlgebraCommand._delimiter : "";
		}

		return base;
	}
}
