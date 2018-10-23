/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.collection.IlluminaRDataCollection;
/*     */ 
/*     */ public class AbFinder implements Runnable
/*     */ {
/*  19 */   boolean ran = true;
/*  20 */   AbstractAberatiionReader agilent = new MultiProbeAberationReader(Long.MAX_VALUE, "agilent");
/*  21 */   Locreader snp = new Locreader(Long.MAX_VALUE, "snp");
/*  22 */   Locreader french = new Locreader(Long.MAX_VALUE, "french");
/*  23 */   Locreader cgh = new Locreader(Long.MAX_VALUE, "cgh");
/*  24 */   int ind = 1;
/*     */   
/*  26 */   SortedSet<Integer> snps = new TreeSet();
/*  27 */   SortedSet<Integer> probes = new TreeSet();
/*  28 */   Set<String> individuals = new java.util.HashSet();
/*     */   
/*     */   PrintWriter out;
/*     */   
/*     */   String chr;
/*     */   
/*     */   File logDir;
/*     */   
/*     */   public static List<Integer> readPos(File user, String file)
/*     */     throws Exception
/*     */   {
/*  39 */     BufferedReader br = AberationFinder.getBufferedReader(user, file);
/*  40 */     List<Integer> res = new ArrayList();
/*  41 */     DataCollection.readPosInfo(br, new int[] { 1 }, true, new List[] { res }, new Class[] { Integer.class });
/*  42 */     br.close();
/*  43 */     return res;
/*     */   }
/*     */   
/*     */   public AbFinder(File dir, File[] user, String[] args, PrintWriter out, Map<String, Number> sum, String chromosme, Location reg) throws Exception {
/*  47 */     System.err.println("chromosome is " + chromosme);
/*  48 */     File data = new File(dir.getParentFile(), "../data");
/*  49 */     this.chr = "1";
/*  50 */     this.logDir = new File("logDir");
/*  51 */     if (!this.logDir.exists()) {
/*  52 */       this.logDir.mkdir();
/*     */     }
/*  54 */     BufferedReader indv = new BufferedReader(new FileReader(new File(dir, "indiv_shared.txt")));
/*  55 */     String st1 = "";
/*  56 */     while ((st1 = indv.readLine()) != null) {
/*  57 */       this.individuals.add(st1.trim());
/*     */     }
/*  59 */     indv.close();
/*  60 */     for (int i = 0; i < user.length; i++) {
/*  61 */       this.snps.addAll(readPos(user[i], "phased1.txt_" + this.ind));
/*  62 */       this.probes.addAll(readPos(user[i], "phased1.txt_3"));
/*  63 */       SNPAberrationReader snps = 
/*  64 */         new SNPAberrationReader(user[i], chromosme, 5, 
/*  65 */         null, "cnv.txt_" + this.ind, AberationFinder.threshold, AberationFinder.limit() ? this.individuals : null, "snps");
/*  66 */       SNPAberrationReader cgh = 
/*  67 */         new SNPAberrationReader(user[i], chromosme, AberationFinder.noOfSnps, 
/*  68 */         null, "cnv.txt_3", AberationFinder.threshold, AberationFinder.limit() ? this.individuals : null, "cgh");
/*  69 */       this.snp.addAll(snps);
/*  70 */       this.cgh.addAll(cgh);
/*     */     }
/*  72 */     this.snp.sort();
/*  73 */     this.snp.merge(0.0D);
/*  74 */     this.cgh.sort();
/*  75 */     this.cgh.merge(0.0D);
/*  76 */     this.snp.probes = this.snps;
/*  77 */     this.cgh.probes = this.probes;
/*  78 */     this.snp.setChr(this.chr);
/*  79 */     this.cgh.setChr(this.chr);
/*  80 */     File cghFile = new File(dir.getParentFile(), "data/" + this.chr + "_cghdata.txt");
/*  81 */     File snpF = new File(dir.getParentFile(), "data/" + this.chr + "_data.txt");
/*  82 */     DataCollection cghdat = null;
/*  83 */     IlluminaRDataCollection illdat = new IlluminaRDataCollection(snpF, (short)0, 2);
/*  84 */     this.agilent.probes = this.probes;
/*  85 */     this.cgh.dc = null;
/*  86 */     this.snp.dc = illdat;
/*     */     
/*  88 */     Location region = new Location(this.chr, Math.min(((Integer)this.probes.first()).intValue(), ((Integer)this.snps.first()).intValue()), Math.max(((Integer)this.probes.last()).intValue(), ((Integer)this.snps.last()).intValue()));
/*  89 */     System.err.println("region is " + region);
/*     */     
/*     */ 
/*  92 */     this.out = out;
/*     */     
/*  94 */     this.agilent.initialise(dir, this.chr, region, 0, 0, "MultiProbeByIndividual.txt", AberationFinder.limit() ? this.individuals : null);
/*  95 */     this.agilent.restrict();
/*  96 */     this.agilent.sort();
/*     */     
/*     */ 
/*  99 */     this.french = new SNPAberrationReader(dir, this.chr, AberationFinder.noOfSnps, null, "FrenchSamples.txt", AberationFinder.threshold, AberationFinder.limit() ? this.individuals : null, "french");
/* 100 */     this.french.probes = this.snps;
/*     */     
/* 102 */     this.snp.mergeNames();
/* 103 */     this.cgh.mergeNames();
/*     */     
/* 105 */     this.agilent.mergeNames();
/* 106 */     this.french.mergeNames();
/*     */     
/*     */ 
/* 109 */     this.snp.sort();
/* 110 */     this.cgh.sort();
/*     */     
/* 112 */     double mergeThresh = 0.0D;
/* 113 */     this.snp.merge(mergeThresh);
/* 114 */     this.cgh.merge(mergeThresh);
/* 115 */     this.agilent.merge(mergeThresh);
/* 116 */     this.snp.merge(mergeThresh);
/* 117 */     this.snp.removeWithObsLessThan(2);
/*     */     
/* 119 */     this.french.restrict();
/* 120 */     this.snp.restrict();
/* 121 */     this.agilent.restrict();
/* 122 */     this.cgh.restrict();
/*     */   }
/*     */   
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 130 */       LocCompare localLocCompare1 = new LocCompare(this.french, this.cgh, this.logDir);
/*     */       
/* 132 */       LocCompare lc = new LocCompare(this.snp, this.cgh, this.logDir);
/*     */       
/*     */ 
/* 135 */       LocCompare localLocCompare2 = new LocCompare(this.agilent, this.cgh, this.logDir);
/*     */       
/*     */ 
/* 138 */       localLocCompare2 = new LocCompare(this.french, this.snp, this.logDir);
/*     */       
/*     */ 
/* 141 */       localLocCompare2 = new LocCompare(this.french, this.agilent, this.logDir);
/*     */       
/*     */ 
/* 144 */       localLocCompare2 = new LocCompare(this.snp, this.agilent, this.logDir);
/*     */     }
/*     */     catch (Exception exc) {
/* 147 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/AbFinder.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */