package apkt.dao.jpa;

import apkt.model.Login;
import apkt.model.MobileInfo;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class AuthentUniqueVarsDaoJpa {
    
    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
    // Affected classes:
    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsDaoJpa (SERVER), AuthentUniqueVarsWS (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
    public static List verifyUniqueVars(
            EntityManager em, 
            String email, 
            String username) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Login> cq = cb.createQuery(Login.class);
        Root<Login> loginRoot = cq.from(Login.class);                
        // using metamodel with Login_
        cq.where(
                cb.or(
                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
                    // Affected classes:
                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsDaoJpa (SERVER), AuthentUniqueVarsWS (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
                    cb.equal(loginRoot.get("email"), email),
                    cb.equal(loginRoot.get("username"), username)
                )
            );
        TypedQuery<Login> q = em.createQuery(cq);
        
        
        try {
            List list = q.getResultList();
            return list;

        // leave this try-catch exception as it is. And do not change it to throws exception,
        // because it is used to verify users that are already registered in the system
        } catch (NoResultException n) {
            return null;
        }
    }
    
    public static List verifyUniqueVarsMobileInfo(
            EntityManager em, 
            String simSerialNumber) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<MobileInfo> cq = cb.createQuery(MobileInfo.class);
        Root<MobileInfo> root = cq.from(MobileInfo.class);                
        // using metamodel with Login_
        cq.where(
                cb.or(
                    // Code is used to verify SimSerialNumber or Email or Username is alredy registered
                    // Affected classes:
                    // AutentUserNameActivity (ANDROID), CadastrarMobileInfoWs (SERVER), AuthentUniqueVarsDaoJpa (SERVER), AuthentUniqueVarsWS (SERVER), LoginBB (SERVER), AuthentUniqueVarsDaoJpaTest (SERVER).
                    cb.equal(root.get("simSerialNumber"), simSerialNumber)
                )
            );
        TypedQuery<MobileInfo> q = em.createQuery(cq);
        
        
        try {
            List list = q.getResultList();
            return list;

        // leave this try-catch exception as it is. And do not change it to throws exception,
        // because it is used to verify users that are already registered in the system
        } catch (NoResultException n) {
            return null;
        }
    }
    
}
