package apkt.backingbean;

import apkt.model.Login;
import java.util.Date;

public class LoginBB {

    private Login login;
    private Login loginAux = new Login();
    boolean msgError;

    public LoginBB(Login login) {
        this.login = login;
    }
    
    public LoginBB(
            String gcmRegId,
            String mobileNum, 
            String mobileNumState, 
            String username, 
            String passWord, 
            String email,
            String docNum,
            Date dateSignup,
            String currencyCode,
            String lang
            ) {
        loginAux.setGcmRegId(gcmRegId);
        loginAux.setMobileNum(mobileNum);
        loginAux.setMobileNumState(mobileNumState);
        loginAux.setUsername(username);
        loginAux.setPassWord(passWord);        
        loginAux.setEmail(email);
        loginAux.setDocNum(docNum);
        loginAux.setDateSignup(dateSignup);
        loginAux.setCurrencyCode(currencyCode);
        loginAux.setLang(lang);
    }
    
    @Override
    public String toString() {
        return login.getId()  + "," + login.getUsername() + "," 
                + login.getMobileNumState() + "," + login.getMobileNum() + "," 
                + login.getEmail() + ";";
    }   
    
    public Login getLoginAux() {
        return loginAux;
    }

    public void setLoginAux(Login loginAux) {
        this.loginAux = loginAux;
    }

    public boolean isMsgError() {
        return msgError;
    }

    public void setMsgError(boolean msgError) {
        this.msgError = msgError;
    }

}
