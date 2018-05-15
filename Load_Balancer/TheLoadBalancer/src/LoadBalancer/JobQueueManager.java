
package LoadBalancer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class JobQueueManager {
    
    private static ArrayList<Job> jobList = new ArrayList();
    
    public static void sortJobs()
    {
        int i = 0;
        if(jobList.size() > 0)
        {
            while((i < 3)&&(jobList.size()>= i)&&(!"H".equals(jobList.get(0).getJobPriority())))
            {
                if("H".equals(jobList.get(i).getJobPriority()))
                {
                    Collections.swap(jobList,i,0);
                }
            }
        }         
    }
    
    public static void addJob(String jobDetails)
    {
        List<String> dividedMessage = Arrays.asList(jobDetails.split("/"));
        jobList.add(new Job(Integer.parseInt(dividedMessage.get(0)),Integer.parseInt(dividedMessage.get(1)),dividedMessage.get(2)));
    }
    
    public static String getJob()
    {
        String jobID = Integer.toString(jobList.get(0).getJobID());
        String jobDuration = Integer.toString(jobList.get(0).getJobDuration());
        return "JOB,"+jobID+","+jobDuration;
    }
    
    public static void removeJob()
    {
       jobList.remove(0); 
    }
    
    
    public static boolean JobListEmpty()
    {
        if(jobList.size()> 0)
            return false;
        else
            return true;
    }
}
