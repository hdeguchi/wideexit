/*
 * 2005/01/31
 */
package soars.application.animator.main.internal;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JComponent;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import soars.application.animator.file.importer.GraphicDataSaxLoader;
import soars.application.animator.file.importer.Importer;
import soars.application.animator.file.loader.FileProperty;
import soars.application.animator.file.loader.SaxLoader;
import soars.application.animator.main.Administrator;
import soars.application.animator.main.Constant;
import soars.application.animator.main.Environment;
import soars.application.animator.main.MainFrame;
import soars.application.animator.main.ResourceManager;
import soars.application.animator.main.internal.AnimatorView;
import soars.application.animator.main.internal.AnimatorViewFrame;
import soars.application.animator.main.internal.WindowProperty;
import soars.application.animator.object.entity.agent.AgentObject;
import soars.application.animator.object.entity.agent.AgentObjectManager;
import soars.application.animator.object.entity.base.EntityBase;
import soars.application.animator.object.entity.base.edit.objects.EditEntitiesDlg;
import soars.application.animator.object.entity.base.edit.objects.EntityComparator;
import soars.application.animator.object.entity.spot.ISpotObjectManipulator;
import soars.application.animator.object.entity.spot.SpotObject;
import soars.application.animator.object.entity.spot.SpotObjectManager;
import soars.application.animator.object.entity.spot.SpotPositionComparator;
import soars.application.animator.object.scenario.ScenarioManager;
import soars.common.utility.swing.image.ImageProperty;
import soars.common.utility.swing.image.ImagePropertyManager;
import soars.common.utility.swing.message.IMessageCallback;
import soars.common.utility.swing.message.MessageDlg;
import soars.common.utility.tool.common.Tool;
import soars.common.utility.tool.file.FileUtility;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.xml.sax.Writer;

/**
* The all object manager class.
 * @author kurata / SOARS project
 */
public class ObjectManager implements IMessageCallback {

	/**
	 * 
	 */
	static private Object _lock = new Object();

	/**
	 * 
	 */
	public int _id = 0;

	/**
	 * 
	 */
	public String[] _spotLog = new String[] { "agents", "$Spot"};

	/**
	 * 
	 */
	public WindowProperty _windowProperty = new WindowProperty( AnimatorViewFrame._minimumWidth, AnimatorViewFrame._minimumHeight);

	/**
	 * Instance of the Animator view class.
	 */
	public AnimatorView _animatorView = null;

	/**
	 * Instance of the SpotObjectManager class.
	 */
	public SpotObjectManager _spotObjectManager = null;

	/**
	 * Instance of the AgentObjectManager class.
	 */
	public AgentObjectManager _agentObjectManager = null;

	/**
	 * Instance of the ScenarioManager class.
	 */
	public ScenarioManager _scenarioManager = null;

	/**
	 * 
	 */
	private BufferedImage _bufferedImage = null;

	/**
	 * 
	 */
	private Color _backgroundColor = new Color( 255, 255, 255);

	/**
	 * 
	 */
	private Color _rubberbandColor = new Color( 0, 0, 0);

	/**
	 * 
	 */
	private Dimension _preferredSize = new Dimension();

	/**
	 * Creates the all object manager class.
	 * @param animatorView
	 */
	public ObjectManager(AnimatorView animatorView) {
		super();
		_animatorView = animatorView;
		_spotObjectManager = new SpotObjectManager( "", null, this, ( Graphics2D)animatorView.getGraphics());
		_agentObjectManager = new AgentObjectManager( this);
		_scenarioManager = new ScenarioManager( animatorView);
	}

	/**
	 * @param id
	 * @param srcObjectManager
	 * @param animatorView
	 */
	public ObjectManager(int id, ObjectManager srcObjectManager, AnimatorView animatorView) {
		_id = id;
		_spotLog[ 0] = srcObjectManager._spotLog[ 0];
		_spotLog[ 1] = srcObjectManager._spotLog[ 1];
		_animatorView = animatorView;
		_spotObjectManager = new SpotObjectManager( srcObjectManager._spotObjectManager, null, this);
		_agentObjectManager = new AgentObjectManager( srcObjectManager._agentObjectManager, this);
		_spotObjectManager.adjust_for_duplication( _agentObjectManager, srcObjectManager._spotObjectManager);
		_agentObjectManager.adjust_for_duplication( _spotObjectManager, srcObjectManager._agentObjectManager);
		_scenarioManager = new ScenarioManager( srcObjectManager._scenarioManager, animatorView, this);
		_scenarioManager.load();
		_agentObjectManager.arrange();
	}

	/**
	 * @return
	 */
	public String get_title_prefix() {
		return ( "[" + get_prefix() + "." + _spotLog[ 1] + "] - ");
	}

	/**
	 * @return
	 */
	private String get_prefix() {
		return ( _spotLog[ 0].equals( "agents") ? "Agent" : "Spot");
	}

	/**
	 * Clears all.
	 */
	public void cleanup() {
		_agentObjectManager.cleanup();
		_spotObjectManager.cleanup();
		_scenarioManager.cleanup();
		_preferredSize.setSize( 0, 0);
	}

	/**
	 * Updates the size of the animation area.
	 */
	public void set_preferred_size() {
		update_preferred_size();
		_animatorView.setPreferredSize( _preferredSize);
		_animatorView.updateUI();
	}

	/**
	 * Updates the size of the animation area.
	 */
	public void update_preferred_size() {
		_spotObjectManager.update_preferred_size( _preferredSize,
			_scenarioManager._spotPropertyManager.get_max_image_size(),
			_agentObjectManager.get_max_image_size(),
			_scenarioManager._headerObject._max);
	}

	/**
	 * Updates the size of the animation area.
	 * @param component the base class for all Swing components
	 */
	public void update_preferred_size(JComponent component) {
		Dimension preferredSize = new Dimension();
		_spotObjectManager.update_preferred_size( preferredSize,
			_scenarioManager._spotPropertyManager.get_max_image_size(),
			_agentObjectManager.get_max_image_size(),
			_scenarioManager._headerObject._max);
		if ( !_preferredSize.equals( preferredSize)) {
			_preferredSize.setSize( preferredSize);
			component.setPreferredSize( _preferredSize);
			component.updateUI();
		}
	}

	/**
	 * Returns the size of the animation area.
	 * @return the size of the animation area
	 */
	public Dimension get_preferred_size() {
		return _preferredSize;
	}

	/**
	 * Returns the size of the animation area.
	 * @return the size of the animation area
	 */
	public Dimension get_size() {
		Dimension dimension = new Dimension();
		_spotObjectManager.get_size( dimension,
			_scenarioManager._spotPropertyManager.get_max_image_size(),
			_agentObjectManager.get_max_image_size(),
			_scenarioManager._headerObject._max);
		return dimension;
	}

	/**
	 * Changes the buffer size for animation.
	 */
	public void resize() {
		if ( null == _animatorView || 0 >= _animatorView.getWidth() || 0 >= _animatorView.getHeight())
			return;

		_bufferedImage = new BufferedImage( _animatorView.getWidth(), _animatorView.getHeight(), BufferedImage.TYPE_INT_RGB);
	}

	/**
	 * Prepares for animation.
	 */
	public void prepare_for_animation() {
		MessageDlg.execute( MainFrame.get_instance(), ResourceManager.get_instance().get( "application.title"), true,
			"prepare_for_animation", ResourceManager.get_instance().get( "file.open.show.message"),
			new Object[] { _animatorView}, this, MainFrame.get_instance());
	}

	/* (non-Javadoc)
	 * @see soars.common.utility.swing.message.IMessageCallback#message_callback(java.lang.String, java.lang.Object[], soars.common.utility.swing.message.MessageDlg)
	 */
	public boolean message_callback(String id, Object[] objects, MessageDlg messageDlg) {
		if ( id.equals( "prepare_for_animation"))
			on_prepare_for_animation( ( JComponent)objects[ 0]);

		return false;
	}

	/**
	 * Prepares for animation.
	 * @param component the base class for all Swing components
	 */
	private void on_prepare_for_animation(JComponent component) {
		_scenarioManager.reset();
		_scenarioManager.read();

		Dimension dimension = get_size();

//		_spotObjectManager.prepare_for_animation(
//			dimension, SpotPropertyManager.get_instance().get_max_image_size(), component);
//		_agentObjectManager.prepare_for_animation(
//			AgentPropertyManager.get_instance().get_max_image_size(), component);

		_bufferedImage = new BufferedImage( dimension.width, dimension.height, BufferedImage.TYPE_INT_RGB);

		component.setPreferredSize( dimension);
		component.updateUI();
	}

	/**
	 * Draws the objects in the visible rectangle.
	 * @param graphics the graphics object of JAVA
	 * @param startPoint the start point of the rubber band
	 * @param endPoint the end point of the rubber band
	 */
	public void draw(Graphics graphics, Point startPoint, Point endPoint) {
		//System.out.println( _animatorView.getVisibleRect().x + ", " + _animatorView.getVisibleRect().y + ", " + _animatorView.getVisibleRect().width + ", " + _animatorView.getVisibleRect().height);
		synchronized( _lock) {
			if ( null == _bufferedImage)
				resize();

			Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
			graphics2D.setBackground( _backgroundColor);
			Rectangle rectangle = _animatorView.getVisibleRect();
			graphics2D.clearRect( rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//			graphics2D.clearRect( 0, 0, _bufferedImage.getWidth(), _bufferedImage.getHeight());

			if ( null != startPoint) {
				graphics2D.setColor( _rubberbandColor);
				graphics2D.drawRect(
					Math.min( startPoint.x, endPoint.x),
					Math.min( startPoint.y, endPoint.y),
					Math.abs( startPoint.x - endPoint.x),
					Math.abs( startPoint.y - endPoint.y));
			}

			_spotObjectManager.draw( graphics2D, rectangle, _animatorView);
			_agentObjectManager.draw( graphics2D, rectangle, _animatorView);
			graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y,
				rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
				rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()),
				rectangle.x, rectangle.y,
				rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
				rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()), _animatorView);
//			graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
//				rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, component);
//			graphics.drawImage( _bufferedImage, 0, 0, component);
			graphics2D.dispose();
		}
	}

	/**
	 * Animates the objects in the visible rectangle.
	 * @param graphics the graphics object of JAVA
	 */
	public void animate(Graphics graphics) {
		Graphics2D graphics2D = ( Graphics2D)_bufferedImage.getGraphics();
		graphics2D.setBackground( _backgroundColor);
		Rectangle rectangle = _animatorView.getVisibleRect();
		//System.out.println( rectangle.x + ", " + rectangle.y + ", " + rectangle.width + ", " + rectangle.height);
		graphics2D.clearRect( rectangle.x, rectangle.y, rectangle.width, rectangle.height);
//		graphics2D.clearRect( 0, 0, _bufferedImage.getWidth(), _bufferedImage.getHeight());
		_spotObjectManager.animate( graphics2D, rectangle, _animatorView);
		_agentObjectManager.animate( graphics2D, rectangle, _animatorView);
		graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y,
			rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
			rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()),
			rectangle.x, rectangle.y,
			rectangle.x + Math.min( rectangle.width, _bufferedImage.getWidth()),
			rectangle.y + Math.min( rectangle.height, _bufferedImage.getHeight()), _animatorView);
//		graphics.drawImage( _bufferedImage, rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height,
//			rectangle.x, rectangle.y, rectangle.x + rectangle.width, rectangle.y + rectangle.height, _animatorView);
//		graphics.drawImage( _bufferedImage, 0, 0, _animatorView);
		graphics2D.dispose();
	}

	/**
	 * Returns true if loading the data from the specified file successfully.
	 * @param file the specified file
	 * @param fileProperties
	 * @param graphics2D the graphics object of JAVA
	 * @return true if loading the data from the specified file successfully
	 */
	public boolean load(File file, Vector<FileProperty> fileProperties, Graphics2D graphics2D) {
		if ( !fileProperties.isEmpty()) {
			if ( !SaxLoader.execute( file, fileProperties, this, graphics2D)) {
				cleanup();
				return false;
			}
		} else {
			if ( !SaxLoader.execute( file, this, graphics2D)) {
				cleanup();
				return false;
			}
		}

		update_preferred_size();

		System.gc();

		return true;
	}

	/**
	 * @param windowProperty
	 * @param file
	 * @return
	 */
	public boolean save_as(WindowProperty windowProperty, File file) {
		_windowProperty = windowProperty;
		return _animatorView.save_as( windowProperty, file);
	}

	/**
	 * @param graphics2D
	 * @return
	 */
	public boolean import_data(Graphics2D graphics2D) {
		Importer importer = new Importer( Administrator.get_instance().get_root_directory(), this);
		if ( !importer.execute( graphics2D)) {
			cleanup();
			return false;
		}

		return true;
	}

	/**
	 * 
	 */
	public void update_graphic_properties() {
		update_graphic_properties( new File( Administrator.get_instance().get_root_directory(), Constant._graphicPropertiesFilename), ( Graphics2D)_animatorView.getGraphics());
	}

	/**
	 * @param file
	 * @param graphics2D
	 */
	private void update_graphic_properties(File file, Graphics2D graphics2D) {
		if ( null == file || !file.exists() || !file.isFile() || !file.canRead())
			return;

		String graphicProperties = FileUtility.read_text_from_file( file, "UTF-8");

		String[] lines = graphicProperties.split( "\n");
		if ( null == lines)
			return;

		Map<String, Integer> spotCounterMap = new HashMap<String, Integer>();

		for ( int i = 0; i < lines.length; ++i) {
			if ( lines[ i].startsWith( "image_property")) {
				String[] words = Tool.split( lines[ i], '\t');
				if ( null == words || 4 != words.length)
					continue;

				ImagePropertyManager.get_instance().put( words[ 1],
					new ImageProperty( Integer.parseInt( words[ 2]), Integer.parseInt( words[ 3])));
			} else if ( lines[ i].startsWith( "spot") || lines[ i].startsWith( "agent")) {
				String[] words = Tool.split( lines[ i], '\t');
				if ( null == words || 6 != words.length) {
					words = Tool.split( lines[ i], ',');
					if ( null == words || 5 != words.length)
						continue;

					if ( words[ 0].equals( "spot"))
						_spotObjectManager.update( words[ 1], words[ 2],
							( new Integer( words[ 3])).intValue(),
							( new Integer( words[ 4])).intValue(),
							spotCounterMap);

					continue;
				}

				if ( words[ 0].equals( "spot"))
					_spotObjectManager.update( words[ 1], words[ 2],
						( new Integer( words[ 3])).intValue(),
						( new Integer( words[ 4])).intValue(),
						spotCounterMap);

				// ここでイメージを設定する！
				if ( words[ 5].equals( "")
					|| null == ImagePropertyManager.get_instance().get( words[ 5]))
					continue;

				if ( !Administrator.get_instance().exist_image_directory() || !Administrator.get_instance().exist_thumbnail_image_directory())
					continue;

				File imagefile = new File( Administrator.get_instance().get_image_directory(), words[ 5]);
				if ( !imagefile.exists() || !imagefile.isFile() || !imagefile.canRead())
					continue;

				imagefile = new File( Administrator.get_instance().get_thumbnail_image_directory(), words[ 5]);
				if ( !imagefile.exists() || !imagefile.isFile() || !imagefile.canRead())
					continue;

				if ( _spotLog[ 0].equals( "agents")) {
					if ( words[ 0].equals( "spot"))
						_spotObjectManager.set_image( words[ 1], words[ 2], words[ 5]);
					else if ( words[ 0].equals( "agent"))
						_agentObjectManager.set_image( words[ 1], words[ 2], words[ 5]);
				} else
					_agentObjectManager.set_image( words[ 1], words[ 2], words[ 5]);
			}
		}
	}

	/**
	 * Returns true if importing the graphic data from the specified file successfully.
	 * @param file the specified file
	 * @param rootDirectory
	 * @param graphics2D the graphics object of JAVA
	 * @return true if importing the graphic data from the specified file successfully
	 */
	public boolean import_graphic_data(File file, File rootDirectory, Graphics2D graphics2D) {
		if ( !GraphicDataSaxLoader.execute( file, rootDirectory, this, graphics2D))
			return false;

//		update_default_image();

		_agentObjectManager.update_dimension();
		_spotObjectManager.update_dimension();

		_scenarioManager.reset();

		update_preferred_size();

		return true;
	}

	/**
	 * Returns true if exporting the graphic data to the specified file successfully.
	 * @param file the specified file
	 * @param rootDirectory
	 * @return true if exporting the graphic data to the specified file successfully
	 */
	public boolean export_graphic_data(File file, File rootDirectory) {
		return _animatorView.export_graphic_data( file, rootDirectory);
	}

	/**
	 * @param component the base class for all Swing components
	 */
	public void on_edit_common_property(JComponent component) {
//		update_default_image();

		_agentObjectManager.update_dimension();
		_spotObjectManager.update_dimension();

		_agentObjectManager.arrange();

		update_preferred_size( component);

//		return true;
	}

//	/**
//	 * 
//	 */
//	private void update_default_image() {
//		AgentPropertyManager.get_instance().update_default_image(
//			CommonProperty.get_instance()._agent_width,
//			CommonProperty.get_instance()._agent_height);
//		SpotPropertyManager.get_instance().update_default_image(
//			CommonProperty.get_instance()._spot_width,
//			CommonProperty.get_instance()._spot_height);
//	}

	/**
	 * Returns true for editting the agent data successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the agent data successfully
	 */
	public boolean edit_agent(JComponent component, Frame frame) {
		EditEntitiesDlg editObjectsDlg
			= new EditEntitiesDlg( frame,
				get_title_prefix() + ResourceManager.get_instance().get( "edit.agents.dialog.title"),
				true,
				false,
				Environment._openAgentImageDirectoryKey,
				_agentObjectManager.get_order(),
				this);
		if ( !editObjectsDlg.do_modal())
			return false;

		update_preferred_size( component);

		return true;
	}

	/**
	 * Returns true for editting the spot data successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the spot data successfully
	 */
	public boolean edit_spot(JComponent component, Frame frame) {
		EditEntitiesDlg editObjectsDlg
			= new EditEntitiesDlg( frame,
				get_title_prefix() + ResourceManager.get_instance().get( "edit.spots.dialog.title"),
				true,
				true,
				Environment._openSpotImageDirectoryKey,
				_spotObjectManager.get_order(),
				this);
		if ( !editObjectsDlg.do_modal())
			return false;

		_agentObjectManager.arrange();

		update_preferred_size( component);

		return true;
	}

	/**
	 * Sets the selection of all objects.
	 * @param selected whether all objects are selected
	 */
	public void select_all_objects(boolean selected) {
		_spotObjectManager.select_all_objects( selected);
	}

	/**
	 * Selects the all objects in the specified rectangle
	 * @param rectangle the specified rectangle
	 */
	public void select_object(Rectangle rectangle) {
		_spotObjectManager.select( rectangle);
	}

	/**
	 * Returns true for editting the properties of all agents successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the properties of all agents successfully
	 */
	public boolean edit_agent_property(JComponent component, Frame frame) {
		if ( !_scenarioManager._agentPropertyManager.edit( get_title_prefix(), frame))
			return false;

		update_preferred_size( component);
		return true;
	}

	/**
	 * Returns true for editting the properties of all spots successfully.
	 * @param component the base class for all Swing components
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for editting the properties of all spots successfully
	 */
	public boolean edit_spot_property(JComponent component, Frame frame) {
		if ( !_scenarioManager._spotPropertyManager.edit( get_title_prefix(), frame))
			return false;

		update_preferred_size( component);
		return true;
	}

	/**
	 * Returns true for retrieving the property of agent successfully.
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for retrieving the property of agent successfully
	 */
	public boolean retrieve_agent_property(Frame frame) {
		Vector<AgentObject> agents = new Vector<AgentObject>();
		_agentObjectManager.get_visible_agent( agents);

		if ( agents.isEmpty())
			return false;

		EntityBase[] entityBases = ( EntityBase[])agents.toArray( new EntityBase[ 0]);
		QuickSort.sort( entityBases, new EntityComparator( true, false));

		return _scenarioManager.retrieve_agent_property( entityBases, _spotLog[ 0] + "/", get_title_prefix(), frame, _animatorView.getParent());
	}

	/**
	 * Returns true for retrieving the property of spot successfully.
	 * @param frame the Frame from which the dialog is displayed
	 * @return true for retrieving the property of spot successfully
	 */
	public boolean retrieve_spot_property(Frame frame) {
		Vector<ISpotObjectManipulator> spots = new Vector<ISpotObjectManipulator>();
		_spotObjectManager.get_visible_spot( spots);

		if ( spots.isEmpty())
			return false;

		EntityBase[] entityBases = ( EntityBase[])spots.toArray( new EntityBase[ 0]);
		QuickSort.sort( entityBases, new EntityComparator( true, false));

		return _scenarioManager.retrieve_spot_property( entityBases, _spotLog[ 0] + "/", get_title_prefix(), frame, _animatorView.getParent());
	}

	/**
	 * Flushes top the selected objects.
	 */
	public void flush_top() {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		_spotObjectManager.get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int top = 0;
		for ( int i = 0; i < objects.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
			Point position = spotObjectManipulator.get_position();
			if ( 0 == i || position.y < top)
				top = position.y;
		}

		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			spotObjectManipulator.move( 0, top - position.y, true);
		}

		update_preferred_size( _animatorView);
		_animatorView.repaint();
	}

	/**
	 * Flushes bottom the selected objects.
	 */
	public void flush_bottom() {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		_spotObjectManager.get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int bottom = 0;
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			if ( ( position.y + dimension.height) > bottom)
				bottom = ( position.y + dimension.height);
		}

		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			spotObjectManipulator.move( 0, bottom - dimension.height - position.y, true);
		}

		update_preferred_size( _animatorView);
		_animatorView.repaint();
	}

	/**
	 * Flushes left the selected objects.
	 */
	public void flush_left() {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		_spotObjectManager.get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int left = 0;
		for ( int i = 0; i < objects.size(); ++i) {
			ISpotObjectManipulator spotObjectManipulator = ( ISpotObjectManipulator)objects.get( i);
			Point position = spotObjectManipulator.get_position();
			if ( 0 == i || position.x < left)
				left = position.x;
		}

		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			spotObjectManipulator.move( left - position.x, 0, true);
		}

		update_preferred_size( _animatorView);
		_animatorView.repaint();
	}

	/**
	 * Flushes the selected objects right.
	 */
	public void flush_right() {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		_spotObjectManager.get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 2 > objects.size())
			return;

		int right = 0;
		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			if ( ( position.x + dimension.width) > right)
				right = ( position.x + dimension.width);
		}

		for ( ISpotObjectManipulator spotObjectManipulator:objects) {
			Point position = spotObjectManipulator.get_position();
			Dimension dimension = spotObjectManipulator.get_dimension();
			spotObjectManipulator.move( right - dimension.width - position.x, 0, true);
		}

		update_preferred_size( _animatorView);
		_animatorView.repaint();
	}

	/**
	 * Makes vertical gaps between the selected objects equal.
	 */
	public void vertical_equal_layout() {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		_spotObjectManager.get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 3 > objects.size())
			return;

		ISpotObjectManipulator[] spotObjectManipulators = sort_spots( objects, true);

		int top = 0, bottom = 0, sum = 0;
		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			Point position = spotObjectManipulators[ i].get_position();
			Dimension dimension = spotObjectManipulators[ i].get_dimension();
			sum += dimension.height;
			if ( 0 == i || position.y < top)
				top = position.y;
			if ( ( position.y + dimension.height) > bottom)
				bottom = ( position.y + dimension.height);
		}

		int space = ( ( bottom - top - sum) / ( spotObjectManipulators.length - 1));

		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			if ( 0 < i)
				spotObjectManipulators[ i].move( 0, top - spotObjectManipulators[ i].get_position().y, true);

			top += ( spotObjectManipulators[ i].get_dimension().height + space);
		}

		update_preferred_size( _animatorView);
		_animatorView.repaint();
	}

	/**
	 * Makes horizontal gaps between the selected objects equal.
	 */
	public void horizontal_equal_layout() {
		Vector<SpotObject> objects = new Vector<SpotObject>();
		_spotObjectManager.get_selected_spot_and_image( objects);
		if ( objects.isEmpty() || 3 > objects.size())
			return;

		ISpotObjectManipulator[] spotObjectManipulators = sort_spots( objects, false);

		int left = 0, right = 0, sum = 0;
		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			Point position = spotObjectManipulators[ i].get_position();
			Dimension dimension = spotObjectManipulators[ i].get_dimension();
			sum += dimension.width;
			if ( 0 == i || position.x < left)
				left = position.x;
			if ( ( position.x + dimension.width) > right)
				right = ( position.x + dimension.width);
		}

		int space = ( ( right - left - sum) / ( spotObjectManipulators.length - 1));

		for ( int i = 0; i < spotObjectManipulators.length; ++i) {
			if ( 0 < i)
				spotObjectManipulators[ i].move( left - spotObjectManipulators[ i].get_position().x, 0, true);

			left += ( spotObjectManipulators[ i].get_dimension().width + space);
		}

		update_preferred_size( _animatorView);
		_animatorView.repaint();
	}

	/**
	 * 
	 * @param spots
	 * @param vertical
	 * @return
	 */
	private ISpotObjectManipulator[] sort_spots(Vector<SpotObject> spots, boolean vertical) {
		if ( spots.isEmpty())
			return null;

		ISpotObjectManipulator[] spotObjectManipulators = ( ISpotObjectManipulator[])spots.toArray( new ISpotObjectManipulator[ 0]);
		if ( 1 == spotObjectManipulators.length)
			return spotObjectManipulators;

		QuickSort.sort( spotObjectManipulators, new SpotPositionComparator( vertical));
		return spotObjectManipulators;
	}

	/**
	 * Returns true if the specified image file is in use.
	 * @param filename the name of the specified image file
	 * @return true if the specified image file is in use
	 */
	public boolean uses_this_image(String filename) {
		boolean result1 = _agentObjectManager.uses_this_image( filename);
		boolean result2 = _spotObjectManager.uses_this_image( filename);
		boolean result3 = _scenarioManager._agentPropertyManager.uses_this_image( filename);
		boolean result4 = _scenarioManager._spotPropertyManager.uses_this_image( filename);
		return ( result1 || result2 || result3 || result4);
	}

	/**
	 * Sets the specified new image if the object uses the specified image.
	 * @param originalFilename the specified image file name
	 * @param newFilename the specified new image file name
	 */
	public void update_image(String originalFilename, String newFilename) {
		_agentObjectManager.update_image( originalFilename, newFilename);
		_spotObjectManager.update_image( originalFilename, newFilename);
		_scenarioManager._agentPropertyManager.update_image( originalFilename, newFilename);
		_scenarioManager._spotPropertyManager.update_image( originalFilename, newFilename);
	}

	/**
	 * Returns true for writing this spot data successfully.
	 * @param windowProperty
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this spot data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	public boolean write(WindowProperty windowProperty, Writer writer) throws SAXException {
		if ( !_scenarioManager.write( writer))
			return false;

		if ( !write_properties( windowProperty, writer))
			return false;

		if ( !write_objects( writer))
			return false;

		return true;
	}

	/**
	 * @param windowProperty
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_properties(WindowProperty windowProperty, Writer writer) throws SAXException {
		writer.startElement( null, null, "property", new AttributesImpl());

		if ( !write_windowProperty( windowProperty, writer))
			return false;

		if ( !write_spotLog( writer))
			return false;

		if ( !write_spot_properties( writer))
			return false;

		if ( !write_agent_properties( writer))
			return false;

		writer.endElement( null, null, "property");

		return true;
	}

	/**
	 * @param windowProperty
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_windowProperty(WindowProperty windowProperty, Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "x", "", String.valueOf( ( null == windowProperty._rectangle) ? 0 : windowProperty._rectangle.x));
		attributesImpl.addAttribute( null, null, "y", "", String.valueOf( ( null == windowProperty._rectangle) ? 0 : windowProperty._rectangle.y));
		attributesImpl.addAttribute( null, null, "width", "", String.valueOf( ( null == windowProperty._rectangle) ? AnimatorViewFrame._minimumWidth : windowProperty._rectangle.width));
		attributesImpl.addAttribute( null, null, "height", "", String.valueOf( ( null == windowProperty._rectangle) ? AnimatorViewFrame._minimumHeight : windowProperty._rectangle.height));
		attributesImpl.addAttribute( null, null, "maximum", "", windowProperty._maximum ? "true" : "false");
		attributesImpl.addAttribute( null, null, "icon", "", windowProperty._icon ? "true" : "false");
		attributesImpl.addAttribute( null, null, "order", "", String.valueOf( windowProperty._order));
		writer.writeElement( null, null, "window", attributesImpl);
		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_spotLog(Writer writer) throws SAXException {
		AttributesImpl attributesImpl = new AttributesImpl();
		attributesImpl.addAttribute( null, null, "type", "", Writer.escapeAttributeCharData( _spotLog[ 0]));
		attributesImpl.addAttribute( null, null, "name", "", Writer.escapeAttributeCharData( _spotLog[ 1]));
		writer.writeElement( null, null, "spot_log", attributesImpl);
		return true;
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_spot_properties(Writer writer) throws SAXException {
		return _scenarioManager._spotPropertyManager.write( writer);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_agent_properties(Writer writer) throws SAXException {
		return _scenarioManager._agentPropertyManager.write( writer);
	}

	/**
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_objects(Writer writer) throws SAXException {
		writer.startElement( null, null, "object", new AttributesImpl());

		if ( !_spotObjectManager.write( writer))
			return false;

		if ( !_agentObjectManager.write( writer))
			return false;

		writer.endElement( null, null, "object");

		return true;
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	public boolean write_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		if ( !write_graphic_data_properties( rootDirectory, writer))
			return false;

		if ( !write_graphic_data_objects( rootDirectory, writer))
			return false;

		return true;
	}

	/**
	 * Returns true for writing this spot graphic data successfully.
	 * @param rootDirectory the root directory for Animator data
	 * @param writer the abstract class for writing to character streams
	 * @return true for writing this spot graphic data successfully
	 * @throws SAXException encapsulate a general SAX error or warning
	 */
	private boolean write_graphic_data_properties(File rootDirectory, Writer writer) throws SAXException {
		writer.startElement( null, null, "property", new AttributesImpl());

		if ( !write_spotLog( writer))
			return false;

		if ( !write_spot_properties_graphic_data( rootDirectory, writer))
			return false;

		if ( !write_agent_properties_graphic_data( rootDirectory, writer))
			return false;
		
		writer.endElement( null, null, "property");

		return true;
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_spot_properties_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		return _scenarioManager._spotPropertyManager.write_graphic_data( rootDirectory, writer);
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_agent_properties_graphic_data(File rootDirectory, Writer writer) throws SAXException {
		return _scenarioManager._agentPropertyManager.write_graphic_data( rootDirectory, writer);
	}

	/**
	 * @param rootDirectory
	 * @param writer
	 * @return
	 * @throws SAXException
	 */
	private boolean write_graphic_data_objects(File rootDirectory, Writer writer) throws SAXException {
		writer.startElement( null, null, "object", new AttributesImpl());

		if ( !_spotObjectManager.write_graphic_data( rootDirectory, writer))
			return false;

		if ( !_agentObjectManager.write_graphic_data( rootDirectory, writer))
			return false;

		writer.endElement( null, null, "object");

		return true;
	}
}
