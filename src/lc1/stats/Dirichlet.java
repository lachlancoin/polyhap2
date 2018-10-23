/*    */ package lc1.stats;
/*    */ 
/*    */ import cern.jet.random.Gamma;
/*    */ import edu.cornell.lassp.houle.RngPack.RandomElement;
/*    */ 
/*    */ public class Dirichlet extends Sampler
/*    */ {
/*    */   final double u;
/*    */   Gamma[] g;
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/* 13 */     Double[] dist = { Double.valueOf(0.3333333333333333D), Double.valueOf(0.3333333333333333D), Double.valueOf(0.3333333333333333D) };
/* 14 */     Dirichlet d = new Dirichlet(dist, 1.0D);
/* 15 */     for (int i = 0; i < 10; i++) {
/* 16 */       Double[] arrayOfDouble1 = d.sample();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public Dirichlet(double[] dist1, double u)
/*    */   {
/* 24 */     super(dist1, u);
/* 25 */     this.u = u;
/* 26 */     this.g = new Gamma[this.dist.length];
/* 27 */     set(u);
/*    */   }
/*    */   
/*    */   public Dirichlet(Double[] dist1, double u) {
/* 31 */     super(dist1, u);
/* 32 */     this.u = u;
/* 33 */     this.g = new Gamma[this.dist.length];
/* 34 */     set(u);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void set(double u)
/*    */   {
/* 42 */     for (int i = 0; i < this.g.length; i++) {
/* 43 */       if (this.dist[i].doubleValue() > 0.0D) {
/* 44 */         if (this.g[i] != null) this.g[i].setState(this.dist[i].doubleValue() * u, 1.0D);
/* 45 */         this.g[i] = new Gamma(this.dist[i].doubleValue() * u, 1.0D, this.re);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/* 51 */   RandomElement re = new RandomElement()
/*    */   {
/* 53 */     public double raw() { return lc1.util.Constants.rand.nextDouble(); }
/*    */   };
/*    */   
/*    */   public Double[] sample() {
/* 57 */     if (this.u == Double.POSITIVE_INFINITY) return this.dist;
/* 58 */     Double[] res = new Double[this.dist.length];
/* 59 */     double sum = 0.0D;
/* 60 */     for (int i = 0; i < res.length; i++) {
/* 61 */       res[i] = Double.valueOf(this.g[i] == null ? 0.0D : Math.max(1.0E-5D, this.g[i].nextDouble()));
/* 62 */       sum += res[i].doubleValue();
/*    */     }
/* 64 */     for (int i = 0; i < res.length; i++) {
/* 65 */       res[i] = Double.valueOf(res[i].doubleValue() / sum);
/*    */     }
/* 67 */     return res;
/*    */   }
/*    */   
/*    */   public double u() {
/* 71 */     return this.u;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/stats/Dirichlet.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */