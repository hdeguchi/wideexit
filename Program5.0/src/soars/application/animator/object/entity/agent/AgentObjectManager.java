/*
 * 2005/02/07
 */
package soars.application.animator.object.entity.agent;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.xml.sax.SAXException;

import soars.application.animator.main.internal.ObjectManager;
import soars.application.animator.object.entity.spot.SpotObjectManager;
import soars.application.animator.object.transition.agent.AgentTransitionManager;
import soars.application.animator.object.transition.spot.SpotTransitionManager;
import soars.common.utility.xml.sax.Writer;

/**
 * The AgentObject hashtable(name(String) - AgentObject).
 * @author kurata / SOARS project
 */
public class AgentObjectManager extends HashMap<String, AgentObject> {

	/**
	 * 
	 */
	private ObjectManager _objectManager = null;

	/**
	 * 
	 */
	protected Vector<AgentObject> _order = new Vector<AgentObject>();

	/**
	 * @param objectManager
	 */
	public AgentObjectManager(ObjectManager objectManager) {
		super();
		_objectManager = objectManager;
	}

	/** Copy constructor(For duplication)
	 * @param agentObjectManager
	 * @param objectManager
	 */
	public AgentObjectManager(AgentObjectManager agentObjectManager, ObjectManager objectManager) {
		// TODO Auto-generated constructor stub
		_objectManager = objectManager;
		for ( AgentObject agentObject:agentObjectManager._order) {
			AgentObject ao = new AgentObject( agentObject, objectManager);
			_order.add( ao);
			put( ao._name, ao);
		}
	}

	/**
	 * @param spotObjectManager new object of duplication
	 * @param srcAgentObjectManager source of duplication
	 */
	public void adjust_for_duplication(SpotObjectManager spotObjectManager, AgentObjectManager srcAgentObjectManager) {
		// TODO Auto-generated method stub
		for ( int i = 0; i < _order.size(); ++i) {
			if ( null == srcAgentObjectManager._order.get( i)._spotObjectManipulator)
				continue;

			_order.get( i)._spotObjectManipulator = spotObjectManager.get( srcAgentObjectManager._order.get( i)._spotObjectManipulator.get_name());
		}
	}

	/**
	 * Returns the array of the AgentObjects.
	 * @return the array of the AgentObjects
	 */
	public Vector<AgentObject> get_order() {
		return _order;
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.cleanup();
		}
		clear();

		_order.clear();
	}

	/**
	 * Returns true for writing all agents data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all agents data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(Writer writer) throws SAXException {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( !agentObject.write( writer))
				return false;
		}
		return true;
	}

	/**
	 * Returns true for writing all agents graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing all agents graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( !agentObject.write_graphic_data( rootDirectory, writer))
				return false;
		}
		return true;
	}

	/**
	 * Appends the specified agent object.
	 * @param name the name of the specified agent
	 * @param agentObject the specified agent object
	 */
	public void append(String name, AgentObject agentObject) {
		put( name, agentObject);
		_order.add( agentObject);
	}

	/**
	 * Initializes all.
	 */
	public void setup() {
		for ( AgentObject agentObject:_order)
			agentObject.append();
	}

	/**
	 * Sets all agents to the scenario.
	 * @param index the index of the scenario
	 * @param agentTransitionManager the scenario data manager of agents
	 * @param spotTransitionManager the scenario data manager of spots
	 */
	public void set(int index, AgentTransitionManager agentTransitionManager, SpotTransitionManager spotTransitionManager) {
		for ( AgentObject agentObject:_order)
			agentTransitionManager.set( index, agentObject, spotTransitionManager);
	}

	/**
	 * Sets the new name to the specified agent.
	 * @param agentObject the specified agent object
	 * @param name the new name
	 * @param graphics2D the graphics object of JAVA
	 */
	public void rename(AgentObject agentObject, String name, Graphics2D graphics2D) {
		remove( agentObject._name);
		agentObject.rename( name, graphics2D);
		put( name, agentObject);
	}

	/**
	 * Updates the agent which has the same name as the specified agent.
	 * @param name the agent name
	 * @param agentObject the specified agent
	 */
	public void update(String name, AgentObject agentObject) {
		AgentObject ao = get( name);
		if ( null == ao)
			return;

		ao.update( agentObject);
	}

	/**
	 * Updates the positions of the all agents.
	 */
	public void arrange() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.arrange();
		}
	}

	/**
	 * Updates the all agents.
	 */
	public void update_dimension() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.update_dimension();
		}
	}

	/**
	 * Returns the maximum size of the area for drawing.
	 * @return the maximum size of the area for drawing
	 */
	public Dimension get_max_image_size() {
		Dimension agent_property_max_dimension = _objectManager._scenarioManager._agentPropertyManager.get_max_image_size();
		Dimension dimension = new Dimension();
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.get_size( dimension, agent_property_max_dimension);
		}
		return dimension;
	}

