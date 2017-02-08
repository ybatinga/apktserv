package apkt.service;

import java.math.BigDecimal;

public class CalcVarsService {
    public static final String FEE_ORDER_TAKER = "1";
    public static final String FEE_ORDER_MAKER = "1";
    public static final Integer MINING_FEES_SATOSHIS = 20000;
    public static final BigDecimal LIMIT_AMOUNT_MAX = new BigDecimal(0.04);
    public static final BigDecimal LIMIT_AMOUNT_MIN = new BigDecimal(0.0299999);
    public static final BigDecimal LIMIT_PRICE = new BigDecimal(3000);
    public static final BigDecimal BTC_TO_SATOSHIS = new BigDecimal(100000000);
}
