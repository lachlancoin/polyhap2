/*    */ package lc1.util;
/*    */ 
/*    */ import com.braju.format.Format;
/*    */ 
/*    */ public class IntegerDouble
/*    */ {
/*    */   public Object stateId;
/*    */   public double d;
/*    */   
/*    */   public IntegerDouble(Object i, double d)
/*    */   {
/* 12 */     this.stateId = i;
/* 13 */     this.d = d;
/*    */   }
/*    */   
/*    */ 
/* 17 */   public void offset(int n) { this.stateId = Integer.valueOf(((Integer)this.stateId).intValue() - n); }
/*    */   
/*    */   IntegerDouble(IntegerDouble i1) {
/* 20 */     this.stateId = i1.stateId;
/* 21 */     this.d = i1.d;
/*    */   }
/*    */   
/*    */   public void set(Object stateId, double sc) {
/* 25 */     this.d = sc;
/* 26 */     this.stateId = stateId;
/*    */   }
/*    */   
/* 29 */   public String toString() { return this.stateId + ":" + Format.sprintf("%5.3g", new Object[] { Double.valueOf(this.d) }); }
/*    */   
/*    */   public void add(double sc)
/*    */   {
/* 33 */     this.d += sc;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/util/IntegerDouble.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */