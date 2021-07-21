package com.tinderclone.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;

public class StringUtils {
	//Returns an encrypted session key string based on the current timestamp string
	public static String generateKey() {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			long now = System.currentTimeMillis();
			Timestamp curr = new Timestamp(now);
			byte[] res = md.digest(curr.toString().getBytes(StandardCharsets.UTF_8));
			
			String session_key = Base64.getEncoder().encodeToString(res);
			
			return session_key;
		} catch(NoSuchAlgorithmException nsae) {
			
		}
		return null;
	}
	

	//Generate and return encrypted string from password input
	public static String generateHash(String pass) {
		try{
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.reset();

			byte[] hash_hex = pass.getBytes(StandardCharsets.UTF_8);
			md.update(hash_hex);

			hash_hex = md.digest();
			String hash_hex_string = Base64.getEncoder().encodeToString(hash_hex);

			return hash_hex_string;
		} catch(NoSuchAlgorithmException nsae) {

		}
		return null;
	}

}
