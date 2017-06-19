package apkt.test;

import apkt.model.Tx;
import apkt.model.TxOutput;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.net.ssl.HttpsURLConnection;

public class ProofOfNotarization {

    public static void main(String[] args) {
        try {
            String url_ = "https://api.blockcypher.com/v1/btc/test3/txs/"
                    + "eca00495df1f3f77bac71e3dc455ae4456c6a8788b0e31aaa2b9d003d109ca16";
            
            URL url = new URL(url_);
            HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
            
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader =
                    new InputStreamReader(in);
            Tx resultObject = new Gson().fromJson(reader, Tx.class);
            
            String hash = "78d8578b12ad5b7f073308c18a5c54c0f595a2c34977064e7080adb1b7232f88";
            
            for (TxOutput to : resultObject.getOutputs()){
                if (hash.equals(to.getDataHex())){
                    boolean t = true;
                }
            }
            
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            String dateInString = resultObject.getConfirmed();

            try {

                Date date = formatter.parse(dateInString.replaceAll("Z$", "+0000"));
                System.out.println(date);

                System.out.println("time zone : " + TimeZone.getDefault().getID());
                System.out.println(formatter.format(date));

            } catch (ParseException e) {
                e.printStackTrace();
            }
            
        } catch (MalformedURLException ex) {
            Logger.getLogger(ProofOfNotarization.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ProofOfNotarization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
