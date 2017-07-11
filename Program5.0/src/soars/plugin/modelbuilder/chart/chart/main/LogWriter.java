/*
 * 2004/12/17
 */
package soars.plugin.modelbuilder.chart.chart.main;

import java.awt.Rectangle;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import ptolemy.plot.Plot;
import ptolemy.plot.PlotPoint;
import soars.common.utility.xml.sax.Writer;

/**
 * @author kurata
 */
public class LogWriter {

	/**
	 * 
	 */
	public LogWriter() {
		super();
	}

	/**
	 * @param plot
	 * @param XRange
	 * @param YRange
	 * @param points
	 * @param filename
	 * @return
	 */
	public boolean write_data(Plot plot, boolean XRange, boolean YRange, Vector points, String filename) throws IOException {
		try {
			OutputStreamWriter outputStreamWriter
				= new OutputStreamWriter( new FileOutputStream( new File( filename)), "UTF-8");
			Writer writer = new Writer( outputStreamWriter);

			outputStreamWriter.write( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( plot.getTitle()));
			attributesImpl.addAttribute( null, null, "xLabel", "", Writer.escapeAttributeCharData( plot.getXLabel()));
			attributesImpl.addAttribute( null, null, "yLabel", "", Writer.escapeAttributeCharData( plot.getYLabel()));

			writer.startElement( null, null, "chart_data", attributesImpl);

			if ( !write( writer, plot, XRange, YRange, points))
				return false;

			writer.endElement( null, null, "chart_data");

			outputStreamWriter.flush();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (SAXException e1) {
			e1.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param writer
	 * @param plot
	 * @param XRange
	 * @param YRange
	 * @param name 
	 * @param points
	 * @param rectangle
	 * @return
	 * @throws SAXException
	 */
	public boolean write(Writer writer, Plot plot, boolean XRange, boolean YRange, String name, Vector points, Rectangle rectangle) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( plot.getTitle()));
		attributesImpl.addAttribute( null, null, "xLabel", "", Writer.escapeAttributeCharData( plot.getXLabel()));
		attributesImpl.addAttribute( null, null, "yLabel", "", Writer.escapeAttributeCharData( plot.getYLabel()));
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( rectangle.height));

		writer.startElement( null, null, "chart_data", attributesImpl);

		if ( !write( writer, plot, XRange, YRange, points))
			return false;

		writer.endElement( null, null, "chart_data");

		return true;
	}

	/**
	 * @param writer
	 * @param plot
	 * @param XRange
	 * @param XRange
	 * @param points
	 * @return
	 * @throws SAXException
	 */
	private boolean write(Writer writer, Plot plot, boolean XRange, boolean YRange, Vector points) throws SAXException {
		if ( XRange) {
			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "min", "", String.valueOf( plot.getXRange()[ 0]));
			attributesImpl.addAttribute( null, null, "max", "", String.valueOf( plot.getXRange()[ 1]));
			writer.writeElement( null, null, "xRange", attributesImpl);
		}

		if ( YRange) {
			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "min", "", String.valueOf( plot.getYRange()[ 0]));
			attributesImpl.addAttribute( null, null, "max", "", String.valueOf( plot.getYRange()[ 1]));
			writer.writeElement( null, null, "yRange", attributesImpl);
		}

		return set_data( writer, plot, points);
	}

	/**
	 * @param writer
	 * @param plot
	 * @param points
	 * @return
	 * @throws SAXException
	 */
	private boolean set_data(Writer writer, Plot plot, Vector points) throws SAXException {
		for ( int i = 0; i < plot.getNumDataSets(); ++i) {
			Vector plotPoints = ( Vector)points.get( i);

			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "id", "", String.valueOf( i));

			String legend = plot.getLegend( i);
			if ( null == legend) {
				if ( null == plotPoints || plotPoints.isEmpty())
					continue;
				else
					legend = "";
			} else {
				if ( null == plotPoints || plotPoints.isEmpty()) {
					attributesImpl.addAttribute( null, null, "legend", "", Writer.escapeAttributeCharData( legend));
					writer.writeElement( null, null, "dataset", attributesImpl);
					continue;
				}
			}

			attributesImpl.addAttribute( null, null, "legend", "", Writer.escapeAttributeCharData( legend));

//			attributesImpl.addAttribute( null, null, "legend", "",
//				( ( null == legend) ? "" : Writer.escapeAttributeCharData( legend)));
//
//			if ( plotPoints.isEmpty()) {
//				writer.writeElement( null, null, "dataset", attributesImpl);
//				continue;
//			}

			writer.startElement( null, null, "dataset", attributesImpl);

			String data = "";
			for ( int j = 0; j < plotPoints.size(); ++j) {
				PlotPoint plotPoint = ( PlotPoint)plotPoints.get( j);
				data += ( ( 0 == j) ? "" : " ");
				data += ( !plotPoint.connected ? "m" : "p");
				data += " ";
				data += String.valueOf( plotPoint.x);
				data += " ";
				data += String.valueOf( plotPoint.y);
			}

			writer.characters( data.toCharArray(), 0, data.length());

			writer.endElement( null, null, "dataset");
		}
		return true;
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @param plot
	 * @param XRange
	 * @param YRange
	 * @param name
	 * @param points
	 * @param rectangle
	 * @return
	 * @throws SAXException
	 */
	public boolean write(int dataset, File file, boolean connect, Writer writer, Plot plot, boolean XRange, boolean YRange, String name, Vector points, Rectangle rectangle) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( name));
		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( plot.getTitle()));
		attributesImpl.addAttribute( null, null, "xLabel", "", Writer.escapeAttributeCharData( plot.getXLabel()));
		attributesImpl.addAttribute( null, null, "yLabel", "", Writer.escapeAttributeCharData( plot.getYLabel()));
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( rectangle.height));

		writer.startElement( null, null, "chart_data", attributesImpl);

		if ( !write( dataset, file, connect, writer, plot, XRange, YRange, points))
			return false;

		writer.endElement( null, null, "chart_data");

		return true;
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @param plot
	 * @param XRange
	 * @param XRange
	 * @param points
	 * @return
	 * @throws SAXException
	 */
	private boolean write(int dataset, File file, boolean connect, Writer writer, Plot plot, boolean XRange, boolean YRange, Vector points) throws SAXException {
		if ( XRange) {
			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "min", "", String.valueOf( plot.getXRange()[ 0]));
			attributesImpl.addAttribute( null, null, "max", "", String.valueOf( plot.getXRange()[ 1]));
			writer.writeElement( null, null, "xRange", attributesImpl);
		}

		if ( YRange) {
			AttributesImpl attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "min", "", String.valueOf( plot.getYRange()[ 0]));
			attributesImpl.addAttribute( null, null, "max", "", String.valueOf( plot.getYRange()[ 1]));
			writer.writeElement( null, null, "yRange", attributesImpl);
		}

		return set_data( dataset, file, connect, writer, plot, points);
	}

	/**
	 * @param dataset
	 * @param file
	 * @param connect
	 * @param writer
	 * @param plot
	 * @param points
	 * @return
	 * @throws SAXException
	 */
	private boolean set_data(int dataset, File file, boolean connect, Writer writer, Plot plot, Vector points) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "id", "", String.valueOf( dataset));

		String legend = plot.getLegend( dataset);
		if ( null == legend)
				legend = "";

		attributesImpl.addAttribute( null, null, "legend", "", Writer.escapeAttributeCharData( legend));

		attributesImpl.addAttribute( null, null, "connect", "", String.valueOf( connect));

		writer.writeElement( null, null, "dataset", attributesImpl);

		return write_data( plot, points, dataset, file);
	}

	/**
	 * @param plot
	 * @param points
	 * @param dataset
	 * @param file
	 * @return
	 */
	public boolean write_data(Plot plot, Vector points, int dataset, File file) {
		try {
			Vector plotPoints = ( Vector)points.get( dataset);

			DataOutputStream dataOutputStream = new DataOutputStream( new FileOutputStream( file));

			for ( int i = 0; i < plotPoints.size(); ++i) {
				PlotPoint plotPoint = ( PlotPoint)plotPoints.get( i);
				dataOutputStream.writeDouble( plotPoint.x);
				dataOutputStream.writeDouble( plotPoint.y);
			}

			dataOutputStream.flush();
			dataOutputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
