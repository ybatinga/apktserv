package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.PymntMthd;
import java.util.List;

public class ListPymntMthdJson {

    private List<PymntMthd> listPymntMthd;
    private PymntMthd pymntMthd;
    private AuthAux authAux;

    public ListPymntMthdJson(List<PymntMthd> listPymntMthd) {
        this.listPymntMthd = listPymntMthd;
    }

    public ListPymntMthdJson(PymntMthd pymntMthd, AuthAux authAux) {
        this.pymntMthd = pymntMthd;
        this.authAux = authAux;
    }

    public List<PymntMthd> getListPymntMthd() {
        return listPymntMthd;
    }

    public void setListPymntMthd(List<PymntMthd> listPymntMthd) {
        this.listPymntMthd = listPymntMthd;
    }

    public PymntMthd getPymntMthd() {
        return pymntMthd;
    }

    public void setPymntMthd(PymntMthd pymntMthd) {
        this.pymntMthd = pymntMthd;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }

}