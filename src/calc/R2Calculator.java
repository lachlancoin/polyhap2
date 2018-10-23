/*    */ package calc;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class R2Calculator implements LDCalculator
/*    */ {
/*    */   public void normalise(double[][] d)
/*    */   {
/*  9 */     double sum = 0.0D;
/* 10 */     for (int i = 0; i < d.length; i++) {
/* 11 */       for (int j = 0; j < d.length; j++) {
/* 12 */         sum += d[i][j];
/*    */       }
/*    */     }
/* 15 */     for (int i = 0; i < d.length; i++) {
/* 16 */       for (int j = 0; j < d.length; j++) {
/* 17 */         d[i][j] /= sum;
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public double calculate(double[][] p_ij)
/*    */   {
/* 26 */     normalise(p_ij);
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
/* 37 */     double[][] r = new double[p_ij.length][p_ij.length];
/* 38 */     for (int i = 0; i < p_ij.length; i++) {
/* 39 */       for (int j = 0; j < p_ij.length; j++) {
/* 40 */         double num = Math.pow(p_ij[i][j] - p_i[i] * p_j[j], 2.0D);
/* 41 */         double denom = p_i[i] * p_j[j] * (1.0D - p_i[i]) * (1.0D - p_j[j]);
/* 42 */         r[i][j] = (num == 0.0D ? 0.0D : num / denom);
/*    */       }
/*    */     }
/* 45 */     if (Double.isNaN(r[0][0])) throw new RuntimeException("!!");
/* 46 */     return r[0][0];
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/calc/R2Calculator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */