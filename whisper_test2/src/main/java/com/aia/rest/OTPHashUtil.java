package com.aia.rest;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/*
 * PBKDF2 salted password hashing.
 * Author: havoc AT defuse.ca
 * www: http://crackstation.net/hashing-security.htm
 */
public class OTPHashUtil {
	private static Logger logger = LoggerFactory.getLogger(OTPHashUtil.class);
	public static final String SHA256_ALGORITHM = "SHA-256";

	// The following constants may be changed without breaking existing hashes.
	public static final int SALT_BYTE_SIZE = 32;

	/**
	 * Returns a salted PBKDF2 hash of the password.
	 * 
	 * @param password
	 *            the password to hash
	 * @return a salted PBKDF2 hash of the password
	 */
	public static SHA256Holder createHash(String password) throws NoSuchAlgorithmException, InvalidKeySpecException {
		SHA256Holder holder = new SHA256Holder();
		holder.setPassword(password);
		holder.setSalt(getRandomSalt());
		holder.setHashPassword(createHash(holder.getPassword(), holder.getSalt()));
		return holder;
	}

	private static String createHash(String password, String salt) throws NoSuchAlgorithmException,
			InvalidKeySpecException {
		MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM);
		md.update(salt.concat(password).getBytes());
		String hash = toHex(md.digest());

		return hash;
	}

	
	public static String hashString(String message, String algorithm)
			throws NoSuchAlgorithmException,
			InvalidKeySpecException {

			MessageDigest md = MessageDigest.getInstance(SHA256_ALGORITHM);
	        md.update(message.getBytes());
	        String hash = toHex(md.digest());
			return hash;
	    
	}
	
	
	
	public static String getRandomSalt() {
		// Generate a random salt
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[SALT_BYTE_SIZE];
		random.nextBytes(salt);
		return toHex(salt);
	}

	/**
	 * Validates a password using a hash.
	 * 
	 * @param password
	 *            the password to check
	 * @param correctHash
	 *            the hash of the valid password
	 * @return true if the password is correct, false if not
	 */
	public static boolean validatePassword(SHA256Holder holder) throws NoSuchAlgorithmException,
			InvalidKeySpecException {

		// Compute the hash of the provided password, using the same salt,
		// iteration count, and hash length
		String testHash = createHash(holder.getPassword(), holder.getSalt());
		// Compare the hashes in constant time. The password is correct if
		// both hashes match.
		return slowEquals(holder.getHashPassword().getBytes(), testHash.getBytes());
	}

	/**
	 * Compares two byte arrays in length-constant time. This comparison method is used so that password hashes cannot
	 * be extracted from an on-line system using a timing attack and then attacked off-line.
	 * 
	 * @param a
	 *            the first byte array
	 * @param b
	 *            the second byte array
	 * @return true if both byte arrays are the same, false if not
	 */
	private static boolean slowEquals(byte[] a, byte[] b) {
		int diff = a.length ^ b.length;
		for (int i = 0; i < a.length && i < b.length; i++)
			diff |= a[i] ^ b[i];
		return diff == 0;
	}

	/**
	 * Converts a byte array into a hexadecimal string.
	 * 
	 * @param array
	 *            the byte array to convert
	 * @return a length*2 character string encoding the byte array
	 */
	private static String toHex(byte[] array) {
		BigInteger bi = new BigInteger(1, array);
		String hex = bi.toString(16);
		int paddingLength = (array.length * 2) - hex.length();
		if (paddingLength > 0)
			return String.format("%0" + paddingLength + "d", 0) + hex;
		else
			return hex;
	}

	/**
	 * Tests the basic functionality of the PasswordHash class
	 * 
	 * @param args
	 *            ignored
	 */
	public static void main(String[] args) {
		try {
			// Print out 10 hashes
			for (int i = 0; i < 10; i++) {
				System.out.println("Test Hash #" + i + " >>>" + OTPHashUtil.createHash("passw0Rd!"));
			}

			// Test password validation
			boolean failure = false;
			System.out.println("Running tests.......");
			for (int i = 0; i < 100; i++) {
				SHA256Holder holder = createHash(String.valueOf(i));

				String hash = holder.getHashPassword();
				String secondHash = createHash(holder.getPassword(), getRandomSalt());
				if (hash.equals(secondHash)) {
					System.out.println("FAILURE: TWO HASHES ARE EQUAL!");
					failure = true;
				}
				String wrongPassword = "" + (i + 1);
				SHA256Holder fakeHolder = new SHA256Holder();
				fakeHolder.setPassword(wrongPassword);
				fakeHolder.setSalt(holder.getSalt());
				fakeHolder.setHashPassword(hash);
				if (validatePassword(fakeHolder)) {
					System.out.println("FAILURE: WRONG PASSWORD ACCEPTED!");
					failure = true;
				}
				if (!validatePassword(holder)) {
					System.out.println("FAILURE: GOOD PASSWORD NOT ACCEPTED!");
					failure = true;
				}
			}
			if (failure)
				System.out.println("TESTS FAILED!");
			else
				System.out.println("TESTS PASSED!");
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex);
		}
	}
}
