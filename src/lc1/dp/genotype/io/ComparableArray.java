/*    */ package lc1.dp.genotype.io;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ComparableArray
/*    */   extends ArrayList<Comparable>
/*    */   implements Comparable
/*    */ {
/*    */   public static ComparableArray make(Comparable comp)
/*    */   {
/* 18 */     ComparableArray compA = new ComparableArray();
/* 19 */     compA.add(comp);
/* 20 */     return compA;
/*    */   }
/*    */   
/*    */ 
/*    */   public static ComparableArray make(Comparable ma, Comparable ma2)
/*    */   {
/* 26 */     ComparableArray compA = new ComparableArray();
/* 27 */     compA.add(ma);compA.add(ma2);
/* 28 */     return compA;
/*    */   }
/*    */   
/* 31 */   public ComparableArray() { super(2); }
/*    */   
/*    */   public ComparableArray(List<Comparable> name)
/*    */   {
/* 35 */     super(name);
/*    */   }
/*    */   
/*    */   public int compareTo(Object obj2) {
/* 39 */     ComparableArray c1 = this;
/* 40 */     ComparableArray c2 = (ComparableArray)obj2;
/* 41 */     if (c1.size() != c2.size()) {
/* 42 */       throw new RuntimeException("sizes not equal!!!");
/*    */     }
/* 44 */     Iterator<Comparable> o1 = c1.iterator();
/* 45 */     Iterator<Comparable> o2 = c2.iterator();
/* 46 */     while (o1.hasNext()) {
/* 47 */       Comparable n1 = (Comparable)o1.next();
/* 48 */       Comparable n2 = (Comparable)o2.next();
/* 49 */       if (n1 != n2) {
/* 50 */         if (n1 == null) return 1;
/* 51 */         if (n2 == null) return -1;
/* 52 */         return n1.compareTo(n2);
/*    */       }
/*    */     }
/* 55 */     return 0;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/ComparableArray.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */