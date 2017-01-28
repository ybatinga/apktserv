package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import java.math.BigDecimal;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author elton
 */
public class PaymentForwardCallback {
    
    private BigDecimal value;
    
    @SerializedName("input_address")
    private String inputAddress;
   
    @SerializedName("input_transaction_hash")    
    private String inputTransactionHash;
    
    @SerializedName("transaction_hash")    
    private String transactionHash;
    
    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }

    public String getInputAddress() {
        return inputAddress;
    }

    public void setInputAddress(String inputAddress) {
        this.inputAddress = inputAddress;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getInputTransactionHash() {
        return inputTransactionHash;
    }

    public void setInputTransactionHash(String inputTransactionHash) {
        this.inputTransactionHash = inputTransactionHash;
    }

    public String getTransactionHash() {
        return transactionHash;
    }

    public void setTransactionHash(String transactionHash) {
        this.transactionHash = transactionHash;
    }

}





