package apkt.mail;

import apkt.service.StringVarsService;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;

public class JavaMail {

    private static String HST = "smtp.zoho.com";
    private static String USR = "amsg@apekato.com";
    private static String PSWR = "RPN'As{SAqg&u2.";
    private static String PORT = "465";
    private static String FROM = "app@apekato.com";

    private static String AUTH = "true";
    private static String DEBUG = "true";
    private static String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
//    private static String SUBJECT = "Testing JavaMail API";
//    private static String TEXT = "This is a test message from my java application. Just ignore it";
    
    public static String send(String TO, String SUBJECT, String TEXT) {

        //Use Properties object to set environment properties
        Properties props = new Properties();

        props.put("mail.smtp.host", HST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USR);

        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.debug", DEBUG);

        props.put("mail.smtp.socketFactory.port", PORT);
        props.put("mail.smtp.socketFactory.class", SOCKET_FACTORY);
        props.put("mail.smtp.socketFactory.fallback", "false");

        try {

            //Obtain the default mail session
            Session session = Session.getDefaultInstance(props, null);
            session.setDebug(true);

            //Construct the mail message
            MimeMessage message = new MimeMessage(session);
             // set plain text message
//            message.setContent(TEXT, "text/html");
            message.setText(TEXT, "UTF-8","html"); // formats portuguese accentuation correctly in zoho mail 
            message.setSubject(SUBJECT);
            message.setFrom(new InternetAddress(FROM));
            message.addRecipient(RecipientType.TO, new InternetAddress(TO));
            message.saveChanges();
           

            //Use Transport to deliver the message
            Transport transport = session.getTransport("smtp");
            transport.connect(HST, USR, PSWR);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            
            

        } catch (Exception e) {
            e.printStackTrace();
            return e.toString();
        }
        return StringVarsService.OK;

    }

}
