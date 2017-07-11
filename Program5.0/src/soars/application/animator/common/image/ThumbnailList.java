/**
 * 
 */
package soars.application.animator.common.image;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import soars.application.animator.common.image.menu.AppendAction;
import soars.application.animator.common.image.menu.EditAction;
import soars.application.animator.common.image.menu.RemoveAction;
import soars.application.animator.common.tool.CommonTool;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.observer.Observer;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.swing.progress.IProgressCallback;
import soars.common.utility.swing.progress.ProgressDlg;
import soars.common.utility.swing.thumbnail.ThumbnailItem;
import soars.common.utility.swing.thumbnail.ThumbnailListBase;
import soars.common.utility.tool.resource.Resource;

/**
 * The thumbnail list class.
 * @author kurata / SOARS project
 */
public class ThumbnailList extends ThumbnailListBase {

	/**
	 * Key mapped to the default directory for the file chooser dialog.
	 */
	private String _openDirectoryKey = "";

	/**
	 * Thumbnail selection handler.
	 */
	private IThumbnailListCallback _thumbnailListCallback = null;

	/**
	 * Common GUI panel for thumbnail list.
	 */
	private ThumbnailPanel _thumbnailPanel = null;

	/**
	 * Context menu item to add a new thumbnail.
	 */
	private JMenuItem _appendMenuItem = null;

	/**
	 * Context menu item to rename the thumbnail.
	 */
	private JMenuItem _editMenuItem = null;

	/**
	 * Context menu item to remove thumbnails.
	 */
	private JMenuItem _removeMenuItem = null;

