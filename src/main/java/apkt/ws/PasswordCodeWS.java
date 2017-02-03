package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.cypher.RsaCypher;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.model.Login;
import apkt.service.StringVarsService;
import apkt.utils.StringUtil;
import com.google.gson.Gson;
import java.net.URLDecoder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("passwordCode")
public class PasswordCodeWS {

    public PasswordCodeWS() {
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
                em.close(); emf.close();
                return StringVarsService.OK;
            }else {
                em.close(); emf.close();
                return null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        
        return null;
    }

}
