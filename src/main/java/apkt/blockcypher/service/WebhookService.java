package apkt.blockcypher.service;

import apkt.model.Event;
import apkt.service.HttpService;
import apkt.service.ProjService;
import apkt.utils.EndpointConfig;
import com.blockcypher.exception.BlockCypherException;
import com.blockcypher.model.webhook.Webhook;
import com.blockcypher.utils.rest.RestUtils;
import com.google.gson.Gson;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service which provides create/read access to a Webhook
 * @author <a href="mailto:seb.auvray@gmail.com">Sebastien Auvray</a>
 */
public final class WebhookService extends AbstractService {

    private static final String ABSOLUTE_PATH = "/{0}/{1}/{2}/hooks";

    private WebhookService(EndpointConfig endpointConfig) {
        super(endpointConfig);
    }

    @Override
    protected String getAbsolutePath() {
        return ABSOLUTE_PATH;
    }

    /**
     * Create a Webhook
     * @param registeredUrl url of the Webhook (ie http://gghhii.ngrok.com:80) This url will receiveall the events from BlockCypher
     * @param filter Events filters
     * @param token token
     * @return Webhook created
     * @throws BlockCypherException
     */
    public Event createWebHook(String eventType, String address, String url) throws BlockCypherException, IOException {
        Event event = new Event();        
        event.setAddress(address);
        event.setToken(ProjService.BLOCKCYPHERTOKEN);
        event.setEvent(eventType);
        event.setUrl(url);
        // if number of confirmations is set to increase, also, modify code in WebHookWS.java
        // for actoins in if conditions with 'fromJson.getConfirmations()'
        event.setConfirmations(new Long("1"));
        String eventJson = new Gson().toJson(event);            
        return HttpService.postHttps(RestUtils.formatUrl(resourceUrl, endpointConfig, null), eventJson, Event.class);
    }

    /**
     * Get an existing Webhook
     * @param id
     * @return
     * @throws BlockCypherException
     */
    public Webhook getWebHook(String id) throws BlockCypherException, IOException {
        return HttpService.getHttps(RestUtils.formatUrl(resourceUrl + "/{3}", endpointConfig, id), Webhook.class);        
    }

    /**
     * Delete an existing Webhook
     * @param id
     * @throws BlockCypherException
     */
    public void deleteWebhook(String id) throws BlockCypherException, IOException {
        HttpService.deleteHttps(RestUtils.formatUrl(resourceUrl + "/{3}", endpointConfig, id));
    }

}
