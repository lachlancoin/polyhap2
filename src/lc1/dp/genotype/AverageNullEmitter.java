/*    */ package lc1.dp.genotype;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import lc1.dp.genotype.io.Constants;
/*    */ 
/*    */ public class AverageNullEmitter
/*    */   implements NullEmitter
/*    */ {
/*    */   final SimpleExtendedDistribution nullEm;
/*    */   
/*    */   AverageNullEmitter(int noSnps)
/*    */   {
/* 15 */     double x = Constants.trainAverageNullModel() ? 0.5D : 1.0D;
/* 16 */     this.nullEm = new SimpleExtendedDistribution(new double[] { x, 1.0D - x }, Double.POSITIVE_INFINITY);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public double getProb(int i, int j)
/*    */   {
/* 24 */     return this.nullEm.probs[j];
/*    */   }
/*    */   
/*    */   public int sample(int i) {
/* 28 */     return this.nullEm.sample();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addCounts(int i, int j, double d)
/*    */   {
/* 35 */     this.nullEm.counts[j] += d;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initialise()
/*    */   {
/* 43 */     this.nullEm.initialise();
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Collection<? extends PseudoDistribution> getCollection()
/*    */   {
/* 51 */     return (Collection)Arrays.asList(new SimpleExtendedDistribution[] { this.nullEm });
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void print(PrintWriter pw, String prefix)
/*    */   {
/* 58 */     pw.println(prefix + " " + this.nullEm.probs[0] + " " + this.nullEm.probs[1]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void transferCountsToProbs(double pseudocountWeight)
/*    */   {
/* 65 */     if (Constants.trainAverageNullModel()) {
/* 66 */       this.nullEm.transfer(pseudocountWeight);
/*    */     }
/*    */   }
/*    */   
/*    */   public int mostLikely(int pos)
/*    */   {
/* 72 */     return this.nullEm.mostLikely();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/AverageNullEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */