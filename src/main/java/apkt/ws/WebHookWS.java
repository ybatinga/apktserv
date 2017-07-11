/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.ws;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.WebhookService;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListWebhookIdJson;
import apkt.mail.JavaMailThread;
import apkt.model.Address;
import apkt.model.Event;
import apkt.model.Login;
import apkt.model.Order;
import apkt.model.OrderPymntMthd;
import apkt.model.OrderStatus;
import apkt.model.PaymentForwardCallback;
import apkt.model.PymntMthd;
import apkt.model.StringTest;
import apkt.model.Tx;
import apkt.model.TxInput;
import apkt.model.TxOutput;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.ProjService;
import apkt.utils.BlockCypherConstants;
import apkt.utils.StringUtil;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

@Path("webhook")
public class WebHookWS {

    String xEvent;
    
    public WebHookWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getText(
            @QueryParam("language") String language, 
            @HeaderParam("X-Eventtype") String xEventType,            
//            @HeaderParam("X-EventId") String xEventId,
            String jsonclass) {

        String toJson = null;  
        Address addressMultisig = null;
        Order order = null;
        String currentStatus;
        Date date = new Date();
        xEvent = xEventType;
        String debug = null;
        OrderPymntMthd orderPymntMthd = null;
        
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            StringUtil.setLanguageResource(language);
            BlockCypherContext blockCypherContext = new BlockCypherContext(
                        BlockCypherConstants.VERSION_V1,
                        BlockCypherConstants.CURRENCY_BTC,
                        BlockCypherConstants.NETWORK);

            WebhookService webhookService = blockCypherContext.getWebhookService();

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            // "payment" event from paymentoforward gets deactivated when "tx-confirmation" is setup
            if (xEventType.equals(Event.EventType.PAYMENT)){
                PaymentForwardCallback fromJson =
                    new Gson().fromJson(jsonclass, PaymentForwardCallback.class);
                toJson = new Gson().toJson(fromJson);
            }else            
            if (xEventType.equals(Event.EventType.CONFIRMED_TX)){
                Tx txJson =
                    new Gson().fromJson(jsonclass, Tx.class);
                toJson = new Gson().toJson(txJson);
                
            }else
            if (xEventType.equals(Event.EventType.CONFIDENCE_TX)){
                
            }else
            if (xEventType.equals(Event.EventType.UNCONFIRMED_TX)){
                Tx txJson =
                    new Gson().fromJson(jsonclass, Tx.class);
                
                EntityTransaction tx = em.getTransaction();
                tx.begin();
                    addressMultisig = findMultisigAddress(em, txJson);
                    if (addressMultisig == null){
                        jsonclass = null;
                        txJson = null;
                        return null;
                    }
                    toJson = new Gson().toJson(txJson);
                    order = GenericDaoJpa.findByAttribute(em, Order.class, "id", addressMultisig.getOrderId());
                    currentStatus = order.getStatus();
                    
                    // this verification is needed because status CONFIRMED may be persisted in database
                    // before status DEPOSITED when webhook TX_CONFIRMATION is called before UNCONFIRMED_TX 
                    if (!currentStatus.equals(Order.OrderStatuses.ORDER_STATUS_TX_CONFIRMED)){
                        order.setStatus(Order.OrderStatuses.ORDER_STATUS_TX_DEPOSITED);
                        order.setUpdatedAt(date);
                        GenericDaoJpa.updateWithoutTx(em, Order.class, order);
                    }
                    
                    OrderStatus orderStatus = new OrderStatus(
                            Order.OrderStatuses.ORDER_STATUS_TX_DEPOSITED,
                            date,
                            order.getId());
                    GenericDaoJpa.insertWithoutTx(em, orderStatus);
                        
                tx.commit();
                em.close(); emf.close();                    

                ListWebhookIdJson listWebhookIdJson = new Gson().fromJson(addressMultisig.getWebhookId(), ListWebhookIdJson.class);
                for (int i = 0; i < listWebhookIdJson.getWebhookIdList().size(); i++){
                    ListWebhookIdJson.WebhookId w = listWebhookIdJson.getWebhookIdList().get(i);
                    if (w.getEventType().equals(Event.EventType.UNCONFIRMED_TX)){
                        webhookService.deleteWebhook(w.getWebhookId().concat("?token="+ProjService.BLOCKCYPHERTOKEN));
                    }
                }
                
                // send email only if status has not been updated to REQUESTED.
                // It is done that way because even when the UNCONFIRMED event has been deleted,
                // the webhook still keeps sending events, even when status is DEPOSITED or CONFIRMED.
                // Also, sometimes CONFIRMED webhooks are sent before an UNCONFIRMED events,
                // for this reason, it needs to verify if current status is REQUESTED or DEPOSITED
                if (currentStatus.equals(Order.OrderStatuses.ORDER_STATUS_TX_DEPOSITED)
                        || currentStatus.equals(Order.OrderStatuses.ORDER_STATUS_TX_REQUESTED)){
                    sendEmail(toJson, currentStatus);
                }
                
            }else
            if (xEventType.equals(Event.EventType.TX_CONFIRMATION)){
                Tx txJson =
                    new Gson().fromJson(jsonclass, Tx.class);
                
                // if number of confirmations is set to increase, also, modify code in WebhookService.java
                // for actoins in 'event.setConfirmations(new Long("1"));'
                if (txJson.getConfirmations() >= 1){                   
                
                    addressMultisig = findMultisigAddress(em, txJson);
                    if (addressMultisig == null){
                        txJson = null;
                        jsonclass = null;
                        return null;
                    }
                    toJson = new Gson().toJson(txJson);
                    if (addressMultisig != null){
                        txJson.setAddressId(addressMultisig.getId());
                        txJson.setOrderId(addressMultisig.getOrderId());
                    }
                    txJson.setEventType(xEventType);
                    txJson.setInputsJson(new Gson().toJson(txJson.getInputs()));
                    txJson.setOutputsJson(new Gson().toJson(txJson.getOutputs()));
                    
                    Tx tx  = GenericDaoJpa.insert(em, txJson);
                    order = GenericDaoJpa.findByAttribute(em, Order.class, "id", addressMultisig.getOrderId());   
                    
                    EntityTransaction et = em.getTransaction();
                    et.begin();
                        
                        for (TxInput t : tx.getInputs()){
                            t.setTxId(tx.getId());
                            t.setOrderId(order.getId());
                            t.setAddressesJson(new Gson().toJson(t.getAddresses()));
                            String res_1 = GenericDaoJpa.insertWithoutTx(em, t);
                        }
                        for (TxOutput t : tx.getOutputs()){
                            t.setTxId(tx.getId());
                            t.setOrderId(order.getId());
                            t.setAddressesJson(new Gson().toJson(t.getAddresses()));
                            String res_1 = GenericDaoJpa.insertWithoutTx(em, t);
                        }
                        
                           
                        currentStatus = order.getStatus();
                        order.setStatus(Order.OrderStatuses.ORDER_STATUS_TX_CONFIRMED);
                        order.setUpdatedAt(date);
                        GenericDaoJpa.updateWithoutTx(em, Order.class, order);
                        OrderStatus orderStatus = new OrderStatus(
                                Order.OrderStatuses.ORDER_STATUS_TX_CONFIRMED,
                                date,
                                order.getId());
                        GenericDaoJpa.insertWithoutTx(em, orderStatus);

                    et.commit();
                    
                    Login makerLogin = ServiceDaoJpa.getUser(em, order.getMakerLoginId());
                    Login takerLogin = ServiceDaoJpa.getUser(em, order.getTakerLoginId());

                    String makerUserName = makerLogin.getUsername();
                    String takerUserName = takerLogin.getUsername();
                    String makerEmail = makerLogin.getEmail();
                    String takerEmail = takerLogin.getEmail();
                    
                    em.close(); emf.close();                    

                    for (OrderPymntMthd opm : order.getOrderPymntMthdList()){
                        if (opm.isPymntMthdSelected()){
                            orderPymntMthd = opm;
                        }
                    }
                    
                    // if number of confirmations is set to increase, also, modify code in WebHookWS.java
                    // for actoins in if conditions with 'fromJson.getConfirmations()'
                    if (tx.getConfirmations() >= 1) {
                        ListWebhookIdJson listWebhookIdJson = new Gson().fromJson(addressMultisig.getWebhookId(), ListWebhookIdJson.class);
                        for (int i = 0; i < listWebhookIdJson.getWebhookIdList().size(); i++){
                            ListWebhookIdJson.WebhookId w = listWebhookIdJson.getWebhookIdList().get(i);
                            if (w.getEventType().equals(Event.EventType.TX_CONFIRMATION)){
                                webhookService.deleteWebhook(w.getWebhookId().concat("?token="+ProjService.BLOCKCYPHERTOKEN));                                
                            }
                        }
                    }
                    
                    // send email only if status has not been updated to DEPOSITED.
                    // It is done that way because even when the CONFIRMED webhook has been deleted,
                    // the webhook still keeps sending events.
                    // Also, sometimes CONFIRMED webhooks are sent before an UNCONFIRMED events,
                    // for this reason, it needs to verify if current status is REQUESTED
                    if (currentStatus.equals(Order.OrderStatuses.ORDER_STATUS_TX_DEPOSITED)
                            || currentStatus.equals(Order.OrderStatuses.ORDER_STATUS_TX_REQUESTED)){
                        if (order.getType().equals(Order.OrderType.ORDER_TYPE_BUY)){
                
                            StringUtil.setLanguageResource(makerLogin.getLang());
                            String emailSubjectMaker = ProjService.RB.getString("email_subject_order_confirmed");
                            StringBuilder emailBodyMaker = new StringBuilder();
                            emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
                            emailBodyMaker.append(makerLogin.getUsername() + ",");
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer") + " ");
                            emailBodyMaker.append(order.getAmountNetBuy());
                            emailBodyMaker.append(" " + ProjService.RB.getString("email_body_order_confirmed_buyer_multisig") + " ");
                            emailBodyMaker.append("<a href=https://blockchain.info/address/");
                            emailBodyMaker.append(addressMultisig.getAddress());
                            emailBodyMaker.append(">");
                            emailBodyMaker.append(addressMultisig.getAddress());
                            emailBodyMaker.append("</a>");
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_pymnt") + " ");
                            emailBodyMaker.append(order.getTotal() + " ");
                            emailBodyMaker.append(" " + ProjService.RB.getString("email_body_order_confirmed_buyer_accnt_info"));
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_name") + " ");
                            emailBodyMaker.append(makerLogin.getDocUsername());
                            emailBodyMaker.append("<br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_branch") + " ");
                            emailBodyMaker.append(orderPymntMthd.getBranch());
                            emailBodyMaker.append("<br></br>");
                            if (orderPymntMthd.getAccountType().equals(PymntMthd.AccountType.CONTA_CORRENTE)){
                                emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_accnt_corrente") + " ");
                            }else if (orderPymntMthd.getAccountType().equals(PymntMthd.AccountType.CONTA_POUPANCA)){
                                emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_accnt_poupanca") + " ");
                            }
                            emailBodyMaker.append(orderPymntMthd.getAccount());
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirm_pymnt"));
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_communicate_email") + " ");
                            emailBodyMaker.append(takerLogin.getEmail());
                            emailBodyMaker.append("<br></br>");
                            emailBodyMaker.append("("+takerLogin.getMobileNumState()+")");
                            emailBodyMaker.append(takerLogin.getMobileNum());
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_end"));
                            
                            StringUtil.setLanguageResource(takerLogin.getLang());
                            String emailSubjectTaker = ProjService.RB.getString("email_subject_order_confirmed");
                            StringBuilder emailBodyTaker = new StringBuilder();
                            emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
                            emailBodyTaker.append(takerLogin.getUsername() + ",");
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_seller") + " ");
                            emailBodyTaker.append(order.getAmountNetSell());
                            emailBodyTaker.append(" " + ProjService.RB.getString("email_body_order_confirmed_seller_multisig") + " ");
                            emailBodyTaker.append("<a href=https://blockchain.info/address/");
                            emailBodyTaker.append(addressMultisig.getAddress());
                            emailBodyTaker.append(">");
                            emailBodyTaker.append(addressMultisig.getAddress());
                            emailBodyTaker.append("</a>");
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_pymnt") + " ");
                            emailBodyTaker.append(order.getTotal() + " ");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_pymnt_account"));
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_wait_email") + " ");
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_confirmation") + " ");
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_release_instructions"));
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_end"));

                            JavaMailThread javaMailThread_1 = new JavaMailThread(
                                takerEmail, 
                                emailSubjectTaker, 
                                emailBodyTaker.toString());
                            JavaMailThread javaMailThread_2 = new JavaMailThread(
                                makerEmail, 
                                emailSubjectMaker,
                                emailBodyMaker.toString());
                            ExecutorService threadExecutor = Executors.newCachedThreadPool();
                            threadExecutor.execute(javaMailThread_1);
                            threadExecutor.execute(javaMailThread_2);
                            threadExecutor.shutdown();

                        } else if (order.getType().equals(Order.OrderType.ORDER_TYPE_SELL)){

                            StringUtil.setLanguageResource(makerLogin.getLang());
                            String emailSubjectMaker = ProjService.RB.getString("email_subject_order_confirmed");                            
                            StringBuilder emailBodyMaker = new StringBuilder();
                            emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
                            emailBodyMaker.append(makerLogin.getUsername() + ",");
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_seller") + " ");
                            emailBodyMaker.append(order.getAmountNetSell());
                            emailBodyMaker.append(" " + ProjService.RB.getString("email_body_order_confirmed_seller_multisig") + " ");
                            emailBodyMaker.append("<a href=https://blockchain.info/address/");
                            emailBodyMaker.append(addressMultisig.getAddress());
                            emailBodyMaker.append(">");
                            emailBodyMaker.append(addressMultisig.getAddress());
                            emailBodyMaker.append("</a>");
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_pymnt") + " ");
                            emailBodyMaker.append(order.getTotal() + " ");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_pymnt_account"));
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_wait_email") + " ");
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_confirmation") + " ");
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_order_confirmed_seller_release_instructions"));
                            emailBodyMaker.append("<br></br><br></br>");
                            emailBodyMaker.append(ProjService.RB.getString("email_body_end"));

                            StringUtil.setLanguageResource(takerLogin.getLang());
                            String emailSubjectTaker = ProjService.RB.getString("email_subject_order_confirmed");
                            StringBuilder emailBodyTaker = new StringBuilder();
                            emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
                            emailBodyTaker.append(takerLogin.getUsername() + ",");
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer") + " ");
                            emailBodyTaker.append(order.getAmountNetBuy());
                            emailBodyTaker.append(" " + ProjService.RB.getString("email_body_order_confirmed_buyer_multisig") + " ");
                            emailBodyTaker.append("<a href=https://blockchain.info/address/");
                            emailBodyTaker.append(addressMultisig.getAddress());
                            emailBodyTaker.append(">");
                            emailBodyTaker.append(addressMultisig.getAddress());
                            emailBodyTaker.append("</a>");
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_pymnt") + " ");
                            emailBodyTaker.append(order.getTotal() + " ");
                            emailBodyTaker.append(" " + ProjService.RB.getString("email_body_order_confirmed_buyer_accnt_info"));
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_name") + " ");
                            emailBodyTaker.append(makerLogin.getDocUsername());
                            emailBodyTaker.append("<br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_branch") + " ");
                            emailBodyTaker.append(orderPymntMthd.getBranch());
                            emailBodyTaker.append("<br></br>");
                            if (orderPymntMthd.getAccountType().equals(PymntMthd.AccountType.CONTA_CORRENTE)){
                                emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_accnt_corrente") + " ");
                            }else if (orderPymntMthd.getAccountType().equals(PymntMthd.AccountType.CONTA_POUPANCA)){
                                emailBodyTaker.append(ProjService.RB.getString("email_body_order_confirmed_buyer_accnt_poupanca") + " ");
                            }
                            emailBodyTaker.append(orderPymntMthd.getAccount());                            
                            emailBodyTaker.append("<br></br><br></br>");                            
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirm_pymnt"));
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_communicate_email") + " ");
                            emailBodyTaker.append(makerLogin.getEmail());
                            emailBodyTaker.append("<br></br>");
                            emailBodyTaker.append("("+makerLogin.getMobileNumState()+")");
                            emailBodyTaker.append(makerLogin.getMobileNum());
                            emailBodyTaker.append("<br></br><br></br>");
                            emailBodyTaker.append(ProjService.RB.getString("email_body_end"));
                            
                            JavaMailThread javaMailThread_1 = new JavaMailThread(
                                takerEmail, 
                                emailSubjectTaker, 
                                emailBodyTaker.toString());
                            JavaMailThread javaMailThread_2 = new JavaMailThread(
                                makerEmail, 
                                emailSubjectMaker,
                                emailBodyMaker.toString());
                            ExecutorService threadExecutor = Executors.newCachedThreadPool();
                            threadExecutor.execute(javaMailThread_1);
                            threadExecutor.execute(javaMailThread_2);
                            threadExecutor.shutdown();   
                        }
                    }
                }
            }
        } catch (Exception exception) {
            
            int lineNumber = new Throwable().getStackTrace()[0].getLineNumber();
            String concat = "xception: ".concat(exception.toString()).concat(" --- Json: ").concat(toJson);
        
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName() + xEventType, new Date().toString() + " " + concat.concat("debug: " + debug));
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown(); 
        }
        
        return null;        
    }
    
    public Address findMultisigAddress(EntityManager em, Tx tx){
        Address address = null;
        for (String s : tx.getAddresses()){
            try {  
                address = GenericDaoJpa.findByAttributeTwo(em, Address.class, "address", s, "type", Address.AddressType.ADDRESS_TYPE_MULTISIG);
            } catch (Exception ex) {

            }
        }
        return address;
    }
    
    public void sendEmail(String toJson, String stage){
//        StringBuilder emailText = new StringBuilder();
//                emailText.append("toJson: ");
//                emailText.append(toJson);
//                emailText.append("\n\n");
//                emailText.append("stage: ");
//                emailText.append(stage);
//            if (paymentForward != null) {
//                emailText.append("address: ");
//                emailText.append(paymentForward.toString());
//            }            
//            if (xception != null) {
//                emailText.append("xception: ");
//                emailText.append(xception);
//            }            
            
//            emailText.append("\n\n");
        
        String concat = stage + " --- X-EventType: ".concat(xEvent).concat(" --- Tx Json: ").concat(toJson);
        
        JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", xEvent, new Date().toString() + " " + concat);
        ExecutorService threadExecutor = Executors.newCachedThreadPool();
        threadExecutor.execute(javaMailThread_1);
        threadExecutor.shutdown();
    }

    public static void main(String[] args) {
        try {

            StringTest stringTest =
                    new StringTest();
            
            stringTest.setTest("test");

            String gson = new Gson().toJson(stringTest);

            gson = gson.replace(" ", "%20");
            gson = gson.replace("\"", "%22");
            gson = gson.replace("{", "%7B");
            gson = gson.replace("}", "%7D");

            String url = "http://localhost:8080/apekatoserv/webresources/"
                    + "generic/"
                    + gson;

            InputStreamReader reader =
                    new InputStreamReader(new URL(url).openStream());

            // parse the JSON back into a TextMessage
            String stringTest_2 =
                    new Gson().fromJson(reader, String.class);

            System.out.println(
                    "LoginResult: " + stringTest_2.toString());

        } catch (Exception exception) {
            exception.printStackTrace(); // show exception details

        }
    }
}
