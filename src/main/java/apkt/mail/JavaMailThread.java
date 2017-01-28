package apkt.mail;

public class JavaMailThread implements Runnable{

    private String TO;
    private String SUBJECT;
    private String TEXT;

    public JavaMailThread(String TO, String SUBJECT, String TEXT) {
        this.TO = TO;
        this.SUBJECT = SUBJECT;
        this.TEXT = TEXT;
    }
    
    @Override
    public void run() {
        JavaMailGun.send(TO, SUBJECT, TEXT);
    }
    
}
