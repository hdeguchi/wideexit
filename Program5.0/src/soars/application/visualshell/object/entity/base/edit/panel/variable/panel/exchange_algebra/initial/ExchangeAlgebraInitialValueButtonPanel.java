/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.table.ExchangeAlgebraInitialValueTable;
import soars.application.visualshell.object.entity.base.object.exchange_algebra.ExchangeAlgebraInitialValue;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.text.TextField;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraInitialValueButtonPanel extends JPanel {

	/**
	 * 
	 */
	protected TextField _valueTextField = null;

	/**
	 * 
	 */
	protected ComboBox _baseTypeComboBox = null;

	/**
	 * 
	 */
	protected KeywordPanel _keywordPanel = null;

	/**
	 * 
	 */
	protected NamePanel _namePanel = null;

	/**
	 * 
	 */
	protected HatPanel _hatPanel = null;

	/**
	 * 
	 */
	protected UnitPanel _unitPanel = null;

	/**
	 * 
	 */
	protected TimePanel _timePanel = null;

	/**
	 * 
	 */
	protected SubjectPanel _subjectPanel = null;

	/**
	 * 
	 */
	protected ExchangeAlgebraInitialValueTable _exchangeAlgebraInitialValueTable = null;

	/**
	 * 
	 */
	protected List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * @param valueTextField
	 * @param baseTypeComboBox
	 * @param keywordPanel
	 * @param namePanel
	 * @param hatPanel
	 * @param unitPanel
	 * @param timePanel
	 * @param subjectPanel
	 * @param exchangeAlgebraInitialValueTable
	 */
	public ExchangeAlgebraInitialValueButtonPanel(TextField valueTextField, ComboBox baseTypeComboBox, KeywordPanel keywordPanel, NamePanel namePanel, HatPanel hatPanel, UnitPanel unitPanel, TimePanel timePanel, SubjectPanel subjectPanel, ExchangeAlgebraInitialValueTable exchangeAlgebraInitialValueTable) {
		super();
		_valueTextField = valueTextField;
		_baseTypeComboBox = baseTypeComboBox;
		_keywordPanel = keywordPanel;
		_namePanel = namePanel;
		_hatPanel = hatPanel;
		_unitPanel = unitPanel;
		_timePanel = timePanel;
		_subjectPanel = subjectPanel;
		_exchangeAlgebraInitialValueTable = exchangeAlgebraInitialValueTable;
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setEnabled(boolean)
	 */
	@Override
	public void setEnabled(boolean enabled) {
		for ( JButton button:_buttons)
			button.setEnabled( enabled);

		super.setEnabled(enabled);
	}

	/**
	 * @param parent
	 */
	public void setup() {
		setLayout( new FlowLayout( FlowLayout.RIGHT, 5, 0));

		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/append.png"));
		JButton button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.append.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_append( e);
			}
		});
		_buttons.add( button);
		add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/insert.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.insert.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_insert( e);
			}
		});
		_buttons.add( button);
		add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/update.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.update.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_update( e);
			}
		});
		_buttons.add( button);
		add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/remove.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.remove.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_remove( e);
			}
		});
		_buttons.add( button);
		add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/up_arrow.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.up.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_up( e);
			}
		});
		_buttons.add( button);
		add( button);

		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/toolbar/menu/edit/down_arrow.png"));
		button = new JButton( imageIcon);
		button.setToolTipText( ResourceManager.get_instance().get( "edit.object.dialog.variable.initial.value.down.button"));
		button.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, button.getPreferredSize().height));
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				on_down( e);
			}
		});
		_buttons.add( button);
		add( button);
	}

	/**
	 * @param actionEvent
	 */
	protected void on_append(ActionEvent actionEvent) {
		ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = new ExchangeAlgebraInitialValue();

		if ( !get( exchangeAlgebraInitialValue, null))
			return;

		_exchangeAlgebraInitialValueTable.append( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue});
	}

	/**
	 * @param actionEvent
	 */
	protected void on_insert(ActionEvent actionEvent) {
		if ( 1 != _exchangeAlgebraInitialValueTable.getSelectedRowCount())
			return;

		ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = new ExchangeAlgebraInitialValue();

		if ( !get( exchangeAlgebraInitialValue, null))
			return;

		_exchangeAlgebraInitialValueTable.insert( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue});
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
		int[] rows = _exchangeAlgebraInitialValueTable.getSelectedRows();
		if ( null == rows || 1 != rows.length)
			return;

		ExchangeAlgebraInitialValue exchangeAlgebraInitialValue = new ExchangeAlgebraInitialValue();

		if ( !get( exchangeAlgebraInitialValue, ( ExchangeAlgebraInitialValue)_exchangeAlgebraInitialValueTable.getValueAt( rows[ 0], 0)))
			return;

		_exchangeAlgebraInitialValueTable.update( new ExchangeAlgebraInitialValue[] { exchangeAlgebraInitialValue});
	}

	/**
	 * @param actionEvent
	 */
	protected void on_remove(ActionEvent actionEvent) {
		_exchangeAlgebraInitialValueTable.on_remove();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_up(ActionEvent actionEvent) {
		_exchangeAlgebraInitialValueTable.up();
	}

	/**
	 * @param actionEvent
	 */
	protected void on_down(ActionEvent actionEvent) {
		_exchangeAlgebraInitialValueTable.down();
	}

	/**
	 * @param exchangeAlgebraInitialValue
	 */
	public void set(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue) {
		_valueTextField.setText( exchangeAlgebraInitialValue._value);

		if ( !exchangeAlgebraInitialValue._keyword.equals( ""))
			_keywordPanel.set( exchangeAlgebraInitialValue._keyword);

		_namePanel.set( exchangeAlgebraInitialValue._name);
		_hatPanel.set( exchangeAlgebraInitialValue._hat);
		_unitPanel.set( exchangeAlgebraInitialValue._unit);
		_timePanel.set( exchangeAlgebraInitialValue._time);
		_subjectPanel.set( exchangeAlgebraInitialValue._subject);
		_baseTypeComboBox.setSelectedItem( !exchangeAlgebraInitialValue._keyword.equals( "")
			? ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.keyword") : ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.immediate"));
	}

	/**
	 * @param exchangeAlgebraInitialValue
	 * @param selectedExchangeAlgebraInitialValue
	 * @return
	 */
	private boolean get(ExchangeAlgebraInitialValue exchangeAlgebraInitialValue, ExchangeAlgebraInitialValue selectedExchangeAlgebraInitialValue) {
		String value = _valueTextField.getText();
		if ( null == value || value.equals( "")
			|| value.equals( "$") || 0 < value.indexOf( '$'))
			return false;

		if ( value.startsWith( "$") && 0 < value.indexOf( "$", 1))
			return false;

		value = ExchangeAlgebraInitialValue.is_correct( value);
		if ( null == value)
			return false;


		String item = ( String)_baseTypeComboBox.getSelectedItem();
		if ( item.equals( ResourceManager.get_instance().get( "edit.exchange.algebra.value.dialog.keyword"))) {
			String keyword = _keywordPanel.get();
			if ( null == keyword || keyword.equals( ""))
				return false;

			if ( _exchangeAlgebraInitialValueTable.contains( "keyword", keyword, selectedExchangeAlgebraInitialValue))
				return false;

			exchangeAlgebraInitialValue._value = value;
			exchangeAlgebraInitialValue._keyword = keyword;
			exchangeAlgebraInitialValue._name = "";
			exchangeAlgebraInitialValue._hat = "";
			exchangeAlgebraInitialValue._unit = "";
			exchangeAlgebraInitialValue._time = "";
			exchangeAlgebraInitialValue._subject = "";
		} else {
			String name = _namePanel.get();
			if ( null == name || name.equals( "") || 0 <= name.indexOf( '$'))
				return false;

			String hat = ( String)_hatPanel.get();
			if ( null == hat || hat.equals( ""))
				return false;

			String unit = _unitPanel.get();
			if ( null == unit || 0 <= unit.indexOf( '$'))
				return false;

			String time = _timePanel.get();
			if ( null == time || 0 <= time.indexOf( '$'))
				return false;

			String subject = _subjectPanel.get();
			if ( null == subject || 0 <= subject.indexOf( '$'))
				return false;

			if ( _exchangeAlgebraInitialValueTable.contains( name, hat, unit, time, subject, selectedExchangeAlgebraInitialValue))
				return false;

			exchangeAlgebraInitialValue._value = value;
			exchangeAlgebraInitialValue._keyword = "";
			exchangeAlgebraInitialValue._name = name;
			exchangeAlgebraInitialValue._hat = hat;
			exchangeAlgebraInitialValue._unit = unit;
			exchangeAlgebraInitialValue._time = time;
			exchangeAlgebraInitialValue._subject = subject;
		}

		return true;
	}
}
