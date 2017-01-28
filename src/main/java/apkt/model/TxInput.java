package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * Input of a transation, ie:
 * <p/>
 * {
 * "prev_hash":
 * "c3fe841599794f88374b0aaf0cbd5b3897d75c4dc897a846e6040054d5495d66",
 * "output_index": 0, "script":
 * "483045022100ddb75ef19a31eb5e25595cb35c6b5f058912cc168a32a215c680a5532900904202200efb197876164fa246ff5009a04f39ff51db70adb90ee342f0aa97ec19d776eb012103f78041c92a4aea6e44ac937c8bd7e504e14768a40879dc7655e533a749fea55b",
 * "output_value": 499950000, "addresses": [
 * "mqz1CxAGWahHuaTnjHFnitfv8VguUwe7dN" ], "script_type": "pay-to-pubkey-hash" }
 *
 * @author <a href="mailto:seb.auvray@gmail.com">Sebastien Auvray</a>
 */
@Entity
@Table(name = "tx_input")
public class TxInput implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "prev_hash")
    @SerializedName("prev_hash")
    private String prevHash;
    
    @Column(name = "output_index")
    @SerializedName("output_index")
    private BigDecimal outputIndex;
    
    @Column(name = "output_value")
    @SerializedName("output_value")
    private BigDecimal outputValue;
    
    @Column(name = "script_type")
    @SerializedName("script_type")
    private String scriptType;
    
    @Column(name = "script")
    private String script;
    
    @Column(name = "addresses_json")
    @SerializedName("addresses_json")
    private String addressesJson;
    
    @Transient
    @SerializedName("addresses")
    private List<String> addresses = new ArrayList<String>();
    
    @Column(name = "sequence")
    private Long sequence;
    
    @Column(name = "age")
    private Long age;
    
    @Column(name = "wallet_name")
    @SerializedName("wallet_name")
    private String walletName;
    
    @Column(name = "wallet_token")
    @SerializedName("wallet_token")
    private String walletToken;
    
//    @JoinColumn(name = "tx_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Tx txId;
    
    @Basic(optional = false)
    @Column(name = "tx_id", nullable = false)
    private Long txId;
    
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    public TxInput() {
    }

    public TxInput(Long id, String prevHash, BigDecimal outputIndex, BigDecimal outputValue, String scriptType, String script, String addressesJson, Long sequence, Long age, String walletName, String walletToken, Long txId) {
        this.id = id;
        this.prevHash = prevHash;
        this.outputIndex = outputIndex;
        this.outputValue = outputValue;
        this.scriptType = scriptType;
        this.script = script;
        this.addressesJson = addressesJson;
        this.sequence = sequence;
        this.age = age;
        this.walletName = walletName;
        this.walletToken = walletToken;
        this.txId = txId;
    }    
    
    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }
    
    public boolean addAddress(String address) {
        return addresses.add(address);
    }
    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the prevHash
     */
    public String getPrevHash() {
        return prevHash;
    }

    /**
     * @param prevHash the prevHash to set
     */
    public void setPrevHash(String prevHash) {
        this.prevHash = prevHash;
    }

    /**
     * @return the outputIndex
     */
    public BigDecimal getOutputIndex() {
        return outputIndex;
    }

    /**
     * @param outputIndex the outputIndex to set
     */
    public void setOutputIndex(BigDecimal outputIndex) {
        this.outputIndex = outputIndex;
    }

    /**
     * @return the outputValue
     */
    public BigDecimal getOutputValue() {
        return outputValue;
    }

    /**
     * @param outputValue the outputValue to set
     */
    public void setOutputValue(BigDecimal outputValue) {
        this.outputValue = outputValue;
    }

    /**
     * @return the scriptType
     */
    public String getScriptType() {
        return scriptType;
    }

    /**
     * @param scriptType the scriptType to set
     */
    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    /**
     * @return the script
     */
    public String getScript() {
        return script;
    }

    /**
     * @param script the script to set
     */
    public void setScript(String script) {
        this.script = script;
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

    /**
     * @return the sequence
     */
    public Long getSequence() {
        return sequence;
    }

    /**
     * @param sequence the sequence to set
     */
    public void setSequence(Long sequence) {
        this.sequence = sequence;
    }

    /**
     * @return the age
     */
    public Long getAge() {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(Long age) {
        this.age = age;
    }

    /**
     * @return the walletName
     */
    public String getWalletName() {
        return walletName;
    }

    /**
     * @param walletName the walletName to set
     */
    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    /**
     * @return the walletToken
     */
    public String getWalletToken() {
        return walletToken;
    }

    /**
     * @param walletToken the walletToken to set
     */
    public void setWalletToken(String walletToken) {
        this.walletToken = walletToken;
    }

    public Long getTxId() {
        return txId;
    }

    public void setTxId(Long txId) {
        this.txId = txId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
