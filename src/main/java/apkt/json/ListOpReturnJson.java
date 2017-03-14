package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.OpReturn;
import java.util.List;

public class ListOpReturnJson {
    private Long userId;
    private List<OpReturn> opReturnList;
    private AuthAux authAux;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<OpReturn> getOpReturnList() {
        return opReturnList;
    }

    public void setOpReturnList(List<OpReturn> opReturnList) {
        this.opReturnList = opReturnList;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
}
