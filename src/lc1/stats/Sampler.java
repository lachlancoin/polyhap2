/*    */ package lc1.stats;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class Sampler
/*    */   implements Serializable
/*    */ {
/*    */   public Double[] dist;
/*    */   
/*    */   public Sampler(double[] dist1, double u)
/*    */   {
/* 21 */     this.dist = new Double[dist1.length];
/* 22 */     for (int i = 0; i < this.dist.length; i++) {
/* 23 */       this.dist[i] = Double.valueOf(dist1[i]);
/*    */     }
/*    */   }
/*    */   
/*    */   public Sampler(Double[] dist1, double u) {
/* 28 */     this.dist = new Double[dist1.length];
/* 29 */     for (int i = 0; i < this.dist.length; i++) {
/* 30 */       this.dist[i] = dist1[i];
/*    */     }
/*    */   }
/*    */   
/*    */   public abstract Double[] sample();
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/Sampler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */