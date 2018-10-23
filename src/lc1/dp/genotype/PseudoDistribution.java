/*    */ package lc1.dp.genotype;
/*    */ 
/*    */ import cern.jet.random.Exponential;
/*    */ import cern.jet.random.Gamma;
/*    */ import java.util.Collection;
/*    */ import java.util.Iterator;
/*    */ import pal.math.UnivariateFunction;
/*    */ import pal.math.UnivariateMinimum;
/*    */ 
/*    */ 
/*    */ public abstract class PseudoDistribution
/*    */ {
/* 13 */   static Gamma dist = new Gamma(0.1D, 1.0D, null);
/* 14 */   static Exponential dist1 = new Exponential(10.0D, null);
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public static double optimiseU(Collection<PseudoDistribution> l, double u, final double upperBound)
/*    */   {
/* 21 */     UnivariateFunction f = new UnivariateFunction() {
/*    */       public double evaluate(double arg0) {
/* 23 */         double logProb = 0.0D;
/* 24 */         for (Iterator<PseudoDistribution> it = PseudoDistribution.this.iterator(); it.hasNext();) {
/* 25 */           logProb += ((PseudoDistribution)it.next()).evaluate(arg0);
/*    */         }
/* 27 */         return -1.0D * (logProb + Math.log(PseudoDistribution.dist.pdf(1.0D / arg0)));
/*    */       }
/*    */       
/*    */       public double getLowerBound() {
/* 31 */         return 0.001D;
/*    */       }
/*    */       
/*    */       public double getUpperBound() {
/* 35 */         return upperBound;
/*    */       }
/*    */       
/* 38 */     };
/* 39 */     UnivariateMinimum uvm = new UnivariateMinimum();
/* 40 */     double u1 = uvm.findMinimum(u, f);
/*    */     
/* 42 */     return u1;
/*    */   }
/*    */   
/*    */   public static void setLambda(double shape, double scale) {
/* 46 */     dist = new Gamma(shape, scale, null);
/* 47 */     dist1 = new Exponential(scale, null);
/*    */   }
/*    */   
/*    */   public double evaluate(double pseudocountWeight) {
/* 51 */     transfer(pseudocountWeight);
/* 52 */     return logProb();
/*    */   }
/*    */   
/*    */   abstract double logProb();
/*    */   
/*    */   abstract void transfer(double paramDouble);
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/PseudoDistribution.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */