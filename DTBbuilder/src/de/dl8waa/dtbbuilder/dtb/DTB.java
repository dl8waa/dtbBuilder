package de.dl8waa.dtbbuilder.dtb;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.swt.SWT;

import de.dl8waa.dtbbuilder.utils.Utils;

public class DTB {
	
	private class DTB_Item {
		private String callsign;		
		private String location;
		
		public DTB_Item(String callsign, String location) {
			super();
			this.callsign = callsign;
			this.location = location;
		}
	}
	
	private ArrayList<DTB_Item> items = new ArrayList<>();
	private final int CALLSIGN_LENGTH = 14;
	private final int LOCATION_LENGTH = 12;
	
	
	public void addItem(String callsign, String location) {
		items.add(new DTB_Item(callsign, location));
	}
	
	
	public void addList(DTB dtb) {
		items.addAll(dtb.items);
	}
	
	
	public boolean write(String fileName) {
		boolean retry = true;
		while (retry) {
			try {
				FileWriter writer = new FileWriter(new File(fileName));
				for (DTB_Item item : items) {
					writer.write(item.callsign + new String(new char[CALLSIGN_LENGTH - item.callsign.length()]));
					writer.write(item.location + new String(new char[LOCATION_LENGTH - item.location.length()]));
				}		 
				writer.flush();
				writer.close();
				retry = false;
			} catch (IOException e) {
				String s = "There is an issue writing the Feature Restrictions List " + fileName;
				if (Utils.showMessage(null, s + ".\nEnsure that the file to be written is closed.", SWT.CANCEL | SWT.RETRY | SWT.ICON_ERROR) == SWT.CANCEL) {
					return false;
				}
			}
		}
		
		return true;
	}
}
