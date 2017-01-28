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
@Table(name = "pymnt_mthd")
public class PymntMthd implements Serializable {
    
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
    
    @Column(name = "currency_code")
    private String currencyCode;
    
    @Column(name = "pymnt_details")
    private String pymntDetails;
    
    @Basic(optional = false)
    @Column(name = "login_id", nullable = false)
    private Long loginId;
    
    public PymntMthd() {
    }

    public PymntMthd(Long id) {
        this.id = id;
    }

    public PymntMthd(Long id, String name) {
        this.id = id;
        this.name = name;
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
    
    public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }
    
    public static class AccountType {
        public static final String CONTA_CORRENTE = "C/C";
        public static final String CONTA_POUPANCA = "Poupança";
    }
    
    public static class PymntMthdType {
        public static final String PYMNT_MTHD_TYPE_BANK_TRANSFER = "BANK_TRANSFER";
        public static final String PYMNT_MTHD_TYPE_DETAILED = "DETAILED";
    }

}
