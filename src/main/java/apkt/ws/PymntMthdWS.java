/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListPymntMthdJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.PymntMthd;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.StringVarsService;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("pymntMthd")
public class PymntMthdWS {
    public PymntMthdWS() {
    }

//        @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(@PathParam("jsonclass") String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            ListPymntMthdJson listPymntMthdJson = new Gson().fromJson(jsonclass, ListPymntMthdJson.class);
            AuthAux authAux = listPymntMthdJson.getAuthAux();
            PymntMthd pymntMthd = listPymntMthdJson.getPymntMthd();
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
            
            EntityTransaction tx = em.getTransaction();
            tx.begin();
                // set random id to avoid ERROR: duplicate key value violates unique constraint 
                pymntMthd.setId(Long.MIN_VALUE);
                String result = GenericDaoJpa.insertWithoutTx(em, pymntMthd);

                if (result.equals(StringVarsService.OK)) {

                    list = ServiceDaoJpa.getObjList(em, PymntMthd.class, pymntMthd.getLoginId(), "loginId", pymntMthd.getCurrencyCode(), "currencyCode");

                }

            tx.commit();
            em.close(); emf.close();

            ListPymntMthdJson listPymntMthdJson_1 = new ListPymntMthdJson(list);

            String listGson = new Gson().toJson(listPymntMthdJson_1);        
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
