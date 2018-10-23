/*     */ package lc1.possel;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import java.util.zip.GZIPInputStream;
/*     */ import lc1.CGH.AberationFinder;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.stats.TrainableNormal;
/*     */ 
/*     */ 
/*     */ public class RunEHH
/*     */ {
/*  33 */   static Logger logger = Logger.getLogger("RunEHH");
/*     */   PrintWriter score;
/*     */   
/*  36 */   static { try { ConsoleHandler handler = new ConsoleHandler();
/*  37 */       FileHandler handlerF = new FileHandler("logres.txt", false);
/*  38 */       logger.addHandler(handlerF);
/*  39 */       Formatter formatter = 
/*  40 */         new Formatter() {
/*     */           public String format(LogRecord record) {
/*  42 */             return record.getMessage() + "\n";
/*     */           }
/*  44 */         };
/*  45 */         handler.setFormatter(formatter);
/*  46 */         handlerF.setFormatter(formatter);
/*     */       } catch (Exception exc) {
/*  48 */         exc.printStackTrace();
/*  49 */         System.exit(0);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*  54 */     final Map<String, Emiss> ancestralMap = new HashMap();
/*     */     
/*     */     final File user;
/*     */     final Map<Double, TrainableNormal> normal;
/*     */     String chrom;
/*     */     final boolean scoring;
/*     */     
/*     */     RunEHH(File user, Map<Double, TrainableNormal> normal, PrintWriter score, boolean scoring, Class clazz)
/*     */       throws Exception
/*     */     {
/*  64 */       this.score = score;
/*  65 */       this.clazz = clazz;
/*  66 */       this.scoring = scoring;
/*     */       
/*  68 */       this.normal = normal;
/*  69 */       this.user = user;
/*     */     }
/*     */     
/*  72 */     public void setChrom(String chrom) throws Exception { this.ancestralMap.clear();
/*  73 */       this.chrom = chrom.split("_")[0];
/*  74 */       File ancestralFile = new File(this.user, "data/" + this.chrom + "_anc.txt");
/*  75 */       System.err.println("ancestral file is " + ancestralFile);
/*     */       
/*  77 */       BufferedReader br = read(ancestralFile);
/*  78 */       String st = "";
/*  79 */       this.ancestralMap.clear();
/*  80 */       while ((st = br.readLine()) != null) {
/*  81 */         String[] str = st.trim().split("\\s+");
/*     */         Emiss em;
/*  83 */         if (str[3].equals("A")) { em = Emiss.A; } else { Emiss em;
/*  84 */           if (str[3].equals("B")) em = Emiss.B; else
/*  85 */             throw new RuntimeException(""); }
/*  86 */         Emiss em; this.ancestralMap.put(str[0], em);
/*     */       }
/*  88 */       br.close();
/*     */     }
/*     */     
/*  91 */     public static BufferedReader read(File f) throws Exception { if (f.exists()) {
/*  92 */         return new BufferedReader(new FileReader(f));
/*     */       }
/*     */       
/*  95 */       File f1 = new File(f.getParentFile(), f.getName() + ".gz");
/*  96 */       if (!f1.exists()) return null;
/*  97 */       return new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(f1))));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Double getHaps(EmissionState maf, int i, String snp_id, List<String> haps, boolean needAncestral)
/*     */     {
/* 107 */       if (maf.getFixedInteger(i) != null) {
/* 108 */         return null;
/*     */       }
/* 110 */       Emiss ancestral = Emiss.a();
/* 111 */       Emiss derived = Emiss.b();
/* 112 */       if (needAncestral) {
/* 113 */         ancestral = (Emiss)this.ancestralMap.get(snp_id);
/* 114 */         if (ancestral == null) return null;
/* 115 */         derived = ancestral == Emiss.a() ? Emiss.b() : Emiss.a();
/*     */       }
/*     */       
/* 118 */       haps.add(derived.toString());
/* 119 */       haps.add(ancestral.toString());
/* 120 */       int derived_index = maf.getEmissionStateSpace().get(derived).intValue();
/* 121 */       int ancestral_index = maf.getEmissionStateSpace().get(ancestral).intValue();
/* 122 */       double[] emiss = maf.getEmiss(i);
/* 123 */       if ((emiss[derived_index] == 0.0D) || (emiss[ancestral_index] == 0.0D)) return null;
/* 124 */       return Double.valueOf(emiss[derived_index]);
/*     */     }
/*     */     
/* 127 */     public Double getCNVIndices(EmissionState maf, int i, List<String> haps, int copyNumber) { if (maf.getFixedInteger(i) != null) return null;
/* 128 */       String cnv_hap = "";
/* 129 */       EmissionStateSpace emStSp = maf.getEmissionStateSpace();
/* 130 */       double[] emiss = maf.getEmiss(i);
/* 131 */       double max_prob = 0.0D;
/* 132 */       for (int k = 0; k < emiss.length; k++) {
/* 133 */         if (emiss[k] > 0.0D)
/* 134 */           if ((emStSp.getCN(k) == copyNumber) && (emiss[k] > max_prob)) {
/* 135 */             max_prob = emiss[k];
/* 136 */             cnv_hap = emStSp.get(k).toString();
/*     */ 
/*     */           }
/* 139 */           else if (emStSp.getCN(k) == 1) { haps.add(emStSp.get(k).toString());
/*     */           }
/*     */       }
/* 142 */       if (cnv_hap.length() == 0) { return null;
/*     */       }
/* 144 */       haps.add(0, cnv_hap);
/* 145 */       return Double.valueOf(max_prob);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 151 */     List<Double> leftAnc = new ArrayList();
/* 152 */     List<Double> leftDerived = new ArrayList();
/* 153 */     List<Double> rightDerived = new ArrayList();
/* 154 */     List<Double> rightAnc = new ArrayList();
/* 155 */     List<Integer> loc = new ArrayList();
/* 156 */     List<String> id = new ArrayList();
/* 157 */     List<String> idR = new ArrayList();
/* 158 */     List<Double> derivedFreq = new ArrayList();
/* 159 */     static double min_p = 0.1D;
/* 160 */     static double max_sc = 1.0D;
/*     */     final Class clazz;
/*     */     
/*     */     public void run(File f, int nextStart, int copyNumber) throws Exception
/*     */     {
/* 165 */       SimpleDataCollection sdt = 
/* 166 */         SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(f, "phased1.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/* 167 */       File snpFile = new File(this.user, "snp.txt");
/* 168 */       sdt.snpid = new ArrayList();
/* 169 */       SimpleDataCollection.readPosInfo(AberationFinder.getBufferedReader(f, "snp.txt"), new int[1], true, new List[] { sdt.snpid }, new Class[] { String.class });
/* 170 */       sdt.calculateMaf(false);
/* 171 */       sdt.split();
/* 172 */       AbstractEHH ehh = 
/* 173 */         (AbstractEHH)this.clazz.getConstructor(new Class[] { sdt.getClass(), String.class }).newInstance(new Object[] { sdt, f.getName() });
/* 174 */       List<String> haps = new ArrayList();
/* 175 */       boolean[] doLR = new boolean[2];
/*     */       
/* 177 */       for (int i = 0; i < sdt.length(); i++)
/*     */       {
/*     */ 
/*     */ 
/*     */         try
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 186 */           int pos = ((Integer)sdt.loc.get(i)).intValue();
/* 187 */           haps.clear();
/* 188 */           Double[][] scores = new Double[2][2];
/* 189 */           List[][] in = new List[2][2];
/* 190 */           boolean doLeft = (this.loc.size() == 0) || (pos > ((Integer)this.loc.get(this.loc.size() - 1)).intValue());
/* 191 */           boolean doRight = pos < nextStart;
/* 192 */           doLR[0] = doLeft;doLR[1] = doRight;
/* 193 */           Double maf = 
/* 194 */             copyNumber == 1 ? 
/* 195 */             getHaps(sdt.maf, i, (String)sdt.snpid.get(i), haps, this.clazz.equals(EHH.class)) : 
/* 196 */             getCNVIndices(sdt.maf, i, haps, copyNumber);
/* 197 */           if (maf != null) {
/* 198 */             if (haps.size() > 2) {
/* 199 */               System.err.println("haps are " + haps);
/*     */ 
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/*     */ 
/* 207 */               doLR[0] = true;
/* 208 */               doLR[1] = true;
/* 209 */               throw new RuntimeException("!!");
/*     */             }
/*     */           }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 232 */           if (doLeft) {
/* 233 */             this.leftDerived.add(scores[0][0]);
/* 234 */             this.leftAnc.add(scores[0][1]);
/* 235 */             this.loc.add(Integer.valueOf(pos));
/* 236 */             this.id.add(f.getName().split("\\.")[0]);
/*     */             
/* 238 */             this.derivedFreq.add(maf);
/*     */           }
/* 240 */           if (doRight) {
/* 241 */             if (pos != ((Integer)this.loc.get(this.rightDerived.size())).intValue()) throw new RuntimeException("!!");
/* 242 */             this.rightDerived.add(scores[1][0]);
/* 243 */             this.rightAnc.add(scores[1][1]);
/* 244 */             this.idR.add(f.getName().split("\\.")[0]);
/*     */           }
/* 246 */           if ((!doRight) || (!doLeft) || 
/* 247 */             (this.rightDerived.size() == this.leftDerived.size())) continue; throw new RuntimeException("!!");
/*     */         }
/*     */         catch (Exception exc) {
/* 250 */           System.err.println("problem at " + i + " " + f);
/* 251 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */     public void run(File f, int start, String hapString, String anc)
/*     */       throws Exception
/*     */     {
/* 259 */       SimpleDataCollection sdt = 
/* 260 */         SimpleDataCollection.readFastPhaseOutput(AberationFinder.getBufferedReader(f, "phased1.txt"), Emiss.class, Emiss.getEmissionStateSpace(1));
/* 261 */       File snpFile = new File(this.user, "snp.txt");
/* 262 */       sdt.snpid = new ArrayList();
/* 263 */       SimpleDataCollection.readPosInfo(AberationFinder.getBufferedReader(f, "snp.txt"), new int[1], true, new List[] { sdt.snpid }, new Class[] { String.class });
/* 264 */       sdt.calculateMaf(false);
/* 265 */       sdt.split();
/* 266 */       AbstractEHH ehh = (AbstractEHH)this.clazz.getConstructor(new Class[] { sdt.getClass(), String.class }).newInstance(new Object[] { sdt, f.getName() });
/*     */       
/* 268 */       List<String> haps = new ArrayList();
/* 269 */       boolean[] doLR = { true, true };
/* 270 */       int i1 = sdt.loc.indexOf(Integer.valueOf(start));
/* 271 */       int i2 = i1 + hapString.length() - 1;
/*     */       
/* 273 */       Map<String, List<PIGData>> m = sdt.getAllHaplotypes(i1, i2);
/* 274 */       List l = (List)m.remove(hapString);
/* 275 */       if (l == null) { throw new RuntimeException("!!");
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 283 */       Double maf = Double.valueOf(l.size() / (((List)m.get(anc)).size() + l.size()));
/* 284 */       haps.clear();
/* 285 */       Double[][] scores = new Double[2][2];
/* 286 */       List[][] in = new List[2][2];
/* 287 */       if (haps.size() <= 2)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 295 */         doLR[0] = true;
/* 296 */         doLR[1] = true;
/* 297 */         throw new RuntimeException("!!");
/*     */       }
/*     */     }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public double scoreNorm(double ratio, double maf1)
/*     */     {
/* 323 */       double maf = round(maf1);
/*     */       
/* 325 */       if ((Double.isNaN(ratio)) || (ratio == Double.POSITIVE_INFINITY) || (ratio == Double.NEGATIVE_INFINITY) || 
/*     */       
/* 327 */         (Double.isInfinite(ratio))) { throw new RuntimeException(" !! " + ratio);
/*     */       }
/* 329 */       TrainableNormal norm = (TrainableNormal)this.normal.get(Double.valueOf(maf));
/*     */       
/* 331 */       double sc = (ratio - norm.getMean()) / norm.getStdDev();
/*     */       
/* 333 */       return sc;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public void calcScores()
/*     */     {
/* 342 */       if (this.rightDerived.size() != this.leftDerived.size()) throw new RuntimeException("!! " + this.rightDerived.size() + " " + this.leftDerived.size());
/* 343 */       Object[] toPrint = new Object[8];
/* 344 */       String fs = "%-5s\t%15s\t%15s\t%10i\t%5.3g\t%5.3g\t%5.3g\t%5.3g";
/* 345 */       toPrint[0] = this.chrom;
/* 346 */       for (int i = 0; i < this.loc.size(); i++) {
/*     */         try {
/* 348 */           toPrint[1] = this.id.get(i);
/* 349 */           toPrint[2] = this.idR.get(i);
/* 350 */           if (toPrint[2].equals(toPrint[1])) toPrint[2] = "-";
/* 351 */           toPrint[3] = this.loc.get(i);
/* 352 */           Double derL = (Double)this.leftDerived.get(i);
/* 353 */           Double derR = (Double)this.rightDerived.get(i);
/* 354 */           Double ancL = (Double)this.leftAnc.get(i);
/* 355 */           Double ancR = (Double)this.rightAnc.get(i);
/* 356 */           if ((derL != null) && (derR != null) && (ancL != null) && (ancR != null)) {
/* 357 */             System.err.print(i + " ");
/* 358 */             double sc = this.clazz == EHH.class ? Math.log((derL.doubleValue() + derR.doubleValue()) / (ancL.doubleValue() + ancR.doubleValue())) : Math.max(derL.doubleValue(), derR.doubleValue());
/* 359 */             addCount(sc, ((Double)this.derivedFreq.get(i)).doubleValue());
/* 360 */             double pval = pvalue(sc, ((Double)this.derivedFreq.get(i)).doubleValue());
/* 361 */             toPrint[4] = this.derivedFreq.get(i);
/* 362 */             toPrint[5] = Double.valueOf(sc);
/* 363 */             toPrint[6] = Double.valueOf(Math.min(pval, 1.0D - pval));
/* 364 */             toPrint[7] = Double.valueOf(scoreNorm(sc, ((Double)this.derivedFreq.get(i)).doubleValue()));
/* 365 */             this.score.println(Format.sprintf(fs, toPrint));
/*     */           }
/*     */           
/*     */         }
/*     */         catch (Exception exc)
/*     */         {
/* 371 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 377 */       System.err.println();
/*     */       
/* 379 */       this.score.flush();
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 385 */     public double round(double maf1) { return Math.round(maf1 * 20.0D) / 20.0D; }
/*     */     
/*     */     public void addCount(double ratio, double maf1) {
/* 388 */       double maf = round(maf1);
/* 389 */       if ((!Double.isNaN(ratio)) && (!Double.isInfinite(ratio)) && 
/* 390 */         (ratio != Double.NEGATIVE_INFINITY) && 
/* 391 */         (ratio != Double.POSITIVE_INFINITY)) {
/* 392 */         TrainableNormal norm = (TrainableNormal)this.normal.get(Double.valueOf(maf));
/* 393 */         if (norm == null)
/*     */         {
/* 395 */           this.normal.put(Double.valueOf(maf), norm = new TrainableNormal(0.0D, 1.0D, 100.0D, 1.0D));
/*     */         }
/* 397 */         norm.addCount(ratio, 1.0D);
/*     */       }
/*     */       else {
/* 400 */         throw new RuntimeException("!! " + ratio);
/*     */       }
/*     */     }
/*     */     
/*     */     public double pvalue(double ratio, double maf1) {
/* 405 */       double maf = round(maf1);
/* 406 */       if ((Double.isNaN(ratio)) || 
/* 407 */         (ratio == Double.POSITIVE_INFINITY) || 
/* 408 */         (ratio == Double.NEGATIVE_INFINITY) || 
/* 409 */         (Double.isInfinite(ratio))) throw new RuntimeException("   !!    " + ratio);
/* 410 */       TrainableNormal norm = (TrainableNormal)this.normal.get(Double.valueOf(maf));
/* 411 */       double sc = 1.0D - norm.cumulative(ratio);
/* 412 */       if (sc < 1.0E-4D) {
/* 413 */         System.err.println(maf1 + "->" + maf + " low pvalue for " + ratio + " " + norm + " pval : " + sc);
/*     */       }
/* 415 */       return sc;
/*     */     }
/*     */   }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/possel/RunEHH.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */