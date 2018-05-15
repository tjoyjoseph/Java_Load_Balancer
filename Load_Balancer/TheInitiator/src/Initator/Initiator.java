
package Initator;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


public class Initiator extends Thread
{    
    private InetAddress LoadBalancerIP_Address;    
    private InetAddress broadcastAddress;
    private boolean nodeAvailable = false;
    

    Initiator(int destPort) throws IOException 
    {
        super();
        MessageSystem.setUpMessagingSystem(destPort);
    }    
    
    @Override
    public void run() 
    {
        try {
            System.out.println("INITIATOR IS NOW RUNNING...");
            System.out.println("");
            getBroadCastAddress();

            MessageSystem.sendMessage("REQUEST TO CONNECT,INITIATOR", broadcastAddress);
            String[] PACKET = MessageSystem.recieveMessage();
            sortMessage(PACKET[2],PACKET[0],Integer.parseInt(PACKET[1]));
            
            PACKET = MessageSystem.recieveMessage();
            sortMessage(PACKET[2],PACKET[0],Integer.parseInt(PACKET[1]));
            
            while(true)
            {
                 
                if((nodeAvailable == true)&&(Job.getJobCount()<201))
                {
                    MessageSystem.sendMessage(Job.getJob(), LoadBalancerIP_Address);                    
                }
            }            
            
        } catch (IOException ex) {
            Logger.getLogger(Initiator.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
    
    private void getBroadCastAddress() throws SocketException
     {
        Enumeration<NetworkInterface> allInterfaces = NetworkInterface.getNetworkInterfaces();
        while (allInterfaces.hasMoreElements()) 
        {
            NetworkInterface aInterface = allInterfaces.nextElement();
            if (!(aInterface.isLoopback())){  
                for (InterfaceAddress interfaceAddress : aInterface.getInterfaceAddresses()) 
                {
                    InetAddress broadcast = interfaceAddress.getBroadcast();
                    if (broadcast != null)                   
                    broadcastAddress = broadcast;
                }
            }
        }
         
     }
    
   private void sortMessage(String MESSAGE,String IP_Address,int port) throws IOException
    {
        List<String> dividedMessage = Arrays.asList(MESSAGE.split(","));
        List<String> absoluteIP = Arrays.asList(IP_Address.split("/"));
        switch(dividedMessage.get(0))
        {
            case "CONNECTION SUCCESSFUL":
                System.out.println("INITIATOR: Connection Successful");
                LoadBalancerIP_Address = InetAddress.getByName(absoluteIP.get(1));
                break;
            case "NODES AVAILABLE":
                System.out.println("INITIATOR: Node Found");
                nodeAvailable = true;
                break;            
        }
    }
    
    
    public static void main(String[] args) throws IOException
    {
        int LBPortNumber = Integer.parseInt("4000");    
        Initiator Initiator = new Initiator(LBPortNumber);
        Initiator.start();
        
    }
    
}
