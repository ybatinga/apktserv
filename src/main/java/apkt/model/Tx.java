package apkt.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "tx")
public class Tx implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    @Column(name = "block_height")
    @SerializedName("block_height")
    private Long blockHeight;
    
    @Column(name = "hash")
    private String hash;
    
    @Column(name = "total")
    private Long total;
    
    @Column(name = "fees")
    private BigDecimal fees;
    
    // changed column name from 'size' to 'size_bytes'
    // because the column name 'size' is a reserved JPA QL keyword
    @Column(name = "size_bytes") 
    @SerializedName("size")
    private Long size;
    
    @Column(name = "preference")
    private String preference;
    
    @Column(name = "relayed_by")
    @SerializedName("relayed_by")
    private String relayedBy;
    
    @Column(name = "received")    
    private String received;
    
    @Column(name = "ver")
    private Long ver;
    
    @Column(name = "lock_time")
    @SerializedName("lock_time")
    private Long lockTime;
    
    @Column(name = "double_spend")
    @SerializedName("double_spend")
    private Boolean doubleSpend;
    
    @Column(name = "double_spend_tx")
    @SerializedName("double_spend_tx")
    private String doubleSpendTx;
    
    @Column(name = "vin_sz")
    @SerializedName("vin_sz")
    private Long vinSz;
    
    @Column(name = "vout_sz")
    @SerializedName("vout_sz")
    private Long voutSz;
    
    @Column(name = "confirmations")
    private Long confirmations;
    
    @Column(name = "confidence")
    private BigDecimal confidence;
    
    @Column(name = "confirmed")        
    private String confirmed;
    
    @Column(name = "receive_count")
    @SerializedName("receive_count")
    private Long receiveCount;
    
    @Column(name = "change_address")
    @SerializedName("change_address")
    private String changeAddress;
    
    @Column(name = "block_hash")
    @SerializedName("block_hash")
    private String blockHash;

    @Column(name = "double_of")
    @SerializedName("double_of")
    private String doubleOf;

    @Column(name = "hex")    
    private String hex;
    
    @Column(name = "addresses_json")
    private String addressesJson;
    
    @Transient
    private List<String> addresses;
    
//    @JoinColumn(name = "address_id", referencedColumnName = "id")
//    @ManyToOne
//    private Address addressId;
//    
//    @JoinColumn(name = "payment_forward_id", referencedColumnName = "id")
//    @ManyToOne
//    private PaymentForward paymentForwardId;
    
    @Column(name = "address_id", nullable = false)
    private Long addressId;
    
    @Column(name = "payment_forward_id", nullable = false)
    private Long paymentForwardId;
    
    @Column(name = "inputs_json")
    private String inputsJson;
    
    @Column(name = "outputs_json")
    private String outputsJson;
    
    @Column(name = "event_type")
    private String eventType;
    
    @Transient
    @SerializedName("inputs")
    private List<TxInput> inputs = new ArrayList<TxInput>();
    
    @Transient
    @SerializedName("outputs")
    private List<TxOutput> outputs = new ArrayList<TxOutput>();

    public Tx() {
    }

    public boolean addInput(TxInput input) {
        return inputs.add(input);
    }

    public boolean addOutput(TxOutput output) {
        return outputs.add(output);
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
    public Long getBlockHeight() {
        return blockHeight;
    }

    public void setBlockHeight(Long blockHeight) {
        this.blockHeight = blockHeight;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public BigDecimal getFees() {
        return fees;
    }

    public void setFees(BigDecimal fees) {
        this.fees = fees;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public String getRelayedBy() {
        return relayedBy;
    }

    public void setRelayedBy(String relayedBy) {
        this.relayedBy = relayedBy;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }

    public Long getVer() {
        return ver;
    }

    public void setVer(Long ver) {
        this.ver = ver;
    }

    public Long getLockTime() {
        return lockTime;
    }

    public void setLockTime(Long lockTime) {
        this.lockTime = lockTime;
    }

    public Boolean getDoubleSpend() {
        return doubleSpend;
    }

    public void setDoubleSpend(Boolean doubleSpend) {
        this.doubleSpend = doubleSpend;
    }

    public String getDoubleSpendTx() {
        return doubleSpendTx;
    }

    public void setDoubleSpendTx(String doubleSpendTx) {
        this.doubleSpendTx = doubleSpendTx;
    }
    
    public Long getVinSz() {
        return vinSz;
    }

    public void setVinSz(Long vinSz) {
        this.vinSz = vinSz;
    }

    public Long getVoutSz() {
        return voutSz;
    }

    public void setVoutSz(Long voutSz) {
        this.voutSz = voutSz;
    }

    public Long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Long confirmations) {
        this.confirmations = confirmations;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }    

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }
   
    public Long getReceiveCount() {
        return receiveCount;
    }

    public void setReceiveCount(Long receiveCount) {
        this.receiveCount = receiveCount;
    }

    public String getChangeAddress() {
        return changeAddress;
    }

    public void setChangeAddress(String changeAddress) {
        this.changeAddress = changeAddress;
    }

    public String getBlockHash() {
        return blockHash;
    }

    public void setBlockHash(String blockHash) {
        this.blockHash = blockHash;
    }

    public String getDoubleOf() {
        return doubleOf;
    }

    public void setDoubleOf(String doubleOf) {
        this.doubleOf = doubleOf;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getAddressesJson() {
        return addressesJson;
    }

    public void setAddressesJson(String addressesJson) {
        this.addressesJson = addressesJson;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<String> addresses) {
        this.addresses = addresses;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getPaymentForwardId() {
        return paymentForwardId;
    }

    public void setPaymentForwardId(Long paymentForwardId) {
        this.paymentForwardId = paymentForwardId;
    }
    
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getInputsJson() {
        return inputsJson;
    }

    public void setInputsJson(String inputsJson) {
        this.inputsJson = inputsJson;
    }

    public String getOutputsJson() {
        return outputsJson;
    }

    public void setOutputsJson(String outputsJson) {
        this.outputsJson = outputsJson;
    }

    public List<TxInput> getInputs() {
        return inputs;
    }

    public void setInputs(List<TxInput> inputs) {
        this.inputs = inputs;
    }

    public List<TxOutput> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<TxOutput> outputs) {
        this.outputs = outputs;
    }
    
}
