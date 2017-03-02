package de.dl8waa.dtbbuilder.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

import static de.dl8waa.dtbbuilder.Constants.*;

public class FileSelector {
	private final String   ALL_FILES_FILTER       	= "*.*";
    private final String   ALL_FILES_FILTER_TEXT  	= "All files (" + ALL_FILES_FILTER + ")";
	
    private final String   FCC_FILE_EXT       		= ".dat";
    private final String   FCC_FILES_FILTER   		= "*" + FCC_FILE_EXT.toLowerCase();
    private final String   FCC_FILES_FILTER_TEXT 	= "FCC database files (" + FCC_FILES_FILTER + ")";
    
    private final String   CRTC_FILE_EXT       		= ".txt";
    private final String   CRTC_FILES_FILTER   		= "*" + CRTC_FILE_EXT.toLowerCase();
    private final String   CRTC_FILES_FILTER_TEXT 	= "CRTC database files (" + CRTC_FILES_FILTER + ")";
    
    private final String   OUTPUT_FILE_EXT       	= ".dtb";
    private final String   OUTPUT_FILES_FILTER   	= "*" + OUTPUT_FILE_EXT.toLowerCase();
    private final String   OUTPUT_FILES_FILTER_TEXT = "Win-Test database files (" + OUTPUT_FILES_FILTER + ")";
    
	private Shell shell = null;
	private FileAction fileAction;
	private String selectedFileName = "";
	private int selectedFileTypeIndex = INVALID_INDEX;

	public String getSelectedFileName() {
		return selectedFileName;
	}

	public int getSelectedFileTypeIndex() {
		return selectedFileTypeIndex;
	}
	
	public FileSelector(FileAction fileAction) {
		super();
		this.fileAction = fileAction;
	}

	public FileSelector(Shell shell, FileAction fileAction) {
		this(fileAction);
		this.shell = shell;
	}

	public boolean execute() {
		final boolean ASK_4_OVERWRITE = true;

		if (shell == null) {
			shell = new Shell(Display.getDefault());
		}
		
		FileDialog dialog = null;

		switch (fileAction) {
			case OPEN_FCC_DB:
				dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterNames(new String[] {
						FCC_FILES_FILTER_TEXT,
						ALL_FILES_FILTER_TEXT });
				dialog.setFilterExtensions(new String[] {
						FCC_FILES_FILTER,
						ALL_FILES_FILTER });
				dialog.setOverwrite(!ASK_4_OVERWRITE);
				break;

			case OPEN_CRTC_DB:
				dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterNames(new String[] {
						CRTC_FILES_FILTER_TEXT,
						ALL_FILES_FILTER_TEXT });
				dialog.setFilterExtensions(new String[] {
						CRTC_FILES_FILTER,
						ALL_FILES_FILTER });
				dialog.setOverwrite(!ASK_4_OVERWRITE);
				break;
				
			case SAVE_WT_DTB:
				dialog = new FileDialog(shell, SWT.SAVE);
				dialog.setFilterNames(new String[] {
						OUTPUT_FILES_FILTER_TEXT,
						ALL_FILES_FILTER_TEXT });
				dialog.setFilterExtensions(new String[] {
						OUTPUT_FILES_FILTER,
						ALL_FILES_FILTER });
				dialog.setOverwrite(ASK_4_OVERWRITE);
				break;
			
			default:
				return false;
		}
		
		selectedFileName = dialog.open();		
		selectedFileTypeIndex = dialog.getFilterIndex();
		if ((selectedFileName == null) || selectedFileName.isEmpty()) {
			return false;
		}

		return true;
	}
}
