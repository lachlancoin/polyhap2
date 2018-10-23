/*    */ package lc1.dp.genotype.io;
/*    */ 
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ import lc1.dp.Permutations;
/*    */ 
/*    */ public class ComparableArrayHelper
/*    */ {
/*  9 */   public static final Permutations perm = new Permutations();
/*    */   
/*    */   public static class OrderComparator implements Comparator<ComparableArray> {
/* 12 */     public int compare(ComparableArray c1, ComparableArray c2) { if (c1.size() != c2.size()) {
/* 13 */         throw new RuntimeException("sizes not equal!!! " + c1 + " cf " + c2);
/*    */       }
/* 15 */       Iterator<Comparable> o1 = c1.iterator();
/* 16 */       Iterator<Comparable> o2 = c2.iterator();
/* 17 */       while (o1.hasNext()) {
/* 18 */         Comparable n1 = (Comparable)o1.next();
/* 19 */         Comparable n2 = (Comparable)o2.next();
/*    */         
/* 21 */         int res = n1.compareTo(n2);
/* 22 */         if (res != 0) return res;
/*    */       }
/* 24 */       return 0;
/*    */     }
/*    */   }
/*    */   
/* 28 */   public static Comparator ORDER = new OrderComparator();
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/ComparableArrayHelper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */