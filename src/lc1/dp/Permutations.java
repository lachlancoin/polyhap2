/*    */ package lc1.dp;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import java.util.TreeSet;
/*    */ import lc1.dp.genotype.io.ComparableArray;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Permutations
/*    */ {
/* 19 */   Map<List<Comparable>, List<ComparableArray>> results = new HashMap();
/*    */   
/*    */ 
/*    */   public List<ComparableArray> get(ComparableArray obj)
/*    */   {
/* 24 */     List<ComparableArray> res = (List)this.results.get(obj);
/* 25 */     if (res == null) {
/* 26 */       Set<ComparableArray> res1 = new TreeSet();
/* 27 */       for (Iterator<ComparableArray> it = calculate(obj).iterator(); it.hasNext();) {
/* 28 */         ComparableArray toadd = (ComparableArray)it.next();
/* 29 */         res1.add(toadd);
/*    */       }
/* 31 */       res = new ArrayList(res1);
/* 32 */       this.results.put(obj, res);
/*    */     }
/*    */     
/* 35 */     return res;
/*    */   }
/*    */   
/* 38 */   static String getString(List<Comparable[]> results) { StringBuffer sb = new StringBuffer();
/* 39 */     Object[] comp; int j; for (Iterator<Comparable[]> it = results.iterator(); it.hasNext(); 
/*    */         
/*    */ 
/* 42 */         j < comp.length)
/*    */     {
/* 40 */       sb.append("[");
/* 41 */       comp = (Object[])it.next();
/* 42 */       j = 0; continue;
/* 43 */       sb.append(comp[j]);
/* 44 */       if (j < comp.length - 1) sb.append(','); else {
/* 45 */         sb.append(']');
/*    */       }
/* 42 */       j++;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 48 */     return sb.toString();
/*    */   }
/*    */   
/* 51 */   public Collection<ComparableArray> calculate(ComparableArray obj) { Collection<ComparableArray> result = new HashSet();
/* 52 */     if (obj.size() == 1) {
/* 53 */       Comparable o = (Comparable)obj.get(0);
/* 54 */       ComparableArray comp_a = new ComparableArray();
/* 55 */       comp_a.add(o);
/* 56 */       result.add(comp_a);
/*    */     }
/*    */     else {
/* 59 */       for (int i = 0; i < obj.size(); i++) {
/* 60 */         ComparableArray l1 = new ComparableArray(obj);
/* 61 */         Comparable newFirstPosition = (Comparable)l1.remove(i);
/* 62 */         Collection<ComparableArray> res_i = calculate(l1);
/* 63 */         for (Iterator<ComparableArray> it = res_i.iterator(); it.hasNext();) {
/* 64 */           ComparableArray nxt = new ComparableArray((List)it.next());
/* 65 */           nxt.add(0, newFirstPosition);
/* 66 */           result.add(nxt);
/*    */         }
/*    */       }
/*    */     }
/*    */     
/* 71 */     return result;
/*    */   }
/*    */   
/* 74 */   public String toString() { StringBuffer sb = new StringBuffer();
/* 75 */     Object[] comp; int j; for (Iterator<List<Comparable>> it = this.results.keySet().iterator(); it.hasNext(); 
/*    */         
/*    */ 
/* 78 */         j < comp.length)
/*    */     {
/* 76 */       sb.append("[");
/* 77 */       comp = ((List)it.next()).toArray();
/* 78 */       j = 0; continue;
/* 79 */       sb.append(comp[j]);
/* 80 */       if (j < comp.length - 1) sb.append(','); else {
/* 81 */         sb.append(']');
/*    */       }
/* 78 */       j++;
/*    */     }
/*    */     
/*    */ 
/*    */ 
/*    */ 
/* 84 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/Permutations.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */