package script;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The ScriptReader class represents paragraph separated script.
 * @author H. Tanuma / SOARS project
 */
public class ScriptReader {

	LineNumberReader reader;
	String fileName;

	/**
	 * Constructor to read script from file.
	 * @param fileName script file name
	 * @throws FileNotFoundException
	 */
	public ScriptReader(String fileName) throws FileNotFoundException {
		this(new FileReader(fileName), fileName);
	}
	/**
	 * Constructor to read script from Reader stream.
	 * @param reader stream of script
	 * @param fileName script name
	 */
	public ScriptReader(Reader reader, String fileName) {
		this.reader = new LineNumberReader(reader);
		this.fileName = fileName;
	}

	/**
	 * Get file name.
	 * @return file name
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * Get file name and line number.
	 * @return file name and line number
	 */
	public String toString() {
		return '\"' + fileName + "\" Line " + (reader.getLineNumber() - 1);
	}
	/**
	 * Read one line of script.
	 * @return line string or null if the end of the script has been reached
	 * @throws IOException
	 */
	public String readLine() throws IOException {
		return reader.readLine();
	}
	/**
	 * Read one paragraph of script.
	 * @return iterator of paragraph lines or null if the end of the script has been reached
	 * @throws IOException
	 */
	public Iterator<Object> readParagraph() throws IOException {
		String line;
		do {
			line = readLine();
			if (line == null) {
				return null;
			}
		} while (line.trim().equals(""));
		final String first = line;
		return new Iterator<Object>() {
			private String line = first;
			public boolean hasNext() {
				return line != null;
			}
			public Object next() {
				if (line == null) {
					throw new NoSuchElementException();
				}
				String next = line;
				try {
					line = readLine();
				}
				catch (IOException e) {
					throw new RuntimeException(e);
				}
				if (line != null && line.trim().equals("")) {
					line = null;
				}
				return next;
			}
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}
	/**
	 * Close script.
	 * @throws IOException
	 */
	public void close() throws IOException {
		reader.close();
	}
}
