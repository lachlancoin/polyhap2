/*    */ package lc1.stats;
/*    */ 
/*    */ import JSci.maths.statistics.ProbabilityDistribution;
/*    */ 
/*    */ public class TruncatedDistribution extends ProbabilityDistribution {
/*    */   ProbabilityDistribution dist1;
/*    */   final double min;
/*    */   final double max;
/*    */   final double probLessThanMin;
/*    */   final double probMoreThanMax;
/*    */   final double totalWeight;
/*    */   
/*    */   TruncatedDistribution(ProbabilityDistribution dist, double min, double max) {
/* 14 */     this.dist1 = dist;
/* 15 */     this.max = max;
/* 16 */     this.min = min;
/* 17 */     this.probLessThanMin = dist.cumulative(min);
/* 18 */     this.probMoreThanMax = (1.0D - dist.cumulative(max));
/* 19 */     this.totalWeight = (dist.cumulative(max) - dist.cumulative(min));
/*    */   }
/*    */   
/*    */ 
/*    */   public double cumulative(double arg0)
/*    */   {
/* 25 */     return (this.dist1.cumulative(arg0) - this.probLessThanMin) / this.totalWeight;
/*    */   }
/*    */   
/*    */   public double inverse(double arg0)
/*    */   {
/* 30 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public double probability(double arg0)
/*    */   {
/* 35 */     return this.dist1.probability(arg0) / this.totalWeight;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/TruncatedDistribution.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */