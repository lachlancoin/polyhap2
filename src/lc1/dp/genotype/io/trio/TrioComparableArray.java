/*    */ package lc1.dp.genotype.io.trio;
/*    */ 
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ 
/*    */ public class TrioComparableArray extends TrComparableArray
/*    */ {
/*    */   public TrioComparableArray(ComparableArray comp)
/*    */   {
/*  9 */     if (comp.size() != 3) throw new RuntimeException("!!");
/* 10 */     for (int i = 0; i < 2; i++) {
/* 11 */       add(comp.get(i));
/*    */     }
/* 13 */     this.third = ((ComparableArray)comp.get(2));
/* 14 */     ComparableArray third_pos = new ComparableArray(false);
/* 15 */     ComparableArray th0 = (ComparableArray)comp.get(0);
/* 16 */     ComparableArray th1 = (ComparableArray)comp.get(1);
/*    */     
/* 18 */     int index1 = ((Integer)this.third.get(0)).intValue();
/* 19 */     int index2 = ((Integer)this.third.get(1)).intValue();
/* 20 */     third_pos.add(th0.get(index1 - 1));third_pos.add(th1.get(index2 - 1));
/* 21 */     add(third_pos);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/trio/TrioComparableArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */