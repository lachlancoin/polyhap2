/*     */ package lc1.dp;
/*     */ 
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.SortedSet;
/*     */ import java.util.TreeMap;
/*     */ import java.util.TreeSet;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ import lc1.dp.genotype.io.ComparableArray;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PairMarkovModel
/*     */   extends CompoundMarkovModel
/*     */ {
/*     */   public MarkovModel[] m1;
/*     */   public final int[] num_copies;
/*     */   final int[] from1;
/*     */   final int[] to1;
/*     */   final int[] in;
/*     */   
/*     */   public void setPseudoCountWeights(double d)
/*     */   {
/*  34 */     for (int i = 0; i < this.m1.length; i++) {
/*  35 */       this.m1[i].setPseudoCountWeights(d);
/*     */     }
/*     */   }
/*     */   
/*     */   public Object clone()
/*     */   {
/*  41 */     MarkovModel[] m = new MarkovModel[this.m1.length];
/*  42 */     for (int i = 0; i < m.length; i++) {
/*  43 */       this.m1[i] = ((MarkovModel)this.m1[i].clone());
/*     */     }
/*  45 */     return new PairMarkovModel(m, this.num_copies);
/*     */   }
/*     */   
/*     */ 
/*     */   public void transferCountsToProbs()
/*     */   {
/*  51 */     for (int i = 0; i < this.m1.length; i++) {
/*  52 */       this.m1[i].transferCountsToProbs();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*  58 */   protected boolean stateSpaceExclude(Comparable obj, Comparable previous) { return compare(obj, previous) < 0; }
/*     */   
/*     */   private static final int compare(Object obj, Object previous) {
/*  61 */     if (obj == previous) return 0;
/*  62 */     if (obj == null) return -1;
/*  63 */     if (previous == null) { return 1;
/*     */     }
/*  65 */     Comparable obj1 = (Comparable)obj;
/*  66 */     Comparable prev1 = (Comparable)previous;
/*  67 */     return obj1.compareTo(prev1);
/*     */   }
/*     */   
/*     */ 
/*     */   protected List<Comparable> initStateSpace()
/*     */   {
/*  73 */     final SortedSet<ComparableArray> set = new TreeSet();
/*  74 */     CopyEnumerator posm = new CopyEnumerator(this.num_copies.length) {
/*     */       public Iterator<Comparable> getPossibilities(int depth) {
/*  76 */         return PairMarkovModel.this.m1[PairMarkovModel.this.num_copies[depth]].getEmissionStateSpace().iterator();
/*     */       }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/*  80 */         set.add(new ComparableArray(Arrays.asList(list)));
/*     */       }
/*     */       
/*     */       public boolean exclude(Object obj, Object previous) {
/*  84 */         return PairMarkovModel.this.stateSpaceExclude((Comparable)obj, (Comparable)previous);
/*     */       }
/*  86 */     };
/*  87 */     posm.run();
/*  88 */     List<Comparable> ss = new ArrayList(set);
/*  89 */     return ss;
/*     */   }
/*     */   
/*     */   protected Iterator<ComparableArray> getEquivalentEmissionStates(ComparableArray compa_inner) {
/*  93 */     return PairEmissionState.perm.get(compa_inner).iterator();
/*     */   }
/*     */   
/*     */   public void updateEmissionStateSpaceDist() {
/*  97 */     if (this.emissionStateSpaceDist == null) {
/*  98 */       this.emissionStateSpaceDist = new double[this.emissionStateSpace.size()][this.emissionStateSpace.size()];
/*     */     }
/* 100 */     double hemizygous = Constants.modelHemizygous();
/* 101 */     for (int i = 0; i < this.emissionStateSpace.size(); i++) {
/* 102 */       if (this.emissionStateSpaceDist[i] == null) {
/* 103 */         this.emissionStateSpaceDist[i] = new double[this.emissionStateSpace.size()];
/*     */       }
/* 105 */       double[] res = this.emissionStateSpaceDist[i];
/* 106 */       ComparableArray compa = (ComparableArray)this.emissionStateSpace.get(i);
/* 107 */       double[][] res_compa = new double[compa.size()][];
/* 108 */       for (int i1 = 0; i1 < compa.size(); i1++) {
/* 109 */         res_compa[i1] = this.m1[this.num_copies[i1]].emissionStateSpaceDist[this.m1[this.num_copies[i1]].getEmissionStateSpaceIndex(compa.get(i1))];
/*     */       }
/* 111 */       Arrays.fill(res, 0.0D);
/* 112 */       double sum = 0.0D;
/* 113 */       for (int k = 0; k < res.length; k++) {
/* 114 */         ComparableArray compa_inner = (ComparableArray)this.emissionStateSpace.get(k);
/* 115 */         for (Iterator<ComparableArray> it = getEquivalentEmissionStates(compa_inner); it.hasNext();) {
/* 116 */           ComparableArray compa_inner_inner = (ComparableArray)it.next();
/* 117 */           double res_inner = 1.0D;
/* 118 */           for (int j = 0; j < compa_inner_inner.size(); j++) {
/* 119 */             res_inner *= res_compa[j][this.m1[this.num_copies[j]].getEmissionStateSpaceIndex(compa_inner_inner.get(j))];
/*     */           }
/* 121 */           res[k] += res_inner;
/* 122 */           sum += res_inner;
/*     */         }
/*     */       }
/* 125 */       if (Math.abs(1.0D - sum) > 0.01D) throw new RuntimeException("!! " + sum);
/* 126 */       if (hemizygous > 0.0D) {
/* 127 */         if (compa.size() > 2) throw new RuntimeException();
/* 128 */         Collection<ComparableArray> equiv = PairEmissionState.perm.get(compa);
/* 129 */         if ((compa.size() == 2) && (equiv.size() == 1)) {
/* 130 */           ComparableArray compa1 = ComparableArray.make((Comparable)compa.get(0), null);
/* 131 */           int alt = getEmissionStateSpaceIndex(compa1);
/* 132 */           double inc = hemizygous * res[i];
/* 133 */           res[alt] += inc;
/* 134 */           res[i] -= inc;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   protected void putInStateSpaceIndex(ComparableArray compa, int index) {
/* 141 */     Collection<ComparableArray> equiv = PairEmissionState.perm.get(compa);
/* 142 */     for (Iterator<ComparableArray> it = equiv.iterator(); it.hasNext();) {
/* 143 */       this.stateSpaceToIndex.put(new ComparableArray((List)it.next()), Integer.valueOf(index));
/*     */     }
/*     */   }
/*     */   
/*     */   protected final void initialiseStateSpace()
/*     */   {
/* 149 */     this.emissionStateSpace = initStateSpace();
/* 150 */     if (this.emissionStateSpace.size() == 0) return;
/* 151 */     if ((this.emissionStateSpace.get(0) instanceof ComparableArray)) {
/* 152 */       this.stateSpaceToIndex = new TreeMap();
/*     */     }
/* 154 */     for (int i = 0; i < this.emissionStateSpace.size(); i++) {
/* 155 */       putInStateSpaceIndex((ComparableArray)this.emissionStateSpace.get(i), i);
/*     */     }
/* 157 */     updateEmissionStateSpaceDist();
/*     */   }
/*     */   
/*     */ 
/*     */   public PairMarkovModel(MarkovModel[] m1, int[] no_copies)
/*     */   {
/* 163 */     super(m1[0].getName() + "x", m1[0].MAGIC, m1[0].noSnps);
/* 164 */     this.num_copies = no_copies;
/* 165 */     this.from1 = new int[no_copies.length];
/* 166 */     this.to1 = new int[no_copies.length];
/* 167 */     this.m1 = m1;
/* 168 */     initialiseStateSpace();
/* 169 */     final Map<Collection, List<Integer>> m = new HashMap();
/*     */     
/*     */ 
/* 172 */     CopyEnumerator dblIterator = new CopyEnumerator(this.num_copies.length)
/*     */     {
/* 174 */       public Iterator getPossibilities(int depth) { return PairMarkovModel.this.m1[PairMarkovModel.this.num_copies[depth]].states(); }
/*     */       
/*     */       public void doInner(Comparable[] list) {
/* 177 */         List l = Arrays.asList(list);
/* 178 */         State st = PairMarkovModel.this.addState(PairMarkovModel.this.constructPair(l));
/* 179 */         Collection set = new HashSet(l);
/* 180 */         List<Integer> equiv = (List)m.get(set);
/* 181 */         if (equiv == null) {
/* 182 */           m.put(set, equiv = new ArrayList());
/*     */         }
/* 184 */         equiv.add(Integer.valueOf(st.index));
/*     */       }
/*     */       
/* 187 */       public boolean exclude(Object obj, Object prv) { if (obj == PairMarkovModel.this.MAGIC) return true;
/* 188 */         return false;
/*     */       }
/* 190 */     };
/* 191 */     dblIterator.run();
/*     */     
/* 193 */     makeEquivalenceClasses(m.values().iterator());
/* 194 */     this.in = new int[this.states.size() - 1];
/* 195 */     for (int jk = 1; jk < this.states.size(); jk++) {
/* 196 */       this.in[(jk - 1)] = jk;
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   protected void makeEquivalenceClasses(Iterator<List<Integer>> it)
/*     */   {
/* 204 */     this.equivalenceClasses.clear();
/* 205 */     while (it.hasNext()) {
/* 206 */       List<Integer> l = (List)it.next();
/* 207 */       int[] val = new int[l.size()];
/* 208 */       for (int i = 0; i < l.size(); i++) {
/* 209 */         val[i] = ((Integer)l.get(i)).intValue();
/*     */       }
/* 211 */       this.equivalenceClasses.add(val);
/*     */     }
/*     */   }
/*     */   
/*     */   public String toString()
/*     */   {
/* 217 */     return this.m1.toString();
/*     */   }
/*     */   
/*     */   public PairMarkovModel(PairMarkovModel m1) {
/* 221 */     this(m1.m1, m1.num_copies);
/* 222 */     this.emissionStateSpace = m1.emissionStateSpace;
/*     */   }
/*     */   
/*     */ 
/*     */   public PairEmissionState constructPair(List st1)
/*     */   {
/* 228 */     return new PairEmissionState(st1, this);
/*     */   }
/*     */   
/*     */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly)
/*     */   {
/* 233 */     for (int i = 0; i < this.m1.length; i++) {
/* 234 */       this.m1[i].setRandomTransitions(u, restart, lastOnly);
/*     */     }
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 240 */     for (int i = 0; i < this.m1.length; i++) {
/* 241 */       this.m1[i].initialiseTransitionCounts();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public double getTransitionScore(int from, int to, int endIndex)
/*     */   {
/* 250 */     updateFrom(from);
/* 251 */     updateTo(to);
/* 252 */     double sc = 1.0D;
/* 253 */     for (int j = 0; j < this.from1.length; j++) {
/* 254 */       sc *= this.m1[this.num_copies[j]].getTransitionScore(this.from1[j], this.to1[j], endIndex);
/*     */     }
/* 256 */     return sc;
/*     */   }
/*     */   
/*     */   boolean updateFrom(int from) {
/* 260 */     if (from == 0) {
/* 261 */       Arrays.fill(this.from1, from);
/* 262 */       return true;
/*     */     }
/*     */     
/* 265 */     PairEmissionState pes1 = (PairEmissionState)getState(from);
/* 266 */     for (int i = 0; i < this.num_copies.length; i++) {
/* 267 */       this.from1[i] = pes1.dist[i].index;
/*     */     }
/* 269 */     return false;
/*     */   }
/*     */   
/*     */   boolean updateTo(int to) {
/* 273 */     if (to == 0) {
/* 274 */       Arrays.fill(this.to1, to);
/* 275 */       return true;
/*     */     }
/*     */     
/* 278 */     PairEmissionState pes1 = (PairEmissionState)getState(to);
/* 279 */     for (int i = 0; i < this.num_copies.length; i++) {
/* 280 */       this.to1[i] = pes1.dist[i].index;
/*     */     }
/* 282 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void validate(int length)
/*     */     throws Exception
/*     */   {
/* 289 */     for (int i = 0; i < this.m1.length; i++) {
/* 290 */       this.m1[i].validate(length);
/*     */     }
/* 292 */     super.validate(length);
/*     */   }
/*     */   
/*     */   public void transform(StateDistribution[] transProbs, StateDistribution[][] singleLine, int i)
/*     */   {
/* 297 */     for (int j = 0; j < transProbs.length; j++) {
/* 298 */       StateDistribution trans = transProbs[j];
/* 299 */       if (trans != null) {
/* 300 */         updateFrom(j);
/* 301 */         for (int j1 = 0; j1 < modelLength(); j1++) {
/* 302 */           Double val = transProbs[j].get(j1);
/* 303 */           if (val.doubleValue() != 0.0D) {
/* 304 */             updateTo(j1);
/* 305 */             for (int k = 0; k < this.num_copies.length; k++) {
/* 306 */               int ind = this.from1[k];
/* 307 */               StateDistribution distr = singleLine[this.num_copies[k]][ind];
/* 308 */               if (distr == null) {
/* 309 */                 distr = new StateDistribution(this.m1[this.num_copies[k]].modelLength());
/* 310 */                 singleLine[this.num_copies[k]][ind] = distr;
/*     */               }
/* 312 */               distr.addCount(Integer.valueOf(this.to1[k]), val);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void addCounts(StateDistribution[] transProbs, int i) {
/* 321 */     StateDistribution[][] singleLine = new StateDistribution[this.m1.length][];
/* 322 */     for (int i1 = 0; i1 < singleLine.length; i1++) {
/* 323 */       singleLine[i1] = new StateDistribution[this.m1[i1].modelLength()];
/*     */     }
/*     */     
/*     */ 
/* 327 */     transform(transProbs, singleLine, i);
/*     */     
/* 329 */     for (int i1 = 0; i1 < singleLine.length; i1++) {
/* 330 */       this.m1[i1].addCounts(singleLine[i1], i);
/*     */     }
/*     */   }
/*     */   
/*     */   static void validate(List<StateDistribution> obs1, double total) {
/* 335 */     double sum = 0.0D;
/* 336 */     for (Iterator<StateDistribution> obs = obs1.iterator(); obs.hasNext();) {
/* 337 */       StateDistribution nxt = (StateDistribution)obs.next();
/* 338 */       if (nxt != null)
/* 339 */         sum += nxt.sum();
/*     */     }
/* 341 */     if (Math.abs(sum - total) > 0.001D) {
/* 342 */       if (Math.abs(sum - total) > 0.1D) throw new RuntimeException("!! " + sum);
/* 343 */       for (Iterator<StateDistribution> obs = obs1.iterator(); obs.hasNext();) {
/* 344 */         StateDistribution nxt = (StateDistribution)obs.next();
/* 345 */         if (nxt != null) {
/* 346 */           nxt.multiplyValues(sum / total);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double totalTransitionDistance(MarkovModel model)
/*     */   {
/* 356 */     PairMarkovModel pmm = (PairMarkovModel)model;
/* 357 */     return this.m1[0].totalTransitionDistance(pmm.m1[0]);
/*     */   }
/*     */   
/*     */ 
/*     */   public void print(PrintWriter pw)
/*     */   {
/* 363 */     for (int i = 0; i < this.m1.length; i++) {
/* 364 */       this.m1[i].print(pw);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean transitionsChanged() {
/* 369 */     for (int i = 0; i < this.m1.length; i++) {
/* 370 */       if (this.m1[i].transitionsChanged()) return true;
/*     */     }
/* 372 */     return false;
/*     */   }
/*     */   
/*     */ 
/*     */   public void transitionsFixed()
/*     */   {
/* 378 */     for (int i = 0; i < this.m1.length; i++) {
/* 379 */       this.m1[i].transitionsFixed();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 387 */   final int[] in0 = new int[1];
/*     */   
/*     */   public int[] statesIn(int j, int i) {
/* 390 */     if (i == 0) return this.in0;
/* 391 */     return this.in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i) {
/* 395 */     if (i == this.noSnps - 1) return this.in0;
/* 396 */     return this.in;
/*     */   }
/*     */   
/*     */   public MarkovModel getMarkovModel(int i)
/*     */   {
/* 401 */     return this.m1[this.num_copies[i]];
/*     */   }
/*     */   
/*     */   public int noCopies()
/*     */   {
/* 406 */     return this.num_copies.length;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/PairMarkovModel.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */