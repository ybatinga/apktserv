package apkt.blockcypher.service;

import apkt.model.PaymentForward;
import apkt.service.HttpService;
import apkt.utils.EndpointConfig;
import com.blockcypher.exception.BlockCypherException;
import com.blockcypher.utils.rest.RestUtils;
import java.io.IOException;

public class PaymentForwardService extends AbstractService {

    private static final String ABSOLUTE_PATH = "/{0}/{1}/{2}/payments";

    private PaymentForwardService(EndpointConfig endpointConfig) {
        super(endpointConfig);
    }

    @Override
    protected String getAbsolutePath() {
        return ABSOLUTE_PATH;
    }
    
    public PaymentForward createPayment(String gsonString) throws BlockCypherException, IOException {
        return HttpService.postHttps(RestUtils.formatUrl(resourceUrl, endpointConfig, null), gsonString, PaymentForward.class);
    }
    
    public void deletePayment(String id) throws BlockCypherException, IOException {
        HttpService.deleteHttps(RestUtils.formatUrl(resourceUrl + "/{3}", endpointConfig, id));
    }
}
