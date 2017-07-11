/*
 * 2005/02/07
 */
package soars.application.animator.object.entity.spot;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.Map;

import org.xml.sax.SAXException;

import soars.application.animator.object.entity.agent.AgentObject;
import soars.common.utility.xml.sax.Writer;

/**
 * The interface to manipulate the Spot object.
 * @author kurata / SOARS project
 */
public interface ISpotObjectManipulator {

	/**
	 * Returns true if this object has only image.
	 * @return true if this object has only image
	 */
	boolean is_image_object();

	/**
	 * Returns true if this object is initialized successfully.
	 * @param graphics2D the graphics object of JAVA
	 * @return true if this object is initialized successfully
	 */
	boolean setup(Graphics2D graphics2D);

	/**
	 * Clears all.
	 */
	void cleanup();

	/**
	 * Returns true for setting property successfully.
	 * @param name the name of the property
	 * @param value the value of the property
	 * @return true for setting property successfully
	 */
	boolean set_property(String name, String value);

	/**
	 * Returns the name of this spot.
	 * @return the name of this spot
	 */
	String get_name();

	/**
	 * Returns true if this spot is visible.
	 * @return true if this spot is visible
	 */
	boolean is_visible();

	/**
	 * Updates the size of this spot.
	 */
	void update_dimension();

	/**
	 * Appends the specified agent object on this spot.
	 * @param agentObject the specified agent object
	 */
	void append(AgentObject agentObject);

	/**
	 * Returns the position of this spot.
	 * @return the position of this spot
	 */
	Point get_position();

	/**
	 * Sets the specified values to the position of this spot.
	 * @param x the x values of the position
	 * @param y the y values of the position
	 */
	void set_position(int x, int y);

	/**
	 * Returns the image position of this spot.
	 * @return the image position of this spot
	 */
	Point get_image_position();

	/**
	 * Returns the size of this spot.
	 * @return the size of this spot
	 */
	Dimension get_dimension();

	/**
	 * @param cursorType
	 * @param deltaX
	 * @param deltaY
	 */
	void update_dimension(int cursorType, int deltaX, int deltaY);

	/**
	 * 
	 */
	void restore_image_size();

	/**
	 * Updates the maximum size of the area for drawing.
	 * @param preferredSize the size of the area for drawing
	 * @param spotPropertyMaxDimension the maximum size for spot property
	 * @param agentMaxDimension the maximum size for agent
	 * @param max the maximum number of agents on the spot
	 */
	void update_preferred_size(Dimension preferredSize, Dimension spotPropertyMaxDimension, Dimension agentMaxDimension, int max);

	/**
	 * Updates the maximum size of the area for drawing.
	 * @param dimension the size of the area for drawing
	 * @param spotPropertyMaxDimension the maximum size for spot property
	 * @param agentMaxDimension the maximum size for agent
	 * @param max the maximum number of agents on the spot
	 */
	void get_size(Dimension dimension, Dimension spotPropertyMaxDimension, Dimension agentMaxDimension, int max);

	/**
	 * Returns true if the specified image file is loaded successfully.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is loaded successfully
	 */
	boolean load_image(String filename);

	/**
	 * Returns true if the specified image file is loaded from resource successfully.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is loaded from resource successfully
	 */
	boolean load_image_from_resource(String filename);

	/**
	 * Returns true if this spot uses the specified image file. 
	 * @param filename the name of the specified image file
	 * @return true if this spot uses the specified image file
	 */
	public boolean uses_this_image(String filename);

	/**
	 * Sets the specified new image if this spot uses the specified image.
	 * @param originalFilename the specified image file name
	 * @param newFilename the specified new image file name
	 */
	public void update_image(String originalFilename, String newFilename);

	/**
	 * @param name
	 * @param number 
	 * @param filename 
	 */
	public void set_image(String name, String number, String filename);

	/**
	 * Returns true for setting the specified data to this spot successfully.
	 * @param name the name of spot
	 * @param number the number in the name of spot
	 * @param x the x value of the position
	 * @param y the y value of the position
	 * @param counterMap the number of spot hashtable(name[String] - the number of spot[integer])
	 * @return true for setting the specified data to this spot successfully
	 */
	boolean update(String name, String number, int x, int y, Map<String, Integer> counterMap);

	/**
	 * Draws this spot.
	 * @param graphics2D the graphics object of JAVA
	 * @param rectangle the visible rectangle
	 * @param imageObserver an asynchronous update interface for receiving notifications about Image information as the Image is constructed
	 */
	void draw(Graphics2D graphics2D, Rectangle rectangle, ImageObserver imageObserver);

	/**
	 * Returns the position of the specified agent on this spot.
	 * @param agentObject the specified agent object
	 * @param pack whether agents are packed for drawing
	 * @return the position of the specified agent on this spot
	 */
	Point get_agent_position(AgentObject agentObject, boolean pack);

	/**
	 * Returns the position of the specified agent on this spot.
	 * @param agentObject the specified agent object
	 * @param index the index of the specified agent object on this spot
	 * @param size the number of agents on this spot
	 * @param pack whether agents are packed for drawing
	 * @return the position of the specified agent on this spot
	 */
	Point get_agent_position(AgentObject agentObject, int index, int size, boolean pack);

	/**
	 * Returns the spot which contains the specified position.
	 * @param point the specified position
	 * @return the spot which contains the specified position
	 */
	ISpotObjectManipulator get_spot(Point point);

	/**
	 * Sets the selection of this spot.
	 * @param selected whether this spot is selected
	 */
	void select(boolean selected);

	/**
	 * Sets the selection of all spots and image objects.
	 * @param selected whether all spots and image objects are selected
	 */
	void select_all_objects(boolean selected);

	/**
	 * Sets the selection of all spots.
	 * @param selected whether all spots are selected
	 */
	void select_all_spot_objects(boolean selected);

	/**
	 * Sets the selection of all image objects.
	 * @param selected whether all image objects are selected
	 */
	void select_all_image_objects(boolean selected);

	/**
	 * Selects the all spots and image objects in the specified rectangle
	 * @param rectangle the specified rectangle
	 */
	void select(Rectangle rectangle);

	/**
	 * Move this spot to top.
	 */
	public void move_to_top();

	/**
	 * Move this spot to bottom.
	 */
	public void move_to_bottom();

	/**
	 * Move this spot to front.
	 */
	public void move_to_front();

	/**
	 * Move this spot to back.
	 */
	public void move_to_back();

	/**
	 * Returns true if this spot is selected.
	 * @return true if this spot is selected
	 */
	boolean selected();

	/**
	 * Returns true if the position is correct.
	 * @param deltaX the x-coordinates of the moving
	 * @param deltaY the y-coordinates of the moving
	 * @return true if the position is correct
	 */
	boolean test(int deltaX, int deltaY);

	/**
	 * Moves this spot.
	 * @param deltaX the x-coordinates of the moving
	 * @param deltaY the y-coordinates for moving
	 * @param modify whether this spot of the modified
	 */
	void move(int deltaX, int deltaY, boolean modify);

	/**
	 * Moves this spot to the specified position.
	 * @param x the x-coordinates of the specified position
	 * @param y the y-coordinates of the specified position
	 * @param modify
	 */
	void move_to(int x, int y, boolean modify);

	/**
	 * Returns true for editting this spot successfully.
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting this spot successfully
	 */
	boolean edit(Frame frame);

	/**
	 * Returns tool tip text of this spot.
	 * @param state the current state
	 * @return tool tip text of this spot
	 */
	String get_tooltip_text(String state);

	/**
	 * Returns whether the specified name is unique.
	 * @param name the specified name
	 * @return whether the specified name is unique
	 */
	public boolean is_unique_name(String name);

	/**
	 * Returns true for writing this spot data successfully.
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this spot data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	boolean write(Writer writer) throws SAXException;

	/**
	 * Returns true for writing this spot graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this spot graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException;
}
