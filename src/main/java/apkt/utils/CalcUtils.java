package apkt.utils;

import apkt.service.CalcVarsService;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalcUtils {
    
    public static Long btcToSatoshis (BigDecimal value){    
        value = value.multiply(CalcVarsService.BTC_TO_SATOSHIS);
        return Long.parseLong(value.setScale(0, RoundingMode.HALF_UP).toString());
    }    
}
