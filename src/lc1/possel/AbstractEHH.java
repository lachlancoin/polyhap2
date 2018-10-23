/*    */ package lc1.possel;
/*    */ 
/*    */ import lc1.dp.data.collection.DataCollection;
/*    */ 
/*    */ public abstract class AbstractEHH
/*    */ {
/*    */   int start;
/*    */   int end;
/*    */   final String name;
/*    */   DataCollection sdt;
/*    */   
/*    */   public void setCore(int i, int i2, String[] string, Double[][] scores, boolean[] doLR, java.util.List<Double>[][] in) {
/* 13 */     this.start = i;
/* 14 */     this.end = i2;
/*    */   }
/*    */   
/* 17 */   public AbstractEHH(DataCollection coll, String f) throws Exception { this.sdt = ((DataCollection)coll.clone());
/* 18 */     this.name = f;
/*    */   }
/*    */   
/*    */   public abstract double score(Double[][] paramArrayOfDouble);
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/AbstractEHH.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */