/*     */ package lc1.dp.genotype.io;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeSet;
/*     */ import java.util.logging.ConsoleHandler;
/*     */ import java.util.logging.FileHandler;
/*     */ import java.util.logging.Formatter;
/*     */ import java.util.logging.LogRecord;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.BaumWelchTrainer;
/*     */ import lc1.dp.CollapsedHMM;
/*     */ import lc1.dp.CompoundMarkovModel;
/*     */ import lc1.dp.MarkovModel;
/*     */ import lc1.dp.PairMarkovModel;
/*     */ import lc1.dp.genotype.CachedHMM;
/*     */ import lc1.dp.genotype.ExponentialHaplotypeHMM;
/*     */ import lc1.dp.genotype.FreeHaplotypeHMM;
/*     */ import lc1.dp.genotype.HaplotypeEmissionState;
/*     */ import lc1.dp.genotype.HaplotypeHMM;
/*     */ import lc1.dp.genotype.io.scorable.CompoundScorableObject;
/*     */ import lc1.dp.genotype.io.scorable.DataCollection;
/*     */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*     */ import lc1.dp.genotype.io.scorable.StateIndices;
/*     */ 
/*     */ public class Fastphase
/*     */ {
/*  36 */   public static final Logger logger = ;
/*  37 */   static long time = System.currentTimeMillis();
/*     */   final int noSites;
/*     */   
/*     */   static {
/*  41 */     try { ConsoleHandler handler = new ConsoleHandler();
/*  42 */       FileHandler handlerF = new FileHandler("stderr_" + Constants.fast(), false);
/*  43 */       logger.addHandler(handlerF);
/*  44 */       Formatter formatter = 
/*  45 */         new Formatter() {
/*     */           public String format(LogRecord record) {
/*  47 */             return record.getSourceClassName() + ":\n" + record.getMessage() + "\n";
/*     */           }
/*  49 */         };
/*  50 */         handler.setFormatter(formatter);
/*  51 */         handlerF.setFormatter(formatter);
/*     */       } catch (Exception exc) {
/*  53 */         exc.printStackTrace();
/*  54 */         System.exit(0);
/*     */       }
/*     */     }
/*     */     
/*     */     private static void change(Comparable[] comp) {
/*  59 */       Comparable tmp = comp[0];
/*  60 */       comp[0] = comp[1];
/*  61 */       comp[1] = tmp;
/*     */     }
/*     */     
/*     */ 
/*     */     private static Double[] geom(Double[] d)
/*     */     {
/*  67 */       double[] prod = { 1.0D, 0.0D, 1.0D };
/*     */       
/*  69 */       for (int i = 0; i < d.length; i++) {
/*  70 */         prod[0] += Math.log(d[i].doubleValue());
/*  71 */         prod[1] += d[i].doubleValue();
/*  72 */         prod[2] = Math.min(prod[2], d[i].doubleValue());
/*     */       }
/*     */       
/*  75 */       double len = d.length;
/*  76 */       return new Double[] { Double.valueOf(Math.exp(prod[0] / len)), Double.valueOf(prod[1] / len), Double.valueOf(prod[2]) };
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  83 */     static boolean isSame(Comparable[] emiss) { return emiss[0] == emiss[1]; }
/*     */     
/*     */     void modify(HaplotypeHMM hapHMM, double probNull) {
/*  86 */       List<Integer> mafa = this.dataC.maf;
/*  87 */       double probNonNull = (1.0D - probNull) / 2.0D;
/*  88 */       int numFounders = hapHMM.modelLength() - 1;
/*  89 */       boolean even = true;
/*  90 */       for (int i = 0; i < hapHMM.noSnps; i++) {
/*  91 */         ((HaplotypeEmissionState)hapHMM.getState(1)).setTheta(new double[] { probNonNull, probNonNull, probNull }, i);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final DataCollection dataC;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     final int modelLength;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     private double getMax(HaplotypeHMM hapHMM, int no)
/*     */     {
/* 113 */       if ((hapHMM instanceof ExponentialHaplotypeHMM)) return 1.0D;
/* 114 */       Double[][] prob = hapHMM.getHittingProb(hapHMM.noSnps);
/* 115 */       double max = Double.NEGATIVE_INFINITY;
/* 116 */       for (int i = 0; i < no; i++) {
/* 117 */         double max_i = getMax(prob[(prob.length - 1 - i)]);
/* 118 */         if (max_i > max) max = max_i;
/*     */       }
/* 120 */       return max;
/*     */     }
/*     */     
/* 123 */     private boolean converged(HaplotypeHMM hapHMM, double thres, int no) { double max = getMax(hapHMM, no);
/* 124 */       logger.info("MAX is " + max);
/* 125 */       if (max < thres) { return true;
/*     */       }
/*     */       
/* 128 */       return false;
/*     */     }
/*     */     
/*     */     private double getMax(Double[] doubles) {
/* 132 */       double max = Double.NEGATIVE_INFINITY;
/* 133 */       for (int i = 0; i < doubles.length; i++) {
/* 134 */         if (doubles[i].doubleValue() > max) max = doubles[i].doubleValue();
/*     */       }
/* 136 */       return max;
/*     */     }
/*     */     
/*     */     public void trainCases(int numIt1, PairMarkovModel hmm_union, PairMarkovModel hmm_cases, int rep, boolean emiss, boolean trans) throws Exception
/*     */     {
/* 141 */       logger.info("training cases");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     public Iterator<TrainingElement> getTrainingElements(final Iterator<HaplotypeHMM> hmm)
/*     */       throws Exception
/*     */     {
/* 151 */       new Iterator() {
/*     */         public boolean hasNext() {
/* 153 */           return hmm.hasNext();
/*     */         }
/*     */         
/* 156 */         public Fastphase.TrainingElement next() { return new Fastphase.TrainingElement(Fastphase.this, (HaplotypeHMM)hmm.next(), Constants.pseudoIterator(Fastphase.this.noIndiv)); }
/*     */         
/*     */         public void remove() {}
/*     */       };
/*     */     }
/*     */     
/*     */     public void train(Iterator<TrainingElement> hmm) throws Exception
/*     */     {
/* 164 */       while (hmm.hasNext()) {
/* 165 */         TrainingElement train = (TrainingElement)hmm.next();
/* 166 */         logger.info("before simple emissions");
/* 167 */         if (Constants.numItExp() > 0) {
/* 168 */           train.train(Constants.numItExp(), 1.0D, Constants.numItFree() <= 0);
/*     */         }
/* 170 */         if (Constants.numItFree() > 0) {
/* 171 */           train.switchHMM();
/* 172 */           train.train(Constants.numItFree(), 1.0D, true);
/*     */         }
/* 174 */         if (Constants.writeHMM()) writeHMM(train.hmm, this.hmmFile, this.dataC);
/* 175 */         this.monitor.sampleFromHMM(train.hmm);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public void trainIncr(int numIt1, int numIt2, int numFounders, double u)
/*     */       throws Exception
/*     */     {
/* 185 */       int numToAdd = 1;
/*     */       
/* 187 */       for (int i = 0; i < this.modelLength; i++) {
/* 188 */         TrainingElement train = new TrainingElement(i);
/*     */         
/* 190 */         double res = Double.NEGATIVE_INFINITY;
/*     */         for (;;) {
/* 192 */           train.train(numIt1, 1.0D, true);
/*     */           
/*     */ 
/* 195 */           if (train.hapHMM.modelLength() - 1 >= numFounders)
/*     */             break;
/* 197 */           if ((train.hapHMM instanceof FreeHaplotypeHMM)) train.hapHMM.fix();
/* 198 */           train.addState(numToAdd);
/*     */         }
/*     */         
/*     */ 
/*     */ 
/* 203 */         logger.info("mid point");
/*     */         
/*     */ 
/* 206 */         if (train.switchHMM()) { train.train(numIt1, 1.0D, true);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 213 */     static int pseudoCount = 0;
/*     */     private List<PhasedIntegerGenotypeData>[] data;
/*     */     File hmmOutputFile;
/*     */     File hmmFile;
/*     */     File phasedFile;
/*     */     File initialHMMFile;
/*     */     
/*     */     public class TrainingElement {
/*     */       TrainingElement(Iterator<Double> haphmm) {
/* 222 */         if (Constants.modify() >= 0.0D) Fastphase.this.modify(haphmm, Constants.modify());
/* 223 */         setHMM(haphmm);
/* 224 */         this.pseudoIterator = pseudoIt;
/*     */         
/* 226 */         Fastphase.this.hmmFile = new File(Fastphase.this.dir, "results_" + haphmm.getClass().toString() + "_" + (haphmm.modelLength() - 1) + ".txt"); }
/*     */       
/*     */       public HaplotypeHMM hapHMM;
/*     */       public CachedHMM[] hmm;
/* 230 */       TrainingElement(int i) { this.pseudoIterator = null;
/* 231 */         HaplotypeHMM haphmm = 
/*     */         
/* 233 */           Constants.numItExp() == -1 ? 
/* 234 */           new FreeHaplotypeHMM("geno", Constants.numF(), Fastphase.this.noSites, Constants.u_global()) : 
/* 235 */           new ExponentialHaplotypeHMM("geno", Constants.numF(), Fastphase.this.noSites, Constants.u_global());
/*     */         
/* 237 */         if (((haphmm instanceof FreeHaplotypeHMM)) && (haphmm.modelLength() > 4)) Fastphase.this.modify((FreeHaplotypeHMM)haphmm, Constants.modify());
/* 238 */         setHMM(haphmm);
/* 239 */         Fastphase.this.hmmFile = new File("results_" + this.hapHMM.getClass().toString() + "_" + (this.hapHMM.modelLength() - 1) + "_" + i + ".txt");
/*     */       }
/*     */       
/*     */       List<StateIndices>[] data1;
/*     */       final Iterator<Double> pseudoIterator;
/*     */       private boolean addState(int numToAdd)
/*     */         throws Exception
/*     */       {
/* 247 */         Fastphase.logger.info("ADDING STATE");
/*     */         
/*     */ 
/*     */ 
/* 251 */         if ((((this.hapHMM instanceof FreeHaplotypeHMM)) && (this.hapHMM.increaseStates(2.0D, 0.5D, numToAdd))) || (
/* 252 */           ((this.hapHMM instanceof ExponentialHaplotypeHMM)) && (((ExponentialHaplotypeHMM)this.hapHMM).doubleStates(10.0D))))
/*     */         {
/* 254 */           setHMM(this.hapHMM);
/* 255 */           Fastphase.this.hmmFile = new File(Fastphase.this.dir, "results_" + this.hapHMM.getClass().toString() + "_" + (this.hapHMM.modelLength() - 1) + ".txt");
/* 256 */           return true;
/*     */         }
/* 258 */         return false;
/*     */       }
/*     */       
/*     */       public boolean switchHMM() {
/*     */         try {
/* 263 */           if ((this.hapHMM instanceof FreeHaplotypeHMM)) return false;
/* 264 */           Fastphase.logger.info("SWITCHING!!!!");
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 269 */           this.hapHMM = new FreeHaplotypeHMM(this.hapHMM);
/* 270 */           Fastphase.this.hmmFile = new File(Fastphase.this.dir, "results_" + this.hapHMM.getClass().toString() + "_" + (this.hapHMM.modelLength() - 1) + ".txt");
/* 271 */           makeGenotypeHMM();
/*     */         } catch (Exception exc) {
/* 273 */           exc.printStackTrace();
/*     */         }
/* 275 */         return true;
/*     */       }
/*     */       
/* 278 */       private void setHMM(HaplotypeHMM hmm) { this.hapHMM = hmm;
/* 279 */         makeGenotypeHMM();
/*     */       }
/*     */       
/*     */ 
/*     */       private void makeGenotypeHMM()
/*     */       {
/* 285 */         this.hmm = new CachedHMM[Fastphase.this.modelLengths.length];
/* 286 */         this.data1 = new List[Fastphase.this.modelLengths.length];
/* 287 */         for (int i = 0; i < this.hmm.length; i++) {
/* 288 */           if (Fastphase.this.modelLengths[i] != 0) {
/* 289 */             int[] no_copies = new int[i + 1];
/* 290 */             Arrays.fill(no_copies, 0);
/* 291 */             CompoundMarkovModel hmm_1 = new PairMarkovModel(new MarkovModel[] { this.hapHMM }, no_copies);
/* 292 */             if (Constants.fast()) hmm_1 = new CollapsedHMM(hmm_1);
/* 293 */             this.hmm[i] = new CachedHMM(hmm_1);
/* 294 */             this.data1[i] = new ArrayList();
/* 295 */             for (Iterator<PhasedIntegerGenotypeData> it = Fastphase.this.data[i].iterator(); it.hasNext();) {
/* 296 */               this.data1[i].add(
/* 297 */                 StateIndices.get((PhasedIntegerGenotypeData)it.next(), this.hmm[i]));
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       
/* 303 */       public double logProb = Double.NEGATIVE_INFINITY;
/*     */       
/* 305 */       public void train(int maxIt, double prec, boolean last) throws Exception { int trainingRounds = 0;
/* 306 */         BaumWelchTrainer[] bwt = new BaumWelchTrainer[Fastphase.this.modelLengths.length];
/* 307 */         for (int i = 0; i < bwt.length; i++) {
/* 308 */           if (Fastphase.this.modelLengths[i] != 0) {
/* 309 */             bwt[i] = new BaumWelchTrainer(this.hmm[i], (StateIndices[])this.data1[i].toArray(new StateIndices[0]));
/*     */           }
/*     */         }
/* 312 */         int num = 0;
/* 313 */         int numZeroChanges = 0;
/*     */         
/* 315 */         for (int i = 0;; i++) {
/* 316 */           double pseudo = ((Double)this.pseudoIterator.next()).doubleValue();
/* 317 */           this.hapHMM.setPseudoCountWeights(pseudo);
/* 318 */           double lp = 0.0D;
/* 319 */           this.hapHMM.initialiseCounts();
/* 320 */           for (int ik = 0; ik < bwt.length; ik++) {
/* 321 */             if (Fastphase.this.modelLengths[ik] != 0) {
/* 322 */               this.hmm[ik].refresh();
/* 323 */               lp += bwt[ik].expectationStep();
/*     */             }
/*     */           }
/* 326 */           BaumWelchTrainer.maximisationStep(this.hapHMM);
/* 327 */           for (int ik = 0; ik < bwt.length; ik++) {
/* 328 */             if (Fastphase.this.modelLengths[ik] != 0) {
/* 329 */               this.hmm[ik].refresh();
/*     */             }
/*     */           }
/* 332 */           long t1 = System.currentTimeMillis();
/*     */           
/*     */ 
/* 335 */           Fastphase.logger.info("log prob is " + lp + " at " + i + " with " + (this.hapHMM.modelLength() - 1) + " states " + (t1 - Fastphase.time) + " at " + pseudo);
/* 336 */           Fastphase.time = t1;
/*     */           
/* 338 */           if (lp + 1.0D < this.logProb) {
/* 339 */             Fastphase.logger.info("warning: log prob should always be increasing ".toUpperCase() + lp + " should be > " + this.logProb);
/* 340 */             break;
/*     */           }
/* 342 */           if ((Math.abs(lp - this.logProb) < prec) && (pseudo <= 0.01D)) num++;
/* 343 */           this.logProb = lp;
/* 344 */           if (((num >= 2) || (i > maxIt) || (numZeroChanges >= 1)) && ((!last) || (pseudo <= 0.01D))) {
/*     */             break;
/*     */           }
/*     */           
/*     */ 
/* 349 */           trainingRounds++;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */     final boolean[] modelLengths;
/*     */     
/*     */     final File dir;
/*     */     
/*     */     final int noIndiv;
/*     */     
/*     */     final String sb_i;
/*     */     
/*     */     final String sb_d;
/*     */     
/*     */     final Sampler monitor;
/*     */     public Fastphase(DataCollection data, Sampler monitor, int numRep, File dir)
/*     */       throws Exception
/*     */     {
/* 369 */       this.modelLength = numRep;
/* 370 */       this.dir = dir;
/* 371 */       this.dataC = data;
/* 372 */       this.noIndiv = data.size();
/* 373 */       SortedSet<Integer> len = new TreeSet();
/* 374 */       for (Iterator<PhasedIntegerGenotypeData> it = data.iterator(); it.hasNext();) {
/* 375 */         PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)it.next();
/*     */         
/* 377 */         len.add(Integer.valueOf(dat.noCopies()));
/*     */       }
/* 379 */       this.modelLengths = new boolean[((Integer)len.last()).intValue()];
/* 380 */       this.data = new List[((Integer)len.last()).intValue()];
/* 381 */       for (int i = 0; i < this.modelLengths.length; i++) {
/* 382 */         this.modelLengths[i] = len.contains(Integer.valueOf(i + 1));
/* 383 */         this.data[i] = (this.modelLengths[i] != 0 ? new ArrayList() : null);
/*     */       }
/* 385 */       for (Iterator<PhasedIntegerGenotypeData> it = data.iterator(); it.hasNext();) {
/* 386 */         PhasedIntegerGenotypeData dat = (PhasedIntegerGenotypeData)it.next();
/* 387 */         this.data[(dat.noCopies() - 1)].add(dat);
/*     */       }
/* 389 */       StringBuffer sb_is = new StringBuffer();StringBuffer sb_ds = new StringBuffer();
/* 390 */       StringBuffer sb_is1 = new StringBuffer();
/* 391 */       for (int i = 0; i < data.get(0).length(); i++) {
/* 392 */         sb_is.append("%8i ");
/* 393 */         sb_ds.append("%8.2g ");
/*     */       }
/* 395 */       this.sb_i = sb_is.toString();
/* 396 */       this.sb_d = sb_ds.toString();
/*     */       
/* 398 */       this.hmmOutputFile = new File(dir, "hmm_output");
/*     */       
/* 400 */       this.noSites = data.get(0).length();
/* 401 */       this.phasedFile = new File(dir, "phased.txt");
/* 402 */       this.monitor = monitor;
/* 403 */       this.initialHMMFile = new File(dir, "results_initial.txt");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     private static boolean noNull(Comparable[] c)
/*     */     {
/* 414 */       for (int i = 0; i < c.length; i++) {
/* 415 */         if (c[i] == null) return false;
/*     */       }
/* 417 */       return true;
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
/*     */     public boolean equal(Comparable[] o1, Comparable[] o2)
/*     */     {
/* 430 */       return Arrays.asList(o1).equals(Arrays.asList(o2));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     public static void writeHMM(CachedHMM[] phmms, File textHMMOutput, DataCollection data)
/*     */       throws Exception
/*     */     {
/* 439 */       PairMarkovModel phmm = null;
/* 440 */       for (int i = 0; i < phmms.length; i++)
/*     */       {
/* 442 */         if (phmms[i] != null) {
/* 443 */           MarkovModel hmm = phmms[i].innerModel;
/* 444 */           phmm = (hmm instanceof PairMarkovModel) ? (PairMarkovModel)hmm : 
/*     */           
/* 446 */             (PairMarkovModel)((CollapsedHMM)hmm).hmm;
/*     */         }
/*     */       }
/* 449 */       logger.info("WRITING HMM " + textHMMOutput.getAbsolutePath());
/* 450 */       PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(textHMMOutput)));
/*     */       
/* 452 */       StringBuffer sb = new StringBuffer();
/* 453 */       Integer[] maf = (Integer[])data.maf.toArray(new Integer[0]);
/* 454 */       Integer[] maf_null = (Integer[])data.maf_null.toArray(new Integer[0]);
/*     */       
/* 456 */       StringBuffer sb1 = new StringBuffer();
/* 457 */       for (int i = 0; i < maf.length; i++) {
/* 458 */         sb1.append("%8i ");
/*     */       }
/* 460 */       pw.println("m_allele " + Format.sprintf(sb1.toString(), maf));
/* 461 */       pw.println("n_allele " + Format.sprintf(sb1.toString(), maf_null));
/* 462 */       phmm.print(pw);
/* 463 */       pw.close();
/*     */     }
/*     */   }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/Fastphase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */