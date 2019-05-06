package apkt.service;

import java.util.Locale;
import java.util.ResourceBundle;

public class ProjService {
    
    public static final String SRNM = "postgres";
    public static final String PSWD = "DT%nU%wGb{ELn2M";
    public static final String SK8 = "jdbc:postgresql://localhost:5432/apekato";
    
    public static final String URL = UrlType.HTTP_PROD;
    public static final String ADDRESS = AddressType.TESTNET;
    public static final String RBPATH = ResourceBundlePath.AWS;
    public static final String BLOCKCYPHERTOKEN = "15cec3f0a8754248af469151f249b5d3";
    public static ResourceBundle RB;

    public class AddressType{
        public static final String BLOCKCYPHERTEST = "C9uvjNrozYgX41C7yKdczFVz5qepHW1Yes";
        public static final String TESTNET = "n4ezJuxz1UYRurYdqD68DSbNijq51Uv73y"; // Private Key (Wallet Import Format): 92NDgKAN43Md1HsTVN3ZX8K2h2mK4hbyWbcGzTZpeZ6Dckc7Q5x
        public static final String MAIN = "1D9Yo3PyXbNJ6osgQnbEQf4wn8kZaPtSPc";
    }

    public static class UrlType{
        public static final String HTTP_PROD = "https://apekato.com/webresources/";
        public static final String HTTP_HOMOLOG = "https://apekato.com/homolog/webresources/";        
    }  
    
    public static class ResourceBundlePath{
        public static final String LOCAL = "src/main/webapp/WEB-INF/classes";
        public static final String AWS = "apktserv-1/WEB-INF/classes";        
    }    
    public static class LanguegeType{
    	public static final ResourceBundle resourceBundle_default = ResourceBundle.getBundle(
                "SystemMessages", Locale.getDefault());

    	public static final ResourceBundle resourceBundle_pt = ResourceBundle.getBundle(
                "SystemMessages", Locale.forLanguageTag("pt"));
    }   
}



