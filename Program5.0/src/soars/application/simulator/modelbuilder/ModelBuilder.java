/**
 * 
 */
package soars.application.simulator.modelbuilder;

import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;

import main.MainGUI;
import soars.application.simulator.main.ResourceManager;
import view.JTabbedWriter;
import view.LogWriter;

/**
 * @author kurata
 *
 */
public class ModelBuilder extends MainGUI {

	/**
	 * 
	 */
	private JTabbedPane _tabbedPane = null;

	/**
	 * 
	 */
	private Map _textAreaMap = null;

	/**
	 * @param tabbedPane
	 * @param textAreaMap
	 */
	public ModelBuilder(JTabbedPane tabbedPane, Map textAreaMap) {
		super();
		_tabbedPane = tabbedPane;
		_textAreaMap = textAreaMap;
	}

	/* (non-Javadoc)
	 * @see main.MainGUI#logAgentsToTab(java.lang.String)
	 */
	public void logAgentsToTab(final String head) {
		logFactoryAgents = new LogWriter.Factory() {
			public LogWriter create(List source, String key) {
				JTabbedWriter tabbedWriter = new JTabbedWriter( _tabbedPane, head + key + " - " + ResourceManager.get_instance().get( "log.viewer.state.logging"), head + key);
				tabbedWriter.panel.remove( tabbedWriter.toolBar);
				tabbedWriter.textArea.setEditable( false);
				_textAreaMap.put( head + key, tabbedWriter.textArea);
				return new LogWriter( tabbedWriter, source, key);
			}
		};
//		try {
//			Field field = MainConsole.class.getDeclaredField( "logFactoryAgents");
//			field.setAccessible( true);
//			field.set( this,
//				new LogWriter.Factory() {
//					public LogWriter create(List source, String key) {
//						JTabbedWriter tabbedWriter = new JTabbedWriter( _tabbedPane, head + key + " - " + ResourceManager.get_instance().get( "log.viewer.state.logging"), head + key);
//						tabbedWriter.panel.remove( tabbedWriter.toolBar);
//						tabbedWriter.textArea.setEditable( false);
//						_textAreaMap.put( head + key, tabbedWriter.textArea);
//						return new LogWriter( tabbedWriter, source, key);
//					}
//				});
////			logFactoryAgents = new LogWriter.Factory() {
////				public LogWriter create(List source, String key) {
////					return new LogWriter( new JTabbedWriter( MainFrame.get_instance()._tabbedPane, head + key + " (logging)", head + key), source, key);
////				}
////			};
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
	}

	/* (non-Javadoc)
	 * @see main.MainGUI#logSpotsToTab(java.lang.String)
	 */
	public void logSpotsToTab(final String head) {
		logFactorySpots = new LogWriter.Factory() {
			public LogWriter create(List source, String key) {
				JTabbedWriter tabbedWriter = new JTabbedWriter( _tabbedPane, head + key + " - " + ResourceManager.get_instance().get( "log.viewer.state.logging"), head + key);
				tabbedWriter.panel.remove( tabbedWriter.toolBar);
				tabbedWriter.textArea.setEditable( false);
				_textAreaMap.put( head + key, tabbedWriter.textArea);
				return new LogWriter( tabbedWriter, source, key);
			}
		};
//		try {
//			Field field = MainConsole.class.getDeclaredField( "logFactorySpots");
//			field.setAccessible( true);
//			field.set( this,
//				new LogWriter.Factory() {
//					public LogWriter create(List source, String key) {
//						JTabbedWriter tabbedWriter = new JTabbedWriter( _tabbedPane, head + key + " - " + ResourceManager.get_instance().get( "log.viewer.state.logging"), head + key);
//						tabbedWriter.panel.remove( tabbedWriter.toolBar);
//						tabbedWriter.textArea.setEditable( false);
//						_textAreaMap.put( head + key, tabbedWriter.textArea);
//						return new LogWriter( tabbedWriter, source, key);
//					}
//				});
////		logFactorySpots = new LogWriter.Factory() {
////			public LogWriter create(List source, String key) {
////				return new LogWriter( new JTabbedWriter( MainFrame.get_instance()._tabbedPane, head + key + " (logging)", head + key), source, key);
////			}
////		};
//		} catch (SecurityException e) {
//			e.printStackTrace();
//		} catch (NoSuchFieldException e) {
//			e.printStackTrace();
//		} catch (IllegalArgumentException e) {
//			e.printStackTrace();
//		} catch (IllegalAccessException e) {
//			e.printStackTrace();
//		}
	}
}
