package com.sms.provider.scheduler.utill;

public class DataMaskingUtil {
	
	
	/**
     * Masks an email address.
     * Example: "test.email@example.com" becomes "t***l@example.com"
     * @param email The email address to mask.
     * @return The masked email address.
     */
	
	public static String maskEmail(String email) {
		 if (email == null || !email.contains("@")) {
	            return email;
	        }
		 
		 int atIndex = email.indexOf('@');
	        String localPart = email.substring(0, atIndex);
	        String domainPart = email.substring(atIndex);
	        
	        if (localPart.length() <= 2) {
	            return localPart.charAt(0) + "***" + domainPart;
	        }
	        
	     // Show the first and last character of the local part
	        return localPart.charAt(0) +
	               "***" +
	               localPart.charAt(localPart.length() - 1) +
	               domainPart;
	}
	
	 /**
     * Masks a mobile number.
     * Example: "9876543210" becomes "987654XXXX"
     * @param mobileNumber The mobile number to mask.
     * @return The masked mobile number.
     */
	
	  public static String maskMobileNumber(String mobileNumber) {
		  if (mobileNumber == null || mobileNumber.length() <= 4) {
	            return "XXXX";
	        }
		  int unmaskedLength = mobileNumber.length() - 4;
	        return mobileNumber.substring(0, unmaskedLength) + "XXXX";
	  }

}
