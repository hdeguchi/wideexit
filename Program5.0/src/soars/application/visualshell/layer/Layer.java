/*
 * 2005/04/25
 */
package soars.application.visualshell.layer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.visualshell.file.importer.initial.base.DataBase;
import soars.application.visualshell.main.Constant;
import soars.application.visualshell.object.base.DrawObject;
import soars.application.visualshell.object.chart.ChartObject;
import soars.application.visualshell.object.entity.agent.AgentObject;
import soars.application.visualshell.object.entity.base.EntityBase;
import soars.application.visualshell.object.entity.base.object.class_variable.ClassVariableObject;
import soars.application.visualshell.object.entity.spot.SpotObject;
import soars.application.visualshell.object.expression.VisualShellExpressionManager;
import soars.application.visualshell.object.role.agent.AgentRole;
import soars.application.visualshell.object.role.base.Role;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectInObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectObject;
import soars.application.visualshell.object.role.base.edit.inheritance.ConnectOutObject;
import soars.application.visualshell.object.role.base.edit.tab.legacy.common.functional_object.FunctionalObject;
import soars.application.visualshell.object.role.base.object.common.CommonRuleManipulator;
import soars.application.visualshell.object.role.spot.SpotRole;
import soars.common.soars.tool.SoarsCommonTool;
import soars.common.soars.warning.WarningManager;
import soars.common.utility.tool.expression.Expression;
import soars.common.utility.xml.sax.Writer;

/**
 * Manages the objects(agent, spot, agent role, spot role, chart object).
 * @author kurata / SOARS project
 */
public class Layer extends Vector<DrawObject> implements ILayerManipulator {

	/**
	 * Name of this layer.
	 */
	public String _name = "";

	/**
	 * 
	 */
	private TreeMap<String, DrawObject> _agentMap = new TreeMap<String, DrawObject>();

	/**
	 * 
	 */
	private TreeMap<String, DrawObject> _spotMap = new TreeMap<String, DrawObject>();

	/**
	 * 
	 */
	private TreeMap<String, DrawObject> _agentRoleMap = new TreeMap<String, DrawObject>();

	/**
	 * 
	 */
	private TreeMap<String, DrawObject> _spotRoleMap = new TreeMap<String, DrawObject>();

	/**
	 * 
	 */
	private Dimension _preferredSize = new Dimension();

	/**
	 * 
	 */
	private Rectangle _visibleRectangle = new Rectangle();

	/**
	 * Creates this object.
	 */
	public Layer() {
		super();
	}

	/**
	 * Creates this object with the specified data.
	 * @param name the name of this layer
	 */
	public Layer(String name) {
		super();
		_name = name;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		for ( DrawObject drawObject:this)
			drawObject.cleanup();

		clear();

		_agentMap.clear();
		_spotMap.clear();
		_agentRoleMap.clear();
		_spotRoleMap.clear();

		_preferredSize.setSize( 0, 0);
		_visibleRectangle.setBounds( 0, 0, 0, 0);
	}

	/**
	 * Invoked when this layer is active.
	 * @param previousLayer the previous active layer
	 * @param component the base class for all Swing components
	 */
	public void on_enter(Layer previousLayer, JComponent component) {
		update_preferred_size( previousLayer, component);
		component.scrollRectToVisible( _visibleRectangle);
	}

	/**
	 * Invoked when this layer is inactive.
	 * @param component the base class for all Swing components
	 */
	public void on_leave(JComponent component) {
		select_all( false);
		component.computeVisibleRect( _visibleRectangle);
	}

	/**
	 * Updates the size of this layer.
	 * @param component the base class for all Swing components
	 */
	public void update_preferred_size(JComponent component) {
		Dimension preferredSize = new Dimension();
		get_preferred_size( preferredSize);
		if ( !_preferredSize.equals( preferredSize)) {
			_preferredSize.setSize( preferredSize);
			component.setPreferredSize( _preferredSize);
			component.updateUI();
		}
	}

	/**
	 * Updates the size of this layer.
	 * @param previousLayer the previous active layer
	 * @param component the base class for all Swing components
	 */
	public void update_preferred_size(Layer previousLayer, JComponent component) {
		if ( !_preferredSize.equals( previousLayer._preferredSize)) {
			component.setPreferredSize( _preferredSize);
			component.updateUI();
		}
	}

	/**
	 * @param preferredSize
	 */
	private void get_preferred_size(Dimension preferredSize) {
		for ( DrawObject drawObject:this)
			drawObject.get_preferred_size( preferredSize);
	}

	/**
	 * @param gis
	 * @return
	 */
	public boolean is_unique_gis_id(String gis) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase._gis.equals( gis))
				return false;
		}
		return true;
	}

	/**
	 * Returns true if the specified id is unique in this layer.
	 * @param id the specified id
	 * @return true if the specified id is unique in this layer
	 */
	public boolean is_unique(int id) {
		for ( int i = size() - 1; i >= 0; --i) {
			//if ( get( i)._id == id)
			if ( !get( i).is_unique( id))
				return false;
		}
		return true;
	}

	/**
	 * Returns the number of the objects whose type is the specified one in this layer.
	 * @param type the specified type
	 * @return the number of the objects whose type is the specified one in this layer
	 */
	public int how_many(String type) {
		int counter = 0;
		for ( DrawObject drawObject:this) {
			//if ( ( type.equals( "objectbase") && ( drawObject instanceof EntityBase))
			if ( ( type.equals( "entitybase") && ( drawObject instanceof EntityBase))
				|| ( type.equals( "agent") && ( drawObject instanceof AgentObject))
				|| ( type.equals( "spot") && ( drawObject instanceof SpotObject))
				|| ( type.equals( "role") && ( drawObject instanceof Role))
				|| ( type.equals( "agent role") && ( drawObject instanceof AgentRole))
				|| ( type.equals( "spot role") && ( drawObject instanceof SpotRole))
				|| ( type.equals( "chart") && ( drawObject instanceof ChartObject)))
				++counter;
		}
		return counter;
	}

	/**
	 * Returns the agent or spot object which has the specified name.
	 * @param fullName the specified name
	 * @return the agent or spot object which has the specified name
	 */
	public EntityBase get_entityBase_has_this_name(String fullName) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase && drawObject.has_same_name( fullName))
				return ( EntityBase)drawObject;
		}
		return null;
	}

	/**
	 * Returns the agent object which has the specified name.
	 * @param name the specified name
	 * @return the agent object which has the specified name
	 */
	public AgentObject get_agent(String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject && drawObject._name.equals( name))
				return ( AgentObject)drawObject;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayer#get_agent_has_this_name(java.lang.String)
	 */
	public AgentObject get_agent_has_this_name(String fullName) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject && drawObject.has_same_name( fullName))
				return ( AgentObject)drawObject;
		}
		return null;
	}

	/**
	 * Returns the spot object which has the specified name.
	 * @param name the specified name
	 * @return the spot object which has the specified name
	 */
	public SpotObject get_spot(String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject && drawObject._name.equals( name))
				return ( SpotObject)drawObject;
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayer#get_spot_has_this_name(java.lang.String)
	 */
	public SpotObject get_spot_has_this_name(String fullName) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject && drawObject.has_same_name( fullName))
				return ( SpotObject)drawObject;
		}
		return null;
	}

	/**
	 * Returns the role object which has the specified name.
	 * @param name the specified name
	 * @return the role object which has the specified name
	 */
	public Role get_role(String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof Role && drawObject._name.equals( name))
				return ( Role)drawObject;
		}
		return null;
	}

	/**
	 * Returns the agent role object which has the specified name.
	 * @param name the specified name
	 * @return the agent role object which has the specified name
	 */
	public AgentRole get_agent_role(String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentRole && drawObject._name.equals( name))
				return ( AgentRole)drawObject;
		}
		return null;
	}

	/**
	 * Returns the spot role object which has the specified name.
	 * @param name the specified name
	 * @return the spot role object which has the specified name
	 */
	public SpotRole get_spot_role(String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotRole && drawObject._name.equals( name))
				return ( SpotRole)drawObject;
		}
		return null;
	}

	/**
	 * Returns the chart object which has the specified name.
	 * @param name the specified name
	 * @return the chart object which has the specified name
	 */
	public ChartObject get_chart(String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof ChartObject && drawObject._name.equals( name))
				return ( ChartObject)drawObject;
		}
		return null;
	}

	/**
	 * @param entity
	 * @param numberObjectName
	 * @param newType
	 * @return
	 */
	public boolean is_number_object_type_correct(String entity, String numberObjectName, String newType) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( !role.is_number_object_type_correct( entity, numberObjectName, newType))
					return false;
			}
		}
		return true;
	}

	/**
	 * Returns true if the number variable is correct.
	 * @param type the object type("agent" or "spot")
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @param entityBase the object(agent object or spot object)
	 * @return true if the number variable is correct
	 */
	public boolean is_number_object_correct(String type, String numberObjectName, String numberObjectType, EntityBase entityBase) {
		for ( DrawObject drawObject:this) {
			if ( type.equals( "agent") && !( drawObject instanceof AgentObject))
				continue;

			if ( type.equals( "spot") && !( drawObject instanceof SpotObject))
				continue;

			EntityBase pb = ( EntityBase)drawObject;
			if ( pb.equals( entityBase))
				continue;

			if ( !pb.is_number_object_correct( type, numberObjectName, numberObjectType))
				return false;
		}
		return true;
	}

	/**
	 * Returns true for updating the number variable type successfully.
	 * @param type the object type("agent" or "spot")
	 * @param numberObjectName the number variable name
	 * @param numberObjectType the number variable type
	 * @param entityBase the object(agent object or spot object)
	 * @return true for updating the number variable type successfully
	 */
	public boolean update_number_object_type(String type, String numberObjectName, String numberObjectType, EntityBase entityBase) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( type.equals( "agent") && !( drawObject instanceof AgentObject))
				continue;

			if ( type.equals( "spot") && !( drawObject instanceof SpotObject))
				continue;

			EntityBase pb = ( EntityBase)drawObject;
			if ( pb.equals( entityBase))
				continue;

			if ( pb.update_number_object_type( type, numberObjectName, numberObjectType)) {
				String[] message = new String[] {
					( ( pb instanceof AgentObject) ? "Agent : " : "Spot : ") + pb._name
				};

				WarningManager.get_instance().add( message);

				result = true;
			}
		}
		return result;
	}

	/**
	 * Returns true for appending the new object created with the specified data.
	 * @param type the specified type
	 * @param id the specified id
	 * @param name the specified name
	 * @param point the specified position
	 * @param component the base class for all Swing components
	 * @return true for appending the new object created with the specified data
	 */
	public boolean append_object(String type, int id, String name, Point point, JComponent component) {
		DrawObject drawObject = DrawObject.create( type, id, name, point, ( Graphics2D)component.getGraphics());
		if ( null == drawObject)
			return false;

		append_object( drawObject);

		update_preferred_size( component);

		return true;
	}

	/**
	 * Returns true for appending the new object created with the specified data.
	 * @param type the specified type
	 * @param component the base class for all Swing components
	 * @return true for appending the new object created with the specified data
	 */
	public DrawObject append_global_object(String type, JComponent component) {
		DrawObject drawObject = DrawObject.create( type, ( Graphics2D)component.getGraphics());
		if ( null == drawObject)
			return null;

		append_object( drawObject);

		update_preferred_size( component);

		return drawObject;
	}

	/**
	 * Returns true for appending the new object created with the specified data.
	 * @param type the specified type
	 * @param id the specified id
	 * @param name the specified name
	 * @param point the specified position
	 * @param component the base class for all Swing components
	 * @return the new object created with the specified data
	 */
	public DrawObject append_object2(String type, int id, String name, Point point, JComponent component) {
		DrawObject drawObject = DrawObject.create( type, id, name, point, ( Graphics2D)component.getGraphics());
		if ( null == drawObject)
			return null;

		append_object( drawObject);

		return drawObject;
	}

	/**
	 * Appends the specified objects.
	 * @param drawObjects the array of the specified objects
	 */
	public void append_object(Vector<DrawObject> drawObjects) {
		for ( int i = drawObjects.size() - 1; i >= 0; --i)
			append_object( drawObjects.get( i));
	}

	/**
	 * Appends the new object created with the specified data, and returns it.
	 * @param type the specified type
	 * @param id the specified id
	 * @param dataBase the specified object data
	 * @param component the base class for all Swing components
	 * @return the new object
	 */
	public DrawObject append_object(String type, int id, DataBase dataBase, JComponent component) {
		DrawObject drawObject = DrawObject.create( type, id, dataBase._name, dataBase._position, ( Graphics2D)component.getGraphics());
		if ( null == drawObject)
			return null;

		append_object( drawObject);

		update_preferred_size( component);

		return drawObject;
	}

	/**
	 * Appends the specified object to this layer.
	 * @param drawObject the specified object
	 */
	public void append_object(DrawObject drawObject) {
		insertElementAt( drawObject, 0);

		if ( drawObject instanceof AgentObject)
			_agentMap.put( drawObject._name, drawObject);
		else if ( drawObject instanceof SpotObject)
			_spotMap.put( drawObject._name, drawObject);
		else if ( drawObject instanceof AgentRole)
			_agentRoleMap.put( drawObject._name, drawObject);
		else if ( drawObject instanceof SpotRole)
			_spotRoleMap.put( drawObject._name, drawObject);
	}

	/**
	 * Returns true if this layer contains the specified object.
	 * @param type the specified type
	 * @param fullName the specified name
	 * @return true if this layer contains the specified object
	 */
	public boolean contains(String type, String fullName) {
		for ( DrawObject drawObject:this) {
			if ( type.equals( "agent") || type.equals( "spot")) {
				if ( drawObject instanceof EntityBase) {
					EntityBase entityBase = ( EntityBase)drawObject;
					if ( entityBase.has_same_name( fullName)
						/*|| entityBase._name.equals( full_name)*/)
						return true;
//				} else if ( drawObject instanceof Role) {
//					Role role = ( Role)drawObject;
//					if ( role.has_same_agent_name( full_name, ""))
//						return true;
				} else if ( drawObject instanceof ChartObject) {
					ChartObject chartObject = ( ChartObject)drawObject;
					if ( chartObject._name.equals( fullName))
						return true;
				}
			} else if ( type.equals( "chart")) {
				if ( drawObject instanceof EntityBase) {
					EntityBase entityBase = ( EntityBase)drawObject;
					if ( entityBase.has_same_name( fullName)
						/*|| entityBase._name.equals( full_name)*/)
						return true;
				} else if ( drawObject instanceof Role) {
					Role role = ( Role)drawObject;
					if ( role._name.equals( fullName)
						|| role.has_same_agent_name( fullName, ""))
						return true;
				} else if ( drawObject instanceof ChartObject) {
					ChartObject chartObject = ( ChartObject)drawObject;
					if ( chartObject._name.equals( fullName))
						return true;
				}
			} else {
				if ( drawObject.is_same_name( type, fullName))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the specified object.
	 * @param type the specified type
	 * @param name the specified name
	 * @param number the specified number
	 * @param exception the exception name
	 * @return true if this layer contains the specified object
	 */
	public boolean contains(String type, String name, String number, String exception) {
		for ( DrawObject drawObject:this) {
			if ( drawObject._name.equals( exception))
				continue;

			if ( type.equals( "agent") || type.equals( "spot")) {
				if ( drawObject instanceof EntityBase) {
					EntityBase entityBase = ( EntityBase)drawObject;
					if ( entityBase.has_same_name( name, number))
						return true;
//				} else if ( drawObject instanceof Role) {
//					Role role = ( Role)drawObject;
//					if ( role.has_same_agent_name( name, number))
//						return true;
				} else if ( drawObject instanceof ChartObject) {
					ChartObject chartObject = ( ChartObject)drawObject;
					if ( SoarsCommonTool.has_same_name( name, number, chartObject._name))
						return true;
				}
//			} else {
//				if ( drawObject.is_same_name( type, full_name))
//					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the object which has the specified data exists.
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if the object which has the specified data exists
	 */
	public boolean exist(String type, String name) {
		if ( type.equals( "agent"))
			return ( ( null == _agentMap.get( name)) ? false : true);
		else if ( type.equals( "spot"))
			return ( ( null == _spotMap.get( name)) ? false : true);
		else
			return ( ( null == _agentRoleMap.get( name) && null == _spotRoleMap.get( name)) ? false : true);
//		for ( DrawObject drawObject:this) {
//			if ( type.equals( "agent") && ( drawObject instanceof AgentObject)) {
//				AgentObject agentObject = ( AgentObject)drawObject;
//				if ( agentObject._name.equals( name))
//					return true;
//			} else if ( type.equals( "spot") 	&& ( drawObject instanceof SpotObject)) {
//				SpotObject spotObject = ( SpotObject)drawObject;
//				if ( spotObject._name.equals( name))
//					return true;
//			} else {
//				if ( drawObject.is_same_name( type, name))
//					return true;
//			}
//		}
//		return false;
	}

	/**
	 * Returns true if this layer contains the object which has the specified data except the specified one.
	 * @param entityBase the specified object
	 * @param type the specified type
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this layer contains the object which has the specified data except the specified one
	 */
	public boolean contains(EntityBase entityBase, String type, String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase pb = ( EntityBase)drawObject;
				if ( !pb.equals( entityBase)
					&& ( pb.is_same_name( type, name) || pb.has_same_name( name, number)/*pb.has_same_name_with_type( type, name, number)*/))
					return true;
//			} else if ( drawObject instanceof Role) {
//				Role role = ( Role)drawObject;
//				if ( role.is_same_name( type, name) || role.has_same_name( names))
//					return true;
			}
		}
		return false;
	}

	/**
	 * @param entityBase
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains(EntityBase entityBase, String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase pb = ( EntityBase)drawObject;
				if ( !pb.equals( entityBase) && ( pb.contains( name, number)))
					return true;
			}
		}
		return false;
	}

	/**
	 * @param name
	 * @param number
	 * @return
	 */
	public boolean contains_as_object_name(String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( entityBase.contains( name, number))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the role object which has the specified data except the specified one.
	 * @param role the specified role object
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if this layer contains the role object which has the specified data except the specified one
	 */
	public boolean contains(Role role, String type, String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof Role) {
				Role r = ( Role)drawObject;
				if ( !r.equals( role) && r.is_same_name( type, name))
					return true;
//			} else if ( drawObject instanceof EntityBase) {
//				EntityBase entityBase = ( EntityBase)drawObject;
//				if ( entityBase.is_same_name( type, name) || entityBase.has_same_name( name))
//					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the role object which has the specified data.
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if this layer contains the role object which has the specified data
	 */
	public boolean contains_this_role_name(String type, String name) {
		for ( DrawObject drawObject:this) {
			if ( type.equals( "agent_role") && drawObject instanceof AgentRole && drawObject._name.equals( name))
				return true;
			else if ( type.equals( "spot_role") && drawObject instanceof SpotRole && drawObject._name.equals( name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the object which has the specified data except the specified one.
	 * @param chartObject the specified chart object
	 * @param name the specified name
	 * @return true if this layer contains the object which has the specified data except the specified one
	 */
	public boolean contains(ChartObject chartObject, String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( entityBase._name.equals( name) || entityBase.has_same_name( name))
					return true;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( role._name.equals( name)
					|| role.has_same_agent_name( name, ""))
					return true;
			} else if ( drawObject instanceof ChartObject) {
				ChartObject co = ( ChartObject)drawObject;
				if ( !co.equals( chartObject) && ( co._name.equals( name)))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the available chart object.
	 * @return true if this layer contains the available chart object
	 */
	public boolean contains_available_chartObject() {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)drawObject;
				if ( chartObject.is_available())
					return true;
			}
		}
		return false;
	}

//	/**
//	 * @param name
//	 * @param number
//	 * @return
//	 */
//	public boolean contains_available_chartObject_uses_this_spot_name(String name, String number) {
//		for ( DrawObject drawObject:this) {
//			if ( drawObject instanceof ChartObject) {
//				ChartObject chartObject = ( ChartObject)drawObject;
//				if ( chartObject.is_available() && chartObject.is_contained( name, number))
//					//&& SoarsCommonTool.has_same_name( name, number, chartObject._object_name))
//					return true;
//			}
//		}
//		return false;
//	}

//	/**
//	 * @param spotObject
//	 * @return
//	 */
//	public boolean contains_spotObject_availabe_chartObject_uses(SpotObject spotObject) {
//		for ( DrawObject drawObject:this) {
//			if ( !( drawObject instanceof ChartObject))
//				continue;
//				
//			ChartObject chartObject = ( ChartObject)drawObject;
//			if ( chartObject.is_available() && chartObject.is_contained( spotObject._name, spotObject._number))
//				//&& SoarsCommonTool.has_same_name( spotObject._name, spotObject._number, chartObject._object_name))
//				return true;
//		}
//		return false;
//	}

	/**
	 * Returns true if this layer contains the object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this layer contains the object which has the specified data
	 */
	public boolean has_same_name(String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.has_same_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the agent object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this layer contains the agent object which has the specified data
	 */
	public boolean has_same_agent_name(String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			if ( agentObject.has_same_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the agent object which has the specified data except the specified one.
	 * @param name the specified name
	 * @param number the specified number
	 * @param drawObject the specified object
	 * @return true if this layer contains the agent object which has the specified data except the specified one
	 */
	public boolean has_same_agent_name(String name, String number, DrawObject drawObject) {
		for ( DrawObject dObject:this) {
			if ( !( dObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)dObject;
			if ( !agentObject.equals( drawObject) && agentObject.has_same_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the spot object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this layer contains the spot object which has the specified data
	 */
	public boolean has_same_spot_name(String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			if ( spotObject.has_same_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the spot object which has the specified data except the specified one.
	 * @param name the specified name
	 * @param number the specified number
	 * @param drawObject the specified object
	 * @return true if this layer contains the spot object which has the specified data except the specified one
	 */
	public boolean has_same_spot_name(String name, String number, DrawObject drawObject) {
		for ( DrawObject dObject:this) {
			if ( !( dObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)dObject;
			if ( !spotObject.equals( drawObject) && spotObject.has_same_name( name, number))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the chart object which has the specified data.
	 * @param name the specified name
	 * @param number the specified number
	 * @return true if this layer contains the chart object which has the specified data
	 */
	public boolean chartObject_has_same_name(String name, String number) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof ChartObject))
				continue;

			ChartObject chartObject = ( ChartObject)drawObject;
			if ( SoarsCommonTool.has_same_name( name, number, chartObject._name))
				return true;
		}
		return false;
	}

//	/**
//	 * @param name
//	 * @param number
//	 * @return
//	 */
//	public boolean role_has_same_agent_name(String name, String number) {
//		for ( DrawObject drawObject:this) {
//			if ( !( drawObject instanceof Role))
//				continue;
//
//			Role role = ( Role)drawObject;
//			if ( role.has_same_agent_name( name, number))
//				return true;
//		}
//		return false;
//	}

	/**
	 * Returns true if this layer contains the object which has the specified data and multi initial data.
	 * @param type the specified type
	 * @param name the specified name
	 * @return true if this layer contains the object which has the specified data and multi initial data
	 */
	public boolean is_multi(String type, String name) {
		EntityBase entityBase = null;
		if ( type.equals( "agent"))
			entityBase = ( EntityBase)_agentMap.get( name);
		else if ( type.equals( "spot"))
			entityBase = ( EntityBase)_spotMap.get( name);

		if ( null == entityBase)
			return false;

		return entityBase.is_multi();
//		for ( DrawObject drawObject:this) {
//			if ( ( type.equals( "agent") || type.equals( "spot"))
//				&& ( drawObject instanceof EntityBase)) {
//				EntityBase entityBase = ( EntityBase)drawObject;
//				if ( entityBase._name.equals( name))
//					return entityBase.is_multi();
//			}
//		}
//		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_role_name(java.lang.String)
	 */
	public boolean is_role_name(String name) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role._name.equals( name))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_role_name(java.lang.String)
	 */
	public boolean is_agent_role_name(String name) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentRole))
				continue;

			AgentRole agentRole = ( AgentRole)drawObject;
			if ( agentRole._name.equals( name))
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_role_name(java.lang.String)
	 */
	public boolean is_spot_role_name(String name) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotRole))
				continue;

			SpotRole spotRole = ( SpotRole)drawObject;
			if ( spotRole._name.equals( name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the object which has the specified alias.
	 * @param kind the type of the object
	 * @param alias the specified alias
	 * @return true if this layer contains the object which has the specified alias
	 */
	public boolean object_contains_this_alias(String kind, String alias) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.object_contains_this_alias( kind, alias))
				return true;
			
		}
		return false;
	}

	/**
	 * Returns true if this layer contains the role object which has the specified alias.
	 * @param alias the specified alias
	 * @return true if this layer contains the role object which has the specified alias
	 */
	public boolean role_contains_this_alias(String alias) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role.contains_this_alias( alias))
				return true;
			
		}
		return false;
	}

	/**
	 * Sets the selection of all objects on this layer.
	 * @param selected whether or not all objects on this layer are selected
	 */
	public void select_all(boolean selected) {
		for ( int i = size() - 1; i >= 0; --i)
			get( i).select( selected);
	}

	/**
	 * Selects the all objects in the specified rectangle.
	 * @param rectangle the specified rectangle
	 */
	public void select(Rectangle rectangle) {
		for ( int i = size() - 1; i >= 0; --i)
			get( i).select( rectangle);
	}

	/**
	 * Returns the object which contains the specified position.
	 * @param point the specified position
	 * @return the object which contains the specified position
	 */
	public DrawObject get(Point point) {
		for ( DrawObject drawObject:this) {
			if ( drawObject.contains( point)) {
				bring_to_top( drawObject);
				return drawObject;
			}
		}
		return null;
	}

	/**
	 * Returns true if the selected objects exist.
	 * @return true if the selected objects exist
	 */
	public boolean exist_selected_object() {
		for ( DrawObject drawObject:this) {
			if ( drawObject.selected())
				return true;
		}
		return false;
	}

	/**
	 * Gets the selected objects.
	 * @param drawObjects the array for the selected objects
	 */
	public void get_selected(Vector<DrawObject> drawObjects) {
		for ( DrawObject drawObject:this) {
			if ( drawObject.selected())
				drawObjects.add( drawObject);
		}
	}

	/**
	 * Gets the selected agent objects.
	 * @param agents the array for the selected agent objects
	 */
	public void get_selected_agents(Vector<DrawObject> agents) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject && drawObject.selected())
				agents.add( drawObject);
		}
	}

	/**
	 * Gets the selected spot objects.
	 * @param spots the array for the selected spot objects
	 */
	public void get_selected_spots(Vector<DrawObject> spots) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject && drawObject.selected())
				spots.add( drawObject);
		}
	}

	/**
	 * Gets the selected agent and spot objects.
	 * @param entityBases the array for the selected agent and spot objects
	 */
	public void get_selected_entityBases(Vector<DrawObject> entityBases) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase && drawObject.selected())
				entityBases.add( drawObject);
		}
	}

	/**
	 * Gets the selected agent role objects.
	 * @param agentRoles the array for the selected agent role objects
	 */
	public void get_selected_agent_roles(Vector<DrawObject> agentRoles) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentRole && drawObject.selected())
				agentRoles.add( drawObject);
		}
	}

	/**
	 * Gets the selected spot role objects.
	 * @param spotRoles the array for the selected spot role objects
	 */
	public void get_selected_spot_roles(Vector<DrawObject> spotRoles) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotRole && drawObject.selected())
				spotRoles.add( drawObject);
		}
	}

	/**
	 * Gets the selected chart objects.
	 * @param charts the array for the selected chart objects
	 */
	public void get_selected_charts(Vector<DrawObject> charts) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof ChartObject && drawObject.selected())
				charts.add( drawObject);
		}
	}

	/**
	 * Gets the clone objects of the selected ones.
	 * @param drawObjects the array for the clone objects
	 */
	public void get_clone_of_selected(Vector<DrawObject> drawObjects) {
		for ( DrawObject drawObject:this) {
			if ( drawObject.selected())
				drawObjects.add( DrawObject.create( drawObject));
		}
	}

	/**
	 * Appends the specified objects to this layer.
	 * @param drawObjects the array of the specified objects
	 * @param pasteCounter the number of the paste
	 * @param component the base class for all Swing components
	 */
	public void append_clone_of_selected(Vector<DrawObject> drawObjects, int pasteCounter, JComponent component) {
		append_clone_of_selected( drawObjects, 10 * pasteCounter, 10 * pasteCounter, pasteCounter, component);
	}

	/**
	 * Appends the specified objects to this layer.
	 * @param drawObjects the array of the specified objects
	 * @param delta the coordinates of the moving
	 * @param pasteCounter the number of the paste
	 * @param component the base class for all Swing components
	 */
	public void append_clone_of_selected(Vector<DrawObject> drawObjects, Point delta, int pasteCounter, JComponent component) {
		append_clone_of_selected( drawObjects, delta.x, delta.y, pasteCounter, component);
	}

	/**
	 * @param drawObjects
	 * @param deltaX
	 * @param deltaY
	 * @param pasteCounter
	 * @param component
	 */
	private void append_clone_of_selected(Vector<DrawObject> drawObjects, int deltaX, int deltaY, int pasteCounter, JComponent component) {
		for ( DrawObject dObject:drawObjects) {
			DrawObject drawObject = DrawObject.create( dObject);
			drawObject._id = LayerManager.get_instance().get_unique_id();
			String name = LayerManager.get_instance().get_unique_name( drawObject, pasteCounter);
			drawObject.rename( name, ( Graphics2D)component.getGraphics());
			drawObject._position.x += deltaX;
			drawObject._position.y += deltaY;
			// TODO グローバル対応
			drawObject._global = false;
			drawObject.set_default_image_color();
			drawObject.select( true);
			append_object( drawObject);
			drawObject.on_paste();
		}

		update_preferred_size( component);
		component.repaint();
	}

	/**
	 * Brings the specified object to top.
	 * @param drawObject the specified object
	 */
	public void bring_to_top(DrawObject drawObject) {
		int index = indexOf( drawObject);
		removeElementAt( index);
		insertElementAt( drawObject, 0);
	}

	/**
	 * Gets the all objects on this layer.
	 * @param drawObjects the array for the all objects
	 */
	public void get_drawObjects(Vector<DrawObject> drawObjects) {
		for ( DrawObject drawObject:this)
			drawObjects.add( drawObject);
	}

	/**
	 * Gets the all agent and spot objects on this layer.
	 * @param entityBases the array for the all agent and spot objects
	 */
	public void get_entityBases(Vector<DrawObject> entityBases) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase)
				entityBases.add( drawObject);
		}
	}

	/**
	 * Gets the all agent objects on this layer.
	 * @param agents the array for the all agent objects
	 */
	public void get_agents(Vector<DrawObject> agents) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject)
				agents.add( drawObject);
		}
	}

	/**
	 * Gets the all spot objects on this layer.
	 * @param spots the array for the all spot objects
	 */
	public void get_spots(Vector<DrawObject> spots) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject)
				spots.add( drawObject);
		}
	}

	/**
	 * @param entityBases
	 * @param gis
	 */
	public void get_gis_spots(List<EntityBase> entityBases, String gis) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				SpotObject spotObject = ( SpotObject)drawObject;
				//if ( spotObject._gis.equals( gis))
				if ( !spotObject._gis.equals( ""))
					// TODO GISで追加されたスポットは全て同じ対象とすることに変更した
					entityBases.add( spotObject);
			}
		}
	}

	/**
	 * Gets the all role objects on this layer.
	 * @param roles the array for the all role objects
	 */
	public void get_roles(Vector<DrawObject> roles) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof Role)
				roles.add( drawObject);
		}
	}

	/**
	 * Gets the all agent role objects on this layer.
	 * @param agentRoles the array for the all agent role objects
	 */
	public void get_agent_roles(Vector<DrawObject> agentRoles) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentRole)
				agentRoles.add( drawObject);
		}
	}

	/**
	 * Gets the all spot role objects on this layer.
	 * @param spotRoles the array for the all spot role objects
	 */
	public void get_spot_roles(Vector<DrawObject> spotRoles) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotRole)
				spotRoles.add( drawObject);
		}
	}

	/**
	 * Gets the all chart objects on this layer.
	 * @param charts the array for the all chart objects
	 */
	public void get_charts(Vector<DrawObject> charts) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof ChartObject)
				charts.add( drawObject);
		}
	}

	/**
	 * @return
	 */
	public boolean initial_data_file_exists() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.has_initial_data_file())
				return true;
		}
		return false;
	}

	/**
	 * @param initialDataFiles
	 */
	public void get_initial_data_files(List<String> initialDataFiles) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.get_initial_data_files( initialDataFiles);
		}
	}

	/**
	 * @param exchangeAlgebraInitialDataFiles
	 */
	public void get_exchange_algebra_initial_data_files(List<String> exchangeAlgebraInitialDataFiles) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.get_exchange_algebra_initial_data_files( exchangeAlgebraInitialDataFiles);
		}
	}

	/**
	 * Returns true if the all objects can be removed.
	 * @return true if the all objects can be removed
	 */
	public boolean can_remove() {
		select_all( true);
		boolean result = can_remove_selected();
		select_all( false);
		return result;
	}

	/**
	 * Returns true if the selected objects can be removed.
	 * @return true if the selected objects can be removed
	 */
	public boolean can_remove_selected() {
		WarningManager.get_instance().cleanup();
		boolean result = true;
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		for ( DrawObject drawObject:drawObjects) {
			if ( drawObject.selected() && !drawObject.can_remove())
				result = false;
			if ( drawObject.selected() && drawObject.is_global_object()) {
				String type = "";
				if ( drawObject instanceof SpotObject)
					type = "Spot";
				else if ( drawObject instanceof SpotRole)
					type = "Spot role";
				String[] message = new String[] {
					type,
					"name = " + drawObject._name
				};
				WarningManager.get_instance().add( message);
				result = false;
			}
		}
		return result;
	}

	/**
	 * Removes the selected objects.
	 */
	public void remove_selected() {
		Vector<DrawObject> drawObjects = new Vector<DrawObject>();
		get_selected( drawObjects);
		for ( DrawObject drawObject:drawObjects) {
			if ( drawObject.selected()) {
				removeElement( drawObject);

				if ( drawObject instanceof AgentObject)
					_agentMap.remove( drawObject._name);
				else if ( drawObject instanceof SpotObject)
					_spotMap.remove( drawObject._name);
				else if ( drawObject instanceof AgentRole)
					_agentRoleMap.remove( drawObject._name);
				else if ( drawObject instanceof SpotRole)
					_spotRoleMap.remove( drawObject._name);

				drawObject.cleanup();
			}
		}
	}

	/**
	 * Removes the specified objects.
	 * @param drawObjects the specified objects
	 */
	public void remove_object(Vector<DrawObject> drawObjects) {
		for ( DrawObject drawObject:drawObjects) {

			removeElement( drawObject);

			if ( drawObject instanceof AgentObject)
				_agentMap.remove( drawObject._name);
			else if ( drawObject instanceof SpotObject)
				_spotMap.remove( drawObject._name);
			else if ( drawObject instanceof AgentRole)
				_agentRoleMap.remove( drawObject._name);
			else if ( drawObject instanceof SpotRole)
				_spotRoleMap.remove( drawObject._name);
		}
	}

	/**
	 * Draws the objects on this layer.
	 * @param graphics2D the graphics object of JAVA
	 * @param component the base class for all Swing components
	 * @param connectionStartPoint the start point of the connection line on editing
	 * @param connectionEndPoint the end point of the connection line on editing
	 * @param connectionColor the color of the connection line
	 */
	public void draw(Graphics2D graphics2D, JComponent component, Point connectionStartPoint, Point connectionEndPoint, Color connectionColor) {
		for ( int i = size() - 1; i >= 0; --i)
			get( i).draw( graphics2D, component);

		for ( int i = size() - 1; i >= 0; --i) {
			if ( !( get( i) instanceof Role))
				continue;

			Role role = ( Role)get( i);
			role.draw_connection( graphics2D);
		}

		if ( null != connectionStartPoint) {
			graphics2D.setColor( connectionColor);
			graphics2D.drawLine( connectionStartPoint.x, connectionStartPoint.y, connectionEndPoint.x, connectionEndPoint.y);
		}
	}

	/**
	 * Gets the names of the agent objects on this layer.
	 * @param agentNames the array for the names
	 */
	public void get_agent_names(Vector<String> agentNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			agentNames.add( drawObject._name);
		}
	}

	/**
	 * Gets the names of the spot objects on this layer.
	 * @param spotNames the array for the names
	 */
	public void get_spot_names(Vector<String> spotNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			spotNames.add( drawObject._name);
		}
	}

	/**
	 * Gets the names of the role objects on this layer.
	 * @param roleNames the array for the names
	 */
	public void get_role_names(Vector<String> roleNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			if ( roleNames.contains( drawObject._name))
				continue;

			roleNames.add( drawObject._name);
		}
	}

	/**
	 * Gets the names of the agent role objects on this layer.
	 * @param agentRoleNames the array for the names
	 */
	public void get_agent_role_names(Vector<String> agentRoleNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentRole))
				continue;

			if ( agentRoleNames.contains( drawObject._name))
				continue;

			agentRoleNames.add( drawObject._name);
		}
	}

	/**
	 * Gets the names of the spot role objects on this layer.
	 * @param spotRoleNames the array for the names
	 */
	public void get_spot_role_names(Vector<String> spotRoleNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotRole))
				continue;

			if ( spotRoleNames.contains( drawObject._name))
				continue;

			spotRoleNames.add( drawObject._name);
		}
	}

	/**
	 * Gets the names of the objects which the agent or spot objects have on this layer.
	 * @param kind the kind of the object
	 * @param objectNames the array for the names
	 */
	public void get_object_names(String kind, Vector<String> objectNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.get_object_names( kind, objectNames);
		}
	}

	/**
	 * Gets the names of the objects which the agent objects have on this layer.
	 * @param kind the kind of the object
	 * @param agentObjectNames the array for the names
	 */
	public void get_agent_object_names(String kind, Vector<String> agentObjectNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			agentObject.get_object_names( kind, agentObjectNames);
		}
	}

	/**
	 * Gets the names of the objects which the spot objects have on this layer.
	 * @param kind the kind of the object
	 * @param spotObjectNames the array for the names
	 */
	public void get_spot_object_names(String kind, Vector<String> spotObjectNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			spotObject.get_object_names( kind, spotObjectNames);
		}
	}

	/**
	 * @param agentExchangeAlgebraNames
	 */
	public void get_agent_exchange_algebra_names(Vector<String> agentExchangeAlgebraNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			agentObject.get_exchange_algebra_names( agentExchangeAlgebraNames);
		}
	}

	/**
	 * @param spotExchangeAlgebraNames
	 */
	public void get_spot_exchange_algebra_names(Vector<String> spotExchangeAlgebraNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			spotObject.get_exchange_algebra_names( spotExchangeAlgebraNames);
		}
	}

	/**
	 * Gets the names of the number variables which the agent objects have on this layer.
	 * @param type the number variable type
	 * @param agentNumberObjectNames the array for the names
	 */
	public void get_agent_number_object_names(String type, Vector<String> agentNumberObjectNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			agentObject.get_number_object_names( type, agentNumberObjectNames);
		}
	}

	/**
	 * Gets the names of the number variables which the spot objects have on this layer.
	 * @param type the number variable type
	 * @param spotNumberObjectNames the array for the names
	 */
	public void get_spot_number_object_names(String type, Vector<String> spotNumberObjectNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			spotObject.get_number_object_names( type, spotNumberObjectNames);
		}
	}

	/**
	 * Gets the names of the objects which the agent or spot objects except the specified one have on this layer.
	 * @param kind the kind of the object
	 * @param objectNames the array for the names
	 * @param entityBase the specified object
	 */
	public void get_object_names(String kind, Vector<String> objectNames, EntityBase entityBase) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase pb = ( EntityBase)drawObject;
			if ( pb.equals( entityBase))
				continue;

			pb.get_object_names( kind, objectNames);
		}
	}

	/**
	 * Gets the names of the objects which the agent objects except the specified one have on this layer.
	 * @param kind the kind of the object
	 * @param agentObjectNames the array for the names
	 * @param entityBase the specified object
	 */
	public void get_agent_object_names(String kind, Vector<String> agentObjectNames, EntityBase entityBase) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			if ( agentObject.equals( entityBase))
				continue;

			agentObject.get_object_names( kind, agentObjectNames);
		}
	}

	/**
	 * Gets the names of the objects which the spot objects except the specified one have on this layer.
	 * @param kind the kind of the object
	 * @param spotObjectNames the array for the names
	 * @param entityBase the specified object
	 */
	public void get_spot_object_names(String kind, Vector<String> spotObjectNames, EntityBase entityBase) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			if ( spotObject.equals( entityBase))
				continue;

			spotObject.get_object_names( kind, spotObjectNames);
		}
	}

	/**
	 * Gets the class variable hashtable which the agent objects have on this layer.
	 * @param classVariableMap the class variable hashtable
	 */
	public void get_agent_class_variable_map(Map<String, ClassVariableObject> classVariableMap) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			agentObject.get_class_variable_map( classVariableMap);
		}
	}

	/**
	 * Gets the class variable hashtable which the spot objects have on this layer.
	 * @param classVariableMap the class variable hashtable
	 */
	public void get_spot_class_variable_map(Map<String, ClassVariableObject> classVariableMap) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			spotObject.get_class_variable_map( classVariableMap);
		}
	}

	/**
	 * Returns the class variable whose name is equal to the specified one.
	 * @param functionalObject the specified functional object
	 * @return the class variable whose name is equal to the specified one
	 */
	public ClassVariableObject get_class_variable(FunctionalObject functionalObject) {
		String spot = CommonRuleManipulator.get_semantic_prefix( functionalObject._spots);
		for ( DrawObject drawObject:this) {
			if ( spot.equals( "")) {
				if ( !( drawObject instanceof AgentObject))
					continue;
			} else {
				if ( !( drawObject instanceof SpotObject))
					continue;

				if ( !spot.equals( "<>")) {
					SpotObject spotObject = ( SpotObject)drawObject;
					if ( !spotObject.has_same_name( functionalObject._spots[ 0]))
						continue;
				}
			}

			EntityBase entityBase = ( EntityBase)drawObject;

			ClassVariableObject classVariableObject = entityBase.get_class_variable( functionalObject._classVariable);
			if ( null != classVariableObject)
				return classVariableObject;
		}
		return null;
	}

	/**
	 * Returns the class variable whose name is equal to the specified one.
	 * @param prefix the prefix of the object full name
	 * @param classVariableName the specified name
	 * @return the class variable whose name is equal to the specified one
	 */
	public ClassVariableObject get_class_variable(String prefix, String classVariableName) {
		for ( DrawObject drawObject:this) {
			if ( prefix.equals( "")) {
				if ( !( drawObject instanceof AgentObject))
					continue;

				AgentObject agentObject = ( AgentObject)drawObject;
				ClassVariableObject classVariableObject = agentObject.get_class_variable( classVariableName);
				if ( null == classVariableObject)
					continue;

				return classVariableObject;
			} else {
				if ( !( drawObject instanceof SpotObject))
					continue;

				SpotObject spotObject = ( SpotObject)drawObject;

				if ( prefix.equals( "<>")) {
					ClassVariableObject classVariableObject = spotObject.get_class_variable( classVariableName);
					if ( null == classVariableObject)
						continue;

					return classVariableObject;
				} else {
					if ( !spotObject.has_same_name( prefix))
						continue;

					ClassVariableObject classVariableObject = spotObject.get_class_variable( classVariableName);
					if ( null == classVariableObject)
						return null;

					return classVariableObject;
				}
			}
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_object_name(String kind, String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( entityBase.has_same_object_name( kind, name))
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the object which has the specified name exists except ths specified one.
	 * @param kind the type of the object
	 * @param name the specified name
	 * @param entityBase ths specified object
	 * @return true if the object which has the specified name exists except ths specified one
	 */
	public boolean is_object_name(String kind, String name, EntityBase entityBase) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase pb = ( EntityBase)drawObject;
				if ( pb.equals( entityBase))
					continue;

				if ( pb.has_same_object_name( kind, name))
					return true;
			}
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_agent_object_name(String kind, String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject) {
				AgentObject agentObject = ( AgentObject)drawObject;
				if ( agentObject.has_same_object_name( kind, name))
					return true;
			}
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_agent_object_name(String kind, String fullName, String name) {
		AgentObject agentObject = get_agent_has_this_name( fullName);
		if ( null == agentObject)
			return false;

		return agentObject.has_same_object_name( kind, name);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_spot_object_name(String kind, String name) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				SpotObject spotObject = ( SpotObject)drawObject;
				if ( spotObject.has_same_object_name( kind, name))
					return true;
			}
		}
		return false;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_spot_object_name(String kind, String fullName, String name) {
		SpotObject spotObject = get_spot_has_this_name( fullName);
		if ( null == spotObject)
			return false;

		return spotObject.has_same_object_name( kind, name);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_number_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_agent_number_object_name(String numberObjectName, String numberObjectType) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject) {
				AgentObject agentObject = ( AgentObject)drawObject;
				if ( agentObject.is_number_object_correct( numberObjectName, numberObjectType))
					return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_agent_number_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_agent_number_object_name(String fullName, String numberObjectName, String numberObjectType) {
		AgentObject agentObject = get_agent_has_this_name( fullName);
		if ( null == agentObject)
			return false;

		return agentObject.is_number_object_correct( numberObjectName, numberObjectType);
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_number_object_name(java.lang.String, java.lang.String)
	 */
	public boolean is_spot_number_object_name(String numberObjectName, String numberObjectType) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				SpotObject spotObject = ( SpotObject)drawObject;
				if ( spotObject.is_number_object_correct( numberObjectName, numberObjectType))
					return true;
			}
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#is_spot_number_object_name(java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean is_spot_number_object_name(String fullName, String numberObjectName, String numberObjectType) {
		SpotObject spotObject = get_spot_has_this_name( fullName);
		if ( null == spotObject)
			return false;

		return spotObject.is_number_object_correct( numberObjectName, numberObjectType);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_agent_number_object_type(java.lang.String)
	 */
	public String get_agent_number_object_type(String numberObjectName) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof AgentObject) {
				AgentObject agentObject = ( AgentObject)drawObject;
				String type = agentObject.get_number_object_type( numberObjectName);
				if ( null != type)
					return type;
			}
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_agent_number_object_type(java.lang.String, java.lang.String)
	 */
	public String get_agent_number_object_type(String fullName, String numberObjectName) {
		AgentObject agentObject = get_agent_has_this_name( fullName);
		if ( null == agentObject)
			return null;

		return agentObject.get_number_object_type( numberObjectName);
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_spot_number_object_type(java.lang.String)
	 */
	public String get_spot_number_object_type(String numberObjectName) {
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				SpotObject spotObject = ( SpotObject)drawObject;
				String type = spotObject.get_number_object_type( numberObjectName);
				if ( null != type)
					return type;
			}
		}
		return null;
	}

	/* (Non Javadoc)
	 * @see soars.application.visualshell.layer.ILayerManipulator#get_spot_number_object_type(java.lang.String, java.lang.String)
	 */
	public String get_spot_number_object_type(String fullName, String numberObjectName) {
		SpotObject spotObject = get_spot_has_this_name( fullName);
		if ( null == spotObject)
			return null;

		return spotObject.get_number_object_type( numberObjectName);
	}

	/**
	 * Gets the initial values of the objects which the agent or spot objects have.
	 * @param kind the kind of the object
	 * @param initialValues the array for the initial values
	 */
	public void get_object_initial_values(String kind, Vector<String> initialValues) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.get_object_initial_values( kind, initialValues);
		}
	}

	/**
	 * Gets the initial values of the objects which the agent objects have.
	 * @param kind the kind of the object
	 * @param initialValues the array for the initial values
	 */
	public void get_agent_object_initial_values(String kind, Vector<String> initialValues) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			agentObject.get_object_initial_values( kind, initialValues);
		}
	}

	/**
	 * Gets the initial values of the objects which the spot objects have.
	 * @param kind the kind of the object
	 * @param initialValues the array for the initial values
	 */
	public void get_spot_object_initial_values(String kind, Vector<String> initialValues) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			spotObject.get_object_initial_values( kind, initialValues);
		}
	}

	/**
	 * Gets the initial values of the objects which the role objects have.
	 * @param roleInitialValues the array for the initial values
	 * @param suffixes the set of the suffixes
	 */
	public void get_role_initial_values(Vector<String> roleInitialValues, String[] suffixes) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			role.get_initial_values( roleInitialValues, suffixes);
		}
	}

	/**
	 * Returns true if other agent objects have the object which has the specified data.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @return true if other agent objects have the object which has the specified data
	 */
	public boolean other_agents_have_same_object_name(String kind, String name) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			if ( !agentObject.selected() && agentObject.has_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other agent objects have the object which has the specified data except the spceified one.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param agentObject the spceified agent object
	 * @return true if other agent objects have the object which has the specified data except the spceified one
	 */
	public boolean other_agents_have_same_object_name(String kind, String name, AgentObject agentObject) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject ao = ( AgentObject)drawObject;
			if ( !ao.equals( agentObject) && ao.has_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other spot objects have the object which has the specified data.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @return true if other spot objects have the object which has the specified data
	 */
	public boolean other_spots_have_same_object_name(String kind, String name) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			if ( !spotObject.selected() && spotObject.has_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other spot objects have the object which has the specified data except the spceified one.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param spotObject the spceified spot object
	 * @return true if other spot objects have the object which has the specified data except the spceified one
	 */
	public boolean other_spots_have_same_object_name(String kind, String name, SpotObject spotObject) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject so = ( SpotObject)drawObject;
			if ( !so.equals( spotObject) && so.has_same_object_name( kind, name))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if other spot objects have the object which has the specified data except the spceified one.
	 * @param kind the specified kind
	 * @param name the specified name
	 * @param gis the specified GIS's ID
	 * @param spotObject the spceified spot object
	 * @return true if other spot objects have the object which has the specified data except the spceified one
	 */
	public boolean other_spots_have_same_object_name(String kind, String name, String gis, SpotObject spotObject) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject so = ( SpotObject)drawObject;
			if ( !so.equals( spotObject) && so.has_same_object_name( kind, name, gis))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the specified class variable name is in use for a different class on other objects except the specified one.
	 * @param classVariable the specified class variable
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param entityBase the specified object
	 * @return true if the specified class variable name is in use for a different class on other objects except the specified one
	 */
	public boolean other_uses_this_class_variable_as_different_class(String classVariable, String jarFilename, String classname, EntityBase entityBase) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject) && !( drawObject instanceof SpotObject))
				continue;

			if ( drawObject instanceof AgentObject && !( entityBase instanceof AgentObject))
				continue;

			if ( drawObject instanceof SpotObject && !( entityBase instanceof SpotObject))
				continue;

			EntityBase pb = ( EntityBase)drawObject;
			if ( !pb.equals( entityBase) && pb.uses_this_class_variable_as_different_class( classVariable, jarFilename, classname))
				return true;
		}
		return false;
	}

	/**
	 * Returns true if the specified class variable name is in use for a different class on other objects.
	 * @param classVariable the specified class variable
	 * @param jarFilename the jar file name
	 * @param classname the class name
	 * @param type the object type
	 * @return true if the specified class variable name is in use for a different class on other objects
	 */
	public boolean other_uses_this_class_variable_as_different_class(String classVariable, String jarFilename, String classname, String type) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject) && !( drawObject instanceof SpotObject))
				continue;

			if ( drawObject instanceof AgentObject && !type.equals( "agent"))
				continue;

			if ( drawObject instanceof SpotObject && !type.equals( "spot"))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.uses_this_class_variable_as_different_class( classVariable, jarFilename, classname))
				return true;
		}
		return false;
	}

	/**
	 * Returns true the specified class is in use.
	 * @param jarFilename the specified jar file name
	 * @param classname the specified class name
	 * @return true the specified class is in use
	 */
	public boolean uses_this_class(String jarFilename, String classname) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.uses_this_class( jarFilename, classname))
				return true;
		}
		return false;
	}

