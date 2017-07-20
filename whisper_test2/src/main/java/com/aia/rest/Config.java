package com.aia.rest;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

public class Config {
	
	
	/* ------------------------- DATABASE CONFIG ----------------------------------- */
	/* DEV / SIT / UAT / PRD */
	public static final String SYBASE_DATASOURCE_CONTEXT = "java:/jdbc/healthcare_db_il";
	public static final String ORACLE_DATASOURCE_CONTEXT = "java:/ojdbc7/oracleHCP";
	
	/* ------------------------- MAIL CONFIG ----------------------------------- */
	/* DEV */
	/*
	public static final String APPT_EMAIL_SENDER = "justina.tandar@decision-science.com";
	public static final String APPT_EMAIL_RECIPIENT = "justina.tandar@decision-science.com";
	public static final String APPT_EMAIL_SUBJECT = "[DEV] Specialist Appointment Booking";
	public static final String SMTP_HOST = "smtp.gmail.com";
	public static final String SMTP_PORT = "587";
	public static final String SMTP_USERNAME = "pepaya85@gmail.com";
	*/
	
	/* SIT / UAT / PRD */
	public static final String APPT_EMAIL_SENDER = "no-reply@aia.com.sg";
	public static final String APPT_EMAIL_RECIPIENT = "sg-medappt@aia.com";
	public static final String APPT_EMAIL_SUBJECT = "Specialist Appointment Booking";
	public static final String SMTP_HOST = "smtp-int.aia.biz";
	public static final String SMTP_PORT = "25";
	
	/* ------------------------- SPECIALIST AVATAR CONFIG ----------------------------------- */
	/* DEV */
	//public static final String AVATAR_URL = "http://cq6aia.decision-science.com:4635/content/dam/aia/img/healthshield-support/specialists/"; 
	
	/* SIT */
	//public static final String AVATAR_URL = "http://cq6aia.decision-science.com:4635/content/dam/aia/img/healthshield-support/specialists/";
	
	/* UAT */
	//public static final String AVATAR_URL = "http://cq6aia.decision-science.com:4635/content/dam/aia/img/healthshield-support/specialists/";
	
	/* PRD */
	public static final String AVATAR_URL = "https://www.aia.com.sg/content/dam/sg/en/photos/healthshield-support/specialists/";
	
	/* ------------------------- Sybase Prefix ----------------------------------- */
	/* DEV */
	/*
	public static final String SYBASE_PREFIX = ""; 
	public static final String SYBASE_SUBSTR = "substr";
	public static final String FORENUM_SUFFIX = "";
	public static final String DISPLAY_NEWLINE = "<br/><br/>";
	*/
	
	/* SIT, UAT, PRD */
	public static final String SYBASE_PREFIX = "db_il..";
	public static final String SYBASE_SUBSTR = "substring";
	public static final String FORENUM_SUFFIX = "01";
	public static final String DISPLAY_NEWLINE = "<br/>";
	
	/* All environments */
	public static final int DOC_TERMINATION_GRACE_PERIOD_MONTHS = 2;
	
	/** Whisper API Key **/	
	public static final String WHISPER_API_KEY = "6xrtrv48eptk9r8p2tktrf55";
	public static final String WHISPER_USERNAME = "";
	public static final String WHISPER_PASSWORD = "";
	public static final String WHISPER_DEBUG_HOST = "";
	public static final String WHISPER_WORKSPACE_ID = "6D7963E7130C36C4";
	public static final String WHISPER_AUTHORIZATION_HEADER = "Basic U1BIQ1BTMDE6SzRjZj9MblY=";
	public static final String WHISPER_TEMPLATE_ID = "8D37462914DAF5DA";
	
	public static final String SMS_OTP_SUBJECT ="AIA Healthcare";
	public static final String SMS_OTP_BODY = "Your OTP is ?, valid for 3 minutes. Issued at %time% on %date%.";
	public static final int OTP_TIMEOUT = -3;
	
	/* DEV */
	/*
	public static final String PROXY_HOST = "";
	public static final int PROXY_PORT = 0;
	public static final boolean PROXY_HTTPS_ENABLED = false;
	public static final String PROXY_USERNAME = "";
	public static final String PROXY_PASSWORD = "";
	*/
	
	/* SIT / UAT / PRD */
//	public static final String PROXY_HOST = "Sgproxy.aia.biz";
	public static final String PROXY_HOST = "";
	public static final int PROXY_PORT = 80;
	public static final boolean PROXY_HTTPS_ENABLED = false;
	
	protected static String getInterfaceCode(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String code = sdf.format(date);
		String result = "";
		for(int i = 0; i < code.length(); i++){
			result += (Integer.parseInt(code.substring(i, i+1)) + 3) % 10;
		}
		return (result.substring(2, result.length()) + result.substring(0,2));
	}
	
	protected static Timestamp getCurrentTimeStamp() {
		Date today = new Date();
		return new java.sql.Timestamp(today.getTime());
	}
	
	public static Timestamp getXMinsAgo(int mins){
		Date threeMinsAgo = new Date();
		Calendar cl = Calendar.getInstance();
		cl.setTime(threeMinsAgo);
		cl.add(Calendar.MINUTE, mins);
		return new java.sql.Timestamp(cl.getTimeInMillis());
	}
}