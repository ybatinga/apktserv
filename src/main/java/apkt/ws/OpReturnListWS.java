package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.json.ListOpReturnJson;
import apkt.json.ListTxOpReturnJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.OpReturn;
import apkt.model.TxOpReturn;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.reflect.Modifier;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("opReturnList")
public class OpReturnListWS {
    
    public OpReturnListWS() {
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
            
            ListTxOpReturnJson listTxOpReturnJson = new Gson().fromJson(jsonclass, ListTxOpReturnJson.class);
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            List<TxOpReturn> txOpReturnList = null;
            
            if (listTxOpReturnJson.getUserId() != null){
                txOpReturnList = ServiceDaoJpa.getObjList(
                    em, 
                    TxOpReturn.class, 
                    listTxOpReturnJson.getUserId(), 
                    "loginId", 
                    OpReturn.OpReturnStatus.OP_RETURN_STATUS_REGISTERED, 
                    "status");
            } else {
                txOpReturnList = ServiceDaoJpa.getObjList(
                    em, 
                    TxOpReturn.class, 
                    null, 
                    null, 
                    OpReturn.OpReturnStatus.OP_RETURN_STATUS_REGISTERED, 
                    "status");
            }
            
            
            
            em.close(); emf.close();
            
            listTxOpReturnJson.setTxOpReturnList(txOpReturnList);
            
            Gson gson = new Gson();
            gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED)
                .create();
            String gsonRes = gson.toJson(listTxOpReturnJson);
            return gsonRes;
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
        
    }
}
