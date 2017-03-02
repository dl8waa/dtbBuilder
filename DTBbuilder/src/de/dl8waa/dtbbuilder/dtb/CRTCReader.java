package de.dl8waa.dtbbuilder.dtb;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CRTCReader extends DatabaseReader {

	public CRTCReader(String fileName) {
		super(fileName);
	}

	@Override
	protected boolean read() {
		final int CALLSIGN_START_POS 				= 0;
		final int CALLSIGN_END_POS   				= 6;
		final int LOCATION_START_POS 				= 186;
		final int LOCATION_END_POS   				= 188;
		final int ALTERNATIVE_LOCATION_START_POS 	= 459;
		final int ALTERNATIVE_LOCATION_END_POS   	= 461;
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		}
		
		try {
			for (String line; (line = br.readLine()) != null; ) {
				if (line.length() < (LOCATION_END_POS+1)) {
					continue;
				}
				String callsign = line.substring(CALLSIGN_START_POS, CALLSIGN_END_POS).trim();
				
				String location = line.substring(LOCATION_START_POS, LOCATION_END_POS).trim();
				if ((location.isEmpty() || location.startsWith(" ")) && (line.length() >= (ALTERNATIVE_LOCATION_END_POS+1))) {
					location = line.substring(ALTERNATIVE_LOCATION_START_POS, ALTERNATIVE_LOCATION_END_POS).trim();
				}
				
				/* 
				 * Request by AXX Feb. 2017:
				 * Special treatment for NL and VO1/VO2:
				 * 	Treat VO1 in NL as NF
				 * 	Treat VO2 in NL as LB
				 */
				if (location.equals("NL")) {
					if (callsign.startsWith("VO1")) {
						location = "NF";
					} else {
						if (callsign.startsWith("VO2")) {
							location = "LB";
						}
					}					
				}
				dtb.addItem(callsign, location);
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
