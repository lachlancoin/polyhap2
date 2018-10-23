package lc1.dp.data.representation;

import java.io.PrintWriter;

public abstract interface SSOData
  extends ScorableObject
{
  public abstract String getName();
  
  public abstract void setName(String paramString);
  
  public abstract String getStringRep(int paramInt);
  
  public abstract void restrictSites(int paramInt);
  
  public abstract void restrictSites(int paramInt1, int paramInt2);
  
  public abstract int length();
  
  public abstract Comparable getElement(int paramInt);
  
  public abstract void printElement(PrintWriter paramPrintWriter, Object paramObject, boolean paramBoolean);
  
  public abstract void addPoint(int paramInt, Comparable paramComparable);
  
  public abstract void set(int paramInt, Comparable paramComparable);
  
  public abstract String toString();
  
  public abstract Class clazz();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/SSOData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */