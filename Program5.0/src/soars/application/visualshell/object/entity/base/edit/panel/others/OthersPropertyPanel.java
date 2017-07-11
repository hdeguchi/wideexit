/*
 * Created on 2005/10/16
 */
package soars.application.visualshell.object.entity.base.edit.panel.others;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 */
public class OthersPropertyPanel extends PropertyPanelBase {

	/**
	 * 
	 */
	private JTextArea _othersTextArea = null;

	/**
	 * @param title
	 * @param entityBase
	 * @param propertyPanelBaseMap
	 * @param index
	 * @param owner
	 * @param parent
	 */
	public OthersPropertyPanel(String title, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap, int index, Frame owner, Component parent) {
		super(title, entityBase, propertyPanelBaseMap, index, owner, parent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_create()
	 */
	@Override
	protected boolean on_create() {
		if ( !super.on_create())
			return false;



		setLayout( new BorderLayout());



		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		insert_horizontal_glue( centerPanel);

		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		setup_others_text_area( panel);

		centerPanel.add( panel);


		insert_vertical_strut( centerPanel);


		add( centerPanel);


		return true;
	}

	/**
	 * @param panel
	 */
	private void setup_others_text_area(JPanel panel) {
		JPanel textAreaPanel = new JPanel();
		textAreaPanel.setLayout( new BoxLayout( textAreaPanel, BoxLayout.X_AXIS));

		textAreaPanel.add( Box.createHorizontalStrut( 5));

		_othersTextArea = new JTextArea( new TextExcluder( "\t"));
		_othersTextArea.setText( _entityBase._others);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _othersTextArea, this));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _othersTextArea);

		if ( _entityBase.is_multi())
			_othersTextArea.setEnabled( false);

		textAreaPanel.add( scrollPane);

		textAreaPanel.add( Box.createHorizontalStrut( 5));

		panel.add( textAreaPanel);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_setup_completed()
	 */
	@Override
	public void on_setup_completed() {
		_othersTextArea.requestFocusInWindow();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase#on_ok()
	 */
	@Override
	public boolean on_ok() {
		_entityBase._others = _othersTextArea.getText();
		return true;
	}
}
