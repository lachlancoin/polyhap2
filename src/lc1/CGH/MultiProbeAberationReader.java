/*    */ package lc1.CGH;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MultiProbeAberationReader
/*    */   extends AbstractAberatiionReader
/*    */ {
/* 14 */   static String[] cols1 = { "Name", "chr", "Start", "End", "Mean", "#probes" };
/*    */   public String[] cols;
/*    */   
/* 17 */   MultiProbeAberationReader(String[] cols, long length, String name) { super(length, name);
/* 18 */     this.cols = cols;
/*    */   }
/*    */   
/* 21 */   public MultiProbeAberationReader(long length, String name) { this(length, cols1, name); }
/*    */   
/*    */   public MultiProbeAberationReader(long length, String[] cols, String name) {
/* 24 */     super(length, name);
/* 25 */     this.cols = cols;
/*    */   }
/*    */   
/*    */   public String[] getCols()
/*    */   {
/* 30 */     return this.cols;
/*    */   }
/*    */   
/*    */   public String getName(String[] str) {
/* 34 */     return this.col[0] >= 0 ? str[this.col[0]] : "";
/*    */   }
/*    */   
/* 37 */   public String getChr(String[] str) { return str[this.col[1]]; }
/*    */   
/*    */   public String getStart(String[] str) {
/* 40 */     return str[this.col[2]];
/*    */   }
/*    */   
/* 43 */   public String getEnd(String[] str) { return str[this.col[3]]; }
/*    */   
/*    */   public int getNoProbes(String[] str) {
/* 46 */     if (this.col[5] >= 0)
/* 47 */       return Integer.parseInt(str[this.col[5]]);
/* 48 */     return 0;
/*    */   }
/*    */   
/* 51 */   public double getNoCopy(String[] str) { if (this.col[4] < 0) return 0.0D;
/* 52 */     return Double.parseDouble(str[this.col[4]]);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/MultiProbeAberationReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */