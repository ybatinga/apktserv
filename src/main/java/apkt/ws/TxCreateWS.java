package apkt.ws;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.AddressService;
import apkt.blockcypher.service.WebhookService;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListWebhookIdJson;
import apkt.json.OrderJson;
import apkt.json.StringResultJson;
import apkt.mail.JavaMailThread;
import apkt.model.Address;
import apkt.model.AddressKeychain;
import apkt.model.Event;
import apkt.model.Login;
import apkt.model.Order;
import apkt.model.OrderPymntMthd;
import apkt.model.OrderStatus;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.ProjService;
import apkt.service.StringVarsService;
import apkt.utils.BlockCypherConstants;
import apkt.utils.StringUtil;
import com.blockcypher.model.transaction.TransactionConstants;
import com.google.gson.Gson;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("txCreate")
public class TxCreateWS {

    public TxCreateWS() {
    }

//    @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(@PathParam("jsonclass") String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getText(String jsonclass) throws IOException {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");

            OrderJson orderJson = new Gson().fromJson(jsonclass, OrderJson.class);
            Order order = orderJson.getOrder();
            Date date = new Date();
            String language = orderJson.getAuthAux().getLanguage();
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            Login loginAuth = ServiceDaoJpa.authUser(em, orderJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            orderJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }

            AddressService addressService;
    //        PaymentForwardService paymentForwardService;
            WebhookService webhookService;
            Address addressMaker;
            Address addressTaker;
            Address addressEscrow;
            Address addressMultisig;
            AddressKeychain addressKeychain = new AddressKeychain();              
            AddressKeychain addressKeychainRes;
            String addressKeychainJson;
    //        PaymentForward paymentForward = new PaymentForward();
    //        String paymentForwardJson;
    //        String paymentForwardReturnJson;
    //        PaymentForward paymentForwardRes;
            Event event;
            OrderPymntMthd orderPymntMthd = null;
            ListWebhookIdJson listWebhookIdJson = new ListWebhookIdJson();
    
            BlockCypherContext blockCypherContext = new BlockCypherContext(
                    BlockCypherConstants.VERSION_V1,
                    BlockCypherConstants.CURRENCY_BTC,
                    BlockCypherConstants.NETWORK);

            addressService = blockCypherContext.getAddressService();
//            paymentForwardService = blockCypherContext.getPaymentForwardService();
            webhookService = blockCypherContext.getWebhookService();
            addressMaker = addressService.createAddress();                                
            Thread.sleep(1000);
            addressTaker = addressService.createAddress();
            Thread.sleep(1000);
            addressEscrow = addressService.createAddress();
            Thread.sleep(1000);
            
            addressKeychain.addPubkeys(addressMaker.getPublicAddress());
            addressKeychain.addPubkeys(addressTaker.getPublicAddress());
            addressKeychain.addPubkeys(addressEscrow.getPublicAddress());
            addressKeychain.setScriptType(TransactionConstants.SCRIPT_TYPE_MULTI_SIG_2_OF_3);

