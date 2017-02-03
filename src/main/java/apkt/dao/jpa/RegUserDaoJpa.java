package apkt.dao.jpa;

import apkt.model.Login;
import apkt.service.StringVarsService;
import javax.persistence.EntityManager;

public class RegUserDaoJpa extends GenericDaoJpa{

    public RegUserDaoJpa() {
    }
    
    public String regUser(EntityManager em, Login login) {
        
        try {
            em.getTransaction().begin();
            em.persist(login);
            em.getTransaction().commit();
        // leave this try-catch exception as it is. And do not change throws exception,
        // because it is used to verify data already registered in the system
        } catch (javax.persistence.RollbackException dbe) {
            
            String[] split = dbe.toString().split(" ");
            
            if (split[22].equals("(username)=(username)")){
                System.out.println("username");
            }
            else if (split[22].equals("(sim_serial_number)=(simSerialNumber)")){                
                System.out.println("serianumber");
            }
        }
        return StringVarsService.OK;
    }

}
