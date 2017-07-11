/**
 * 
 */
package soars.application.animator.object.chart;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.beans.PropertyVetoException;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.main.MainFrame;
import soars.application.animator.main.internal.InternalFrameRectangleMap;
import soars.application.animator.main.internal.WindowProperty;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 *
 */
public class ChartObject {

	/**
	 * 
	 */
	public String _name = "";

	/**
	 * Title of this chart.
	 */
	public String _title = "";

	/**
	 * Name for the horizontal axis.
	 */
	public String _horizontalAxis = "";

	/**
	 * Name for the vertical axis.
	 */
	public String _verticalAxis = "";

	/**
	 * Array for the pairs of the NumberObjectData objects.
	 */
	public List<ChartDataPair> _chartDataPairs = new ArrayList<ChartDataPair>();

	/**
	 * 
	 */
	public AnimatorInternalChartFrame _animatorInternalChartFrame = null;

	/**
	 * 
	 */
	public WindowProperty _windowProperty = null;

	/**
	 * 
	 */
	static private final int _indicationDataset = 0;

	/**
	 * @param words 
	 */
	public ChartObject(String[] words) {
		super();
		_name = words[ 0];
		_title = words[ 8];
		_horizontalAxis = words[ 9];
		_verticalAxis = words[ 10];
		_windowProperty = new WindowProperty( AnimatorInternalChartFrame._minimumWidth, AnimatorInternalChartFrame._minimumHeight);
	}

	/**
	 * @param name
	 * @param title
	 * @param horizontalAxis
	 * @param verticalAxis
	 * @param windowProperty
	 */
	public ChartObject(String name, String title, String horizontalAxis, String verticalAxis, WindowProperty windowProperty) {
		super();
		_name = name;
		_title = title;
		_horizontalAxis = horizontalAxis;
		_verticalAxis = verticalAxis;
		_windowProperty = windowProperty;
	}

	/**
	 * _chartFrame and _rectangle are not copied!
	 * @param chartObject
	 */
	public ChartObject(ChartObject chartObject) {
		super();
		_name = chartObject._name;
		_title = chartObject._title;
		_horizontalAxis = chartObject._horizontalAxis;
		_verticalAxis = chartObject._verticalAxis;
		_windowProperty = new WindowProperty( chartObject._windowProperty);
		for ( ChartDataPair chartDataPair:chartObject._chartDataPairs)
			_chartDataPairs.add( new ChartDataPair( chartDataPair, this));
	}

	/**
	 * Invoked from ObjectManager's import_data( ... ) method.
	 * @param words
	 * @param chartLogDirectory
	 * @return
	 */
	public boolean setup(String[] words, File chartLogDirectory) {
		if ( null != _animatorInternalChartFrame)
			return false;

		_animatorInternalChartFrame = new AnimatorInternalChartFrame( _windowProperty._order);
		if ( !_animatorInternalChartFrame.create( words[ 0]))
			return false;

		_animatorInternalChartFrame.setTitle( _title);
		_animatorInternalChartFrame.setXLabel( _horizontalAxis);
		_animatorInternalChartFrame.setYLabel( _verticalAxis);

		MainFrame.get_instance().append_internalFrame(_animatorInternalChartFrame);

		return append( words, chartLogDirectory);
	}

	/**
	 * Invoked from ObjectManager's import_data( ... ) method.
	 * @param words
	 * @param chartLogDirectory
	 * @return
	 */
	public boolean append(String[] words, File chartLogDirectory) {
		if ( null == _animatorInternalChartFrame)
			return false;

		int index = Integer.parseInt( words[ 1]);
		ChartDataPair chartDataPair = new ChartDataPair(
			index,
			new ChartData[] { new ChartData( words[ 2], words[ 3], words[ 4]),
				new ChartData( words[ 5], words[ 6], words[ 7])},
			words[ 11].equals( "true"), words[ 12].equals( "true"), this);

		if ( !chartDataPair.load( _animatorInternalChartFrame, chartLogDirectory))
			return false;

		_chartDataPairs.add( chartDataPair);

		return true;
	}

	/**
	 * Invoked from SaxLoader's at_end_of_load() method.
	 * @param name
	 * @param chartLogDirectory
	 * @return
	 */
	public boolean setup(String name, File chartLogDirectory) {
		if ( null != _animatorInternalChartFrame)
			return false;

		_animatorInternalChartFrame = new AnimatorInternalChartFrame( _windowProperty._order);
		if ( !_animatorInternalChartFrame.create( name))
			return false;

		_animatorInternalChartFrame.setTitle( _title);
		_animatorInternalChartFrame.setXLabel( _horizontalAxis);
		_animatorInternalChartFrame.setYLabel( _verticalAxis);

		for ( ChartDataPair chartDataPair:_chartDataPairs) {
			if ( !chartDataPair.load( _animatorInternalChartFrame, chartLogDirectory))
				return false;
		}

		MainFrame.get_instance().append_internalFrame(_animatorInternalChartFrame);

		if ( !GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersects( _windowProperty._rectangle)
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowProperty._rectangle).width <= 10
			|| _windowProperty._rectangle.y <= -_animatorInternalChartFrame.getInsets().top
			|| GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().intersection( _windowProperty._rectangle).height <= _animatorInternalChartFrame.getInsets().top) {
			_windowProperty._rectangle.x = 0;
		}

