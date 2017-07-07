package apkt.cypher;

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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class AesCypher {  
    private static final String ALGO = "AES";  
    
  
    private static byte[] encrypt(byte[] keyValue, String Data) throws Exception {  
        Key key = new SecretKeySpec(keyValue, ALGO);   
        Cipher c = Cipher.getInstance(ALGO);  
        c.init(Cipher.ENCRYPT_MODE, key);  
        byte[] encVal = c.doFinal(Data.getBytes());  
//        String encryptedValue = new BASE64Encoder().encode(encVal);  
        return encVal;  
    }  
  
    private static String decrypt(byte[] keyValue, String encryptedData) throws Exception {  
        Key key = new SecretKeySpec(keyValue, ALGO);   
        Cipher c = Cipher.getInstance(ALGO);  
        c.init(Cipher.DECRYPT_MODE, key);  
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);  
        byte[] decValue = c.doFinal(decordedValue);  
        String decryptedValue = new String(decValue);  
        return decryptedValue;  
    }  
  
    public static byte[] setKeyValue() throws NoSuchAlgorithmException{
        KeyGenerator kg = KeyGenerator.getInstance("AES");
        kg.init(256);
        SecretKey k = kg.generateKey();
        return k.getEncoded();
    }
  
//    public static byte[] packData(
//            byte[] keyValue,
//            AuthAux authAux, 
//            Object object
//            ) throws Exception {  
//        authAux.setObject(object);
//        byte[] encdata = encrypt(keyValue, new Gson().toJson(authAux));  
//        return encdata;
//        
//    }  
    
//    public static String unpackData(String key, String data) {  
//        try {  
//            keyValue = key.getBytes(); 
//            String decdata = decrypt(data);  
//            return decdata;
//        } catch (Exception ex) {  
//            Logger.getLogger(apkt.test.AESExample.class.getName()).log(Level.SEVERE, null, ex);  
//        } 
//        return null;
//    }  
    
}  