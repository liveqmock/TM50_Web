package web.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class TmAesUtil {
	
	/**
	 * AES 방식으로 암호화(키는 16byte 필수, 예 : 00000000001234)
	 * @param plaintext
	 * @param sKey - 16byte 필수, 예 : 00000000001234
	 * @return
	 */
	public static String encrypto(String plaintext, String sKey)
	{
		String sResult = "";
	    String message = plaintext;

	    if(sKey.length()!=16)
	    	return plaintext;
	    
	    byte[] raw = sKey.getBytes();

	    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	    try
	    {
	    	Cipher cipher = Cipher.getInstance("AES");
	    	cipher.init(1, skeySpec);
	    	byte[] encrypted = cipher.doFinal(message.getBytes());
	     
	    	sResult =  Base64.encodeBytes(encrypted);
	    }
	    catch (Exception e)
	    {
	    	System.out.println("encrypto : " + e );
	    }
	    return sResult;
	  }
	  
	/**
	 * AES 방식으로 복호화(키는 16byte 필수, 예 : 00000000001234)
	 * @param ciphertext
	 * @param sKey - 16byte 필수, 예 : 00000000001234
	 * @return
	 */
	  public static String decrypto(String ciphertext, String sKey)
	  {
		  String sResult = "";
		  
		  if(sKey.length()!=16)
			  return ciphertext;
	   
		  byte[] raw = sKey.getBytes();

		  SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		  try
		  {
			  byte[] bResult = Base64.decode(ciphertext);

			  Cipher cipher = Cipher.getInstance("AES");
			  cipher.init(2, skeySpec);
			  byte[] original = cipher.doFinal(bResult);

			  sResult = new String(original);
		  }
		  catch (Exception e)
		  {
			  System.out.println("encrypto : " + e );
		  }
		  return sResult;
	  }
	  
	  public static void main(String[] args) throws Exception {
		  String str = "hello";
		  System.out.println("원문 : " + str);
		  System.out.println("암호 : " + encrypto(str, "1234567890123cba"));
		  System.out.println("복호 : " + decrypto(encrypto(str,"1234567890123cba"), "1234567890123cba"));
	  }

}
