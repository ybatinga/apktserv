package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import com.google.gson.annotations.SerializedName;
import java.math.BigDecimal;

public class Event {
    private String id;
    private String event;
    private String hash;
    @SerializedName("wallet_name")
    private String walletName;
    private String token;
    private String address;
    private Long confirmations;
    private String script;
    private String url;
    private BigDecimal confidence;

    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Long getConfirmations() {
        return confirmations;
    }

    public void setConfirmations(Long confirmations) {
        this.confirmations = confirmations;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String script) {
        this.script = script;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigDecimal getConfidence() {
        return confidence;
    }

    public void setConfidence(BigDecimal confidence) {
        this.confidence = confidence;
    }
    
    public static class EventType {
        public static final String UNCONFIRMED_TX = "unconfirmed-tx";
        public static final String CONFIRMED_TX = "confirmed-tx";
        public static final String TX_CONFIRMATION = "tx-confirmation";
        public static final String DOUBLE_SPEND_TX = "double-spend-tx";
        public static final String PAYMENT = "payment";
        public static final String CONFIDENCE_TX = "tx-confidence";
    }
    
}
