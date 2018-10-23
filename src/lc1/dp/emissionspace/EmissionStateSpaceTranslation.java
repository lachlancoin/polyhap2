/*    */ package lc1.dp.emissionspace;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.HashSet;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Map.Entry;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EmissionStateSpaceTranslation
/*    */ {
/* 15 */   Map<Integer, Integer> bigToSmall = new HashMap();
/* 16 */   Map<Integer, int[]> smallToBig = new HashMap();
/*    */   
/*    */   public EmissionStateSpaceTranslation(EmissionStateSpace big, EmissionStateSpace small, boolean replaceB)
/*    */   {
/* 20 */     Map<Integer, Set<Integer>> smallToBig1 = new HashMap();
/* 21 */     String[] stri = getHapStrings(big);
/* 22 */     String[] strj = getHapStrings(small);
/* 23 */     if (stri.length != strj.length) {
/* 24 */       for (int i = 0; i < stri.length; i++) {
/* 25 */         String st = stri[i].replaceAll("X", "AA").replaceAll("Y", "BB").replaceAll("Z", "AB")
/* 26 */           .replaceAll("T", "AAA").replaceAll("U", "AAB").replaceAll("V", "ABB").replaceAll("W", "BBB");
/* 27 */         if (replaceB) { st = 
/* 28 */             st.replaceAll("B", "A");
/*    */         }
/*    */         
/* 31 */         for (int j = 0; j < strj.length; j++)
/*    */         {
/* 33 */           String st1 = strj[j].replaceAll("0", "_").replaceAll("1", "A").replaceAll("2", "AA").replaceAll("3", "AAA");
/* 34 */           if (st.equals(st1)) {
/* 35 */             this.bigToSmall.put(Integer.valueOf(i), Integer.valueOf(j));
/* 36 */             Set<Integer> l = (Set)smallToBig1.get(Integer.valueOf(j));
/* 37 */             if (l == null) smallToBig1.put(Integer.valueOf(j), l = new HashSet());
/* 38 */             l.add(Integer.valueOf(i));
/* 39 */             break;
/*    */           }
/*    */         }
/*    */       }
/*    */       
/* 44 */       for (Iterator<Map.Entry<Integer, Set<Integer>>> it = smallToBig1.entrySet().iterator(); it.hasNext();) {
/* 45 */         Map.Entry<Integer, Set<Integer>> nxt = (Map.Entry)it.next();
/* 46 */         int[] res = new int[((Set)nxt.getValue()).size()];
/* 47 */         int i = 0;
/* 48 */         for (Iterator<Integer> it1 = ((Set)nxt.getValue()).iterator(); it1.hasNext(); i++) {
/* 49 */           res[i] = ((Integer)it1.next()).intValue();
/*    */         }
/* 51 */         this.smallToBig.put((Integer)nxt.getKey(), res);
/*    */       }
/*    */     }
/*    */     else {
/* 55 */       for (int i = 0; i < stri.length; i++) {
/* 56 */         this.bigToSmall.put(Integer.valueOf(i), Integer.valueOf(i));
/* 57 */         this.smallToBig.put(Integer.valueOf(i), new int[] { i });
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */   private static String[] getHapStrings(EmissionStateSpace small)
/*    */   {
/* 65 */     String[] res = new String[small.size()];
/* 66 */     for (int j = 0; j < small.size(); j++)
/*    */     {
/* 68 */       res[j] = small.getHaploPairString(small.get(j));
/*    */     }
/*    */     
/* 71 */     return res;
/*    */   }
/*    */   
/*    */   public Integer bigToSmall(int i) {
/* 75 */     return (Integer)this.bigToSmall.get(Integer.valueOf(i));
/*    */   }
/*    */   
/* 78 */   public int[] smallToBig(int i) { return (int[])this.smallToBig.get(Integer.valueOf(i)); }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/emissionspace/EmissionStateSpaceTranslation.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */