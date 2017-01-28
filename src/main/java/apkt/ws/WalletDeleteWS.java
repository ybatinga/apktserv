
package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListWalletJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.Wallet;
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

@Path("walletDelete")
public class WalletDeleteWS {

    public WalletDeleteWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            ListWalletJson listWalletJson = new Gson().fromJson(jsonclass, ListWalletJson.class);
            AuthAux authAux = listWalletJson.getAuthAux();
            Wallet wallet = listWalletJson.getWallet();
            Integer deletedCount = 0;
            List<Wallet> list = null;        
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            Login loginAuth = ServiceDaoJpa.authUser(em, listWalletJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            listWalletJson.setAuthAux(null);
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
                    wallet.getId(),
                    wallet.getClass());
                list = ServiceDaoJpa.getObjList(em, Wallet.class, wallet.getLoginId(), "loginId");
    //        }
             
            em.close(); emf.close();

            ListWalletJson listWalletJson_2 = new ListWalletJson(list);            
            String listGson = new Gson().toJson(listWalletJson_2);        
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
