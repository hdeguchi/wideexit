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
import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.EditRoleFrame;
import soars.application.visualshell.object.role.base.edit.tab.base.PanelRoot;
import soars.application.visualshell.object.role.base.edit.tab.base.RulePropertyPanelBase;
import soars.application.visualshell.object.role.base.edit.tab.generic.base.GenericPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.command.GenericCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.generic.property.Property;
import soars.application.visualshell.object.role.base.edit.tab.legacy.base.SelectRuleValuePropertyPanel1;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.AttachDetachCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.CreateAgentCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.CreateObjectCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.ExTransferCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.FunctionalObjectCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.KeywordCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.MapCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.MoveCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.OthersCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.RoleCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.SetRoleCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.SpotVariableCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.SubstitutionCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.TimeCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.collection.CollectionCommandPropertyPanel1;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.collection.CollectionCommandPropertyPanel2;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.collection.CollectionCommandPropertyPanel3;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.equip.AgentEquipCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.equip.SpotEquipCommandPropertyPanel;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanel1;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanel2;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanel3;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanel4;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanel5;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.exchange_algebra.ExchangeAlgebraCommandPropertyPanel6;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel1;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel2;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel3;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel4;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel5;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel6;
import soars.application.visualshell.object.role.base.edit.tab.legacy.command.list.ListCommandPropertyPanel7;
import soars.application.visualshell.object.role.base.edit.table.data.CommonRuleData;
import soars.application.visualshell.object.role.base.edit.tree.RuleTree;
import soars.application.visualshell.object.role.base.object.generic.GenericRule;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.soars.environment.BasicEnvironment;

/**
 * @author kurata
 *
 */
public class CommandTabbedPane extends RuleTabbedPane {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	static protected List<String> _commandTypeOrder = null;

	/**
	 * 
	 */
	static Map<String, Property> _agentRoleCommandTypePropertyMap = null;

	/**
	 * 
	 */
	static Map<String, Property> _spotRoleCommandTypePropertyMap = null;

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

			_commandTypeOrder = new ArrayList<String>();

			List<String> orderFilenames = new ArrayList<String>();
			orderFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.command.order.resource.filename"), Constant._systemRuleScriptsRelativePathName));
			if ( null != userRuleScriptsFolder)
				orderFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.command.order.resource.filename"), userRuleScriptsFolder.getAbsolutePath()));
			else {
				if ( !projectFoldername.equals( ""))
					orderFilenames.add( projectFoldername + String.format( ResourceManager.get_instance().get( "generic.gui.rule.command.order.resource.filename"), Constant._userRuleScriptsExternalRelativePathName));
			}

			List<String> propertyFilenames = new ArrayList<String>();
			propertyFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.command.properties.resource.filename"), Constant._systemRuleScriptsRelativePathName, language));
			if ( null != userRuleScriptsFolder)
				propertyFilenames.add( String.format( ResourceManager.get_instance().get( "generic.gui.rule.command.properties.resource.filename"), userRuleScriptsFolder.getAbsolutePath(), language));
			else {
				if ( !projectFoldername.equals( ""))
					propertyFilenames.add( projectFoldername + String.format( ResourceManager.get_instance().get( "generic.gui.rule.command.properties.resource.filename"), Constant._userRuleScriptsExternalRelativePathName, language));
			}

			_agentRoleCommandTypePropertyMap = new HashMap<String, Property>();
			_spotRoleCommandTypePropertyMap = new HashMap<String, Property>();

			if ( !load( _commandTypeOrder, orderFilenames, _agentRoleCommandTypePropertyMap, _spotRoleCommandTypePropertyMap, propertyFilenames, false, "command"))
				return;
		}
	}

	/**
	 * @param genericRule
	 * @param role
	 * @return
	 */
	public static String get_method(GenericRule genericRule, Role role) {
		return get_method( genericRule, ( ( role instanceof AgentRole ) ? _agentRoleCommandTypePropertyMap : _spotRoleCommandTypePropertyMap));
	}

	/**
	 * @param genericRule
	 * @param role
	 * @return
	 */
	public static String get_name(GenericRule genericRule, Role role) {
		return get_name( genericRule, ( ( role instanceof AgentRole ) ? _agentRoleCommandTypePropertyMap : _spotRoleCommandTypePropertyMap));
	}

	/**
	 * @param genericRule
	 * @param role
	 * @return
	 */
	public static Color get_color(GenericRule genericRule, Role role) {
		return get_color( genericRule, ( ( role instanceof AgentRole ) ? _agentRoleCommandTypePropertyMap : _spotRoleCommandTypePropertyMap));
	}

	/**
	 * @param kind
	 */
	public CommandTabbedPane(String kind) {
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
		return super.setup( _commandTypeOrder, ( ( role instanceof AgentRole ) ? _agentRoleCommandTypePropertyMap : _spotRoleCommandTypePropertyMap), ruleTree, role, buddiesMap, frame, editRoleFrame);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane#create_genericPropertyPanel(soars.application.visualshell.object.role.base.edit.tab.generic.property.Property, soars.application.visualshell.object.role.base.Role, java.util.Map, int, java.awt.Frame, soars.application.visualshell.object.role.base.edit.EditRoleFrame)
	 */
	@Override
	protected GenericPropertyPanel create_genericPropertyPanel(Property property, Role role, Map<String, List<PanelRoot>> buddiesMap, int index, Frame frame, EditRoleFrame editRoleFrame) {
		return new GenericCommandPropertyPanel( _kind, property, role, buddiesMap, index, frame, editRoleFrame);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane#setup_legacyPropertyPanel(java.lang.String, soars.application.visualshell.object.role.base.Role, java.awt.Frame, soars.application.visualshell.object.role.base.edit.EditRoleFrame)
	 */
	@Override
	protected boolean setup_legacyPropertyPanel(String type, Role role, Frame frame, EditRoleFrame editRoleFrame) {
		RulePropertyPanelBase defaultRulePropertyPanelBase = null;

		if ( type.equals( "keyword")) {
			KeywordCommandPropertyPanel keywordCommandPropertyPanel = new KeywordCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.keyword"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.keyword"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.keyword"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( keywordCommandPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = keywordCommandPropertyPanel;
		} else if ( type.equals( "move")) {
			if ( role instanceof AgentRole) {
				MoveCommandPropertyPanel moveCommandPropertyPanel = new MoveCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.move"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.move"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.move"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( moveCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = moveCommandPropertyPanel;
			}
		} else if ( type.equals( "spotvariable")) {
			SpotVariableCommandPropertyPanel spotVariableCommandPropertyPanel = new SpotVariableCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.spot.variable"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.spot.variable"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.spot.variable"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( spotVariableCommandPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = spotVariableCommandPropertyPanel;
		} else if ( type.equals( "time")) {
			TimeCommandPropertyPanel timeCommandPropertyPanel = new TimeCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.time"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.time"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.time"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( timeCommandPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = timeCommandPropertyPanel;
		} else if ( type.equals( "role")) {
			if ( role instanceof AgentRole) {
				RoleCommandPropertyPanel roleCommandPropertyPanel = new RoleCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.role"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.role"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.role"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( roleCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = roleCommandPropertyPanel;
			} else {
				SelectRuleValuePropertyPanel1 roleCommandPropertyPanel = new SelectRuleValuePropertyPanel1(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.role"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.role"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.role"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					ResourceManager.get_instance().get( "edit.rule.dialog.command.role.label"),
					true,
					frame, editRoleFrame);
				if ( !create( roleCommandPropertyPanel))
					return false;

					roleCommandPropertyPanel.setup( RulePropertyPanelBase.get_spot_role_names( true));

					defaultRulePropertyPanelBase = roleCommandPropertyPanel;
			}
		} else if ( type.equals( "setrole")) {
			if ( role instanceof AgentRole) {
				SetRoleCommandPropertyPanel setRoleCommandPropertyPanel = new SetRoleCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.set.role"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.set.role"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.set.role"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( setRoleCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = setRoleCommandPropertyPanel;
			}
		} else if ( type.equals( "substitution")) {
			SubstitutionCommandPropertyPanel substitutionCommandPropertyPanel = new SubstitutionCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.substitution"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.substitution"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.substitution"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( substitutionCommandPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = substitutionCommandPropertyPanel;
		} else if ( type.equals( "collection")) {
			CollectionCommandPropertyPanel1 collectionCommandPropertyPanel1 = new CollectionCommandPropertyPanel1(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.add"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.collection"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( collectionCommandPropertyPanel1, ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
				return false;

			defaultRulePropertyPanelBase = collectionCommandPropertyPanel1;

			CollectionCommandPropertyPanel2 collectionCommandPropertyPanel2 = new CollectionCommandPropertyPanel2(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.remove"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.collection"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( collectionCommandPropertyPanel2, ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
				return false;

			CollectionCommandPropertyPanel3 collectionCommandPropertyPanel3 = new CollectionCommandPropertyPanel3(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.collection.others"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.collection"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.collection"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( collectionCommandPropertyPanel3, ResourceManager.get_instance().get( "edit.rule.dialog.command.collection")))
				return false;

		} else if ( type.equals( "list")) {
			ListCommandPropertyPanel1 listCommandPropertyPanel1 = new ListCommandPropertyPanel1(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.first"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel1, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

			defaultRulePropertyPanelBase = listCommandPropertyPanel1;

			ListCommandPropertyPanel2 listCommandPropertyPanel2 = new ListCommandPropertyPanel2(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.add.last"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel2, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

			ListCommandPropertyPanel3 listCommandPropertyPanel3 = new ListCommandPropertyPanel3(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.get"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel3, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

			ListCommandPropertyPanel4 listCommandPropertyPanel4 = new ListCommandPropertyPanel4(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel4, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

			ListCommandPropertyPanel5 listCommandPropertyPanel5 = new ListCommandPropertyPanel5(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.remove.union"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel5, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

			ListCommandPropertyPanel6 listCommandPropertyPanel6 = new ListCommandPropertyPanel6(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.order.rotate"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel6, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

			ListCommandPropertyPanel7 listCommandPropertyPanel7 = new ListCommandPropertyPanel7(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.list.move.others"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.list"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.list"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( listCommandPropertyPanel7, ResourceManager.get_instance().get( "edit.rule.dialog.command.list")))
				return false;

		} else if ( type.equals( "getequip")) {
		//} else if ( type.equals( "putequip")) {
			if ( role instanceof AgentRole) {
				AgentEquipCommandPropertyPanel agentEquipCommandPropertyPanel = new AgentEquipCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.agent.equip"),
					_kind,
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.get.equip"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( agentEquipCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = agentEquipCommandPropertyPanel;
			}
			if ( role instanceof SpotRole) {
				SpotEquipCommandPropertyPanel spotEquipCommandPropertyPanel = new SpotEquipCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.spot.equip"),
					_kind,
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.get.equip"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( spotEquipCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = spotEquipCommandPropertyPanel;
			}
		} else if ( type.equals( "createagent")) {
			CreateAgentCommandPropertyPanel createAgentCommandPropertyPanel = new CreateAgentCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.create.agent"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.create.agent"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.create.agent"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( createAgentCommandPropertyPanel, ResourceManager.get_instance().get( "edit.rule.dialog.command.dynamic.creation")))
				return false;

			defaultRulePropertyPanelBase = createAgentCommandPropertyPanel;
		} else if ( type.equals( "createobject")) {
			CreateObjectCommandPropertyPanel createObjectCommandPropertyPanel = new CreateObjectCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.create.object"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.create.object"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.create.object"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( createObjectCommandPropertyPanel, ResourceManager.get_instance().get( "edit.rule.dialog.command.dynamic.creation")))
				return false;

			defaultRulePropertyPanelBase = createObjectCommandPropertyPanel;
		} else if ( type.equals( "attach")) {
		//} else if ( type.equals( "detach")) {
			if ( role instanceof AgentRole) {
				AttachDetachCommandPropertyPanel attachDetachCommandPropertyPanel = new AttachDetachCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.attach.detach"),
					_kind,
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.attach"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( attachDetachCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = attachDetachCommandPropertyPanel;
			}
		} else if ( type.equals( "functionalobject")) {
			if ( Environment.get_instance().is_functional_object_enable()) {
				FunctionalObjectCommandPropertyPanel functionalObjectCommandPropertyPanel = new FunctionalObjectCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.functional.object"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.functional.object"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.functional.object"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( functionalObjectCommandPropertyPanel))
					return false;

				defaultRulePropertyPanelBase = functionalObjectCommandPropertyPanel;
			}
		} else if ( type.equals( "map")) {
			MapCommandPropertyPanel mapCommandPropertyPanel = new MapCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.map"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.map"),
				CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.map"), "command"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( mapCommandPropertyPanel))
				return false;

			defaultRulePropertyPanelBase = mapCommandPropertyPanel;
		} else if ( type.equals( "exchangealgebra")) {
			if ( Environment.get_instance().is_exchange_algebra_enable()) {
				ExchangeAlgebraCommandPropertyPanel1 exchangeAlgebraCommandPropertyPanel1 = new ExchangeAlgebraCommandPropertyPanel1(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.projection"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exchangeAlgebraCommandPropertyPanel1, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

				defaultRulePropertyPanelBase = exchangeAlgebraCommandPropertyPanel1;

				ExchangeAlgebraCommandPropertyPanel2 exchangeAlgebraCommandPropertyPanel2 = new ExchangeAlgebraCommandPropertyPanel2(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.plus"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exchangeAlgebraCommandPropertyPanel2, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

				ExchangeAlgebraCommandPropertyPanel3 exchangeAlgebraCommandPropertyPanel3 = new ExchangeAlgebraCommandPropertyPanel3(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.operatons"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exchangeAlgebraCommandPropertyPanel3, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

				ExchangeAlgebraCommandPropertyPanel4 exchangeAlgebraCommandPropertyPanel4 = new ExchangeAlgebraCommandPropertyPanel4(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.norm"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exchangeAlgebraCommandPropertyPanel4, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

				ExchangeAlgebraCommandPropertyPanel5 exchangeAlgebraCommandPropertyPanel5 = new ExchangeAlgebraCommandPropertyPanel5(
					ResourceManager.get_instance().get( ( role instanceof AgentRole)
						? "edit.rule.dialog.command.exchange.algebra.agent.equip"
						: "edit.rule.dialog.command.exchange.algebra.spot.equip"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exchangeAlgebraCommandPropertyPanel5, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

				ExchangeAlgebraCommandPropertyPanel6 exchangeAlgebraCommandPropertyPanel6 = new ExchangeAlgebraCommandPropertyPanel6(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra.dynamic.creation"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.exchange.algebra"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exchangeAlgebraCommandPropertyPanel6, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

			}
		} else if ( type.equals( "extransfer")) {
			if ( Environment.get_instance().is_exchange_algebra_enable() && Environment.get_instance().is_extransfer_enable()) {
				ExTransferCommandPropertyPanel exTransferCommandPropertyPanel = new ExTransferCommandPropertyPanel(
					ResourceManager.get_instance().get( "edit.rule.dialog.command.extransfer"),
					_kind,
					ResourceManager.get_instance().get( "rule.type.command.extransfer"),
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.extransfer"), "command"),
					role,
					_rulePropertyPanelBases.size(),
					frame, editRoleFrame);
				if ( !create( exTransferCommandPropertyPanel, ResourceManager.get_instance().get( "edit.rule.dialog.command.exchange.algebra")))
					return false;

				defaultRulePropertyPanelBase = exTransferCommandPropertyPanel;
			}
		//} else if ( type.equals( "nextstage")) {
		//} else if ( type.equals( "terminate")) {
		//} else if ( type.equals( "trace")) {
		} else if ( type.equals( "others")) {
			OthersCommandPropertyPanel othersCommandPropertyPanel = new OthersCommandPropertyPanel(
				ResourceManager.get_instance().get( "edit.rule.dialog.command.others"),
				_kind,
				ResourceManager.get_instance().get( "rule.type.command.others"),
				role,
				_rulePropertyPanelBases.size(),
				frame, editRoleFrame);
			if ( !create( othersCommandPropertyPanel,
					CommonRuleData.get_color( ResourceManager.get_instance().get( "rule.type.command.others"), "command")))
				return false;

			defaultRulePropertyPanelBase = othersCommandPropertyPanel;
		}

		if ( null == _defaultRulePropertyPanelBase && null != defaultRulePropertyPanelBase)
			_defaultRulePropertyPanelBase = defaultRulePropertyPanelBase;

		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane#on_update_stage()
	 */
	@Override
	public void on_update_stage() {
		super.on_update_stage();

		RulePropertyPanelBase rulePropertyPanelBase = get( ResourceManager.get_instance().get( "rule.type.command.others"));
		if ( null != rulePropertyPanelBase) {
			OthersCommandPropertyPanel othersCommandPropertyPanel = ( OthersCommandPropertyPanel)rulePropertyPanelBase;
			othersCommandPropertyPanel.on_update_stage();
		}
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.edit.tab.RuleTabbedPane#update_stage_name(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = super.update_stage_name(newName, originalName);

		RulePropertyPanelBase rulePropertyPanelBase = get( ResourceManager.get_instance().get( "rule.type.command.others"));
		if ( null != rulePropertyPanelBase) {
			OthersCommandPropertyPanel othersCommandPropertyPanel = ( OthersCommandPropertyPanel)rulePropertyPanelBase;
			if ( othersCommandPropertyPanel.update_stage_name( newName, originalName))
				result = true;
		}

		return result;
	}
}
