package apkt.ws;

import apkt.backingbean.LoginBB;
import apkt.backingbean.MobileInfoBB;
import apkt.cypher.RsaCypher;
import apkt.json.RegUserPostJson;
import apkt.model.Login;
import apkt.dao.jpa.AuthentUniqueVarsDaoJpa;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.dao.jpa.LoginWSDaoJpa;
import apkt.dao.jpa.RegUserDaoJpa;
import apkt.json.LoginJson;
import apkt.mail.JavaMailThread;
import apkt.model.MobileInfo;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

@Path("regUser")
public class RegUserWS {

    public RegUserWS() {
    }

//    @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(@PathParam("jsonclass") String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            RegUserDaoJpa regUserDaoJpa = new RegUserDaoJpa();
            
            RegUserPostJson regUserPostJson = 
                    new Gson().fromJson(jsonclass, RegUserPostJson.class);
            
            LoginWSDaoJpa loginWSDaoJpa = new LoginWSDaoJpa();
            
            Login loginResult = null;
            
            MobileInfo mobileInfoRes = null;
            
            LoginJson loginJson = null;
            
            String simSerialNumExists = null;
            String emailExists = null;
            String usernameExists = null;

            String email = new String (RsaCypher.decryptData(regUserPostJson.getEmailB()));
            
            byte[] decoded = Base64.decodeBase64(regUserPostJson.getPasswordB());            
            byte[] decrypted = RsaCypher.decryptData(decoded);
            String sha512HexPassword = DigestUtils.sha512Hex(decrypted);

            // set login variables in LoginBB constructor
            LoginBB loginBB = new LoginBB(
                    regUserPostJson.getGcmRegId(),
                    regUserPostJson.getMobileNum(),
                    regUserPostJson.getMobileNumState(),
                    regUserPostJson.getUsername(),
                    sha512HexPassword,
                    email,
                    regUserPostJson.getDocNum(),
                    new Date(),
                    regUserPostJson.getCurrencyCode(),
                    regUserPostJson.getLang());

            // send password to user, before hashing password
    //        JavaMail javaMail = new JavaMail();
    //        javaMail.send(cadMobInfJsonClass.getEmail());
            
            // Code is used to verify SimSerialNumber or Email or Username is alredy registered
            // Affected classes:
            // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsWS (SERVER), AuthentUniqueVarsDaoJpa (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
            List<Login> loginAlreadyRegList = AuthentUniqueVarsDaoJpa.verifyUniqueVars(
                    em,
                    email,
                    regUserPostJson.getUsername());            
            List<MobileInfo> mobileInfoAlreadyRegList = AuthentUniqueVarsDaoJpa.verifyUniqueVarsMobileInfo(
                    em,
                    regUserPostJson.getSimSerialNumber());
            
            // checks if verification returned a list with an already registered login.
            // If so, it gets the variables (SimSerialNumber or Email or Username)
            // already registered.
            if (loginAlreadyRegList.size() > 0 
//                    || mobileInfoAlreadyRegList.size() > 0 // uncomment this line of code when simserialnumber verification is needed
                    ){

                // assigns to *Exists variables values that have already been registered.
                for (Login loginAlredyRegistered : loginAlreadyRegList){
                    
                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
                    // Affected classes:
                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsWS (SERVER), AuthentUniqueVarsDaoJpa (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
                    if (loginAlredyRegistered.getEmail()
                            .equals(email)){
                        emailExists = loginAlredyRegistered.getEmail();
                    }
                    if (loginAlredyRegistered.getUsername()
                            .equals(regUserPostJson.getUsername())){
                        usernameExists = loginAlredyRegistered.getUsername();
                    }
                }

                // assigns to *Exists variables values that have already been registered.
//                for (MobileInfo mobileInfoAlredyRegistered : mobileInfoAlreadyRegList){
//                    
//                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
//                    // Affected classes:
//                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsWS (SERVER), AuthentUniqueVarsDaoJpa (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
//                    if (mobileInfoAlredyRegistered.getSimSerialNumber()
//                            .equals(regUserPostJson.getSimSerialNumber())){
//                        simSerialNumExists = mobileInfoAlredyRegistered.getSimSerialNumber();
//                    }
//                }
            }else{
                try {
                    // persists Login 
                    String ok = regUserDaoJpa.regUser(em, loginBB.getLoginAux());

                    // sets mobileInfo variables in MobileInfoBB constructor
                    MobileInfoBB mobileInfo = new MobileInfoBB(
                            loginBB.getLoginAux(),
                            regUserPostJson.getLine1Number(),
                            regUserPostJson.getDeviceId(),
                            regUserPostJson.getNetworkCountryIso(),
                            regUserPostJson.getSimOperatorName(),
                            regUserPostJson.getSimSerialNumber(),
                            regUserPostJson.getSubscriberId(),
                            regUserPostJson.getDeviceSoftwareVersion(),
                            regUserPostJson.getNetworkOperator(),
                            regUserPostJson.getNetworkOperatorName(),
                            regUserPostJson.getSimOperator(),
                            regUserPostJson.getVoiceMailNumber(),
                            regUserPostJson.getCallState(),
                            regUserPostJson.getDataState(),
                            regUserPostJson.getSimState(),
                            regUserPostJson.getNetworkType(),
                            regUserPostJson.getPhoneType());

                    // persists MobileInfo
                    mobileInfoRes = regUserDaoJpa.insert(em, mobileInfo.getMobileInfoAux());
                    
                } catch (Exception e) {
                    JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), new Date().toString() + " " + e.toString());
                    ExecutorService threadExecutor = Executors.newCachedThreadPool();
                    threadExecutor.execute(javaMailThread_1);
                    threadExecutor.shutdown();                         
                }finally{
                    loginResult = loginWSDaoJpa.loginCheck(
                            em, 
                            email,
                            sha512HexPassword);
                }    
            }
            
            if (loginResult == null){
                loginJson = new LoginJson(simSerialNumExists, emailExists, usernameExists);
            }else{
                loginJson = new LoginJson(
                        loginResult.getId(),
                        loginResult.getGcmRegId(),
                        loginResult.getUsername(),
                        loginResult.getDocNum(),
                        loginResult.getDocUsername(),
                        loginResult.getEmail(),
                        loginResult.getCurrencyCode(),
                        mobileInfoRes.getSimSerialNumber(),
                        mobileInfoRes.getSimOperator(),
                        mobileInfoRes.getNetworkCountryIso(),
                        mobileInfoRes.getSubscriberId(),
                        loginResult.getDateSignup(),
                        simSerialNumExists,
                        emailExists,
                        usernameExists
                        );
            }
            em.close(); emf.close();
            return new Gson().toJson(loginJson);
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), new Date().toString() + " " + ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
    }

    
//    public static void main(String[] args) {;
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
//        EntityManager em = emf.createEntityManager();
//        
//        RegUserDaoJpa regUserDaoJpa = new RegUserDaoJpa();
//            
//            RegUserPostJson regUserPostJson =
//                new RegUserPostJson(
//                "94594294",
//                "41",
//                "mcante",
//                "password",
//                "mcante@gmail.com",
//                "23467587312",
//                new Date(),
//                "deviceId",
//                "simSerialNumberwe",
//                "simOperator",
//                "line1Number",
//                "simOperatorName",
//                "networkOperator",
//                "subscriberId",
//                "networkOperatorName",
//                "networkCountryIso",
//                0,
//                0,
//                0,
//                "deviceSoftwareVersion",
//                0,
//                0,
//                "voiceMailNumber",
//                0,
//                "RSE91bGvvJ9eSyeF2Gt3mO0R-fW0bm_UT8M-XaomiZeQ3aK6Qbr4MT2UU6OTnETrEgbucfmwZhpOxCiRXTy58QfxL8F6_Se09cPAHuMGoc6C7kTIHCaMn0XyBYWkPunFKhVw-3d_HxB9S3oQVLM0z5tt8Xp7IBEttg");
////        regUserPostJson.setMotoboy(true);
//            
//        LoginWSDaoJpa loginWSDaoJpa = new LoginWSDaoJpa();
//        
//        Login loginResult = new Login();
//        
//        String simSerialNumExists = null;
//        String emailExists = null;
//        String usernameExists = null;
//
//        // encode password into bytes 
//        byte[] encoded = Base64.encodeBase64(regUserPostJson.getPassWord().getBytes());
//        // decode password into string with sha512Hex hash
//        String sha512HexPassword = DigestUtils.sha512Hex(encoded);
//
//        // set login variables in LoginBB constructor
//        LoginBB loginBB = new LoginBB(
//                regUserPostJson.getGcmRegId(),
//                regUserPostJson.getMobileNum(),
//                regUserPostJson.getMobileNumState(),
//                regUserPostJson.getUsername(),
//                sha512HexPassword,
//                regUserPostJson.getEmail(),
//                regUserPostJson.getDocNum(),
//                regUserPostJson.getDateLogin(),
//                regUserPostJson.getCurrencyCode(),
//                regUserPostJson.getLang());
//
//        // send password to user, before hashing password
////        JavaMail javaMail = new JavaMail();
////        javaMail.send(cadMobInfJsonClass.getEmail());
//        
//        // Code is used to verify SimSerialNumber or Email or Username is alredy registered
//            // Affected classes:
//            // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsWS (SERVER), AuthentUniqueVarsDaoJpa (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
//            List<Login> loginAlreadyRegList = AuthentUniqueVarsDaoJpa
//                    .verifyUniqueVars(
//                    em,
//                    regUserPostJson.getEmail(),
//                    regUserPostJson.getUsername());            
//            List<MobileInfo> mobileInfoAlreadyRegList = AuthentUniqueVarsDaoJpa
//                    .verifyUniqueVars(
//                    em,
//                    regUserPostJson.getEmail(),
//                    regUserPostJson.getUsername());
//            
//            // checks if verification returned a list with an already registered login.
//            // If so, it gets the variables (SimSerialNumber or Email or Username)
//            // already registered.
//            if (loginAlreadyRegList.size() > 0 || mobileInfoAlreadyRegList.size() > 0){
//
//                // assigns to *Exists variables values that have already been registered.
//                for (Login loginAlredyRegistered : loginAlreadyRegList){
//                    
//                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
//                    // Affected classes:
//                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsWS (SERVER), AuthentUniqueVarsDaoJpa (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
//                    if (loginAlredyRegistered.getEmail()
//                            .equals(regUserPostJson.getEmail())){
//                        emailExists = loginAlredyRegistered.getEmail();
//                    }
//                    if (loginAlredyRegistered.getUsername()
//                            .equals(regUserPostJson.getUsername())){
//                        usernameExists = loginAlredyRegistered.getUsername();
//                    }
//                }
//
//                // assigns to *Exists variables values that have already been registered.
//                for (MobileInfo mobileInfoAlredyRegistered : mobileInfoAlreadyRegList){
//                    
//                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
//                    // Affected classes:
//                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsWS (SERVER), AuthentUniqueVarsDaoJpa (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
//                    if (mobileInfoAlredyRegistered.getSimSerialNumber()
//                            .equals(regUserPostJson.getSimSerialNumber())){
//                        simSerialNumExists = mobileInfoAlredyRegistered.getSimSerialNumber();
//                    }
//                }
//                
//        }else{
//            try {
//                // persists Login 
//                String ok = regUserDaoJpa.regUser(em, loginBB.getLoginAux());
//
//                // sets mobileInfo variables in MobileInfoBB constructor
//                MobileInfoBB mobileInfo = new MobileInfoBB(
//                        loginBB.getLoginAux(),
//                        regUserPostJson.getLine1Number(),
//                        regUserPostJson.getDeviceId(),
//                        regUserPostJson.getNetworkCountryIso(),
//                        regUserPostJson.getSimOperatorName(),
//                        regUserPostJson.getSimSerialNumber(),
//                        regUserPostJson.getSubscriberId(),
//                        regUserPostJson.getDeviceSoftwareVersion(),
//                        regUserPostJson.getNetworkOperator(),
//                        regUserPostJson.getNetworkOperatorName(),
//                        regUserPostJson.getSimOperator(),
//                        regUserPostJson.getVoiceMailNumber(),
//                        regUserPostJson.getCallState(),
//                        regUserPostJson.getDataState(),
//                        regUserPostJson.getSimState(),
//                        regUserPostJson.getNetworkType(),
//                        regUserPostJson.getPhoneType());
//                
//                EntityTransaction tx = em.getTransaction();
//                tx.begin();
//                    // persists MobileInfo
//                    regUserDaoJpa.insertWithoutTx(em, mobileInfo.getMobileInfoAux());
//
//                tx.commit();
//
//                loginResult = loginWSDaoJpa.loginCheck(em, regUserPostJson.getEmail(), regUserPostJson.getPassWord());
//            } catch (Exception ex) {
//                Logger.getLogger(RegUserWS.class.getName()).log(Level.SEVERE, null, ex);
//            }
//
//        }
//        
//        // sets id with zere value since it has not received value from
//        // method loginWSDaoJpa.loginCheck(...).
//        if (loginResult.getId() == null){
//            loginResult.setId(Long.parseLong("0"));
//        }
//       
//    }
    
//    public static void main(String[] args) {
//        try {
//            
//            CadMobInfPostJsonClass cadMobInfJsonClass =
//                new CadMobInfPostJsonClass(
//                "94594294",
//                "41",
//                "mcante",
//                "password",
//                "mcante@gmail.com",
//                new Date(),
//                "deviceId",
//                "simSerialNumberwe",
//                "simOperator",
//                "line1Number",
//                "simOperatorName",
//                "networkOperator",
//                "subscriberId",
//                "networkOperatorName",
//                "networkCountryIso",
//                0,
//                0,
//                0,
//                "deviceSoftwareVersion",
//                0,
//                0,
//                "voiceMailNumber",
//                0);
//        cadMobInfJsonClass.setMotoboy(true);
//            
//        String gson = new Gson().toJson(cadMobInfJsonClass);
//            
//            gson = gson.replace(" ", "%20");
//            gson = gson.replace("\"", "%22");
//            gson = gson.replace("{", "%7B");
//            gson = gson.replace("}", "%7D");
//            
//            String url = Proj.wsip
//                    + "CadMob/"
//                    + gson;
//
//            InputStreamReader reader =
//                    new InputStreamReader(new URL(url).openStream());
//
//             // parse the JSON back into a TextMessage
//            CadMobInfGetJsonClass cadMobInfGetJsonClass_2 =
//                    new Gson().fromJson(reader, CadMobInfGetJsonClass.class);
//
//            System.out.println(
//                    "LoginResult: " + cadMobInfGetJsonClass_2.toString());
//                
//        } catch (Exception exception) {
//            exception.printStackTrace(); // show exception details
//
//        }
//    }
}
