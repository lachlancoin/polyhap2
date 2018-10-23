package lc1.stats;

public abstract interface TransitionDistribution
{
  public abstract void transferProbToPseudo();
  
  public abstract void addCounts(StateDistribution paramStateDistribution);
  
  public abstract double getProbs(int paramInt);
  
  public abstract double getPseudo(int paramInt);
  
  public abstract void initialise();
  
  public abstract void transfer(double paramDouble);
  
  public abstract double KLDistance(TransitionDistribution paramTransitionDistribution);
  
  public abstract double sum();
  
  public abstract void fillPseudo(double paramDouble);
  
  public abstract void setProbs(int paramInt, double paramDouble);
  
  public abstract void setPseudo(int paramInt, double paramDouble);
  
  public abstract TransitionDistribution clone(TransitionDistribution paramTransitionDistribution);
  
  public abstract void validate();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/TransitionDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */