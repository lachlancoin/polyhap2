/*    */ package lc1.dp.genotype.io;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import lc1.dp.genotype.ExponentialHaplotypeHMM;
/*    */ import lc1.dp.genotype.FreeHaplotypeHMM;
/*    */ import lc1.dp.genotype.HaplotypeHMM;
/*    */ 
/*    */ public class HaplotypeHMMIterator implements Iterator<HaplotypeHMM>
/*    */ {
/*    */   final int noSites;
/*    */   final int count;
/* 12 */   int i = 0;
/*    */   
/* 14 */   public HaplotypeHMMIterator(int noSites, int count) { this.noSites = noSites;
/* 15 */     this.count = count;
/*    */   }
/*    */   
/*    */   public HaplotypeHMM next() {
/* 19 */     this.i += 1;
/* 20 */     return 
/* 21 */       Constants.numItExp() == -1 ? 
/* 22 */       new FreeHaplotypeHMM("geno", Constants.numF(), this.noSites, Constants.u_global()) : 
/* 23 */       new ExponentialHaplotypeHMM("geno", Constants.numF(), this.noSites, Constants.u_global());
/*    */   }
/*    */   
/*    */   public boolean hasNext()
/*    */   {
/* 28 */     return this.i < this.count;
/*    */   }
/*    */   
/*    */   public void remove() {}
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/HaplotypeHMMIterator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */