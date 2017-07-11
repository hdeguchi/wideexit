/*
 * 2005/05/18
 */
package soars.common.utility.swing.dnd.list;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.TransferHandler;

/**
 * @author kurata
 */
public class ArrayListTransferHandler extends TransferHandler {

	/**
	 * 
	 */
	private DataFlavor _localArrayListFlavor, _serialArrayListFlavor;

	/**
	 * 
	 */
	private String _localArrayListType
		= DataFlavor.javaJVMLocalObjectMimeType + ";class=java.util.ArrayList";

	/**
	 * 
	 */
	private JList _sourceList = null;

	/**
	 * 
	 */
	private int[] _indices = null;

	/**
	 * 
	 */
	private int _addIndex = -1;	// Location where items were added

	/**
	 * 
	 */
	private int _addCount = 0;		// Number of items added

	/**
	 * 
	 */
	public ArrayListTransferHandler() {
		super();
		try {
			_localArrayListFlavor = new DataFlavor( _localArrayListType);
		} catch (ClassNotFoundException e) {
			System.out.println(
				"ArrayListTransferHandler: unable to create data flavor");
		}
		_serialArrayListFlavor = new DataFlavor( ArrayList.class, "ArrayList");
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#importData(javax.swing.JComponent, java.awt.datatransfer.Transferable)
	 */
	public boolean importData(JComponent component, Transferable transferable) {
		JList targetList = null;
		ArrayList arrayList = null;
		if ( !canImport( component, transferable.getTransferDataFlavors()))
			return false;

		targetList = ( JList)component;

		try {
			if ( hasLocalArrayListFlavor( transferable.getTransferDataFlavors()))
				arrayList = ( ArrayList)transferable.getTransferData( _localArrayListFlavor);
			else if ( hasSerialArrayListFlavor( transferable.getTransferDataFlavors()))
				arrayList = ( ArrayList)transferable.getTransferData( _serialArrayListFlavor);
			else
				return false;
		} catch (UnsupportedFlavorException ufe) {
			System.out.println( "importData: unsupported data flavor");
			return false;
		} catch (IOException ioe) {
			System.out.println( "importData: I/O exception");
			return false;
		}

		// At this point we use the same code to retrieve the data
		// locally or serially.

		// We'll drop at the current selected index.
		int index = targetList.getSelectedIndex();

		// Prevent the user from dropping data back on itself.
		// For example, if the user is moving items #4,#5,#6 and #7 and
		// attempts to insert the items after item #5, this would
		// be problematic when removing the original items.
		// This is interpreted as dropping the same data on itself
		// and has no effect.
		if ( _sourceList.equals( targetList)) {
			//if ( null != _indices && _indices[ 0] - 1 <= index
			//	&& _indices[ _indices.length - 1] >= index) {
			if ( null != _indices && _indices[ 0] <= index
				&& _indices[ _indices.length - 1] >= index) {
				_indices = null;
				return true;
			}
		}

		DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)targetList.getModel();
		int max = defaultComboBoxModel.getSize();
		if ( index < 0)
			index = max; 

		if ( index > _indices[ 0]) {
			index++;
			if ( index > max) {
				index = max;
			}
		}
		_addIndex = index;
		_addCount = arrayList.size();
		for ( int i = 0; i < arrayList.size(); ++i)
			defaultComboBoxModel.insertElementAt( arrayList.get( i), index++);

		return true;
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
	 */
	protected void exportDone(JComponent component, Transferable transferable, int action) {
		if ( ( MOVE == action) && ( null != _indices)) {
			DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)_sourceList.getModel();

			// If we are moving items around in the same list, we
			// need to adjust the indices accordingly since those
			// after the insertion point have moved.
			if ( 0 < _addCount) {
				for ( int i = 0; i < _indices.length; ++i) {
					if ( _indices[ i] > _addIndex) {
						_indices[ i] += _addCount;
					}
				}
			}

			for ( int i = _indices.length - 1; i >= 0; --i)
				defaultComboBoxModel.removeElementAt( _indices[ i]);

			select( ( JList)component, transferable);
		}
		_indices = null;
		_addIndex = -1;
		_addCount = 0;
	}

