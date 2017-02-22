package apkt.test;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.WebhookService;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListWebhookIdJson;
import apkt.model.Address;
import apkt.model.Event;
import apkt.service.HttpService;
import apkt.service.ProjService;
import apkt.utils.BlockCypherConstants;
import com.blockcypher.exception.BlockCypherException;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DeleteWebhook {
    public static void main(String[] args) throws Exception {
        try {
            BlockCypherContext blockCypherContext = new BlockCypherContext(
                BlockCypherConstants.VERSION_V1,
                BlockCypherConstants.CURRENCY_BTC,
                BlockCypherConstants.NETWORK);
            
            WebhookService webhookService = blockCypherContext.getWebhookService();
//            Event event = webhookService.createWebHook(Event.EventType.DOUBLE_SPEND_TX, "C5zH2a3hrwbLH8dSzZqXtZbgQWXfYDVg8z", ProjService.URL.concat("webhook"));
            List o = HttpService.getHttps(BlockCypherConstants.BLOCK_CYPHER_ENDPOINT 
                    + "/" + BlockCypherConstants.VERSION_V1 
                    + "/" + BlockCypherConstants.CURRENCY_BTC
                    + "/" + BlockCypherConstants.NETWORK
                    + "/hooks?token="
                    + ProjService.BLOCKCYPHERTOKEN, List.class);
            List<ListWebhookIdJson.WebhookId> webhookIdList = (List<ListWebhookIdJson.WebhookId>) o;
            for (int i = 0; i < o.size(); i++){
                LinkedTreeMap<String, String> sm = (LinkedTreeMap<String, String>) o.get(i);
                String s = sm.get("id");
                webhookService.deleteWebhook(s.concat("?token="+ProjService.BLOCKCYPHERTOKEN));
            }
            
            String test = "test";
        } catch (BlockCypherException ex) {
            Logger.getLogger(DeleteWebhook.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DeleteWebhook.class.getName()).log(Level.SEVERE, null, ex);
        }
        
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
//        EntityManager em = emf.createEntityManager();
//        Address address = null;
//        try {
//            address = GenericDaoJpa.findByAttributeTwo(em, Address.class, "address", "DWMYgKNTupbHzuhkD6fxaP7MkpK2yzHStk", "type", Address.AddressType.ADDRESS_TYPE_MULTISIG);
//        } catch (Exception ex) {
//            Logger.getLogger(DeleteWebhook.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        ListWebhookIdJson listWebhookIdJson = new Gson().fromJson(address.getWebhookId(), ListWebhookIdJson.class);
////        ListWebhookIdJson listWebhookIdJson = new ListWebhookIdJson();
//        listWebhookIdJson.add("1", Event.EventType.TX_CONFIRMATION);
//        listWebhookIdJson.add("3", "4");
//      
//        for (ListWebhookIdJson.WebhookId w : listWebhookIdJson.getWebhookIdList()){
//            if (w.getEventType().equals(Event.EventType.TX_CONFIRMATION)){
//                String test = "";
//            }
//        }
        
//        String gson = new Gson().toJson(webhookIdList);
//        
//        ListWebhookIdJson webhookIdList1 = new Gson().fromJson(gson, ListWebhookIdJson.class);
//        String test = "d";
        
        
    }
}
