package de.dl8waa.dtbbuilder.dtb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FCCReader extends DatabaseReader {

	public FCCReader(String fileName) {
		super(fileName);
	}

	@Override
	protected boolean read() {
		final int CALLSIGN_INDEX = 4;
		final int LOCATION_INDEX = 17;
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
				
		try {
			String line;
			while ((line = br.readLine()) != null) {
				String[] splitLine = line.split("\\|", (LOCATION_INDEX+2));
				if (splitLine.length < (LOCATION_INDEX+1)) {
					continue;
				}
				dtb.addItem(splitLine[CALLSIGN_INDEX], splitLine[LOCATION_INDEX]);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}

}
