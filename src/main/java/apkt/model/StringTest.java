package apkt.model;

import com.blockcypher.utils.gson.GsonFactory;

public class StringTest {
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
    
    @Override
    public String toString() {
        return GsonFactory.getGson().toJson(this);
    }
    
}
