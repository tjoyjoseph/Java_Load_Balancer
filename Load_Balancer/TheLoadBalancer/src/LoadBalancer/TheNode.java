package LoadBalancer;

import java.net.InetAddress;

public class TheNode {
    private int NodeID;
    private InetAddress IP_Address;
    private int portNumber = 0, MAXCORES = 0, availableCores = 0 ;
    
    private double utilized = 0;

    
    public TheNode next;
    
    public TheNode(int NodeID, InetAddress IP_Address, int port, int totCores)
    {
        this.NodeID = NodeID;
        this.IP_Address = IP_Address;
        this.portNumber = port;
        this.MAXCORES = totCores;
        this.availableCores = totCores;
        updateUtility();
    }
    

    private void updateUtility()
    {
        utilized = (double) availableCores / MAXCORES;
    }
    
    public void addJob()
    {
        availableCores -= 1;
        updateUtility();
    }
    
    public int getNodeID()
    {
        return NodeID;
    }
    
    public InetAddress getIP_Address()
    {
        return IP_Address;
    }
    
    public int getPortNumber()
    {
        return portNumber;
    }
    
    public int getCoreCount()
    {
        return availableCores;
    }
    
    public double getUtilizationRate()
    {
        return utilized;
    }
    
    public void removeJob()
    {
        availableCores += 1;
        updateUtility();
    }
    
  

}
