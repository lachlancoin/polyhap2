package lc1.dp.data.representation;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lc1.CGH.Aberation;
import lc1.dp.states.EmissionState;

public abstract interface PIGData
  extends CSOData, SSOData, ScorableObject
{
  public abstract String toString();
  
  public abstract PIGData recombine(Map<Integer, Integer> paramMap, int paramInt);
  
  public abstract Set<Integer>[] getSwitches();
  
  public abstract Collection<Aberation> getDeletedPositions(EmissionState paramEmissionState);
  
  public abstract String getStringRep(int paramInt1, int paramInt2);
  
  public abstract void removeAll(List<Integer> paramList);
  
  public abstract void reverse();
  
  public abstract int compareTo(Object paramObject);
  
  public abstract void switchAlleles(int paramInt);
  
  public abstract void setAsMissing(List<Integer> paramList, double paramDouble);
  
  public abstract void applyAlias(int[] paramArrayOfInt);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/PIGData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */