package Initator;

import java.util.Random;


public class Job {
    private static int jobNum = 1;
    
    
    private static String createJob()
    {
        String jobID = Integer.toString(jobNum);
        jobNum++;
        
        Random rand = new Random(); 
        String jobDuration  = Integer.toString(rand.nextInt(10)+1);
        
        int priorityNum = rand.nextInt(2);
        String jobPriority = (priorityNum == 1) ? "H" : "L";
        
        String job = jobID+"/"+jobDuration+"/"+jobPriority;
        
        return job;
                
    }
    
    public static String getJob()
    {
        String job = createJob();
        job = "JOB,"+job;
        return job;
    }
    
    public static int getJobCount()
    {
        return jobNum;
    }
    
}
