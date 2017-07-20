package com.aia.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.*;

public class Validation {
	
	public boolean validateApptDate(String apptDate){
		boolean isValid = false;
		
		if (apptDate != null && !apptDate.equals("")){
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
			sdf.setLenient(false);
			
			try{
				Date date = sdf.parse(apptDate);
				isValid = true;
			} catch (Exception e){
				isValid = false;
			}
		}
		return isValid;
	}
	
	public boolean validateApptTime(String time){
		boolean isValid = false;
		
		if (time != null && !time.equals("")){
			if(time.equalsIgnoreCase("AM") || time.equalsIgnoreCase("PM")){
				isValid = true;
			}
		}
		return isValid;
	}
	
	public boolean validatePreferredComm(String comm){
		boolean isValid = false;
		
		if (comm != null && !comm.equals("")){
			if(comm.equalsIgnoreCase("phone") || comm.equalsIgnoreCase("email")){
				isValid = true;
			}
		}
		return isValid;
	}
	
	public boolean validatePhone(String phone){
		String PHONE_PATTERN = "^([\\d\\+\\-]{8,20})$";
		Pattern pattern = Pattern.compile(PHONE_PATTERN);
		Matcher matcher = pattern.matcher(phone.trim());
		return matcher.matches();	
	}
	
	public boolean validateEmail(String email){
		String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		Matcher matcher = pattern.matcher(email.trim());
		return matcher.matches();
	}
	
	public boolean validateOtherLocation(String otherLocation){
		boolean isValid = false;
		
		if (otherLocation != null && !otherLocation.equals("")) {
			if (otherLocation.equalsIgnoreCase("yes") || otherLocation.equalsIgnoreCase("no")){
				isValid = true;
			}
		}
		return isValid;
	}
	
	public boolean validateNRIC(String nric) {
		boolean isValid = false;
		
		if(nric != null && !nric.equals("")){
			if (nric.matches("[STst]\\d{7}[A-Za-z]")) {				
				int i;
				char[] icArray = nric.toUpperCase().toCharArray();
				int[] icArrayInt = new int[10];
				
				icArrayInt[1] = Character.getNumericValue(nric.charAt(1)) * 2;
				icArrayInt[2] = Character.getNumericValue(nric.charAt(2)) * 7;
				icArrayInt[3] = Character.getNumericValue(nric.charAt(3)) * 6;
				icArrayInt[4] = Character.getNumericValue(nric.charAt(4)) * 5;
				icArrayInt[5] = Character.getNumericValue(nric.charAt(5)) * 4;
				icArrayInt[6] = Character.getNumericValue(nric.charAt(6)) * 3;
				icArrayInt[7] = Character.getNumericValue(nric.charAt(7)) * 2;
			 
			    int weight = 0;
			    for(i = 1; i < 8; i++) {
			        weight += icArrayInt[i];
			    }
			 
			    int offset = (icArray[0] == 'G') ? 4:0;
			    int temp = (offset + weight) % 11;
			    
			    char[] s = {'J','Z','I','H','G','F','E','D','C','B','A'};
			    char[] t = {'G','F','E','D','C','B','A','J','Z','I','H'};
			    char[] fg = {'X','W','U','T','R','Q','P','N','M','L','K'};
			 
			    char theAlpha = '\0';
			    if (icArray[0] == 'S') { theAlpha = s[temp]; }
			    else if (icArray[0] == 'T') { theAlpha = t[temp]; }
			    else if (icArray[0] == 'F' || icArray[0] == 'G') { theAlpha = fg[temp]; }
			    
			    isValid = (icArray[8] == theAlpha);
			}
		}
		return isValid;
	}
	
	public boolean validateDOB(String dob) {
		boolean isValid = false;
		
		if(dob != null && !dob.equals("")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setLenient(false);
			
			try{
				Date date = sdf.parse(dob);
				isValid = true;
			} catch (Exception e){
				isValid = false;
			}
		}
		return isValid;
	}
}