/**
 * 
 */
package soars.application.manager.model.main.panel.tab.doc;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import soars.application.manager.model.main.Constant;
import soars.application.manager.model.main.ResourceManager;
import soars.common.utility.swing.panel.StandardPanel;

/**
 * @author kurata
 *
 */
public class SoarsDocPage extends StandardPanel {

	/**
	 * 
	 */
	public JButton _backButton = null;

	/**
	 * 
	 */
	public JButton _forwardButton = null;

	/**
	 * 
	 */
	public SoarsDocEditorPane _soarsDocEditorPane = null;

	/**
	 * 
	 */
	public SoarsDocPage() {
		super();
	}

	/**
	 * @return
	 */
	public boolean setup() {
		setLayout( new BorderLayout());


		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.X_AXIS));

		if ( !setup_north_panel( northPanel))
			return false;

		add( northPanel, "North");


		JPanel centerPanel = new JPanel();
		centerPanel.setLayout( new BoxLayout( centerPanel, BoxLayout.Y_AXIS));

		if ( !setup_center_panel( centerPanel))
			return false;

		add( centerPanel);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_north_panel(JPanel parent) {
		ImageIcon imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/button/back.png"));
		_backButton = new JButton( imageIcon);
		_backButton.setToolTipText( ResourceManager.get_instance().get( "history.back.menu"));
		//_backButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _backButton.getPreferredSize().height));
		_backButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_soarsDocEditorPane.back();
			}
		});
		parent.add( _backButton);


		imageIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/button/forward.png"));
		_forwardButton = new JButton( imageIcon);
		_forwardButton.setToolTipText( ResourceManager.get_instance().get( "history.forward.menu"));
		//_forwardButton.setPreferredSize( new Dimension( imageIcon.getIconWidth() + 8, _forwardButton.getPreferredSize().height));
		_forwardButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				_soarsDocEditorPane.forward();
			}
		});
		parent.add( _forwardButton);


		return true;
	}

	/**
	 * @param parent
	 * @return
	 */
	private boolean setup_center_panel(JPanel parent) {
		_soarsDocEditorPane = new SoarsDocEditorPane( _backButton, _forwardButton);
		_soarsDocEditorPane.setEditable( false);
		if ( !_soarsDocEditorPane.setup())
			return false;

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy( JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy( JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.getViewport().setView( _soarsDocEditorPane);

		parent.add( scrollPane);

		return true;
	}

	/**
	 * 
	 */
	public void refresh() {
		_soarsDocEditorPane.refresh();
	}

	/**
	 * 
	 */
	public void back() {
		_soarsDocEditorPane.back();
	}

	/**
	 * 
	 */
	public void forward() {
		_soarsDocEditorPane.forward();
	}
}
