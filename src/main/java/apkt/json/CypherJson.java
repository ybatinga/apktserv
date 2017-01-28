package apkt.json;

import java.math.BigInteger;
import java.util.List;

public class CypherJson {
    private List<BigInteger> bigIntegerList;
    private byte[] encrypted;
    private byte[] aesKeyValue;
    private Object object;

    public CypherJson(List<BigInteger> bigIntegerList, byte[] encrypted) {
        this.bigIntegerList = bigIntegerList;
        this.encrypted = encrypted;
    }

    public List<BigInteger> getBigIntegerList() {
        return bigIntegerList;
    }

    public void setBigIntegerList(List<BigInteger> bigIntegerList) {
        this.bigIntegerList = bigIntegerList;
    }

    public byte[] getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(byte[] encrypted) {
        this.encrypted = encrypted;
    }

    public byte[] getAesKeyValue() {
        return aesKeyValue;
    }

    public void setAesKeyValue(byte[] aesKeyValue) {
        this.aesKeyValue = aesKeyValue;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}