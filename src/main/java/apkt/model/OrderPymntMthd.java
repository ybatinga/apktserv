package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "order_pymnt_mthd")
public class OrderPymntMthd implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Column(name = "pymnt_mthd_type")
    private String pymntMthdType;
    
    @Column(name = "bank_code")
    private String bankCode;
    
    @Basic(optional = false)
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "account_type")
    private String accountType;
    
    @Column(name = "branch")
    private String branch;
    
    @Column(name = "account")
    private String account;

    @Basic(optional = false)
    @Column(name = "login_id", nullable = false)
    private Long loginId;

    @Column(name = "pymnt_mthd_selected")
    private boolean pymntMthdSelected;
    
    @Column(name = "currency_code")
    private String currencyCode;
    
    @Column(name = "pymnt_details")
    private String pymntDetails;
    
    // set variable as protect for Gson variable exclusion strategy
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable=false)
    @ManyToOne(optional=false)
    protected Order orderId;    
    
    public OrderPymntMthd() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPymntMthdType() {
        return pymntMthdType;
    }

    public void setPymntMthdType(String pymntMthdType) {
        this.pymntMthdType = pymntMthdType;
    }
    
    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    public boolean isPymntMthdSelected() {
        return pymntMthdSelected;
    }

    public void setPymntMthdSelected(boolean pymntMthdSelected) {
        this.pymntMthdSelected = pymntMthdSelected;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public String getPymntDetails() {
        return pymntDetails;
    }

    public void setPymntDetails(String pymntDetails) {
        this.pymntDetails = pymntDetails;
    }
    
    public Order getOrderId() {
        return orderId;
    }

    public void setOrderId(Order orderId) {
        this.orderId = orderId;
    }

}

