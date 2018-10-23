/*    */ package lc1.dp.genotype;
/*    */ 
/*    */ import com.braju.format.Format;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Arrays;
/*    */ import java.util.Collection;
/*    */ import lc1.dp.genotype.io.Constants;
/*    */ 
/*    */ public class SiteNullEmitter implements NullEmitter
/*    */ {
/*    */   final SimpleExtendedDistribution[] nullEm;
/*    */   
/*    */   SiteNullEmitter(int noSnps)
/*    */   {
/* 15 */     double x = Constants.trainAverageNullModel() ? 0.9D : 0.98D;
/* 16 */     this.nullEm = new SimpleExtendedDistribution[noSnps];
/* 17 */     for (int i = 0; i < noSnps; i++) {
/* 18 */       this.nullEm[i] = 
/* 19 */         new SimpleExtendedDistribution(new double[] { x, 1.0D - x }, Double.POSITIVE_INFINITY);
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public double getProb(int i, int j)
/*    */   {
/* 27 */     return this.nullEm[i].probs[j];
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void addCounts(int i, int j, double d)
/*    */   {
/* 34 */     this.nullEm[i].counts[j] += d;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void initialise()
/*    */   {
/* 42 */     for (int i = 0; i < this.nullEm.length; i++) {
/* 43 */       this.nullEm[i].initialise();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public Collection<? extends PseudoDistribution> getCollection()
/*    */   {
/* 52 */     return (Collection)Arrays.asList(this.nullEm);
/*    */   }
/*    */   
/*    */ 
/*    */   public void print(PrintWriter pw, String prefix)
/*    */   {
/* 58 */     StringBuffer sb1 = new StringBuffer(prefix);
/* 59 */     for (int i = 0; i < this.nullEm.length; i++) {
/* 60 */       sb1.append("%8.2g ");
/*    */     }
/* 62 */     pw.println(Format.sprintf(sb1.toString(), HaplotypeEmissionState.emissions(this.nullEm, 0)));
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public void transferCountsToProbs(double pseudocountWeight)
/*    */   {
/* 69 */     if (Constants.trainAverageNullModel()) {
/* 70 */       for (int i = 0; i < this.nullEm.length; i++) {
/* 71 */         this.nullEm[i].transfer(pseudocountWeight);
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */   public int sample(int i) {
/* 77 */     return this.nullEm[i].sample();
/*    */   }
/*    */   
/*    */   public int mostLikely(int pos) {
/* 81 */     return this.nullEm[pos].mostLikely();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/SiteNullEmitter.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */