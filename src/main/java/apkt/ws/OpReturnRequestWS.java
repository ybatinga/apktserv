package apkt.ws;

import apkt.json.OpReturnJson;
import apkt.mail.JavaMailThread;
import apkt.model.OpReturn;
import apkt.opreturn.OpReturnRunnable;
import com.google.common.util.concurrent.Service;
import com.google.gson.Gson;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;

@Path("opReturnRequestWS")
public class OpReturnRequestWS {
    
    public static NetworkParameters params = TestNet3Params.get();
    public static final String APP_NAME = "WalletTemplate";
    private static final String WALLET_FILE_NAME = APP_NAME.replaceAll("[^a-zA-Z0-9.-]", "_") + "-"
            + params.getPaymentProtocolId();
    public static WalletAppKit bitcoin;

    public OpReturnRequestWS() {
    }

    
//    @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            OpReturnJson opReturnJson = new Gson().fromJson(jsonclass, OpReturnJson.class);
            OpReturn opReturn = opReturnJson.getOpReturn();
            opReturn.setDateOpReturn(new Date());
            opReturn.setText("test");
            
            setupWalletKit();
//        Address address = getBitcoin().wallet().currentReceiveAddress();

            bitcoin.addListener(new Service.Listener() {

                @Override
                public void running() {
                    super.running();
                }

            }, Runnable::run);
            bitcoin.addListener(new Service.Listener() {}, OpReturnRunnable::runLater);
            bitcoin.startAsync();
            
//            OpReturnService opReturnService = OpReturnService.getInstance();
//
//            String address = opReturnService.getFreshReceiveAddress();

//            opReturn.setAddress("address");
//            
//            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
//            EntityManager em = emf.createEntityManager();
//            
//                GenericDaoJpa.insert(em, opReturn);
//            
//            em.close(); emf.close();
            
            
//            demo.startThread();

            opReturn.setDateOpReturn(null);
            opReturn.setText(null);
            opReturnJson.setOpReturn(opReturn);
            
            String gson = new Gson().toJson(opReturnJson);        
            return gson;
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
    }
    
    public static void main(String[] args) {
        try {

        OpReturn opReturn = new OpReturn("test", "addresssadfsaa", OpReturn.OpReturnStatus.OP_RETURN_STATUS_INVALID_DATA, new Date());
        OpReturnJson opReturnJson = new OpReturnJson(opReturn);
        String gson = new Gson().toJson(opReturnJson);
        String url_ = "http://localhost:8080/webresources/".concat("opReturnRequestWS");
        URL url = new URL(url_);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();
        
        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        OutputStream os = urlConnection.getOutputStream();
        os.write(gson.getBytes());
        os.flush();

        InputStream in = urlConnection.getInputStream();
        InputStreamReader reader =
                new InputStreamReader(in);

        String test = null;
            
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }

    public void setupWalletKit() {
        // If seed is non-null it means we are restoring from backup.
        if (bitcoin == null)
        bitcoin = new WalletAppKit(params, new File("."), WALLET_FILE_NAME) {
            @Override
            protected void onSetupCompleted() {
                // Don't make the user wait for confirmations for now, as the intention is they're sending it
                // their own money!
                bitcoin.wallet().allowSpendingUnconfirmedTransactions();

            }
        };
    
    }
}
