/*    */ package lc1.dp;
/*    */ 
/*    */ import lc1.dp.genotype.io.Constants;
/*    */ 
/*    */ 
/*    */ public class ComplexTerm
/*    */   extends AbstractTerm
/*    */ {
/*    */   public double[] prob;
/*    */   
/*    */   protected ComplexTerm(double[] prob, int i, double sc)
/*    */   {
/* 13 */     super(i, sc);
/* 14 */     this.prob = prob;
/*    */     
/* 16 */     double sum = 0.0D;
/* 17 */     for (int k = 0; k < prob.length; k++) {
/* 18 */       sum += prob[k];
/*    */     }
/* 20 */     if (Math.abs(sum - this.score) > 0.0D) throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public int getBestPath()
/*    */   {
/* 25 */     return Constants.sample(this.prob, this.score);
/*    */   }
/*    */   
/*    */   public void scale(double d) {
/* 29 */     super.scale(d);
/* 30 */     for (int j = 0; j < this.prob.length; j++) {
/* 31 */       this.prob[j] *= d;
/*    */     }
/*    */   }
/*    */   
/*    */   public double scaleScore() {
/* 36 */     int max = 0;
/* 37 */     for (int i = 1; i < this.prob.length; i++) {
/* 38 */       if (this.prob[i] > this.prob[max]) max = i;
/*    */     }
/* 40 */     return this.prob[max];
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/ComplexTerm.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */