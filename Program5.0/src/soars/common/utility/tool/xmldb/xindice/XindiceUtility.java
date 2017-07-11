/**
 * 
 */
package soars.common.utility.tool.xmldb.xindice;

import org.w3c.dom.Node;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XPathQueryService;
import org.xmldb.api.modules.XUpdateQueryService;

/**
 * @author kurata
 *
 */
public class XindiceUtility {

	/**
	 * @param home
	 * @return
	 */
	public static boolean initialize(String home) {
		return initialize( "org.apache.xindice.client.xmldb.embed.DatabaseImpl", home);
		//return initialize( "org.apache.xindice.client.xmldb.DatabaseImpl", home);
	}

	/**
	 * @param classname
	 * @param home
	 * @return
	 */
	public static boolean initialize(String classname, String home) {
		Database database = null;
		try {
			Class cls = Class.forName( classname);
			database = ( Database)cls.newInstance();
			database.setProperty( "db-home", home);
			database.setProperty( "managed", "true");
			DatabaseManager.registerDatabase( database);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return false;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return false;
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param home
	 * @return
	 */
	public static Database connect(String home) {
		return connect( "org.apache.xindice.client.xmldb.embed.DatabaseImpl", home);
		//return initialize( "org.apache.xindice.client.xmldb.DatabaseImpl", home);
	}

	/**
	 * @param classname
	 * @param home
	 * @return
	 */
	public static Database connect(String classname, String home) {
		Database database = null;
		try {
			Class cls = Class.forName( classname);
			database = ( Database)cls.newInstance();
			database.setProperty( "db-home", home);
			database.setProperty( "managed", "true");
			DatabaseManager.registerDatabase( database);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		return database;
	}

	/**
	 * @param database
	 * @return
	 */
	public static boolean disconnect(Database database) {
		try {
			DatabaseManager.deregisterDatabase( database);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param parentCollection
	 * @param name
	 * @return
	 */
	public static Collection createCollection(Collection parentCollection, String name) {
		if ( null != XindiceTool.getChildCollection( parentCollection, name))
			return null;

		CollectionManagementService collectionManagementService = XindiceTool.getCollectionManagementService( parentCollection);
		if ( null == collectionManagementService)
			return null;

		return XindiceTool.createCollection( collectionManagementService, name);
	}

	/**
	 * @param parent
	 * @param name
	 * @return
	 */
	public static Collection createCollection(String parent, String name) {
		Collection parentCollection = getCollection( parent);
		if ( null == parentCollection)
			return null;

		if ( null != XindiceTool.getChildCollection( parentCollection, name)) {
			close( parentCollection);
			return null;
		}

		CollectionManagementService collectionManagementService = XindiceTool.getCollectionManagementService( parentCollection);
		if ( null == collectionManagementService)
			return null;

		Collection collection = XindiceTool.createCollection( collectionManagementService, name);

		close( parentCollection);
		return collection;
	}

	/**
	 * @param parentCollection
	 * @param collection
	 * @return
	 */
	public static boolean removeCollection(Collection parentCollection, Collection collection) {
		CollectionManagementService collectionManagementService = XindiceTool.getCollectionManagementService( parentCollection);
		if ( null == collectionManagementService)
			return false;

		return XindiceTool.removeCollection( collectionManagementService, collection);
	}

	/**
	 * @param parentCollection
	 * @param name
	 * @return
	 */
	public static boolean removeCollection(Collection parentCollection, String name) {
		CollectionManagementService collectionManagementService = XindiceTool.getCollectionManagementService( parentCollection);
		if ( null == collectionManagementService)
			return false;

		return XindiceTool.removeCollection( collectionManagementService, name);
	}

	/**
	 * @param collection
	 * @return
	 */
	public static boolean removeCollection(Collection collection) {
		Collection parentCollection = XindiceTool.getParentCollection( collection);
		if ( null == parentCollection)
			return false;

		CollectionManagementService collectionManagementService = XindiceTool.getCollectionManagementService( parentCollection);
		if ( null == collectionManagementService) {
			close( parentCollection);
			return false;
		}

		boolean result = XindiceTool.removeCollection( collectionManagementService, collection);

		close( parentCollection);

		return result;
	}

	/**
	 * @param parentCollection
	 * @param name
	 * @return
	 */
	public static Collection getCollection(Collection parentCollection, String name) {
		try {
			return parentCollection.getChildCollection( name);
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param uri
	 * @return
	 */
	public static Collection getCollection(String uri) {
		Collection collection;
		try {
			collection = DatabaseManager.getCollection( uri);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		} catch (Throwable ex) {
			ex.printStackTrace();
			return null;
		}
		return collection;
	}

	/**
	 * @param collection
	 * @return
	 */
	public static boolean close(Collection collection) {
		try {
			collection.close();
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param collection
	 * @param id
	 * @param filename
	 * @param encoding
	 * @return
	 */
	public static boolean addDocument(Collection collection, String id, String filename, String encoding) {
		return XindiceTool.addDocument( collection, id, filename, encoding);
	}

	/**
	 * @param collection
	 * @param id
	 * @param node
	 * @return
	 */
	public static boolean addDocument(Collection collection, String id, Node node) {
		return XindiceTool.addDocument( collection, id, node);
	}

	/**
	 * @param collection
	 * @param data
	 * @return
	 */
	public static String addDocument(Collection collection, byte[] data) {
		return XindiceTool.addDocument( collection, data);
	}

	/**
	 * @param collection
	 * @param query
	 * @return
	 */
	public static ResourceIterator query(Collection collection, String query) {
		XPathQueryService xPathQueryService = getXPathQueryService( collection);
		if ( null == xPathQueryService)
			return null;

		ResourceSet resourceSet;
		try {
			resourceSet = xPathQueryService.query( query);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		ResourceIterator resourceIterator;
		try {
			resourceIterator = resourceSet.getIterator();
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		return resourceIterator;
	}

	/**
	 * @param collection
	 * @return
	 */
	public static XPathQueryService getXPathQueryService(Collection collection) {
		return getXPathQueryService( collection, Constant._version);
	}

	/**
	 * @param collection
	 * @param version
	 * @return
	 */
	public static XPathQueryService getXPathQueryService(Collection collection, String version) {
		XPathQueryService xPathQueryService;
		try {
			xPathQueryService = ( XPathQueryService)collection.getService( "XPathQueryService", version);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return xPathQueryService;
	}

	/**
	 * @param collection
	 * @param update
	 * @return
	 */
	public static boolean update(Collection collection, String update) {
		XUpdateQueryService xUpdateQueryService = getXUpdateQueryService( collection);
		if ( null == xUpdateQueryService)
			return false;

		String xupdate = "<xupdate:modifications version=\"" + Constant._version + "\" "
			+ "xmlns:xupdate=\"http://www.xmldb.org/xupdate\">"
			+ update
			+ "</xupdate:modifications>";

		try {
			long num = xUpdateQueryService.update( xupdate);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @param collection
	 * @return
	 */
	private static XUpdateQueryService getXUpdateQueryService(Collection collection) {
		return getXUpdateQueryService( collection, Constant._version);
	}

	/**
	 * @param collection
	 * @param version
	 * @return
	 */
	private static XUpdateQueryService getXUpdateQueryService(Collection collection, String version) {
		XUpdateQueryService xUpdateQueryService;
		try {
			xUpdateQueryService = ( XUpdateQueryService)collection.getService( "XUpdateQueryService", version);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return xUpdateQueryService;
	}
}
