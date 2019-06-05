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

/**
 * Output of a transaction, ie:
 * {
 * "value": 100000000,
 * "script": "76a914a4e9eecbbfd050cb2d47eb0452a97ccb607f53c788ac",
 * "spent_by": "",
 * "addresses": [
 * "mvYwMT3aZ5jNcRNNjv7ckxjbqMDtvQbAHz"
 * ],
 * "script_type": "pay-to-pubkey-hash"
 * }
 * @author <a href="mailto:seb.auvray@gmail.com">Sebastien Auvray</a>
 */
@Entity
@Table(name = "tx_output")
public class TxOutput implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "value")        
    private BigDecimal value;
    
    @Column(name = "script")
    private String script;
    
    @Column(name = "script_type")
    @SerializedName("script_type")
    private String scriptType;
    
    @Column(name = "spent_by")
    @SerializedName("spent_by")
    private String spentBy;
    
    @Column(name = "data_hex")
    @SerializedName("data_hex")
    private String dataHex;
    
    @Column(name = "data_string")
    @SerializedName("data_string")
    private String dataString;    
    
    @Column(name = "addresses_json")
    @SerializedName("addresses_json")
    private String addressesJson;
    
    @Transient
    @SerializedName("addresses")
    private List<String> addresses = new ArrayList<String>();
    
//    @JoinColumn(name = "tx_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Tx txId;
    
    @Basic(optional = false)
    @Column(name = "tx_id", nullable = false)
    private Long txId;
    
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    public TxOutput() {
    }

    public boolean addAddress(String address) {
        return addresses.add(address);
    }

    public Long getId() {
        return id;
    }

  
    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }

    public String getSpentBy() {
        return spentBy;
    }

    public void setSpentBy(String spentBy) {
        this.spentBy = spentBy;
    }

    public String getDataHex() {
        return dataHex;
    }

    public void setDataHex(String dataHex) {
        this.dataHex = dataHex;
    }

    public String getDataString() {
        return dataString;
    }

    public void setDataString(String dataString) {
        this.dataString = dataString;
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
