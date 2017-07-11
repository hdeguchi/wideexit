/*
 * 2004/05/14
 */
package soars.application.visualshell.state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import soars.application.visualshell.common.menu.basic1.EditAction;
import soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1;
import soars.application.visualshell.common.menu.basic1.RemoveAction;
import soars.application.visualshell.common.menu.basic2.CopyAction;
import soars.application.visualshell.common.menu.basic2.DeselectAllAction;
import soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2;
import soars.application.visualshell.common.menu.basic2.PasteAction;
import soars.application.visualshell.common.menu.basic2.SelectAllAction;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.main.VisualShellView;
import soars.application.visualshell.menu.edit.CopyObjectsAction;
import soars.application.visualshell.menu.edit.DisconnectAction;
import soars.application.visualshell.menu.edit.DisconnectOneAction;
import soars.application.visualshell.menu.edit.FlushBottomAction;
import soars.application.visualshell.menu.edit.FlushLeftAction;
import soars.application.visualshell.menu.edit.FlushRightAction;
import soars.application.visualshell.menu.edit.FlushTopAction;
import soars.application.visualshell.menu.edit.HorizontalEqualLayoutAction;
import soars.application.visualshell.menu.edit.IEditMenuHandler;
import soars.application.visualshell.menu.edit.MoveAction;
import soars.application.visualshell.menu.edit.PasteObjectsAction;
import soars.application.visualshell.menu.edit.SwapNamesAction;
import soars.application.visualshell.menu.edit.VerticalEqualLayoutAction;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectOutObject;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.application.visualshell.observer.Observer;
import soars.application.visualshell.observer.WarningDlg1;
import soars.application.visualshell.state.menuitem.DisconnectOneMenuItem;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.swing.gui.UserInterface;
import soars.common.utility.tool.clipboard.Clipboard;

/**
 * @author kurata
 */
public class EditState extends StateBase implements IBasicMenuHandler1, IBasicMenuHandler2, IEditMenuHandler {

	/**
	 * 
	 */
	private DrawObject _selectedDrawObject = null;

	/**
	 * 
	 */
	private Point _mousePosition = new Point();

	/**
	 * 
	 */
	private Point _rubberbandStartPoint = null;

	/**
	 * 
	 */
	private Point _rubberbandEndPoint = new Point();

	/**
	 * 
	 */
	private Rectangle _currentRectangle = new Rectangle();

	/**
	 * 
	 */
	private Rectangle _previousRectangle = new Rectangle();

	/**
	 * 
	 */
	private Rectangle _clientRectangle = new Rectangle();

	/**
	 * 
	 */
	private UserInterface _userInterface = null;

	/**
	 * 
	 */
	private JPopupMenu _popupMenu = null;

	/**
	 * 
	 */
	private JMenuItem _disconnectMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _editMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _moveMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _copyMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _pasteMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _selectAllMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _deselectAllMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _flushTopMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _flushBottomMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _flushLeftMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _flushRightMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _verticalEqualLayoutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _horizontalEqualLayoutMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _swapNamesMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _copyObjectsMenuItem = null;

	/**
	 * 
	 */
	private JMenuItem _pasteObjectsMenuItem = null;

	/**
	 * 
	 */
	protected Vector<DrawObject> _selectedDrawobjects = new Vector<DrawObject>();

	/**
	 * 
	 */
	protected int _pasteCounter = 0;

	/**
	 * 
	 */
	private Point _pastePosition = null;

	/**
	 * 
	 */
	private ConnectOutObject _selectedConnectOutObject = null;

	/**
	 * 
	 */
	private ConnectObject _selectedConnectObject = null;

	/**
	 * 
	 */
	private Point _connectionStartPoint = null;

	/**
	 * 
	 */
	private Point _connectionEndPoint = new Point();

	/**
	 * 
	 */
	private Color _connectionColor = null;

	/**
	 * 
	 */
	private Rectangle _connectionPreviousRectangle = new Rectangle();

	/**
	 * 
	 */
	static private final String _password = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><visual_shell_data><layer_data><layer name=\"__new_layer\"></layer></layer_data></visual_shell_data>";

	/**
	 * 
	 */
	public EditState() {
		super();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#setup()
	 */
	public boolean setup() {
		return super.setup();
	}

	/**
	 * 
	 */
	private void update_menu() {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		LayerManager.get_instance().get_selected( drawObjects);
		MainFrame.get_instance()._editCopyButton.setEnabled( !drawObjects.isEmpty());
		MainFrame.get_instance()._editPasteButton.setEnabled( !_selectedDrawobjects.isEmpty());
		MainFrame.get_instance()._editRemoveButton.setEnabled( !drawObjects.isEmpty() && !contains_global_object( drawObjects));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#cleanup()
	 */
	public void cleanup() {
		super.cleanup();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#refresh()
	 */
	public void refresh() {
		super.refresh();
		_selectedDrawobjects.clear();
		_pasteCounter = 0;
		update_menu();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_enter()
	 */
	public boolean on_enter(String newStateName) {
		LayerManager.get_instance().resize( VisualShellView.get_instance());
		LayerManager.get_instance().update_preferred_size( VisualShellView.get_instance());
		setup_popup_menu();
		return super.on_enter(newStateName);
	}

	/**
	 * 
	 */
	private void setup_popup_menu() {
		_userInterface = new UserInterface();
		_popupMenu = new JPopupMenu();

		_disconnectMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.disconnect.menu"),
			new DisconnectAction( ResourceManager.get_instance().get( "edit.popup.menu.disconnect.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.disconnect.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.disconnect.stroke"));

		_popupMenu.addSeparator();

		_editMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.edit.menu"),
			new EditAction( ResourceManager.get_instance().get( "common.popup.menu.edit.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.edit.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.edit.stroke"));
		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.remove.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "common.popup.menu.remove.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.remove.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.remove.stroke"));
		_moveMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.move.menu"),
			new MoveAction( ResourceManager.get_instance().get( "edit.popup.menu.move.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.move.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.move.stroke"));

		_popupMenu.addSeparator();

		_copyMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.copy.menu"),
			new CopyAction( ResourceManager.get_instance().get( "common.popup.menu.copy.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.copy.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.copy.stroke"));
		_pasteMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.paste.menu"),
			new PasteAction( ResourceManager.get_instance().get( "common.popup.menu.paste.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.paste.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.paste.stroke"));

		_popupMenu.addSeparator();

		_selectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"),
			new SelectAllAction( ResourceManager.get_instance().get( "common.popup.menu.select.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.select.all.stroke"));
		_deselectAllMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "common.popup.menu.deselect.all.menu"),
			new DeselectAllAction( ResourceManager.get_instance().get( "common.popup.menu.deselect.all.menu"), this),
			ResourceManager.get_instance().get( "common.popup.menu.deselect.all.mnemonic"),
			ResourceManager.get_instance().get( "common.popup.menu.deselect.all.stroke"));

		_popupMenu.addSeparator();

		_flushTopMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.top.menu"),
			new FlushTopAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.top.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.top.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.top.stroke"));
		_flushBottomMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.menu"),
			new FlushBottomAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.bottom.stroke"));
		_flushLeftMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.left.menu"),
			new FlushLeftAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.left.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.left.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.left.stroke"));
		_flushRightMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.flush.right.menu"),
			new FlushRightAction( ResourceManager.get_instance().get( "edit.popup.menu.flush.right.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.right.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.flush.right.stroke"));

		_popupMenu.addSeparator();

		_verticalEqualLayoutMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.menu"),
			new VerticalEqualLayoutAction( ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.vertical.equal.layout.stroke"));
		_horizontalEqualLayoutMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.menu"),
			new HorizontalEqualLayoutAction( ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.horizontal.equal.layout.stroke"));

		_popupMenu.addSeparator();

		_swapNamesMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.swap.names.menu"),
			new SwapNamesAction( ResourceManager.get_instance().get( "edit.popup.menu.swap.names.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.swap.names.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.swap.names.stroke"));

		_popupMenu.addSeparator();

		_copyObjectsMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.menu"),
			new CopyObjectsAction( ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.copy.objects.stroke"));
		_pasteObjectsMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.menu"),
			new PasteObjectsAction( ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.menu"), this),
			ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.mnemonic"),
			ResourceManager.get_instance().get( "edit.popup.menu.paste.objects.stroke"));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_leave()
	 */
	public boolean on_leave(String newStateName) {
		LayerManager.get_instance().select_all( false);
		_selectedDrawobjects.clear();
		update_menu();
		return super.on_leave(newStateName);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#cancel()
	 */
	public void cancel() {
		LayerManager.get_instance().select_all( false);
		update_menu();
		VisualShellView.get_instance().repaint();
		super.cancel();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_resized(java.awt.event.ComponentEvent)
	 */
	public void on_resized(ComponentEvent componentEvent) {
		LayerManager.get_instance().resize( VisualShellView.get_instance());
		super.on_resized(componentEvent);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	public void on_mouse_left_double_click(MouseEvent mouseEvent) {
		if ( 0 != ( ( InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK
			| InputEvent.SHIFT_DOWN_MASK | InputEvent.SHIFT_MASK
			| InputEvent.META_DOWN_MASK | InputEvent.META_MASK)
			& mouseEvent.getModifiersEx())) {
			update_menu();
			return;
		}

		LayerManager.get_instance().select_all( false);
		if ( null != _selectedDrawObject) {
			_selectedDrawObject.select( false);
			_selectedDrawObject = null;
		}

		LayerManager.get_instance().edit_object( _mousePosition, VisualShellView.get_instance(), MainFrame.get_instance());

		update_menu();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	public void on_mouse_left_down(MouseEvent mouseEvent) {
		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
			&& 0 != ( ( InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK) & mouseEvent.getModifiersEx())) {
			update_menu();
			return;
		}

		_mousePosition.setLocation( mouseEvent.getPoint());

		if ( 0 != ( ( InputEvent.SHIFT_DOWN_MASK | InputEvent.CTRL_DOWN_MASK | InputEvent.META_DOWN_MASK) & mouseEvent.getModifiersEx())) {
			if ( null != _selectedDrawObject) {
//				_selected_drawObject.select( false);
				_selectedDrawObject = null;
			}

			DrawObject drawObject = LayerManager.get_instance().get( _mousePosition);
			if ( null == drawObject) {
				update_menu();
				return;
			}

			if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( !role.isRole( _mousePosition)) {
					update_menu();
					return;
				}
			}

			drawObject.select( !drawObject.selected());
			LayerManager.get_instance().bring_to_top( drawObject);
		} else {
			_selectedDrawObject = LayerManager.get_instance().get( _mousePosition);
			if ( null != _selectedDrawObject) {
				if ( _selectedDrawObject instanceof Role) {
					Role role = ( Role)_selectedDrawObject;
					if ( !role.isRole( _mousePosition)) {
						_selectedDrawObject = null;
						if ( role.isConnectOutObject( _mousePosition))
							start_connection( role, mouseEvent);
						else
							start_rubberband( mouseEvent);

						update_menu();
						return;
					}
				}

				Vector<DrawObject> drawObjects = new Vector<DrawObject>();
				LayerManager.get_instance().get_selected( drawObjects);
				if ( drawObjects.contains( _selectedDrawObject))
					_selectedDrawObject = null;
				else {
					LayerManager.get_instance().select_all( false);
					_selectedDrawObject.select( true);
				}
			} else {
				start_rubberband( mouseEvent);
				return;
			}
		}

		update_menu();

		VisualShellView.get_instance().repaint();
	}

	/**
	 * @param role
	 * @param mouseEvent
	 */
	private void start_connection(Role role, MouseEvent mouseEvent) {
		LayerManager.get_instance().select_all( false);
		_selectedConnectOutObject = role._connectOutObject;
		_connectionStartPoint = _mousePosition = _selectedConnectOutObject.get_center();
		_connectionEndPoint.setLocation( mouseEvent.getPoint());
		_connectionColor = new Color( role._imageColor.getRGB());
		_connectionPreviousRectangle.setBounds( mouseEvent.getPoint().x, mouseEvent.getPoint().y, 0, 0);
		VisualShellView.get_instance().repaint();
	}

	/**
	 * @param mouseEvent
	 */
	private void start_rubberband(MouseEvent mouseEvent) {
		LayerManager.get_instance().select_all( false);
		_rubberbandStartPoint = _mousePosition;
		_rubberbandEndPoint.setLocation( mouseEvent.getPoint());
		_previousRectangle.setBounds( mouseEvent.getPoint().x, mouseEvent.getPoint().y, 0, 0);
		VisualShellView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_mouse_left_up(java.awt.event.MouseEvent)
	 */
	public void on_mouse_left_up(MouseEvent mouseEvent) {
		if ( 0 <= System.getProperty( "os.name").indexOf( "Mac")
			&& 0 != ( ( InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK) & mouseEvent.getModifiersEx())) {
			on_mouse_right_up( mouseEvent);
			update_menu();
			return;
		}

//		if ( null != _selectedDrawObject) {
//			_selectedDrawObject.select( false);
//			_selectedDrawObject = null;
//		} else if ( null != _selectedConnectOutObject) {
		if ( null != _selectedConnectOutObject) {
			DrawObject drawObject = LayerManager.get_instance().get( mouseEvent.getPoint());
			if ( null != drawObject && ( drawObject instanceof Role)
				&& ( ( ( _selectedConnectOutObject._parent instanceof AgentRole) && ( drawObject instanceof AgentRole))
					|| ( ( _selectedConnectOutObject._parent instanceof SpotRole) && ( drawObject instanceof SpotRole)))) {
				Role role = ( Role)drawObject;
				if ( role.isConnectInObject( mouseEvent.getPoint())
					&& !role.equals( _selectedConnectOutObject._parent)
					//&& !_selectedConnectOutObject.equals( connectInObject._connectObject)) {
					&& !role._connectInObject._connectObjects.contains( _selectedConnectOutObject)
					&& !_selectedConnectOutObject._connectObjects.contains( role._connectInObject)
					&& !LayerManager.get_instance().contradict( _selectedConnectOutObject._parent._connectInObject, role)) {
					//connectInObject.disconnect();
					//_selected_connectOutObject.disconnect();
					_selectedConnectOutObject.connect( role._connectInObject);
					_selectedConnectOutObject.update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());
					role._connectInObject.update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());
					LayerManager.get_instance().modified();
				}
			}

			_selectedConnectOutObject = null;
			_connectionStartPoint = null;
			_connectionColor = null;
		} else {
			if ( null == _rubberbandStartPoint) {
				update_menu();
				return;
			}

			_rubberbandStartPoint = null;
		}

		update_menu();

		VisualShellView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_mouse_dragged(java.awt.event.MouseEvent)
	 */
	public void on_mouse_dragged(MouseEvent mouseEvent) {
		if ( 0 == ( ( InputEvent.BUTTON1_DOWN_MASK | InputEvent.BUTTON1_MASK) & mouseEvent.getModifiersEx())) {
			update_menu();
			return;
		}

		if ( 0 != ( ( InputEvent.ALT_DOWN_MASK | InputEvent.ALT_GRAPH_DOWN_MASK
			| InputEvent.ALT_GRAPH_MASK | InputEvent.ALT_MASK
			| InputEvent.CTRL_DOWN_MASK | InputEvent.CTRL_MASK
			| InputEvent.META_DOWN_MASK | InputEvent.META_MASK
			| InputEvent.SHIFT_DOWN_MASK | InputEvent.SHIFT_MASK) & mouseEvent.getModifiersEx())) {
			update_menu();
			return;
		}

		if ( null != _selectedDrawObject) {
			_selectedDrawObject.move(
				mouseEvent.getX() - _mousePosition.x,
				mouseEvent.getY() - _mousePosition.y);
			_mousePosition.setLocation( mouseEvent.getPoint());

			if ( !VisualShellView.get_instance().getVisibleRect().contains( mouseEvent.getPoint()))
				VisualShellView.get_instance().scrollRectToVisible( _selectedDrawObject.get_rectangle());

			LayerManager.get_instance().update_preferred_size( VisualShellView.get_instance());
		} else if ( null != _selectedConnectOutObject) {
			if ( null == _connectionStartPoint) {
				update_menu();
				return;
			}

			scrollRectToVisible( mouseEvent, _connectionStartPoint, _connectionEndPoint, _connectionPreviousRectangle);
		} else {
			if ( null == _rubberbandStartPoint) {
				Vector<DrawObject> drawObjects = new Vector<DrawObject>();
				LayerManager.get_instance().get_selected( drawObjects);
				if ( drawObjects.isEmpty()) {
					update_menu();
					return;
				}

				move( drawObjects, mouseEvent.getX() - _mousePosition.x, mouseEvent.getY() - _mousePosition.y);
				_mousePosition.setLocation( mouseEvent.getPoint());
			} else {
				_rubberbandEndPoint.setLocation( mouseEvent.getPoint());

				if ( 0 > _rubberbandEndPoint.x)
					_rubberbandEndPoint.x = 0;
				if ( 0 > _rubberbandEndPoint.y)
					_rubberbandEndPoint.y = 0;

				_currentRectangle.setBounds(
					Math.min( _rubberbandStartPoint.x, _rubberbandEndPoint.x),
					Math.min( _rubberbandStartPoint.y, _rubberbandEndPoint.y),
					Math.abs( _rubberbandStartPoint.x - _rubberbandEndPoint.x + 1),
					Math.abs( _rubberbandStartPoint.y - _rubberbandEndPoint.y + 1));

				if ( VisualShellView.get_instance().getVisibleRect().contains( _currentRectangle))
					VisualShellView.get_instance().scrollRectToVisible( _currentRectangle);
				else {
					_clientRectangle.setBounds( VisualShellView.get_instance().getVisibleRect());

					if ( _currentRectangle.x < VisualShellView.get_instance().getVisibleRect().x
						|| _currentRectangle.x + _currentRectangle.width > VisualShellView.get_instance().getVisibleRect().x + VisualShellView.get_instance().getVisibleRect().width) {
						if ( _currentRectangle.x == _previousRectangle.x) {
							if ( 0 != _currentRectangle.x)
								_clientRectangle.x = _currentRectangle.x + _currentRectangle.width - VisualShellView.get_instance().getVisibleRect().width;
						} else
							_clientRectangle.x = _currentRectangle.x;
					}

					if ( _currentRectangle.y < VisualShellView.get_instance().getVisibleRect().y
						|| _currentRectangle.y + _currentRectangle.height > VisualShellView.get_instance().getVisibleRect().y + VisualShellView.get_instance().getVisibleRect().height) {
						if ( _currentRectangle.y == _previousRectangle.y) {
							if ( 0 != _currentRectangle.y)
								_clientRectangle.y = _currentRectangle.y + _currentRectangle.height - VisualShellView.get_instance().getVisibleRect().height;
						} else
							_clientRectangle.y = _currentRectangle.y;
					}

					VisualShellView.get_instance().scrollRectToVisible( _clientRectangle);
				}

				_previousRectangle.setBounds( _currentRectangle);

				LayerManager.get_instance().select( _currentRectangle);
			}
		}

		update_menu();

		VisualShellView.get_instance().repaint();
	}

	/**
	 * @param mouseEvent
	 * @param startPoint
	 * @param endPoint
	 * @param previousRectangle
	 * @return
	 */
	private Rectangle scrollRectToVisible(MouseEvent mouseEvent, Point startPoint, Point endPoint, Rectangle previousRectangle) {
		endPoint.setLocation( mouseEvent.getPoint());

		if ( 0 > endPoint.x)
			endPoint.x = 0;
		if ( 0 > endPoint.y)
			endPoint.y = 0;

		Rectangle currentRectangle = new Rectangle(
			Math.min( startPoint.x, endPoint.x),
			Math.min( startPoint.y, endPoint.y),
			Math.abs( startPoint.x - endPoint.x + 1),
			Math.abs( startPoint.y - endPoint.y + 1));

		if ( VisualShellView.get_instance().getVisibleRect().contains( currentRectangle))
			VisualShellView.get_instance().scrollRectToVisible( currentRectangle);
		else {
			Rectangle clientRectangle = new Rectangle( VisualShellView.get_instance().getVisibleRect());

			if ( currentRectangle.x < VisualShellView.get_instance().getVisibleRect().x
				|| currentRectangle.x + currentRectangle.width > VisualShellView.get_instance().getVisibleRect().x + VisualShellView.get_instance().getVisibleRect().width) {
				if ( currentRectangle.x == previousRectangle.x) {
					if ( 0 != currentRectangle.x)
						clientRectangle.x = currentRectangle.x + currentRectangle.width - VisualShellView.get_instance().getVisibleRect().width;
				} else
					clientRectangle.x = currentRectangle.x;
			}

			if ( currentRectangle.y < VisualShellView.get_instance().getVisibleRect().y
				|| currentRectangle.y + currentRectangle.height > VisualShellView.get_instance().getVisibleRect().y + VisualShellView.get_instance().getVisibleRect().height) {
				if ( currentRectangle.y == previousRectangle.y) {
					if ( 0 != currentRectangle.y)
						clientRectangle.y = currentRectangle.y + currentRectangle.height - VisualShellView.get_instance().getVisibleRect().height;
				} else
					clientRectangle.y = currentRectangle.y;
			}

			VisualShellView.get_instance().scrollRectToVisible( clientRectangle);
		}

		previousRectangle.setBounds( currentRectangle);

		return currentRectangle;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	public void on_mouse_right_up(MouseEvent mouseEvent) {
//		if ( null != _selectedDrawObject) {
//			_selectedDrawObject.select( false);
//			_selectedDrawObject = null;
//			update_menu();
//			return;
//		}

		while ( remove_disconnectOneMenuItems())
			;

		_disconnectMenuItem.setEnabled( true);

		_editMenuItem.setEnabled( true);
		_removeMenuItem.setEnabled( true);
		_moveMenuItem.setEnabled( true);
		_copyMenuItem.setEnabled( true);

		_pasteMenuItem.setEnabled( !_selectedDrawobjects.isEmpty());
		_pastePosition = new Point( mouseEvent.getPoint());

		_selectAllMenuItem.setEnabled(
			!LayerManager.get_instance().get_current_layer().isEmpty());

		_flushTopMenuItem.setEnabled( true);
		_flushBottomMenuItem.setEnabled( true);
		_flushLeftMenuItem.setEnabled( true);
		_flushRightMenuItem.setEnabled( true);
		_verticalEqualLayoutMenuItem.setEnabled( true);
		_horizontalEqualLayoutMenuItem.setEnabled( true);

		_swapNamesMenuItem.setEnabled( true);

		_copyObjectsMenuItem.setEnabled( true);
		_pasteObjectsMenuItem.setEnabled( can_paste_objects());

		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		LayerManager.get_instance().get_selected( drawObjects);

		DrawObject drawObject = LayerManager.get_instance().get( mouseEvent.getPoint());
		if ( null == drawObject) {
			_disconnectMenuItem.setEnabled( false);

			_editMenuItem.setEnabled( false);
			_removeMenuItem.setEnabled( false);
			_moveMenuItem.setEnabled( false);
			_copyMenuItem.setEnabled( false);

			_deselectAllMenuItem.setEnabled( !drawObjects.isEmpty());

			_flushTopMenuItem.setEnabled( false);
			_flushBottomMenuItem.setEnabled( false);
			_flushLeftMenuItem.setEnabled( false);
			_flushRightMenuItem.setEnabled( false);
			_verticalEqualLayoutMenuItem.setEnabled( false);
			_horizontalEqualLayoutMenuItem.setEnabled( false);

			_swapNamesMenuItem.setEnabled( false);

			_copyObjectsMenuItem.setEnabled( false);
		} else {
			if ( ( drawObject instanceof Role) && ( !( ( Role)drawObject).isRole( mouseEvent.getPoint()))) {
				LayerManager.get_instance().get_current_layer().select_all( false);
				drawObject.select( true);
				VisualShellView.get_instance().repaint();
				Role role = ( Role)drawObject;
				ConnectObject connectObject = role.isConnectInObject( mouseEvent.getPoint()) ? role._connectInObject : role._connectOutObject;
				boolean disconnect = connectObject.can_disconnect();
				_disconnectMenuItem.setEnabled( disconnect);
				_selectedConnectObject = disconnect ? connectObject : null;
				if ( disconnect && 1 < connectObject._connectObjects.size()) {
					int index = _popupMenu.getComponentIndex( _disconnectMenuItem);
					if ( 0 <= index) {
						for ( int i = connectObject._connectObjects.size() - 1; i >= 0; --i) {
							ConnectObject co = ( ConnectObject)connectObject._connectObjects.get( i);
							_userInterface.insert_popup_menuitem(
								_popupMenu,
								index + 1,
								new DisconnectOneMenuItem(
									ResourceManager.get_instance().get( "edit.popup.menu.disconnect.menu") + " - " + co._parent._name),
								new DisconnectOneAction(
									ResourceManager.get_instance().get( "edit.popup.menu.disconnect.menu") + " - " + co._parent._name, co, this));
						}
					}
				}

				_editMenuItem.setEnabled( false);
				_removeMenuItem.setEnabled( false);
				_moveMenuItem.setEnabled( false);
				_copyMenuItem.setEnabled( false);

				_flushTopMenuItem.setEnabled( false);
				_flushBottomMenuItem.setEnabled( false);
				_flushLeftMenuItem.setEnabled( false);
				_flushRightMenuItem.setEnabled( false);
				_verticalEqualLayoutMenuItem.setEnabled( false);
				_horizontalEqualLayoutMenuItem.setEnabled( false);

				_swapNamesMenuItem.setEnabled( false);

				_copyObjectsMenuItem.setEnabled( false);
			} else {
				if ( drawObjects.isEmpty() && null == _selectedDrawObject) {
					_disconnectMenuItem.setEnabled( false);

					_removeMenuItem.setEnabled( !drawObject.is_global_object());

					_moveMenuItem.setEnabled( 1 < LayerManager.get_instance().size() && is_closure_connection( drawObject, drawObjects));

					_deselectAllMenuItem.setEnabled( true);

					_flushTopMenuItem.setEnabled( false);
					_flushBottomMenuItem.setEnabled( false);
					_flushLeftMenuItem.setEnabled( false);
					_flushRightMenuItem.setEnabled( false);
					_verticalEqualLayoutMenuItem.setEnabled( false);
					_horizontalEqualLayoutMenuItem.setEnabled( false);

					_swapNamesMenuItem.setEnabled( false);

					_copyObjectsMenuItem.setEnabled( is_closure_connection( drawObject, drawObjects));

					drawObject.select( true);
					VisualShellView.get_instance().repaint();
				} else {
//					if ( drawObjects.contains( drawObject)) {
					if ( ( null != _selectedDrawObject && _selectedDrawObject == drawObject) || drawObjects.contains( drawObject)) {
						_disconnectMenuItem.setEnabled( false);

						_editMenuItem.setEnabled( 1 == drawObjects.size());
						_removeMenuItem.setEnabled( !drawObject.is_global_object() && !contains_global_object( drawObjects));
						_moveMenuItem.setEnabled( 1 < LayerManager.get_instance().size() && is_closure_connection( drawObjects));

						_deselectAllMenuItem.setEnabled( true);

						_flushTopMenuItem.setEnabled( 1 < drawObjects.size());
						_flushBottomMenuItem.setEnabled( 1 < drawObjects.size());
						_flushLeftMenuItem.setEnabled( 1 < drawObjects.size());
						_flushRightMenuItem.setEnabled( 1 < drawObjects.size());
						_verticalEqualLayoutMenuItem.setEnabled( 2 < drawObjects.size());
						_horizontalEqualLayoutMenuItem.setEnabled( 2 < drawObjects.size());

						_swapNamesMenuItem.setEnabled( can_swap_names( drawObjects));

						_copyObjectsMenuItem.setEnabled( is_closure_connection( drawObjects) && can_copy_objects( drawObjects));
					} else {
						_disconnectMenuItem.setEnabled( false);

						_editMenuItem.setEnabled( false);
						_removeMenuItem.setEnabled( false);
						_moveMenuItem.setEnabled( false);
						_copyMenuItem.setEnabled( false);

						_deselectAllMenuItem.setEnabled( true);

						_flushTopMenuItem.setEnabled( false);
						_flushBottomMenuItem.setEnabled( false);
						_flushLeftMenuItem.setEnabled( false);
						_flushRightMenuItem.setEnabled( false);
						_verticalEqualLayoutMenuItem.setEnabled( false);
						_horizontalEqualLayoutMenuItem.setEnabled( false);

						_swapNamesMenuItem.setEnabled( false);

						_copyObjectsMenuItem.setEnabled( false);
					}
				}
			}
		}

		update_menu();

		_popupMenu.show( VisualShellView.get_instance(), mouseEvent.getX(), mouseEvent.getY());
	}

	/**
	 * @return
	 */
	private boolean remove_disconnectOneMenuItems() {
		for ( int i = 0; i < _popupMenu.getComponentCount(); ++i) {
			if ( _popupMenu.getComponent( i) instanceof DisconnectOneMenuItem) {
				_popupMenu.remove( _popupMenu.getComponent( i));
				return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.state.StateBase#is_closure_connection(java.util.Vector)
	 */
	public boolean is_closure_connection(Vector<DrawObject> drawObjects) {
		if ( drawObjects.isEmpty())
			return false;

		for ( DrawObject drawObject:drawObjects) {
			if ( !is_closure_connection( drawObject, drawObjects))
				return false;
		}

		return true;
	}

	/**
	 * @param drawObject
	 * @param drawObjects
	 * @return
	 */
	private boolean is_closure_connection(DrawObject drawObject, Vector<DrawObject> drawObjects) {
		if ( !( drawObject instanceof Role))
			return true;

		Role role = ( Role)drawObject;
		return role.is_closure( drawObjects);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.state.StateBase#can_swap_names(java.util.Vector)
	 */
	public boolean can_swap_names(Vector<DrawObject> drawObjects) {
		if ( 2 != drawObjects.size())
			return false;

		if ( drawObjects.get( 0) instanceof AgentRole
			&& drawObjects.get( 1) instanceof AgentRole)
			return true;

		if ( drawObjects.get( 0) instanceof SpotRole
			&& drawObjects.get( 1) instanceof SpotRole)
			return true;

		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.state.StateBase#can_copy_objects(java.util.Vector)
	 */
	public boolean can_copy_objects(Vector<DrawObject> drawObjects) {
		if ( drawObjects.isEmpty())
			return false;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.state.StateBase#can_paste_objects()
	 */
	public boolean can_paste_objects() {
		String data = Clipboard.get_text();
		if ( null == data)
			return false;

		if ( !data.equals( _password))
			return false;

		String systemTemporaryDirectory = SoarsCommonTool.get_system_temporary_directory();
		File file = new File( systemTemporaryDirectory + LayerManager._temporaryFilename);
		return ( file.exists() && file.isFile() && file.canRead());
	}

	/**
	 * @param drawObjects
	 * @return
	 */
	private boolean contains_global_object(Vector<DrawObject> drawObjects) {
		// TODO Auto-generated method stub
		for ( DrawObject drawObject:drawObjects) {
			if ( drawObject.is_global_object())
				return true;
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#paint(java.awt.Graphics)
	 */
	public void paint(Graphics graphics) {
		LayerManager.get_instance().draw( graphics, VisualShellView.get_instance(), _rubberbandStartPoint, _rubberbandEndPoint, _connectionStartPoint, _connectionEndPoint, _connectionColor);
		super.paint(graphics);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.state.StateBase#on_key_pressed(java.awt.event.KeyEvent)
	 */
	public void on_key_pressed(KeyEvent keyEvent) {
//		int ctrl_key = ( 0 <= System.getProperty( "os.name").indexOf( "Mac")) ? InputEvent.ALT_MASK : InputEvent.CTRL_MASK;
		switch ( keyEvent.getKeyCode()) {
			case KeyEvent.VK_A:
				if ( 0 != ( Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() & keyEvent.getModifiers())) {
//				if ( 0 != ( ctrl_key & keyEvent.getModifiers())) {
					LayerManager.get_instance().get_current_layer().select_all( true);
					VisualShellView.get_instance().repaint();
				}
				break;
			case KeyEvent.VK_ESCAPE:
				LayerManager.get_instance().select_all( false);
				VisualShellView.get_instance().repaint();
				break;
			case KeyEvent.VK_LEFT:
				move( -1, 0);
				VisualShellView.get_instance().repaint();
				break;
			case KeyEvent.VK_UP:
				move( 0, -1);
				VisualShellView.get_instance().repaint();
				break;
			case KeyEvent.VK_RIGHT:
				move( 1, 0);
				VisualShellView.get_instance().repaint();
				break;
			case KeyEvent.VK_DOWN:
				move( 0, 1);
				VisualShellView.get_instance().repaint();
				break;
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				on_remove( null);
				VisualShellView.get_instance().repaint();
				break;
//			case KeyEvent.VK_CONTROL:
//				_control = true;
//				break;
			case KeyEvent.VK_C:
				if ( 0 == ( Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() & keyEvent.getModifiers()))
//				if ( !_control)
					break;

				on_copy();
				break;
			case KeyEvent.VK_X:
				if ( 0 == ( Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() & keyEvent.getModifiers()))
//				if ( !_control)
					break;

				on_cut();
				break;
			case KeyEvent.VK_V:
				if ( 0 == ( Toolkit.getDefaultToolkit().getMenuShortcutKeyMask() & keyEvent.getModifiers()))
//				if ( !_control)
					break;

				_pastePosition = null;
				on_paste();
				break;
		}

		super.on_key_pressed(keyEvent);

		update_menu();
	}

	/**
	 * @param deltaX
	 * @param deltaY
	 */
	private void move(int deltaX, int deltaY) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		LayerManager.get_instance().get_selected( drawObjects);
		move( drawObjects, deltaX, deltaY);
	}

	/**
	 * @param drawObjects
	 * @param deltaX
	 * @param deltaY
	 */
	private void move(Vector<DrawObject> drawObjects, int deltaX, int deltaY) {
		for ( DrawObject drawObject:drawObjects) {
			if ( !drawObject.test( deltaX, deltaY))
				return;
		}

		for ( DrawObject drawObject:drawObjects)
			drawObject.move( deltaX, deltaY);

		LayerManager.get_instance().update_preferred_size( VisualShellView.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_disconnect(java.awt.event.ActionEvent)
	 */
	public void on_disconnect(ActionEvent actionEvent) {
		if ( null == _selectedConnectObject)
			return;

		while ( !_selectedConnectObject._connectObjects.isEmpty()) {
			ConnectObject connectObject = ( ConnectObject)_selectedConnectObject._connectObjects.get( 0);
			_selectedConnectObject.disconnect( connectObject);
			connectObject.update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());
		}

		_selectedConnectObject.update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());

		LayerManager.get_instance().modified();
		VisualShellView.get_instance().repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_disconnect_one(soars.application.visualshell.object.role.base.inheritance.ConnectObject, java.awt.event.ActionEvent)
	 */
	public void on_disconnect_one(ConnectObject connectObject, ActionEvent actionEvent) {
		if ( null == _selectedConnectObject || null == connectObject)
			return;

		_selectedConnectObject.disconnect( connectObject);
		_selectedConnectObject.update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());
		connectObject.update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());
		LayerManager.get_instance().modified();
		VisualShellView.get_instance().repaint();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		LayerManager.get_instance().get_selected( drawObjects);
		if ( 1 != drawObjects.size()) {
			update_menu();
			return;
		}

		LayerManager.get_instance().select_all( false);
		if ( null != _selectedDrawObject) {
			_selectedDrawObject.select( false);
			_selectedDrawObject = null;
		}

		LayerManager.get_instance().edit_object( ( DrawObject)drawObjects.get( 0),
			VisualShellView.get_instance(), MainFrame.get_instance());

		update_menu();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		if ( !LayerManager.get_instance().exist_selected_object()) {
			update_menu();
			return;
		}

		if ( !LayerManager.get_instance().can_remove_selected()) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					MainFrame.get_instance(),
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message2"),
					MainFrame.get_instance());
				warningDlg1.do_modal();
			}
			update_menu();
			return;
		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.object.confirm.remove.message"),
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
			LayerManager.get_instance().remove_selected();
			LayerManager.get_instance().update_name_dimension( ( Graphics2D)VisualShellView.get_instance().getGraphics());
			VisualShellView.get_instance().repaint();
		}

		update_menu();
	}

	/**
	 * @param actionEvent
	 */
	public void on_move(ActionEvent actionEvent) {
		LayerManager.get_instance().move_object(
			VisualShellView.get_instance(), MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_redo(java.awt.event.ActionEvent)
	 */
	public void on_redo(ActionEvent actionEvent) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_undo(java.awt.event.ActionEvent)
	 */
	public void on_undo(ActionEvent actionEvent) {
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_copy(java.awt.event.ActionEvent)
	 */
	public void on_copy(ActionEvent actionEvent) {
		on_copy();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_cut(java.awt.event.ActionEvent)
	 */
	public void on_cut(ActionEvent actionEvent) {
		on_cut();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_paste(java.awt.event.ActionEvent)
	 */
	public void on_paste(ActionEvent actionEvent) {
		on_paste();
	}

	/**
	 * 
	 */
	private void on_copy() {
		_selectedDrawobjects.clear();
		_pasteCounter = 0;
		LayerManager.get_instance().get_clone_of_selected( _selectedDrawobjects);
		update_menu();
	}

	/**
	 * 
	 */
	private void on_cut() {
	}

	/**
	 * 
	 */
	private void on_paste() {
		if ( _selectedDrawobjects.isEmpty()) {
			update_menu();
			return;
		}

		LayerManager.get_instance().select_all( false);
		LayerManager.get_instance().append_clone_of_selected(
			_selectedDrawobjects, _pastePosition, ++_pasteCounter, VisualShellView.get_instance());
		Observer.get_instance().modified();
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_select_all(java.awt.event.ActionEvent)
	 */
	public void on_select_all(ActionEvent actionEvent) {
		LayerManager.get_instance().get_current_layer().select_all( true);
		update_menu();
		VisualShellView.get_instance().repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.menu.basic2.IBasicMenuHandler2#on_deselect_all(java.awt.event.ActionEvent)
	 */
	public void on_deselect_all(ActionEvent actionEvent) {
		LayerManager.get_instance().get_current_layer().select_all( false);
		update_menu();
		VisualShellView.get_instance().repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_top(java.awt.event.ActionEvent)
	 */
	public void on_flush_top(ActionEvent actionEvent) {
		LayerManager.get_instance().flush_top( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_bottom(java.awt.event.ActionEvent)
	 */
	public void on_flush_bottom(ActionEvent actionEvent) {
		LayerManager.get_instance().flush_bottom( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_left(java.awt.event.ActionEvent)
	 */
	public void on_flush_left(ActionEvent actionEvent) {
		LayerManager.get_instance().flush_left( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_flush_right(java.awt.event.ActionEvent)
	 */
	public void on_flush_right(ActionEvent actionEvent) {
		LayerManager.get_instance().flush_right( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_vertical_equal_layout(java.awt.event.ActionEvent)
	 */
	public void on_vertical_equal_layout(ActionEvent actionEvent) {
		LayerManager.get_instance().vertical_equal_layout( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_horizontal_equal_layout(java.awt.event.ActionEvent)
	 */
	public void on_horizontal_equal_layout(ActionEvent actionEvent) {
		LayerManager.get_instance().horizontal_equal_layout( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_swap_names(java.awt.event.ActionEvent)
	 */
	public void on_swap_names(ActionEvent actionEvent) {
		LayerManager.get_instance().swap_names( VisualShellView.get_instance());
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_copy_objects(java.awt.event.ActionEvent)
	 */
	public void on_copy_objects(ActionEvent actionEvent) {
		if ( !MainFrame.get_instance().copy_objects( actionEvent)) {
			JOptionPane.showMessageDialog(
				MainFrame.get_instance(),
				"Could not copy objects!",
				ResourceManager.get_instance().get( "application.title"),
				JOptionPane.ERROR_MESSAGE );
			update_menu();
			return;
		}

		Clipboard.set( _password);
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.menu.edit.IEditMenuHandler#on_paste_objects(java.awt.event.ActionEvent)
	 */
	public void on_paste_objects(ActionEvent actionEvent) {
		WarningManager.get_instance().cleanup();
		if ( !MainFrame.get_instance().paste_objects( actionEvent)) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					MainFrame.get_instance(),
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message1"),
					MainFrame.get_instance());
				warningDlg1.do_modal();
			}
			update_menu();
			return;
		}
		update_menu();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.state.StateBase#get_selected_drawobjects()
	 */
	public Vector<DrawObject> get_selected_drawobjects() {
		return _selectedDrawobjects;
	}
}
