package lc1.stats;

import lc1.dp.states.EmissionState;

public abstract interface ProbabilityDistribution
{
  public abstract double sample();
  
  public abstract double probability(double paramDouble);
  
  public abstract void addCount(double paramDouble1, double paramDouble2);
  
  public abstract ProbabilityDistribution clone();
  
  public abstract ProbabilityDistribution clone(double paramDouble);
  
  public abstract void transfercounts(EmissionState paramEmissionState, int paramInt1, int paramInt2);
  
  public abstract void initialise();
  
  public abstract void setParamsAsAverageOf(ProbabilityDistribution[] paramArrayOfProbabilityDistribution);
  
  public abstract void transfer(double paramDouble);
  
  public abstract void addCounts(ProbabilityDistribution paramProbabilityDistribution);
  
  public abstract double[] getCount(double[] paramArrayOfDouble);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/ProbabilityDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */