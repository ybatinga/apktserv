package apkt.ws;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.AddressService;
import apkt.model.Address;
import apkt.service.ProjService;
import apkt.utils.BlockCypherConstants;
import com.google.gson.Gson;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Path("genericGet")
public class GenericGet {

    public GenericGet() {
    }

    @GET
    @Path("/{jsonclass}")
    @Produces("application/json")
    public String getJson(@PathParam("jsonclass") String jsonclass) {        
        AddressService addressService;
        Address addressMaker;
        try{
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            BlockCypherContext blockCypherContext = new BlockCypherContext(
                    BlockCypherConstants.VERSION_V1,
                    BlockCypherConstants.CURRENCY_BTC,
                    BlockCypherConstants.NETWORK);

            addressService = blockCypherContext.getAddressService();
            
            addressMaker = addressService.createAddress();
            
        } catch (Exception exception) {
            exception.printStackTrace();
            return new Gson().toJson(exception.toString());
        }
        return new Gson().toJson(addressMaker);
        
    }
    
    public static void main(String[] args) {
        try {

        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        // From https://www.washington.edu/itconnect/security/ca/load-der.crt
         InputStream caInput = new BufferedInputStream(new FileInputStream("/home/elton/Desktop/server.cer"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        /*gsonString = StringUtils.formatJsonSpecialCharacters(gsonString);*/

        String url_ = ProjService.URL.concat("genericGet").concat("/").concat("test");

        URL url = new URL(url_);
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());

        InputStream in = urlConnection.getInputStream();
        InputStreamReader reader =
                new InputStreamReader(in);

        Address addressMaker = new Gson().fromJson(reader,
                        Address.class);
        
        String test = null;
            
        } catch (KeyManagementException ex) {
            Logger.getLogger(GenericGet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(GenericGet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (KeyStoreException ex) {
            Logger.getLogger(GenericGet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (CertificateException ex) {
            Logger.getLogger(GenericGet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(GenericGet.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
