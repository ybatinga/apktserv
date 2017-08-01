package apkt.test;

import apkt.mail.JavaMailThread;
import apkt.service.ProjService;
import apkt.service.StringsService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SendEmailTest {
    
    public static void main(String[] args) {
        
        StringBuilder emailBodyMaker = new StringBuilder();
                emailBodyMaker.append(ProjService.RB.getString("email_body_hi") + " " + " ");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_send_exactly") + " ");
                emailBodyMaker.append("0.1232" + " ");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_send_quantity_to_wallet")  + " ");
                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
                    emailBodyMaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
                    emailBodyMaker.append("<a href=https://www.blocktrail.com/BTC/address/");
                }
                emailBodyMaker.append("paymentForward.getInputAddress()");
                emailBodyMaker.append(">");
                emailBodyMaker.append("paymentForward.getInputAddress()");
                emailBodyMaker.append("</a>");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_confirmation") + " ");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_confirm_pymnt") + " ");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_instructions_release_bitcoin"));
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_sell_maker_communicate_email") + " ");
                emailBodyMaker.append("takerLogin.getEmail()");
                emailBodyMaker.append("<br></br><br></br>");
                emailBodyMaker.append(ProjService.RB.getString("email_body_end"));
                                
                StringBuilder emailBodyTaker = new StringBuilder();
                emailBodyTaker.append(ProjService.RB.getString("email_body_hi") + " " + " ");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_notify_seller") + " ");
                emailBodyTaker.append("order.getAmountNetBuy()" + " ");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_pymnt_wallet") + " ");
                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
                    emailBodyTaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
                    emailBodyTaker.append("<a href=https://www.blocktrail.com/BTC/address/");
                }
                emailBodyTaker.append("multisigAddress.getAddress()");
                emailBodyTaker.append(">");
                emailBodyTaker.append("multisigAddress.getAddress()");
                emailBodyTaker.append("</a>");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirmation") + " ");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_confirm_pymnt") + " ");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_sell_taker_communicate_email") + " ");
                emailBodyTaker.append("takerLogin.getEmail()");
                emailBodyTaker.append("<br></br><br></br>");
                emailBodyTaker.append(ProjService.RB.getString("email_body_end"));

                JavaMailThread javaMailThread_1 = new JavaMailThread(
                    "desenv.notes@gmail.com", 
                    ProjService.RB.getString("email_subject_order_executed_tx_finalized").concat("desenv.notes@gmail.com"), 
                    emailBodyTaker.toString());
                JavaMailThread javaMailThread_2 = new JavaMailThread(
                    "desenv.notes@gmail.com", 
                    ProjService.RB.getString("email_subject_order_executed_tx_finalized").concat("desenv.notes@gmail.com"),
                    emailBodyMaker.toString());
                ExecutorService threadExecutor = Executors.newCachedThreadPool();
                threadExecutor.execute(javaMailThread_1);
                threadExecutor.execute(javaMailThread_2);
                threadExecutor.shutdown();        
        
//        StringBuilder emailBodyMaker = new StringBuilder();
//                emailBodyMaker.append(ProjService.RB.getString("email_body_TransactionFinalized_hi"));
//                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_notify_seller"));
//                emailBodyMaker.append("<br></br><br></br>");
//                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_pymnt_volume") + " ");
//                emailBodyMaker.append("0.00123" + " ");
//                emailBodyMaker.append(" " + ProjService.RB.getString("email_body_order_created_buy_maker_pymnt_wallet") + " ");
//                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
//                    emailBodyMaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
//                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
//                    emailBodyMaker.append("<a href=https://www.blocktrail.com/BTC/address/");
//                }
//                emailBodyMaker.append("1241234");
//                emailBodyMaker.append(">");
//                emailBodyMaker.append("address");
//                emailBodyMaker.append("</a>.");
//                emailBodyMaker.append("<br></br><br></br>");
//                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_confirmation") + " ");
//                emailBodyMaker.append("<br></br><br></br>");
//                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_confirm_pymnt") + " ");
//                emailBodyMaker.append("<br></br><br></br>");
//                emailBodyMaker.append(ProjService.RB.getString("email_body_order_created_buy_maker_communicate_email") + " ");
//                emailBodyMaker.append("desenv.notes@gmail.com");
//                emailBodyMaker.append(".");
//                emailBodyMaker.append("<br></br><br></br>");
//                emailBodyMaker.append(ProjService.RB.getString("email_body_end"));
//                
//                StringBuilder emailBodyTaker = new StringBuilder();
//                emailBodyTaker.append(ProjService.RB.getString("email_body_TransactionFinalized_hi"));
//                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_send_exactly") + " ");
//                emailBodyTaker.append("0.123423" + " ");
//                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_send_quantity_to_wallet") + " ");              
//                if (ProjService.ADDRESS.equals(ProjService.AddressType.TESTNET)){
//                    emailBodyTaker.append("<a href=https://www.blocktrail.com/tBTC/address/");
//                } else if (ProjService.ADDRESS.equals(ProjService.AddressType.MAIN)){
//                    emailBodyTaker.append("<a href=https://www.blocktrail.com/BTC/address/");
//                }
//                emailBodyTaker.append("assasdfds");
//                emailBodyTaker.append(">");
//                emailBodyTaker.append("address");
//                emailBodyTaker.append("</a>.");
//                emailBodyTaker.append("<br></br><br></br>");
//                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_confirmation") + " ");
//                emailBodyTaker.append("<br></br><br></br>");
//                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_confirm_pymnt") + " ");
//                emailBodyTaker.append("<br></br><br></br>");
//                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_instructions_release_bitcoin"));                
//                emailBodyTaker.append("<br></br><br></br>");
//                emailBodyTaker.append(ProjService.RB.getString("email_body_order_created_buy_taker_communicate_email") + " ");
//                emailBodyTaker.append("desenvapp@gmail.com");
//                emailBodyTaker.append(".");
//                emailBodyTaker.append(ProjService.RB.getString("email_body_end"));
//
//                JavaMailThread javaMailThread_1 = new JavaMailThread(
//                    "desenv.notes@gmail.com", 
//                    ProjService.RB.getString("email_subject_order_executed_tx_finalized_pt").concat("desenv.notes@gmail.com"), 
//                    emailBodyTaker.toString());
//                JavaMailThread javaMailThread_2 = new JavaMailThread(
//                    "desenv.notes@gmail.com", 
//                    ProjService.RB.getString("email_subject_order_executed_tx_finalized_pt").concat("desenv.notes@gmail.com"),
//                    emailBodyMaker.toString());
//                ExecutorService threadExecutor = Executors.newCachedThreadPool();
//                threadExecutor.execute(javaMailThread_1);
//                threadExecutor.execute(javaMailThread_2);
//                threadExecutor.shutdown();
    }

}
