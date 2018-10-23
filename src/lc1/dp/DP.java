/*     */ package lc1.dp;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.genotype.StateDistribution;
/*     */ import lc1.dp.genotype.io.scorable.StateIndices;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DP
/*     */ {
/*  19 */   static Logger logger = ;
/*  20 */   public static boolean verbose = true;
/*     */   
/*     */   protected TraceMatrix backwardTrace;
/*     */   
/*     */   public final double[][] emissions;
/*     */   
/*     */   protected TraceMatrix forwardTrace;
/*     */   
/*     */   protected MarkovModel hmm;
/*     */   
/*     */   protected final boolean logspace;
/*     */   protected final int modelLength;
/*     */   StateIndices obj;
/*     */   public final String protName;
/*     */   protected final int seqLength;
/*     */   final double nullEms;
/*  36 */   double threshold = -200.0D;
/*  37 */   double thresholdMax = 200.0D;
/*     */   
/*  39 */   public DP(MarkovModel hmm, String protName, boolean logspace, int seqLength) { logger.setLevel(Level.OFF);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  48 */     this.logspace = logspace;
/*  49 */     this.modelLength = hmm.modelLength();
/*  50 */     this.seqLength = seqLength;
/*  51 */     this.protName = protName;
/*  52 */     this.hmm = hmm;
/*     */     
/*     */ 
/*  55 */     this.nullEms = (logspace ? 0 : 1);
/*  56 */     this.emissions = new double[this.modelLength][];
/*  57 */     this.emiss = new boolean[this.modelLength];
/*  58 */     this.adv = new int[this.modelLength];
/*  59 */     for (int i = 0; i < this.modelLength; i++) {
/*  60 */       State st = hmm.getState(i);
/*  61 */       this.emiss[i] = (st instanceof EmissionState);
/*  62 */       this.adv[i] = st.adv;
/*     */     }
/*  64 */     this.forwardTrace = new TraceMatrix(this.modelLength, seqLength, true, logspace);
/*  65 */     this.backwardTrace = new TraceMatrix(this.modelLength, seqLength, false, logspace);
/*     */   }
/*     */   
/*     */   public DP(MarkovModel hmm, String protName, StateIndices obj, boolean logspace) {
/*  69 */     this(hmm, protName, logspace, obj.length());
/*  70 */     this.obj = obj;
/*     */   }
/*     */   
/*     */   public final int fillEmissions()
/*     */   {
/*     */     int[] equiv;
/*     */     int k;
/*  77 */     for (Iterator<int[]> it = this.hmm.equivalenceClasses(); it.hasNext(); 
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  86 */         k < equiv.length)
/*     */     {
/*  78 */       equiv = (int[])it.next();
/*  79 */       EmissionState state = (EmissionState)this.hmm.getState(equiv[0]);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  85 */       double[] d = this.obj.score(state, this.logspace);
/*  86 */       k = 0; continue;
/*  87 */       this.emissions[equiv[k]] = d;k++;
/*     */     }
/*     */     
/*     */ 
/*     */ 
/*  91 */     return 1;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public void reset()
/*     */   {
/*  99 */     this.forwardTrace.clear();
/* 100 */     this.backwardTrace.clear();
/* 101 */     int changed = fillEmissions();
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
/*     */   double backward(int k, int i)
/*     */   {
/* 125 */     int max_j = -1;
/* 126 */     int max_i = 0;
/* 127 */     double max = 0.0D;
/* 128 */     double sum = 0.0D;
/*     */     
/*     */ 
/* 131 */     double summ = 0.0D;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 137 */     int[] out = this.hmm.statesOut(k, i);
/* 138 */     for (int k1 = 0; k1 < out.length; k1++)
/*     */     {
/* 140 */       int j1 = out[k1];
/* 141 */       int i1 = i + this.adv[j1];
/* 142 */       double val = this.hmm.getTransitionScore(k, j1, i1);
/*     */       
/* 144 */       summ += val;
/*     */       
/*     */ 
/* 147 */       AbstractTerm AbstractTerm = this.backwardTrace.getTrace(j1, i1);
/* 148 */       double score; double score; if (AbstractTerm == null) { score = 0.0D;
/*     */       } else {
/* 150 */         double d = this.emiss[j1] != 0 ? this.emissions[j1][i1] : this.nullEms;
/* 151 */         score = val * AbstractTerm.score() * d;
/*     */       }
/* 153 */       if (score > max) {
/* 154 */         max = score;
/* 155 */         max_j = j1;
/* 156 */         max_i = i1;
/*     */       }
/* 158 */       sum += score;
/*     */     }
/*     */     
/* 161 */     if (Math.abs(summ - 1.0D) > SimpleDistribution.tolerance) {
/*     */       try {
/* 163 */         this.hmm.validate(this.seqLength);
/*     */       } catch (Exception exc) {
/* 165 */         exc.printStackTrace();
/*     */       }
/* 167 */       throw new RuntimeException("sum not right " + summ + " " + i + " " + this.obj.length() + " " + k + " ");
/*     */     }
/* 169 */     if (i >= 0) {
/* 170 */       this.backwardTrace.setTrace(Integer.valueOf(k), i, new Term(max_j, max_i, sum));
/*     */     }
/* 172 */     return sum;
/*     */   }
/*     */   
/* 175 */   protected void calcScoresBackward() { if (this.logspace) { throw new RuntimeException("only run backward in prob space");
/*     */     }
/* 177 */     this.backwardTrace.setTrace(Integer.valueOf(this.hmm.MAGIC.index), this.seqLength - 1, new Term(0, this.seqLength - 1, 1.0D));
/*     */     
/* 179 */     for (int i = this.seqLength - 1; i >= 0; i--) {
/* 180 */       double logscale = this.backwardTrace.getLogScale(i + 1);
/* 181 */       for (int j = this.modelLength - 1; j >= 1; j--)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 186 */         backward(j, i);
/*     */       }
/* 188 */       double[] min = this.backwardTrace.minScore(i);
/* 189 */       logger.fine("minmax " + min[0] + " " + min[1] + " " + logscale);
/* 190 */       if (min[0] < this.threshold) {
/* 191 */         double scale = Math.min(-min[0], this.thresholdMax - min[1]);
/* 192 */         this.backwardTrace.scale(Math.exp(scale), i);
/* 193 */         logscale += scale;
/*     */       }
/*     */       
/* 196 */       this.backwardTrace.setLogscale(i, logscale);
/*     */     }
/*     */     
/* 199 */     this.backwardTrace.overall = backward(0, -1);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 206 */     logger.fine("bsc " + this.backwardTrace.overall);
/*     */   }
/*     */   
/*     */   protected void calcScoresForward(boolean full) {
/* 210 */     if (this.logspace) throw new RuntimeException("only run forward in prob space");
/* 211 */     for (int i = 0; i < this.seqLength; i++) {
/* 212 */       double logscale = this.forwardTrace.getLogScale(i - 1);
/* 213 */       for (int j = 1; j < this.modelLength; j++) {
/* 214 */         if (full) forwardComplete(j, i, this.emiss[j] != 0 ? this.emissions[j][i] : this.nullEms); else
/* 215 */           forward(j, i, this.emiss[j] != 0 ? this.emissions[j][i] : this.nullEms);
/*     */       }
/* 217 */       double[] min = this.forwardTrace.minScore(i);
/* 218 */       if (min[0] < this.threshold) {
/* 219 */         double scale = Math.min(-min[0], this.thresholdMax - min[1]);
/* 220 */         this.forwardTrace.scale(Math.exp(scale), i);
/* 221 */         logscale += scale;
/*     */       }
/* 223 */       this.forwardTrace.setLogscale(i, logscale);
/*     */     }
/*     */     
/* 226 */     this.forwardTrace.overall = (full ? forwardComplete(0, this.seqLength - 1, this.nullEms) : forward(0, this.seqLength - 1, this.nullEms));
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   protected double calcScoresViterbi()
/*     */   {
/* 237 */     for (int i = 0; i < this.seqLength; i++) {
/* 238 */       for (int j = 1; j < this.modelLength; j++)
/*     */       {
/*     */ 
/*     */ 
/* 242 */         viterbi(j, i, this.emiss[j] != 0 ? this.emissions[j][i] : this.nullEms);
/*     */       }
/*     */     }
/*     */     
/* 246 */     this.forwardTrace.overall = viterbi(0, this.seqLength - 1, this.nullEms);
/*     */     
/* 248 */     return this.forwardTrace.overall;
/*     */   }
/*     */   
/*     */ 
/*     */   public void clear()
/*     */   {
/* 254 */     this.forwardTrace.clear();
/* 255 */     this.backwardTrace.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double fillPosteriorProbabilities(int i, double[][] posterior)
/*     */   {
/* 262 */     double total = 0.0D;
/*     */     
/*     */ 
/* 265 */     double scale_i = -this.forwardTrace.getLogScale(i) - this.backwardTrace.getLogScale(i) + this.forwardTrace.getLogScale(this.seqLength - 1);
/* 266 */     for (int j = 0; j < this.modelLength; j++)
/*     */     {
/*     */ 
/* 269 */       AbstractTerm forward = this.forwardTrace.getTrace(j, i);
/* 270 */       AbstractTerm backward = this.backwardTrace.getTrace(j, i);
/* 271 */       if ((forward == null) || (backward == null)) { posterior[j][i] = 0.0D;
/*     */       } else {
/* 273 */         double unscaled = forward.score() * backward.score() / this.forwardTrace.overall;
/* 274 */         posterior[j][i] = (Math.exp(scale_i) * unscaled);
/* 275 */         total += posterior[j][i];
/*     */       }
/*     */     }
/* 278 */     return total;
/*     */   }
/*     */   
/* 281 */   public void fillPosteriorProbabilities(double[][] posterior) { for (int i = 0; i < this.seqLength; i++)
/* 282 */       fillPosteriorProbabilities(i, posterior);
/*     */   }
/*     */   
/*     */   double forward(int j, int i, double emissionScore) {
/* 286 */     int max_j = -1;
/* 287 */     double max = 0.0D;
/* 288 */     double sum = 0.0D;
/* 289 */     int adv = this.adv[j];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 295 */     int[] toStates = this.hmm.statesIn(j, i);
/* 296 */     for (int k1 = 0; k1 < toStates.length; k1++) {
/* 297 */       int j1 = toStates[k1];
/*     */       
/* 299 */       AbstractTerm AbstractTerm = this.forwardTrace.getTrace(j1, i - adv);
/* 300 */       double score; double score; if (AbstractTerm == null) score = 0.0D; else
/* 301 */         score = this.hmm.getTransitionScore(j1, j, i) * AbstractTerm.score();
/* 302 */       if (score > max) {
/* 303 */         max = score;
/* 304 */         max_j = j1;
/*     */       }
/*     */       
/* 307 */       sum += score;
/*     */     }
/* 309 */     AbstractTerm newTerm = new Term(max_j, i - adv, sum * emissionScore);
/*     */     
/* 311 */     this.forwardTrace.setTrace(Integer.valueOf(j), i, newTerm);
/* 312 */     return sum;
/*     */   }
/*     */   
/*     */   double forwardComplete(int j, int i, double emissionScore) {
/* 316 */     double sum = 0.0D;
/* 317 */     int adv = this.adv[j];
/* 318 */     double[] score = new double[this.modelLength];
/* 319 */     for (int j1 = 0; j1 < this.modelLength; j1++) {
/* 320 */       AbstractTerm term = this.forwardTrace.getTrace(j1, i - adv);
/* 321 */       if (term == null) score[j1] = 0.0D; else {
/* 322 */         score[j1] = (this.hmm.getTransitionScore(j1, j, i) * term.score() * emissionScore);
/*     */       }
/*     */       
/* 325 */       sum += score[j1];
/*     */     }
/* 327 */     AbstractTerm newTerm = new ComplexTerm(score, i - adv, sum);
/*     */     
/* 329 */     this.forwardTrace.setTrace(Integer.valueOf(j), i, newTerm);
/* 330 */     return sum;
/*     */   }
/*     */   
/*     */   private State getBestEmission(int i) {
/* 334 */     State max_j = null;
/* 335 */     double max = Double.NEGATIVE_INFINITY;
/* 336 */     for (Iterator<State> it = this.hmm.states(); it.hasNext();) {
/* 337 */       State j = (State)it.next();
/* 338 */       if ((j instanceof EmissionState)) {
/* 339 */         double sc = this.emissions[j.index][i];
/* 340 */         if (sc > max) {
/* 341 */           max_j = j;
/* 342 */           max = sc;
/*     */         }
/*     */       }
/*     */     }
/* 346 */     return max_j;
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
/*     */   final int[] adv;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   private double[] getOverallScore()
/*     */   {
/* 378 */     double scoreForward = Math.log(this.forwardTrace.overall) - this.forwardTrace.getLogScale(this.seqLength - 1);
/* 379 */     double scoreBackward = Math.log(this.backwardTrace.overall) - this.backwardTrace.getLogScale(0);
/*     */     
/* 381 */     return new double[] { scoreForward, scoreBackward };
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public StateDistribution getPosterior(int i, StateDistribution id)
/*     */   {
/* 388 */     double scale_i = Math.exp(-this.forwardTrace.getLogScale(i) - this.backwardTrace.getLogScale(i) + this.forwardTrace.getLogScale(this.seqLength - 1));
/*     */     
/* 390 */     for (Iterator<State> it = this.hmm.states(); it.hasNext();) {
/* 391 */       State j = (State)it.next();
/* 392 */       if (!(j instanceof DotState)) {
/* 393 */         AbstractTerm forward = this.forwardTrace.getTrace(j.index, i);
/* 394 */         AbstractTerm backward = this.backwardTrace.getTrace(j.index, i);
/* 395 */         if ((forward != null) && (backward != null))
/*     */         {
/* 397 */           double unscaled = forward.score() * backward.score() / this.forwardTrace.overall;
/* 398 */           if (unscaled > 0.0D) { id.put(j, scale_i * unscaled);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 403 */     return id;
/*     */   }
/*     */   
/*     */   public double[][] getPosteriorMatch()
/*     */   {
/* 408 */     double[][] posterior = new double[this.modelLength][this.seqLength];
/* 409 */     fillPosteriorProbabilities(posterior);
/* 410 */     return posterior;
/*     */   }
/*     */   
/*     */   public double[] getPosteriorMatchSum(State[] states) {
/* 414 */     double[][] d = getPosteriorMatch();
/* 415 */     double[] d1 = new double[d[0].length];
/* 416 */     Arrays.fill(d1, 0.0D);
/* 417 */     for (int j = 0; j < states.length; j++) {
/* 418 */       for (int i = 0; i < d1.length; i++) {
/* 419 */         d1[i] += d[states[j].index][i];
/*     */       }
/*     */     }
/* 422 */     return d1;
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
/*     */   public StatePath getStatePath()
/*     */   {
/* 449 */     StatePath path = new StatePath(this.protName, this.forwardTrace.overall);
/* 450 */     getStatePath(path, new StoppingCriterion() {
/*     */       public boolean stop(Object emission) {
/* 452 */         return false;
/*     */       }
/* 454 */     });
/* 455 */     return path;
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
/*     */   final boolean[] emiss;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatePath(StatePath path, StoppingCriterion stop)
/*     */   {
/* 494 */     State firstState = path.getFirstState();
/* 495 */     int j = firstState == null ? 0 : firstState.index;
/* 496 */     Integer st = path.getFirstEmissionPos();
/* 497 */     int i = st == null ? this.seqLength - 1 : st.intValue();
/*     */     
/*     */ 
/* 500 */     AbstractTerm tr = this.forwardTrace.getTrace(j, i);
/* 501 */     if (tr == null) return i;
/* 502 */     i = tr.i;
/* 503 */     j = tr.getBestPath();
/*     */     label263:
/* 505 */     while (j > 0) {
/* 506 */       boolean emiss = this.emiss[j];
/*     */       try {
/* 508 */         Object obj_i = emiss ? 
/* 509 */           (Comparable)this.hmm.getEmissionStateSpace().get(this.obj.getBestIndex((EmissionState)this.hmm.getState(j), i)) : 
/* 510 */           null;
/*     */         
/* 512 */         path.add(this.hmm.getState(j), obj_i, i);
/* 513 */         if (!stop.stop(obj_i)) break label263; return i;
/*     */       } catch (Exception exc) {
/* 515 */         exc.printStackTrace();
/* 516 */         logger.info(i);
/* 517 */         logger.info(j);
/* 518 */         logger.info(path);
/*     */         
/* 520 */         System.exit(0);
/* 521 */         path.add(this.hmm.getState(j), null, i);
/*     */       }
/*     */       
/* 524 */       tr = this.forwardTrace.getTrace(j, i);
/* 525 */       i = tr.i;
/* 526 */       j = tr.getBestPath();
/*     */     }
/*     */     
/* 529 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addTransitionPosterior(int i, StateDistribution[] transitionPosterior)
/*     */   {
/* 535 */     double scale_i = Math.exp(this.forwardTrace.getLogScale(this.seqLength - 1) - this.forwardTrace.getLogScale(i) - this.backwardTrace.getLogScale(i + 1));
/* 536 */     for (int k = 0; k < this.modelLength; k++) {
/* 537 */       StateDistribution d1 = transitionPosterior[k];
/* 538 */       for (int j = 0; j < this.modelLength; j++) {
/* 539 */         double sc = getTransitionProb(k, j, i) * scale_i;
/* 540 */         if (sc > 0.0D) d1.add(j, sc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private double getTransitionProb(int k, int l, int i)
/*     */   {
/* 547 */     int adv = this.adv[l];
/* 548 */     if (i + adv < 0) return 0.0D;
/* 549 */     double f_ki = this.forwardTrace.getScore(k, i, false);
/* 550 */     double b_li = this.backwardTrace.getScore(l, i + adv, false);
/* 551 */     double p_x = this.forwardTrace.overall;
/* 552 */     double e_li = 
/* 553 */       this.emiss[l] != 0 ? 
/* 554 */       this.emissions[l][(i + adv)] : i + adv == this.seqLength ? 0.0D : 1.0D;
/* 555 */     return f_ki * e_li * b_li / p_x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public void fillComplexTrace()
/*     */   {
/* 564 */     calcScoresForward(true);
/*     */   }
/*     */   
/*     */   public double search(boolean incBackward)
/*     */   {
/* 569 */     calcScoresForward(false);
/* 570 */     if (incBackward) calcScoresBackward();
/* 571 */     double[] sc = getOverallScore();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 582 */     return sc[0];
/*     */   }
/*     */   
/* 585 */   public double searchViterbi() { return calcScoresViterbi(); }
/*     */   
/*     */ 
/*     */   public double validate(StateDistribution dist, double sum)
/*     */   {
/* 590 */     double sum1 = dist.sum();
/* 591 */     if (Math.abs(sum - sum1) > 0.01D) throw new RuntimeException("sum is not right " + sum + " " + sum1);
/* 592 */     return sum1;
/*     */   }
/*     */   
/*     */ 
/*     */   double viterbi(int j, int i, double emiss)
/*     */   {
/* 598 */     int max_j = -1;
/* 599 */     int max_i = 0;
/*     */     
/*     */ 
/*     */ 
/* 603 */     double max = Double.NEGATIVE_INFINITY;
/*     */     
/* 605 */     for (int j2 = 0; j2 < this.modelLength; j2++)
/*     */     {
/*     */ 
/* 608 */       int adv = this.adv[j];
/* 609 */       AbstractTerm term = this.forwardTrace.getTrace(j2, i - adv);
/* 610 */       if (term != null)
/*     */       {
/* 612 */         double score = Math.log(this.hmm.getTransitionScore(j2, j, i)) + 
/* 613 */           term.score();
/*     */         
/* 615 */         if (score > max) {
/* 616 */           max = score;
/* 617 */           max_j = j2;
/* 618 */           max_i = i - adv;
/*     */         }
/*     */       } }
/* 621 */     double result = max;
/*     */     
/* 623 */     AbstractTerm newT = new Term(max_j, max_i, result + emiss);
/* 624 */     this.forwardTrace.setTrace(Integer.valueOf(j), i, newT);
/* 625 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/DP.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */