package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.TxOpReturn;
import java.util.List;

public class ListTxOpReturnJson {
    private Long userId;
    private String type;
    private List<TxOpReturn> txOpReturnList;
    private AuthAux authAux;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public List<TxOpReturn> getTxOpReturnList() {
        return txOpReturnList;
    }

    public void setTxOpReturnList(List<TxOpReturn> txOpReturnList) {
        this.txOpReturnList = txOpReturnList;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
}