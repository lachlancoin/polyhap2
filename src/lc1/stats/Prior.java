package lc1.stats;

import java.io.Serializable;

public abstract interface Prior
  extends Serializable
{
  public abstract double probability(double paramDouble);
}


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/Prior.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */