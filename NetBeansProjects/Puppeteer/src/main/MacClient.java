
package main;

/**
 *
 * @author alyacarina
 */
public class MacClient {
    public static void main(String[] args){
        BasicClient bcican = new BasicClient();
        bcican.establishConnection(4646, "10.22.143.189");
    }
}
