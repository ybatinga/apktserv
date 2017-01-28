package apkt.json;

import apkt.backingbean.AuthAux;
import apkt.model.Wallet;
import java.util.List;

public class ListWalletJson {
    List<Wallet> listWallet;
    private Wallet wallet;
    private AuthAux authAux;
    
    public ListWalletJson() {
    }

    public ListWalletJson(List<Wallet> listWallet) {
        this.listWallet = listWallet;
    }

    public List<Wallet> getListWallet() {
        return listWallet;
    }

    public void setListWallet(List<Wallet> listWallet) {
        this.listWallet = listWallet;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public AuthAux getAuthAux() {
        return authAux;
    }

    public void setAuthAux(AuthAux authAux) {
        this.authAux = authAux;
    }
    
}
