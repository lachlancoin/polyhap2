/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ 
/*     */ public class ExtractHalpotypeStructure implements Runnable
/*     */ {
/*  18 */   static int bef = 50;
/*  19 */   static int aft = 50;
/*     */   final File[] files;
/*     */   final int chrom;
/*     */   final AbstractAberatiionReader abR;
/*     */   final File out;
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try {
/*  28 */       for (Iterator<Location> it = this.abR.iterator(); it.hasNext();) {
/*  29 */         Location region = (Location)it.next();
/*     */         
/*  31 */         for (int i = 0; i < this.files.length; i++)
/*     */         {
/*     */ 
/*  34 */           if (this.chrom == Integer.parseInt(region.chr)) {
/*  35 */             Locreader snpLocations = new SNPLocationReader1(this.files[i], this.chrom, null);
/*  36 */             System.err.println(this.files[i] + " " + snpLocations.getFirst().min + " " + snpLocations.getLast().max + " " + region);
/*  37 */             if ((snpLocations.getFirst().min < region.min) && (snpLocations.getLast().max > region.max)) {
/*  38 */               Logger.global.info("file is " + this.files[i]);
/*  39 */               List<Integer> locs = snpLocations.getLocs();
/*  40 */               int posMin = getPos(locs, region.min).intValue() - 1;
/*  41 */               int posMax = getPos(locs, region.max).intValue();
/*  42 */               posMin = Math.max(0, posMin - bef);
/*  43 */               posMax = Math.min(locs.size(), posMax + aft);
/*  44 */               SimpleDataCollection sdt = 
/*  45 */                 SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(this.files[i], "phased.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/*  46 */               sdt.loc = locs.subList(posMin, posMax);
/*  47 */               for (Iterator<PIGData> it1 = sdt.data.values().iterator(); it1.hasNext();) {
/*  48 */                 PIGData nxt = (PIGData)it1.next();
/*  49 */                 nxt.restrictSites(posMin, posMax);
/*  50 */                 if (nxt.length() != sdt.loc.size()) { throw new RuntimeException("!!");
/*     */                 }
/*     */               }
/*  53 */               PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new FileWriter(
/*  54 */                 new File(this.out, "reg_" + region.toStringPrint() + ".txt"))));
/*  55 */               for (int i1 = 0; i1 < sdt.loc.size(); i1++) {
/*  56 */                 if (((Integer)sdt.loc.get(i1)).intValue() < region.min) { pw.print("-");
/*  57 */                 } else if (((Integer)sdt.loc.get(i1)).intValue() > region.max) pw.print("+"); else
/*  58 */                   pw.print("=");
/*     */               }
/*  60 */               pw.println();
/*  61 */               sdt.writeLocation(pw, null);
/*  62 */               sdt.writeFastphase(pw, false);
/*  63 */               sdt.writeLocation(pw, null);
/*  64 */               pw.close();
/*  65 */               System.err.println("matched region " + region + " at " + this.files[i]);
/*  66 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*  74 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public ExtractHalpotypeStructure(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosome, Location reg) throws Exception
/*     */   {
/*  80 */     this.abR = new MultiProbeAberationReader(2147483647L, "") {
/*     */       public boolean exclude(String[] str) {
/*  82 */         return false;
/*     */       }
/*  84 */     };
/*  85 */     this.abR.initialise(dir, null, null, 0, 0, "valsum1507_1.txt", null);
/*  86 */     this.out = new File(dir.getParentFile(), "reg1");
/*  87 */     if (!this.out.exists()) this.out.mkdir();
/*  88 */     this.chrom = Integer.parseInt(chromosome);
/*  89 */     this.files = user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Integer getPos(List<Integer> locs, long min)
/*     */   {
/*  97 */     for (int i = 0; i < locs.size(); i++) {
/*  98 */       if (((Integer)locs.get(i)).intValue() > min) return Integer.valueOf(i);
/*     */     }
/* 100 */     return Integer.valueOf(0);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/ExtractHalpotypeStructure.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */