/*
 * Created on 2005/11/08
 */
package soars.application.visualshell.object.expression;

import java.awt.Frame;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.expression.edit.EditExpressionDlg;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.generic.common.expression.Variable;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.tool.expression.ExpressionManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The expression manager for Visual Shell.
 * @author kurata / SOARS project
 */
public class VisualShellExpressionManager extends ExpressionManager {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static private VisualShellExpressionManager _visualShellExpressionManager = null;

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
			if ( null == _visualShellExpressionManager) {
				_visualShellExpressionManager = new VisualShellExpressionManager();
				_visualShellExpressionManager.initialize();
			}
		}
	}

	/**
	 * Returns the instance of this object.
	 * @return the instance of this object
	 */
	public static VisualShellExpressionManager get_instance() {
		if ( null == _visualShellExpressionManager) {
			System.exit( 0);
		}

		return _visualShellExpressionManager;
	}

	/**
	 * Creates this object.
	 */
	public VisualShellExpressionManager() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param visualShellExpressionManager the specified data
	 */
	public VisualShellExpressionManager(VisualShellExpressionManager visualShellExpressionManager) {
		Iterator iterator = visualShellExpressionManager.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			put( ( String)entry.getKey(), new Expression( ( Expression)entry.getValue()));
		}
	}

	/**
	 * Returns true if the specified expression can be removed.
	 * @param expression the specified expression
	 * @return true if the specified expression can be removed
	 */
	public boolean can_remove(Expression expression) {
		return can_remove( expression._value[ 0]);
	}

	/**
	 * Returns true if the specified function can be removed.
	 * @param function the specified function
	 * @return true if the specified function can be removed
	 */
	public boolean can_remove(String function) {
		boolean result = true;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			if ( expression._value[ 0].equals( function))
				continue;

			if ( 0 <= expression._value[ 2].indexOf( function + "(")) {
				String[] message = new String[] {
					"Expression",
					"function = " + expression._value[ 0] + "(" + expression._value[ 1] + ")",
					"expression = " + expression._value[ 2]
				};
				WarningManager.get_instance().add( message);
				result = false;
			}
		}

		return result;
	}

	/**
	 * Returns true if all data are replaced with tne specified ones successfully.
	 * @param visualShellExpressionMap tne specified data
	 * @return true if all data are replaced with tne specified ones successfully
	 */
	public boolean replace(TreeMap<String, Expression> visualShellExpressionMap) {
		clear();
		putAll( visualShellExpressionMap);
		return true;
	}

	/**
	 * Returns the script for the initial data.
	 * @return the script for the initial data
	 */
	public String get_initial_data() {
		String script = "";
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();

			script += ( ResourceManager.get_instance().get( "initial.data.expression")
				+ "\t" + expression._value[ 0]
				+ "\t" + expression._value[ 1]
				+ "\t" + expression._value[ 2] + Constant._lineSeparator);
		}
		return ( script + Constant._lineSeparator);
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		if ( isEmpty())
			return true;

		AttributesImpl attributesImpl = new AttributesImpl();
		writer.startElement( null, null, "expression_data", attributesImpl);

		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();

			attributesImpl = new AttributesImpl();
			attributesImpl.addAttribute( null, null, "value0", "", Writer.escapeAttributeCharData( expression._value[ 0]));
			attributesImpl.addAttribute( null, null, "value1", "", Writer.escapeAttributeCharData( expression._value[ 1]));
			attributesImpl.addAttribute( null, null, "value2", "", Writer.escapeAttributeCharData( expression._value[ 2]));
			writer.writeElement( null, null, "expression", attributesImpl);
		}

		writer.endElement( null, null, "expression_data");

		return true;
	}

	/**
	 * Appends the specified data.
	 * @param expressionMap the specified data
	 */
	public void append(TreeMap<String, Expression> expressionMap) {
		Iterator iterator = expressionMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			put( expression._value[ 0], expression);
		}
	}

	/**
	 * @param role
	 * @return
	 */
	public Map<String, Vector<Variable>> get_variablesMap(Role role) {
		// TODO 2014.2.7 for version 5.0 or later
		HashMap<String, Vector<Variable>> variablesMap = new HashMap<String, Vector<Variable>>();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();

			Vector<Variable> variables = new Vector<Variable>();
			if ( !expression._value[ 1].equals( "")) {
				String[] words = expression._value[ 1].split( ",");
				if ( null == words || 0 == words.length)
					continue;

				for ( String word:words) {
					if ( words.equals( ""))
						continue;

					variables.add( new Variable( word, role));
				}
			}

			variablesMap.put( expression.get_function(), variables);
		}

		return variablesMap;
	}

	/**
	 * @param name
	 * @return
	 */
	public String get_function(String name) {
		// TODO 2014.2.7 for version 5.0 or later
		Expression expression = get( name);
		if ( null == expression)
			return null;

		return expression.get_function();
	}

	/**
	 * @param function
	 * @return
	 */
	public static String get_name(String function) {
		// TODO 2014.2.7 for version 5.0 or later
		String[] words = function.split( "\\(");
		if ( null == words || 2 > words.length)
			return "";

		return words[ 0];
	}

	/**
	 * @return
	 */
	public int get_max_variables_count() {
		// TODO 2014.2.8 to make sinulation script of expression spot
		int size = 0;
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Expression expression = ( Expression)entry.getValue();
			size = Math.max( size, expression.get_variables_count());
		}
		return size;
	}

	/**
	 * @param editRoleFrame
	 * @param frame
	 */
	public void edit(EditRoleFrame editRoleFrame, Frame frame) {
		// TODO 2014.2.14
		EditExpressionDlg editExpressionDlg = new EditExpressionDlg( frame,
			ResourceManager.get_instance().get( "edit.expression.dialog.title"),
			true,
			editRoleFrame);
		if ( !editExpressionDlg.do_modal())
			return;
	}
}
