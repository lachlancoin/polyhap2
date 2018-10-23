package lc1.dp.genotype;

import java.io.Serializable;

public abstract interface Prior
  extends Serializable
{
  public abstract double probability(double paramDouble);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/Prior.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */