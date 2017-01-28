package apkt.service;

public class StringsServiceEn {
    public static final String ok = "OK";
    public static String email_body_hi = "Hi ";
    public static String email_body_end = "Sincerely, <br></br>Equipe Apekato<br></br>support@apekato.com";

    
    public static String email_subject_order_created_buy_maker = "Instruções para efetuar a compra no Apekato";
    public static String email_subject_order_created_buy_taker = "Instruções para efetuar a venda no Apekato";
    public static String email_subject_order_created_sell_maker = "Instruções para efetuar a venda no Apekato";
    public static String email_subject_order_created_sell_taker = "Instruções para efetuar a compra no Apekato";

    public static String email_subject_order_executed_tx_finalized = "Transaction finalized on Apekato";
    
    public static String email_subject_password_forgot_subject =
            "Password reset request";
    
    public static String email_body_order_created_buy_maker_pymnt_volume = 
            "The seller was notified via email to send the amount of ";
    public static String email_body_order_created_buy_maker_pymnt_wallet = 
            " bitcoin to the escrow wallet: ";
    public static String email_body_order_created_buy_maker_confirmation = 
            "Upon confirmation in the blockchain that bitcoin was sent to a escrow wallet, "
            + "you will receive an email with the seller's bank account details "
            + "so that you can pay the seller via bank transfer. ";
    public static String email_body_order_created_buy_maker_confirm_pymnt = 
            "After that, send an email to the seller "
            + "confirming the bank transfer payment so that the seller can release "
            + "the bitcoin to be sent to your wallet. ";
    public static String email_body_order_created_buy_maker_communicate_email = 
            "Seller contact information:<br></br> " ;
    
    public static String email_body_order_created_buy_taker_send_exactly = 
            "Please, send exactly ";
    public static String email_body_order_created_buy_taker_send_quantity_to_wallet = 
            " bitcoin to the escrow wallet: ";
    public static String email_body_order_created_buy_taker_confirmation = 
            "Upon confirmation in the blockchain that bitcoin was sent to a escrow wallet, "
            + "the contact will receive an email informing your bank account details "
            + "in order to pay you via bank transfer. ";
    public static String email_body_order_created_buy_taker_confirm_pymnt = 
            "Once payment was made, the contact should send you an email confirming the bank "
            + "transfer payment so that you can release the bitcoin to the contac's wallet. ";
        public static String email_body_order_created_buy_taker_instructions_release_bitcoin = 
            "In order to release the bitcoin, select the o item 'Transactions' in the app's menu. "
            + "After that, select the transaction, and press 'RELEASE BITCOIN' button.";
    public static String email_body_order_created_buy_taker_communicate_email = 
            "Contact information:<br></br> " ;

    public static String email_body_order_created_sell_maker_send_exactly = 
            "Please send exactly ";
    public static String email_body_order_created_sell_maker_send_quantity_to_wallet = 
            " bitcoin to the escrow wallet: ";
    public static String email_body_order_created_sell_maker_confirmation = 
            "Upon confirmation in the blockchain that bitcoin was sent to a escrow wallet, "
            + "the contact will receive an email informing your bank account details "
            + "in order to pay you via bank transfer. ";
    public static String email_body_order_created_sell_maker_confirm_pymnt = 
            "Once payment was made, the contact should send you an email confirming the bank "
            + "transfer payment so that you can release the bitcoin to the contac's wallet. ";
        public static String email_body_order_created_sell_maker_instructions_release_bitcoin = 
            "In order to release the bitcoin, select the o item 'Transactions' in the app's menu. "
            + "After that, select the transaction, and press 'RELEASE BITCOIN' button.";
    public static String email_body_order_created_sell_maker_communicate_email = 
            "Contact information:<br></br> " ;

    public static String email_body_order_created_sell_taker_notify_seller = 
            "The seller was notified via email to send the amount of ";
    public static String email_body_order_created_buy_taker_pymnt_wallet = 
            " bitcoin to the escrow wallet: ";
    public static String email_body_order_created_sell_taker_confirmation = 
            "Upon confirmation in the blockchain that bitcoin was sent to a escrow wallet, "
            + "you will receive an email with the seller's bank account details "
            + "so that you can pay the seller via bank transfer. ";
    public static String email_body_order_created_sell_taker_confirm_pymnt = 
            "After that, send an email to the seller "
            + "confirming the bank transfer payment so that the seller can release "
            + "the bitcoin to be sent to your wallet. ";
    public static String email_body_order_created_sell_taker_communicate_email = 
            "Seller contact information:<br></br> " ;
    
    public static String email_body_order_confirmed_buyer =
            "It was confirmed in the blockchain the amount of ";
    public static String email_body_order_confirmed_buyer_multisig =
            " bitcoin was sent to the escrow wallet: ";
    public static String email_body_order_confirmed_buyer_pymnt =
            "Please, pay exactly ";
    public static String email_body_order_confirmed_buyer_accnt_info =
            " Brazil real into the bank account:";
//    public static String email_body_order_confirmed_buyer_doc =
//            "CPF: ";
    public static String email_body_order_confirmed_buyer_name =
            "Name: ";
    public static String email_body_order_confirmed_buyer_branch =
            "Branch: ";
    public static String email_body_order_confirmed_buyer_accnt_corrente =
            "Account Number (Conta Corrente): ";
    public static String email_body_order_confirmed_buyer_accnt_poupanca =
            "Account Number (Conta Poupança): ";
    
    public static String email_body_password_forgot =
            "Enter this code on Apekato to recover your password: ";
            
    public static String email_body_order_confirmed_seller =
            "It was confirmed in the blockchain the amount of ";
    public static String email_body_order_confirmed_seller_multisig =
            " bitcoin was sent to the escrow wallet: ";
    
    public static String email_body_order_confirmed_seller_pymnt =
            "The contact was notified via email to pay the amount of ";
    public static String email_body_order_confirmed_seller_pymnt_account = 
            " Brazil real into your bank account.";
    public static String email_body_order_confirmed_seller_wait_email =
            "Please, wait to receive the contact's email confirming that payment was made into your bank account. ";
    public static String email_body_order_confirmed_seller_confirmation =
            "Upon payment confirmation, release the bitcoin to the contac's wallet. ";
    public static String email_body_order_confirmed_seller_release_instructions =
            "In order to release the bitcoin, select the o item 'Transactions' in the app's menu. "
            + "After that, select the transaction, and press 'RELEASE BITCOIN' button.";
    
//    public static String email_body_ExecutedOrder_2_sell = "Sua ordem de venda foi executada:";
//    public static String email_body_ExecutedOrder_2_buy = "Sua ordem de compra foi executada:";
//    public static String email_body_ExecutedOrder_3 = "Data e Hora: ";
//    public static String email_body_ExecutedOrder_4 = "Volume: ";
//    public static String email_body_ExecutedOrder_5 = "Unit price: ";
//    public static String email_body_ExecutedOrder_6 = "Amount total: ";
//    public static String email_body_ExecutedOrder_7 = "Service fee: ";
//    public static String email_body_ExecutedOrder_8 = "Net: "; // verify if 'Net value' or 'Net total'
    public static String symbol_btc = "Ƀ";
    
    public static String email_body_tx_finalized_buyer_notify_seller = "The seller ";
    public static String email_body_tx_finalized_buyer_released = " has released the funds (";
    public static String email_body_tx_finalized_buyer_released_btc = " BTC) to  you.";
    
    public static String email_body_tx_finalized_seller_notify = "You have released the funds (";
    public static String email_body_tx_finalized_seller_notify_funds = " BTC) to the contact ";

    public static String exception_response_code_400 = "400";
    public static String exception_response_file_not_found_exception = "java.io.FileNotFoundException";
    
  
}
