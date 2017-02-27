package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import java.io.Serializable;
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
@Table(name = "order_status")
public class OrderStatus implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    private Long id;
    
    @Basic(optional = false)    
    @Column(name = "status", nullable = false)
    private String status;  
        
    @Column(name = "date_status")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateStatus;
    
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    public OrderStatus() {
    }

    public OrderStatus(String status, Date dateStatus, Long orderId) {
        this.status = status;
        this.dateStatus = dateStatus;
        this.orderId = orderId;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getDateStatus() {
        return dateStatus;
    }

    public void setDateStatus(Date dateStatus) {
        this.dateStatus = dateStatus;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

}
