/*     */ package lc1.CGH;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ProcessFiles
/*     */ {
/*     */   public static void main(String[] args) {}
/*     */   
/*     */   static String[] getChr()
/*     */   {
/*  27 */     List<String> c = new ArrayList();
/*  28 */     for (int i = 22; i >= 1; i--) {
/*  29 */       c.add(i);
/*     */     }
/*  31 */     c.add("X");
/*  32 */     return (String[])c.toArray(new String[0]);
/*     */   }
/*     */   
/*     */   public static void processSNP()
/*     */   {
/*     */     try
/*     */     {
/*  39 */       String abs = "MultiProbeByIndividual.txt";
/*  40 */       String probes = "IC_Custom244K_238459_ProbesTable.txt";
/*  41 */       String outFile = "MultiP";
/*     */       
/*  43 */       File user = new File(System.getProperty("user.dir"));
/*  44 */       String[] chr = getChr();
/*  45 */       File outDir = new File(user, outFile);
/*  46 */       if (!outDir.exists()) outDir.mkdir();
/*  47 */       for (int i = 0; i < chr.length; i++) {
/*  48 */         AbstractAberatiionReader agilent = new MultiProbeAberationReader(Long.MAX_VALUE, "");
/*  49 */         AbstractAberatiionReader p244k = new AgilentProbeReader(Long.MAX_VALUE);
/*  50 */         p244k.initialise(user, chr[i], null, 0, 0, probes, null);
/*  51 */         agilent.initialise(user, chr[i], null, 0, 0, abs, null);
/*  52 */         agilent.sort();
/*  53 */         Locreader sp = extractSingleProbes(agilent, p244k, false, 0, new int[] { 0, Integer.MAX_VALUE });
/*  54 */         print(sp, outDir, "probes_" + chr[i] + ".txt");
/*  55 */         print(agilent, outDir, "regions_" + chr[i] + ".txt");
/*     */       }
/*     */     } catch (Exception exc) {
/*  58 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void processCGH()
/*     */   {
/*     */     try {
/*  65 */       String abs = "MultiProbeByIndividual.txt";
/*  66 */       String probes = "IC_Custom244K_238459_ProbesTable.txt";
/*  67 */       String outFile = "MultiP";
/*  68 */       Set<String> individuals = new HashSet();
/*  69 */       BufferedReader indv = new BufferedReader(new FileReader(new File("indiv_shared.txt")));
/*  70 */       String st1 = "";
/*  71 */       while ((st1 = indv.readLine()) != null) {
/*  72 */         individuals.add(st1.trim());
/*     */       }
/*  74 */       indv.close();
/*  75 */       File user = new File(System.getProperty("user.dir"));
/*  76 */       String[] chr = getChr();
/*  77 */       File outDir = new File(user, outFile);
/*  78 */       if (!outDir.exists()) outDir.mkdir();
/*  79 */       for (int i = 0; i < chr.length; i++) {
/*  80 */         AbstractAberatiionReader agilent = new MultiProbeAberationReader(Long.MAX_VALUE, "");
/*  81 */         AbstractAberatiionReader p244k = new AgilentProbeReader(Long.MAX_VALUE);
/*     */         
/*     */ 
/*  84 */         p244k.initialise(user, chr[i], null, 0, 0, probes, null);
/*  85 */         p244k.sort();
/*  86 */         agilent.initialise(user, chr[i], null, 0, 0, abs, individuals);
/*  87 */         agilent.sort();
/*  88 */         agilent.restrictEnds(p244k);
/*  89 */         agilent.sort();
/*  90 */         Locreader sp = extractSingleProbes(agilent, p244k, false, 0, new int[] { 0, Integer.MAX_VALUE });
/*  91 */         File data = new File("../data/" + chr[i] + " ");
/*  92 */         List<Integer> locs = DataCollection.readPosInfo(new File("../data/" + chr[i] + "_data.txt"), 4, true);
/*  93 */         Locreader locsL = new Locreader(10000000L, "");
/*  94 */         for (int ik = 0; ik < locs.size(); ik++) {
/*  95 */           locsL.add(new Location(chr[i], ((Integer)locs.get(ik)).intValue(), ((Integer)locs.get(ik)).intValue()));
/*     */         }
/*  97 */         agilent.getNoProbes(locsL);
/*     */         
/*  99 */         print(agilent, outDir, "regions_" + chr[i] + ".txt");
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 103 */       exc.printStackTrace();
/*     */     }
/*     */   }
/*     */   
/*     */   private static void print(Locreader agilent, File outDir, String string)
/*     */     throws Exception
/*     */   {
/* 110 */     List<Location> l = new ArrayList();
/* 111 */     for (Iterator<Location> it = agilent.iterator(); it.hasNext();) {
/* 112 */       Location loc = (Location)it.next();
/* 113 */       l.add(loc);
/*     */     }
/*     */     
/* 116 */     Collections.sort(l);
/* 117 */     PrintWriter regions = new PrintWriter(new BufferedWriter(new FileWriter(new File(outDir, string))));
/* 118 */     for (int i = 0; i < l.size(); i++) {
/* 119 */       Location loc = (Location)l.get(i);
/* 120 */       loc.print(regions);
/*     */     }
/* 122 */     regions.close();
/*     */   }
/*     */   
/*     */   public static Locreader extractSingleProbes(Locreader ag1, Locreader agPr, boolean noExclusions, int exact, int[] maxMatchPerFeature)
/*     */   {
/* 127 */     Locreader locr = new Locreader(2147483647L, "");
/*     */     
/* 129 */     if (agPr.number() == 0) {
/* 130 */       return locr;
/*     */     }
/* 132 */     Collection<Location> probesinMultiRegion = new ArrayList();
/* 133 */     Collection<Location> multiRegionWithProbes = new ArrayList();
/* 134 */     Collection<Location> exclude = null;
/*     */     
/* 136 */     if ((noExclusions) && (exclude.size() > 0)) {
/* 137 */       throw new RuntimeException("!! " + exclude);
/*     */     }
/* 139 */     locr.addAll(probesinMultiRegion.iterator());
/* 140 */     return locr;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/CGH/ProcessFiles.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */