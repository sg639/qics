package net.imcore;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
http://www.imcore.net | hosihito@gmail.com
Developer. Kyoungbin Lee
2012.09.07

AES256 EnCrypt / DeCrypt
 */
public class Aes256 {

	public static void main(String[] args) throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
		String key = 	"h2y0u1n6d0a2i2b3n0g9s4t2e4e3l431";

		String plainText;
		String encodeText;
		String decodeText;
		// Encrypt
		plainText  = "fdvdsvsddfwerwerwerwerwerw";

		plainText  = "LINE=BA1&POCNO=C15339803-0000&WD=20160125&OTP=";
		plainText += "9f8f98e0-58ca-42e9-9821-ec1ab0e9bf28";

		//System.out.println("plainText : "+plainText);

		encodeText = AES256Cipher.AES_Encode(plainText, key);
		//System.out.println("AES256_Encode : "+encodeText);
		//System.out.println("AES256_Encode : "+URLEncoder.encode(encodeText,"UTF-8"));

		// Decrypt
		decodeText = AES256Cipher.AES_Decode(encodeText, key);
		//System.out.println("AES256_Decode : "+decodeText);

	}

}
