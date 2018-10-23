/*     */ package lc1.dp.core;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Callable;
/*     */ import java.util.concurrent.ExecutorService;
/*     */ import java.util.concurrent.Executors;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.model.CachedHMM;
/*     */ import lc1.dp.model.CompoundMarkovModel;
/*     */ import lc1.dp.model.MarkovModel;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.HaplotypeEmissionState;
/*     */ import lc1.dp.states.IlluminaNoBg;
/*     */ import lc1.dp.states.MergedEmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.SimpleDistribution;
/*     */ import lc1.stats.SkewNormal;
/*     */ import lc1.stats.StateDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BaumWelchTrainer
/*     */ {
/*  38 */   public static ExecutorService es = Executors.newFixedThreadPool(Constants.numThreads());
/*     */   
/*  40 */   public static void involeTasks(List l, boolean seq) throws Exception { if ((!seq) && (Constants.numThreads() > 1)) { es.invokeAll(l);
/*     */     }
/*     */     else {
/*  43 */       for (Iterator it = l.iterator(); it.hasNext();) {
/*     */         try {
/*  45 */           ((Callable)it.next()).call();
/*     */         } catch (Exception exc) {
/*  47 */           exc.printStackTrace();
/*  48 */           System.exit(0);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public MarkovModel hmm;
/*     */   final double[] logprob;
/*     */   public EmissionState[] data;
/*  58 */   int firstNonZeroIndex = -1;
/*     */   DP[] dp;
/*     */   double[] weight;
/*     */   final int seqLength;
/*     */   
/*     */   public Callable init(final int j)
/*     */   {
/*  65 */     new Callable() {
/*     */       public Object call() {
/*  67 */         BaumWelchTrainer.this.dp[j] = new DP(BaumWelchTrainer.this.hmm, "", BaumWelchTrainer.this.data[j], false);
/*  68 */         BaumWelchTrainer.this.dp[j].reset(true);
/*  69 */         return null;
/*     */       }
/*     */     };
/*     */   }
/*     */   
/*  74 */   public boolean trainDists = false;
/*     */   
/*  76 */   public BaumWelchTrainer(MarkovModel hmm, EmissionState[] obj, double[] weight) { this.hmm = hmm;
/*  77 */     this.pcs = new PropertyChangeSupport(this);
/*  78 */     this.data = obj;
/*  79 */     this.emissionCount = new StateDistribution[obj.length];
/*  80 */     this.logprob = new double[obj.length];
/*  81 */     for (int l = 0; l < this.emissionCount.length; l++) {
/*  82 */       this.emissionCount[l] = new StateDistribution(hmm.modelLength());
/*     */     }
/*  84 */     this.dp = new DP[obj.length];
/*  85 */     this.weight = weight;
/*     */     
/*  87 */     this.seqLength = obj[0].length();
/*  88 */     this.transProbs = new StateDistribution[this.seqLength + 1][hmm.modelLength()];
/*  89 */     List tasks = new ArrayList();
/*  90 */     for (int j = 0; j < obj.length; j++) {
/*  91 */       if ((!this.trainDists) && (((HaplotypeEmissionState)obj[j]).hasIlluminaDist())) {
/*  92 */         this.trainDists = true;
/*     */       }
/*  94 */       if ((weight[j] > 1.0E-10D) || (Constants.r_train() < 1.0E7D) || (Constants.b_train() < 1.0E7D)) {
/*  95 */         tasks.add(init(j));
/*  96 */         if (this.firstNonZeroIndex < 0) this.firstNonZeroIndex = j;
/*     */       }
/*     */     }
/*     */     try {
/* 100 */       involeTasks(tasks, false);
/*     */     }
/*     */     catch (Exception exc) {
/* 103 */       exc.printStackTrace();
/*     */     }
/* 105 */     for (int j = 0; j < hmm.modelLength(); j++) {
/* 106 */       for (int i = 0; i < this.seqLength + 1; i++) {
/* 107 */         this.transProbs[i][j] = new StateDistribution(hmm.modelLength());
/*     */       }
/*     */     }
/*     */     
/* 111 */     this.probDists = new ProbDists[Constants.format().length];
/*     */     
/* 113 */     CompoundMarkovModel cmm = (CompoundMarkovModel)hmm;
/* 114 */     for (int i = 0; i < this.probDists.length; i++) {
/* 115 */       this.probDists[i] = new ProbDists();
/*     */       
/* 117 */       cmm.probB(i).probDists(this.probDists[i].s2, this.probDists[i].probBIndices);
/* 118 */       cmm.probR(this.probDists[i].stateIndices, this.probDists[i].stateIndexCNV, this.probDists[i].s1, this.probDists[i].s1_memb, i);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 124 */       if (Constants.trainEnsemble() > 0) {
/* 125 */         this.probDists[i].mvf = new ProbMultivariate(this.probDists[i].s1, this.probDists[i].s1_memb);
/* 126 */         this.probDists[i].initColors();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   final StateDistribution[][] transProbs;
/*     */   
/*     */   public double validate(Collection<StateDistribution> dist, int no)
/*     */   {
/* 137 */     double sum = 0.0D;
/*     */     
/* 139 */     for (Iterator<StateDistribution> it = dist.iterator(); it.hasNext();) {
/* 140 */       StateDistribution d = (StateDistribution)it.next();
/* 141 */       if (d != null) {
/* 142 */         double s = d.sum();
/* 143 */         sum += s;
/*     */       }
/*     */     }
/* 146 */     if (Math.abs(sum / no - 1.0D) > 0.01D) {
/* 147 */       if (Math.abs(sum / no - 1.0D) > 0.3D) {
/* 148 */         throw new RuntimeException("sum not right " + sum + " " + no);
/*     */       }
/*     */       
/* 151 */       Logger.global.warning("sum was not 1 " + sum * no + ".  Normalising !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
/* 152 */       for (Iterator<StateDistribution> it = dist.iterator(); it.hasNext();) {
/* 153 */         StateDistribution d = (StateDistribution)it.next();
/* 154 */         if (d != null) {
/* 155 */           d.multiplyValues(no / sum);
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 161 */     return sum;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addTransitionProbSum(int j)
/*     */     throws Exception
/*     */   {
/* 169 */     for (int i = -1; i < this.seqLength; i++) {
/* 170 */       this.dp[j].addTransitionPosterior(i, this.transProbs[(i + 1)], this.weight[j]);
/*     */     }
/*     */   }
/*     */   
/*     */   public void validateCounts(int total)
/*     */   {
/* 176 */     for (int i = -1; i < this.seqLength; i++) {
/*     */       try
/*     */       {
/* 179 */         double sum = validate((Collection)Arrays.asList(this.transProbs[(i + 1)]), this.data.length);
/* 180 */         if (Math.abs(sum / total - 1.0D) <= 0.01D) continue; throw new RuntimeException("!! " + sum + " " + total);
/*     */       } catch (Exception exc) {
/* 182 */         exc.printStackTrace();
/* 183 */         System.exit(0);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void compare(Map<State, SimpleDistribution> dist1, Map<State, SimpleDistribution> dist2)
/*     */   {
/* 191 */     for (Iterator<State> it = dist1.keySet().iterator(); it.hasNext();) {
/* 192 */       State k = (State)it.next();
/* 193 */       SimpleDistribution d1 = (SimpleDistribution)dist1.get(k);
/* 194 */       SimpleDistribution d2 = (SimpleDistribution)dist2.get(k);
/* 195 */       if (d1.different(d2)) {
/* 196 */         Logger.getAnonymousLogger().info("changed " + k + "-->" + "\n" + d1.toString() + " to " + d2.toString());
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Callable expectationStep(final int j1, final double[] pseudo, final boolean last) {
/* 202 */     Callable run = new Callable() {
/*     */       public Object call() {
/* 204 */         double time = System.currentTimeMillis();
/* 205 */         BaumWelchTrainer.this.dp[j1].reset(false);
/* 206 */         BaumWelchTrainer.this.logprob[j1] = 
/* 207 */           BaumWelchTrainer.this.dp[j1].search(true);
/* 208 */         Object[] tmp = { Integer.valueOf(j1), BaumWelchTrainer.this.dp[j1] };
/* 209 */         BaumWelchTrainer.this.firePropertyChange("expec_i", null, tmp);
/* 210 */         if (Double.isInfinite(BaumWelchTrainer.this.logprob[j1])) throw new RuntimeException("is infinite!");
/*     */         try {
/* 212 */           BaumWelchTrainer.this.addTransitionProbSum(j1);
/* 213 */           if ((pseudo[BaumWelchTrainer.emiss_col] < 1000.0D) || (pseudo[BaumWelchTrainer.data_col] < 1000.0D)) {
/* 214 */             BaumWelchTrainer.this.addEmissionProbSum(Integer.valueOf(j1), last);
/*     */           }
/*     */         }
/*     */         catch (Exception exc) {
/* 218 */           exc.printStackTrace();
/* 219 */           System.exit(0);
/*     */         }
/* 221 */         return Double.valueOf(BaumWelchTrainer.this.logprob[j1]);
/*     */       }
/* 223 */     };
/* 224 */     return run;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 230 */   public static int emiss_col = 0;
/* 231 */   public static int data_col = 3;
/*     */   
/*     */ 
/* 234 */   public static Long[] t = new Long[12];
/*     */   
/* 236 */   static String tPrint = "%5i %5i %5i %5i %5i %5i %5i %5i %5i %5i %5i %5i";
/*     */   public static long t1;
/*     */   final StateDistribution[] emissionCount;
/*     */   static final boolean ML = false;
/*     */   
/* 241 */   public double expectationStep(double[] pseudo, boolean last) { for (int i = 0; i < this.seqLength + 1; i++) {
/* 242 */       for (int j = 0; j < this.hmm.modelLength(); j++) {
/* 243 */         this.transProbs[i][j].reset();
/*     */       }
/*     */     }
/*     */     
/* 247 */     double logprob = 0.0D;
/* 248 */     Arrays.fill(t, Long.valueOf(0L));
/* 249 */     List tasks = new ArrayList();
/* 250 */     for (int j = 0; j < this.data.length; j++) {
/* 251 */       if ((this.weight[j] >= 1.0E-9D) || ((pseudo[3] <= 1000.0D) && ((Constants.r_train() <= 1.0E7D) || (Constants.b_train() <= 1.0E7D)))) {
/* 252 */         tasks.add(expectationStep(j, pseudo, last));
/* 253 */         t1 = System.currentTimeMillis();
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */     try
/*     */     {
/* 261 */       involeTasks(tasks, false);
/*     */       
/*     */ 
/* 264 */       for (int j = 0; j < this.data.length; j++) {
/* 265 */         logprob += this.weight[j] * this.logprob[j];
/*     */       }
/* 267 */       firePropertyChange("expectation", null, this);
/*     */     } catch (Exception exc) {
/* 269 */       exc.printStackTrace();
/*     */     }
/* 271 */     int tmp226_225 = 3; Long[] tmp226_222 = t;tmp226_222[tmp226_225] = Long.valueOf(tmp226_222[tmp226_225].longValue() - t[2].longValue()); int 
/* 272 */       tmp248_247 = 2; Long[] tmp248_244 = t;tmp248_244[tmp248_247] = Long.valueOf(tmp248_244[tmp248_247].longValue() - t[1].longValue()); int 
/* 273 */       tmp270_269 = 1; Long[] tmp270_266 = t;tmp270_266[tmp270_269] = Long.valueOf(tmp270_266[tmp270_269].longValue() - t[0].longValue());
/* 274 */     t1 = System.currentTimeMillis();
/* 275 */     if (this.firstNonZeroIndex < 0) return logprob;
/* 276 */     for (int k = 0; k < this.hmm.modelLength(); k++) {
/* 277 */       for (int i = -1; i < this.seqLength; i++) {
/* 278 */         StateDistribution[] dist_i = this.transProbs[(i + 1)];
/* 279 */         StateDistribution dist_ik = dist_i[k];
/* 280 */         for (int j = 0; j < this.hmm.modelLength(); j++) {
/* 281 */           dist_ik.dist[j] *= this.hmm.getTransitionScore(k, j, i + this.dp[this.firstNonZeroIndex].adv[j]);
/*     */         }
/*     */       }
/*     */     }
/* 285 */     t[4] = Long.valueOf(System.currentTimeMillis() - t1);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/* 290 */     tasks.clear();
/*     */     
/* 292 */     for (int i = -1; i < this.seqLength; i++) {
/* 293 */       this.hmm.addCounts(this.transProbs[(i + 1)], i, this.data.length);
/*     */     }
/*     */     
/* 296 */     t[5] = Long.valueOf(System.currentTimeMillis() - t1 - t[4].longValue());
/* 297 */     if ((this.hmm instanceof CachedHMM)) {
/* 298 */       ((CachedHMM)this.hmm).transferEmissionCountsToMemberStates();
/*     */     }
/*     */     
/* 301 */     t[6] = Long.valueOf(System.currentTimeMillis() - t1 - t[4].longValue() - t[5].longValue());
/* 302 */     return logprob;
/*     */   }
/*     */   
/* 305 */   public static boolean maximisationStep(MarkovModel hmm, final int index, List tasks) { tasks.add(new Callable() {
/*     */       public Object call() {
/* 307 */         BaumWelchTrainer.this.transferCountsToProbs(index);
/*     */         
/* 309 */         return null;
/*     */       }
/* 311 */     });
/* 312 */     t[(t.length - 1)] = Long.valueOf(System.currentTimeMillis());
/* 313 */     System.err.println(Format.sprintf(tPrint, t));
/* 314 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void addEmissionProbSum(Integer l, boolean last)
/*     */   {
/* 321 */     Object[] tmp = new Object[3];
/* 322 */     Arrays.fill(tmp, null);
/* 323 */     tmp[1] = l;
/*     */     
/* 325 */     firePropertyChange("init", null, tmp);
/*     */     
/* 327 */     for (int i = 0; i < this.seqLength; i++) {
/* 328 */       this.emissionCount[l.intValue()].reset();
/* 329 */       tmp[2] = Integer.valueOf(i);
/*     */       
/*     */ 
/* 332 */       this.dp[l.intValue()].getPosterior(i, this.emissionCount[l.intValue()]);
/* 333 */       tmp[0] = this.emissionCount[l.intValue()];
/* 334 */       firePropertyChange("emiss", null, tmp);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 349 */       for (int j = 0; j < this.hmm.modelLength(); j++)
/*     */       {
/* 351 */         if (this.dp[l.intValue()].emiss[j] != 0) {
/* 352 */           EmissionState k = (EmissionState)this.hmm.getState(j);
/* 353 */           double val = this.emissionCount[l.intValue()].get(j).doubleValue();
/* 354 */           if (val > Constants.bwThresh()) {
/* 355 */             this.data[l.intValue()].addCount(k, Double.valueOf(val), i, this.weight[l.intValue()]);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/* 361 */     firePropertyChange("finished", null, tmp);
/*     */   }
/*     */   
/*     */   private double sum(Iterator<SimpleDistribution> it) {
/* 365 */     double sum = 0.0D;
/* 366 */     while (it.hasNext()) {
/* 367 */       sum += ((SimpleDistribution)it.next()).sum();
/*     */     }
/* 369 */     return sum;
/*     */   }
/*     */   
/*     */ 
/* 373 */   final List<Integer> copyNumber = new ArrayList();
/*     */   
/*     */   public final ProbDists[] probDists;
/*     */   
/*     */   double[] condb;
/*     */   
/*     */   final PropertyChangeSupport pcs;
/*     */   
/*     */   public void maximisationStep(int i, double pseudo, List tasks)
/*     */   {
/* 383 */     if (this.trainDists)
/*     */     {
/* 385 */       double[] mvst = Constants.meanvarskewprior();
/* 386 */       if (Constants.r_train() < 1000.0D) {
/* 387 */         for (int ik = 0; ik < this.probDists.length; ik++) {
/* 388 */           this.probDists[ik].getTasks(tasks, mvst, pseudo);
/*     */         }
/*     */       }
/*     */       
/* 392 */       if (Constants.b_train() < 1000.0D)
/*     */       {
/* 394 */         for (int ik = 0; ik < this.probDists.length; ik++) {
/* 395 */           this.probDists[ik].getBTasks(tasks, pseudo);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public static final List<SkewNormal> extract(Map<String, List<SkewNormal>> m)
/*     */   {
/* 406 */     List<SkewNormal> res = new ArrayList();
/* 407 */     for (Iterator<List<SkewNormal>> it = m.values().iterator(); it.hasNext();) {
/* 408 */       res.add((SkewNormal)((List)it.next()).get(0));
/*     */     }
/* 410 */     return res;
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
/*     */   public static final void train(List<SkewNormal> m, final double pseudo, List tasks)
/*     */   {
/* 429 */     double[] meanvarskew = Constants.meanvarskewprior();
/*     */     
/* 431 */     for (Iterator<SkewNormal> it = m.iterator(); it.hasNext();) {
/* 432 */       SkewNormal pdist0 = (SkewNormal)it.next();
/* 433 */       tasks.add(new Callable()
/*     */       {
/*     */         public Object call() {
/* 436 */           BaumWelchTrainer.this.maximise(pseudo * this.val$meanvarskew[0], pseudo * this.val$meanvarskew[1], pseudo * this.val$meanvarskew[2]);
/* 437 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public IlluminaNoBg getState(int i, int index)
/*     */   {
/* 447 */     EmissionState state = this.data[i];
/* 448 */     if ((state instanceof MergedEmissionState)) {
/* 449 */       return (IlluminaNoBg)((MergedEmissionState)state).em[index];
/*     */     }
/*     */     
/* 452 */     return (IlluminaNoBg)state;
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
/*     */   public void addPropertyChangeListener(PropertyChangeListener arg0)
/*     */   {
/* 467 */     this.pcs.addPropertyChangeListener(arg0);
/*     */   }
/*     */   
/*     */   public void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
/* 471 */     this.pcs.firePropertyChange(propertyName, oldValue, newValue);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public Set<Integer> getDataIndices()
/*     */   {
/* 479 */     Set<Integer> s = new HashSet();
/* 480 */     for (int i = 0; i < this.data.length; i++) {
/* 481 */       s.add(Integer.valueOf(((HaplotypeEmissionState)this.data[i]).dataIndex()));
/*     */     }
/* 483 */     return s;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/BaumWelchTrainer.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */