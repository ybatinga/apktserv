package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

@Entity
@Table(name = "order_tx")
public class Order implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic(optional = false)
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;
    
    @Basic(optional = false)
    @Column(name = "amount_net_buy", nullable = false)
    private BigDecimal amountNetBuy;
    
    @Basic(optional = false)
    @Column(name = "amount_net_sell", nullable = false)
    private BigDecimal amountNetSell;
    
    @Basic(optional = false)    
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    
    @Column(name = "fee_buy")
    private BigDecimal feeBuy;
    
    @Column(name = "fee_sell")
    private BigDecimal feeSell;
    
    @Column(name = "mining_fee")
    private BigDecimal miningFee;
    
    @Column(name = "total")
    private BigDecimal total;
    
    @Column(name = "type")
    private String type;
    
//    @JoinColumn(name = "wallet_id", referencedColumnName = "id")
//    @ManyToOne
//    private Wallet walletId;
//  
//    @JoinColumn(name = "bank_id", referencedColumnName = "id")
//    @ManyToOne
//    private Bank bankId;
//    
//    @JoinColumn(name = "maker_login_id", referencedColumnName = "id")
//    @ManyToOne
//    private Login makerLoginId;
//    
//    @JoinColumn(name = "taker_login_id", referencedColumnName = "id")
//    @ManyToOne
//    private Login takerLoginId;
    
    @Column(name = "maker_login_id", nullable = false)
    private Long makerLoginId;
    
    @Column(name = "taker_login_id", nullable = false)
    private Long takerLoginId;
    
    @Column(name = "version")
    @Version
    private Long version;
    
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedAt;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "currency_code")
    private String currencyCode;
    
    @Column(name = "escrow_address")
    private String escrowAddress;
    
    @JoinColumn(name = "order_wallet_id", referencedColumnName = "id")
    @OneToOne(cascade = CascadeType.PERSIST)
    private OrderWallet orderWalletId;

    @OneToMany(cascade = CascadeType.PERSIST, mappedBy = "orderId")
    private List<OrderPymntMthd> orderPymntMthdList;
    
//    @JoinColumn(name = "order_pymnt_mthd_id", referencedColumnName = "id")
//    @OneToOne(cascade = CascadeType.PERSIST)
//    private OrderPymntMthd orderPymntMthdId;
    
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "orderId")
//    private List<Address> addressList;
//    
//    @OneToMany(mappedBy = "orderId")
//    private List<Status> statusList;
    
    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountNetBuy() {
        return amountNetBuy;
    }

    public void setAmountNetBuy(BigDecimal amountNetBuy) {
        this.amountNetBuy = amountNetBuy;
    }

    public BigDecimal getAmountNetSell() {
        return amountNetSell;
    }

    public void setAmountNetSell(BigDecimal amountNetSell) {
        this.amountNetSell = amountNetSell;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getFeeBuy() {
        return feeBuy;
    }

    public void setFeeBuy(BigDecimal feeBuy) {
        this.feeBuy = feeBuy;
    }

    public BigDecimal getFeeSell() {
        return feeSell;
    }

    public void setFeeSell(BigDecimal feeSell) {
        this.feeSell = feeSell;
    }

    public BigDecimal getMiningFee() {
        return miningFee;
    }

    public void setMiningFee(BigDecimal miningFee) {
        this.miningFee = miningFee;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getMakerLoginId() {
        return makerLoginId;
    }

    public void setMakerLoginId(Long makerLoginId) {
        this.makerLoginId = makerLoginId;
    }

    public Long getTakerLoginId() {
        return takerLoginId;
    }

    public void setTakerLoginId(Long takerLoginId) {
        this.takerLoginId = takerLoginId;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
    
    public String getEscrowAddress() {
        return escrowAddress;
    }

    public void setEscrowAddress(String escrowAddress) {
        this.escrowAddress = escrowAddress;
    }
    
    public OrderWallet getOrderWalletId() {
        return orderWalletId;
    }

    public void setOrderWalletId(OrderWallet orderWalletId) {
        this.orderWalletId = orderWalletId;
    }

    public List<OrderPymntMthd> getOrderPymntMthdList() {
        return orderPymntMthdList;
    }

    public void setOrderPymntMthdList(List<OrderPymntMthd> orderPymntMthdList) {
        this.orderPymntMthdList = orderPymntMthdList;
    }
    
//    public List<Address> getAddressList() {
//        return addressList;
//    }
//
//    public void setAddressList(List<Address> addressList) {
//        this.addressList = addressList;
//    }
//
//    public List<Status> getStatusList() {
//        return statusList;
//    }
//
//    public void setStatusList(List<Status> statusList) {
//        this.statusList = statusList;
//    }

    public static class OrderType {
        public static final String ORDER_TYPE_BUY = "BUY_ORDER";
        public static final String ORDER_TYPE_SELL = "SELL_ORDER";
    }

    public static class OrderAction {
        public static final String ORDER_ACTION_MAKER = "MAKER";
        public static final String ORDER_ACTION_TAKER = "TAKER";
    }
    
    public static class OrderStatuses {
        public static final String ORDER_STATUS_OPEN = "TX_OPEN";
        public static final String ORDER_STATUS_TX_REQUESTED = "TX_REQUESTED";
        public static final String ORDER_STATUS_TX_DEPOSITED = "TX_DEPOSITED";
        public static final String ORDER_STATUS_TX_CONFIRMED = "TX_CONFIRMED";
        public static final String ORDER_STATUS_TX_PAYED = "TX_PAYED";
        public static final String ORDER_STATUS_TX_RELEASED = "TX_RELEASED";
        public static final String ORDER_STATUS_TX_CANCELED = "TX_CANCELED";      
    }
    
    public static class OrderListType {
        public static final String ORDER_LIST_TYPE_TRANSACTIONS = "TRANSACTIONS";
        public static final String ORDER_LIST_TYPE_BUY = "BUY";
        public static final String ORDER_LIST_TYPE_SELL = "SELL";
        public static final String ORDER_LIST_TYPE_BUY_PUBLIC = "BUY_PUBLIC";
        public static final String ORDER_LIST_TYPE_SELL_PUBLIC = "SELL_PUBLIC";
    }
}