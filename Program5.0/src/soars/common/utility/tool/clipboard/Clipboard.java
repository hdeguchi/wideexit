/*
 * 2005/06/09
 */
package soars.common.utility.tool.clipboard;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author kurata
 */
public class Clipboard {

	/**
	 * 
	 */
	public Clipboard() {
		super();
	}

	/**
	 * @return
	 */
	public static String get_text() {
		try {
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents( null);
			if ( null == transferable)
				return null;

			return ( String)transferable.getTransferData( DataFlavor.stringFlavor);
		} catch (Throwable ex) {
			//ex.printStackTrace();
			return null;
		}
//		} catch (HeadlessException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (UnsupportedFlavorException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (IllegalArgumentException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (IllegalStateException e) {
//			//e.printStackTrace();
//			return null;
//		}
	}

	/**
	 * @param text
	 */
	public static void set(String text) {
		if ( null == text)
			return;

		StringSelection stringSelection = new StringSelection( text);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents( stringSelection, stringSelection);
	}

	/**
	 * @return
	 */
	public static File[] get_files() {
		try {
			Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents( null);
			if ( null == transferable)
				return null;

			if ( transferable.isDataFlavorSupported( DataFlavor.javaFileListFlavor)) {
				List list = ( List)transferable.getTransferData( DataFlavor.javaFileListFlavor);
				return ( File[])list.toArray( new File[ 0]);
			} else if ( transferable.isDataFlavorSupported( URISelection._uriFlavor)) {
				String string = ( String)transferable.getTransferData( URISelection._uriFlavor);
				if ( null == string)
					return null;

				StringTokenizer stringTokenizer = new StringTokenizer( string, "\r\n");
				List<File> list = new ArrayList<File>();
				while( stringTokenizer.hasMoreElements()) {
					URI uri = new URI( ( String)stringTokenizer.nextElement());
					String scheme = uri.getScheme();
					if ( null == scheme)
						return null;

					if ( scheme.equals( "file"))
						list.add( new File( uri.getPath()));
				}
				return list.toArray( new File[ 0]);
			} else
				return null;
		} catch (Throwable ex) {
			//ex.printStackTrace();
			return null;
		}
//		} catch (HeadlessException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (UnsupportedFlavorException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (URISyntaxException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (IllegalArgumentException e) {
//			//e.printStackTrace();
//			return null;
//		} catch (IllegalStateException e) {
//			//e.printStackTrace();
//			return null;
//		}
	}

	/**
	 * @param files
	 */
	public static void set(File[] files) {
		if ( null == files || 0 == files.length)
			return;

		if ( 0 <= System.getProperty( "os.name").indexOf( "Windows")) {
			FileSelection fileSelection = new FileSelection( files);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents( fileSelection, fileSelection);
		} else {
			URISelection uriSelection = new URISelection( files);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents( uriSelection, uriSelection);
		}
	}
}
