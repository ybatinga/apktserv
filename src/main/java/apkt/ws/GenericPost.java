package apkt.ws;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("genericPost")
public class GenericPost {

    public GenericPost() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(GenericPost.class.getName()).log(Level.SEVERE, null, ex);
        }
        return jsonclass;        
    }

    public static void main(String[] args) {
        try {
            
            
        } catch (Exception exception) {
            exception.printStackTrace(); // show exception details

        }
    }
}
