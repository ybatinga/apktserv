/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.cypher.RsaCypher;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.LoginWSDaoJpa;
import apkt.json.LoginJson;
import apkt.mail.JavaMailThread;
import apkt.model.AppVersion;
import apkt.model.Login;
import apkt.model.MobileInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * REST Web Service
 *
 * @author eizesazake
 */
@Path("login")
public class LoginWS {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LoginWS
     */
    public LoginWS() {
    }

//    @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(@PathParam("jsonclass") String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try{
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");            
//            CypherJson cypherJson = new Gson().fromJson(jsonclass, CypherJson.class);
//            byte[] b = RsaCypher.decryptData(cypherJson.getEncrypted());
//            List<BigInteger> bigIntegerList = cypherJson.getBigIntegerList();
//            AuthAux authAux =
//                    new Gson().fromJson(new String(b), AuthAux.class);
            
            AuthAux authAux =
                    new Gson().fromJson(jsonclass, AuthAux.class);
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
        
            EntityManager em = emf.createEntityManager();

            LoginWSDaoJpa loginWSDaoJpa = new LoginWSDaoJpa();

            LoginJson loginJson = null;
            MobileInfo mobileInfo = null;
            
            String appVer = null;
            boolean isVerifyVersion = false;
            
            String email = new String (RsaCypher.decryptData(authAux.getEmailB()));
            
            byte[] decoded = Base64.decodeBase64(authAux.getPasswordB());            
            byte[] decrypted = RsaCypher.decryptData(decoded);
            String sha512HexPassword = DigestUtils.sha512Hex(decrypted);

//            if (email.equals("desenv.notes@gmail.com")){
//                authAux.setSimSerialNumber("8955044rjsclgried009");
//            }
                
            Login login = loginWSDaoJpa.loginCheck(em, email, sha512HexPassword);
            if (login != null){
                // sets app language
                login.setLang(authAux.getLanguage());
                // update language 
                GenericDaoJpa.update(em, Login.class, login);
                
                mobileInfo = loginWSDaoJpa.mobileInfoCheck(
                    em, 
                    login.getId(),
                    authAux.getSimSerialNumber(),
                    authAux.getSimOperator(),
                    // data below is not available when there is no cell operator service
//                    authAux.getNetworkCountryIso(), 
                    authAux.getSubscriberId()
                    );
            }
            
    //        if (!loginPostJsonClass.getGcmClientRegId().equals(
    //                login.getGcmRegId())){
    //            loginWSDaoJpa.gcmRegId(em, loginPostJsonClass.getGcmClientRegId(), login.getId());
    //        }

            if (login != null 
                    //&& mobileInfo != null
                    ) {
                AppVersion appVersion = (AppVersion) GenericDaoJpa.findByAttribute(
                        em, 
                        AppVersion.class,
                        "appName",
                        authAux.getAppName());
                if (appVersion != null){
                    appVer = appVersion.getAppVersion();
                    isVerifyVersion = appVersion.isVerifyVersion();
                }
                // the following validation can't be used:
                // if (!loginPostJsonClass.getGcmClientRegId().equals(login.getGcmRegId())).
                // This validation doesn't work when there are two factors combined:
                // 1. android app is connected to a wifi router (not 3G)
                // 2. server app is running the cloud
                // When these factors are combined, everytime an update to gcm_reg_id column
                // in Login table is done manually for testing, the android app is not 
                // able to update the gcmRegId on postgres.
                // It just won't do it. I tried using EntityManager's flush() and clear() methods,
                // also tried using a new instance of EntityManager*, and none of them worked.
                loginWSDaoJpa.gcmRegId(em, 
                        authAux.getGcmClientRegId(),
                        null,
//                        new Gson().toJson(bigIntegerList), 
                        login.getId());
                login.setGcmRegId(authAux.getGcmClientRegId());
                
                if (mobileInfo == null){
                    mobileInfo = new MobileInfo();
                }
                
                loginJson = new LoginJson(
                        login.getId(),
                        login.getGcmRegId(),
                        login.getUsername(),
                        login.getDocNum(),
                        login.getDocUsername(),
                        login.getEmail(),
                        login.getCurrencyCode(),
                        mobileInfo.getSimSerialNumber(),
                        mobileInfo.getSimOperator(),
                        mobileInfo.getNetworkCountryIso(),
                        mobileInfo.getSubscriberId(),
                        null, //login.getDateSignup(), causes on android app: java.text.ParseException: Unparseable date: at java.text.DateFormat.parse
                        appVer,
                        isVerifyVersion);


            } else if (login == null) {
                boolean userNonexistent = true;
                loginJson = new LoginJson(userNonexistent);
            }

            em.close(); emf.close();
            
            // Gson variable exclusion strategy
            // http://programmerbruce.blogspot.com.br/2011/07/gson-v-jackson-part-4.html
            Gson gson = new Gson();
            gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED)
                .create();
            String gsonRes = gson.toJson(loginJson);
            return gsonRes;
            
        }catch(Exception e){
            e.printStackTrace();
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", "Login", new Date().toString() + " " + e.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
        }
        return null;
    }
}
