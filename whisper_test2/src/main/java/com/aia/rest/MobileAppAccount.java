package com.aia.rest;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.WhispirSDK;
import com.whispir.sdk.WhispirSDKConstants;
import com.whispir.sdk.impl.MessageHelperImpl;
import com.whispir.sdk.interfaces.MessageHelper;

import java.util.Properties;
import java.io.InputStream;
import java.io.FileInputStream;

import sg.aia.webservice.utility.aesencryption.AESDecrypt;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;


@Path("/portalotp")
public class MobileAppAccount {
	
	private static final Logger log = LoggerFactory.getLogger(MobileAppAccount.class);
	

	@POST
	@Path("/add")
	public Response addUser(@FormParam("nric") String name,
			@FormParam("mobile") int age) {

		return Response.status(200)
				.entity("addUser is called, nric : " + name + ", mobile : " + age)
				.build();

	}
	
	@POST
	@Path("/generateotp")
	public Response generateSMSOTP(@FormParam("nric") String nric, @FormParam("mobile") String mobile) {

		String result = "";
		JSONObject json = new JSONObject();		
		
		String querySQL = "";
		DataSource datasource;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		long last_inserted_id = 0;
		
		try {
			
			
			//Generate the random OTP
			String OTP = "";
			String secureOTP = "";
			Random randomNum = new Random();
			
			for (int i = 0; i <= 5; i ++){
				OTP += String.valueOf(randomNum.nextInt(9));
			}
			
			
			/* Hard code OTP for pen test purpose */
			OTP = "123456";
			/* <END> */
			
			log.info("Random OTP: " + OTP);
			long mobileNo = 90628459;			
			/*
			Context initContext = new InitialContext();
			datasource = (DataSource) initContext.lookup(Config.ORACLE_DATASOURCE_CONTEXT);
			
			if (type.equals("login"))
			{
				conn = datasource.getConnection();
				
				querySQL = "SELECT mobile FROM healthcare_mobile_account WHERE nric = ?";
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, nric);
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					mobileNo = rs.getLong("mobile");
				}
				try { if(rs != null) rs.close(); } catch (Exception e) { log.error("generateOTP{} -- Error encountered: " + e.getMessage()); }
				try { if(pstmt != null) pstmt.close(); } catch (Exception e) { log.error("generateOTP{} -- Error encountered: " + e.getMessage()); }
				try { if(conn != null) conn.close(); } catch (Exception e) { log.error("generateOTP{} -- Error encountered: " + e.getMessage()); }
				
			}
			else // register
			{
				try
				{
					mobileNo = Long.parseLong(mobile);
					//Check whether the mobile number has been registered
					String registeredNric = "";
					conn = datasource.getConnection();
					
					querySQL = "SELECT nric FROM healthcare_mobile_account WHERE mobile = ?";
					pstmt = conn.prepareStatement(querySQL);
					pstmt.setLong(1, mobileNo);
					rs = pstmt.executeQuery();
					
					while (rs.next())
					{
						registeredNric = rs.getString("nric");
					}
					
					if (registeredNric != ""){
						log.error("generateOTP{} -- Error: Mobile number " + mobileNo + " has been registered");
						//Mobile number has been registered, return error
						json.put("status", "-2");
						json.put("msg", "Mobile number has been registered. Please enter a different mobile number");
						result = json.toString();
						return result;
					} 
				}
				catch (Exception ex) {
					mobileNo = 0;
					
				} finally {
					try {
						if (rs != null) {
							rs.close();
						}
					} catch (SQLException e) {
						log.error("generateOTP{} -- Error encountered: " + e.getMessage());
					}
					try {
						if (pstmt != null) {
							pstmt.close();
						}
					} catch (SQLException e) {
						log.error("generateOTP{} -- Error encountered: " + e.getMessage());
					}
					try {
						if (conn != null) {
							conn.close();
						}
					} catch (SQLException e) {
						log.error("generateOTP{} -- Error encountered: " + e.getMessage());
					}
				}
			}
			
				*/		
			if (mobileNo != 0)
			{
					
				try
				{
					WhispirSDK whispirSDK = new WhispirSDK();
					
				    if (!"".equals(Config.WHISPER_DEBUG_HOST)) 
				    {
				    	whispirSDK = new WhispirSDK(Config.WHISPER_API_KEY, Config.WHISPER_USERNAME,
				        		Config.WHISPER_PASSWORD, Config.WHISPER_DEBUG_HOST, Config.WHISPER_AUTHORIZATION_HEADER);
				    } 
				    else 
				    {
				    	whispirSDK = new WhispirSDK(Config.WHISPER_API_KEY, Config.WHISPER_USERNAME,
				        		Config.WHISPER_PASSWORD, "", Config.WHISPER_AUTHORIZATION_HEADER);
				    }
				    
				    
				    /* Get the AES key and decrypt the password stored in properties file */
			    	Properties prop = new Properties();
			    	InputStream input = null;
			    	String PROXY_HOST = "";
			    	String username = "";
			    	String AES_KEY = "";
			    	String encryptedPassword = "";
			    	String decryptedPassword = "";
			    	
			    	try {
			    	//	input = new FileInputStream("/apps/sgp/jboss-eap-6.3-hcp/standalone/configuration/aia-quality-healthcare.properties");
			    	//	prop.load(input);
			    		
			    	//	AES_KEY = prop.getProperty("AES_KEY");
			    		AES_KEY = "BxT3Nnl93Ld+eR3n0k7Knu==9PG+yQcg";
			    		
			    	
			    		
			    	//	PROXY_HOST = prop.getProperty("PROXY_HOST");
			    		PROXY_HOST = "";
			    		
			    		
			    	//	username = prop.getProperty("USERNAME");
			    	//  encryptedPassword = prop.getProperty("PASSWORD");
			    		
			    		username = "SCMOT32";
			    		encryptedPassword = "4E15C913C580A23300D75B9326CBAFFE";
				    	
			    		decryptedPassword = AESDecrypt.decryptPassword(encryptedPassword, AES_KEY);
			    		log.info("decryptedPassword : " + decryptedPassword);
				    	
			    	} catch (Exception e){
			    		//log.error("Unable to read properties file: " + e.getMessage());
			    		
			    	} finally {
			    		try {
			    			if (input != null) input.close();
			    		} catch (Exception e) {}
			    	}
				    
				    if (!"".equals(PROXY_HOST))
				    {
				    	//if (!"".equals(Config.PROXY_USERNAME) && !"".equals(Config.PROXY_PASSWORD))
				    	if (!"".equals(username) && !"".equals(decryptedPassword))
				    	{
				        	whispirSDK.setProxy(PROXY_HOST, Config.PROXY_PORT, Config.PROXY_HTTPS_ENABLED, username, decryptedPassword);
				        } 
				        else 
				        {
				        	whispirSDK.setProxy(PROXY_HOST, Config.PROXY_PORT, Config.PROXY_HTTPS_ENABLED);
				        }
				    }
				    
				    Map<String, String> options = new HashMap<String, String>();
				    options.put(WhispirSDKConstants.PUSH_NOTIFICATIONS, WhispirSDKConstants.PUSH_NOTIFICATIONS_ENABLED);
				    
				    Map<String, String> contents = new HashMap<String, String>();
				    				    
				    DateFormat df = new SimpleDateFormat("dd MMM yyyy");
				    DateFormat tf = new SimpleDateFormat("hh:mm:ss aa");
				    Date dateobj = new Date();
				    
				    String currentDate = df.format(dateobj);
				    String currentTime = tf.format(dateobj);
				    
				    String smsContent = Config.SMS_OTP_BODY.replace("?", OTP);
				    smsContent = smsContent.replace("%date%", currentDate);
				    smsContent = smsContent.replace("%time%", currentTime);
				    
				    contents.put("body", smsContent);
				    contents.put("templateId", Config.WHISPER_TEMPLATE_ID);
				    
				    MessageHelper messageHelper = new MessageHelperImpl(whispirSDK);
				    String to = "+" + String.valueOf(mobileNo);				    
				    log.info("SMS Recipient: " + to);
				    log.info("SMS Subject: " + Config.SMS_OTP_SUBJECT);
				    
				    WhispirResponse whispirResponse = messageHelper.sendMessage(Config.WHISPER_WORKSPACE_ID, to, Config.SMS_OTP_SUBJECT, contents);
				    
				    log.info("Whispir Status Code: " + whispirResponse.getStatusCode());
				    log.info("Whispir Raw Response: " + whispirResponse.getRawResponse());
				}
				catch (Exception e)
				{
					log.error("generateOTP{} -- Whisper Error encountered: " + e.getMessage());
					e.printStackTrace();
					result = "{\"status\": \"-1\"}";
					//return result;
				}
			   /*
				//Update the database
				querySQL = "BEGIN "
						 + "INSERT INTO healthcare_account_otp(id, nric, mobile, otp_sent, date_otp_sent) "
						 + "VALUES (sq_healthcare_otp.NEXTVAL, ?, ?, ?, ?) "
						 + "RETURNING id INTO ?; "
						 + "END; ";
				
				conn = datasource.getConnection();
				cstmt = conn.prepareCall(querySQL);
				cstmt.setString(1, nric);
				cstmt.setLong(2, mobileNo);
				cstmt.setString(3, OTP);
				cstmt.setTimestamp(4, Config.getCurrentTimeStamp());
				cstmt.registerOutParameter(5, Types.NUMERIC);
				cstmt.execute();
				last_inserted_id = cstmt.getLong(5);
				JSONObject account = new JSONObject();
				account.put("otpID", String.valueOf(last_inserted_id));
				//account.put("otp", OTP);
				
				json.put("status", "1");
				json.put("data", account);
				
				try { if(rs != null) rs.close(); } catch (Exception e) { log.error("generateOTP{} -- Error encountered: " + e.getMessage()); }
				try { if(cstmt != null) cstmt.close(); } catch (Exception e) { log.error("generateOTP{} -- Error encountered: " + e.getMessage()); }
				try { if(conn != null) conn.close(); } catch (Exception e) { log.error("generateOTP{} -- Error encountered: " + e.getMessage()); }
				
				*/
			
			} else {
				json.put("status", "-1");
				json.put("msg", "Account not found");
			}
			
			result = json.toString();
			
		} catch(Exception e) {
			log.error("generateOTP{} -- Error encountered: " + e.getMessage());
			e.printStackTrace();
			result = "{\"status\": \"-1\"}";
			
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				log.error("generateOTP{} -- Error encountered: " + e.getMessage());
			}
			try {
				if (cstmt != null) {
					cstmt.close();
				}
			} catch (SQLException e) {
				log.error("generateOTP{} -- Error encountered: " + e.getMessage());
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				log.error("generateOTP{} -- Error encountered: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				log.error("generateOTP{} -- Error encountered: " + e.getMessage());
			}
		}

		return Response.status(200)
				.entity("addUser is called, nric : " + nric + ", mobile : " + mobile)
				.build();
	}
	
	@POST
	@Path("/verifyotp")
	@Produces("application/json")
	public String verifySMSOTP(@FormParam("otpID") String id, @FormParam("otp") String otp, @FormParam("type") String type, 
								@FormParam("key") String key, @FormParam("id") String sessionID, @FormParam("nric") String nric){
		String result = "";
		JSONObject json = new JSONObject();		
		
		String querySQL = "";
		DataSource datasource;
		PreparedStatement pstmt = null;
		CallableStatement cstmt = null;
		Connection conn = null;
		ResultSet rs = null;
		
		long mobile = 0;
		String nricDB = "";
		String dob = "";
		String otp_sent = "";
		String uniqueID = "";
		
		try {
			//Check for the session ID
			/*if(!Customer.checkSessionID(nric, sessionID, "B")){
				json.put("status", "0");
				result = json.toString();
				return result;
			}*/
			
			//Check for the key first
			/*
			if (!key.equals(Config.getInterfaceCode()))
			{
				json.put("status", "0");
				result = json.toString();
				return result;
			}
			*/
			
			//Retrieve the OTP sent and timestamp
			Context initContext = new InitialContext();
			datasource = (DataSource) initContext.lookup(Config.ORACLE_DATASOURCE_CONTEXT);
			
			conn = datasource.getConnection();
			querySQL = "SELECT nric, mobile, otp_sent FROM healthcare_account_otp WHERE id = ? AND date_otp_sent >= ? AND date_otp_sent <= ?";
			pstmt = conn.prepareStatement(querySQL);
			pstmt.setLong(1, Long.parseLong(id));
			pstmt.setTimestamp(2, Config.getXMinsAgo(Config.OTP_TIMEOUT));
			pstmt.setTimestamp(3, Config.getCurrentTimeStamp());
			
			rs = pstmt.executeQuery();
			while(rs.next()){
				nricDB = rs.getString("nric");
				otp_sent = rs.getString("otp_sent");
				mobile = rs.getLong("mobile");
			}
			try { if (rs != null) rs.close(); } catch (Exception e) {};
		    try { if (pstmt != null) pstmt.close(); } catch (Exception e) {};
		    try { if (conn != null) conn.close(); } catch (Exception e) {};

			//Validate the OTP
			if(otp_sent == null || otp_sent.equals(""))
			{
				//No results, otp expired
				json.put("status", "-1");
				json.put("msg", "OTP has expired. Please request for a new OTP");
				
			} 
			else if (otp_sent.equals(otp))
			{
				//Update the database with the submitted OTP
				conn = datasource.getConnection();
				querySQL = "UPDATE healthcare_account_otp SET otp_received = ?, date_otp_received = ? WHERE id = ?";
				conn.setAutoCommit(false);
				
				pstmt = conn.prepareStatement(querySQL);
				pstmt.setString(1, otp);
				pstmt.setTimestamp(2, Config.getCurrentTimeStamp());
				pstmt.setLong(3, Long.parseLong(id));
				
				pstmt.executeUpdate();
				conn.commit();
				
			    try { if (pstmt != null) pstmt.close(); } catch (Exception e) {};
			    try { if (conn != null) conn.close(); } catch (Exception e) {};
				
				if (type.equals("register"))
				{
					querySQL = "BEGIN "
							 + "INSERT INTO healthcare_mobile_account(id, nric, dob, mobile, country_code, date_created) "
							 + "VALUES (sq_healthcare_account.NEXTVAL, ?, ?, ?, 65, ?) "
							 + "RETURNING id INTO ?; "
							 + "END; ";
					
					conn = datasource.getConnection();
					cstmt = conn.prepareCall(querySQL);
					cstmt.setString(1, nricDB);
					cstmt.setDouble(2, 0);
					cstmt.setLong(3, mobile);
					cstmt.setTimestamp(4, Config.getCurrentTimeStamp());
					cstmt.registerOutParameter(5, Types.NUMERIC);
					cstmt.execute();
					long last_inserted_id = cstmt.getLong(5);
					
					log.info("MobileAppAccount{} -- Update otp and date received statement executed and committed.");
					json.put("status", "1");
					
					//Invalidate the session ID used for registration
				//	boolean isInvalidated = Customer.invalidateSessionID(nric, sessionID, "B");
					
				} else {
					//boolean isInvalidated = Customer.invalidateSessionID(nric, sessionID, "B");
				//	uniqueID = Customer.generateSessionID(nric, "A");
					JSONObject sessionData = new JSONObject();
					sessionData.put("id", "asdf1234");
					
					json.put("status", "1");
					json.put("data", sessionData);
				}
				//json.put("status", "1");
			} 
			else 
			{
				json.put("status", "-1");
				json.put("msg", "Incorrect OTP");
			}
				
			result = json.toString();
			log.info("verifyOTP{} -- OTP verified");
			
		} catch(Exception e) {
			log.error("verifyOTP{} -- Error encountered: " + e.getMessage());
			e.printStackTrace();
			result = "{\"status\": \"-1\"}";
			
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
			} catch (SQLException e) {
				log.error("verifyOTP{} -- Error encountered: " + e.getMessage());
			}
			try {
				if (pstmt != null) {
					pstmt.close();
				}
			} catch (SQLException e) {
				log.error("verifyOTP{} -- Error encountered: " + e.getMessage());
			}
			try {
				if (cstmt != null) {
					cstmt.close();
				}
			} catch (SQLException e) {
				log.error("verifyOTP{} -- Error encountered: " + e.getMessage());
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				log.error("verifyOTP{} -- Error encountered: " + e.getMessage());
			}
		}
		
		return result;
	}
}