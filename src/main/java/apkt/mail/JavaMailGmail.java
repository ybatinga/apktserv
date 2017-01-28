package apkt.mail;

import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.internet.MimeMessage.RecipientType;

import java.util.*;

public class JavaMailGmail {

    private static String HST = "smtp.gmail.com";
    private static String USR = "apekato.msg@gmail.com";
    private static String PSWR = "GW3ethxc27B9FtkZ"; // ********************** Fill with correct password GW3ethxc27B9FtkZ
    private static String PORT = "465";
    private static String FROM = "apekato.msg@gmail.com";
//    private static String TO = "******@gmail.com";

    private static String STARTTLS = "true";
    private static String AUTH = "true";
    private static String DEBUG = "false";
    private static String SOCKET_FACTORY = "javax.net.ssl.SSLSocketFactory";
//    private static String SUBJECT = "Testing JavaMail API";
//    private static String TEXT = "This is a test message from my java application. Just ignore it";
    
    public static void send(String TO, String SUBJECT, String TEXT) {
        //Use Properties object to set environment properties
        Properties props = new Properties();

        props.put("mail.smtp.host", HST);
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.user", USR);

        props.put("mail.smtp.auth", AUTH);
        props.put("mail.smtp.starttls.enable", STARTTLS);
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
//            message.setText(TEXT);
             // set plain text message
            message.setContent(TEXT, "text/html");
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
        }

    }

}
