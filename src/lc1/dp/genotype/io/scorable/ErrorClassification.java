/*    */ package lc1.dp.genotype.io.scorable;
/*    */ 
/*    */ import com.braju.format.Format;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.Arrays;
/*    */ import java.util.Comparator;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import lc1.dp.genotype.io.ComparableArray;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ErrorClassification
/*    */ {
/*    */   Map<Object, Integer> stateSpaceIndex;
/*    */   final List ss;
/*    */   public int[][][] errorcase;
/*    */   final double thresh;
/*    */   
/*    */   public ErrorClassification(List ss, double thresh)
/*    */   {
/* 26 */     this.ss = ss;
/* 27 */     this.stateSpaceIndex = new HashMap();
/* 28 */     for (int i = 0; i < ss.size(); i++) {
/* 29 */       this.stateSpaceIndex.put(ss.get(i), Integer.valueOf(i));
/*    */     }
/* 31 */     this.errorcase = init(ss.size());
/* 32 */     this.thresh = thresh;
/*    */   }
/*    */   
/*    */   private int[][][] init(int i) {
/* 36 */     int[][][] res = new int[2][i][i];
/* 37 */     for (int k = 0; k < 2; k++) {
/* 38 */       for (int j = 0; j < i; j++) {
/* 39 */         Arrays.fill(res[k][j], 0);
/*    */       }
/*    */     }
/* 42 */     return res; }
/*    */   
/* 44 */   static Comparator comp = new Comparator() {
/*    */     public int compare(Comparable o1, Comparable o2) {
/* 46 */       if (o1 == o2) return 0;
/* 47 */       if (o1 == null) return -1;
/* 48 */       if (o2 == null) return 1;
/* 49 */       return o1.compareTo(o2);
/*    */     }
/*    */   };
/*    */   
/*    */   public void compare(CompoundScorableObject orig, CompoundScorableObject orig_mod, CompoundScorableObject pred, double[] cert)
/*    */   {
/* 55 */     for (int i = 0; i < orig.length(); i++) {
/* 56 */       if (cert[i] >= this.thresh) {
/* 57 */         compare(new ComparableArray((ComparableArray)orig.getElement(i)), 
/* 58 */           new ComparableArray((ComparableArray)orig_mod.getElement(i)), 
/* 59 */           new ComparableArray((ComparableArray)pred.getElement(i)));
/*    */       }
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   private void compare(ComparableArray orig1, ComparableArray orig_mod1, ComparableArray pred1)
/*    */   {
/* 68 */     for (int i = 0; i < orig1.size(); i++) {
/* 69 */       int cas = orig1.get(i) == orig_mod1.get(i) ? 0 : 1;
/* 70 */       int from = ((Integer)this.stateSpaceIndex.get(orig1.get(i))).intValue();
/* 71 */       int to = ((Integer)this.stateSpaceIndex.get(pred1.get(i))).intValue();
/* 72 */       this.errorcase[cas][from][to] += 1;
/*    */     }
/*    */   }
/*    */   
/*    */ 
/* 77 */   String formatStr = "%5.3g %5.3g %5.3g";
/*    */   
/* 79 */   public void print(PrintWriter pw) { for (int i = 0; i < this.errorcase.length; i++) {
/* 80 */       pw.println("case " + (i == 0 ? "orig==orig_mod" : "orig!=orig_mod"));
/* 81 */       pw.println(this.ss);
/* 82 */       for (int j = 0; j < this.errorcase[i].length; j++) {
/* 83 */         Double[] d = new Double[9];
/* 84 */         for (int k = 0; k < this.errorcase[i][j].length; k++) {
/* 85 */           d[k] = Double.valueOf(this.errorcase[i][j][k]);
/*    */         }
/* 87 */         pw.print("\t");
/* 88 */         pw.println(Format.sprintf(this.formatStr, d));
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/ErrorClassification.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */