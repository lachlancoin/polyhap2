/*    */ package lc1.evd;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ abstract class LawlessFunction
/*    */   extends UnivariateFunction
/*    */ {
/*    */   double[] x;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   double[] y;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   int n;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   static double Lawless423(double lambda, double[] x, double[] y, int n, int z, float c)
/*    */   {
/* 38 */     double esum = 0.0D;
/* 39 */     double total = 0.0D;
/*    */     
/* 41 */     for (int i = 0; i < n; i++)
/*    */     {
/* 43 */       double mult = y == null ? 1.0D : y[i];
/* 44 */       esum += mult * Math.exp(-1.0D * lambda * x[i]);
/* 45 */       total += mult;
/*    */     }
/*    */     
/* 48 */     esum += z * Math.exp(-1.0D * lambda * c);
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 53 */     return -1.0D * Math.log(esum / total) / lambda;
/*    */   }
/*    */   
/*    */   static double Lawless415(double lambda, double[] x, double[] c, int n)
/*    */   {
/* 58 */     double esum = 0.0D;
/* 59 */     double total = 0.0D;
/*    */     
/* 61 */     for (int i = 0; i < n; i++)
/*    */     {
/* 63 */       double mult = c == null ? 1.0D : c[i];
/* 64 */       esum += mult * Math.exp(-1.0D * lambda * x[i]);
/* 65 */       total += mult;
/*    */     }
/*    */     
/* 68 */     return -1.0D * Math.log(esum / total) / lambda;
/*    */   }
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
/*    */   public LawlessFunction(double[] x, double[] y, int n)
/*    */   {
/* 82 */     this.x = x;
/* 83 */     this.y = y;
/* 84 */     this.n = n;
/*    */   }
/*    */   
/*    */   public abstract double[] evaluate(double paramDouble);
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/evd/LawlessFunction.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */