/*    */ package lc1.stats;
/*    */ 
/*    */ import JSci.maths.statistics.ProbabilityDistribution;
/*    */ 
/*    */ public class UniformDistribution extends ProbabilityDistribution {
/*    */   double prob;
/*    */   
/*  8 */   public UniformDistribution(double min, double max) { this.min = min;
/*  9 */     this.max = max;
/* 10 */     this.prob = (1.0D / (max - min));
/* 11 */     if (max == min) throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   double min;
/*    */   double max;
/* 16 */   public double cumulative(double arg0) { if (arg0 < this.min) return 0.0D;
/* 17 */     if (arg0 > this.max) return 1.0D;
/* 18 */     return (arg0 - this.min) / this.max;
/*    */   }
/*    */   
/*    */   public double inverse(double arg0)
/*    */   {
/* 23 */     return arg0 * (this.max - this.min) + this.min;
/*    */   }
/*    */   
/*    */   public double probability(double arg0)
/*    */   {
/* 28 */     if (arg0 < this.min) return 0.0D;
/* 29 */     if (arg0 > this.max) return 0.0D;
/* 30 */     return this.prob;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/UniformDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */