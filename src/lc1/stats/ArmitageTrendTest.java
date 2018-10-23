/*    */ package lc1.stats;
/*    */ import java.io.PrintStream;
/*    */ 
/*  4 */ public class ArmitageTrendTest { ChiSq chisq = new ChiSq();
/*    */   
/*  6 */   public static void main(String[] args) { double[] cse = { 89.0D, 369.0D, 342.0D };
/*  7 */     double[] cntrl = { 56.0D, 250.0D, 266.0D };
/*  8 */     ArmitageTrendTest att = new ArmitageTrendTest();
/*  9 */     att.set(cse, cntrl);
/* 10 */     System.err.println(att.chisq() + " " + att.getSig());
/* 11 */     att.set(cse, cntrl);
/* 12 */     System.err.println(att.chisq() + " " + att.getSig());
/*    */   }
/*    */   
/*    */   double[] cse;
/*    */   double[] cntrl;
/*    */   double[] total;
/* 18 */   double sum_nx = 0.0D;
/* 19 */   double sum_Nx = 0.0D;
/* 20 */   double sum_Nx2 = 0.0D;
/* 21 */   double T = 0.0D;
/* 22 */   double t = 0.0D;
/*    */   
/*    */   public void set(double[] cse, double[] cntrl) {
/* 25 */     this.cse = cse;
/* 26 */     this.cntrl = cntrl;
/* 27 */     this.total = new double[cse.length];
/* 28 */     this.sum_nx = 0.0D;
/* 29 */     this.sum_Nx = 0.0D;
/* 30 */     this.sum_Nx2 = 0.0D;
/* 31 */     this.T = 0.0D;
/* 32 */     this.t = 0.0D;
/* 33 */     for (int i = 0; i < this.total.length; i++) {
/* 34 */       this.total[i] = (cse[i] + cntrl[i]);
/* 35 */       this.T += this.total[i];
/* 36 */       this.t += cse[i];
/* 37 */       this.sum_nx += i * cse[i];
/* 38 */       this.sum_Nx += i * this.total[i];
/* 39 */       this.sum_Nx2 += Math.pow(i, 2.0D) * this.total[i];
/*    */     }
/*    */   }
/*    */   
/*    */   public double getSig() {
/* 44 */     double res = ChiSq.chi2prob(1, chisq());
/* 45 */     return res;
/*    */   }
/*    */   
/* 48 */   double chisq() { double num = this.T * Math.pow(this.T * this.sum_nx - this.t * this.sum_Nx, 2.0D);
/* 49 */     double denom = this.t * (this.T - this.t) * (this.T * this.sum_Nx2 - Math.pow(this.sum_Nx, 2.0D));
/* 50 */     if (num == 0.0D) return 0.0D;
/* 51 */     return num / denom;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/ArmitageTrendTest.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */