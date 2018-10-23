/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import lc1.dp.Dirichlet;
/*     */ import lc1.dp.SimpleDistribution;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.TransitionProbs;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SiteTransitions
/*     */   implements Serializable
/*     */ {
/*     */   State MAGIC;
/*     */   public TransitionProbs[] transProbs;
/*     */   
/*  26 */   public SiteTransitions(int length) { this.transProbs = new TransitionProbs[length]; }
/*     */   
/*     */   public void setTransitionScore(int from, int to, int indexOfToEmission, double d, double d_ps) {
/*  29 */     if (to == 0) { return;
/*     */     }
/*  31 */     SimpleExtendedDistribution dist = this.transProbs[indexOfToEmission].transitionsOut[from];
/*  32 */     dist.probs[to] = d;
/*  33 */     dist.pseudo[to] = d_ps;
/*     */   }
/*     */   
/*     */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly) {
/*  37 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  38 */       this.transProbs[i].setRandom(u, restart, lastOnly);
/*     */     }
/*     */   }
/*     */   
/*     */   public void initialiseTransitionCounts() {
/*  43 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  44 */       this.transProbs[i].initialiseCounts(i == 0, i == this.transProbs.length - 1);
/*     */     }
/*     */   }
/*     */   
/*     */   public void transitionsFixed() {
/*  49 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  50 */       this.transProbs[i].setModelChanged(false);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean transitionsChanged() {
/*  55 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  56 */       if (this.transProbs[i].modelChanged()) return true;
/*     */     }
/*  58 */     return false;
/*     */   }
/*     */   
/*  61 */   public Collection<PseudoDistribution> getDistributions(int i) { return this.transProbs[i].getDistributions(); }
/*     */   
/*     */   public void transferTransitions(double pseudocountWeights) {
/*  64 */     for (int i = 0; i < this.transProbs.length - 1; i++)
/*     */     {
/*  66 */       this.transProbs[i].transfer(pseudocountWeights);
/*     */     }
/*     */   }
/*     */   
/*     */   public Double[] getTrans(int from, int to)
/*     */   {
/*  72 */     Double[] d = new Double[this.transProbs.length];
/*  73 */     d[0] = Double.valueOf(this.transProbs[0].getTransition(0, to));
/*  74 */     for (int i = 1; i < d.length - 1; i++) {
/*  75 */       d[i] = Double.valueOf(this.transProbs[i].getTransition(from, to));
/*     */     }
/*  77 */     d[(d.length - 1)] = Double.valueOf(this.transProbs[(this.transProbs.length - 1)].getTransition(from, 0));
/*  78 */     return d;
/*     */   }
/*     */   
/*     */   public double totalTransitionDistance(SiteTransitions trans) {
/*  82 */     double sum = 0.0D;
/*  83 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  84 */       sum += this.transProbs[i].transitionDistance(trans.transProbs[i]);
/*     */     }
/*  86 */     return sum;
/*     */   }
/*     */   
/*     */   public SiteTransitions(SiteTransitions trans_init, SiteTransitions trans_ps, List<State> states)
/*     */   {
/*  91 */     this(trans_init.transProbs.length);
/*  92 */     for (int i = 0; i < this.transProbs.length; i++) {
/*  93 */       this.transProbs[i] = new TransitionProbs(trans_init.transProbs[i], trans_ps.transProbs[i], states);
/*     */     }
/*  95 */     this.MAGIC = ((State)states.get(0));
/*     */   }
/*     */   
/*  98 */   public double getTransitionScore(int from, int to, int indexOfToEmission) { if ((from == 0) && (to == 0)) return 0.0D;
/*  99 */     int id = to == 0 ? indexOfToEmission + 1 : indexOfToEmission;
/* 100 */     if (id >= this.transProbs.length) {
/* 101 */       if (to == 0) return 1.0D;
/* 102 */       return 0.0D;
/*     */     }
/* 104 */     double d = this.transProbs[id].getTransition(from, to);
/* 105 */     return d;
/*     */   }
/*     */   
/* 108 */   public void validate() { for (int i = 0; i < this.transProbs.length; i++)
/* 109 */       this.transProbs[i].validate();
/*     */   }
/*     */   
/*     */   public static SimpleDistribution getPseudoDist(List<State> states) {
/* 113 */     double frac = 0.7D;
/* 114 */     SimpleDistribution pseudoCounts = new SimpleDistribution();
/* 115 */     double cum = 0.0D;
/* 116 */     for (int j = 1; 
/* 117 */         j < states.size() - 1; j++) {
/* 118 */       double d = frac * (1.0D - cum);
/* 119 */       pseudoCounts.put(states.get(j), d);
/* 120 */       cum += d;
/*     */     }
/* 122 */     pseudoCounts.put(states.get(j), 1.0D - cum);
/* 123 */     return pseudoCounts;
/*     */   }
/*     */   
/* 126 */   public void initialise(List<State> states, double u) { int numFounders = states.size() - 1;
/* 127 */     this.MAGIC = ((State)states.get(0));
/* 128 */     Double[] d = new Double[numFounders + 1];
/* 129 */     double inv_col = 1.0D / numFounders;
/* 130 */     Arrays.fill(d, Double.valueOf(inv_col));
/* 131 */     d[0] = Double.valueOf(0.0D);
/* 132 */     Dirichlet dir = new Dirichlet(d, u);
/*     */     
/*     */ 
/* 135 */     for (int i = 0; i < this.transProbs.length; i++) {
/* 136 */       this.transProbs[i] = new TransitionProbs(states, i == 0, dir);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void increaseStates(double small, int newStatesLength, int newModelLength, double u)
/*     */   {
/* 143 */     Double[] d = new Double[newModelLength];
/* 144 */     Arrays.fill(d, Double.valueOf(1.0D / (newModelLength - 1)));
/* 145 */     d[0] = Double.valueOf(0.0D);
/* 146 */     Double[] d1 = new Double[newStatesLength];
/* 147 */     Arrays.fill(d1, Double.valueOf(1.0D / newStatesLength));
/* 148 */     Dirichlet allStates = new Dirichlet(d, u);
/* 149 */     Dirichlet newStates = new Dirichlet(d1, u);
/* 150 */     this.transProbs[0].expand(newModelLength);
/* 151 */     this.transProbs[0].transitionsOut[0].extend(small, d, newStates);
/* 152 */     this.transProbs[0].validate();
/* 153 */     for (int i = 1; i < this.transProbs.length; i++) {
/* 154 */       this.transProbs[i].expand(newModelLength);
/* 155 */       for (int j = 1; j < newModelLength; j++)
/*     */       {
/* 157 */         SimpleExtendedDistribution dist = this.transProbs[i].transitionsOut[j];
/* 158 */         if (dist != null) { dist.extend(small, d, newStates);
/*     */         } else {
/* 160 */           this.transProbs[i].transitionsOut[j] = new SimpleExtendedDistribution(allStates);
/*     */         }
/*     */       }
/* 163 */       this.transProbs[i].validate();
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void setSelfSelf(HaplotypeEmissionState st, double d)
/*     */   {
/* 172 */     for (int i = 1; i < this.transProbs.length - 1; i++) {
/* 173 */       SimpleExtendedDistribution out = this.transProbs[i].transitionsOut[st.index];
/* 174 */       Double val = Double.valueOf(d / (1.0D - d));
/* 175 */       out.probs[st.index] = val.doubleValue();
/* 176 */       out.pseudo[st.index] = val.doubleValue();
/* 177 */       out.normalise();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/SiteTransitions.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */