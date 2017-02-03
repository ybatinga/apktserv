package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.cypher.RsaCypher;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.LoginWSDaoJpa;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.ProjService;
import apkt.service.StringVarsService;
import apkt.utils.StringUtil;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import org.apache.commons.lang.RandomStringUtils;

@Path("passwordForgot")
public class PasswordForgotWS {

    public PasswordForgotWS() {
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
            // email needs to be set to reset password
            String email = new String (RsaCypher.decryptData(authAux.getEmailB()));
            
            Login login = LoginWSDaoJpa.emailCheck(em, email);
            
            if (login != null){
                String generatedString = RandomStringUtils.randomAlphanumeric(30);
                String ok = LoginWSDaoJpa.updatePassAuxWord(em, login.getId(), generatedString);
                
                StringUtil.setLanguageResource(login.getLang());
                
                StringBuilder emailBody = new StringBuilder();
                emailBody.append(ProjService.RB.getString("email_body_hi") + " " + " ");
                emailBody.append(login.getUsername() + ",");
                emailBody.append("<br></br><br></br>");
                emailBody.append(ProjService.RB.getString("email_body_password_forgot") + " ");
                emailBody.append(generatedString);
                emailBody.append("<br></br><br></br>");
                emailBody.append(ProjService.RB.getString("email_body_end"));
                
                JavaMailThread javaMailThread_1 = new JavaMailThread(
                    email, 
                    ProjService.RB.getString("email_subject_password_forgot_subject"), 
                    emailBody.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.shutdown();
                
                em.close(); emf.close();
                return StringVarsService.OK;
            } else {
                em.close(); emf.close();
                return null;
            }
        }catch(Exception ex){
            
        }
        
        return null;
    }

}
