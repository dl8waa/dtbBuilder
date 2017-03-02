package de.dl8waa.dtbbuilder;

public class Constants {
	public static final String  APP_NAME		= "DTB Builder";
	public static final String  APP_REVISION	= APP_NAME + " rev. 2.1";
	
	public static final int 	INVALID_INDEX	= -1;
	
	public static enum FileAction {
		OPEN_FCC_DB,
		OPEN_CRTC_DB,
		SAVE_WT_DTB
	}
	
	public static enum RunResult {
		SUCCESS,
		NO_FILES,
		READ_ERROR,
		WRITE_ERROR
	}
}
