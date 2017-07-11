package view;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * The Clipboard class implements clipboard accessor.
 * @author H. Tanuma / SOARS project
 */
public final class Clipboard {

	private Clipboard() {
	}

	public static String getString() throws UnsupportedFlavorException, IOException {
		return (String) Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null).getTransferData(DataFlavor.stringFlavor);
	}
	public static void setString(String string) {
		StringSelection selection = new StringSelection(string);
		Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
	}
}
