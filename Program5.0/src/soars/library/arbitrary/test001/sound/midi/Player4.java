/**
 * 
 */
package soars.library.arbitrary.test001.sound.midi;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MetaEventListener;
import javax.sound.midi.MetaMessage;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 *
 */
public class Player4 extends Dialog implements MetaEventListener, ControllerEventListener {

//	/**
//	 * 
//	 */
//	private MidiDevice _midiDevice = null;

	/**
	 * 
	 */
	private Sequencer _sequencer = null;

	/**
	 * @param arg0
	 */
	public Player4(Frame arg0) {
		super(arg0, "", true);
	}

	/**
	 * @param filename
	 */
	public boolean play(String filename) {
		File file = new File( filename.replaceAll( "\\\\", "/"));
		if ( !file.exists() || !file.isFile() || !file.canRead())
			return false;

//		MidiDevice.Info[] info = MidiSystem.getMidiDeviceInfo();
//
//		if ( 0 == info.length)
//			return false;

		try {
//			for ( int i = 0; i < info.length; ++i) {
//				MidiDevice midiDevice = MidiSystem.getMidiDevice( info[ i]);
//				if ( !info[ i].getName().equals( "Java Sound Synthesizer"))
//					continue;
//
//				_midiDevice = midiDevice;
//				break;
//			}
//
//			if ( null == _midiDevice)
//				return false;
//
//			if ( !_midiDevice.isOpen())
//				_midiDevice.open();

			Sequence sequence = MidiSystem.getSequence( file);
			_sequencer = MidiSystem.getSequencer();
			_sequencer.addMetaEventListener( this);
			_sequencer.addControllerEventListener( this, new int[] { ShortMessage.TIMING_CLOCK});
			_sequencer.open();
			_sequencer.setSequence( sequence);
			_sequencer.start();

			Thread thread = new Thread( new Observer());
			thread.start();
			try {
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
			return false;
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		if ( !create())
			return false;

		pack();
		setSize( 300, 100);
		setLocationRelativeTo( getParent());
		setTitle( file.getName());
		setVisible( true);

		return true;
	}

	/* (non-Javadoc)
	 * @see javax.sound.midi.MetaEventListener#meta(javax.sound.midi.MetaMessage)
	 */
	public void meta(MetaMessage arg0) {
		//System.out.println( String.format( "%x", arg0.getType()) + ", " + ", " + arg0.toString());
	}

	/* (non-Javadoc)
	 * @see javax.sound.midi.ControllerEventListener#controlChange(javax.sound.midi.ShortMessage)
	 */
	public void controlChange(ShortMessage arg0) {
		//System.out.println( String.format( "%x", arg0.getCommand()));
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		link_to_cancel( getRootPane());


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_stopButton();

		insert_horizontal_glue();


		setDefaultCloseOperation( DISPOSE_ON_CLOSE);


		return true;
	}

	/**
	 * 
	 */
	private void setup_stopButton() {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JButton button = new JButton( "Stop");
		button.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				stop();
				dispose();
			}
		});
		panel.add( button);

		panel.add( Box.createHorizontalStrut( 5));
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	public boolean stop() {
		if ( null != _sequencer) {
			_sequencer.stop();
			_sequencer.close();
			_sequencer = null;
			
		}
//		if ( null != _midiDevice) {
//			_midiDevice.close();
//			_midiDevice = null;
//		}

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
	 */
	protected void on_cancel(ActionEvent actionEvent) {
		stop();
		super.on_cancel(actionEvent);
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
	 */
	protected void on_ok(ActionEvent actionEvent) {
		stop();
		super.on_ok(actionEvent);
	}

	class Observer implements Runnable {

		/* (non-Javadoc)
		 * @see java.lang.Runnable#run()
		 */
		public void run() {
			while ( 0l >= _sequencer.getTickPosition()) {
				try {
					Thread.sleep( 1000l);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep( 2500l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * The entry point of this application.
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		if ( 1 > args.length)
			System.exit( 1);

		Player4 player4 = new Player4( null);
		boolean result = player4.play( args[ 0]);
	}
}

///**
// * @author kurata
// *
// */
//public class Player4 extends Dialog {
//
//	/**
//	 * 
//	 */
//	private AudioClip _audioClip = null;
//
//	/**
//	 * @param arg0
//	 */
//	public Player4(Frame arg0) {
//		super(arg0, "", true);
//	}
//
//	/**
//	 * @param filename
//	 */
//	public boolean play(String filename) {
//		File file = new File( filename.replaceAll( "\\\\", "/"));
//		if ( !file.exists() || !file.isFile() || !file.canRead())
//			return false;
//
//		try {
//			_audioClip = Applet.newAudioClip( file.toURI().toURL());
//			_audioClip.play();
//		} catch (MalformedURLException e) {
//			e.printStackTrace();
//			return false;
//		}
//
//		if ( !create())
//			return false;
//
//		pack();
//		setSize( 300, 100);
//		setLocationRelativeTo( getParent());
//		setTitle( file.getName());
//		setVisible( true);
//
//		return true;
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
//	 */
//	protected boolean on_init_dialog() {
//		if ( !super.on_init_dialog())
//			return false;
//
//
//		link_to_cancel( getRootPane());
//
//
//		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));
//
//		insert_horizontal_glue();
//
//		setup_stopButton();
//
//		insert_horizontal_glue();
//
//
//		setDefaultCloseOperation( DISPOSE_ON_CLOSE);
//
//
//		return true;
//	}
//
//	/**
//	 * 
//	 */
//	private void setup_stopButton() {
//		JPanel panel = new JPanel();
//		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));
//
//		panel.add( Box.createHorizontalStrut( 5));
//
//		JButton button = new JButton( "Stop");
//		button.addActionListener( new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				stop();
//			}
//		});
//		panel.add( button);
//
//		panel.add( Box.createHorizontalStrut( 5));
//		getContentPane().add( panel);
//	}
//
////	/**
////	 * 
////	 */
////	public boolean play() {
////		if ( null == _audioClip)
////			return false;
////
////		_audioClip.play();
////		return true;
////	}
//
//	/**
//	 * 
//	 */
//	public boolean stop() {
//		if ( null == _audioClip)
//			return false;
//
//		_audioClip.stop();
//
//		dispose();
//
//		return true;
//	}
//
////	/**
////	 * 
////	 */
////	public boolean loop() {
////		if ( null == _audioClip)
////			return false;
////
////		_audioClip.loop();
////		return true;
////	}
//
//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.window.Dialog#on_cancel(java.awt.event.ActionEvent)
//	 */
//	protected void on_cancel(ActionEvent actionEvent) {
//		stop();
//		super.on_cancel(actionEvent);
//	}
//
//	/* (non-Javadoc)
//	 * @see soars.common.utility.swing.window.Dialog#on_ok(java.awt.event.ActionEvent)
//	 */
//	protected void on_ok(ActionEvent actionEvent) {
//		stop();
//		super.on_ok(actionEvent);
//	}
//}
