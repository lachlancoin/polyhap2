/*     */ package lc1.dp.states;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import lc1.dp.data.representation.CSOData;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpaceTranslation;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.stats.SimpleDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.TrainableNormal;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public abstract class EmissionState
/*     */   extends State
/*     */ {
/*  30 */   static double round = ;
/*     */   
/*     */   protected final SimpleDistribution lengthDistrib;
/*     */   
/*     */   public SkewNormal[] probR()
/*     */   {
/*  36 */     return null;
/*     */   }
/*     */   
/*  39 */   public IlluminaProbB[] probB() { return null; }
/*     */   
/*     */   public Integer noCop() {
/*  42 */     return null;
/*     */   }
/*     */   
/*  45 */   static Map<String, SkewNormal[]> m = new HashMap();
/*     */   public double[] distribution;
/*     */   
/*  48 */   public static SkewNormal make(double[] meanvarskew, double prior_mod) { double mean_i = meanvarskew[0];
/*  49 */     double var_i = meanvarskew[1];
/*  50 */     double skew_i = meanvarskew[2];
/*  51 */     double[] lower = { -5.0D, 0.001D, -1.0E10D };
/*  52 */     double[] upper = { 5.0D, 1000.0D, 1.0E10D };
/*     */     
/*  54 */     return skew_i == 0.0D ? new TrainableNormal(mean_i, var_i, round, prior_mod) : 
/*  55 */       new SkewNormal(mean_i, var_i, 
/*  56 */       skew_i, lower, upper, round, prior_mod);
/*     */   }
/*     */   
/*     */   public static SkewNormal[] getProbRGroup(int noCop, double bg, double[][] meanvarskew, double prior_mod)
/*     */   {
/*  61 */     double[] x = Constants.r_x();
/*  62 */     for (int i = 0; i < x.length; i++) {
/*  63 */       if (Math.abs(noCop / bg - x[i]) < 1.0E-4D) {
/*  64 */         SkewNormal[] res = new SkewNormal[meanvarskew.length];
/*  65 */         res[0] = make(meanvarskew[0], prior_mod);
/*     */         
/*  67 */         if (noCop == 1) {
/*  68 */           if (m.containsKey(res[0].name())) {
/*  69 */             return (SkewNormal[])m.get(res[0].name());
/*     */           }
/*     */           
/*     */ 
/*  73 */           for (int i1 = 1; i1 < res.length; i1++) {
/*  74 */             res[i1] = make(meanvarskew[i1], prior_mod);
/*     */           }
/*  76 */           m.put(res[0].name(), res);
/*  77 */           return res;
/*     */         }
/*     */         
/*     */ 
/*  81 */         for (int i1 = 1; i1 < res.length; i1++) {
/*  82 */           res[i1] = make(meanvarskew[i1], prior_mod);
/*     */         }
/*  84 */         return res;
/*     */       }
/*     */     }
/*     */     
/*  88 */     return null;
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int noSnps();
/*     */   
/*     */ 
/*     */   public abstract EmissionStateSpace getEmissionStateSpace();
/*     */   
/*     */ 
/*     */   public EmissionState(String name, int adv)
/*     */   {
/* 100 */     super(name, adv);
/* 101 */     this.lengthDistrib = new SimpleDistribution(new int[] { adv }, new double[] { 1.0D });
/*     */   }
/*     */   
/*     */   public synchronized void addCountSynch(int obj_index, double value, int i) {
/* 105 */     addCount(obj_index, value, i);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void addCount(int paramInt1, double paramDouble, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void addCountDT(double paramDouble1, int paramInt1, double paramDouble2, int paramInt2);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public SimpleDistribution adv(State st)
/*     */   {
/* 124 */     return this.lengthDistrib;
/*     */   }
/*     */   
/*     */   public EmissionState(EmissionState st1) {
/* 128 */     this(st1.name, st1.adv);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract void print(PrintWriter paramPrintWriter, String paramString, List<Integer> paramList);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseCounts()
/*     */   {
/* 143 */     IlluminaProbB[] prob = probB();
/* 144 */     for (int i = 0; i < prob.length; i++) {
/* 145 */       prob[i].initialiseBCounts();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int sample(int paramInt);
/*     */   
/*     */   public abstract double score(int paramInt1, int paramInt2);
/*     */   
/*     */   public abstract double[] getEmiss(int paramInt);
/*     */   
/*     */   public String toString(int i)
/*     */   {
/* 158 */     return "";
/*     */   }
/*     */   
/* 161 */   public String toString() { return getName(); }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract int mostLikely(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean transferCountsToProbs(double pseudo)
/*     */   {
/* 175 */     if ((Constants.b_train() < 1.0E7D) || (Constants.r_train() < 1.0E7D)) return true;
/* 176 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public abstract Integer getFixedInteger(int paramInt);
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double score(EmissionState hmm_state, int i_data, int stSpSize)
/*     */   {
/* 191 */     throw new RuntimeException("!!");
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
/*     */   public final int getBestIndex(int i)
/*     */   {
/* 216 */     Integer index1 = getFixedInteger(i);
/* 217 */     if (index1 != null) return index1.intValue();
/* 218 */     return Constants.getMax(getEmiss(i));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void setFixedIndex(int i, int k)
/*     */   {
/* 225 */     throw new RuntimeException("wrong class " + getClass());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getBestIndex(EmissionState data_state, int i_hmm, boolean sample)
/*     */   {
/* 235 */     throw new RuntimeException("!!");
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
/*     */   protected double calcDistribution(EmissionState hmm_state, int i_data)
/*     */   {
/* 256 */     double sum = 0.0D;
/* 257 */     throw new RuntimeException("!!");
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
/*     */   public final int length()
/*     */   {
/* 280 */     return noSnps();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public final double[] score(EmissionState hmm_state, boolean logspace)
/*     */   {
/* 289 */     double[] score = new double[noSnps()];
/* 290 */     int len = getEmissionStateSpace().size();
/*     */     
/* 292 */     for (int i = 0; i < score.length; i++) {
/* 293 */       double sc = score(hmm_state, i, len);
/* 294 */       score[i] = (logspace ? Math.log(sc) : sc);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 299 */     return score;
/*     */   }
/*     */   
/* 302 */   public Double[] phenValue() { throw new RuntimeException("!!"); }
/*     */   
/*     */   public int dataIndex(int i) {
/* 305 */     return -1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCount(EmissionState hmm_state, Double double1, int i, double weight)
/*     */   {
/* 314 */     throw new RuntimeException("!!");
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
/*     */   public void addCountSynchDT(Double[] phenValue, double d, int i)
/*     */   {
/* 345 */     for (int k = 0; k < phenValue.length; k++) {
/* 346 */       if (phenValue[k] != null) {
/* 347 */         addCountDT(phenValue[k].doubleValue(), k, d, i);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public static HaplotypeEmissionState getEmissionState(CSOData obj, boolean replaceNWithUnknown, double pseudo, int[] se) {
/* 353 */     EmissionStateSpace emStSp = Emiss.getEmissionStateSpace(obj.noCopies() - 1);
/* 354 */     if (emStSp.copyNumber.size() == 1) {
/* 355 */       replaceNWithUnknown = true;
/*     */     }
/* 357 */     HaplotypeEmissionState hes = getEmissionState(obj, emStSp, replaceNWithUnknown, se);
/*     */     
/* 359 */     if (pseudo > 0.0D) {
/* 360 */       Map<Integer, List<Integer>> diffCNV = new HashMap();
/* 361 */       for (int j = 0; j < emStSp.defaultList.size(); j++) {
/* 362 */         int cn = emStSp.getCN(j);
/* 363 */         if (cn != 2) {
/* 364 */           List<Integer> l = (List)diffCNV.get(Integer.valueOf(cn));
/* 365 */           if (l == null) {
/* 366 */             diffCNV.put(Integer.valueOf(cn), l = new ArrayList());
/*     */           }
/*     */           
/* 369 */           l.add(Integer.valueOf(j));
/*     */         } }
/* 371 */       double[] incr = new double[emStSp.defaultList.size()];
/* 372 */       for (int j = 0; j < incr.length; j++) {
/* 373 */         int cn = emStSp.getCN(j);
/* 374 */         if (cn != 2) {
/* 375 */           List<Integer> l = (List)diffCNV.get(Integer.valueOf(cn));
/* 376 */           incr[j] = (pseudo / diffCNV.size() / l.size());
/*     */         } }
/* 378 */       if (se != null) {
/* 379 */         for (int i = se[0]; i < se[1]; i++) {
/* 380 */           SimpleExtendedDistribution dist = (SimpleExtendedDistribution)hes.emissions[i];
/* 381 */           double[] probs = dist.probs;
/*     */           
/*     */ 
/* 384 */           for (int j = 0; j < probs.length; j++) {
/* 385 */             probs[j] = (probs[j] * (1.0D - pseudo) + incr[j]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 392 */     return hes;
/*     */   }
/*     */   
/*     */   public static HaplotypeEmissionState getEmissionState(CSOData obj, EmissionStateSpace emStSp, boolean replaceNWithUnknown, int[] se) {
/* 396 */     boolean fixed = false;
/* 397 */     Integer n_index = emStSp.getFromString("");
/*     */     
/*     */ 
/*     */ 
/* 401 */     HaplotypeEmissionState hes = new HaplotypeEmissionState(obj.getName(), obj.length(), emStSp.size(), emStSp, null, null, 1.0D);
/* 402 */     for (int i = 0; i < obj.length(); i++) {
/* 403 */       fixed = (se == null) || (i < se[0]) || (i >= se[1]);
/* 404 */       double[] prob = new double[emStSp.size()];
/* 405 */       Arrays.fill(prob, 0.0D);
/* 406 */       Comparable comp = obj.getElement(i);
/* 407 */       Integer genoIndex = emStSp.getGenotype(comp);
/*     */       int[] indices;
/*     */       double[] weights;
/* 410 */       if ((replaceNWithUnknown) && ((genoIndex == null) || ((n_index != null) && (genoIndex == n_index)))) {
/* 411 */         int[] indices = emStSp.getGenoForCopyNo(obj.noCopies());
/* 412 */         double[] weights = new double[indices.length];
/* 413 */         Arrays.fill(weights, 1.0D / indices.length);
/*     */       }
/*     */       else {
/* 416 */         indices = emStSp.getGenotypeConsistent(genoIndex.intValue());
/* 417 */         weights = emStSp.getWeights(genoIndex.intValue());
/*     */       }
/* 419 */       if ((fixed) && (indices.length > 1)) fixed = false;
/* 420 */       for (int k = 0; k < indices.length; k++) {
/* 421 */         prob[indices[k]] = weights[k];
/*     */       }
/* 423 */       if ((indices.length > 1) || (!fixed)) {
/* 424 */         hes.setTheta(prob, i);
/*     */       }
/*     */       else {
/* 427 */         hes.setTheta(indices[0], i);
/*     */       }
/*     */     }
/* 430 */     return hes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static EmissionState getEmissionState(EmissionStateSpace emStSp, EmissionState st, EmissionStateSpaceTranslation trans)
/*     */   {
/* 437 */     return new WrappedEmissionState(st, emStSp, trans);
/*     */   }
/*     */   
/*     */   public static HaplotypeEmissionState getEmissionState(String name, int len, EmissionStateSpace emStSp, EmissionStateSpace subSp, double cn_ratio)
/*     */   {
/* 442 */     List<String> genoList = new ArrayList();
/* 443 */     for (int k = 0; k < subSp.getGenotypeList().size(); k++) {
/* 444 */       char[] ch = ((ComparableArray)subSp.getGenotype(k)).toStringShort().replaceAll("_", "").toCharArray();
/* 445 */       Arrays.sort(ch);
/* 446 */       genoList.add(new String(ch));
/*     */     }
/* 448 */     HaplotypeEmissionState hes = new HaplotypeEmissionState(name, len, emStSp.size(), emStSp, null, null, 1.0D);
/*     */     
/* 450 */     for (int i = 0; i < len; i++) {
/* 451 */       double[] prob = new double[emStSp.size()];
/* 452 */       Arrays.fill(prob, 0.0D);
/* 453 */       for (int k = 0; k < genoList.size(); k++) {
/* 454 */         String compa = (String)genoList.get(k);
/*     */         
/* 456 */         int genoIndex = emStSp.getFromString(compa).intValue();
/* 457 */         int cn = compa.length();
/* 458 */         int[] indices = emStSp.getGenotypeConsistent(genoIndex);
/* 459 */         double[] weights = emStSp.getWeights(genoIndex);
/* 460 */         double diff = cn - ((CompoundEmissionStateSpace)emStSp).noCopies();
/* 461 */         double val = diff == 0.0D ? cn_ratio : 1.0D;
/*     */         
/* 463 */         for (int k1 = 0; k1 < indices.length; k1++) {
/* 464 */           prob[indices[k1]] = (weights[k1] * val);
/*     */         }
/*     */       }
/* 467 */       SimpleExtendedDistribution.normalise(prob);
/* 468 */       hes.setTheta(prob, i);
/*     */     }
/*     */     
/*     */ 
/* 472 */     return hes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public static HaplotypeEmissionState getEmissionState(SimpleScorableObject obj, EmissionStateSpace emStSp, double soften)
/*     */   {
/* 479 */     HaplotypeEmissionState hes = new HaplotypeEmissionState(obj.getName(), obj.length(), emStSp.size(), emStSp, null, null, 1.0D);
/* 480 */     double incr = soften / emStSp.defaultList.size();
/* 481 */     for (int i = 0; i < obj.length(); i++) {
/* 482 */       double[] prob = emStSp.getArray((String)obj.getElement(i));
/* 483 */       if (soften > 0.0D) {
/* 484 */         double[] cp = new double[prob.length];
/* 485 */         System.arraycopy(prob, 0, cp, 0, prob.length);
/* 486 */         for (int k = 0; k < prob.length; k++) {
/* 487 */           prob[k] = (cp[k] * (1.0D - soften) + incr);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 492 */       hes.setTheta(prob, i);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 498 */     return hes;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EmissionState getEmissionState(EmissionState mother, EmissionState child, EmissionStateSpace emStSp)
/*     */   {
/* 509 */     return 
/* 510 */       new CachedEmissionState(
/* 511 */       new PairEmissionState(Arrays.asList(new EmissionState[] { mother, child }), 
/* 512 */       emStSp, true, mother.probB(), mother.probR()), emStSp.size());
/*     */   }
/*     */   
/*     */ 
/*     */   public static EmissionState getEmissionState(EmissionState father, EmissionState mother, EmissionState child, EmissionStateSpace emStSp)
/*     */   {
/* 518 */     return 
/* 519 */       new CachedEmissionState(
/* 520 */       new PairEmissionState(Arrays.asList(new EmissionState[] { father, mother, child }), 
/* 521 */       emStSp, true, father.probB(), mother.probR()), emStSp.size());
/*     */   }
/*     */   
/*     */   public static HaplotypeEmissionState getEmissionState(EmissionState mother, EmissionState child)
/*     */   {
/* 526 */     return (HaplotypeEmissionState)getEmissionState(mother, child, Emiss.getEmissionStateSpace(2));
/*     */   }
/*     */   
/* 529 */   public static EmissionState getEmissionState(EmissionState father, EmissionState mother, EmissionState child) { return getEmissionState(father, mother, child, Emiss.getEmissionStateSpace(5)); }
/*     */   
/*     */   public EmissionState[] split()
/*     */   {
/* 533 */     return ((CompoundState)this).getMemberStates(true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public static EmissionState getEmissionState(String name, EmissionStateSpace emStSp, int noSnps)
/*     */   {
/* 543 */     return null;
/*     */   }
/*     */   
/*     */   public PIGData getGenotypeData() {
/* 547 */     EmissionStateSpace stSp = getEmissionStateSpace();
/* 548 */     PIGData res = SimpleScorableObject.make(this.name, noSnps(), stSp);
/* 549 */     for (int i = 0; i < noSnps(); i++) {
/* 550 */       Comparable em = stSp.get(getBestIndex(i));
/* 551 */       res.addPoint(i, em);
/*     */     }
/* 553 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public int noCopies()
/*     */   {
/* 559 */     return getEmissionStateSpace().noCopies();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public Collection<Integer> getConstantPos()
/*     */   {
/* 566 */     Set<Integer> s = new HashSet();
/* 567 */     for (int i = 0; i < noSnps(); i++) {
/* 568 */       if (score(getBestIndex(i), i) > 0.9999D) s.add(Integer.valueOf(i));
/*     */     }
/* 570 */     return s;
/*     */   }
/*     */   
/* 573 */   public Collection<? extends Integer> getMultPos() { Set<Integer> s = new HashSet();
/* 574 */     for (int i = 0; i < noSnps(); i++) {
/* 575 */       if (getFixedInteger(i) == null) {
/* 576 */         int[] ind = Constants.getMax2(getEmiss(i));
/* 577 */         double sum = score(ind[0], i) + score(ind[1], i);
/* 578 */         if (sum < 0.99999D) s.add(Integer.valueOf(i));
/*     */       }
/*     */     }
/* 581 */     return s;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public abstract void reverse();
/*     */   
/*     */ 
/*     */   public String getUnderlyingData(int i)
/*     */   {
/* 591 */     return getBestIndex(i);
/*     */   }
/*     */   
/*     */ 
/*     */   public abstract int getParamIndex();
/*     */   
/*     */   public void switchAlleles(int i)
/*     */   {
/* 599 */     throw new RuntimeException("!! " + getClass());
/*     */   }
/*     */   
/* 602 */   public void fixLikelihood(boolean X, int i) { throw new RuntimeException("!!"); }
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
/*     */   public void removeAll(List<Integer> toDrop)
/*     */   {
/* 632 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */   public void setAsMissing(List<Integer> toDrop, double cn_ratio)
/*     */   {
/* 638 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void applyAlias(int[] alias)
/*     */   {
/* 645 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public void setProbR(SkewNormal probR1) {
/* 649 */     throw new RuntimeException("!!");
/*     */   }
/*     */   
/*     */   public double scoreEmiss(Double[] phenValue, int i) {
/* 653 */     throw new RuntimeException("!!");
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/states/EmissionState.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */