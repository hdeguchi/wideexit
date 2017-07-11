/*
 * 2005/03/10
 */
package soars.application.visualshell.common.tool;

import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.util.Arrays;
import java.util.Locale;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;

import soars.application.visualshell.main.Environment;
import soars.application.visualshell.main.ResourceManager;
import soars.common.utility.swing.tool.ExampleFileFilter;
import soars.common.utility.tool.sort.QuickSort;

/**
 * The common tool for Visual Shell.
 * @author kurata / SOARS project
 */
public class CommonTool {

	/**
	 * Returns the combo box to select the font name.
	 * @return the combo box to select the font name
	 */
	public static JComboBox get_font_family_combo_box() {
		GraphicsEnvironment graphicsEnvironment
			= GraphicsEnvironment.getLocalGraphicsEnvironment();

		Font[] fonts = graphicsEnvironment.getAllFonts();
		Vector<String> familyNames = new Vector<String>();

		for ( int i = 0; i < fonts.length; ++i) {
			if ( !familyNames.contains( fonts[ i].getFamily( Locale.getDefault())))
				familyNames.add( fonts[ i].getFamily( Locale.getDefault()));
		}

		return new JComboBox( familyNames);
	}

	/**
	 * Returns the combo box to select the font style.
	 * @return the combo box to select the font style
	 */
	public static JComboBox get_font_style_combo_box() {
		String[] styles = {
			ResourceManager.get_instance().get( "common.font.style.plain"),
			ResourceManager.get_instance().get( "common.font.style.bold"),
			ResourceManager.get_instance().get( "common.font.style.italic"),
			ResourceManager.get_instance().get( "common.font.style.bold&italic")
		};

		return new JComboBox( styles);
	}

	/**
	 * Returns the combo box to select the font size.
	 * @return the combo box to select the font size
	 */
	public static JComboBox get_font_size_combo_box() {
		String[] sizes = {
			"8", "9", "10", "11", "12", "13", "14", "16", "18", "20", "24", "28", "32", "36", "48", "72"
		};

		return new JComboBox( sizes);
	}

	/**
	 * Returns the font style string from the specified font style constant.
	 * @param style the specified font style constant
	 * @return the font style string from the specified font style constant
	 */
	public static String get_font_style(int style) {
		if ( Font.PLAIN == style)
			return ResourceManager.get_instance().get( "common.font.style.plain");
		else if ( ( Font.BOLD + Font.ITALIC) == style)
			return ResourceManager.get_instance().get( "common.font.style.bold&italic");
		else if ( Font.BOLD == style)
			return ResourceManager.get_instance().get( "common.font.style.bold");
		else if ( Font.ITALIC == style)
			return ResourceManager.get_instance().get( "common.font.style.italic");
		return "";
	}

	/**
	 * Returns the font style constant from the font style string.
	 * @param style the specified font style string
	 * @return the font style constant from the font style string
	 */
	public static int get_font_style(String style) {
		if ( style.equals( ResourceManager.get_instance().get( "common.font.style.plain")))
			return Font.PLAIN;
		else if ( style.equals( ResourceManager.get_instance().get( "common.font.style.bold&italic")))
			return ( Font.BOLD + Font.ITALIC);
		else if ( style.equals( ResourceManager.get_instance().get( "common.font.style.bold")))
			return Font.BOLD;
		else if ( style.equals( ResourceManager.get_instance().get( "common.font.style.italic")))
			return Font.ITALIC;
		return Font.PLAIN;
	}

	/**
	 * Updates the specified Combo box with the specified data.
	 * @param comboBox the specified Combo box
	 * @param names the specified data
	 */
	public static void update(JComboBox comboBox, String[] names) {
		if ( null == names || 0 == names.length) {
			comboBox.removeAllItems();
			return;
		}

		String name = ( String)comboBox.getSelectedItem();

		comboBox.removeAllItems();

		int index = 0;
		for ( int i = 0; i < names.length; ++i) {
			comboBox.addItem( names[ i]);
			if ( names[ i].equals( name))
				index = i;
		}

		comboBox.setSelectedIndex( index);
	}

	/**
	 * Returns the image file selected by the user.
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the image file selected by the user
	 */
	public static File get_imagefile(String openDirectoryKey, Component component) {
		return get_open_file(
			openDirectoryKey,
			ResourceManager.get_instance().get( "select.imagefile.dialog.title"),
			new String[] { "gif", "jpg", "jpeg", "png"},
			"imagefile",
			component);
	}

	/**
	 * Returns the image file selected by the user.
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the image file selected by the user
	 */
	public static File get_imagefile(String openDirectoryKey, String title, Component component) {
		return get_open_file( openDirectoryKey, title,
			new String[] { "gif", "jpg", "jpeg", "png"},
			"imagefile",
			component);
	}

	/**
	 * Returns the file selected by the user.
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param extensions the specified file extensions
	 * @param description the human readable description of file types
	 * @param component the parent component of the file chooser dialog
	 * @return the file selected by the user
	 */
	public static File get_open_file(String openDirectoryKey, String title, String[] extensions, String description, Component component) {
		String openDirectory = "";

		String value = Environment.get_instance().get( openDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				openDirectory = value;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}

		if ( !openDirectory.equals( ""))
			fileChooser.setCurrentDirectory( new File( openDirectory));

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			File file = fileChooser.getSelectedFile();

			File directory = fileChooser.getCurrentDirectory();
			openDirectory = directory.getAbsolutePath();

			Environment.get_instance().set( openDirectoryKey, openDirectory);

			return file;
		}

		return null;
	}

	/**
	 * Returns the file saved by the user.
	 * @param saveDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param extensions the specified file extensions
	 * @param description the human readable description of file types
	 * @param component the parent component of the file chooser dialog
	 * @return the file saved by the user
	 */
	public static File get_save_file(String saveDirectoryKey, String title, String[] extensions, String description, Component component) {
		String saveDirectory = "";

		String value = Environment.get_instance().get( saveDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				saveDirectory = value;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}

		if ( !saveDirectory.equals( ""))
			fileChooser.setCurrentDirectory( new File( saveDirectory));

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showSaveDialog( component)) {
			File file = fileChooser.getSelectedFile();

			File directory = fileChooser.getCurrentDirectory();
			saveDirectory = directory.getAbsolutePath();

			Environment.get_instance().set( saveDirectoryKey, saveDirectory);

			return file;
		}

		return null;
	}

	/**
	 * Returns the image files array selected by the user.
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the image files array selected by the user
	 */
	public static File[] get_imagefiles(String openDirectoryKey, Component component) {
		return get_open_files(
			openDirectoryKey,
			ResourceManager.get_instance().get( "select.imagefile.dialog.title"),
			new String[] { "gif", "jpg", "jpeg", "png"},
			"imagefile",
			component);
	}

	/**
	 * Returns the files array selected by the user.
	 * @param openDirectoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param extensions the specified file extensions
	 * @param description the human readable description of file types
	 * @param component the parent component of the file chooser dialog
	 * @return the files array selected by the user
	 */
	public static File[] get_open_files(String openDirectoryKey, String title, String[] extensions, String description, Component component) {
		String openDirectory = "";
		
		String value = Environment.get_instance().get( openDirectoryKey, "");
		if ( null != value && !value.equals( "")) {
			File directory = new File( value);
			if ( directory.exists())
				openDirectory = value;
		}
	
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);

		fileChooser.setMultiSelectionEnabled( true);

		if ( null != extensions || null != description) {
			ExampleFileFilter filter = new ExampleFileFilter();

			if ( null != extensions) {
				for ( int i = 0; i < extensions.length; ++i)
					filter.addExtension( extensions[ i]);
			}

			if ( null != description)
				filter.setDescription( description);

			fileChooser.setFileFilter( filter);
		}
	
		if ( !openDirectory.equals( ""))
			fileChooser.setCurrentDirectory( new File( openDirectory));
	
		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			File[] files = fileChooser.getSelectedFiles();
	
			File directory = fileChooser.getCurrentDirectory();
			openDirectory = directory.getAbsolutePath();
	
			Environment.get_instance().set( openDirectoryKey, openDirectory);

			return files;
		}

		return null;
	}

	/**
	 * Returns the directory selected by the user.
	 * @param directoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File get_directory(String directoryKey, String title, Component component) {
		String directoryName = "";

		File directory = null;
		String value = Environment.get_instance().get( directoryKey, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( directory.exists())
				directoryName = value;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);

		if ( !directoryName.equals( "")) {
			fileChooser.setCurrentDirectory( new File( directoryName + "/../"));
			fileChooser.setSelectedFile( directory);
		}

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			directory = fileChooser.getSelectedFile();
			directoryName = directory.getAbsolutePath();

			//File directory = fileChooser.getCurrentDirectory();
			//directory_name = directory.getAbsolutePath();

			Environment.get_instance().set( directoryKey, directoryName);

			return directory;
		}

		return null;
	}

	/**
	 * Returns the directory selected by the user.
	 * @param directoryKey the key mapped to the default directory for the file chooser dialog
	 * @param title the title of the file chooser dialog
	 * @param component the parent component of the file chooser dialog
	 * @return the directory selected by the user
	 */
	public static File[] get_directories(String directoryKey, String title, Component component) {
		File directory = null;
		String value = Environment.get_instance().get( directoryKey, "");
		if ( null != value && !value.equals( "")) {
			directory = new File( value);
			if ( !directory.exists() || !directory.isDirectory())
				directory = null;
		}

		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle( title);
		fileChooser.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY);
		fileChooser.setMultiSelectionEnabled( true);

		if ( null != directory)
			fileChooser.setCurrentDirectory( directory);

		if ( JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog( component)) {
			File[] directories = fileChooser.getSelectedFiles();
			Environment.get_instance().set( directoryKey, fileChooser.getCurrentDirectory().getAbsolutePath());
			return directories;
		}

		return null;
	}

	/**
	 * Creates the directory under the specified parent directory, and returns it.
	 * @param parent the name of the specified parent directory
	 * @return the directory
	 */
	public static File make_work_directory(String parent) {
		int index = 1;
		while ( true) {
			File work_directory = new File( parent + "/" + index);
			if ( work_directory.exists()) {
				++index;
				continue;
			}

			if ( !work_directory.mkdirs())
				return null;

			return work_directory;
		}
	}

	/**
	 * Creates the directory under the specified parent directory, and returns its name.
	 * @param parent the name of the specified parent directory
	 * @return the name of the directory
	 */
	public static String get_work_directory_name(String parent) {
		int index = 1;
		while ( true) {
			File work_directory = new File( parent + "/" + index);
			if ( work_directory.exists()) {
				++index;
				continue;
			}

			return work_directory.getAbsolutePath();
		}
	}

	/**
	 * Creates the appropriate directory under the specified parent directory, and returns its name.
	 * @param parent the name of the specified parent directory
	 * @param number the parameter to determine the name of the directory
	 * @return the name of the directory
	 */
	public static String get_work_directory_name(String parent, int number) {
		int index = 1;
		while ( true) {
			File work_directory = new File( parent + "/" + index);
			if ( work_directory.exists()) {
				++index;
				continue;
			}

			if ( 0 == number)
				return work_directory.getAbsolutePath();

			++index;
			--number;
		}
	}

	/**
	 * Returns true if the specified text is correct as the numeric value.
	 * @param text the specified text
	 * @return true if the specified text is correct as the numeric value
	 */
	public static boolean is_number_correct(String text) {
		if ( text.matches( "\\$.+"))
			return true;

		if ( 0 > text.indexOf( '/')) {
			try {
				double d = Double.parseDouble( text);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}
		} else {
			String[] elements = text.split( "/");
			if ( 2 != elements.length)
				return false;

			if ( elements[ 0].equals( "") || elements[ 1].equals( ""))
				return false;

			double numerator;
			try {
				numerator = Double.parseDouble( elements[ 0]);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}

			double denominator;
			try {
				denominator = Double.parseDouble( elements[ 1]);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true if the specified text is correct as the numeric value of probability.
	 * @param text the specified text
	 * @return true if the specified text is correct as the numeric value of probability
	 */
	public static boolean is_probability_correct(String text) {
		if ( text.matches( "\\$.+"))
			return true;

		if ( 0 > text.indexOf( '/')) {
			try {
				double d = Double.parseDouble( text);
				if ( 0.0 > d || 1.0 < d)
					return false;
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}
		} else {
			String[] elements = text.split( "/");
			if ( 2 != elements.length)
				return false;

			if ( elements[ 0].equals( "") || elements[ 1].equals( ""))
				return false;

			double numerator;
			try {
				numerator = Double.parseDouble( elements[ 0]);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}

			if ( 0.0 > numerator)
				return false;

			double denominator;
			try {
				denominator = Double.parseDouble( elements[ 1]);
			} catch (NumberFormatException e) {
				//e.printStackTrace();
				return false;
			}

			if ( 0.0 > denominator)
				return false;

			if ( numerator > denominator)
				return false;
		}

		return true;
	}

	/**
	 * Returns the array (0, 1, 2, ... 21, 22, 23)
	 * @return the array (0, 1, 2, ... 21, 22, 23)
	 */
	public static String[] get_hours() {
		return get_numbers( 24);
	}

	/**
	 * Returns the array (0, 1, 2, ... 57, 58, 59)
	 * @return the array (0, 1, 2, ... 57, 58, 59)
	 */
	public static String[] get_minutes() {
		return get_numbers( 60);
	}

	/**
	 * @param number
	 * @return
	 */
	private static String[] get_numbers(int number) {
		String[] values = new String[ number];
		for ( int i = 0; i < number; ++i)
			values[ i] = String.valueOf( i);

		return values;
	}

	/**
	 * Returns the array (00, 01, 02, ... 21, 22, 23)
	 * @return the array (00, 01, 02, ... 21, 22, 23)
	 */
	public static String[] get_hours00() {
		return get_numbers00( 24);
	}

	/**
	 * Returns the array (00, 01, 02, ... 57, 58, 59)
	 * @return the array (00, 01, 02, ... 57, 58, 59)
	 */
	public static String[] get_minutes00() {
		return get_numbers00( 60);
	}

	/**
	 * @param number
	 * @return
	 */
	private static String[] get_numbers00(int number) {
		String[] values = new String[ number];
		for ( int i = 0; i < number; ++i)
			values[ i] = ( ( i < 10) ? ( "0" + String.valueOf( i)) : String.valueOf( i));

		return values;
	}

	/**
	 * Gets the aliases in the specified text. The alias is the word which start with '$',
	 * @param text the specified text
	 * @param aliases the array of the aliases
	 * @param suffixes the array of the suffixes for the alias
	 */
	public static void get_aliases(String text, Vector<String> aliases, String[] suffixes) {
		int from = 0;
		while ( true) {
			int index = text.indexOf( '$', from);
			if ( 0 > index)
				break;

			from = get_alias( text, index, aliases, suffixes);
			if ( text.length() <= from)
				break;
		}
	}

	/**
	 * @param text
	 * @param index
	 * @param aliases
	 * @param suffixes
	 * @return
	 */
	private static int get_alias(String text, int index, Vector<String> aliases, String[] suffixes) {
		if ( text.length() - 1 <= index || '$' != text.charAt( index))
			return text.length();

		int from = index;
		String alias = "";
		for ( int i = index; i < text.length(); ++i) {
			++from;
			char c = text.charAt( i);
			if ( is_suffix( c, suffixes))
				break;

			alias += c;
		}

		if ( 1 < alias.length()
			&& !alias.equals( "$Name") && !alias.equals( "$Role") && !alias.equals( "$Spot"))
			aliases.add( alias);

		return from;
	}

	/**
	 * @param c
	 * @param suffixes
	 * @return
	 */
	private static boolean is_suffix(char c, String[] suffixes) {
		for ( int i = 0; i < suffixes.length; ++i) {
			if ( suffixes[ i].charAt( 0) == c)
				return true;
		}
		return false;
	}

	/**
	 * Sorts the integer array, and returns the sorted one. 
	 * @param indices the integer array
	 * @return the sorted integer array
	 */
	public static Vector<int[]> sort_indices(Vector<int[]> indices) {
		if ( null == indices || 2 > indices.size())
			return indices;

		int[][] array = ( int[][])indices.toArray( new int[ 0][ 0]);
		QuickSort.sort( array, new IndicesComparator());
		indices = new Vector( Arrays.asList( array));
		return indices;
	}

	/**
	 * Makes the integer array from the specified text, and returns it.
	 * @param word the specidied text
	 * @param separator the separator text
	 * @param interval the interval text
	 * @return the integer array
	 */
	public static Vector<int[]> get_indices(String word, String separator, String interval) {
		String[] words = word.split( separator);
		if ( null == words || 0 == words.length)
			return null;

		Vector<int[]> indices = new Vector<int[]>();
		for ( int i = 0; i < words.length; ++i) {
			if ( !get_indices( words[ i], interval, indices))
				return null;
		}

		if ( indices.isEmpty())
			return null;

		return indices;
	}

	/**
	 * @param word
	 * @param interval
	 * @param indices
	 * @return
	 */
	private static boolean get_indices(String word, String interval, Vector<int[]> indices) {
		String[] words = word.split( interval);
		if ( null == words)
			return false;

		int[] range;
		switch ( words.length) {
			case 1:
				try {
					int n = Integer.parseInt( words[ 0]);
					if ( 1 > n)
						return false;

					range = new int[ 2];
					range[ 0] = range[ 1] = n;
				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return false;
				}
				break;
			case 2:
				int index_from, index_to;

				try {
					index_from = Integer.parseInt( words[ 0]);
					if ( 1 > index_from)
						return false;

				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return false;
				}

				try {
					index_to = Integer.parseInt( words[ 1]);
					if ( 1 > index_to)
						return false;

				} catch (NumberFormatException e) {
					//e.printStackTrace();
					return false;
				}

				if ( index_from > index_to)
					return false;

				range = new int[ 2];
				range[ 0] = index_from;
				range[ 1] = index_to;
				break;
			default:
				return false;
		}

		append_indices( range, indices);

		return true;
	}

	/**
	 * Appends the source integer array to the destination one.
	 * @param sourceIndices the source integer array
	 * @param destinationIndices the destination integer array
	 */
	public static void append_indices(Vector<int[]> sourceIndices, Vector<int[]> destinationIndices) {
		for ( int[] range:sourceIndices)
			append_indices( range, destinationIndices);
	}

	/**
	 * @param range
	 * @param indices
	 */
	private static void append_indices(int[] range, Vector<int[]> indices) {
		for ( int i = 0; i < indices.size(); ++i) {
			int[] r = ( int[])indices.get( i);
			if ( ( ( r[ 1] + 1) < range[ 0]) || ( ( range[ 1] + 1) < r[ 0]))
				continue;

			if ( r[ 0] <= range[ 0] && range[ 1] <= r[ 1])
				return;

//			if ( 1 == indices.size()) {
//				r[ 0] = Math.min( r[ 0], range[ 0]);
//				r[ 1] = Math.max( r[ 1], range[ 1]);
//			} else {
				range[ 0] = Math.min( r[ 0], range[ 0]);
				range[ 1] = Math.max( r[ 1], range[ 1]);
				indices.removeElementAt( i);
				append_indices( range, indices);
//			}
			return;
		}

		indices.add( range);
	}

	/**
	 * Removes the source integer array from the destination one.
	 * @param sourceIndices the source integer array
	 * @param destinationIndices the destination integer array
	 */
	public static void trim_indices(Vector<int[]> sourceIndices, Vector<int[]> destinationIndices) {
		for ( int[] range:sourceIndices)
			trim_indices( range, destinationIndices);
	}

	/**
	 * @param range
	 * @param indices
	 */
	private static void trim_indices(int[] range, Vector<int[]> indices) {
		for ( int i = 0; i < indices.size(); ++i) {
			int[] r = ( int[])indices.get( i);
			if ( r[ 1] < range[ 0] || range[ 1] < r[ 0])
				continue;

			if ( range[ 0] <= r[ 0] && r[ 1] <= range[ 1]) {
				indices.removeElementAt( i);
				trim_indices( range, indices);
				return;
			}

			if ( range[ 0] <= r[ 0] && range[ 1] < r[ 1]) {
				r[ 0] = ( range[ 1] + 1);
				continue;
			}

			if ( r[ 0] < range[ 0] && r[ 1] <= range[ 1]) {
				r[ 1] = ( range[ 0] - 1);
				continue;
			}

			if ( r[ 0] < range[ 0] && range[ 1] < r[ 1]) {
				indices.insertElementAt( new int[] { range[ 1] + 1, r[ 1]}, i + 1);
				r[ 1] = ( range[ 0] - 1);
				trim_indices( range, indices);
				return;
			}
		}
	}

	/**
	 * Merges the source integer array into the destination one.
	 * @param sourceIndices the source integer array
	 * @param destinationIndices the destination integer array
	 */
	public static void merge_indices(Vector<int[]> sourceIndices, Vector<int[]> destinationIndices) {
		for ( int[] range:sourceIndices)
			merge_indices( range, destinationIndices);
	}

	/**
	 * @param range
	 * @param indices
	 */
	private static void merge_indices(int[] range, Vector<int[]> indices) {
		if ( indices.isEmpty()) {
			indices.add( new int[] { range[ 0], range[ 1]});
			return;
		}
		for ( int i = 0; i < indices.size(); ++i) {
			int[] r = ( int[])indices.get( i);
			if ( range[ 0] < r[ 0] - 1) {
				if ( range[ 1] < r[ 0] - 1) {
					indices.insertElementAt( new int[] { range[ 0], range[ 1]}, i);
					return;
				} else if ( r[ 0] - 1 <= range[ 1] && range[ 1] <= r[ 1]) {
					r[ 0] = range[ 0];
					return;
				} else {
					r[ 0] = range[ 0];
					while ( !merge_indices( i, range, indices))
						;
					return;
				}
			} else if ( range[ 0] == r[ 0] - 1) {
				r[ 0] = range[ 0];
				if ( r[ 0] - 1 <= range[ 1] && range[ 1] <= r[ 1])
					return;
				else {
					while ( !merge_indices( i, range, indices))
						;
					return;
				}
			} else if ( r[ 0] <= range[ 0] && range[ 0] <= r[ 1]) {
				if ( r[ 0] <= range[ 1] && range[ 1] <= r[ 1])
					return;
				else {
					while ( !merge_indices( i, range, indices))
						;
					return;
				}
			} else if ( r[ 1] + 1 == range[ 0]) {
				while ( !merge_indices( i, range, indices))
					;
				return;
			} else {
				if ( indices.size() - 1 == i)
					indices.add( new int[] { range[ 0], range[ 1]});
			}
		}
	}

	/**
	 * @param index
	 * @param range
	 * @param indices
	 * @return
	 */
	private static boolean merge_indices(int index, int[] range, Vector<int[]> indices) {
		int[] r1 = ( int[])indices.get( index);
		if ( indices.size() - 1 == index) {
			r1[ 1] = range[ 1];
			return true;
		} else {
			int[] r2 = ( int[])indices.get( index + 1);
			if ( range[ 1] < r2[ 0] - 1) {
				r1[ 1] = range[ 1];
				return true;
			} else if ( r2[ 0] - 1 <= range[ 1] && range[ 1] <= r2[ 1]) {
				r1[ 1] = r2[ 1];
				indices.removeElementAt( index + 1);
				return true;
			} else {
				indices.removeElementAt( index + 1);
				return false;
			}
		}
	}

	/**
	 * Returns true if the destination integer array contains the source one.
	 * @param ranges1 the destination integer array
	 * @param ranges2 the source integer array
	 * @return true if the destination integer array contains the source one
	 */
	public static boolean contains_ranges(Vector<int[]> ranges1, Vector<int[]> ranges2) {
		for ( int[] range:ranges2) {
			if ( !contains_ranges( range, ranges1))
				return false;
		}
		return true;
	}

	/**
	 * @param range
	 * @param indices
	 * @return
	 */
	private static boolean contains_ranges(int[] range, Vector<int[]> indices) {
		for ( int[] r:indices) {
			if ( ( r[ 0] <= range[ 0] && range[ 0] <= r[ 1])
				&& ( r[ 0] <= range[ 1] && range[ 1] <= r[ 1]))
				return true;
		}
		return false;
	}

	/**
	 * Makes the indices text from the specified integer array, and returns it.
	 * @param indices the specified integer array
	 * @param separator the separator text
	 * @param interval the interval text
	 * @return the indices text
	 */
	public static String get_indices(Vector<int[]> indices, String separator, String interval) {
		String script = "";
		for ( int i = 0; i < indices.size(); ++i) {
			int[] range = ( int[])indices.get( i);
			script += ( ( 0 == i) ? "" : separator);
			script += String.valueOf( range[ 0]);
			script += ( ( range[ 0] == range[ 1]) ? "" : ( interval + String.valueOf( range[ 1])));
		}
		return script;
	}

	/**
	 * Returns the maximum integer in the specified integer array.
	 * @param indices the specified integer array
	 * @return the maximum integer in the specified integer array
	 */
	public static int get_max(Vector<int[]> indices) {
		int max = 1;
		for ( int[] range:indices) {
			if ( max < range[ 1])
				max = range[ 1];
		}
		return max;
	}

	/**
	 * Returns the integer in the source integer array which the destination integer array does not contain.
	 * @param array1 the source integer array
	 * @param array2 the destination integer array
	 * @return the integer in the source integer array which the destination integer array does not contain
	 */
	public static int get_new_int_element(int[] array1, int[] array2) {
		if ( array1.length == array2.length)
			return -1;

		for ( int i = 0; i < array1.length; ++i) {
			if ( 0 > Arrays.binarySearch( array2, array1[ i]))
				return array1[ i];
		}

		return -1;
	}

	/**
	 * Returns the integer in the source integer array which the destination integer array does not contain.
	 * @param array1 the destination integer array
	 * @param array2 the source integer array
	 * @return the integer in the source integer array which the destination integer array does not contain
	 */
	public static int get_lost_int_element(int[] array1, int[] array2) {
		if ( array1.length == array2.length)
			return -1;

		for ( int i = 0; i < array2.length; ++i) {
			if ( 0 > Arrays.binarySearch( array1, array2[ i]))
				return array2[ i];
		}

		return -1;
	}
}
