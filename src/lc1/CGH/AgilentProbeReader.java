/*    */ package lc1.CGH;
/*    */ 
/*    */ 
/*    */ public class AgilentProbeReader
/*    */   extends AbstractAberatiionReader
/*    */ {
/*    */   public AgilentProbeReader(long lengthLim)
/*    */   {
/*  9 */     super(lengthLim, "");
/*    */   }
/*    */   
/* 12 */   String[] cols = { "ChromNum", "GeneName" };
/*    */   
/*    */ 
/*    */ 
/*    */   public String[] getCols()
/*    */   {
/* 18 */     return this.cols;
/*    */   }
/*    */   
/*    */   public String getName(String[] str) {
/* 22 */     return "";
/*    */   }
/*    */   
/* 25 */   public String getChr(String[] str) { return str[this.col[0]]; }
/*    */   
/*    */   public String getStart(String[] str) {
/* 28 */     return str[this.col[1]].split(":")[1].split("-")[0];
/*    */   }
/*    */   
/* 31 */   public String getEnd(String[] str) { return str[this.col[1]].split(":")[1].split("-")[1]; }
/*    */   
/*    */   public int getNoProbes(String[] str) {
/* 34 */     return -1;
/*    */   }
/*    */   
/* 37 */   public String getProbeId(String[] str) { return str[1]; }
/*    */   
/*    */ 
/*    */   public boolean exclude(String[] str)
/*    */   {
/* 42 */     return false;
/*    */   }
/*    */   
/*    */   public double getNoCopy(String[] str) {
/* 46 */     return 0.0D;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/AgilentProbeReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */