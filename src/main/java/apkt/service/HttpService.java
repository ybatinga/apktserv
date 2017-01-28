package apkt.service;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.Class;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class HttpService {
    
    public static <T> T postHttps(String postUrl, String gsonString, Class<T> clazz) throws IOException{
        URL url = new URL(postUrl);
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();

        urlConnection.setDoOutput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json");

        OutputStream os = urlConnection.getOutputStream();
        if (gsonString != null){
            os.write(gsonString.getBytes());
        }            
        os.flush();

        InputStream in = urlConnection.getInputStream();
        InputStreamReader reader =
                new InputStreamReader(in);

        T returnedObject =
                    new Gson().fromJson(reader, clazz);

        return returnedObject;
        
    }
    
    public static <T> T getHttps(String getUrl, Class<T> clazz) throws IOException{
        
        InputStreamReader reader =
                    new InputStreamReader(new URL(getUrl).openStream());

        // parse the JSON back into a TextMessage
        T returnedObject =
                new Gson().fromJson(reader, clazz);
        
        return returnedObject;
    }
       
    public static void deleteHttps(String getUrl) throws IOException{
        
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        HttpDelete HttpDelete = new HttpDelete(getUrl);
        HttpResponse response = httpClient.execute(HttpDelete);                
    }
}
