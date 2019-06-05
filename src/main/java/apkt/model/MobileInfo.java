package apkt.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "mobile_info")
public class MobileInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "line1_number", length = 2147483647)
    private String line1Number;
    @Column(name = "device_id", length = 2147483647)
    private String deviceId;
    @Column(name = "network_country_iso", length = 2147483647)
    private String networkCountryIso;
    @Column(name = "sim_operator_name", length = 2147483647)
    private String simOperatorName;
    @Column(name = "sim_serial_number", length = 2147483647)
    private String simSerialNumber;
    @Column(name = "subscriber_id", length = 2147483647)
    private String subscriberId;
    @Column(name = "device_software_version", length = 2147483647)
    private String deviceSoftwareVersion;
    @Column(name = "network_operator", length = 2147483647)
    private String networkOperator;
    @Column(name = "network_operator_name", length = 2147483647)
    private String networkOperatorName;
    @Column(name = "sim_operator", length = 2147483647)
    private String simOperator;
    @Column(name = "voice_mail_number", length = 2147483647)
    private String voiceMailNumber;
    @Column(name = "call_state")
    private Integer callState;
    @Column(name = "data_state")
    private Integer dataState;
    @Column(name = "sim_state")
    private Integer simState;
    @Column(name = "network_type")
    private Integer networkType;
    @Column(name = "phone_type")
    private Integer phoneType;
//    @JoinColumn(name = "login_id", referencedColumnName = "id", nullable = false)
//    @ManyToOne(optional = false)
//    private Login loginId;
    @Basic(optional = false)
    @Column(name = "login_id", nullable = false)
    private Long loginId;
    
    public MobileInfo() {
    }

    public MobileInfo(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLine1Number() {
        return line1Number;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getNetworkCountryIso() {
        return networkCountryIso;
    }

    public void setNetworkCountryIso(String networkCountryIso) {
        this.networkCountryIso = networkCountryIso;
    }

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getDeviceSoftwareVersion() {
        return deviceSoftwareVersion;
    }

    public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
        this.deviceSoftwareVersion = deviceSoftwareVersion;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public String getSimOperator() {
        return simOperator;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    public String getVoiceMailNumber() {
        return voiceMailNumber;
    }

    public void setVoiceMailNumber(String voiceMailNumber) {
        this.voiceMailNumber = voiceMailNumber;
    }

    public Integer getCallState() {
        return callState;
    }

    public void setCallState(Integer callState) {
        this.callState = callState;
    }

    public Integer getDataState() {
        return dataState;
    }

    public void setDataState(Integer dataState) {
        this.dataState = dataState;
    }

    public Integer getSimState() {
        return simState;
    }

    public void setSimState(Integer simState) {
        this.simState = simState;
    }

    public Integer getNetworkType() {
        return networkType;
    }

    public void setNetworkType(Integer networkType) {
        this.networkType = networkType;
    }

    public Integer getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(Integer phoneType) {
        this.phoneType = phoneType;
    }

    public Long getLoginId() {
        return loginId;
    }

    public void setLoginId(Long loginId) {
        this.loginId = loginId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MobileInfo)) {
            return false;
        }
        MobileInfo other = (MobileInfo) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }
}
