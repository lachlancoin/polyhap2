/*     */ package lc1.dp.core;
/*     */ 
/*     */ import com.braju.format.Format;
/*     */ import java.io.File;
/*     */ import java.io.PrintStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import lc1.dp.emissionspace.EmissionStateSpace;
/*     */ import lc1.dp.model.MarkovModel;
/*     */ import lc1.dp.states.DotState;
/*     */ import lc1.dp.states.EmissionState;
/*     */ import lc1.dp.states.State;
/*     */ import lc1.stats.SimpleDistribution;
/*     */ import lc1.stats.StateDistribution;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DP
/*     */ {
/*  30 */   static Logger logger = ;
/*  31 */   public static boolean verbose = true;
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
/*     */   EmissionState obj;
/*     */   public final String protName;
/*     */   protected final int seqLength;
/*     */   final double nullEms;
/*  47 */   double threshold = -200.0D;
/*  48 */   double thresholdMax = 200.0D;
/*     */   
/*  50 */   public DP(MarkovModel hmm, String protName, boolean logspace, int seqLength) { logger.setLevel(Level.OFF);
/*  51 */     File localFile = new File("tmp_out");
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  99 */     this.objIndex = -1;
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 698 */     this.logprob = null;this.logspace = logspace;this.modelLength = hmm.modelLength();this.seqLength = seqLength;this.protName = protName;this.hmm = hmm;this.nullEms = (logspace ? 0 : 1);this.emissions = new double[this.modelLength][];this.emiss = new boolean[this.modelLength];this.adv = new int[this.modelLength];
/*  75 */     for (int i = 0; i < this.modelLength; i++) {
/*  76 */       State st = hmm.getState(i);
/*  77 */       this.emiss[i] = (st instanceof EmissionState);
/*  78 */       this.adv[i] = st.adv;
/*     */     }
/*  80 */     this.forwardTrace = new TraceMatrix(this.modelLength, seqLength, true, logspace);
/*  81 */     this.backwardTrace = new TraceMatrix(this.modelLength, seqLength, false, logspace);
/*  82 */     this.stateIndex = new int[hmm.modelLength()];
/*  83 */     Arrays.fill(this.stateIndex, -1);
/*     */   }
/*     */   
/*     */   public DP(MarkovModel hmm, String protName, EmissionState obj, boolean logspace) {
/*  87 */     this(hmm, protName, logspace, obj.noSnps());
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*  95 */     this.obj = obj;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */   public final void fillEmissions(boolean first)
/*     */   {
/* 103 */     boolean objUpdated = this.obj.getParamIndex() != this.objIndex;
/* 104 */     for (Iterator<int[]> it = this.hmm.equivalenceClasses(); it.hasNext();) {
/* 105 */       int[] equiv = (int[])it.next();
/* 106 */       EmissionState state = (EmissionState)this.hmm.getState(equiv[0]);
/* 107 */       if ((objUpdated) || (state.getParamIndex() != this.stateIndex[equiv[0]]))
/*     */       {
/* 109 */         double[] d = this.obj.score(state, this.logspace);
/*     */         
/* 111 */         for (int k = 0; k < equiv.length; k++) {
/* 112 */           this.emissions[equiv[k]] = d;
/*     */         }
/*     */       }
/* 115 */       this.stateIndex[equiv[0]] = state.getParamIndex();
/*     */     }
/* 117 */     this.objIndex = this.obj.getParamIndex();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public void reset(boolean first)
/*     */   {
/* 124 */     this.forwardTrace.clear();
/* 125 */     this.backwardTrace.clear();
/* 126 */     fillEmissions(first);
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
/*     */   double backward(int k, int i)
/*     */   {
/* 147 */     int max_j = -1;
/* 148 */     int max_i = 0;
/* 149 */     double max = 0.0D;
/* 150 */     double sum = 0.0D;
/* 151 */     double summ = 0.0D;
/* 152 */     int[] out = this.hmm.statesOut(k, i);
/* 153 */     for (int k1 = 0; k1 < out.length; k1++) {
/* 154 */       int j1 = out[k1];
/* 155 */       int i1 = i + this.adv[j1];
/* 156 */       double val = this.hmm.getTransitionScore(k, j1, i1);
/* 157 */       summ += val;
/*     */       
/*     */ 
/* 160 */       AbstractTerm AbstractTerm = this.backwardTrace.getTrace(j1, i1);
/* 161 */       double score; double score; if (AbstractTerm == null) { score = 0.0D;
/*     */       } else {
/* 163 */         double d = this.emiss[j1] != 0 ? this.emissions[j1][i1] : this.nullEms;
/* 164 */         score = val * AbstractTerm.score() * d;
/*     */       }
/*     */       
/* 167 */       if (score > max) {
/* 168 */         max = score;
/* 169 */         max_j = j1;
/* 170 */         max_i = i1;
/*     */       }
/*     */       
/* 173 */       sum += score;
/*     */     }
/*     */     
/* 176 */     if (Math.abs(summ - 1.0D) > SimpleDistribution.tolerance) {
/*     */       try {
/* 178 */         this.hmm.validate(this.hmm.noSnps.intValue());
/*     */       } catch (Exception exc) {
/* 180 */         exc.printStackTrace();
/*     */       }
/* 182 */       throw new RuntimeException("sum not right " + summ + " " + i + " " + this.obj.length() + " " + k + " ");
/*     */     }
/* 184 */     if (i >= 0) {
/* 185 */       this.backwardTrace.setTrace(Integer.valueOf(k), i, new Term(max_j, max_i, sum));
/*     */     }
/*     */     
/* 188 */     return sum;
/*     */   }
/*     */   
/* 191 */   protected void calcScoresBackward() { if (this.logspace) { throw new RuntimeException("only run backward in prob space");
/*     */     }
/* 193 */     this.backwardTrace.setTrace(Integer.valueOf(MarkovModel.MAGIC.getIndex()), this.seqLength - 1, new Term(0, this.seqLength - 1, 1.0D));
/*     */     
/* 195 */     for (int i = this.seqLength - 1; i >= 0; i--) {
/* 196 */       double logscale = this.backwardTrace.getLogScale(i + 1);
/* 197 */       for (int j = this.modelLength - 1; j >= 1; j--)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/* 202 */         backward(j, i);
/*     */       }
/* 204 */       double[] min = this.backwardTrace.minScore(i);
/* 205 */       logger.info("minmaxb " + min[0] + " " + min[1] + " " + logscale);
/* 206 */       if (min[0] < this.threshold) {
/* 207 */         double scale = Math.min(-min[0], this.thresholdMax - min[1]);
/* 208 */         this.backwardTrace.scale(Math.exp(scale), i);
/* 209 */         logscale += scale;
/*     */ 
/*     */       }
/* 212 */       else if (min[1] > this.thresholdMax) {
/* 213 */         double scale = 
/* 214 */           Math.min(this.thresholdMax - 10.0D - min[1], Math.max(-min[1], this.threshold - min[0]));
/* 215 */         this.backwardTrace.scale(Math.exp(scale), i);
/* 216 */         logscale += scale;
/*     */       }
/* 218 */       this.backwardTrace.setLogscale(i, logscale);
/*     */     }
/*     */     
/* 221 */     this.backwardTrace.overall = backward(0, -1);
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 228 */     logger.fine("bsc " + this.backwardTrace.overall);
/*     */   }
/*     */   
/*     */   public void calcScoresForward(boolean full) {
/* 232 */     if (this.logspace) throw new RuntimeException("only run forward in prob space");
/* 233 */     for (int i = 0; i < this.seqLength; i++) {
/* 234 */       double logscale = this.forwardTrace.getLogScale(i - 1);
/* 235 */       for (int j = 1; j < this.modelLength; j++) {
/* 236 */         if (full) { forwardComplete(j, i, this.emiss[j] != 0 ? this.emissions[j][i] : this.nullEms);
/*     */         }
/*     */         else
/*     */         {
/*     */           try
/*     */           {
/* 242 */             forward(j, i, this.emiss[j] != 0 ? this.emissions[j][i] : this.nullEms);
/*     */           } catch (Exception exc) {
/* 244 */             exc.printStackTrace();
/*     */           }
/*     */         }
/*     */       }
/* 248 */       double[] min = this.forwardTrace.minScore(i);
/* 249 */       if (Double.isInfinite(min[1])) {
/* 250 */         EmissionState es = this.obj;
/*     */         
/* 252 */         double[] pr = es.getEmiss(i);
/*     */         
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 260 */         EmissionStateSpace ss = es.getEmissionStateSpace();
/* 261 */         List<Double> d = new ArrayList();
/* 262 */         List<String> str = new ArrayList();
/* 263 */         int size = 0;
/* 264 */         StringBuffer sb = new StringBuffer();StringBuffer sb1 = new StringBuffer();
/* 265 */         for (int k = 0; k < pr.length; k++) {
/* 266 */           if (pr[k] > 0.0D) {
/* 267 */             d.add(Double.valueOf(pr[k]));
/* 268 */             String string = ss.get(k).toString();
/* 269 */             str.add(string);
/* 270 */             if (string.length() > size) { size = string.length();
/*     */             }
/*     */           }
/*     */         }
/*     */         
/* 275 */         for (int k = 0; k < d.size(); k++) {
/* 276 */           sb.append("%" + (size + 2) + "s ");
/* 277 */           sb1.append("%" + (size + 2) + "." + (size - 1) + "g ");
/*     */         }
/* 279 */         System.err.println(this.obj.getName());
/* 280 */         System.err.println(Format.sprintf(sb.toString(), str.toArray()));
/* 281 */         System.err.println(Format.sprintf(sb1.toString(), d.toArray()));
/* 282 */         throw new RuntimeException("max should be greater than zero at " + i);
/*     */       }
/*     */       
/* 285 */       if (min[0] < this.threshold) {
/* 286 */         double scale = Math.min(-min[0], this.thresholdMax - min[1]);
/* 287 */         this.forwardTrace.scale(Math.exp(scale), i);
/* 288 */         logscale += scale;
/*     */       }
/* 290 */       else if (min[1] > this.thresholdMax) {
/* 291 */         double scale = 
/* 292 */           Math.min(this.thresholdMax - 10.0D - min[1], Math.max(-min[1], this.threshold - min[0]));
/* 293 */         this.forwardTrace.scale(Math.exp(scale), i);
/* 294 */         logscale += scale;
/*     */       }
/* 296 */       this.forwardTrace.setLogscale(i, logscale);
/*     */     }
/*     */     
/* 299 */     this.forwardTrace.overall = (full ? forwardComplete(0, this.seqLength - 1, this.nullEms) : forward(0, this.seqLength - 1, this.nullEms));
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
/* 310 */     for (int i = 0; i < this.seqLength; i++) {
/* 311 */       for (int j = 1; j < this.modelLength; j++)
/*     */       {
/*     */ 
/*     */ 
/* 315 */         viterbi(j, i, this.emiss[j] != 0 ? this.emissions[j][i] : this.nullEms);
/*     */       }
/*     */     }
/*     */     
/* 319 */     this.forwardTrace.overall = viterbi(0, this.seqLength - 1, this.nullEms);
/*     */     
/* 321 */     return this.forwardTrace.overall;
/*     */   }
/*     */   
/*     */   public void clear()
/*     */   {
/* 326 */     this.forwardTrace.clear();
/* 327 */     this.backwardTrace.clear();
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */   public double fillPosteriorProbabilities(int i, double[][] posterior)
/*     */   {
/* 334 */     double total = 0.0D;
/*     */     
/*     */ 
/* 337 */     double scale_i = -this.forwardTrace.getLogScale(i) - this.backwardTrace.getLogScale(i) + this.forwardTrace.getLogScale(this.seqLength - 1);
/* 338 */     for (int j = 0; j < this.modelLength; j++)
/*     */     {
/*     */ 
/* 341 */       AbstractTerm forward = this.forwardTrace.getTrace(j, i);
/* 342 */       AbstractTerm backward = this.backwardTrace.getTrace(j, i);
/* 343 */       if ((forward == null) || (backward == null)) { posterior[j][i] = 0.0D;
/*     */       } else {
/* 345 */         double unscaled = forward.score() * backward.score() / this.forwardTrace.overall;
/* 346 */         posterior[j][i] = (Math.exp(scale_i) * unscaled);
/* 347 */         total += posterior[j][i];
/*     */       }
/*     */     }
/* 350 */     return total;
/*     */   }
/*     */   
/* 353 */   public void fillPosteriorProbabilities(double[][] posterior) { for (int i = 0; i < this.seqLength; i++)
/* 354 */       fillPosteriorProbabilities(i, posterior);
/*     */   }
/*     */   
/*     */   double forward(int j, int i, double emissionScore) {
/* 358 */     int max_j = -1;
/* 359 */     double max = 0.0D;
/* 360 */     double sum = 0.0D;
/* 361 */     int adv = this.adv[j];
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 367 */     int[] toStates = this.hmm.statesIn(j, i);
/* 368 */     for (int k1 = 0; k1 < toStates.length; k1++) {
/* 369 */       int j1 = toStates[k1];
/*     */       
/* 371 */       AbstractTerm AbstractTerm = this.forwardTrace.getTrace(j1, i - adv);
/* 372 */       double score; double score; if (AbstractTerm == null) score = 0.0D; else
/* 373 */         score = this.hmm.getTransitionScore(j1, j, i) * AbstractTerm.score();
/* 374 */       if (score > max) {
/* 375 */         max = score;
/* 376 */         max_j = j1;
/*     */       }
/*     */       
/*     */ 
/*     */ 
/* 381 */       sum += score;
/*     */     }
/* 383 */     AbstractTerm newTerm = new Term(max_j, i - adv, sum * emissionScore);
/*     */     
/* 385 */     this.forwardTrace.setTrace(Integer.valueOf(j), i, newTerm);
/* 386 */     return sum;
/*     */   }
/*     */   
/*     */   double forwardComplete(int j, int i, double emissionScore) {
/* 390 */     double sum = 0.0D;
/* 391 */     int adv = this.adv[j];
/* 392 */     double[] score = new double[this.modelLength];
/* 393 */     for (int j1 = 0; j1 < this.modelLength; j1++) {
/* 394 */       AbstractTerm term = this.forwardTrace.getTrace(j1, i - adv);
/* 395 */       if (term == null) score[j1] = 0.0D; else {
/* 396 */         score[j1] = (this.hmm.getTransitionScore(j1, j, i) * term.score() * emissionScore);
/*     */       }
/*     */       
/* 399 */       sum += score[j1];
/*     */     }
/* 401 */     AbstractTerm newTerm = new ComplexTerm(score, i - adv, sum);
/*     */     
/* 403 */     this.forwardTrace.setTrace(Integer.valueOf(j), i, newTerm);
/* 404 */     return sum;
/*     */   }
/*     */   
/*     */   private State getBestEmission(int i) {
/* 408 */     State max_j = null;
/* 409 */     double max = Double.NEGATIVE_INFINITY;
/* 410 */     for (Iterator<State> it = this.hmm.states(); it.hasNext();) {
/* 411 */       State j = (State)it.next();
/* 412 */       if ((j instanceof EmissionState)) {
/* 413 */         double sc = this.emissions[j.getIndex()][i];
/* 414 */         if (sc > max) {
/* 415 */           max_j = j;
/* 416 */           max = sc;
/*     */         }
/*     */       }
/*     */     }
/* 420 */     return max_j;
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
/*     */   int objIndex;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   final int[] stateIndex;
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
/* 452 */     double scoreForward = Math.log(this.forwardTrace.overall) - this.forwardTrace.getLogScale(this.seqLength - 1);
/* 453 */     double scoreBackward = Math.log(this.backwardTrace.overall) - this.backwardTrace.getLogScale(0);
/* 454 */     if ((Double.isNaN(scoreForward)) || (Double.isNaN(scoreBackward))) throw new RuntimeException("is nan " + this.forwardTrace.overall + " " + this.forwardTrace.getLogScale(this.seqLength - 1) + " " + this.backwardTrace.overall + " " + this.backwardTrace.getLogScale(0));
/* 455 */     return new double[] { scoreForward, scoreBackward };
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
/*     */   public StateDistribution getPosteriorML(int i, StateDistribution id)
/*     */   {
/* 470 */     for (int j = 1; j < this.hmm.modelLength(); j++) {
/* 471 */       if (this.emiss[j] != 0)
/*     */       {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 478 */         id.put(j, this.emissions[j][i]);
/*     */       }
/*     */     }
/*     */     
/*     */ 
/*     */ 
/* 484 */     id.normalise();
/* 485 */     return id;
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
/*     */   public StateDistribution getPosterior(int i, StateDistribution id)
/*     */   {
/* 498 */     double scale_i = Math.exp(-this.forwardTrace.getLogScale(i) - this.backwardTrace.getLogScale(i) + this.forwardTrace.getLogScale(this.seqLength - 1));
/* 499 */     for (int j = 1; j < this.hmm.modelLength(); j++) {
/* 500 */       if (this.emiss[j] != 0) {
/* 501 */         AbstractTerm forward = this.forwardTrace.getTrace(j, i);
/* 502 */         AbstractTerm backward = this.backwardTrace.getTrace(j, i);
/* 503 */         if ((forward != null) && (backward != null))
/*     */         {
/* 505 */           double unscaled = forward.score() * backward.score() / this.forwardTrace.overall;
/* 506 */           if (unscaled > 0.0D) { id.put(j, scale_i * unscaled);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     
/*     */ 
/* 513 */     return id;
/*     */   }
/*     */   
/*     */   public double[][] getPosteriorMatch()
/*     */   {
/* 518 */     double[][] posterior = new double[this.modelLength][this.seqLength];
/* 519 */     fillPosteriorProbabilities(posterior);
/* 520 */     return posterior;
/*     */   }
/*     */   
/*     */   public double[] getPosteriorMatchSum(State[] states) {
/* 524 */     double[][] d = getPosteriorMatch();
/* 525 */     double[] d1 = new double[d[0].length];
/* 526 */     Arrays.fill(d1, 0.0D);
/* 527 */     for (int j = 0; j < states.length; j++) {
/* 528 */       for (int i = 0; i < d1.length; i++) {
/* 529 */         d1[i] += d[states[j].getIndex()][i];
/*     */       }
/*     */     }
/* 532 */     return d1;
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
/*     */   public StatePath getStatePath(boolean sample)
/*     */   {
/* 559 */     StatePath path = new StatePath(this.protName, this.forwardTrace.overall);
/* 560 */     getStatePath(path, new StoppingCriterion() {
/*     */       public boolean stop(Object emission) {
/* 562 */         return false;
/*     */       }
/* 564 */     }, sample);
/* 565 */     return path;
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
/*     */   final int[] adv;
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
/*     */   Double logprob;
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public int getStatePath(StatePath path, StoppingCriterion stop, boolean sample)
/*     */   {
/* 604 */     State firstState = path.getFirstState();
/* 605 */     int j = firstState == null ? 0 : firstState.getIndex();
/* 606 */     Integer st = path.getFirstEmissionPos();
/* 607 */     int i = st == null ? this.seqLength - 1 : st.intValue();
/*     */     
/*     */ 
/* 610 */     AbstractTerm tr = this.forwardTrace.getTrace(j, i);
/* 611 */     if (tr == null) return i;
/* 612 */     i = tr.i;
/* 613 */     j = tr.getBestPath();
/*     */     label258:
/* 615 */     while (j != 0) {
/* 616 */       boolean emiss = this.emiss[j];
/*     */       try
/*     */       {
/* 619 */         Object obj_i = null;
/* 620 */         if (emiss) {
/* 621 */           EmissionState state_j = (EmissionState)this.hmm.getState(j);
/*     */           
/* 623 */           obj_i = 
/*     */           
/* 625 */             Integer.valueOf(this.obj.getBestIndex(state_j, i, sample));
/*     */         }
/*     */         
/*     */ 
/*     */ 
/*     */ 
/* 631 */         path.add(this.hmm.getState(j), obj_i, i);
/* 632 */         if (!stop.stop(obj_i)) break label258; return i;
/*     */       } catch (Exception exc) {
/* 634 */         exc.printStackTrace();
/* 635 */         logger.info(i);
/* 636 */         logger.info(j);
/* 637 */         logger.info(path);
/*     */         
/* 639 */         System.exit(0);
/* 640 */         path.add(this.hmm.getState(j), null, i);
/*     */       }
/*     */       
/* 643 */       tr = this.forwardTrace.getTrace(j, i);
/* 644 */       i = tr.i;
/* 645 */       j = tr.getBestPath();
/*     */     }
/*     */     
/* 648 */     return i;
/*     */   }
/*     */   
/*     */ 
/*     */   public void addTransitionPosterior(int i, StateDistribution[] transitionPosterior, double weight)
/*     */   {
/* 654 */     double scale_i = Math.exp(this.forwardTrace.getLogScale(this.seqLength - 1) - this.forwardTrace.getLogScale(i) - this.backwardTrace.getLogScale(i + 1));
/* 655 */     for (int k = 0; k < this.modelLength; k++) {
/* 656 */       StateDistribution d1 = transitionPosterior[k];
/*     */       
/* 658 */       for (int j = 0; j < this.modelLength; j++) {
/* 659 */         double sc = getTransitionProb(k, j, i) * scale_i * weight;
/* 660 */         if (sc > 0.0D) d1.add(j, sc);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   private double getTransitionProb(int k, int l, int i)
/*     */   {
/* 667 */     int adv = this.adv[l];
/* 668 */     if (i + adv < 0) return 0.0D;
/* 669 */     double f_ki = this.forwardTrace.getScore(k, i, false);
/* 670 */     double b_li = this.backwardTrace.getScore(l, i + adv, false);
/* 671 */     double p_x = this.forwardTrace.overall;
/* 672 */     double e_li = 
/* 673 */       this.emiss[l] != 0 ? 
/* 674 */       this.emissions[l][(i + adv)] : i + adv == this.seqLength ? 0.0D : 1.0D;
/* 675 */     return f_ki * e_li * b_li / p_x;
/*     */   }
/*     */   
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   public double searchML()
/*     */   {
/* 684 */     double sc = 0.0D;
/* 685 */     double adj = this.emiss.length;
/* 686 */     for (int i = 0; i < this.seqLength; i++) {
/* 687 */       double sc_i = 0.0D;
/* 688 */       for (int j = 0; j < this.emiss.length; j++) {
/* 689 */         if (this.emiss[j] != 0) {
/* 690 */           sc_i += this.emissions[j][i] * adj;
/*     */         }
/*     */       }
/* 693 */       sc += Math.log(sc_i);
/*     */     }
/* 695 */     return sc;
/*     */   }
/*     */   
/*     */   public double search(boolean incBackward)
/*     */   {
/* 700 */     calcScoresForward(false);
/* 701 */     if (incBackward) calcScoresBackward();
/* 702 */     double[] sc = getOverallScore();
/*     */     
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 712 */     this.logprob = Double.valueOf(sc[0]);
/* 713 */     return sc[0];
/*     */   }
/*     */   
/* 716 */   public double searchViterbi() { return calcScoresViterbi(); }
/*     */   
/*     */ 
/*     */   public double validate(StateDistribution dist, double sum)
/*     */   {
/* 721 */     double sum1 = dist.sum();
/* 722 */     if (Math.abs(sum - sum1) > 0.01D) throw new RuntimeException("sum is not right " + sum + " " + sum1);
/* 723 */     return sum1;
/*     */   }
/*     */   
/*     */   double viterbi(int j, int i, double emiss) {
/* 727 */     int max_j = -1;
/* 728 */     int max_i = 0;
/* 729 */     double max = Double.NEGATIVE_INFINITY;
/* 730 */     int[] toStates = this.hmm.statesIn(j, i);
/* 731 */     for (int j3 = 0; j3 < toStates.length; j3++) {
/* 732 */       int j2 = toStates[j3];
/*     */       
/* 734 */       int adv = this.adv[j];
/* 735 */       AbstractTerm term = this.forwardTrace.getTrace(j2, i - adv);
/* 736 */       if (term != null) {
/* 737 */         double score = Math.log(this.hmm.getTransitionScore(j2, j, i)) + 
/* 738 */           term.score();
/*     */         
/*     */ 
/*     */ 
/* 742 */         if (score > max) {
/* 743 */           max = score;
/* 744 */           max_j = j2;
/* 745 */           max_i = i - adv;
/*     */         }
/*     */       } }
/* 748 */     double result = max;
/*     */     
/*     */ 
/* 751 */     AbstractTerm newT = new Term(max_j, max_i, result + emiss);
/*     */     
/* 753 */     this.forwardTrace.setTrace(Integer.valueOf(j), i, newT);
/* 754 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/lachlan/Work/polyHap2/polyHap2.jar!/lc1/dp/core/DP.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */