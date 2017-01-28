/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package apkt.test;

import apkt.blockcypher.context.BlockCypherContext;
import apkt.blockcypher.service.AddressService;
import apkt.model.Address;
import apkt.utils.BlockCypherConstants;

/**
 *
 * @author elton
 */
public class AddressGenMain {

    public static void main(String[] args) {

        try {

            BlockCypherContext blockCypherContext = new BlockCypherContext(
                    BlockCypherConstants.VERSION_V1,
                    BlockCypherConstants.CURRENCY_BTC,
                    BlockCypherConstants.NETWORK);
            AddressService addressService = blockCypherContext.getAddressService();

            Address address = addressService.createAddress();

            /*String inputString = "";
             String resourceUrl = BlockCypherConstants.BLOCK_CYPHER_ENDPOINT_TEST;
			
             Address address = (Address) JerseyRestUtils.post(resourceUrl, inputString, "1", Address.class);
			
             String debug = "debug";
             */

            System.out.println(
                    "Address: " + address.getAddress() + "\n"
                    + "Public key: " + address.getPublicAddress() + "\n"
                    + "Private key: " + address.getPrivateAddress());

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}