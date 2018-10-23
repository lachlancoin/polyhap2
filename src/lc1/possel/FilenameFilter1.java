/*    */ package lc1.possel;
/*    */ 
/*    */ import java.io.File;
/*    */ 
/*    */ public class FilenameFilter1 implements java.io.FilenameFilter {
/*    */   String pre;
/*    */   
/*  8 */   FilenameFilter1(String pre) { this.pre = pre; }
/*    */   
/*    */   public boolean accept(File arg0, String arg1)
/*    */   {
/* 12 */     return arg1.startsWith(this.pre);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/FilenameFilter1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */