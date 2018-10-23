package lc1.dp.data.representation;

import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Collection;

public abstract interface ScorableObject
  extends Serializable, Comparable
{
  public abstract int length();
  
  public abstract Comparable getElement(int paramInt);
  
  public abstract void print(PrintWriter paramPrintWriter, boolean paramBoolean1, boolean paramBoolean2, Collection<Integer> paramCollection);
  
  public abstract String getName();
  
  public abstract Object clone();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/ScorableObject.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */