package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "address")
public class Address implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;    
    @Basic(optional = false)
    @Column(name = "address", nullable = false)
    private String address;        
    @Column(name = "public")
    @SerializedName("public")
    private String publicAddress;   
    @Column(name = "private")
    @SerializedName("private")
    private String privateAddress;    
    @Basic(optional = false)    
    @Column(name = "type", nullable = false)
    private String type;    
    @Column(name = "webhook_id")
    private String webhookId;            
    @Column(name = "total_received")
    private BigDecimal totalReceived;    
    @Column(name = "total_sent")
    private BigDecimal totalSent;    
    @Column(name = "balance")
    private BigDecimal balance;   
    @Column(name = "unconfirmed_balance")
    private BigDecimal unconfirmedBalance;    
    @Column(name = "final_balance")
    private BigDecimal finalBalance;   
    @Column(name = "n_tx")
    private Long nTx;   
    @Column(name = "unconfirmed_n_tx")
    private Long unconfirmedNTx;    
    @Column(name = "final_n_tx")
    private Long finalNTx;    
    @Column(name = "txrefs")
    private String txrefs;    
    @Column(name = "unconfirmed_txrefs")
    private String unconfirmedTxrefs;    
    @Column(name = "tx_url")
    private String txUrl;    
    @Column(name = "has_more")
    private Boolean hasMore;    
//    @JoinColumn(name = "order_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Order orderId;
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    
    public Address() {
    }

    public Address(Long id) {
        this.id = id;
    }

    public Address(Long id, String address, String publicAddress, String type) {
        this.id = id;
        this.address = address;
        this.publicAddress = publicAddress;
        this.type = type;
    }
    
    public Address(AddressKeychain addressKeychain) {
        this.address = addressKeychain.getAddress();
    }
    
    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getPrivateAddress() {
        return privateAddress;
    }

    public void setPrivateAddress(String privateAddress) {
        this.privateAddress = privateAddress;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public void setWebhookId(String webhookId) {
        this.webhookId = webhookId;
    }

    public BigDecimal getTotalReceived() {
        return totalReceived;
    }

    public void setTotalReceived(BigDecimal totalReceived) {
        this.totalReceived = totalReceived;
    }

    public BigDecimal getTotalSent() {
        return totalSent;
    }

    public void setTotalSent(BigDecimal totalSent) {
        this.totalSent = totalSent;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getUnconfirmedBalance() {
        return unconfirmedBalance;
    }

    public void setUnconfirmedBalance(BigDecimal unconfirmedBalance) {
        this.unconfirmedBalance = unconfirmedBalance;
    }

    public BigDecimal getFinalBalance() {
        return finalBalance;
    }

    public void setFinalBalance(BigDecimal finalBalance) {
        this.finalBalance = finalBalance;
    }

    public Long getnTx() {
        return nTx;
    }

    public void setnTx(Long nTx) {
        this.nTx = nTx;
    }

    public Long getUnconfirmedNTx() {
        return unconfirmedNTx;
    }

    public void setUnconfirmedNTx(Long unconfirmedNTx) {
        this.unconfirmedNTx = unconfirmedNTx;
    }

    public Long getFinalNTx() {
        return finalNTx;
    }

    public void setFinalNTx(Long finalNTx) {
        this.finalNTx = finalNTx;
    }

    public String getTxrefs() {
        return txrefs;
    }

    public void setTxrefs(String txrefs) {
        this.txrefs = txrefs;
    }

    public String getUnconfirmedTxrefs() {
        return unconfirmedTxrefs;
    }

    public void setUnconfirmedTxrefs(String unconfirmedTxrefs) {
        this.unconfirmedTxrefs = unconfirmedTxrefs;
    }

    public String getTxUrl() {
        return txUrl;
    }

    public void setTxUrl(String txUrl) {
        this.txUrl = txUrl;
    }

    public Boolean getHasMore() {
        return hasMore;
    }

    public void setHasMore(Boolean hasMore) {
        this.hasMore = hasMore;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public static class AddressType {

        public static final String ADDRESS_TYPE_MAKER = "MAKER";
        public static final String ADDRESS_TYPE_TAKER = "TAKER";
        public static final String ADDRESS_TYPE_ARBITRATOR = "ARBITRATOR";
        public static final String ADDRESS_TYPE_MULTISIG = "MULTISIG";
    }
}
