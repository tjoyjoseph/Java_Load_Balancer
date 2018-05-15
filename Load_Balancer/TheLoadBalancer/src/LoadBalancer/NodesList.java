
package LoadBalancer;

import java.net.InetAddress;




public class NodesList{
    public static TheNode firstNode = null;
    public static int nodeNum = 0;
    public static int availableNodes = 0;
 

    static public boolean isEmpty()
    {
        return(firstNode == null);
    }

    public static int insertFirstNode(InetAddress IP_Address, int port, int coreCount)
    {        
        nodeNum++;
        TheNode newNode = new TheNode(nodeNum,IP_Address, port, coreCount);

        newNode.next = firstNode;
        firstNode = newNode;
        availableNodes++;
        return nodeNum;
    }

    public static TheNode removeFirst()
    {
        TheNode linkReference = firstNode;
        if(!isEmpty())
        {
            firstNode = firstNode.next;
        }
        else
        {
            System.out.println("Empty NodeedList");
        }

        return linkReference;
    }


    public static TheNode find(int NodeID)
    {
        TheNode theNode = firstNode;

        if(!isEmpty())
        {
            while (theNode.getNodeID() != NodeID)
           {
                if(theNode.next == null)
                {
                    return null;

                }
                else
                {
                    theNode = theNode.next;
                }
           }
       }
        else
        {
            System.out.println("Empty NodeList");
        }
        return theNode;
    }


    public static void sortNode()
    {
        if(!((firstNode == null)||(firstNode.next == null)||(firstNode.getUtilizationRate() == 1)))
        {
            TheNode currentNode = firstNode.next;
            for(int i = 0; i < availableNodes; i++)
            { 

                if(firstNode.getUtilizationRate() < currentNode.getUtilizationRate())
                {
                    removeNode(currentNode.getNodeID());
                    currentNode.next = firstNode;
                    firstNode = currentNode;
                }
                else if(firstNode.getUtilizationRate()  == currentNode.getUtilizationRate())
                {
                    if(firstNode.getCoreCount()  < currentNode.getCoreCount())
                    {
                        removeNode(currentNode.getNodeID());
                        currentNode.next = firstNode;
                        firstNode = currentNode;
                    }
                }

                if(!(currentNode.next == null))
                {
                    currentNode = currentNode.next;

                }
            }
        }
    }

    public static TheNode removeNode(int NodeID)
    {
        TheNode currentNode = firstNode;
        TheNode previousNode = firstNode;

        while(currentNode.getNodeID() != NodeID)
        {
            if(currentNode.next == null)
            {
                return null;
            }
            else
            {
                previousNode = currentNode;

                currentNode = currentNode.next;
            }
        }

        if(currentNode == firstNode)
        {
            firstNode = firstNode.next;
        }
        else
        {
            previousNode.next = currentNode.next;
        }

        return currentNode;
    }

    public static void removeJobFromNode(int NodeID)
    {
        TheNode currentNode = firstNode;


        while(currentNode.getNodeID() != NodeID)
        {
            if(currentNode.next != null)
            {
                currentNode = currentNode.next;
            }
        }

        if(currentNode.getNodeID() == NodeID)
        {
            currentNode.removeJob();
        }
    }
}
