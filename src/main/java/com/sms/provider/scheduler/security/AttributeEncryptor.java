package com.sms.provider.scheduler.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.AttributeConverter;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

@Component
public class AttributeEncryptor implements AttributeConverter<String, String> {
	
	 private static final String AES = "AES";
	 private final Key key;
	 
	 public AttributeEncryptor(@Value("${pii.encryption.key}") String secret) {
	        key = new SecretKeySpec(secret.getBytes(), AES);
	    }
	 

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if(attribute == null) return null;
		
		try {
            Cipher cipher = Cipher.getInstance(AES);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt attribute", e);
        }
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		  if (dbData == null) return null;
		  try {
	            Cipher cipher = Cipher.getInstance(AES);
	            cipher.init(Cipher.DECRYPT_MODE, key);
	            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
	        } catch (Exception e) {
	            throw new IllegalStateException("Failed to decrypt attribute", e);
	        }
	}

}
