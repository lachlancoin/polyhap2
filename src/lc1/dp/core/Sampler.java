/*     */ package lc1.dp.core;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.BufferedWriter;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.FileReader;
/*     */ import java.io.FileWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.collection.DataC;
/*     */ import lc1.dp.data.collection.DataCollection;
/*     */ import lc1.dp.data.collection.SimpleDataCollection;
/*     */ import lc1.dp.data.representation.CSOData;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.data.representation.Emiss;
/*     */ import lc1.dp.data.representation.PIGData;
/*     */ import lc1.dp.data.representation.SimpleScorableObject;
/*     */ import lc1.dp.emissionspace.CompoundEmissionStateSpace;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.genotype.io.trio.TrComparableArray;
/*     */ import lc1.dp.genotype.io.trio.TrioComparableArray;
/*     */ import lc1.dp.model.CachedHMM;
/*     */ import lc1.dp.model.CompoundMarkovModel;
/*     */ import lc1.dp.model.WrappedModel;
/*     */ import lc1.dp.states.CompoundState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.PhasedDataState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.IntegerDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Sampler
/*     */ {
/*     */   public DataC data;
/*     */   final String sb_i1;
/*     */   static final boolean buffer1 = false;
/*     */   static final boolean buffer = false;
/*     */   public File directory;
/*  59 */   Map<String, List<CSOData>[]> spList = null;
/*     */   
/*     */   public void removeSamples() {
/*  62 */     File[] sampleDir = this.directory.listFiles();
/*  63 */     for (int i = 0; i < sampleDir.length; i++)
/*  64 */       if (!Constants.resample()) {
/*  65 */         deleteDir(sampleDir[i]);
/*  66 */         if (sampleDir[i].exists()) throw new RuntimeException("exists!");
/*     */       }
/*     */   }
/*     */   
/*     */   private void setDirectory(String str) {
/*  71 */     this.directory = new File(Constants.getDirFile(), str);
/*  72 */     this.directory.mkdir();
/*  73 */     if (!this.directory.exists()) this.directory.mkdir();
/*  74 */     removeSamples();
/*     */   }
/*     */   
/*     */   public List<CSOData>[] getBuffer(String key)
/*     */   {
/*  79 */     if (this.spList.containsKey(key)) {
/*  80 */       return (List[])this.spList.get(key);
/*     */     }
/*     */     
/*  83 */     List[] l = {
/*  84 */       new ArrayList(), 
/*  85 */       new ArrayList() };
/*     */     
/*  87 */     this.spList.put(key, l);
/*  88 */     return l;
/*     */   }
/*     */   
/*     */   public static PrintWriter[] getOutputStream(File directory, String key) throws Exception {
/*  92 */     PrintWriter[] res = new PrintWriter[2];
/*  93 */     for (int i1 = 0; i1 < res.length; i1++) {
/*  94 */       File f = new File(directory, (i1 == 0 ? "hap_" : "states_") + key);
/*  95 */       res[i1] = new PrintWriter(new BufferedOutputStream(new FileOutputStream(f)));
/*     */     }
/*  97 */     return res;
/*     */   }
/*     */   
/* 100 */   public static BufferedReader[] getInputStream(File directory, String key) throws Exception { BufferedReader[] res = new BufferedReader[2];
/* 101 */     res = new BufferedReader[2];
/* 102 */     for (int i1 = 0; i1 < res.length; i1++) {
/* 103 */       File f = new File(directory, (i1 == 0 ? "hap_" : "states_") + key);
/* 104 */       res[i1] = new BufferedReader(new FileReader(f));
/*     */     }
/*     */     
/* 107 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public Sampler(DataCollection data, String dirname)
/*     */   {
/* 113 */     this.data = data;
/* 114 */     setDirectory(dirname);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 119 */     StringBuffer sb_is1 = new StringBuffer();
/* 120 */     for (int i = 0; i < data.length(); i++) {
/* 121 */       sb_is1.append("%1i");
/*     */     }
/* 123 */     this.sb_i1 = sb_is1.toString(); }
/*     */   
/* 125 */   static Comparator entryComp = new Comparator()
/*     */   {
/*     */     public int compare(Map.Entry<List<Comparable>, Integer> o1, Map.Entry<List<Comparable>, Integer> o2) {
/* 128 */       return ((Integer)o1.getValue()).compareTo((Integer)o2.getValue());
/*     */     }
/*     */   };
/*     */   
/*     */ 
/*     */   public String getPrintString()
/*     */   {
/* 135 */     return this.sb_i1;
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
/*     */   public static void sample(StatePath sp, CompoundMarkovModel hmm, boolean sample, PhasedDataState[] states)
/*     */   {
/* 149 */     CompoundEmissionStateSpace emStSp = (CompoundEmissionStateSpace)((EmissionState)hmm.getState(1)).getEmissionStateSpace();
/*     */     
/*     */ 
/* 152 */     PhasedDataState result_phased = states[0];
/* 153 */     PhasedDataState result_switches = states[1];
/* 154 */     EmissionState[] prev = (EmissionState[])null;
/* 155 */     EmissionState[] prev_true = (EmissionState[])null;
/* 156 */     for (int i = 0; i < sp.size(); i++) {
/* 157 */       StatePath.StatePosEmission spe = sp.getSPE(i);
/* 158 */       CompoundState state_i = (CompoundState)spe.state;
/* 159 */       if (spe.pos.intValue() != i) throw new RuntimeException();
/* 160 */       EmissionState[] states_i = hmm.disambiguate(state_i.getMemberStates(false), prev, i, sample);
/* 161 */       EmissionState[] states_i_true = hmm.disambiguate(state_i.getMemberStates(true), prev_true, i, sample);
/* 162 */       result_switches.addPoint(i, makeArray(states_i_true));
/*     */       
/* 164 */       Integer selected = 
/* 165 */         (Integer)sp.getEmission(i);
/*     */       
/*     */ 
/* 168 */       int[] possibilities = emStSp.getHaps(selected.intValue());
/*     */       
/*     */ 
/* 171 */       if (possibilities.length > 1) {
/* 172 */         double[] prob = new double[possibilities.length];
/* 173 */         double sum = 0.0D;
/* 174 */         for (int k = 0; k < prob.length; k++) {
/* 175 */           int[] me = emStSp.getMemberIndices(possibilities[k]);
/* 176 */           prob[k] = states_i_true[0].score(me[0], i);
/* 177 */           for (int k1 = 1; k1 < me.length; k1++) {
/* 178 */             prob[k] *= states_i_true[k1].score(me[k1], i);
/*     */           }
/*     */           
/* 181 */           sum += prob[k];
/*     */         }
/* 183 */         selected = Integer.valueOf(possibilities[Constants.getMax(prob)]);
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */ 
/* 189 */         selected = Integer.valueOf(possibilities[0]);
/*     */       }
/* 191 */       result_phased.addPoint(i, emStSp.getHapl(selected));
/*     */       
/* 193 */       prev = states_i;
/* 194 */       prev_true = states_i_true;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 203 */   boolean check = false;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addObject(Object[] obj, CSOData[] objects, int k)
/*     */     throws Exception
/*     */   {
/* 211 */     objects[0].print((PrintWriter)obj[0], true, false, false, null);
/* 212 */     objects[1].print((PrintWriter)obj[1], true, false, false, null);
/* 213 */     ((PrintWriter)obj[0]).flush();
/* 214 */     ((PrintWriter)obj[1]).flush();
/*     */   }
/*     */   
/*     */ 
/*     */   public static boolean deleteDir(File dir)
/*     */   {
/* 220 */     if (dir.isDirectory()) {
/* 221 */       String[] children = dir.list();
/* 222 */       for (int i = 0; i < children.length; i++) {
/* 223 */         boolean success = deleteDir(new File(dir, children[i]));
/* 224 */         if (!success) {
/* 225 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 231 */     return dir.delete();
/*     */   }
/*     */   
/* 234 */   boolean unwrapToSample = Constants.unwrapForSampling();
/*     */   
/* 236 */   public void sampleFromHMM(EmissionState dat1, int noSamples, List models, File sampleDir) throws Exception { boolean sample = noSamples > 1;
/*     */     
/*     */ 
/* 239 */     EmissionStateSpace[] stateEm = new EmissionStateSpace[dat1.noCopies()];
/* 240 */     if ((Constants.transMode(1) == null) && (Constants.saveStates())) {
/* 241 */       for (int i = 0; i < stateEm.length; i++) {
/* 242 */         int[] temp = new int[i + 1];
/* 243 */         Arrays.fill(temp, Constants.numF(0));
/* 244 */         stateEm[i] = Emiss.getStateEmissionStateSpace(temp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 250 */     String key = dat1.getName();
/* 251 */     CompoundMarkovModel hmm = (CompoundMarkovModel)models.get(dat1.noCopies() - 1);
/* 252 */     if (this.unwrapToSample) {
/* 253 */       while ((hmm instanceof WrappedModel)) {
/* 254 */         hmm = ((WrappedModel)hmm).getHMM();
/*     */       }
/*     */       
/* 257 */     } else if ((hmm instanceof CachedHMM)) {
/* 258 */       ((CachedHMM)hmm).refresh();
/*     */     }
/* 260 */     DP dp = new DP(hmm, dat1.getName(), dat1, false);
/*     */     
/* 262 */     dp.reset(false);
/* 263 */     dp.calcScoresForward(sample);
/* 264 */     int len = 1;
/* 265 */     Object[] os1 = getOutputStream(sampleDir, key);
/* 266 */     CompoundEmissionStateSpace emStSp = (CompoundEmissionStateSpace)((EmissionState)hmm.getState(1)).getEmissionStateSpace();
/*     */     
/* 268 */     PhasedDataState[] sam = {
/* 269 */       SimpleScorableObject.make(dat1.getName(), dat1.noSnps(), emStSp), 
/* 270 */       SimpleScorableObject.make(dat1.getName(), dat1.noSnps(), stateEm[(emStSp.getMembers().length - 1)]) };
/*     */     
/*     */ 
/* 273 */     for (int k = 0; k < noSamples; k++) {
/* 274 */       StatePath sp = dp.getStatePath(sample);
/*     */       
/* 276 */       sample(sp, hmm, sample, sam);
/* 277 */       sam[0].setName(key + "_" + k);
/* 278 */       sam[1].setName(key + "_" + k);
/* 279 */       addObject(os1, sam, k);
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 284 */     ((PrintWriter)os1[0]).close();
/* 285 */     ((PrintWriter)os1[1]).close();
/*     */   }
/*     */   
/*     */ 
/*     */   public void sampleFromHMM(List models, int noSamples, int modelCount, List<EmissionState>[] bwt)
/*     */   {
/* 291 */     if (!this.directory.exists()) this.directory.mkdir();
/* 292 */     File sampleDir = new File(this.directory, "hmm_" + modelCount);
/* 293 */     sampleDir.mkdir();
/* 294 */     modelCount++;
/*     */     
/* 296 */     PrintWriter[] pw_rep = (PrintWriter[])null;
/*     */     
/*     */ 
/* 299 */     int index = 0;
/*     */     try {
/* 301 */       if (pw_rep != null) {
/* 302 */         pw_rep = new PrintWriter[noSamples];
/* 303 */         for (int k = 0; k < pw_rep.length; k++) {
/* 304 */           pw_rep[k] = new PrintWriter(new BufferedWriter(new FileWriter(new File(sampleDir, "rep_" + k))));
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 310 */       Iterator<EmissionState> it = this.data.dataLvalues();
/* 309 */       while (
/* 310 */         it.hasNext()) {
/* 311 */         sampleFromHMM((EmissionState)it.next(), noSamples, models, sampleDir);
/* 312 */         index++;
/*     */       }
/* 314 */       if (pw_rep != null) {
/* 315 */         for (int k = 0; k < pw_rep.length; k++) {
/* 316 */           pw_rep[k].close();
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception exc) {
/* 321 */       exc.printStackTrace();
/* 322 */       System.exit(0);
/*     */     }
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
/*     */   double[][] rPhased;
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
/*     */   List<CSOData>[] get(String key, List<Integer> rep, EmissionStateSpace[] emStSp)
/*     */     throws Exception
/*     */   {
/* 353 */     List[] res = { new ArrayList(), new ArrayList() };
/* 354 */     for (int i = 0; i < rep.size(); i++) {
/* 355 */       BufferedReader[] os = getInputStream(new File(this.directory, "hmm_" + rep.get(i)), key);
/* 356 */       res[0].addAll(getIterator(os[0], Emiss.class, emStSp[0]));
/* 357 */       res[1].addAll(getIterator(os[1], Integer.class, emStSp[1]));
/* 358 */       os[0].close();os[1].close();
/*     */     }
/* 360 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */   public double[] getDistClusters(EmissionState emission, int numStates, double[] distClust)
/*     */   {
/* 366 */     EmissionStateSpace stSp = emission.getEmissionStateSpace();
/* 367 */     for (int m = 0; m < emission.noSnps(); m++) {
/* 368 */       double[] emissionProb = emission.getEmiss(m);
/* 369 */       for (int i = 0; i < emission.getEmiss(m).length; i++) {
/* 370 */         String pairStates = stSp.getHaploPair(Integer.valueOf(i)).toString().replaceAll(" ", "");
/* 371 */         distClust[(Integer.parseInt(pairStates.substring(1, 2)) + numStates * m - 1)] += emissionProb[i];
/* 372 */         distClust[(Integer.parseInt(pairStates.substring(3, 4)) + numStates * m - 1)] += emissionProb[i];
/*     */       }
/*     */     }
/* 375 */     return distClust;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void calcBestPathSampling(HaplotypeEmissionState dat_em, EmissionStateSpace[] ststSp, PIGData dat, List<Integer> rep)
/*     */   {
/*     */     try
/*     */     {
/* 384 */       String key = dat.getName();
/* 385 */       List[] spList = 
/* 386 */         get(key, rep, ststSp);
/* 387 */       int len = dat.length();
/* 388 */       double[] uncertaintyPhase = this.data.uncertaintyPhase(key);
/* 389 */       double[] uncertaintyVitPhase = this.data.uncertaintyVitPhase(key);
/* 390 */       double[] uncertaintyState = this.data.uncertaintyState(key);
/* 391 */       EmissionState stateEm = this.data.getState(key, ststSp[1]);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 399 */       Arrays.fill(uncertaintyPhase, 1.0D);
/* 400 */       Arrays.fill(uncertaintyVitPhase, 1.0D);
/* 401 */       Arrays.fill(uncertaintyState, 0.0D);
/* 402 */       PIGData datvit = (PIGData)((PIGData)spList[1].get(0)).clone();
/* 403 */       if (spList[0].size() == 1)
/*     */       {
/* 405 */         dat = (PIGData)spList[0].get(0);
/* 406 */         dat.setName(key);
/* 407 */         datvit.setName(key);
/* 408 */         this.data.setData(key, dat);
/* 409 */         this.data.setViterbi(key, datvit);
/* 410 */         for (int i = 0; i < dat.length(); i++) {
/* 411 */           dat_em.emissions[i] = new IntegerDistribution(dat_em.getEmissionStateSpace().get(dat.getElement(i)).intValue());
/*     */         }
/*     */       }
/*     */       else {
/* 415 */         dat.sampleGenotype(dat_em, spList[0]);
/* 416 */         dat.samplePhase(spList[0], uncertaintyPhase, dat_em.getEmissionStateSpace());
/* 417 */         if (stateEm != null) { datvit.sampleGenotype((HaplotypeEmissionState)stateEm, spList[1]);
/*     */         }
/* 419 */         if ((dat.length() != len) || (datvit.length() != len)) throw new RuntimeException("!! " + this.data.length() + " " + datvit.length() + " " + len);
/* 420 */         datvit.setName(dat.getName());
/*     */         
/* 422 */         if (stateEm != null) datvit.samplePhase(spList[1], uncertaintyVitPhase, stateEm.getEmissionStateSpace());
/* 423 */         this.data.setViterbi(key, datvit);
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 428 */       this.data.setRecSites(dat.getName(), sampleRecSites(spList[1]));
/*     */     } catch (Exception exc) {
/* 430 */       exc.printStackTrace();
/*     */     }
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
/*     */   double[][] dPhased;
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
/*     */   private int noShared(ComparableArray comp_train, ComparableArray comp__no_train)
/*     */   {
/* 469 */     List<Comparable> l = new ArrayList(comp_train.elements());
/* 470 */     List<Comparable> l1 = new ArrayList(comp__no_train.elements());
/* 471 */     int cnt = 0;
/* 472 */     for (int i = 0; i < l.size(); i++) {
/* 473 */       if (l1.remove(l.get(i))) cnt++;
/*     */     }
/* 475 */     return cnt;
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
/*     */   public void calcBestPathSampling(List<Integer> rep)
/*     */   {
/* 513 */     Logger.global.info("sampling from hmm");
/*     */     
/* 515 */     int index = 0;
/*     */     
/* 517 */     EmissionStateSpace[] stateEm = new EmissionStateSpace[Constants.maxNoCopies];
/* 518 */     if ((Constants.transMode(1) == null) && (Constants.saveStates())) {
/* 519 */       for (int i = 0; i < stateEm.length; i++) {
/* 520 */         int[] temp = new int[i + 1];
/* 521 */         Arrays.fill(temp, Constants.numF(0));
/* 522 */         stateEm[i] = Emiss.getStateEmissionStateSpace(temp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 527 */     for (Iterator<PIGData> it = this.data.iterator(); it.hasNext(); index++) {
/* 528 */       PIGData dat = (PIGData)it.next();
/* 529 */       HaplotypeEmissionState emSt = (HaplotypeEmissionState)this.data.getL(dat.getName());
/* 530 */       if (!(emSt instanceof HaplotypeEmissionState)) {
/* 531 */         ((DataCollection)this.data).dataL.put(dat.getName(), emSt = new HaplotypeEmissionState(emSt.getName(), emSt.length(), emSt.getEmissionStateSpace().size(), emSt.getEmissionStateSpace(), null, null, 1.0D));
/*     */       }
/* 533 */       calcBestPathSampling(emSt, 
/* 534 */         new EmissionStateSpace[] { emSt.getEmissionStateSpace(), 
/* 535 */         stateEm[(dat.noCopies() - 1)] }, dat, rep);
/*     */     }
/* 537 */     Logger.global.info("... done sampling");
/* 538 */     if (!Constants.keepSamples()) { removeSamples();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void countsHap(List<Integer> rep)
/*     */     throws Exception
/*     */   {
/* 547 */     getHapSp2();
/* 548 */     int noSNP = this.data.snpid.size();
/* 549 */     List<String> ind = getInd();
/* 550 */     List<Integer> position = this.data.loc;
/* 551 */     this.rPhased = new double[noSNP][noSNP];
/* 552 */     this.dPhased = new double[noSNP][noSNP];
/* 553 */     Map<String, int[]> ldCount = new HashMap();
/* 554 */     for (int m1 = 0; m1 < noSNP - 1; m1++) {
/* 555 */       for (int m2 = m1 + 1; m2 < noSNP; m2++) {
/* 556 */         if (((Integer)position.get(m2)).intValue() - ((Integer)position.get(m1)).intValue() <= Constants.distanceLD) {
/* 557 */           String si = m1 + "_" + m2;
/* 558 */           ldCount.put(si, new int[4]);
/*     */         }
/*     */       }
/*     */     }
/* 562 */     for (int r = 0; r < rep.size(); r++) {
/* 563 */       for (int i = 0; i < ind.size(); i++) {
/* 564 */         BufferedReader br = new BufferedReader(new FileReader(new File(this.directory + "/hmm_" + rep.get(r), "hap_" + (String)ind.get(i))));
/* 565 */         String st = br.readLine();
/* 566 */         while (st != null) {
/* 567 */           if (st.startsWith("#")) {
/* 568 */             for (int c = 0; c < this.data.get((String)ind.get(i)).noCopies(); c++) {
/* 569 */               int a = this.data.get((String)ind.get(i)).noCopies();
/* 570 */               st = br.readLine();
/* 571 */               for (int m1 = 0; m1 < noSNP - 1; m1++) {
/* 572 */                 for (int m2 = m1 + 1; m2 < noSNP; m2++) {
/* 573 */                   if (((Integer)position.get(m2)).intValue() - ((Integer)position.get(m1)).intValue() <= Constants.distanceLD) {
/* 574 */                     char[] hap = { st.charAt(m1), st.charAt(m2) };
/* 575 */                     String s = new String(hap);
/* 576 */                     ((int[])ldCount.get(m1 + "_" + m2))[getSpIndex(this.hapSp2, s)] += 1;
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/* 581 */             st = br.readLine();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 586 */     for (Iterator<String> is = ldCount.keySet().iterator(); is.hasNext();) {
/* 587 */       String id = (String)is.next();
/* 588 */       double[] score = calR2D((int[])ldCount.get(id));
/* 589 */       String[] ids = id.split("_");
/* 590 */       this.dPhased[Integer.parseInt(ids[0])][Integer.parseInt(ids[1])] = score[0];
/* 591 */       this.rPhased[Integer.parseInt(ids[0])][Integer.parseInt(ids[1])] = score[1];
/*     */     }
/*     */   }
/*     */   
/*     */   public void printLD(PrintStream ps)
/*     */   {
/* 597 */     for (int m1 = 0; m1 < this.data.snpid.size() - 1; m1++) {
/* 598 */       for (int m2 = m1 + 1; m2 < this.data.snpid.size(); m2++) {
/* 599 */         if (!Double.isNaN(this.rPhased[m1][m2])) {
/* 600 */           ps.print(m1 + 1);ps.print("\t");ps.print(m2 + 1);ps.print("\t");
/* 601 */           ps.print(this.dPhased[m1][m2]);ps.print("\t");ps.println(this.rPhased[m1][m2]);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public double[] calR2D(int[] counts)
/*     */   {
/* 609 */     int sum = 0;
/* 610 */     for (int i = 0; i < counts.length; i++) {
/* 611 */       sum += counts[i];
/*     */     }
/* 613 */     double[] probs = new double[counts.length];
/* 614 */     for (int i = 0; i < probs.length; i++) {
/* 615 */       probs[i] = (counts[i] / sum);
/*     */     }
/* 617 */     double[] mProbs = new double[counts.length];
/* 618 */     probs[0] += probs[1];
/* 619 */     mProbs[1] = (probs[2] + probs[3]);
/* 620 */     mProbs[2] = (probs[1] + probs[3]);
/* 621 */     mProbs[3] = (probs[0] + probs[2]);
/* 622 */     double d = probs[0] * probs[3] - probs[1] * probs[2];
/* 623 */     double[] dMax = { Math.min(mProbs[0] * mProbs[2], mProbs[1] * mProbs[3]), 
/* 624 */       -Math.min(mProbs[0] * mProbs[3], mProbs[1] * mProbs[2]) };
/* 625 */     double[] score = { d > 0.0D ? d / dMax[0] : d / dMax[1], Math.pow(d, 2.0D) / (mProbs[0] * mProbs[1] * mProbs[2] * mProbs[3]) };
/*     */     
/* 627 */     return score;
/*     */   }
/*     */   
/* 630 */   List<String> hapSp2 = new ArrayList();
/*     */   
/* 632 */   public void getHapSp2() { this.hapSp2.add("AA");
/* 633 */     this.hapSp2.add("AB");
/* 634 */     this.hapSp2.add("BA");
/* 635 */     this.hapSp2.add("BB");
/*     */   }
/*     */   
/*     */   public int getSpIndex(List<String> hapSp, String aa) {
/* 639 */     int i = -1;
/* 640 */     while (i < hapSp.size()) {
/* 641 */       i++;
/* 642 */       if (((String)hapSp.get(i)).equals(aa)) break;
/*     */     }
/* 644 */     if (i == -1) throw new RuntimeException("!!");
/* 645 */     return i;
/*     */   }
/*     */   
/*     */   public List<String> getInd()
/*     */   {
/* 650 */     int index = 0;
/* 651 */     List<String> ind = new ArrayList();
/* 652 */     for (Iterator<PIGData> it = this.data.iterator(); it.hasNext(); index++) {
/* 653 */       PIGData dat = (PIGData)it.next();
/* 654 */       ind.add(dat.getName());
/*     */     }
/* 656 */     return ind;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public List<PIGData> getIterator(BufferedReader stream, Class clazz, EmissionStateSpace emStsp)
/*     */     throws Exception
/*     */   {
/* 664 */     return new ArrayList(DataCollection.readFastPhaseOutput(stream, clazz, emStsp).data.values());
/*     */   }
/*     */   
/*     */   private SortedMap<Integer, Integer>[] sampleRecSites(List<CSOData> spList) {
/* 668 */     ComparableArray obj = (ComparableArray)((CSOData)spList.get(0)).getElement(0);
/* 669 */     int gap = 0;
/* 670 */     if (!(obj instanceof TrComparableArray)) return null;
/* 671 */     SortedMap[] m = new SortedMap[((TrComparableArray)obj).third.size()];
/* 672 */     for (int j = 0; j < m.length; j++)
/* 673 */       m[j] = new TreeMap();
/*     */     CSOData nxt;
/* 675 */     int i; for (Iterator<CSOData> it = spList.iterator(); it.hasNext(); 
/*     */         
/*     */ 
/* 678 */         i < nxt.length())
/*     */     {
/* 676 */       nxt = (CSOData)it.next();
/* 677 */       ComparableArray prev = ((TrComparableArray)nxt.getElement(0)).third;
/* 678 */       i = 1; continue;
/* 679 */       ComparableArray third = ((TrComparableArray)nxt.getElement(i)).third;
/* 680 */       for (int j = 0; j < m.length; j++) {
/* 681 */         if (!third.get(j).equals(prev.get(j))) {
/* 682 */           int i1 = i;
/*     */           
/* 684 */           Integer cnt = (Integer)m[j].get(Integer.valueOf(i1));
/* 685 */           m[j].put(Integer.valueOf(i1), Integer.valueOf(cnt == null ? 1 : cnt.intValue() + 1));
/*     */         }
/*     */       }
/*     */       
/* 689 */       prev = third;i++;
/*     */     }
/*     */     
/* 692 */     for (int j = 0; j < m.length; j++) {
/* 693 */       for (Iterator<Map.Entry<Integer, Integer>> it = m[j].entrySet().iterator(); it.hasNext();) {
/* 694 */         if (((Integer)((Map.Entry)it.next()).getValue()).intValue() < Constants.sampleThresh() * spList.size()) {
/* 695 */           it.remove();
/*     */         }
/*     */       }
/*     */     }
/* 699 */     return m;
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
/*     */   int validate(double[] d)
/*     */   {
/* 766 */     double sum = 0.0D;
/* 767 */     int maxIndex = 0;
/* 768 */     for (int i = 0; i < d.length; i++) {
/* 769 */       sum += d[i];
/* 770 */       if (d[i] > d[maxIndex]) { maxIndex = i;
/*     */       }
/*     */     }
/* 773 */     return maxIndex;
/*     */   }
/*     */   
/* 776 */   private double sum(SortedSet<Map.Entry<List<Comparable>, Integer>> set) { double sum = 0.0D;
/* 777 */     for (Iterator<Map.Entry<List<Comparable>, Integer>> it = set.iterator(); it.hasNext();) {
/* 778 */       sum += ((Integer)((Map.Entry)it.next()).getValue()).intValue();
/*     */     }
/* 780 */     return sum;
/*     */   }
/*     */   
/*     */   private static String toString(List<ComparableArray> poss)
/*     */   {
/* 785 */     StringBuffer sb = new StringBuffer();
/* 786 */     sb.append("{");
/* 787 */     for (int i = 0; i < poss.size(); i++) {
/* 788 */       sb.append(Arrays.asList(new List[] { poss }).toString());
/*     */     }
/* 790 */     sb.append("}");
/* 791 */     return sb.toString();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   private static ComparableArray makeArray(EmissionState[] st)
/*     */   {
/* 798 */     List<Comparable> res = new ArrayList();
/* 799 */     for (int i = 0; i < st.length; i++) {
/* 800 */       if ((st[i] instanceof CompoundState)) {
/* 801 */         res.add(makeArray(((CompoundState)st[i]).getMemberStates(true)));
/*     */       }
/* 803 */       else if ((st[i] instanceof State)) {
/* 804 */         res.add(Integer.valueOf(st[i].getIndex()));
/*     */       }
/*     */       else {
/* 807 */         res.add(st[i]);
/*     */       }
/*     */     }
/* 810 */     if (((st[0] instanceof CompoundState)) && (st.length == 3)) {
/* 811 */       return new TrioComparableArray(new ComparableArray(res));
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 816 */     return new ComparableArray(res);
/*     */   }
/*     */   
/*     */   public void calcBestPathViterbi(List models) throws Exception {
/* 820 */     this.data.clearViterbi();
/* 821 */     int cntSwitch = 0;
/* 822 */     EmissionStateSpace[] stateEm = new EmissionStateSpace[Constants.noCopies[0]];
/* 823 */     if ((Constants.transMode(1) == null) && (Constants.saveStates())) {
/* 824 */       for (int i = 0; i < stateEm.length; i++) {
/* 825 */         int[] temp = new int[i + 1];
/* 826 */         Arrays.fill(temp, Constants.numF(0));
/* 827 */         stateEm[i] = Emiss.getStateEmissionStateSpace(temp);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 833 */     for (Iterator<EmissionState> it = this.data.dataLvalues(); it.hasNext();) {
/*     */       try {
/* 835 */         EmissionState dat1 = (EmissionState)it.next();
/* 836 */         String key = dat1.getName();
/* 837 */         int nocop = this.data.noCopies(key);
/*     */         
/* 839 */         CompoundMarkovModel hmm = (CompoundMarkovModel)models.get(nocop - 1);
/* 840 */         while ((hmm instanceof WrappedModel)) {
/* 841 */           hmm = ((WrappedModel)hmm).getHMM();
/*     */         }
/*     */         
/* 844 */         DP dp = new DP(hmm, "", dat1, true);
/* 845 */         dp.reset(true);
/* 846 */         dp.searchViterbi();
/* 847 */         StatePath sp = dp.getStatePath(false);
/*     */         
/* 849 */         EmissionState[] previous = (EmissionState[])null;
/* 850 */         PIGData viterbi = SimpleScorableObject.make(dat1.getName(), dat1.length(), stateEm[1]);
/*     */         
/* 852 */         for (int i = 0; i < sp.size(); i++) {
/* 853 */           CompoundState st = (CompoundState)sp.getState(i);
/* 854 */           EmissionState[] states = hmm.disambiguate(st.getMemberStates(true), previous, i, false);
/* 855 */           if (i > 1) for (int k = 0; k < states.length; k++) {
/* 856 */               if (states[k] != previous[k]) { cntSwitch++;
/*     */               }
/*     */             }
/* 859 */           ComparableArray compa = makeArray(states);
/* 860 */           viterbi.addPoint(i, compa);
/*     */           
/* 862 */           previous = states;
/*     */         }
/* 864 */         System.err.println("viterbi " + viterbi);
/* 865 */         this.data.setViterbi(key, viterbi);
/*     */       }
/*     */       catch (Exception exc) {
/* 868 */         exc.printStackTrace();
/*     */       }
/*     */     }
/*     */     
/* 872 */     System.err.println("switch count is " + cntSwitch);
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/Sampler.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */