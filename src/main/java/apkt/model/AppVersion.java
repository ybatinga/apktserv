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
@Table(name = "app_version")
public class AppVersion implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic(optional = false)
    @Column(name = "app_version", nullable = false)
    private String appVersion;
    @Column(name = "date_version", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateVersion;
    @Column(name = "verify_version")
    private boolean verifyVersion;
    @Column(name = "app_name", nullable = false)
    private String appName;

    public AppVersion() {
    }

    public AppVersion(String appVersion, Date dateVersion) {
        this.appVersion = appVersion;
        this.dateVersion = dateVersion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public Date getDateVersion() {
        return dateVersion;
    }

    public void setDateVersion(Date dateVersion) {
        this.dateVersion = dateVersion;
    }
    
    public boolean isVerifyVersion() {
        return verifyVersion;
    }

    public void setVerifyVersion(boolean verifyVersion) {
        this.verifyVersion = verifyVersion;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
