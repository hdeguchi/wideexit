/**
 * 
 */
package soars.library.arbitrary.test001.object_checker;

import java.awt.FlowLayout;
import java.awt.Frame;

import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @author kurata
 *
 */
public class Test1Dlg extends JDialog {


	/**
	 * 
	 */
	private JTextField _textField = null;

	/**
	 * @param arg0
	 * @param object
	 */
	static public void execute(Frame arg0, Object object) {
		Test1Dlg test1Dlg = new Test1Dlg(arg0);
		test1Dlg.execute(object);
	}

	/**
	 * @param arg0
	 */
	public Test1Dlg(Frame arg0) {
		super(arg0, "Object checker", true);
	}

	/**
	 * @param object 
	 */
	private void execute(Object object) {
		if ( null == object)
			return;

		setResizable( false);

		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		int pad = 5;

		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, pad, 0));

		_textField = new JTextField( object.getClass().getName());

		panel.add( _textField);

		getContentPane().add( panel);

		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		pack();
		setLocation( 10, 10);
		setVisible( true);
	}
}
