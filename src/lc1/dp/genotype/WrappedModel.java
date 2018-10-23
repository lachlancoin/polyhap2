package lc1.dp.genotype;

import lc1.dp.CompoundMarkovModel;
import lc1.dp.PairMarkovModel;

public abstract interface WrappedModel
{
  public abstract CompoundMarkovModel getHMM();
  
  public abstract PairMarkovModel unwrapModel();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/WrappedModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */