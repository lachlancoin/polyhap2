/*     */ package lc1.dp.genotype.io.scorable;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import lc1.dp.ComparableArrayComparator;
/*     */ import lc1.dp.PairEmissionState;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ 
/*     */ public abstract class CompoundScorableObject
/*     */   extends SimpleScorableObject
/*     */ {
/*  27 */   static Comparator comp = new ComparableArrayComparator();
/*  28 */   static Comparator countComp = new Comparator()
/*     */   {
/*     */     public int compare(Map.Entry<ComparableArray, Integer> o1, Map.Entry<ComparableArray, Integer> o2) {
/*  31 */       return ((Integer)o1.getValue()).compareTo((Integer)o2.getValue());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean allPairedNullStates()
/*     */   {
/*  40 */     for (int i = 0; i < length(); i++) {
/*  41 */       ComparableArray comp = (ComparableArray)getElement(i);
/*  42 */       boolean containsNull = false;
/*  43 */       boolean allNull = true;
/*  44 */       for (int j = 0; j < comp.size(); j++) {
/*  45 */         if (comp.get(j) == null) {
/*  46 */           containsNull = true;
/*     */         }
/*     */         else {
/*  49 */           allNull = false;
/*     */         }
/*     */       }
/*  52 */       if ((containsNull) && (!allNull)) return false;
/*     */     }
/*  54 */     return true;
/*     */   }
/*     */   
/*     */   public String getStringRep(int i)
/*     */   {
/*  59 */     ComparableArray comp = (ComparableArray)getElement(i);
/*  60 */     StringBuffer sb = new StringBuffer(toString(comp.get(0)));
/*  61 */     for (int j = 1; j < comp.size(); j++) {
/*  62 */       sb.append("_" + toString(comp.get(j)));
/*     */     }
/*  64 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public void addMissingData(double perc, double error) {
/*  68 */     for (int i = 0; i < length(); i++) {
/*  69 */       ComparableArray missing = (ComparableArray)getElement(i);
/*  70 */       for (int j = 0; j < missing.size(); j++) {
/*  71 */         double ra = Constants.rand.nextDouble();
/*  72 */         if (ra < perc) {
/*  73 */           missing.set(j, null);
/*     */         }
/*  75 */         else if (ra < error) {
/*  76 */           missing.set(j, ((Boolean)missing.get(j)).booleanValue() ? Boolean.FALSE : Boolean.TRUE);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public int noCopies() {
/*  83 */     return ((ComparableArray)getElement(0)).size();
/*     */   }
/*     */   
/*     */   protected CompoundScorableObject(String name, int noSites) {
/*  87 */     super(name, noSites, ComparableArray.class);
/*     */   }
/*     */   
/*     */   public abstract ComparableArray exampleSplit(Object paramObject, int paramInt);
/*     */   
/*     */   public Comparable copyElement(Comparable element) {
/*  93 */     ComparableArray el = (ComparableArray)element;
/*  94 */     ComparableArray res = new ComparableArray(el);
/*  95 */     return res;
/*     */   }
/*     */   
/*  98 */   public CompoundScorableObject(CompoundScorableObject data) { super(data); }
/*     */   
/*     */ 
/*     */ 
/*     */   public static String getName(ScorableObject[] unit)
/*     */   {
/* 104 */     StringBuffer buf = new StringBuffer(unit[0].getName());
/* 105 */     for (int i = 1; i < unit.length; i++) {
/* 106 */       buf.append("_");
/* 107 */       buf.append(unit[i].getName());
/*     */     }
/* 109 */     return buf.toString();
/*     */   }
/*     */   
/*     */   public CompoundScorableObject(CompoundScorableObject[] unit) {
/* 113 */     this(getName(unit), unit);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public CompoundScorableObject(String name, CompoundScorableObject[] unit)
/*     */   {
/* 120 */     this(name, unit[0].length());
/* 121 */     int noCopies = 0;
/* 122 */     for (int i = 0; i < unit.length; i++) {
/* 123 */       noCopies += unit[i].noCopies();
/*     */     }
/* 125 */     for (int i = 0; i < unit[0].length(); i++) {
/* 126 */       ComparableArray obj = new ComparableArray();
/* 127 */       int k = 0;
/* 128 */       for (int j = 0; j < unit.length; j++) {
/* 129 */         ComparableArray comp = (ComparableArray)unit[j].getElement(i);
/* 130 */         obj.addAll(comp);
/* 131 */         k += comp.size();
/*     */       }
/* 133 */       addPoint(obj);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   ComparableArray shuffle(ComparableArray input)
/*     */   {
/* 144 */     final ComparableArray l = new ComparableArray(input);
/* 145 */     ComparableArray res = new ComparableArray();
/* 146 */     Iterator<Comparable> it = new Iterator() {
/*     */       public void remove() {}
/*     */       
/* 149 */       public boolean hasNext() { return l.size() > 0; }
/*     */       
/*     */       public Comparable next() {
/* 152 */         return (Comparable)l.remove(Constants.nextInt(l.size()));
/*     */       }
/*     */     };
/* 155 */     while (it.hasNext()) {
/* 156 */       res.add((Comparable)it.next());
/*     */     }
/* 158 */     return res;
/*     */   }
/*     */   
/*     */   public void mix() {
/* 162 */     for (int i = 0; i < length(); i++) {
/* 163 */       set(i, shuffle((ComparableArray)getElement(i)));
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void set(int i, Comparable obj)
/*     */   {
/* 171 */     super.set(i, obj);
/*     */   }
/*     */   
/*     */ 
/*     */   public static int countDiff(ComparableArray obj1, ComparableArray obj2)
/*     */   {
/* 177 */     int cnt = 0;
/* 178 */     for (int i = 0; i < obj1.size(); i++) {
/* 179 */       if (obj1.get(i) == null) {
/* 180 */         if (obj2.get(i) != null) cnt++;
/*     */       }
/* 182 */       else if (!((Comparable)obj1.get(i)).equals(obj2.get(i))) cnt++;
/*     */     }
/* 184 */     return cnt;
/*     */   }
/*     */   
/*     */   public static boolean containsNull(ComparableArray val)
/*     */   {
/* 189 */     for (int i = 0; i < val.size(); i++) {
/* 190 */       if (val.get(i) == null) return true;
/*     */     }
/* 192 */     return false;
/*     */   }
/*     */   
/*     */   public static int countNull(ComparableArray val) {
/* 196 */     int count = 0;
/* 197 */     for (int i = 0; i < val.size(); i++) {
/* 198 */       if (val.get(i) == null) count++;
/*     */     }
/* 200 */     return count;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void samplePhase(List<CompoundScorableObject> spList, int i, int i1, Set<ComparableArray> poss)
/*     */   {
/* 211 */     ComparableArray em1 = (ComparableArray)getElement(i);
/* 212 */     ComparableArray em2 = (ComparableArray)getElement(i1);
/* 213 */     Integer[][] diff = new Integer[noCopies() + 1][];
/*     */     
/* 215 */     for (int k = 0; k < noCopies() + 1; k++) {
/* 216 */       diff[k] = { Integer.valueOf(0), Integer.valueOf(k) };
/*     */     }
/* 218 */     for (int j = 0; j < spList.size(); j++) {
/* 219 */       CompoundScorableObject data = (CompoundScorableObject)spList.get(j);
/* 220 */       ComparableArray c1 = (ComparableArray)data.getElement(i);
/* 221 */       if (PairEmissionState.isEqualGenotype(c1, em1)) {
/* 222 */         ComparableArray c2 = (ComparableArray)data.getElement(i1);
/* 223 */         if (PairEmissionState.isEqualGenotype(c2, em2)) {
/* 224 */           int tmp148_147 = 0; Integer[] tmp148_146 = diff[countDiff(c1, c2)];tmp148_146[tmp148_147] = Integer.valueOf(tmp148_146[tmp148_147].intValue() + 1);
/*     */         }
/*     */       } }
/* 227 */     Arrays.sort(diff, comp);
/* 228 */     for (int k = diff.length - 1; k >= 0; k--) {
/* 229 */       int numDiff = diff[k][1].intValue();
/* 230 */       int count = diff[k][0].intValue();
/* 231 */       if (count == 0) throw new RuntimeException("!!");
/* 232 */       List<ComparableArray> l1 = new ArrayList();
/* 233 */       for (Iterator<ComparableArray> it = poss.iterator(); it.hasNext();) {
/* 234 */         ComparableArray next = (ComparableArray)it.next();
/* 235 */         l1.add(next);
/* 236 */         if (countDiff(em2, next) != numDiff) it.remove();
/*     */       }
/* 238 */       if (poss.size() == 0) {
/* 239 */         poss.addAll(l1);
/*     */       }
/*     */       else {
/* 242 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private void phaseRelative(CompoundScorableObject original, int i, int i1, Set<ComparableArray> poss)
/*     */   {
/* 251 */     ComparableArray emission = (ComparableArray)getElement(i);
/* 252 */     ComparableArray emission1 = (ComparableArray)getElement(i1);
/* 253 */     ComparableArray c1 = (ComparableArray)original.getElement(i);
/* 254 */     ComparableArray c2 = (ComparableArray)original.getElement(i1);
/* 255 */     int noDiff = countDiff(c1, c2);
/* 256 */     for (Iterator<ComparableArray> it = poss.iterator(); it.hasNext();) {
/* 257 */       if (countDiff((ComparableArray)it.next(), emission1) != noDiff) { it.remove();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public int[] phaseCorrect(CompoundScorableObject original, boolean includeGaps)
/*     */   {
/* 267 */     SortedSet<ComparableArray> poss = new TreeSet();
/* 268 */     int correct = 0;
/* 269 */     int wrong = 0;
/* 270 */     Integer[] phasePos = updatePhasePositions(includeGaps);
/*     */     
/* 272 */     for (int jk = 0; jk < phasePos.length; jk++) {
/* 273 */       int i = phasePos[jk].intValue();
/* 274 */       ComparableArray emiss = (ComparableArray)getElement(i);
/*     */       
/*     */ 
/*     */ 
/* 278 */       if (PairEmissionState.isEqualGenotype(emiss, (ComparableArray)original.getElement(i)))
/*     */       {
/*     */ 
/*     */ 
/* 282 */         poss.clear();
/* 283 */         poss.addAll(PairEmissionState.decompose(emiss, noCopies()));
/* 284 */         for (int jk1 = jk - 1; jk1 >= 0; jk1--) {
/* 285 */           int i1 = phasePos[jk1].intValue();
/* 286 */           ComparableArray emiss1 = (ComparableArray)getElement(i1);
/* 287 */           if (PairEmissionState.isEqualGenotype(emiss1, (ComparableArray)original.getElement(i1)))
/*     */           {
/* 289 */             phaseRelative(original, i, i1, poss);
/* 290 */             if (!poss.contains((ComparableArray)getElement(phasePos[jk].intValue()))) {
/* 291 */               wrong++;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 306 */               correct++;
/*     */               
/* 308 */               if (poss.size() == 1) break;
/*     */             }
/*     */           }
/*     */         } } }
/* 312 */     return new int[] { wrong, correct + wrong };
/*     */   }
/*     */   
/*     */   public void samplePhase(List<CompoundScorableObject> spList)
/*     */   {
/* 317 */     SortedSet<ComparableArray> poss = new TreeSet();
/* 318 */     Integer[] phasePos = updatePhasePositions(true);
/* 319 */     for (int jk = 0; jk < phasePos.length; jk++) {
/* 320 */       int i = phasePos[jk].intValue();
/* 321 */       ComparableArray emiss = (ComparableArray)getElement(i);
/* 322 */       poss.clear();
/* 323 */       poss.addAll(PairEmissionState.decompose(emiss, noCopies()));
/* 324 */       for (int jk1 = jk - 1; (jk1 >= 0) && (poss.size() > 1); jk1--) {
/* 325 */         int i1 = phasePos[jk1].intValue();
/* 326 */         samplePhase(spList, i, i1, poss);
/*     */       }
/* 328 */       if (poss.size() > 0) {
/* 329 */         set(i, (Comparable)poss.first());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public Integer[] updatePhasePositions(boolean inclGaps)
/*     */   {
/* 337 */     List<Integer> l = new ArrayList();
/* 338 */     for (int i = 0; i < length(); i++) {
/* 339 */       ComparableArray comp = (ComparableArray)getElement(i);
/* 340 */       if ((!containsNull(comp)) || (inclGaps)) {
/* 341 */         for (int j = 1; j < comp.size(); j++) {
/* 342 */           if (comp.get(j) != comp.get(0)) {
/* 343 */             l.add(Integer.valueOf(i));
/* 344 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 349 */     return (Integer[])l.toArray(new Integer[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   public ComparableArray makeGapType(ComparableArray dat)
/*     */   {
/* 355 */     ComparableArray dat1 = new ComparableArray();
/* 356 */     for (int i = 0; i < dat.size(); i++) {
/* 357 */       if (dat.get(i) == null) {
/* 358 */         dat1.add(Boolean.TRUE);
/*     */       } else
/* 360 */         dat1.add(Boolean.FALSE);
/*     */     }
/* 362 */     return dat1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int[] genotypeCorrect(CompoundScorableObject orig_no_missing, CompoundScorableObject orig_with_missing, double[] certainty, double thresh)
/*     */   {
/* 369 */     int[] correct = new int[3];
/* 370 */     int[] wrong = new int[3];
/* 371 */     for (int i = 0; i < length(); i++) {
/* 372 */       if ((certainty == null) || (certainty[i] >= thresh))
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 377 */         if (containsNull((ComparableArray)orig_with_missing.getElement(i)))
/*     */         {
/* 379 */           ComparableArray original_no_missing = (ComparableArray)orig_no_missing.getElement(i);
/* 380 */           ComparableArray original_with_missing = (ComparableArray)orig_with_missing.getElement(i);
/* 381 */           ComparableArray dat = (ComparableArray)getElement(i);
/* 382 */           boolean equalGapType = PairEmissionState.isEqualGenotype(makeGapType(dat), makeGapType(original_no_missing));
/* 383 */           boolean equalGenotype = PairEmissionState.isEqualGenotype(dat, original_no_missing);
/* 384 */           int original_gap = countNull(original_no_missing);
/* 385 */           int original_with_missing_gap = countNull(original_with_missing);
/* 386 */           if (equalGapType) {
/* 387 */             if (original_gap > 0) correct[0] += 1; else {
/* 388 */               correct[1] += 1;
/*     */             }
/*     */             
/*     */           }
/* 392 */           else if (original_gap > 0) {
/* 393 */             wrong[0] += 1;
/*     */           } else {
/* 395 */             wrong[1] += 1;
/*     */           }
/*     */           
/* 398 */           if (original_with_missing_gap > original_gap) {
/* 399 */             if (equalGenotype) {
/* 400 */               correct[2] += 1;
/*     */             }
/*     */             else {
/* 403 */               wrong[2] += 1;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 409 */     return new int[] { wrong[0], correct[0] + wrong[0], wrong[1], correct[1] + wrong[1], wrong[2], correct[2] + wrong[2] };
/*     */   }
/*     */   
/*     */   private int maxIndex(int[] d) {
/* 413 */     int maxIndex = 0;
/* 414 */     for (int i = 1; i < d.length; i++) {
/* 415 */       if (d[i] > d[maxIndex]) { maxIndex = i;
/*     */       }
/*     */     }
/* 418 */     return maxIndex;
/*     */   }
/*     */   
/*     */   private int countGaps(ComparableArray exemplar) {
/* 422 */     int cnt_gaps = 0;
/* 423 */     for (int k = 0; k < exemplar.size(); k++) {
/* 424 */       if (exemplar.get(k) == null) cnt_gaps++;
/*     */     }
/* 426 */     return cnt_gaps;
/*     */   }
/*     */   
/*     */   public void sampleGenotype(List<CompoundScorableObject> spList, double[] certainty) {
/* 430 */     Map<ComparableArray, Integer> m = new TreeMap();
/* 431 */     for (int i = 0; i < length(); i++) {
/* 432 */       m.clear();
/* 433 */       for (int j = 0; j < spList.size(); j++) {
/* 434 */         CompoundScorableObject obj = (CompoundScorableObject)spList.get(j);
/* 435 */         ComparableArray exemplar = (ComparableArray)PairEmissionState.decompose((ComparableArray)obj.getElement(i), noCopies()).get(0);
/* 436 */         Integer sc = (Integer)m.get(exemplar);
/* 437 */         m.put(exemplar, Integer.valueOf(sc == null ? 0 : sc.intValue() + 1));
/*     */       }
/* 439 */       List<Map.Entry<ComparableArray, Integer>> l = new ArrayList((Collection)m.entrySet());
/* 440 */       Collections.sort(l, countComp);
/* 441 */       Map.Entry<ComparableArray, Integer> entry = (Map.Entry)l.get(l.size() - 1);
/* 442 */       certainty[i] = (((Integer)entry.getValue()).intValue() / spList.size());
/* 443 */       ComparableArray res = (ComparableArray)entry.getKey();
/* 444 */       set(i, res);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public int[][] compareBin(CompoundScorableObject csoData, boolean inclGaps, double[] certainty, double[] lowerBError)
/*     */   {
/* 451 */     ComparableArray lastData = null;
/* 452 */     ComparableArray lastPred = null;
/* 453 */     int[] switchWrong = new int[lowerBError.length];
/* 454 */     int[] switchRight = new int[lowerBError.length];
/*     */     
/*     */ 
/* 457 */     if (csoData.length() != length()) throw new RuntimeException("!!");
/* 458 */     for (int i = 0; i < csoData.length(); i++) {
/* 459 */       ComparableArray pred = (ComparableArray)getElement(i);
/* 460 */       if ((pred != null) && 
/* 461 */         (!equal(pred))) {
/* 462 */         ComparableArray data = (ComparableArray)csoData.getElement(i);
/* 463 */         if ((data != null) && (
/* 464 */           (!containsNull(data)) || (inclGaps)))
/*     */         {
/* 466 */           if (lastData != null) {
/* 467 */             double cert = certainty[i];
/* 468 */             int j = 0;
/* 469 */             for (j = 0; j < lowerBError.length; j++)
/* 470 */               if (cert <= 1.0D - lowerBError[j])
/*     */                 break;
/* 472 */             int sameData = countDiff(data, lastData);
/* 473 */             int samePred = countDiff(pred, lastPred);
/* 474 */             if (sameData == samePred) switchRight[j] += 1; else {
/* 475 */               switchWrong[j] += 1;
/*     */             }
/*     */           }
/* 478 */           lastData = new ComparableArray(data);
/* 479 */           lastPred = new ComparableArray(pred);
/*     */         }
/*     */       } }
/* 482 */     return new int[][] { switchWrong, switchRight };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static boolean equal(ComparableArray pred)
/*     */   {
/* 489 */     for (int j = 1; j < pred.size(); j++)
/* 490 */       if (pred.get(0) != pred.get(j)) {
/* 491 */         if ((pred.get(0) == null) || (pred.get(j) == null)) return false;
/* 492 */         if (!((Comparable)pred.get(j)).equals(pred.get(0))) return false;
/*     */       }
/* 494 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract char toString(Object paramObject);
/*     */   
/*     */ 
/*     */ 
/* 503 */   public void print(PrintWriter pw) { print(pw, true); }
/*     */   
/*     */   public void print(PrintWriter pw, boolean idline) {
/* 506 */     if (idline) pw.println("# id " + getName());
/* 507 */     List<StringBuffer> l = new ArrayList();
/* 508 */     for (int i = 0; i < length(); i++) {
/* 509 */       ComparableArray obj = (ComparableArray)getElement(i);
/* 510 */       for (int j = 0; j < obj.size(); j++) {
/* 511 */         if (l.size() <= j) {
/* 512 */           StringBuffer sb = new StringBuffer();
/* 513 */           for (int i1 = 0; i1 < i; i1++) {
/* 514 */             sb.append(' ');
/*     */           }
/* 516 */           l.add(sb);
/*     */         }
/* 518 */         StringBuffer sb = (StringBuffer)l.get(j);
/* 519 */         sb.append(toString(obj.get(j)));
/*     */       }
/*     */     }
/*     */     
/* 523 */     for (Iterator<StringBuffer> it = l.iterator(); it.hasNext();) {
/* 524 */       StringBuffer sb = (StringBuffer)it.next();
/* 525 */       pw.println(sb.toString());
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean equals(Object o1, Object o2)
/*     */   {
/* 531 */     if (o1 == o2) { return true;
/*     */     }
/*     */     
/* 534 */     return false;
/*     */   }
/*     */   
/*     */   public static Map getCounts(ComparableArray obj1) {
/* 538 */     Map<Comparable, Integer> m = new HashMap();
/* 539 */     for (int i = 0; i < obj1.size(); i++) {
/* 540 */       Integer count = (Integer)m.get(obj1.get(i));
/* 541 */       m.put((Comparable)obj1.get(i), Integer.valueOf(count == null ? 1 : count.intValue() + 1));
/*     */     }
/* 543 */     return m;
/*     */   }
/*     */   
/*     */   private boolean consistent(ComparableArray obj1, ComparableArray obj2) {
/* 547 */     return getCounts(obj1).equals(getCounts(obj2));
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/io/scorable/CompoundScorableObject.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */