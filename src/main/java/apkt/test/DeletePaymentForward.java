package apkt.test;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.PaymentForwardService;
import apkt.model.PaymentForward;
import apkt.service.HttpService;
import apkt.service.ProjService;
import apkt.utils.BlockCypherConstants;
import com.blockcypher.exception.BlockCypherException;
import com.google.gson.internal.LinkedTreeMap;
import java.io.IOException;
import java.util.List;

public class DeletePaymentForward {
    public static void main(String[] args) throws BlockCypherException {
        try {
            BlockCypherContext blockCypherContext = new BlockCypherContext(
                    BlockCypherConstants.VERSION_V1,
                    BlockCypherConstants.CURRENCY_BTC,
                    BlockCypherConstants.NETWORK);
            
            PaymentForwardService paymentForwardService = blockCypherContext.getPaymentForwardService();
            
            List o = HttpService.getHttps("https://api.blockcypher.com/v1/bcy/test/payments?token="+ProjService.BLOCKCYPHERTOKEN, List.class);
            
            for (int i = 0; i < o.size(); i++){
                LinkedTreeMap<String, String> sm = (LinkedTreeMap<String, String>) o.get(i);
                String s = sm.get("id");
                paymentForwardService.deletePayment(s);
            }
            
            String test = "test";
        } catch (IOException ex) {
        
        }
    }
}
