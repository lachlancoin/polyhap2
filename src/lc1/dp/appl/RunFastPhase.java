/*     */ package lc1.dp.appl;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.core.BaumWelchTrainer;
/*     */ import lc1.dp.core.Sampler;
/*     */ import lc1.dp.data.classification.ErrorClassificationAbstract;
/*     */ import lc1.dp.data.classification.ErrorClassificationCNV;
/*     */ import lc1.dp.data.classification.ErrorClassificationGenotype;
/*     */ import lc1.dp.data.classification.ErrorClassificationHaplopair;
/*     */ import lc1.dp.data.collection.DataC;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.collection.LikelihoodDataCollection;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.external.FastPhaseFormat;
/*     */ import lc1.dp.external.Fastphase;
/*     */ import lc1.dp.model.HaplotypeHMMIterator;
/*     */ import lc1.dp.model.MarkovModel;
/*     */ import lc1.dp.model.PairMarkovModel;
/*     */ import lc1.dp.states.AlleleCopyPairEmissionState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.util.Constants;
/*     */ import pal.statistics.ChiSquareTest;
/*     */ 
/*     */ public class RunFastPhase
/*     */ {
/*  57 */   static Logger logger = ;
/*     */   
/*     */   static Integer[] num;
/*  60 */   static Calendar cal = new GregorianCalendar();
/*     */   
/*  62 */   static Double[] chiSq(Integer[][] vit_control, Integer[][] vit_cases) { Double[] p = new Double[vit_control[0].length];
/*  63 */     for (int i = 0; i < p.length; i++) {
/*  64 */       List<Number> expected = new ArrayList();
/*  65 */       List<Integer> obs = new ArrayList();
/*  66 */       double sum = 0.0D;
/*  67 */       int len = vit_control.length;
/*  68 */       for (int j = 0; j < len; j++) {
/*  69 */         if ((vit_control[j][i].intValue() != 0) || (vit_cases[j][i].intValue() != 0)) {
/*  70 */           expected.add(vit_control[j][i]);
/*  71 */           obs.add(vit_cases[j][i]);
/*  72 */           sum += vit_control[j][i].intValue();
/*     */         }
/*     */       }
/*  75 */       double[] ex = new double[expected.size()];
/*  76 */       int[] o = new int[expected.size()];
/*  77 */       for (int j = 0; j < expected.size(); j++) {
/*  78 */         ex[j] = (((Number)expected.get(j)).doubleValue() / sum);
/*  79 */         o[j] = ((Integer)obs.get(j)).intValue();
/*     */       }
/*  81 */       p[i] = Double.valueOf(ChiSquareTest.compare(ex, o));
/*     */     }
/*  83 */     return p;
/*     */   }
/*     */   
/*  86 */   static Comparator comp = new Comparator()
/*     */   {
/*     */     public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
/*  89 */       return -1 * ((Integer)o1.getValue()).compareTo((Integer)o2.getValue());
/*     */     }
/*     */   };
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
/*     */   public static Runnable getMonitor(Sampler sampler, final DataC original, final DataC affy_reference, final PrintWriter summary, File clusterFile, final File parentfile)
/*     */   {
/* 106 */     new Runnable() {
/* 107 */       Map<String, boolean[]> unc = null;
/*     */       
/* 109 */       public void run() { runSample();
/* 110 */         if (Constants.summarise()) runSummary();
/*     */       }
/*     */       
/*     */       public void runSample() {
/*     */         try {
/* 115 */           if (Constants.sample()) {
/* 116 */             List<Integer> list = new ArrayList();
/* 117 */             if (Constants.keepBest()) {
/* 118 */               list.add(Integer.valueOf(0));
/*     */             }
/*     */             else {
/* 121 */               for (int i = 0; i < Constants.numRep(); i++) {
/* 122 */                 list.add(Integer.valueOf(i));
/*     */               }
/*     */             }
/* 125 */             RunFastPhase.logger.info("starting sample");
/* 126 */             RunFastPhase.this.calcBestPathSampling(list);
/* 127 */             if (Constants.distanceLD > 0) {
/* 128 */               RunFastPhase.this.countsHap(list);
/* 129 */               PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream(new File("ldDr2"))));
/* 130 */               RunFastPhase.this.printLD(ps);
/* 131 */               ps.close();
/*     */             }
/* 133 */             RunFastPhase.logger.info("ending sample");
/*     */             
/*     */ 
/* 136 */             PrintWriter pw_hap1 = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "phased1.txt" + Constants.column()))));
/*     */             
/* 138 */             PrintWriter pw_hap2 = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "phased2.txt" + Constants.column()))));
/*     */             
/*     */ 
/* 141 */             pw_hap2.println(RunFastPhase.cal.getTime());
/*     */             
/* 143 */             Collection<Integer> toD = Constants.drop() ? RunFastPhase.this.data.calculateMaf1().getConstantPos() : null;
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 149 */             PrintWriter pw_st = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "phased_states.txt" + Constants.column()))));
/*     */             
/* 151 */             RunFastPhase.this.data.printHapMapFormat(pw_hap1, toD, true, 
/*     */             
/* 153 */               new String[] { "snpid", "loc" }, 
/* 154 */               new String[0], 
/*     */               
/* 156 */               new String[] { "uncertaintyVitPhase", "uncertaintyPhase", "dataL" }, "%7s");
/*     */             
/*     */ 
/* 159 */             ((DataCollection)RunFastPhase.this.data).writeFastphase(pw_hap2, false);
/* 160 */             pw_hap1.close();
/* 161 */             pw_hap2.close();
/*     */             
/*     */ 
/*     */ 
/*     */ 
/* 166 */             RunFastPhase.this.data.writeFastphase(((DataCollection)RunFastPhase.this.data).viterbi, ((DataCollection)RunFastPhase.this.data).uncertaintyVitPhase, 
/* 167 */               pw_st, false, true, false, null);
/* 168 */             pw_st.close();
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (Exception exc)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/* 183 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/* 187 */       public void runSummary() { RunFastPhase.logger.info("starting run");
/*     */         
/*     */ 
/*     */         try
/*     */         {
/*     */           double fn;
/*     */           
/*     */           double tn;
/*     */           
/*     */           double fp;
/*     */           
/* 198 */           double tp = fp = tn = fn = 0.0D;
/*     */           
/*     */ 
/* 201 */           if (Constants.sample())
/*     */           {
/* 203 */             if (Constants.sampleWithPedigree())
/*     */             {
/*     */ 
/*     */ 
/* 207 */               for (Iterator<String> it = RunFastPhase.this.data.getKeys().iterator(); it.hasNext();) {
/* 208 */                 String j = (String)it.next();
/* 209 */                 SortedMap[] rec2 = RunFastPhase.this.data.recSites(j);
/* 210 */                 System.err.println(j);
/* 211 */                 String[] j1 = j.split(";");
/* 212 */                 SortedMap[] rec1 = original.recSites(j1[(j1.length - 1)]);
/* 213 */                 if (rec1 != null)
/*     */                 {
/*     */ 
/* 216 */                   summary.println("orig vs inferred" + j1[(j1.length - 1)]);
/*     */                   
/*     */ 
/*     */ 
/* 220 */                   for (int k = 0; k < rec1.length; k++) {
/* 221 */                     summary.println("orig: " + rec1[k]);
/* 222 */                     List<Map.Entry<Integer, Integer>> l = new ArrayList((Collection)rec2[k].entrySet());
/* 223 */                     Collections.sort(l, RunFastPhase.comp);
/* 224 */                     summary.println("inf : " + l);
/* 225 */                     Set<Integer> trues = rec1[k].keySet();
/* 226 */                     Set<Integer> positives = rec2[k].keySet();
/* 227 */                     Set<Integer> negatives = new HashSet();
/* 228 */                     for (int i = 0; i < RunFastPhase.this.data.length(); i++) {
/* 229 */                       negatives.add(Integer.valueOf(i));
/*     */                     }
/*     */                     
/*     */ 
/* 233 */                     for (Iterator<Integer> it1 = positives.iterator(); it1.hasNext();) {
/* 234 */                       Integer pos = (Integer)it1.next();
/* 235 */                       if (contains(trues, pos)) { tp += 1.0D;
/*     */                       } else {
/* 237 */                         System.err.println("false positive " + pos + " cf " + trues);
/* 238 */                         fp += 1.0D;
/*     */                       }
/*     */                     }
/* 241 */                     for (Iterator<Integer> it1 = negatives.iterator(); it1.hasNext();) {
/* 242 */                       Integer fa = (Integer)it1.next();
/* 243 */                       if (!contains(positives, fa)) {
/* 244 */                         if (trues.contains(fa)) {
/* 245 */                           System.err.println("false negative " + fa + " cf " + trues);
/* 246 */                           fn += 1.0D;
/*     */                         } else {
/* 248 */                           tn += 1.0D;
/*     */                         }
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */             
/*     */ 
/* 258 */             summary.println("tp:fn:tn:fp " + tp + " " + fn + " " + tn + " " + fp);
/* 259 */             if (tp + fn > 0.0D) summary.println("sensitivity " + tp / (tp + fn));
/* 260 */             if (tn + fp > 0.0D) summary.println("specifitity " + tn / (tn + fp));
/* 261 */             if (tp + fp > 0.0D) summary.println("ppv " + tp / (tp + fp));
/* 262 */             if (tn + fn > 0.0D) { summary.println("npv " + fn / (tn + fn));
/*     */             }
/*     */           }
/*     */           
/* 266 */           if (Constants.sampleWithPedigree())
/*     */           {
/*     */ 
/* 269 */             RunFastPhase.this.data.extractFromTrioData();
/*     */           }
/*     */           
/*     */ 
/* 273 */           if ((Constants.fillGaps()) && (Constants.sample())) {
/* 274 */             Set<Integer> gapSites = new HashSet();
/* 275 */             Map<Integer, Integer> polySites = new TreeMap();
/* 276 */             Map<Integer, Map<String, Double>> polyCertainty = new TreeMap();
/* 277 */             Map<Integer, Integer> realPolySites = new TreeMap();
/*     */             
/* 279 */             int gap_count = 0;
/* 280 */             int poly_count = 0;
/* 281 */             for (int i = 0; i < original.length(); i++) {
/* 282 */               for (Iterator<String> it = original.getKeys().iterator(); it.hasNext();) {
/* 283 */                 String key = (String)it.next();
/* 284 */                 if (((ComparableArray)RunFastPhase.this.data.get(key).getElement(i)).containsNull()) {
/* 285 */                   Integer val = (Integer)polySites.get(Integer.valueOf(i));
/* 286 */                   polySites.put(Integer.valueOf(i), Integer.valueOf(val == null ? 1 : val.intValue() + 1));
/*     */                   
/* 288 */                   poly_count++;
/*     */                 }
/*     */                 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 295 */                 if (((ComparableArray)original.get(key).getElement(i)).containsNull()) {
/* 296 */                   Integer val = (Integer)realPolySites.get(Integer.valueOf(i));
/* 297 */                   realPolySites.put(Integer.valueOf(i), Integer.valueOf(val == null ? 1 : val.intValue() + 1));
/*     */                 }
/* 299 */                 if (((ComparableArray)affy_reference.get(key).getElement(i)).containsNull()) {
/* 300 */                   gapSites.add(Integer.valueOf(i));
/* 301 */                   gap_count++;
/*     */                 }
/*     */               }
/*     */             }
/* 305 */             summary.println("frac poly " + polySites.size() / gapSites.size() + " " + poly_count / gap_count);
/* 306 */             summary.println(polySites);
/* 307 */             summary.println(gapSites);
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
/* 320 */             if (RunFastPhase.this.data.ped() != null)
/*     */             {
/* 322 */               double[][] cons = RunFastPhase.this.data.checkConsistency();
/* 323 */               summary.println("Consistency inferred");
/* 324 */               summary.println("Consistency emissions: " + cons[0][0] + " " + cons[0][1]);
/* 325 */               summary.println("Consistency state:     " + cons[1][0] + " " + cons[1][1]);
/* 326 */               summary.println("Consistency original");
/* 327 */               cons = original.checkConsistency();
/* 328 */               summary.println("Consistency emissions: " + cons[0][0] + " " + cons[0][1]);
/* 329 */               summary.println("Consistency state:     " + cons[1][0] + " " + cons[1][1]);
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
/* 341 */           int numIt = Constants.numItSum();
/* 342 */           int[][] sources = original.getSourcePositions();
/* 343 */           if (original != affy_reference) {
/* 344 */             if (numIt > 0) {
/* 345 */               PrintWriter logCNV = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "logCNV.txt" + Constants.column()))));
/* 346 */               PrintWriter logGeno = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "logGeno.txt" + Constants.column()))));
/* 347 */               PrintWriter logHaplo = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "logHaplo.txt" + Constants.column()))));
/* 348 */               summary.println("COMPARING INFERENCE WITH AFFY");
/*     */               
/* 350 */               RunFastPhase.compare(RunFastPhase.this.data, affy_reference, 
/* 351 */                 this.unc, 
/* 352 */                 "sampling avg", 
/* 353 */                 0.0D, summary, 
/*     */                 
/* 355 */                 sources, 
/* 356 */                 new PrintWriter[] { logCNV, logGeno, logHaplo });
/*     */               
/* 358 */               logCNV.close();
/* 359 */               logGeno.close();
/* 360 */               logHaplo.close();
/*     */             }
/*     */             
/* 363 */             summary.println("COMPARING ORIGINAL WITH AFFY");
/* 364 */             RunFastPhase.compare(original, affy_reference, this.unc, 
/* 365 */               "sampling avg", 
/* 366 */               0.0D, summary, 
/* 367 */               sources, 
/* 368 */               new PrintWriter[3]);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         }
/*     */         catch (Exception exc)
/*     */         {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 391 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/*     */       private boolean contains(Set<Integer> trues, Integer pos) {
/* 396 */         int gap = 2;
/* 397 */         for (int i = pos.intValue() - gap; i <= pos.intValue() + gap; i++) {
/* 398 */           if (trues.contains(Integer.valueOf(i))) return true;
/*     */         }
/* 400 */         return false;
/*     */       }
/*     */       
/*     */       private double getMax(Collection<Double> set) {
/* 404 */         double max = 0.0D;
/* 405 */         for (Iterator<Double> it = set.iterator(); it.hasNext();) {
/* 406 */           Double d = (Double)it.next();
/* 407 */           if (d.doubleValue() > max) {
/* 408 */             max = d.doubleValue();
/*     */           }
/*     */         }
/* 411 */         return max;
/*     */       }
/*     */       
/* 414 */       void compare1(Map<Integer, Map<String, Double>> polyC, Set<Integer> real, PrintWriter pw, int cnt_thresh) { double[] thres = { 0.0D, 0.5D, 0.6D, 0.7D, 0.8D, 0.9D, 0.95D };
/* 415 */         for (int j = 0; j < thres.length; j++) {
/* 416 */           Set<Integer> poly = new HashSet();
/* 417 */           for (Iterator<Map.Entry<Integer, Map<String, Double>>> it = polyC.entrySet().iterator(); it.hasNext();) {
/* 418 */             Map.Entry<Integer, Map<String, Double>> ent = (Map.Entry)it.next();
/* 419 */             if ((getMax(((Map)ent.getValue()).values()) > thres[j]) && (((Map)ent.getValue()).size() >= cnt_thresh)) {
/* 420 */               poly.add((Integer)ent.getKey());
/*     */             }
/*     */           }
/* 423 */           Set intersection = new HashSet(real);
/* 424 */           intersection.retainAll(poly);
/* 425 */           double sens = intersection.size() / real.size();
/* 426 */           double fp = poly.size() - intersection.size();
/* 427 */           double spec = fp / poly.size();
/* 428 */           pw.println("site tp / true // fp / pred at " + thres[j] + " % " + sens + " " + spec);
/*     */         }
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void run(DataC originalData, File summar, File clusters, File parentfile)
/*     */     throws Exception
/*     */   {
/* 445 */     if (originalData == null) throw new RuntimeException("!!");
/* 446 */     long time = System.currentTimeMillis();
/*     */     
/* 448 */     boolean allowCloning = Constants.allowCloning;
/*     */     
/* 450 */     PrintWriter summary = null;
/* 451 */     File affy = new File("calls.50K_Merged.CEPH.Affy.txt");
/* 452 */     File affy_p = new File("sim.phased.txt");
/* 453 */     File affy_up = new File("sim.unphased.txt");
/*     */     DataC reference;
/* 455 */     if (affy_up.exists()) {
/* 456 */       DataC reference = new SimpleDataCollection(affy_up, (short)0, 2);
/* 457 */       String[] names = (String[])reference.getKeys().toArray(new String[0]);
/* 458 */       reference = new SimpleDataCollection(affy_p, (short)0, 2);
/* 459 */       ((SimpleDataCollection)reference).transform(Arrays.asList(names));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     }
/* 468 */     else if (affy.exists()) {
/* 469 */       DataC reference = new SimpleDataCollection();
/* 470 */       SimpleDataCollection.readAffy(affy.getParentFile(), affy.getName(), true, (SimpleDataCollection)reference, 2);
/*     */     } else { DataC reference;
/* 472 */       if ((originalData instanceof LikelihoodDataCollection)) {
/* 473 */         reference = Constants.allowCloning() ? originalData.clone() : originalData;
/*     */       }
/*     */       else
/*     */       {
/* 477 */         reference = originalData; }
/*     */     }
/* 479 */     if (reference.length() != originalData.length()) throw new RuntimeException("!! " + reference.length() + " " + originalData.length());
/* 480 */     if ((Constants.format()[0].equals("illumina")) && ((originalData instanceof LikelihoodDataCollection)) && (Constants.allowCloning())) {
/* 481 */       originalData.clearData();
/* 482 */       ((LikelihoodDataCollection)originalData).calculateMLGenotypeData(Constants.allowCloning());
/*     */     }
/*     */     
/* 485 */     DataCollection copy = Constants.allowCloning() ? (DataCollection)originalData.clone() : (DataCollection)originalData;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 494 */     if (Constants.run())
/*     */     {
/*     */ 
/* 497 */       if ((Constants.trainWithPedigree()) || (Constants.sampleWithPedigree())) {
/* 498 */         copy.arrangeDataAccordingToPedigree();
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/* 504 */       String col = Constants.column();
/*     */       
/*     */ 
/*     */ 
/* 508 */       PrintWriter pw_indiv = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "indiv.txt" + Constants.column()))));
/*     */       
/*     */ 
/*     */ 
/* 512 */       Sampler sampler = new Sampler(copy, "samples");
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 518 */       Collection<Integer> toD = Constants.drop() ? sampler.data.calculateMaf1().getConstantPos() : null;
/*     */       
/*     */ 
/*     */ 
/* 522 */       originalData.writeSNPFile(new File(parentfile, "snp.txt"), Constants.chrom0(), true, toD);
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
/* 535 */       reference.printIndiv(pw_indiv);
/* 536 */       pw_indiv.close();
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 544 */       Fastphase fp = new Fastphase(copy, sampler, Constants.numRep(), parentfile);
/*     */       
/*     */ 
/*     */ 
/* 548 */       if (!Constants.resample()) { Iterator<MarkovModel> hit;
/*     */         Iterator<MarkovModel> hit;
/* 550 */         if (Constants.transMode(1) != null)
/*     */         {
/*     */ 
/* 553 */           final HaplotypeHMMIterator counts = new HaplotypeHMMIterator("counts", originalData.length(), Constants.numRep(), 
/* 554 */             originalData.getLocations(), Constants.modify(0).length, 
/* 555 */             Emiss.countSpace(), Constants.transMode(0), Constants.modify(0), Constants.modifyFrac(0), originalData.numLevels());
/* 556 */           HaplotypeHMMIterator alleles = new HaplotypeHMMIterator("alleles", originalData.length(), Constants.numRep(), 
/* 557 */             originalData.getLocations(), Constants.modify(1).length, 
/* 558 */             Emiss.alleleSpace(), Constants.transMode(1), Constants.modify(1), Constants.modifyFrac(1), originalData.numLevels());
/* 559 */           hit = new Iterator()
/*     */           {
/*     */             public boolean hasNext() {
/* 562 */               return (RunFastPhase.this.hasNext()) && (counts.hasNext());
/*     */             }
/*     */             
/*     */             public MarkovModel next() {
/* 566 */               MarkovModel cnt = counts.next();
/* 567 */               MarkovModel allele = RunFastPhase.this.next();
/*     */               
/* 569 */               return 
/*     */               
/* 571 */                 new PairMarkovModel(new MarkovModel[] { cnt, allele }, new int[] { 0, 1 }, AlleleCopyPairEmissionState.class, 
/* 572 */                 Emiss.stateSpace(), false);
/*     */             }
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */             public void remove() {}
/*     */           };
/*     */         }
/*     */         else
/*     */         {
/* 583 */           hit = new HaplotypeHMMIterator("", originalData.length(), Constants.numRep(), originalData.getLocations(), Constants.modify(0).length, 
/* 584 */             Emiss.stateSpace(), 
/* 585 */             Constants.transMode(0), Constants.modify(0), Constants.modifyFrac(0), originalData.numLevels());
/*     */         }
/*     */         
/* 588 */         Callable call = fp.train(fp.getTrainingElements(hit));
/* 589 */         List tasks = Arrays.asList(new Callable[] { call });
/*     */         
/*     */ 
/*     */ 
/* 593 */         BaumWelchTrainer.es.invokeAll(tasks);
/*     */       }
/* 595 */       summary = new PrintWriter(new BufferedWriter(new FileWriter(summar)));
/* 596 */       Runnable monitor = getMonitor(sampler, originalData, reference, summary, clusters, parentfile);
/* 597 */       summary.println(cal.getTime().toString().toUpperCase() + "------------------------------");
/* 598 */       summary.println(Constants.transMode(0) + " " + Constants.transMode(1) + " " + Constants.numRep() + " ");
/* 599 */       monitor.run();
/*     */       
/*     */ 
/* 602 */       Constants.plot();
/*     */     }
/*     */     
/*     */ 
/* 606 */     if (Constants.runFastPhase()) {
/* 607 */       if (summary == null) {
/* 608 */         summary = new PrintWriter(new BufferedWriter(new FileWriter(summar)));
/*     */       }
/* 610 */       FastPhaseFormat.runFastphase(copy, originalData, originalData, Constants.numF(0), Constants.numRep(), 
/* 611 */         Constants.sum(Constants.numIt()), summary);
/* 612 */       logger.info("finished in " + (System.currentTimeMillis() - time));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 620 */     logger.info("finished in " + (System.currentTimeMillis() - time));
/* 621 */     if (Constants.plot() == 1) System.exit(0);
/* 622 */     BaumWelchTrainer.es.shutdown();
/* 623 */     summary.close();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static void add(int[] d, int[] toAdd)
/*     */   {
/* 630 */     for (int i = 0; i < d.length; i++)
/* 631 */       d[i] += toAdd[i];
/*     */   }
/*     */   
/*     */   public static Set getSet(Integer[] i) {
/* 635 */     return new HashSet((Collection)Arrays.asList(i));
/*     */   }
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
/*     */   public static void compare(DataC data, DataC orig_no_missing, Map<String, boolean[]> inferredPositions, String origin, double thresh, PrintWriter pw, int[][] sources, PrintWriter[] logSumm)
/*     */   {
/* 649 */     if (data.length() != orig_no_missing.length()) throw new RuntimeException("!!");
/* 650 */     logger.info("comparing ...");
/*     */     
/* 652 */     EmissionState maf = data.maf();
/* 653 */     Set<Integer> gapPositions = new TreeSet();
/* 654 */     Set[][] noCopies = {
/* 655 */       { getSet(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6) }), getSet(new Integer[] { Integer.valueOf(0), Integer.valueOf(1), Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6) }) }, 
/* 656 */       { getSet(new Integer[] { Integer.valueOf(1) }), getSet(new Integer[] { Integer.valueOf(1) }) }, 
/* 657 */       { getSet(new Integer[] { Integer.valueOf(1) }), getSet(new Integer[] { Integer.valueOf(2) }) }, 
/* 658 */       { getSet(new Integer[] { Integer.valueOf(1) }), getSet(new Integer[] { Integer.valueOf(3) }) }, 
/* 659 */       { getSet(new Integer[] { Integer.valueOf(2) }), getSet(new Integer[] { Integer.valueOf(1) }) }, 
/* 660 */       { getSet(new Integer[] { Integer.valueOf(2) }), getSet(new Integer[] { Integer.valueOf(2) }) }, 
/* 661 */       { getSet(new Integer[] { Integer.valueOf(2) }), getSet(new Integer[] { Integer.valueOf(3) }) }, 
/* 662 */       { getSet(new Integer[] { Integer.valueOf(3) }), getSet(new Integer[] { Integer.valueOf(1) }) }, 
/* 663 */       { getSet(new Integer[] { Integer.valueOf(3) }), getSet(new Integer[] { Integer.valueOf(2) }) }, 
/* 664 */       { getSet(new Integer[] { Integer.valueOf(3) }), getSet(new Integer[] { Integer.valueOf(3) }) } };
/*     */     
/* 666 */     EmissionStateSpace[] numberofcopies = data.getNoCopies();
/* 667 */     for (int ii1 = 0; ii1 < numberofcopies.length; ii1++)
/*     */     {
/* 669 */       pw.println("NO COPIES == " + (ii1 + 1));
/*     */       
/* 671 */       int[][] switch_ = new int[noCopies.length][];
/* 672 */       for (int i = 0; i < switch_.length; i++) {
/* 673 */         switch_[i] = new int[2];
/*     */       }
/* 675 */       int[] missing = new int[6];
/* 676 */       EmissionStateSpace emStSp = numberofcopies[ii1];
/* 677 */       if (emStSp != null) {
/* 678 */         ErrorClassificationAbstract[] classif = {
/* 679 */           new ErrorClassificationCNV(emStSp, thresh, sources.length, logSumm[0]), new ErrorClassificationGenotype(emStSp, thresh, sources.length, logSumm[1]), 
/* 680 */           new ErrorClassificationHaplopair(emStSp, thresh, sources.length, logSumm[2]) };
/* 681 */         for (Iterator<String> it = data.getKeys().iterator(); it.hasNext();) {
/* 682 */           String key = (String)it.next();
/* 683 */           PIGData inference = data.get(key);
/* 684 */           if (inference.noCopies() == ii1 + 1) {
/* 685 */             for (int ik = 0; ik < inference.length(); ik++) {
/* 686 */               if (((ComparableArray)inference.getElement(ik)).noCopies(true) != 2) gapPositions.add(Integer.valueOf(ik));
/*     */             }
/* 688 */             if (orig_no_missing.containsKey(inference.getName())) {
/* 689 */               PIGData original_no_missing = orig_no_missing.get(inference.getName());
/* 690 */               if (inference.length() != original_no_missing.length()) {
/* 691 */                 throw new RuntimeException("!!");
/*     */               }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 698 */               if (Constants.fillGaps())
/*     */               {
/* 700 */                 for (int ik = 0; ik < classif.length; ik++) {
/* 701 */                   classif[ik].compare(original_no_missing, inference, data.getL(key), sources, null);
/*     */                 }
/*     */               }
/*     */               
/* 705 */               for (int i = 0; i < switch_.length; i++) {
/* 706 */                 add(switch_[i], inference.phaseCorrect(original_no_missing, noCopies[i][0], noCopies[i][1]));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/* 711 */         pw.println("gap positions " + gapPositions.size());
/* 712 */         pw.println("gap pos " + gapPositions);
/* 713 */         pw.println("comparing with " + origin);
/*     */         
/* 715 */         pw.println("phasing accuracy");
/* 716 */         for (int i = 0; i < switch_.length; i++) {
/* 717 */           pw.println(Arrays.asList(new Set[] { noCopies[i][0] }) + "->" + Arrays.asList(new Set[] { noCopies[i][1] }));
/* 718 */           int[] sw = switch_[i];
/* 719 */           Integer[] res = { Integer.valueOf(sw[0]), Integer.valueOf(sw[1]) };
/* 720 */           pw.println("perc " + Format.sprintf("%5i : %5i", res));
/* 721 */           pw.println("perc " + Format.sprintf("%5.3g ", new Double[] { Double.valueOf(res[0].intValue() / res[1].intValue()) }));
/*     */         }
/*     */         
/* 724 */         if (Constants.fillGaps()) {
/* 725 */           pw.println("missing data accuracy");
/* 726 */           pw.println("'-' called as '?' : total '-' //'?' called as '-' : total '?' // inferred ? with correct genotype  : total real '?'");
/*     */           
/*     */ 
/* 729 */           Integer[] res = { Integer.valueOf(missing[0]), Integer.valueOf(missing[1]), Integer.valueOf(missing[2]), Integer.valueOf(missing[3]), Integer.valueOf(missing[4]), Integer.valueOf(missing[5]) };
/* 730 */           pw.println("perc " + Format.sprintf("%5i:%5i // %5i:%5i // %5i:%5i", res));
/* 731 */           for (int ik = 0; ik < classif.length; ik++)
/* 732 */             classif[ik].print(pw);
/*     */         }
/*     */       }
/*     */     }
/* 736 */     pw.flush();
/* 737 */     logger.info("... done comparing");
/*     */   }
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static double sum(int[] switchWrong)
/*     */   {
/* 790 */     double sum = 0.0D;
/* 791 */     for (int i = 0; i < switchWrong.length; i++) {
/* 792 */       sum += switchWrong[i];
/*     */     }
/* 794 */     return sum;
/*     */   }
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
/*     */ 
/*     */   static Boolean get(String st)
/*     */   {
/* 820 */     if (st.equals("-")) return null;
/* 821 */     int i = Integer.parseInt(st);
/* 822 */     if (i == 0) return zero;
/* 823 */     if (i == 1) return one;
/* 824 */     throw new RuntimeException("!!"); }
/*     */   
/* 826 */   static Boolean one = new Boolean(true); static Boolean zero = new Boolean(false);
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/appl/RunFastPhase.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */