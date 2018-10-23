package lc1.dp;

import java.io.PrintWriter;
import java.util.Collection;
import lc1.dp.genotype.StateDistribution;

public abstract interface AbstractTransitionProbs
{
  public abstract void addCounts(StateDistribution[] paramArrayOfStateDistribution);
  
  public abstract double getTransition(int paramInt1, int paramInt2);
  
  public abstract void initialiseCounts(boolean paramBoolean1, boolean paramBoolean2);
  
  public abstract void transfer(double paramDouble1, double paramDouble2);
  
  public abstract Collection getDistributions();
  
  public abstract void validate();
  
  public abstract double transitionDistance(AbstractTransitionProbs paramAbstractTransitionProbs);
  
  public abstract AbstractTransitionProbs clone(AbstractTransitionProbs paramAbstractTransitionProbs);
  
  public abstract void print(PrintWriter paramPrintWriter, Double[] paramArrayOfDouble);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/AbstractTransitionProbs.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */