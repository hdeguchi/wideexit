/**
 * 
 */
package soars.application.visualshell.object.entity.base.edit.panel.variable.panel.exchange_algebra.initial.base;

import java.awt.Color;
import java.awt.Dimension;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.edit.panel.base.PropertyPanelBase;
import soars.application.visualshell.object.entity.base.edit.panel.variable.panel.base.panel.InitialValueTextPanelBase;
import soars.common.utility.swing.text.TextExcluder;
import soars.common.utility.swing.text.TextField;
import soars.common.utility.swing.text.TextUndoRedoManager;

/**
 * @author kurata
 *
 */
public class ExchangeAlgebraInitialValueTextPanelBase extends InitialValueTextPanelBase {

	/**
	 * 
	 */
	private Color _color = null;

	/**
	 * 
	 */
	private EntityBase _entityBase = null;

	/**
	 * 
	 */
	private Map<String, PropertyPanelBase> _propertyPanelBaseMap = null;

	/**
	 * 
	 */
	private JLabel _label = null;

	/**
	 * @param color
	 * @param propertyPanelBaseMap
	 * @param entityBase
	 */
	public ExchangeAlgebraInitialValueTextPanelBase(Color color, EntityBase entityBase, Map<String, PropertyPanelBase> propertyPanelBaseMap) {
		super();
		_color = color;
		_entityBase = entityBase;
		_propertyPanelBaseMap = propertyPanelBaseMap;
	}

	/**
	 * 
	 */
	public void setup() {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 5));

		_label = create_label();
		_label.setHorizontalAlignment( SwingConstants.RIGHT);
		_label.setForeground( _color);
		add( _label);

		add( Box.createHorizontalStrut( 5));

		_textField = new TextField();
		_textField.setDocument( new TextExcluder( Constant._prohibitedCharacters11));
		_textField.setHorizontalAlignment( SwingConstants.RIGHT);
		_textField.setSelectionColor( _color);
		_textField.setForeground( _color);
		_textUndoRedoManagers.add( new TextUndoRedoManager( _textField, this));
		add( _textField);

		add( Box.createHorizontalStrut( 5));
	}

	/**
	 * @return
	 */
	protected JLabel create_label() {
		return null;
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_label_width(int width) {
		return Math.max( width, _label.getPreferredSize().width);
	}

	/**
	 * @param width
	 */
	public void adjust(int width) {
		_label.setPreferredSize( new Dimension( width, _label.getPreferredSize().height));
	}
}
