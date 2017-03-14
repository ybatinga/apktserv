package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.OpReturn;

public class OpReturnJson {
    private OpReturn opReturn;
    private AuthAux authAux;

    public OpReturnJson(OpReturn opReturn){
        this.opReturn = opReturn;
    }
    
    public OpReturn getOpReturn() {
        return opReturn;
    }

    public void setOpReturn(OpReturn opReturn) {
        this.opReturn = opReturn;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
}
