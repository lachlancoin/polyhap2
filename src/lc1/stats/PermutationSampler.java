/*    */ package lc1.stats;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Random;
/*    */ import lc1.util.Constants;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PermutationSampler
/*    */   extends Sampler
/*    */ {
/*    */   Dirichlet dir;
/*    */   
/*    */   public PermutationSampler(double[] dist1, double u)
/*    */   {
/* 17 */     super(dist1, u);
/* 18 */     for (int i = 0; i < this.dist.length; i++) {
/* 19 */       this.l.add(Integer.valueOf(i));
/*    */     }
/* 21 */     this.dir = new Dirichlet(dist1, u);
/*    */   }
/*    */   
/*    */   public PermutationSampler(Double[] dist1, double u)
/*    */   {
/* 26 */     super(dist1, u);
/* 27 */     for (int i = 0; i < this.dist.length; i++) {
/* 28 */       this.l.add(Integer.valueOf(i));
/*    */     }
/* 30 */     this.dir = new Dirichlet(dist1, u);
/*    */   }
/*    */   
/*    */ 
/* 34 */   List<Integer> s = new ArrayList();
/* 35 */   List<Integer> l = new ArrayList();
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
/*    */ 
/*    */ 
/*    */ 
/*    */   public Double[] sample()
/*    */   {
/* 51 */     Double[] dist = this.dir.sample();
/* 52 */     Double[] res = (Double[])dist.clone();
/*    */     
/* 54 */     for (int k = res.length - 1; k > 0; k--) {
/* 55 */       int w = Constants.rand.nextInt(k + 1);
/* 56 */       Double temp = res[w];
/* 57 */       if ((temp.doubleValue() > 0.0D) && (res[k].doubleValue() > 0.0D)) {
/* 58 */         res[w] = res[k];
/* 59 */         res[k] = temp;
/*    */       }
/*    */     }
/* 62 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/PermutationSampler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */