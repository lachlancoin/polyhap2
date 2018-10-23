/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.util.Set;
/*    */ import java.util.logging.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExcelAberationReader
/*    */   extends AbstractAberatiionReader
/*    */ {
/*    */   public ExcelAberationReader(long lengthLim)
/*    */   {
/* 17 */     super(lengthLim, "");
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/* 23 */   static String[] cols = { "aaaa", "Chr", "Start", "Stop", "Mean", "NoOfProbes", "Amplification", "Deletion" };
/*    */   String name;
/*    */   
/*    */   public String getName(String[] str)
/*    */   {
/* 28 */     return this.name;
/*    */   }
/*    */   
/* 31 */   public String getChr(String[] str) { return str[this.col[1]].substring(3); }
/*    */   
/*    */ 
/*    */   public double getNoCopy(String[] str)
/*    */   {
/* 36 */     double ampl = Double.parseDouble(str[this.col[6]]);
/* 37 */     double del = Double.parseDouble(str[this.col[7]]);
/* 38 */     return Math.abs(ampl) < 0.001D ? del : ampl;
/*    */   }
/*    */   
/*    */   public void initialise(BufferedReader dir, String chromosome, Location region, int st1, int st2, String name, Set<String> indiv) throws Exception {
/* 42 */     if (name.indexOf('/') >= 0) {
/* 43 */       this.name = name.substring(name.lastIndexOf('/') + 1);
/*    */     }
/* 45 */     this.name = this.name.split("_")[0];
/* 46 */     if ((indiv != null) && (!indiv.contains(this.name))) {
/* 47 */       Logger.global.info("excluding " + this.name);
/* 48 */       return;
/*    */     }
/* 50 */     if (this.name.length() < 4) throw new RuntimeException(" " + name);
/* 51 */     super.initialise(dir, chromosome, region, st1, st2, this.name, indiv);
/*    */   }
/*    */   
/*    */   public String getStart(String[] str)
/*    */   {
/* 56 */     return str[this.col[2]];
/*    */   }
/*    */   
/* 59 */   public String getEnd(String[] str) { return str[this.col[3]]; }
/*    */   
/*    */   public int getNoProbes(String[] str) {
/* 62 */     return Integer.parseInt(str[this.col[5]]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   public String[] getCols()
/*    */   {
/* 69 */     return cols;
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/ExcelAberationReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */