/**
 * 
 */
package soars.application.visualshell.object.role.base.object.base;

import java.util.Vector;

import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.object.Rules;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulatorNew;
import soars.common.soars.warning.WarningManager;

/**
 * @author kurata
 *
 */
public class RuleNew extends Rule {

	/**
	 * @param kind
	 * @param type
	 * @param value
	 */
	public RuleNew(String kind, String type, String value) {
		super(kind, type, value);
	}

	/**
	 * @param ruleNew
	 */
	public RuleNew(RuleNew ruleNew) {
		super(ruleNew);
	}

	/**
	 * @param kind
	 * @param column
	 * @param type
	 * @param or
	 */
	public RuleNew(String kind, int column, String type, boolean or) {
		super(kind, column, type, or);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#can_remove(int, java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Vector, soars.application.visualshell.object.role.base.object.Rules, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean can_remove(int row, String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		// TODO 移行が全て完了したらこれは不要になる
		return true;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.object.role.base.object.base.Rule#can_remove(int, java.lang.String, java.lang.String, java.lang.String, boolean, java.lang.String, java.util.Vector, soars.application.visualshell.object.role.base.object.Rules, soars.application.visualshell.object.role.base.Role)
	 */
	@Override
	public boolean can_remove(int row, String entityType, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		// TODO これからはこちらに移行してゆく
		if ( kind.equals( "spot variable"))
			return can_remove_spot_variable_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "probability"))
			return can_remove_probability_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "collection"))
			return can_remove_collection_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "list"))
			return can_remove_list_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "map"))
			return can_remove_map_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "keyword"))
			return can_remove_keyword_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "number object"))
			return can_remove_number_object_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "role variable"))
			return can_remove_role_variable_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "time variable"))
			return can_remove_time_variable_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "class variable"))
			return can_remove_class_variable_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "file"))
			return can_remove_file_name( row, objectName, entityType, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else if ( kind.equals( "exchange algebra"))
			return can_remove_exchange_algebra_name( row, entityType, objectName, otherEntitiesHaveThisObjectName, headName, ranges, rules, role);
		else
			return true;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param spotVariableName
	 * @param otherEntitiesHaveThisSpotVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_spot_variable_name(int row, String entityType, String spotVariableName, boolean otherEntitiesHaveThisSpotVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedSpotVariableNames[] = get_used_spot_variable_names( role);
		if ( null == usedSpotVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedSpotVariableNames.length; ++i) {
			if ( null == usedSpotVariableNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedSpotVariableNames[ i], entityType, spotVariableName, otherEntitiesHaveThisSpotVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"spot variable = " + usedSpotVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;

	}

	/**
	 * @param row
	 * @param entityType
	 * @param probabilityName
	 * @param otherEntitiesHaveThisProbabilityName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_probability_name(int row, String entityType, String probabilityName, boolean otherEntitiesHaveThisProbabilityName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedProbabilityNames[] = get_used_probability_names();
		if ( null == usedProbabilityNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedProbabilityNames.length; ++i) {
			if ( null == usedProbabilityNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedProbabilityNames[ i], entityType, probabilityName, otherEntitiesHaveThisProbabilityName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"probability = " + usedProbabilityNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param collectionName
	 * @param otherEntitiesHaveThisCollectionName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_collection_name(int row, String entityType, String collectionName, boolean otherEntitiesHaveThisCollectionName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedCollectionNames[] = get_used_collection_names();
		if ( null == usedCollectionNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedCollectionNames.length; ++i) {
			if ( null == usedCollectionNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedCollectionNames[ i], entityType, collectionName, otherEntitiesHaveThisCollectionName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"collection = " + usedCollectionNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param listName
	 * @param otherEntitiesHaveThisListName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_list_name(int row, String entityType, String listName, boolean otherEntitiesHaveThisListName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedListNames[] = get_used_list_names();
		if ( null == usedListNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedListNames.length; ++i) {
			if ( null == usedListNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedListNames[ i], entityType, listName, otherEntitiesHaveThisListName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"list = " + usedListNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param mapMame
	 * @param otherEntitiesHaveThisMapName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_map_name(int row, String entityType, String mapMame, boolean otherEntitiesHaveThisMapName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedMapNames[] = get_used_map_names();
		if ( null == usedMapNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedMapNames.length; ++i) {
			if ( null == usedMapNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedMapNames[ i], entityType, mapMame, otherEntitiesHaveThisMapName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"map = " + usedMapNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param keywordName
	 * @param otherEntitiesHaveThisKeywordName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_keyword_name(int row, String entityType, String keywordName, boolean otherEntitiesHaveThisKeywordName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedKeywordNames[] = get_used_keyword_names();
		if ( null == usedKeywordNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedKeywordNames.length; ++i) {
			if ( null == usedKeywordNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedKeywordNames[ i], entityType, keywordName, otherEntitiesHaveThisKeywordName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"keyword = " + usedKeywordNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param numberObjectName
	 * @param otherEntitiesHaveThisNumberObjectName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_number_object_name(int row, String entityType, String numberObjectName, boolean otherEntitiesHaveThisNumberObjectName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedNumberObjectNames[] = get_used_number_object_names();
		if ( null == usedNumberObjectNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedNumberObjectNames.length; ++i) {
			if ( null == usedNumberObjectNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedNumberObjectNames[ i], entityType, numberObjectName, otherEntitiesHaveThisNumberObjectName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"number object = " + usedNumberObjectNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param roleVariableName
	 * @param otherEntitiesHaveThisRoleVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_role_variable_name(int row, String entityType, String roleVariableName, boolean otherEntitiesHaveThisRoleVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedRoleVariableNames[] = get_used_role_variable_names();
		if ( null == usedRoleVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedRoleVariableNames.length; ++i) {
			if ( null == usedRoleVariableNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedRoleVariableNames[ i], entityType, roleVariableName, otherEntitiesHaveThisRoleVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"role variable = " + usedRoleVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param timeVariableName
	 * @param otherEntitiesHaveThisTimeVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_time_variable_name(int row, String entityType, String timeVariableName, boolean otherEntitiesHaveThisTimeVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String usedTimeVariableNames[] = get_used_time_variable_names();
		if ( null == usedTimeVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedTimeVariableNames.length; ++i) {
			if ( null == usedTimeVariableNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedTimeVariableNames[ i], entityType, timeVariableName, otherEntitiesHaveThisTimeVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"time variable = " + usedTimeVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param objectName
	 * @param otherEntitiesHaveThisClassVariableName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_class_variable_name(int row, String entityType, String classVariableName, boolean otherEntitiesHaveThisClassVariableName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String[] usedClassVariableNames = get_used_class_variable_names( role);
		if ( null == usedClassVariableNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedClassVariableNames.length; ++i) {
			if ( null == usedClassVariableNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedClassVariableNames[ i], entityType, classVariableName, otherEntitiesHaveThisClassVariableName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"class variable = " + usedClassVariableNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param fileName
	 * @param otherEntitiesHaveThisFileName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_file_name(int row, String entityType, String fileName, boolean otherEntitiesHaveThisFileName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String[] usedFileNames = get_used_file_names();
		if ( null == usedFileNames)
			return true;

		boolean result = true;

		for ( int i = 0; i < usedFileNames.length; ++i) {
			if ( null == usedFileNames[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( usedFileNames[ i], entityType, fileName, otherEntitiesHaveThisFileName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"file = " + usedFileNames[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}

	/**
	 * @param row
	 * @param entityType
	 * @param exchangeAlgebraName
	 * @param otherEntitiesHaveThisExchangeAlgebraName
	 * @param headName
	 * @param ranges
	 * @param rules
	 * @param role
	 * @return
	 */
	private boolean can_remove_exchange_algebra_name(int row, String entityType, String exchangeAlgebraName, boolean otherEntitiesHaveThisExchangeAlgebraName, String headName, Vector<String[]> ranges, Rules rules, Role role) {
		String used_exchange_algebra_names[] = get_used_exchange_algebra_names( role);
		if ( null == used_exchange_algebra_names)
			return true;

		boolean result = true;

		for ( int i = 0; i < used_exchange_algebra_names.length; ++i) {
			if ( null == used_exchange_algebra_names[ i])
				continue;

			if ( !CommonRuleManipulatorNew.correspond( used_exchange_algebra_names[ i], entityType, exchangeAlgebraName, otherEntitiesHaveThisExchangeAlgebraName, headName, ranges))
				continue;

			String[] message = new String[] {
				"Role",
				"name = " + role._name,
				"type = " + _kind + "::" + _type,
				"exchange algebra = " + used_exchange_algebra_names[ i],
				"row = " + ( row + 1),
				"column = " + ( _column + 1)
			};
			WarningManager.get_instance().add( message);
			result = false;
		}
		return result;
	}
}
