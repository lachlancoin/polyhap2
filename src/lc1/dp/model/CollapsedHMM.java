/*     */ package lc1.dp.model;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.illumina.IlluminaProbB;
/*     */ import lc1.dp.states.CachedEmissionState;
/*     */ import lc1.dp.states.CompoundState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.PseudoDistribution;
/*     */ import lc1.stats.SimpleExtendedDistribution;
/*     */ import lc1.stats.StateDistribution;
/*     */ import lc1.util.Constants;
/*     */ 
/*     */ public class CollapsedHMM extends CompoundMarkovModel implements WrappedModel
/*     */ {
/*     */   public CompoundMarkovModel hmm;
/*     */   public final int[] collapse;
/*     */   
/*     */   public IlluminaProbB probB(int i)
/*     */   {
/*  25 */     return this.hmm.probB(i);
/*     */   }
/*     */   
/*  28 */   public final List<int[]> expand = new ArrayList();
/*     */   final int[] in;
/*     */   
/*     */   public CompoundMarkovModel getHMM() {
/*  32 */     return this.hmm;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public boolean trainEmissions()
/*     */   {
/*  39 */     return this.hmm.trainEmissions();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public PairMarkovModel unwrapModel()
/*     */   {
/*  46 */     CompoundMarkovModel hmm = getHMM();
/*  47 */     while ((hmm instanceof WrappedModel)) {
/*  48 */       hmm = ((WrappedModel)hmm).getHMM();
/*     */     }
/*  50 */     return (PairMarkovModel)hmm;
/*     */   }
/*     */   
/*     */   public void splitCounts()
/*     */   {
/*  55 */     for (int j = 1; j < this.expand.size(); j++) {
/*  56 */       CachedEmissionState ges = (CachedEmissionState)getState(j);
/*  57 */       PseudoDistribution[] emissions = ges.emissions;
/*  58 */       int[] ex = (int[])this.expand.get(j);
/*  59 */       PseudoDistribution[][] l = new SimpleExtendedDistribution[ex.length][];
/*  60 */       for (int k = 0; k < ex.length; k++) {
/*  61 */         l[k] = ((CachedEmissionState)getState(ex[k])).emissions;
/*     */       }
/*  63 */       for (int i = 0; i < emissions.length; i++) {
/*  64 */         double[] count = emissions[i].counts();
/*  65 */         for (int i1 = 0; i1 < count.length; i1++) {
/*  66 */           double cnt = count[i1] / ex.length;
/*  67 */           for (int k = 0; k < ex.length; k++) {
/*  68 */             l[k][i].setCounts(i1, cnt);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public CollapsedHMM(CompoundMarkovModel hmm)
/*     */   {
/*  77 */     super(hmm.getName() + "f", hmm.noSnps.intValue(), null);
/*  78 */     this.hmm = hmm;
/*  79 */     this.collapse = new int[hmm.states.size()];
/*  80 */     this.collapse[0] = 0;
/*  81 */     this.expand.add(new int[1]);
/*  82 */     boolean allOneLength = true;
/*  83 */     for (Iterator<int[]> it = hmm.equivalenceClasses.iterator(); it.hasNext();) {
/*  84 */       int[] equiv = (int[])it.next();
/*  85 */       if (equiv.length > 1) allOneLength = false;
/*  86 */       State st = addState(hmm.getState(equiv[0]));
/*  87 */       for (int i = 0; i < equiv.length; i++) {
/*  88 */         this.collapse[equiv[i]] = st.getIndex();
/*     */       }
/*  90 */       if (st.getIndex() != this.expand.size()) throw new RuntimeException("!!");
/*  91 */       this.expand.add(equiv);
/*     */     }
/*  93 */     this.in = new int[this.states.size() - 1];
/*  94 */     for (int jk = 1; jk < this.states.size(); jk++) {
/*  95 */       this.in[(jk - 1)] = jk;
/*     */     }
/*  97 */     if (allOneLength) { throw new RuntimeException("do not need collapsedHMM");
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 105 */     Logger.global.info("state space size for collapsed " + this.states.size() + " for " + getName());
/*     */   }
/*     */   
/*     */ 
/*     */   public void addCounts(StateDistribution[] transProbs, int i, int numIndiv)
/*     */   {
/* 111 */     StateDistribution[] transProbs1 = new StateDistribution[this.hmm.states.size()];
/* 112 */     for (int j = 0; j < this.hmm.states.size(); j++) {
/* 113 */       transProbs1[j] = new StateDistribution(this.hmm.states.size());
/*     */     }
/* 115 */     for (int j = 0; j < transProbs.length; j++) {
/* 116 */       for (int k = 0; k < transProbs[j].dist.length; k++) {
/* 117 */         int[] from = (int[])this.expand.get(j);
/* 118 */         double[][] prob = new double[from.length][];
/* 119 */         double sum = 0.0D;
/* 120 */         double value = transProbs[j].dist[k];
/* 121 */         if (value != 0.0D) {
/* 122 */           int[] to = (int[])this.expand.get(k);
/* 123 */           for (int m = 0; m < from.length; m++) {
/* 124 */             prob[m] = new double[to.length];
/* 125 */             for (int n = 0; n < to.length; n++) {
/* 126 */               prob[m][n] = this.hmm.getTransitionScore(from[m], to[n], i + this.hmm.getState(k).adv);
/* 127 */               sum += prob[m][n];
/*     */             }
/*     */           }
/*     */           
/*     */ 
/* 132 */           for (int m = 0; m < from.length; m++) {
/* 133 */             for (int n = 0; n < to.length; n++) {
/* 134 */               transProbs1[from[m]].dist[to[n]] += value * (sum == 0.0D ? 1.0D : prob[m][n] / sum);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 140 */     double diff = sum(transProbs) - sum(transProbs1);
/* 141 */     if (Math.abs(diff) > 0.01D) {
/* 142 */       throw new RuntimeException("!! " + diff);
/*     */     }
/*     */     
/*     */ 
/* 146 */     this.hmm.addCounts(transProbs1, i, numIndiv);
/*     */   }
/*     */   
/*     */   public String toString(int[] d)
/*     */   {
/* 151 */     Integer[] d1 = new Integer[d.length];
/* 152 */     for (int i = 0; i < d.length; i++) {
/* 153 */       d1[i] = Integer.valueOf(d[i]);
/*     */     }
/* 155 */     return Arrays.asList(d1).toString();
/*     */   }
/*     */   
/* 158 */   public void refresh() { this.hmm.refresh(); }
/*     */   
/*     */   private double sum(StateDistribution[] transProbs) {
/* 161 */     double sum = 0.0D;
/* 162 */     for (int i = 0; i < transProbs.length; i++) {
/* 163 */       sum += transProbs[i].sum();
/*     */     }
/* 165 */     return sum;
/*     */   }
/*     */   
/*     */   public Object clone(boolean swtch)
/*     */   {
/* 170 */     return null;
/*     */   }
/*     */   
/*     */   public double getTransitionScore(int from, int to, int positionOfToEmission)
/*     */   {
/* 175 */     int[] frome = (int[])this.expand.get(from);
/* 176 */     int[] toe = (int[])this.expand.get(to);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 186 */     double sc = 0.0D;
/*     */     
/* 188 */     for (int j = 0; j < toe.length; j++) {
/* 189 */       sc += this.hmm.getTransitionScore(frome[0], toe[j], positionOfToEmission);
/*     */     }
/*     */     
/* 192 */     return sc;
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
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 211 */     this.hmm.initialiseTransitionCounts();
/*     */   }
/*     */   
/*     */ 
/*     */   public void setPseudoCountWeights(double[] d, double[] d1)
/*     */   {
/* 217 */     this.hmm.setPseudoCountWeights(d, d1);
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
/*     */   public void transferCountsToProbs(int index)
/*     */   {
/* 231 */     this.hmm.transferCountsToProbs(index);
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 240 */   final int[] in0 = new int[1];
/*     */   
/*     */   public int[] statesIn(int j, int i) {
/* 243 */     if (i == 0) return this.in0;
/* 244 */     return this.in;
/*     */   }
/*     */   
/*     */   public int[] statesOut(int j, int i) {
/* 248 */     if (i == this.noSnps.intValue() - 1) return this.in0;
/* 249 */     return this.in;
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
/*     */   public MarkovModel getMarkovModel(int i)
/*     */   {
/* 276 */     return this.hmm.getMarkovModel(i);
/*     */   }
/*     */   
/*     */   public int noCopies()
/*     */   {
/* 281 */     return this.hmm.noCopies();
/*     */   }
/*     */   
/*     */ 
/*     */   public EmissionState[] disambiguate(EmissionState[] state, EmissionState[] previous, int positionOfToEmiss, boolean sample)
/*     */   {
/* 287 */     if (previous == null) return state;
/* 288 */     int[] possibilities = (int[])this.expand.get(this.hmm.getCompoundState(state).getIndex());
/* 289 */     if (possibilities.length == 1) return state;
/* 290 */     int index = this.hmm.getCompoundState(previous).getIndex();
/* 291 */     double[] prob = new double[possibilities.length];
/* 292 */     double sum = 0.0D;
/* 293 */     for (int i = 0; i < prob.length; i++) {
/* 294 */       prob[i] = this.hmm.getTransitionScore(index, possibilities[i], positionOfToEmiss);
/* 295 */       sum += prob[i];
/*     */     }
/* 297 */     int chosen = sample ? Constants.sample(prob, sum) : Constants.getMax(prob);
/* 298 */     EmissionState[] res = ((CompoundState)this.hmm.getState(possibilities[chosen])).getMemberStates(false);
/*     */     
/*     */ 
/*     */ 
/* 302 */     return this.hmm.disambiguate(res, previous, index, sample);
/*     */   }
/*     */   
/*     */ 
/*     */   public CompoundState getCompoundState(EmissionState[] res)
/*     */   {
/* 308 */     return this.hmm.getCompoundState(res);
/*     */   }
/*     */   
/*     */ 
/*     */   public MarkovModel[] getMemberModels()
/*     */   {
/* 314 */     return this.hmm.getMemberModels();
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/model/CollapsedHMM.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */