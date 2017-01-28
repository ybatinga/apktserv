package apkt.test;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class TxModelTest {

    public static void main(String[] args) {

        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String postUrl = "https://api.blockcypher.com/v1/bcy/test/faucet?token=" + "15cec3f0a8754248af469151f249b5d3";
            Gson gson = new Gson();
            HttpPost post = new HttpPost(postUrl);
            JsonObject object = new JsonObject();
            String address =
"BsRpS2WjXVBNTS6rQM1EY9vh9hkZGLRboa"
                    ;
                    object.addProperty("address", address);
            object.addProperty("amount", 5075000);
            StringEntity postingString = new StringEntity(object.toString());//convert your pojo to   json
            post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            String json = EntityUtils.toString(response.getEntity());
            httpClient.close();

        } catch (IOException ex) {
            Logger.getLogger(AddressGenTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

    }
    
}
