/*    */ package lc1.association.io;
/*    */ 
/*    */ public class GenotypeData {
/*    */   final int no;
/*    */   
/*  6 */   private GenotypeData(int no) { this.no = no; }
/*    */   
/*    */   public String toString() {
/*  9 */     return this.no;
/*    */   }
/*    */   
/* 12 */   int getNumber() { return this.no; }
/*    */   
/* 14 */   public static final GenotypeData AA = new GenotypeData(0);
/* 15 */   public static final GenotypeData Aa = new GenotypeData(1);
/* 16 */   public static final GenotypeData aa = new GenotypeData(2);
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/GenotypeData.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */