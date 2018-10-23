/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class LocCompare {
/*    */   Integer[] oneVtwo;
/*    */   Integer[] twoVone;
/*    */   int[] noProbes;
/*    */   
/*    */   public String toString() {
/* 11 */     return this.name1 + " " + this.name2 + " " + java.util.Arrays.asList(this.oneVtwo) + " " + java.util.Arrays.asList(ratios(this.oneVtwo)) + " " + java.util.Arrays.asList(this.twoVone) + " " + java.util.Arrays.asList(ratios(this.twoVone));
/*    */   }
/*    */   
/* 14 */   private Double[] ratios(Integer[] r) { return new Double[] { Double.valueOf(r[0].intValue() / r[1].intValue()), Double.valueOf(r[2].intValue() / r[3].intValue()) }; }
/*    */   
/*    */ 
/*    */   String name1;
/*    */   
/*    */   String name2;
/*    */   PrintWriter out1;
/*    */   PrintWriter out2;
/* 22 */   static int overlp = 2;
/*    */   
/* 24 */   LocCompare(Locreader loc1, Locreader loc2, java.io.File logDir) throws Exception { this.name1 = loc1.name;
/* 25 */     this.name2 = loc2.name;
/* 26 */     PrintWriter out1 = new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(new java.io.File(logDir, this.name1 + "v" + this.name2))));
/* 27 */     PrintWriter out2 = new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(new java.io.File(logDir, this.name2 + "v" + this.name1))));
/* 28 */     this.oneVtwo = loc1.detected(loc2, overlp, out1);
/* 29 */     this.twoVone = loc2.detected(loc1, overlp, out2);
/* 30 */     this.noProbes = new int[] { loc1.probes.size(), loc2.probes.size() };
/*    */     
/* 32 */     System.err.println(this);
/* 33 */     System.err.println(this.noProbes[0] + " " + this.noProbes[1]);
/* 34 */     out1.flush();
/* 35 */     out2.flush();
/* 36 */     out1.close();
/* 37 */     out2.close();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/LocCompare.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */