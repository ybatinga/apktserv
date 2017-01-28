package apkt.blockcypher.service;

import apkt.model.Address;
import apkt.model.AddressKeychain;
import apkt.service.HttpService;
import apkt.utils.EndpointConfig;
import com.blockcypher.exception.BlockCypherException;
import com.blockcypher.utils.rest.RestUtils;

import java.io.IOException;

/**
 * Address service to give create/read access to address details
 * @author <a href="mailto:seb.auvray@gmail.com">Sebastien Auvray</a>
 */
public final class AddressService extends AbstractService {

    private static final String ABSOLUTE_PATH = "/{0}/{1}/{2}/addrs";

    private AddressService(EndpointConfig endpointConfig) {
        super(endpointConfig);
    }

    @Override
    protected String getAbsolutePath() {
        return ABSOLUTE_PATH;
    }

    /**
     * Get Address details
     * @param address
     * @return
     * @throws IOException
     */
    public Object getAddress(String address) throws BlockCypherException, IOException {
        return HttpService.getHttps(RestUtils.formatUrl(resourceUrl + "/{3}/full?includeHex=true", endpointConfig, address), Object.class);
    }
    
    /**
     * Create an address
     * @return Created Address
     * @throws BlockCypherException
     */
    public Address createAddress() throws BlockCypherException, IOException {
        return HttpService.postHttps(RestUtils.formatUrl(resourceUrl, endpointConfig, null), null, Address.class);
    }
    
    public AddressKeychain createAddressMultisig(String gsonString) throws BlockCypherException, IOException {
        return HttpService.postHttps(RestUtils.formatUrl(resourceUrl, endpointConfig, null), gsonString, AddressKeychain.class);
    }

}
