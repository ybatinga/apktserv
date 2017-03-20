package apkt.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "tx_op_return")
public class TxOpReturn implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "text", nullable = false)
    private String text;
    
    @Basic(optional = false)
    @Column(name = "address", nullable = false)
    private String address;
    
    @Basic(optional = false)
    @Column(name = "status", nullable = false)
    private String status;
    
    @Basic(optional = false)
    @Column(name = "date_op_return")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateOpReturn;

    @Column(name = "tx_id")
    private String txId;
    
    @Column(name = "fee")
    private BigDecimal fee;
    
    public TxOpReturn() {
    }

    public TxOpReturn(Long id) {
        this.id = id;
    }

    public TxOpReturn(String text, String address, String status, Date dateOpReturn, String txId, BigDecimal fee) {
        this.text = text;
        this.address = address;
        this.status = status;
        this.dateOpReturn = dateOpReturn;
        this.txId = txId;
        this.fee = fee;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getDateOpReturn() {
        return dateOpReturn;
    }

    public void setDateOpReturn(Date dateOpReturn) {
        this.dateOpReturn = dateOpReturn;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }
}