	/**
	 * Creates the thumbnail list.
	 * @param imageDirectory the image directory
	 * @param thumbnailImageDirectory the thumbnail directory
	 * @param openDirectoryKey the key mapped to the default directory for JFileChooser
	 * @param thumbnailListCallback the thumbnail selection handler
	 * @param thumbnailPanel the common GUI panel for thumbnail list
	 * @param owner the frame of the parent container
	 * @param parent the parent container of this component
	 */
	public ThumbnailList(File imageDirectory, File thumbnailImageDirectory, String openDirectoryKey, IThumbnailListCallback thumbnailListCallback, ThumbnailPanel thumbnailPanel, Frame owner, Component parent) {
		super(imageDirectory, thumbnailImageDirectory, ImagePropertyManager.get_instance(),
			ResourceManager.get_instance().get( "application.title"), 
			new String[] { ResourceManager.get_instance().get( "thumbnail.load.show.message"),
				ResourceManager.get_instance().get( "thumbnail.make.show.message"),
				ResourceManager.get_instance().get( "thumbnail.confirm.overwrite.message"),
				ResourceManager.get_instance().get( "thumbnail.confirm.remove.message")},
			new String[] { ResourceManager.get_instance().get( "dialog.yes"),
				ResourceManager.get_instance().get( "dialog.no"),
				ResourceManager.get_instance().get( "dialog.yes.to.all"),
				ResourceManager.get_instance().get( "dialog.no.to.all"),
				ResourceManager.get_instance().get( "dialog.cancel")},
			ResourceManager.get_instance().get( "dialog.cancel"), owner, parent);
		_max = AnimatorImageManager._thumbnailMax;
		_side = AnimatorImageManager._thumbnailSize;
		_openDirectoryKey = openDirectoryKey;
		_thumbnailListCallback = thumbnailListCallback;
		_thumbnailPanel = thumbnailPanel;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#modified()
	 */
	protected void modified() {
		Observer.get_instance().modified();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#changed()
	 */
	protected void changed() {
		_thumbnailPanel.update();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#changed(java.lang.String[])
	 */
	protected void changed(String[] filenames) {
		changed();

		if ( 0 < filenames.length) {
			ProgressDlg.execute( _owner, _title + " - " + ResourceManager.get_instance().get( "thumbnail.update.show.message"),
				true, "update", filenames, ( IProgressCallback)this, _parent);
			Administrator.get_instance().repaint();
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#update(java.lang.String[], soars.common.utility.swing.progress.ProgressDlg)
	 */
	protected boolean update(String[] filenames, ProgressDlg progressDlg) {
		for ( int i = 0; i < filenames.length; ++i) {
			progressDlg.set( ( int)( 100.0 * ( double)( i + 1) / ( double)filenames.length));
			File file = new File( Administrator.get_instance().get_image_directory(), filenames[ i]);
			BufferedImage bufferedImage = Resource.load_image( file.getAbsolutePath());
			if ( null == bufferedImage)
				continue;

			AnimatorImageManager.get_instance().put( file.getAbsolutePath(), bufferedImage);
		}
		return true;
	}

	/**
	 * Returns true if this component is initialized successfully.
	 * @param imageFilename the image filename
	 * @param popupMenu true if the context menu is used
	 * @param singleSelection true if single-item selection is set
	 * @return if this component is initialized successfully
	 */
	public boolean setup(String imageFilename, boolean popupMenu, boolean singleSelection) {
		if ( !super.setup(imageFilename, popupMenu))
			return false;

		if ( singleSelection)
			setSelectionMode( ListSelectionModel.SINGLE_SELECTION);

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#setup_popup_menu()
	 */
	protected void setup_popup_menu() {
		super.setup_popup_menu();

		_appendMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "thumbnail.new.imagefiles.menu"),
			new AppendAction( ResourceManager.get_instance().get( "thumbnail.new.imagefiles.menu"), this),
			ResourceManager.get_instance().get( "thumbnail.new.imagefiles.mnemonic"),
			ResourceManager.get_instance().get( "thumbnail.new.imagefiles.stroke"));

		_popupMenu.addSeparator();

		_editMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "thumbnail.edit.imagefile.menu"),
			new EditAction( ResourceManager.get_instance().get( "thumbnail.edit.imagefile.menu"), this),
			ResourceManager.get_instance().get( "thumbnail.edit.imagefile.mnemonic"),
			ResourceManager.get_instance().get( "thumbnail.edit.imagefile.stroke"));

		_popupMenu.addSeparator();

		_removeMenuItem = _userInterface.append_popup_menuitem(
			_popupMenu,
			ResourceManager.get_instance().get( "thumbnail.remove.imagefiles.menu"),
			new RemoveAction( ResourceManager.get_instance().get( "thumbnail.remove.imagefiles.menu"), this),
			ResourceManager.get_instance().get( "thumbnail.remove.imagefiles.mnemonic"),
			ResourceManager.get_instance().get( "thumbnail.remove.imagefiles.stroke"));
	}

	/**
	 * Arranges all components.
	 */
	public void adjust() {
		setVisibleRowCount( 0);
//		int[] indices = getSelectedIndices();
//		if ( 1 == indices.length)
//			scrollRectToVisible( getCellBounds( indices[ 0], indices[ 0]));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#on_mouse_left_double_click(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_left_double_click(MouseEvent mouseEvent) {
		super.on_mouse_left_double_click(mouseEvent);

		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		if ( !getCellBounds( index, index).contains( mouseEvent.getPoint()))
			return;

		on_select();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_mouse_right_up(java.awt.event.MouseEvent)
	 */
	protected void on_mouse_right_up(MouseEvent mouseEvent) {
		_appendMenuItem.setEnabled( true);
		_editMenuItem.setEnabled( true);
		_removeMenuItem.setEnabled( true);

		if ( 0 == _defaultComboBoxModel.getSize() /*|| -1 == index*/) {
			_editMenuItem.setEnabled( false);
			_removeMenuItem.setEnabled( false);
		} else {
			int index = locationToIndex( mouseEvent.getPoint());
			if ( 0 <= index && _defaultComboBoxModel.getSize() > index
				&& getCellBounds( index, index).contains( mouseEvent.getPoint())) {
				int[] indices = getSelectedIndices();
				if ( 0 == indices.length) {
					_editMenuItem.setEnabled( false);
					_removeMenuItem.setEnabled( false);
				} else {
					boolean contains = ( 0 <= Arrays.binarySearch( indices, index));
					if ( !contains) {
						setSelectedIndex( index);
					} else {
						if ( 1 == indices.length) {
							setSelectedIndex( index);
						} else {
							_editMenuItem.setEnabled( false);
						}
					}
				}
			} else {
				_editMenuItem.setEnabled( false);
				_removeMenuItem.setEnabled( false);
			}
		}

		_popupMenu.show( this, mouseEvent.getX(), mouseEvent.getY());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.list.StandardList#on_key_pressed(java.awt.event.KeyEvent)
	 */
	protected void on_key_pressed(KeyEvent keyEvent) {
		int index = getSelectedIndex();
		if ( 0 == _defaultComboBoxModel.getSize() || -1 == index)
			return;

		switch ( keyEvent.getKeyCode()) {
			case KeyEvent.VK_ENTER:
				on_select();
				break;
			case KeyEvent.VK_DELETE:
			case KeyEvent.VK_BACK_SPACE:
				on_remove( null);
				break;
		}
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#on_select()
	 */
	protected void on_select() {
		int[] indices = getSelectedIndices();
		if ( 1 != indices.length)
			return;

		_thumbnailListCallback.selected( ( ThumbnailItem)_defaultComboBoxModel.getElementAt( indices[ 0]));
	}

	/**
	 * Invoked when the context menu item to add a new thumbnail is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_append(ActionEvent actionEvent) {
		File[] files = CommonTool.get_imagefiles( _openDirectoryKey, _parent);
		if ( null == files)
			return;

		append( files);
	}

	/**
	 * Invoked when the context menu item to rename the thumbnail is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_edit(ActionEvent actionEvent) {
		int index = getSelectedIndex();
		if ( 0 > index)
			return;

		ThumbnailItem thumbnailItem = ( ThumbnailItem)_defaultComboBoxModel.getElementAt( index);

		String originalFilename = thumbnailItem._file.getName();

		EditImageFilenameDlg editImageFilenameDlg = new EditImageFilenameDlg( MainFrame.get_instance(),
			ResourceManager.get_instance().get( "edit.image.filename.dialog.title"),
			true, originalFilename, _imagePropertyManager);
		if ( !editImageFilenameDlg.do_modal( _parent))
			return;

		String newFilename = editImageFilenameDlg._newFilename;

		if ( !rename( thumbnailItem, newFilename))
			return;

		_thumbnailListCallback.update();

		File file = new File( _imageDirectory, originalFilename);
		Image image = AnimatorImageManager.get_instance().get( file.getAbsolutePath());
		if ( null != image) {
			AnimatorImageManager.get_instance().remove( file.getAbsolutePath());
			file = new File( _imageDirectory, newFilename);
			AnimatorImageManager.get_instance().put( file.getAbsolutePath(), image);
		}

		Administrator.get_instance().update_image( originalFilename, newFilename);

		Administrator.get_instance().repaint();

		modified();
	}

	/**
	 * Invoked when the context menu item to remove thumbnails is selected.
	 * @param actionEvent the semantic event which indicates that a component-defined action occurred
	 */
	public void on_remove(ActionEvent actionEvent) {
		if ( !remove())
			return;

		_thumbnailListCallback.update();
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.thumbnail.ThumbnailListBase#can_remove(int[])
	 */
	protected boolean can_remove(int[] indices) {
		// TODO 今後リストで表示する必要がある
		boolean result = true;
		for ( int i = 0; i < indices.length; ++i) {
			ThumbnailItem thumbnailItem = ( ThumbnailItem)_defaultComboBoxModel.getElementAt( indices[ i]);
			if ( Administrator.get_instance().uses_this_image( thumbnailItem._file.getName())) {
				JOptionPane.showMessageDialog(
					_parent,
					ResourceManager.get_instance().get( "thumbnail.could.not.remove.show.message") +  " " + thumbnailItem._file.getName(),
					ResourceManager.get_instance().get( "application.title"),
					JOptionPane.ERROR_MESSAGE);

				return false;
			}
		}
		return true;
	}
}
