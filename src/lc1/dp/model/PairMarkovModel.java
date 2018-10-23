/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.data.representation.ComparableArray;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.states.CompoundState;
/*     */ import lc1.dp.states.DotState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.PairEmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.StateDistribution;
/*     */ import lc1.util.Constants;
/*     */ import lc1.util.CopyEnumerator;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PairMarkovModel
/*     */   extends CompoundMarkovModel
/*     */ {
/*     */   public MarkovModel[] m1;
/*     */   public final int[] num_copies;
/*     */   public final int[] count_copies;
/*     */   final Class emissionClass;
/*     */   double[] pseudo1;
/*     */   double[] pseudo2;
/*     */   
/*     */   public void setPseudoCountWeights(double[] d, double[] d1)
/*     */   {
/*  45 */     this.pseudo1 = d;
/*  46 */     this.pseudo2 = d;
/*  47 */     for (int i = 0; i < this.m1.length; i++)
/*  48 */       this.m1[i].setPseudoCountWeights(d, d1);
/*     */   }
/*     */   
/*     */   public boolean trainEmissions() {
/*  52 */     for (int i = 0; i < this.m1.length; i++) {
/*  53 */       if (this.m1[i].trainEmissions()) return true;
/*     */     }
/*  55 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void refresh()
/*     */   {
/*  61 */     for (int i = 0; i < this.m1.length; i++) {
/*  62 */       if ((this.m1[i] instanceof CompoundMarkovModel)) {
/*  63 */         ((CompoundMarkovModel)this.m1[i]).refresh();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public EmissionState[] disambiguate(EmissionState[] state, EmissionState[] previous, int indexOfToEmiss, boolean sample)
/*     */   {
/*  75 */     return state;
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
/*     */   public void transferCountsToProbs(int index)
/*     */   {
/*  94 */     for (int i = 0; i < this.probB.length; i++) {
/*  95 */       this.probB[i].bMaximistation(this.pseudo1[3], index);
/*     */     }
/*  97 */     for (int i = 0; i < this.m1.length; i++) {
/*  98 */       this.m1[i].transferCountsToProbs(index);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Object clone(boolean swtch)
/*     */   {
/* 107 */     MarkovModel[] m = new MarkovModel[this.m1.length];
/* 108 */     for (int i = 0; i < m.length; i++) {
/* 109 */       this.m1[i] = ((MarkovModel)this.m1[i].clone(swtch));
/*     */     }
/* 111 */     return new PairMarkovModel(m, this.num_copies, this.emissionClass, this.emissionStateSpace, true);
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
/*     */   public IlluminaProbB probB(int i)
/*     */   {
/* 160 */     return this.probB[i];
/*     */   }
/*     */   
/*     */ 
/* 164 */   private final Map<List<Comparable>, CompoundState> compoundStates = new HashMap();
/*     */   final IlluminaProbB[] probB;
/*     */   
/* 167 */   public CompoundState getCompoundState(EmissionState[] res) { CompoundState state = (CompoundState)this.compoundStates.get(Arrays.asList(res));
/* 168 */     if (state == null) throw new RuntimeException("!!");
/* 169 */     return state;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final Comparator<CompoundState> getComparator()
/*     */   {
/* 177 */     new Comparator()
/*     */     {
/*     */       public int compare(CompoundState l1, CompoundState l2) {
/* 180 */         ComparableArray c1 = l1.getMemberStatesRecursive(false);
/* 181 */         ComparableArray c2 = l2.getMemberStatesRecursive(false);
/* 182 */         List<Comparable> co1 = new ArrayList(c1.elements());
/* 183 */         List<Comparable> co2 = new ArrayList(c2.elements());
/* 184 */         Collections.sort(co1);
/* 185 */         Collections.sort(co2);
/* 186 */         for (int i = 0; i < co1.size(); i++) {
/* 187 */           int res = ((Comparable)co1.get(i)).compareTo(co2.get(i));
/* 188 */           if (res != 0) return res;
/*     */         }
/* 190 */         return 0;
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
/*     */   static String getName(MarkovModel[] m1, int[] no_copies)
/*     */   {
/* 205 */     StringBuffer sb = new StringBuffer("(");
/* 206 */     for (int i = 0; i < no_copies.length; i++) {
/* 207 */       sb.append(m1[no_copies[i]].getName() + (i < no_copies.length - 1 ? "X" : ""));
/*     */     }
/* 209 */     sb.append(")");
/* 210 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public PairMarkovModel(MarkovModel[] m1, int[] no_copies, Class emClass, EmissionStateSpace emissionStateSpace, final boolean decomp)
/*     */   {
/* 215 */     super(getName(m1, no_copies), m1[0].noSnps.intValue(), emissionStateSpace);
/*     */     
/* 217 */     int noSources = Constants.format.length;
/*     */     
/* 219 */     this.probB = new IlluminaProbB[noSources];
/* 220 */     for (int i = 0; i < this.probB.length; i++) {
/* 221 */       this.probB[i] = new IlluminaProbB(emissionStateSpace, Constants.b_train() < 1000.0D);
/*     */     }
/*     */     
/*     */ 
/* 225 */     this.emissionClass = emClass;
/* 226 */     this.num_copies = no_copies;
/* 227 */     this.count_copies = new int[m1.length];
/* 228 */     Arrays.fill(this.count_copies, 0);
/* 229 */     for (int i = 0; i < this.num_copies.length; i++) {
/* 230 */       this.count_copies[this.num_copies[i]] += 1;
/*     */     }
/* 232 */     this.from1 = new int[no_copies.length];
/* 233 */     this.to1 = new int[no_copies.length];
/* 234 */     this.m1 = m1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 242 */     Comparator<CompoundState> comp = getComparator();
/* 243 */     final SortedMap<CompoundState, List<CompoundState>> m = new TreeMap(comp);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 248 */     CopyEnumerator dblIterator = new CopyEnumerator(this.num_copies.length) {
/*     */       public Iterator getPossibilities(int depth) {
/* 250 */         return PairMarkovModel.this.m1[PairMarkovModel.this.num_copies[depth]].states();
/*     */       }
/*     */       
/*     */       public void doInner(Comparable[] list)
/*     */       {
/* 255 */         ComparableArray l = new ComparableArray(Arrays.asList(list));
/* 256 */         CompoundState st = PairMarkovModel.this.constructPair(l.elements(), decomp);
/*     */         
/* 258 */         List<CompoundState> equiv = (List)m.get(st);
/* 259 */         if (equiv == null) {
/* 260 */           m.put(st, equiv = new ArrayList());
/*     */         }
/* 262 */         equiv.add(st);
/* 263 */         PairMarkovModel.this.compoundStates.put(l.elements(), st);
/*     */       }
/*     */       
/* 266 */       public boolean exclude(Object obj, Object prv) { if (obj == PairMarkovModel.MAGIC) return true;
/* 267 */         if (((State)obj instanceof DotState)) { throw new RuntimeException("!! " + obj);
/*     */         }
/*     */         
/*     */ 
/* 271 */         return false;
/*     */       }
/* 273 */     };
/* 274 */     dblIterator.run();
/* 275 */     this.singleLine = new StateDistribution[m1.length][];
/* 276 */     for (int i1 = 0; i1 < this.singleLine.length; i1++) {
/* 277 */       this.singleLine[i1] = new StateDistribution[m1[i1].modelLength()];
/* 278 */       for (int i2 = 0; i2 < this.singleLine[i1].length; i2++) {
/* 279 */         this.singleLine[i1][i2] = new StateDistribution(m1[i1].modelLength());
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 286 */     for (Iterator<List<CompoundState>> it = m.values().iterator(); it.hasNext();) {
/* 287 */       List<CompoundState> nxt = (List)it.next();
/* 288 */       addState((State)nxt.get(0)); }
/*     */     List<CompoundState> nxt;
/* 290 */     int j; for (Iterator<List<CompoundState>> it = m.values().iterator(); it.hasNext(); 
/*     */         
/* 292 */         j < nxt.size())
/*     */     {
/* 291 */       nxt = (List)it.next();
/* 292 */       j = 1; continue;
/* 293 */       addState((State)nxt.get(j));j++;
/*     */     }
/*     */     
/*     */ 
/* 296 */     makeEquivalenceClasses(m.values().iterator());
/* 297 */     System.err.println(this.emissionClass);
/* 298 */     this.in = new int[this.states.size() - 1];
/* 299 */     for (int jk = 1; jk < this.states.size(); jk++) {
/* 300 */       this.in[(jk - 1)] = jk;
/*     */     }
/* 302 */     Logger.global.info("state space size is " + this.states.size() + " for " + getName());
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   SkewNormal probR2;
/*     */   
/*     */ 
/*     */ 
/*     */   final int[] from1;
/*     */   
/*     */ 
/*     */ 
/*     */   protected void makeEquivalenceClasses(Iterator<List<CompoundState>> it)
/*     */   {
/* 318 */     this.equivalenceClasses.clear();
/* 319 */     while (it.hasNext()) {
/* 320 */       List<CompoundState> l = (List)it.next();
/* 321 */       int[] val = new int[l.size()];
/* 322 */       for (int i = 0; i < l.size(); i++) {
/* 323 */         val[i] = ((CompoundState)l.get(i)).getIndex();
/*     */       }
/* 325 */       this.equivalenceClasses.add(val);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public String toString()
/*     */   {
/* 332 */     StringBuffer sb = new StringBuffer(this.m1[0].toString());
/* 333 */     for (int i = 1; i < this.m1.length; i++) {
/* 334 */       sb.append(this.m1[i].toString());
/*     */     }
/* 336 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public PairMarkovModel(PairMarkovModel m1) {
/* 340 */     this(m1.m1, m1.num_copies, m1.emissionClass, m1.emissionStateSpace, true);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public int noCop(List st1)
/*     */   {
/* 347 */     int noCop = 0;
/* 348 */     for (int i = 0; i < st1.size(); i++) {
/* 349 */       noCop += ((EmissionState)st1.get(i)).noCop().intValue();
/*     */     }
/* 351 */     return noCop;
/*     */   }
/*     */   
/* 354 */   private PairEmissionState constructPair(List st1, boolean decompose) { PairEmissionState res = null;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 361 */       res = 
/* 362 */         (PairEmissionState)this.emissionClass.getConstructor(new Class[] { List.class, EmissionStateSpace.class, Boolean.TYPE, IlluminaProbB[].class, SkewNormal[].class }).newInstance(new Object[] { st1, this.emissionStateSpace, Boolean.valueOf(decompose), 
/* 363 */         this.probB });
/*     */     } catch (Exception exc) {
/* 365 */       exc.printStackTrace();
/*     */     }
/*     */     
/* 368 */     return res;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 379 */     for (int i = 0; i < this.m1.length; i++) {
/* 380 */       this.m1[i].initialiseTransitionCounts();
/*     */     }
/*     */   }
/*     */   
/*     */   public void initialiseEmissionCounts()
/*     */   {
/* 386 */     super.initialiseEmissionCounts();
/* 387 */     for (int i = 0; i < this.probB.length; i++) {
/* 388 */       this.probB[i].initialiseBCounts();
/*     */       
/* 390 */       List<SkewNormal> l = new ArrayList();
/* 391 */       probR(null, null, l, null, i);
/* 392 */       for (Iterator<SkewNormal> it = l.iterator(); it.hasNext();) {
/* 393 */         ((SkewNormal)it.next()).initialiseCounts();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getTransitionScore(int from, int to, int endIndex)
/*     */   {
/* 405 */     updateFrom(from);
/* 406 */     updateTo(to);
/* 407 */     double sc = 1.0D;
/* 408 */     for (int j = 0; j < this.from1.length; j++) {
/* 409 */       sc *= this.m1[this.num_copies[j]].getTransitionScore(this.from1[j], this.to1[j], endIndex);
/*     */     }
/* 411 */     return sc;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   final int[] to1;
/*     */   
/*     */ 
/*     */ 
/*     */   final StateDistribution[][] singleLine;
/*     */   
/*     */ 
/*     */   final int[] in;
/*     */   
/*     */ 
/*     */   boolean updateFrom(int from)
/*     */   {
/* 429 */     if (from == 0) {
/* 430 */       Arrays.fill(this.from1, from);
/* 431 */       return true;
/*     */     }
/*     */     
/* 434 */     PairEmissionState pes1 = (PairEmissionState)getState(from);
/* 435 */     for (int i = 0; i < this.num_copies.length; i++) {
/* 436 */       this.from1[i] = pes1.getInnerState(i, true).getIndex();
/*     */     }
/* 438 */     return false;
/*     */   }
/*     */   
/*     */   boolean updateTo(int to) {
/* 442 */     if (to == 0) {
/* 443 */       Arrays.fill(this.to1, to);
/* 444 */       return true;
/*     */     }
/*     */     
/* 447 */     PairEmissionState pes1 = (PairEmissionState)getState(to);
/* 448 */     for (int i = 0; i < this.num_copies.length; i++) {
/* 449 */       this.to1[i] = pes1.getInnerState(i, true).getIndex();
/*     */     }
/* 451 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate(int length)
/*     */     throws Exception
/*     */   {
/* 458 */     for (int i = 0; i < this.m1.length; i++) {
/* 459 */       this.m1[i].validate(length);
/*     */     }
/* 461 */     super.validate(length);
/*     */   }
/*     */   
/*     */   public void transform(StateDistribution[] transProbs, int i)
/*     */   {
/* 466 */     for (int j = 0; j < transProbs.length; j++) {
/* 467 */       updateFrom(j);
/* 468 */       for (int j1 = 0; j1 < modelLength(); j1++) {
/* 469 */         Double val = transProbs[j].get(j1);
/*     */         
/* 471 */         updateTo(j1);
/* 472 */         for (int k = 0; k < this.num_copies.length; k++) {
/* 473 */           int ind = this.from1[k];
/* 474 */           StateDistribution distr = this.singleLine[this.num_copies[k]][ind];
/*     */           
/*     */ 
/*     */ 
/*     */ 
/* 479 */           distr.addCount(Integer.valueOf(this.to1[k]), val);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCounts(StateDistribution[] transProbs, int i, int numIndiv)
/*     */   {
/* 490 */     for (int i1 = 0; i1 < this.singleLine.length; i1++) {
/* 491 */       StateDistribution[] ss = this.singleLine[i1];
/* 492 */       for (int i2 = 0; i2 < ss.length; i2++) {
/* 493 */         ss[i2].reset();
/*     */       }
/*     */     }
/*     */     
/* 497 */     transform(transProbs, i);
/* 498 */     for (int i1 = 0; i1 < this.singleLine.length; i1++)
/*     */     {
/* 500 */       this.m1[i1].addCounts(this.singleLine[i1], i, numIndiv * this.count_copies[i1]);
/*     */     }
/*     */   }
/*     */   
/*     */   void validate(List<StateDistribution> obs1, double total) {
/* 505 */     double sum = 0.0D;
/* 506 */     for (Iterator<StateDistribution> obs = obs1.iterator(); obs.hasNext();) {
/* 507 */       StateDistribution nxt = (StateDistribution)obs.next();
/* 508 */       if (nxt != null)
/* 509 */         sum += nxt.sum();
/*     */     }
/* 511 */     if (Math.abs(sum / total - 1.0D) > 0.001D) {
/* 512 */       if (Math.abs(sum - total) > 0.1D) throw new RuntimeException("!! " + sum + " " + total + " " + this.emissionClass);
/* 513 */       for (Iterator<StateDistribution> obs = obs1.iterator(); obs.hasNext();) {
/* 514 */         StateDistribution nxt = (StateDistribution)obs.next();
/* 515 */         if (nxt != null) {
/* 516 */           nxt.multiplyValues(sum / total);
/*     */         }
/*     */       }
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
/* 533 */   final int[] in0 = new int[1];
/*     */   
/*     */   public int[] statesIn(int j, int i) {
/* 536 */     if (i == 0) return this.in0;
/* 537 */     return this.in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i) {
/* 541 */     if (i == this.noSnps.intValue() - 1) return this.in0;
/* 542 */     return this.in;
/*     */   }
/*     */   
/*     */   public MarkovModel getMarkovModel(int i)
/*     */   {
/* 547 */     return this.m1[this.num_copies[i]];
/*     */   }
/*     */   
/*     */   public int noCopies()
/*     */   {
/* 552 */     return this.num_copies.length;
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
/*     */   public MarkovModel[] getMemberModels()
/*     */   {
/* 571 */     return this.m1;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/PairMarkovModel.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */