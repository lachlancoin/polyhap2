/*    */ package lc1.association.io;
/*    */ 
/*    */ public class GenotypeValue
/*    */ {
/*    */   final Short value;
/*  6 */   public static GenotypeValue negone = new GenotypeValue(-1);
/*  7 */   public static GenotypeValue zero = new GenotypeValue(0);
/*  8 */   public static GenotypeValue one = new GenotypeValue(1);
/*    */   
/*    */   public static GenotypeValue make(int val) {
/* 11 */     if (val == -1) return negone;
/* 12 */     if (val == 0) return zero;
/* 13 */     if (val == 1) return one;
/* 14 */     return null;
/*    */   }
/*    */   
/*    */   private GenotypeValue(int value) {
/* 18 */     this(new Short((short)value));
/*    */   }
/*    */   
/* 21 */   private GenotypeValue(Short value) { this.value = value; }
/*    */   
/*    */ 
/*    */ 
/*    */   public Short getVal()
/*    */   {
/* 27 */     return this.value;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/GenotypeValue.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */