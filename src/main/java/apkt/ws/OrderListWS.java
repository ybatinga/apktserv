package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListOrderJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.Order;
import apkt.dao.jpa.ServiceDaoJpa;
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
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("orderList")
public class OrderListWS {

    public OrderListWS() {
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
//            CypherJson cypherJson = new Gson().fromJson(jsonclass, CypherJson.class);
//            byte[] b = RsaCypher.decryptData(cypherJson.getEncrypted());
//            AuthAux authAux = new Gson().fromJson(new String(b), AuthAux.class);
            
            ListOrderJson listOrderJson = new Gson().fromJson(jsonclass, ListOrderJson.class);
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            Login loginAuth = ServiceDaoJpa.authUser(em, listOrderJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            listOrderJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
            
            List<Order> list = ServiceDaoJpa.getOrderList(em, listOrderJson.getUserId(), listOrderJson.getListType());
            
            em.close(); emf.close();
            
            listOrderJson.setOrderList(list);
            
            Gson gson = new Gson();
            gson = new GsonBuilder()
                .excludeFieldsWithModifiers(Modifier.PROTECTED)
                .create();
            String gsonRes = gson.toJson(listOrderJson);
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
