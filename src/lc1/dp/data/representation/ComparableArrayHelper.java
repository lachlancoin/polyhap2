/*    */ package lc1.dp.data.representation;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ 
/*    */ public class ComparableArrayHelper
/*    */ {
/*    */   public static class OrderComparator implements java.util.Comparator<ComparableArray>
/*    */   {
/*    */     public int compare(ComparableArray c1, ComparableArray c2)
/*    */     {
/* 11 */       if (c1.size() != c2.size()) {
/* 12 */         throw new RuntimeException("sizes not equal!!! " + c1 + " cf " + c2);
/*    */       }
/* 14 */       Iterator<Comparable> o1 = c1.iterator();
/* 15 */       Iterator<Comparable> o2 = c2.iterator();
/* 16 */       while (o1.hasNext()) {
/* 17 */         Comparable n1 = (Comparable)o1.next();
/* 18 */         Comparable n2 = (Comparable)o2.next();
/*    */         
/* 20 */         int res = n1.compareTo(n2);
/* 21 */         if (res != 0) return res;
/*    */       }
/* 23 */       return 0;
/*    */     }
/*    */   }
/*    */   
/* 27 */   public static java.util.Comparator ORDER = new OrderComparator();
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/data/representation/ComparableArrayHelper.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */