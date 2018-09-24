/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.ws;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author ubuntu
 */
@Path("genericGetBasic")
public class GenericGetBasic {

    /**
     * Creates a new instance of GenericGet
     */
    public GenericGetBasic() {
    }

    /**
     * Retrieves representation of an instance of chain.GenericGet
     * @return an instance of java.lang.String
     */
    @GET
    @Path("/{jsonclass}")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("jsonclass") String jsonclass) {   
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GenericGetBasic.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    public static void main(String[] args) {
        try {

        String url_ = "https://localhost/webresources/".concat("genericGetBasic").concat("/").concat("test");

        URL url = new URL(url_);
        HttpURLConnection urlConnection = (HttpURLConnection)url.openConnection();

        InputStream in = urlConnection.getInputStream();
        InputStreamReader reader =
                new InputStreamReader(in);
        
        String test = null;
            
        } catch (IOException ex) {
            Logger.getLogger(GenericGetBasic.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
