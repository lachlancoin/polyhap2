/*     */ package lc1.dp.genotype;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.DP;
/*     */ import lc1.dp.Dirichlet;
/*     */ import lc1.dp.MarkovModel;
/*     */ import lc1.dp.State;
/*     */ import lc1.dp.StatePath;
/*     */ import lc1.dp.genotype.io.Constants;
/*     */ import lc1.dp.genotype.io.scorable.StateScorableObject;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExponentialHaplotypeHMM
/*     */   extends HaplotypeHMM
/*     */ {
/*     */   SimpleExtendedDistribution[] alpha;
/*     */   final SimpleExtendedDistribution[] exp_rd;
/*  32 */   final boolean trainAlpha = true;
/*  33 */   final boolean trainRd = true;
/*  34 */   boolean transChanged = true;
/*     */   
/*  36 */   public ExponentialHaplotypeHMM(ExponentialHaplotypeHMM hmm_init, ExponentialHaplotypeHMM hmm_pseudo, double pseudocount) { super(hmm_init, hmm_pseudo);
/*  37 */     this.exp_rd = new SimpleExtendedDistribution[this.noSnps];
/*  38 */     this.alpha = new SimpleExtendedDistribution[this.noSnps];
/*  39 */     for (int i = 0; i < this.noSnps; i++) {
/*  40 */       this.alpha[i] = 
/*  41 */         (hmm_pseudo == null ? new SimpleExtendedDistribution(hmm_init.alpha[i]) : 
/*  42 */         new SimpleExtendedDistribution(hmm_init.alpha[i], hmm_pseudo.alpha[i]));
/*     */       
/*  44 */       this.exp_rd[i] = (hmm_pseudo == null ? new SimpleExtendedDistribution(hmm_init.exp_rd[i]) : 
/*  45 */         new SimpleExtendedDistribution(hmm_init.exp_rd[i], hmm_pseudo.exp_rd[i]));
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
/*     */   public ExponentialHaplotypeHMM(String name, int numFounders, int noSnps, double u)
/*     */   {
/*  61 */     super(name, numFounders, noSnps, u);
/*     */     
/*  63 */     this.exp_rd = new SimpleExtendedDistribution[noSnps];
/*  64 */     this.alpha = new SimpleExtendedDistribution[noSnps];
/*     */     
/*  66 */     double[] d = new double[numFounders + 1];
/*  67 */     double inv = 1.0D / numFounders;
/*  68 */     Arrays.fill(d, inv);
/*  69 */     d[0] = 0.0D;
/*  70 */     Dirichlet dir = new Dirichlet(d, u);
/*  71 */     for (int i = 0; i < noSnps; i++) {
/*  72 */       if (i > 0) {
/*  73 */         this.exp_rd[i] = new SimpleExtendedDistribution(new double[] { 0.9D, 0.1D }, Double.POSITIVE_INFINITY);
/*     */       } else
/*  75 */         this.exp_rd[i] = new SimpleExtendedDistribution(new double[] { 0.0D, 1.0D }, Double.POSITIVE_INFINITY);
/*  76 */       this.alpha[i] = new SimpleExtendedDistribution(dir);
/*     */     }
/*     */   }
/*     */   
/*     */   public void resetTrans() {
/*  81 */     int numFounders = modelLength() - 1;
/*  82 */     double[] d = new double[numFounders + 1];
/*  83 */     double inv = 1.0D / numFounders;
/*  84 */     Arrays.fill(d, inv);
/*  85 */     d[0] = 0.0D;
/*  86 */     for (int i = 0; i < this.noSnps; i++) {
/*  87 */       if (i > 0) {
/*  88 */         this.exp_rd[i] = new SimpleExtendedDistribution(new double[] { 0.5D, 0.5D }, Double.POSITIVE_INFINITY);
/*     */       } else
/*  90 */         this.exp_rd[i] = new SimpleExtendedDistribution(new double[] { 0.0D, 1.0D }, Double.POSITIVE_INFINITY);
/*  91 */       this.alpha[i] = new SimpleExtendedDistribution(d, Double.POSITIVE_INFINITY);
/*     */     }
/*     */   }
/*     */   
/*     */   public ExponentialHaplotypeHMM(String name, List<double[]> theta, List<double[]> alphas, double[] r)
/*     */   {
/*  97 */     super(name, ((double[])theta.get(0)).length, theta.size(), 1.0D);
/*  98 */     int numFounders = ((double[])theta.get(0)).length;
/*  99 */     this.exp_rd = new SimpleExtendedDistribution[this.noSnps];
/* 100 */     this.alpha = new SimpleExtendedDistribution[this.noSnps];
/* 101 */     for (int i = 0; i < this.noSnps; i++) {
/* 102 */       this.exp_rd[i] = new SimpleExtendedDistribution(new double[] { 1.0D - r[i], r[i] }, Double.POSITIVE_INFINITY);
/* 103 */       this.alpha[i] = new SimpleExtendedDistribution(numFounders + 1, 1.0D);
/* 104 */       double[] alpha_i = (double[])alphas.get(i);
/* 105 */       double[] theta_i = (double[])theta.get(i);
/* 106 */       for (int j = 0; j < numFounders; j++) {
/* 107 */         HaplotypeEmissionState state_j = (HaplotypeEmissionState)getState(j + 1);
/* 108 */         state_j.setTheta(new double[] { 0.0D, theta_i[j], 0.0D }, i);
/* 109 */         this.alpha[i].probs[state_j.index] = alpha_i[j];
/* 110 */         this.alpha[i].pseudo[state_j.index] = alpha_i[j];
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean doubleStates(double u) {
/* 116 */     this.transChanged = true;
/* 117 */     this.hittingProb = null;
/* 118 */     int numF = this.states.size() - 1;
/*     */     
/* 120 */     for (int j = 0; j < numF; j++) {
/* 121 */       HaplotypeEmissionState hes = (HaplotypeEmissionState)this.states.get(j + 1);
/* 122 */       HaplotypeEmissionState hes_new = (HaplotypeEmissionState)hes.clone();
/* 123 */       hes_new.name = (numF + j + 1);
/* 124 */       hes_new.setRandom(u, false);
/* 125 */       addState(hes_new);
/*     */     }
/*     */     
/* 128 */     for (int i = 0; i < this.noSnps; i++) {
/* 129 */       this.exp_rd[i].revertToPseudo();
/* 130 */       this.alpha[i].doubleSt();
/* 131 */       if (this.alpha[i].probs.length != modelLength()) { throw new RuntimeException("!!");
/*     */       }
/*     */     }
/* 134 */     return true;
/*     */   }
/*     */   
/*     */   public boolean increaseStates(double u, double small, int no)
/*     */   {
/* 139 */     this.hittingProb = null;
/* 140 */     int j = this.states.size() - 1;
/* 141 */     State newState = super.makeState(j, this.noSnps, u);
/* 142 */     addState(newState);
/* 143 */     for (int i = 0; i < this.noSnps; i++) {
/* 144 */       this.alpha[i].multiplyValues(1.0D - small);
/* 145 */       this.alpha[i].put(newState, small);
/*     */     }
/* 147 */     return true;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void addCounts(StateDistribution[] observed, int i)
/*     */   {
/* 155 */     if (i + 1 == this.noSnps) return;
/* 156 */     SimpleExtendedDistribution jump = this.alpha[(i + 1)];
/* 157 */     SimpleExtendedDistribution exp_count = this.exp_rd[(i + 1)];
/* 158 */     for (int j = 0; j < observed.length; j++) {
/* 159 */       StateDistribution dist1 = observed[j];
/* 160 */       State st = (State)this.states.get(j);
/* 161 */       if (dist1 != null) {
/* 162 */         for (int j1 = 0; j1 < modelLength(); j1++) {
/* 163 */           State state = getState(j1);
/* 164 */           Double val = dist1.get(state);
/* 165 */           if (val.doubleValue() != 0.0D)
/* 166 */             if (!state.equals(st)) {
/* 167 */               jump.counts[state.index] += val.doubleValue();
/* 168 */               exp_count.counts[1] += val.doubleValue();
/*     */             }
/*     */             else {
/* 171 */               double exp = this.exp_rd[(i + 1)].probs[0];
/* 172 */               double non_jump_prob = exp;
/* 173 */               double jump_prob = (1.0D - exp) * jump.probs[j];
/* 174 */               double alloc = val.doubleValue() * (jump_prob / (jump_prob + non_jump_prob));
/* 175 */               jump.counts[state.index] += alloc;
/* 176 */               exp_count.counts[1] += alloc;
/* 177 */               exp_count.counts[0] += val.doubleValue() - alloc;
/*     */             }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public Double[] transform(SimpleExtendedDistribution[] d) {
/* 185 */     Double[] res = new Double[d.length];
/* 186 */     for (int i = 0; i < res.length; i++) {
/* 187 */       res[i] = 
/* 188 */         Double.valueOf(d[i].probs[0]);
/*     */     }
/*     */     
/*     */ 
/* 192 */     return res;
/*     */   }
/*     */   
/*     */   public void print(PrintWriter sb) {
/* 196 */     super.print(sb);
/* 197 */     StringBuffer sb1 = new StringBuffer();
/* 198 */     for (int i = 0; i < this.noSnps - 1; i++) {
/* 199 */       sb1.append("%8.2g ");
/*     */     }
/* 201 */     sb.print("Exp:     ");
/* 202 */     sb.println(Format.sprintf(sb1.toString(), transform(this.exp_rd)));
/* 203 */     sb1.append("%8.2g ");
/* 204 */     for (int i = 1; i < modelLength(); i++) {
/* 205 */       sb.print("Alpha_");
/* 206 */       sb.print(i + "  ");
/* 207 */       sb.println(Format.sprintf(sb1.toString(), summary(i)));
/*     */     }
/*     */   }
/*     */   
/*     */   public Object clone() {
/* 212 */     return new ExponentialHaplotypeHMM(this, null, 0.0D);
/*     */   }
/*     */   
/* 215 */   public MarkovModel clone(MarkovModel pseudo, double d) { return new ExponentialHaplotypeHMM(this, (ExponentialHaplotypeHMM)pseudo, d); }
/*     */   
/*     */ 
/*     */   public double getTransitionScore(int from, int to, int indexOfToEmission)
/*     */   {
/* 220 */     if (indexOfToEmission >= this.noSnps) return 0.0D;
/* 221 */     if (from == 0) {
/* 222 */       if (indexOfToEmission != 0) return 0.0D;
/* 223 */       return this.alpha[0].probs[to];
/*     */     }
/* 225 */     if (to == 0) {
/* 226 */       if (indexOfToEmission == this.noSnps - 1) return 1.0D;
/* 227 */       return 0.0D;
/*     */     }
/* 229 */     if (indexOfToEmission == 0) return 0.0D;
/* 230 */     double exp = this.exp_rd[indexOfToEmission].probs[0];
/* 231 */     double toProb = this.alpha[indexOfToEmission].probs[to];
/* 232 */     if (to == from) {
/* 233 */       return exp + (1.0D - exp) * toProb;
/*     */     }
/*     */     
/* 236 */     return (1.0D - exp) * toProb;
/*     */   }
/*     */   
/*     */ 
/*     */   public void initialiseTransitionCounts()
/*     */   {
/* 242 */     for (int i = 0; i < this.alpha.length; i++) {
/* 243 */       this.alpha[i].initialise();
/* 244 */       this.exp_rd[i].initialise();
/*     */     }
/*     */   }
/*     */   
/* 248 */   public void setRandomTransitions(double u, boolean restart, boolean lastOnly) { this.transChanged = true;
/*     */     
/* 250 */     for (int i = 0; i < this.alpha.length; 
/* 251 */         i++) {
/* 252 */       if ((!lastOnly) || (i == this.alpha.length - 1)) this.alpha[i].resample(u, restart);
/* 253 */       if ((i < this.exp_rd.length) && (!lastOnly)) {
/* 254 */         this.exp_rd[i].revertToPseudo();
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   Double[] summary(int j)
/*     */   {
/* 263 */     Double[] res = new Double[this.alpha.length];
/* 264 */     for (int i = 0; i < this.alpha.length; i++) {
/* 265 */       res[i] = Double.valueOf(this.alpha[i].probs[j]);
/*     */     }
/* 267 */     return res;
/*     */   }
/*     */   
/*     */   public double totalTransitionDistance(MarkovModel m1) {
/* 271 */     ExponentialHaplotypeHMM hmm = (ExponentialHaplotypeHMM)m1;
/* 272 */     double sum = 0.0D;
/* 273 */     for (int i = 0; i < this.alpha.length; i++) {
/* 274 */       sum += this.alpha[i].KLDistance(hmm.alpha[i]);
/* 275 */       if (i > 0) sum += this.exp_rd[(i - 1)].KLDistance(hmm.exp_rd[(i - 1)]);
/*     */     }
/* 277 */     return sum / (this.alpha.length + this.exp_rd.length);
/*     */   }
/*     */   
/* 280 */   static double KLDist(double d1, double d2) { return (d1 == 0.0D ? 0.0D : d1 * Math.log(d1 / d2)) + (
/* 281 */       d1 == 1.0D ? 0.0D : (1.0D - d1) * Math.log((1.0D - d1) / (1.0D - d2)));
/*     */   }
/*     */   
/*     */   public void transferCountsToProbs()
/*     */   {
/* 286 */     super.transferCountsToProbs();
/* 287 */     this.transChanged = true;
/*     */   }
/*     */   
/*     */ 
/*     */   public static Iterator getIterator(Object obj)
/*     */   {
/* 293 */     new Iterator() {
/* 294 */       boolean hasNext = true;
/*     */       
/* 296 */       public boolean hasNext() { if (this.hasNext) {
/* 297 */           this.hasNext = false;
/* 298 */           return true;
/*     */         }
/*     */         
/* 301 */         this.hasNext = true;
/* 302 */         return false;
/*     */       }
/*     */       
/*     */       public Object next()
/*     */       {
/* 307 */         return ExponentialHaplotypeHMM.this;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       public void remove() {}
/*     */     };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public boolean transitionsChanged()
/*     */   {
/* 328 */     return this.transChanged;
/*     */   }
/*     */   
/*     */ 
/*     */   public void transitionsFixed()
/*     */   {
/* 334 */     this.transChanged = false;
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
/*     */   public void transferTransitionCountsToProbs()
/*     */   {
/* 347 */     if (Constants.trainTransitions()) {
/* 348 */       for (int i = 0; i < this.noSnps; i++) {
/* 349 */         this.alpha[i].transfer(this.pseudocountWeights);
/* 350 */         this.exp_rd[i].transfer(this.pseudocountWeights);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean recombineRandom(ExponentialHaplotypeHMM hap)
/*     */   {
/* 357 */     HaplotypeEmissionState st1 = (HaplotypeEmissionState)getState(Constants.nextInt(modelLength() - 1) + 1);
/* 358 */     HaplotypeEmissionState st2 = (HaplotypeEmissionState)hap.getState(Constants.nextInt(modelLength() - 1) + 1);
/* 359 */     st1.recombine(st2, Constants.nextInt(this.noSnps));
/* 360 */     return true;
/*     */   }
/*     */   
/* 363 */   public boolean recombine(ExponentialHaplotypeHMM hap) { StatePath[] sp = new StatePath[hap.modelLength() - 1];
/* 364 */     for (int j = 1; j < hap.modelLength(); j++) {
/* 365 */       HaplotypeEmissionState dat = (HaplotypeEmissionState)hap.getState(j);
/* 366 */       DP dp = new DP(this, "", new StateScorableObject(dat, this), true);
/* 367 */       dp.reset();
/* 368 */       dp.searchViterbi();
/* 369 */       sp[(j - 1)] = dp.getStatePath();
/*     */     }
/*     */     
/* 372 */     List<StatePath> sp1 = new ArrayList();
/* 373 */     for (int j = sp.length - 1; j >= 0; j--) {
/* 374 */       if (!sp[j].getFirstState().equals(sp[j].getLastState())) sp1.add(sp[j]);
/*     */     }
/* 376 */     if (sp1.size() == 0) {
/* 377 */       Logger.getAnonymousLogger().info("all states where consistent, so no recombinations");
/* 378 */       return false;
/*     */     }
/* 380 */     int j = Constants.nextInt(sp1.size());
/* 381 */     Logger.getAnonymousLogger().info("RECOMBINING AT " + j + " " + Arrays.asList(hap.getHittingProb(hap.noSnps)[(j + 1)]));
/* 382 */     int i = sp[j].getRecombineIndex();
/* 383 */     Logger.getAnonymousLogger().info("RECOMBINING AT POS" + i);
/* 384 */     HaplotypeEmissionState st = (HaplotypeEmissionState)sp[j].getState(i);
/* 385 */     HaplotypeEmissionState st1 = (HaplotypeEmissionState)sp[j].getEmission(i);
/*     */     
/* 387 */     st.recombine(st1, i);
/* 388 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/genotype/ExponentialHaplotypeHMM.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */