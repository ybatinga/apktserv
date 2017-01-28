package apkt.test;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public class AddressGenTest {

    public static void main(String[] args) {
        
        try {
            CloseableHttpClient httpClient = HttpClientBuilder.create().build();
            String postUrl = "https://api.blockcypher.com/v1/bcy/test/addrs";// put in your url
            Gson gson = new Gson();
            HttpPost post = new HttpPost(postUrl);
    //			StringEntity  postingString =new StringEntity(null);//convert your pojo to   json
    //			post.setEntity(postingString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
            String json = EntityUtils.toString(response.getEntity());
            httpClient.close();
        
        } catch (IOException ex) {
            Logger.getLogger(AddressGenTest.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
    }
}
