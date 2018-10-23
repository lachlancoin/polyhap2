package lc1.dp.genotype.io;

import java.util.ArrayList;
import lc1.dp.PairMarkovModel;

public abstract interface ProgressMonitor
{
  public abstract void monitor(String paramString);
  
  public abstract ArrayList<Integer[][]> viterbi(PairMarkovModel paramPairMarkovModel)
    throws Exception;
  
  public abstract Double[][] forwardBackward(PairMarkovModel paramPairMarkovModel)
    throws Exception;
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/ProgressMonitor.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */