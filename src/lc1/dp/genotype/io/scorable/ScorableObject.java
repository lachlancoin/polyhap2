package lc1.dp.genotype.io.scorable;

import java.io.PrintWriter;

public abstract interface ScorableObject
{
  public abstract int length();
  
  public abstract Object getElement(int paramInt);
  
  public abstract void print(PrintWriter paramPrintWriter);
  
  public abstract String getName();
  
  public abstract Object clone();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/ScorableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */