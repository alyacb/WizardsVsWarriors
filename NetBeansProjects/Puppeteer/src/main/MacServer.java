
package main;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author alyacarina
 */
public class MacServer {
    public static void main(String[] args) throws UnknownHostException{
        BasicServer bs = new BasicServer();
        System.out.println(InetAddress.getLocalHost().getHostAddress());
        bs.setUpAndGo(4454);
    }
}
