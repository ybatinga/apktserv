package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.LoginJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.json.ListPymntMthdJson;
import apkt.model.PymntMthd;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Path("currencyCode")
public class CurrencyCodeWS {

    public CurrencyCodeWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getText(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");    
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            LoginJson loginJson = new Gson().fromJson(jsonclass, LoginJson.class);
            AuthAux authAux = loginJson.getAuthAux();
            
            Login loginAuth = ServiceDaoJpa.authUser(em, authAux);
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            loginJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
            
            Login login = (Login) GenericDaoJpa.find(em, Login.class, loginJson.getId());
            
            login.setCurrencyCode(loginJson.getCurrencyCode());
            
            GenericDaoJpa.update(em, Login.class, login);
            List<PymntMthd> pymntMthdList = ServiceDaoJpa.getObjList(em, PymntMthd.class, login.getId(), "loginId", login.getCurrencyCode(), "currencyCode");                
            em.close(); emf.close();

            // return a ListPymntMthd of the selected currency because, when making Buy/Sell Orders,
            // there is a verification of payment method for that currency
            ListPymntMthdJson listPymntMthdJson = new ListPymntMthdJson(pymntMthdList);
            return new Gson().toJson(listPymntMthdJson);
            
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), new Date().toString() + " " + ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
        
    }
}
