/*
 * Created on 2006/03/10
 */
package soars.application.visualshell.object.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.nio.IntBuffer;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.layer.Layer;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.chart.edit.EditChartObjectDlg;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.resource.Resource;
import soars.common.utility.xml.sax.Writer;

/**
 * The class for the chart object
 * @author kurata / SOARS project
 */
public class ChartObject extends DrawObject {

	/**
	 * Image object for this object.
	 */
	static public BufferedImage _bufferedImage
		= Resource.load_image_from_resource(
				Constant._resourceDirectory + "/image/icon/chart.png",
				ChartObject.class);

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
	 * If true, a line is drawn to connect to the previous point.
	 */
	public boolean _connect = true;

	/**
	 * If true, a line is drawn.
	 */
	public boolean _show = true;

	/**
	 * Array for the pairs of the NumberObjectData objects.
	 */
	public Vector<NumberObjectData[]> _numberObjectDataPairs = new Vector<NumberObjectData[]>();

	/**
	 * Creates this object with the specified data.
	 * @param id the specified id
	 * @param name the specified name
	 * @param position the specified position
	 * @param graphics2D the graphics object of JAVA
	 */
	public ChartObject(int id, String name, Point position, Graphics2D graphics2D) {
		super(id, name, position, graphics2D);
		setup_graphics();
	}

