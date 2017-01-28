package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListPymntMthdJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.PymntMthd;
import apkt.dao.jpa.ServiceDaoJpa;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("pymntMthdDelete")
public class PymntMthdDeleteWS {

    public PymntMthdDeleteWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            ListPymntMthdJson listPymntMthdJson = new Gson().fromJson(jsonclass, ListPymntMthdJson.class);
            AuthAux authAux = listPymntMthdJson.getAuthAux();
            PymntMthd pymntMthd = listPymntMthdJson.getPymntMthd();
            Integer deletedCount = 0;
            List<PymntMthd> list = null;         
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            Login loginAuth = ServiceDaoJpa.authUser(em, listPymntMthdJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            listPymntMthdJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
    //        Integer deletedCount = 1;
            
    //        int res = DaoJpaService.authUser(
    //                objGenDelJson.getEmail(), 
    //                objGenDelJson.getUsername(), 
    //                objGenDelJson.getPassWord(),
    //                objGenDelJson.getSimSerialNumber(),
    //                objGenDelJson.getDeviceId());
            
    //        if (res == 1){
                deletedCount = GenericDaoJpa.deleteObj(
                    em,
                    pymntMthd.getId(),
                    pymntMthd.getClass());
                list = ServiceDaoJpa.getObjList(em, PymntMthd.class, pymntMthd.getLoginId(), "loginId", pymntMthd.getCurrencyCode(), "currencyCode");
    //        }
             
            em.close(); emf.close();

            ListPymntMthdJson listPymntMthdJson_2 = new ListPymntMthdJson(list);            
            String listGson = new Gson().toJson(listPymntMthdJson_2);        
            return listGson;
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
        
    }
}
