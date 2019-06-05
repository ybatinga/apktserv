package apkt.model;

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
@Table(name = "login")
public class Login implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "username", nullable = false, length = 30)
    private String username;
    
    @Column(name = "doc_num", length = 30)
    private String docNum;
    
    @Column(name = "doc_username")
    private String docUsername;
    
//    @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @Column(name = "email", nullable = false, length = 30)
    private String email;
    
    @Basic(optional = false)
    @Column(name = "mobile_num_state", nullable = false, length = 2)
    private String mobileNumState;
    
    @Basic(optional = false)
    @Column(name = "mobile_num", nullable = false, length = 9)    
    private String mobileNum;
    
    @Basic(optional = false)
    @Column(name = "pass_word", nullable = false, length = 128)
    private String passWord;
    
    @Column(name = "passauxword", length = 30)
    private String passauxword;
    
    @Basic(optional = false)
    @Column(name = "gcm_reg_id", nullable = false, length = 1000)
    private String gcmRegId;
    
//    @Column(name = "device_id")
//    private String deviceId;
//    
//    @Column(name = "sim_serial_number")
//    private String simSerialNumber;
//    
//    @Column(name = "sim_operator")
//    private String simOperator;
    
    @Basic(optional = false)
    @Column(name = "date_signup", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateSignup;
    
    @Column(name = "rsa_pubkey")
    private String rsaPubkey;
    
    @Column(name = "currency_code")
    private String currencyCode;
    
    @Column(name = "lang")
    private String lang;
    
//    @OneToMany( mappedBy = "loginId")        
//    private List<Bank> bankList;
//    
//    @OneToMany( mappedBy = "loginId")        
//    private List<Wallet> walletList;
    
    public Login() {
    }

    public Login(Long id) {
        this.id = id;
    }

    public Login(Long id, String username, String mobileNum, String passWord, Date dateSignup) {
        this.id = id;
        this.username = username;
        this.mobileNum = mobileNum;
        this.passWord = passWord;
        this.dateSignup = dateSignup;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDocNum() {
        return docNum;
    }

    public void setDocNum(String docNum) {
        this.docNum = docNum;
    }

    public String getDocUsername() {
        return docUsername;
    }

    public void setDocUsername(String docUsername) {
        this.docUsername = docUsername;
    }
    
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNumState() {
        return mobileNumState;
    }

    public void setMobileNumState(String mobileNumState) {
        this.mobileNumState = mobileNumState;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public String getPassauxword() {
        return passauxword;
    }

    public void setPassauxword(String passauxword) {
        this.passauxword = passauxword;
    }

    public String getGcmRegId() {
        return gcmRegId;
    }

    public void setGcmRegId(String gcmRegId) {
        this.gcmRegId = gcmRegId;
    }

//    public String getDeviceId() {
//        return deviceId;
//    }
//
//    public void setDeviceId(String deviceId) {
//        this.deviceId = deviceId;
//    }
//
//    public String getSimSerialNumber() {
//        return simSerialNumber;
//    }
//
//    public void setSimSerialNumber(String simSerialNumber) {
//        this.simSerialNumber = simSerialNumber;
//    }
//
//    public String getSimOperator() {
//        return simOperator;
//    }
//
//    public void setSimOperator(String simOperator) {
//        this.simOperator = simOperator;
//    }

    public Date getDateSignup() {
        return dateSignup;
    }

    public void setDateSignup(Date dateSignup) {
        this.dateSignup = dateSignup;
    }

    public String getRsaPubkey() {
        return rsaPubkey;
    }

    public void setRsaPubkey(String rsaPubkey) {
        this.rsaPubkey = rsaPubkey;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

//    public List<Bank> getBankList() {
//        return bankList;
//    }
//
//    public void setBankList(List<Bank> bankList) {
//        this.bankList = bankList;
//    }

//    public List<Wallet> getWalletList() {
//        return walletList;
//    }
//
//    public void setWalletList(List<Wallet> walletList) {
//        this.walletList = walletList;
//    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Login)) {
            return false;
        }
        Login other = (Login) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

}
