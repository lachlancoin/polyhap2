package lc1.dp.transition;

import java.io.PrintWriter;
import java.util.Collection;
import lc1.stats.SimpleExtendedDistribution;

public abstract interface ExpTransProb
{
  public abstract SimpleExtendedDistribution getExp(int paramInt);
  
  public abstract void initialiseExpRd();
  
  public abstract void transferExp(double[] paramArrayOfDouble);
  
  public abstract void transferExp(double paramDouble);
  
  public abstract void validateExp();
  
  public abstract double evaluateExpRd(double[] paramArrayOfDouble);
  
  public abstract Collection getExpRdColl();
  
  public abstract void printExp(PrintWriter paramPrintWriter, double paramDouble, String paramString);
  
  public abstract ExpTransProb clone(boolean paramBoolean, int paramInt);
  
  public abstract int noStates();
  
  public abstract ExpTransProb clone(int[] paramArrayOfInt);
  
  public abstract String info();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/transition/ExpTransProb.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */