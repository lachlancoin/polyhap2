/*    */ package lc1.dp.core;
/*    */ 
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ public class ComplexTerm extends AbstractTerm
/*    */ {
/*    */   public double[] prob;
/*    */   
/*    */   protected ComplexTerm(double[] prob, int i, double sc)
/*    */   {
/* 11 */     super(i, sc);
/* 12 */     this.prob = prob;
/* 13 */     double sum = 0.0D;
/* 14 */     for (int k = 0; k < prob.length; k++) {
/* 15 */       sum += prob[k];
/*    */     }
/* 17 */     if (Math.abs(sum - this.score) > 0.0D) throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   public int getBestPath()
/*    */   {
/* 22 */     return Constants.sample(this.prob, this.score);
/*    */   }
/*    */   
/*    */   public void scale(double d) {
/* 26 */     super.scale(d);
/* 27 */     for (int j = 0; j < this.prob.length; j++) {
/* 28 */       this.prob[j] *= d;
/*    */     }
/*    */   }
/*    */   
/*    */   public double scaleScore() {
/* 33 */     int max = 0;
/* 34 */     for (int i = 1; i < this.prob.length; i++) {
/* 35 */       if (this.prob[i] > this.prob[max]) max = i;
/*    */     }
/* 37 */     return this.prob[max];
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/ComplexTerm.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */