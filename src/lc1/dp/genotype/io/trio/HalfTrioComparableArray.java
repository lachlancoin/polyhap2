/*    */ package lc1.dp.genotype.io.trio;
/*    */ 
/*    */ import lc1.dp.data.representation.ComparableArray;
/*    */ 
/*    */ public class HalfTrioComparableArray extends TrComparableArray
/*    */ {
/*    */   public HalfTrioComparableArray(ComparableArray comp)
/*    */   {
/*  9 */     if (comp.size() != 2) throw new RuntimeException("!!");
/* 10 */     add(comp.get(0));
/* 11 */     this.third = ((ComparableArray)comp.get(1));
/* 12 */     ComparableArray third_pos = new ComparableArray(false);
/* 13 */     ComparableArray th0 = (ComparableArray)comp.get(0);
/* 14 */     int index1 = ((Integer)this.third.get(0)).intValue();
/* 15 */     third_pos.add(th0.get(index1 - 1));
/* 16 */     add(third_pos);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/trio/HalfTrioComparableArray.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */