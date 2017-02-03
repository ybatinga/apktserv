package apkt.mail;

import apkt.service.StringVarsService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;

public class JavaMailGun {
    
    public static String send(String TO, String SUBJECT, String TEXT) {
        ClientResponse clientResponse;
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api",
                    "key-96346434c4868eb3c6dd514a49a5d769"));
        WebResource webResource =
            client.resource("https://api.mailgun.net/v3/apekato.com/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", "Apekato<msg@apekato.com>");
        formData.add("to", TO);
        formData.add("subject", SUBJECT);
        formData.add("html", TEXT);
        
        clientResponse = webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                                                    post(ClientResponse.class, formData);
        return StringVarsService.OK;

    }
}
