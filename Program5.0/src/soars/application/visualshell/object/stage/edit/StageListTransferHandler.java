/*
 * 2005/06/17
 */
package soars.application.visualshell.object.stage.edit;

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

import soars.application.visualshell.main.Constant;

/**
 * @author kurata
 */
public class StageListTransferHandler extends TransferHandler {

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
	private ArrayList _arrayList = null;

	/**
	 * 
	 */
	private JList _sourceList = null;

	/**
	 * 
	 */
	private JList _targetList = null;

	/**
	 * 
	 */
	private int[] _indices = null;

	/**
	 * 
	 */
	private StageList _mainStageList = null;

	/**
	 * 
	 */
	private StageList _initialStageList = null;

	/**
	 * 
	 */
	private StageList _terminalStageList = null;

	/**
	 * @param initialStageList
	 * @param mainStageList
	 * @param terminalStageList
	 * 
	 */
	public StageListTransferHandler(StageList mainStageList, StageList initialStageList, StageList terminalStageList) {
		super();
		_mainStageList = mainStageList;
		_initialStageList = initialStageList;
		_terminalStageList = terminalStageList;

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
		StageList targetList = ( StageList)component;

		if ( !canImport( targetList, transferable.getTransferDataFlavors()))
			return false;

		try {
			if ( hasLocalArrayListFlavor( transferable.getTransferDataFlavors()))
				_arrayList = ( ArrayList)transferable.getTransferData( _localArrayListFlavor);
			else if ( hasSerialArrayListFlavor( transferable.getTransferDataFlavors()))
				_arrayList = ( ArrayList)transferable.getTransferData( _serialArrayListFlavor);
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

		_targetList = targetList;

		return true;
	}

	/* (Non Javadoc)
	 * @see javax.swing.TransferHandler#exportDone(javax.swing.JComponent, java.awt.datatransfer.Transferable, int)
	 */
	protected void exportDone(JComponent component, Transferable transferable, int action) {

		int addIndex = -1;	// Location where items were added
		int addCount = 0;		// Number of items added

		List list = new ArrayList();

		if ( null != _arrayList && null != _targetList) {
			// At this point we use the same code to retrieve the data
			// locally or serially.

			// We'll drop at the current selected index.
			addIndex = _targetList.getSelectedIndex();

			StageList targetStageList = ( StageList)_targetList;
			DefaultComboBoxModel targetDefaultComboBoxModel = ( DefaultComboBoxModel)_targetList.getModel();
			int max = targetDefaultComboBoxModel.getSize();
			if ( addIndex < 0)
				addIndex = max;
			else {
				if ( null != targetStageList._dropPosition && 0 < max
					&& !_targetList.getCellBounds( 0, max - 1).contains( targetStageList._dropPosition))
					addIndex = max;
			}

			if ( _sourceList.equals( _targetList)) {
				if ( addIndex > _indices[ 0]) {
					//addIndex++;
					if ( _indices[ 0] + 1 == addIndex)
						addIndex++;
					if ( addIndex > max) {
						addIndex = max;
					}
				}
			}

			int index = addIndex;
			addCount = _arrayList.size();
			for ( int i = 0; i < _arrayList.size(); ++i) {
				StageCheckBox stageCheckBox = ( StageCheckBox)_arrayList.get( i);
				if ( !_sourceList.equals( _targetList)
					&& ( stageCheckBox._stage._name.equals( Constant._initializeChartStageName) || stageCheckBox._stage._name.equals( Constant._updateChartStageName)))
					continue;

				targetDefaultComboBoxModel.insertElementAt( stageCheckBox, index++);
				list.add( stageCheckBox);
			}
		}

		if ( ( null != _targetList) && ( MOVE == action) && ( null != _indices)) {
			if ( _targetList.equals( _sourceList)) {
				DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)_sourceList.getModel();

				// If we are moving items around in the same list, we
				// need to adjust the indices accordingly since those
				// after the insertion point have moved.
				if ( 0 < addCount) {
					for ( int i = 0; i < _indices.length; ++i) {
						if ( _indices[ i] > addIndex) {
							_indices[ i] += addCount;
						}
					}
				}

				for ( int i = _indices.length - 1; i >= 0; --i) {
					if ( defaultComboBoxModel.getSize() > _indices[ i])
						defaultComboBoxModel.removeElementAt( _indices[ i]);
				}

				select( _targetList, list);
				//select( ( JList)component, transferable);
			} else {
				DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)_sourceList.getModel();

				for ( int i = 0; i < _indices.length; ++i) {
					StageCheckBox stageCheckBox = ( StageCheckBox)defaultComboBoxModel.getElementAt( _indices[ i]);
					if ( null == stageCheckBox._stage || stageCheckBox._stage._name.equals( Constant._initializeChartStageName)
						|| stageCheckBox._stage._name.equals( Constant._updateChartStageName)) {
						_indices = null;
						_targetList = null;
						return;
					}
				}

				for ( int i = _indices.length - 1; i >= 0; --i) {
					if ( defaultComboBoxModel.getSize() > _indices[ i])
						defaultComboBoxModel.removeElementAt( _indices[ i]);
				}

				select( _targetList, list);
				//select( ( JList)component, transferable);

				if ( 0 < defaultComboBoxModel.getSize())
					_sourceList.setSelectedIndex( ( defaultComboBoxModel.getSize() > _indices[ 0])
						? _indices[ 0] : defaultComboBoxModel.getSize() - 1);

				defaultComboBoxModel = ( DefaultComboBoxModel)_targetList.getModel();
				if ( 0 <= addIndex && defaultComboBoxModel.getSize() > addIndex) {
					Object object = defaultComboBoxModel.getElementAt( addIndex);
					_targetList.setSelectedValue( object, true);
				}
			}
		}

		_indices = null;
		_targetList = null;
		_arrayList = null;
	}

	/**
	 * @param targetList
	 * @param list
	 */
	private void select(JList targetList, List list) {
		if ( null != list && !list.isEmpty()) {
			int[] indices = new int[ list.size()];
			DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)targetList.getModel();
			for ( int i = 0; i < list.size(); ++i) {
				Object object = list.get( i);
				indices[ i] = defaultComboBoxModel.getIndexOf( object);
			}
			targetList.setSelectedIndices( indices);
		}
	}

//	/**
//	 * @param targetList
//	 * @param transferable
//	 */
//	private void select(JList targetList, Transferable transferable) {
//		ArrayList arrayList = null;
//		try {
//			if ( hasLocalArrayListFlavor( transferable.getTransferDataFlavors()))
//				arrayList = ( ArrayList)transferable.getTransferData( _localArrayListFlavor);
//			else if ( hasSerialArrayListFlavor( transferable.getTransferDataFlavors()))
//				arrayList = ( ArrayList)transferable.getTransferData( _serialArrayListFlavor);
//		} catch (UnsupportedFlavorException ufe) {
//			System.out.println( "select: unsupported data flavor");
//			return;
//		} catch (IOException ioe) {
//			System.out.println( "select: I/O exception");
//			return;
//		}
//
//		if ( null != arrayList) {
//			DefaultComboBoxModel defaultComboBoxModel = ( DefaultComboBoxModel)targetList.getModel();
//			for ( int i = 0; i < defaultComboBoxModel.getSize(); ++i) {
//				Object object = defaultComboBoxModel.getElementAt( i);
//				if ( arrayList.contains( object))
//					targetList.setSelectedValue( object, true);
//			}
//		}
//	}

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
		return MOVE;
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
