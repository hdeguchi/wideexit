/*
 * 2005/01/13
 */
package soars.plugin.modelbuilder.chart.chartmanager;

import java.util.ArrayList;
import java.util.List;

import soars.common.utility.swing.window.MDIChildFrame;
import soars.common.soars.tool.SoarsReflection;

import role.Role;
import util.AskCommand;
import env.EquippedObject;

/**
 * @author kurata
 */
public class ChartManager extends AskCommand<Role> {

	transient private Object _chart = null;

	/**
	 * 
	 */
	private String[] _class_names = {
		"soars.plugin.modelbuilder.chart.chart.main.ChartFrame",
		"soars.common.utility.swing.window.Frame",
		"soars.common.utility.tool.resource.Resource",
		"soars.common.utility.xml.sax.Writer",
		"ptolemy.plot.Plot"
	};

	/**
	 * @return
	 */
	private boolean check() {
		for ( int i = 0; i < _class_names.length; ++i) {
			if ( null == SoarsReflection.get_class( _class_names[ i]))
				return false;
		}
		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean create(Role role, String parameter) throws Exception {
		if ( !check())
			return false;

		if ( null != _chart)
			return true;

		String[] words = parameter.split( "=");
		if ( null == words || 2 > words.length)
			throw new RuntimeException( "Invalid parameter - create " + parameter);

		if ( words[ 0].equals( "Frame")) {
			Class cls = createInternalChartFrame();
			if ( null != cls) {
				if ( !create( words[ 1]))
					return false;

				List resultList = new ArrayList();
				if ( !SoarsReflection.execute_static_method( cls, "append", new Class[] { MDIChildFrame.class}, new Object[] { _chart}, resultList))
					return false;

				if ( resultList.isEmpty() || null == resultList.get( 0)
					|| !( resultList.get( 0) instanceof Boolean))
					return false;

				Boolean result = ( Boolean)resultList.get( 0);
				if ( !result.booleanValue())
					return false;

			} else {
				_chart = SoarsReflection.get_class_instance( "soars.plugin.modelbuilder.chart.chart.main.ChartFrame");
				if ( null == _chart)
					return false;

				if ( !create( words[ 1]))
					return false;
			}

		} else if ( words[ 0].equals( "Logger")) {
			_chart = SoarsReflection.get_class_instance( "soars.plugin.modelbuilder.chart.chart.main.ChartLogger");
			if ( null == _chart)
				return false;
		}

		return true;
	}

	/**
	 * @return
	 */
	private Class createInternalChartFrame() {
		Class cls = SoarsReflection.get_class( "soars.application.simulator.main.MainFrame");
		if ( null == cls)
			return null;

		_chart = SoarsReflection.get_class_instance( "soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame");
		if ( null == _chart)
			return null;

		return cls;
	}

	/**
	 * @param name
	 * @return
	 */
	private boolean create(String name) {
		List resultList = new ArrayList();
		if ( !SoarsReflection.execute_class_method(
			_chart, "create", new Class[] { String.class}, new Object[] { name}, resultList))
			return false;

		if ( resultList.isEmpty() || null == resultList.get( 0)
			|| !( resultList.get( 0) instanceof Boolean))
			return false;

		Boolean result = ( Boolean)resultList.get( 0);
		if ( !result.booleanValue())
			return false;

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean append(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		String[] words = parameter.split( "=");
		if ( null == words || ( 3 != words.length && 4 != words.length))
			throw new RuntimeException( "Invalid parameter - append " + parameter);


		if ( !words[ 0].matches( "[0-9]") && !words[ 0].matches( "^[1-9][0-9]+"))
			throw new RuntimeException( "Invalid parameter - append " + parameter);

		int dataset = Integer.parseInt( words[ 0]);

//		if ( 100 <= dataset)
//			return false;


		EquippedObject equippedObject = role.getSelf();
//		if ( null == equippedObject)
//			throw new Exception( getClass().getName() + " append(...)");

		Number number = ( Number)equippedObject.getEquip( words[ 1]);
//		if ( null == number)
//			throw new Exception( getClass().getName() + " append(...)");

		double x = number.doubleValue();


		equippedObject = role.getSelf();
//		if ( null == equippedObject)
//			throw new Exception( getClass().getName() + " append(...)");

		number = ( Number)equippedObject.getEquip( words[ 2]);
//		if ( null == number)
//			throw new Exception( getClass().getName() + " append(...)");

		double y = number.doubleValue();


		if ( 3 == words.length) {
			if ( !SoarsReflection.execute_class_method( _chart, "append",
				new Class[] { Integer.class, Double.class, Double.class},
				new Object[] { new Integer( dataset), new Double( x), new Double( y)}))
				throw new RuntimeException( "Invalid parameter - append " + parameter);
		} else {
			if ( !SoarsReflection.execute_class_method( _chart, "append",
				new Class[] { Integer.class, Double.class, Double.class, Boolean.class},
				new Object[] { new Integer( dataset), new Double( x), new Double( y),
					new Boolean( words[ 3].equals( "true") ? true : false)}))
				throw new RuntimeException( "Invalid parameter - append " + parameter);
		}

		return true;
	}

	/**
	 * @param dataset
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean append(int dataset, double x, double y) throws Exception {
		if ( null == _chart)
			return false;

		if ( !SoarsReflection.execute_class_method( _chart, "append",
			new Class[] { Integer.class, Double.class, Double.class},
			new Object[] { new Integer( dataset), new Double( x), new Double( y)}))
			throw new RuntimeException( "Invalid parameter - append ");

		return true;
	}

	/**
	 * @param dataset
	 * @param x
	 * @param y
	 * @param connected
	 * @return
	 */
	public boolean append(int dataset, double x, double y, boolean connected) throws Exception {
		if ( null == _chart)
			return false;

		if ( !SoarsReflection.execute_class_method( _chart, "append",
			new Class[] { Integer.class, Double.class, Double.class, Boolean.class},
			new Object[] { new Integer( dataset), new Double( x), new Double( y),
				new Boolean( connected)}))
			throw new RuntimeException( "Invalid parameter - append ");

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean addLegend(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		String[] words = parameter.split( "=");
		if ( null == words || 2 != words.length)
			throw new RuntimeException( "Invalid parameter - addLegend " + parameter);

		if ( !words[ 0].matches( "[0-9]") && !words[ 0].matches( "^[1-9][0-9]+"))
			throw new RuntimeException( "Invalid parameter - addLegend " + parameter);

		int dataset = Integer.parseInt( words[ 0]);

		if ( !SoarsReflection.execute_class_method( _chart, "addLegend",
			new Class[] { Integer.class, String.class},
			new Object[] { new Integer( dataset), words[ 1]}))
			throw new RuntimeException( "Invalid parameter - addLegend " + parameter);

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean setXLabel(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		if ( !SoarsReflection.execute_class_method( _chart, "setXLabel",
			new Class[] { String.class}, new Object[] { parameter}))
			throw new RuntimeException( "Invalid parameter - setXLabel " + parameter);

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean setYLabel(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		if ( !SoarsReflection.execute_class_method( _chart, "setYLabel",
			new Class[] { String.class}, new Object[] { parameter}))
			throw new RuntimeException( "Invalid parameter - setYLabel " + parameter);

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean setXRange(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		String[] words = parameter.split( "=");
		if ( null == words || 2 != words.length)
			throw new RuntimeException( "Invalid parameter - setXRange " + parameter);


		EquippedObject equippedObject = role.getSelf();
//		if ( null == equippedObject)
//			throw new Exception( getClass().getName() + " setXRange(...)");

		Number number = ( Number)equippedObject.getEquip( words[ 0]);
//		if ( null == number)
//			throw new Exception( getClass().getName() + " setXRange(...)");

		double min = number.doubleValue();


		equippedObject = role.getSelf();
//		if ( null == equippedObject)
//			throw new Exception( getClass().getName() + " setXRange(...)");

		number = ( Number)equippedObject.getEquip( words[ 1]);
//		if ( null == number)
//			throw new Exception( getClass().getName() + " setXRange(...)");

		double max = number.doubleValue();


		if ( !SoarsReflection.execute_class_method( _chart, "setXRange",
			new Class[] { Double.class, Double.class},
			new Object[] { new Double( min), new Double( max)}))
			throw new RuntimeException( "Invalid parameter - setXRange " + parameter);

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean setYRange(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		String[] words = parameter.split( "=");
		if ( null == words || 2 != words.length)
			throw new RuntimeException( "Invalid parameter - setYRange " + parameter);


		EquippedObject equippedObject = role.getSelf();
//		if ( null == equippedObject)
//			throw new Exception( getClass().getName() + " setYRange(...)");

		Number number = ( Number)equippedObject.getEquip( words[ 0]);
//		if ( null == number)
//			throw new Exception( getClass().getName() + " setYRange(...)");

		double min = number.doubleValue();


		equippedObject = role.getSelf();
//		if ( null == equippedObject)
//			throw new Exception( getClass().getName() + " setYRange(...)");

		number = ( Number)equippedObject.getEquip( words[ 1]);
//		if ( null == number)
//			throw new Exception( getClass().getName() + " setYRange(...)");

		double max = number.doubleValue();


		if ( !SoarsReflection.execute_class_method( _chart, "setYRange",
			new Class[] { Double.class, Double.class},
			new Object[] { new Double( min), new Double( max)}))
			throw new RuntimeException( "Invalid parameter - setYRange " + parameter);

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean write(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		List resultList = new ArrayList();
		if ( !SoarsReflection.execute_class_method(
			_chart, "write", new Class[] { String.class}, new Object[] { parameter}, resultList))
			return false;

		if ( resultList.isEmpty() || null == resultList.get( 0)
			|| !( resultList.get( 0) instanceof Boolean))
			return false;

		Boolean result = ( Boolean)resultList.get( 0);
		if ( !result.booleanValue())
			return false;

		return true;
	}

	/**
	 * @param role
	 * @param parameter
	 * @return
	 */
	public boolean setTitle(Role role, String parameter) throws Exception {
		if ( null == _chart)
			return false;

		if ( !SoarsReflection.execute_class_method( _chart, "setTitle",
			new Class[] { String.class}, new Object[] { parameter}))
			throw new RuntimeException( "Invalid parameter - setTitle " + parameter);

		return true;
	}
}
