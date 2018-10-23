/*    */ package calc;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class DPrimeCalculator implements LDCalculator
/*    */ {
/*    */   public void normalise(double[][] d) {
/*  8 */     double sum = 0.0D;
/*  9 */     for (int i = 0; i < d.length; i++) {
/* 10 */       for (int j = 0; j < d.length; j++) {
/* 11 */         sum += d[i][j];
/*    */       }
/*    */     }
/* 14 */     for (int i = 0; i < d.length; i++) {
/* 15 */       for (int j = 0; j < d.length; j++) {
/* 16 */         d[i][j] /= sum;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public double calculate(double[][] p_ij)
/*    */   {
/* 25 */     normalise(p_ij);
/*    */     
/* 27 */     double[] p_i = new double[p_ij.length];
/* 28 */     double[] p_j = new double[p_ij.length];
/* 29 */     Arrays.fill(p_i, 0.0D);
/* 30 */     Arrays.fill(p_j, 0.0D);
/* 31 */     for (int i = 0; i < p_ij.length; i++) {
/* 32 */       for (int j = 0; j < p_ij.length; j++) {
/* 33 */         p_i[i] += p_ij[i][j];
/* 34 */         p_j[j] += p_ij[i][j];
/*    */       }
/*    */     }
/*    */     
/* 38 */     double[][] r = new double[p_ij.length][p_ij.length];
/* 39 */     for (int i = 0; i < p_ij.length; i++) {
/* 40 */       for (int j = 0; j < p_ij.length; j++) {
/* 41 */         p_ij[i][j] -= p_i[i] * p_j[j];
/*    */       }
/*    */     }
/* 44 */     double Dminus = -Math.min(p_i[0] * p_j[0], p_i[1] * p_j[1]);
/* 45 */     double Dplis = Math.min(p_i[0] * p_j[1], p_i[1] * p_j[0]);
/* 46 */     double Dprime = r[0][0] > 0.0D ? r[0][0] / Dplis : 
/* 47 */       r[0][0] / Dminus;
/* 48 */     return Dprime;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/calc/DPrimeCalculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */