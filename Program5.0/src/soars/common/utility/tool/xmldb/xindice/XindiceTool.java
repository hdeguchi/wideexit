/**
 * 
 */
package soars.common.utility.tool.xmldb.xindice;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import org.apache.xindice.util.XindiceException;
import org.apache.xindice.xml.dom.DOMParser;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.BinaryResource;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

/**
 * @author kurata
 *
 */
public class XindiceTool {

	/**
	 * @param collection
	 * @return
	 */
	public static String getName(Collection collection) {
		String name;
		try {
			name = collection.getName();
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return name;
	}

	/**
	 * @param collection
	 * @return
	 */
	public static Collection getParentCollection(Collection collection) {
		Collection parentCollection;
		try {
			parentCollection = collection.getParentCollection();
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return parentCollection;
	}

	/**
	 * @param parentCollection
	 * @param name
	 * @return
	 */
	public static Collection getChildCollection(Collection parentCollection, String name) {
		Collection collection;
		try {
			collection = parentCollection.getChildCollection( name);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return collection;
	}

	/**
	 * @param xml
	 * @return
	 */
	public static Document getDocument(String xml) {
		Document document;
		try {
			document= DOMParser.toDocument( xml);
		} catch (XindiceException e) {
			e.printStackTrace();
			return null;
		}
		return document;
	}

	/**
	 * @param collection
	 * @return
	 */
	public static CollectionManagementService getCollectionManagementService(Collection collection) {
		return getCollectionManagementService( collection, Constant._version);
	}

	/**
	 * @param collection
	 * @param version
	 * @return
	 */
	private static CollectionManagementService getCollectionManagementService(Collection collection, String version) {
		CollectionManagementService collectionManagementService;
		try {
			collectionManagementService = ( CollectionManagementService)collection.getService( "CollectionManagementService", version);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return collectionManagementService;
	}

	/**
	 * @param collectionManagementService
	 * @param name
	 * @return
	 */
	public static Collection createCollection(CollectionManagementService collectionManagementService, String name) {
		Collection collection;
		try {
			collection = collectionManagementService.createCollection( name);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}
		return collection;
	}

	/**
	 * @param collectionManagementService
	 * @param collection
	 * @return
	 */
	public static boolean removeCollection(CollectionManagementService collectionManagementService, Collection collection) {
		String name = getName( collection);
		if ( null == name)
			return false;

		return removeCollection( collectionManagementService, name);
	}

	/**
	 * @param collectionManagementService
	 * @param name
	 * @return
	 */
	public static boolean removeCollection(CollectionManagementService collectionManagementService, String name) {
		try {
			collectionManagementService.removeCollection( name);
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
		String data = read( filename, encoding);
		if ( null == data)
			return false;

		XMLResource xmlResource;
		try {
			xmlResource = ( XMLResource)collection.createResource( id, XMLResource.RESOURCE_TYPE);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		try {
			xmlResource.setContent( data);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		try {
			collection.storeResource( xmlResource);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param filename
	 * @param encoding
	 * @return
	 */
	public static String read(String filename, String encoding) {
		File file = new File( filename);

		BufferedReader bufferedReader;
		try {
			bufferedReader = ( null == encoding)
				? new BufferedReader( new InputStreamReader( new FileInputStream( file)))
				: new BufferedReader( new InputStreamReader( new FileInputStream( file), encoding));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		String data = "";
		while ( true) {
			String line = null;
			try {
				line = bufferedReader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				try {
					bufferedReader.close();
				} catch (IOException e1) {
					e1.printStackTrace();
					return null;
				}
			}

			if ( null == line)
				break;

			data += ( line + "\n");
		}

		try {
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return data;
	}

	/**
	 * @param collection
	 * @param id
	 * @param node
	 * @return
	 */
	public static boolean addDocument(Collection collection, String id, Node node) {
		XMLResource xmlResource;
		try {
			xmlResource = ( XMLResource)collection.createResource( id, XMLResource.RESOURCE_TYPE);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		try {
			xmlResource.setContentAsDOM( node);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		try {
			collection.storeResource( xmlResource);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * @param collection
	 * @param data
	 * @return
	 */
	public static String addDocument(Collection collection, byte[] data) {
		BinaryResource resource;
		try {
			resource = ( BinaryResource)collection.createResource( null, BinaryResource.RESOURCE_TYPE);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		try {
			resource.setContent( data);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		try {
			collection.storeResource( resource);
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		String id;
		try {
			id = resource.getId();
		} catch (XMLDBException e) {
			e.printStackTrace();
			return null;
		}

		return id;
	}
}
