/*
 * 2005/05/01
 */
package soars.application.visualshell.object.stage.edit;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import soars.application.visualshell.common.swing.ListBase;
import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.stage.Stage;
import soars.application.visualshell.observer.WarningDlg1;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.common.Tool;

/**
 * @author kurata
 */
public class StageList extends ListBase implements DropTargetListener {

	/**
	 * 
	 */
	private StageList[] _stageLists = null;

	/**
	 * 
	 */
	private String[] _messages = null;

	/**
	 * 
	 */
	public Point _dropPosition = null;

	/**
	 * 
	 */
	private EditRoleFrame _editRoleFrame = null;

	/**
	 * @param messages
	 * @param editRoleFrame
	 * @param owner
	 * @param parent
	 */
	public StageList(String[] messages, EditRoleFrame editRoleFrame, Frame owner, Component parent) {
		super(owner, parent);
		_messages = messages;
		_editRoleFrame = editRoleFrame;
	}

	/**
	 * @param name
	 * @return
	 */
	public boolean contains(String name) {
		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i) {
			StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( i);
			if ( name.equals( stageCheckBox._stage._name))
				return true;
		}
		return false;
	}

	/**
	 * @param stage
	 * @return
	 */
	public boolean contains(Stage stage) {
		for ( int i = 0; i < _defaultComboBoxModel.getSize(); ++i) {
			StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( i);
			if ( stage.equals( stageCheckBox._stage))
				return true;
		}
		return false;
	}

	/**
	 * @param stages
	 * @param stageLists
	 * @param stageListTransferHandler
	 * @return
	 */
	public boolean setup(Vector<Stage> stages, StageList[] stageLists, StageListTransferHandler stageListTransferHandler) {
		//if ( !super.setup(true, true))
		if ( !super.setup(true, false))
			return false;

		_stageLists = stageLists;


		setTransferHandler( stageListTransferHandler);
		setDragEnabled( true);

		try {
			getDropTarget().addDropTargetListener( this);
		} catch (TooManyListenersException e) {
			e.printStackTrace();
		}

		for ( Stage stage:stages) {
			StageCheckBox stageCheckBox = new StageCheckBox( stage);
			_defaultComboBoxModel.addElement( stageCheckBox);
		}

		setCellRenderer( new StageListCellRenderer());

		setSelectedIndex( 0);

		setToolTipText( "StageList");

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_left_down(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_down(MouseEvent mouseEvent) {
		Point point = mouseEvent.getPoint();
		if ( 16 <= point.x)
			return;

		int index = locationToIndex( point);
		if ( 0 > index)
			return;

		StageCheckBox stageCheckBox = ( StageCheckBox)getModel().getElementAt( index);

		if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
			|| stageCheckBox._stage._name.equals( Constant._updateChartStageName))
			return;

		stageCheckBox._stage._random = !stageCheckBox._stage._random;

		repaint();
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.swing.ListBase#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		Point point = mouseEvent.getPoint();
		if ( !getCellBounds( index, index).contains( point))
			return;

		if ( 16 > point.x)
			return;

		on_edit( null);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		_insertMenuItem.setEnabled( true);
		_editMenuItem.setEnabled( true);
		_removeMenuItem.setEnabled( null == _editRoleFrame);

		if ( 0 == _defaultComboBoxModel.getSize() /*|| -1 == index*/) {
			_insertMenuItem.setEnabled( false);
			_editMenuItem.setEnabled( false);
			_removeMenuItem.setEnabled( false);
		} else {
			int index = locationToIndex( mouseEvent.getPoint());
			if ( 0 <= index && _defaultComboBoxModel.getSize() > index
				&& getCellBounds( index, index).contains( mouseEvent.getPoint())) {
				int[] indices = getSelectedIndices();
				if ( 0 == indices.length) {
					_insertMenuItem.setEnabled( false);
					_editMenuItem.setEnabled( false);
					_removeMenuItem.setEnabled( false);
				} else {
					boolean contains = ( 0 <= Arrays.binarySearch( indices, index));
					if ( !contains) {
						setSelectedIndex( index);
						StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( index);
						if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
							|| stageCheckBox._stage._name.equals( Constant._updateChartStageName)) {
							_editMenuItem.setEnabled( false);
							_removeMenuItem.setEnabled( false);
						}
					} else {
						if ( 1 == indices.length) {
							setSelectedIndex( index);
							StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( index);
							if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
								|| stageCheckBox._stage._name.equals( Constant._updateChartStageName)) {
								_editMenuItem.setEnabled( false);
								_removeMenuItem.setEnabled( false);
							}
						} else {
							_insertMenuItem.setEnabled( false);
							_editMenuItem.setEnabled( false);
							for ( int i = 0; i < indices.length; ++i) {
								StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( indices[ i]);
								if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
									|| stageCheckBox._stage._name.equals( Constant._updateChartStageName)) {
									_removeMenuItem.setEnabled( false);
									break;
								}
							}
						}
					}
				}
			} else {
				_insertMenuItem.setEnabled( false);
				_editMenuItem.setEnabled( false);
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_append(java.awt.event.ActionEvent)
	 */
	public void on_append(ActionEvent actionEvent) {
		EditStagePropertyDlg editStagePropertyDlg
			= new EditStagePropertyDlg( _owner, _messages[ 0], 	true, this, _stageLists, _editRoleFrame);
		if ( !editStagePropertyDlg.do_modal( _parent))
			return;

		_defaultComboBoxModel.addElement( new StageCheckBox( editStagePropertyDlg._stage));
		setSelectedIndex( _defaultComboBoxModel.getSize() - 1);
		scrollRectToVisible( getCellBounds( _defaultComboBoxModel.getSize() - 1,
			_defaultComboBoxModel.getSize() - 1));
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_insert(java.awt.event.ActionEvent)
	 */
	public void on_insert(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		EditStagePropertyDlg editStagePropertyDlg
			= new EditStagePropertyDlg( _owner, _messages[ 0], true, this, _stageLists, _editRoleFrame);
		if ( !editStagePropertyDlg.do_modal( _parent))
			return;
	
		_defaultComboBoxModel.insertElementAt( new StageCheckBox( editStagePropertyDlg._stage), indices[ 0]);
		setSelectedIndex( indices[ 0]);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_edit(java.awt.event.ActionEvent)
	 */
	public void on_edit(ActionEvent actionEvent) {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( indices[ 0]);

		if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
			|| stageCheckBox._stage._name.equals( Constant._updateChartStageName))
			return;

		EditStagePropertyDlg editStagePropertyDlg
			= new EditStagePropertyDlg( _owner, _messages[ 1], true, stageCheckBox._stage, this, _stageLists, _editRoleFrame);
		if ( !editStagePropertyDlg.do_modal( _parent))
			return;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.common.menu.basic1.IBasicMenuHandler1#on_remove(java.awt.event.ActionEvent)
	 */
	public void on_remove(ActionEvent actionEvent) {
		if ( null != _editRoleFrame)
			return;

		int[] indices = getSelectedIndices();
		if ( 1 > indices.length)
			return;

		WarningManager.get_instance().cleanup();
		boolean canRemove = true;
		for ( int i = 0; i < indices.length; ++i) {
			StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( indices[ i]);
			if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
				|| stageCheckBox._stage._name.equals( Constant._updateChartStageName))
				return;

			if ( !LayerManager.get_instance().can_remove_stage_name( stageCheckBox._stage._name))
				canRemove = false;

			if ( null != _editRoleFrame && !_editRoleFrame.can_remove_stage_name( stageCheckBox._stage._name))
				canRemove = false;
		}

		if ( !canRemove) {
			if ( !WarningManager.get_instance().isEmpty()) {
				WarningDlg1 warningDlg1 = new WarningDlg1(
					_owner,
					ResourceManager.get_instance().get( "warning.dialog1.title"),
					ResourceManager.get_instance().get( "warning.dialog1.message2"),
					_parent);
				warningDlg1.do_modal();
			}
			return;
		}

		if ( JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
			_parent,
			_messages[ 2],
			ResourceManager.get_instance().get( "application.title"),
			JOptionPane.YES_NO_OPTION)) {
//			boolean modified = false;
			indices = Tool.quick_sort_int( indices, true);
			for ( int i = indices.length - 1; i >= 0; --i) {
				StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( indices[ i]);
				_defaultComboBoxModel.removeElementAt( indices[ i]);
//				if ( StageManager.get_instance().remove( stageCheckBox._stage._name))
//					modified = true;

				stageCheckBox._stage.cleanup();
			}

//			if ( modified) {
//				Observer.get_instance().on_update_stage();
//				Observer.get_instance().modified();
//			}

			if ( 0 < _defaultComboBoxModel.getSize()) {
				if ( indices[ 0] < _defaultComboBoxModel.getSize())
					setSelectedIndex( indices[ 0]);
				else
					setSelectedIndex( _defaultComboBoxModel.getSize() - 1);
			}
		}
	}

	/**
	 * @return
	 */
	public Vector<String> get_stage_names() {
		Vector<String> names = new Vector<String>();

		DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)getModel();
		for ( int i = 0; i < defaultComboBoxModel.getSize(); ++i) {
			StageCheckBox stageCheckBox = ( StageCheckBox)defaultComboBoxModel.getElementAt( i);
			names.add( stageCheckBox._stage._name);
		}

		return names;
	}

	/**
	 * @param stages
	 */
	public void on_ok(Vector<Stage> stages) {
		stages.clear();
		DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)getModel();
		for ( int i = 0; i < defaultComboBoxModel.getSize(); ++i) {
			StageCheckBox stageCheckBox = ( StageCheckBox)defaultComboBoxModel.getElementAt( i);
			stages.add( stageCheckBox._stage);
		}
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragEnter(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragEnter(DropTargetDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragOver(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dragOver(DropTargetDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dropActionChanged(java.awt.dnd.DropTargetDragEvent)
	 */
	public void dropActionChanged(DropTargetDragEvent arg0) {
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#drop(java.awt.dnd.DropTargetDropEvent)
	 */
	public void drop(DropTargetDropEvent arg0) {
		_dropPosition = arg0.getLocation();
	}

	/* (Non Javadoc)
	 * @see java.awt.dnd.DropTargetListener#dragExit(java.awt.dnd.DropTargetEvent)
	 */
	public void dragExit(DropTargetEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see javax.swing.JList#getToolTipText(java.awt.event.MouseEvent)
	 */
	public String getToolTipText(MouseEvent mouseEvent) {
		int index = locationToIndex( mouseEvent.getPoint());
		if ( 0 > index || _defaultComboBoxModel.getSize() <= index)
			return null;

		StageCheckBox stageCheckBox = ( StageCheckBox)_defaultComboBoxModel.getElementAt( index);
		if ( null == stageCheckBox)
			return null;

		if ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName))
			return ResourceManager.get_instance().get( "initialize.chart.stage.comment");
		else if ( stageCheckBox._stage._name.equals( Constant._updateChartStageName))
			return ResourceManager.get_instance().get( "update.chart.stage.comment");

		return stageCheckBox._stage._comment.equals( "") ? null : stageCheckBox._stage._comment;
	}
}
