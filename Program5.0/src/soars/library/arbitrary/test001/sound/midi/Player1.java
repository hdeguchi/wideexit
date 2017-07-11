/**
 * 
 */
package soars.library.arbitrary.test001.sound.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Receiver;
import javax.sound.midi.ShortMessage;

import soars.common.utility.swing.window.MDIChildFrame;
import soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame;
import soars.plugin.modelbuilder.chart.chart.main.ChartFrame;
import soars.plugin.modelbuilder.chart.chart.main.IChart;
import soars.common.soars.tool.SoarsReflection;

/**
 * @author kurata
 *
 */
public class Player1 {


	/**
	 * 
	 */
	private Receiver _receiver = null;


	/**
	 * 
	 */
	private boolean _uses_chart = false;

	/**
	 * 
	 */
	private IChart _chart = null;


	/**
	 * 
	 */
	private double _length = 0.0;


	/**
	 * 
	 */
	private final double _bar_length = 1920.0;

	/**
	 * 
	 */
	public Player1() {
		super();
	}

	/**
	 * @param uses_chart
	 */
	public Player1(boolean uses_chart) {
		super();
		_uses_chart = uses_chart;
	}

	/**
	 * @return
	 */
	public boolean open() {
		try {
			_receiver = MidiSystem.getReceiver();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return false;
		}
		if ( null == _receiver)
			return false;

		return setup_chart();
	}

	/**
	 * @return
	 */
	private boolean setup_chart() {
		if ( !_uses_chart)
			return true;

		Class cls = SoarsReflection.get_class( "soars.application.simulator.main.MainFrame");
		if ( null != cls) {
			_chart = new InternalChartFrame();
			if ( !_chart.create( ""))
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
			_chart = new ChartFrame();
			if ( !_chart.create( ""))
				return false;
		}

		_chart.setTitle( "MIDI");
		_chart.setXLabel( "bar");
		_chart.setYLabel( "tone number");

		return true;
	}

	/**
	 * 
	 */
	public void close() {
		if ( null != _receiver)
			_receiver.close();
	}

	/**
	 * @param noteNumber
	 * @param velocity
	 * @param length
	 */
	public void sound(int noteNumber, int velocity, int length) {
		if ( null == _receiver)
			return;

		if ( null != _chart)
			_chart.append( 0, _length / _bar_length, noteNumber);

		try {
			ShortMessage shortMessage = new ShortMessage();
			shortMessage.setMessage( ShortMessage.NOTE_ON, noteNumber, velocity);
			_receiver.send( shortMessage, 1000L * length);
		} catch (InvalidMidiDataException e1) {
			e1.printStackTrace();
		}

		try {
			Thread.sleep( length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if ( null != _chart) {
			_length += length;
			_chart.append( 0, _length / _bar_length, noteNumber);
		}
	}

	/**
	 * @param length
	 */
	public void rest(int length) {
		try {
			Thread.sleep( length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		if ( null != _chart) {
			//_chartFrame.append( 0, _length / _bar_length);
			_length += length;
			//_chartFrame.append( 0, _length / _bar_length);
		}
	}
}
