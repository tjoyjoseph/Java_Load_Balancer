
package LoadBalancer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class MessageSystem {
    
    private static DatagramSocket SOCK = null;
    
    public static void setUpMessagingSystem(int port) throws SocketException
    {
        SOCK = new DatagramSocket(port); 
        System.out.println("LOAD BALANCER PORT NUMBER: "+SOCK.getPort());
    }
    
    public static void sendMessage(String MESSAGE_OUT, InetAddress IP_Address,int Port) throws IOException
    {
        if(SOCK == null)
        {
            SOCK = new DatagramSocket(4000);
        }
        byte[] BUFFER_OUT = new byte[256];                      
        BUFFER_OUT = MESSAGE_OUT.getBytes();
        DatagramPacket SendPACKET = new DatagramPacket(BUFFER_OUT, BUFFER_OUT.length, IP_Address, Port);
        SOCK.send(SendPACKET);  
        
    }
    
    public static String[] recieveMessage() throws IOException
    {
        if(SOCK == null)
        {
            SOCK = new DatagramSocket(4000);
        }
        byte[] BUFFER_IN = new byte[256];
        DatagramPacket RecievePACKET = new DatagramPacket(BUFFER_IN, BUFFER_IN.length);
        SOCK.receive(RecievePACKET);
        String MESSAGE = new String(RecievePACKET.getData()).trim();         
        String[] returnStrings= {RecievePACKET.getAddress().toString(),String.valueOf(RecievePACKET.getPort()),MESSAGE};
        
        return returnStrings;
        
    }
    
}
