package apkt.json;

import apkt.backingbean.AuthAux;

public class ObjectJson {
    private Object object;
    private AuthAux authAux;

    public ObjectJson(Object object, AuthAux authAux) {
        this.setObject(object);
        this.authAux = authAux;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
}
