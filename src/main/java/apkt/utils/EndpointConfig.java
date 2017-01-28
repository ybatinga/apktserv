package apkt.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.commons.lang.StringUtils;

public class EndpointConfig {

    public static final String PROPERTY_FILE = "blockcypher.endpoint.properties";
    private static String propertyFileVersion;
    private static String propertyFileCurrency;
    private static String propertyFileNetwork;
    private static String propertyFileEndpoint;
    private String version = null;
    private String currency = null;
    private String network = null;
    private String token = null;
    private String endpoint = null;

    public EndpointConfig(String version, String currency, String network) {
        endpointConfig(version, currency, network, BlockCypherConstants.BLOCK_CYPHER_ENDPOINT);
    }
    
    public EndpointConfig(String version, String currency, String network, String token) {
        endpointConfigToken(version, currency, network, token, BlockCypherConstants.BLOCK_CYPHER_ENDPOINT);
    }

    private void endpointConfig(String version, String currency, String network, String endpoint) {
        this.version = version;
        this.currency = currency;
        this.network = network;
        this.endpoint = endpoint;
    }
    
    private void endpointConfigToken(String version, String currency, String network, String token, String endpoint) {
        this.version = version;
        this.currency = currency;
        this.network = network;
        this.token = token;
        this.endpoint = endpoint;
    }

    public EndpointConfig() {
        this.version = propertyFileVersion;
        this.currency = propertyFileCurrency;
        this.network = propertyFileNetwork;
        this.endpoint = propertyFileEndpoint;
    }

    static {
        try {
            Properties prop = new Properties();
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream stream = loader.getResourceAsStream(PROPERTY_FILE);
            if (stream != null) {
                prop.load(stream);
                propertyFileVersion = prop.getProperty("version");
                propertyFileCurrency = prop.getProperty("currency");
                propertyFileNetwork = prop.getProperty("network");
                propertyFileEndpoint = prop.getProperty("endpoint");
            }
        } catch (IOException e) {
        }
    }

    public String getVersion() {
        return version;
    }

    public String getCurrency() {
        return currency;
    }

    public String getNetwork() {
        return network;
    }

    public String getEndpoint() {
        return endpoint;
    }
    
    public String getToken() {
        return token;
    }


    public boolean isValid() {
        return !(StringUtils.isBlank(version) ||
                StringUtils.isBlank(currency) ||
                StringUtils.isBlank(network) ||
                StringUtils.isBlank(token) ||
                StringUtils.isBlank(endpoint));
    }

}