		_animatorInternalChartFrame.setBounds( _windowProperty._rectangle);

		if ( _windowProperty._maximum) {
			try {
				_animatorInternalChartFrame.setMaximum( true);
			} catch (PropertyVetoException e) {
				e.printStackTrace();
			}
		} else {
			if ( _windowProperty._icon) {
				try {
					_animatorInternalChartFrame.setIcon( true);
				} catch (PropertyVetoException e) {
					e.printStackTrace();
				}
			}
		}

		_animatorInternalChartFrame.setVisible( visible());

		return true;
	}

	/**
	 * 
	 */
	public void cleanup() {
		if ( null != _animatorInternalChartFrame) {
			_animatorInternalChartFrame.dispose();
			_animatorInternalChartFrame = null;
		}
		_chartDataPairs.clear();
	}

	/**
	 * @param internalFrameRectangleMap
	 * @param desktopPane
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write(InternalFrameRectangleMap internalFrameRectangleMap, JDesktopPane desktopPane, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _name));
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));
		attributesImpl.addAttribute( null, null, "horizontal_axis", "", Writer.escapeAttributeCharData( _horizontalAxis));
		attributesImpl.addAttribute( null, null, "vertical_axis", "", Writer.escapeAttributeCharData( _verticalAxis));

		Rectangle rectangle = internalFrameRectangleMap.get( _animatorInternalChartFrame);
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( ( null == rectangle) ? 0 : rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( ( null == rectangle) ? 0 : rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( ( null == rectangle) ? AnimatorInternalChartFrame._minimumWidth : rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( ( null == rectangle) ? AnimatorInternalChartFrame._minimumHeight : rectangle.height));
		attributesImpl.addAttribute( null, null, "maximum", "", _animatorInternalChartFrame.isMaximum() ? "true" : "false");
		attributesImpl.addAttribute( null, null, "icon", "", _animatorInternalChartFrame.isIcon() ? "true" : "false");
		attributesImpl.addAttribute( null, null, "order", "", String.valueOf( desktopPane.getComponentZOrder( _animatorInternalChartFrame)));

		writer.startElement( null, null, "chart_frame", attributesImpl);

		for ( ChartDataPair chartDataPair:_chartDataPairs) {
			if ( !chartDataPair.write( writer))
				return false;
		}

		writer.endElement( null, null, "chart_frame");

		return true;
	}

	/**
	 * @param ratio
	 */
	public void indicate(double ratio) {
		_animatorInternalChartFrame.clear( _indicationDataset);
		for ( ChartDataPair chartDataPair:_chartDataPairs)
			chartDataPair.indicate( _animatorInternalChartFrame, _indicationDataset, ratio);
	}

	/**
	 * 
	 */
	public void clear_indication() {
		_animatorInternalChartFrame.clear( _indicationDataset);
	}

	/**
	 * @return
	 */
	public boolean visible() {
		for ( ChartDataPair chartDataPair:_chartDataPairs) {
			if ( chartDataPair._visible)
				return true;
		}
		return false;
	}

	/**
	 * @param visible
	 */
	public void visible(boolean visible) {
		for ( ChartDataPair chartDataPair:_chartDataPairs)
			chartDataPair._visible = visible;
	}

	/**
	 * @param chartObject
	 * @return
	 */
	public boolean update(ChartObject chartObject) {
		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( !chartDataPair.update( _animatorInternalChartFrame, ( ChartDataPair)chartObject._chartDataPairs.get( i)))
				return false;
		}

		_animatorInternalChartFrame.clearLegends();

		for ( int i = 0; i < _chartDataPairs.size(); ++i) {
			ChartDataPair chartDataPair = ( ChartDataPair)_chartDataPairs.get( i);
			if ( !chartDataPair._visible)
				continue;

			_animatorInternalChartFrame.addLegend( chartDataPair._dataset, chartDataPair.getLegend());
		}

		_animatorInternalChartFrame.repaint();

		_animatorInternalChartFrame.setVisible( visible());

		return true;
	}

	/**
	 * 
	 */
	public void debug() {
		for ( ChartDataPair chartDataPair:_chartDataPairs)
			chartDataPair.debug( _name, _animatorInternalChartFrame.get_points());
	}
}
