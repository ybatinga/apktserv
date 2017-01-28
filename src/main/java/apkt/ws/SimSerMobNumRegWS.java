package apkt.ws;

import apkt.cypher.RsaCypher;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.SimSerMobNumRegDaoJpa;
import apkt.json.LoginJson;
import apkt.json.RegUserPostJson;
import apkt.model.Login;
import apkt.model.PymntMthd;
import apkt.model.Wallet;
import apkt.dao.jpa.ServiceDaoJpa;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Modifier;
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
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

@Path("simSerMobNumReg")
public class SimSerMobNumRegWS {

    public SimSerMobNumRegWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            RegUserPostJson regUserPostJson = 
                    new Gson().fromJson(jsonclass, RegUserPostJson.class);
            
            SimSerMobNumRegDaoJpa simSerMobNumRegDaoJpa = new SimSerMobNumRegDaoJpa();
            
            String email = new String (RsaCypher.decryptData(regUserPostJson.getAuthAux().getEmailB()));
            byte[] encoded = Base64.encodeBase64(RsaCypher.decryptData(regUserPostJson.getAuthAux().getPasswordB()));
            // decodes password into string with sha512Hex hash
            String sha512HexPassword = DigestUtils.sha512Hex(encoded);
            // there is code redundancy as there are two Login class updates (GenericDaoJpa.updateWithoutTx and simSerMobNumRegDaoJpa.update),
            // but, since GenericDaoJpa.updateWithoutTx is used temporarily to update language throughout the app,
            // the code block below is used            
            Login login = GenericDaoJpa.findByAttributeTwo(em, Login.class, "email", email, "passWord", sha512HexPassword);
            // update language 
            GenericDaoJpa.update(em, Login.class, login);
            regUserPostJson.setAuthAux(null);
            if (login == null){
                return null;
            }
            
            Login loginResult = simSerMobNumRegDaoJpa.update(regUserPostJson);
            List<PymntMthd> pymntMthdList = ServiceDaoJpa.getObjList(em, PymntMthd.class, loginResult.getId(), "loginId", loginResult.getCurrencyCode(), "currencyCode");
            List<Wallet> walletList = ServiceDaoJpa.getObjList(em, Wallet.class, loginResult.getId(), "loginId");        em.close(); emf.close();
            LoginJson loginJson = new LoginJson(
                        loginResult.getId(),
                        loginResult.getGcmRegId(),
                        loginResult.getUsername(),
                        loginResult.getDocNum(),
                        loginResult.getDocUsername(),
                        loginResult.getEmail(),
                        loginResult.getCurrencyCode(),
                        regUserPostJson.getSimSerialNumber(),
                        regUserPostJson.getSimOperator(),
                        regUserPostJson.getNetworkCountryIso(),
                        regUserPostJson.getSubscriberId(),
                        loginResult.getDateSignup(),
                        pymntMthdList,
                        walletList
                        );
        
        // Gson variable exclusion strategy
        // http://programmerbruce.blogspot.com.br/2011/07/gson-v-jackson-part-4.html
        Gson gson = new Gson();
        gson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.PROTECTED)
            .create();
        String gsonRes = gson.toJson(loginJson);
        return gsonRes;
            
        } catch (NoResultException ex) {
            Logger.getLogger(SimSerMobNumRegWS.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(SimSerMobNumRegWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
        
    }

    public static void main(String[] args) throws CertificateException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		// FileReader("/usr/local/my/envir/glassfish-3.1.2.2/glassfish/domains/domain1/config/nestle"));

		// Load CAs from an InputStream
		// (could be from a resource or ByteArrayInputStream or ...)
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		// From https://www.washington.edu/itconnect/security/ca/load-der.crt
		InputStream caInput = new BufferedInputStream(new FileInputStream("/usr/local/my/envir/glassfish-3.1.2.2/glassfish/domains/domain1/config/server.cer"));
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

		String url_ = "https://localhost:8181/apekatoserv/webresources/"
                + "simSerMobNumReg";
		
                RegUserPostJson regUserPostJson = new RegUserPostJson();
                regUserPostJson.setId(Long.MIN_VALUE);
                regUserPostJson.setEmail("asdf@asdf.com");
                String gson = new Gson().toJson(regUserPostJson);
	
                
		URL url = new URL(url_);                
		HttpsURLConnection urlConnection =
		    (HttpsURLConnection)url.openConnection();
                urlConnection.setSSLSocketFactory(context.getSocketFactory());
//                HttpURLConnection urlConnection =
//		    (HttpURLConnection)url.openConnection();
                
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");

                OutputStream os = urlConnection.getOutputStream();
                os.write(gson.getBytes());
                os.flush();
                
		
		InputStream in = urlConnection.getInputStream();
		InputStreamReader reader =
				new InputStreamReader(in);        
        
        RegUserPostJson stringTest =
                new Gson().fromJson(reader, RegUserPostJson.class);

        System.out.println(
                "LoginResult: " + stringTest.toString());
	

	}
}