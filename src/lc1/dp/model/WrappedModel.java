package lc1.dp.model;

public abstract interface WrappedModel
{
  public abstract CompoundMarkovModel getHMM();
  
  public abstract PairMarkovModel unwrapModel();
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/WrappedModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */