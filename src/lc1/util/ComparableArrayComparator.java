/*    */ package lc1.util;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComparableArrayComparator
/*    */   implements Comparator
/*    */ {
/*    */   public int compare(Object obj1, Object obj2)
/*    */   {
/* 13 */     Comparable[] c1 = (Comparable[])obj1;
/* 14 */     Comparable[] c2 = (Comparable[])obj2;
/* 15 */     if (c1.length != c2.length) {
/* 16 */       throw new RuntimeException("sizes not equal!!!");
/*    */     }
/* 18 */     for (int i = 0; i < c1.length; i++) {
/* 19 */       Comparable n1 = c1[i];
/* 20 */       Comparable n2 = c2[i];
/* 21 */       if (n1 != n2) {
/* 22 */         if (n1 == null) return 1;
/* 23 */         if (n2 == null) return -1;
/* 24 */         return n1.compareTo(n2);
/*    */       }
/*    */     }
/* 27 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/util/ComparableArrayComparator.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */