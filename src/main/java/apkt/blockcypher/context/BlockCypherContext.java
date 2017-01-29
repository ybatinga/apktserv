package apkt.blockcypher.context;

import apkt.blockcypher.service.AddressService;
import apkt.blockcypher.service.BlockChainService;
import apkt.blockcypher.service.InfoService;
import apkt.blockcypher.service.PaymentForwardService;
import apkt.blockcypher.service.TransactionService;
import apkt.blockcypher.service.WebhookService;
import apkt.utils.EndpointConfig;

import java.lang.reflect.Constructor;
import java.text.MessageFormat;

/**
 * BlockCypher Context holds the following services:
 * - addressService
 * - blockChainService
 * - transactionService
 * - webhookService
 * - infoService
 * @author <a href="mailto:seb.auvray@gmail.com">Sebastien Auvray</a>
 */
public final class BlockCypherContext {


    private AddressService addressService;
    private BlockChainService blockChainService;
    private TransactionService transactionService;
    private WebhookService webhookService;
    private InfoService infoService;
    private PaymentForwardService paymentForwardService;
    private EndpointConfig endpointConfig;
    

    /**
     * Constructor. If you do not provide a version currency and network it will be read from blockcypher.endpoint.properties in classpath
     */
    public BlockCypherContext() throws Exception {
        endpointConfig = new EndpointConfig();
        if (!endpointConfig.isValid()) {
            throw new RuntimeException("Creating BlockCypherContext() but you did not provide:" + EndpointConfig.PROPERTY_FILE);
        }
        createServices(endpointConfig);
    }

    /**
     * Constructor.
     * @param version API version, ie: v1
     * @param currency currency, ie: btc (bitcoin), ltc (lightcoin), uro (urocoin)
     * @param network network, ie: main, test, test3
     */
    public BlockCypherContext(String version, String currency, String network) throws Exception {
        endpointConfig = new EndpointConfig(version, currency, network);
        createServices(endpointConfig);
    }
    
        /**
     * Constructor.
     * @param version API version, ie: v1
     * @param currency currency, ie: btc (bitcoin), ltc (lightcoin), uro (urocoin)
     * @param network network, ie: main, test, test3
		 * @param token token, ie: YOURTOKEN
     */
    public BlockCypherContext(String version, String currency, String network, String token) throws Exception {
        endpointConfig = new EndpointConfig(version, currency, network, token);
        createServices(endpointConfig);
    }
    

    private void createServices(EndpointConfig endpointConfig) throws Exception {
            this.addressService = this.getPrivateConstructor(AddressService.class).newInstance(endpointConfig);
            this.blockChainService = this.getPrivateConstructor(BlockChainService.class).newInstance(endpointConfig);
            this.transactionService = this.getPrivateConstructor(TransactionService.class).newInstance(endpointConfig);
            this.webhookService = this.getPrivateConstructor(WebhookService.class).newInstance(endpointConfig);
            this.infoService = this.getPrivateConstructor(InfoService.class).newInstance(endpointConfig);
            this.paymentForwardService = this.getPrivateConstructor(PaymentForwardService.class).newInstance(endpointConfig);
    }

    private <T> Constructor<T> getPrivateConstructor(final Class<T> clazz) throws Exception {
        //Constructor<T> declaredConstructor = clazz.getDeclaredConstructor(String.class, String.class, String.class);
        Constructor<T> declaredConstructor = clazz.getDeclaredConstructor(EndpointConfig.class);
        declaredConstructor.setAccessible(true);
        return declaredConstructor;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public BlockChainService getBlockChainService() {
        return blockChainService;
    }

    public TransactionService getTransactionService() {
        return transactionService;
    }

    public WebhookService getWebhookService() {
        return webhookService;
    }

    public InfoService getInfoService() {
        return infoService;
    }

    public PaymentForwardService getPaymentForwardService() {
        return paymentForwardService;
    }
    
    public EndpointConfig getEndpointConfig() {
        return endpointConfig;
    }

}
