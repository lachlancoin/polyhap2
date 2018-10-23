/*    */ package lc1.dp;
/*    */ 
/*    */ import cern.jet.random.Gamma;
/*    */ import edu.cornell.lassp.houle.RngPack.RandomElement;
/*    */ 
/*    */ public class Dirichlet implements java.io.Serializable
/*    */ {
/*    */   public Double[] dist;
/*    */   final double u;
/*    */   Gamma[] g;
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 14 */     Double[] dist = { Double.valueOf(0.3333333333333333D), Double.valueOf(0.3333333333333333D), Double.valueOf(0.3333333333333333D) };
/* 15 */     Dirichlet d = new Dirichlet(dist, 1.0D);
/* 16 */     for (int i = 0; i < 10; i++) {
/* 17 */       Double[] arrayOfDouble1 = d.sample();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Dirichlet(double[] dist1, double u)
/*    */   {
/* 26 */     this.u = u;
/* 27 */     this.dist = new Double[dist1.length];
/* 28 */     for (int i = 0; i < this.dist.length; i++) {
/* 29 */       this.dist[i] = Double.valueOf(dist1[i]);
/*    */     }
/* 31 */     this.g = new Gamma[this.dist.length];
/* 32 */     set(u);
/*    */   }
/*    */   
/*    */   public Dirichlet(Double[] dist1, double u) {
/* 36 */     this.u = u;
/* 37 */     this.dist = new Double[dist1.length];
/* 38 */     for (int i = 0; i < this.dist.length; i++) {
/* 39 */       this.dist[i] = dist1[i];
/*    */     }
/* 41 */     this.g = new Gamma[this.dist.length];
/* 42 */     set(u);
/*    */   }
/*    */   
/*    */   public void set(double u) {
/* 46 */     if (u == Double.POSITIVE_INFINITY) return;
/* 47 */     for (int i = 0; i < this.g.length; i++) {
/* 48 */       this.g[i] = (this.dist[i].doubleValue() > 0.0D ? new Gamma(this.dist[i].doubleValue() * u, 1.0D, this.re) : null);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/* 53 */   RandomElement re = new RandomElement()
/*    */   {
/* 55 */     public double raw() { return lc1.dp.genotype.io.Constants.rand.nextDouble(); }
/*    */   };
/*    */   
/*    */   public Double[] sample() {
/* 59 */     if (this.u == Double.POSITIVE_INFINITY) return this.dist;
/* 60 */     Double[] res = new Double[this.dist.length];
/* 61 */     double sum = 0.0D;
/* 62 */     for (int i = 0; i < res.length; i++) {
/* 63 */       res[i] = Double.valueOf(this.g[i] == null ? 0.0D : Math.max(1.0E-5D, this.g[i].nextDouble()));
/* 64 */       sum += res[i].doubleValue();
/*    */     }
/* 66 */     for (int i = 0; i < res.length; i++) {
/* 67 */       res[i] = Double.valueOf(res[i].doubleValue() / sum);
/*    */     }
/* 69 */     return res;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/Dirichlet.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */