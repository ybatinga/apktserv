/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.ws;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author eizesazake
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(apkt.ws.CurrencyCodeWS.class);
        resources.add(apkt.ws.OrderListWS.class);
        resources.add(apkt.ws.GenericGet.class);
        resources.add(apkt.ws.GenericPost.class);
        resources.add(apkt.ws.GenericResource.class);
        resources.add(apkt.ws.GenericResourceHttpDelete.class);
        resources.add(apkt.ws.LoginWS.class);
        resources.add(apkt.ws.OrderWS.class);
        resources.add(apkt.ws.OrderListWS.class);
        resources.add(apkt.ws.OrderListMarketWS.class);
        resources.add(apkt.ws.PasswordCodeWS.class);
        resources.add(apkt.ws.PasswordForgotWS.class);
        resources.add(apkt.ws.PasswordResetWS.class);
        resources.add(apkt.ws.PymntMthdDeleteWS.class);
        resources.add(apkt.ws.PymntMthdListWS.class);
        resources.add(apkt.ws.PymntMthdWS.class);
        resources.add(apkt.ws.RegUserWS.class);
        resources.add(apkt.ws.SimSerMobNumRegWS.class);
        resources.add(apkt.ws.TxCancelWS.class);
        resources.add(apkt.ws.TxCreateWS.class);
        resources.add(apkt.ws.TxExecuteWS.class);
        resources.add(apkt.ws.WalletWS.class);
        resources.add(apkt.ws.WalletDeleteWS.class);
        resources.add(apkt.ws.WebHookWS.class);
    }
    
}
