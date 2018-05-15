
package LoadBalancer;


public class Job {
    private int jobID;
    private int jobDuration;
    private String jobPriority;
    
    Job(int ID, int duration, String priority)
    {
      jobID = ID;
      jobDuration = duration;
      jobPriority = priority;
              
    }
    
    public int getJobID()
    {
        return jobID;
    }
    public int getJobDuration()
    {
        return jobDuration;
    }
    public String getJobPriority()
    {
        return jobPriority;
    }
    
    
    
    
}
