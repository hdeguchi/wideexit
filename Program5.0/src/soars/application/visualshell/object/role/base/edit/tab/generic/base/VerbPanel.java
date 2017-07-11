/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.generic.base;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.ConstantPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.ObjectPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.SubjectPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.expression.ExpressionPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Verb;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.common.utility.swing.combo.ComboBox;
import soars.common.utility.swing.tool.SwingTool;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class VerbPanel extends JPanel {

	/**
	 * 
	 */
	protected int _standardControlWidth = 240;

	/**
	 * 
	 */
	protected Property _property = null;

	/**
	 * 
	 */
	protected GenericPropertyPanel _genericPropertyPanel = null;

	/**
	 * 
	 */
	protected Map<Verb, SubjectPanel> _verbSubjectPanelMap = new HashMap<Verb, SubjectPanel>();

	/**
	 * 
	 */
	protected Map<String, Verb> _methodNameVerbMap = new HashMap<String, Verb>();

	/**
	 * 
	 */
	protected Map<Verb, JPanel> _verbObjectPanelBaseMap = new HashMap<Verb, JPanel>();

	/**
	 * 
	 */
	protected Map<Verb, Vector<PanelRoot>> _verbObjectPanelsMap = new HashMap<Verb, Vector<PanelRoot>>();

	/**
	 * 
	 */
	protected JLabel _verbLabel = null;

	/**
	 * 
	 */
	protected ComboBox _verbComboBox = null;

	/**
	 * 
	 */
	private Verb _previousVerb = null;

	/**
	 * 
	 */
	private JPanel _subjectMainPanel = null;

	/**
	 * 
	 */
	private JPanel _objectMainPanel = null;

	/**
	 * 
	 */
	private Component _parent = null;

	/**
	 * 
	 */
	private Frame _owner = null;

	/**
	 * @param property
	 * @param genericPropertyPanel
	 * @param owner
	 * @param parent
	 */
	public VerbPanel(Property property, GenericPropertyPanel genericPropertyPanel, Frame owner, Component parent) {
		super();
		_property = property;
		_genericPropertyPanel = genericPropertyPanel;
		_owner = owner;
		_parent = parent;
	}

	/**
	 * @param role
	 * @param buddiesMap
	 * @param parent
	 * @return
	 */
	public boolean setup(Role role, Map<String, List<PanelRoot>> buddiesMap, JPanel parent) {
		if ( _property.isEmpty())
			return false;

		setup_methodNameVerbMap();

		if ( !setup_subjectPanel( role, buddiesMap, parent))
			return false;

		setup( parent);

//		if ( !setup_subjectPanel( role, buddiesMap, parent))
//			return false;

		if ( !setup_objectPanel( role, buddiesMap, parent))
			return false;

		return true;
	}

	/**
	 * 
	 */
	private void setup_methodNameVerbMap() {
		for ( Verb verb:_property)
			_methodNameVerbMap.put( verb._methodName, verb);
	}

	/**
	 * @param role
	 * @param buddiesMap
	 * @param parent
	 * @return
	 */
	private boolean setup_subjectPanel(Role role, Map<String, List<PanelRoot>> buddiesMap, JPanel parent) {
		_subjectMainPanel = new JPanel();
		_subjectMainPanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		for ( Verb verb:_property) {
			if ( !setup_verbSubjectPanelMap( verb, role, buddiesMap, northPanel))
				return false;
		}

		SwingTool.insert_vertical_strut( northPanel, 5);

		_subjectMainPanel.add( northPanel, "North");

		parent.add( _subjectMainPanel);

		return true;
	}

	/**
	 * @param verb
	 * @param role
	 * @param buddiesMap
	 * @param parent
	 * @return
	 */
	private boolean setup_verbSubjectPanelMap(Verb verb, Role role, Map<String, List<PanelRoot>> buddiesMap, JPanel parent) {
		// このverbのSubjectPanelを設定
		// 同じ内容のSubjectPanelがあれば共用する→Mapが必要
		SubjectPanel subjectPanel = get_same_subjectPanel( verb._subject);
		if ( null != subjectPanel)
			_verbSubjectPanelMap.put( verb, subjectPanel);
		else {
			Subject subject = new Subject( verb._subject);
			subjectPanel = new SubjectPanel( subject, this, _property, role, buddiesMap, _genericPropertyPanel);
			if ( !subjectPanel.setup())
				return false;

			_verbSubjectPanelMap.put( verb, subjectPanel);
			parent.add( subjectPanel);
		}
		return true;
	}

	/**
	 * @param subject
	 * @return
	 */
	private SubjectPanel get_same_subjectPanel(Subject subject) {
		Collection<SubjectPanel> subjectPanels = _verbSubjectPanelMap.values();
		for ( SubjectPanel subjectPanel:subjectPanels) {
			if ( subjectPanel._subject.same_as( subject))
				return subjectPanel;
		}
		return null;
	}

	/**
	 * @param parent
	 */
	private void setup(JPanel parent) {
		setLayout( new BoxLayout( this, BoxLayout.X_AXIS));

		add( Box.createHorizontalStrut( 2));

		JPanel basePanel = new JPanel();
		basePanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		JPanel panel = new JPanel();
		panel.setLayout( new GridLayout( 1, 2));

		setup_verb_panel( panel);

		setup_denial_panel( panel);

		northPanel.add( panel);

		panel.setBorder( BorderFactory.createLineBorder( Color.red, 1));

		SwingTool.insert_vertical_strut( northPanel, 5);

		basePanel.add( northPanel, "North");

		add( basePanel);

		add( Box.createHorizontalStrut( 2));

		parent.add( this);
	}

	/**
	 * @param parent
	 */
	private void setup_verb_panel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		SwingTool.insert_vertical_strut( northPanel, 5);

		if ( 1 == _property.size())
			setup_verbLabel( northPanel);
		else
			setup_verbComboBox( northPanel);

		SwingTool.insert_vertical_strut( northPanel, 5);

		panel.add( northPanel, "North");

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_verbLabel(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new FlowLayout( FlowLayout.LEFT, 5, 0));

		_verbLabel = _genericPropertyPanel.create_label( _property.get_verb_names()[ 0], false);
		panel.add( _verbLabel);

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	private void setup_verbComboBox(JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BoxLayout( panel, BoxLayout.X_AXIS));

		panel.add( Box.createHorizontalStrut( 5));

		_verbComboBox = _genericPropertyPanel.create_comboBox( _property.get_verb_names(), _standardControlWidth, _property._color, false);
		_verbComboBox.addActionListener( new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Verb verb = _methodNameVerbMap.get( ( String)_verbComboBox.getSelectedItem());

				SubjectPanel activeSubjectPanel = _verbSubjectPanelMap.get( verb);
				Collection<SubjectPanel> subjectPanels = _verbSubjectPanelMap.values();
				for ( SubjectPanel subjectPanel:subjectPanels)
					subjectPanel.setVisible( subjectPanel == activeSubjectPanel);

				_subjectMainPanel.setVisible( activeSubjectPanel._visible);

				_verbSubjectPanelMap.get( verb).update_variable_panel();

				JPanel activePanel = _verbObjectPanelBaseMap.get( verb);
				Collection<JPanel> panels = _verbObjectPanelBaseMap.values();
				for ( JPanel panel:panels)
					panel.setVisible( panel == activePanel);

				Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
				_objectMainPanel.setVisible( !panelRoots.isEmpty());
				for ( PanelRoot panelRoot:panelRoots)
					panelRoot.update_variable_panel();

				show_denial_panel( verb);

				_previousVerb = verb;
			}
		});
		_verbComboBox.addItemListener( new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO ここで兄弟パネルの同期を取る
				if ( ItemEvent.DESELECTED == arg0.getStateChange()) {
					SubjectPanel activeSubjectPanel = _verbSubjectPanelMap.get( _previousVerb);
					activeSubjectPanel.synchronize();

					Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( _previousVerb);
					for ( PanelRoot panelRoot:panelRoots)
						panelRoot.synchronize();
				}
			}
		});
		panel.add( _verbComboBox);

		panel.add( Box.createHorizontalStrut( 5));

		parent.add( panel);
	}

	/**
	 * @param parent
	 */
	protected void setup_denial_panel(JPanel parent) {
	}

	/**
	 * @param verb
	 */
	protected void show_denial_panel(Verb verb) {
	}

	/**
	 * @param role
	 * @param buddiesMap
	 * @param parent
	 * @return
	 */
	private boolean setup_objectPanel(Role role, Map<String, List<PanelRoot>> buddiesMap, JPanel parent) {
		_objectMainPanel = new JPanel();
		_objectMainPanel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		for ( Verb verb:_property) {
			if ( !setup_verbObjectPanelMap( verb, role, buddiesMap, northPanel))
				return false;
		}

		_objectMainPanel.add( northPanel, "North");

		parent.add( _objectMainPanel);

		return true;
	}

	/**
	 * @param verb
	 * @param role
	 * @param buddiesMap
	 * @param parent
	 * @return
	 */
	private boolean setup_verbObjectPanelMap(Verb verb, Role role, Map<String, List<PanelRoot>> buddiesMap, JPanel parent) {
		JPanel panel = new JPanel();
		panel.setLayout( new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout( new BoxLayout( northPanel, BoxLayout.Y_AXIS));

		Vector<PanelRoot> panelRoots = new Vector<PanelRoot>();

		for ( Subject object:verb._objects) {
			// TODO ここで生成
			PanelRoot panelRoot = null;
			if ( object._expression)
				panelRoot = new ExpressionPanel( object, _property, role, buddiesMap, _genericPropertyPanel, _owner, parent);
			else if ( object._constant)
				panelRoot = new ConstantPanel( object, _property, role, buddiesMap, _genericPropertyPanel);
			else
				panelRoot = new ObjectPanel( object, this, _property, role, buddiesMap, _genericPropertyPanel);

			if ( !panelRoot.setup())
				return false;

			northPanel.add( panelRoot);

			SwingTool.insert_vertical_strut( northPanel, 5);

			panelRoots.add( panelRoot);
		}

		panel.add( northPanel, "North");

		parent.add( panel);

		_verbObjectPanelBaseMap.put( verb, panel);

		_verbObjectPanelsMap.put( verb, panelRoots);

		return true;
	}

	/**
	 * 
	 */
	public void initialize() {
		Collection<SubjectPanel> subjectPanels = _verbSubjectPanelMap.values();
		for ( SubjectPanel subjectPanel:subjectPanels)
			subjectPanel.initialize();

		Collection<Vector<PanelRoot>> values = _verbObjectPanelsMap.values();
		for ( Vector<PanelRoot> value:values) {
			for ( PanelRoot panelRoot:value)
				panelRoot.initialize();
		}

		if ( null != _verbComboBox) {
			_verbComboBox.setSelectedIndex( 0);
			// TODO これ重要
			_previousVerb = _methodNameVerbMap.get( ( String)_verbComboBox.getSelectedItem());
			show_denial_panel( _previousVerb);
		} else {
			if ( _property.isEmpty() || !_verbSubjectPanelMap.get( _property.get( 0))._visible) {
				_subjectMainPanel.setVisible( false);
				return;
			}

			_verbSubjectPanelMap.get( _property.get( 0)).update_variable_panel();

			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( _property.get( 0));
			for ( PanelRoot panelRoot:panelRoots)
				panelRoot.update_variable_panel();
		}
	}

	/**
	 * @return
	 */
	public Vector<ObjectPanel> get_objectPanels() {
		Vector<ObjectPanel> objectPanels = new Vector<ObjectPanel>();
		Collection<Vector<PanelRoot>> values = _verbObjectPanelsMap.values();
		for ( Vector<PanelRoot> value:values) {
			for ( PanelRoot panelRoot:value) {
				if ( !( panelRoot instanceof ObjectPanel))
					continue;

				objectPanels.add( ( ObjectPanel)panelRoot);
			}
		}
		return objectPanels;
//		if ( null != _verbComboBox) {
//			Verb verb = _methodNameVerbMap.get( ( String)_verbComboBox.getSelectedItem());
//			return _verbObjectPanelsMap.get( verb);
//		} else {
//			if ( _property.isEmpty())
//				return null;
//
//			return _verbObjectPanelsMap.get( _property.get( 0));
//		}
	}

	/**
	 * @return
	 */
	public SubjectPanel get_current_subjectPanel() {
		return ( ( null != _verbComboBox)
			? _verbSubjectPanelMap.get( _methodNameVerbMap.get( ( String)_verbComboBox.getSelectedItem()))
			: _verbSubjectPanelMap.get( _property.get( 0)));
	}

	/**
	 * @param genericRule
	 * @return
	 */
	public boolean set(GenericRule genericRule) {
		// TODO ここが重要
		Verb verb = get_verb( genericRule._id);
		if ( null == verb)
			return false;

		if ( null != _verbComboBox)
			_verbComboBox.setSelectedItem( verb._methodName);

		SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
		if ( !subjectPanel.set( genericRule._subject, true))
			return false;

		Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
		if ( panelRoots.size() != genericRule._objects.size())
			return false;

		for ( int i = 0; i < panelRoots.size(); ++i) {
			if ( !panelRoots.get( i).set( genericRule._objects.get( i), true))
				return false;
		}

		show_denial_panel( verb);

		_previousVerb = verb;

		return true;
	}

	/**
	 * @param id
	 * @return
	 */
	private Verb get_verb(String id) {
		for ( Verb verb:_property) {
			if ( verb._id.equals( id))
				return verb;
		}
		return null;
	}

	/**
	 * @param genericRule
	 * @return
	 */
	public boolean get(GenericRule genericRule) {
		// TODO ここが重要
		if ( _property.isEmpty())
			return false;

		Verb verb = ( 1 == _property.size()) ? _property.get( 0) : _methodNameVerbMap.get( ( String)_verbComboBox.getSelectedItem());
		verb.get( genericRule);

		SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
		if ( !subjectPanel.get( genericRule._subject, true))
			return false;

		Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
		for ( PanelRoot panelRoot:panelRoots) {
			IObject object = panelRoot.get( true);
			if ( null == object)
				return false;

			genericRule._objects.add( object);
		}

		return true;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = false;
		for ( Verb verb:_property) {
			SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
			if ( subjectPanel.update_stage_name( newName, originalName))
				result = true;

			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
			for ( PanelRoot panelRoot:panelRoots) {
				if ( panelRoot.update_stage_name( newName, originalName))
					result = true;
			}
		}
		return result;
	}

	/**
	 * 
	 */
	public void on_update_stage() {
		for ( Verb verb:_property) {
			SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
			subjectPanel.on_update_stage();

			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
			for ( PanelRoot panelRoot:panelRoots)
				panelRoot.on_update_stage();
		}
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 */
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
		for ( Verb verb:_property) {
			SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
			subjectPanel.on_update_expression( newExpression, newVariableCount, originalExpression);

			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
			for ( PanelRoot panelRoot:panelRoots)
				panelRoot.on_update_expression( newExpression, newVariableCount, originalExpression);
		}
	}

	/**
	 * 
	 */
	public void on_update_expression() {
		// TODO 2014.2.14
		for ( Verb verb:_property) {
			SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
			subjectPanel.on_update_expression();

			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
			for ( PanelRoot panelRoot:panelRoots)
				panelRoot.on_update_expression();
		}
	}

	/**
	 * 
	 */
	public void adjust() {
		int width = 0;
		for ( Verb verb:_property) {
			SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
			width = subjectPanel.get_max_width( width);
			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
			for ( PanelRoot panelRoot:panelRoots) {
				width = panelRoot.get_max_width( width);
			}
		}

		if ( 0 == width)
			return;

		for ( Verb verb:_property) {
			SubjectPanel subjectPanel = _verbSubjectPanelMap.get( verb);
			subjectPanel.set_max_width( width);
			Vector<PanelRoot> panelRoots = _verbObjectPanelsMap.get( verb);
			for ( PanelRoot panelRoot:panelRoots) {
				panelRoot.set_max_width( width);
			}
		}
	}
}
