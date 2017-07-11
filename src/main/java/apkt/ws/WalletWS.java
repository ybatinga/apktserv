package apkt.ws;

import apkt.backingbean.AuthAux;
import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.AddressService;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListWalletJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.Wallet;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.ProjService;
import apkt.service.StringVarsService;
import com.google.gson.Gson;
import apkt.utils.BlockCypherConstants;
import apkt.utils.StringUtil;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
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

@Path("wallet")
public class WalletWS {

    List<Wallet> walletlist = null;
    ListWalletJson listWalletJson_2;
    String listGson;
    
    public WalletWS() {
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
            ListWalletJson listWalletJson = new Gson().fromJson(jsonclass, ListWalletJson.class);
            AuthAux authAux = listWalletJson.getAuthAux();
            Wallet wallet = listWalletJson.getWallet();

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            Login loginAuth = ServiceDaoJpa.authUser(em, listWalletJson.getAuthAux());
            StringUtil.setLanguageResource(loginAuth.getLang());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            listWalletJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
            
            EntityTransaction tx = em.getTransaction();

            BlockCypherContext blockCypherContext = new BlockCypherContext(
                        BlockCypherConstants.VERSION_V1,
                        BlockCypherConstants.CURRENCY_BTC,
                        BlockCypherConstants.NETWORK);

            AddressService addressService = blockCypherContext.getAddressService();
            addressService.getAddress(wallet.getAddress());

            tx.begin();
                // set random id to avoid ERROR: duplicate key value violates unique constraint 
                wallet.setId(Long.MIN_VALUE);
                String result = GenericDaoJpa.insertWithoutTx(em, wallet);

                if (result.equals(StringVarsService.OK)) {

                    walletlist = ServiceDaoJpa.getObjList(em, Wallet.class, wallet.getLoginId(), "loginId");

                }

            tx.commit();
            em.close(); emf.close();
            listWalletJson_2 = new ListWalletJson(walletlist);
            listGson = new Gson().toJson(listWalletJson_2);
            return listGson;
        
        } catch (Exception ex) {            
            String e = ex.toString();
            if (e.contains(ProjService.RB.getString("exception_response_file_not_found_exception"))){
                Wallet wallet = new Wallet(
                        null,
                        ProjService.RB.getString("exception_response_file_not_found_exception"),
                        ProjService.RB.getString("exception_response_file_not_found_exception"));
                walletlist = new ArrayList<Wallet>(); 
                walletlist.add(wallet);
                listWalletJson_2 = new ListWalletJson(walletlist);
                listGson = new Gson().toJson(listWalletJson_2);
                return listGson;
            }else if (e.contains(ProjService.RB.getString("exception_response_code_400"))){
                Wallet wallet = new Wallet(
                        null,
                        ProjService.RB.getString("exception_response_code_400"),
                        ProjService.RB.getString("exception_response_code_400"));
                walletlist = new ArrayList<Wallet>(); 
                walletlist.add(wallet);
                listWalletJson_2 = new ListWalletJson(walletlist);
                listGson = new Gson().toJson(listWalletJson_2);
                return listGson;
            }else{
                JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), new Date().toString() + " " + ex.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.shutdown();
            }
            return null;        
        }
        
    }

}
