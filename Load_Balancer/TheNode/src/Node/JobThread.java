
package Node;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;


public class JobThread extends Thread
{
    int runfor = 0;
    int nodeNum = 0;
    String jobID ;
    
    private final  InetAddress LoadBalancerIP_Address; 
    private final String MESSAGE_OUT;
    
    JobThread(int run, String jobID, int nodeNum, InetAddress IP_Address )
    {
        runfor = run;
        this.jobID = jobID;
        this.nodeNum = nodeNum;
        this.LoadBalancerIP_Address = IP_Address;        
        this.MESSAGE_OUT = "JOB DONE,"+nodeNum+","+jobID;
    }

    @Override
    public void run() 
    {
        try {
            Thread.sleep(runfor*1000);
            System.out.println("Node:"+nodeNum+" JOB FINISHED: "+jobID);
            MessageSystem.sendMessage(MESSAGE_OUT, LoadBalancerIP_Address);
            
        } catch (InterruptedException | IOException ex) {
            Logger.getLogger(JobThread.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
    
}
