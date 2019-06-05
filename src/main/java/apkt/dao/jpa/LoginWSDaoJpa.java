package apkt.dao.jpa;

import apkt.model.Login;
import apkt.model.MobileInfo;
import apkt.service.StringVarsService;
import java.io.IOException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class LoginWSDaoJpa {

    public Login loginCheck(
            EntityManager em,
                String email,
//                String mobileNumState, 
//                String mobileNum, 
                String password
            ) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Login> cq = cb.createQuery(Login.class);
            Root<Login> loginRoot = cq.from(Login.class);
            cq.where(
                        cb.equal(loginRoot.get("email"), email),
    //                    cb.equal(loginRoot.get("mobileNumState"), mobileNumState),
    //                    cb.equal(loginRoot.get("mobileNum"), mobileNum),
                        cb.equal(loginRoot.get("passWord"), password)
                    );
            TypedQuery<Login> q = em.createQuery(cq);
        
            Login login = q.getSingleResult();
            return login;

        // do not change this catch. And do not add throw NoResultException in the method.
        // There is a verification right after this loginCheck method call that 
        // checks the return result
        } catch (NoResultException n) {
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public static Login emailCheck(
            EntityManager em, String email) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Login> cq = cb.createQuery(Login.class);
            Root<Login> loginRoot = cq.from(Login.class);
            cq.where(
                        cb.equal(loginRoot.get("email"), email)
                    );
            TypedQuery<Login> q = em.createQuery(cq);
        
            Login login = q.getSingleResult();
            return login;

        // do not change this catch. And do not add throw NoResultException in the method.
        // There is a verification right after this loginCheck method call that 
        // checks the return result
        } catch (NoResultException n) {
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public MobileInfo mobileInfoCheck(
            EntityManager em,    
            Long loginId,
            String simSerialNumber,
            String simOperator,
            // data below is not available when there is no cell operator service
//            String networkCountryIso, 
            String subscriberId            
            ) {
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<MobileInfo> cq = cb.createQuery(MobileInfo.class);
            Root<MobileInfo> mobileInfoRoot = cq.from(MobileInfo.class);
            cq.where(
                        cb.equal(mobileInfoRoot.get("loginId"), loginId),
                        cb.equal(mobileInfoRoot.get("simSerialNumber"), simSerialNumber),
                        cb.equal(mobileInfoRoot.get("simOperator"), simOperator),
                        // data below is not available when there is no cell operator service
//                        cb.equal(mobileInfoRoot.get("networkCountryIso"), networkCountryIso),
                        cb.equal(mobileInfoRoot.get("subscriberId"), subscriberId)
                    );
            TypedQuery<MobileInfo> q = em.createQuery(cq);
        
        
            MobileInfo mobileInfo = q.getSingleResult();
            return mobileInfo;

        // do not change this catch. And do not append throw NoResultException to the method.
        // There is a verification right after this loginCheck method call that 
        // checks the return result
        } catch (NoResultException n) {
            return null;
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
    
    public String gcmRegId(
            EntityManager em
            , String gcmRegId
            , String rsaPubkey
            , long userId) throws Exception{
        
        em.getTransaction().begin();
        
            em.createQuery(
                    "UPDATE Login login "
                        + "SET login.gcmRegId =:gcmRegId "
                    + "WHERE login.id =:userId"
                        )
                    .setParameter("gcmRegId", gcmRegId)
                    .setParameter("userId", userId)
                    .executeUpdate();
        
        em.getTransaction().commit();
        
        return StringVarsService.OK;
    }
    
    public static String updatePassAuxWord(
            EntityManager em,
            long userId,
            String passauxword) throws IOException {
        em.getTransaction().begin();
        
            em.createQuery(
                    "UPDATE Login login "
                        + "SET login.passauxword =:passauxword "
                    + "WHERE login.id =:userId"
                        )
                    .setParameter("passauxword", passauxword)
                    .setParameter("userId", userId)
                    .executeUpdate();
        
        em.getTransaction().commit();
        
        return StringVarsService.OK;
    }
    
    public static String updatePassword(
            EntityManager em,
            long userId,
            String passWord) throws IOException {
        em.getTransaction().begin();
        
            em.createQuery(
                    "UPDATE Login login "
                        + "SET login.passWord =:passWord "
                    + "WHERE login.id =:userId"
                        )
                    .setParameter("passWord", passWord)
                    .setParameter("userId", userId)
                    .executeUpdate();
        
        em.getTransaction().commit();
        
        return StringVarsService.OK;
    }
}
