package apkt.cypher;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RsaCypher {

//    private static final String PUBLIC_KEY_FILE = "brew";
     private static final String PRIVATE_KEY_FILE = "malt";

//    public static void main(String[] args) throws IOException {
//
//        try {
////            System.out.println("-------GENRATE PUBLIC and PRIVATE KEY-------------");  
//            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");  
//            keyPairGenerator.initialize(4096); //1024 used for normal securities    
//            KeyPair keyPair = keyPairGenerator.generateKeyPair();  
//            PublicKey publicKey = keyPair.getPublic();  
//            PrivateKey privateKey = keyPair.getPrivate();  
//            System.out.println("\n------- PULLING OUT PARAMETERS WHICH MAKES KEYPAIR----------\n");  
//            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
//            RSAPublicKeySpec rsaPubKeySpec = keyFactory.getKeySpec(publicKey, RSAPublicKeySpec.class);  
//            RSAPrivateKeySpec rsaPrivKeySpec = keyFactory.getKeySpec(privateKey, RSAPrivateKeySpec.class);  
////  
////            System.out.println("\n--------SAVING PUBLIC KEY AND PRIVATE KEY TO FILES-------\n");  
////    
//            RsaCypher rsaObj = new RsaCypher();
////
////            rsaObj.saveKeys(PUBLIC_KEY_FILE, rsaPubKeySpec.getModulus(), rsaPubKeySpec.getPublicExponent());  
////            rsaObj.saveKeys(PRIVATE_KEY_FILE, rsaPrivKeySpec.getModulus(), rsaPrivKeySpec.getPrivateExponent());  
//
//            //Encrypt Data using Public Key    
////            byte[] encryptedData = rsaObj.encryptData("Hello java programmers!!!");
//
//            //Descypt Data using Private Key    
////            rsaObj.decryptData(encryptedData);
//
//        } catch (InvalidKeySpecException ex) {
//            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException e) {  
//            System.out.println(e);  
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }

//     public static void main(String[] args) throws IOException {
//
//        try {
//            byte[] b = Rsa.encryptData("test");
//            ByteJson byteJson = new ByteJson(b);
//
//            String gson = new Gson().toJson(byteJson);
//
//            ByteJson byteJson_ = new Gson().fromJson(gson, ByteJson.class);
//            byte[] b_ = Rsa.decryptData(byteJson_.getB());
//            
//            String s = new String(b_);
//            
//            String sdf = "sdf";
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//    }
     
    private void saveKeys(String fileName, BigInteger mod, BigInteger exp) throws IOException {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            System.out.println("Generating " + fileName + "...");
            fos = new FileOutputStream("/usr/local/factory/" + fileName);
            System.out.println("path: " + new File(fileName).getAbsolutePath());
            oos = new ObjectOutputStream(new BufferedOutputStream(fos));
            oos.writeObject(mod);
            oos.writeObject(exp);
            System.out.println(fileName + " generated successfully");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                oos.close();
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }

//    public static byte[] encryptData(String data) throws IOException {
//        System.out.println("\n----------------ENCRYPTION STARTED------------");
//        System.out.println("Data Before Encryption :" + data);
//        
//        byte[] encryptedData = null;
//        try {
//            PublicKey pubKey = readPublicKeyFromFile(PUBLIC_KEY_FILE);
//            Cipher cipher = Cipher.getInstance("RSA");
//            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
//            encryptedData = cipher.doFinal(data.getBytes());
//
//            System.out.println("Encryted Data: " + new String(encryptedData));
//
//
//        } catch (IllegalBlockSizeException ex) {
//            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (BadPaddingException ex) {
//            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (InvalidKeyException ex) {
//            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchAlgorithmException ex) {
//            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
//        } catch (NoSuchPaddingException ex) {
//            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
//        } 
//        System.out.println("----------------ENCRYPTION COMPLETED------------");
//        return encryptedData;
//    }

    public static byte[] decryptData(byte[] data) throws IOException {
//        System.out.println("\n----------------DECRYPTION STARTED------------");
        byte[] descryptedData = null;
        try {
            PrivateKey privateKey = readPrivateKeyFromFile(PRIVATE_KEY_FILE);
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            descryptedData = cipher.doFinal(data);
//            System.out.println("Decrypted Data: " + new String(descryptedData));
        } catch (IllegalBlockSizeException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BadPaddingException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } 
//        System.out.println("----------------DECRYPTION COMPLETED------------");
        return descryptedData;
    }

    private static PublicKey readPublicKeyFromFile(String fileName) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File("/usr/local/factory/" + fileName));
            ois = new ObjectInputStream(fis);
            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();
            //Get Public Key    
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PublicKey publicKey = fact.generatePublic(rsaPublicKeySpec);
            return publicKey;
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

    private static PrivateKey readPrivateKeyFromFile(String fileName) throws IOException {
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = new FileInputStream(new File("/usr/local/factory/" + fileName));
            ois = new ObjectInputStream(fis);
            BigInteger modulus = (BigInteger) ois.readObject();
            BigInteger exponent = (BigInteger) ois.readObject();
            //Get Private Key    
            RSAPrivateKeySpec rsaPrivateKeySpec = new RSAPrivateKeySpec(modulus, exponent);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = fact.generatePrivate(rsaPrivateKeySpec);
            return privateKey;
        } catch (InvalidKeySpecException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RsaCypher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                ois.close();
                if (fis != null) {
                    fis.close();
                }
            }
        }
        return null;
    }

}
