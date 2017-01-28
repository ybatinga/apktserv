package apkt.test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MediaType;

public class SendMailGunTest {

    public static void main(String[] args) {
        
        
        SendSimpleMessage();
    }
    
    public static ClientResponse SendSimpleMessage() {
        Client client = Client.create();
        client.addFilter(new HTTPBasicAuthFilter("api",
                    "key-96346434c4868eb3c6dd514a49a5d769"));
        WebResource webResource =
            client.resource("https://api.mailgun.net/v3/apekato.com/messages");
        MultivaluedMapImpl formData = new MultivaluedMapImpl();
        formData.add("from", "Apekato<msg@apekato.com>");
        formData.add("to", "Apekato <eltonsasaki@gmail.com>");
        formData.add("subject", "Hello Apekato");
        formData.add("html", "<b>Congratulations</b> Apekato, you just sent an email with Mailgun!  You are truly awesome!  You can see a record of this email in your logs: https://mailgun.com/cp/log .  You can send up to 300 emails/day from this sandbox server.  Next, you should add your own domain so you can send 10,000 emails/month for free.");
        return webResource.type(MediaType.APPLICATION_FORM_URLENCODED).
                                                    post(ClientResponse.class, formData);
    }
}
