package apkt.test;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.TransactionService;
import apkt.blockcypher.utils.SignUtils;
import apkt.dao.jpa.GenericDaoJpa;
import apkt.model.Address;
import apkt.model.IntermediaryTransaction;
import apkt.model.Order;
import apkt.model.OrderWallet;
import apkt.model.Tx;
import apkt.service.CalcVarsService;
import apkt.utils.BlockCypherConstants;
import apkt.utils.CalcUtils;
//import com.blockcypher.context.BlockCypherContext;
//import com.blockcypher.model.transaction.Transaction;
//import com.blockcypher.model.transaction.intermediary.IntermediaryTransaction;
//import com.blockcypher.service.TransactionService;
//import com.blockcypher.utils.sign.SignUtils;
import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class TransactionMultiSigFundingAndSpendingMain {
    public static void main(String[] args) {
        try {

            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            BlockCypherContext blockCypherContext = new BlockCypherContext(
            BlockCypherConstants.VERSION_V1,
            BlockCypherConstants.CURRENCY_BTC,
            BlockCypherConstants.NETWORK);

            TransactionService transactionService = blockCypherContext.getTransactionService();

            Order order = GenericDaoJpa.find(em, Order.class, new Long("127"));
            Address multisigAddress = null;
            OrderWallet orderWallet = null;

            List<String> addressDest = new ArrayList<String>();
            List<String> pubKeyMultiSigList = new ArrayList<String>();
            List<String> privKeyMultiSigList = new ArrayList<String>();
            
            List<Address> addressList = GenericDaoJpa.findListByAttribute(em, Address.class, "orderId", order.getId());
            
            // fill pubKeyMultiSigList and privKeyMultiSigList repeating three times "for" iteration.
            // It prevents the following exception from happening: "java.lang.IndexOutOfBoundsException: Index: 2, Size: 1".
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_MAKER)) {
                    pubKeyMultiSigList.add(address.getPublicAddress());
                    privKeyMultiSigList.add(address.getPrivateAddress());                    
                }
            }            
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_TAKER)) {
                    pubKeyMultiSigList.add(address.getPublicAddress());
                    privKeyMultiSigList.add(address.getPrivateAddress());                    
                }
            }            
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_ESCROW)) {
                    pubKeyMultiSigList.add(address.getPublicAddress()); 
                    privKeyMultiSigList.add(address.getPrivateAddress());
                }
            }
            for (Address address : addressList){
                if (address.getType().equals(Address.AddressType.ADDRESS_TYPE_MULTISIG)) {
                    multisigAddress = address;
                }
            }
            
            orderWallet = order.getOrderWalletId();
            addressDest.add("1LEUz9xqFJyZYkvWa7P7FkjohzGe5tkwTd");
            
            Long value = CalcUtils.btcToSatoshis(order.getAmountNetSell());

            // subtract MINING_FEES_SATOSHIS from value 
            value = value - CalcVarsService.MINING_FEES_SATOSHIS;
            
            IntermediaryTransaction intermTxSpend = transactionService.newFundingTransaction(
                    pubKeyMultiSigList,
                    addressDest,
                    value,
                    new BigDecimal(CalcVarsService.MINING_FEES_SATOSHIS),
                    true);
	
            intermTxSpend.addPubKeys(pubKeyMultiSigList.get(0));
            SignUtils.signWithHexKeyNoPubKey(intermTxSpend, privKeyMultiSigList.get(0));
            Thread.sleep(1000);
            intermTxSpend.addPubKeys(pubKeyMultiSigList.get(1));
            SignUtils.signWithHexKeyNoPubKey(intermTxSpend, privKeyMultiSigList.get(1));
            Thread.sleep(1000);
            Tx transaction = transactionService.sendTransaction(intermTxSpend);
        } catch (Exception exception) {
                exception.printStackTrace();
        }
    }
}
