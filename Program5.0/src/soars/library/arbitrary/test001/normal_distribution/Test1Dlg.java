/**
 * 
 */
package soars.library.arbitrary.test001.normal_distribution;

import java.awt.Dimension;
import java.awt.Frame;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author kurata
 *
 */
public class Test1Dlg extends JDialog {


	/**
	 * 
	 */
	private NormalDistributionTable _normalDistributionTable = null;

	/**
	 * @param frame
	 */
	public Test1Dlg(Frame frame) {
		super(frame, "Normal Distribution", false);
	}

	/**
	 * 
	 */
	public void start() {
		if ( !on_init_dialog())
			return;

		pack();
		setLocation( 10, 10);
		setVisible( true);
	}

	/**
	 * 
	 */
	protected boolean on_init_dialog() {
		//setResizable( false);

		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		if ( !setup_normalDistributionTable())
			return false;

		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * @return
	 */
	private boolean setup_normalDistributionTable() {
		_normalDistributionTable = new NormalDistributionTable();
		if ( !_normalDistributionTable.setup())
			return false;

		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getViewport().setView( _normalDistributionTable);
		scrollPane.setPreferredSize( new Dimension( 600, 400));

		panel.add( scrollPane);
		panel.add( Box.createHorizontalStrut( 5));

		getContentPane().add( panel);

		return true;
	}

	/**
	 * @param row
	 * @param column
	 * @param value
	 */
	public void set(int row, int column, double value) {
		if ( 30 < row)
			return;

		if ( 9 < column)
			return;

		String data = String.valueOf( 1 - value);
		if ( 6 < data.length())
			data = data.substring( 0, 6);

		if ( 6 > data.length()) {
			while ( 6 > data.length())
				data += "0";
		}

		_normalDistributionTable.setValueAt( data, row, column + 1);
	}
}
