package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.OrderJson;
import apkt.model.Login;
import apkt.model.Order;
import apkt.model.OrderStatus;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.json.StringResultJson;
import com.google.gson.Gson;
import java.net.URLDecoder;
import java.util.Date;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("txCancel")
public class TxCancelWS {

    public TxCancelWS() {
    }

    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();
            
            OrderJson orderJson = new Gson().fromJson(jsonclass, OrderJson.class);
            Order orderPost = orderJson.getOrder();
            Date date = new Date();
            
            Login loginAuth = ServiceDaoJpa.authUser(em, orderJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            orderJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
            
            Order order = GenericDaoJpa.find(em, Order.class, new Long(orderPost.getId()));
            order.setUpdatedAt(date);
            order.setStatus(Order.OrderStatuses.ORDER_STATUS_TX_CANCELED);
            EntityTransaction tx = em.getTransaction();
            tx.begin();
            
                String result = GenericDaoJpa.updateWithoutTx(em, Order.class, order);

                OrderStatus orderStatus = new OrderStatus(
                        order.getStatus(),
                        date,
                        order.getId());
                GenericDaoJpa.insertWithoutTx(em, orderStatus);
            tx.commit();
            
            StringResultJson stringResultJson = new StringResultJson();
            stringResultJson.setResult("txCancel");
            String res = new Gson().toJson(stringResultJson);        
            return res;        
        } catch (Exception ex){
            return null;
        }
    }
}
