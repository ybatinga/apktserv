package apkt.test;

import apkt.backingbean.AuthAux;
import com.google.gson.Gson;
import java.security.Key;  
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;  
import java.util.logging.Logger;  
import javax.crypto.Cipher;  
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;  
import org.apache.commons.lang.RandomStringUtils;
import sun.misc.BASE64Decoder;  
import sun.misc.BASE64Encoder;  
  
public class AESExample {  
  
    private static final String ALGO = "AES";  
    private static byte[] keyValue;  
  
    public AESExample(String key) {  
        keyValue = key.getBytes();  
    }  
  
    private static String encrypt(String Data) throws Exception {  
        Key key = generateKey();  
        Cipher c = Cipher.getInstance(ALGO);  
        c.init(Cipher.ENCRYPT_MODE, key);  
        byte[] encVal = c.doFinal(Data.getBytes());  
        String encryptedValue = new BASE64Encoder().encode(encVal);  
        return encryptedValue;  
    }  
  
    private static String decrypt(String encryptedData) throws Exception {  
        Key key = generateKey();  
        Cipher c = Cipher.getInstance(ALGO);  
        c.init(Cipher.DECRYPT_MODE, key);  
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);  
        byte[] decValue = c.doFinal(decordedValue);  
        String decryptedValue = new String(decValue);  
        return decryptedValue;  
    }  
  
    private static Key generateKey() throws Exception {  
        Key key = new SecretKeySpec(keyValue, ALGO);  
        return key;  
    }  
    
    private static byte[] setKeyValue() throws NoSuchAlgorithmException{
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(128);
        SecretKey k = kg.generateKey();
        return k.getEncoded();
    }
  
//    public static String pack(
//            AuthAux authAux, 
//            Object object
//            ) {  
//        try {  
//            authAux.setObject(object);
//            keyValue = setKeyValue(); 
//            String encdata = encrypt(new Gson().toJson(authAux));  
//            return encdata;
//        } catch (Exception ex) {  
//            Logger.getLogger(AESExample.class.getName()).log(Level.SEVERE, null, ex);  
//        } 
//        return null;
//    }  
    
    public static String unpack(String key, String data) {  
        try {  
            keyValue = key.getBytes(); 
            String decdata = decrypt(data);  
            return decdata;
        } catch (Exception ex) {  
            Logger.getLogger(AESExample.class.getName()).log(Level.SEVERE, null, ex);  
        } 
        return null;
    }  
    
    public static void main(String args[]) {  
//        try {  
//            AESExample aes = new AESExample("lv39eptlvuhaqqsr");  
//            String encdata = aes.encrypt("Java is cool");  
//            System.out.println("Encrypted Data - " + encdata);  
//            String decdata = aes.decrypt(encdata);  
//            System.out.println("Decrypted Data - " + decdata);  
//        } catch (Exception ex) {  
//            Logger.getLogger(AESExample.class.getName()).log(Level.SEVERE, null, ex);  
//        }  
        
        String generatedString = RandomStringUtils.randomAscii(30);
 
    System.out.println(generatedString);
    }  
}  