/*
 * 2004/01/28
 */
package soars.common.utility.tool.common;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Vector;

import soars.common.utility.tool.sort.DoubleComparator;
import soars.common.utility.tool.sort.IntegerComparator;
import soars.common.utility.tool.sort.QuickSort;
import soars.common.utility.tool.sort.StringComparator;
import soars.common.utility.tool.sort.StringNumberComparator;
import soars.common.utility.tool.stream.StreamPumper;

/**
 * @author kurata
 */
public class Tool {

	/**
	 * @return
	 */
	public static double getVersion() {
		String version = System.getProperty( "java.version");
		String[] words = split( version, '.');
		if ( null == words || 2 > words.length)
			return -1.0;

		version = words[ 0] + "." + words[ 1];
		try {
			return Double.parseDouble( version);
		} catch (NumberFormatException e) {
			//e.printStackTrace();
			return -1.0;
		}
	}

	/**
	 * @param className
	 * @return
	 */
	public static Object get_class_instance(String className) {

		Class targetClass = null;

		try {
			targetClass = Class.forName( className);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}

		Object targetObject = null;

		try {
			targetObject = targetClass.newInstance();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
			return null;
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
			return null;
		}

		return targetObject;
	}

	/**
	 * @param srcArray
	 * @param length
	 * @param cls
	 * @return
	 */
	public static Object get_array(Object srcArray, int length, Class cls) {
		return get_array( srcArray, 0, 0, length, cls);
	}

	/**
	 * @param srcArray
	 * @param srcPos
	 * @param destPos
	 * @param length
	 * @param cls
	 * @return
	 */
	public static Object get_array(Object srcArray, int srcPos, int destPos, int length, Class cls) {
		Object destArray = Array.newInstance( cls.getComponentType(), length);
		System.arraycopy( srcArray, srcPos, destArray, destPos, length);
		return destArray;
	}

	/**
	 * @return
	 */
	public static String get_java_command() {
		String java_home = System.getProperty( "java.home");
		java_home = ( java_home.startsWith( "'") ? java_home.substring( 1, java_home.length() - 1) : java_home);
		return ( java_home + "/bin/java");
	}

//	/**
//	 * @param key
//	 * @return
//	 */
//	public static String getenv_on_unix(String key) {
//		Process process;
//		try {
//			process = Runtime.getRuntime().exec( "sh -c set");
//		} catch (IOException e) {
//			e.printStackTrace();
//			return null;
//		}
//
//		BufferedReader bufferedReader = new BufferedReader( new InputStreamReader( process.getInputStream()));
//
//		while ( true) {
//			String line = null;
//			try {
//				line = bufferedReader.readLine();
//			} catch (IOException e1) {
//				try {
//					bufferedReader.close();
//				} catch (IOException e2) {
//					e2.printStackTrace();
//				}
//				e1.printStackTrace();
//				return null;
//			}
//
//			if ( null == line)
//				break;
//
//			if ( 0 == line.length())
//				continue;
//
//			if ( !line.startsWith( key + "="))
//				continue;
//
//			return line.substring( ( key + "=").length());
//		}
//
//		return null;
//	}

	/**
	 * @param string
	 * @param start
	 * @param end
	 * @return
	 */
	public static String get_sentence(String string, String start, String end) {

		int left, right, left_offset, right_offset;
		if ( -1 != ( left_offset = start.indexOf( '(')) && -1 != ( right_offset = end.indexOf( ')'))) {
			left = '(';
			right = ')';
		} else if ( -1 != ( left_offset = start.indexOf( '{')) && -1 != ( right_offset = end.indexOf( '}'))) {
			left = '{';
			right = '}';
		} else if ( -1 != ( left_offset = start.indexOf( '[')) && -1 != ( right_offset = end.indexOf( ']'))) {
			left = '[';
			right = ']';
		} else
			return null;

		int startPosition = string.indexOf( start);
		if ( -1 == startPosition)
			return null;

		startPosition += start.length();
		if ( string.length() <= startPosition)
			return null;

		String result = string.substring( startPosition);

		if ( -1 == result.indexOf( end))
			return null;

		int counter = 1;
		for ( int i = 0; i < result.length(); ++i) {
			if ( left == result.charAt( i))
				++counter;
			else if ( right == result.charAt( i))
				--counter;

			if ( 0 == counter) {
				int offset = i - right_offset;
				if ( 0 > offset)
					return null;

				result = result.substring( 0, offset);
				return result.trim();
			}
		}

		return null;
	}

	/**
	 * @param string
	 * @param start
	 * @param end
	 * @return
	 */
	public static String[] get_sentences(String string, String start, String end) {

		int left, right, left_offset, right_offset;
		if ( -1 != ( left_offset = start.indexOf( '(')) && -1 != ( right_offset = end.indexOf( ')'))) {
			left = '(';
			right = ')';
		} else if ( -1 != ( left_offset = start.indexOf( '{')) && -1 != ( right_offset = end.indexOf( '}'))) {
			left = '{';
			right = '}';
		} else if ( -1 != ( left_offset = start.indexOf( '[')) && -1 != ( right_offset = end.indexOf( ']'))) {
			left = '[';
			right = ']';
		} else
			return null;

		String target = string;
		target = target.trim();

		Vector<String> resultVector = new Vector<String>();
		int counter = 0;

		while ( true) {

			if ( !target.startsWith( start)) {
				if ( 0 < target.length())
					resultVector.add( target);

				break;
			}

			if ( target.length() <= ( start.length() + end.length()))
				break;

			target = target.substring( start.length());

			counter = 1;

			for ( int i = 0; i < target.length(); ++i) {
				if ( left == target.charAt( i))
					++counter;
				else if ( right == target.charAt( i))
					--counter;
	
				if ( 0 == counter) {
					int offset = i - right_offset;
					if ( 0 > offset)
						return null;
	
					resultVector.add( target.substring( 0, offset).trim());
					target = target.substring( offset + end.length());
					target = target.trim();
					break;
				}
			}
		}

		if ( resultVector.isEmpty())
			return null;

		String[] sentences = new String[ resultVector.size()];
		for ( int i = 0; i < resultVector.size(); ++i)
			sentences[ i] = resultVector.get( i);

		return sentences;
	}

	/**
	 * @param string
	 * @param character
	 * @return
	 */
	public static String remove_character_from_string_head(String string, char character) {

		String newString = "";

		for ( int i = 0; i < string.length(); ++i) {
			if ( character != string.charAt( i))
				newString += string.charAt( i);
		}

		return newString;
	}

	/**
	 * @param sentence
	 * @param start
	 * @return
	 */
	public static String get_sub_string(String sentence, String start) {
		return sentence.substring( sentence.indexOf( start));
	}

	/**
	 * @param string
	 * @param start
	 * @param end
	 * @param newString
	 * @return
	 */
	public static String replace_string(String string, String start, String end, String newString) {

		int left, right, left_offset, right_offset;
		if ( -1 != ( left_offset = start.indexOf( '(')) && -1 != ( right_offset = end.indexOf( ')'))) {
			left = '(';
			right = ')';
		} else if ( -1 != ( left_offset = start.indexOf( '{')) && -1 != ( right_offset = end.indexOf( '}'))) {
			left = '{';
			right = '}';
		} else if ( -1 != ( left_offset = start.indexOf( '[')) && -1 != ( right_offset = end.indexOf( ']'))) {
			left = '[';
			right = ']';
		} else
			return null;

		int startPosition = string.indexOf( start);
		if ( -1 == startPosition)
			return null;

		int endPosition = string.indexOf( end);
		if ( -1 == endPosition)
			return null;

		endPosition += end.length();

		return ( string.substring( 0, startPosition) + newString + string.substring( endPosition));
	}

	/**
	 * @param sentence
	 * @param string
	 * @param newString
	 * @return
	 */
	public static String replace_string(String sentence, String string, String newString) {

		if ( sentence.length() < string.length())
			return sentence;

		Vector<Integer> indexVector = new Vector<Integer>();
		int index = 0;
		while ( true) {
			index = sentence.indexOf( string, index);
			if ( -1 == index)
				break;

			if ( 0 == index) {
				if ( sentence.length() == string.length()) {
					indexVector.add( new Integer( 0));
					break;
				} else {
					if ( ' ' == sentence.charAt( string.length()))
						indexVector.add( new Integer( 0));

					index = string.length();
				}
			} else if ( ( sentence.length() - string.length()) == index) {
				if ( ' ' == sentence.charAt( index - 1))
					indexVector.add( new Integer( index));

				break;

			} else if ( ( 0 < index)  && ( ( sentence.length() - string.length()) > index)) {
				if ( ( ' ' == sentence.charAt( index - 1)) && ( ' ' == sentence.charAt( index + string.length())))
					indexVector.add( new Integer( index));

				index += string.length();
			} else
				break;
		}

		if ( indexVector.isEmpty())
			return sentence;

		int[] indices = new int[ indexVector.size()];
		for ( int i = 0; i < indices.length; ++i) {
			Integer integer = indexVector.get( i);
			indices[ i] = integer.intValue();
		}

		String result = "";
		if ( 0 < indices[ 0])
			result = sentence.substring( 0, indices[ 0]);

		for ( int i = 0; i < indices.length; ++i) {
			result += newString;
			if ( indices.length - 1 > i)
				result += sentence.substring( indices[ i] + string.length(), indices[ i + 1]);
			else {
				if ( sentence.length() > ( indices[ i] + string.length()))
					result += sentence.substring( indices[ i] + string.length());
			}
		}

		return result;
	}

	/**
	 * @param vector
	 * @return
	 */
	public static boolean is_every_string_same(Vector<String> vector) {

		String string0 = vector.get( 0);

		for ( int i = 1; i < vector.size(); ++i) {
			String string = vector.get( i);
			if ( !string.equals( string0))
				return false;
		}

		return true;
	}

	/**
	 * @param vector
	 * @param targetVector
	 * @return
	 */
	public static boolean has_same_string(Vector<String> vector, Vector<String> targetVector) {

		for ( int i = 0; i < vector.size(); ++i) {
			String string = vector.get( i);
			if ( targetVector.contains( string))
				return true;
		}

		return false;
	}

	/**
	 * @param srcVector0
	 * @param srcVector1
	 * @param destVector0
	 * @param destVector1
	 */
	public static boolean has_same_string(Vector<String> srcVector0, Vector<String> srcVector1, Vector<String> destVector0, Vector<String> destVector1) {

		if ( srcVector0.size() != srcVector1.size() || destVector0.size() != destVector1.size())
			return false;

		for ( int i = 0; i < srcVector0.size(); ++i) {
			String src0 = srcVector0.get( i);
			String src1 = srcVector1.get( i);
			for ( int j = 0; j < destVector0.size(); ++j) {
				String dest0 = destVector0.get( j);
				String dest1 = destVector1.get( j);
				if ( dest0.equals( src0) && dest1.equals( src1))
					return true;
			}
		}

		return false;
	}

	/**
	 * @param srcVector0
	 * @param srcVector1
	 * @param destVector0
	 * @param destVector1
	 */
	public static boolean append_vector(Vector<String> srcVector0, Vector<String> srcVector1, Vector<String> destVector0, Vector<String> destVector1) {

		if ( srcVector0.size() != srcVector1.size() || destVector0.size() != destVector1.size())
			return false;

		boolean result = false;
		for ( int i = 0; i < srcVector0.size(); ++i) {
			String src0 = srcVector0.get( i);
			String src1 = srcVector1.get( i);

			boolean found = false;
			for ( int j = 0; j < destVector0.size(); ++j) {
				String dest0 = destVector0.get( j);
				String dest1 = destVector1.get( j);
				if ( dest0.equals( src0) && dest1.equals( src1)) {
					found = true;
					break;
				}
			}

			if ( found)
				continue;

			destVector0.add( src0);
			destVector1.add( src1);
			result = true;
		}

		return result;
	}

	/**
	 * @param vector
	 * @param string
	 * @return
	 */
	public static int has_string(Vector<String> vector, String string) {

		for ( int i = 0; i < vector.size(); ++i) {
			if ( string.equals( vector.get( i)))
				return i;
		}

		return -1;
	}

	/**
	 * @param coefficient
	 * @param exponent
	 * @return
	 */
	public static String get_constant(double coefficient, double exponent) {

		String constant;

		if ( 1.0 == coefficient) {
			if ( 0.0 == exponent)
				constant = "";
			else if ( 1.0 == exponent)
				constant = "10.0";
			else
				constant = ( "Math.pow( 10.0, " + exponent + " )");
		} else {
			if ( 0.0 == exponent)
				constant = ( String.valueOf( coefficient));
			else if ( 1.0 == exponent)
				constant = ( coefficient + " * 10.0");
			else
				constant = ( coefficient + " * Math.pow( 10.0, " + exponent + " )");
		}

		return constant;
	}

	/**
	 * @param expression
	 * @return
	 */
	public static String[] get_variable_names_from_expression(String expression) {

		Vector<String> resultVector = new Vector<String>();

		String sentence = "";

		for ( int i = 0; i < expression.length(); ++i) {

			char character = expression.charAt( i);

			if ( ' ' != character)
				sentence += character;

			if ( ' ' == character || ( ' ' != character && expression.length() - 1 == i)) {
				if ( 0 == sentence.length())
					continue;
				else {
					if ( -1 == sentence.indexOf( '(')
						&& -1 == sentence.indexOf( ')')
						&& -1 == sentence.indexOf( '+')
						&& -1 == sentence.indexOf( '-')
						&& -1 == sentence.indexOf( '*')
						&& -1 == sentence.indexOf( '/')
						&& -1 == sentence.indexOf( '=')
						&& -1 == sentence.indexOf( '>')
						&& -1 == sentence.indexOf( '<')
						&& -1 == sentence.indexOf( '.')) {
						if ( sentence.endsWith( ","))
							sentence = sentence.substring( 0, sentence.lastIndexOf( ','));

						if ( 0 != sentence.length())
							resultVector.add( sentence);
					}

					sentence = "";
				}
			}
		}

		if ( resultVector.isEmpty())
			return null;

		String[] sentences = new String[ resultVector.size()];
		for ( int i = 0; i < resultVector.size(); ++i)
			sentences[ i] = resultVector.get( i);

		return sentences;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean is_int(String string) {
		return ( string.matches( "[0-9]+")
			|| string.matches( "-[0-9]+"));
//
//		String temp = string.trim();
//
//		if ( 0 == temp.length())
//			return false;
//
//		for ( int i = 0; i < temp.length(); ++i) {
//
//			char character = temp.charAt( i);
//
//			if ( 0 == i && '-' == character)
//				continue;
//
//			if ( '0' != character
//				&& '1' != character
//				&& '2' != character
//				&& '3' != character
//				&& '4' != character
//				&& '5' != character
//				&& '6' != character
//				&& '7' != character
//				&& '8' != character
//				&& '9' != character)
//				return false;
//		}
//
//		if ( temp.equals( "-"))
//			return false;
//
//		return true;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean is_positive_int(String string) {
		return ( string.matches( "[0-9]+"));
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean is_negative_int(String string) {
		return ( string.matches( "-[0-9]+"));
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean is_double(String string) {
		return ( string.matches( "[0-9]+")
			|| string.matches( "-[0-9]+")
			|| string.matches( "[0-9]+[.][0-9]+")
			|| string.matches( "-[0-9]+[.][0-9]+"));
//
//		String temp = string.trim();
//
//		if ( 0 == temp.length())
//			return false;
//
//		boolean point = false;
//
//		for ( int i = 0; i < temp.length(); ++i) {
//
//			char character = temp.charAt( i);
//
//			if ( 0 == i && '-' == character)
//				continue;
//
//			if ( '.' == character) {
//				if ( point)
//					return false;
//				else
//					point = true;
//			}
//
//			if ( '0' != character
//				&& '1' != character
//				&& '2' != character
//				&& '3' != character
//				&& '4' != character
//				&& '5' != character
//				&& '6' != character
//				&& '7' != character
//				&& '8' != character
//				&& '9' != character)
//				return false;
//		}
//
//		if ( !point)
//			return false;
//
//		if ( temp.equals( "-.") || temp.equals( "."))
//			return false;
//
//		return true;
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean is_positive_double(String string) {
		return ( string.matches( "[0-9]+")
			|| string.matches( "[0-9]+[.][0-9]+"));
	}

	/**
	 * @param string
	 * @return
	 */
	public static boolean is_negative_double(String string) {
		return ( string.matches( "-[0-9]+")
			|| string.matches( "-[0-9]+[.][0-9]+"));
	}

	/**
	 * @param text
	 * @param separator
	 * @return
	 */
	public static String[] split(String text, char separator) {
		List<String> words = new ArrayList<String>();
		String word = "";
		for ( int i = 0; i < text.length(); ++i) {
			char c = text.charAt( i);
			if ( separator == c) {
				words.add( word);
				word = "";
				continue;
			}

			word += c;
		}

		words.add( word);

		return ( String[])words.toArray( new String[ 0]);
	}

	/**
	 * @param list
	 * @param ascend
	 * @param toLower
	 * @return
	 */
	public static String[] quick_sort_string(List<String> list, boolean ascend, boolean toLower) {
		return quick_sort_string( list, new StringComparator( ascend, toLower));
	}

	/**
	 * @param list
	 * @param ascend
	 * @param toLower
	 * @return
	 */
	public static String[] quick_sort_string_with_number(List<String> list, boolean ascend, boolean toLower) {
		return quick_sort_string( list, new StringNumberComparator( ascend, toLower));
	}

	/**
	 * @param list
	 * @param comparator
	 * @return
	 */
	private static String[] quick_sort_string(List<String> list, Comparator<Object> comparator) {
		if ( list.isEmpty())
			return null;

		String[] result = null;
		if ( 1 == list.size()) {
			result = new String[ 1];
			result[ 0] = ( String)list.get( 0);
		} else {
			result = new String[ list.size()];
			for ( int i = 0; i < list.size(); ++i)
				result[ i] = ( String)list.get( i);

			QuickSort.sort( result, comparator);
		}

		return result;
	}

	/**
	 * @param numericValues
	 * @return
	 */
	public static int get_minimum(int[] numericValues) {
		int[] result = quick_sort_int( numericValues, true);
		return result[ 0];
	}

	/**
	 * @param numericValues
	 * @return
	 */
	public static int get_maximum(int[] numericValues) {
		int[] result = quick_sort_int( numericValues, true);
		return result[ result.length - 1];
	}

	/**
	 * @param numericValues
	 * @param ascend
	 * @return
	 */
	public static int[] quick_sort_int(int[] numericValues, boolean ascend) {
		Integer[] integers = new Integer[ numericValues.length];
		for ( int i = 0; i < numericValues.length; ++i)
			integers[ i] = new Integer( numericValues[ i]);

		integers = quick_sort_integer( integers, new IntegerComparator( ascend));

		int[] result = new int[ integers.length];
		for ( int i = 0; i < integers.length; ++i)
			result[ i] = integers[ i].intValue();

		return result;
	}

	/**
	 * @param integers
	 * @param comparator
	 * @return
	 */
	private static Integer[] quick_sort_integer(Integer[] integers, Comparator<Object> comparator) {
		if ( 1 == integers.length)
			return integers;

		QuickSort.sort( integers, comparator);
		return integers;
	}

	/**
	 * @param numericValues
	 * @return
	 */
	public static double get_minimum(double[] numericValues) {
		double[] result = quick_sort_double( numericValues, true);
		return result[ 0];
	}

	/**
	 * @param numericValues
	 * @return
	 */
	public static double get_maximum(double[] numericValues) {
		double[] result = quick_sort_double( numericValues, true);
		return result[ result.length - 1];
	}

	/**
	 * @param numericValues
	 * @param ascend
	 * @return
	 */
	public static double[] quick_sort_double(double[] numericValues, boolean ascend) {
		Double[] doubles = new Double[ numericValues.length];
		for ( int i = 0; i < numericValues.length; ++i)
			doubles[ i] = new Double( numericValues[ i]);

		doubles = quick_sort_double( doubles, new DoubleComparator( ascend));

		double[] result = new double[ doubles.length];
		for ( int i = 0; i < doubles.length; ++i)
			result[ i] = doubles[ i].doubleValue();

		return result;
	}

	/**
	 * @param doubles
	 * @param comparator
	 * @return
	 */
	private static Double[] quick_sort_double(Double[] doubles, Comparator<Object> comparator) {
		if ( 1 == doubles.length)
			return doubles;

		QuickSort.sort( doubles, comparator);
		return doubles;
	}

	/**
	 * @param string
	 * @param validValues
	 * @return
	 */
	public static boolean is_string_valid(String string, String validValues) {
		if ( null == string || null == validValues)
			return false;

		for (int i = 0; i < string.length(); i++) {
			if ( -1 == validValues.indexOf( string.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param string
	 * @param target
	 * @param replacement
	 * @return
	 */
	public static String replace_all(String string, String target, String replacement) {
		int from = 0;
		while ( true) {
			int index = string.indexOf( target, from);
			if ( 0 > index || string.length() <= index)
				break;

			string = ( string.substring( 0, index) + replacement
				+ ( ( string.length() > ( index + target.length())) ? string.substring( index + target.length()) : ""));
			from = index + target.length();
			if ( string.length() <= from)
				break;
		}

		return string;
	}

	/**
	 * @param text
	 * @return
	 */
	public static String format_real_number_text(String text) {
		if ( text.equals( "") || text.equals( ".")
			|| text.equals( "-") || text.equals( "-.")
			|| text.equals( "+") || text.equals( "+.")
			|| text.matches( ".+[eE].+"))
			return text;

		String result = text;
		if ( result.startsWith( "+"))
			result = result.substring( 1);

		if ( result.startsWith( "."))
			result = ( "0" + result);

		if ( result.matches( "-\\.[0-9]+"))
			result = result.replaceFirst( "-", "-0");

		if ( result.endsWith( "."))
			result += "0";
		else {
			if ( 0 > result.indexOf( "."))
				result += ".0";
		}

		if ( result.matches( "[0]+\\.[0]+") || result.matches( "-[0]+\\.[0]+"))
			return "0.0";
		else if ( result.matches( "[0]{2,}\\.[0-9]+") || result.matches( "-[0]{2,}\\.[0-9]+"))
			result = result.replaceFirst( "[0]{2,}", "0");
		else if ( result.matches( "[0]+[1-9][0-9]*\\.[0-9]+") || result.matches( "-[0]+[1-9][0-9]*\\.[0-9]+"))
			result = result.replaceFirst( "[0]+", "");

		int end = result.indexOf( ".");
		if ( 0 > end || result.length() - 2 <= end)
			return result;

		int i;
		for ( i = result.length() - 1; i > end + 1; --i) {
			if ( '0' != result.charAt( i))
				break;
		}

		if ( result.length() - 1 > i)
			result = result.substring( 0, i + 1);

		return result;
	}

	/**
	 * @param text
	 * @return
	 */
	public static String format_real_number_text_not_always_decimal(String text) {
		if ( text.equals( "") || text.equals( ".")
			|| text.equals( "-") || text.equals( "-.")
			|| text.equals( "+") || text.equals( "+.")
			|| text.matches( ".+[eE].+"))
			return text;

		String result = text;
		if ( result.startsWith( "+"))
			result = result.substring( 1);

		if ( result.startsWith( "."))
			result = ( "0" + result);

		if ( result.matches( "-\\.[0-9]+"))
			result = result.replaceFirst( "-", "-0");

		if ( result.endsWith( "."))
			result += "0";

		if ( result.matches( "[0]{2,}") || result.matches( "-[0]{2,}"))
			return "0";
		else if ( result.matches( "[0]+\\.[0]+") || result.matches( "-[0]+\\.[0]+"))
			return "0.0";
		else if ( result.matches( "[0]{2,}\\.[0-9]+") || result.matches( "-[0]{2,}\\.[0-9]+"))
			result = result.replaceFirst( "[0]{2,}", "0");
		else if ( result.matches( "[0]+[1-9][0-9]*") || result.matches( "-[0]+[1-9][0-9]*")
			|| result.matches( "[0]+[1-9][0-9]*\\.[0-9]+") || result.matches( "-[0]+[1-9][0-9]*\\.[0-9]+"))
			result = result.replaceFirst( "[0]+", "");

		int end = result.indexOf( ".");
		if ( 0 > end || result.length() - 2 <= end)
			return result;

		int i;
		for ( i = result.length() - 1; i > end + 1; --i) {
			if ( '0' != result.charAt( i))
				break;
		}

		if ( result.length() - 1 > i)
			result = result.substring( 0, i + 1);

		return result;
	}

	/**
	 * @param text
	 * @return
	 */
	public static String format_plus_real_number_text(String text) {
		if ( text.equals( "") || text.equals( ".")
			|| text.equals( "-") || text.equals( "-.")
			|| text.equals( "+") || text.equals( "+.")
			|| text.matches( ".+[eE].+"))
			return text;

		String result = text;
		if ( result.startsWith( "+"))
			result = result.substring( 1);

		if ( result.startsWith( "."))
			result = ( "0" + result);

		if ( result.endsWith( "."))
			result += "0";
		else {
			if ( 0 > result.indexOf( "."))
				result += ".0";
		}

		if ( result.matches( "[0]+\\.[0]+"))
			return "0.0";
		else if ( result.matches( "[0]{2,}\\.[0-9]+"))
			result = result.replaceFirst( "[0]{2,}", "0");
		else if ( result.matches( "[0]+[1-9][0-9]*\\.[0-9]+"))
			result = result.replaceFirst( "[0]+", "");

		int end = result.indexOf( ".");
		if ( 0 > end || result.length() - 2 <= end)
			return result;

		int i;
		for ( i = result.length() - 1; i > end + 1; --i) {
			if ( '0' != result.charAt( i))
				break;
		}

		if ( result.length() - 1 > i)
			result = result.substring( 0, i + 1);

		return result;
	}

	/**
	 * @param text
	 * @return
	 */
	public static String format_plus_real_number_text_not_always_decimal(String text) {
		if ( text.equals( "") || text.equals( ".")
			|| text.equals( "-") || text.equals( "-.")
			|| text.equals( "+") || text.equals( "+.")
			|| text.matches( ".+[eE].+"))
			return text;

		String result = text;
		if ( result.startsWith( "+"))
			result = result.substring( 1);

		if ( result.startsWith( "."))
			result = ( "0" + result);

		if ( result.endsWith( "."))
			result += "0";

		if ( result.matches( "[0]{2,}"))
			return "0";
		else if ( result.matches( "[0]+\\.[0]+"))
			return "0.0";
		else if ( result.matches( "[0]{2,}\\.[0-9]+"))
			result = result.replaceFirst( "[0]{2,}", "0");
		else if ( result.matches( "[0]+[1-9][0-9]*") || result.matches( "[0]+[1-9][0-9]*\\.[0-9]+"))
			result = result.replaceFirst( "[0]+", "");

		int end = result.indexOf( ".");
		if ( 0 > end || result.length() - 2 <= end)
			return result;

		int i;
		for ( i = result.length() - 1; i > end + 1; --i) {
			if ( '0' != result.charAt( i))
				break;
		}

		if ( result.length() - 1 > i)
			result = result.substring( 0, i + 1);

		return result;
	}

	/**
	 * @param indices
	 * @param index
	 * @return
	 */
	public static boolean contains(int[] indices, int index) {
		for ( int i = 0; i < indices.length; ++i) {
			if ( indices[ i] == index)
				return true;
		}
		return false;
	}

	/**
	 * @param command
	 * @return
	 */
	public static String get_uuid(String command) {
		Process process;
		InputStream inputStream;
		try {
			process = ( Process)Runtime.getRuntime().exec( command);
			inputStream = process.getInputStream();
			new StreamPumper( process.getErrorStream(), false).start();
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}

		String uuid = read( inputStream);
		if ( null == uuid)
			return null;

		uuid = uuid.substring( 0, 36);

		if ( !uuid.matches( "([0-9A-Fa-f]{8})-([0-9A-Fa-f]{4})-([0-9A-Fa-f]{4})-([0-9A-Fa-f]{4})-([0-9A-Fa-f]{12})"))
			return null;

		return uuid;
	}

	/**
	 * @param inputStream
	 * @return
	 */
	public static String read(InputStream inputStream) {
		String text = "";
		try {
			while ( true) {
				int c = inputStream.read();
				if ( -1 == c)
					break;

				text += ( char)c;
			}
		} catch (IOException e) {
			//e.printStackTrace();
			return null;
		}

		return text;
	}

	/**
	 * @param array
	 * @return
	 */
	public static boolean is_consecutive(int[] array) {
		for ( int i = 0; i < array.length - 1; ++i) {
			if ( array[ i + 1] != array[ i] + 1)
				return false;
		}
		return true;
	}

	/**
	 * @return
	 */
	static public String encodeHtmlFromString(String value) {
		if (value==null)
			return "";

		StringBuilder sb = new StringBuilder();
		int len = value.length();
		char c;
		boolean skipLF = false;
		for (int i = 0; i < len; i++) {
			c = value.charAt(i);

			if (skipLF) {
				skipLF = false;
				if (c == '\n') {
					continue;
				} else {
					sb.append("<br>");
				}
			}

			switch (c) {
				case ' ' :
					sb.append("&nbsp;");
					break;
				case '&' :
					sb.append("&amp;");
					break;
				case '"' :
					sb.append("&quot;");
					break;
				case '<' :
					sb.append("&lt;");
					break;
				case '>' :
					sb.append("&gt;");
					break;
				case '\n' :
					sb.append("<br>");
					break;
				case '\r' :
					skipLF = true;
					break;
				case '\t' :
					sb.append("&nbsp;");
					break;
				case 0x0B :
					sb.append("<br>");
					break;
				default :
					sb.append(c);
			}
		}

		if (skipLF) {
			sb.append("<br>");
		}

		return sb.toString();
	}

	/**
	 * @return
	 */
	static public String encodeXmlFromString(String value) {
		if ( null == value)
			return "";

		StringBuilder stringBuilder = new StringBuilder();

		for ( int i = 0; i < value.length(); ++i) {
			char c = value.charAt(i);
			if ( '<' == c)
				stringBuilder.append( "&lt;");
			else if ( '&' == c)
				stringBuilder.append( "&amp;");
			else if ( '>' == c)
				stringBuilder.append( "&gt;");
			else if ( ( 0x20 > c && ( 0x09 != c && 0x0a != c && 0x0d != c)) || 0x7f == c)
				stringBuilder.append( " ");
			else
				stringBuilder.append(c);
		}

		return stringBuilder.toString();
	}

	/**
	 * @param list
	 * @return
	 */
	public static int[] get_array(List<Integer> list) {
		int[] array = new int[ list.size()];
		for ( int i = 0; i < list.size(); ++i)
			array[ i] = list.get( i).intValue();
		return array;
	}
}
