package script;

import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * The ScriptParser class implements paragraph and item method script parser.
 * @author H. Tanuma / SOARS project
 */
public class ScriptParser {

	protected Method[] methods = null;
	protected Object toString = this;

	/**
	 * Get file name and line number of script.
	 * @return file name and line number
	 */
	public String toString() {
		return toString == this ? super.toString() : String.valueOf(toString);
	}
	/**
	 * Open script file and run script.
	 * @param fileName script file name
	 * @throws FileNotFoundException
	 */
	public void parseAll(String fileName) throws FileNotFoundException {
		parseAll(new ScriptReader(fileName));
	}
	/**
	 * Run script in ScriptReader stream.
	 * @param reader script stream
	 */
	public void parseAll(ScriptReader reader) {
		toString = reader;
		Iterator<?> paragraph;
		try {
			while ( (paragraph = reader.readParagraph()) != null ) {
				String line = paragraph.next().toString().trim().split("\\s", 2)[0];
				Method method = getClass().getMethod(line, new Class[] {Iterator.class});
				method.invoke(this, new Object[] {paragraph});
				while (paragraph.hasNext()) {
					paragraph.next();
				}
			}
			reader.close();
		}
		catch (Exception e) {
			throw new RuntimeException("Script Error at " + reader, e);
		}
		finally {
			toString = this;
		}
	}
	/**
	 * Paragraph method to describe comment.
	 * @param paragraph ignored
	 */
	public static void ignore(Iterator<?> paragraph) {
	}
	/**
	 * Paragraph method to display message.
	 * @param paragraph message lines
	 */
	public static void print(Iterator<?> paragraph) {
		while (paragraph.hasNext()) {
			System.out.println(paragraph.next());
		}
	}
	/**
	 * Paragraph method to display message and blank line.
	 * @param paragraph message lines
	 */
	public static void println(Iterator<?> paragraph) {
		print(paragraph);
		System.out.println();
	}
	/**
	 * Paragraph method to assign item methods to each column.
	 * @param paragraph tab separated item method names
	 * @throws NoSuchMethodException
	 */
	public void item(Iterator<?> paragraph) throws NoSuchMethodException {
		String[] items = (paragraph.next().toString()).split("\t");
		methods = new Method[items.length];
		for (int i = 0; i < items.length; ++i) {
			methods[i] = getClass().getMethod(items[i].trim().split("\\s", 2)[0], new Class[] {String.class});
		}
	}
	/**
	 * Paragraph method to execute item methods.
	 * @param paragraph tab separated parameters for item methods
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void data(Iterator<?> paragraph) throws IllegalAccessException, InvocationTargetException {
		String[] args;
		while (paragraph.hasNext()) {
			args = (paragraph.next().toString()).split("\t", methods.length + 1);
			for (int item = 0; item < methods.length; ++item) {
				String arg = (item < args.length) ? args[item] : "";
				methods[item].invoke(this, new Object[] {arg});
			}
		}
	}
	/**
	 * Composition of item and data paragraph methods.
	 * @param paragraph first line for item method and following lines for data method
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public void itemData(Iterator<?> paragraph) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		item(paragraph);
		data(paragraph);
	}
	/**
	 * Item method to describe comment.
	 * @param item ignored
	 */
	public static void ignore(String item) {
	}
	/**
	 * Item method to display message.
	 * @param item message string
	 */
	public static void print(String item) {
		System.out.print(item);
		System.out.print('\t');
	}
	/**
	 * Item method to display message and line feed.
	 * @param item message string
	 */
	public static void println(String item) {
		System.out.println(item);
	}
}