//	/**
//	 * @param file
//	 * @param kind
//	 * @return
//	 */
//	public boolean uses_this_file(File file, String kind) {
//		for ( DrawObject drawObject:this) {
//			if ( !( drawObject instanceof EntityBase))
//				continue;
//
//			EntityBase entityBase = ( EntityBase)drawObject;
//			if ( entityBase.uses_this_file( file, kind))
//				return true;
//		}
//		return false;
//	}

	/**
	 * @param srcPath
	 * @param destPath
	 * @return
	 */
	public boolean move_file(File srcPath, File destPath) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.move_file( srcPath, destPath))
				result = true;
		}
		return result;
	}

	/**
	 * Updates the sizes of the object names.
	 * @param graphics2D the graphics object of JAVA
	 */
	public void update_name_dimension(Graphics2D graphics2D) {
		for ( DrawObject drawObject:this)
			drawObject.setup_name_dimension( graphics2D);
	}

	/**
	 * Returns true if the agent name can be adjusted.
	 * @param headName the prefix of the agent name
	 * @param ranges the ranges for the agent number
	 * @param onRemove true for removing the object
	 * @return true if the agent name can be adjusted
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, boolean onRemove) {
		boolean result1 = true;
		boolean result2 = true;
		boolean result3 = true;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( ( !onRemove || !entityBase.selected()) && !entityBase.can_adjust_agent_name( headName, ranges))
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( ( !onRemove || !role.selected()) && !role.can_adjust_agent_name( headName, ranges))
					result2 = false;
			} else if ( drawObject instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)drawObject;
				if ( ( !onRemove || !chartObject.selected()) && !chartObject.can_adjust_agent_name( headName, ranges))
					result3 = false;
			}
		}
		return ( result1 && result2 && result3);
	}

	/**
	 * Returns true if it is possible to update the specified agent name with the new one.
	 * @param headName the prefix of the specified agent name
	 * @param ranges the ranges for the specified agent number
	 * @param newHeadName the prefix of the new agent name
	 * @param newRanges the ranges for the new agent number
	 * @return true if it is possible to update the specified agent name with the new one
	 */
	public boolean can_adjust_agent_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result1 = true;
		boolean result2 = true;
		boolean result3 = true;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( !entityBase.can_adjust_agent_name( headName, ranges, newHeadName, newRanges))
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( !role.can_adjust_agent_name( headName, ranges, newHeadName, newRanges))
					result2 = false;
			} else 	if ( drawObject instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)drawObject;
				if ( !chartObject.can_adjust_agent_name( headName, ranges, newHeadName, newRanges))
					result3 = false;
			}
		}
		return ( result1 && result2 && result3);
	}

	/**
	 * Returns true if the spot name can be adjusted.
	 * @param headName the prefix of the spot name
	 * @param ranges the ranges for the spot number
	 * @param onRemove true for removing the object
	 * @return true if the spot name can be adjusted
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, boolean onRemove) {
		boolean result1 = true;
		boolean result2 = true;
		boolean result3 = true;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( ( !onRemove || !entityBase.selected()) && !entityBase.can_adjust_spot_name( headName, ranges))
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( ( !onRemove || !role.selected()) && !role.can_adjust_spot_name( headName, ranges))
					result2 = false;
			} else if ( drawObject instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)drawObject;
				if ( ( !onRemove || !chartObject.selected()) && !chartObject.can_adjust_spot_name( headName, ranges))
					result3 = false;
			}
		}
		return ( result1 && result2 && result3);
	}

	/**
	 * Returns true if it is possible to update the specified spot name with the new one successfully.
	 * @param headName the prefix of the specified spot name
	 * @param ranges the ranges for the specified spot number
	 * @param newHeadName the prefix of the new spot name
	 * @param newRanges the ranges for the new spot number
	 * @return true if it is possible to update the specified spot name with the new one successfully
	 */
	public boolean can_adjust_spot_name(String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result1 = true;
		boolean result2 = true;
		boolean result3 = true;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( !entityBase.can_adjust_spot_name( headName, ranges, newHeadName, newRanges))
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( !role.can_adjust_spot_name( headName, ranges, newHeadName, newRanges))
					result2 = false;
			} else if ( drawObject instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)drawObject;
				if ( !chartObject.can_adjust_spot_name( headName, ranges, newHeadName, newRanges))
					result3 = false;
			}
		}
		return ( result1 && result2 && result3);
	}

	/**
	 * Returns true if the specified object can be removed.
	 * @param kind the specified kind of the object
	 * @param objectName the specified name of the object
	 * @param otherSpotsHaveThisObjectName true if other spot objects have the object which has the specified data
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param onRemove true for removing the object
	 * @return true if the specified object can be removed
	 */
	public boolean can_remove(String kind, String objectName, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, boolean onRemove) {
		// TODO 従来のもの
		boolean result = true;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( ( !onRemove || !role.selected())
				&& !role.can_remove( kind, objectName, otherSpotsHaveThisObjectName, headName, ranges))
				result = false;
		}
		return result;
	}

	/**
	 * @param entity
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param onRemove
	 * @return
	 */
	public boolean can_remove(String entity, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, boolean onRemove) {
		// TODO これからはこちらに移行してゆく
		boolean result = true;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( ( !onRemove || !role.selected())
				&& !role.can_remove( entity, kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified object can be removed.
	 * @param kind the specified kind of the object
	 * @param type the type of the number variable
	 * @param name the specified name of the object
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param onRemove true for removing the object
	 * @return true if the specified object can be removed
	 */
	public boolean can_remove(String kind, String type, String name, String headName, Vector<String[]> ranges, boolean onRemove) {
		boolean result = true;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof ChartObject))
				continue;

			ChartObject chartObject = ( ChartObject)drawObject;
			if ( ( !onRemove || !chartObject.selected())
				&& !chartObject.can_remove( kind, type, name, headName, ranges))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified object can be removed, except the specified agent or spot object.
	 * @param kind the specified kind of the object
	 * @param name the specified name of the object
	 * @param otherSpotsHaveThisObjectName true if other spot objects have the object which has the specified data
	 * @param headName the prefix of the agent or spot name
	 * @param ranges the ranges for the agent or spot number
	 * @param entityBase the specified agent or spot object
	 * @param onRemove true for removing the object
	 * @return true if the specified object can be removed, except the specified agent or spot object
	 */
	public boolean can_remove(String kind, String name, boolean otherSpotsHaveThisObjectName, String headName, Vector<String[]> ranges, EntityBase entityBase, boolean onRemove) {
		// TODO 従来のもの
		boolean result1 = true;
		boolean result2 = true;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase pb = ( EntityBase)drawObject;
				if ( ( !onRemove || !pb.selected()) && !pb.equals( entityBase)
					&& !pb.can_remove( kind, name, otherSpotsHaveThisObjectName, headName, ranges))	// このpb.can_remove( ... )は不要か？
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( ( !onRemove || !role.selected())
					&& !role.can_remove( kind, name, otherSpotsHaveThisObjectName, headName, ranges))
					result2 = false;
			}
		}
		return ( result1 && result2);
	}

	/**
	 * @param entity
	 * @param kind
	 * @param objectName
	 * @param otherEntitiesHaveThisObjectName
	 * @param headName
	 * @param ranges
	 * @param entityBase
	 * @param onRemove
	 * @return
	 */
	public boolean can_remove(String entity, String kind, String objectName, boolean otherEntitiesHaveThisObjectName, String headName, Vector<String[]> ranges, EntityBase entityBase, boolean onRemove) {
		// TODO これからはこちらに移行してゆく
		boolean result1 = true;
		boolean result2 = true;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase pb = ( EntityBase)drawObject;
				if ( ( !onRemove || !pb.selected()) && !pb.equals( entityBase)
					&& !pb.can_remove( kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges))	// このpb.can_remove( ... )は不要か？
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( ( !onRemove || !role.selected())
					&& !role.can_remove( entity, kind, objectName, otherEntitiesHaveThisObjectName, headName, ranges))
					result2 = false;
			}
		}
		return ( result1 && result2);
	}

	/**
	 * Returns true for updating the specified object name with the new one successfully, except the specified agent or spot object.
	 * @param kind the specified kind of the object
	 * @param name the specified name of the object
	 * @param newName the new name of the object
	 * @param entityBase the specified agent or spot object
	 * @return true for updating the specified object name with the new one successfully, except the specified agent or spot object
	 */
	public boolean update_object_name(String kind, String name, String newName, EntityBase entityBase) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase) {
				EntityBase pb = ( EntityBase)drawObject;
				if ( pb.equals( entityBase))
					continue;

				if ( pb.update_object_name( kind, name, newName, ( ( entityBase instanceof AgentObject) ? "agent" : "spot"))) {
					String[] message = new String[] {
						( ( pb instanceof AgentObject) ? "Agent : " : "Spot : ") + pb._name
					};

					WarningManager.get_instance().add( message);

					result = true;
				}
			} else if ( drawObject instanceof Role) {
				Role role = ( Role)drawObject;
				if ( role.update_object_name( kind, name, newName, ( ( entityBase instanceof AgentObject) ? "agent" : "spot"))) {
					String[] message = new String[] {
						( ( role instanceof AgentRole) ? "Agent role : " : "Spot role : ") + role._name
					};

					WarningManager.get_instance().add( message);

					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Returns true for updating the specified number variable name with the new one successfully, except the specified agent or spot object.
	 * @param entity "agent" or "spot"
	 * @param name the specified name of the number variable
	 * @param newName the new name of the number variable
	 * @param entityBase the specified agent or spot object
	 * @return true for updating the specified number variable name with the new one successfully, except the specified agent or spot object
	 */
	public boolean update_number_object_name(String entity, String name, String newName, EntityBase entityBase) {
		boolean result = false;
		for ( int i = 0; i < size(); ++i) {
			if ( get( i) instanceof EntityBase) {
				EntityBase pb = ( EntityBase)get( i);
				if ( pb.equals( entityBase))
					continue;

				if ( pb.update_object_name( "number object", name, newName, entity)) {
					String[] message = new String[] {
						( ( pb instanceof AgentObject) ? "Agent : " : "Spot : ") + pb._name
					};

					WarningManager.get_instance().add( message);

					result = true;
				}
			} else if ( get( i) instanceof Role) {
				Role role = ( Role)get( i);
				if ( role.update_object_name( "number object", name, newName, entity)) {
					String[] message = new String[] {
						( ( role instanceof AgentRole) ? "Agent role : " : "Spot role : ") + role._name
					};

					WarningManager.get_instance().add( message);

					result = true;
				}
			} else if ( get( i) instanceof ChartObject) {
				ChartObject chartObject = ( ChartObject)get( i);
				if ( chartObject.update_object_name( "number object", entity, name, newName)) {
					String[] message = new String[] {
						"Chart : " + chartObject._name
					};

					WarningManager.get_instance().add( message);

					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * Returns true if the specified role can be removed.
	 * @param name the specified role name
	 * @param onRemove true for removing the object
	 * @param role the specified role
	 * @return true if the specified role can be removed
	 */
	public boolean can_remove_role(String name, boolean onRemove, Role role) {
		boolean result1 = true;
		boolean result2 = true;
		for ( DrawObject drawObject:this) {
			if ( ( role instanceof AgentRole && drawObject instanceof AgentObject)
				|| ( role instanceof SpotRole && drawObject instanceof SpotObject)) {
				EntityBase entityBase = ( EntityBase)drawObject;
				if ( ( !onRemove || !entityBase.selected()) && !entityBase.can_remove_role_name( name))
					result1 = false;
			} else if ( drawObject instanceof Role) {
				Role r = ( Role)drawObject;
				if ( ( !onRemove || !r.selected()) && !r.can_remove_role_name( name))
					result2 = false;
			}
		}
		return ( result1 && result2);
	}

	/**
	 * Returns true if the specified expression can be removed.
	 * @param function the function of the specified expression
	 * @return true if the specified expression can be removed
	 */
	public boolean can_remove_expression(Expression expression) {
		boolean result = true;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( !role.can_remove_expression( expression))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true for updating the specified stage name with the new one.
	 * @param newName the new stage name
	 * @param originalName the specified stage name
	 * @return true for updating the specified stage name with the new one
	 */
	public boolean update_stage_name(String newName, String originalName) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role.update_stage_name( newName, originalName))
				result = true;
		}
		return result;
	}

	/**
	 * Returns true if the specified stage can be removed.
	 * @param stageName the specified stage name
	 * @return true if the specified stage can be removed
	 */
	public boolean can_remove_stage_name(String stageName) {
		boolean result = true;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( !role.can_remove_stage_name( stageName))
				result = false;
		}
		return result;
	}

	/**
	 * Returns true if the specified stage names can be adjusted.
	 * @param stageNames the array of the specified stage names
	 * @return true if the stage names can be adjusted
	 */
	public boolean can_adjust_stage_name(Vector<String> stageNames) {
		boolean result = true;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( !role.can_adjust_stage_name( stageNames))
				result = false;
		}
		return result;
	}

	/**
	 * Invoked when the agent object has been changed.
	 * @param newName the new agent name
	 * @param originalName the original agent name
	 * @param headName the prefix of the agent name
	 * @param ranges the ranges for the agent number
	 * @param newHeadName the new prefix of the agent name
	 * @param newRanges the new ranges for the agent number
	 * @return
	 */
	public boolean update_agent_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase
				|| drawObject instanceof Role
				|| drawObject instanceof ChartObject) {
				if ( drawObject.update_agent_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
					result = true;
			}
		}
		return result;
	}

	/**
	 * Invoked when the spot object has been changed.
	 * @param newName the new spot name
	 * @param originalName the original spot name
	 * @param headName the prefix of the spot name
	 * @param ranges the ranges for the spot number
	 * @param newHeadName the new prefix of the spot name
	 * @param newRanges the new ranges for the spot number
	 * @return
	 */
	public boolean update_spot_name_and_number(String newName, String originalName, String headName, Vector<String[]> ranges, String newHeadName, Vector<String[]> newRanges) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof EntityBase
				|| drawObject instanceof Role
				|| drawObject instanceof ChartObject) {
				if ( drawObject.update_spot_name_and_number( newName, originalName, headName, ranges, newHeadName, newRanges))
					result = true;
			}
		}
		return result;
	}

	/**
	 * Invoked when the specified role object has been changed.
	 * @param originalName the role spot name
	 * @param role the specified role object
	 * @return
	 */
	public boolean update_role_name(String originalName, Role role) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( ( role instanceof AgentRole && drawObject instanceof AgentObject)
				|| ( role instanceof SpotRole && drawObject instanceof SpotObject)
				|| drawObject instanceof Role) {
				if ( drawObject.update_role_name( originalName, role._name))
					result = true;
			}
		}
		return result;
	}

	/**
	 * Invoked when the expressions have been changed.
	 * @param visualShellExpressionManager the expressions manager for Visual Shell
	 * @return
	 */
	public boolean update_expression(VisualShellExpressionManager visualShellExpressionManager) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role.update_expression( visualShellExpressionManager))
				result = true;
		}
		return result;
	}

	/**
	 * @param newExpression
	 * @param newVariableCount
	 * @param originalExpression
	 * @return
	 */
	public boolean update_expression(Expression newExpression, int newVariableCount, Expression originalExpression) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role.update_expression( newExpression, newVariableCount, originalExpression))
				result = true;
		}
		return result;
	}

	/**
	 * Invoked when the specified stage names have been changed.
	 * @param stageNames the array of the specified stage names
	 * @return
	 */
	public void on_remove_stage_name(Vector<String> stageNames) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			role.on_remove_stage_name( stageNames);
		}
	}

	/**
	 * @param filename
	 * @return
	 */
	public boolean uses_this_image(String filename) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase.uses_this_image( filename))
				return true;
		}
		return false;
	}

	/**
	 * 
	 */
	public void update_image() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.update_image();
		}
	}

	/**
	 * @param originalFilename
	 * @param newFilename
	 */
	public void update_image(String originalFilename, String newFilename) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.update_image( originalFilename, newFilename);
		}
	}

	/**
	 * @return
	 */
	public boolean transform_map_objects() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( !entityBase.transform_map_objects())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean transform_time_conditions_and_commands() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( !role.transform_time_conditions_and_commands())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean transform_keyword_conditions_and_commands() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( !role.transform_keyword_conditions_and_commands())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean transform_numeric_conditions_and_commands() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( !role.transform_numeric_conditions_and_commands())
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	public boolean is_initial_data_file_correct() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			if ( !entityBase.is_initial_data_file_correct())
				return false;
		}
		return true;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @param index number of this layer
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer, int index) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( String.valueOf( index)));

		if ( isEmpty()) {
			writer.writeElement( null, null, "layer", attributesImpl);
			return true;
		}

		writer.startElement( null, null, "layer", attributesImpl);
		for ( DrawObject drawObject:this) {
			if ( !drawObject.write( writer))
				return false;
		}
		writer.endElement( null, null, "layer");
		return true;
	}

	/**
	 * Returns true if the specified objects can be pasted.
	 * @param drawObjects the array for the specified objects
	 * @return true if the specified objects can be pasted
	 */
	public boolean can_paste(Layer drawObjects) {
		boolean result = true;
		for ( DrawObject drawObject:this) {
			//if ( !drawObject.can_paste( this)) {
				if ( !drawObject.can_paste( drawObjects))
					result = false;
			//}
		}
		return result;
	}

	/**
	 * Updates the stage manager.
	 * @return
	 */
	public boolean update_stage_manager() {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role.update_stage_manager())
				result = true;
		}
		return result;
	}

	/**
	 * Gets the used expressions.
	 * @param expressionMap the expression hashtable
	 * @param usedExpressionMap the used expression hashtable
	 */
	public void get_used_expressions(TreeMap<String, Expression> expressionMap, TreeMap<String, Expression> usedExpressionMap) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			role.get_used_expressions( expressionMap, usedExpressionMap);
		}
	}

	/**
	 * Updates the specified function name with the new one.
	 * @param originalFunctionName the specified function name
	 * @param newFunctionName the new function name
	 * @return
	 */
	public boolean update_function(String originalFunctionName, String newFunctionName) {
		boolean result = false;
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof Role))
				continue;

			Role role = ( Role)drawObject;
			if ( role.update_function( originalFunctionName, newFunctionName))
				result = true;
		}
		return result;
	}

	/**
	 * @param imagefiles
	 * @param thumbnailImagefiles
	 * @return
	 */
	public boolean get_imagefiles_selected_objects_use(List<File> imagefiles, List<File> thumbnailImagefiles) {
		Vector<DrawObject> entityBases = new Vector<DrawObject>();
		get_selected_entityBases( entityBases);
		for ( DrawObject drawObject:entityBases) {
			EntityBase entityBase = ( EntityBase)drawObject;
			if ( entityBase._imageFilename.equals( ""))
				continue;

			File imagefile = new File( LayerManager.get_instance().get_image_directory(), entityBase._imageFilename);
			if ( !imagefile.exists() || !imagefile.isFile() || !imagefile.canRead())
				return false;

			File thumbnailImagefile = new File( LayerManager.get_instance().get_thumbnail_image_directory(), entityBase._imageFilename);
			if ( !thumbnailImagefile.exists() || !thumbnailImagefile.isFile() || !thumbnailImagefile.canRead())
				return false;

			if ( imagefiles.contains( imagefile))
				continue;

			imagefiles.add( imagefile);
			thumbnailImagefiles.add( thumbnailImagefile);
		}
		return true;
	}

	/**
	 * @param files
	 * @return
	 */
	public boolean get_user_data_files_selected_objects_use(List<File> files) {
		Vector<DrawObject> entityBases = new Vector<DrawObject>();
		get_selected_entityBases( entityBases);
		for ( DrawObject drawObject:entityBases) {
			EntityBase entityBase = ( EntityBase)drawObject;
			if ( !entityBase.get_user_data_files( files))
				return false;
		}
		return true;
	}

	/**
	 * Returns true for writing this object data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @param name the name of this layer
	 * @return true for writing this object data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_selected_objects(Writer writer, String name) throws SAXException {
		Vector<DrawObject> agents = new Vector<DrawObject>();
		get_selected_agents( agents);

		Vector<DrawObject> spots = new Vector<DrawObject>();
		get_selected_spots( spots);

		Vector<DrawObject> agent_roles = new Vector<DrawObject>();
		get_selected_agent_roles( agent_roles);

		Vector<DrawObject> spot_roles = new Vector<DrawObject>();
		get_selected_spot_roles( spot_roles);

		Vector<DrawObject> charts = new Vector<DrawObject>();
		get_selected_charts( charts);

		if ( agents.isEmpty() && spots.isEmpty()
			&& agent_roles.isEmpty() && spot_roles.isEmpty()
			&& charts.isEmpty())
			return false;

		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( String.valueOf( name)));

		writer.startElement( null, null, "layer", attributesImpl);

		if ( !write_selected_objects( writer, agents))
			return false;

		if ( !write_selected_objects( writer, spots))
			return false;

		if ( !write_selected_objects( writer, agent_roles))
			return false;

		if ( !write_selected_objects( writer, spot_roles))
			return false;

		if ( !write_selected_objects( writer, charts))
			return false;

		writer.endElement( null, null, "layer");
		return true;
	}

	/**
	 * @param writer
	 * @param drawObjects
	 * @return
	 * @throws SAXException
	 */
	private boolean write_selected_objects(Writer writer, Vector<DrawObject> drawObjects) throws SAXException {
		for ( DrawObject drawObject:drawObjects) {
			if ( !drawObject.write( writer))
				return false;
		}
		return true;
	}

	/**
	 * Gets the jar file names.
	 * @param list the array for the jar file names
	 */
	public void get_jar_filenames(List<String> list) {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof EntityBase))
				continue;

			EntityBase entityBase = ( EntityBase)drawObject;
			entityBase.get_jar_filenames( list);
		}
	}

	/**
	 * Returns true if the connection between the specified role object and one which has the specified connection object contradicts.
	 * @param connectInObject the specified connection object
	 * @param role the specified role object
	 * @return true if the connection between role objects contradicts
	 */
	public boolean contradict(ConnectInObject connectInObject, Role role) {
		for ( ConnectObject connectObject:connectInObject._connectObjects) {
			ConnectOutObject connectOutObject = ( ConnectOutObject)connectObject;
			if ( connectOutObject._parent.equals( role))
				return true;

			if ( contradict( connectOutObject._parent._connectInObject, role))
				return true;
		}
		return false;
	}

	/**
	 * Returns true for connecting the role objects successfully.
	 * @param type the type of role objects
	 * @param roleConnectionMap the role objects connection hashtable
	 * @param graphics2D the graphics object of JAVA
	 * @return true for connecting the role objects successfully
	 */
	public boolean connect(String type, Map<Role, String[]> roleConnectionMap, Graphics2D graphics2D) {
		if ( type.equals( "agent_role"))
			return connect( roleConnectionMap, _agentRoleMap, graphics2D);
		else if ( type.equals( "spot_role"))
			return connect( roleConnectionMap, _spotRoleMap, graphics2D);

		return false;
	}

	/**
	 * @param roleConnectionMap
	 * @param roleMap
	 * @param graphics2D
	 * @return
	 */
	private boolean connect(Map<Role, String[]> roleConnectionMap, TreeMap<String, DrawObject> roleMap, Graphics2D graphics2D) {
		Iterator iterator = roleConnectionMap.entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			Role role = ( Role)entry.getKey();
			String[] names = ( String[])entry.getValue();
			if ( 2 > names.length)
				continue;

			for ( int i = 1; i < names.length; ++i) {
				Role r = ( Role)roleMap.get( names[ i]);
				if ( null == r)
					return false;

				role._connectInObject.connect( r._connectOutObject);
			}

			role._connectInObject.update_name_dimension( graphics2D);
		}

		return true;
	}

	/**
	 * Updates the specified coordinate for the top left corner of objects, and returns it.
	 * @param type the type of the object
	 * @param originCoordinate the specified coordinate for the top left corner of objects
	 * @return the coordinates for the top left corner
	 */
	public Point get_origin_coordinate(String type, Point originCoordinate) {
		for ( DrawObject drawObject:this) {
			if ( ( type.equals( "agent") && ( drawObject instanceof AgentObject))
				|| ( type.equals( "spot") && ( drawObject instanceof SpotObject))
				|| ( type.equals( "role") && ( drawObject instanceof Role))
				|| ( type.equals( "agent role") && ( drawObject instanceof AgentRole))
				|| ( type.equals( "spot role") && ( drawObject instanceof SpotRole))
				|| ( type.equals( "chart") && ( drawObject instanceof ChartObject))) {
				if ( null == originCoordinate)
					originCoordinate = new Point( drawObject._position);
				else {
					originCoordinate.x = Math.min( originCoordinate.x, drawObject._position.x);
					originCoordinate.y = Math.min( originCoordinate.y, drawObject._position.y);
				}
			}
		}
		return originCoordinate;
	}

	/**
	 * Creates the text which contains the relative coordinates of objects, and returns it.
	 * @param type the type of the object
	 * @param originCoordinate the origin coordinate
	 * @return the text which contains the relative coordinatesof objects
	 */
	public String get_graphic_properties(String type, Point originCoordinate) {
		String text = "";
		for ( DrawObject drawObject:this) {
			if ( ( type.equals( "role") && ( drawObject instanceof Role))
				|| ( type.equals( "agent role") && ( drawObject instanceof AgentRole))
				|| ( type.equals( "spot role") && ( drawObject instanceof SpotRole))
				|| ( type.equals( "chart") && ( drawObject instanceof ChartObject)))
				text += ( type + "\t" + drawObject._name + "\t"
					+ String.valueOf( drawObject._position.x - originCoordinate.x) + "\t"
					+ String.valueOf( drawObject._position.y - originCoordinate.y) + "\t"
					+ drawObject._imageFilename + "\n");
			else if ( ( type.equals( "agent") && ( drawObject instanceof AgentObject))
				|| ( type.equals( "spot") && ( drawObject instanceof SpotObject))) {
				EntityBase entityBase = ( EntityBase)drawObject;
				text += ( type + "\t" + entityBase._name + "\t"
					+ entityBase._number + "\t"
					+ String.valueOf( entityBase._position.x - originCoordinate.x) + "\t"
					+ String.valueOf( entityBase._position.y - originCoordinate.y) + "\t"
					+ entityBase._imageFilename + "\n");
			}
		}
		return text;
	}

	/**
	 * Creates the text which contains the chart properties, and returns it.
	 * @return the text which contains the chart properties
	 */
	public String get_chart_properties() {
		String text = "";
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof ChartObject))
				continue;

			ChartObject chartObject = ( ChartObject)drawObject;
			text += chartObject.get_properties();
		}
		return text;
	}

	/**
	 * @return
	 */
	public boolean exist_global_spot_and_role() {
		// TODO Auto-generated method stub
		return ( exist_global_spot() && exist_global_role());
	}

	/**
	 * @return
	 */
	public boolean exist_global_spot() {
		// TODO Auto-generated method stub
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				if ( drawObject.is_global_object())
					return true;
			}
		}
		return false;
	}

	/**
	 * @return
	 */
	public boolean exist_global_role() {
		// TODO Auto-generated method stub
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotRole) {
				if ( drawObject.is_global_object())
					return true;
			}
		}
		return false;
	}

	/**
	 * @param component
	 * @return
	 */
	public boolean create_global_spot_and_role(JComponent component) {
		// TODO どうやって作らせようか？考え中、、、
		if ( !LayerManager.get_instance().exist_global_spot()) {
			DrawObject drawObject = append_global_object( "spot", component);
			if ( null == drawObject)
				return false;

			if ( drawObject._nameDimension.width > drawObject._dimension.width)
				drawObject._position.x += ( ( drawObject._nameDimension.width - drawObject._dimension.width) / 2);
		}

		SpotObject spotObject = LayerManager.get_instance().get_spot( Constant._globalSpotName);
		if ( null == spotObject)
			return false;

		int height = ( spotObject._dimension.height + spotObject._nameDimension.height);

		if ( !LayerManager.get_instance().exist_global_role()) {
			DrawObject drawObject = append_global_object( "spot_role", component);
			if ( null == drawObject)
				return false;

			if ( drawObject._nameDimension.width > drawObject._dimension.width)
				drawObject._position.x += ( ( drawObject._nameDimension.width - drawObject._dimension.width) / 2);

			drawObject._position.y += ( height + Constant._visualShellRoleConnectionLength + 2 * Constant._visualShellRoleConnectionRadius);
		}

		return true;
	}

	/**
	 * @param range
	 */
	public void get_gis_range(double[] range) {
		// TODO Auto-generated method stub
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				SpotObject spotObject = ( SpotObject)drawObject;
				//if ( spotObject._gis.equals( gis))
				if ( !spotObject._gis.equals( ""))
					// TODO GISで追加されたスポットは全て同じ対象とすることに変更した
					spotObject.get_gis_range( range);
			}
		}
	}

	/**
	 * @param range
	 * @param ratio
	 */
	public void update_gis_coordinates(double[] range, double[] ratio) {
		// TODO Auto-generated method stub
		for ( DrawObject drawObject:this) {
			if ( drawObject instanceof SpotObject) {
				SpotObject spotObject = ( SpotObject)drawObject;
				//if ( spotObject._gis.equals( gis))
				if ( !spotObject._gis.equals( ""))
					// TODO GISで追加されたスポットは全て同じ対象とすることに変更した
					spotObject.update_gis_coordinates( range, ratio);
			}
		}
	}

	/**
	 * For debug print.
	 */
	public void print_agents() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof AgentObject))
				continue;

			AgentObject agentObject = ( AgentObject)drawObject;
			agentObject.print();
		}
	}

	/**
	 * For debug print.
	 */
	public void print_spots() {
		for ( DrawObject drawObject:this) {
			if ( !( drawObject instanceof SpotObject))
				continue;

			SpotObject spotObject = ( SpotObject)drawObject;
			spotObject.print();
		}
	}
}
