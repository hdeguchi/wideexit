/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab;

import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Verb;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.expression.Expression;

/**
 * @author kurata
 *
 */
public class RuleTabbedPane extends JPanel {

	/**
	 * 
	 */
	public String _kind = "";

	/**
	 * 
	 */
	public RuleTree _ruleTree = null;

	/**
	 * 
	 */
	public List<RulePropertyPanelBase> _rulePropertyPanelBases = new ArrayList<RulePropertyPanelBase>();

	/**
	 * 
	 */
	public RulePropertyPanelBase _defaultRulePropertyPanelBase = null;

	/**
	 * @param order
	 * @param orderFilenames
	 * @param agentRoleTypePropertyMap
	 * @param spotRoleTypePropertyMap
	 * @param propertyFilenames
	 * @param denial
	 * @param kind
	 * @return
	 */
	protected static boolean load(List<String> order, List<String> orderFilenames, Map<String, Property> agentRoleTypePropertyMap, Map<String, Property> spotRoleTypePropertyMap, List<String> propertyFilenames, boolean denial, String kind) {
		for ( String orderFilename:orderFilenames) {
			if ( !load_order( order, orderFilename))
				return false;
		}

		for ( int i = 0; i < propertyFilenames.size(); ++i) {
			Vector<Property> properties = new Vector<Property>();
			if ( !load_properties( properties, propertyFilenames.get( i), denial, 0 == i, kind))
				return false;

			for ( Property property:properties) {
				if ( property._entity.equals( "agent"))
					agentRoleTypePropertyMap.put( property._type, property);
				else if ( property._entity.equals( "spot"))
					spotRoleTypePropertyMap.put( property._type, property);
				else if ( property._entity.equals( "both")) {
					agentRoleTypePropertyMap.put( property._type, property);
					spotRoleTypePropertyMap.put( property._type, property);
				}
				property.sort();
			}
		}

		return true;
	}

	/**
	 * @param order
	 * @param filename
	 * @return
	 */
	protected static boolean load_order(List<String> order, String filename) {
		File file = new File( filename);
		if ( !file.exists())
			return true;

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		int number = 0;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return false;
			}

			if ( null == line)
				break;

