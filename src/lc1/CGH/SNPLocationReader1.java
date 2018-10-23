/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ 
/*    */ public class SNPLocationReader1 extends Locreader
/*    */ {
/*    */   public SNPLocationReader1(File dir, String chromosome, Location region) throws Exception
/*    */   {
/* 10 */     super(Long.MAX_VALUE, "");
/*    */     
/* 12 */     BufferedReader br = AberationFinder.getBufferedReader(dir, "snp.txt");
/* 13 */     br.readLine();
/*    */     
/*    */     String st;
/*    */     
/* 17 */     while ((st = br.readLine()) != null) { String st;
/* 18 */       if (st.startsWith("|")) st = br.readLine();
/* 19 */       String[] str = st.trim().split("\\s+");
/*    */       
/* 21 */       long min = Integer.parseInt(str[2]);
/*    */       
/*    */ 
/*    */ 
/*    */ 
/* 26 */       Location location = new Location(chromosome, min, min);
/*    */       
/* 28 */       if ((region == null) || (region.overlaps(location) >= 0.0D)) {
/* 29 */         add(location);
/*    */       }
/*    */     }
/* 32 */     br.close();
/* 33 */     sort();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/SNPLocationReader1.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */