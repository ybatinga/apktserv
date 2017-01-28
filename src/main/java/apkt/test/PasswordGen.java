package apkt.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class PasswordGen {

	public static void main(String[] args) {
		

            // encode password into bytes 
            byte[] encoded = Base64.encodeBase64("bVEA#998".getBytes());
            // decode password into string with sha512Hex hash
            String password = DigestUtils.sha512Hex(encoded);
            System.out.println(password);

	}

}