//	/**
//	 * @param agentPropertyMaxDimension
//	 * @param imageObserver
//	 */
//	public void prepare_for_animation(Dimension agentPropertyMaxDimension, ImageObserver imageObserver) {
//		for ( AgentObject agentObject:_order)
//			agentObject.prepare_for_animation( agentPropertyMaxDimension, imageObserver);
//	}

	/**
	 * Draws the all agents.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void draw(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		for ( AgentObject agentObject:_order)
			agentObject.draw( graphics2D, rectangle, imageObserver);
	}

	/**
	 * Animates the all agents.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	public void animate(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver) {
		for ( AgentObject agentObject:_order)
			agentObject.animate( graphics2D, rectangle, imageObserver);
	}

	/**
	 * Returns the agent which contains the specified point.
	 * @param point the specified point
	 * @param state the current state
	 * @return the agent which contains the specified point
	 */
	public AgentObject get_agent(Point point, String state) {
		for ( int i = _order.size() - 1; i >= 0; --i) {
			if ( !_order.get( i)._visible || !_order.get( i).contains( point, state))
				continue;

			return _order.get( i);
		}
		return null;
	}

	/**
	 * Sets the specified selection to the all agents.
	 * @param selected the specified selection
	 */
	public void select_all_objects(boolean selected) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.select( selected);
		}
	}

	/**
	 * Selects the agents contained the specified rectangle.
	 * @param rectangle the specified rectangle
	 */
	public void select(Rectangle rectangle) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.select( rectangle);
		}
	}

	/**
	 * Returns true if the selected agent exists.
	 * @return true if the selected agent exists
	 */
	public boolean exist_selected_agent() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( null == agentObject._spotObjectManipulator)
				continue;

			if ( agentObject._spotObjectManipulator.is_visible()
				&& agentObject._visible && agentObject._selected)
				return true;
		}
		return false;
	}

	/**
	 * Gets the selected agents.
	 * @param agents the array of the selected agents
	 */
	public void get_selected_agent(Vector<AgentObject> agents) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( null == agentObject._spotObjectManipulator)
				continue;

			if ( agentObject._spotObjectManipulator.is_visible()
				&& agentObject._visible && agentObject._selected)
				agents.add( agentObject);
		}
	}

	/**
	 * Returns true if the visible agent exists.
	 * @return true if the visible agent exists
	 */
	public boolean exist_visible_agent() {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( null == agentObject._spotObjectManipulator)
				continue;

			if ( agentObject._spotObjectManipulator.is_visible() && agentObject._visible)
				return true;
		}
		return false;
	}

	/**
	 * Gets the visible agents.
	 * @param agents the array of the visible agents
	 */
	public void get_visible_agent(Vector<AgentObject> agents) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( agentObject._visible)
				agents.add( agentObject);
//			if ( null == agentObject._spotObjectManipulator)
//				continue;
//
//			if ( agentObject._spotObjectManipulator.is_visible() && agentObject._visible)
//				agents.add( agentObject);
		}
	}

	/**
	 * Returns true if an agent uses the specified image file.
	 * @param filename the specified image file name
	 * @return true if an agent uses the specified image file
	 */
	public boolean uses_this_image(String filename) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			if ( agentObject.uses_this_image( filename))
				return true;
		}
		return false;
	}

	/**
	 * Sets the specified new image the agents which use the specified image.
	 * @param originalFilename the specified image file name
	 * @param newFilename the specified new image file name
	 */
	public void update_image(String originalFilename, String newFilename) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.update_image( originalFilename, newFilename);
		}
	}

	/**
	 * @param name
	 * @param number
	 * @param filename
	 */
	public void set_image(String name, String number, String filename) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			AgentObject agentObject = ( AgentObject)entry.getValue();
			agentObject.set_image( name, number, filename);
		}
	}
}
