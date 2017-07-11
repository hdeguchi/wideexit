/**
 * 
 */
package soars.application.visualshell.object.role.base.edit.tab;

import java.awt.Color;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import soars.application.visualshell.layer.LayerManager;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.condition.GenericConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.legacy.base.EditRuleValuePropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.base.SelectRuleValuePropertyPanel4;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.CollectionAndListConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.FunctionalObjectConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.KeywordConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.ListConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.NumberObjectConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.ProbabilityConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.RoleConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.SpotConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.condition.TimeConditionPropertyPanel;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.soars.environment.BasicEnvironment;

/**
 * @author kurata
 *
 */
public class ConditionTabbedPane extends RuleTabbedPane {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static protected List<String> _conditionTypeOrder = null;

	/**
	 * 
	 */
	static Map<String, Property> _agentRoleConditionTypePropertyMap = null;

	/**
	 * 
	 */
	static Map<String, Property> _spotRoleConditionTypePropertyMap = null;

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
			File userRuleScriptsFolder = LayerManager.get_instance().get_user_rule_scripts_directory();
			String projectFoldername = BasicEnvironment.get_instance().get( BasicEnvironment._projectFolderDirectoryKey, "");

			String language = Locale.getDefault().getLanguage();
			if ( null == language || language.equals( ""))
				language = "en";

			_conditionTypeOrder = new ArrayList<String>();

			List<String> orderFilenames = new ArrayList<String>();
			orderFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.condition.order.resource.filename"), Constant._systemRuleScriptsRelativePathName));
			if ( null != userRuleScriptsFolder)
				orderFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.condition.order.resource.filename"), userRuleScriptsFolder.getAbsolutePath()));
			else {
				if ( !projectFoldername.equals( ""))
					orderFilenames.add( projectFoldername + String.format( ResourceManager.get_instance().get( "generic.gui.rule.condition.order.resource.filename"), Constant._userRuleScriptsExternalRelativePathName));
			}

			List<String> propertyFilenames = new ArrayList<String>();
			propertyFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.condition.properties.resource.filename"), Constant._systemRuleScriptsRelativePathName, language));
			if ( null != userRuleScriptsFolder)
				propertyFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.condition.properties.resource.filename"), userRuleScriptsFolder.getAbsolutePath(), language));
			else {
				if ( !projectFoldername.equals( ""))
					propertyFilenames.add( projectFoldername + String.format( ResourceManager.get_instance().get( "generic.gui.rule.condition.properties.resource.filename"), Constant._userRuleScriptsExternalRelativePathName, language));
			}

			_agentRoleConditionTypePropertyMap = new HashMap<String, Property>();
			_spotRoleConditionTypePropertyMap = new HashMap<String, Property>();

			if ( !load( _conditionTypeOrder, orderFilenames, _agentRoleConditionTypePropertyMap, _spotRoleConditionTypePropertyMap, propertyFilenames, true, "condition"))
				return;
		}
	}

	/**
	 * @param genericRule
	 * @param role
	 * @return
	 */
	public static String get_method(GenericRule genericRule, Role role) {
		return get_method( genericRule, ( ( role instanceof AgentRole) ? _agentRoleConditionTypePropertyMap : _spotRoleConditionTypePropertyMap));
	}

	/**
	 * @param genericRule
	 * @param role
	 * @return
	 */
	public static String get_name(GenericRule genericRule, Role role) {
		return get_name( genericRule, ( ( role instanceof AgentRole) ? _agentRoleConditionTypePropertyMap : _spotRoleConditionTypePropertyMap));
	}

	/**
	 * @param genericRule
	 * @param role
	 * @return
	 */
	public static Color get_color(GenericRule genericRule, Role role) {
		return get_color( genericRule, ( ( role instanceof AgentRole) ? _agentRoleConditionTypePropertyMap : _spotRoleConditionTypePropertyMap));
	}

	/**
	 * @param kind
	 */
	public ConditionTabbedPane(String kind) {
		super(kind);
	}

	/**
	 * @param ruleTree
	 * @param role
	 * @param buddiesMap
	 * @param frame
	 * @param editRoleFrame
	 * @return
	 */
	public boolean setup(RuleTree ruleTree, Role role, Map<String, List<PanelRoot>> buddiesMap, Frame frame, EditRoleFrame editRoleFrame) {
		return super.setup( _conditionTypeOrder, ( ( role instanceof AgentRole) ? _agentRoleConditionTypePropertyMap : _spotRoleConditionTypePropertyMap), ruleTree, role, buddiesMap, frame, editRoleFrame);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane#create_genericPropertyPanel(soars.application.visualshell.object.role.base.edit.tab.generic.property.Property, soars.application.visualshell.object.role.base.Role, java.util.Map, int, java.awt.Frame, soars.application.visualshell.object.role.base.edit.EditRoleFrame)
	 */
	@Override
	protected GenericPropertyPanel create_genericPropertyPanel(Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, int index, Frame frame, EditRoleFrame editRoleFrame) {
		return new GenericConditionPropertyPanel( _kind, property, role, buddiesMap, index, frame, editRoleFrame);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane#setup_legacyPropertyPanel(java.lang.String, soars.application.visualshell.object.role.base.Role, java.awt.Frame, soars.application.visualshell.object.role.base.edit.EditRoleFrame)
	 */
	@Override
	protected boolean setup_legacyPropertyPanel(String type, Role role, Frame frame, EditRoleFrame editRoleFrame) {
		RulePropertyPanelBase defaultRulePropertyPanelBase = null;

		if ( type.equals( "spot")) {
			SpotConditionPropertyPanel spotConditionPropertyPanel = new SpotConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.spot"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.spot"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( spotConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = spotConditionPropertyPanel;
		} else if ( type.equals( "keyword")) {
			KeywordConditionPropertyPanel keywordConditionPropertyPanel = new KeywordConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.keyword"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.keyword"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.keyword"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( keywordConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = keywordConditionPropertyPanel;
		} else if ( type.equals( "time")) {
			TimeConditionPropertyPanel timeConditionPropertyPanel = new TimeConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.time"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.time"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.time"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( timeConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = timeConditionPropertyPanel;
		} else if ( type.equals( "isrole")) {
			if ( role instanceof AgentRole) {
				RoleConditionPropertyPanel roleConditionPropertyPanel = new RoleConditionPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.condition.role"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.condition.role"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.role"), "condition"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( roleConditionPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = roleConditionPropertyPanel;
			}
		} else if ( type.equals( "probability")) {
			ProbabilityConditionPropertyPanel probabilityConditionPropertyPanel = new ProbabilityConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.probability"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.probability"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.probability"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( probabilityConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = probabilityConditionPropertyPanel;
		} else if ( type.equals( "agentname")) {
			if ( role instanceof AgentRole) {
				SelectRuleValuePropertyPanel4 nameConditionPropertyPanel = new SelectRuleValuePropertyPanel4(
					"agent",
					ResourceManager.get_instance().get( "edit.rule.dialog.condition.agent.name"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.condition.agent.name"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.agent.name"), "condition"),
					role,
					_rulePropertyPanelBases.size(),
					ResourceManager.get_instance().get( "edit.rule.dialog.condition.agent.name.label"),
					false,
					frame, editRoleFrame);
				if ( !create( nameConditionPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = nameConditionPropertyPanel;
			}
		} else if ( type.equals( "spotname")) {
			if ( role instanceof SpotRole) {
				SelectRuleValuePropertyPanel4 nameConditionPropertyPanel = new SelectRuleValuePropertyPanel4(
					"spot",
					ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.name"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.condition.spot.name"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.spot.name"), "condition"),
					role,
					_rulePropertyPanelBases.size(),
					ResourceManager.get_instance().get( "edit.rule.dialog.condition.spot.name.label"),
					false,
					frame, editRoleFrame);
				if ( !create( nameConditionPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = nameConditionPropertyPanel;
			}
		} else if ( type.equals( "numberobject")) {
			NumberObjectConditionPropertyPanel numberObjectConditionPropertyPanel = new NumberObjectConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.number.object"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.number.object"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.number.object"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( numberObjectConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = numberObjectConditionPropertyPanel;
		} else if ( type.equals( "collection")) {
			CollectionAndListConditionPropertyPanel collectionConditionPropertyPanel = new CollectionAndListConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.collection"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.collection"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.collection"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( collectionConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = collectionConditionPropertyPanel;
		} else if ( type.equals( "list")) {
			CollectionAndListConditionPropertyPanel listConditionPropertyPanel1 = new CollectionAndListConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.inclusion.condition"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.list"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listConditionPropertyPanel1))
				return false;

			defaultRulePropertyPanelBase = listConditionPropertyPanel1;

			ListConditionPropertyPanel listConditionPropertyPanel2 = new ListConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.list.specified.condition"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.list"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listConditionPropertyPanel2))
				return false;

		} else if ( type.equals( "functionalobject")) {
			FunctionalObjectConditionPropertyPanel functionalObjectConditionPropertyPanel = new FunctionalObjectConditionPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.functional.object"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.functional.object"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.functional.object"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( functionalObjectConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = functionalObjectConditionPropertyPanel;
		} else if ( type.equals( "others")) {
			EditRuleValuePropertyPanel othersConditionPropertyPanel = new EditRuleValuePropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.others"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.condition.others"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.condition.others"), "condition"),
				role,
				_rulePropertyPanelBases.size(),
				ResourceManager.get_instance().get( "edit.rule.dialog.condition.other.label"),
				400,
				true,
				frame, editRoleFrame);
			if ( !create( othersConditionPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = othersConditionPropertyPanel;
		}

		if ( null == _defaultRulePropertyPanelBase && null != defaultRulePropertyPanelBase)
			_defaultRulePropertyPanelBase = defaultRulePropertyPanelBase;

		return true;
	}
}
