/*
 * 2004/10/08
 */
package soars.common.utility.swing.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.TransferHandler;

import soars.common.utility.swing.dnd.button.DDButton;
import soars.common.utility.swing.dnd.common.DragMouseAdapter;
import soars.common.utility.swing.menu.MenuAction;

/**
 * @author kurata
 */
public class UserInterface implements ActionListener {

	/**
	 * 
	 */
	private Map<String, MenuAction> _menuMap = new HashMap<String, MenuAction>();

	/**
	 * 
	 */
	public UserInterface() {
	}

	/* (Non Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		String name = arg0.getActionCommand();
		MenuAction menuAction = _menuMap.get( name);
		if ( null == menuAction)
			return;

		menuAction.selected( arg0);
	}

	/**
	 * @param stroke
	 * @return
	 */
	static public String get_stroke(String stroke) {
		return ( ( stroke.startsWith( "ctrl ") && ( 0 <= System.getProperty( "os.name").indexOf( "Mac"))) ? stroke.replaceFirst( "ctrl", "meta") : stroke);
	}

	/**
	 * @param menuBar
	 * @param name
	 * @param tearOff
	 * @param mnemonic
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenu append_menu(JMenuBar menuBar, String name, boolean tearOff, String mnemonic, JLabel label, String message) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_menu( menuBar, name, tearOff, label, message);

		return append_menu( menuBar, name, tearOff, mnemonic.charAt( 0), label, message);
	}

	/**
	 * @param menuBar
	 * @param name
	 * @param tearOff
	 * @param mnemonic
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenu append_menu(JMenuBar menuBar, String name, boolean tearOff, char mnemonic, JLabel label, String message) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu menu = new JMenu( name, tearOff);
		menu.setMnemonic( mnemonic);
		append_menu_to_status_bar( menu, label, message);
		menuBar.add( menu);
		return menu;
	}

	/**
	 * @param menuBar
	 * @param name
	 * @param tearOff 
	 * @param mnemonic
	 * @return
	 */
	public JMenu append_menu(JMenuBar menuBar, String name, boolean tearOff, String mnemonic) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_menu( menuBar, name, tearOff);

