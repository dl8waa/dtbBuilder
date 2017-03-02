package de.dl8waa.dtbbuilder.utils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public class Utils {
	/**
	 * Add a file name extension if not existing yet. 
	 * @param fileName
	 * @param ext
	 * @return Full filename
	 */
//	public static String addFileExtension(String fileName, String ext) {
//		if ((fileName != null) && (!fileName.toLowerCase().endsWith(ext))) {
//			return fileName + ext;
//		}
//		return fileName;
//	}
	
	/**
	 * Check, if a file exists or not.
	 * 
	 * @param fileName Name of the file to be checked.
	 * @return File exists or not.
	 */
	public static boolean fileExists(String fileName) {
		return new File(fileName).exists();
	}
	
	/**
	 * Extract the file name from a full path. Example: "C:\temp\MyFile.txt"
	 * will be reduced to MyFile.txt.
	 * 
	 * @param fullPath
	 * @return Extracted file name.
	 */
	public static String extractFileName(String fullPath) {
		Path p = Paths.get(fullPath);
		return p.getFileName().toString();
	}
	
	/**
	 * Arrange a Shell object in screen center.
	 * @param display Current Display .
	 * @param shell Shell object to be arranged.
	 */
	public static void centerShell(Display display, Shell shell) {
		Monitor primary = display.getPrimaryMonitor();
		Rectangle bounds = primary.getBounds();
		Rectangle rect = shell.getBounds();

		int x = bounds.x + (bounds.width - rect.width) / 2;
		int y = bounds.y + (bounds.height - rect.height) / 2;

		shell.setLocation(x, y);
	}
	
	/**
	 * Show a MessageBox dialog.
	 * 
	 * @param shell Current Shell. If null, temporary Display and Shell are created.  
	 * @param message Text to be shown.
	 * @param style MessageBox style parameter. See MessageBox documentation.
	 * @return The MessageBox return value. See MessageBox documentation. Example values are
	 * 		   SWT.ICON_ERROR
	 * 		   SWT.ICON_INFORMATION | SWT.OK
	 * 		   SWT.ICON_INFORMATION | SWT.OK | SWT.CANCEL
	 */
	public static int showMessage(Shell shell, String message, int style) {
		if (shell == null) {
			Display display = Display.getCurrent();
			if (display == null) {
				display = new Display();
			}
			shell = new Shell(display);
		}

		MessageBox messageBox = new MessageBox(shell, style);
		messageBox.setMessage(message);
		return messageBox.open();
	}
}
