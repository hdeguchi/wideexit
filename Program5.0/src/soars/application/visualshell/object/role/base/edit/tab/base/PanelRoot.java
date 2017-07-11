/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.text.AbstractDocument.DefaultDocumentEvent;
import javax.swing.undo.UndoManager;

import soars.application.visualshell.common.selector.IObjectSelectorHandler;
import soars.application.visualshell.common.selector.ObjectSelector;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Subject;
import soars.application.visualshell.object.role.base.object.generic.element.IObject;
import soars.common.utility.swing.text.ITextUndoRedoManagerCallBack;
import soars.common.utility.swing.text.TextUndoRedoManager;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class PanelRoot extends JPanel implements IObjectSelectorHandler, ITextUndoRedoManagerCallBack {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static public Map<String, String> _typeNameMap = null;

	/**
	 * 
	 */
	protected int _standardNameWidth = 160;

	/**
	 * 
	 */
	protected int _standardNumberSpinnerWidth = 80;

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
	protected Role _role = null;

	/**
	 * 
	 */
	protected Map<String, List<PanelRoot>> _buddiesMap = null;

	/**
	 * 
	 */
	protected RulePropertyPanelBase _rulePropertyPanelBase = null;

	/**
	 * 
	 */
	public boolean _visible = true;

	/**
	 * 
	 */
	protected List<TextUndoRedoManager> _textUndoRedoManagers = new ArrayList<TextUndoRedoManager>();

	/**
	 * 
	 */
	static {
		startup();
	}

	/**
	 * 
	 */
	private static void startup() {
		synchronized( _lock) {
			if ( null == _typeNameMap) {
				_typeNameMap = new HashMap<String, String>();
				_typeNameMap.put( "probability", "probability");
				_typeNameMap.put( "collection", "collection");
				_typeNameMap.put( "list", "list");
				_typeNameMap.put( "map", "map");
				_typeNameMap.put( "keyword", "keyword");
				_typeNameMap.put( "number object", "number_object");
				_typeNameMap.put( "integer number object", "integer_number_object");
				_typeNameMap.put( "real number object", "real_number_object");
				_typeNameMap.put( "role variable", "role_variable");
				_typeNameMap.put( "time variable", "time_variable");
				_typeNameMap.put( "agent variable", "agent_variable");
				_typeNameMap.put( "spot variable", "spot_variable");
				_typeNameMap.put( "class variable", "class_variable");
				_typeNameMap.put( "file", "file");
				_typeNameMap.put( "exchange algebra", "exchange_algebra");
				_typeNameMap.put( "extransfer", "extransfer");
				_typeNameMap.put( "role", "role");
				_typeNameMap.put( "agent role", "agent_role");
				_typeNameMap.put( "spot role", "spot_role");
			}
		}
	}

	/**
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param rulePropertyPanelBase
	 */
	public PanelRoot(Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, RulePropertyPanelBase rulePropertyPanelBase) {
		super();
		_property = property;
		_role = role;
		_buddiesMap = buddiesMap;
		_rulePropertyPanelBase = rulePropertyPanelBase;
	}

	/**
	 * @return
	 */
	public boolean setup() {
		return true;
	}

	/**
	 * @param subject
	 * @return
	 */
	protected boolean setup_buddiesMap(Subject subject) {
		if ( subject._sync.equals( ""))
			return true;

		List<PanelRoot> panelRoots = _buddiesMap.get( subject._sync);
		if ( null == panelRoots) {
			panelRoots = new ArrayList<PanelRoot>();
			_buddiesMap.put( subject._sync, panelRoots);
		}

		panelRoots.add( this);

		return true;
	}

	/**
	 * 
	 */
	public void initialize() {
	}

	/**
	 * @param width
	 * @return
	 */
	public int get_max_width(int width) {
		return 0;
	}

	/**
	 * @param width
	 */
	public void set_max_width(int width) {
	}

	/**
	 * 
	 */
	public void update_variable_panel() {
	}

	/**
	 * @param object
	 * @param check
	 * @return
	 */
	public boolean set(IObject object, boolean check) {
		return false;
	}

	/**
	 * @param check
	 * @return
	 */
	public IObject get(boolean check) {
		return null;
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return
	 */
	public boolean update_stage_name(String newName, String originalName) {
		return false;
	}

	/**
	 * 
	 */
	public void on_update_stage() {
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 */
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
	}

	/**
	 * 
	 */
	public void on_update_expression() {
	}

	/**
	 * 
	 */
	public void synchronize() {
	}

	/**
	 * @param subject
	 */
	protected void synchronize(Subject subject) {
		List<PanelRoot> panelRoots = _buddiesMap.get( subject._sync);
		if ( null == panelRoots)
			return;

		for ( PanelRoot panelRoot:panelRoots) {
			if ( panelRoot == this)
				continue;

			panelRoot.synchronize( this);
		}
	}
	/**
	 * @param panelRoot
	 * @return
	 */
	protected boolean same_as(PanelRoot panelRoot) {
		return false;
	}


	/**
	 * @param panelRoot
	 */
	public void synchronize(PanelRoot panelRoot) {
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.common.selector.IObjectSelectorHandler#changed(java.lang.String, java.lang.String, java.lang.String, soars.application.visualshell.common.selector.ObjectSelector)
	 */
	@Override
	public void changed(String name, String number, String fullName, ObjectSelector objectSelector) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(UndoManager undoManager) {
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.text.ITextUndoRedoManagerCallBack#on_changed(javax.swing.text.AbstractDocument.DefaultDocumentEvent, javax.swing.undo.UndoManager)
	 */
	@Override
	public void on_changed(DefaultDocumentEvent defaultDocumentEvent, UndoManager undoManager) {
	}
}
