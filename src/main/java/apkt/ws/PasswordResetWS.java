package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.cypher.RsaCypher;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.LoginWSDaoJpa;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.service.StringVarsService;
import apkt.utils.StringUtil;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

@Path("passwordReset")
public class PasswordResetWS {

    public PasswordResetWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getText(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");    
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            AuthAux authAux =
                    new Gson().fromJson(jsonclass, AuthAux.class);
            String email = new String (RsaCypher.decryptData(authAux.getEmailB()));
            String passwordResetCode = new String (RsaCypher.decryptData(authAux.getPasswordResetCodeB()));
            Login login = GenericDaoJpa.findByAttributeTwo(em, Login.class, "email", email, "passauxword", passwordResetCode);
            if (login != null){
                String ok = LoginWSDaoJpa.updatePassAuxWord(em, login.getId(), "");
                
                byte[] decoded = Base64.decodeBase64(authAux.getPasswordB());            
                byte[] decrypted = RsaCypher.decryptData(decoded);
                String sha512HexPassword = DigestUtils.sha512Hex(decrypted);
                
                String ok_1 = LoginWSDaoJpa.updatePassword(em, login.getId(), sha512HexPassword);
            }
            
            em.close(); emf.close();
        }catch(Exception ex){
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), new Date().toString() + " " + ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
        
        return StringVarsService.OK;
    }
}
