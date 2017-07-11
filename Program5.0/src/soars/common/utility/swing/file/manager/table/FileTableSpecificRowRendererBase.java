/**
 * 
 */
package soars.common.utility.swing.file.manager.table;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Point;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import soars.common.utility.swing.file.manager.Constant;

/**
 * @author kurata
 *
 */
public class FileTableSpecificRowRendererBase extends Box implements TableCellRenderer {

	/**
	 * 
	 */
	protected final Border _emptyBorder = BorderFactory.createEmptyBorder( 1, 1, 1, 1);

	/**
	 * 
	 */
	protected Icon _directoryCloseIcon = null;

	/**
	 * 
	 */
	protected Icon _leafIcon = null;

	/**
	 * 
	 */
	protected final JLabel _textLabel;

	/**
	 * 
	 */
	protected final JLabel _iconLabel;

	/**
	 * @param table
	 */
	public FileTableSpecificRowRendererBase(JTable table) {
		super(BoxLayout.X_AXIS);

		_directoryCloseIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/directory_close.png"));
		_leafIcon = new ImageIcon( getClass().getResource( Constant._resourceDirectory + "/image/file.png"));

		_textLabel = new JLabel( "dummy");
		_textLabel.setOpaque( true);
		_textLabel.setBorder( _emptyBorder);
		_iconLabel = new JLabel( _directoryCloseIcon);
		_iconLabel.setBorder(BorderFactory.createEmptyBorder());
		table.setRowHeight( Math.max( _textLabel.getPreferredSize().height, _iconLabel.getPreferredSize().height));
		add( _iconLabel);
		add( _textLabel);
		add( Box.createHorizontalGlue());
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object arg1, boolean arg2, boolean arg3, int arg4, int arg5) {
		FileTableBase fileTableBase = ( FileTableBase)arg0;
		if ( null != arg1) {
			File file = ( File)arg1;
			_textLabel.setText( file.getName());
			on_setIcon( _iconLabel, file);
			FontMetrics fontMetrics = arg0.getFontMetrics( arg0.getFont());
			int swidth = fontMetrics.stringWidth( _textLabel.getText()) + _textLabel.getInsets().left + _textLabel.getInsets().right;
			int cwidth = arg0.getColumnModel().getColumn( arg5).getWidth() - _iconLabel.getPreferredSize().width;
			_textLabel.setPreferredSize( new Dimension( swidth > cwidth ? cwidth : swidth, 0));
			if ( arg2 || arg1 == fileTableBase._dropTargetItem) {
				_textLabel.setForeground( arg0.getSelectionForeground());
				_textLabel.setBackground( arg0.getSelectionBackground());
			} else {
				_textLabel.setForeground( arg0.getForeground());
				_textLabel.setBackground( arg0.getBackground());
			}
			if ( arg3) {
				_textLabel.setBorder( UIManager.getBorder( "Table.focusCellHighlightBorder"));
			} else {
				_textLabel.setBorder( _emptyBorder);
			}
			_textLabel.setFont( arg0.getFont());
		}

		return this;
	}

	/**
	 * @param file
	 * @param position
	 * @param table
	 * @return
	 */
	public boolean is_on_label(File file, Point position, JTable table) {
		JLabel textLabel = new JLabel( file.getName());
		JLabel iconLabel = new JLabel( _directoryCloseIcon);
		on_setIcon( iconLabel, file);
		FontMetrics fontMetrics = table.getFontMetrics( table.getFont());
		int swidth = fontMetrics.stringWidth( textLabel.getText()) + textLabel.getInsets().left + textLabel.getInsets().right;
		int cwidth = table.getColumnModel().getColumn( 0).getWidth() - iconLabel.getPreferredSize().width;
		textLabel.setPreferredSize( new Dimension( swidth > cwidth ? cwidth : swidth, 0));
		int width = ( iconLabel.getPreferredSize().width + textLabel.getPreferredSize().width);
		return ( 0 <= position.x && position.x <= width);
	}

	/**
	 * @param iconLabel
	 * @param file
	 */
	protected void on_setIcon(JLabel iconLabel, File file) {
		iconLabel.setIcon( file.isDirectory() ? _directoryCloseIcon : _leafIcon);
	}
}
