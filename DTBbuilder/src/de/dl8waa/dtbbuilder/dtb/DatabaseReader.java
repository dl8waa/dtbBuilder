package de.dl8waa.dtbbuilder.dtb;

import org.eclipse.swt.SWT;

import de.dl8waa.dtbbuilder.utils.Utils;

public abstract class DatabaseReader {
	protected DTB dtb = new DTB();
	protected String fileName;
	
	private   boolean readSuccess = false;
	public	  boolean getReadSuccess() { return readSuccess; } 
	
	protected abstract boolean read();
	
	
	public DatabaseReader(String fileName) {
		super();
		this.fileName = fileName;
		if (!Utils.fileExists(fileName)) {
			Utils.showMessage(null, "File not found: " + fileName, SWT.ICON_ERROR);
			readSuccess = false;
		} else {
			readSuccess = read();
		}
	}

	public DTB getDTB() {
		return dtb;
	}
}
