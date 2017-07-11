/**
 * 
 */
package soars.library.arbitrary.test001.sound.midi;

import java.util.ArrayList;
import java.util.List;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;

import soars.common.utility.swing.window.MDIChildFrame;
import soars.plugin.modelbuilder.chart.chart.main.InternalChartFrame;
import soars.plugin.modelbuilder.chart.chart.main.ChartFrame;
import soars.plugin.modelbuilder.chart.chart.main.IChart;
import soars.common.soars.tool.SoarsReflection;

/**
 * @author kurata
 *
 */
public class Player2 {


	/**
	 * 
	 */
	private Synthesizer _synthesizer = null;

	/**
	 * 
	 */
	private MidiChannel _midiChannel = null;


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
	public Player2() {
		super();
	}

	/**
	 * @param uses_chart
	 */
	public Player2(boolean uses_chart) {
		super();
		_uses_chart = uses_chart;
	}

	/**
	 * @param instrumentNo
	 * @param channelNo
	 * @return
	 */
	public boolean open(int instrumentNo, int channelNo) {
		try {
			_synthesizer = MidiSystem.getSynthesizer();
			_synthesizer.open();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return false;
		}

		if ( null == _synthesizer)
			return false;

		_synthesizer.loadInstrument( _synthesizer.getDefaultSoundbank().getInstruments()[ instrumentNo]);
		_midiChannel = _synthesizer.getChannels()[ channelNo];
		if ( null == _midiChannel)
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
		if ( null != _midiChannel)
			_midiChannel.allSoundOff();

		if ( null != _synthesizer)
			_synthesizer.close();
	}

	/**
	 * @param noteNumber
	 * @param velocity
	 * @param length
	 */
	public void sound(int noteNumber, int velocity, int length) {
		if ( null == _midiChannel)
			return;

		if ( null != _chart)
			_chart.append( 0, _length / _bar_length, noteNumber);

		_midiChannel.noteOn( noteNumber, velocity);

		try {
			Thread.sleep( length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		_midiChannel.noteOff( noteNumber);

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
