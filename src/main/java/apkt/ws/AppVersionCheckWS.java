/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.StringResultJson;
import apkt.model.AppVersion;
import com.google.gson.Gson;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("appVersionCheck")
public class AppVersionCheckWS {

    public AppVersionCheckWS() {
    }
    
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        AppVersion appVersionRes = null;
        try{
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");    
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            AppVersion appVersion = new Gson().fromJson(jsonclass, AppVersion.class);
            appVersionRes = (AppVersion) GenericDaoJpa.findByAttribute(
                        em, 
                        AppVersion.class,
                        "appName",
                        appVersion.getAppName());

        } catch (Exception e){
            
        }
        return new Gson().toJson(appVersionRes);
    }
    
    public static void main(String[] args) {
        try {

        
        StringResultJson stringResultJson = new StringResultJson();
                stringResultJson.setResult("test");
                String gson = new Gson().toJson(stringResultJson);
        String url_ = "http://localhost:8080/webresources/".concat("appVersionCheck");
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
