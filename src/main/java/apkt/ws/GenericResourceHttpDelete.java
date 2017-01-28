package apkt.ws;

import apkt.model.StringTest;
import apkt.test.AddressGenTest;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

@Path("genericResourceHttpDelete")
public class GenericResourceHttpDelete {

    @Context
    private UriInfo context;

    public GenericResourceHttpDelete() {
    }

    @GET
    @Path("/{jsonclass}")
    @Produces("application/json")
    public String getJson(@PathParam("jsonclass") String jsonclass) {
        String json = null;
        try {
            
            

            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String postUrl = "https://api.blockcypher.com/v1/bcy/test/hooks/522f0b27-4171-4315-ad8c-228f742def26?token=b003f23b0c8cd2ec9424aa299f330e53";// put in your url
            
            HttpDelete HttpDelete = new HttpDelete(postUrl);
             

        HttpResponse response = httpClient.execute(HttpDelete);
        String test = "test";
            
//            Gson gson = new Gson();
//            HttpPost post = new HttpPost(postUrl);
//            //			StringEntity  postingString =new StringEntity(null);//convert your pojo to   json
//            //			post.setEntity(postingString);
//            post.setHeader("Content-type", "application/json");
//            HttpResponse response = httpClient.execute(post);
//            json = EntityUtils.toString(response.getEntity());
//            httpClient.close();

        } catch (IOException ex) {
            Logger.getLogger(AddressGenTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        return json;
    }
    
    public static void main(String[] args) {
        try {

            StringTest stringTest =
                    new StringTest();
            
            stringTest.setTest("test");

            String gson = new Gson().toJson(stringTest);

            gson = gson.replace(" ", "%20");
            gson = gson.replace("\"", "%22");
            gson = gson.replace("{", "%7B");
            gson = gson.replace("}", "%7D");

            String url = "http://localhost:8080/apekatoserv/webresources/"
                    + "generic/"
                    + gson;

            InputStreamReader reader =
                    new InputStreamReader(new URL(url).openStream());

            // parse the JSON back into a TextMessage
            String stringTest_2 =
                    new Gson().fromJson(reader, String.class);

            System.out.println(
                    "LoginResult: " + stringTest_2.toString());
            
//            String url = "https://api.blockcypher.com/v1/bcy/test/hooks/"
//                + "d37e8b8d-f7ff-4334-8645-c4348ad70b98?token=b003f23b0c8cd2ec9424aa299f330e53";
//            try {            
//                JerseyRestUtils.delete(url, null, null);
//            } catch (BlockCypherException ex) {
//                Logger.getLogger(GenericResource.class.getName()).log(Level.SEVERE, null, ex);
//            }
            
            

        } catch (Exception exception) {
            exception.printStackTrace(); // show exception details

        }
    }
}

