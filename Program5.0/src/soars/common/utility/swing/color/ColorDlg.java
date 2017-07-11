/*
 * Created on 2006/01/17
 */
package soars.common.utility.swing.color;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import soars.common.utility.swing.window.Dialog;

/**
 * @author kurata
 */
public class ColorDlg extends Dialog implements IColorSelection {

	/**
	 * 
	 */
	public ColorControlBase _selectedColor = null;

	/**
	 * 
	 */
	public Color _initialColor = null;

	/**
	 * 
	 */
	public ColorControl[][] _defaultColors = new ColorControl[ 2][ 14];

	/**
	 * 
	 */
	public Color[][] _colors = new Color[ 2][ 14];

	/**
	 * 
	 */
	private JButton _makeColorButton = null;

	/**
	 * 
	 */
	private String _ok = "";

	/**
	 * 
	 */
	private String _cancel = "";

	/**
	 * 
	 */
	private String _makeColor = "";

	/**
	 * @param frame
	 * @param title
	 * @param initialColor
	 * @param component
	 * @param ok
	 * @param cancel
	 * @param makeColor
	 * @return
	 */
	public static Color showDialog(Frame frame, String title, Color initialColor, Component component, String ok, String cancel, String makeColor) {
		Color[][] colors = new Color[][] {
			{ Color.black,
				new Color( 128, 128, 128),
				new Color( 128, 0, 0),
				new Color( 128, 128, 0),
				new Color( 0, 128, 0),
				new Color( 0, 128, 128),
				new Color( 0, 0, 128),
				new Color( 128, 0, 128),
				new Color( 128, 128, 64),
				new Color( 0, 64, 64),
				new Color( 0, 128, 255),
				new Color( 0, 64, 128),
				new Color( 128, 0, 255),
				new Color( 128, 64, 0)},
			{ Color.white,
				new Color( 192, 192, 192),
				Color.red,
				Color.yellow,
				Color.green,
				Color.cyan,
				Color.blue,
				Color.magenta,
				new Color( 255, 255, 128),
				new Color( 0, 255, 128),
				new Color( 128, 255, 255),
				new Color( 128, 128, 255),
				new Color( 255, 0, 128),
				new Color( 255, 128, 64)}
		};

		return showDialog( frame, title, initialColor, colors, component, ok, cancel, makeColor);
	}

	/**
	 * @param frame
	 * @param title
	 * @param initialColor
	 * @param colors
	 * @param component
	 * @param ok
	 * @param cancel
	 * @param makeColor
	 * @return
	 */
	public static Color showDialog(Frame frame, String title, Color initialColor, Color[][] colors, Component component, String ok, String cancel, String makeColor) {
		ColorDlg colorDlg = new ColorDlg( frame, title, true, initialColor, colors, ok, cancel, makeColor);
		if ( !colorDlg.do_modal( component))
			return null;

		return colorDlg._selectedColor._color;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param arg2
	 * @param initialColor
	 * @param colors
	 * @param ok
	 * @param cancel
	 * @param makeColor
	 * @throws HeadlessException
	 */
	public ColorDlg(Frame arg0, String arg1, boolean arg2, Color initialColor, Color[][] colors, String ok, String cancel, String makeColor)
		throws HeadlessException {
		super(arg0, arg1, arg2);
		_initialColor = new Color( initialColor.getRGB());
		_colors = colors;
		_ok = ok;
		_cancel = cancel;
		_makeColor = makeColor;
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.window.Dialog#on_init_dialog()
	 */
	protected boolean on_init_dialog() {
		if ( !super.on_init_dialog())
			return false;


		setResizable( false);


		getContentPane().setLayout( new BoxLayout( getContentPane(), BoxLayout.Y_AXIS));

		insert_horizontal_glue();

		setup_color();

		//insert_horizontal_glue();

		setup_default_colors( 0);
		setup_default_colors( 1);

		insert_horizontal_glue();

		setup_ok_and_cancel_button( _ok, _cancel, true, true);

		insert_horizontal_glue();


		adjust();

		setDefaultCloseOperation( DISPOSE_ON_CLOSE);

		return true;
	}

	/**
	 * 
	 */
	private void setup_color() {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_selectedColor = new ColorControlBase( _initialColor);
		_selectedColor.setPreferredSize( new Dimension( 280, 20));

		panel.add( _selectedColor);

		_makeColorButton = new JButton( _makeColor);
		_makeColorButton.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				on_action_performed( arg0);
			}
		});

		panel.add( _makeColorButton);

		getContentPane().add( panel);
	}

	/**
	 * @param arg0
	 */
	protected void on_action_performed(ActionEvent arg0) {
		Color color = JColorChooser.showDialog( this, _makeColor, _selectedColor._color);
		if ( null == color)
			return;

		_selectedColor._color = new Color( color.getRGB());
		_selectedColor.repaint();
	}

	/**
	 * @param index
	 */
	private void setup_default_colors(int index) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 3, 0));

		JPanel colorPanel = new JPanel();
		colorPanel.setLayout( new FlowLayout( FlowLayout.LEFT, 2, 1));

		for ( int i = 0; i < _defaultColors[ index].length; ++i) {
			_defaultColors[ index][ i] = new ColorControl( _colors[ index][ i], this);
			_defaultColors[ index][ i].setPreferredSize( new Dimension( 18, 18));
			colorPanel.add( _defaultColors[ index][ i]);
		}

		panel.add( colorPanel);
		getContentPane().add( panel);
	}

	/**
	 * 
	 */
	private void adjust() {
		int width = ( ( _defaultColors[ 0][ 0].getPreferredSize().width * _defaultColors[ 0].length)
			+ ( 2 * ( _defaultColors[ 0].length - 1))
			- _makeColorButton.getPreferredSize().width - 5);
		_selectedColor.setPreferredSize( new Dimension(width,
			_selectedColor.getPreferredSize().height));
	}

	/* (Non Javadoc)
	 * @see soars.common.utility.swing.color.IColorSelection#on_selected(java.awt.Color)
	 */
	public void on_selected(Color color) {
		_selectedColor._color = new Color( color.getRGB());
		_selectedColor.repaint();
	}
}
