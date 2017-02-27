package apkt.ws;

import apkt.mail.JavaMailThread;
import apkt.service.ProjService;
import apkt.service.StringVarsService;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.core.MediaType;

@Path("opReturnRequestWS")
public class OpReturnRequestWS {

    public OpReturnRequestWS() {
    }

    
    @GET
    @Path("/{jsonclass}")
    @Produces("application/json")
    public String getJson(String jsonclass) {
//    @POST    
//    @Consumes("application/json")
//    @Produces("application/json")
//    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
//            List<Order> list = ServiceDaoJpa.getOrderList(em, listOrderJson);
            
            em.close(); emf.close();
            
//            listOrderJson.setOrderList(list);
            
            return StringVarsService.OK;
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

        String url_ = "http://localhost:8080/webresources/".concat("opReturnRequestWS").concat("/").concat("test");

        URL url = new URL(url_);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        InputStream in = urlConnection.getInputStream();
        InputStreamReader reader =
                new InputStreamReader(in);

        String test = null;
            
        } catch (Exception ex) {
            System.out.println(ex);
        } 
    }

}
