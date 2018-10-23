/*     */ package lc1.possel;
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
/*     */ import lc1.CGH.AberationFinder;
/*     */ import lc1.CGH.AbstractAberatiionReader;
/*     */ import lc1.CGH.Location;
/*     */ import lc1.CGH.Locreader;
/*     */ import lc1.CGH.MultiProbeAberationReader;
/*     */ import lc1.CGH.SNPLocationReader1;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ 
/*     */ public class ExtractHalpotypeStructure implements Runnable
/*     */ {
/*  24 */   static int bef = 50;
/*  25 */   static int aft = 50;
/*     */   final File[] files;
/*     */   final int chrom;
/*     */   final AbstractAberatiionReader abR;
/*     */   final File out;
/*     */   
/*     */   public void run()
/*     */   {
/*     */     try {
/*  34 */       for (Iterator<Location> it = this.abR.iterator(); it.hasNext();) {
/*  35 */         Location region = (Location)it.next();
/*     */         
/*  37 */         for (int i = 0; i < this.files.length; i++)
/*     */         {
/*     */ 
/*  40 */           if (this.chrom == Integer.parseInt(region.chr)) {
/*  41 */             Locreader snpLocations = new SNPLocationReader1(this.files[i], this.chrom, null);
/*  42 */             System.err.println(this.files[i] + " " + snpLocations.getFirst().min + " " + snpLocations.getLast().max + " " + region);
/*  43 */             if ((snpLocations.getFirst().min < region.min) && (snpLocations.getLast().max > region.max)) {
/*  44 */               Logger.global.info("file is " + this.files[i]);
/*  45 */               List<Integer> locs = snpLocations.getLocs();
/*  46 */               int posMin = getPos(locs, region.min).intValue() - 1;
/*  47 */               int posMax = getPos(locs, region.max).intValue();
/*  48 */               posMin = Math.max(0, posMin - bef);
/*  49 */               posMax = Math.min(locs.size(), posMax + aft);
/*  50 */               SimpleDataCollection sdt = 
/*  51 */                 SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(this.files[i], "phased.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/*  52 */               sdt.loc = locs.subList(posMin, posMax);
/*  53 */               for (Iterator<PIGData> it1 = sdt.data.values().iterator(); it1.hasNext();) {
/*  54 */                 PIGData nxt = (PIGData)it1.next();
/*  55 */                 nxt.restrictSites(posMin, posMax);
/*  56 */                 if (nxt.length() != sdt.loc.size()) { throw new RuntimeException("!!");
/*     */                 }
/*     */               }
/*  59 */               PrintWriter pw = new PrintWriter(new java.io.BufferedWriter(new FileWriter(
/*  60 */                 new File(this.out, "reg_" + region.toStringPrint() + ".txt"))));
/*  61 */               for (int i1 = 0; i1 < sdt.loc.size(); i1++) {
/*  62 */                 if (((Integer)sdt.loc.get(i1)).intValue() < region.min) { pw.print("-");
/*  63 */                 } else if (((Integer)sdt.loc.get(i1)).intValue() > region.max) pw.print("+"); else
/*  64 */                   pw.print("=");
/*     */               }
/*  66 */               pw.println();
/*  67 */               sdt.writeLocation(pw, null);
/*  68 */               sdt.writeFastphase(pw, false);
/*  69 */               sdt.writeLocation(pw, null);
/*  70 */               pw.close();
/*  71 */               System.err.println("matched region " + region + " at " + this.files[i]);
/*  72 */               break;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exc)
/*     */     {
/*  80 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public ExtractHalpotypeStructure(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosome, Location reg) throws Exception
/*     */   {
/*  86 */     this.abR = new MultiProbeAberationReader(2147483647L, "") {
/*     */       public boolean exclude(String[] str) {
/*  88 */         return false;
/*     */       }
/*  90 */     };
/*  91 */     this.abR.initialise(dir, null, null, 0, 0, "valsum1507_1.txt", null);
/*  92 */     this.out = new File(dir.getParentFile(), "reg1");
/*  93 */     if (!this.out.exists()) this.out.mkdir();
/*  94 */     this.chrom = Integer.parseInt(chromosome);
/*  95 */     this.files = user;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   private static Integer getPos(List<Integer> locs, long min)
/*     */   {
/* 103 */     for (int i = 0; i < locs.size(); i++) {
/* 104 */       if (((Integer)locs.get(i)).intValue() > min) return Integer.valueOf(i);
/*     */     }
/* 106 */     return Integer.valueOf(0);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/ExtractHalpotypeStructure.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */