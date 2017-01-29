package apkt.test;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.AddressService;
import apkt.json.ListWebhookIdJson;
import apkt.model.Address;
import apkt.service.HttpService;
import apkt.service.ProjService;
import com.blockcypher.exception.BlockCypherException;
import apkt.utils.BlockCypherConstants;
import apkt.utils.CalcUtils;
import com.google.gson.internal.LinkedTreeMap;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddressGetDetails {
    public static void main(String[] args) throws Exception {
        
        try {
            BlockCypherContext blockCypherContext = new BlockCypherContext(
                    BlockCypherConstants.VERSION_V1,
                    BlockCypherConstants.CURRENCY_BTC,
                    BlockCypherConstants.NETWORK);
            
            AddressService addressService = blockCypherContext.getAddressService();
            LinkedTreeMap<String, String> t = (LinkedTreeMap<String, String>) addressService.getAddress("Dp63RPqvtBpG6Mkywbj2q3697RPVFkMv5K");
            Object o = t.get("final_balance");
            Double d = (Double) o;
            Long l = new Long("440000");
            BigDecimal b = new BigDecimal(d).subtract(new BigDecimal(l));
            
            
            String s = "";
        } catch (BlockCypherException ex) {
            Logger.getLogger(AddressGetDetails.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            System.out.println(ex.toString());
        } 
        
    }
}
