package apkt.dao.jpa;

import apkt.service.StringVarsService;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.RollbackException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

public class GenericDaoJpa {
    
//    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
//    private static EntityManager em = emf.createEntityManager();
    
    public static <T> T insert(EntityManager em, T object) throws Exception{
        try {
            EntityTransaction tx = em.getTransaction();
            tx.begin();
                em.persist(object);
            tx.commit();
        } catch (RollbackException e) {
             return null;
        }
        return object;

    }
    
    public static <T> String insertWithoutTx(EntityManager em, T object) throws RollbackException{
            
        em.persist(object);
           
        return StringVarsService.OK;

    }
    
    public static <T> int deleteObj(EntityManager em, Long objId, Class<T> tClass) throws Exception{
            
        int deletedCount = 0;
        
        
        Object object = em.find(tClass, objId);

        em.getTransaction().begin();

        em.remove(object);

        em.getTransaction().commit();
                
        return deletedCount;
    }
    
    public static <T> String update(EntityManager em, Class<T> tClass, T object) throws RollbackException{
        
        EntityTransaction tx = em.getTransaction();
        tx.begin();
            em.merge(object);
        tx.commit();

        return StringVarsService.OK;
    }
    
    public static <T> String updateWithoutTx(EntityManager em, Class<T> tClass, T object) throws RollbackException{

        em.merge(object);

        return StringVarsService.OK;

    }
    
    public static <T> T find(EntityManager em, Class<T> tClass, Long id) throws RollbackException{
        T object;
        object = em.find(tClass, id);            
        return object;
    }
    
    public static <T> T findByAttribute (
            EntityManager em,
            Class<T> tClass, 
            String attibute, 
            Object object) throws Exception{
        try {
            T tRes = null;

            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(tClass);

            Metamodel m = em.getMetamodel();
            EntityType<T> classMetaModel = m.entity(tClass);

            Root<T> tRoot = cq.from(tClass);

            cq.select(tRoot).where(cb.equal(tRoot.get(attibute), object));            
            TypedQuery<T> q = em.createQuery(cq);
            tRes = q.getSingleResult();            

            return tRes;
        }catch (NoResultException ex){
            return null;
        }
    }
    
    public static <T> T findByAttributeTwo (
            EntityManager em,
            Class<T> tClass, 
            String attibute_1,
            Object object_1,
            String attibute_2,
            Object object_2
            ) throws Exception{
        try {
            T tRes = null;
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<T> cq = cb.createQuery(tClass);

            Metamodel m = em.getMetamodel();
            EntityType<T> classMetaModel = m.entity(tClass);

            Root<T> tRoot = cq.from(tClass);

            cq.select(tRoot).where(
                    cb.equal(tRoot.get(attibute_1), object_1),
                    cb.equal(tRoot.get(attibute_2), object_2));            
            TypedQuery<T> q = em.createQuery(cq);
            tRes = q.getSingleResult();
            return tRes;
        }catch (NoResultException ex){
            return null;
        }
    }
    
    public static <T> List<T> findListByAttribute(
            EntityManager em,
            Class<T> tClass, 
            String attibute, 
            Object object) throws Exception{
        List<T> tList = null;
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);

        Metamodel m = em.getMetamodel();
        EntityType<T> classMetaModel = m.entity(tClass);

        Root<T> tRoot = cq.from(tClass);

        cq.select(tRoot).where(cb.equal(tRoot.get(attibute), object));            
        TypedQuery<T> q = em.createQuery(cq);
        tList = q.getResultList();            
        
        return tList;
    }
    
    public static <T> List getObjListGeneric(EntityManager em, Class<T> tClass) throws Exception{
        List<T> tList = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);

        Metamodel m = em.getMetamodel();
        EntityType<T> classMetaModel = m.entity(tClass);

        Root<T> tRoot = cq.from(tClass);

        cq.select(tRoot);            
        TypedQuery<T> q = em.createQuery(cq);
        tList = q.getResultList();            
        
        return tList;
    }
    
    public static <T> T getLastEntry(EntityManager em, Class<T> tClass) throws Exception{
        List<T> tList = null;
        T tRes = null;
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);

        Root<T> tRoot = cq.from(tClass);

        cq.select(tRoot);            
        TypedQuery<T> q = em.createQuery(cq);
        tList = q.setMaxResults(1).getResultList();
        if (tList.size() > 0){
            tRes = tList.get(0);
        }
        return tRes;
    }
}
