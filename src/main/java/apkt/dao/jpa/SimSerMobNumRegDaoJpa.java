package apkt.dao.jpa;

import apkt.backingbean.MobileInfoBB;
import apkt.json.RegUserPostJson;
import apkt.model.Login;
import apkt.model.MobileInfo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class SimSerMobNumRegDaoJpa {

    public Login update(RegUserPostJson regUserPostJson) throws NoResultException, Exception{
        
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
        EntityManager em = emf.createEntityManager();

        Login loginResult = null;

        em.getTransaction().begin();

            em.createQuery(
                    "update Login login "
                    + "set "
//                    + "login.deviceId =:deviceId, "                    
//                    + "login.simSerialNumber =:simSerialNumber, "
//                    + "login.simOperator =:simOperator, "
                    + "login.mobileNumState =:mobileNumState, "
                    + "login.mobileNum =:mobileNum "
                    + "where login.id =:id")
//                    .setParameter("deviceId", regUserPostJson.getDeviceId())
//                    .setParameter("simSerialNumber", regUserPostJson.getSimSerialNumber())
//                    .setParameter("simOperator", regUserPostJson.getSimOperator())
                    .setParameter("mobileNumState", regUserPostJson.getMobileNumState())
                    .setParameter("mobileNum", regUserPostJson.getMobileNum())
                    .setParameter("id", regUserPostJson.getId())
                    .executeUpdate();

            Login login = ServiceDaoJpa.getUser(em, regUserPostJson.getId());
            MobileInfo mobileInfoExists = verifyMobileInfoExists(em, login);

            // verifies if MobileInfo doesn't exist yet, because a business user 
            // registered through the web platform
            if (mobileInfoExists == null){

                // sets mobileInfo variables in MobileInfoBB constructor
                MobileInfoBB mobileInfo = new MobileInfoBB(
                        login,
                        regUserPostJson.getLine1Number(),
                        regUserPostJson.getDeviceId(),
                        regUserPostJson.getNetworkCountryIso(),
                        regUserPostJson.getSimOperatorName(),
                        regUserPostJson.getSimSerialNumber(),
                        regUserPostJson.getSubscriberId(),
                        regUserPostJson.getDeviceSoftwareVersion(),
                        regUserPostJson.getNetworkOperator(),
                        regUserPostJson.getNetworkOperatorName(),
                        regUserPostJson.getSimOperator(),
                        regUserPostJson.getVoiceMailNumber(),
                        regUserPostJson.getCallState(),
                        regUserPostJson.getDataState(),
                        regUserPostJson.getSimState(),
                        regUserPostJson.getNetworkType(),
                        regUserPostJson.getPhoneType());


                // persists MobileInfo
                RegUserDaoJpa.insert(em, mobileInfo.getMobileInfoAux());


            }else{

                em.createQuery(
                        "update MobileInfo mobileInfo "
                        + "set "
                        + "mobileInfo.line1Number =:line1Number, "                        
                        + "mobileInfo.deviceId =:deviceId, "                    
                        + "mobileInfo.networkCountryIso =:networkCountryIso, "
                        + "mobileInfo.simOperatorName =:simOperatorName, "
                        + "mobileInfo.simSerialNumber =:simSerialNumber, "
                        + "mobileInfo.subscriberId =:subscriberId, "
                        + "mobileInfo.deviceSoftwareVersion =:deviceSoftwareVersion, "
                        + "mobileInfo.networkOperator =:networkOperator, "
                        + "mobileInfo.networkOperatorName =:networkOperatorName, "
                        + "mobileInfo.simOperator =:simOperator, "                        
                        + "mobileInfo.voiceMailNumber =:voiceMailNumber, "
                        + "mobileInfo.callState =:callState, "
                        + "mobileInfo.dataState =:dataState, "
                        + "mobileInfo.simState =:simState, "
                        + "mobileInfo.networkType =:networkType, "
                        + "mobileInfo.phoneType =:phoneType "
                        + "where mobileInfo.loginId =:loginId")
                        .setParameter("line1Number", regUserPostJson.getLine1Number())
                        .setParameter("deviceId", regUserPostJson.getDeviceId())
                        .setParameter("networkCountryIso", regUserPostJson.getNetworkCountryIso())
                        .setParameter("simOperatorName", regUserPostJson.getSimOperatorName())                        
                        .setParameter("simSerialNumber", regUserPostJson.getSimSerialNumber())
                        .setParameter("subscriberId", regUserPostJson.getSubscriberId())
                        .setParameter("deviceSoftwareVersion", regUserPostJson.getDeviceSoftwareVersion())
                        .setParameter("networkOperator", regUserPostJson.getNetworkOperator())
                        .setParameter("networkOperatorName", regUserPostJson.getNetworkOperatorName())
                        .setParameter("simOperator", regUserPostJson.getSimOperator())                        
                        .setParameter("voiceMailNumber", regUserPostJson.getVoiceMailNumber())
                        .setParameter("callState", regUserPostJson.getCallState())
                        .setParameter("dataState", regUserPostJson.getDataState())
                        .setParameter("simState", regUserPostJson.getSimState())
                        .setParameter("networkType", regUserPostJson.getNetworkType())
                        .setParameter("phoneType", regUserPostJson.getPhoneType())
                        .setParameter("loginId", login.getId())
                        .executeUpdate();


            }
        em.getTransaction().commit();

        // gets Login, which will be used to identify the userType
        loginResult = ServiceDaoJpa.getUser(regUserPostJson.getId());
        
        return loginResult;
        
    }
    
    public MobileInfo verifyMobileInfoExists(
            EntityManager em, 
            Login login
            ) throws NoResultException{

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MobileInfo> cq = cb.createQuery(MobileInfo.class);
        Root<MobileInfo> mobileInfoRoot = cq.from(MobileInfo.class);                
        // using metamodel with Login_
        cq.where(
                cb.or(
                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
                    // Affected classes:
                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsDaoJpa (SERVER), AuthentUniqueVarsWS (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
                    cb.equal(mobileInfoRoot.get("loginId"), login.getId())
                )
            );
        TypedQuery<MobileInfo> q = em.createQuery(cq);
        MobileInfo mobileInfo = q.getSingleResult();
        return mobileInfo;

    }
    
}

