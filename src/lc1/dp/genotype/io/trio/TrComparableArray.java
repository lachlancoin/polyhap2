/*    */ package lc1.dp.genotype.io.trio;
/*    */ 
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ 
/*    */ public abstract class TrComparableArray extends ComparableArray {
/*    */   public ComparableArray third;
/*    */   
/*    */   TrComparableArray() {
/*  9 */     super(true);
/*    */   }
/*    */   
/*    */   public Comparable getReal(int j) {
/* 13 */     if (j == size() - 1) return this.third;
/* 14 */     return get(j);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/trio/TrComparableArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */