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
        resources.add(apkt.ws.AppVersionCheckWS.class);
        resources.add(apkt.ws.GenericGetBasic.class);
        resources.add(apkt.ws.GenericPost.class);
        resources.add(apkt.ws.GenericResource.class);
        resources.add(apkt.ws.GenericResourceHttpDelete.class);
        resources.add(apkt.ws.LoginWS.class);
        resources.add(apkt.ws.OpReturnListWS.class);
        resources.add(apkt.ws.OpReturnRequestWS.class);
        resources.add(apkt.ws.PasswordCodeWS.class);
        resources.add(apkt.ws.PasswordForgotWS.class);
        resources.add(apkt.ws.PasswordResetWS.class);
    }
    
}
