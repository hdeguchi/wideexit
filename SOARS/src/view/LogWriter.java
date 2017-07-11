package view;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;

import env.Environment;
import env.EquippedObject;
import env.StepCounter;

/**
 * The LogWriter class represents writer object assigned to log output.
 * @author H. Tanuma / SOARS project
 */
public class LogWriter extends PrintWriter {

	/**
	 * The LogWriter.Factory interface represents factory for LogWriter class.
	 * @author H. Tanuma / SOARS project
	 */
	public interface Factory {
		public LogWriter create(List<? extends EquippedObject> source, String key) throws Exception;
	}

	List<? extends EquippedObject> source;
	String key;
	char separator = '\t';
	LinkedHashSet<EquippedObject> selves = new LinkedHashSet<EquippedObject>();
	String past = null;
	int sizeTitle = 0;

	public LogWriter(Writer out, List<? extends EquippedObject> source, String key) {
		super(out);
		this.source = source;
		this.key = key;
	}
	public LogWriter(OutputStream out, List<? extends EquippedObject> source, String key) {
		super(out);
		this.source = source;
		this.key = key;
	}
	public void setSeparator(char separator) {
		this.separator = separator;
	}
	public boolean title() {
		int size = selves.size();
		if (size != source.size()) {
			Iterator<? extends EquippedObject> it = source.iterator();
			while (it.hasNext()) {
				EquippedObject self = it.next();
				if (!selves.contains(self) && self.getProp(key) != null) {
					selves.add(self);
				}
			}
		}
		if (size != 0) {
			return false;
		}
		size = selves.size();
		if (size == 0) {
			return true;
		}
		sizeTitle = size;
		print(separator);
		print(EquippedObject.ID_KEY);
		println(dump(EquippedObject.ID_KEY));
		print(separator);
		print(EquippedObject.NAME_KEY);
		println(dump(EquippedObject.NAME_KEY));
		print(separator);
		print(key);
		println(dump(key));
		return false;
	}
	protected String dump(String key) {
		StringBuffer buffer = new StringBuffer();
		Iterator<EquippedObject> it = selves.iterator();
		while (it.hasNext()) {
			buffer.append(separator).append(it.next().getProp(key));
		}
		return buffer.toString();
	}
	public void log(boolean skip) {
		if (title()) {
			return;
		}
		String dump = dump(key);
		if (skip) {
			if (dump.equals(past)) {
				return;
			}
			past = dump;
		}
		else if (past != null) {
			past = dump;
		}
		StepCounter stepCounter = Environment.getCurrent().getStepCounter();
		print(stepCounter);
		print(separator);
		print(stepCounter.getStep());
		println(dump);
	}
	public void close() {
		if (selves.size() != sizeTitle) {
			print(separator);
			print(EquippedObject.ID_KEY);
			println(dump(EquippedObject.ID_KEY));
			print(separator);
			print(EquippedObject.NAME_KEY);
			println(dump(EquippedObject.NAME_KEY));
		}
		flush();
		super.close();
	}
}