            addressKeychainRes = addressService.createAddressMultisig(new Gson().toJson(addressKeychain));           
            Thread.sleep(1000);
            addressMultisig = new Address(addressKeychainRes);            

//            paymentForward.setDestination(addressKeychainRes.getAddress());
//            paymentForward.setProcessFeesAddress(ProjService.ADDRESS);
//            paymentForward.setProcessFeesPercent(new BigDecimal(String.valueOf((new BigDecimal(StaticVars.FEE_ORDER_MAKER).add(new BigDecimal(StaticVars.FEE_ORDER_TAKER)).divide(new BigDecimal("100"))))));
//            paymentForward.setMiningFeesSatoshis(StaticVars.MINING_FEES_SATOSHIS);
//            paymentForward.setToken(ProjService.BLOCKCYPHERTOKEN);
//            paymentForward.setEnableConfirmations(false);
//            paymentForwardJson = paymentForward.toString();            
//            paymentForwardRes = paymentForwardService.createPayment(paymentForwardJson);            
//            Thread.sleep(1000);
            
//            event = webhookService.createWebHook(Event.EventType.UNCONFIRMED_TX, addressMultisig.getAddress(), ProjService.URL.concat("webhook").concat("?secret=test"));
//            listWebhookIdJson.add(event.getId(), Event.EventType.UNCONFIRMED_TX);
//            Thread.sleep(1000);
//            event = webhookService.createWebHook(Event.EventType.CONFIDENCE_TX, addressMultisig.getAddress(), ProjService.URL.concat("webhook"));
            event = webhookService.createWebHook(Event.EventType.TX_CONFIRMATION, addressMultisig.getAddress(), ProjService.URL.concat("webhook").concat("?language=" + language));
            listWebhookIdJson.add(event.getId(), Event.EventType.TX_CONFIRMATION);
            Thread.sleep(1000);

            String gson = new Gson().toJson(listWebhookIdJson);
        
            addressMaker.setOrderId(order.getId());
            addressTaker.setOrderId(order.getId());
            addressEscrow.setOrderId(order.getId());
            addressMultisig.setOrderId(order.getId());
//            paymentForwardRes.setOrderId(order.getId());
            addressMaker.setType(Address.AddressType.ADDRESS_TYPE_MAKER);            
            addressTaker.setType(Address.AddressType.ADDRESS_TYPE_TAKER);
            addressEscrow.setType(Address.AddressType.ADDRESS_TYPE_ARBITRATOR);
            addressMultisig.setType(Address.AddressType.ADDRESS_TYPE_MULTISIG);      
            addressMultisig.setWebhookId(gson);
//            paymentForwardRes.setWebhookId(event.getId());
            
            EntityTransaction tx = em.getTransaction();
            tx.begin();
                String result_1 = GenericDaoJpa.insertWithoutTx(em, addressMaker);
                String result_2 = GenericDaoJpa.insertWithoutTx(em, addressTaker);
                String result_3 = GenericDaoJpa.insertWithoutTx(em, addressEscrow);
                String result_4 = GenericDaoJpa.insertWithoutTx(em, addressMultisig);
//                String result_5 = GenericDaoJpa.insertWithoutTx(em, paymentForwardRes);
                // orderId for OrderWallet object must be set since there is a reference for it in order_wallet
                order.getOrderWalletId().setOrderId(order.getId());
                order.setEscrowAddress(addressMultisig.getAddress());
                
                order.setUpdatedAt(date);
                String result_6 = GenericDaoJpa.updateWithoutTx(em, Order.class, order);
                
                for (OrderPymntMthd opm : order.getOrderPymntMthdList()){
                    if (opm.isPymntMthdSelected()){
                        orderPymntMthd = opm;
                        orderPymntMthd.setOrderId(order);
                    }
                }
                String result_7 = GenericDaoJpa.updateWithoutTx(em, OrderPymntMthd.class, orderPymntMthd);

                OrderStatus orderStatus = new OrderStatus(
                        order.getStatus(),
                        date,
                        order.getId());
                GenericDaoJpa.insertWithoutTx(em, orderStatus);
            
            tx.commit();
            Login makerLogin = ServiceDaoJpa.getUser(em, order.getMakerLoginId());
            Login takerLogin = ServiceDaoJpa.getUser(em, order.getTakerLoginId());
                
//            paymentForwardService.deletePayment(paymentForward.getId());
            em.close(); emf.close();
            
            String makerUserName = makerLogin.getUsername();
            String takerUserName = takerLogin.getUsername();
            String makerEmail = makerLogin.getEmail();
            String takerEmail = takerLogin.getEmail();
            
            if (order.getType().equals(Order.OrderType.ORDER_TYPE_BUY)){
                StringUtil.setLanguageResource(makerLogin.getLang());
                String emailSubjectMaker = ProjService.RB.getString("email_subject_order_created_buy_maker");
                StringBuilder emailBodyMaker = new StringBuilder();
                emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyMaker.append(makerLogin.getUsername() + ",");
                
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyMaker.append("<br></br><br></br>");       
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyMaker.append(ProjService.RB.getString("email_boby_testnet_bitcoin_msg"));
                
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_pymnt_volume") + " ");
                emailBodyMaker.append(order.getAmountNetBuy() + " ");
                emailBodyMaker.append(" " + ProjService.RB.getString("email_body_order_created_buy_maker_pymnt_wallet") + " ");
                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
                    emailBodyMaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
                    emailBodyMaker.append("<a href=https://www.blocktrail.com/BTC/address/");
                }
                emailBodyMaker.append(addressMultisig.getAddress());
                emailBodyMaker.append(">");
                emailBodyMaker.append(addressMultisig.getAddress());
                emailBodyMaker.append("</a>");
                emailBodyMaker.append("<br></br><br></br>");
                if (order.getCurrencyCode().equals(StringVarsService.CURRENCY_CODE_BRL)){
                    emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_confirmation_brl") + " ");
                } else {
                    emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_confirmation") + " ");                    
                }
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_confirm_pymnt") + " ");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_communicate_email") + " ");
                emailBodyMaker.append(takerLogin.getEmail());
                emailBodyMaker.append("<br></br>");
                emailBodyMaker.append("("+takerLogin.getMobileNumState()+")");
                emailBodyMaker.append(takerLogin.getMobileNum());
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_end"));
                
                StringUtil.setLanguageResource(takerLogin.getLang());
                String emailSubjectTaker = ProjService.RB.getString("email_subject_order_created_buy_taker");
                StringBuilder emailBodyTaker = new StringBuilder();
                emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyTaker.append(takerLogin.getUsername() + ",");
                
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyTaker.append("<br></br><br></br>");       
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyTaker.append(ProjService.RB.getString("email_boby_testnet_bitcoin_msg"));
                
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_send_exactly") + " ");
                emailBodyTaker.append(order.getAmountNetSell() + " ");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_send_quantity_to_wallet") + " ");              
                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
                    emailBodyTaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
                    emailBodyTaker.append("<a href=https://www.blocktrail.com/BTC/address/");
                }
                emailBodyTaker.append(addressMultisig.getAddress());
                emailBodyTaker.append(">");
                emailBodyTaker.append(addressMultisig.getAddress());
                emailBodyTaker.append("</a>");
                emailBodyTaker.append("<br></br><br></br>");
                if (order.getCurrencyCode().equals(StringVarsService.CURRENCY_CODE_BRL)){
                    emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_confirmation_brl") + " ");
                } else {
                    emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_confirmation") + " ");
                }
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_confirm_pymnt") + " ");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_instructions_release_bitcoin"));                
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_communicate_email") + " ");
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
                
            } else if (order.getType().equals(Order.OrderType.ORDER_TYPE_SELL)){
                
                StringUtil.setLanguageResource(makerLogin.getLang());
                String emailSubjectMaker = ProjService.RB.getString("email_subject_order_created_sell_maker");
                StringBuilder emailBodyMaker = new StringBuilder();
                emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyMaker.append(makerLogin.getUsername() + ",");
                
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyMaker.append("<br></br><br></br>");       
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyMaker.append(ProjService.RB.getString("email_boby_testnet_bitcoin_msg"));
                
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_send_exactly") + " ");
                emailBodyMaker.append(order.getAmountNetSell() + " ");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_send_quantity_to_wallet") + " ");
                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
                    emailBodyMaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
                    emailBodyMaker.append("<a href=https://www.blocktrail.com/BTC/address/");
                }
               
                emailBodyMaker.append(addressMultisig.getAddress());
                emailBodyMaker.append(">");
                emailBodyMaker.append(addressMultisig.getAddress());
                emailBodyMaker.append("</a>");
                emailBodyMaker.append("<br></br><br></br>");
                if (order.getCurrencyCode().equals(StringVarsService.CURRENCY_CODE_BRL)){
                    emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_confirmation_brl") + " ");
                }else {
                    emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_confirmation") + " ");
                }
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_confirm_pymnt") + " ");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_instructions_release_bitcoin"));
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_communicate_email") + " ");
                emailBodyMaker.append(takerLogin.getEmail());
                emailBodyMaker.append("<br></br>");
                emailBodyMaker.append("("+takerLogin.getMobileNumState()+")");
                emailBodyMaker.append(takerLogin.getMobileNum());
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_end"));
                                
                StringUtil.setLanguageResource(takerLogin.getLang());
                String emailSubjectTaker = ProjService.RB.getString("email_subject_order_created_sell_taker");
                StringBuilder emailBodyTaker = new StringBuilder();
                emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " ");
                emailBodyTaker.append(takerLogin.getUsername() + ",");
                
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyTaker.append("<br></br><br></br>");       
                // keyword to search for bitcoin testnet: BITCOINTESTNET
                emailBodyTaker.append(ProjService.RB.getString("email_boby_testnet_bitcoin_msg"));
                
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_notify_seller") + " ");
                emailBodyTaker.append(order.getAmountNetBuy() + " ");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_pymnt_wallet") + " ");
                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
                    emailBodyTaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
                    emailBodyTaker.append("<a href=https://www.blocktrail.com/BTC/address/");
                }
                emailBodyTaker.append(addressMultisig.getAddress());
                emailBodyTaker.append(">");
                emailBodyTaker.append(addressMultisig.getAddress());
                emailBodyTaker.append("</a>");
                emailBodyTaker.append("<br></br><br></br>");
                if (order.getCurrencyCode().equals(StringVarsService.CURRENCY_CODE_BRL)){
                    emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirmation_brl") + " ");
                } else {
                    emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirmation") + " ");
                }
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirm_pymnt") + " ");
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
//            orderExecutedTime = DateFormatService.time24hourFormatString(order.getDateTaker())
//					+ "     "
//					+ DateFormatService.brDateFormat(order.getDateTaker());
            
            
//            StringBuilder emailText = new StringBuilder();
//            emailText.append(ProjService.RB.getString("email_body_hi") + " ");
//            emailText.append(makerLogin.getUsername());
//            emailText.append(",");
//            emailText.append("<br><br>");
//            emailText.append(emailBodyExecutedOrder_2);
//            emailText.append("<br><br>");
////            emailText.append(ProjService.RB.getString("email_body_ExecutedOrder_3"));
////            emailText.append(orderExecutedTime);
//            emailText.append("<br>");
//            emailText.append(ProjService.RB.getString("email_body_ExecutedOrder_4"));
//            emailText.append(ProjService.RB.getString("symbol_btc"));
//            emailText.append(" ");
//            emailText.append(ProjService.RB.getString("symbol_btc"));
//            emailText.append(paymentForwardRes.toString());
//
//            JavaMailThread javaMailThread_1 = new JavaMailThread(makerLogin.getEmail(), "txcreate", emailText.toString());
//            JavaMailThread javaMailThread_2 = new JavaMailThread(takerLogin.getEmail(), "txcreate", emailText.toString());
//            ExecutorService threadExecutor = Executors.newCachedThreadPool();
//            threadExecutor.execute(javaMailThread_1);
//            threadExecutor.execute(javaMailThread_2);
//            threadExecutor.shutdown(); 
            
        } catch (Exception exception) {
            exception.printStackTrace();
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", this.getClass().getName() + Order.OrderStatuses.ORDER_STATUS_TX_REQUESTED, exception.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
        }
        
        StringResultJson stringResultJson = new StringResultJson();
        stringResultJson.setResult("okey");
        String res = new Gson().toJson(stringResultJson);        
        return res;        
    }
}