		return append_menu( menuBar, name, tearOff, mnemonic.charAt( 0));
	}

	/**
	 * @param menuBar
	 * @param name
	 * @param tearOff 
	 * @param mnemonic
	 * @return
	 */
	public JMenu append_menu(JMenuBar menuBar, String name, boolean tearOff, char mnemonic) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu menu = new JMenu( name, tearOff);
		menu.setMnemonic( mnemonic);
		menuBar.add( menu);
		return menu;
	}

	/**
	 * @param menuBar
	 * @param name
	 * @param tearOff 
	 * @return
	 */
	public JMenu append_menu(JMenuBar menuBar, String name, boolean tearOff) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu menu = new JMenu( name, tearOff);
		menuBar.add( menu);
		return menu;
	}

	/**
	 * @param menuBar
	 * @param name
	 * @param tearOff
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenu append_menu(JMenuBar menuBar, String name, boolean tearOff, JLabel label, String message) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu menu = new JMenu( name, tearOff);
		append_menu_to_status_bar( menu, label, message);
		menuBar.add( menu);
		return menu;
	}

	/**
	 * @param menu
	 * @param name
	 * @param tearOff
	 * @param mnemonic
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenu append_menu(JMenu menu, String name, boolean tearOff, String mnemonic, JLabel label, String message) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_menu( menu, name, tearOff, label, message);

		return append_menu( menu, name, tearOff, mnemonic.charAt( 0), label, message);
	}

	/**
	 * @param menu
	 * @param name
	 * @param tearOff
	 * @param mnemonic
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenu append_menu(JMenu menu, String name, boolean tearOff, char mnemonic, JLabel label, String message) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu new_menu = new JMenu( name, tearOff);
		menu.setMnemonic( mnemonic);
		append_menu_to_status_bar( new_menu, label, message);
		menu.add( new_menu);
		return new_menu;
	}

	/**
	 * @param menu
	 * @param name
	 * @param tearOff 
	 * @param mnemonic
	 * @return
	 */
	public JMenu append_menu(JMenu menu, String name, boolean tearOff, String mnemonic) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_menu( menu, name, tearOff);

		return append_menu( menu, name, tearOff, mnemonic.charAt( 0));
	}

	/**
	 * @param menu
	 * @param name
	 * @param tearOff 
	 * @param mnemonic
	 * @return
	 */
	public JMenu append_menu(JMenu menu, String name, boolean tearOff, char mnemonic) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu new_menu = new JMenu( name, tearOff);
		menu.setMnemonic( mnemonic);
		menu.add( new_menu);
		return new_menu;
	}

	/**
	 * @param menu
	 * @param name
	 * @param tearOff 
	 * @return
	 */
	public JMenu append_menu(JMenu menu, String name, boolean tearOff) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu new_menu = new JMenu( name, tearOff);
		menu.add( new_menu);
		return new_menu;
	}

	/**
	 * @param menu
	 * @param name
	 * @param tearOff
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenu append_menu(JMenu menu, String name, boolean tearOff, JLabel label, String message) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menu");

		JMenu new_menu = new JMenu( name, tearOff);
		append_menu_to_status_bar( new_menu, label, message);
		menu.add( new_menu);
		return new_menu;
	}

	/**
	 * @param menu
	 * @param label
	 * @param message
	 */
	public void append_menu_to_status_bar(JMenu menu, final JLabel label, final String message) {
		if ( null == message || message.equals( ""))
			return;

		menu.addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				label.setText( message);
			}
			public void mouseExited(MouseEvent arg0) {
				label.setText( "");
			}
		});
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, String mnemonic, String stroke, JLabel label, String message) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_menuitem( menu, name, menuAction, stroke, label, message);

		return append_menuitem( menu, name, menuAction, mnemonic.charAt( 0), stroke, label, message);
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, String mnemonic, String stroke, JLabel label, String message) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_checkbox_menuitem( menu, name, menuAction, stroke, label, message);

		return append_checkbox_menuitem( menu, name, menuAction, mnemonic.charAt( 0), stroke, label, message);
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, char mnemonic, String stroke, JLabel label, String message) {
		JMenuItem item = append_menuitem( menu, name, menuAction, stroke, label, message);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, char mnemonic, String stroke, JLabel label, String message) {
		JCheckBoxMenuItem item = append_checkbox_menuitem( menu, name, menuAction, stroke, label, message);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, String stroke, JLabel label, String message) {
		JMenuItem item = append_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, String stroke, JLabel label, String message) {
		JCheckBoxMenuItem item = append_checkbox_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, String mnemonic, String stroke) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_menuitem( menu, name, menuAction, stroke);

		return append_menuitem( menu, name, menuAction, mnemonic.charAt( 0), stroke);
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, String mnemonic, String stroke) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_checkbox_menuitem( menu, name, menuAction, stroke);

		return append_checkbox_menuitem( menu, name, menuAction, mnemonic.charAt( 0), stroke);
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, char mnemonic, String stroke) {
		JMenuItem item = append_menuitem( menu, name, menuAction, stroke);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, char mnemonic, String stroke) {
		JCheckBoxMenuItem item = append_checkbox_menuitem( menu, name, menuAction, stroke);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, String stroke) {
		JMenuItem item = append_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, String stroke) {
		JCheckBoxMenuItem item = append_checkbox_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param keyStroke
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, KeyStroke keyStroke) {
		JMenuItem item = append_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		item.setAccelerator( keyStroke);

		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param keyStroke
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, KeyStroke keyStroke) {
		JCheckBoxMenuItem item = append_checkbox_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		item.setAccelerator( keyStroke);

		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction, JLabel label, String message) {
		JMenuItem item = append_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction, JLabel label, String message) {
		JCheckBoxMenuItem item = append_checkbox_menuitem( menu, name, menuAction);
		if ( null == item)
			return null;

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @return
	 */
	public JMenuItem append_menuitem(JMenu menu, String name, MenuAction menuAction) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menuitem");

		JMenuItem item = new JMenuItem( name);
		menu.add( item);
		item.addActionListener( this);
		item.setActionCommand( name);
		_menuMap.put( name, menuAction);
		menuAction._item = item;
		return item;
	}

	/**
	 * @param menu
	 * @param name
	 * @param menuAction
	 * @return
	 */
	public JCheckBoxMenuItem append_checkbox_menuitem(JMenu menu, String name, MenuAction menuAction) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_menuitem");

		JCheckBoxMenuItem item = new JCheckBoxMenuItem( name);
		menu.add( item);
		item.addActionListener( this);
		item.setActionCommand( name);
		_menuMap.put( name, menuAction);
		menuAction._item = item;
		return item;
	}

	/**
	 * @param item
	 * @param label
	 * @param message
	 */
	public void append_menuItem_to_status_bar(JMenuItem item, final JLabel label, final String message) {
		if ( null == message || message.equals( ""))
			return;

		item.addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				label.setText( message);
			}
			public void mouseExited(MouseEvent arg0) {
				label.setText( "");
			}
		});
	}

	/**
	 * @param name
	 * @param state
	 */
	public void enable_menu_item(String name, boolean state) {
		MenuAction menuAction = _menuMap.get( name);
		if ( null == menuAction)
			return;

		menuAction._item.setEnabled( state);
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param name
	 * @param menuAction
	 * @param toolTipText
	 * @param label
	 * @param message
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, String imagefile, String name, MenuAction menuAction, String toolTipText, JLabel label, String message) {
		JButton button = append_tool_button( toolBar, imagefile, name, menuAction);
		if ( null == button)
			return null;

		button.setToolTipText( toolTipText);
		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param name
	 * @param menuAction
	 * @param toolTipText
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, String imagefile, String name, MenuAction menuAction, String toolTipText) {
		JButton button = append_tool_button( toolBar, imagefile, name, menuAction);
		if ( null == button)
			return null;

		button.setToolTipText( toolTipText);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param name
	 * @param menuAction
	 * @param label
	 * @param message
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, String imagefile, String name, MenuAction menuAction, JLabel label, String message) {
		JButton button = append_tool_button( toolBar, imagefile, name, menuAction);
		if ( null == button)
			return null;

		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param name
	 * @param menuAction
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, String imagefile, String name, MenuAction menuAction) {
		ImageIcon imageIcon = new ImageIcon( imagefile);
		return append_tool_button( toolBar, imageIcon, name, menuAction);
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param name
	 * @param menuAction
	 * @param toolTipText
	 * @param label
	 * @param message
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, String name, MenuAction menuAction, String toolTipText, JLabel label, String message) {
		JButton button = append_tool_button( toolBar, imageIcon, name, menuAction);
		if ( null == button)
			return null;

		button.setToolTipText( toolTipText);
		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param name
	 * @param menuAction
	 * @param toolTipText
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, String name, MenuAction menuAction, String toolTipText) {
		JButton button = append_tool_button( toolBar, imageIcon, name, menuAction);
		if ( null == button)
			return null;

		button.setToolTipText( toolTipText);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param name
	 * @param menuAction
	 * @param label
	 * @param message
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, String name, MenuAction menuAction, JLabel label, String message) {
		JButton button = append_tool_button( toolBar, imageIcon, name, menuAction);
		if ( null == button)
			return null;

		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param name
	 * @param menuAction
	 * @return
	 */
	public JButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, String name, MenuAction menuAction) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_tool_button");

		JButton button;
		if ( null == imageIcon)
			button = new JButton();
		else
			button = new JButton( imageIcon);

		toolBar.add( button);
		button.addActionListener( this);
		button.setActionCommand( name);
		_menuMap.put( name, menuAction);
		return button;
	}

	/**
	 * @param button
	 * @param label
	 * @param message
	 */
	public void append_tool_bar_button_to_status_bar(JButton button, final JLabel label, final String message) {
		if ( null == message || message.equals( ""))
			return;

		button.addMouseListener( new MouseAdapter() {
			public void mouseEntered(MouseEvent arg0) {
				label.setText( message);
			}
			public void mouseExited(MouseEvent arg0) {
				label.setText( "");
			}
		});
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String mnemonic, String stroke, JLabel label, String message) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_popup_menuitem( popupMenu, name, menuAction, stroke, label, message);

		return append_popup_menuitem( popupMenu, name, menuAction, mnemonic.charAt( 0), stroke, label, message);
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String mnemonic, String stroke, JLabel label, String message) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_popup_checkbox_menuitem( popupMenu, name, menuAction, stroke, label, message);

		return append_popup_checkbox_menuitem( popupMenu, name, menuAction, mnemonic.charAt( 0), stroke, label, message);
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, char mnemonic, String stroke, JLabel label, String message) {
		JMenuItem item = append_popup_menuitem( popupMenu, name, menuAction, stroke, label, message);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, char mnemonic, String stroke, JLabel label, String message) {
		JCheckBoxMenuItem item = append_popup_checkbox_menuitem( popupMenu, name, menuAction, stroke, label, message);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String stroke, JLabel label, String message) {
		JMenuItem item = append_popup_menuitem( popupMenu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String stroke, JLabel label, String message) {
		JCheckBoxMenuItem item = append_popup_checkbox_menuitem( popupMenu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String mnemonic, String stroke) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_popup_menuitem( popupMenu, name, menuAction, stroke);

		return append_popup_menuitem( popupMenu, name, menuAction, mnemonic.charAt( 0), stroke);
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String mnemonic, String stroke) {
		if ( null == mnemonic || mnemonic.equals( ""))
			return append_popup_checkbox_menuitem( popupMenu, name, menuAction, stroke);

		return append_popup_checkbox_menuitem( popupMenu, name, menuAction, mnemonic.charAt( 0), stroke);
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, char mnemonic, String stroke) {
		JMenuItem item = append_popup_menuitem( popupMenu, name, menuAction, stroke);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, char mnemonic, String stroke) {
		JCheckBoxMenuItem item = append_popup_checkbox_menuitem( popupMenu, name, menuAction, stroke);
		if ( null == item)
			return null;

		item.setMnemonic( mnemonic);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String stroke) {
		JMenuItem item = append_popup_menuitem( popupMenu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param stroke
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, String stroke) {
		JCheckBoxMenuItem item = append_popup_checkbox_menuitem( popupMenu, name, menuAction);
		if ( null == item)
			return null;

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param label
	 * @param message
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, JLabel label, String message) {
		JMenuItem item = append_popup_menuitem( popupMenu, name, menuAction);
		if ( null == item)
			return null;

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @param label
	 * @param message
	 * @return
	 */
	public JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction, JLabel label, String message) {
		JCheckBoxMenuItem item = append_popup_checkbox_menuitem( popupMenu, name, menuAction);
		if ( null == item)
			return null;

		append_menuItem_to_status_bar( item, label, message);
		return item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @return
	 */
	public JMenuItem append_popup_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		JMenuItem item = new JMenuItem( name);
		popupMenu.add( item);
		item.addActionListener( this);
		item.setActionCommand( name);
		_menuMap.put( name, menuAction);
		menuAction._item = item;
		return item;
	}

	/**
	 * @param popupMenu
	 * @param item
	 * @param menuAction
	 */
	public void append_popup_menuitem(JPopupMenu popupMenu, JMenuItem item, MenuAction menuAction) {
		if ( null == item)
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		if ( null == item.getText() || item.getText().equals( ""))
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		popupMenu.add( item);
		item.addActionListener( this);
		item.setActionCommand( item.getText());
		_menuMap.put( item.getText(), menuAction);
		menuAction._item = item;
	}

	/**
	 * @param popupMenu
	 * @param index
	 * @param name
	 * @param menuAction
	 * @param mnemonic
	 * @param stroke
	 * @return
	 */
	public JMenuItem insert_popup_menuitem(JPopupMenu popupMenu, int index, String name, MenuAction menuAction, String mnemonic, String stroke) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - insert_popup_menuitem");

		JMenuItem item = new JMenuItem( name);
		insert_popup_menuitem( popupMenu, index, item, menuAction);

		if ( null != mnemonic && !mnemonic.equals( ""))
			item.setMnemonic( mnemonic.charAt( 0));

		if ( null != stroke && !stroke.equals( ""))
			item.setAccelerator( KeyStroke.getKeyStroke( get_stroke( stroke)));

		return item;
	}

	/**
	 * @param popupMenu
	 * @param index
	 * @param item
	 * @param menuAction
	 */
	public void insert_popup_menuitem(JPopupMenu popupMenu, int index, JMenuItem item, MenuAction menuAction) {
		if ( null == item)
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		if ( null == item.getText() || item.getText().equals( ""))
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		if ( 0 > index || popupMenu.getComponentCount() < index)
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		popupMenu.insert( item, index);
		item.addActionListener( this);
		item.setActionCommand( item.getText());
		_menuMap.put( item.getText(), menuAction);
		menuAction._item = item;
	}

	/**
	 * @param popupMenu
	 * @param name
	 * @param menuAction
	 * @return
	 */
	private JCheckBoxMenuItem append_popup_checkbox_menuitem(JPopupMenu popupMenu, String name, MenuAction menuAction) {
		if ( null == name || name.equals( ""))
			throw new RuntimeException( "Invalid name - append_popup_menuitem");

		JCheckBoxMenuItem item = new JCheckBoxMenuItem( name);
		popupMenu.add( item);
		item.addActionListener( this);
		item.setActionCommand( name);
		_menuMap.put( name, menuAction);
		menuAction._item = item;
		return item;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param transferHandler
	 * @param toolTipText
	 * @param label
	 * @param message
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, String imagefile, TransferHandler transferHandler, String toolTipText, JLabel label, String message) {
		DDButton button = append_tool_button( toolBar, imagefile, transferHandler);
		if ( null == button)
			return null;

		button.setToolTipText( toolTipText);
		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param transferHandler
	 * @param toolTipText
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, String imagefile, TransferHandler transferHandler, String toolTipText) {
		DDButton button = append_tool_button( toolBar, imagefile, transferHandler);
		if ( null == button)
			return null;

		button.addMouseListener( new DragMouseAdapter());
		button.setToolTipText( toolTipText);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param transferHandler
	 * @param label
	 * @param message
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, String imagefile, TransferHandler transferHandler, JLabel label, String message) {
		DDButton button = append_tool_button( toolBar, imagefile, transferHandler);
		if ( null == button)
			return null;

		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imagefile
	 * @param transferHandler
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, String imagefile, TransferHandler transferHandler) {
		ImageIcon imageIcon = new ImageIcon( imagefile);
		return append_tool_button( toolBar, imageIcon, transferHandler);
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param transferHandler
	 * @param toolTipText
	 * @param label
	 * @param message
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, TransferHandler transferHandler, String toolTipText, final JLabel label, final String message) {
		DDButton button = append_tool_button( toolBar, imageIcon, transferHandler);
		if ( null == button)
			return null;

		button.setToolTipText( toolTipText);
		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param transferHandler
	 * @param toolTipText
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, TransferHandler transferHandler, String toolTipText) {
		DDButton button = append_tool_button( toolBar, imageIcon, transferHandler);
		if ( null == button)
			return null;

		button.addMouseListener( new DragMouseAdapter());
		button.setToolTipText( toolTipText);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param transferHandler
	 * @param label
	 * @param message
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, TransferHandler transferHandler, JLabel label, String message) {
		DDButton button = append_tool_button( toolBar, imageIcon, transferHandler);
		if ( null == button)
			return null;

		append_tool_bar_button_to_status_bar( button, label, message);
		return button;
	}

	/**
	 * @param toolBar
	 * @param imageIcon
	 * @param transferHandler
	 * @return
	 */
	public DDButton append_tool_button(JToolBar toolBar, ImageIcon imageIcon, TransferHandler transferHandler) {
		DDButton button;
		if ( null == imageIcon)
			button = new DDButton();
		else
			button = new DDButton( imageIcon);

		toolBar.add( button);

		button.setTransferHandler( transferHandler);

		return button;
	}

	/**
	 * @param button
	 * @param label
	 * @param message
	 */
	public void append_tool_bar_button_to_status_bar(DDButton button, final JLabel label, final String message) {
		if ( null == message || message.equals( ""))
			button.addMouseListener( new DragMouseAdapter());
		else {
			button.addMouseListener( new MouseAdapter() {
				public void mousePressed(MouseEvent arg0) {
					JComponent component = ( JComponent)arg0.getSource();
					TransferHandler transferHandler = component.getTransferHandler();
					transferHandler.exportAsDrag( component, arg0, TransferHandler.COPY);
				}
				public void mouseEntered(MouseEvent arg0) {
					label.setText( message);
				}
				public void mouseExited(MouseEvent arg0) {
					label.setText( "");
				}
			});
		}
	}

	/**
	 * @param menuList
	 * @param menuBar
	 * @return
	 */
	public boolean this_menuItem_exists(List<List<String>> menuList, JMenuBar menuBar) {
		List<String> list = menuList.get( menuList.size() - 1);
		if ( this_menuItem_exists( list.get( 0), menuBar))
			return true;

		JMenu menu = null;
		for ( int i = 0; i < menuList.size(); ++i) {
			list = menuList.get( i);
			String name = list.get( 0);
			if ( 0 == i) {
				menu = get_menu( name, menuBar);
				if ( null == menu)
					return false;
			} else {
				JMenuItem menuItem = get_menu( name, menu);
				if ( null == menuItem)
					return false;

				if ( menuList.size() - 1 == i)
					break;
				else {
					if ( menuItem instanceof JMenu)
						menu = ( JMenu)menuItem;
					else
						return true;
				}
			}
		}
		return true;
	}

	/**
	 * @param name
	 * @param menuBar
	 * @return
	 */
	private boolean this_menuItem_exists(String name, JMenuBar menuBar) {
		for ( int i = 0; i < menuBar.getMenuCount(); ++i) {
			JMenu menu = menuBar.getMenu( i);
			if ( menu.getText().equals( name))
				return true;

			if ( this_menuItem_exists( name, menu))
				return true;
		}
		return false;
	}

	/**
	 * @param name
	 * @param menu
	 * @return
	 */
	private boolean this_menuItem_exists(String name, JMenu menu) {
		for ( int i = 0; i < menu.getItemCount(); ++i) {
			JComponent component = menu.getItem( i);
			if ( !( component instanceof JMenuItem) && !( component instanceof JMenu))
				continue;

			if ( component instanceof JMenu) {
				JMenu childMenu = ( JMenu)component;
				if ( childMenu.getText().equals( name))
					return true;

				if ( this_menuItem_exists( name, childMenu))
					return true;
			} else {
				JMenuItem menuItem = ( JMenuItem)component;
				if ( menuItem.getText().equals( name))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param name
	 * @param menuBar
	 * @return
	 */
	private JMenu get_menu(String name, JMenuBar menuBar) {
		for ( int i = 0; i < menuBar.getMenuCount(); ++i) {
			JMenu menu = menuBar.getMenu( i);
			if ( menu.getText().equals( name))
				return menu;
		}
		return null;
	}

	/**
	 * @param name
	 * @param menu
	 * @return
	 */
	private JMenuItem get_menu(String name, JMenu menu) {
		for ( int i = 0; i < menu.getItemCount(); ++i) {
			JComponent component = menu.getItem( i);
			if ( !( component instanceof JMenuItem) && !( component instanceof JMenu))
				continue;

			JMenuItem menuItem = ( JMenuItem)component;
			if ( menuItem.getText().equals( name))
				return menuItem;
		}
		return null;
	}

	/**
	 * @param menuList
	 * @param menuBar
	 * @param messageLabel
	 * @return
	 */
	public JMenu create_menu(List<List<String>> menuList, JMenuBar menuBar, JLabel messageLabel) {
		JMenu menu = null;
		for ( int i = 0; i < menuList.size() - 1; ++i) {
			List<String> list = menuList.get( i);
			String[] menuElements = list.toArray( new String[ 0]);
			menu = ( ( 0 == i) ? create_menu( menuElements, menuBar, messageLabel)
				: create_menu( menuElements, menu, messageLabel));
			if ( null == menu)
				return null;
		}
		return menu;
	}

	/**
	 * @param menuElements
	 * @param menuBar
	 * @param messageLabel
	 * @return
	 */
	public JMenu create_menu(String[] menuElements, JMenuBar menuBar, JLabel messageLabel) {
		for ( int i = 0; i < menuBar.getMenuCount(); ++i) {
			JMenu menu = menuBar.getMenu( i);
			if ( menu.getText().equals( menuElements[ 0]))
				return menu;
		}

		JMenu newMenu = append_menu(
			menuBar,
			menuElements[ 0],
			true,
			( ( 1 < menuElements.length && null != menuElements[ 1]) ? menuElements[ 1] : ""),
			messageLabel,
			( ( 2 < menuElements.length && null != menuElements[ 2]) ? menuElements[ 2] : ""));
		return newMenu;
	}

	/**
	 * @param menuElements
	 * @param menu
	 * @param messageLabel
	 * @return
	 */
	public JMenu create_menu(String[] menuElements, JMenu menu, JLabel messageLabel) {
		for ( int i = 0; i < menu.getItemCount(); ++i) {
			JComponent component = menu.getItem( i);
			if ( !( component instanceof JMenuItem && !( component instanceof JMenu)))
				continue;

			JMenuItem menuItem = ( JMenuItem)component;
			if ( menuItem.getText().equals( menuElements[ 0]))
				return ( ( menuItem instanceof JMenu) ? ( JMenu)menuItem : null);
		}

		JMenu newMenu = append_menu(
			menu,
			menuElements[ 0],
			true,
			( ( 1 < menuElements.length && null != menuElements[ 1]) ? menuElements[ 1] : ""),
			messageLabel,
			( ( 2 < menuElements.length && null != menuElements[ 2]) ? menuElements[ 2] : ""));
		return newMenu;
	}
}
