/*     */ package lc1.dp.genotype.io;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.PairMarkovModel;
/*     */ import lc1.dp.genotype.HaplotypeEmissionState;
/*     */ import lc1.dp.genotype.io.scorable.CompoundScorableObject;
/*     */ import lc1.dp.genotype.io.scorable.DataCollection;
/*     */ import lc1.dp.genotype.io.scorable.ErrorClassification;
/*     */ import lc1.dp.genotype.io.scorable.HaplotypeData;
/*     */ import lc1.dp.genotype.io.scorable.PhasedIntegerGenotypeData;
/*     */ import lc1.lines.FindLines;
/*     */ import pal.statistics.ChiSquareTest;
/*     */ 
/*     */ public class RunFastPhase
/*     */ {
/*  37 */   static Logger logger = ;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   static Integer[] num;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 145 */   static Calendar cal = new GregorianCalendar();
/*     */   
/* 147 */   static Double[] chiSq(Integer[][] vit_control, Integer[][] vit_cases) { Double[] p = new Double[vit_control[0].length];
/* 148 */     for (int i = 0; i < p.length; i++) {
/* 149 */       List<Number> expected = new ArrayList();
/* 150 */       List<Integer> obs = new ArrayList();
/* 151 */       double sum = 0.0D;
/* 152 */       int len = vit_control.length;
/* 153 */       for (int j = 0; j < len; j++) {
/* 154 */         if ((vit_control[j][i].intValue() != 0) || (vit_cases[j][i].intValue() != 0)) {
/* 155 */           expected.add(vit_control[j][i]);
/* 156 */           obs.add(vit_cases[j][i]);
/* 157 */           sum += vit_control[j][i].intValue();
/*     */         }
/*     */       }
/* 160 */       double[] ex = new double[expected.size()];
/* 161 */       int[] o = new int[expected.size()];
/* 162 */       for (int j = 0; j < expected.size(); j++) {
/* 163 */         ex[j] = (((Number)expected.get(j)).doubleValue() / sum);
/* 164 */         o[j] = ((Integer)obs.get(j)).intValue();
/*     */       }
/*     */       
/* 167 */       p[i] = Double.valueOf(ChiSquareTest.compare(ex, o));
/*     */     }
/*     */     
/* 170 */     return p;
/*     */   }
/*     */   
/*     */   public static void writeInferredFile(Iterator<PhasedIntegerGenotypeData> data, File phasedFile) throws Exception
/*     */   {
/* 175 */     PrintWriter pw1 = new PrintWriter(new BufferedWriter(new FileWriter(phasedFile)));
/* 176 */     for (int k = 0; data.hasNext(); k++) {
/* 177 */       pw1.println(k + " copies");
/* 178 */       PhasedIntegerGenotypeData nxt = (PhasedIntegerGenotypeData)data.next();
/* 179 */       nxt.print(pw1);
/*     */     }
/* 181 */     pw1.close();
/*     */   }
/*     */   
/*     */   public static ProgressMonitor getMonitor(final Sampler sampler, DataCollection orig_no_miss, final DataCollection original_missing, final PrintWriter summary, final File clusterFile, final File parentfile)
/*     */   {
/* 186 */     new ProgressMonitor()
/*     */     {
/*     */       public void monitor(String msg) {
/*     */         try {
/* 190 */           List<Integer> pos = RunFastPhase.this.loc;
/* 191 */           List<String> name = RunFastPhase.this.names;
/* 192 */           summary.println(msg);
/* 193 */           sampler.calcBestPathSampling();
/* 194 */           if (Constants.fillGaps()) {
/* 195 */             Set<Integer> gapSites = new HashSet();
/* 196 */             Map<Integer, Integer> polySites = new TreeMap();
/* 197 */             Map<Integer, Map<String, Double>> polyCertainty = new TreeMap();
/* 198 */             Map<Integer, Integer> realPolySites = new TreeMap();
/* 199 */             Map<Integer, String> names = new TreeMap();
/* 200 */             int gap_count = 0;
/* 201 */             int poly_count = 0;
/* 202 */             for (int i = 0; i < RunFastPhase.this.get(0).length(); i++) {
/* 203 */               for (int j = 0; j < RunFastPhase.this.size(); j++) {
/* 204 */                 if (CompoundScorableObject.containsNull((ComparableArray)sampler.data.get(j).getElement(i))) {
/* 205 */                   Integer val = (Integer)polySites.get(pos.get(i));
/* 206 */                   polySites.put((Integer)pos.get(i), Integer.valueOf(val == null ? 1 : val.intValue() + 1));
/* 207 */                   if (name.size() > 0) names.put((Integer)pos.get(i), (String)name.get(i));
/* 208 */                   poly_count++;
/* 209 */                   Map<String, Double> cert = (Map)polyCertainty.get(pos.get(i));
/* 210 */                   if (cert == null) polyCertainty.put((Integer)pos.get(i), cert = new HashMap());
/* 211 */                   cert.put(sampler.data.get(j).getName(), Double.valueOf(sampler.uncertaintyL[j][i]));
/*     */                 }
/*     */                 
/* 214 */                 if (CompoundScorableObject.containsNull((ComparableArray)RunFastPhase.this.get(j).getElement(i))) {
/* 215 */                   Integer val = (Integer)realPolySites.get(pos.get(i));
/* 216 */                   realPolySites.put((Integer)pos.get(i), Integer.valueOf(val == null ? 1 : val.intValue() + 1));
/*     */                 }
/* 218 */                 if (CompoundScorableObject.containsNull((ComparableArray)original_missing.get(j).getElement(i))) {
/* 219 */                   gapSites.add(Integer.valueOf(i));
/* 220 */                   gap_count++;
/*     */                 }
/*     */               }
/*     */             }
/* 224 */             summary.println("frac poly " + polySites.size() / gapSites.size() + " " + poly_count / gap_count);
/* 225 */             summary.println(polySites);
/* 226 */             summary.println(gapSites);
/* 227 */             PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(new File(parentfile, "gap_poly_sites.txt"))));
/* 228 */             pw.println("total " + sampler.data.size());
/* 229 */             for (Iterator<Map.Entry<Integer, Integer>> it = polySites.entrySet().iterator(); it.hasNext();) {
/* 230 */               Map.Entry<Integer, Integer> ent = (Map.Entry)it.next();
/* 231 */               pw.print(ent);
/* 232 */               pw.print("\t");
/* 233 */               pw.print((String)names.get(ent.getKey()));
/* 234 */               pw.print("\t");
/* 235 */               pw.println(polyCertainty.get(ent.getKey()));
/*     */             }
/* 237 */             pw.close();
/* 238 */             summary.println("cnt thresh 1 ");
/* 239 */             compare1(polyCertainty, realPolySites.keySet(), summary, 1);
/* 240 */             summary.println("cnt thresh 2 ");
/* 241 */             compare1(polyCertainty, realPolySites.keySet(), summary, 2);
/*     */           }
/* 243 */           sampler.data.printHapMapFormat(new File(parentfile, "phased.txt"), null);
/* 244 */           original_missing.printHapMapFormat(new File(parentfile, "data_with_missing.txt"), null);
/* 245 */           double[] thresh = Constants.getThresh();
/* 246 */           double[] nocopies = Constants.noCopies();
/* 247 */           for (int k = 0; k < thresh.length; k++) {
/* 248 */             RunFastPhase.compare(sampler.data.iterator(), RunFastPhase.this.iterator(), original_missing.iterator(), 
/* 249 */               "sampling avg", Arrays.asList(sampler.uncertaintyL).iterator(), thresh[k], summary, 
/* 250 */               (Collection)Arrays.asList(new Integer[] { Integer.valueOf(1), Integer.valueOf(2) }));
/*     */ 
/*     */           }
/*     */           
/*     */ 
/*     */         }
/*     */         catch (Exception exc)
/*     */         {
/*     */ 
/* 259 */           exc.printStackTrace();
/*     */         }
/*     */       }
/*     */       
/*     */       private double getMax(Collection<Double> set) {
/* 264 */         double max = 0.0D;
/* 265 */         for (Iterator<Double> it = set.iterator(); it.hasNext();) {
/* 266 */           Double d = (Double)it.next();
/* 267 */           if (d.doubleValue() > max) {
/* 268 */             max = d.doubleValue();
/*     */           }
/*     */         }
/* 271 */         return max;
/*     */       }
/*     */       
/* 274 */       void compare1(Map<Integer, Map<String, Double>> polyC, Set<Integer> real, PrintWriter pw, int cnt_thresh) { double[] thres = { 0.0D, 0.5D, 0.6D, 0.7D, 0.8D, 0.9D, 0.95D };
/* 275 */         for (int j = 0; j < thres.length; j++) {
/* 276 */           Set<Integer> poly = new HashSet();
/* 277 */           for (Iterator<Map.Entry<Integer, Map<String, Double>>> it = polyC.entrySet().iterator(); it.hasNext();) {
/* 278 */             Map.Entry<Integer, Map<String, Double>> ent = (Map.Entry)it.next();
/* 279 */             if ((getMax(((Map)ent.getValue()).values()) > thres[j]) && (((Map)ent.getValue()).size() >= cnt_thresh)) {
/* 280 */               poly.add((Integer)ent.getKey());
/*     */             }
/*     */           }
/* 283 */           Set intersection = new HashSet(real);
/* 284 */           intersection.retainAll(poly);
/* 285 */           double sens = intersection.size() / real.size();
/* 286 */           double fp = poly.size() - intersection.size();
/* 287 */           double spec = fp / poly.size();
/* 288 */           pw.println("site tp / true // fp / pred at " + thres[j] + " % " + sens + " " + spec);
/*     */         }
/*     */       }
/*     */       
/*     */       public ArrayList<Integer[][]> viterbi(PairMarkovModel hmm) throws Exception
/*     */       {
/* 294 */         PrintWriter hap = new PrintWriter(new BufferedWriter(new FileWriter(clusterFile)));
/* 295 */         hap.println(Format.sprintf(sampler.getPrintString(), RunFastPhase.num));
/* 296 */         ArrayList<Integer[][]> res = sampler.calcBestPathViterbi(hap, hmm);
/* 297 */         hap.close();
/* 298 */         return res;
/*     */       }
/*     */       
/*     */       public Double[][] forwardBackward(PairMarkovModel hmm) throws Exception
/*     */       {
/* 303 */         return sampler.calcBestPathForwardBackward(hmm);
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static void run(DataCollection obj, PrintWriter summary, File clusters, File parentfile)
/*     */     throws Exception
/*     */   {
/* 313 */     summary.println(cal.getTime().toString().toUpperCase() + "------------------------------");
/* 314 */     long time = System.currentTimeMillis();
/* 315 */     summary.println(Constants.numItExp() + " " + Constants.numRep() + " " + Constants.numF());
/* 316 */     DataCollection original_with_missing = new DataCollection(obj);
/* 317 */     original_with_missing.modify();
/*     */     
/* 319 */     DataCollection copy = new DataCollection(original_with_missing);
/* 320 */     if (Constants.noCopies()[1] == 1.0D) {
/* 321 */       FastPhaseFormat.runFastphase(copy, obj, original_with_missing, Constants.numF(), Constants.numRep(), Constants.numItExp() + Constants.numItFree(), summary);
/* 322 */       logger.info("finished in " + (System.currentTimeMillis() - time));
/*     */     }
/*     */     
/*     */ 
/* 326 */     Sampler sampler = new Sampler(copy);
/* 327 */     ProgressMonitor monitor = getMonitor(sampler, obj, original_with_missing, summary, clusters, parentfile);
/*     */     
/* 329 */     Fastphase fp = new Fastphase(copy, sampler, Constants.numRep(), parentfile);
/*     */     
/* 331 */     fp.train(fp.getTrainingElements(new HaplotypeHMMIterator(obj.length(), Constants.numRep())));
/* 332 */     monitor.monitor("no switching");
/* 333 */     sampler.finalise();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 338 */     logger.info("finished in " + (System.currentTimeMillis() - time));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private static void add(int[] d, int[] toAdd)
/*     */   {
/* 347 */     for (int i = 0; i < d.length; i++) {
/* 348 */       d[i] += toAdd[i];
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void compare(Iterator<PhasedIntegerGenotypeData> data, Iterator<PhasedIntegerGenotypeData> orig_no_missing, Iterator<PhasedIntegerGenotypeData> orig_with_missing, String origin, Iterator<double[]> unc, double thresh, PrintWriter pw, Collection<Integer> nocopies)
/*     */   {
/* 358 */     logger.info("comparing ...");
/* 359 */     int[] switch_gap = new int[2];
/* 360 */     int[] switch_no_gap = new int[2];
/* 361 */     int[] missing = new int[6];
/* 362 */     ErrorClassification classif = new ErrorClassification(Arrays.asList(HaplotypeEmissionState.stateSpace), thresh);
/* 363 */     for (int i = 0; data.hasNext(); i++) {
/* 364 */       PhasedIntegerGenotypeData inference = (PhasedIntegerGenotypeData)data.next();
/* 365 */       if (nocopies.contains(Integer.valueOf(inference.noCopies()))) {
/* 366 */         PhasedIntegerGenotypeData original_no_missing = (PhasedIntegerGenotypeData)orig_no_missing.next();
/* 367 */         PhasedIntegerGenotypeData original_with_missing = (PhasedIntegerGenotypeData)orig_with_missing.next();
/* 368 */         add(switch_gap, inference.phaseCorrect(original_no_missing, true));
/* 369 */         add(switch_no_gap, inference.phaseCorrect(original_no_missing, false));
/* 370 */         double[] nxt = unc == null ? null : (double[])unc.next();
/* 371 */         if (Constants.fillGaps()) {
/* 372 */           classif.compare(original_no_missing, original_with_missing, inference, nxt);
/* 373 */           add(missing, inference.genotypeCorrect(original_no_missing, original_with_missing, nxt, thresh));
/*     */         }
/*     */       } }
/* 376 */     pw.println("comparing with " + origin);
/*     */     
/* 378 */     pw.println("phasing accuracy");
/* 379 */     Integer[] res = { Integer.valueOf(switch_gap[0]), Integer.valueOf(switch_gap[1]), Integer.valueOf(switch_no_gap[0]), Integer.valueOf(switch_no_gap[1]) };
/* 380 */     pw.println("perc " + Format.sprintf("%5i : %5i // %5i  : %5i", res));
/* 381 */     pw.println("perc " + Format.sprintf("%5.3g : %5.3g", new Double[] { Double.valueOf(res[0].intValue() / res[1].intValue()), Double.valueOf(res[2].intValue() / res[3].intValue()) }));
/*     */     
/* 383 */     if (Constants.fillGaps()) {
/* 384 */       pw.println("missing data accuracy");
/* 385 */       pw.println("'-' called as '?' : total '-' //'?' called as '-' : total '?' // inferred ? with correct genotype  : total real '?'");
/*     */       
/*     */ 
/* 388 */       Integer[] res = { Integer.valueOf(missing[0]), Integer.valueOf(missing[1]), Integer.valueOf(missing[2]), Integer.valueOf(missing[3]), Integer.valueOf(missing[4]), Integer.valueOf(missing[5]) };
/* 389 */       pw.println("perc " + Format.sprintf("%5i:%5i // %5i:%5i // %5i:%5i", res));
/* 390 */       classif.print(pw);
/*     */     }
/* 392 */     pw.flush();
/* 393 */     logger.info("... done comparing");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static void compareBin(Iterator<PhasedIntegerGenotypeData> data, Iterator<PhasedIntegerGenotypeData> orig, String origin, double[][] certainty, PrintWriter pw)
/*     */   {
/* 401 */     double[] upperB = { 0.4D, 0.3D, 0.2D, 0.1D, 0.0D };
/* 402 */     int[] switchWrong = new int[upperB.length];
/* 403 */     int[] switchRight = new int[upperB.length];
/* 404 */     int[] missingRight = new int[upperB.length];
/* 405 */     int[] missingWrong = new int[upperB.length];
/* 406 */     double[] bu = (double[])null;
/* 407 */     for (int i = 0; data.hasNext(); i++) {
/* 408 */       PhasedIntegerGenotypeData nxt = (PhasedIntegerGenotypeData)data.next();
/* 409 */       PhasedIntegerGenotypeData nxtO = (PhasedIntegerGenotypeData)orig.next();
/* 410 */       double[] cert = (double[])null;
/* 411 */       if (certainty == null) {
/* 412 */         if (bu == null) {
/* 413 */           bu = new double[nxt.length()];
/* 414 */           Arrays.fill(bu, 1.0D);
/*     */         }
/* 416 */         cert = bu;
/*     */       }
/*     */       else {
/* 419 */         cert = certainty[i];
/*     */       }
/* 421 */       int[][] res = nxt.compareBin(nxtO, true, cert, upperB);
/* 422 */       int[][] res1 = nxt.compareBin(nxtO, false, cert, upperB);
/* 423 */       for (int j = 0; j < res[0].length; j++) {
/* 424 */         switchWrong[j] += res[0][j];
/* 425 */         switchRight[j] += res[1][j];
/* 426 */         missingWrong[j] += res1[0][j];
/* 427 */         missingRight[j] += res1[1][j];
/*     */       }
/*     */     }
/* 430 */     pw.println("comparing with " + origin);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 439 */     Double[] res = { Double.valueOf(sum(switchWrong)), Double.valueOf(sum(switchWrong) + sum(switchRight)), 
/* 440 */       Double.valueOf(sum(missingWrong)), Double.valueOf(sum(missingWrong) + sum(missingRight)) };
/* 441 */     pw.println("perc " + Format.sprintf("%5.3f  %5.3f %5.3f %5.3f", res));
/*     */   }
/*     */   
/*     */   private static double sum(int[] switchWrong)
/*     */   {
/* 446 */     double sum = 0.0D;
/* 447 */     for (int i = 0; i < switchWrong.length; i++) {
/* 448 */       sum += switchWrong[i];
/*     */     }
/* 450 */     return sum;
/*     */   }
/*     */   
/*     */   public static List<HaplotypeData> extractUnpaired(List<HaplotypeData> data)
/*     */   {
/* 455 */     List<HaplotypeData> newData = new ArrayList();
/* 456 */     int len = (int)Math.round(data.size() / 2.0D);
/* 457 */     for (int j = 0; j < len; j++) {
/* 458 */       if (((HaplotypeData)data.get(2 * j + 1)).allNull()) newData.add((HaplotypeData)data.get(2 * j));
/*     */     }
/* 460 */     return newData;
/*     */   }
/*     */   
/*     */   public static void removeIdents(List<PhasedIntegerGenotypeData> l) {
/* 464 */     Double[] maf = FindLines.getMaf(new List[] { l }, ((PhasedIntegerGenotypeData)l.get(0)).length());
/* 465 */     for (int i = maf.length - 1; i >= 0; i--) {
/* 466 */       if (maf[i].doubleValue() == 0.0D) {
/* 467 */         logger.info("removing " + i);
/* 468 */         for (int j = 0; j < l.size(); j++) {
/* 469 */           ((PhasedIntegerGenotypeData)l.get(j)).remove(i);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   static Boolean get(String st) {
/* 476 */     if (st.equals("-")) return null;
/* 477 */     int i = Integer.parseInt(st);
/* 478 */     if (i == 0) return zero;
/* 479 */     if (i == 1) return one;
/* 480 */     throw new RuntimeException("!!"); }
/*     */   
/* 482 */   static Boolean one = new Boolean(true); static Boolean zero = new Boolean(false);
/*     */   
/*     */   public static List<HaplotypeData> readPhasedData(File f, double missing, int noInd, int noMarkers) throws Exception
/*     */   {
/* 486 */     BufferedReader br = new BufferedReader(new FileReader(f));
/* 487 */     String top = "";
/* 488 */     List<HaplotypeData> list = new ArrayList();
/* 489 */     for (int count = 0; ((top = br.readLine()) != null) && (count < noInd); count++) {
/* 490 */       String[] t = top.split("\\s+");
/* 491 */       HaplotypeData data = new HaplotypeData(count, t.length);
/* 492 */       for (int i = 0; (i < t.length) && (i < noMarkers); i++) {
/* 493 */         data.addPoint(get(t[i]));
/*     */       }
/* 495 */       data.addMissingData(missing, Constants.addError());
/* 496 */       list.add(data);
/*     */     }
/* 498 */     br.close();
/*     */     
/* 500 */     return list;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/RunFastPhase.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */