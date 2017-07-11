/**
 * 
 */
package soars.application.visualshell.file.importer.initial.comment;

import java.util.HashMap;

import soars.application.visualshell.main.Constant;
import soars.application.visualshell.main.ResourceManager;

/**
 * The comment data hashtable(type[String] - line[String]).
 * @author kurata / SOARS project
 */
public class CommentDataMap extends HashMap<String, String> {

	/**
	 * Creates this object.
	 */
	public CommentDataMap() {
		super();
	}

	/**
	 * Returns true for appending the comment data successfully.
	 * @param words the array of words extracted from the specified line
	 * @param line the specified line
	 * @param number the line number
	 * @return true for appending the comment data successfully
	 */
	public boolean append(String[] words, String line, int number) {
		if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.comment.title"))
			|| words[ 1].equals( ResourceManager.get_instance().get( "initial.data.comment.date"))
			|| words[ 1].equals( ResourceManager.get_instance().get( "initial.data.comment.author"))
			|| words[ 1].equals( ResourceManager.get_instance().get( "initial.data.comment.mail")))
			put( words[ 1], words[ 2]);
		else if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.comment.export")))
			return true;
//			put( words[ 1], String.valueOf( words[ 2].equals( "true")));
		else if ( words[ 1].equals( ResourceManager.get_instance().get( "initial.data.comment.comment"))) {
			String comment = ( String)get( words[ 1]);
			if ( null == comment)
				comment = "";

			comment += ( words[ 2] + Constant._lineSeparator);
			put( words[ 1], comment);
		} else
			return false;

		return true;
	}
}
