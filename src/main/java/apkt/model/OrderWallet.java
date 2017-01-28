package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
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

@Entity
@Table(name = "order_wallet")
public class OrderWallet implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    
    @Basic(optional = false)
    @Column(name = "address", nullable = false)
    private String address;
    
//    @JoinColumn(name = "login_id", referencedColumnName = "id")
//    @ManyToOne(optional = false)
//    private Login loginId;
    
    @Basic(optional = false)
    @Column(name = "login_id", nullable = false)
    private Long loginId;
    
    @Basic(optional = false)
    @Column(name = "order_id", nullable = false)
    private Long orderId;

    public OrderWallet() {
    }

    public OrderWallet(Wallet wallet) {              
        this.name = wallet.getName();
        this.address = wallet.getAddress();
        this.loginId = wallet.getLoginId();        
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
    
}

