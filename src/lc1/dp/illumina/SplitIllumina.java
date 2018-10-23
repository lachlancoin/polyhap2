/*    */ package lc1.dp.illumina;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.PrintWriter;
/*    */ 
/*    */ public class SplitIllumina
/*    */ {
/*    */   public static void main(String[] args)
/*    */   {
/*    */     try
/*    */     {
/* 13 */       int ind = 3;
/* 14 */       BufferedReader br = new BufferedReader(new java.io.FileReader(new File("French_raw.txt")));
/* 15 */       String header = br.readLine();
/* 16 */       String st = br.readLine();
/* 17 */       while (st != null) {
/* 18 */         String chrom = st.split("\\s+")[ind];
/* 19 */         File dir = new File(chrom);
/* 20 */         dir.mkdir();
/* 21 */         File f = new File(dir, "data.txt");
/* 22 */         PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new java.io.FileWriter(f)));
/* 23 */         pw.println(header);
/* 24 */         while ((st != null) && (chrom.equals(st.split("\\s+")[ind]))) {
/* 25 */           pw.println(st);
/* 26 */           st = br.readLine();
/*    */         }
/* 28 */         pw.close();
/*    */       }
/*    */     }
/*    */     catch (Exception exc) {
/* 32 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/illumina/SplitIllumina.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */