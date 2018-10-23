/*    */ package lc1.stats;
/*    */ 
/*    */ import cern.colt.function.DoubleDoubleFunction;
/*    */ import cern.colt.matrix.DoubleMatrix1D;
/*    */ import cern.colt.matrix.impl.DenseDoubleMatrix1D;
/*    */ import cern.colt.matrix.linalg.Algebra;
/*    */ 
/*    */ public class TOwen
/*    */ {
/*    */   DoubleMatrix1D h;
/*    */   double a;
/* 12 */   Algebra alg = new Algebra();
/*    */   int jmax;
/*    */   
/* 15 */   TOwen(double[] h, double a, int jmax, int cutpoint) { this.h = new DenseDoubleMatrix1D(h);
/* 16 */     this.a = a;
/* 17 */     this.jmax = jmax;
/* 18 */     this.cutpoint = cutpoint;
/*    */   }
/*    */   
/*    */   int cutpoint;
/* 22 */   public static cern.colt.matrix.DoubleMatrix2D multOuter(DoubleMatrix1D a, DoubleMatrix1D b, DoubleDoubleFunction f) { double[][] res = new double[a.size()][b.size()];
/* 23 */     for (int i = 0; i < res.length; i++) {
/* 24 */       for (int j = 0; j < res[i].length; j++) {
/* 25 */         res[i][j] = f.apply(a.get(i), b.get(j));
/*    */       }
/*    */     }
/* 28 */     return new cern.colt.matrix.impl.DenseDoubleMatrix2D(res);
/*    */   }
/*    */   
/*    */   class Fui implements DoubleDoubleFunction { Fui() {}
/*    */     
/* 33 */     public double apply(double h, double i) { return Math.pow(h, 2.0D * i) / (Math.pow(2.0D, i) * cern.jet.stat.Gamma.gamma(i + 1.0D)); }
/*    */   }
/*    */   
/*    */   double[] makevec(int st, int end) {
/* 37 */     double[] res = new double[end - st];
/* 38 */     for (int i = 0; i < res.length; i++) {
/* 39 */       res[i] = (st + i);
/*    */     }
/* 41 */     return res;
/*    */   }
/*    */   
/* 44 */   class LowProcedure implements cern.colt.function.DoubleProcedure { boolean low = true;
/*    */     
/* 46 */     public LowProcedure(boolean low) { this.low = low; }
/*    */     
/*    */     public boolean apply(double arg0) {
/* 49 */       return arg0 < TOwen.this.cutpoint;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/TOwen.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */