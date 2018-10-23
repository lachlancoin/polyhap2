/*    */ package lc1.association.io;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class CharSNP
/*    */   extends SNP
/*    */ {
/*    */   char[] snp;
/*    */   
/*    */   public String toString()
/*    */   {
/* 15 */     return this.name;
/*    */   }
/*    */   
/*    */   public void print(PrintWriter pw)
/*    */   {
/* 20 */     int i = 0;
/* 21 */     for (i = 0; i < this.snp.length - 1; i++) {
/* 22 */       pw.print(this.snp[i]);
/*    */     }
/* 24 */     pw.println(this.snp[i]);
/*    */   }
/*    */   
/*    */   public void print(PrintWriter pw, int i) {
/* 28 */     pw.print(this.snp[i]);
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */   CharSNP(List<String> split)
/*    */   {
/* 35 */     this.name = ((String)split.get(0));
/* 36 */     this.snp = new char[split.size() - 1];
/* 37 */     for (int j = 1; j < split.size(); j++) {
/* 38 */       String geno = (String)split.get(j);
/*    */       try
/*    */       {
/* 41 */         geno = geno.replace('*', 'N');
/* 42 */         this.snp[(j - 1)] = Converter.converter.getCode(geno);
/*    */       } catch (Exception exc) {
/* 44 */         System.err.println("problem with " + geno);
/*    */       }
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/association/io/CharSNP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */