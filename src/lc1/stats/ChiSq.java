/*    */ package lc1.stats;
/*    */ 
/*    */ import pal.statistics.ChiSquareDistribution;
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
/*    */ public class ChiSq
/*    */ {
/*    */   public static double compare(double[] ef, int[] of)
/*    */   {
/* 33 */     boolean chi2failed = false;
/*    */     
/*    */ 
/* 36 */     int samples = 0;
/* 37 */     for (int i = 0; i < of.length; i++)
/*    */     {
/* 39 */       samples += of[i];
/*    */     }
/*    */     
/*    */ 
/* 43 */     double chi2 = 0.0D;
/* 44 */     int below1 = 0;
/* 45 */     int below5 = 0;
/* 46 */     for (int i = 0; i < of.length; i++)
/*    */     {
/* 48 */       double efn = ef[i] * samples;
/* 49 */       if (efn < 1.0D)
/*    */       {
/* 51 */         below1++;
/*    */       }
/* 53 */       if (efn < 5.0D)
/*    */       {
/* 55 */         below5++;
/*    */       }
/* 57 */       chi2 += (of[i] - efn) * (of[i] - efn) / efn;
/*    */     }
/*    */     
/*    */ 
/* 61 */     double criticals = chi2prob(of.length - 1, chi2);
/*    */     
/*    */ 
/* 64 */     if (below1 > 0)
/*    */     {
/* 66 */       chi2failed = true;
/*    */     }
/*    */     
/* 69 */     if (below5 > (int)Math.floor(samples / 5.0D))
/*    */     {
/* 71 */       chi2failed = true;
/*    */     }
/*    */     
/* 74 */     if (chi2failed)
/*    */     {
/* 76 */       return -criticals;
/*    */     }
/*    */     
/*    */ 
/* 80 */     return criticals;
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
/*    */ 
/*    */   public static double chi2prob(int deg, double chi2)
/*    */   {
/* 95 */     return 1.0D - ChiSquareDistribution.cdf(chi2, deg);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/ChiSq.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */