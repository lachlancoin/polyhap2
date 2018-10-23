/*    */ package lc1.CGH;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ProbeReader
/*    */   extends AbstractAberatiionReader
/*    */ {
/* 10 */   String[] cols = { "aaa", "ChrName", "Start", "Stop", "aaaa" };
/*    */   
/*    */   ProbeReader(String[] cols) {
/* 13 */     super(1000000L, "");
/* 14 */     this.cols = cols;
/*    */   }
/*    */   
/* 17 */   public ProbeReader() { super(1000000L, ""); }
/*    */   
/*    */ 
/*    */   public String[] getCols()
/*    */   {
/* 22 */     return this.cols;
/*    */   }
/*    */   
/*    */   public String getName(String[] str) {
/* 26 */     return "";
/*    */   }
/*    */   
/* 29 */   public String getChr(String[] str) { return str[this.col[1]]; }
/*    */   
/*    */   public String getStart(String[] str) {
/* 32 */     return str[this.col[2]];
/*    */   }
/*    */   
/* 35 */   public String getEnd(String[] str) { return str[this.col[3]]; }
/*    */   
/*    */   public int getNoProbes(String[] str) {
/* 38 */     return -1;
/*    */   }
/*    */   
/*    */   public boolean exclude(String[] str) {
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   public double getNoCopy(String[] str) {
/* 46 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/ProbeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */