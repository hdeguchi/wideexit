/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.initial;

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
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.map.table.MapInitialValueTable;
import soars.application.visualshell.object.entity.base.object.map.MapInitialValue;

/**
 * @author kurata
 *
 */
public class MapInitialValueButtonPanel extends JPanel {

	/**
	 * 
	 */
	protected MapInitialValueKeyPanel _mapInitialValueKeyPanel = null;

	/**
	 * 
	 */
	protected MapInitialValueValuePanel _mapInitialValueValuePanel = null;

	/**
	 * 
	 */
	protected MapInitialValueTable _mapInitialValueTable = null;

	/**
	 * 
	 */
	protected List<JButton> _buttons = new ArrayList<JButton>();

	/**
	 * @param mapInitialValueKeyPanel
	 * @param mapInitialValueValuePanel
	 * @param mapInitialValueTable
	 */
	public MapInitialValueButtonPanel(MapInitialValueKeyPanel mapInitialValueKeyPanel, MapInitialValueValuePanel mapInitialValueValuePanel, MapInitialValueTable mapInitialValueTable) {
		super();
		_mapInitialValueKeyPanel = mapInitialValueKeyPanel;
		_mapInitialValueValuePanel = mapInitialValueValuePanel;
		_mapInitialValueTable = mapInitialValueTable;
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
	 * 
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
	}

	/**
	 * @param actionEvent
	 */
	protected void on_append(ActionEvent actionEvent) {
		MapInitialValue mapInitialValue = new MapInitialValue();

		if ( !_mapInitialValueKeyPanel.get( mapInitialValue))
			return;

		if ( !_mapInitialValueValuePanel.get( mapInitialValue))
			return;

		_mapInitialValueTable.append( new MapInitialValue[] { mapInitialValue, mapInitialValue, mapInitialValue});
	}

	/**
	 * @param actionEvent
	 */
	protected void on_update(ActionEvent actionEvent) {
		if ( 1 != _mapInitialValueTable.getSelectedRowCount())
			return;

		MapInitialValue mapInitialValue = new MapInitialValue();

		if ( !_mapInitialValueKeyPanel.get( mapInitialValue))
			return;

		if ( !_mapInitialValueValuePanel.get( mapInitialValue))
			return;

		_mapInitialValueTable.update( new MapInitialValue[] { mapInitialValue, mapInitialValue, mapInitialValue});
	}

	/**
	 * @param actionEvent
	 */
	protected void on_remove(ActionEvent actionEvent) {
		_mapInitialValueTable.on_remove();
	}

	/**
	 * 
	 */
	public void on_setup_completed() {
	}
}
