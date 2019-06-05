package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.json.OpReturnJson;
import apkt.mail.JavaMailThread;
import apkt.model.OpReturn;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("opReturnRequest")
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
            OpReturn opReturn =  opReturnJson.getOpReturn();
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            List<OpReturn> opReturnList = ServiceDaoJpa.getObjList(
                    em, 
                    OpReturn.class, 
                    null, 
                    null, 
                    OpReturn.OpReturnStatus.OP_RETURN_STATUS_INVALID_DATA, 
                    "status");
            
            OpReturn opReturnUpdate = (OpReturn) opReturnList.get(opReturnList.size() - 1);
            opReturnUpdate.setText(opReturn.getText());
            opReturnUpdate.setDateOpReturn(new Date());
            opReturnUpdate.setStatus(OpReturn.OpReturnStatus.OP_RETURN_STATUS_WAITING_TX);
            opReturnUpdate.setType(opReturn.getType());
            opReturnUpdate.setEmail(opReturn.getEmail());
            opReturnUpdate.setFee(OpReturn.OpReturnFee.FEE);
            opReturnUpdate.setLoginId(opReturn.getLoginId());
            opReturnUpdate.setLang(opReturn.getLang());
            
            GenericDaoJpa.update(em, OpReturn.class, opReturnUpdate);
            
            em.close(); emf.close();
            
            opReturnUpdate.setText(null);
            opReturnUpdate.setDateOpReturn(null);
            opReturnUpdate.setStatus(null);
            String gson = new Gson().toJson(opReturnUpdate);        
            return gson;
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName(), new Date().toString() + " " + ex.toString());
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
}
