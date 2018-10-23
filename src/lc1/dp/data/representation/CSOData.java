package lc1.dp.data.representation;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;
import lc1.dp.emissionspace.EmissionStateSpace;
import lc1.dp.states.EmissionState;
import lc1.dp.states.HaplotypeEmissionState;

public abstract interface CSOData
  extends SSOData, ScorableObject
{
  public abstract int noCopies();
  
  public abstract void mkTrCompArray();
  
  public abstract void mix();
  
  public abstract void set(int paramInt, Comparable paramComparable);
  
  public abstract PIGData[] split();
  
  public abstract int[] phaseCorrect(CSOData paramCSOData, Collection<Integer> paramCollection1, Collection<Integer> paramCollection2);
  
  public abstract void samplePhase(List<CSOData> paramList, double[] paramArrayOfDouble, EmissionStateSpace paramEmissionStateSpace);
  
  public abstract double getUncertainty(EmissionState paramEmissionState, int paramInt);
  
  public abstract void sampleGenotype(HaplotypeEmissionState paramHaplotypeEmissionState, List<CSOData> paramList);
  
  public abstract void print(PrintWriter paramPrintWriter, boolean paramBoolean1, boolean paramBoolean2, Collection<Integer> paramCollection);
  
  public abstract void print(PrintWriter paramPrintWriter, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Collection<Integer> paramCollection);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/CSOData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */