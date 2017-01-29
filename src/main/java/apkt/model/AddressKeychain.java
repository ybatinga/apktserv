package apkt.model;

import com.blockcypher.model.address.Address;
import com.blockcypher.model.transaction.summary.TransactionSummary;
import com.blockcypher.utils.gson.GsonFactory;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AddressKeychain {

    private String address;
    
    @SerializedName("public")
    private String publicAddress;
    
    @SerializedName("private")
    private String privateAddress;        
    
    private String wif;    
    
    private List<String> pubkeys = new ArrayList<String>();
    
    @SerializedName("script_type")
    private String scriptType;

    public AddressKeychain() {
    }
    
    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPublicAddress() {
        return publicAddress;
    }

    public void setPublicAddress(String publicAddress) {
        this.publicAddress = publicAddress;
    }

    public String getPrivateAddress() {
        return privateAddress;
    }

    public void setPrivateAddress(String privateAddress) {
        this.privateAddress = privateAddress;
    }

    public String getWif() {
        return wif;
    }

    public void setWif(String wif) {
        this.wif = wif;
    }

    public boolean addPubkeys(String address) {
        return pubkeys.add(address);
    }
    
    public String getPubkeys(int index) {
        return pubkeys.get(index);
    }
    
    public List<String> getPubkeysList() {
        return pubkeys;
    }
    
    public String getScriptType() {
        return scriptType;
    }

    public void setScriptType(String scriptType) {
        this.scriptType = scriptType;
    }
    
}
