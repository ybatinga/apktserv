package apkt.service;

public class StringsService {
    
    public static final String ok = "OK";
    public static String email_body_hi = "Olá ";
    public static String email_body_end = "Atenciosamente, <br></br>Equipe Apekato<br></br>support@apekato.com";
    
    public static String email_subject_order_created_buy_maker = "Instruções para efetuar a compra no Apekato";
    public static String email_subject_order_created_buy_taker = "Instruções para efetuar a venda no Apekato";
    public static String email_subject_order_created_sell_maker = "Instruções para efetuar a venda no Apekato";
    public static String email_subject_order_created_sell_taker = "Instruções para efetuar a compra no Apekato";

    public static String email_subject_order_executed_tx_finalized = "Transação finalizada no Apekato";
    
    public static String email_subject_password_forgot_subject =
            " Solicitação de redefinição de senha";
    
    public static String email_body_order_created_buy_maker_pymnt_volume = 
            "O comerciante foi notificado via email para enviar a quantia de ";
    public static String email_body_order_created_buy_maker_pymnt_wallet = 
            " bitcoin na carteira de custódia: ";
    public static String email_body_order_created_buy_maker_confirmation = 
            "Ao confirmar-se no blockchain o envio para a carteira de custódia, "
            + "você receberá um email com informações da conta bancária "
            + "do comerciante para que você efetue o pagamento. ";
    public static String email_body_order_created_buy_maker_confirm_pymnt = 
            "Em seguida, comunique-se com o comerciante confirmando o pagamento para que o mesmo libere o bitcoin " 
            + "para sua carteira. ";
    public static String email_body_order_created_buy_maker_communicate_email = 
            "Informações de contato do comerciante:<br></br> " ;
        
    public static String email_body_order_created_buy_taker_send_exactly = 
            "Por favor, envie exatamente ";
    public static String email_body_order_created_buy_taker_send_quantity_to_wallet = 
            "bitcoin para a carteira de custódia: ";
    public static String email_body_order_created_buy_taker_confirmation = 
            "Ao confirmar-se no blockchain o envio para a carteira de custódia, "
            + "o comprador receberá um email com informações da sua conta bancária para que o comprador efetue "
            + "o pagamento. ";
    public static String email_body_order_created_buy_taker_confirm_pymnt = 
            "Efetuado o pagamento, o comprador deve enviar-lhe um email confirmando esse pagamento para que você "
            + "libere o bitcoin para a carteira do comprador. ";
        public static String email_body_order_created_buy_taker_instructions_release_bitcoin = 
            "Para liberar o bitcoin, selecione o item 'Transações' no menu do aplicativo. "
            + "Em seguida, selecione a transação, e pressione o botão 'LIBERAR BITCOIN'.";
    public static String email_body_order_created_buy_taker_communicate_email = 
            "Informações de contato do comprador:<br></br> " ;

    public static String email_body_order_created_sell_maker_send_exactly = 
            "Por favor, envie exatamente ";
    public static String email_body_order_created_sell_maker_send_quantity_to_wallet = 
            "bitcoin para a carteira de custódia: ";
    public static String email_body_order_created_sell_maker_confirmation = 
            "Ao confirmar-se no blockchain o envio para a carteira de custódia, "
            + "o comprador receberá um email com informações da sua conta bancária para que o comprador efetue "
            + "o pagamento. ";
    public static String email_body_order_created_sell_maker_confirm_pymnt = 
            "Efetuado o pagamento, o comprador deve enviar-lhe um email confirmando esse pagamento para que você "
            + "libere o bitcoin para a carteira do comprador. ";
        public static String email_body_order_created_sell_maker_instructions_release_bitcoin = 
            "Para liberar o bitcoin, selecione o item 'Transações' no menu do aplicativo. "
            + "Em seguida, selecione a transação, e pressione o botão 'LIBERAR BITCOIN'.";
    public static String email_body_order_created_sell_maker_communicate_email = 
            "Informações de contato do comprador:<br></br> " ;

    public static String email_body_order_created_sell_taker_notify_seller = 
            "O comerciante foi notificado via email para enviar a quantia de ";
    public static String email_body_order_created_buy_taker_pymnt_wallet = 
            "bitcoin na carteira de custódia: ";
    public static String email_body_order_created_sell_taker_confirmation = 
            "Ao confirmar-se no blockchain o envio para a carteira de custódia, "
            + "você receberá um email com informações da conta bancária "
            + "do comerciante para que você efetue o pagamento. ";
    public static String email_body_order_created_sell_taker_confirm_pymnt = 
            "Em seguida, comunique-se com o comerciante confirmando o pagamento para que o mesmo libere " +
            "o bitcoin para sua carteira. ";
    public static String email_body_order_created_sell_taker_communicate_email = 
            "Informações de contato do comerciante:<br></br> " ;
           
    
    public static String email_body_order_confirmed_buyer =
            "Foi confirmado no blockchain o envio de ";
    public static String email_body_order_confirmed_buyer_multisig =
            " bitcoin na carteira de custódia: ";
    public static String email_body_order_confirmed_buyer_pymnt =
            "Por favor, efetue o pagamento de exatamente ";
    public static String email_body_order_confirmed_buyer_accnt_info =
            " reais na conta bancária:";
//    public static String email_body_order_confirmed_buyer_doc =
//            "CPF: ";
    public static String email_body_order_confirmed_buyer_name =
            "Nome: ";
    public static String email_body_order_confirmed_buyer_branch =
            "Agência: ";
    public static String email_body_order_confirmed_buyer_accnt_corrente =
            "Conta Corrente: ";
    public static String email_body_order_confirmed_buyer_accnt_poupanca =
            "Conta Poupança: ";
    public static String email_body_password_forgot =
            "Insira esse código no Apekato para recuperar sua senha : ";
            
    public static String email_body_order_confirmed_seller =
            "Foi confirmado o envio de ";
    public static String email_body_order_confirmed_seller_multisig =
            " bitcoin na carteira de custódia: ";
    public static String email_body_order_confirmed_seller_pymnt =
            "O comprador foi notificado via email para que efetue o pagamento de ";
    public static String email_body_order_confirmed_seller_pymnt_account = "reais na sua conta bancária.";
    public static String email_body_order_confirmed_seller_wait_email =
            "Por favor, aguarde o email do comprador confirmando o pagamento. ";
    public static String email_body_order_confirmed_seller_confirmation =
            "Ao confirmar-se o pagamento, libere o bitcoin para a carteira do comprador. ";
    public static String email_body_order_confirmed_seller_release_instructions =
            "Para liberar o bitcoin, selecione o item 'Transações' no menu do aplicativo. "
            + "Em seguida, selecione a transação, e pressione o botão 'LIBERAR BITCOIN'.";
    
    
    
//    public static String email_body_ExecutedOrder_2_sell = "Sua ordem de venda foi executada:";
//    public static String email_body_ExecutedOrder_2_buy = "Sua ordem de compra foi executada:";
//    public static String email_body_ExecutedOrder_3 = "Data e Hora: ";
//    public static String email_body_ExecutedOrder_4 = "Volume: ";
//    public static String email_body_ExecutedOrder_5 = "Preço unitário: ";
//    public static String email_body_ExecutedOrder_6 = "Valor total: ";
//    public static String email_body_ExecutedOrder_7 = "Comissão: ";
//    public static String email_body_ExecutedOrder_8 = "Líquido: ";
    public static String symbol_btc = "Ƀ";
    
    public static String email_body_tx_finalized_buyer_notify_seller = "O vendedor ";
    public static String email_body_tx_finalized_buyer_released = " liberou os fundos (";
    public static String email_body_tx_finalized_buyer_released_btc = " BTC) para voc&ecirc;.";
    
    public static String email_body_tx_finalized_seller_notify = "Voc&ecirc; liberou os fundos (";
    public static String email_body_tx_finalized_seller_notify_funds = " BTC) para o comprador ";

    public static String exception_response_code_400 = "400 for URL";
    public static String exception_response_file_not_found_exception = "java.io.FileNotFoundException";
    
  
}
