/**
 * 
 */
package soars.application.visualshell.object.arbitrary.select;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import soars.application.visualshell.main.MainFrame;
import soars.application.visualshell.main.ResourceManager;

/**
 * @author kurata
 *
 */
public class JarAndClassMap extends HashMap<JarAndClass, JarAndClass> {

	/**
	 * 
	 */
	public JarAndClassMap() {
		super();
	}

	/**
	 * @param jarFilename
	 * @param classname
	 * @return
	 */
	public JarAndClass get(String jarFilename, String classname) {
		Iterator iterator = entrySet().iterator();
		while ( iterator.hasNext()) {
			Object object = iterator.next();
			Map.Entry entry = ( Map.Entry)object;
			JarAndClass jarAndClass = ( JarAndClass)entry.getKey();
			if ( jarAndClass.equals( jarFilename, classname))
				return ( JarAndClass)entry.getValue();
		}
		return null;
	}

	/**
	 * @param jarFilename
	 * @param classname
	 * @return
	 */
	public JarAndClass select(String jarFilename, String classname) {
		// 新たなjarFilenameとclassnameが選択された場合は、新たなJarAndClassを作ってこのマップへ追加する
		// 無視する場合はそのままのjarFilenameとclassnameでJarAndClassを作って返す
		// 読み込みを中止する場合はnullを返す
		SelectJarAndClassDlg selectJarAndClassDlg = new SelectJarAndClassDlg(
			MainFrame.get_instance(),
			ResourceManager.get_instance().get( "select.jar.and.class.dialog.title"),
			true,
			jarFilename,
			classname,
			this);
		selectJarAndClassDlg.do_modal( MainFrame.get_instance());
		return selectJarAndClassDlg._jarAndClass;
	}
}
