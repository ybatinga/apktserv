 
package apkt.test;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.model.Address;
import apkt.model.Order;
import apkt.model.OrderStatus;
import apkt.model.Wallet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FindByAttributesTwoDaoTest {
    public static void main(String[] args) {
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
    //        List<String> addressList = new ArrayList<String>();
    //        addressList.add("C5FkzfDpXfvPq4rNe1qPgKrKPfqYjjVoiv");
    //        addressList.add("DdnD5a8dj5FgDf32KB8v1JBgK9HGbaX1EX");
    //        addressList.add("BvSGE4Tqg4baVkDKLYGxbTCHREsfqMujft");
    //
    //        for (String s : addressList){
    //            try {  
    //                Address o = GenericDaoJpa.findByAttributeTwo(em, Address.class, "address", s, "type", Address.AddressType.ADDRESS_TYPE_MULTISIG);
    //            } catch (Exception ex) {
    //                Logger.getLogger(FindByAttrib  utesTwoDaoTest.class.getName()).log(Level.SEVERE, null, ex);
    //            }
    //        }
            
            OrderStatus orderStatus = new OrderStatus();
            orderStatus.setDateStatus(new Date());
            orderStatus.setOrderId(new Long("45"));
            orderStatus.setStatus("sdfsdf");
            GenericDaoJpa.insert(em, orderStatus);
            
//            Wallet w = new Wallet();
//            w.setAddress("sdfa");
//            w.setLoginId(new Long("1"));
//            w.setName("sdf");
//            GenericDaoJpa.insert(em, w); 
            
                        em.close(); emf.close();

        } catch (Exception ex) {
            Logger.getLogger(FindByAttributesTwoDaoTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
