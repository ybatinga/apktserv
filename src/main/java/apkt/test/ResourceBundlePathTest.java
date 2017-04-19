package apkt.test;

import apkt.service.ProjService;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResourceBundlePathTest {
public static void main(String[] args) {
    try {
        //All Files	/Users/eizesazake/Documents/NetBeansProjects/apekatoserv/src/main/webapp/WEB-INF/classes
        ///Users/eizesazake/Documents/NetBeansProjects/apekatoserv/src/main/java/apkt/test/ResourceBundlePathTest.java
//        File file = new File("../../../webapp/WEB-INF/classes");
        File file = new File(ProjService.RBPATH);
        URL[] urls = {file.toURI().toURL()};
        ClassLoader loader = new URLClassLoader(urls);
        ResourceBundle rb;
        String language = "en";
        if (language.equals("pt")){
            rb = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("pt"), loader);
        } else {
            rb = ResourceBundle.getBundle("SystemMessages", Locale.getDefault(), loader);
        }
        String emailSubject = rb.getString("email_body_op_return_subject_notarize");
        System.out.println(emailSubject);
    } catch (Exception ex) {
        System.out.println(ex.toString());
    }
}
    
}