	/**
	 * Creates this object with the specified data.
	 * @param chartObject the specified object
	 */
	public ChartObject(ChartObject chartObject) {
		super(chartObject);
		_title = chartObject._title;
		_horizontalAxis = chartObject._horizontalAxis;
		_verticalAxis = chartObject._verticalAxis;
		_connect = chartObject._connect;

		_numberObjectDataPairs = new Vector<NumberObjectData[]>();
		for ( NumberObjectData[] numberObjectDataPair:chartObject._numberObjectDataPairs)
			_numberObjectDataPairs.add( new NumberObjectData[] { numberObjectDataPair[ 0], numberObjectDataPair[ 1]});
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_dimension()
	 */
	protected Dimension get_default_dimension() {
		return new Dimension( _bufferedImage.getWidth(), _bufferedImage.getHeight());
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_image_color()
	 */
	protected Color get_default_image_color() {
		return null;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#get_default_text_color()
	 */
	protected Color get_default_text_color() {
		return new Color( 0, 0, 0);
	}

	/**
	 * Returns whether or not this chart object is available.
	 * @return whether or not this chart object is available
	 */
	public boolean is_available() {
		return !_numberObjectDataPairs.isEmpty();
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#draw(java.awt.Graphics2D, java.awt.image.ImageObserver)
	 */
	public void draw(Graphics2D graphics2D, ImageObserver imageObserver) {
		super.draw(graphics2D, imageObserver);
		graphics2D.drawImage( _bufferedImage, _position.x, _position.y, imageObserver);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges) {
		return can_adjust_name( "agent", headName, ranges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges) {
		return can_adjust_name( "spot", headName, ranges);
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @return
	 */
	private boolean can_adjust_name(String type, String headName, Vector<String[]> ranges) {
		boolean result = true;
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			for ( NumberObjectData numberObjectData:numberObjectDataPair) {
				if ( !numberObjectData.can_adjust_name( type, headName, ranges, this))
					result = false;
			}
		}
		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_agent_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return can_adjust_name( "agent", headName, ranges, newHeadName, newRanges);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_adjust_spot_name(java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return can_adjust_name( "spot", headName, ranges, newHeadName, newRanges);
	}

	/**
	 * @param type
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean can_adjust_name(String type, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = true;
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			for ( NumberObjectData numberObjectData:numberObjectDataPair) {
				if ( !numberObjectData.can_adjust_name( type, headName, ranges, newHeadName, newRanges, this))
					result = false;
			}
		}
		return result;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_agent_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_name_and_number( "agent", newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#update_spot_name_and_number(java.lang.String, java.lang.String, java.lang.String, java.util.Vector, java.lang.String, java.util.Vector)
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		return update_name_and_number( "spot", newName, originalName, headName, ranges, newHeadName, newRanges);
	}

	/**
	 * @param type
	 * @param newName
	 * @param originalName
	 * @param headName
	 * @param ranges
	 * @param newHeadName
	 * @param newRanges
	 * @return
	 */
	private boolean update_name_and_number(String type, String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			for ( NumberObjectData numberObjectData:numberObjectDataPair)
				if ( numberObjectData.update_name_and_number( type, newName, originalName, headName, ranges, newHeadName, newRanges))
					result = true;
		}
		return result;
	}

	/**
	 * @param kind
	 * @param type
	 * @param name
	 * @param headName
	 * @param ranges
	 * @return
	 */
	public boolean can_remove(String kind, String type, String name, String headName, Vector<String[]> ranges) {
		if ( !kind.equals( "number object"))
			return true;

		boolean result = true;
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			for ( NumberObjectData numberObjectData:numberObjectDataPair) {
				if ( !numberObjectData.can_remove( type, name, headName, ranges, this)) {
					result = false;
				}
			}
		}
		return result;
	}

	/**
	 * Returns true for updating the specified number variable name with the new one successfully.
	 * @param kind the kind of the object
	 * @param entity "agent" or "spot"
	 * @param name the name of the specified number variable
	 * @param newName the name of the new number variable
	 * @return true for updating the specified number variable name with the new one successfully
	 */
	public boolean update_object_name(String kind, String entity, String name, String newName) {
		if ( !kind.equals( "number object"))
			return false;

		boolean result = false;
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			for ( NumberObjectData numberObjectData:numberObjectDataPair) {
				if ( numberObjectData.update_object_name( entity, name, newName))
					result = true;
			}
		}
		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#can_paste(soars.application.visualshell.layer.Layer)
	 */
	public boolean can_paste(Layer drawObjects) {
		if ( drawObjects.contains( this, _name)) {
			String[] message = new String[] {
				"Chart",
				"name = " + _name
			};

			WarningManager.get_instance().add( message);
			return false;
		}

		for ( int i = 0; i < Constant._kinds.length; ++i) {
			if ( drawObjects.is_object_name( Constant._kinds[ i], _name)) {
				String[] message = new String[] {
					"Chart",
					"name = " + _name
				};

				WarningManager.get_instance().add( message);
				return false;
			}
		}

		boolean result = true;
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			for ( NumberObjectData numberObjectData:numberObjectDataPair) {
				if ( !numberObjectData.can_paste( drawObjects, this))
					result = false;
			}
		}
		return result;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#edit(javax.swing.JComponent, java.awt.Frame)
	 */
	public void edit(JComponent component, Frame frame) {
		EditChartObjectDlg editChartObjectDlg = new EditChartObjectDlg(
			frame,
			ResourceManager.get_instance().get( "edit.chart.dialog.title") + " - " + _name,
			true,
			this);
		editChartObjectDlg.do_modal();
	}

	/**
	 * Returns the script of this object for ModelBuilder.
	 * @param command the number of initial commands
	 * @return the script of this object for ModelBuilder
	 */
	public String get_chart_spot_script(IntBuffer command) {
		String script = ( "\t" + _name
			+ "\t<>startRule " + _name
			+ "\t<>setEquip __t=util.IntValue ; <>logEquip __t ; <>askEquip __t=0");

		IntBuffer counter = IntBuffer.allocate( 1);
		counter.put( 0, 2);

		for ( int i = 0; i < _numberObjectDataPairs.size(); ++i) {
			NumberObjectData[] numberObjectDataPair = ( NumberObjectData[])_numberObjectDataPairs.get( i);
			for ( int j = 0; j < numberObjectDataPair.length; ++j) {
				if ( !numberObjectDataPair[ j]._type.equals( "step")) {
					script += ( "\t<>keyword __object_" + String.valueOf( i + 1) + String.valueOf( j) + "=" + numberObjectDataPair[ j]._objectName);
					script += ( "\t<>keyword __variable_" + String.valueOf( i + 1) + String.valueOf( j) + "=" + numberObjectDataPair[ j]._numberVariable);
					counter.put( 0, counter.get( 0) + 2);
				}

				script += ( "\t<>setEquip __value_" + String.valueOf( i + 1) + String.valueOf( j) + "=util.DoubleValue ; <>logEquip __value_" + String.valueOf( i + 1) + String.valueOf( j));
				counter.put( 0, counter.get( 0) + 1);
			}
		}

		script += ( "\t<>setClass " + Constant._chartGetValueClassVariableName + "=" + Constant._chartGetValueMainClassname + " ; <>logEquip " + Constant._chartGetValueClassVariableName);
		counter.put( 0, counter.get( 0) + 1);

		if ( command.get( 0) < counter.get( 0))
			command.put( 0, counter.get( 0));

		return ( script + Constant._lineSeparator);
	}

	/**
	 * Returns the script of this object for ModelBuilder.
	 * @param ruleCondition the number of the rule conditions
	 * @param experimentName the name of the experiment
	 * @return the script of this object for ModelBuilder
	 */
	public String get_role_initialize_command(int ruleCondition, String experimentName) {
		String script = get_header( Constant._initializeChartStageName, ruleCondition);
		String title = ( _title.equals( "") ? "Untitled" : _title);

//			String sample1 = "<>setEquip chart1=soars.plugin.modelbuilder.chart.chartmanager.ChartManager ; "
//				+ "<>askEquip chart1=create Frame"
//				+ " ; <>askEquip chart1=setTitle Chart1"
//				+ " ; <>askEquip chart1=setXLabel x"
//				+ " ; <>askEquip chart1=setYLabel y";

		title += ( experimentName.equals( "") ? "" : ( " - " + experimentName)); 
		script += ( "<>setEquip " + _name + "=" + Constant._chartManagerMainClassname + " ; "
			+ "<>askEquip " + _name + "=create Frame" + "=" + _name
			+ ( title.equals( "") ? "" : ( " ; <>askEquip " + _name + "=setTitle " + title))
			+ ( _horizontalAxis.equals( "") ? "" : ( " ; <>askEquip " + _name + "=setXLabel " + _horizontalAxis))
			+ ( _verticalAxis.equals( "") ? "" : ( " ; <>askEquip " + _name + "=setYLabel " + _verticalAxis)));

//			String sample2 = " ; <>askEquip chart1=addLegend 0="
//				+ "step"
//				+ " - "
//				+ "y";

		for ( int i = 0; i < _numberObjectDataPairs.size(); ++i) {
			NumberObjectData[] numberObjectDataPair = ( NumberObjectData[])_numberObjectDataPairs.get( i);
			if ( 2 != numberObjectDataPair.length)
				continue;

			script += ( " ; <>askEquip " + _name + "=addLegend " + String.valueOf( i + 1) + "="
				+ ( numberObjectDataPair[ 0]._type.equals( "step") ? "step" : numberObjectDataPair[ 0]._numberVariable)
				+ " - "
				+ ( numberObjectDataPair[ 1]._type.equals( "step") ? "step" : numberObjectDataPair[ 1]._numberVariable));
		}

		script += " ; TRUE\tLine=1";

		return ( script + Constant._lineSeparator);
	}

	/**
	 * Returns the script of this object for ModelBuilder.
	 * @param ruleCondition the number of the rule conditions
	 * @return the script of this object for ModelBuilder
	 */
	public String get_role_update_command(int ruleCondition) {
		String script = get_header( Constant._updateChartStageName, ruleCondition);
		script += ( "<>askEquip __t=__t+1");

//			String sample1 = " ; <>askEquip __value_00=__t";
//			String sample2 = " ; <>equip __object_01"
//				+ " ; <>addParam __get_value_class=__object_01=java.lang.String"
//				+ " ; <>equip __variable_01"
//				+ " ; <>addParam __get_value_class=__variable_01=java.lang.String"
//				+ " ; <>invokeClass __value_01=__get_value_class="
//				+ "get_agent_value";

		for ( int i = 0; i < _numberObjectDataPairs.size(); ++i) {
			NumberObjectData[] numberObjectDataPair = ( NumberObjectData[])_numberObjectDataPairs.get( i);
			for ( int j = 0; j < numberObjectDataPair.length; ++j) {
				if ( numberObjectDataPair[ j]._type.equals( "step"))
					script += ( " ; <>askEquip __value_" + String.valueOf( i + 1) + String.valueOf( j) + "=__t");
				else {
					script += ( " ; <>equip __object_" + String.valueOf( i + 1) + String.valueOf( j)
						+ " ; <>addParam " + Constant._chartGetValueClassVariableName + "=__object_" + String.valueOf( i + 1) + String.valueOf( j) + "=java.lang.String"
						+ " ; <>equip __variable_" + String.valueOf( i + 1) + String.valueOf( j)
						+ " ; <>addParam " + Constant._chartGetValueClassVariableName + "=__variable_" + String.valueOf( i + 1) + String.valueOf( j) + "=java.lang.String"
						+ " ; <>invokeClass __value_" + String.valueOf( i + 1) + String.valueOf( j) + "=" + Constant._chartGetValueClassVariableName + "=");
					script += ( numberObjectDataPair[ j]._type.equals( "agent") ? Constant._chartGetAgentValueMethodname : Constant._chartGetSpotValueMethodname);
				}
			}
		}

//			String sample3 = " ; <>askEquip chart1=append 0"
//				+ "=__value_00"
//				+ "=__value_01"
//				+ "=true";

		for ( int i = 0; i < _numberObjectDataPairs.size(); ++i) {
			NumberObjectData[] numberObjectDataPair = ( NumberObjectData[])_numberObjectDataPairs.get( i);
			if ( 2 != numberObjectDataPair.length)
				continue;

			script += ( " ; <>askEquip " + _name + "=append " + String.valueOf( i + 1)
				+ "=__value_" + String.valueOf( i + 1) + String.valueOf( 0)
				+ "=__value_" + String.valueOf( i + 1) + String.valueOf( 1)
				+ "=" + ( _connect ? "true" : "false"));
		}
		
		script += " ; TRUE\tLine=1";

		return ( script + Constant._lineSeparator);
	}
	
	/**
	 * @param stageName
	 * @param ruleCondition
	 * @return
	 */
	private String get_header(String stageName, int ruleCondition) {
		String script = ( _name + "\t" + stageName);

		for ( int i = 0; i < ruleCondition - 1; ++i)
			script += "\t";

		return script;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.object.base.DrawObject#write(soars.common.utility.xml.sax.Writer)
	 */
	public boolean write(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		write( _name, attributesImpl);

		attributesImpl.addAttribute( null, null, "title", "", Writer.escapeAttributeCharData( _title));
		attributesImpl.addAttribute( null, null, "horizontal_axis", "", Writer.escapeAttributeCharData( _horizontalAxis));
		attributesImpl.addAttribute( null, null, "vertical_axis", "", Writer.escapeAttributeCharData( _verticalAxis));
		attributesImpl.addAttribute( null, null, "connect", "", ( _connect ? "true" : "false"));

		writer.startElement( null, null, "chart", attributesImpl);

		if ( !_numberObjectDataPairs.isEmpty()) {
			writer.startElement( null, null, "number_object_pair", new AttributesImpl());

			write_number_object_pairs( writer);

			writer.endElement( null, null, "number_object_pair");
		}

		write_comment( writer);

		writer.endElement( null, null, "chart");

		return true;
	}

	/**
	 * @param writer
	 * @throws SAXException
	 */
	private void write_number_object_pairs(Writer writer) throws SAXException {
		String[] prifix = new String[] { "horizontal_", "vertical_"};
		for ( NumberObjectData[] numberObjectDataPair:_numberObjectDataPairs) {
			AttributesImpl attributesImpl = new AttributesImpl();

			for ( int i = 0; i < numberObjectDataPair.length; ++i)
				numberObjectDataPair[ i].write( prifix[ i], attributesImpl, writer);

			writer.writeElement( null, null, "pair", attributesImpl);
		}
	}

	/**
	 * @return
	 */
	public String get_properties() {
		String text = "";
		for ( int i = 0; i < _numberObjectDataPairs.size(); ++i) {
			NumberObjectData[] numberObjectDataPair = ( NumberObjectData[])_numberObjectDataPairs.get( i);
			text += ( _name + "\t" + String.valueOf( i + 1) + "\t"
				+ numberObjectDataPair[ 0]._type + "\t" + numberObjectDataPair[ 0]._objectName + "\t" + numberObjectDataPair[ 0]._numberVariable + "\t"
				+ numberObjectDataPair[ 1]._type + "\t" + numberObjectDataPair[ 1]._objectName + "\t" + numberObjectDataPair[ 1]._numberVariable + "\t"
				+ _title + "\t" + _horizontalAxis + "\t" + _verticalAxis + "\t" + ( _connect ? "true" : "false") + "\t" + ( _show ? "true" : "false") + "\n");
		}
		return text;
	}
}
