/*    */ package lc1.util;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class Converter
/*    */ {
/*    */   final Double[][][] store;
/*    */   final String outpString;
/*    */   final String headerLine;
/*    */   
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 17 */       Converter conv = new Converter(new File("lhoods.chr22.merged.txt"));
/* 18 */       conv.write();
/*    */     } catch (Exception exc) {
/* 20 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */ 
/*    */ 
/* 26 */   final int noIndiv = 62;
/* 27 */   final int noSnps = 759;
/* 28 */   final String[] names = { "A", "B", "AA", "AB", "BB" };
/*    */   
/* 30 */   public Converter(File f) throws Exception { BufferedReader br = new BufferedReader(new java.io.FileReader(f));
/* 31 */     String st = "";
/* 32 */     StringBuffer sb = new StringBuffer();
/* 33 */     StringBuffer header = new StringBuffer();
/* 34 */     this.store = new Double[this.names.length][62]['˷'];
/* 35 */     for (int j = 0; j < 759; j++) {
/* 36 */       header.append("\"" + j + "\" ");
/* 37 */       if (j < 758) sb.append("%5.3f ");
/*    */     }
/* 39 */     this.headerLine = header.toString();
/* 40 */     sb.append("%5.3f");
/* 41 */     this.outpString = sb.toString();
/* 42 */     for (int i = 0; i < 759; i++) {
/* 43 */       for (int j = 0; j < 62; j++) {
/* 44 */         for (int k = 0; k < this.names.length; k++)
/*    */         {
/* 46 */           this.store[k][j][i] = Double.valueOf(Double.parseDouble(br.readLine()));
/*    */         }
/*    */       }
/*    */     }
/* 50 */     if ((st = br.readLine()) != null) throw new RuntimeException(st);
/* 51 */     br.close();
/*    */   }
/*    */   
/*    */   public void write() throws Exception {
/* 55 */     for (int k = 0; k < this.names.length; k++) {
/* 56 */       File out = new File(this.names[k] + ".lhood");
/* 57 */       PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(out)));
/* 58 */       pw.println(this.headerLine);
/* 59 */       for (int j = 0; j < 62; j++) {
/* 60 */         pw.print(com.braju.format.Format.sprintf("%-6s", new String[] { "\"" + j + "\"" }));
/* 61 */         pw.println(com.braju.format.Format.sprintf(this.outpString, this.store[k][j]));
/*    */       }
/* 63 */       pw.close();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/util/Converter.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */