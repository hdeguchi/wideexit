/**
 * 
 */
package soars.library.arbitrary.test001.sound.midi;

import javax.sound.midi.MidiChannel;
import javax.sound.midi.Synthesizer;

/**
 * @author kurata
 *
 */
public class Player3 {

	/**
	 * @param synthesizer
	 * @param instrumentNo
	 * @param channelNo
	 * @return
	 */
	public static MidiChannel getMidiChannel(Synthesizer synthesizer, int instrumentNo, int channelNo) {
		synthesizer.loadInstrument( synthesizer.getDefaultSoundbank().getInstruments()[ instrumentNo]);
		return synthesizer.getChannels()[ channelNo];
	}

	/**
	 * @param midiChannel
	 * @param noteNumber
	 * @param velocity
	 * @param length
	 */
	public static void sound(MidiChannel midiChannel, int noteNumber, int velocity, int length) {
		midiChannel.noteOn( noteNumber, velocity);

		try {
			Thread.sleep( length);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		midiChannel.noteOff( noteNumber);
	}
}
