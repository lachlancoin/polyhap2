package lc1.dp.genotype;

import java.io.PrintWriter;
import java.util.Collection;

public abstract interface NullEmitter
{
  public abstract double getProb(int paramInt1, int paramInt2);
  
  public abstract void addCounts(int paramInt1, int paramInt2, double paramDouble);
  
  public abstract void initialise();
  
  public abstract Collection<? extends PseudoDistribution> getCollection();
  
  public abstract void print(PrintWriter paramPrintWriter, String paramString);
  
  public abstract void transferCountsToProbs(double paramDouble);
  
  public abstract int sample(int paramInt);
  
  public abstract int mostLikely(int paramInt);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/NullEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */