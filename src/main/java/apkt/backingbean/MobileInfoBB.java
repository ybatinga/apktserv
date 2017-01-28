package apkt.backingbean;

import apkt.model.Login;
import apkt.model.MobileInfo;

public class MobileInfoBB {
    
    private MobileInfo mobileInfoAux = new MobileInfo();
    
    public MobileInfoBB(Login login, String line1Number, String deviceId, String networkCountryIso, String simOperatorName, String simSerialNumber, String subscriberId, String deviceSoftwareVersion, String networkOperator, String networkOperatorName, String simOperator, String voiceMailNumber, Integer callState, Integer dataState, Integer simState, Integer networkType, Integer phoneType/*, long loginId_int*/) {
        mobileInfoAux.setLoginId(login.getId());
        mobileInfoAux.setLine1Number(line1Number);
        mobileInfoAux.setDeviceId(deviceId);
        mobileInfoAux.setNetworkCountryIso(networkCountryIso);
        mobileInfoAux.setSimOperatorName(simOperatorName);
        mobileInfoAux.setSimSerialNumber(simSerialNumber);
        mobileInfoAux.setSubscriberId(subscriberId);
        mobileInfoAux.setDeviceSoftwareVersion(deviceSoftwareVersion);
        mobileInfoAux.setNetworkOperator(networkOperator);
        mobileInfoAux.setNetworkOperatorName(networkOperatorName);
        mobileInfoAux.setSimOperator(simOperator);
        mobileInfoAux.setVoiceMailNumber(voiceMailNumber);
        mobileInfoAux.setCallState(callState);
        mobileInfoAux.setDataState(dataState);
        mobileInfoAux.setSimState(simState);
        mobileInfoAux.setNetworkType(networkType);
        mobileInfoAux.setPhoneType(phoneType);
//        mobileInfoAux.loginId_int = loginId_int;
    }

    public MobileInfo getMobileInfoAux() {
        return mobileInfoAux;
    }

    public void setMobileInfoAux(MobileInfo mobileInfoAux) {
        this.mobileInfoAux = mobileInfoAux;
    }
    
}
