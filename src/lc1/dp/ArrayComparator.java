/*    */ package lc1.dp;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ class ArrayComparator
/*    */   implements Comparator<Comparable[]>
/*    */ {
/*    */   public int compare(Comparable[] o1, Comparable[] o2)
/*    */   {
/* 10 */     for (int i = 0; i < o1.length; i++) {
/* 11 */       if ((o1[i] != null) || (o2[i] != null)) {
/* 12 */         if (o1[i] == null) return -1;
/* 13 */         if (o2[i] == null) { return 1;
/*    */         }
/* 15 */         int res = o1[i].compareTo(o2[i]);
/* 16 */         if (res != 0) return res;
/*    */       }
/*    */     }
/* 19 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/ArrayComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */