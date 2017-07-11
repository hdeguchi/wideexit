/**
 * 
 */
package soars.common.soars.tool;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import soars.common.soars.constant.CommonConstant;
import soars.common.utility.tool.file.ZipUtility;

/**
 * @author kurata
 *
 */
public class Converter {

	/**
	 * @param file
	 * @return
	 */
	public static File vsl2soars(File file) {
		if ( !file.getName().endsWith( ".vsl"))
			return null;

		File outputFile = new File( file.getParentFile(), file.getName().substring( 0, file.getName().length() - ".vsl".length()) + ".soars");

		try {
			ZipOutputStream zipOutputStream = new ZipOutputStream( new FileOutputStream( outputFile));
			ZipEntry inputZipEntry = null;
			try {
				zipOutputStream.putNextEntry( new ZipEntry( CommonConstant._soarsRootDirectoryName + "/"));
				zipOutputStream.closeEntry();

				zipOutputStream.putNextEntry( new ZipEntry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._visualShell4IdentifyFileName));
				zipOutputStream.closeEntry();

				zipOutputStream.putNextEntry( new ZipEntry( CommonConstant._soarsRootDirectoryName + "/" + CommonConstant._visualShellZipFileName));
				BufferedInputStream bufferedInputStream = new BufferedInputStream( new FileInputStream( file));
				byte buf[] = new byte[ ZipUtility._bufferSize];
				int count;
				while ( -1 != ( count = bufferedInputStream.read( buf, 0, ZipUtility._bufferSize)))
					zipOutputStream.write( buf, 0, count);
				zipOutputStream.closeEntry();
			} finally {
				zipOutputStream.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

		return outputFile;
	}
}
