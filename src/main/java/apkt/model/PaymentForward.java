package apkt.model;

import java.math.BigDecimal;
import java.util.List;

import com.blockcypher.model.transaction.Transaction;
import com.blockcypher.utils.gson.GsonFactory;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "payment_forward")
public class PaymentForward implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_pk", nullable = false)
    private Long idPk;
    
    @Column(name = "identifier")
    private String id;
    
    @Column(name = "token")
    private String token;
    
    @Column(name = "destination")
    private String destination;
    
    @Column(name = "input_address")
    @SerializedName("input_address")
    private String inputAddress;
    
    @Column(name = "process_fees_address")
    @SerializedName("process_fees_address")
    private String processFeesAddress;
    
    @Column(name = "process_fees_satoshis")
    @SerializedName("process_fees_satoshis")
    private BigDecimal processFeesSatoshis;
    
    @Column(name = "process_fees_percent")
    @SerializedName("process_fees_percent")
    private BigDecimal processFeesPercent;
    
    @Column(name = "callback_url")
    @SerializedName("callback_url")
    private String callbackUrl;
    
    @Column(name = "enable_confirmations")
    @SerializedName("enable_confirmations")
    private boolean enableConfirmations;
    
    @Column(name = "mining_fees_satoshis")
    @SerializedName("mining_fees_satoshis")
    private Integer miningFeesSatoshis;
    
    @Transient
    private List<Transaction> transactions;
    
    @Column(name = "transactions_json")
    private String transactionsJson;
    
    @Column(name = "value")
    private BigDecimal value;
    
    @Column(name = "input_transaction_hash")
    @SerializedName("input_transaction_hash")
    private String inputTransactionHash;
    
    @Column(name = "transaction_hash")
    @SerializedName("transaction_hash")
    private String transactionHash;
    
    @Column(name = "webhook_id")
    @SerializedName("webhook_id")
    private String webhookId;  
    
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Order orderId;
    
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    public PaymentForward() {
    }

    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }

    public Long getIdPk() {
        return idPk;
    }

    public void setIdPk(Long idPk) {
        this.idPk = idPk;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getInputAddress() {
        return inputAddress;
    }

    public void setInputAddress(String inputAddress) {
        this.inputAddress = inputAddress;
    }

    public String getProcessFeesAddress() {
        return processFeesAddress;
    }

    public void setProcessFeesAddress(String processFeesAddress) {
        this.processFeesAddress = processFeesAddress;
    }

    public BigDecimal getProcessFeesSatoshis() {
        return processFeesSatoshis;
    }

    public void setProcessFeesSatoshis(BigDecimal processFeesSatoshis) {
        this.processFeesSatoshis = processFeesSatoshis;
    }

    public BigDecimal getProcessFeesPercent() {
        return processFeesPercent;
    }

    public void setProcessFeesPercent(BigDecimal processFeesPercent) {
        this.processFeesPercent = processFeesPercent;
    }

    public String getCallbackUrl() {
        return callbackUrl;
    }

    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public boolean isEnableConfirmations() {
        return enableConfirmations;
    }

    public void setEnableConfirmations(boolean enableConfirmations) {
        this.enableConfirmations = enableConfirmations;
    }

    public Integer getMiningFeesSatoshis() {
        return miningFeesSatoshis;
    }

    public void setMiningFeesSatoshis(Integer miningFeesSatoshis) {
        this.miningFeesSatoshis = miningFeesSatoshis;
    }
    
    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getTransactionsJson() {
        return transactionsJson;
    }

    public void setTransactionsJson(String transactionsJson) {
        this.transactionsJson = transactionsJson;
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

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
}
