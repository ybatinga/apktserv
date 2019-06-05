package apkt.dao.jpa;

import apkt.backingbean.AuthAux;
import apkt.cypher.RsaCypher;
import apkt.model.Login;
import apkt.model.MobileInfo;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

public class ServiceDaoJpa {
    
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
    private static EntityManager em = emf.createEntityManager();
    
    public static Login getUser(EntityManager em, long userId) {
        return (Login) em.createQuery(
                "select login from Login login where login.id =:userId ").setParameter("userId", userId).getSingleResult();
    }
    
    public static Login getUser(long userId) {
        return (Login) em.createQuery(
                "select login from Login login where login.id =:userId ").setParameter("userId", userId).getSingleResult();
    }
    
    public static Login authUser(
            EntityManager em,
            AuthAux authAux) throws IOException {
        LoginWSDaoJpa loginWSDaoJpa = new LoginWSDaoJpa();
        
        String email = new String (RsaCypher.decryptData(authAux.getEmailB()));
        // encodes password into bytes 
        byte[] encoded = Base64.encodeBase64(RsaCypher.decryptData(authAux.getPasswordB()));
        // decodes password into string with sha512Hex hash
        String sha512HexPassword = DigestUtils.sha512Hex(encoded);

        Login login = loginWSDaoJpa.loginCheck(em, email, sha512HexPassword);

        // this validation is in LoginWS and DaoJapService
//        if (email.equals("desenv.notes@gmail.com")){
//            authAux.setSimSerialNumber("8955044rjsclgried009");
//        }
        
        MobileInfo mobileInfo = loginWSDaoJpa.mobileInfoCheck(
                em, 
                login.getId(),
                authAux.getSimSerialNumber(),
                authAux.getSimOperator(),
                // data below is not available when there is no cell operator service
//                authAux.getNetworkCountryIso(), 
                authAux.getSubscriberId()
                );
        
        if (login != null && mobileInfo != null){
            return login;
        } else{
            return null;
        }
    }
    
    public static <T> List getObjList(EntityManager em, Class<T> tClass, Long id, String loginIdColumn) throws Exception{
        List<T> tList = null;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);

        Metamodel m = em.getMetamodel();
        EntityType<T> classMetaModel = m.entity(tClass);         

        Root<T> tRoot = cq.from(tClass);

//            Join<T, Login> login = tRoot.join(classMetaModel.getSingularAttribute(loginIdColumn, Login.class));

//            cq.select(tRoot).where(login.get("id").in(id));
        if (id != null && loginIdColumn != null){
            cq.select(tRoot).where(cb.equal(tRoot.get(loginIdColumn), id));
        }
        cq.orderBy(cb.desc(tRoot.get("id")));
        TypedQuery<T> q = em.createQuery(cq);
        tList = q.getResultList();            
        
        return tList;
    }
    
    public static <T> List getObjList(
            EntityManager em, Class<T> tClass, 
            Long id, String loginIdColumn,
            String stringValue, String stringColumnName) throws Exception{
        
        List<T> tList = null;

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);

        Metamodel m = em.getMetamodel();
        EntityType<T> classMetaModel = m.entity(tClass);         

        Root<T> tRoot = cq.from(tClass);
        
        if (
                (id != null && loginIdColumn != null)
                && (stringValue != null && stringColumnName != null)
        ){
            cq.select(tRoot).where(
                cb.equal(tRoot.get(loginIdColumn), id),
                cb.equal(tRoot.get(stringColumnName), stringValue));
        } else if (
                (id != null && loginIdColumn != null)
                && (stringValue == null && stringColumnName == null)
        ){
            cq.select(tRoot).where(
                cb.equal(tRoot.get(loginIdColumn), id));
        } else if (
                (id == null && loginIdColumn == null)
                && (stringValue != null && stringColumnName != null)
        ){
            cq.select(tRoot).where(cb.equal(tRoot.get(stringColumnName), stringValue));
        } 
        cq.orderBy(cb.desc(tRoot.get("id")));
        
        em.getEntityManagerFactory().getCache().evictAll();
        
        TypedQuery<T> q = em.createQuery(cq);
        tList = q.getResultList();            
        
        return tList;
    }
    
}
