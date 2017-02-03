package apkt.ws;

import apkt.dao.jpa.GenericDaoJpa;
import apkt.json.ListOrderJson;
import apkt.mail.JavaMailThread;
import apkt.model.Login;
import apkt.model.Order;
import apkt.model.OrderPymntMthd;
import apkt.model.OrderStatus;
import apkt.model.OrderWallet;
import apkt.dao.jpa.ServiceDaoJpa;
import apkt.service.CalcVarsService;
import apkt.service.StringVarsService;
import com.google.gson.Gson;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;

@Path("order")
public class OrderWS {

    public OrderWS() {
    }

    //    @GET
//    @Path("/{jsonclass}")
//    @Produces("application/json")
//    public String getJson(@PathParam("jsonclass") String jsonclass) {
    @POST    
    @Consumes("application/json")
    @Produces("application/json")
    public String getJson(String jsonclass) {
        try {
            jsonclass = URLDecoder.decode(jsonclass, "UTF-8");

            ListOrderJson listOrderJson = new Gson().fromJson(jsonclass, ListOrderJson.class);
    //        ListOrderJson listOrderJson = new Gson().fromJson(jsonclass, ListOrderJson.class);        
            
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("apekato");
            EntityManager em = emf.createEntityManager();

            Login loginAuth = ServiceDaoJpa.authUser(em, listOrderJson.getAuthAux());
            // update language 
            GenericDaoJpa.update(em, Login.class, loginAuth);
            listOrderJson.setAuthAux(null);
            if (loginAuth == null){
                return null;
            }
            
            Order order = listOrderJson.getOrder();
            
            List<Order> list = null;
            Date date = new Date();
            
            BigDecimal price = order.getPrice();
            BigDecimal amount = order.getAmount();
            
            if (
//                    price.compareTo(CalcVarsService.LIMIT_PRICE) < 0 &&
                    amount.compareTo(CalcVarsService.LIMIT_AMOUNT_MAX) < 0 && 
                    amount.compareTo(CalcVarsService.LIMIT_AMOUNT_MIN) > 0){
                EntityTransaction tx = em.getTransaction();
            
                order.setUpdatedAt(date);

                // tx.begin() and tx.commit() is not used here because order insert needs to commit to return orderId,
                // that will be used to update OrderPymntMthd and OrderWallet with orderId
                Order orderRes = GenericDaoJpa.insert(em, order);
                
                if (order.getType().equals(Order.OrderType.ORDER_TYPE_BUY)){
                    OrderWallet orderWallet = orderRes.getOrderWalletId();
                    orderWallet.setOrderId(orderRes.getId());
                    GenericDaoJpa.update(em, OrderWallet.class, orderWallet);
                }

                if (orderRes.getId() != null) {
                    for(OrderPymntMthd opm : order.getOrderPymntMthdList()){
                        opm.setOrderId(order);
                        GenericDaoJpa.update(em, OrderPymntMthd.class, opm);
                    }
                    OrderStatus orderStatus = new OrderStatus(
                        orderRes.getStatus(),
                        date,
                        orderRes.getId());
                    GenericDaoJpa.insert(em, orderStatus);
                }

                em.close(); emf.close();
            }
            
            return StringVarsService.OK;
        } catch (Exception ex) {
            JavaMailThread javaMailThread_1 = new JavaMailThread("desenv.notes@gmail.com", Order.OrderStatuses.ORDER_STATUS_OPEN, ex.toString());
            ExecutorService threadExecutor = Executors.newCachedThreadPool();
            threadExecutor.execute(javaMailThread_1);
            threadExecutor.shutdown();
            return null;
        }
        
    }

}
