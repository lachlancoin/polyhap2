/*    */ package lc1.stats;
/*    */ 
/*    */ import java.util.Map;
/*    */ 
/*    */ public class Interpolation {
/*    */   double[] mean;
/*    */   double[] x;
/*    */   
/*  9 */   public Interpolation(double[] x, double[] mean) { this.mean = mean;
/* 10 */     this.x = x;
/*    */   }
/*    */   
/*    */   private double getSlope(int x_1, int x_2)
/*    */   {
/* 15 */     double y_1 = this.mean[x_1];
/* 16 */     double y_2 = this.mean[x_2];
/* 17 */     return (y_2 - y_1) / (x_2 - x_1);
/*    */   }
/*    */   
/* 20 */   Map<Double, Double> sc = new java.util.HashMap();
/*    */   
/*    */   public double getScore(double x1) {
/* 23 */     Double res = (Double)this.sc.get(Double.valueOf(x1));
/* 24 */     if (res == null) {
/* 25 */       this.sc.put(Double.valueOf(x1), res = Double.valueOf(getScore1(x1)));
/*    */     }
/* 27 */     return res.doubleValue();
/*    */   }
/*    */   
/* 30 */   public double getScore2(double x1) { Double res = (Double)this.sc.get(Double.valueOf(x1));
/* 31 */     if (res == null) {
/* 32 */       return getScore1(x1);
/*    */     }
/* 34 */     return res.doubleValue();
/*    */   }
/*    */   
/*    */   private double getScore1(double x1) {
/* 38 */     if (x1 < this.x[0]) throw new RuntimeException(x1 + " !! " + this.x[0]);
/* 39 */     if (x1 > this.x[(this.x.length - 1)]) throw new RuntimeException(x1 + " !! " + this.x[(this.x.length - 1)]);
/* 40 */     if (x1 == this.x[0]) return this.mean[0];
/* 41 */     for (int i = 0; i < this.x.length - 1; i++) {
/* 42 */       if (x1 == this.x[(i + 1)]) return this.mean[(i + 1)];
/* 43 */       if ((x1 > this.x[i]) && (x1 < this.x[(i + 1)])) {
/* 44 */         double slope = getSlope(i, i + 1);
/* 45 */         return this.mean[i] + slope * (x1 - this.x[i]);
/*    */       }
/*    */     }
/* 48 */     throw new RuntimeException("!!");
/*    */   }
/*    */   
/*    */   private double extrapolate(double x, int x1, double slope) {
/* 52 */     return this.mean[x1] + slope * (x - x1);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/Interpolation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */