/*    */ package lc1.dp;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComparableArrayComparator
/*    */   implements Comparator
/*    */ {
/*    */   public int compare(Object obj1, Object obj2)
/*    */   {
/* 16 */     Comparable[] c1 = (Comparable[])obj1;
/* 17 */     Comparable[] c2 = (Comparable[])obj2;
/* 18 */     if (c1.length != c2.length) {
/* 19 */       throw new RuntimeException("sizes not equal!!!");
/*    */     }
/* 21 */     for (int i = 0; i < c1.length; i++) {
/* 22 */       Comparable n1 = c1[i];
/* 23 */       Comparable n2 = c2[i];
/* 24 */       if (n1 != n2) {
/* 25 */         if (n1 == null) return 1;
/* 26 */         if (n2 == null) return -1;
/* 27 */         return n1.compareTo(n2);
/*    */       }
/*    */     }
/* 30 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/ComparableArrayComparator.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */