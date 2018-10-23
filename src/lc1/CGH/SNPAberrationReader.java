/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.File;
/*    */ import java.io.PrintStream;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SNPAberrationReader
/*    */   extends Locreader
/*    */ {
/* 22 */   static int gap = 0;
/*    */   
/*    */ 
/*    */ 
/*    */   public SNPAberrationReader(File dir, String chromosome, int noOfSnps, Location startEnd, String file, double thresh, Set<String> indiv, String name)
/*    */     throws Exception
/*    */   {
/* 29 */     super(Long.MAX_VALUE, name);
/* 30 */     BufferedReader br = AberationFinder.getBufferedReader(dir, file);
/* 31 */     String st = br.readLine();
/* 32 */     if (st.startsWith("from")) {
/* 33 */       String[] str = st.split("\\s+");
/*    */       
/*    */ 
/* 36 */       st = br.readLine();
/*    */     }
/* 38 */     List<Location> list = new ArrayList();
/* 39 */     String chr = "Chr";
/* 40 */     String[] names = st.split("\\s+");
/* 41 */     while ((st = br.readLine()) != null)
/* 42 */       if ((st.startsWith("from")) || (st.startsWith("File"))) {
/* 43 */         System.err.println("removed " + st);
/*    */       }
/*    */       else {
/* 46 */         String[] str = st.split("\\s+");
/* 47 */         if (str.length == 1) str = st.split(",");
/* 48 */         String chrom = str[2];
/* 49 */         if (chrom.startsWith("pbs")) { chrom = chromosome;
/*    */         }
/*    */         
/*    */ 
/*    */ 
/* 54 */         if ((chromosome == null) || (chrom.equals(chromosome)) || (chrom.equals(".")))
/*    */         {
/*    */ 
/*    */ 
/*    */ 
/* 59 */           long min = Long.parseLong(str[3]);
/* 60 */           long max = Long.parseLong(str[4]);
/* 61 */           int nocop = 
/* 62 */             str[7].startsWith("CNV") ? 2 : str[7].startsWith("DEL") ? 0 : 
/* 63 */             Integer.parseInt(str[7]);
/* 64 */           double cert = str.length > 8 ? Double.parseDouble(str[8]) : 1.0D;
/* 65 */           int noSnps = Integer.parseInt(str[5]);
/*    */           
/* 67 */           if (noSnps >= noOfSnps) {
/* 68 */             if ((noOfSnps > 1) && (max == min)) throw new RuntimeException("!!");
/* 69 */             Location location = max > min ? new Location(chrom, min - gap, max + gap) : new Location(chrom, max - gap, min + gap);
/*    */             
/* 71 */             int ind = str[1].indexOf('_');
/* 72 */             String nme = ind >= 0 ? str[1].substring(0, ind) : str[1];
/*    */             
/* 74 */             location.setNoCop(nocop);
/* 75 */             location.setName(nme);
/* 76 */             location.setNoSnps(noSnps);
/* 77 */             if (noSnps < 1) throw new RuntimeException("!!");
/* 78 */             if ((indiv == null) || (indiv.contains(nme)))
/*    */             {
/* 80 */               if ((cert > thresh) && ((startEnd == null) || (startEnd.overlaps(location) >= 0.0D))) { add(location);
/*    */               } else
/* 82 */                 System.err.println("removed " + st); }
/*    */           }
/*    */         } }
/* 85 */     sort();
/* 86 */     br.close();
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/SNPAberrationReader.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */