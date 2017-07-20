package com.aia.rest;

import java.io.Serializable;

public class SHA256Holder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6448855915297836410L;
	private String password;
	private String salt;
	private String hashPassword;

	public SHA256Holder() {

	}

	public SHA256Holder(String password, String salt, String hashPassword) {
		this.password = password;
		this.salt = salt;
		this.hashPassword = hashPassword;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt
	 *            the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}

	/**
	 * @return the hashPassword
	 */
	public String getHashPassword() {
		return hashPassword;
	}

	/**
	 * @param hashPassword
	 *            the hashPassword to set
	 */
	public void setHashPassword(String hashPassword) {
		this.hashPassword = hashPassword;
	}

	public String toString() {
		return String.format("Password: %s || Salt: %s || Hash: %s", this.password, this.salt, this.hashPassword);
	}

}
