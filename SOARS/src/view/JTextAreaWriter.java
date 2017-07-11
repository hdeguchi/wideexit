package view;

import java.io.IOException;
import java.io.Writer;

import javax.swing.JTextArea;

/**
 * The JTextAreaWriter class represents writer object assigned to text window in tabbed pane.
 * @author H. Tanuma / SOARS project
 */
public class JTextAreaWriter extends Writer {

	public final JTextArea textArea = new JTextArea();

	public void write(int c) throws IOException {
		write(String.valueOf((char) c));
	}
	public void write(String str) throws IOException {
		textArea.append(str);
	}
	public void write(String str, int off, int len) throws IOException {
		write(str.substring(off, off + len));
	}
	public void write(char[] cbuf, int off, int len) throws IOException {
		write(new String(cbuf, off, len));
	}
	public void flush() throws IOException {
	}
	public void close() throws IOException {
	}
}