			if ( !read( line, order, ++number)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	/**
	 * @param line
	 * @param order
	 * @param number
	 * @return
	 */
	private static boolean read(String line, List<String> order, int number) {
		if ( line.startsWith( "//") || line.equals( ""))
			return true;

		order.add( line);

		return true;
	}

	/**
	 * @param properties
	 * @param filename
	 * @param denial
	 * @param system
	 * @param kind
	 * @return
	 */
	protected static boolean load_properties(Vector<Property> properties, String filename, boolean denial, boolean system, String kind) {
		File file = new File( filename);
		if ( !file.exists())
			return true;

		BufferedReader bufferedReader;
		try {
			bufferedReader = new BufferedReader( new InputStreamReader( new FileInputStream( file), "UTF8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		int number = 0;
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e1) {
				try {
					bufferedReader.close();
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				e1.printStackTrace();
				return false;
			}

			if ( null == line)
				break;

			if ( !read( line, properties, file, denial, system, kind, ++number)) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;
			}
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		//if ( !_properties.isEmpty())
		//	_properties.firstElement().print();

		return true;
	}

	/**
	 * @param line
	 * @param properties
	 * @param file
	 * @param denial
	 * @param system
	 * @param kind
	 * @param number
	 * @return
	 */
	private static boolean read(String line, Vector<Property> properties, File file, boolean denial, boolean system, String kind, int number) {
		if ( line.startsWith( "//"))
			return true;

		if ( line.startsWith( "entity:")) {
			String[] words = Tool.split( line.substring( "entity:".length()), ',');
			if ( null == words || 1 > words.length) {
				JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
				return false;
			}

			Property property = new Property( denial, system, kind);
			property._entity = words[ 0];
			properties.add( property);
		} else {
			if ( properties.isEmpty())
				return true;

			if ( line.startsWith( "type:")) {
				String[] words = Tool.split( line.substring( "type:".length()), ',');
				if ( null == words || 1 > words.length) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}

				properties.lastElement()._type = words[ 0];
			} else if ( line.startsWith( "name:")) {
				String[] words = Tool.split( line.substring( "name:".length()), ',');
				if ( null == words || 1 > words.length) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}

				properties.lastElement()._name = words[ 0];
			} else if ( line.startsWith( "parent:")) {
				String[] words = Tool.split( line.substring( "parent:".length()), ',');
				if ( null == words || 1 > words.length || words[ 0].equals( "")) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}

				words = Tool.split( words[ 0], ';');
				if ( null == words || 1 > words.length || words[ 0].equals( "")) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}

				for ( String word:words) {
					if ( null == word || word.equals( "")) {
						JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
						return false;
					}

					properties.lastElement()._parents.add( word);
				}
			} else if ( line.startsWith( "color:")) {
				String[] words = Tool.split( line.substring( "color:".length()), ',');
				if ( null == words || 3 > words.length) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}

				if ( !properties.lastElement().set_color( words[ 0], words[ 1], words[ 2])) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "title:")) {
				String[] words = Tool.split( line.substring( "title:".length()), ',');
				if ( null == words || 1 > words.length) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}

				properties.lastElement()._title = words[ 0];
			} else if ( line.startsWith( "denial:no")) {
				properties.lastElement()._denial = false;
			} else if ( line.startsWith( "subject:")) {
				if ( !properties.lastElement().set_subject( line.substring( "subject:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "object:")) {
				if ( !properties.lastElement().set_object( line.substring( "object:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "expression:")) {
				if ( !properties.lastElement().set_expression( line.substring( "expression:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "variable:")) {
				if ( !properties.lastElement().set_variable( line.substring( "variable:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "constant:")) {
				if ( !properties.lastElement().set_constant( line.substring( "constant:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "value:")) {
				if ( !properties.lastElement().set_value( line.substring( "value:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "sync:")) {
				if ( !properties.lastElement().set_sync( line.substring( "sync:".length()))) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			} else if ( line.startsWith( "verb:")) {
				if ( !properties.lastElement().set_verb( line.substring( "verb:".length()), denial)) {
					JOptionPane.showMessageDialog( null, file.getName() + " : Line " + number, ResourceManager.get_instance().get( "application.title"), JOptionPane.ERROR_MESSAGE);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param genericRule
	 * @param typePropertyMap
	 * @return
	 */
	protected static String get_method(GenericRule genericRule, Map<String, Property> typePropertyMap) {
		Property property = typePropertyMap.get( genericRule._type);
		if ( null == property)
			return "Unknown";

		for ( Verb verb:property) {
			if ( genericRule._id.equals( verb._id))
				return verb._method;
		}

		return "Unknown";
	}

	/**
	 * @param genericRule
	 * @param typePropertyMap
	 * @return
	 */
	protected static String get_name(GenericRule genericRule, Map<String, Property> typePropertyMap) {
		Property property = typePropertyMap.get( genericRule._type);
		if ( null == property)
			return "Unknown";

		return property._name;
	}

	/**
	 * @param genericRule
	 * @param typePropertyMap
	 * @return
	 */
	protected static Color get_color(GenericRule genericRule, Map<String, Property> typePropertyMap) {
		Property property = typePropertyMap.get( genericRule._type);
		if ( null == property)
			return Color.black;

		return property._color;
	}

	/**
	 * @param kind
	 */
	public RuleTabbedPane(String kind) {
		super();
		_kind = kind;
	}

	/**
	 * @param typeOrder
	 * @param typePropertyMap
	 * @param ruleTree
	 * @param role
	 * @param buddiesMap
	 * @param frame
	 * @param editRoleFrame
	 * @return
	 */
	public boolean setup(List<String> typeOrder, Map<String, Property> typePropertyMap, RuleTree ruleTree, Role role, Map<String, List<PanelRoot>> buddiesMap, Frame frame, EditRoleFrame editRoleFrame) {
		_ruleTree = ruleTree;

		setLayout( new BoxLayout( this, BoxLayout.Y_AXIS));

		for ( int i = 0; i < typeOrder.size(); ++i) {
			Property property = typePropertyMap.get( typeOrder.get( i));
			if ( null != property) {
				if ( !setup_genericPropertyPanel( property, role, buddiesMap, frame, editRoleFrame))
					return false;
			} else {
				if ( !setup_legacyPropertyPanel( typeOrder.get( i), role, frame, editRoleFrame))
					return false;
			}
		}

		// TODO For exchange algebra
		if ( Environment.get_instance().is_exchange_algebra_enable()) {
			if ( !setup_legacyPropertyPanel( "exchangealgebra", role, frame, editRoleFrame))
				return false;
			if ( !setup_legacyPropertyPanel( "extransfer", role, frame, editRoleFrame))
				return false;
		}

		ruleTree.expand();

		return true;
	}

	/**
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param frame
	 * @param editRoleFrame
	 * @return
	 */
	protected boolean setup_genericPropertyPanel(Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, Frame frame, EditRoleFrame editRoleFrame) {
		if ( ( role instanceof AgentRole && property._entity.equals( "spot"))
			|| ( role instanceof SpotRole && property._entity.equals( "agent")))
			return true;

		GenericPropertyPanel genericPropertyPanel = create_genericPropertyPanel( property, role, buddiesMap, _rulePropertyPanelBases.size(), frame, editRoleFrame);
		if ( property._parents.isEmpty()) {
			if ( !create( genericPropertyPanel))
				return false;
		} else {
			if ( !create( genericPropertyPanel, property._parents.toArray( new String[ 0])))
				return false;
		}

		if ( null == _defaultRulePropertyPanelBase)
			_defaultRulePropertyPanelBase = genericPropertyPanel;

		return true;
	}

	/**
	 * @param property
	 * @param role
	 * @param buddiesMap
	 * @param index
	 * @param frame
	 * @param editRoleFrame
	 * @return
	 */
	protected GenericPropertyPanel create_genericPropertyPanel(Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, int index, Frame frame, EditRoleFrame editRoleFrame) {
		return null;
	}

	/**
	 * @param type
	 * @param role
	 * @param frame
	 * @param editRoleFrame
	 * @return
	 */
	protected boolean setup_legacyPropertyPanel(String type, Role role, Frame frame, EditRoleFrame editRoleFrame) {
		return true;
	}

	/**
	 * @param rulePropertyPanelBase
	 * @return
	 */
	protected boolean create(RulePropertyPanelBase rulePropertyPanelBase) {
		if ( !rulePropertyPanelBase.create())
			return false;

		add( rulePropertyPanelBase);
		_ruleTree.append( rulePropertyPanelBase);
		_rulePropertyPanelBases.add( rulePropertyPanelBase);
		return true;
	}

	/**
	 * @param rulePropertyPanelBase
	 * @param nodeText
	 * @return
	 */
	protected boolean create(RulePropertyPanelBase rulePropertyPanelBase, String nodeText) {
		return create( rulePropertyPanelBase, new String[] { nodeText});
	}

	/**
	 * @param rulePropertyPanelBase
	 * @param nodeTexts
	 * @return
	 */
	protected boolean create(RulePropertyPanelBase rulePropertyPanelBase, String[] nodeTexts) {
		if ( !rulePropertyPanelBase.create())
			return false;

		add( rulePropertyPanelBase);
		_ruleTree.append( nodeTexts, rulePropertyPanelBase);
		_rulePropertyPanelBases.add( rulePropertyPanelBase);
		return true;
	}

	/**
	 * @param rulePropertyPanelBase
	 * @param color
	 * @return
	 */
	protected boolean create(RulePropertyPanelBase rulePropertyPanelBase, Color color) {
		if ( !rulePropertyPanelBase.create())
			return false;

		add( rulePropertyPanelBase);
		_ruleTree.append( rulePropertyPanelBase);
		_rulePropertyPanelBases.add( rulePropertyPanelBase);
		return true;
	}

	/**
	 * 
	 */
	public void select_default() {
		_ruleTree.select( _defaultRulePropertyPanelBase);
		select( _defaultRulePropertyPanelBase);
	}

	/**
	 * @param rulePropertyPanelBase
	 */
	public void select(RulePropertyPanelBase rulePropertyPanelBase) {
		for ( RulePropertyPanelBase rppb:_rulePropertyPanelBases)
			rppb.setVisible( rppb.equals( rulePropertyPanelBase));
	}

	/**
	 * @param rulePropertyPanelBase
	 */
	public void update(RulePropertyPanelBase rulePropertyPanelBase) {
		for ( RulePropertyPanelBase rppb:_rulePropertyPanelBases)
			rppb.update( rulePropertyPanelBase);
	}

	/**
	 * @param type
	 * @return
	 */
	public RulePropertyPanelBase get(String type) {
		for ( RulePropertyPanelBase rppb:_rulePropertyPanelBases) {
			if ( rppb._type.equals( type))
				return rppb;
		}
		return null;
	}

	/**
	 * @param rulePropertyPanelBase
	 * @return
	 */
	public int get_index(RulePropertyPanelBase rulePropertyPanelBase) {
		for ( int i = 0; i < _rulePropertyPanelBases.size(); ++i) {
			if ( _rulePropertyPanelBases.get( i).equals( rulePropertyPanelBase))
				return i;
		}
		return -1;
	}

	/**
	 * 
	 */
	public void on_update_stage() {
		for ( RulePropertyPanelBase rulePropertyPanelBase:_rulePropertyPanelBases) {
			if ( rulePropertyPanelBase instanceof GenericPropertyPanel)
				rulePropertyPanelBase.on_update_stage();
		}
	}

	/**
	 * @param newName
	 * @param originalName
	 * @return 
	 */
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = false;
		for ( RulePropertyPanelBase rulePropertyPanelBase:_rulePropertyPanelBases) {
			if ( rulePropertyPanelBase instanceof GenericPropertyPanel) {
				if ( rulePropertyPanelBase.update_stage_name( newName, originalName))
					result = true;
			}
		}
		return result;
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 */
	public void on_update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		// TODO 2014.2.14
		for ( RulePropertyPanelBase rulePropertyPanelBase:_rulePropertyPanelBases) {
			if ( rulePropertyPanelBase instanceof GenericPropertyPanel)
				rulePropertyPanelBase.on_update_expression( newExpression, newVariableCount, originalExpression);
		}
	}

	/**
	 * 
	 */
	public void on_update_expression() {
		// TODO 2014.2.14
		for ( RulePropertyPanelBase rulePropertyPanelBase:_rulePropertyPanelBases) {
			if ( rulePropertyPanelBase instanceof GenericPropertyPanel)
				rulePropertyPanelBase.on_update_expression();
		}
	}
}
