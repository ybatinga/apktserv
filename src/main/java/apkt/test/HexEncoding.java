package apkt.test;

import org.spongycastle.util.encoders.Hex;

public class HexEncoding {

	public static void main(String[] args) {
//		String s = "4920616d207468652077616c727573";
//		byte[] d = Hex.decode(s);	
//		String k = new String(d);
//		System.out.println(k);
		
		byte[] s = "I am the walrus".getBytes();
		byte[] d = Hex.encode(s);	
		String k = new String(d);
		System.out.println(k);

	}

}
