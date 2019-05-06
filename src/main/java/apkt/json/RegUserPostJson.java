package apkt.json;

import apkt.backingbean.AuthAux;
import java.util.Date;

public class RegUserPostJson {

    private Long id;
    private byte[] emailB;
    private byte[] passwordB;
    private String gcmRegId;
    private String username;
    private String email;
    private String docNum;
    private String mobileNumState;
    private String mobileNum;
    private Date dateLogin;
    private String passWord;
    private String deviceId;
    private String simSerialNumber;
    private String simOperator;
    private String line1Number;
    private String simOperatorName;
    private String networkOperator;
    private String subscriberId; // IMSI (International mobile subscriber
    // identity)
    // numero atrelado ao ship (SerialNumber)
    private String networkOperatorName;
    private String networkCountryIso;
    private int callState;
    private int dataState;
    private int simState;
    private String deviceSoftwareVersion;
    private int networkType;
    private int phoneType;
    private String voiceMailNumber;
    private int dataActivity;
    private String currencyCode;
    private String lang;
    private AuthAux authAux;

    public RegUserPostJson() {
    }

    public RegUserPostJson(String mobileNum, String mobileNumState, String username, String passWord, String email, String docNum, Date dateLogin, String deviceId, String simSerialNumber, String simOperator, String line1Number, String simOperatorName, String networkOperator, String subscriberId, String networkOperatorName, String networkCountryIso, int callState, int dataState, int simState, String deviceSoftwareVersion, int networkType, int phoneType, String voiceMailNumber, int dataActivity, String gcmRegId) {
        this.mobileNum = mobileNum;
        this.mobileNumState = mobileNumState;
        this.username = username;
        this.passWord = passWord;
        this.email = email;
        this.docNum = docNum;
        this.dateLogin = dateLogin;
        this.deviceId = deviceId;
        this.simSerialNumber = simSerialNumber;
        this.simOperator = simOperator;
        this.line1Number = line1Number;
        this.simOperatorName = simOperatorName;
        this.networkOperator = networkOperator;
        this.subscriberId = subscriberId;
        this.networkOperatorName = networkOperatorName;
        this.networkCountryIso = networkCountryIso;
        this.callState = callState;
        this.dataState = dataState;
        this.simState = simState;
        this.deviceSoftwareVersion = deviceSoftwareVersion;
        this.networkType = networkType;
        this.phoneType = phoneType;
        this.voiceMailNumber = voiceMailNumber;
        this.dataActivity = dataActivity;
        this.gcmRegId = gcmRegId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
public byte[] getEmailB() {
        return emailB;
    }

    public void setEmailB(byte[] emailB) {
        this.emailB = emailB;
    }

    public byte[] getPasswordB() {
        return passwordB;
    }

    public void setPasswordB(byte[] passwordB) {
        this.passwordB = passwordB;
    }

    public String getGcmRegId() {
        return gcmRegId;
    }

    public void setGcmRegId(String gcmRegId) {
        this.gcmRegId = gcmRegId;
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

    public Date getDateLogin() {
        return dateLogin;
    }

    public void setDateLogin(Date dateLogin) {
        this.dateLogin = dateLogin;
    }

    public String getSimSerialNumber() {
        return simSerialNumber;
    }

    public void setSimSerialNumber(String simSerialNumber) {
        this.simSerialNumber = simSerialNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSimOperator() {
        return simOperator;
    }

    public void setSimOperator(String simOperator) {
        this.simOperator = simOperator;
    }

    public String getLine1Number() {
        return line1Number;
    }

    public void setLine1Number(String line1Number) {
        this.line1Number = line1Number;
    }

    public String getSimOperatorName() {
        return simOperatorName;
    }

    public void setSimOperatorName(String simOperatorName) {
        this.simOperatorName = simOperatorName;
    }

    public String getNetworkOperator() {
        return networkOperator;
    }

    public void setNetworkOperator(String networkOperator) {
        this.networkOperator = networkOperator;
    }

    public String getSubscriberId() {
        return subscriberId;
    }

    public void setSubscriberId(String subscriberId) {
        this.subscriberId = subscriberId;
    }

    public String getNetworkOperatorName() {
        return networkOperatorName;
    }

    public void setNetworkOperatorName(String networkOperatorName) {
        this.networkOperatorName = networkOperatorName;
    }

    public String getNetworkCountryIso() {
        return networkCountryIso;
    }

    public void setNetworkCountryIso(String networkCountryIso) {
        this.networkCountryIso = networkCountryIso;
    }

    public int getCallState() {
        return callState;
    }

    public void setCallState(int callState) {
        this.callState = callState;
    }

    public int getDataState() {
        return dataState;
    }

    public void setDataState(int dataState) {
        this.dataState = dataState;
    }

    public int getSimState() {
        return simState;
    }

    public void setSimState(int simState) {
        this.simState = simState;
    }

    public String getDeviceSoftwareVersion() {
        return deviceSoftwareVersion;
    }

    public void setDeviceSoftwareVersion(String deviceSoftwareVersion) {
        this.deviceSoftwareVersion = deviceSoftwareVersion;
    }

    public int getNetworkType() {
        return networkType;
    }

    public void setNetworkType(int networkType) {
        this.networkType = networkType;
    }

    public int getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(int phoneType) {
        this.phoneType = phoneType;
    }

    public String getVoiceMailNumber() {
        return voiceMailNumber;
    }

    public void setVoiceMailNumber(String voiceMailNumber) {
        this.voiceMailNumber = voiceMailNumber;
    }

    public int getDataActivity() {
        return dataActivity;
    }

    public void setDataActivity(int dataActivity) {
        this.dataActivity = dataActivity;
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

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
    
}
