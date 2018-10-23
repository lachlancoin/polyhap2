/*     */ package lc1.possel;
/*     */ 
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.CGH.Aberation;
/*     */ import lc1.CGH.AberationFinder;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.CGH.Locreader;
/*     */ import lc1.CGH.SNPLocationReader1;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExtendedHaplotypeFinder
/*     */   implements Runnable
/*     */ {
/*     */   final File[] files;
/*     */   final int chrom;
/*     */   final File out;
/*  32 */   int thresh = 3;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/*  41 */       for (int i = 0; i < this.files.length; i++) {
/*  42 */         PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(this.out, this.chrom + "_" + i))));
/*     */         
/*  44 */         Locreader snpLocations = new SNPLocationReader1(this.files[i], this.chrom, null);
/*  45 */         Logger.global.info("file is " + this.files[i]);
/*  46 */         List<Integer> locs = snpLocations.getLocs();
/*  47 */         SimpleDataCollection sdt = 
/*  48 */           SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(this.files[i], "phased.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/*  49 */         sdt.loc = locs;
/*  50 */         sdt.split();
/*  51 */         EHHFinder ehh = new EHHFinder(sdt);
/*  52 */         List<Aberation> obj = sdt.getDeletedPositions();
/*  53 */         for (int i1 = 0; i1 < obj.size(); i1++) {
/*  54 */           Aberation ab = (Aberation)obj.get(i1);
/*  55 */           if (ab.end - ab.start >= 3) {
/*  56 */             System.err.println("aBERATION " + ab);
/*  57 */             ehh.extend(ab, pw);
/*     */             
/*  59 */             pw.println("##############################################");
/*  60 */             pw.flush();
/*     */           }
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  78 */         pw.close();
/*     */       }
/*     */       
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*  84 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public ExtendedHaplotypeFinder(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosome, Location reg)
/*     */     throws Exception
/*     */   {
/*  91 */     this.out = new File(dir.getParentFile(), "interesting regions");
/*  92 */     if (!this.out.exists()) this.out.mkdir();
/*  93 */     this.chrom = Integer.parseInt(chromosome);
/*  94 */     this.files = user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Integer getPos(List<Integer> locs, long min)
/*     */   {
/* 102 */     for (int i = 0; i < locs.size(); i++) {
/* 103 */       if (((Integer)locs.get(i)).intValue() > min) return Integer.valueOf(i);
/*     */     }
/* 105 */     return Integer.valueOf(0);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/ExtendedHaplotypeFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */