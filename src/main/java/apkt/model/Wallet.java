package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "wallet")
public class Wallet implements Serializable {
    
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
 
    public Wallet() {
    }

    public Wallet(Long id) {
        this.id = id;
    }

    public Wallet(Long id, String name, String address) {
        this.id = id;
        this.name = name;
        this.address = address;
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

}
