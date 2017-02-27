package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.json.OpReturnJson;
import apkt.mail.JavaMailThread;
import apkt.model.OpReturn;
import apkt.service.OpReturnService;
import apkt.service.ProjService;
import apkt.service.StringVarsService;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

@Path("opReturnRequestWS")
public class OpReturnRequestWS {

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
            if (OpReturnService.bitcoin.isRunning()){
                boolean sfa = true;
            }
            opReturn.setAddress(OpReturnService.bitcoin.wallet().freshReceiveAddress().toString());
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
                GenericDaoJpa.insert(em, opReturn);
            
            em.close(); emf.close();
            
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

        OpReturn opReturn = new OpReturn("test", "addresssadfsaa");
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

}
