/*    */ package lc1.CGH;
/*    */ 
/*    */ import java.io.BufferedWriter;
/*    */ import java.io.File;
/*    */ import java.io.FileWriter;
/*    */ import java.io.PrintStream;
/*    */ import java.io.PrintWriter;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.logging.Logger;
/*    */ import lc1.dp.data.collection.SimpleDataCollection;
/*    */ import lc1.dp.data.representation.Emiss;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExtendedHaplotypeFinder
/*    */   implements Runnable
/*    */ {
/*    */   final File[] files;
/*    */   final int chrom;
/*    */   final File out;
/* 27 */   int thresh = 3;
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   public void run()
/*    */   {
/*    */     try
/*    */     {
/* 36 */       for (int i = 0; i < this.files.length; i++) {
/* 37 */         PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(this.out, this.chrom + "_" + i))));
/*    */         
/* 39 */         Locreader snpLocations = new SNPLocationReader1(this.files[i], this.chrom, null);
/* 40 */         Logger.global.info("file is " + this.files[i]);
/* 41 */         List<Integer> locs = snpLocations.getLocs();
/* 42 */         SimpleDataCollection sdt = 
/* 43 */           SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(this.files[i], "phased.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/* 44 */         sdt.loc = locs;
/* 45 */         EHHFinder ehh = new EHHFinder(sdt);
/* 46 */         List<Aberation> obj = sdt.getDeletedPositions();
/* 47 */         for (int i1 = 0; i1 < obj.size(); i1++) {
/* 48 */           Aberation ab = (Aberation)obj.get(i1);
/* 49 */           if (ab.end - ab.start >= 3) {
/* 50 */             System.err.println("aBERATION " + ab);
/* 51 */             ehh.extend(ab, pw);
/*    */             
/* 53 */             pw.println("##############################################");
/* 54 */             pw.flush();
/*    */           }
/*    */         }
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/* 72 */         pw.close();
/*    */       }
/*    */       
/*    */     }
/*    */     catch (Exception exc)
/*    */     {
/* 78 */       exc.printStackTrace();
/*    */     }
/*    */   }
/*    */   
/*    */   public ExtendedHaplotypeFinder(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosome, Location reg)
/*    */     throws Exception
/*    */   {
/* 85 */     this.out = new File(dir.getParentFile(), "interesting regions");
/* 86 */     if (!this.out.exists()) this.out.mkdir();
/* 87 */     this.chrom = Integer.parseInt(chromosome);
/* 88 */     this.files = user;
/*    */   }
/*    */   
/*    */ 
/*    */ 
/*    */ 
/*    */   private static Integer getPos(List<Integer> locs, long min)
/*    */   {
/* 96 */     for (int i = 0; i < locs.size(); i++) {
/* 97 */       if (((Integer)locs.get(i)).intValue() > min) return Integer.valueOf(i);
/*    */     }
/* 99 */     return Integer.valueOf(0);
/*    */   }
/*    */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/ExtendedHaplotypeFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */