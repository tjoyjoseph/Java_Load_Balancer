package Node;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;

   
public class Node extends Thread{
     private InetAddress LoadBalancerIP_Address;  
     private int coreCount = 0;     
     private int nodeID = 0;     
     InetAddress broadcastAddress;     
     
     Node(int destPort, int threadCount) throws IOException  
     {
        super();      
        this.coreCount = threadCount;
        MessageSystem.setUpMessagingSystem(destPort);
        System.out.println("NODE CORES: "+coreCount+" NODE ID: "+nodeID);
        System.out.println("");
     }
     
     void doJob(String jobID, String jobDuration) throws InterruptedException, IOException
    {        
        System.out.println("NODE "+nodeID+": Job ID:"+jobID+"Job execution time: "+jobDuration+" seconds");
        Thread nodeThread = new Thread(new JobThread(Integer.parseInt(jobDuration),jobID,nodeID, LoadBalancerIP_Address ));
        nodeThread.start();        
    }
     
    
    @Override
     public void run()
     {
         try {
             getBroadCastAddress();
             MessageSystem.sendMessage("REQUEST TO CONNECT,NODE,"+coreCount, broadcastAddress);
             System.out.println("Starting Node:"+nodeID);
             System.out.println("");
             while(true)
             {
                String[] PACKET = MessageSystem.recieveMessage();
                sortMessage(PACKET[2],PACKET[0],Integer.parseInt(PACKET[1]));                 
             }
         } catch (IOException | InterruptedException ex) {
             //Logger.getLogger(Node.class.getName()).log(Level.SEVERE, null, ex);
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
     
      protected void sortMessage(String MESSAGE,String IP_Address,int port) throws IOException, InterruptedException
    {
        List<String> dividedMessage = Arrays.asList(MESSAGE.split(","));
        List<String> absoluteIP = Arrays.asList(IP_Address.split("/"));
        switch(dividedMessage.get(0))
        {
            case "CONNECTION SUCCESSFUL":
                System.out.println("NODE: Connection Successful");
                nodeID = Integer.parseInt(dividedMessage.get(1));
                LoadBalancerIP_Address = InetAddress.getByName(absoluteIP.get(1));
                break;
                
            case "JOB":
                doJob(dividedMessage.get(1),dividedMessage.get(2));
                break;            
        }
    }
  
     
    public static void main(String[] args) throws IOException
    {
        int LBPortNumber = 4000;
        if(args.length != 0) 
        {
            LBPortNumber = Integer.parseInt(args[0]);  
        }        
        int nodeCores = 0;
        if(args.length > 1)
        {
            nodeCores = Integer.parseInt(args[1]);
        }
        else
        {
            Random rand = new Random(); 
            nodeCores = rand.nextInt(6)+1; 
        }
        Node Node = new Node(LBPortNumber,nodeCores);
        Node.start();
        
    }

}
