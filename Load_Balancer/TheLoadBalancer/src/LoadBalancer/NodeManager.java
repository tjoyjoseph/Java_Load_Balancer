
package LoadBalancer;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class NodeManager {
    
    //private static final NodesList theNodeList = new NodesList();    
    
    public static int addNode(InetAddress ip, int port, int numOfCores) throws UnknownHostException
    {
        int nodeID = NodesList.insertFirstNode(ip,port,numOfCores);
        
        return nodeID;
    }
    
    public static boolean sendJobToNextNode(String job) 
    {
        try {
            NodesList.sortNode();
            TheNode firstNode = NodesList.firstNode;
            if(firstNode != null)
            {
                if(firstNode.getUtilizationRate() > 0)
                {
                    System.out.println("Bode ip address is "+firstNode.getIP_Address());
                    if(firstNode.getIP_Address().isReachable (10000) == true)
                    {
                        MessageSystem.sendMessage(job, firstNode.getIP_Address(), firstNode.getPortNumber());
                        NodesList.firstNode.addJob();
                        return true;
                    }
                    NodesList.removeFirst();
                }
            }
 
        } catch (IOException ex) {
            return false;
            
        }
        return false;
    }
    
    public static TheNode findNode(int nodeID)
    {
         TheNode returnNode = NodesList.find(nodeID);
        
         return returnNode;
    }
    
    public static TheNode getFirstNode()
    {
        
        TheNode firstNode = NodesList.firstNode;
        return firstNode;
    }
    
    public static void removeNode(int nodeID)
    {
        NodesList.removeNode(nodeID);
    }
    
    public static void sortNodeList()
    {
        NodesList.sortNode();
    }
    
    public static void nodeJobComplete(int nodeID) {
        NodesList.removeJobFromNode(nodeID);
    }
    
}
