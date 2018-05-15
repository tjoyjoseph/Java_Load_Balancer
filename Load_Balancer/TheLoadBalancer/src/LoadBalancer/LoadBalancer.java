package LoadBalancer;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoadBalancer extends Thread{   
   
    TheNode node;
    
    private static InetAddress initiatorAddress;
    private static int initiatorPort; 
   
  
    LoadBalancer(int listeningPort) throws IOException{
        super();
        MessageSystem.setUpMessagingSystem(listeningPort);
     }
    
    @Override
    public void run()
    {
        try {
            System.out.println("Starting Load Balancer");
            System.out.println("");
            while(true)
            {
                String[] PACKET = MessageSystem.recieveMessage();

                sortMessage(PACKET[2],PACKET[0],Integer.parseInt(PACKET[1]));

                if(!JobQueueManager.JobListEmpty())
                {
                    MessageSystem.sendMessage("NODES AVAILABLE",initiatorAddress,initiatorPort);
                    if(NodeManager.sendJobToNextNode(JobQueueManager.getJob()) == true)
                    {
                        System.out.println("Job being sent to Node:"+JobQueueManager.getJob());
                        JobQueueManager.removeJob();
                    }
                }
            }
        } catch (IOException | InterruptedException ex) 
        {
            Logger.getLogger(LoadBalancer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    protected void sortMessage(String MESSAGE,String IP_Address,int port) throws IOException, InterruptedException
    {
        List<String> dividedMessage = Arrays.asList(MESSAGE.split(","));
        List<String> absoluteIP = Arrays.asList(IP_Address.split("/"));
        switch(dividedMessage.get(0))
        {
            case "REQUEST TO CONNECT":
                switch(dividedMessage.get(1))
                {
                    case "INITIATOR":
                        initiatorPort = port;
                        initiatorAddress = InetAddress.getByName(absoluteIP.get(1));
                        MessageSystem.sendMessage("CONNECTION SUCCESSFUL",initiatorAddress,initiatorPort);  
                        if(NodeManager.getFirstNode() != null)
                        {
                            MessageSystem.sendMessage("NODE AVAILABLE",initiatorAddress,initiatorPort);
                        }
                        break;  
                    case "NODE":
                        
                        int nodeID = NodeManager.addNode(InetAddress.getByName(absoluteIP.get(1)),port,Integer.parseInt(dividedMessage.get(2)));
                        MessageSystem.sendMessage("CONNECTION SUCCESSFUL,"+nodeID,InetAddress.getByName(absoluteIP.get(1)),port);
                        if(initiatorPort != 0)
                        {
                            MessageSystem.sendMessage("NODES AVAILABLE",initiatorAddress,initiatorPort);
                        }
                        break; 
                }
                break; 
            
            case "JOB":
                System.out.println("\nFROM INITIATOR: "+MESSAGE);
                JobQueueManager.addJob(dividedMessage.get(1));

                break;
            case "JOB DONE":
                System.out.println("FROM NODE "+Integer.parseInt(dividedMessage.get(1))+": "+MESSAGE);
                NodeManager.nodeJobComplete(Integer.parseInt(dividedMessage.get(1)));              
                
                if ("10".equals(dividedMessage.get(2)))
                {
                    System.exit(0);
                }
                MessageSystem.sendMessage("JOB DONE,"+dividedMessage.get(2),initiatorAddress,initiatorPort);
                break;
                 
        }

    }
    
    public static void main(String[] args) throws IOException
    {
        int portNumber;
        if(args.length == 0)
            portNumber = 4000;
        else
            portNumber = Integer.parseInt(args[0]);       

        LoadBalancer LoadBalancer = new LoadBalancer(portNumber);
        LoadBalancer.start();
        
    }
    
}