	/**
	 * @param targetList
	 * @param transferable
	 */
	private void select(JList targetList, Transferable transferable) {
		ArrayList arrayList = null;
		try {
			if ( hasLocalArrayListFlavor( transferable.getTransferDataFlavors()))
				arrayList = ( ArrayList)transferable.getTransferData( _localArrayListFlavor);
			else if ( hasSerialArrayListFlavor( transferable.getTransferDataFlavors()))
				arrayList = ( ArrayList)transferable.getTransferData( _serialArrayListFlavor);
		} catch (UnsupportedFlavorException ufe) {
			System.out.println( "select: unsupported data flavor");
			return;
		} catch (IOException ioe) {
			System.out.println( "select: I/O exception");
			return;
		}

		if ( null != arrayList) {
			DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)targetList.getModel();
			for ( int i = 0; i < defaultComboBoxModel.getSize(); ++i) {
				Object object = defaultComboBoxModel.getElementAt( i);
				if ( arrayList.contains( object))
					targetList.setSelectedValue( object, true);
			}
		}
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#canImport(javax.swing.JComponent, java.awt.datatransfer.DataFlavor[])
	 */
	public boolean canImport(JComponent component, DataFlavor[] dataFlavors) {
		if ( hasLocalArrayListFlavor( dataFlavors))
			return true;
		if ( hasSerialArrayListFlavor( dataFlavors))
			return true;
		return false;
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#createTransferable(javax.swing.JComponent)
	 */
	protected Transferable createTransferable(JComponent component) {
		if ( !( component instanceof JList))
			return null;

		_sourceList = ( JList)component;
		_indices = _sourceList.getSelectedIndices();

    List<Object> values = _sourceList.getSelectedValuesList();
    if (values == null || values.isEmpty())
			return null;

		ArrayList<Object> arrayList = new ArrayList<Object>();
		for ( Object value:values)
			arrayList.add( value);

		return new ArrayListTransferable( arrayList);
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#getSourceActions(javax.swing.JComponent)
	 */
	public int getSourceActions(JComponent arg0) {
		return COPY_OR_MOVE;
	}

	/**
	 * @param flavors
	 * @return
	 */
	private boolean hasLocalArrayListFlavor(DataFlavor[] flavors) {
		if ( null == _localArrayListFlavor)
			return false;

		for ( int i = 0; i < flavors.length; ++i) {
			if ( flavors[ i].equals( _localArrayListFlavor)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param flavors
	 * @return
	 */
	private boolean hasSerialArrayListFlavor(DataFlavor[] flavors) {
		if ( null == _serialArrayListFlavor)
			return false;

		for ( int i = 0; i < flavors.length; ++i) {
			if ( flavors[ i].equals( _serialArrayListFlavor)) {
				return true;
			}
		}
		return false;
	}

	public class ArrayListTransferable implements Transferable {

		private ArrayList _arrayList;

		/**
		 * @param arrayList
		 */
		public ArrayListTransferable(ArrayList arrayList) {
			super();
			_arrayList = arrayList;
		}

		/* (Non Javadoc)
		 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
		 */
		public Object getTransferData(DataFlavor dataFlavor)
			throws UnsupportedFlavorException, IOException {
			if ( !isDataFlavorSupported( dataFlavor)) {
				throw new UnsupportedFlavorException( dataFlavor);
			}
			return _arrayList;
		}

		/* (Non Javadoc)
		 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
		 */
		public DataFlavor[] getTransferDataFlavors() {
			return new DataFlavor[] { _localArrayListFlavor, _serialArrayListFlavor};
		}

		/* (Non Javadoc)
		 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
		 */
		public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
			if ( _localArrayListFlavor.equals( dataFlavor))
				return true;

			if ( _serialArrayListFlavor.equals( dataFlavor))
				return true;

			return false;
		}
	}
}
